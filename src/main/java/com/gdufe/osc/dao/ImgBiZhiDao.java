package com.gdufe.osc.dao;

import com.gdufe.osc.entity.ImgBiZhi;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 21:26
 */
@Repository
public interface ImgBiZhiDao {

	int insertImgLink(String link);

	List<ImgBiZhi> listImgLink(Integer offset, Integer limit);

	Long countImg();
}
