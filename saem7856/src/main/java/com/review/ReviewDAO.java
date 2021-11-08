package com.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import saem.util.DBConn;

public class ReviewDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertReview(ReviewDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "insert into review(num, userId, subject, content, hitCount, reg_date, gdsNum) "
					+ " values (bbs_seq.nextval, ?, ?, ?, 0, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, dto.getGdsNum());

			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles() != null) {
				sql = "insert into revPhotoFile(fileNum, num, imageFilename) values "
						+ " (revPhotoFile_seq.nextval, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				for(int i=0; i<dto.getImageFiles().length; i++) {
					pstmt.setInt(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
		}

		return result;
	}

	// 데이터 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "select nvl(count(*), 0) from review";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);

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

	// 검색에서의 데이터 개수
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "select nvl(count(*), 0) from review r join member1 m on r.userId = m.userId ";
			if (condition.equals("all")) {
				sql += "  where instr(subject, ?) >= 1 or instr(content, ?) >= 1 ";
			} else if (condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  where to_char(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += "  where instr(" + condition + ", ?) >= 1 ";
			}

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			if (condition.equals("all")) {
				pstmt.setString(2, keyword);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
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

		return result;
	}

	// 게시물 리스트
	public List<ReviewDTO> listReview(int start, int end) {
		List<ReviewDTO> list = new ArrayList<ReviewDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" select * from ( ");
			sb.append("     select rownum rnum, tb.* from ( ");
			sb.append("         select num, userName, subject, hitCount, ");
			sb.append("               to_char(reg_date, 'YYYY-MM-DD') reg_date, gdsNum ");
			sb.append("         from review r ");
			sb.append("         join member1 m on r.userId = m.userId ");
			sb.append("         order by num desc ");
			sb.append("     ) tb where rownum <= ? ");
			sb.append(" ) where rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReviewDTO dto = new ReviewDTO();

				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setGdsNum(rs.getInt("gdsNum"));

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

	public List<ReviewDTO> listReview(int start, int end, String condition, String keyword) {
		List<ReviewDTO> list = new ArrayList<ReviewDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" select * from ( ");
			sb.append("     select rownum rnum, tb.* from ( ");
			sb.append("         select num, userName, subject, hitCount, ");
			sb.append("               to_char(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append("         from review r ");
			sb.append("         join member1 m on r.userId = m.userId ");
			if (condition.equals("all")) {
				sb.append("     where instr(subject, ?) >= 1 or instr(content, ?) >= 1 ");
			} else if (condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append("     where to_char(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append("     where instr(" + condition + ", ?) >= 1 ");
			}
			sb.append("         order by num desc ");
			sb.append("     ) tb where rownum <= ? ");
			sb.append(" ) where rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			
			if (condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, end);
				pstmt.setInt(4, start);
			} else {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReviewDTO dto = new ReviewDTO();

				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setGdsNum(rs.getInt("gdsNum"));

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

	// 조회수 증가하기
	public int updateHitCount(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "update review set hitCount=hitCount+1 where num=?";
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
				} catch (SQLException e2) {
				}
			}
		}

		return result;
	}

	// 해당 게시물 보기
	public ReviewDTO readReview(int num) {
		ReviewDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "select num, r.userId, userName, subject, content, reg_date, hitCount "
					+ " FROM review r "
					+ " join member1 m on r.userId=m.userId "
					+ " WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReviewDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
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
	
	public int updateReview(ReviewDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update review set subject=?, content=? where num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getNum());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles() != null) {
				sql = "insert into revPhotoFile(fileNum, num, imageFilename) values "
						+ " (revPhotoFile_seq.nextval, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				for(int i=0; i<dto.getImageFiles().length; i++) {
					pstmt.setInt(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return result;
	}
	
	public List<ReviewDTO> listPhotoFile(int num) {
		List<ReviewDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select fileNum, num, imageFilename from revPhotoFile where num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto = new ReviewDTO();
				dto.setFileNum(rs.getInt("fileNum"));
				dto.setNum(rs.getInt("num"));
				dto.setImageFilename(rs.getString("imageFilename"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return list;
	}
	
	public ReviewDTO readPhotoFile(int fileNum) {
		ReviewDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select fileNum, num, imageFilename from revPhotoFile where fileNum = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, fileNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ReviewDTO();
				
				dto.setFileNum(rs.getInt("fileNum"));
				dto.setNum(rs.getInt("num"));
				dto.setImageFilename(rs.getString("imageFilename"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return dto;
	}

	// 이전글
	public ReviewDTO preReadBoard(int num, String condition, String keyword) {
		ReviewDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" select * from ( ");
				sb.append("    select num, subject ");
				sb.append("    from review r ");
				sb.append("    join member1 m on r.userId = m.userId ");
				sb.append("    where ( num > ? ) ");
				if (condition.equals("all")) {
					sb.append("   and ( instr(subject, ?) >= 1 or instr(content, ?) >= 1 ) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   and ( to_char(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   and ( instr(" + condition + ", ?) >= 1 ) ");
				}
				sb.append("     order by num asc ");
				sb.append(" ) where rownum = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, num);
				pstmt.setString(2, keyword);
				if (condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
			} else {
				sb.append(" select * from ( ");
				sb.append("     select num, subject from review ");
				sb.append("     where num > ? ");
				sb.append("     order by num asc ");
				sb.append(" ) where rownum = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReviewDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
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

	// 다음글
	public ReviewDTO nextReadBoard(int num, String condition, String keyword) {
		ReviewDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" select * from ( ");
				sb.append("    select num, subject ");
				sb.append("    from review r ");
				sb.append("    join member1 m on r.userId = m.userId ");
				sb.append("    where ( num < ? ) ");
				if (condition.equals("all")) {
					sb.append("   and ( instr(subject, ?) >= 1 or instr(content, ?) >= 1 ) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   and ( to_char(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   and ( instr(" + condition + ", ?) >= 1 ) ");
				}
				sb.append("     order by num desc ");
				sb.append(" ) where rownum = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, num);
				pstmt.setString(2, keyword);
				if (condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
			} else {
				sb.append(" select * from ( ");
				sb.append("     select num, subject from review ");
				sb.append("     where num < ? ");
				sb.append("     order by num desc ");
				sb.append(" ) where rownum = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReviewDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
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

	// 게시물 삭제
	public int deleteReview(int num, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (userId.equals("admin")) {
				sql = "delete from review where num=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				
				result = pstmt.executeUpdate();
			} else {
				sql = "delete from review where num=? and userId=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				pstmt.setString(2, userId);
				
				result = pstmt.executeUpdate();
			}
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
	
	public int deletePhotoFile(String mode, int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql = "delete from revPhotoFile where num = ?";
			} else {
				sql = "delete from revPhotoFile where fileNum = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				
				result = pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return result;
	}
}
