package cn.edu.hfut.pe.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;
import io.netty.channel.Channel;


public class RunTimeCache {
	
	/**
	 * <dcuid, channel>
	 */
	private volatile static ConcurrentMap<Long, Channel>  MAP_DCUID_CHANNEL = new ConcurrentHashMap<>();

	
	public static Channel getChannelByDcuid(long dcuid) {
		return MAP_DCUID_CHANNEL.get(dcuid);
	}
	
	public static Channel getChannelByDeviceid(long deviceId) {
		MON_DEVICE device =ConfCache.getDevice(deviceId);
		if(device == null) {
			return null;
		}
		return getChannelByDcuid(device.getDCU_ID());
	}
	
	public static Channel getChannelByDcuid(MON_DEVICE device) {
		return MAP_DCUID_CHANNEL.get(device.getDCU_ID());
	}
	
	public static Channel putChannelByDcuid(long dcuid, Channel ch) {
		return MAP_DCUID_CHANNEL.put(dcuid, ch);
	}
	
	
	public static final Cache<String, MON_METRIC_CONFIG> DATA_CACHE = CacheBuilder.newBuilder()
			.concurrencyLevel(16)
			.initialCapacity(6000)
            .maximumSize(24000) // 设置缓存的最大容量
            .expireAfterWrite(2, TimeUnit.MINUTES) // 设置缓存在写入2分钟后失效
            .build();
	
	public static final Cache<String, Long> CMD_CACHE = CacheBuilder.newBuilder()
			.concurrencyLevel(16)
			.initialCapacity(6000)
            .maximumSize(24000) // 设置缓存的最大容量
            .expireAfterWrite(2, TimeUnit.MINUTES) // 设置缓存在写入2分钟后失效
            .build();
}
