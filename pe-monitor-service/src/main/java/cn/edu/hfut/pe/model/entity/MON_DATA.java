package cn.edu.hfut.pe.model.entity;

public class MON_DATA {
	private long DEVICE_ID;// 被管理对象id bigint FALSE TRUE
	private long METRIC_CONFIG_ID;// bigint FALSE TRUE
	private long COLL_TIME;// 采集时间 datetime FALSE FALSE
	private Number COLL_VALUE;// 采集值 bigint FALSE FALSE

	public MON_DATA() {
	}

	public MON_DATA(long dEVICE_ID, long mETRIC_CONFIG_ID, long cOLL_TIME, Number cOLL_VALUE) {
		DEVICE_ID = dEVICE_ID;
		METRIC_CONFIG_ID = mETRIC_CONFIG_ID;
		COLL_TIME = cOLL_TIME;
		COLL_VALUE = cOLL_VALUE;
	}

	public long getDEVICE_ID() {
		return DEVICE_ID;
	}

	public void setDEVICE_ID(long dEVICE_ID) {
		DEVICE_ID = dEVICE_ID;
	}

	public long getMETRIC_CONFIG_ID() {
		return METRIC_CONFIG_ID;
	}

	public void setMETRIC_CONFIG_ID(long mETRIC_CONFIG_ID) {
		METRIC_CONFIG_ID = mETRIC_CONFIG_ID;
	}

	public long getCOLL_TIME() {
		return COLL_TIME;
	}

	public void setCOLL_TIME(long cOLL_TIME) {
		COLL_TIME = cOLL_TIME;
	}

	public Number getCOLL_VALUE() {
		return COLL_VALUE;
	}

	public void setCOLL_VALUE(Number cOLL_VALUE) {
		COLL_VALUE = cOLL_VALUE;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MON_DATA [DEVICE_ID=");
		builder.append(DEVICE_ID);
		builder.append(", METRIC_CONFIG_ID=");
		builder.append(METRIC_CONFIG_ID);
		builder.append(", COLL_TIME=");
		builder.append(COLL_TIME);
		builder.append(", COLL_VALUE=");
		builder.append(COLL_VALUE);
		builder.append("]");
		return builder.toString();
	}

}
