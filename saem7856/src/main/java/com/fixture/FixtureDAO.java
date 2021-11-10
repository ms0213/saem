package com.fixture;

import java.sql.Connection;
import java.sql.SQLException;

import saem.util.DBConn;

public class FixtureDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertFixture(FixtureDTO dto) throws SQLException {
		int result = 0;
		
		return result;
	}
	
	public int dataCount() {
		int result = 0;
		
		return result;
	}
	
	public int dataCount(String condition, String keyword) {
		int result = 0;
		
		return result;
	}
}
