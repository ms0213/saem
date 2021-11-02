<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

	<div class="inner">

		<!-- Search -->
		<section id="search" class="alt">
			<form method="post" action="#">
				<input type="text" name="query" id="query" placeholder="Search" />
			</form>
		</section>

		<!-- Menu -->
		<nav id="menu">
			<header class="major">
				<h2>Menu</h2>
			</header>
			<ul>
				<li><a href="index.html">Homepage</a></li>
				<li><a href="./aboutUs.html">About us</a></li>
				<li><span class="opener">League</span>
					<ul>
						<li><a href="#">EPL</a></li>
						<li><a href="#">라리가</a></li>
						<li><a href="#">분데스리가</a></li>
						<li><a href="#">리그1</a></li>
						<li><a href="#">러시아 프리미어리그</a></li>
						<li><a href="#">쉬페르리그</a></li>
					</ul></li>
				<li><span class="opener">News</span>
					<ul>
						<li><a href="#">일정</a></li>
						<li><a href="#">기사</a></li>
						<li><a href="#">photo</a></li>
					</ul></li>
				<li><span class="opener">Community</span>
					<ul>
						<li><a href="#">자유게시판</a></li>
						<li><a href="#">중고거래</a></li>
						<li><a href="#">굿즈</a></li>
					</ul></li>
				<li><span class="opener">Contact</span>
					<ul>
						<li><a href="#">공지사항</a></li>
						<li><a href="#">contact us</a></li>
					</ul></li>
				<li><span class="opener">Admin</span>
					<ul>
						<li><a href="#">Lorem Dolor</a></li>
						<li><a href="#">Ipsum Adipiscing</a></li>
						<li><a href="#">Tempus Magna</a></li>
						<li><a href="#">Feugiat Veroeros</a></li>
					</ul></li>
			</ul>
		</nav>


		<!-- Section -->
		<section>
			<header class="major">
				<h2>Get in touch</h2>
			</header>
			<ul class="contact">
				<li class="icon solid fa-envelope"><a href="#">information@untitled.tld</a></li>
				<li class="icon solid fa-phone">(000) 000-0000</li>
				<li class="icon solid fa-home">1234 Somewhere Road #8254<br />
					Nashville, TN 00000-0000
				</li>
			</ul>
		</section>

		<!-- Footer -->
		<footer id="footer">
			<p class="copyright">
				&copy; Untitled. All rights reserved. Demo Images: <a
					href="https://unsplash.com">Unsplash</a>. Design: <a
					href="https://html5up.net">HTML5 UP</a>.
			</p>
		</footer>

	</div>

</body>
</html>