package com.mybatis.wunao;

import com.mybatis.wunao.springcache.mapper.SpringCacheExpandMapper;
import com.mybatis.wunao.springcache.model.SpringCache;
import com.mybatis.wunao.springcache.model.SpringCacheExample;
import com.mybatis.wunao.springcache.model.SpringCacheExpand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WunaoApplicationTests {

	@Autowired
	SpringCacheExpandMapper springCacheExpandMapper;

	//简单展示selectByPrimaryKey
	@Test
	public void test1() {
		SpringCacheExample example = new SpringCacheExample();
		SpringCacheExample.Criteria criteria = example.createCriteria();
		//可直接使用子类mapper调用父类mapper方法
		SpringCache springCache = springCacheExpandMapper.selectByPrimaryKey("1");
		System.out.println(springCache);
	}

	//简单展示Example、Criteria
	@Test
	public void test2() {
		SpringCacheExample example = new SpringCacheExample();
		SpringCacheExample.Criteria criteria = example.createCriteria();
		//可直接使用子类mapper调用父类mapper方法
		SpringCache springCache = springCacheExpandMapper.selectByPrimaryKey("1");
		System.out.println(springCache);
	}

	//
	@Test
	public void test2() {
		List<SpringCacheExpand> list = springCacheExpandMapper.selectNameEqual("关联到spring_cache_relevance");
		System.out.println(list);
	}

}
