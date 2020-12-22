package com.gdufe.osc.dao.impl;

import com.gdufe.osc.dao.DownloadImgDao;
import com.gdufe.osc.dao.mapper.master.DownloadImgMapper;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.entity.DownloadImgExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class DownloadImgDaoImpl implements DownloadImgDao {

	@Autowired
	private DownloadImgMapper downloadImgMapper;

	private static final Long LINE = 100L;

	@Override
	public List<DownloadImg> listAllDownloadImg() {
		DownloadImgExample example = new DownloadImgExample();
		example.createCriteria().andIdLessThan(LINE);
		return downloadImgMapper.selectByExample(example);
	}

	@Override
	public DownloadImg getImageIds() {
		DownloadImgExample example = new DownloadImgExample();
		example.createCriteria().andIdGreaterThan(LINE);
		List<DownloadImg> imgIds = downloadImgMapper.selectByExample(example);
		return CollectionUtils.isEmpty(imgIds) ? null : imgIds.get(0);
	}

	@Override
	public int update(long id) {
		DownloadImg img = new DownloadImg();
		img.setLink("");
		img.setLinkname("");
		DownloadImgExample example = new DownloadImgExample();
		example.createCriteria().andIdEqualTo(id);
		return downloadImgMapper.updateByExampleSelective(img, example);
	}
}
