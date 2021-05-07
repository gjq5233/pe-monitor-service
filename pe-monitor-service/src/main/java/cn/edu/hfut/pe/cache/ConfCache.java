package cn.edu.hfut.pe.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import cn.edu.hfut.pe.model.DeviceAddr;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;

public class ConfCache {
	
	
	/**
	 * 设备类型+metric指标+tags
	 */
	private static volatile List<MON_DCU> LIST_MON_DCU = new ArrayList<>();

	/**
	 * 设备表<dcuid, MON_DCU>
	 */
	public static volatile Map<Long, MON_DCU> MAP_ID_DCU = new HashMap<>();

	public synchronized final static void setDcuConf(List<MON_DCU> dcuList) {
		if (dcuList == null || CollectionUtils.isEmpty(dcuList)) {
			return;
		}
		LIST_MON_DCU = dcuList;
		MAP_ID_DCU = dcuList.stream().collect(Collectors.toMap(MON_DCU::getDCU_ID, v -> v));
	}
	
	public synchronized final static List<MON_DCU> getDcuList() {
		return LIST_MON_DCU;
	}
	
	public synchronized final static MON_DCU getDcu(Long dcuId) {
		return MAP_ID_DCU.get(dcuId);
	}
	

	/**
	 * 设备类型+metric指标+tags
	 */
	private static volatile List<MON_DEVICE> LIST_MON_DEVICE = new ArrayList<>();

	/**
	 * 设备表<device_id, MON_DEVICE>
	 */
	public static volatile Map<DeviceAddr, MON_DEVICE> MAP_DCUADDR_DEVICE = new HashMap<>();

	/**
	 * 设备表<device_id, MON_DEVICE>
	 */
	public static volatile Map<Long, MON_DEVICE> MAP_ID_DEVICE = new HashMap<>();

	public synchronized final static void setDeviceConf(List<MON_DEVICE> deviceList) {
		if (deviceList == null || CollectionUtils.isEmpty(deviceList)) {
			return;
		}
		LIST_MON_DEVICE = deviceList;
		MAP_DCUADDR_DEVICE = deviceList.stream().collect(Collectors
				.toMap(device -> new DeviceAddr(device.getDEVICE_ID(), device.getDEVICE_UNIT_IDENTFIER()), v -> v));
		MAP_ID_DEVICE = deviceList.stream().collect(Collectors.toMap(MON_DEVICE::getDEVICE_ID, v -> v));
	}

	

	public synchronized final static List<MON_DEVICE> getDeviceList() {
		return LIST_MON_DEVICE;
	}
	
	public synchronized final static MON_DEVICE getDevice(DeviceAddr addr) {
		return MAP_DCUADDR_DEVICE.get(addr);
	}
	public synchronized final static MON_DEVICE getDevice(Long deviceId) {
		return MAP_ID_DEVICE.get(deviceId);
	}


	public synchronized final static boolean isEmptyDevice() {
		return CollectionUtils.isEmpty(LIST_MON_DEVICE);
	}

	
	

	/**
	 * <graphid, 监视器指标>
	 */
	public volatile static Map<Long, MON_METRIC_CONFIG> MAP_ID_METRIC_CONFIG = new HashMap<>();

	public volatile static Map<Long, List<MON_METRIC_CONFIG>> MAP_TEMPLATE_ID_METRIC_CONFIGLIST = new HashMap<>();

	public volatile static Map<Long, List<MON_METRIC_CONFIG>> MAP_PARENTID_METRIC_CONFIGLIST = new HashMap<>();

	public synchronized final static void setMetricConf(List<MON_METRIC_CONFIG> metricConfList) {
		if (metricConfList == null || CollectionUtils.isEmpty(metricConfList)) {
			return;
		}
		MAP_ID_METRIC_CONFIG = metricConfList.stream()
				.collect(Collectors.toMap(MON_METRIC_CONFIG::getMETRIC_CONFIG_ID, v -> v));

		MAP_TEMPLATE_ID_METRIC_CONFIGLIST = metricConfList.stream()
				.collect(Collectors.groupingBy(MON_METRIC_CONFIG::getTEMPLATE_ID));

		MAP_PARENTID_METRIC_CONFIGLIST = metricConfList.stream()
				.collect(Collectors.groupingBy(MON_METRIC_CONFIG::getPARENT_GRAPH_ID));

	}

	public synchronized final static MON_METRIC_CONFIG getMetricConf(Long metricConfId) {
		MON_METRIC_CONFIG graph = MAP_ID_METRIC_CONFIG.get(metricConfId);
		return graph;
	}
	
	public synchronized final static MON_METRIC_CONFIG getMetricConfAndSub(Long metricConfId) {
		MON_METRIC_CONFIG graph = MAP_ID_METRIC_CONFIG.get(metricConfId);
		graph.setSubConfList(MAP_PARENTID_METRIC_CONFIGLIST.get(metricConfId));
		return graph;
	}

	public synchronized final static List<MON_METRIC_CONFIG> getGraphListByTemplateid(Long templateid) {
		return MAP_TEMPLATE_ID_METRIC_CONFIGLIST.get(templateid);
	}
	
	public synchronized final static List<MON_METRIC_CONFIG> getGraphListByDeviceid(Long templateid) {
		return MAP_TEMPLATE_ID_METRIC_CONFIGLIST.get(templateid);
	}
	
	public synchronized final static List<MON_METRIC_CONFIG> getGraphList(MON_DEVICE device) {
		return MAP_TEMPLATE_ID_METRIC_CONFIGLIST.get(device.getTEMPLATE_ID());
	}

	public synchronized final static List<MON_METRIC_CONFIG> getGraphListByParentid(Long parentid) {
		return MAP_PARENTID_METRIC_CONFIGLIST.get(parentid);
	}

	/**
	 * <deviceid+@+metricConfigId, time>
	 */
	public final static Map<String, Long> MAP_MOID_METRIC_CONFIG_ID_TIME = new HashMap<>();

	
	
	private final static long DEADSTATE_TIMEINMILLS = 1000L * 60 * 30;//30分钟 可设置

	/**
	 * 是否在处理线程中，不在则加入
	 * 
	 * @param moid
	 * @param graphid
	 * @return true表示加入成功，false表示已经在处理
	 */
	public synchronized final static boolean addInProcess(Long moid, Long graphid) {
		String key = moid + "@" + graphid;
		Long lastTime = MAP_MOID_METRIC_CONFIG_ID_TIME.get(key);
		long curTime = System.currentTimeMillis();
		if (lastTime == null || curTime > lastTime + DEADSTATE_TIMEINMILLS) {// null或者超时，处理线程意外死了？
			MAP_MOID_METRIC_CONFIG_ID_TIME.put(key, curTime);
			return true;
		}
		return false;
	}

	public synchronized final static void updateInProcess(Long moid, Long graphid) {
		MAP_MOID_METRIC_CONFIG_ID_TIME.put(moid + "@" + graphid, System.currentTimeMillis());
	}

	public synchronized final static boolean removeInProcess(Long moid, Long graphid) {
		MAP_MOID_METRIC_CONFIG_ID_TIME.remove(moid + "@" + graphid);
		return true;
	}
}
