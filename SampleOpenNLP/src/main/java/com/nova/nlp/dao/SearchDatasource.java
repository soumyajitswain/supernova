package com.nova.nlp.dao;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

public class SearchDatasource {
	private final DataSource dataSource;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public SearchDatasource() {
		dataSource = setupDataSource("jdbc:mysql://172.16.1.59:5905/callistotd", "callistodev","mysql12345678");
	}
	private DataSource setupDataSource(String connectURI, String uName, String pwd) {
		ConnectionFactory connectionFactory =
				new DriverManagerConnectionFactory(connectURI,uName,pwd);

		PoolableConnectionFactory poolableConnectionFactory =
				new PoolableConnectionFactory(
						connectionFactory, new GenericObjectPool(), null, null, false, false);

		ObjectPool connectionPool =
				new GenericObjectPool(poolableConnectionFactory);

		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

		return dataSource;
	}
	protected DataSource getDataSource() {
		return dataSource;
	}

}
