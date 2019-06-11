package com.gdufe.osc.dao.impl;

import com.gdufe.osc.dao.DownloadImgDao;
import com.gdufe.osc.dao.mapper.DownloadImgMapper;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.entity.DownloadImgExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DownloadImgDaoImpl implements DownloadImgDao {

	@Autowired
	private DownloadImgMapper downloadImgMapper;

	@Override
	public List<DownloadImg> listAllDownloadImg() {
		DownloadImgExample example = new DownloadImgExample();
		return downloadImgMapper.selectByExample(example);
	}
}
