package com.gdufe.osc.cron;

import com.gdufe.osc.OscApplicationTests;
import com.gdufe.osc.scheduled.CronTaskByStock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: yizhen
 * @date: 2019/2/25 20:25
 */
public class StockTest extends OscApplicationTests {

	@Autowired
	private CronTaskByStock cronTaskByStock;

	@Test
	public void getCronTest() {
		cronTaskByStock.notifyStockTime();
	}
}
