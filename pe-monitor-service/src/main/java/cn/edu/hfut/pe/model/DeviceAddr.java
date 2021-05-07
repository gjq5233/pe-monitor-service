package cn.edu.hfut.pe.model;

public class DeviceAddr {
	protected long DCU_ID;// 电力采集单元唯一性标示 bigint FALSE TRUE
	protected int DEVICE_UNIT_IDENTFIER;// 设备在DCU中的地址 1-127 tinyint FALSE FALSE

	public DeviceAddr() {
	}

	public DeviceAddr(long dCU_ID, int dEVICE_UNIT_IDENTFIER) {
		DCU_ID = dCU_ID;
		DEVICE_UNIT_IDENTFIER = dEVICE_UNIT_IDENTFIER;
	}

	public long getDCU_ID() {
		return DCU_ID;
	}

	public void setDCU_ID(long dCU_ID) {
		DCU_ID = dCU_ID;
	}

	public int getDEVICE_UNIT_IDENTFIER() {
		return DEVICE_UNIT_IDENTFIER;
	}

	public void setDEVICE_UNIT_IDENTFIER(int dEVICE_UNIT_IDENTFIER) {
		DEVICE_UNIT_IDENTFIER = dEVICE_UNIT_IDENTFIER;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (DCU_ID ^ (DCU_ID >>> 32));
		result = prime * result + DEVICE_UNIT_IDENTFIER;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceAddr other = (DeviceAddr) obj;
		if (DCU_ID != other.DCU_ID)
			return false;
		if (DEVICE_UNIT_IDENTFIER != other.DEVICE_UNIT_IDENTFIER)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeviceAddr [DCU_ID=");
		builder.append(DCU_ID);
		builder.append(", DEVICE_UNIT_IDENTFIER=");
		builder.append(DEVICE_UNIT_IDENTFIER);
		builder.append("]");
		return builder.toString();
	}

}
