package com.contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import saem.util.DBConn;

public class ContactDAO {
	private Connection conn = DBConn.getConnection();
	
	// 글작성
	public int insertContact(ContactDTO dto) throws SQLException{
		int result = 0 ;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO contact(num, firstName, lastName, league, member, email, comments, reg_date) "
					+ " VALUES(contact_seq.NEXTVAL, ?, ?, ?,?,?,?,SYSDATE) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getFirstName());
			pstmt.setString(2, dto.getLastName());
			pstmt.setString(3, dto.getLeague());
			pstmt.setString(4, dto.getMember());
			pstmt.setString(5, dto.getEmail());
			pstmt.setString(6, dto.getComment());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	// 데이터 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql ;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM contact";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			// select 결과 대입
			if(rs.next()) result = rs.getInt(1);
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	// 검색에서 데이터 개수
	public int dataCount(String condition, String keyword) {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM contact c ";
			
			if(condition.equals("member")){
				sql +=" WHERE INSTR(member, ? ) >=1";
			} else if(condition.equals("league")) {
				sql+=" WHERE INSTR(league,?) >=1";
			} /*else if(condition.equals("checked")) {
				sql+=" 		 WHERE checked = 'n' ";
			}*/
			pstmt = conn.prepareStatement(sql);
			
			if(! condition.equals("checked")) {
				pstmt.setString(1, keyword);
			}

			rs = pstmt.executeQuery();
			if(rs.next()) result = rs.getInt(1);
			
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
		return result;
	}
	
	// 게시물 리스트 (검색)
	public List<ContactDTO> listContact(int start, int end, String condition, String keyword){
		List<ContactDTO> list = new ArrayList<ContactDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("			SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("				SELECT num, firstName, lastName, league, member, email, comments, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append("				  FROM contact");
			if (condition.equals("member")) {
				sb.append("			 WHERE INSTR(member, ?) >=1 ");
			} else if (condition.equals("league")) {
				sb.append("			 WHERE INSTR(league, ?) >=1");
			} /*else if(condition.equals("checked")) {
				sb.append(" 		 WHERE checked = 'n' ");
			}*/
			sb.append("				 ORDER BY num DESC ");
			sb.append("				 ) tb WHERE ROWNUM <= ? ");
			sb.append("			) WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
				pstmt.setString(1, keyword);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);

				rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ContactDTO dto = new ContactDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setFirstName(rs.getString("firstName"));
				dto.setLastName(rs.getString("lastName"));
				dto.setLeague(rs.getString("league"));
				dto.setMember(rs.getString("member"));
				dto.setEmail(rs.getString("email"));
				dto.setComment(rs.getString("comments"));
				dto.setReg_date(rs.getString("reg_date"));
				//dto.setChecked(rs.getString("checked"));
				dto.setFullname(rs.getString("firstName"), rs.getString("lastName"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}		return list;
		
	}
	
	// 게시물 리스트
	public List<ContactDTO> listContact(int start, int end){
		List<ContactDTO> list = new ArrayList<ContactDTO>()	;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT * FROM ( ");
			sb.append("		SELECT ROWNUM rnum, tb.* ");
			sb.append("		  FROM ( ");
			sb.append("			SELECT num, firstName, lastName,league, chekced , member, email, comments, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append("			  FROM contact ");
			sb.append("		  ORDER BY num DESC ) tb ");
			sb.append("		 WHERE ROWNUM <= ? )");
			sb.append("	WHERE rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ContactDTO dto = new ContactDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setFirstName(rs.getString("firstName"));
				dto.setLastName(rs.getString("lastName"));
				dto.setLeague(rs.getString("league"));
				dto.setMember(rs.getString("member"));
				dto.setEmail(rs.getString("email"));
				dto.setComment(rs.getString("comments"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setChecked(rs.getString("chekced"));
				dto.setFullname(rs.getString("firstName"), rs.getString("lastName"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return list;
	}
	
	// 상세 보기
	public ContactDTO readContact(int num) {
		ContactDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, firstName, lastName, league, member, email, comments, reg_date"
				+ "  FROM contact"
				+ " WHERE num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ContactDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setFirstName(rs.getString("firstName"));
				dto.setLastName(rs.getString("lastName"));
				dto.setLeague(rs.getString("league"));
				dto.setMember(rs.getString("member"));
				dto.setEmail(rs.getString("email"));
				dto.setComment(rs.getString("comments"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setFullname(rs.getString("firstName"), rs.getString("lastName"));

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
	
	public int deleteContact(int num) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM contact WHERE num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
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
	
	public int updateChecked(ContactDTO dto) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE contact SET chekced = ? WHERE num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getChecked());
			pstmt.setInt(2, dto.getNum());
			
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
	
}
