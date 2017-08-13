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
import java.util.List;

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
						rslt.getString("PORTION"), rslt.getString("COMMENT"), rslt.getTimestamp("LISTENED_DATE") != null ? rslt.getTimestamp("LISTENED_DATE").toLocalDateTime().toLocalDate() : null, rslt.getBoolean("CONCEPT"));

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
		System.out.println("Query getAllListeningSessions returned " + sessions.size() + " rows");
		return sessions;
	}
	
	
	@Override
	public Collection<String> getAllBandsForAlbum(String album) {
		List<String> bands = new ArrayList<String>();
		String sql = sqlMapping.getValue("Music.getAllBandsForAlbum");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, album);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				bands.add(rslt.getString("BAND"));
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllBandsForAlbum failed \n" + e);
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
		System.out.println("Query getAllBandsForAlbum returned " + bands.size() + " rows");
		return bands;
	}
	
	@Override
	public Collection<String> getAllAlbumsForBand(String band) {
		List<String> albums = new ArrayList<String>();
		String sql = sqlMapping.getValue("Music.getAllAlbumsForBand");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, band);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				albums.add(rslt.getString("ALBUM"));
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllAlbumsForBand failed \n" + e);
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
		System.out.println("Query getAllAlbumsForBand returned " + albums.size() + " rows");
		return albums;
	}
	

	@Override
	public void addListeningSession(String band, String album, String portion, String comment, LocalDate date, Boolean concept) {
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
			if (concept != null) {
				ps.setBoolean(6, concept);
			} else {
				ps.setNull(6, Types.BOOLEAN);
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
	
	
	@Override
	public void editListeningSession(String band, String album, String portion, String comment, LocalDate date, Boolean concept, Integer id) {
		String sql = sqlMapping.getValue("Music.editListeningSession");
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
			if (concept != null) {
				ps.setBoolean(6, concept);
			} else {
				ps.setNull(6, Types.BOOLEAN);
			}
			ps.setInt(7,  id);
			
			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query editListeningSession failed \n" + e);
			Notification.show("Faled to edit Listening Session, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query editListeningSession updated " + result + " rows");
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
				bands.add(rslt.getString("BAND"));
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
		System.out.println("Query getAllBands returned " + bands.size() + " rows");
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
		System.out.println("Query getAllAlbums returned " + albums.size() + " rows");
		return albums;
	}

}
