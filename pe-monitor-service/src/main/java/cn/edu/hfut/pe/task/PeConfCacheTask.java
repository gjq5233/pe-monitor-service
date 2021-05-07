package cn.edu.hfut.pe.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.edu.hfut.pe.cache.ConfCache;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;
import cn.edu.hfut.pe.service.IpeConfService;

@Component
@EnableScheduling
public class PeConfCacheTask implements Runnable {

	private final static Logger LOG = LoggerFactory.getLogger(PeConfCacheTask.class);

	@Autowired
	private IpeConfService peConfService;

	@Override
	@Scheduled(initialDelay = 0, fixedDelayString = "${task.cycle.conf.load:30000}")
	public void run() {

		cacheStaticData();
		mapCache();
		LOG.info("StaticDataCacheInit finished");
	}

	private void mapCache() {
	}

	/**
	 * 缓存配置信息
	 *
	 */
	public void cacheStaticData() {
		List<MON_DCU> dcuList = peConfService.getMON_DCUlist();
		if (CollectionUtils.isEmpty(dcuList)) {
			LOG.warn("MON_DCU table get data failed");
			return;
		}
		ConfCache.setDcuConf(dcuList);

		List<MON_DEVICE> moList = peConfService.getMON_DEVICElist();
		if (CollectionUtils.isEmpty(moList)) {
			LOG.warn("MON_DEVICE table get data failed");
			return;
		}
		ConfCache.setDeviceConf(moList);

		List<MON_METRIC_CONFIG> graphList = peConfService.getMON_METRIC_CONFIGlist();
		if (CollectionUtils.isEmpty(graphList)) {
			LOG.warn("MON_METRIC_CONFIG table get data failed");
			return;
		}
		ConfCache.setMetricConf(graphList);

		LOG.info("pe conf data loading completed");
		System.out.println("pe conf data loading completed");
	}

	public void setPeConfService(IpeConfService peConfService) {
		this.peConfService = peConfService;
	}

	public static void main(String[] args) {

	}
}