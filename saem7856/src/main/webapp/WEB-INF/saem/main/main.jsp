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
.content .item {
    cursor: pointer;
	color: black; font-weight: bold; font-size: 20px;
	
}
.features img {
  width: 200px; height: 300px; cursor: pointer;
}

.major {
	padding-top: 100px;
}

</style>
</head>
<body class="is-preload">

	<!-- Wrapper -->
	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">

				<!-- Header -->
				<header id="header">
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"></jsp:include>
				</header>

				<!-- Banner -->
				<section id="banner" style="padding: 0;">
					<video autoplay muted loop style="width: 100%; height: 100%;" class="content">
						<!--크롬 자동 재생 정책으로 비디오 오디오 따로-->
						<source src="./images/goal.mp4" type="video/webm">
					</video>
				</section>

				<!-- Section -->
				<section>
					<header class="major">
					<p style="border-top : solid 2px rgba(210, 215, 217, 0.75);"></p>
						<h2>최신 기사!</h2>
					</header>
					<div class="features">
						<c:forEach var="dto" items="${list}" varStatus="status">
							<article>
								<span>
									<img src="${pageContext.request.contextPath}/uploads/articlePhoto/${dto.imageFilename}">
								</span>
								<div class="content">
									<div class="item"
									onclick="location.href='${pageContext.request.contextPath}/article/article.do?page=1&num=${dto.num}';">${dto.subject}</div>
									<p>${dto.content}</p>
								</div>
							</article>	
						</c:forEach>					
					</div>
				</section>

				<!-- Section -->
				<section>
					<header class="major">
						<h2>Ipsum sed dolor</h2>
					</header>
					<div class="posts">
						<article>
							<a href="#" class="image"><img src="images/pic01.jpg" alt="" /></a>
							<h3>Interdum aenean</h3>
							<p>Aenean ornare velit lacus, ac varius enim lorem
								ullamcorper dolore. Proin aliquam facilisis ante interdum. Sed
								nulla amet lorem feugiat tempus aliquam.</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/pic02.jpg" alt="" /></a>
							<h3>Nulla amet dolore</h3>
							<p>Aenean ornare velit lacus, ac varius enim lorem
								ullamcorper dolore. Proin aliquam facilisis ante interdum. Sed
								nulla amet lorem feugiat tempus aliquam.</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/pic03.jpg" alt="" /></a>
							<h3>Tempus ullamcorper</h3>
							<p>Aenean ornare velit lacus, ac varius enim lorem
								ullamcorper dolore. Proin aliquam facilisis ante interdum. Sed
								nulla amet lorem feugiat tempus aliquam.</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/pic04.jpg" alt="" /></a>
							<h3>Sed etiam facilis</h3>
							<p>Aenean ornare velit lacus, ac varius enim lorem
								ullamcorper dolore. Proin aliquam facilisis ante interdum. Sed
								nulla amet lorem feugiat tempus aliquam.</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/pic05.jpg" alt="" /></a>
							<h3>Feugiat lorem aenean</h3>
							<p>Aenean ornare velit lacus, ac varius enim lorem
								ullamcorper dolore. Proin aliquam facilisis ante interdum. Sed
								nulla amet lorem feugiat tempus aliquam.</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/pic06.jpg" alt="" /></a>
							<h3>Amet varius aliquam</h3>
							<p>Aenean ornare velit lacus, ac varius enim lorem
								ullamcorper dolore. Proin aliquam facilisis ante interdum. Sed
								nulla amet lorem feugiat tempus aliquam.</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
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