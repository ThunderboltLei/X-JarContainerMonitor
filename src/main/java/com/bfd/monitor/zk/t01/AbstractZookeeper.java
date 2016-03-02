/*
 * Copyright (C) 2015 The Piramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t01;

import java.io.IOException;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * @Author: lm8212<br>
 * @Date: 2013-12-2 下午4:41:59<br>
 * @Project: TestJava<br>
 * @Package: com.test.java.hadoop.zookeeper<br>
 * @File: AbstractZookeeper.java<br>
 * @Description: <br>
 */
public class AbstractZookeeper implements Watcher {

	private static Logger logger = Logger.getLogger(AbstractZookeeper.class
			.getName());

	// 缓存时间
	private static final int SESSION_TIME = 2000;
	protected ZooKeeper zooKeeper;
	protected CountDownLatch countDownLatch = new CountDownLatch(1);

	public void connect(String hosts) throws IOException, InterruptedException {
		zooKeeper = new ZooKeeper(hosts, SESSION_TIME, this);
		countDownLatch.await();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		// if (event.getState() == KeeperState.SyncConnected) {
		countDownLatch.countDown();
		logger.info(countDownLatch.getCount());
		logger.info("Event: " + event.getType());
		// }
	}

	public void close() throws InterruptedException {
		zooKeeper.close();
		logger.info("Watcher is closed...");
	}

}
