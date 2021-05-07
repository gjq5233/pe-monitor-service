package cn.edu.hfut.pe.util;

import java.util.concurrent.atomic.AtomicInteger;

import cn.edu.hfut.pe.dcu.comm.msg.Msg;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;

public class SeqUtil {
	private final static String SPLIT_CHAR = "@";

	public static final String genKey(long dcuid, int deviceUnitId, byte func, int seq) {
		return dcuid + SPLIT_CHAR + deviceUnitId + SPLIT_CHAR + func + SPLIT_CHAR + seq;
	}
	
	public static final  String genKey(MON_DEVICE device, Msg req) {
		return genKey(device.getDCU_ID(), device.getDEVICE_UNIT_IDENTFIER(), req.getFunc(), req.getSeq());
	}
	
	/**
	 * 4字节流水号生成
	 * 
	 * web下发指令，使用数据库序列作为序列号
	 */
	private static final AtomicInteger inc4 = new AtomicInteger(1);

	public static final int genSeq4() {
		return inc4.incrementAndGet();
	}
	
	public static final int genDataSeq() {
		return inc4.incrementAndGet() & 0x7FFFFFFF;
	}
}
