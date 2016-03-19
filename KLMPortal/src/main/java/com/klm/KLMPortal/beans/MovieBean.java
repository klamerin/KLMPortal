package com.klm.KLMPortal.beans;

import java.sql.Date;
import java.sql.Timestamp;

public class MovieBean {

	private Integer id;
	private String name;
	private Integer rating;
	private String comment;
	private boolean watched;
	private Date watchedDate;
	private Timestamp lastModified;
	private boolean recommend;

	public MovieBean(Integer id, String name, Integer rating, String comment, boolean watched, Date watchedDate,
			Timestamp lastModified, boolean recommend) {
		super();
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.comment = comment;
		this.watched = watched;
		this.watchedDate = watchedDate;
		this.lastModified = lastModified;
		this.recommend = recommend;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isWatched() {
		return watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
	}

	public Date getWatchedDate() {
		return watchedDate;
	}

	public void setWatchedDate(Date watchedDate) {
		this.watchedDate = watchedDate;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

}
