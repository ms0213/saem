<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>saem 7856</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp" />
<style type="text/css">
table {
    display: table;
    border-collapse: separate;
    box-sizing: border-box;
    text-indent: initial;
    border-spacing: 2px;
    border-color: grey;
}
.table-border tr {
	border-bottom: 1px solid #ccc; 
}
.table-border tr:first-child {
	border-top: 2px solid #ccc;
}
.table-list tr:first-child{
	background: #eee;
}
.table-list th, .table-list td {
	text-align: center;
}
.table-list .left {
	text-align: left; padding-left: 5px; 
}

.table-list .num {
	width: 60px; color: #787878;
}
.table-list .subject {
	color: #787878;
}
.table-list .name {
	width: 100px; color: #787878;
}
.table-list .date {
	width: 100px; color: #787878;
}
.table-list .hit {
	width: 100px; color: #787878;
}
.body-title {
    color: #424951;
    padding-top: 25px;
    padding-bottom: 5px;
    margin: 0 0 25px 0;
    border-bottom: 1px solid #ddd;
}
.body-title h3 {
    font-size: 23px;
    min-width: 700px;
    font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
    font-weight: bold;
    margin: 0 0 -5px 0;
    padding-bottom: 5px;
    display: inline-block;
    border-bottom: 3px solid #424951;
}
.btn {
	color: #333;
	border: 1px solid #333;
	background-color: #fff;
	padding: 4px 10px;
	border-radius: 4px;
	font-weight: 500;
	cursor:pointer;
	font-size: 14px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.btn:hover, .btn:active, .btn:focus {
	background-color: #e6e6e6;
	border-color: #adadad;
	color:#333;
}
.btn[disabled], fieldset[disabled] .btn {
	pointer-events: none;
	cursor: not-allowed;
	filter: alpha(opacity=65);
	-webkit-box-shadow: none;
	box-shadow: none;
	opacity: .65;
}
.selectField {
	border: 1px solid #999;
	padding: 4px 5px;
	border-radius: 4px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
	width: 100px;
	font-size: 14px;
}
.search_wrapper {
	display: flex;
	align-items: center;
	background-color: #fff;
	border: 1px solid #dee2e6;
	background-color: #fff;
	border-radius: 4px;
	padding: 5px 10px;
}
.body-container {
    margin: 0 auto 15px;
    width: 100%;
    min-height: 450px;
}
.body-title {
    color: #424951;
    padding-top: 25px;
    padding-bottom: 5px;
    margin: 0 0 25px 0;
    border-bottom: 1px solid #ddd;
}
.table {
    width: 100%;
    border-spacing: 0;
    border-collapse: collapse;
}
tbody {
    display: table-row-group;
    vertical-align: middle;
    border-color: inherit;
}
.page-box {
    clear: both;
    padding: 20px 0;
    text-align: center;
}
.reply {
	clear: both; padding: 20px 0 10px;
}
.reply .bold {
	font-weight: 600;
}

.reply .form-header {
	padding-bottom: 7px;
}
.reply-form  td {
	padding: 2px 0 2px;
}
.reply-form textarea {
	width: 100%; height: 75px;
}
.reply-form button {
	padding: 8px 25px;
}

.reply .reply-info {
	padding-top: 25px; padding-bottom: 7px;
}
.reply .reply-info  .reply-count {
	color: #3EA9CD; font-weight: 700;
}

.reply .reply-list tr td {
	padding: 7px 5px;
}
.reply .reply-list .bold {
	font-weight: 600;
}

.reply .deleteReply, .reply .deleteReplyAnswer {
	cursor: pointer;
}
.reply .notifyReply {
	cursor: pointer;
}

.reply-list .list-header {
	border: 1px solid #ccc; background: #eee;
}
.reply-list td {
	padding-left: 7px; padding-right: 7px;
}

.reply-answer {
	display: none;
}
.reply-answer .answer-left {
	float: left; width: 5%;
}
.reply-answer .answer-right {
	float: left; width: 95%;
}
.reply-answer .answer-list {
	border-top: 1px solid #ccc; padding: 0 10px 7px;
}
.reply-answer .answer-form {
	clear: both; padding: 3px 10px 5px;
}
.reply-answer .answer-form textarea {
	width: 100%; height: 75px;
}
.reply-answer .answer-footer {
	clear: both; padding: 0 13px 10px 10px; text-align: right;
}

.answer-article {
	clear: both;
}
.answer-article .answer-article-header {
	clear: both; padding-top: 5px;
}
.answer-article .answer-article-body {
	clear:both; padding: 5px 5px; border-bottom: 1px solid #ccc;
}
</style>
<script type="text/javascript">
function searchList() {
	var f = document.searchForm;
	f.submit();
}
</script>
<script type="text/javascript">
<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
	function deleteBoard() {
	    if(confirm("게시글을 삭제 하시 겠습니까 ? ")) {
		    var query = "num=${dto.num}&${query}";
		    var url = "${pageContext.request.contextPath}/bbs/delete.do?" + query;
	    	location.href = url;
	    }
	}
</c:if>
</script>
<script type="text/javascript">
function login() {
	location.href="${pageContext.request.contextPath}/member/login.do";
}

function ajaxFun(url, method, query, dataType, fn) {
	$.ajax({
		type:method,
		url:url,
		data:query,
		dataType:dataType,
		success:function(data) {
			fn(data);
		},
		beforeSend:function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status === 403) {
				login();
				return false;
			} else if(jqXHR.status === 405) {
				alert("접근을 허용하지 않습니다.");
				return false;
			}
	    	
			console.log(jqXHR.responseText);
		}
	});
}

// 게시글 공감 여부
$(function(){
	$(".btnSendBoardLike").click(function(){
		var $i = $(this).find("i");
		var isNoLike = $i.css("color") == "rgb(0, 0, 0)";
		var msg = isNoLike ? "게시글에 공감하십니까 ? " : "게시글 공감을 취소하시겠습니까 ? ";
		
		if(! confirm( msg )) {
			return false;
		}
		
		var url = "${pageContext.request.contextPath}/bbs/insertBoardLike.do";
		var num = "${dto.num}";
		// var query = {num:num, isNoLike:isNoLike};
		var query = "num=" + num + "&isNoLike=" + isNoLike;;

		var fn = function(data) {
			var state = data.state;
			if(state === "true") {
				var color = "black";
				if( isNoLike ) {
					color = "blue";
				}
				$i.css("color", color);
				
				var count = data.boardLikeCount;
				$("#boardLikeCount").text(count);
			} else if(state === "liked") {
				alert("좋아요는 한번만 가능합니다. !!!");
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

// 페이징 처리
$(function(){
	listPage(1);
});

function listPage(page) {
	var url = "${pageContext.request.contextPath}/bbs/listReply.do";
	var query = "num=${dto.num}&pageNo="+page;
	var selector = "#listReply";
	
	var fn = function(data){
		$(selector).html(data);
	};
	ajaxFun(url, "get", query, "html", fn);
}

// 리플 등록
$(function(){
	$(".btnSendReply").click(function(){
		var num = "${dto.num}";
		var $tb = $(this).closest("table");
		var content = $tb.find("textarea").val().trim();
		if(! content) {
			$tb.find("textarea").focus();
			return false;
		}
		content = encodeURIComponent(content);
		
		var url = "${pageContext.request.contextPath}/bbs/insertReply.do";
		var query = "num=" + num + "&content=" + content + "&answer=0";
		
		var fn = function(data){
			$tb.find("textarea").val("");
			
			var state = data.state;
			if(state === "true") {
				listPage(1);
			} else if(state === "false") {
				alert("댓글을 추가 하지 못했습니다.");
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

// 댓글 삭제
$(function(){
	$("body").on("click", ".deleteReply", function(){
		if(! confirm("게시물을 삭제하시겠습니까 ? ")) {
		    return false;
		}
		
		var replyNum = $(this).attr("data-replyNum");
		var page = $(this).attr("data-pageNo");
		
		var url = "${pageContext.request.contextPath}/bbs/deleteReply.do";
		var query = "replyNum="+replyNum;
		
		var fn = function(data){
			// var state = data.state;
			listPage(page);
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

// 댓글 좋아요 / 싫어요
$(function(){
	// 댓글 좋아요 / 싫어요 등록
	$("body").on("click", ".btnSendReplyLike", function(){
		var replyNum = $(this).attr("data-replyNum");
		var replyLike = $(this).attr("data-replyLike");
		var $btn = $(this);
		
		var msg = "게시물이 마음에 들지 않으십니까 ?";
		if(replyLike === "1") {
			msg="게시물에 공감하십니까 ?";
		}
		
		if(! confirm(msg)) {
			return false;
		}
		
		var url = "${pageContext.request.contextPath}/bbs/insertReplyLike.do";
		var query = "replyNum=" + replyNum + "&replyLike=" + replyLike;
		
		var fn = function(data){
			var state = data.state;
			if(state === "true") {
				var likeCount = data.likeCount;
				var disLikeCount = data.disLikeCount;
				
				$btn.parent("td").children().eq(0).find("span").html(likeCount);
				$btn.parent("td").children().eq(1).find("span").html(disLikeCount);
			} else if(state === "liked") {
				alert("게시물 공감 여부는 한번만 가능합니다. !!!");
			} else {
				alert("게시물 공감 여부 처리가 실패했습니다. !!!");
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

// 댓글별 답글 리스트
function listReplyAnswer(answer) {
	var url = "${pageContext.request.contextPath}/bbs/listReplyAnswer.do";
	var query = "answer=" + answer;
	var selector = "#listReplyAnswer" + answer;
	
	var fn = function(data){
		$(selector).html(data);
	};
	ajaxFun(url, "get", query, "html", fn);
}

// 댓글별 답글 개수
function countReplyAnswer(answer) {
	var url = "${pageContext.request.contextPath}/bbs/countReplyAnswer.do";
	var query = "answer=" + answer;
	
	var fn = function(data){
		var count = data.count;
		var selector = "#answerCount"+answer;
		$(selector).html(count);
	};
	
	ajaxFun(url, "post", query, "json", fn);
}

// 답글 버튼(댓글별 답글 등록폼 및 답글리스트)
$(function(){
	$("body").on("click", ".btnReplyAnswerLayout", function(){
		var $trReplyAnswer = $(this).closest("tr").next();
		// var $trReplyAnswer = $(this).parent().parent().next();
		// var $answerList = $trReplyAnswer.children().children().eq(0);
		
		var isVisible = $trReplyAnswer.is(':visible');
		var replyNum = $(this).attr("data-replyNum");
			
		if(isVisible) {
			$trReplyAnswer.hide();
		} else {
			$trReplyAnswer.show();
            
			// 답글 리스트
			listReplyAnswer(replyNum);
			
			// 답글 개수
			countReplyAnswer(replyNum);
		}
	});
	
});

// 댓글별 답글 등록
$(function(){
	$("body").on("click", ".btnSendReplyAnswer", function(){
		var num = "${dto.num}";
		var replyNum = $(this).attr("data-replyNum");
		var $td = $(this).closest("td");
		
		var content = $td.find("textarea").val().trim();
		if(! content) {
			$td.find("textarea").focus();
			return false;
		}
		content = encodeURIComponent(content);
		
		var url = "${pageContext.request.contextPath}/bbs/insertReply.do";
		var query = "num=" + num + "&content=" + content + "&answer=" + replyNum;
		
		var fn = function(data){
			$td.find("textarea").val("");
			
			var state = data.state;
			if(state === "true") {
				listReplyAnswer(replyNum);
				countReplyAnswer(replyNum);
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

// 댓글별 답글 삭제
$(function(){
	$("body").on("click", ".deleteReplyAnswer", function(){
		if(! confirm("게시물을 삭제하시겠습니까 ? ")) {
		    return false;
		}
		
		var replyNum = $(this).attr("data-replyNum");
		var answer = $(this).attr("data-answer");
		
		var url = "${pageContext.request.contextPath}/bbs/deleteReply.do";
		var query = "replyNum=" + replyNum;
		
		var fn = function(data){
			listReplyAnswer(answer);
			countReplyAnswer(answer);
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});
</script>
</head>

<body class="is-preload">

	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">
				
				<!-- Header -->
				<header id="header">
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"></jsp:include>
				</header>
				<main>
	<div class="body-container" style="padding-top: 30px;">
		<div class="title" style="margin: 30px 0 30px 0">
							<h1>
								<i></i> 자유 게시판
							</h1>
						</div>
        
		<table class="table table-border table-article">
			<tr>
				<td colspan="2" align="center">
					${dto.subject}
				</td>
			</tr>
			
			<tr>
				<td width="50%">
					이름 : ${dto.userName}
				</td>
				<td align="right">
					${dto.reg_date} | 조회 ${dto.hitCount}
				</td>
			</tr>
			
			<tr>
				<td colspan="2" valign="top" height="200">
					${dto.content}
				</td>
			</tr>
			
			<tr>
				<td colspan="2" align="center" style="padding-bottom: 20px;">
					<button type="button" class="btn btnSendBoardLike" title="좋아요"><i class="fas fa-thumbs-up" style="color: ${isUserLike?'blue':'black'}"></i>&nbsp;&nbsp;<span id="boardLikeCount">${dto.boardLikeCount}</span></button>
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					이전글 :
					<c:if test="${not empty preReadDto}">
						<a href="${pageContext.request.contextPath}/bbs/article.do?${query}&num=${preReadDto.num}">${preReadDto.subject}</a>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					다음글 :
					<c:if test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/bbs/article.do?${query}&num=${nextReadDto.num}">${nextReadDto.subject}</a>
					</c:if>
				</td>
			</tr>
		</table>
		
		<table class="table">
			<tr>
				<td width="50%">
					<c:if test="${sessionScope.member.userId=='admin'}">
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/bbs/updateN.do?num=${dto.num}&page=${page}';">수정</button>
					</c:if>
<%-- 					<c:if test="${sessionScope.member.userId==dto.userId && sessionScope.member.userId!='admin'}"> --%>
<%-- 						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/bbs/updateN.do?num=${dto.num}&page=${page}';">수정</button> --%>
<%-- 					</c:if> --%>
					<c:choose>
						<c:when test="${sessionScope.member.userId==dto.userId && sessionScope.member.userId!='admin'}">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/bbs/update.do?num=${dto.num}&page=${page}';">수정</button>
						</c:when>					
						<c:otherwise>
<!-- 							<button type="button" class="btn" hidden="true">수정</button> -->
						</c:otherwise>
					</c:choose>
		    	
					<c:choose>
			    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
			    			<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">삭제</button>
			    		</c:otherwise>
			    	</c:choose>
				</td>
				<td align="right">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/bbs/list.do?${query}';">리스트</button>
				</td>
			</tr>
		</table>
        
        <div class="reply">
			<form name="replyForm" method="post">
				<div class='form-header'>
					<span class="bold">댓글쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
				</div>
				
				<table class="table reply-form">
					<tr>
						<td>
							<textarea class='boxTA' name="content"></textarea>
						</td>
					</tr>
					<tr>
					   <td align='right'>
					        <button type='button' class='btn btnSendReply'>댓글 등록</button>
					    </td>
					 </tr>
				</table>
			</form>
			
			<div id="listReply"></div>
		</div>
		
	</div>
</main>
			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>