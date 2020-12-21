package com.gdufe.osc.dao;

import com.gdufe.osc.entity.DownloadImg;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadImgDao {

	List<DownloadImg> listAllDownloadImg();

	DownloadImg getImageIds();

	int update(long id);
}
