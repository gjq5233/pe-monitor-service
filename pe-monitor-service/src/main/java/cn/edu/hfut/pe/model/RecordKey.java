package cn.edu.hfut.pe.model;

import java.util.TreeMap;

/**
 * 指标数据中不变的部分,指标名称+标签
 * 
 * @author Administrator
 *
 */
public class RecordKey {
	private String metric;//监控指标名称,如：load.1min
	private TreeMap<String, String> tags;//包含通用tag：roomId,endpoint/ip,

	public RecordKey() {
	}

	public RecordKey(String metric, TreeMap<String, String> tags) {
		this.metric = metric;
		this.tags = tags;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecordKey [metric=");
		builder.append(metric);
		builder.append(", tags=");
		builder.append(tags);
		builder.append("]");
		return builder.toString();
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public TreeMap<String, String> getTags() { 
		return tags;
	}

	public void setTags(TreeMap<String, String> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metric == null) ? 0 : metric.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
		RecordKey other = (RecordKey) obj;
		if (metric == null) {
			if (other.metric != null)
				return false;
		} else if (!metric.equals(other.metric))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

}
