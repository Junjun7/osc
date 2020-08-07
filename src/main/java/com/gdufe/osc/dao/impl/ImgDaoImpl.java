package com.gdufe.osc.dao.impl;

import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.dao.mapper.master.ImgMapper;
import com.gdufe.osc.entity.Img;
import com.gdufe.osc.entity.ImgExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 21:27
 */
@Slf4j
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
	public int insertImgLink(long id, String link) {
		Img img = new Img();
		img.setLink(link);
		img.setId(id);
		int res = 0;
		try {
			res = imgMapper.insertSelective(img);
		} finally {
			return res > 0 ? 1 : 0;
		}
	}

	/**
	 * example.createCriteria().andLinkNotLike("http:%");
	 * 这个先不请求到gdufe888的图片
	 * 下面计算count类似
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Cacheable(value = "zhiHuImg", key = "#offset + '_' + #limit")
	@Override
	public List<Img> listImgLink(int offset, int limit) {
		log.info("offset = {}, limit = {}", offset, limit);
		ImgExample example = new ImgExample();
		example.createCriteria().andLinkNotLike("http:%");
		example.setOffset(offset);
		example.setLimit(limit);
		example.setOrderByClause("id DESC");
		List<Img> res = imgMapper.selectByExample(example);
		log.info("res = {}", CollectionUtils.isEmpty(res));
		return res;
	}

	@Cacheable(value = "zhiHuImgCount")
	@Override
	public long countImg() {
		ImgExample example = new ImgExample();
		example.createCriteria().andLinkNotLike("http:%");
		return imgMapper.countByExample(example);
	}
}








