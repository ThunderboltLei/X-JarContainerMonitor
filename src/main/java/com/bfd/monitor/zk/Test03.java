/*
 * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk;

import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author: Thunderbolt.Lei<br>
 * @description: <br>
 */
public class Test03 implements Watcher, Runnable, StatCallback {
	
	private final static Logger logger = Logger.getLogger(Test03.class);

	private static String znode = "/user";

	private static ZooKeeper zk;
	private static Stat stat;

	public Test03(String hostPort, String znode, String filename) {
		try {
			zk = new ZooKeeper(hostPort, 2000, this);
		} catch (IOException e) {
			logger.error(e);
		}

		try {
			stat = getZk().exists(znode, false);
			if (null == stat) {
				getZk().create(znode, Bytes.toBytes("{first data}"),
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} else {
				logger.info("1. Show data: "
						+ Bytes.toString(getZk().getData(znode, true, stat)));
			}
		} catch (KeeperException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	public static void main(String[] args) {
		Test03 temp = new Test03("172.24.2.80:12181", znode, "");
		new Thread(temp).start();
		// thread begins
		try {
			temp.getZk().setData(znode, Bytes.toBytes("{second data}"),
					temp.getStat().getVersion());
		} catch (KeeperException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		}

		synchronized (temp) {
			try {
				temp.wait();
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	}

	public void run() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	}

	public void close() {
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * 监视事件被触发时
	 */
	public void process(WatchedEvent event) {
		String path = event.getPath();
		if (event.getType() == Event.EventType.None) {
			switch (event.getState()) {
			case SyncConnected:
				break;
			case Expired:
				this.close();
				break;
			}
		} else {
			if (path != null && path.equals(znode)) {
				getZk().exists(znode, true, this, null);
			}
		}
	}

	/**
	 * 状态回调方法，此方法被执行的触发条件是 在异步请求exists方法时，如果节点状态已经改变则执行此方法。
	 */
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		boolean exists = false;
		switch (rc) {
		// everything is fine
		case Code.Ok:
			exists = true;
			break;
		// node doesnot exist
		case Code.NoNode:
			exists = false;
			break;
		case Code.SessionExpired:
		
		// end
		case Code.NoAuth:
			this.close();
			break;
		// other else
		default:
			zk.exists(znode, false, this, null);
			return;
		}

		byte[] buf = null;
		if (exists) {
			try {
				buf = getZk().getData(znode, false, null);
			} catch (KeeperException e) {
				logger.error(e);
			} catch (InterruptedException e) {
				logger.error(e);
				return;
			}
		}
		// show data
		logger.info("Async Show: " + Bytes.toString(buf));
	}

	public ZooKeeper getZk() {
		return zk;
	}

	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}

	public Stat getStat() {
		return stat;
	}

	public void setStat(Stat stat) {
		this.stat = stat;
	}
}

