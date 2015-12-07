/*
 * Copyright (C) 2015 The Piramid by ThunderboltLei
 */

package com.bfd.monitor.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author: lm8212<br>
 * @description: 加载系统配置信息<br>
 */
public class ConfigUtils {

	// 日志
	private static final Logger logger = Logger.getLogger(ConfigUtils.class);
	// 读取系统配置信息
	private static Properties configProps;

	static {
		configProps = new Properties();

		try {
			configProps.load(logger.getClass().getClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (IOException e) {
			logger.error(e.fillInStackTrace());
		}
	}

}
