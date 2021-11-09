<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class='reply-info'>
	<span class='reply-count'>댓글 ${replyCount}개</span>
	<span>[목록, ${pageNo}/${total_page} 페이지]</span>
</div>

<table class='table reply-list'>
	<c:forEach var="dto" items="${listReply}">
		<tr class='list-header'>
			<td width='50%'>
				<span class='bold'>${dto.userName}</span>
			</td>
			<td width='50%' align='right'>
				<span>${dto.reg_date}</span> |
				<c:choose>
					<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
						<span class='deleteReply' data-replyNum='${dto.replyNum}' data-pageNo='${pageNo}'>삭제</span>
					</c:when>
					<c:otherwise>
						<span class="notifyReply">신고</span>
					</c:otherwise>
				</c:choose>
				
			</td>
		</tr>
		<tr>
			<td colspan='2' valign='top'>${dto.content}</td>
		</tr>

		<tr>
			<td>
				<button type='button' class='btn btnReplyAnswerLayout' data-replyNum='${dto.replyNum}'>답글 <span id="answerCount${dto.replyNum}">${dto.answerCount}</span></button>
			</td>
		</tr>
	
	    <tr class='reply-answer' style="display: none;">
	        <td colspan='2'>
	            <div id='listReplyAnswer${dto.replyNum}' class='answer-list'></div>
	            <div class="answer-form">
	                <div class='answer-left'>└</div>
	                <div class='answer-right'><textarea class='boxTA' style="resize:none"></textarea></div>
	            </div>
	             <div class='answer-footer'>
	                <button type='button' class='btn btnSendReplyAnswer' data-replyNum='${dto.replyNum}'>답글 등록</button>
	            </div>
			</td>
	    </tr>
	</c:forEach>
</table>

<div class="page-box">
	${paging}
</div>			
