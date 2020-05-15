package com.gdufe.osc.dao.impl;

import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.mapper.master.ImgBiZhiMapperMaster;
import com.gdufe.osc.dao.mapper.slave.ImgBiZhiMapperSlave;
import com.gdufe.osc.entity.ImgBiZhi;
import com.gdufe.osc.entity.ImgBiZhiExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 21:27
 */
@Repository
public class ImgBiZhiDaoImpl implements ImgBiZhiDao {

	@Autowired
	private ImgBiZhiMapperMaster imgBiZhiMapperMaster;

	@Autowired
	private ImgBiZhiMapperSlave imgBiZhiMapperSlave;

	@Override
	public int insertImgLink(String link) {
		ImgBiZhi img = new ImgBiZhi();
		img.setLink(link);
		int res = 0;
		try {
			res = imgBiZhiMapperMaster.insertSelective(img);
		} finally {
			return res > 0 ? 1 : 0;
		}
	}

	@Cacheable(value = "zhiHuImgBiZhi", key = "#offset + '_' + #limit")
	@Override
	public List<ImgBiZhi> listImgLink(int offset, int limit) {
		System.out.println("imgBiZhiMapperSlave = " + imgBiZhiMapperSlave);
		ImgBiZhiExample example = new ImgBiZhiExample();
		example.setOffset(offset);
		example.setLimit(limit);
		example.setOrderByClause("id DESC");
		return imgBiZhiMapperSlave.selectByExample(example);
	}

	@Cacheable(value = "zhiHuImgBiZhiCount")
	@Override
	public long countImg() {
		ImgBiZhiExample example = new ImgBiZhiExample();
		return imgBiZhiMapperSlave.countByExample(example);
	}
}








