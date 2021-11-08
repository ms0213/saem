package com.trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import saem.util.DBConn;

public class TradeDAO {
	private Connection conn = DBConn.getConnection();
	
	// 글 작성
	public int insertTrade(TradeDTO dto) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			sql = "SELECT trade_seq.NEXTVAL FROM dual"; // trade 시퀀스 번호랑 tradeFile 이랑 맞추기 위함
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			seq =0;
			if(rs.next()) {
				seq = rs.getInt(1);
			}
			dto.setNum(seq); // 다음 seq로 게시물 등록
			
			rs.close();
			pstmt.close();
			rs=null;
			pstmt = null;
			
			// 게시글 일반 등록
			sql ="INSERT INTO trade (num, userId, subject, content, type, reg_date, pay ) "
					+ " VALUES(?,?,?,?,?,SYSDATE,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getType());
			pstmt.setInt(6, dto.getPay());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			// images 배열
			if(dto.getImageFiles() != null) {
				sql = "INSERT INTO tradefile(fileNum, num, imageFilename) VALUES"
						+ " (tradePhotoFile_seq.NEXTVAL, ? , ?) ";
				
				pstmt = conn.prepareStatement(sql);
				for(int i = 0; i<dto.getImageFiles().length; i++) {
					pstmt.setInt(1, dto.getNum()); // 위에서 seq 만들었던 것
					pstmt.setString(2, dto.getImageFiles()[i]);
					pstmt.executeUpdate();
				}
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
		
		return result ;
	}
	
	// 게시물 수정
	public int updateTrade(TradeDTO dto) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE trade SET subject = ?, content =?, type=?, pay=? WHERE num = ?";
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getType());
			pstmt.setInt(4, dto.getPay());
			pstmt.setInt(5, dto.getNum());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles()!=null) {
				sql="INSERT INTO tradeFile(fileNum, num, imageFilename) VALUES "
						+ " (tradePhotoFile_seq.NEXTVAL, ? , ?) ";
				pstmt = conn.prepareStatement(sql);
				for (int i = 0; i < dto.getImageFiles().length; i++) {
					pstmt.setInt(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					pstmt.executeUpdate();
				}
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
	
	// 데이터 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM trade";
			pstmt = conn.prepareStatement(sql);

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
	
	// 검색에서 데이터 개수
	public int dataCount(String condition, String keyword) {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		
		try {
			//INSTR(문자열, 검색할 문자, 시작 지점, n번째 검색 단어) : 찾는 문자가 없으면 0 반환, 찾는 단어 '앞'글자의 인덱스 반환
			// NVL("값","지정값") : "값"이 있으면 "값", "값"이 없으면 "지정값"으로 출력
			// 조건 별로 WHERE 절 작성
			sql = "SELECT NVL(COUNT(*),0) FROM trade t JOIN member1 m ON t.userId = m.userId ";
			if(condition.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if (condition.equals("reg_date")) {
				sql += " WHERE TO_CHAR(reg_date, 'YYYYMMDD') =? ";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?) >=1 ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			if(condition.equals("all")) pstmt.setString(2, keyword);
			rs=pstmt.executeQuery();
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
	
	// 게시물 리스트
	public List<TradeDTO> listBoard(int start, int end){
		List<TradeDTO> list = new ArrayList<TradeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT num, userName, subject, hitCount, ");
			sb.append("					TO_CHAR(reg_date,'YYYY-MM-DD') reg_date, type, pay ");
			sb.append("			FROM trade t ");
			sb.append("			JOIN member1 m ON t.userId = m.userId");
			sb.append("			ORDER BY num DESC ");
			sb.append("		) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				TradeDTO dto = new TradeDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setType(rs.getString("type"));
				dto.setPay(rs.getInt("pay"));
				
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
	
	// 게시물 리스트 (검색)
	public List<TradeDTO> listBoard(int start, int end, String condition, String keyword){
		List<TradeDTO> list = new ArrayList<TradeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM (");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT num, userName, subject, hitCount, type, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, pay ");
			sb.append("			FROM trade t");
			sb.append("			JOIN member1 m ON t.userId = m.userId ");
			if(condition.equals("all")) {
				sb.append("		WHERE INSTR(subject, ?) >= 1 OR INSTR(content,?)>=1 OR INSTR(type,?) >=1 ");
			} else if ( condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append("     WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append("		WHERE INSTR(" +condition+",?) >=1");
			}
			sb.append("			ORDER BY num DESC ");
			sb.append("			) tb WHERE ROWNUM <= ?	");
			sb.append("		) WHERE rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setString(3, keyword);
				pstmt.setInt(4, end);
				pstmt.setInt(5, start);
			} else {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				TradeDTO dto = new TradeDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setType(rs.getString("type"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setPay(rs.getInt("pay"));
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
	
	// 조회수 증가
	public int updateHitCount(int num) throws SQLException{
		int result =0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE trade SET hitCount=hitCount+1 WHERE num=?";
			
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
	public TradeDTO readTrade(int num) {
		TradeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT num, t.userId, userName, subject, content, "
					+ " reg_date, hitCount, type, pay"
					+ " FROM trade t "
					+ " JOIN member1 m ON t.userId=m.userId "
					+ " WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new TradeDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setType(rs.getString("type"));
				dto.setPay(rs.getInt("pay"));
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
	// 수정 시 첨부한 사진 목록으로 가져오기
	public List<TradeDTO> listPhotoFile(int num){
		List<TradeDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT fileNum, num, imageFilename FROM tradeFile WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				TradeDTO dto = new TradeDTO();
				
				dto.setFileNum(rs.getInt("fileNum"));
				dto.setNum(rs.getInt("num"));
				dto.setImageFilename(rs.getString("imageFilename"));
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
	
	// 게시물 삭제 (+ 댓글 삭제)
	public int deleteTrade(int num, String userId) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if (userId.equals("admin")) {
				sql = "DELETE FROM trade WHERE num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				result = pstmt.executeUpdate();
			} else {
				sql = "DELETE FROM trade WHERE num=? AND userId=?";
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
		return result ;
	}
	
	// 사진 삭제
	public int deletePhotoFile(String mode, int num) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if (mode.equals("all")) {
				sql = "DELETE FROM tradeFile WHERE num = ?";
			} else {
				sql = "DELETE FROM tradeFile WHERE fileNum = ?";
			}
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
}