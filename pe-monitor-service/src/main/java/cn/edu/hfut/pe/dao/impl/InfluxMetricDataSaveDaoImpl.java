package cn.edu.hfut.pe.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.pe.dao.ImetricDataSaveDao;
import cn.edu.hfut.pe.model.MetricData;
import influxdb.util.InfluxConn;
import influxdb.util.MetricPointUtil;

public class InfluxMetricDataSaveDaoImpl implements ImetricDataSaveDao {
	private final static Logger LOG = LoggerFactory.getLogger(InfluxMetricDataSaveDaoImpl.class);
	private InfluxConn influxConn;

	private String dataBase;

	public void init() {
		String createDatabaseQueryString = String.format("CREATE DATABASE \"%s\"", dataBase);
		influxConn.getInfluxDB().query(new Query(createDatabaseQueryString, dataBase), r -> {
			LOG.warn("db create success,{}", r);
		}, e -> {
			LOG.warn("db create faild,{}", e);
		});
	}

	@Override
	public void saveMetricList(List<MetricData> list) throws IOException {
		if (isAccessible()) {
			BatchPoints batchPoints = BatchPoints.database(dataBase).build();
			list.stream().map(MetricPointUtil::getPoint).forEach(d -> batchPoints.point(d));
			influxConn.getInfluxDB().write(batchPoints);
			LOG.trace("write influx 1st:{}", batchPoints.getPoints().get(0));
		} else {
			LOG.warn("db is unavailable");
		}
	}

	@Override
	public boolean isAccessible() {
		return influxConn.isActive();
	}

	public void setInfluxConn(InfluxConn influxConn) {
		this.influxConn = influxConn;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		InfluxConn c = new InfluxConn();
//		c.setIp("118.195.147.42");
//		if(args !=null && args.length>0) {
//			c.setIp(args[0]);
//		}
		int sss = 20000;
		
//		if(args !=null && args.length>1) {
//			sss = Integer.parseInt(args[1]);
//			if(sss<20000) {
//				sss = 20000;
//			}
//		}
		
		c.setIp("127.0.0.1");
		
		c.setUser("influxWrite");
		c.setPassword("influxWrite");
		c.connectToInfluxDB();
		
		InfluxMetricDataSaveDaoImpl dao = new InfluxMetricDataSaveDaoImpl();
		dao.setDataBase("monitor");
		dao.setInfluxConn(c);
		
		long _1MonthInMs = 31L*24*60*60*1000L;
		long endTime = System.currentTimeMillis();
		long startTime = endTime  -_1MonthInMs;
		long _1min = 60*1000L;
		
		List<MetricData> dataList = new ArrayList<>(sss*2);
		int count =0;
		for(int deviceId = 1; deviceId<=2000; deviceId++) {
			String metric = "table" + deviceId%25;//表名
			String filedName = "value";
			
			for(int metricId = 1; metricId<300; metricId ++) {
				
				if(metricId<=100) {
					
					filedName = "filed" + metricId;
				} else {
					metric = "metric"+metricId;
				}
				
				Number v = metricId;
				if(metricId%4==0) {
					v = metricId/10.0;
				}
				
				long curTime = startTime;
				while(endTime > curTime) {
					MetricData data = new MetricData();
					data.setMetric(metric);
					data.deviceId(deviceId);
					data.addField(filedName, v);
					data.time(curTime);
					dataList.add(data);
					
					count++;
					if(dataList.size()>=sss) {
						try {
							dao.saveMetricList(dataList);
							dataList = new ArrayList<>(sss*2);
							LOG.info("count:{}", count);
						} catch (Exception e) {
							LOG.error("e:{}", e);
						}
						
					}
					
					curTime +=_1min;
				}
			}
		}
//		
//		List<MetricData> list = new ArrayList<MetricData>();
//		MetricData md = MetricData.getInstance("gps");
//		md.tag("hphm", "皖A12345").tag("hpzl", "02").addField("jd", 117).addField("wd", 33);
//		list.add(md);
//		dao.saveMetricList(list);
//		System.out.println(c.isActive());
//		
//		InfluxDB client =c.connectToInfluxDB();
//		QueryResult qr = client.query(new Query("SELECT * FROM \"gps\".\"autogen\".\"gps\" "), TimeUnit.SECONDS);
//		System.out.println(qr);
		
	}
}
