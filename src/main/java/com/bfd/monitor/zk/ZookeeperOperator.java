/*
 * Copyright (C) 2015 The Piramid by ThunderboltLei
 */

package com.bfd.monitor.zk;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * @author: lm8212
 * @description:
 */
public class ZookeeperOperator extends AbstractZookeeper {

	private static final Logger logger = Logger
			.getLogger(ZookeeperOperator.class.getName());

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

	public byte[] getData(String path) throws KeeperException,
			InterruptedException {
		return this.zooKeeper.getData(path, false, null);
	}

	public static void main(String[] args) {
		try {
			ZookeeperOperator zkoperator = new ZookeeperOperator();
			zkoperator.connect("hadoop-master");

			logger.info(new String(zkoperator.getData("/user/root/test01")));

			byte[] data = new byte[] { 'a', 'b', 'c', 'd' };

			// zkoperator.create("/root",null);
			// System.out.println(Arrays.toString(zkoperator.getData("/root")));
			//
			// zkoperator.create("/root/child1",data);
			// System.out.println(Arrays.toString(zkoperator.getData("/root/child1")));
			//
			// zkoperator.create("/root/child2",data);
			// System.out.println(Arrays.toString(zkoperator.getData("/root/child2")));

			// String zktest = "ZooKeeper的Java API测试";
			// zkoperator.create("/user/root/test03", zktest.getBytes());
			// logger.debug("获取设置的信息："
			// + new String(zkoperator.getData("/user/root")));

			System.out.println("节点孩子信息:");
			zkoperator.getChild("/user/root/test01");

			zkoperator.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}