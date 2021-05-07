package cn.edu.hfut.pe.model;

import java.util.Map;

/**
 * 指标数据中变的部分,值+时间
 * 
 * @author Administrator
 *
 */
public class RecordValue {
	private Map<String, Number> fields;
	private long time; 
	public RecordValue() {
	}
	
	public RecordValue(Map<String, Number> fields, long time) {
		this.fields = fields;
		this.time = time;
	}

	public Map<String, Number> getFields() {
		return fields;
	}

	public void setFields(Map<String, Number> fields) {
		this.fields = fields;
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
		builder.append("RecordValue [fields=");
		builder.append(fields);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}
	
	
}
