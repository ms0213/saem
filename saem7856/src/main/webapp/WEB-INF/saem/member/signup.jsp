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
#tel1 {
	margin : 0;
}

#tel2 {
	margin: 0;
}

input[type=text] {
	display: inline-block;
}

input[type="text"], input[type="password"], input[type="email"], input[type="tel"], input[type="search"], input[type="url"], select, textarea {
	margin : 0 0 10px 0;
}


input[type="date"] {
	height: 2.75em;
	border-radius: 0.375em;
	border: solid 1px rgba(210, 215, 217, 0.75);
}

select {
	width: 20%;
	display : inline-block;
	margin : 0;
}

#tel1 {
	width: 18%;
}

p {
margin : 0;
}

td {
vertical-align: middle;
}

#name, #addr2, #email, #tel, #zip {
 margin: 0;
}
</style>

<script type="text/javascript">
function memberOk() {
	var f = document.memberForm;
	var str;

	str = f.userId.value;
	if( !/^[a-z][a-z0-9_]{4,9}$/i.test(str) ) { 
		alert("아이디를 다시 입력 하세요. ");
		f.userId.focus();
		return;
	}

	var mode = "${mode}";
	if(mode === "signup" && f.userIdValid.value === "false") {
		alert("아이디 중복 검사가 실행되지 않았습니다.");
		f.userId.focus();
		return;
	}
	
	str = f.userPwd.value;
	if( !/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i.test(str) ) { 
		alert("패스워드를 다시 입력 하세요. ");
		f.userPwd.focus();
		return;
	}

	if( str != f.userPwd2.value ) {
        alert("패스워드가 일치하지 않습니다. ");
        f.userPwd.focus();
        return;
	}
	
    str = f.userName.value;
    if( !/^[가-힣]{2,5}$/.test(str) ) {
        alert("이름을 다시 입력하세요. ");
        f.userName.focus();
        return;
    }

    str = f.birth.value;
    if( !str ) {
        alert("생년월일를 입력하세요. ");
        f.birth.focus();
        return;
    }
    
    str = f.tel1.value;
    if( !str ) {
        alert("전화번호를 입력하세요. ");
        f.tel1.focus();
        return;
    }

    str = f.tel2.value;
    if( !/^\d{3,4}$/.test(str) ) {
        alert("숫자만 가능합니다. ");
        f.tel2.focus();
        return;
    }

    str = f.tel3.value;
    if( !/^\d{4}$/.test(str) ) {
    	alert("숫자만 가능합니다. ");
        f.tel3.focus();
        return;
    }
    
    str = f.email1.value.trim();
    if( !str ) {
        alert("이메일을 입력하세요. ");
        f.email1.focus();
        return;
    }

    str = f.email2.value.trim();
    if( !str ) {
        alert("이메일을 입력하세요. ");
        f.email2.focus();
        return;
    }

   	f.action = "${pageContext.request.contextPath}/member/${mode}_ok.do";
    f.submit();
}

function changeEmail() {
    var f = document.memberForm;
	    
    var str = f.selectEmail.value;
    if(str!="direct") {
        f.email2.value=str; 
        f.email2.readOnly = true;
        f.email1.focus(); 
    }
    else {
        f.email2.value="";
        f.email2.readOnly = false;
        f.email1.focus();
    }
}

//아이디 중복 검사
function userIdCheck() {
	var userId=$("#userId").val();
	
	if(!/^[a-z][a-z0-9_]{4,9}$/i.test(userId)) { 
		var str = "아이디는 5~10자 이내이며, 첫글자는 영문자로 시작해야 합니다.";
		$("#userId").focus();
		$("#userId").parent().next(".help-block").html(str);
		return;
	}
	
	var url = "${pageContext.request.contextPath}/member/userIdCheck.do";
	var query = "userId=" + userId;
	$.ajax({
		type:"POST"
		,url:url
		,data:query
		,dataType:"json"
		,success:function(data) {
			var passed = data.passed;
			
			if(passed === "true") {
				var str = "<span style='color:blue; font-weight: bold;'>" + userId + "</span> 아이디는 사용가능 합니다.";
				$("#userId").parent().next(".help-block").html(str);
				$("#userIdValid").val("true");
			} else {
				var str = "<span style='color:red; font-weight: bold;'>" + userId + "</span> 아이디는 사용할수 없습니다.";
				$("#userId").parent().next(".help-block").html(str);
				$("#userId").val("");
				$("#userIdValid").val("false");
				$("#userId").focus();
			}
		}
	});
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
					<div class="title-body">
						<span class="article-title">${title}</span>
					</div>
					
						<div class="body-container">
							<form name="memberForm" method="post">
							<table class = "table table-border table-form">
								<tr>
									<td style="text-align: center">아&nbsp;이&nbsp;디</td>
									<td>
									<p>
										<input type="text" name="userId" id="userId" maxlength="10" class="boxTF" value="${dto.userId}" style="width: 50%;" ${mode=="update" ? "readonly='readonly' ":""}>
											<c:if test="${mode=='signup'}">
												<button type="button" class="btn" onclick="userIdCheck();">중복검사</button>
											</c:if>
									</p>
										<p class="help-block">아이디는 5~10자 이내이며, 첫글자는 영문자로 시작해야합니다.</p>
									</td>
								</tr>
								
								<tr>
									<td style="text-align: center">패스워드</td>
									<td>
										<p>
											<input type="password" name="userPwd" class="boxTF" maxlength="10" style="width: 50%;">
										</p>
										<p class="help-block">패스워드는 5~10자 이내이며, 하나 이상의 숫자나 특수문자가 포함되어야 합니다.</p>
									</td>
								</tr>
								<tr>
									<td style="text-align: center">패스워드 확인</td>
									<td>
										<p>
											<input type="password" name="userPwd2" class="boxTF" maxlength="10" style="width: 50%;">
										</p>
										<p class="help-block">패스워드를 한번 더 입력해주세요.</p>
									</td>
								</tr>
		
								<tr>
									<td  style="text-align: center">이&nbsp;&nbsp;&nbsp;&nbsp;름</td>
									<td>
										<input type="text" id="name" name="userName" maxlength="10" class="boxTF" value="${dto.userName}" style="width: 50%;" ${mode=="update" ? "readonly='readonly' ":""}>
									</td>
								</tr>
							
								<tr>
									<td style="text-align: center">생년월일</td>
									<td>
										<input type="date" name="birth" class="boxTF" value="${dto.birth}" style="width: 50%;">
									</td>
								</tr>
							
								<tr>
									<td style="text-align: center">이 메 일</td>
									<td>
										  <select name="selectEmail" class="selectField" onchange="changeEmail();">
												<option value="">선 택</option>
												<option value="naver.com"   ${dto.email2=="naver.com" ? "selected='selected'" : ""}>네이버 메일</option>
												<option value="hanmail.net" ${dto.email2=="hanmail.net" ? "selected='selected'" : ""}>한 메일</option>
												<option value="gmail.com"   ${dto.email2=="gmail.com" ? "selected='selected'" : ""}>지 메일</option>
												<option value="hotmail.com" ${dto.email2=="hotmail.com" ? "selected='selected'" : ""}>핫 메일</option>
												<option value="direct">직접입력</option>
										  </select>
										  <input type="text" id="email" name="email1" maxlength="30" class="boxTF" value="${dto.email1}" style="width: 33%;"> @ 
										  <input type="text" id="email" name="email2" maxlength="30" class="boxTF" value="${dto.email2}" style="width: 33%;" readonly="readonly">
									</td>
								</tr>
								
								<tr>
									<td style="text-align: center">전화번호</td>
									<td>
										  <select name="tel1" class="selectField" id="tel1">
												<option value="">선 택</option>
												<option value="010" ${dto.tel1=="010" ? "selected='selected'" : ""}>010</option>
												<option value="02"  ${dto.tel1=="02"  ? "selected='selected'" : ""}>02</option>
												<option value="031" ${dto.tel1=="031" ? "selected='selected'" : ""}>031</option>
												<option value="032" ${dto.tel1=="032" ? "selected='selected'" : ""}>032</option>
												<option value="033" ${dto.tel1=="033" ? "selected='selected'" : ""}>033</option>
												<option value="041" ${dto.tel1=="041" ? "selected='selected'" : ""}>041</option>
												<option value="042" ${dto.tel1=="042" ? "selected='selected'" : ""}>042</option>
												<option value="043" ${dto.tel1=="043" ? "selected='selected'" : ""}>043</option>
												<option value="044" ${dto.tel1=="044" ? "selected='selected'" : ""}>044</option>
												<option value="051" ${dto.tel1=="051" ? "selected='selected'" : ""}>051</option>
												<option value="052" ${dto.tel1=="052" ? "selected='selected'" : ""}>052</option>
												<option value="053" ${dto.tel1=="053" ? "selected='selected'" : ""}>053</option>
												<option value="054" ${dto.tel1=="054" ? "selected='selected'" : ""}>054</option>
												<option value="055" ${dto.tel1=="055" ? "selected='selected'" : ""}>055</option>
												<option value="061" ${dto.tel1=="061" ? "selected='selected'" : ""}>061</option>
												<option value="062" ${dto.tel1=="062" ? "selected='selected'" : ""}>062</option>
												<option value="063" ${dto.tel1=="063" ? "selected='selected'" : ""}>063</option>
												<option value="064" ${dto.tel1=="064" ? "selected='selected'" : ""}>064</option>
												<option value="070" ${dto.tel1=="070" ? "selected='selected'" : ""}>070</option>
										  </select>
										  <input type="text" id="tel" name="tel2" maxlength="4" class="boxTF" value="${dto.tel2}" style="width: 33%;"> -
										  <input type="text" id="tel" name="tel3" maxlength="4" class="boxTF" value="${dto.tel3}" style="width: 33%;">
									</td>
								</tr>
							
								<tr>
									<td style="text-align: center">우편번호</td>
									<td>
										<input type="text" id="zip" name="zip" id="zip" maxlength="7" class="boxTF" value="${dto.zip}" readonly="readonly" style="width: 50%;">
										<button type="button" class="btn" onclick="daumPostcode();">우편번호검색</button>
									</td>
								</tr>
								
								<tr>
									<td  style="text-align: center" valign="top">주&nbsp;&nbsp;&nbsp;&nbsp;소</td>
									<td>
										<p>
											<input type="text" name="addr1" id="addr1" maxlength="50" class="boxTF" value="${dto.addr1}" readonly="readonly" style="width: 96%;">
										</p>
										<p class="block">
											<input type="text" name="addr2" id="addr2" maxlength="50" class="boxTF" value="${dto.addr2}" style="width: 96%;">
										</p>
									</td>
								</tr>
								
							</table>
							
							<table class="table">
								<c:if test="${mode=='signup'}">
									<tr style="border:none;">
										<td align="center"  style="background-color: white;">
											<div class="col-6 col-12-small">
												<input type="checkbox" id="terms" name="terms" checked>
												<label for="terms"> 약관에 동의하시겠습니까? </label>
												<a href="">약관보기</a>
											</div>
										</td>
									</tr>
								</c:if>
										
								<tr style="border:none; background-color: white;">
									<td align="center" style="border: none;">
									    <button type="button" class="btn" name="btnOk" onclick="memberOk();"> ${mode=="signup"?"회원가입":"정보수정"} </button>
									    <button type="reset" class="btn"> 다시입력 </button>
									    <button type="button" class="btn" 
									    	onclick="javascript:location.href='${pageContext.request.contextPath}/';"> ${mode=="signup"?"가입취소":"수정취소"} </button>
									    <input type="hidden" name="userIdValid" id="userIdValid" value="false">
									</td>
								</tr>
								
								<tr style="border:none; background-color: white;">
									<td align="center">
										<span class="msg-box">${message}</span>
									</td>
								</tr>
							</table>
							</form>
						</div>
				</section>
			</div>
		</div>
		
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    function daumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;

                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if(data.userSelectedType === 'R'){
                    //법정동명이 있을 경우 추가한다.
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zip').value = data.zonecode; //5자리 새우편번호 사용
                document.getElementById('addr1').value = fullAddr;

                // 커서를 상세주소 필드로 이동한다.
                document.getElementById('addr2').focus();
            }
        }).open();
    }
</script>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>