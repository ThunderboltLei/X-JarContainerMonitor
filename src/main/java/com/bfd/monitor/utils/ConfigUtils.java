/**
 * 
 */
package com.bfd.monitor.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author lm8212
 *
 */
public class ConfigUtils {

	private static Logger logger = Logger.getLogger(ConfigUtils.class);

	private static Properties configProp;

	static {
		configProp = new Properties();

		try {
			configProp.load(logger.getClass().getClassLoader().getResourceAsStream(
					"config.properties"));
		} catch (IOException e) {
			logger.error(e.fillInStackTrace());
		}
	}

}
