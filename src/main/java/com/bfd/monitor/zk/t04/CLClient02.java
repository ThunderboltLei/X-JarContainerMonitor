/*
c * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t04;

import java.nio.charset.Charset;
import java.util.Random;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author: Thunderbolt.Lei<br>
 * @description: <br>
 */
public class CLClient02 {
	public static final String C_PATH_SUB = ChildrenListener.getcPath()
			+ "/cat";

	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String zookeeperConnectionString = "ThunderboltLei:2181";
					RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,
							3);
					CuratorFramework client = CuratorFrameworkFactory
							.newClient(zookeeperConnectionString, retryPolicy);
					client.start();

					Random random = new Random();
					Thread.sleep(1000 * random.nextInt(3));

					Stat stat = client.checkExists().forPath(C_PATH_SUB);
					if (stat == null) {
						client.create()
								.withMode(CreateMode.EPHEMERAL)
								.forPath(
										C_PATH_SUB,
										"catData".getBytes(Charset
												.forName(ChildrenListener
														.getCharset())));
					}

					Thread.sleep(1000 * random.nextInt(3));
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
