package com.fixture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import saem.util.DBConn;

public class FixtureDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertFixture(FixtureDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO fixture(num, subject, color, sday, eday, "
					+ " stime, etime, memo) "
					+ " VALUES(fixture_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getColor());
			pstmt.setString(3, dto.getSday());
			pstmt.setString(4, dto.getEday());
			pstmt.setString(5, dto.getStime());
			pstmt.setString(6, dto.getEtime());
			pstmt.setString(7, dto.getMemo());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	public List<FixtureDTO> listMonth(String startDay, String endDay) {
		List<FixtureDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT num, subject, sday, eday, stime, etime, ");
			sb.append("               color");
			sb.append("  FROM fixture");
			sb.append("  WHERE ");
			sb.append("  ( ");
			sb.append("      ( ");
			sb.append("         ( TO_DATE(sday, 'YYYYMMDD') >= TO_DATE(?, 'YYYYMMDD') ");
			sb.append("             AND TO_DATE(sday, 'YYYYMMDD') <= TO_DATE(?, 'YYYYMMDD')  ");
			sb.append("          )  OR ( TO_DATE(eday, 'YYYYMMDD') <= TO_DATE(?, 'YYYYMMDD')  ");
			sb.append("             AND TO_DATE(eday, 'YYYYMMDD') <= TO_DATE(?, 'YYYYMMDD')  ");
			sb.append("          )");
			sb.append("       ) ");
			sb.append("  ) ");
			sb.append("  ORDER BY sday ASC, num DESC ");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, startDay);
			pstmt.setString(2, endDay);
			pstmt.setString(3, startDay);
			pstmt.setString(4, endDay);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				FixtureDTO dto = new FixtureDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setSday(rs.getString("sday"));
				dto.setEday(rs.getString("eday"));
				dto.setStime(rs.getString("stime"));
				dto.setEtime(rs.getString("etime"));
				dto.setColor(rs.getString("color"));
				

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		return list;
	}
	
	public List<FixtureDTO> listDay(String date) {
		List<FixtureDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT num, subject, sday, eday, color ");
			sb.append("  FROM fixture");
			sb.append("  WHERE ");
			sb.append("  ( ");
			sb.append("      ( ");
			sb.append("         TO_DATE(sday, 'YYYYMMDD') = TO_DATE(?, 'YYYYMMDD') ");
			sb.append("         OR (eday IS NOT NULL AND TO_DATE(sday, 'YYYYMMDD') <= TO_DATE(?, 'YYYYMMDD') AND TO_DATE(eday, 'YYYYMMDD') >= TO_DATE(?, 'YYYYMMDD')) ");
			sb.append("      ) "); 
			sb.append("  ) ");
			sb.append("  ORDER BY num DESC ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, date);
			pstmt.setString(2, date);
			pstmt.setString(3, date);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				FixtureDTO dto = new FixtureDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setSday(rs.getString("sday"));
				dto.setEday(rs.getString("eday"));
				dto.setColor(rs.getString("color"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
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
		return list;
	}
	
	public FixtureDTO readFixture(int num) {
		FixtureDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT num, subject, sday, eday, stime, etime, "
				+ "      color, memo "
				+ "  FROM fixture"
				+ "  WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);

			String period, s;
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new FixtureDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setSday(rs.getString("sday"));
				s = dto.getSday().substring(0, 4) + "-" + dto.getSday().substring(4, 6) + "-"
						+ dto.getSday().substring(6);
				dto.setSday(s);
				dto.setEday(rs.getString("eday"));
				if (dto.getEday() != null && dto.getEday().length() == 8) {
					s = dto.getEday().substring(0, 4) + "-" + dto.getEday().substring(4, 6) + "-"
							+ dto.getEday().substring(6);
					dto.setEday(s);
				}
				dto.setStime(rs.getString("stime"));
				if (dto.getStime() != null && dto.getStime().length() == 4) {
					s = dto.getStime().substring(0, 2) + ":" + dto.getStime().substring(2);
					dto.setStime(s);
				}
				dto.setEtime(rs.getString("etime"));
				if (dto.getEtime() != null && dto.getEtime().length() == 4) {
					s = dto.getEtime().substring(0, 2) + ":" + dto.getEtime().substring(2);
					dto.setEtime(s);
				}

				period = dto.getSday();
				if (dto.getStime() != null && dto.getStime().length() != 0) {
					period += " " + dto.getStime();
				}
				if (dto.getEday() != null && dto.getEday().length() != 0) {
					period += " ~ " + dto.getEday();
				}
				if (dto.getEtime() != null && dto.getEtime().length() != 0) {
					period += " " + dto.getEtime();
				}
				dto.setPeriod(period);

				dto.setColor(rs.getString("color"));
				dto.setMemo(rs.getString("memo"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
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
		return dto;
	}
	
	public int updateFixture(FixtureDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("UPDATE fixture SET ");
			sb.append("  subject=?, color=?, sday=?, eday=?, stime=?, ");
			sb.append("  etime=?, memo=? ");
			sb.append("  WHERE num=? ");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getColor());
			pstmt.setString(3, dto.getSday());
			pstmt.setString(4, dto.getEday());
			pstmt.setString(5, dto.getStime());
			pstmt.setString(6, dto.getEtime());
			pstmt.setString(7, dto.getMemo());
			pstmt.setInt(8, dto.getNum());
			

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	public int deleteFixture(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM fixture WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
}
