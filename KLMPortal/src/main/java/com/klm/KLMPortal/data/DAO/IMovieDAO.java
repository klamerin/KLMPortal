package com.klm.KLMPortal.data.DAO;

import java.sql.Date;
import java.util.ArrayList;

import com.klm.KLMPortal.beans.MovieBean;

public interface IMovieDAO {

	public ArrayList<String> getAllMovieNames();
	public ArrayList<MovieBean> getAllMovies();
	public ArrayList<MovieBean> getAllWatchedMovies();
	public ArrayList<MovieBean> getAllUnwatchedMovies();
	public ArrayList<MovieBean> getAllRecommendedMovies();
	
//	public void addToWatchMovie(String name, String comment);
	public void addNewMovie(String name, Boolean watched, Integer rating, String comment, Date date, Boolean recommend);
	public void editMovie(String name, Boolean watched, Integer rating, String comment, Date date, Boolean recommend, Integer id);
	public void deleteMovie(Integer id);
}
