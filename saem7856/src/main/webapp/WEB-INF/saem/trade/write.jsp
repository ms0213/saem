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
<script type="text/javascript">
function sendOk() {
    var f = document.boardForm;
	var str;
	
    str = f.subject.value.trim();
    if(!str) {
        f.subject.focus();
        return;
    }

    str = f.content.value.trim();
    if(!str) {
        f.content.focus();
        return;
    }


    if($(':radio[name="tradeType"]:checked').length < 1){
		$("#구매").focus();
    	//$(":radio[id='구매']").prop("checked", true);
		return;
    }
    
    f.action = "${pageContext.request.contextPath}/trade/${mode}_ok.do";
    f.submit();
}

<c:if test="${mode=='update'}">
function deleteFile(fileNum) {
	if(! confirm("이미지를 삭제 하시겠습니까 ?")) {
		return;
	}
	
	var query = "num=${dto.num}&fileNum=" + fileNum + "&page=${page}";
	var url = "${pageContext.request.contextPath}/trade/deleteFile.do?" + query;
	location.href = url;
	}
</c:if>
</script>
<style type="text/css">
label{
	margin:0;
}
textarea{
	resize: none;
	height: 450px;
}
</style>
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
				
				<div class="title" style="margin:30px 0 30px 0">
					<h1>중고거래</h1>
				</div>
			
				<form name="boardForm" method="post" enctype="multipart/form-data">
					<table class="table table-border table-form">
						<tr> 
							<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
							<td> 
								<input type="text" name="subject" maxlength="100" class="boxTF" value="${dto.subject}">
							</td>
						</tr>
						
						<tr> 
							<td>작성자</td>
							<td> 
								<p style="margin:0;">${sessionScope.member.userName}</p>
							</td>
						</tr>
						<tr>
							<td>금액</td>
							<td>
								<input type="text" name="pay" class="boxTF" value="${dto.pay}" style="width:20%; display:inline"><span>&nbsp;&nbsp;원</span>
							</td>
						</tr>
						<tr>
							<td>구&nbsp;&nbsp;&nbsp;&nbsp;분</td>
							<td>
								<input type="radio" name="tradeType" value="구매" id="구매"><label for="구매" >구매</label>
								<input type="radio" name="tradeType" value="판매"  id="판매"><label for="판매" >판매</label>
								<input type="radio" name="tradeType"  value="완료" id="거래완료"><label for="거래완료" >거래완료</label>
							</td>
						</tr>
						<tr>
							<td>이미지</td>
							<td> 
								<input type="file" name="selectFile" accept="image/*" multiple="multiple" class="boxTF">
							</td>
						</tr>
						<c:if test="${mode=='update'}">
							<tr>
								<td>등록이미지</td>
								<td> 
									<div class="img-box">
										<c:forEach var="vo" items="${listFile}">
											<img src="${pageContext.request.contextPath}/uploads/trade/${vo.imageFilename}"
												onclick="deleteFile('${vo.fileNum}');">
										</c:forEach>
									</div>
								</td>
							</tr>
						</c:if>
						<tr> 
							<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
							<td> 
								<textarea name="content" class="boxTA">${dto.content}</textarea>
							</td>
						</tr>
						

					</table>
						
					<table class="table">
						<tr> 
							<td align="center">
								<button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
								<button type="reset" class="btn">다시입력</button>
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/trade/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
								<c:if test="${mode=='update'}">
									<input type="hidden" name="num" value="${dto.num}">
									<input type="hidden" name="page" value="${page}">
								</c:if>
							</td>
						</tr>
					</table>
				</form>
			
			</div>
		</div>
		
		<!-- Sidebar -->
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>
	</div>
	
	<!-- Scripts -->	
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp"/>
</body>
</html>