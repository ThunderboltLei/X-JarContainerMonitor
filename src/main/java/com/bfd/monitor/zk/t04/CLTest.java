/*
 * Copyright (C) 2016 The Pyramid by ThunderboltLei
 */

package com.bfd.monitor.zk.t04;

/**
 * @author: Thunderbolt.Lei<br>
 * @description: <br>
 */
public class CLTest {
	
	public static void main(String[] args){
		ChildrenListener.main(null);
		
		CLClient01.main(null);
		CLClient02.main(null);
		CLClient03.main(null);
	}

}
