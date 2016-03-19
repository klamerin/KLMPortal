package com.klm.KLMPortal.data;

import javax.sql.DataSource;

import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.klm.KLMPortal.data.DAO.IMovieDAO;

public abstract class DAOFactory {

	protected static DataSource dataSource;
	protected static PropertyFile dataSourceProperties;

	protected abstract void initDataSource();

	protected abstract void initDataSourceProperties();

	public static DAOFactory getFactory() {
			return new MSSQLDAOFactory();
	}

	public abstract IMovieDAO getPortalDAO();
	
	public abstract IGeneralInfoDAO getGeneralInfoDAO();

}
