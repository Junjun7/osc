package com.gdufe.osc.dao.impl;

import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.dao.mapper.ImgMapper;
import com.gdufe.osc.entity.Img;
import com.gdufe.osc.entity.ImgExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 21:27
 */
@Repository
public class ImgDaoImpl implements ImgDao {

	@Autowired
	private ImgMapper imgMapper;

	@Override
	public int insertImgLink(String link) {
		Img img = new Img();
		img.setLink(link);
		int res = 0;
		try {
			res = imgMapper.insertSelective(img);
		} finally {
			return res > 0 ? 1 : 0;
		}
	}

	@Override
	public List<Img> listImgLink(Integer offset, Integer limit) {
		ImgExample example = new ImgExample();
		example.setOffset(offset);
		example.setLimit(limit);
		example.setOrderByClause("id DESC");
		return imgMapper.selectByExample(example);
	}

	@Override
	public Long countImg() {
		ImgExample example = new ImgExample();
		return imgMapper.countByExample(example);
	}
}








