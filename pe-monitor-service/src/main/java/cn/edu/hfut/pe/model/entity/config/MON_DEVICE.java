package cn.edu.hfut.pe.model.entity.config;

import cn.edu.hfut.pe.model.DeviceAddr;

/**
 * 电力设备表.如：配电设备(与设备管理单元一一对应， 如果是一对多需要加设备地址)
 * 
 * 主要目的：采集他的指标，控制，调整参数，告警，时间
 * 
 * 主要步骤：加设备的时候，通过相关字段选择模板，复制监视器(采集表达式、阈值、冗余字段)到最终item表
 * 
 * @author 86189
 *
 */
public class MON_DEVICE extends DeviceAddr {

	private long DEVICE_ID;// 被管理对象id bigint TRUE FALSE
	private String DEVICE_NAME;// 被管理对象名称 varchar(64) FALSE FALSE
	private long TEMPLATE_ID;// 模板id, 此对象最终关联到设备的采集指标和采集方式等信息 bigint FALSE TRUE

	public MON_DEVICE() {

	}

	public MON_DEVICE(long dEVICE_ID, long dCU_ID, int dEVICE_UNIT_IDENTFIER, String dEVICE_NAME, long tEMPLATE_ID) {
		DEVICE_ID = dEVICE_ID;
		DCU_ID = dCU_ID;
		DEVICE_UNIT_IDENTFIER = dEVICE_UNIT_IDENTFIER;
		DEVICE_NAME = dEVICE_NAME;
		TEMPLATE_ID = tEMPLATE_ID;
	}

	public long getDEVICE_ID() {
		return DEVICE_ID;
	}

	public void setDEVICE_ID(long dEVICE_ID) {
		DEVICE_ID = dEVICE_ID;
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

	public String getDEVICE_NAME() {
		return DEVICE_NAME;
	}

	public void setDEVICE_NAME(String dEVICE_NAME) {
		DEVICE_NAME = dEVICE_NAME;
	}

	public long getTEMPLATE_ID() {
		return TEMPLATE_ID;
	}

	public void setTEMPLATE_ID(long tEMPLATE_ID) {
		TEMPLATE_ID = tEMPLATE_ID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MON_DEVICE [DEVICE_ID=");
		builder.append(DEVICE_ID);
		builder.append(", DCU_ID=");
		builder.append(DCU_ID);
		builder.append(", DEVICE_UNIT_IDENTFIER=");
		builder.append(DEVICE_UNIT_IDENTFIER);
		builder.append(", DEVICE_NAME=");
		builder.append(DEVICE_NAME);
		builder.append(", TEMPLATE_ID=");
		builder.append(TEMPLATE_ID);
		builder.append("]");
		return builder.toString();
	}

}
