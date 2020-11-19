package com.gdufe.osc.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

/**
 * @author changwenbo
 * @date 2020/11/18 21:58
 */
@DAO(catalog = "ABCD")
public interface AbcDAO {

	@SQL("SELECT id FROM test.cookies")
	int getResetId();

}
