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
.table-article tr > td {
	padding-left: 5px; padding-right: 5px; vertical-align: top;
}
.table-article tr > td div {
	height: 100% !important;
}
.table-article .img {
	max-width:100%; height:auto; resize:both;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/ckeditor5/ckeditor.js"></script>

<script type="text/javascript">
<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
	function deletePhoto() {
	    if(confirm("게시글을 삭제 하시 겠습니까 ? ")) {
		    var query = "num=${dto.num}&page=${page}";
		    var url = "${pageContext.request.contextPath}/player/delete.do?" + query;
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
<main>
	<div class="body-container">
		<div class="body-title">
			<h3><i class="fas fa-futbol"></i> 선수 정보 </h3>
		</div>
        
		<table class="table table-border table-article">
			<colgroup>
				<col width="30%" />
				<col width="70%" />
			</colgroup>
		
			<tr>
				<td colspan="2" align="center">
					${dto.subject}
				</td>
			</tr>
			

			<tr>
				<td style="padding-top: 0;">
					<div class="editor">${dto.content}</div>
				</td>
			
				<td style="padding-top: 0;">
					<div class="editor2">${dto.content2}</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					리그 순위표
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-top: 0;">
					<div class="editor3">${dto.content3}</div>
				</td>
			</tr>
		</table>
		
		<table class="table">
			<tr>
				<td width="50%">
					<c:choose>
						<c:when test="${sessionScope.member.userId==dto.userId}">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/player/update.do?num=${dto.num}&page=${page}';">수정</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn" disabled="disabled">수정</button>
						</c:otherwise>
					</c:choose>
			    	
					<c:choose>
			    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
			    			<button type="button" class="btn" onclick="deletePhoto();">삭제</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">삭제</button>
			    		</c:otherwise>
			    	</c:choose>
				</td>
				<td align="right">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/player/list.do?page=${page}';">선수목록</button>
				</td>
			</tr>
		</table>
        
	</div>
</main>

<script type="text/javascript">
ClassicEditor
	.create( document.querySelector( '.editor' ), {
	})
	.then( editor => {
		window.editor = editor;
		editor.isReadOnly = true;
		editor.ui.view.top.remove( editor.ui.view.stickyPanel );
	} );
	
ClassicEditor
	.create( document.querySelector( '.editor2' ), {
	})
	.then( editor => {
		window.editor = editor;
		editor.isReadOnly = true;
		editor.ui.view.top.remove( editor.ui.view.stickyPanel );
	} );
	
ClassicEditor
	.create( document.querySelector( '.editor3' ), {
	})
	.then( editor => {
		window.editor = editor;
		editor.isReadOnly = true;
		editor.ui.view.top.remove( editor.ui.view.stickyPanel );
	} );
</script> 
			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>