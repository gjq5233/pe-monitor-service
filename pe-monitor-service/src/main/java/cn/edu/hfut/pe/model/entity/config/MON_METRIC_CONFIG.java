package cn.edu.hfut.pe.model.entity.config;

import java.util.List;

import com.google.common.math.DoubleMath;

import cn.edu.hfut.pe.model.ModbusValueExps;

/**
 * 指标配置表,从属于模板
 * 
 * 一条记录包括指标名称(英文点分)、指标中文名称，指标描述，采集表达式，业务种类(测量值、状态值)，
 * 
 * 数据类型(int32 str float)， 倍数(采集表达式获取的值需要乘以此数)， 单位，阈值
 * 
 * @author 86189
 *
 */
public class MON_METRIC_CONFIG {
	private long METRIC_CONFIG_ID;// bigint TRUE FALSE
	private long TEMPLATE_ID;// 附属于哪个模板，模板删除，此记录也关联删除 bigint FALSE TRUE
	private String METRIC_CONFIG_NAME;// 监视器名称。监视器和协议，采集手段等相关 VARCHAR(64) FALSE FALSE
	private String METRIC_NAME;// 指标名称.类比opentsdb的metric;或者influxdb的measurement。如line.voltage.uab表示线电压Uab。不同的设备可能都有相同的指标
								// varchar(64) FALSE FALSE
	private int METRIC_BUSI_TYPE;// 指标业务类型 1、非枚举 4、枚举 枚举不能做平均值 tinyint FALSE FALSE
	private int METRIC_VALUE_TYPE;// 值类型( 1:int32 2:int64 3:double 4:string 5:enum) tinyint FALSE FALSE
	private String METRIC_VALUE_UNIT;// 值单位(千伏安，米，%) VARCHAR(16) FALSE FALSE
	private String DEFAULT_V_NO_DATA;// 采集不到时给的默认值，默认-1.类型实际应该为FIELD_V_TYPE字段的兼容类型。可能用不到 -1 varchar(4) FALSE FALSE
	private String VALUE_EXPS;// 获取监控值的表达式 modbus-功能号:位置:modbus数据类型:[bit类型的位置/长度]。例：3:240:BYTES:96，3:241:INT16
								// varchar(64) FALSE FALSE
	private double MULTIPLIER;// 自定义倍数MULTIPLIER=采集值/实际值，默认为1。比如表达式数据为151的电压值，如果倍数为10，最终的数据应该是15.1 1 double
								// FALSE FALSE
	private long PARENT_GRAPH_ID;// 父监视器id，如果此值不为0，表示从父指标获取数据，主要是modbus批量获取用 0 bigint FALSE FALSE
	private int COLLECTION_CYCLE;// 采集周期，单位:秒 10-3600*24。 只对PARENT_GRAPH_ID为0的监视器有效 30 int FALSE FALSE

	private ModbusValueExps mve;

	List<MON_METRIC_CONFIG> subConfList;

	public MON_METRIC_CONFIG() {

	}

	public MON_METRIC_CONFIG(long mETRIC_CONFIG_ID, long tEMPLATE_ID, String mETRIC_CONFIG_NAME, String mETRIC_NAME,
			int mETRIC_BUSI_TYPE, int mETRIC_VALUE_TYPE, String mETRIC_VALUE_UNIT, String dEFAULT_V_NO_DATA,
			String vALUE_EXPS, double mULTIPLIER, long pARENT_GRAPH_ID, int cOLLECTION_CYCLE) {
		METRIC_CONFIG_ID = mETRIC_CONFIG_ID;
		TEMPLATE_ID = tEMPLATE_ID;
		METRIC_CONFIG_NAME = mETRIC_CONFIG_NAME;
		METRIC_NAME = mETRIC_NAME;
		METRIC_BUSI_TYPE = mETRIC_BUSI_TYPE;
		METRIC_VALUE_TYPE = mETRIC_VALUE_TYPE;
		METRIC_VALUE_UNIT = mETRIC_VALUE_UNIT;
		DEFAULT_V_NO_DATA = dEFAULT_V_NO_DATA;
		VALUE_EXPS = vALUE_EXPS;
		MULTIPLIER = mULTIPLIER;
		PARENT_GRAPH_ID = pARENT_GRAPH_ID;
		COLLECTION_CYCLE = cOLLECTION_CYCLE;
	}

	public Number getMULTIPLIERvalue(Number value) {
		if (DoubleMath.fuzzyEquals(1, MULTIPLIER, 0.0001)) {
			return value;
		}
		if (DoubleMath.fuzzyEquals(0, MULTIPLIER, 0.0000000000000001)) {// error
			return value;
		}

		// 1:int32 2:int64 3:double 4:string
		double vv = value.doubleValue() / MULTIPLIER;
		switch (METRIC_VALUE_TYPE) {
		case 1:
			return Double.valueOf(vv).intValue();
		case 2:
			return Double.valueOf(vv).longValue();
		case 3:
			return vv;
		default:
			break;
		}
		return value;
	}

	public long getMETRIC_CONFIG_ID() {
		return METRIC_CONFIG_ID;
	}

	public void setMETRIC_CONFIG_ID(long mETRIC_CONFIG_ID) {
		METRIC_CONFIG_ID = mETRIC_CONFIG_ID;
	}

	public long getTEMPLATE_ID() {
		return TEMPLATE_ID;
	}

	public void setTEMPLATE_ID(long tEMPLATE_ID) {
		TEMPLATE_ID = tEMPLATE_ID;
	}

	public String getMETRIC_CONFIG_NAME() {
		return METRIC_CONFIG_NAME;
	}

	public void setMETRIC_CONFIG_NAME(String mETRIC_CONFIG_NAME) {
		METRIC_CONFIG_NAME = mETRIC_CONFIG_NAME;
	}

	public String getMETRIC_NAME() {
		return METRIC_NAME;
	}

	public void setMETRIC_NAME(String mETRIC_NAME) {
		METRIC_NAME = mETRIC_NAME;
	}

	public int getMETRIC_BUSI_TYPE() {
		return METRIC_BUSI_TYPE;
	}

	public void setMETRIC_BUSI_TYPE(int mETRIC_BUSI_TYPE) {
		METRIC_BUSI_TYPE = mETRIC_BUSI_TYPE;
	}

	public int getMETRIC_VALUE_TYPE() {
		return METRIC_VALUE_TYPE;
	}

	public void setMETRIC_VALUE_TYPE(int mETRIC_VALUE_TYPE) {
		METRIC_VALUE_TYPE = mETRIC_VALUE_TYPE;
	}

	public String getMETRIC_VALUE_UNIT() {
		return METRIC_VALUE_UNIT;
	}

	public void setMETRIC_VALUE_UNIT(String mETRIC_VALUE_UNIT) {
		METRIC_VALUE_UNIT = mETRIC_VALUE_UNIT;
	}

	public String getDEFAULT_V_NO_DATA() {
		return DEFAULT_V_NO_DATA;
	}

	public void setDEFAULT_V_NO_DATA(String dEFAULT_V_NO_DATA) {
		DEFAULT_V_NO_DATA = dEFAULT_V_NO_DATA;
	}

	public String getVALUE_EXPS() {
		return VALUE_EXPS;
	}

	public void setVALUE_EXPS(String vALUE_EXPS) {
		VALUE_EXPS = vALUE_EXPS;
	}

	public double getMULTIPLIER() {
		return MULTIPLIER;
	}

	public void setMULTIPLIER(double mULTIPLIER) {
		MULTIPLIER = mULTIPLIER;
	}

	public long getPARENT_GRAPH_ID() {
		return PARENT_GRAPH_ID;
	}

	public void setPARENT_GRAPH_ID(long pARENT_GRAPH_ID) {
		PARENT_GRAPH_ID = pARENT_GRAPH_ID;
	}

	public int getCOLLECTION_CYCLE() {
		return COLLECTION_CYCLE;
	}

	public void setCOLLECTION_CYCLE(int cOLLECTION_CYCLE) {
		COLLECTION_CYCLE = cOLLECTION_CYCLE;
	}

	public List<MON_METRIC_CONFIG> getSubConfList() {
		return subConfList;
	}

	public void setSubConfList(List<MON_METRIC_CONFIG> subConfList) {
		this.subConfList = subConfList;
	}

	public ModbusValueExps getMve() {
		return mve;
	}

	public void setMve(ModbusValueExps mve) {
		this.mve = mve;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MON_METRIC_CONFIG [METRIC_CONFIG_ID=");
		builder.append(METRIC_CONFIG_ID);
		builder.append(", TEMPLATE_ID=");
		builder.append(TEMPLATE_ID);
		builder.append(", METRIC_CONFIG_NAME=");
		builder.append(METRIC_CONFIG_NAME);
		builder.append(", METRIC_NAME=");
		builder.append(METRIC_NAME);
		builder.append(", METRIC_BUSI_TYPE=");
		builder.append(METRIC_BUSI_TYPE);
		builder.append(", METRIC_VALUE_TYPE=");
		builder.append(METRIC_VALUE_TYPE);
		builder.append(", METRIC_VALUE_UNIT=");
		builder.append(METRIC_VALUE_UNIT);
		builder.append(", DEFAULT_V_NO_DATA=");
		builder.append(DEFAULT_V_NO_DATA);
		builder.append(", VALUE_EXPS=");
		builder.append(VALUE_EXPS);
		builder.append(", MULTIPLIER=");
		builder.append(MULTIPLIER);
		builder.append(", PARENT_GRAPH_ID=");
		builder.append(PARENT_GRAPH_ID);
		builder.append(", COLLECTION_CYCLE=");
		builder.append(COLLECTION_CYCLE);
		builder.append(", subConfListSize=");
		builder.append(subConfList == null ? 0 : subConfList.size());
		builder.append("]");
		return builder.toString();
	}

}
