package cn.edu.hfut.pe.model;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import cn.edu.hfut.pe.dcu.cons.TagsKey;


/**
 * 最新核心指标数据模型.指标名,标签列表,值列表,时间。 其中有个特殊标签:moid,一般指标都有
 * 
 * @author Administrator
 *
 */
public class MetricData {

	private String metric;// net.if
	private TreeMap<String, String> tags;// <name, etho>,<moid, 1>
	private Map<String, Number> fields;// <out, 112>
	private long time;

	public final static MetricData getInstance(RecordKey k, RecordValue v) {
		MetricData data = new MetricData();
		data.setMetric(k.getMetric());
		data.setTags(k.getTags());
		data.setFields(v.getFields());
		data.setTime(v.getTime());
		return data;
	}

	public final static MetricData getInstance(String metric) {
		MetricData data = new MetricData();
		data.setMetric(metric);
		data.setTime(System.currentTimeMillis());
		return data;
	}

	public Long deviceId() {
		String moidStr = tags.get(TagsKey.DEVICE_ID);
		if (moidStr == null) {
			return null;
		}
		try {
			return Long.parseLong(moidStr);
		} catch (Exception e) {
			System.out.println(this);
		}
		return null;
	}

	public final MetricData tag(String key, String value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		if (tags == null) {
			tags = Maps.newTreeMap();
		}
		tags.put(key, value);
		return this;
	}

	public final MetricData time() {
		this.time = System.currentTimeMillis();
		return this;
	}

	public final MetricData time(long time) {
		this.time = time;
		return this;
	}

	public final MetricData tags(Map<String, String> tags) {
		Objects.requireNonNull(fields);
		if (this.tags == null) {
			this.tags = Maps.newTreeMap();
		}
		this.tags.putAll(tags);
		return this;
	}

	public final MetricData addField(String key, Number value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		if (fields == null) {
			fields = Maps.newTreeMap();
		}
		fields.put(key, value);
		return this;
	}

	public final MetricData removeField(String key) {
		Objects.requireNonNull(key);
		if (fields == null) {
			return this;
		}
		fields.remove(key);
		return this;
	}

	public final MetricData addField(Map<String, Number> fields) {
		Objects.requireNonNull(fields);
		if (this.fields == null) {
			this.fields = Maps.newHashMap();
		}
		this.fields.putAll(fields);
		return this;
	}

	public final RecordKey recordKey() {
		return new RecordKey(metric, tags);
	}

	public final RecordValue recordValue() {
		return new RecordValue(fields, time);
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

	public int keyHashCode() {
		return recordKey().hashCode();
	}

	public String keyStr() {
		String key = metric;
		if (!CollectionUtils.isEmpty(tags)) {
			String tag = "";
			for (Map.Entry<String, String> e : tags.entrySet()) {
				if (e.getKey().equals(TagsKey.DEVICE_ID)) {
					continue;
				}
				tag += "," + e.getKey() + "=" + e.getValue();
			}
			if (tag.equals("")) {
				return key;
			}
			key = key + "@" + tag.substring(1);
		}
		return key;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Map<String, Number> getFields() {
		return fields;
	}

	public void setFields(Map<String, Number> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetricData [metric=");
		builder.append(metric);
		builder.append(", tags=");
		builder.append(tags);
		builder.append(", fields=");
		builder.append(fields);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}

	
	public boolean emptyFields() {
		return CollectionUtils.isEmpty(fields);
	}

	public MetricData deviceId(long deviceId) {
		return tag(TagsKey.DEVICE_ID, deviceId+"");
	}
	
	public static void main(String[] args) {
		MetricData md = new MetricData();
		md.setMetric("cpu");
		TreeMap<String, String> tags = Maps.newTreeMap();
		tags.put("host", "10.0.99.1");
		tags.put("roomId", "1");
		TreeMap<String, Number> values = Maps.newTreeMap();
		values.put("idle", 117);
		values.put("utilization", 110);

		md.setTags(tags);
		md.setFields(values);
		System.out.println(JSON.toJSONString(md.time()));


		System.out.println(Long.MAX_VALUE);
		System.out.println(md.keyStr());
		System.out.println(md.deviceId());
		md.deviceId(555);
		System.out.println(md.deviceId());
	}

	
}
