package com.gdufe.osc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 18:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class TweetListDetails extends TweetList {

	private List<String> imgBig;

	private List<String> imgSmall;

	private String imgBigStr;

	private String imgSmallStr;

}










