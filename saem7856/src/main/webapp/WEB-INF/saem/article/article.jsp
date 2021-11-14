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

table tbody tr:nth-child(2n + 1) {
	background-color: white;
}

img {display:block; margin:0px auto;}

</style>
<script type="text/javascript">
<c:if test="${sessionScope.member.userId=='admin'}">
function deleteArticle() {
    if(confirm("게시글을 삭제 하시겠습니까 ? ")) {
	    var query = "num=${dto.num}&${query}";
	    var url = "${pageContext.request.contextPath}/article/delete.do?" + query;
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

// 좋아요
$(function(){
	$(".btnSendArticleLike").click(function(){
		var uid = "${empty sessionScope.member?'false':'true'}";
		if(uid=="false") {
			alert("로그인 하셔야합니다.");
			return false;
		}
		
		var $i = $(this).find("i");
		var isNoLike = $i.css("color") == "rgb(0, 0, 0)";
		var msg = isNoLike ? "기사에 좋아요를 누르시겠습니까?" : "좋아요를 취소하시겠습니까?";
		
		if(! confirm( msg )) {
			return false;
		}
		
		
		var url = "${pageContext.request.contextPath}/article/insertArticleLike.do";
		var num = "${dto.num}";
		// var query = {num:num, isNoLike:isNoLike};
		var query = "num=" + num + "&isNoLike=" + isNoLike;;

		var fn = function(data) {
			var state = data.state;

			if(state === "true") {
				var color = "black";
				if( isNoLike ) {
					color = "#F56A6A";
				}
				$i.css("color", color);
				
				var count = data.articleLikeCount;
				$("#articleLikeCount").text(count);
			} else if(state === "liked") {
				alert("좋아요는 한 번만 가능합니다.");
			}
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
				
					<div class = "title-body" style="margin:30px 0 30px 0">
						<h1>NEWS</h1>
					</div>
					
					<table class="table table-border table-article">
						<tr style="background-color:rgba(230, 235, 237, 0.25)">
							<td colspan="2" align="center">
								<b>${dto.subject}</b>
							</td>
						</tr>
						
						<tr>
							<td style="text-align: right">
								${dto.reg_date} | 조회 ${dto.hitCount}
							</td>
						</tr>
						
						<tr style="border-bottom: none;">
							<td colspan="2" style="padding-bottom: 0; margin: 0 auto;">
								<c:if test="${not empty dto.imageFilename}">
								<img src="${pageContext.request.contextPath}/uploads/articlePhoto/${dto.imageFilename}">
								</c:if>
							</td>
						</tr>
						<tr style="border:none;">
							<td colspan="2">
								${dto.content}
							</td>
						</tr>
						
						<tr style="border:none">
							<td colspan="2" align="center" style="padding-bottom: 20px;">
								<button type="button" class="btn btnSendArticleLike" title="좋아요"><i class="fas fa-thumbs-up" style="color: ${isUserLike?'#F56A6A':'black'}"></i>&nbsp;&nbsp;<span id="articleLikeCount">${dto.articleLikeCount}</span></button>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								링&nbsp;&nbsp;&nbsp;크&nbsp;&nbsp;: 
								<a href="${dto.link}" target="_blank">${dto.link}</a>
							</td>
						</tr>
						<tr>
						<td colspan="2">
							이전글 :
							<c:if test="${not empty preReadDto}">
								<a href="${pageContext.request.contextPath}/article/article.do?${query}&num=${preReadDto.num}">${preReadDto.subject}</a>
							</c:if>
						</td>
						</tr>
						
						<tr>
							<td colspan="2" style="background-color: white;">
							다음글 : 
							<c:if test="${not empty nextReadDto}">
								<a href="${pageContext.request.contextPath}/article/article.do?${query}&num=${nextReadDto.num}">${nextReadDto.subject}</a>
							</c:if>
							</td>
						</tr>
					</table>				
					<table>
						<tr style="border:none;">
							<td style="float: left">
								<c:choose>
									<c:when test="${sessionScope.member.userId=='admin'}">
										<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/article/update.do?num=${dto.num}&page=${page}';">수정</button>
									</c:when>
								</c:choose>
								<c:choose>
									<c:when test="${sessionScope.member.userId=='admin'}">
										<button type="button" class="btn" onclick="deleteArticle()">삭제</button>
									</c:when>
								</c:choose>
							</td>
							<td style="float:right">
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/article/list.do?page=${page}';">리스트</button>	
							</td>
						</tr>
					</table>
			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>