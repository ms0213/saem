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

<script type="text/javascript">
function sendOk() {
    var f = document.articleForm;
	var str;
	
    str = f.subject.value.trim();
    if(!str) {
        alert("제목을 입력하세요. ");
        f.subject.focus();
        return;
    }

    str = f.content.value.trim();
    if(!str) {
        alert("내용을 입력하세요. ");
        f.content.focus();
        return;
    }
    
    str = f.link.value.trim();
    if(!str) {
        alert("기사 링크를 입력하세요.");
        f.link.focus();
        return;
    }
    
    var mode = "${mode}";
    if( (mode === "write") && (!f.selectFile.value) ) {
        alert("이미지 파일을 추가 하세요. ");
        f.selectFile.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/article/${mode}_ok.do";
    f.submit();
}
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
				<section>
					<div class = "title-body">
						<span class="article-title"> NEWS </span>
					</div>
					
					<form name="articleForm" method="post" enctype="multipart/form-data">
						<table class="table table-border table-form">
							<tr>
								<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
								<td> 
									<input type="text" name="subject" maxlength="100" class="boxTF" value='${dto.subject}'>
								</td>					
							</tr>
							
							<tr>
								<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
								<td>
									<textarea name="content" class="boxTA">${dto.content}</textarea>
								</td>
							</tr>
							
							<tr>
								<td>이미지</td>
								<td>
									<input type="file" name="selectFile" accept="image/*" class="boxTF">
								</td>
							</tr>
							
							<tr>
								<td> 링&nbsp;&nbsp;&nbsp;&nbsp;크 </td>
								<td>
									<input type="text" name="link" maxlength="500" class="boxTF" value="${dto.link}">
								</td>
							</tr>
							
							<c:if test="${mode=='update'}">
								<tr>
									<td>등록이미지</td>
									<td>
										<p>
											<img src="${pageContext.request.contextPath}/uploads/articlephoto/${dto.imageFilename}" class="img">
											<span class="info">(새로운 이미지가 등록되면 기존 이미지는 삭제됩니다.)</span>
										</p>
									</td>
								</tr>
							</c:if>
						</table>
						
						<table class="table">
							<tr>
								<td align="center">
									<button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
									<button type="reset" class="btn">다시입력</button>
									<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/article/list.do';">등록취소</button>
									
									<c:if test="${mode=='update'}">
										<input type="hidden" name="num" value="${dto.num}">
										<input type="hidden" name="imageFilename" value="${dto.imageFilename}">
										<input type="hidden" name="page" value="${page}">
									</c:if>
									
									
									
								</td>
							</tr>
						</table>
					</form>
						
						
						
				</section>
			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>