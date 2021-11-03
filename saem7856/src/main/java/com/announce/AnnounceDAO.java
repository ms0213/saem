package com.announce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import saem.util.DBConn;

public class AnnounceDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertAnnounce(AnnounceDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "";
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
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
	
	public List<AnnounceDTO> listAnnounce(int start, int end) {
		List<AnnounceDTO> list = new ArrayList<AnnounceDTO>();
		
		return list;
	}
	
	public List<AnnounceDTO> listAnnounce(int start, int end, String condition, String keyword) {
		List<AnnounceDTO> list = new ArrayList<AnnounceDTO>();
		
		return list;
	}
	
	public List<AnnounceDTO> listAnnounce() {
		List<AnnounceDTO> list = new ArrayList<AnnounceDTO>();
		
		return list;
	}
	
	public AnnounceDTO readAnnounce(int num) {
		AnnounceDTO dto = null;
		
		return dto;
	}
	
	public AnnounceDTO preReadAnnounce(int num, String condition, String keyword) {
		AnnounceDTO dto = null;
		
		return dto;
	}
	
	public AnnounceDTO nextReadAnnounce(int num, String condition, String keyword) {
		AnnounceDTO dto = null;
		
		return dto;
	}
	
	public int updateHitCount(int num) throws SQLException {
		int result = 0;
		
		return result;
	}
	
	public int updateAnnounce(AnnounceDTO dto) throws SQLException {
		int result = 0;
		
		return result;
	}
	
	public int deleteAnnounce(int num) throws SQLException {
		int result = 0;
		
		return result;
	}
	
	public int deleteAnnounceList(int[] nums) throws SQLException {
		int result = 0;
		
		return result;
	}
	
	
}
