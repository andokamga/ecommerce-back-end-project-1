package org.onLineShop.service.from;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TokemAcess {
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("expires_in")
	private Integer expiresIn;
}
