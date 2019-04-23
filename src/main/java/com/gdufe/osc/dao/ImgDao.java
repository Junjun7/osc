package com.gdufe.osc.dao;

import com.gdufe.osc.entity.Img;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 21:26
 */
@Repository
public interface ImgDao {

	int insertImgLink(String link);

	List<Img> listImgLink(Integer offset, Integer limit);
}
