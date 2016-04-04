package com.klm.KLMPortal.data.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.klm.KLMPortal.beans.MovieBean;
import com.klm.KLMPortal.data.AbstractDAO;
import com.klm.KLMPortal.data.MSSQLDAOFactory;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class MovieDAOImpl extends AbstractDAO implements IMovieDAO {

	public MovieDAOImpl() {
		initSQLMapping();
	}

	@Override
	public ArrayList<String> getAllMovieNames() {
		ArrayList<String> films = new ArrayList<String>();
		String sql = sqlMapping.getValue("Movie.getAllFilmNames");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			// ps.setLong(1, businessUnitId);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				films.add(rslt.getString("NAME"));
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllFilmNames failed \n" + e);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		return films;
	}

	@Override
	public ArrayList<MovieBean> getAllMovies() {
		ArrayList<MovieBean> films = new ArrayList<MovieBean>();
		String sql = sqlMapping.getValue("Movie.getAllFilms");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			// ps.setLong(1, businessUnitId);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				MovieBean film = new MovieBean(rslt.getInt("ID"), rslt.getString("NAME"), rslt.getDouble("RATING"),
						rslt.getString("COMMENT"), rslt.getBoolean("WATCHED"), rslt.getDate("WATCHED_DATE"),
						rslt.getTimestamp("LAST_MODIFIED"), rslt.getBoolean("RECOMMEND"), rslt.getString("WATCHED_BECAUSE"), rslt.getDouble("SADNESS_LEVEL"), rslt.getInt("DESIRE_LEVEL"), rslt.getBoolean("REWATCH_NEEDED") ,rslt.getInt("WATCH_COUNT"));
				films.add(film);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllFilms failed \n" + e);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		return films;
	}

	@Override
	public ArrayList<MovieBean> getAllWatchedMovies() {
		ArrayList<MovieBean> films = new ArrayList<MovieBean>();
		String sql = sqlMapping.getValue("Movie.getAllWatchedFilms");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			// ps.setLong(1, businessUnitId);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				MovieBean film = new MovieBean(rslt.getInt("ID"), rslt.getString("NAME"), rslt.getDouble("RATING"),
						rslt.getString("COMMENT"), rslt.getBoolean("WATCHED"), rslt.getDate("WATCHED_DATE"),
						rslt.getTimestamp("LAST_MODIFIED"), rslt.getBoolean("RECOMMEND"), rslt.getString("WATCHED_BECAUSE"), rslt.getDouble("SADNESS_LEVEL"), rslt.getInt("DESIRE_LEVEL"), rslt.getBoolean("REWATCH_NEEDED") ,rslt.getInt("WATCH_COUNT"));
				films.add(film);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllWatchedFilms failed \n" + e);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		return films;
	}

	@Override
	public ArrayList<MovieBean> getAllUnwatchedMovies() {
		ArrayList<MovieBean> films = new ArrayList<MovieBean>();
		String sql = sqlMapping.getValue("Movie.getAllUnwatchedFilms");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			// ps.setLong(1, businessUnitId);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				MovieBean film = new MovieBean(rslt.getInt("ID"), rslt.getString("NAME"), rslt.getDouble("RATING"),
						rslt.getString("COMMENT"), rslt.getBoolean("WATCHED"), rslt.getDate("WATCHED_DATE"),
						rslt.getTimestamp("LAST_MODIFIED"), rslt.getBoolean("RECOMMEND"), rslt.getString("WATCHED_BECAUSE"), rslt.getDouble("SADNESS_LEVEL"), rslt.getInt("DESIRE_LEVEL"), rslt.getBoolean("REWATCH_NEEDED") ,rslt.getInt("WATCH_COUNT"));
				films.add(film);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllUnwatchedFilms failed \n" + e);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		return films;
	}

	@Override
	public ArrayList<MovieBean> getAllRecommendedMovies() {
		ArrayList<MovieBean> films = new ArrayList<MovieBean>();
		String sql = sqlMapping.getValue("Movie.getAllRecommendedFilms");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			// ps.setLong(1, businessUnitId);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				MovieBean film = new MovieBean(rslt.getInt("ID"), rslt.getString("NAME"), rslt.getDouble("RATING"),
						rslt.getString("COMMENT"), rslt.getBoolean("WATCHED"), rslt.getDate("WATCHED_DATE"),
						rslt.getTimestamp("LAST_MODIFIED"), rslt.getBoolean("RECOMMEND"), rslt.getString("WATCHED_BECAUSE"), rslt.getDouble("SADNESS_LEVEL"), rslt.getInt("DESIRE_LEVEL"), rslt.getBoolean("REWATCH_NEEDED") ,rslt.getInt("WATCH_COUNT"));
				films.add(film);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllRecommendedFilms failed \n" + e);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		return films;
	}

	@Override
	public void addNewMovie(String name, Boolean watched, Double rating, String comment, Date date,
			Boolean recommend, String watchedBecause, Double sadnessLevel, Integer desireLevel, Boolean rewatchNeeded) {
		String sql = sqlMapping.getValue("Movie.addMovie");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, name);
			if (rating != null) {
				ps.setDouble(2, rating);
			} else {
				ps.setNull(2, Types.FLOAT);
			}
			if (comment != null) {
				ps.setString(3, comment);
			} else {
				ps.setNull(3, Types.VARCHAR);
			}
			ps.setBoolean(4, watched);
			if (date != null) {
				ps.setDate(5, date);
			} else {
				ps.setNull(5, Types.TIMESTAMP);
			}
			if (recommend != null) {
				ps.setBoolean(6, recommend);
			} else {
				ps.setNull(6, Types.BIT);
			}
			if (watchedBecause != null) {
				ps.setString(7, watchedBecause);
			} else {
				ps.setNull(7, Types.VARCHAR);
			}
			if (sadnessLevel != null) {
				ps.setDouble(8, sadnessLevel);
			} else {
				ps.setNull(8, Types.DOUBLE);
			}
			
			if (desireLevel != null) {
				ps.setInt(9, desireLevel);
			} else {
				ps.setNull(9, Types.INTEGER);
			}
			if (rewatchNeeded != null) {
				ps.setBoolean(10, rewatchNeeded);
			} else {
				ps.setNull(10, Types.BIT);
			}
			ps.setInt(11, 1);

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query addMovie failed \n" + e);
			Notification.show("Faled to add Movie, sorry...", Type.ERROR_MESSAGE);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		System.out.println("Query addMovie updated " + result + " rows");
	};

	@Override
	public void editMovie(String name, Boolean watched, Double rating, String comment, Date date, Boolean recommend, String watchedBecause, Double sadnessLevel, Integer desireLevel, Boolean rewatchNeeded, Integer id) {
		String sql = sqlMapping.getValue("Movie.editMovie");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			if (comment != null) {
				ps.setString(1, comment);
			} else {
				ps.setNull(1, Types.VARCHAR);
			}
			if (rating != null) {
				ps.setDouble(2, rating);
			} else {
				ps.setNull(2, Types.FLOAT);
			}
			if (recommend != null) {
				ps.setBoolean(3, recommend);
			} else {
				ps.setNull(3, Types.BIT);
			}
			ps.setBoolean(4, watched);
			if (date != null) {
				ps.setDate(5, date);
			} else {
				ps.setNull(5, Types.TIMESTAMP);
			}
			ps.setString(6, name);
			if (watchedBecause != null) {
				ps.setString(7, watchedBecause);
			} else {
				ps.setNull(7, Types.VARCHAR);
			}
			if (sadnessLevel != null) {
				ps.setDouble(8, sadnessLevel);
			} else {
				ps.setNull(8, Types.DOUBLE);
			}
			if (desireLevel != null) {
				ps.setInt(9, desireLevel);
			} else {
				ps.setNull(9, Types.INTEGER);
			}
			if (rewatchNeeded != null) {
				ps.setBoolean(10, rewatchNeeded);
			} else {
				ps.setNull(10, Types.BIT);
			}
			ps.setInt(11, id);

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query editMovie failed \n" + e);
			Notification.show("Faled to edit Movie, sorry...", Type.ERROR_MESSAGE);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		System.out.println("Query editMovie updated " + result + " rows");
	};
	
	@Override
	public void deleteMovie(Integer id) {
		String sql = sqlMapping.getValue("Movie.deleteMovie");
		Connection con = null;
		int result = 0;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query deleteMovie failed \n" + e);
			Notification.show("Faled to delete movie, sorry...", Type.ERROR_MESSAGE);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		System.out.println("Query deleteMovie updated " + result + " rows");
	}
	
	@Override
	public void incrementWatchCount(Integer id) {
	String sql = sqlMapping.getValue("Movie.incrementWatchCount");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			
			ps.setInt(1, id);

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query incrementWatchCount failed \n" + e);
			Notification.show("Faled to edit Movie, sorry...", Type.ERROR_MESSAGE);
			if (con != null) {
				try {
					System.out.println("The transaction is rolled back \n" + e);
					con.rollback();
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				System.out.println("Unable to establish DB connection: " + e.getMessage());
				System.out.println(e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
		System.out.println("Query incrementWatchCount updated " + result + " rows");
	};
	
}
