package br.com.dextra.utils;

public enum IndexKeys {

	POST("post"),DOCUMENT("document"),COMMENT("comment");

	private String value;

	IndexKeys(String value){
		this.value=value;
	}

	public String getKey(){
		return value;
	}

}
