package com.klm.KLMPortal.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the GENERAL_INFO database table.
 * 
 */
@Entity
@Table(name="GENERAL_INFO")
@NamedQuery(name="GeneralInfo.findAll", query="SELECT g FROM GeneralInfo g")
public class GeneralInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private int id;

	@Column(name="INFO_KEY")
	private String infoKey;

	@Column(name="INFO_VALUE")
	private String infoValue;

	public GeneralInfo() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfoKey() {
		return this.infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getInfoValue() {
		return this.infoValue;
	}

	public void setInfoValue(String infoValue) {
		this.infoValue = infoValue;
	}

}