<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

		<a href="${pageContext.request.contextPath}/" class="logo"><strong>Saem7856</strong> by
			WithSoccer</a>
		<c:if test="${empty sessionScope.member}">
		<ul class="icons">
			<li><a href="${pageContext.request.contextPath}/member/login.do" style="border-bottom: none;"><span class="label">로그인</span></a></li>
			<li><a href="${pageContext.request.contextPath}/member/signup.do" style="border-bottom: none;"><span class="label">회원가입</span></a></li>
		</ul>
		</c:if>
		<c:if test="${not empty sessionScope.member}">
		<ul class="icons">
			<li><span>${sessionScope.member.userName}</span>님</li>
			<li><a href="${pageContext.request.contextPath}/member/logout.do"  style="border-bottom: none;"><span class="label">로그아웃</span></a></li>
			<li><a href="${pageContext.request.contextPath}/member/pwd.do?mode=update" style="border-bottom: none;"><span class="label">정보수정</span></a></li>
		</ul>		
		</c:if>