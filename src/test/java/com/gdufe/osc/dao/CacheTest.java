package com.gdufe.osc.dao;

import com.gdufe.osc.OscApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author changwenbo
 * @date 2020/11/17 20:04
 */
@Slf4j
public class CacheTest extends OscApplicationTests {

	@Autowired
	private DownloadImgDao downloadImgDao;

	@Autowired
	private SqlSessionFactory factory;

	@Autowired
	private AbcDAO abcDAO;

	@Test
	public void testCache() {

		log.info("cache = {}", factory.getConfiguration().getLocalCacheScope());
		log.info("cache = {}", factory.getConfiguration().isCacheEnabled());

		int resetId = abcDAO.getResetId();
		log.info("res = {}", resetId);
	}
}
