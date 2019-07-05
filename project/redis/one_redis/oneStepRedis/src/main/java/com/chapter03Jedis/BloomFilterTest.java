/**
 * @(#)BloomFilterTest.java, 2019/01/05.
 * <p/>
 * Copyright 2019 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.chapter03Jedis;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.nio.charset.Charset;


public class BloomFilterTest {

	@Test
	public void testGuava() {
		Funnel<Integer> funnel = Funnels.integerFunnel();
		int size = 1000000;
		double errorChance = 0.001;		//错误率
		BloomFilter<Integer> filter = BloomFilter.create(funnel , size , errorChance);
		for(int i = 0 ; i < size ; i++) {
			filter.put(i);
		}

		for(int i = 0 ; i < size ; i++ ) {
			if( !filter.mightContain(i) ) {
				System.out.println("发现不存在的数据 : " + i);
			}
		}
	}
}
