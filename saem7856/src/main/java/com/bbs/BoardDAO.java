package com.bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saem.util.DBConn;

public class BoardDAO {
	private Connection conn = DBConn.getConnection();

	// 데이터 추가
	public int insertBoard(BoardDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;

		try {
			sql = "SELECT bbs_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			seq = 0;
			if (rs.next()) {
				seq = rs.getInt(1);
			}
			dto.setNum(seq);

			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;

			sql = "INSERT INTO bbs(num, notice, userId, subject, content, hitCount, reg_date) "
					+ "  VALUES (?, ?, ?, ?, ?, 0, SYSDATE)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNum());
			pstmt.setInt(2, dto.getNotice());
			pstmt.setString(3, dto.getUserId());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());

			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

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
			sql = "SELECT NVL(COUNT(*), 0) FROM bbs";
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
			sql = "SELECT NVL(COUNT(*), 0) FROM bbs b JOIN member1 m ON b.userId = m.userId ";
			if (condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if (condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += "  WHERE INSTR(" + condition + ", ?) >= 1 ";
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
	public List<BoardDTO> listBoard(int start, int end) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("     SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("         SELECT b.num, userName, subject, hitCount, ");
			sb.append("               TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append("               NVL(replyCount, 0) replyCount ");
			sb.append("         FROM bbs b ");
			sb.append("         JOIN member1 m ON b.userId = m.userId ");
			sb.append("         LEFT OUTER JOIN ( ");
			sb.append("             SELECT num, COUNT(*) replyCount FROM bbsReply WHERE answer=0 ");
			sb.append("             GROUP BY num");
			sb.append("         ) c ON b.num = c.num");
			sb.append("         ORDER BY num DESC ");
			sb.append("     ) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();

				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				dto.setReplyCount(rs.getInt("replyCount"));

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

	public List<BoardDTO> listBoard(int start, int end, String condition, String keyword) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("     SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("         SELECT b.num, userName, subject, hitCount, ");
			sb.append("               TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append("               NVL(replyCount, 0) replyCount ");
			sb.append("         FROM bbs b ");
			sb.append("         JOIN member1 m ON b.userId = m.userId ");
			sb.append("         LEFT OUTER JOIN ( ");
			sb.append("             SELECT num, COUNT(*) replyCount FROM bbsReply WHERE answer=0 ");
			sb.append("             GROUP BY num");
			sb.append("         ) c ON b.num = c.num");
			if (condition.equals("all")) {
				sb.append("     WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append("     WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append("     WHERE INSTR(" + condition + ", ?) >= 1 ");
			}
			sb.append("         ORDER BY num DESC ");
			sb.append("     ) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");

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
				BoardDTO dto = new BoardDTO();

				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				dto.setReplyCount(rs.getInt("replyCount"));

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
			sql = "UPDATE bbs SET hitCount=hitCount+1 WHERE num=?";
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
	public BoardDTO readBoard(int num) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT b.num, b.userId, userName, subject, content, reg_date, hitCount, "
					+ "    NVL(boardLikeCount, 0) boardLikeCount "
					+ " FROM bbs b "
					+ " JOIN member1 m ON b.userId=m.userId "
					+ " LEFT OUTER JOIN ("
					+ "      SELECT num, COUNT(*) boardLikeCount FROM bbsLike"
					+ "      GROUP BY num"
					+ " ) bc ON b.num = bc.num"
					+ " WHERE b.num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				dto.setBoardLikeCount(rs.getInt("boardLikeCount"));
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
	
	// 로그인 유저의 게시글 공감 유무
		public boolean isUserBoardLike(int num, String userId) {
			boolean result = false;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT num, userId FROM bbsLike WHERE num = ? AND userId = ?";
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

	// 이전글
	public BoardDTO preReadBoard(int num, String condition, String keyword) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" SELECT * FROM ( ");
				sb.append("    SELECT num, subject ");
				sb.append("    FROM bbs b ");
				sb.append("    JOIN member1 m ON b.userId = m.userId ");
				sb.append("    WHERE ( num > ? ) ");
				if (condition.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
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
				sb.append("     SELECT num, subject FROM bbs ");
				sb.append("     WHERE num > ? ");
				sb.append("     ORDER BY num ASC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO();
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
	public BoardDTO nextReadBoard(int num, String condition, String keyword) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" SELECT * FROM ( ");
				sb.append("    SELECT num, subject ");
				sb.append("    FROM bbs b ");
				sb.append("    JOIN member1 m ON b.userId = m.userId ");
				sb.append("    WHERE ( num < ? ) ");
				if (condition.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
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
				if (condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
			} else {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT num, subject FROM bbs ");
				sb.append("     WHERE num < ? ");
				sb.append("     ORDER BY num DESC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO();
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

	// 게시물 수정
	public int updateBoard(BoardDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE bbs SET subject=?, content=? WHERE num=? AND userId=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getNum());
			pstmt.setString(4, dto.getUserId());
			
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

	// 게시물 삭제
	public int deleteBoard(int num, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (userId.equals("admin")) {
				sql = "DELETE FROM bbs WHERE num=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				
				result = pstmt.executeUpdate();
			} else {
				sql = "DELETE FROM bbs WHERE num=? AND userId=?";
				
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
	
	// 공지글
		public List<BoardDTO> listNotice() {
			List<BoardDTO> list = new ArrayList<BoardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				sb.append("SELECT num, b.userId, userName, subject, ");
				sb.append("       hitCount, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date  ");
				sb.append(" FROM bbs b ");
				sb.append(" JOIN member1 m ON b.userId=m.userId ");
				sb.append(" WHERE notice=1  ");
				sb.append(" ORDER BY num DESC ");

				pstmt = conn.prepareStatement(sb.toString());

				rs = pstmt.executeQuery();

				while (rs.next()) {
					BoardDTO dto = new BoardDTO();

					dto.setNum(rs.getInt("num"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setReg_date(rs.getString("reg_date"));

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

		public BoardDTO readNotice(int num) {
			BoardDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT num, notice, b.userId, userName, subject, content, hitCount, reg_date "
						+ " FROM bbs b "
						+ " JOIN member1 m ON b.userId=m.userId "
						+ " WHERE num = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new BoardDTO();

					dto.setNum(rs.getInt("num"));
					dto.setNotice(rs.getInt("notice"));
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
		
		public List<BoardDTO> listNotice(int start, int end, String condition, String keyword) {
			List<BoardDTO> list = new ArrayList<BoardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT ROWNUM rnum, tb.* FROM ( ");
				sb.append("         SELECT num, b.userId, userName, subject, ");
				sb.append("               hitCount, reg_date ");
				sb.append("         FROM bbs b ");
				sb.append("         JOIN member1 m ON b.userId = m.userId ");
				if (condition.equals("all")) {
					sb.append("     WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("     WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
				} else {
					sb.append("     WHERE INSTR(" + condition + ", ?) >= 1 ");
				}
				sb.append("         ORDER BY num DESC ");
				sb.append("     ) tb WHERE ROWNUM <= ? ");
				sb.append(" ) WHERE rnum >= ? ");
				
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
					BoardDTO dto = new BoardDTO();

					dto.setNum(rs.getInt("num"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setReg_date(rs.getString("reg_date")); // yyyy-MM-dd HH:mm:ss

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
		
		// 게시물 리스트
		public List<BoardDTO> listNotice(int start, int end) {
			List<BoardDTO> list = new ArrayList<BoardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT ROWNUM rnum, tb.* FROM ( ");
				sb.append("         SELECT num, n.userId, userName, subject, ");
				sb.append("               hitCount, reg_date ");
				sb.append("         FROM bbs n ");
				sb.append("         JOIN member1 m ON n.userId = m.userId ");
				sb.append("         ORDER BY num DESC ");
				sb.append("     ) tb WHERE ROWNUM <= ? ");
				sb.append(" ) WHERE rnum >= ? ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, end);
				pstmt.setInt(2, start);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					BoardDTO dto = new BoardDTO();

					dto.setNum(rs.getInt("num"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setReg_date(rs.getString("reg_date"));

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
		
		// 게시물의 공감 추가
		public int insertBoardLike(int num, String userId) throws SQLException {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO bbsLike(num, userId) VALUES (?, ?)";
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
					} catch (SQLException e) {
					}
				}
			}
			
			return result;
		}
		
		// 게시글 공감 삭제
		public int deleteBoardLike(int num, String userId) throws SQLException {
			int resuslt = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "DELETE FROM bbsLike WHERE num = ? AND userId = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				pstmt.setString(2, userId);
				resuslt = pstmt.executeUpdate();
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
			
			return resuslt;
		}
		
		// 게시물의 공감 개수
		public int countBoardLike(int num) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT NVL(COUNT(*), 0) FROM bbsLike WHERE num=?";
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
					} catch (SQLException e) {
					}
				}
					
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}
			
			return result;
		}

		// 게시물의 댓글 및 답글 추가
		public int insertReply(ReplyDTO dto) throws SQLException {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO bbsReply(replyNum, num, userId, content, answer, reg_date) "
						+ " VALUES (bbsReply_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, dto.getNum());
				pstmt.setString(2, dto.getUserId());
				pstmt.setString(3, dto.getContent());
				pstmt.setInt(4, dto.getAnswer());
				
				result = pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
			}
			
			return result;
		}

		// 게시물의 댓글 개수
		public int dataCountReply(int num) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT NVL(COUNT(*), 0) FROM bbsReply WHERE num=? AND answer=0";
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
					} catch (SQLException e) {
					}
				}
					
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}
			
			return result;
		}

		// 게시물 댓글 리스트
		public List<ReplyDTO> listReply(int num, int start, int end) {
			List<ReplyDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT ROWNUM rnum, tb.* FROM ( ");
				sb.append("         SELECT r.replyNum, r.userId, userName, num, content, r.reg_date, ");
				sb.append("                NVL(answerCount, 0) answerCount, ");
				sb.append("                NVL(likeCount, 0) likeCount, ");
				sb.append("                NVL(disLikeCount, 0) disLikeCount ");
				sb.append("         FROM bbsReply r ");
				sb.append("         JOIN member1 m ON r.userId = m.userId ");
				sb.append("	        LEFT OUTER  JOIN (");
				sb.append("	            SELECT answer, COUNT(*) answerCount ");
				sb.append("             FROM bbsReply  WHERE answer != 0 ");
				sb.append("             GROUP BY answer ");
				sb.append("         ) a ON r.replyNum = a.answer ");
				sb.append("         LEFT OUTER  JOIN ( ");
				sb.append("	            SELECT replyNum,  ");
				sb.append("                 COUNT(DECODE(replyLike, 1, 1)) likeCount, ");
				sb.append("                 COUNT(DECODE(replyLike, 0, 1)) disLikeCount ");
				sb.append("             FROM bbsReplyLike GROUP BY replyNum  ");
				sb.append("         ) b ON r.replyNum = b.replyNum  ");
				sb.append("	        WHERE num = ? AND r.answer=0 ");
				sb.append("         ORDER BY r.replyNum DESC ");
				sb.append("     ) tb WHERE ROWNUM <= ? ");
				sb.append(" ) WHERE rnum >= ? ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);

				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					ReplyDTO dto = new ReplyDTO();
					
					dto.setReplyNum(rs.getInt("replyNum"));
					dto.setNum(rs.getInt("num"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
					dto.setAnswerCount(rs.getInt("answerCount"));
					dto.setLikeCount(rs.getInt("likeCount"));
					dto.setDisLikeCount(rs.getInt("disLikeCount"));
					
					list.add(dto);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}
			
			return list;
		}

		public ReplyDTO readReply(int replyNum) {
			ReplyDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT replyNum, num, r.userId, userName, content ,r.reg_date "
						+ "  FROM bbsReply r JOIN member1 m ON r.userId=m.userId  "
						+ "  WHERE replyNum = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, replyNum);

				rs=pstmt.executeQuery();
				
				if(rs.next()) {
					dto=new ReplyDTO();
					
					dto.setReplyNum(rs.getInt("replyNum"));
					dto.setNum(rs.getInt("num"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
					
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}
			
			return dto;
		}
		
		// 게시물의 댓글 삭제
		public int deleteReply(int replyNum, String userId) throws SQLException {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			if(! userId.equals("admin")) {
				ReplyDTO dto = readReply(replyNum);
				if(dto == null || (! userId.equals(dto.getUserId()))) {
					return result;
				}
			}
			
			try {
				sql = "DELETE FROM bbsReply "
						+ "  WHERE replyNum IN  "
						+ "  (SELECT replyNum FROM bbsReply START WITH replyNum = ?"
						+ "     CONNECT BY PRIOR replyNum = answer)";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, replyNum);
				
				result = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}		
			
			return result;
		}

		// 댓글의 답글 리스트
		public List<ReplyDTO> listReplyAnswer(int answer) {
			List<ReplyDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb=new StringBuilder();
			
			try {
				sb.append(" SELECT replyNum, num, r.userId, userName, content, reg_date, answer ");
				sb.append(" FROM bbsReply r ");
				sb.append(" JOIN member1 m ON r.userId=m.userId ");
				sb.append(" WHERE answer=? ");
				sb.append(" ORDER BY replyNum DESC ");
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, answer);

				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					ReplyDTO dto=new ReplyDTO();
					
					dto.setReplyNum(rs.getInt("replyNum"));
					dto.setNum(rs.getInt("num"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
					dto.setAnswer(rs.getInt("answer"));
					
					list.add(dto);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
					
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}
			return list;
		}
		
		// 댓글의 답글 개수
		public int dataCountReplyAnswer(int answer) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT NVL(COUNT(*), 0) FROM bbsReply WHERE answer=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, answer);
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					result=rs.getInt(1);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}
			
			return result;
		}
		
		// 댓글의 좋아요 / 싫어요 추가
		public int insertReplyLike(ReplyDTO dto) throws SQLException {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql="INSERT INTO bbsReplyLike(replyNum, userId, replyLike) VALUES (?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getReplyNum());
				pstmt.setString(2, dto.getUserId());
				pstmt.setInt(3, dto.getReplyLike());
				result = pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}		
			return result;
		}
		
		// 댓글의 좋아요 / 싫어요 개수
		public Map<String, Integer> countReplyLike(int replyNum) {
			Map<String, Integer> map = new HashMap<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = " SELECT COUNT(DECODE(replyLike, 1, 1)) likeCount,  "
					+ "     COUNT(DECODE(replyLike, 0, 1)) disLikeCount  "
					+ " FROM bbsReplyLike WHERE replyNum = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, replyNum);
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					map.put("likeCount", rs.getInt("likeCount"));
					map.put("disLikeCount", rs.getInt("disLikeCount"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs != null) {
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
			
			return map;
		}	
	
}
