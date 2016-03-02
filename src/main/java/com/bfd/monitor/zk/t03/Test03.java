/*
 * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t03;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author: Thunderbolt.Lei<br>
 * @description: <br>
 */
public class Test03 {

	private final static Logger logger = Logger.getLogger(Test03.class);

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			doOne();
		} else {
			doAction(Integer.parseInt(args[0]));
		}
	}

	public static void doOne() throws Exception {
		String host1 = "ThunderboltLei:2181";
		ZooKeeper zk = connection(host1);
		initQueue(zk);
		produce(zk, 1);
		produce(zk, 2);
		cosume(zk);
		cosume(zk);
		cosume(zk);
		zk.close();
	}

	public static void doAction(int client) throws Exception {
		String host1 = "ThunderboltLei:2181";
		String host2 = "ThunderboltLei:2182";
		String host3 = "ThunderboltLei:2183";

		ZooKeeper zk = null;
		switch (client) {
		case 1:
			zk = connection(host1);
			initQueue(zk);
			produce(zk, 1);
			break;
		case 2:
			zk = connection(host2);
			initQueue(zk);
			produce(zk, 2);
			break;
		case 3:
			zk = connection(host3);
			initQueue(zk);
			cosume(zk);
			cosume(zk);
			cosume(zk);
			break;
		}
	}

	// 创建一个与服务器的连接
	public static ZooKeeper connection(String host) throws IOException {
		return new ZooKeeper(host, 60000, new Watcher() {
			public void process(WatchedEvent event) {
			}
		});
	}

	public static void initQueue(ZooKeeper zk) throws KeeperException,
			InterruptedException {
		if (zk.exists("/queue-fifo", false) == null) {
			logger.info("create /queue-fifo task-queue-fifo");
			zk.create("/queue-fifo", "task-queue-fifo".getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			logger.info("/queue-fifo is exist!");
		}
	}

	public static void produce(ZooKeeper zk, int x) throws KeeperException,
			InterruptedException {
		logger.info("create /queue-fifo/x" + x + " x" + x);
		zk.create("/queue-fifo/x" + x, ("x" + x).getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}

	public static void cosume(ZooKeeper zk) throws KeeperException,
			InterruptedException {
		List<String> list = zk.getChildren("/queue-fifo", true);
		if (list.size() > 0) {
			long min = Long.MAX_VALUE;
			for (String num : list) {
				if (min > Long.parseLong(num.substring(1))) {
					min = Long.parseLong(num.substring(1));
				}
			}
			logger.info("delete /queue/x" + min);
			zk.delete("/queue-fifo/x" + min, 0);
		} else {
			logger.info("No node to cosume");
		}
	}
}