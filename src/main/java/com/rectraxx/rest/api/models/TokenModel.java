package com.rectraxx.rest.api.models;

import java.io.Serializable;

public class TokenModel implements Serializable {

	private final String token;


	public TokenModel(String token)
	{
		this.token = token;
	}


	public String getToken()
	{
		return this.token;
	}
}
