<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>saem 7856</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp"/>
<style type="text/css">
/* 모달대화상자 */
.ui-widget-header { /* 타이틀바 */
	background: none;
	border: none;
	border-bottom: 1px solid #ccc;
	border-radius: 0;
}
.ui-dialog .ui-dialog-title {
	padding-top: 5px; padding-bottom: 5px;
}
.ui-widget-content { /* 내용 */
   /* border: none; */
   border-color: #ccc; 
}

.img-box {
	max-width: 100%;
	padding: 5px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
}
.img-box img {
	width: 400px; height: 100%;
	flex: 0 0 auto;
	cursor: pointer;
}

.photo-layout img {
	width: 570px; height: 600px;
}


</style>
<script type="text/javascript">

function imageViewer(img) {
	var viewer = $(".photo-layout");
	var s="<img src='"+img+"'>";
	viewer.html(s);

	$(".dialog-photo").dialog({
		title:"이미지",
		width: 600,
		height: 530,
		modal : true
	});
}

function deleteBoard(){
	if (confirm("게시글을 삭제하시겠습니까?")) {
		var query = "num=${dto.num}&${query}";
		var url = "${pageContext.request.contextPath}/trade/delete.do?"+query;
		location.href= url;
	}
}

// 댓글 및 답글
// AJAX
function ajaxFun(url, method, query, dataType, fn){
	$.ajax({
		type:method,
		url : url,
		data:query,
		dataType : dataType,
		success:function(data){
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

// 댓글 리스트
$(function(){
	listPage(1);
});

function listPage(page){
	var url = "${pageContext.request.contextPath}/trade/listReply.do";
	var query = "num=${dto.num}&pageNo="+page;
	var selector = "#listReply";
	
	var fn = function(data){
		$(selector).html(data);
	};
	
	ajaxFun(url,"get",query,"html",fn);  // select 에서 list를 긁어오니까 get으로 주소줄에 매개변수를 넣기때문
}

// 댓글 등록
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
		var url = "${pageContext.request.contextPath}/trade/insertReply.do";
		var query = "num="+num+"&content="+content+"&answer=0";
		
		var fn = function(data) {
			var state = data.state;
			
			$tb.find("textarea").val("");
			
			if(state === "true") {
				listPage(1);
			} else {
				alert("댓글 추가가 실패 했습니다.");
			}
			
		};
		ajaxFun(url, "post", query, "json", fn); // post : 각각의 데이터를 주소줄에 보내는게 아니라 몸체에 넣어서 보냄
	});
});

//댓글 삭제
$(function(){
	$("body").on("click", ".deleteReply", function(){
		if(! confirm("게시글을 삭제 하시겠습니까 ? ")) {
			return false;
		}
		
		var replyNum = $(this).attr("data-replyNum");
		var pageNo = $(this).attr("data-pageNo");
		
		var url = "${pageContext.request.contextPath}/trade/deleteReply.do";
		var query = "replyNum=" + replyNum;
		
		var fn = function(data) {
			listPage(pageNo);
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

//댓글별 답글 리스트
function listReplyAnswer(answer) {
	var url = "${pageContext.request.contextPath}/trade/listReplyAnswer.do";
	var query = "answer=" + answer;
	var selector = "#listReplyAnswer" + answer;
	
	var fn = function(data) {
		$(selector)	.html(data);
	};
	ajaxFun(url, "get", query, "html", fn);
}

// 댓글별 답글 개수
function countReplyAnswer(answer) {
	var url = "${pageContext.request.contextPath}/trade/countReplyAnswer.do";
	var query = "answer=" + answer;

	var fn = function(data) {
		var count = data.count;
		var selector = "#answerCount"+answer;
		$(selector).html(count);
	};
	ajaxFun(url, "post", query, "json", fn);
}


// 답글 버튼
$(function(){
	$("body").on("click", ".btnReplyAnswerLayout", function(){
		var $tr = $(this).closest("tr").next();
		
		var isVisible = $tr.is(":visible");
		var replyNum = $(this).attr("data-replyNum");
		
		if( isVisible ) {
			$tr.hide();
		} else {
			$tr.show();
			
			// 답글 리스트
			listReplyAnswer(replyNum);
			
			// 답글 개수
			countReplyAnswer(replyNum);
		}
		
	});
})

// 답글 등록 버튼
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
		
		var url = "${pageContext.request.contextPath}/trade/insertReplyAnswer.do";
		var query = "num="+num+"&content="+content+"&answer="+replyNum;
		
		var fn = function(data) {
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

// 답글 삭제
$(function(){
	$("body").on("click", ".deleteReplyAnswer", function(){
		if(! confirm("게시글을 삭제 하시겠습니까 ?")) {
			return false;
		}
		
		var replyNum = $(this).attr("data-replyNum");
		var answer = $(this).attr("data-answer");
		
		var url = "${pageContext.request.contextPath}/trade/deleteReplyAnswer.do";
		var query = "replyNum=" + replyNum;
		
		var fn = function(data) {
			listReplyAnswer(answer);
			countReplyAnswer(answer);
		};
		ajaxFun(url, "post", query, "json", fn);
	});
});

</script>
</head>
<body class="is-preload">
	<!--  Wrapper -->
	<div id="wrapper">
	
		<!-- Main -->
		<div id="main">
			<div class="inner">

				<!-- Header -->
				<header id="header">
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"/>
				</header>
				
				<!-- Section -->						
				<div class="title" style="margin:30px 0 30px 0">
					<h1>중고거래</h1>
				</div>
				<table class="table table-border table-article">
					<tr>
						<td colspan="2" align="center">
							<span style = "color: tomato;">[${dto.type}]</span> ${dto.subject}
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
						<td  colspan="2" >
							금액 : 
							<c:if test="${dto.pay != ''}">
								${dto.pay}원							
							</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2" valign="top" height="200">
							${dto.content}
						</td>
					</tr>
					
					<c:if test="${listFile.size() > 0 }">
						<tr>
							<td colspan="2" height="200" style="padding:0; padding-top:10px">
								<div class="img-box">
									<c:forEach var="vo" items="${listFile}">
										<img src="${pageContext.request.contextPath}/uploads/trade/${vo.imageFilename}"
											onclick="imageViewer('${pageContext.request.contextPath}/uploads/trade/${vo.imageFilename}');">
									</c:forEach>
								</div>
							</td>	
						</tr>
					</c:if>
				</table>
				
				<table class="table">
					<tr>
						<td width="50%">
							<c:choose>
								<c:when test="${sessionScope.member.userId==dto.userId}">
									<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/trade/update.do?num=${dto.num}&page=${page}';">수정</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="btn" disabled="disabled">수정</button>
								</c:otherwise>
							</c:choose>
					    	
							<c:choose>
					    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin' || dto.userId=='admin'}">
					    			<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
					    		</c:when>
					    		<c:otherwise>
					    			<button type="button" class="btn" disabled="disabled">삭제</button>
					    		</c:otherwise>
					    	</c:choose>
						</td>
						<td align="right">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/trade/list.do?${query}';">리스트</button>
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
									<textarea class='boxTA' name="content" style="resize:none"></textarea>
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
		</div>
		
	<!-- Sidebar -->
	<div id="sidebar">
		<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
	</div>
			
</div>
	<div class="dialog-photo">
      <div class="photo-layout"></div>
</div>
	<!-- Scripts -->	
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp"/>
</body>
</html>