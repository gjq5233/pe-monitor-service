package cn.edu.hfut.pe.task;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import base.util.netty.ChannelUtil;
import cn.edu.hfut.pe.cache.ConfCache;
import cn.edu.hfut.pe.cache.RunTimeCache;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;
import cn.edu.hfut.pe.service.IpeService;
import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;

/**
 * 执行频率由xml配置， 采集频率由数据库指标配置
 * 
 * 采集 modbus等由中心配置的数据
 * 
 * @author 86189
 *
 */
@Component
@EnableScheduling
public class PeCollectTask extends Thread {
	private final static Logger LOG = LoggerFactory.getLogger(PeCollectTask.class);

	@Autowired
	private IpeService pe2Service;

	@Override
	@Scheduled(initialDelay = 20000, fixedDelayString = "${task.cycle.collect:5000}")
	public void run() {
		LOG.debug("{} exe", this.getClass().getSimpleName());
		dealMoList();
	}

	public void dealMoList() {
		List<MON_DEVICE> deviceList = ConfCache.getDeviceList();
		if (CollectionUtils.isEmpty(deviceList)) {
			return;
		}

		/**
		 * 1、处理所有监控对象
		 */
		for (MON_DEVICE device : deviceList) {
			Channel ch = RunTimeCache.getChannelByDcuid(device);
			if (!ChannelUtil.isActive(ch)) {// 如果此设备未登录，则跳过处理
				LOG.info("device no session, device:{}", device);
				continue;
			}
			/**
			 * 2、获取此对象的所有指标监视器
			 */
			List<MON_METRIC_CONFIG> moGraphList = ConfCache.getGraphListByTemplateid(device.getTEMPLATE_ID());
			if (CollectionUtils.isEmpty(moGraphList)) {
				LOG.info("device metric config is empty, device:{}", device);
				continue;
			}
			dealDevice(device, moGraphList);
		}
	}

	private void dealDevice(MON_DEVICE device, List<MON_METRIC_CONFIG> moGraphList) {

		/**
		 * 3、PARENT_GRAPH_ID为0，表示从dcu获取数据，不为0的从父结果取
		 */
		List<MON_METRIC_CONFIG> commGraphList = moGraphList.stream().filter(graph -> graph.getPARENT_GRAPH_ID() == 0)
				.collect(Collectors.toList());

		/**
		 * 4、处理单个设备的单个监视器
		 */
		for (MON_METRIC_CONFIG graph : commGraphList) {
			Long deviceId = device.getDEVICE_ID();
			Long graphid = graph.getMETRIC_CONFIG_ID();
			if (!ConfCache.addInProcess(deviceId, graphid)) {//已经在schedule处理中，跳过
				continue;// 已经在处理
			}
			dealGraph(deviceId, graphid);
		}
	}

	/**
	 * 1、根据deviceId graphid找到执行器
	 * 
	 * 2、执行器执行 监控对象+指标监视器的处理
	 * 
	 * 3、延时监视器中指定的时间，循环调用
	 * 
	 * @param deviceId
	 * @param graphid
	 */
	private void dealGraph(Long deviceId, Long graphid) {
		MON_DEVICE device = ConfCache.getDevice(deviceId);
		if (device == null) {
			ConfCache.removeInProcess(deviceId, graphid);
			LOG.info("deviceid:{} is not exist, deleted?", deviceId);
			return;
		}
		MON_METRIC_CONFIG graph = ConfCache.getMetricConf(graphid);
		if (graph == null) {
			ConfCache.removeInProcess(deviceId, graphid);
			LOG.info("graphid:{} is not exist, deleted?", graphid);
			return;
		}

		Channel ch = RunTimeCache.getChannelByDcuid(device);
		if (!ChannelUtil.isActive(ch)) {// 如果此设备未登录，则跳过处理
			ConfCache.removeInProcess(deviceId, graphid);
			LOG.debug("device:{} is not logined", device);
			return;
		}

		EventExecutor exe = ch.eventLoop();
		if (exe == null) {
			LOG.debug("device:{} eventloop is null", device);
			ConfCache.removeInProcess(deviceId, graphid);
			return;
		}

		ConfCache.updateInProcess(deviceId, graphid);// 更新处理时间，表示在处理中

		int collCycleInSec = graph.getCOLLECTION_CYCLE();
		exe.schedule(() -> dealGraph(deviceId, graphid), collCycleInSec, TimeUnit.SECONDS);// 下一次处理
		exe.execute(() -> pe2Service.dealMoGraph(ch, device, graph));// 当前处理
	}

	public void setPe2Service(IpeService pe2Service) {
		this.pe2Service = pe2Service;
	}

	public static void main(String[] args) {
		String metric = "aaabbb_status";
		if (metric.endsWith("_status")) {
			metric = metric.substring(0, metric.length() - 7);
		}
		System.out.println(metric);

		String s = "1.1.1.123:20";
		int index = s.indexOf(":");
		System.out.println(s.substring(0, index));
	}
}
