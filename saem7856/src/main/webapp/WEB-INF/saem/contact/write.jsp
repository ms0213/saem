<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>saem 7856</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp"/>
<script type="text/javascript">
	function sendOk(){
		var f = document.contactForm;
		
		f.action = "${pageContext.request.contextPath}/contact/write_ok.do";
		f.submit();
		alert('Thank you for your comments.👻 \n We will confirm and get back to you.');	
	}

</script>
<style type="text/css">

#main{
	margin-bottom: 50px;
}

#contact-body ul{
	list-style: none;
	width:100%;
	margin-bottom:15px;
}

textarea{
	resize : none;
	height: 300px;
}

li{

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
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"/>			
				</header>

				<div class="title" style="margin:30px 0 30px 0">
					<h1>Contact Us</h1>
				</div>
				
				<form name ="contactForm" method="post">
					<div id="contact-body">
						<div>
							<ul style="float: left; width: 400px;">
								<li class="col-title">First Name</li>
								<li class="col-input" >
									<input type="text" style="width: 90%;" name="firstName" id="firstName" required="required" placeholder="First Name" style="width: 40%;">
								</li>
							</ul>
							<ul style="float: left; width: 400px;">
								<li class="col-title">Last Name</li>
								<li class="col-input">
									<input type="text" style="width: 90%;" name="lastName" id="lastName" required="required" placeholder="Last Name"style="width:40%;">
								</li>
							</ul>
						</div>
						<ul style="clear: both;">
							<li class="col-title">League</li>
							<li class="col-input">
								<select name="league" class="selectField" style="width:50%">
									<option value=""> </option>
									<option value="Bundesliga">Bundesliga</option>
									<option value="EPL">EPL</option>
									<option value="LaLiga">La Liga</option>
									<option value="LIGUE1">LIGUE1 Uber Eats</option>
									<option value="Russian">Russian Premier Liga</option>
									<option value="SuperLig">Süper Lig</option>
								</select>
							</li>
						</ul>
						<ul>
							<li class="col-title">Members</li>
							<li class="col-input">
								<select name="member" class="selectField" style="width:50%">
									<option value=""> </option>
									<option value="김민재">김민재</option>
									<option value="석현준">석현준</option>
									<option value="손흥민">손흥민</option>
									<option value="석현준">석현준</option>
									<option value="이강인">이강인</option>
									<option value="이재성">이재성</option>
									<option value="정우영">정우영</option>
									<option value="황의조">황의조</option>
									<option value="황인범">황인범</option>
								</select>
							</li>
						</ul>
						<ul>
							<li class="col-title">Email Address *</li>
							<li class="col-input">
								<input type="email" name="email" id="email" placeholder="Email" style="width:60%;" required="required">
							</li>
						</ul>
						<ul>
							<li class="col-title">Leave Us a comment *</li>
							<li class="col-input">
								<textarea name="comments" placeholder="We'd love to hear from you." required="required" ></textarea>
							</li>
						</ul>
					</div>
					<div class="form-footer">
						<button type="button" class="btn" onclick="sendOk()" style="float:right">Send</button>
						<button type="reset" class="btn" style="margin-right:15px; float:right" >Reset</button>
					</div>
				</form>
			</div>
		</div>
		
		<!-- Sidebar -->
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp"/>
</body>
</html>