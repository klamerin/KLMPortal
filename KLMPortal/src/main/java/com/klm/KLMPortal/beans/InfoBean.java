package com.klm.KLMPortal.beans;

public class InfoBean {
	
	private Integer id;
	private String infoKey;
	private String infoValue;
	private String comment;

	public InfoBean(Integer id, String infoKey, String infoValue, String comment) {
		super();
		this.id = id;
		this.infoKey = infoKey;
		this.infoValue = infoValue;
		this.comment = comment;
	}

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getInfoValue() {
		return infoValue;
	}

	public void setInfoValue(String infoValue) {
		this.infoValue = infoValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
