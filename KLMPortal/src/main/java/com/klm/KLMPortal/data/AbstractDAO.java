package com.klm.KLMPortal.data;

import java.io.File;

public abstract class AbstractDAO {

	protected String modelName = this.getClass().getSimpleName().replaceFirst("DAOImpl", "");
	protected PropertyFile sqlMapping;

	public void initSQLMapping() {
		try {
			sqlMapping = new PropertyFile(getClass().getResourceAsStream(
					"/" + "mssql" + File.separator + this.getClass().getSimpleName().replaceFirst("DAOImpl", "")
							+ File.separator + IConstant.SQL_MAPPING_PROPERTY_FILE_NAME));
		} catch (Exception e) {
			System.out.println("exception cought!!!" + e);
		}

	}

	public String getModelName() {
		return modelName;
	}
}
