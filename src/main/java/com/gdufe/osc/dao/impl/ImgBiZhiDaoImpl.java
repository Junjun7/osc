package com.gdufe.osc.dao.impl;

import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.dao.mapper.ImgBiZhiMapper;
import com.gdufe.osc.dao.mapper.ImgMapper;
import com.gdufe.osc.entity.Img;
import com.gdufe.osc.entity.ImgBiZhi;
import com.gdufe.osc.entity.ImgBiZhiExample;
import com.gdufe.osc.entity.ImgExample;
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
	private ImgBiZhiMapper imgBiZhiMapper;

	@Override
	public int insertImgLink(String link) {
		ImgBiZhi img = new ImgBiZhi();
		img.setLink(link);
		int res = 0;
		try {
			res = imgBiZhiMapper.insertSelective(img);
		} finally {
			return res > 0 ? 1 : 0;
		}
	}

	@Cacheable(value = "zhiHuImgBiZhi", key = "#offset+#limit")
	@Override
	public List<ImgBiZhi> listImgLink(Integer offset, Integer limit) {
		ImgBiZhiExample example = new ImgBiZhiExample();
		example.setOffset(offset);
		example.setLimit(limit);
		example.setOrderByClause("id DESC");
		return imgBiZhiMapper.selectByExample(example);
	}

	@Cacheable(value = "zhiHuImgBiZhiCount")
	@Override
	public Long countImg() {
		ImgBiZhiExample example = new ImgBiZhiExample();
		return imgBiZhiMapper.countByExample(example);
	}
}








