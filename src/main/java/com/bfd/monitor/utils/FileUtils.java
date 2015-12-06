/*
 * Copyright (C) 2015 The Piramid by ThunderboltLei
 */

package com.bfd.monitor.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author: lm8212<br>
 * @description: 动态加载JAR的配置文件的操作<br>
 */
public class FileUtils {

	// 日志
	private static final Logger logger = Logger.getLogger(FileUtils.class);
	// 动态加载的配置
	private static Properties monitorProps;
	// 存储动态加载的信息
	private static Set<Entry<Object, Object>> monitorInfosSet;

	static {
		monitorProps = new Properties();
		try {
			monitorProps.load(logger.getClass().getClassLoader()
					.getResourceAsStream("monitors.properties"));
		} catch (IOException e) {
			logger.error(e.fillInStackTrace());
		}

		/**
		 * 解析并存储动态加载的信息 包名:类路径,类路径,...,类路径
		 */
		setMonitorInfosSet(monitorProps.entrySet());
	}

	/**
	 * @return the monitorConHashMap
	 */
	public static Set<Entry<Object, Object>> getMonitorInfosSet() {
		return monitorInfosSet;
	}

	/**
	 * @param monitorConHashMap
	 *            the monitorConHashMap to set
	 */
	public static void setMonitorInfosSet(Set<Entry<Object, Object>> set) {
		monitorInfosSet = set;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<Entry<Object, Object>> set = FileUtils.getMonitorInfosSet();
		Iterator<Entry<Object, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<Object, Object> e = it.next();
			logger.info("key: " + e.getKey() + ", value: " + e.getValue());
		}
	}
}
