package influxdb.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.springframework.util.CollectionUtils;

import base.util.CharsetUtil;

public class MetricPointUtil {

	public final static Point getPoint(cn.edu.hfut.pe.model.MetricData data) {
		org.influxdb.dto.Point.Builder builer = Point.measurement(data.getMetric()).time(data.getTime(),
				TimeUnit.MILLISECONDS);
		Map<String, String> tagMap = data.getTags();
		if (!CollectionUtils.isEmpty(tagMap)) {
			for (Map.Entry<String, String> e : tagMap.entrySet()) {
				try {
					builer.tag(e.getKey(), URLEncoder.encode(e.getValue(), CharsetUtil.UTF_8_CHARSET));
				} catch (UnsupportedEncodingException e1) {
				}
			}
		}

		Map<String, Number> valueMap = data.getFields();
		if (!CollectionUtils.isEmpty(valueMap)) {
			for (Map.Entry<String, Number> e : valueMap.entrySet()) {
				builer.addField(e.getKey(), e.getValue());
			}
		}

		return builer.build();
	}

}
