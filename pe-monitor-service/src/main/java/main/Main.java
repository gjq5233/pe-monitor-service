package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


import base.util.AppConfPathUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

public class Main {
	static {
		AppConfPathUtil.logConfInit();
	}

	private final static Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println(Long.MAX_VALUE);

		LOG.warn("logback.conf.path:{}", AppConfPathUtil.LOG_CONF_PATH);
		LOG.warn("spring.conf.path:{}", AppConfPathUtil.SPRING_CONF_PATH);

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);

		System.setProperty("io.netty.noUnsafe", "false");// 默认是false，直接缓冲区;true
		System.setProperty("io.netty.allocator.type", "pooled");// 默认是pooled，使用缓冲池;unpooled

		System.setProperty("fastjson.compatibleWithJavaBean", "true");
		ResourceLeakDetector.setLevel(Level.PARANOID);
		if (AppConfPathUtil.SPRING_CONF_PATH.startsWith("applicationContext")) {
			new ClassPathXmlApplicationContext(AppConfPathUtil.SPRING_CONF_PATH);
		} else {
			new FileSystemXmlApplicationContext(AppConfPathUtil.SPRING_CONF_PATH);
		}

	}

}
