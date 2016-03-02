/*
 * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t02;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author: Thunderbolt.Lei<br>
 * @description: <br>
 */
public class Test02 {

	private final static Logger logger = Logger.getLogger(Test02.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Watcher watcher = new Watcher() {
			// 监控所有被触发的事件
			public void process(WatchedEvent event) {
				logger.info("触发了" + event.getType() + "事件！");
			}
		};

		ZooKeeper zooKeeper = new ZooKeeper("ThunderboltLei:2181", 1000,
				watcher);// 第一个参数：ZooKeeper服务器的连接地址，如果ZooKeeper是集群模式或伪集群模式（即ZooKeeper服务器有多个），那么每个连接地址之间使用英文逗号间隔，单个连接地址的语法格式为“主机IP:ZooKeeper服务器端口号”；
							// 第二个参数：session超时时长（单位：毫秒）
							// 第三个参数：用于监控目录节点数据变化和子目录状态变化的Watcher对象
		zooKeeper.create("/test02", "RootNodeData".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);// 创建一个节点名为“/RootNode”的目录节点
		logger.info("“/test02”节点状态：" + zooKeeper.exists("/test02", true));// 判断指定目录节点是否存在
		logger.info("“RootNode”节点上数据："
				+ new String(zooKeeper.getData("/test02", false, null)));// 获取“test02”节点上的数据
		zooKeeper.create("/test02/ChildNode1", "ChildNode1Data".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);// 在“test02”节点下创建一个名为“ChildNode1”的子目录节点
		zooKeeper.create("/test02/ChildNode2", "ChildNode2Data".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);// 在“test02”节点下创建一个和“ChildNode1”同级的名为“ChildNode2”的子目录节点
		logger.info("目录节点“test02”下的所有子目录节点有："
				+ zooKeeper.getChildren("/test02", true)); // 取出目录节点“test02”下的所有子目录节点
		zooKeeper.setData("/test02/ChildNode2", "NewChildNode2Data".getBytes(),
				-1);// 修改名为“ChildNode2”的目录节点数据

		zooKeeper.delete("/test02/ChildNode1", -1);// 删除“/test02/ChildNode1”目录节点
		logger.info("“/test02/ChildNode1”节点状态："
				+ zooKeeper.exists("/test02/ChildNode1", false));// 判断“/test02/ChildNode1”目录节点是否存在
		zooKeeper.delete("/test02/ChildNode2", -1);// 删除“/test02/ChildNode2”目录节点
		zooKeeper.delete("/test02", -1);// 删除“/test02”目录节点
		zooKeeper.close(); // 关闭与ZooKeeper的连接
	}

}
