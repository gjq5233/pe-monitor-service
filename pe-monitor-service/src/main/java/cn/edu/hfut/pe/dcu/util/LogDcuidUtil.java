package cn.edu.hfut.pe.dcu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.pe.dcu.cons.DcuAttrCons;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import io.netty.channel.Channel;

public class LogDcuidUtil {
	private final static Logger LOGG = LoggerFactory.getLogger(LogDcuidUtil.class);

	private static Long confDcuid;// 配置中配置打印日志

	public static boolean trace = true; //测试时开启， false时log里面的表达式不会计算, 部署时设置为false；

	public static boolean log = true; 

	public final static boolean isLogDcu(Long ducid) {
		if (!log) {
			return false;
		}
		if (confDcuid == null || ducid == null) {
			return false;
		}
		if (ducid.longValue() != confDcuid.longValue()) {
			return false;
		}
		return true;
	}

	public final static void setConfDcuid(Long confDcuid) {
		if (!log) {
			return;
		}
		LogDcuidUtil.confDcuid = confDcuid;
		LOGG.info("===confDcuid:{}===", confDcuid);
	}

	public final static void log(Logger LOG, String msg, Object... para) {
		if (!log) {
			return;
		}
		LOG.warn("===" + msg + "===", para);
	}

	public final static void log(Long dcuid, Logger LOG, String msg, Object... para) {
		if (!log) {
			return;
		}
		if (isLogDcu(dcuid)) {
			log(LOG, msg, para);
		}
	}

	public final static void log(Channel ch, Logger LOG, String msg, Object... para) {
		if (!log) {
			return;
		}
		if (ch == null) {
			return;
		}
		MON_DCU dcu = ch.attr(DcuAttrCons.DCU).get();
		Long dcuid = null;
		if(dcu != null) {
			dcuid = dcu.getDCU_ID();
		}
		log(dcuid, LOG, msg, para);
	}

	public static void main(String[] args) {
		log(LOGG, "{} {} {} {}", 1, 2, 3, 4, 5);
	}
}
