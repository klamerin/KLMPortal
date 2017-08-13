package com.klm.KLMPortal.data.DAO;

import java.time.LocalDate;
import java.util.ArrayList;

import com.klm.KLMPortal.beans.MovieBean;

public interface IMovieDAO {

	public ArrayList<String> getAllMovieNames();
	public ArrayList<MovieBean> getAllMovies();
	public ArrayList<MovieBean> getAllWatchedMovies();
	public ArrayList<MovieBean> getAllUnwatchedMovies();
	public ArrayList<MovieBean> getToWatchAloneFilms();
	public ArrayList<MovieBean> getToWatchWithTinaFilms();
	public ArrayList<MovieBean> getAllRecommendedMovies();
	
//	public void addToWatchMovie(String name, String comment);
	public void addNewMovie(String name, Boolean watched, Double rating, String comment, LocalDate date, Boolean recommend, String watchedBecause, Double sadnessLevel, Integer desireLevel, Boolean rewatchNeeded, Boolean withTina);
	public void editMovie(String name, Boolean watched, Double rating, String comment, LocalDate date, Boolean recommend, String watchedBecause, Double sadnessLevel, Integer desireLevel, Boolean rewatchNeeded, Boolean withTina, Integer watchCount, Integer id);
	public void deleteMovie(Integer id);
	public void incrementWatchCount(Integer id);
	
	
	
	
}
