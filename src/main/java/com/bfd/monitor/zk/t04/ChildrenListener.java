/*
 * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t04;

import java.io.IOException;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author: Thunderbolt.Lei<br>
 * @description: <br>
 */
public class ChildrenListener {

	private final static Logger logger = Logger
			.getLogger(ChildrenListener.class);

	private static final String C_PATH = "/test01";
	private static final String CHARSET = "UTF-8";

	public void addInfo(String serviceName, String info) {
		try {
			ZooKeeper zk = new ZooKeeper("ThunderboltLei:2181", 3000, null);
			Stat stat = zk.exists("/test01" + serviceName, null);
			if (null != stat)
				zk.delete("/test01/", -1);
			zk.create("/test01/" + serviceName, info.getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void main(String[] args) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String zookeeperConnectionString = "ThunderboltLei:2181";
						RetryPolicy retryPolicy = new ExponentialBackoffRetry(
								1000, 3);
						CuratorFramework client = CuratorFrameworkFactory
								.newClient(zookeeperConnectionString,
										retryPolicy);
						client.start();

						// ensure path of /test
						new EnsurePath(C_PATH).ensure(client
								.getZookeeperClient());

						final PathChildrenCache pathChildrenCache = new PathChildrenCache(
								client, C_PATH, true);
						pathChildrenCache.getListenable().addListener(
								new PathChildrenCacheListener() {
									@Override
									public void childEvent(
											CuratorFramework client,
											PathChildrenCacheEvent event)
											throws Exception {
										logger.info("----- catch children [ "
												+ event.getData().getPath()
												+ " ] change -----");
										logger.info("--- " + event.getType()
												+ ","
												+ event.getData().getData());
										List<ChildData> childDataList = pathChildrenCache
												.getCurrentData();
										if (childDataList != null
												&& childDataList.size() > 0) {
											logger.info("--- all children as:");
											for (ChildData childData : childDataList) {
												logger.info("--- "
														+ childData.getPath()
														+ ","
														+ new String(childData
																.getData(),
																getCharset()));
											}
										}
									}
								});
						pathChildrenCache.start();

						Thread.sleep(Integer.MAX_VALUE);
						client.close();
					} catch (Exception e) {
						logger.error(e);
					}
				}
			}).start();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @return the cPath
	 */
	public static String getcPath() {
		return C_PATH;
	}

	/**
	 * @return the charset
	 */
	public static String getCharset() {
		return CHARSET;
	}

}
