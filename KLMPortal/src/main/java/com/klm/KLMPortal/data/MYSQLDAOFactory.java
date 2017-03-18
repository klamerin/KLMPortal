package com.klm.KLMPortal.data;

import java.sql.Connection;
import java.sql.SQLException;

import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.klm.KLMPortal.data.DAO.IMovieDAO;
import com.klm.KLMPortal.data.DAO.IMusicDAO;
import com.klm.KLMPortal.data.DAOImpl.GeneralInfoDAOImpl;
import com.klm.KLMPortal.data.DAOImpl.MovieDAOImpl;
import com.klm.KLMPortal.data.DAOImpl.MusicDAOImpl;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MYSQLDAOFactory extends DAOFactory {
	
	
	public MYSQLDAOFactory()
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
    		MysqlDataSource mysqlDS = new MysqlDataSource();
    		dataSource = mysqlDS;
//    			mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
//    			mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
//    			mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        	
//            JtdsDataSource dtds = new JtdsDataSource();
//            dataSource = dtds;
    			mysqlDS.setServerName(dataSourceProperties.getValue("serverName"));
    			mysqlDS.setUser(dataSourceProperties.getValue("username"));
            mysqlDS.setPassword(dataSourceProperties.getValue("password"));
            mysqlDS.setDatabaseName(dataSourceProperties.getValue("databaseName"));
            mysqlDS.setPortNumber(Integer.parseInt(dataSourceProperties.getValue("databasePort")));
//            mysqlDS.setDescription(dataSourceProperties.getValue("databaseDescription"));
        }
    }


    public static Connection getConnection()
        throws SQLException
    {
        return dataSource.getConnection();
    }


    @Override
    public IMovieDAO getMovieDAO()
    {
        return new MovieDAOImpl();
    }


	@Override
	public IGeneralInfoDAO getGeneralInfoDAO() {
		return new GeneralInfoDAOImpl();
	}


	@Override
	public IMusicDAO getMusicDAO() {
		return new MusicDAOImpl();
	}
}
