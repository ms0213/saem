<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<div class="inner">

		<!-- Search -->
		<h3 style="padding-bottom:20px; margin-bottom: 20px">saem7856 withsoccer</h3>

		<!-- Menu -->
		<nav id="menu">
			<header class="major" style="margin-top:0; padding-top:10px"> 
				<h2>Menu</h2>
			</header>
			<ul>
				<li><a href="${pageContext.request.contextPath}/">Homepage</a></li>
				<li><a href="${pageContext.request.contextPath}/about.jsp">About us</a></li>
				<li><a href="${pageContext.request.contextPath}/player/list.do">Players</a></li>
				<li><span class="opener">News</span>
					<ul>
						<li><a href="${pageContext.request.contextPath}/fixture/month.do">일정</a></li>
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
						<li><a href="${pageContext.request.contextPath}/contact/write.do">contact us</a></li>
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
						<li><a href="${pageContext.request.contextPath}/contact/list.do">건의 관리</a></li>
					</ul></li>
				</c:if>	
			</ul>
		</nav>
		
				<!-- Section -->
		<section>
			<header class="major">
				<h2>Get in touch</h2>
			</header>
			<ul class="contact">
				<li class="icon solid fa-envelope"><a href="#">saem7856@semi.com</a></li>
				<li class="icon solid fa-phone">(000) 000-0000</li>
				<li class="icon solid fa-home">서울특별시 마포구 서교동 447-5<br />
					매일 00:00~24:00
				</li>
			</ul>
		</section>
	</div>
