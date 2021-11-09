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
#member h3{
  font-family: "고딕","나눔고딕","나눔 고딕", "맑은 고딕", sans-serif;
}
#member{
	width:100%;
	height : 200px;
	display: flex;
	flex-direction: row;
	justify-content: space-between;
}

.sections{
	width : 20%;
	height :100%;
	text-align : center;
	margin-bottom:30px;
	flex-wrap:nowrap;
	background-repeat: no-repeat ;
	border : 1px solid gray;
}

.sections .section-image{
	width:100%; 
	height:180px; 
	margin-bottom:25px;
}
.right{
	margin-right:5px;
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
				
				<!-- Content -->
					<section>
						<header class="main">
							<h1>About Us</h1>
						</header>

						<span class="image main"><img src="images/son.jpg" alt="" /></span>

						<p>with soccer 는 해외 축구 리그를 뛰고 있는 한국 선수들을 소개하는 페이지 입니다.</p>
						<hr class="major" />

						<h2>Member</h2>
						<p></p>
						<div id="member">
							<div class="sections right">
								<div class="section-image" style="background:url('images/min.png'); background-size: cover;"></div>
								<h3>김민선</h3>
								<p>어쩌고</p>
							</div>
							<div class="sections right">
								<div class="section-image" style="background:url('images/ye.png'); background-size: cover;"></div>
								<h3>김예림</h3>
								<p>어쩌고</p>
							</div>
							<div class="sections right">
								<div class="section-image" style="background:url('images/jun.png'); background-size: cover;"></div>
								<h3>박준호</h3>
								<p>어쩌고</p>
							</div>
							<div class="sections right">
								<div class="section-image" style="background:url('images/tae.png'); background-size: cover;"></div>
								<h3>박태훈</h3>
								<p>어쩌고</p>
							</div>							
							<div class="sections">
								<div class="section-image" style="background:url('images/sora.png'); background-size: cover;"></div>
								<h3>이소라</h3>
								<p>어쩌고</p>
							</div>							
						</div>						
					</section>
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