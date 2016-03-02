/*
 * Copyright (C) 2015 The Piramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t01;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * @author: lm8212
 * @description:
 */
public class ZookeeperOperator extends AbstractZookeeper {

	private final static Logger logger = Logger
			.getLogger(ZookeeperOperator.class.getName());
	
	private final static String PATH = "/test01/data";

	/**
	 * 
	 * <b>function:</b>创建持久态的znode,比支持多层创建.比如在创建/parent/child的情况下,无/parent.无法通过
	 * 
	 * @createDate 2013-01-16 15:08:38
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void create(String path, byte[] data) throws KeeperException,
			InterruptedException {
		/**
		 * 此处采用的是CreateMode是PERSISTENT 表示The znode will not be automatically
		 * deleted upon client's disconnect. EPHEMERAL 表示The znode will be
		 * deleted upon the client's disconnect.
		 */
		this.zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
	}

	/**
	 * 
	 * <b>function:</b>获取节点信息
	 * 
	 * @createDate 2013-01-16 15:17:22
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void getChild(String path) throws KeeperException,
			InterruptedException {
		try {
			List<String> list = this.zooKeeper.getChildren(path, false);
			if (list.isEmpty()) {
				logger.debug(path + "中没有节点");
			} else {
				logger.debug(path + "中存在节点");
				for (String child : list) {
					logger.debug("节点为：" + child);
				}
			}
		} catch (KeeperException.NoNodeException e) {
			logger.error(e);
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public byte[] getData(String path) throws KeeperException,
			InterruptedException {
		return this.zooKeeper.getData(path, false, null);
	}

	public boolean delete(String path) {
		try {
			Stat stat = this.zooKeeper.exists(path, true);
			if (null != stat) {
				logger.info(1111);
				this.zooKeeper.delete(path, -1);
			} else {
				logger.info(2222);
			}
			return true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	public static void main(String[] args) {

		String json = "{\"a\":1, \"b\":2, \"c\":3, \"d\":4}";

		try {

			ZookeeperOperator zkoperator = new ZookeeperOperator();
			zkoperator.connect("ThunderboltLei");

			// logger.info(new String(zkoperator.getData("/test01")));

			zkoperator.create("/test01", null);
			
			zkoperator.delete(PATH);

			zkoperator.create(PATH, json.getBytes());
			logger.info(Arrays.toString(zkoperator.getData(PATH)));
			//
			// zkoperator.create("/root/child1",data);
			// logger.info(Arrays.toString(zkoperator.getData("/root/child1")));
			//
			// zkoperator.create("/root/child2",data);
			// logger.info(Arrays.toString(zkoperator.getData("/root/child2")));

			// String zktest = "ZooKeeper的Java API测试";
			// zkoperator.create("/user/root/test03", zktest.getBytes());
			// logger.debug("获取设置的信息："
			// + new String(zkoperator.getData("/user/root")));

			logger.info("节点孩子信息:");
			zkoperator.getChild(PATH);

			zkoperator.close();

		} catch (Exception e) {
			logger.error(e);
		}

	}

}