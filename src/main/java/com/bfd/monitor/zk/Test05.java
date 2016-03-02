/*
 * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk;

import javax.sound.midi.Patch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;

/**
 * @author: Thunderbolt.Lei<br>
 * @description: <br>
 */
public class Test05 {

	private final static Logger logger = Logger.getLogger(Test05.class);

	private final static String PATH = "/test01";

	static CuratorFramework zkclient = null;
	static String nameSpace = "test01";
	static {

		String zkhost = "ThunderboltLei:2181"; // zk的host
		RetryPolicy rp = new ExponentialBackoffRetry(1000, 3); // 重试机制
		Builder builder = CuratorFrameworkFactory.builder()
				.connectString(zkhost).connectionTimeoutMs(3000)
				.sessionTimeoutMs(3000).retryPolicy(rp);
		builder.namespace(nameSpace);
		CuratorFramework zclient = builder.build();
		zkclient = zclient;
		zkclient.start(); // 放在这前面执行
		zkclient.newNamespaceAwareEnsurePath(PATH);

	}

	public static void main(String[] args) throws Exception {

		watch();
		Thread.sleep(Long.MAX_VALUE);

	}

	/**
	 * 
	 * 监听节点变化
	 * 
	 * */
	public static void watch() throws Exception {
		PathChildrenCache cache = new PathChildrenCache(zkclient, PATH, false);
		cache.start();

		logger.info("Starting to mnitor the path: " + PATH + "........");
		PathChildrenCacheListener plis = new PathChildrenCacheListener() {

			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED: {
					logger.info("Node added: "
							+ ZKPaths
									.getNodeFromPath(event.getData().getPath()));
					break;
				}

				case CHILD_UPDATED: {
					logger.info("Node changed: "
							+ ZKPaths
									.getNodeFromPath(event.getData().getPath()));
					break;
				}

				case CHILD_REMOVED: {
					logger.info("Node removed: "
							+ ZKPaths
									.getNodeFromPath(event.getData().getPath()));
					break;
				}
				default: {
					logger.info("Another type: " + event.getData().getPath());
					break;
				}
				}

			}
		};
		// 注册监听
		cache.getListenable().addListener(plis);

	}
}