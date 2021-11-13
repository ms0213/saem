package com.photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saem.util.DBConn;

public class PhotoDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertPhoto(PhotoDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			sql = "select gallery_seq.nextval from dual";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			seq = 0;
			if(rs.next()) {
				seq = rs.getInt(1);
			}
			dto.setNum(seq);
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			sql = "insert into gallery (num, subject, content, reg_date, hitCount) "
					+ " values (?, ?, ?, sysdate, 0)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles() != null) {
				sql = "insert into galPhotoFile(fileNum, num, imageFilename) values "
						+ " (galPhotoFile_seq.nextval, ?, ?)";
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
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select nvl(count(*), 0) from gallery";
			pstmt = conn.prepareStatement(sql);
			
			rs= pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
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
		
		return result;
	}
	
	public List<PhotoDTO> listPhoto(int start, int end) {
		List<PhotoDTO> list = new ArrayList<PhotoDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" select * from ( ");
			sb.append("		select rownum rnum, tb.* from ( ");
			sb.append("			select p.num, subject, content, imageFilename, to_char(reg_date, 'YYYY-MM-DD') reg_date, nvl(replyCount, 0) replyCount ");
			sb.append("			from gallery p");
			sb.append("			left outer join ( ");
			sb.append("				select fileNum, num, imageFilename from ( ");
			sb.append("					select fileNum, num, imageFilename, ");
			sb.append("						row_number() over(partition by num order by fileNum asc) rank ");
			sb.append("					from galPhotoFile g");
			sb.append("				) where rank = 1 ");
			sb.append("			) i on p.num = i.num ");
			sb.append("			left outer join ( ");
			sb.append("				select num, count(*) replyCount from photoReply where answer = 0 ");
			sb.append("				group by num ");
			sb.append("			) c on p.num = c.num");
			sb.append("			order by num desc ");
			sb.append("		) tb where rownum <= ? ");
			sb.append("	) where rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				PhotoDTO dto = new PhotoDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setImageFilename(rs.getString("imageFilename"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setReplyCount(rs.getInt("replyCount"));
				
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
	
	public int updateHitCount(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE gallery SET hitCount=hitCount+1 WHERE num=?";
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
	
	public PhotoDTO readPhoto(int num) {
		PhotoDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select g.num, subject, content, reg_date, hitCount, "
					+ "    nvl(photoLikeCount, 0) photoLikeCount "
					+ " from gallery g "
					+ " left outer join ("
					+ "      select num, count(*) photoLikeCount from photoLike"
					+ "      group by num"
					+ " ) gl on g.num = gl.num"
					+ " where g.num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new PhotoDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setPhotoLikeCount(rs.getInt("photoLikeCount"));
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
	
	public boolean isUserPhotoLike(int num, String userId) {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select num, userId from photoLike where num = ? and userId = ?";
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
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public int updatePhoto(PhotoDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update gallery set subject=?, content=? where num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getNum());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles() != null) {
				sql = "insert into galPhotoFile(fileNum, num, imageFilename) values "
						+ " (galPhotoFile_seq.nextval, ?, ?)";
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
	
	public int deletePhoto(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete from gallery where num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
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
	
	public List<PhotoDTO> listPhotoFile(int num) {
		List<PhotoDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select fileNum, num, imageFilename from galPhotoFile where num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				PhotoDTO dto = new PhotoDTO();
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
	
	public PhotoDTO readPhotoFile(int fileNum) {
		PhotoDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select fileNum, num, imageFilename from galPhotoFile where fileNum = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, fileNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new PhotoDTO();
				
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
	
	public PhotoDTO preReadPhoto(int num) {
		PhotoDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("	select * from ( ");
			sb.append("		select num, subject from gallery ");
			sb.append("		where num > ? ");
			sb.append("		order by num asc ");
			sb.append("	) where rownum = 1 ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new PhotoDTO();
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
	
	public PhotoDTO nextReadPhoto(int num) {
		PhotoDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("	select * from ( ");
			sb.append("		select num, subject from gallery ");
			sb.append("		where num < ? ");
			sb.append("		order by num desc ");
			sb.append("	) where rownum = 1 ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new PhotoDTO();
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
	
	public int deletePhotoFile(String mode, int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql = "delete from galPhotoFile where num = ?";
			} else {
				sql = "delete from galPhotoFile where fileNum = ?";
				
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
	
	public int insertPhotoLike(int num, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into photoLike(num, userId) values (?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
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
	
	public int deletePhotoLike(int num, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete from photoLike where num = ? and userId = ?";
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
	
	public int countPhotoLike(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select nvl(count(*),0) from photoLike where num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public int insertReply(ReplyDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into photoReply(replyNum, num, userId, content, answer, reg_date) values (photoReply_seq.nextval, ?, ?, ?, ?, sysdate)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, dto.getAnswer());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public int dataCountReply(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select nvl(count(*),0) from photoReply where num = ? and answer = 0";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public List<ReplyDTO> listReply(int num, int start, int end) {
		List<ReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" select * from ( ");
			sb.append(" 	select rownum rnum, tb.* from ( ");
			sb.append(" 		select r.replyNum, r.userId, userName, num, content, r.reg_date, ");
			sb.append(" 			nvl(answerCount, 0) answerCount, ");
			sb.append(" 			nvl(likeCount, 0) likeCount, ");
			sb.append(" 			nvl(disLikeCount, 0) disLikeCount ");
			sb.append(" 		from photoReply r ");
			sb.append(" 		join member1 m on r.userId = m.userId ");
			sb.append(" 		left outer join ( ");
			sb.append(" 			select answer, count(*) answerCount ");
			sb.append(" 			from photoReply where answer != 0 ");
			sb.append(" 			group by answer ");
			sb.append(" 		) a on r.replyNum = a.answer ");
			sb.append(" 		left outer join ( ");
			sb.append(" 			select replyNum, ");
			sb.append(" 				count(decode(replyLike, 1, 1)) likeCount, ");
			sb.append(" 				count(decode(replyLike, 0, 1)) disLikeCount ");
			sb.append(" 			from photoReplyLike group by replyNum ");
			sb.append(" 		) b on r.replyNum = b.replyNum ");
			sb.append(" 		where num = ? and r.answer = 0 ");
			sb.append(" 		order by r.replyNum asc ");
			sb.append(" 	) tb where rownum <= ? ");
			sb.append(" ) where rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next() ) {
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
		} catch (Exception e) {
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
			sql = "select replyNum, num, r.userId, userName, content, r.reg_date "
					+ " from photoReply r join member1 m on r.userId = m.userId "
					+ " where replyNum = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, replyNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ReplyDTO();
				
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
	
	public int deleteReply(int replyNum, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		if(! userId.equals("admin")) {
			ReplyDTO dto = readReply(replyNum);
			if(dto == null || (!userId.equals(dto.getUserId()))) {
				return result;
			}
		}
		
		try {
			sql = "delete from photoReply where replyNum in (select replyNum from photoReply start with replyNum = ?"
					+ " connect by prior replyNum = answer)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, replyNum);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public List<ReplyDTO> listReplyAnswer(int answer) {
		List<ReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" select replyNum, num, r.userId, userName, content, reg_date, answer ");
			sb.append(" from photoReply r ");
			sb.append(" join member1 m on r.userId = m.userId ");
			sb.append(" where answer = ?");
			sb.append(" order by replyNum asc ");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, answer);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReplyDTO dto = new ReplyDTO();
				
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
	
	public int dataCountReplyAnswer(int answer) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select nvl(count(*), 0) from photoReply where answer = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, answer);
			
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
	
	public int insertReplyLike(ReplyDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into photoReplyLike(replyNum, userId, replyLike) values (?, ?, ?)";
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
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public Map<String, Integer> countReplyLike(int replyNum) {
		Map<String, Integer> map = new HashMap<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select count(decode(replyLike, 1, 1)) likeCount, "
					+ " count(decode(replyLike, 0, 1)) disLikeCount "
					+ " from photoReplyLike where replyNum = ? ";
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

