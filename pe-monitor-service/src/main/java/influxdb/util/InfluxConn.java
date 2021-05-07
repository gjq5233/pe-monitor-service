package influxdb.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import okhttp3.OkHttpClient;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InfluxConn {
	private final static Logger LOG = LoggerFactory.getLogger(InfluxConn.class);
	private String ip;
	private int port = 8086;
	private String user = "a";
	private String password = "a";
	private InfluxDB influxDB;
	private final ScheduledExecutorService sche = Executors.newSingleThreadScheduledExecutor();
	private volatile boolean isActive;

	public InfluxDB connectToInfluxDB() {
		return connectToInfluxDB(null);
	}

	public InfluxDB connectToInfluxDB(final OkHttpClient.Builder client) {
		OkHttpClient.Builder clientToUse;
		if (client == null) {
			clientToUse = new OkHttpClient.Builder();
		} else {
			clientToUse = client;
		}
		influxDB = InfluxDBFactory.connect("http://" + ip + ":" + port, user, password, clientToUse);
		influxDB.setLogLevel(InfluxDB.LogLevel.NONE);
		influxDB.enableGzip();
		influxDB.enableBatch(100, 100, TimeUnit.MILLISECONDS);
		try {
			Pong response = influxDB.ping();
			if (response.isGood()) {
				isActive = true;
			}
		} catch (Throwable e) {
			LOG.error("{}", e);
		}
		sche.scheduleWithFixedDelay(() -> ping(), 30, 30, TimeUnit.SECONDS);
		if (isActive) {
			LOG.warn("Successful Connected to InfluxDB {}:{}:{}, version:{}", influxDB, ip, port, influxDB.version());
		} else {
			LOG.warn("failed Connected to InfluxDB {}:{}:{}, version:{}", influxDB, ip, port, influxDB.version());
		}

		return influxDB;
	}

	private void ping() {
		try {
			Pong response = influxDB.ping();
			if (response.isGood()) {
				if (!isActive) {
					LOG.warn("Successful Connected to InfluxDB {}:{}:{}, version:{}", influxDB, ip, port,
							influxDB.version());
				}
				isActive = true;
			} else {
				if (isActive) {
					LOG.warn("failed Connected to InfluxDB {}:{}:{}, version:{}", influxDB, ip, port,
							influxDB.version());
				}
				isActive = false;
			}
		} catch (Throwable e) {
			isActive = false;
			LOG.error("{}", e);
		}
	}
	
	public void close() {
		try {
			influxDB.close();
		} catch (Exception e) {
		}
		sche.shutdownNow();
		sche.shutdown();
	}

	public boolean isActive() {
		return isActive;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public InfluxDB getInfluxDB() {
		return influxDB;
	}

}
