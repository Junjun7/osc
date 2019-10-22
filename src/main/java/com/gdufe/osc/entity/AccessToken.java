package com.gdufe.osc.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 10:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

	@SerializedName("access_token")
	private String accessToken;

	@SerializedName("refresh_token")
	private String refreshToken;

	@SerializedName("token_type")
	private String tokenType;

	@SerializedName("expires_in")
	private Integer expiresIn;

	private Integer uid;
}
