/*
 * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t04;

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

/**
 * @author: Thunderbolt.Lei<br>
 * @description: <br>
 */
public class ChildrenListener {
	
	private static final String C_PATH = "/test01";
	private static final String CHARSET = "UTF-8";

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
										System.out
												.println("================== catch children change ==================");
										System.out.println("==="
												+ event.getType() + ","
												+ event.getData().getPath()
												+ ","
												+ event.getData().getData());
										List<ChildData> childDataList = pathChildrenCache
												.getCurrentData();
										if (childDataList != null
												&& childDataList.size() > 0) {
											System.out
													.println("===all children as:");
											for (ChildData childData : childDataList) {
												System.out.println("=="
														+ childData.getPath()
														+ ","
														+ new String(childData
																.getData(),
																"UTF-8"));
											}
										}
									}
								});
						pathChildrenCache.start();

						Thread.sleep(Integer.MAX_VALUE);
						client.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
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
