<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
				<li><a href="${pageContext.request.contextPath}/">Homepage</a></li>
				<li><a href="${pageContext.request.contextPath}/aboutUs.html">About us</a></li>
				<li><a href="${pageContext.request.contextPath}/player/list.do">Players</a></li>
				<li><span class="opener">News</span>
					<ul>
						<li><a href="${pageContext.request.contextPath}/fixture/list.do">일정</a></li>
						<li><a href="${pageContext.request.contextPath}/article/list.do">기사</a></li>
						<li><a href="${pageContext.request.contextPath}/photo/list.do">photo</a></li>
					</ul></li>
				<li><span class="opener">Community</span>
					<ul>
						<li><a href="${pageContext.request.contextPath}/bbs/list.do">자유게시판</a></li>
						<li><a href="${pageContext.request.contextPath}/trade/list.do">중고거래</a></li>
						<li><a href="${pageContext.request.contextPath}/goods/list.do">굿즈</a></li>
					</ul></li>
				<li><span class="opener">Contact</span>
					<ul>
						<li><a href="${pageContext.request.contextPath}/announce/list.do">공지사항</a></li>
						<li><a href="#">contact us</a></li>
					</ul></li>
				<c:if test="${sessionScope.member.userId=='admin'}">	
				<li><span class="opener">Admin</span>
					<ul>
						<li><a href="${pageContext.request.contextPath}/announce/write.do">공지사항 등록</a></li>
						<li><a href="${pageContext.request.contextPath}/bbs/notice.do">자유게시판 공지사항 등록</a></li>
						<li><a href="${pageContext.request.contextPath}/goods/write.do">굿즈 등록</a></li>
						<li><a href="${pageContext.request.contextPath}/photo/write.do">사진 등록</a></li>
						<li><a href="${pageContext.request.contextPath}/article/write.do">기사 등록</a></li>
						<li><a href="${pageContext.request.contextPath}/player/write.do">선수 등록</a></li>
					</ul></li>
				</c:if>	
			</ul>
		</nav>
	</div>
