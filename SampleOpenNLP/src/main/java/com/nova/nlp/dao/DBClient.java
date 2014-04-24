package com.nova.nlp.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DBClient {
	private static final Logger LOG = Logger.getLogger(DBClient.class.getName());
	private static final SearchDatasource datasource = new SearchDatasource();
	public DBClient() {
	}
	public List<Object[]> getData(String searchStr) {
		List<Object[]> data = new ArrayList<Object[]>();
		String sql = "SELECT * FROM MESSAGE LIMIT 10";
		if(searchStr != null) {
			sql = "SELECT * FROM MESSAGE WHERE "+searchStr+" LIMIT 10";
			LOG.info(sql);
		}
		Statement stmt;
		ResultSet rs;
		Connection c = null;
		try {
            c = datasource.getDataSource().getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
            	Object[] o = new Object[6];
            	o[0] = rs.getString(1)==null?"":rs.getString(1);
            	o[1] = rs.getString(2)==null?"":rs.getString(2);
            	o[2] = rs.getString(3)==null?"":rs.getString(3);
            	o[3] = rs.getString(4)==null?"":rs.getString(4);
            	o[4] = rs.getString(5)==null?"":rs.getString(5);
            	o[5] = rs.getString(6)==null?"":rs.getString(6);
            	data.add(o);
            }
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}

}
