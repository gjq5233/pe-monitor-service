package cn.edu.hfut.pe.model.entity.config;

/**
 * 数据采集单元
 * 
 * @author 86189
 *
 */
public class MON_DCU {

	private long DCU_ID;// 电力采集单元唯一性标示 bigint TRUE FALSE
	private volatile long DCU_IMEI;// 国际移动设备识别码 bigint FALSE FALSE
	private String DCU_NAME;// dcu名称 varchar(64) FALSE FALSE
	private String DCU_ADDR;// dcu地址，地图位置 varchar(128) FALSE FALSE
	private volatile long DCU_LAC;// "Location Area Code 位置区域码 0~0xFFFF" bigint FALSE FALSE
	private volatile long DCU_CI;// "Cell Identity 基站编号 GSM网络：0~0x0000FFFF LTE网络： 0~0x0FFFFFFF" bigint FALSE
									// FALSE
	private volatile long time = System.currentTimeMillis();

	public MON_DCU() {

	}

	public MON_DCU(long dCU_ID, long dCU_IMEI, String dCU_NAME, String dCU_ADDR, long dCU_LAC, long dCU_CI) {
		DCU_ID = dCU_ID;
		DCU_IMEI = dCU_IMEI;
		DCU_NAME = dCU_NAME;
		DCU_ADDR = dCU_ADDR;
		DCU_LAC = dCU_LAC;
		DCU_CI = dCU_CI;
	}

	public long getDCU_ID() {
		return DCU_ID;
	}

	public void setDCU_ID(long dCU_ID) {
		DCU_ID = dCU_ID;
	}

	public long getDCU_IMEI() {
		return DCU_IMEI;
	}

	public void setDCU_IMEI(long dCU_IMEI) {
		DCU_IMEI = dCU_IMEI;
	}

	public String getDCU_NAME() {
		return DCU_NAME;
	}

	public void setDCU_NAME(String dCU_NAME) {
		DCU_NAME = dCU_NAME;
	}

	public String getDCU_ADDR() {
		return DCU_ADDR;
	}

	public void setDCU_ADDR(String dCU_ADDR) {
		DCU_ADDR = dCU_ADDR;
	}

	public long getDCU_LAC() {
		return DCU_LAC;
	}

	public void setDCU_LAC(long dCU_LAC) {
		DCU_LAC = dCU_LAC;
	}

	public long getDCU_CI() {
		return DCU_CI;
	}

	public void setDCU_CI(long dCU_CI) {
		DCU_CI = dCU_CI;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MON_DCU [DCU_ID=");
		builder.append(DCU_ID);
		builder.append(", DCU_IMEI=");
		builder.append(DCU_IMEI);
		builder.append(", DCU_NAME=");
		builder.append(DCU_NAME);
		builder.append(", DCU_ADDR=");
		builder.append(DCU_ADDR);
		builder.append(", DCU_LAC=");
		builder.append(DCU_LAC);
		builder.append(", DCU_CI=");
		builder.append(DCU_CI);
		builder.append("]");
		return builder.toString();
	}

}
