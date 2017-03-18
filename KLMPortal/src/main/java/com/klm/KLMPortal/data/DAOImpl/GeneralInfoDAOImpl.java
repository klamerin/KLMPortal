package com.klm.KLMPortal.data.DAOImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import com.klm.KLMPortal.beans.EventBean;
import com.klm.KLMPortal.beans.InfoBean;
import com.klm.KLMPortal.data.AbstractDAO;
import com.klm.KLMPortal.data.MSSQLDAOFactory;
import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class GeneralInfoDAOImpl extends AbstractDAO implements IGeneralInfoDAO {

	public GeneralInfoDAOImpl() {
		initSQLMapping();
	}

	@Override
	public ArrayList<InfoBean> getAllInfos() {
		ArrayList<InfoBean> infos = new ArrayList<InfoBean>();
		String sql = sqlMapping.getValue("GeneralInfo.getAllInfos");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				InfoBean info = new InfoBean(rslt.getInt("ID"), rslt.getString("INFO_KEY"), rslt.getString("INFO_VALUE"), rslt.getString("COMMENT"));
				infos.add(info);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllInfos failed \n" + e);
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
		return infos;
	}

	@Override
	public void addNewInfo(String infoKey, String infoValue, String comment) {
		String sql = sqlMapping.getValue("GeneralInfo.addNewInfo");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, infoKey);
			if (infoValue == null) {
				ps.setNull(2, Types.VARCHAR);
			} else {
				ps.setString(2, infoValue);
			}
			if (comment == null) {
				ps.setNull(3, Types.VARCHAR);
			} else {
				ps.setString(3, comment);
			}

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query addNewInfo failed \n" + e);
			Notification.show("Faled to add New Info, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query addNewInfo updated " + result + " rows");

	}

	@Override
	public void setInfoData(Integer infoId, String infoValue, String comment) {
		String sql = sqlMapping.getValue("GeneralInfo.setInfoData");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			if (infoValue == null) {
				ps.setNull(1, Types.VARCHAR);
			} else {
				ps.setString(1, infoValue);
			}
			if (comment == null) {
				ps.setNull(2, Types.VARCHAR);
			} else {
				ps.setString(2, comment);
			}
			ps.setInt(3, infoId);

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query setInfoValue failed \n" + e);
			Notification.show("Faled to set Info data, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query setInfoData updated " + result + " rows");

	}
	
	@Override
	public void deleteInfo(Integer infoId) {
		String sql = sqlMapping.getValue("GeneralInfo.deleteInfo");
		Connection con = null;
		int result = 0;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, infoId);

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query deleteInfo failed \n" + e);
			Notification.show("Faled to delete Info, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query deleteInfo updated " + result + " rows");
	}

	@Override
	public ArrayList<EventBean> getAllEvents() {
		ArrayList<EventBean> events = new ArrayList<EventBean>();
		String sql = sqlMapping.getValue("GeneralInfo.getAllEventsByType");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, "%");
			
			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				EventBean event = new EventBean(rslt.getInt("ID"), rslt.getString("EVENT_NAME"), rslt.getDate("EVENT_SET_DATE"), rslt.getDate("EVENT_ETA_DATE"), rslt.getString("COMMENT"));
				events.add(event);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllEventsByType failed \n" + e);
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
		return events;
	}
	
	
	@Override
	public ArrayList<EventBean> getPostEventsNotReceived() {
		ArrayList<EventBean> events = new ArrayList<EventBean>();
		String sql = sqlMapping.getValue("GeneralInfo.getPostEventsNotReceived");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				EventBean event = new EventBean(rslt.getInt("ID"), rslt.getString("EVENT_NAME"), rslt.getDate("EVENT_SET_DATE"), rslt.getDate("EVENT_ETA_DATE"), rslt.getString("COMMENT"));
				events.add(event);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getPostEventsNotReceived failed \n" + e);
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
		return events;
	}
	
	
	@Override
	public ArrayList<EventBean> getAllEventsByType(String eventType) {
		ArrayList<EventBean> events = new ArrayList<EventBean>();
		String sql = sqlMapping.getValue("GeneralInfo.getAllEventsByType");
		Connection con = null;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println("eventType at getAllEventsByType:" + eventType);
			ps.setString(1, eventType + "%");

			ResultSet rslt = ps.executeQuery();
			con.commit();
			while (rslt.next()) {
				EventBean event = new EventBean(rslt.getInt("ID"), rslt.getString("EVENT_NAME").replace(eventType, ""), rslt.getDate("EVENT_SET_DATE"), rslt.getDate("EVENT_ETA_DATE"), rslt.getString("COMMENT"));
				events.add(event);
			}
			rslt.close();
		} catch (SQLException e) {
			System.out.println("Query getAllPostEvents failed \n" + e);
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
		return events;
	}

	@Override
	public void addNewEvent(String eventName, Date eventSetDate, Date eventETADate, String comment, String eventType) {
		String sql = sqlMapping.getValue("GeneralInfo.addNewEvent");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, eventType + eventName);
			if (eventETADate == null) {
				ps.setNull(2, Types.DATE);
			} else {
				ps.setDate(2, new java.sql.Date(eventETADate.getTime()));
			}
			if (comment == null) {
				ps.setNull(3, Types.VARCHAR);
			} else {
				ps.setString(3, comment);
			}
			if (eventSetDate == null) {
				ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
			} else {
				ps.setDate(4, new java.sql.Date(eventSetDate.getTime()));
			}

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query addNewEvent failed \n" + e);
			Notification.show("Faled to add New Event, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query addNewEvent updated " + result + " rows");

	}

	@Override
	public void setEventETADate(Integer eventId, Date eventETADate) {
		String sql = sqlMapping.getValue("GeneralInfo.setEventETADate");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			if (eventETADate == null) {
				ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));
			} else {
				ps.setDate(1, new java.sql.Date(eventETADate.getTime()));
			}
			ps.setInt(2, eventId);
			
			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query setEventETADate failed \n" + e);
			Notification.show("Faled to set Event ETA date, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query setEventETADate updated " + result + " rows");

	}

	@Override
	public void setEventSetDate(Integer eventId, Date eventSetDate) {
		String sql = sqlMapping.getValue("GeneralInfo.setEventSetDate");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			if (eventSetDate == null) {
				ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));
			} else {
				ps.setDate(1, new java.sql.Date(eventSetDate.getTime()));
			}
			ps.setInt(2, eventId);

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query setEventSetDate failed \n" + e);
			Notification.show("Faled to set Event Set Date, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query setEventSetDate updated " + result + " rows");

	}
	
	@Override
	public void setEventComment(Integer eventId, String comment) {
		String sql = sqlMapping.getValue("GeneralInfo.setEventComment");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			if (comment == null) {
				ps.setNull(1, Types.VARCHAR);
			} else {
				ps.setString(1, comment);
			}
			ps.setInt(2, eventId);
			
			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query setEventComment failed \n" + e);
			Notification.show("Faled to set Event comment, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query setEventComment updated " + result + " rows");

	}

	
	public void setEventData(Integer eventId, String eventName, Date eventSetDate, Date eventETADate, String comment, String eventType) {
//		GeneralInfo.setEventData = UPDATE GENERAL_EVENTS SET EVENT_NAME = ?, EVENT_SET_DATE = ?, EVENT_ETA_DATE = ?, SET COMMENT = ? WHERE ID = ?
		String sql = sqlMapping.getValue("GeneralInfo.setEventData");
		Connection con = null;
		int result = 0;
		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, eventType + eventName);
			if (eventSetDate == null) {
				ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
			} else {
				ps.setDate(2, new java.sql.Date(eventSetDate.getTime()));
			}
			if (eventETADate == null) {
				ps.setNull(3, Types.DATE);
			} else {
				ps.setDate(3, new java.sql.Date(eventETADate.getTime()));
			}
			if (comment == null) {
				ps.setNull(4, Types.VARCHAR);
			} else {
				ps.setString(4, comment);
			}
			ps.setInt(5, eventId);
			
			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query setEventComment failed \n" + e);
			Notification.show("Faled to set Event comment, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query setEventComment updated " + result + " rows");

	}
	
	@Override
	public void deleteEvent(Integer eventId) {
		String sql = sqlMapping.getValue("GeneralInfo.deleteEvent");
		Connection con = null;
		int result = 0;

		try {
			con = MSSQLDAOFactory.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, eventId);

			result = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Query deleteEvent failed \n" + e);
			Notification.show("Faled to delete event, sorry...", Type.ERROR_MESSAGE);
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
		System.out.println("Query deleteEvent updated " + result + " rows");
	}

}
