package com.article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import saem.util.DBConn;

public class ArticleDAO {
	private Connection conn = DBConn.getConnection();
	
	public int writeArticle(ArticleDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO news(num, subject, content, link, reg_date, hitCount, imageFilename) "
					+ " VALUES (news_seq.NEXTVAL, ?, ?, ?, SYSDATE, 0, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getLink());
			pstmt.setString(4, dto.getimageFilename());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return result;
	}
	
	public int dataCount() { // 전체 데이터 개수
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM news";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return result;
	}
	
	// 검색해서 데이터 개수
	public int DataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM news ";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR (content, ?) >= 1 ";
			} else if (condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(reg_date, 'YYYYMMDD') =? ";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?) >=1 ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
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
	
	public List<ArticleDTO> listArticle(int start, int end)  {
		List<ArticleDTO> list = new ArrayList<ArticleDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT num, subject, content, link, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append(" 			hitCount, imageFilename ");
			sb.append("			FROM news");
			sb.append("			ORDER BY num DESC ");
			sb.append("		) tb WHERE ROWNUM <= ? ");
			sb.append("	) WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ArticleDTO dto = new ArticleDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setLink(rs.getString("link"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getString("hitCount"));
				dto.setimageFilename(rs.getString("imageFilename"));
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
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	
	// 게시물 검색 시 리스트
	public List<ArticleDTO> listArticle(int start, int end, String condition, String keyword) {
		List<ArticleDTO> list = new ArrayList<ArticleDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT num, subject, content, link, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append(" 			hitCount, imageFilename ");
			sb.append("			FROM news");
			if(condition.equals("all")) {
				sb.append("	WHERE INSTR(subject, ?) >= 1 OR INSTR(content,?) >= 1  ");
			} else if ( condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append("	WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append("	WHERE INSTR(" + condition + ", ?) >= 1 ");
			}
			sb.append("			ORDER BY num DESC ");
			sb.append("		) tb WHERE ROWNUM <= ? ");
			sb.append("	) WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if(condition.equals("all")) {
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
			while(rs.next()) {
				ArticleDTO dto = new ArticleDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setLink(rs.getString("link"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getString("hitCount"));
				dto.setimageFilename(rs.getString("imageFilename"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if ( rs != null ) {
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
	
	public ArticleDTO readArticle(int num) {
		ArticleDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT b.num, subject, content, reg_date, hitCount, imageFilename, link, "
					+ " 	NVL(articleLikeCount, 0) articleLikeCount "
					+ " FROM news b"
					+ " LEFT OUTER JOIN ("
					+ " 	SELECT num, COUNT(*) articleLikeCount FROM newsLike"
					+ "		GROUP BY num"
					+ " ) bc ON b.num = bc.num"
					+ " WHERE b.num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				dto = new ArticleDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getString("hitCount"));
				dto.setimageFilename(rs.getString("imageFilename"));
				dto.setLink(rs.getString("link"));
				
				dto.setArticleLikeCount(rs.getInt("articleLikeCount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return dto;
	}
	
	public ArticleDTO preReadArticle(int num, String condition, String keyword) {
		ArticleDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" SELECT * FROM ( ");
				sb.append("		SELECT num, subject FROM news ");
				sb.append("		WHERE ( num > ? ) ");
				if (condition.equals("all")) {
					sb.append("		AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + condition + ", ?) >= 1 ) ");
				}
				sb.append("     ORDER BY num ASC ");
				sb.append(" ) WHERE ROWNUM = 1 ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, num);
				pstmt.setString(2, keyword);
				if (condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
			} else {
				sb.append(" SELECT * FROM ( ");
				sb.append("		SELECT num, subject FROM news ");
				sb.append("		WHERE num > ? ");
				sb.append("		ORDER BY num ASC ");
				sb.append("	)	WHERE rownum = 1");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ArticleDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
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
	
	public ArticleDTO nextReadArticle(int num, String condition, String keyword) {
		ArticleDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(keyword != null && keyword.length()!=0) {
				sb.append(" SELECT * FROM ( ");
				sb.append("		SELECT num, subject FROM news ");
				sb.append("		WHERE ( num < ? ) ");
				if(condition.equals("all")) {
					sb.append(" AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + condition + ", ?) >= 1 ) ");
				}
				sb.append("     ORDER BY num DESC ");
				sb.append(" ) WHERE ROWNUM = 1 ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, num);
				pstmt.setString(2, keyword);
				if(condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
			} else {
				
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT num, subject FROM news ");
			sb.append(" 	WHERE num < ? ");
			sb.append(" 	ORDER BY num DESC ");
			sb.append(" ) WHERE ROWNUM = 1 ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ArticleDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if( rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return dto;
	}
	
	public int updateHitCount(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE news SET hitCount=hitCount+1 WHERE num=?";
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
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public int updateArticle(ArticleDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE news SET subject=?, content=?, imageFilename=?, link=? WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getimageFilename());
			pstmt.setString(4, dto.getLink());
			pstmt.setInt(5, dto.getNum());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
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
	
	// 게시글 삭제하기
	public int deleteArticle(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM news WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	// 기사 좋아요 추가
	public int insertArticleLike(int num, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO newsLike(num, userId) VALUES (?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
			
			result = pstmt.executeUpdate();
			
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
	
	// 기사 좋아요 삭제
	public int deleteArticleLike(int num, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM newsLike WHERE num = ? AND userId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}

	// 기사 좋아요 개수
	public int countArticleLike(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM newsLike WHERE num =?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	// 기사 좋아요 유무
	public boolean isUserArticleLike(int num, String userId) {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, userId FROM newsLike WHERE num = ? AND userId = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
}
