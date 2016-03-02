/*
 * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t04;

/**
 * @author: Thunderbolt.Lei<br>
 * @description: 可以监控某一路径的直接子结点(一级子结点)变化，add,update,delete。
 *               利用此特性可以很方便的监控集群中的所有结点
 *               ，当然也就很方便的可以实现简单的key.hashCode()%serverCount式的分布式计算
 *               ，还可以实现简单的定制规则的负载均衡。<br>
 *               1.run ChildrenListener<br>
 *               2.run CLTest<br>
 */
public class CLTest {

	public static void main(String[] args) {
		ChildrenListener.main(null);

		CLClient01.main(null);
		CLClient02.main(null);
		CLClient03.main(null);
	}

}
