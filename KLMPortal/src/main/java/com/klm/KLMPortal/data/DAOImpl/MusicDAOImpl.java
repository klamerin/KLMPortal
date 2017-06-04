package com.klm.KLMPortal.data.DAOImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import com.klm.KLMPortal.beans.MusicBean;
import com.klm.KLMPortal.data.AbstractDAO;
import com.klm.KLMPortal.data.MSSQLDAOFactory;
import com.klm.KLMPortal.data.DAO.IMusicDAO;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class MusicDAOImpl extends AbstractDAO implements IMusicDAO {

	public MusicDAOImpl() {
		initSQLMapping();
	}

	@Override
	public ArrayList<MusicBean> getAllListeningSessions() {
		ArrayList<MusicBean> sessions = new ArrayList<MusicBean>();
		String sql = sqlMapping.getValue("Music.getAllListeningSessions");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			// ps.setLong(1, businessUnitId);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				MusicBean session = new MusicBean(rslt.getInt("ID"), rslt.getString("BAND"), rslt.getString("ALBUM"),
						rslt.getString("PORTION"), rslt.getString("COMMENT"), rslt.getTimestamp("LISTENED_DATE") != null ? rslt.getTimestamp("LISTENED_DATE").toLocalDateTime().toLocalDate() : null);

				sessions.add(session);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllListeningSessions failed \n" + e);
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
		return sessions;
	}

	@Override
	public void addListeningSession(String band, String album, String portion, String comment, LocalDate date) {
		String sql = sqlMapping.getValue("Music.addListeningSession");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			java.sql.PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, band);
			ps.setString(2, album);
			if (portion != null) {
				ps.setString(3, portion);
			} else {
				ps.setNull(3, Types.VARCHAR);
			}
			if (comment != null) {
				ps.setString(4, comment);
			} else {
				ps.setNull(4, Types.VARCHAR);
			}
			if (date != null) {
				ps.setTimestamp(5, Timestamp.valueOf(date.atStartOfDay()));
			} else {
				ps.setNull(5, Types.TIMESTAMP);
			}
			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query addListeningSession failed \n" + e);
			Notification.show("Faled to add Listening Session, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query addListeningSession updated " + result + " rows");
	};

	public Collection<String> getAllBands() {
		Collection<String> bands = new ArrayList<String>();
		String sql = sqlMapping.getValue("Music.getAllBands");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			// ps.setLong(1, businessUnitId);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				bands.add(rslt.getString("NAME"));
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllBands failed \n" + e);
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
		return bands;
	}

	public Collection<String> getAllAlbums() {
		Collection<String> albums = new ArrayList<String>();
		String sql = sqlMapping.getValue("Music.getAllAlbums");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			// ps.setLong(1, businessUnitId);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				albums.add(rslt.getString("ALBUM"));
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllAlbums failed \n" + e);
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
		return albums;
	}

	// @Override
	// public void editMovie(String name, Boolean watched, Double rating, String
	// comment, Date date, Boolean recommend, String watchedBecause, Double
	// sadnessLevel, Integer desireLevel, Boolean rewatchNeeded, Integer id) {
	// String sql = sqlMapping.getValue("Movie.editMovie");
	// Connection con = null;
	// int result = 0;
	// try {
	// con = MSSQLDAOFactory.getConnection();
	// con.setAutoCommit(false);
	//
	// PreparedStatement ps = con.prepareStatement(sql);
	//
	// if (comment != null) {
	// ps.setString(1, comment);
	// } else {
	// ps.setNull(1, Types.VARCHAR);
	// }
	// if (rating != null) {
	// ps.setDouble(2, rating);
	// } else {
	// ps.setNull(2, Types.FLOAT);
	// }
	// if (recommend != null) {
	// ps.setBoolean(3, recommend);
	// } else {
	// ps.setNull(3, Types.BIT);
	// }
	// ps.setBoolean(4, watched);
	// if (date != null) {
	// ps.setDate(5, date);
	// } else {
	// ps.setNull(5, Types.TIMESTAMP);
	// }
	// ps.setString(6, name);
	// if (watchedBecause != null) {
	// ps.setString(7, watchedBecause);
	// } else {
	// ps.setNull(7, Types.VARCHAR);
	// }
	// if (sadnessLevel != null) {
	// ps.setDouble(8, sadnessLevel);
	// } else {
	// ps.setNull(8, Types.DOUBLE);
	// }
	// if (desireLevel != null) {
	// ps.setInt(9, desireLevel);
	// } else {
	// ps.setNull(9, Types.INTEGER);
	// }
	// if (rewatchNeeded != null) {
	// ps.setBoolean(10, rewatchNeeded);
	// } else {
	// ps.setNull(10, Types.BIT);
	// }
	// ps.setInt(11, id);
	//
	// result = ps.executeUpdate();
	// con.commit();
	// } catch (SQLException e) {
	// System.out.println("Query editMovie failed \n" + e);
	// Notification.show("Faled to edit Movie, sorry...", Type.ERROR_MESSAGE);
	// if (con != null) {
	// try {
	// System.out.println("The transaction is rolled back \n" + e);
	// con.rollback();
	// con.close();
	// } catch (SQLException ex) {
	// System.out.println(ex);
	// }
	// } else {
	// System.out.println("Unable to establish DB connection: " +
	// e.getMessage());
	// System.out.println(e);
	// }
	// } finally {
	// if (con != null) {
	// try {
	// con.close();
	// } catch (SQLException e) {
	// System.out.println(e);
	// }
	// }
	// }
	// System.out.println("Query editMovie updated " + result + " rows");
	// };
	//
	// @Override
	// public void deleteMovie(Integer id) {
	// String sql = sqlMapping.getValue("Movie.deleteMovie");
	// Connection con = null;
	// int result = 0;
	//
	// try {
	// con = MSSQLDAOFactory.getConnection();
	// con.setAutoCommit(false);
	//
	// PreparedStatement ps = con.prepareStatement(sql);
	//
	// ps.setInt(1, id);
	//
	// result = ps.executeUpdate();
	// con.commit();
	// } catch (SQLException e) {
	// System.out.println("Query deleteMovie failed \n" + e);
	// Notification.show("Faled to delete movie, sorry...", Type.ERROR_MESSAGE);
	// if (con != null) {
	// try {
	// System.out.println("The transaction is rolled back \n" + e);
	// con.rollback();
	// con.close();
	// } catch (SQLException ex) {
	// System.out.println(ex);
	// }
	// } else {
	// System.out.println("Unable to establish DB connection: " +
	// e.getMessage());
	// System.out.println(e);
	// }
	// } finally {
	// if (con != null) {
	// try {
	// con.close();
	// } catch (SQLException e) {
	// System.out.println(e);
	// }
	// }
	// }
	// System.out.println("Query deleteMovie updated " + result + " rows");
	// }
	//
}
