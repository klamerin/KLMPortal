package com.klm.KLMPortal.data;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import com.klm.KLMPortal.data.DAO.GeneralInfoDAOImpl;
import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.klm.KLMPortal.data.DAO.IMovieDAO;
import com.klm.KLMPortal.data.DAO.MovieDAOImpl;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;

public class MSSQLDAOFactory extends DAOFactory
{

    public MSSQLDAOFactory()
    {
        initDataSourceProperties();
        initDataSource();
    }


    @Override
    protected void initDataSourceProperties()
    {
        if (dataSourceProperties == null)
        {
        	dataSourceProperties = new PropertyFile(getClass().getResourceAsStream("/"
                                                                                   + IConstant.DATASOURCE_PROPERTY_FILE_NAME));
        }
    }


    @Override
    protected void initDataSource()
    {
        if (dataSource == null)
        {
            JtdsDataSource dtds = new JtdsDataSource();
            dataSource = dtds;
            dtds.setServerName(dataSourceProperties.getValue("serverName"));
            dtds.setUser(dataSourceProperties.getValue("username"));
            dtds.setPassword(dataSourceProperties.getValue("password"));
            dtds.setDatabaseName(dataSourceProperties.getValue("databaseName"));
            dtds.setPortNumber(Integer.parseInt(dataSourceProperties.getValue("databasePort")));
            dtds.setDescription(dataSourceProperties.getValue("databaseDescription"));
        }
    }


    public static Connection getConnection()
        throws SQLException
    {
        return dataSource.getConnection();
    }


    @Override
    public IMovieDAO getPortalDAO()
    {
        return new MovieDAOImpl();
    }


	@Override
	public IGeneralInfoDAO getGeneralInfoDAO() {
		return new GeneralInfoDAOImpl();
	}
}

