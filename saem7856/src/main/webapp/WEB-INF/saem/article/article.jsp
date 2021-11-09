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
				
					<div class = "title-body">
						<span class="article-title"> NEWS </span>
					</div>
					
					<table class="table table-border table-article">
						<tr>
							<td colspan="2" align="center">
								<b>${dto.subject}</b>
							</td>
						</tr>
						
						<tr>
							<td style="text-align: right">
								${dto.reg_date} | 조회 ${dto.hitCount}
							</td>
						</tr>
						
						<tr style="background-color:white; border-bottom: none;">
							<td colspan="2" style="padding-bottom: 0; margin: 0 auto;">
								<c:if test="${not empty dto.imageFilename}">
								<img src="${pageContext.request.contextPath}/uploads/articlePhoto/${dto.imageFilename}">
								</c:if>
							</td>
						</tr>
						<tr style="background-color: white; border:0px;">
							<td colspan="2">
								${dto.content}
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
								링&nbsp;&nbsp;&nbsp;크 : 
								<a href="${dto.link}">${dto.link}</a>
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
						<tr>
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