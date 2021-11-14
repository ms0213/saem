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
.ui-widget-header { /* 타이틀바 */
	background: none;
	border: none;
	border-bottom: 1px solid #ccc;
	border-radius: 0;
}
.ui-dialog .ui-dialog-title {
	padding-top: 5px; padding-bottom: 5px;
}
.ui-widget-content { /* 내용 */
   /* border: none; */
   border-color: #ccc; 
}

.help-block {
	margin-top: 3px; 
}

.titleDate {
	display: inline-block;
	font-weight: 600; 
	font-size: 19px;
	font-family: 나눔고딕, "맑은 고딕", 돋움, sans-serif;
	padding:2px 4px 4px;
	text-align:center;
	position: relative;
	top: 4px;
}
.btnDate {
	display: inline-block;
	font-size: 10px;
	font-family: 나눔고딕, "맑은 고딕", 돋움, sans-serif;
	color:#333;
	padding:3px 5px 5px;
	border:1px solid #ccc;
    background-color:#fff;
    text-align:center;
    cursor: pointer;
    border-radius:2px;
}

.textDate {
      font-weight: 500; cursor: pointer;  display: block; color:#333;
}
.preMonthDate, .nextMonthDate {
      color:#aaa;
}
.nowDate {
      color:#111;
}
.saturdayDate{
      color:#0000ff;
}
.sundayDate{
      color:#ff0000;
}

.scheduleSubject {
   display:block;
   /*width:100%;*/
   width:110px;
   margin:1.5px 0;
   font-size:13px;
   color:#555;
   background:#eee;
   cursor: pointer;
   white-space:nowrap; overflow:hidden; text-overflow:ellipsis;
}
.scheduleMore {
   display:block;
   width:110px;
   margin:0 0 1.5px;
   font-size:13px;
   color:#555;
   cursor: pointer;
   text-align:right;
}

ul.tabs {
	margin: 0;
	padding: 0;
	list-style: none;
	height: 35px;
	width: 100%;
	border-bottom: 1px solid #ddd;
}
ul.tabs > li {
	float: left;
	margin: 0;
	cursor: pointer;
	padding: 0px 21px;
	height: 35px;
	line-height: 35px;
	overflow: hidden;
	position: relative;
	background: #fff;
	border-bottom: 1px solid #ddd;
}
ul.tabs li:hover {
	background: #e7e7e7;
}	
ul.tabs li.active {
	font-weight: 700;
	border: 1px solid #ddd;
	border-bottom-color:  transparent;
}
</style>

<script type="text/javascript">
function login() {
	location.href = "${pageContext.request.contextPath}/member/login.do";
}

function ajaxFun(url, method, query, dataType, fn) {
	$.ajax({
		type:method,
		url:url,
		data:query,
		dataType:dataType,
		success:function(data) {
			fn(data);
		},
		beforeSend:function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status === 403) {
				login();
				return false;
			} else if(jqXHR.status === 405) {
				alert("접근을 허용하지 않습니다.");
				return false;
			}
	    	
			console.log(jqXHR.responseText);
		}
	});
}

$(function(){
	$("#tab-month").addClass("active");
});

$(function() {
	var today = "${today}";
	$("#largeCalendar .textDate").each(function (i) {
        var s = $(this).attr("data-date");
        if(s === today) {
        	$(this).parent().css("background", "#ffffd9");
        }
    });
});

$(function(){
	$("ul.tabs li").click(function() {
		tab = $(this).attr("data-tab");
		
		$("ul.tabs li").each(function(){
			$(this).removeClass("active");
		});
		
		$("#tab-" + tab).addClass("active");
		
		var url = "${pageContext.request.contextPath}/fixture"	
		if(tab === "month") {
			url += "/month.do";
		} else if(tab === "day") {
			url += "/day.do";
		} else if(tab === "year") {
			url += "/year.do";
		}
		
		location.href = url;
	});
});

function changeDate(year, month) {
	var url = "${pageContext.request.contextPath}/schedule/month.do?year="+year+"&month="+month;
	location.href = url;
}

// 스케쥴 등록 -----------------------
// 등록 대화상자 출력
$(function(){
	$(".textDate").click(function(){
		// 폼 reset
		$("form[name=scheduleForm]").each(function(){
			this.reset();
		});
		$("#form-eday").closest("tr").show();
		
		var date = $(this).attr("data-date");
		date = date.substr(0,4) + "-" + date.substr(4,2) + "-" + date.substr(6,2);

		$("form[name=scheduleForm] input[name=sday]").val(date);
		$("form[name=scheduleForm] input[name=eday]").val(date);
		
		$("#form-sday").datepicker({showMonthAfterYear:true});
		$("#form-eday").datepicker({showMonthAfterYear:true});
		
		$("#form-sday").datepicker("option", "defaultDate", date);
		$("#form-eday").datepicker("option", "defaultDate", date);
		
		$('#schedule-dialog').dialog({
			  modal: true,
			  height: 580,
			  width: 600,
			  title: '스케쥴 등록',
			  close: function(event, ui) {
			  }
		});

	});
});

$(function(){
	$("#form-sday").change(function(){
		$("#form-eday").val($("#form-sday").val());
		$("#form-stime").val("00:00").show();
		$("#form-etime").val("00:00").show();
	});
	
	$("#form-repeat").change(function(){
		if($(this).val() === "0") {
			$("#form-repeat_cycle").val("").hide();
			
			$("#form-allDay").prop("checked", true);
			$("#form-allDay").removeAttr("disabled");
			
			$("#form-eday").val($("#form-sday").val());
			$("#form-eday").closest("tr").show();
		} else {
			$("#form-repeat_cycle").show();
			
			$("#form-allDay").prop("checked", true);
			$("#form-allDay").attr("disabled","disabled");

			$("#form-stime").val("").hide();
			$("#form-eday").val("");
			$("#form-etime").val("");
			$("#form-eday").closest("tr").hide();
		}
	});
	
});

// 등록
$(function(){
	$("#btnScheduleSendOk").click(function(){
		if(! check() ) {
			return;
		}
		
		var query = $("form[name=scheduleForm]").serialize();
		var url = "${pageContext.request.contextPath}/fixture/insert.do";

		var fn = function(data){
			var state = data.state;
			if(state === "true") {
				var dd=$("#form-sday").val().split("-");
				var y = dd[0];
				var m = dd[1];
				if(m.substr(0,1) === "0") m = m.substr(1,1);
			
				location.href="${pageContext.request.contextPath}/fixture/month.do?year="+y+"&month="+m;
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);		
	});
});

// 등록 대화상자 닫기
$(function(){
	$("#btnScheduleSendCancel").click(function(){
		$('#schedule-dialog').dialog("close");
	});
});

// 등록내용 유효성 검사
function check() {
	if(! $("#form-subject").val()) {
		$("#form-subject").focus();
		return false;
	}

	if(! $("#form-sday").val()) {
		$("#form-sday").focus();
		return false;
	}

	if($("#form-eday").val()) {
		var s1=$("#form-sday").val().replace("-", "");
		var s2=$("#form-eday").val().replace("-", "");
		if(s1>s2) {
			$("#form-sday").focus();
			return false;
		}
	}
	
	if($("#form-stime").val()!="" && !isValidTime($("#form-stime").val())) {
		$("#form-stime").focus();
		return false;
	}

	if($("#form-etime").val()!="" && !isValidTime($("#form-etime").val())) {
		$("#form-etime").focus();
		return false;
	}
	
	if($("#form-etime").val()) {
		var s1=$("#form-stime").val().replace(":", "");
		var s2=$("#form-etime").val().replace(":", "");
		if(s1>s2) {
			$("#form-stime").focus();
			return false;
		}
	}	
	
	return true;
}

// 시간 형식 유효성 검사
function isValidTime(data) {
	if(! /(\d){2}[:](\d){2}/g.test(data)) {
		return false;
	}
	
	var t=data.split(":");
	if(t[0]<0||t[0]>23||t[1]<0||t[1]>59) {
		return false;
	}

	return true;
}

// 스케쥴 제목 클릭 -----------------------
$(function(){
	$(".scheduleSubject").click(function(){
		var date = $(this).attr("data-date");
		var num = $(this).attr("data-num");
		var url = "${pageContext.request.contextPath}/fixture/day.do?date="+date+"&num="+num;
		location.href = url;
	});
});

//스케쥴 more(더보기) ----------------------- 
$(function(){
	$(".scheduleMore").click(function(){
		var date = $(this).attr("data-date");
		var url = "${pageContext.request.contextPath}/fixture/day.do?date=" + date;
		location.href = url;
	});
});
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

				<main>
	<div class="body-container" style="width: 900px;">
		<div class="body-title">
			<h1>일정관리 </h1>
		</div>
        
		<div>
			<div style="clear: both;">
				<ul class="tabs">
					<li id="tab-month" data-tab="month">월별일정</li>
					<li id="tab-day" data-tab="day">상세일정</li>
					<li id="tab-year" data-tab="year">년도</li>
				</ul>
			</div>
		
			<div id="tab-content" style="clear:both; padding: 20px 0 0px;">
				<table style="width: 840px; margin:0 auto; border-spacing: 0;" >
					<tr height="60">
						<td width="200">&nbsp;</td>
						<td align="center">
							<span class="btnDate" onclick="changeDate(${todayYear}, ${todayMonth});">오늘</span>
							<span class="btnDate" onclick="changeDate(${year}, ${month-1});">＜</span>
							<span class="titleDate">${year}年 ${month}月</span>
							<span class="btnDate" onclick="changeDate(${year}, ${month+1});">＞</span>
						</td>
						<td width="200">&nbsp;</td>
					</tr>
				</table>
		   		
				<table id="largeCalendar" style="width: 840px; margin:0 auto; border-spacing: 1px; background: #ccc;" >
					<tr align="center" height="30" bgcolor="#fff">
						<td width="120" style="color:#ff0000;">일</td>
						<td width="120">월</td>
						<td width="120">화</td>
						<td width="120">수</td>
						<td width="120">목</td>
						<td width="120">금</td>
						<td width="120" style="color:#0000ff;">토</td>
					</tr>

					<c:forEach var="row" items="${days}" >
						<tr align="left" height="120" valign="top" bgcolor="#fff">
							<c:forEach var="d" items="${row}">
								<td style="padding: 5px;">
									${d}
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
			    </table>		   
		   </div>
		</div>
	</div>

	<c:if test="${sessionScope.member.userId == 'admin'}">
	<div id="schedule-dialog" style="display: none;">
		<form name="scheduleForm">
			<table style="width: 100%; margin: 20px auto 0; border-spacing: 0; border-collapse: collapse;">
				<tr>
					<td width="100" valign="top" style="text-align: right; padding-top: 5px;">
						<label style="font-weight: 900;">제목</label>
					</td>
					<td style="padding: 0 0 15px 15px;">
						<p style="margin-top: 1px; margin-bottom: 5px;">
							<input type="text" name="subject" id="form-subject" maxlength="100" class="boxTF" style="width: 95%;">
						</p>
						<p class="help-block">* 제목은 필수 입니다.</p>
					</td>
				</tr>
				
				<tr>
					<td width="100" valign="top" style="text-align: right; padding-top: 5px;">
						<label style="font-weight: 900;">일정분류</label>
					</td>
					<td style="padding: 0 0 15px 15px;">
						<p style="margin-top: 1px; margin-bottom: 5px;">
							<select name="color" id="form-color" class="selectField">
								<option value="green">EPL</option>
								<option value="blue">라리가</option>
								<option value="tomato">분데스리가</option>
								<option value="purple">리그1</option>
								<option value="yello">쉬페르리그</option>
								<option value="brown">러시아프리미어리그</option>
							</select>
						</p>
					</td>
				</tr>
				
				<tr>
					<td width="100" valign="top" style="text-align: right; padding-top: 5px;">
						<label style="font-weight: 900;">시작일자</label>
					</td>
					<td style="padding: 0 0 15px 15px;">
						<p style="margin-top: 1px; margin-bottom: 5px;">
							<input type="text" name="sday" id="form-sday" maxlength="10" class="boxTF" readonly="readonly" style="width: 25%; background: #fff;">
							<input type="text" name="stime" id="form-stime" maxlength="5" class="boxTF" style="width: 15%;" placeholder="시작시간">
						</p>
						<p class="help-block">* 시작날짜는 필수입니다.</p>
					</td>
				</tr>
				
				<tr>
					<td width="100" valign="top" style="text-align: right; padding-top: 5px;">
						<label style="font-weight: 900;">종료일자</label>
					</td>
					<td style="padding: 0 0 15px 15px;">
						<p style="margin-top: 1px; margin-bottom: 5px;">
							<input type="text" name="eday" id="form-eday" maxlength="10" class="boxTF" readonly="readonly" style="width: 25%; background: #fff;">
							<input type="text" name="etime" id="form-etime" maxlength="5" class="boxTF" style="width: 15%;" placeholder="종료시간">
						</p>
						<p class="help-block">종료일자는 선택사항이며, 시작일자보다 작을 수 없습니다.</p>
					</td>
				</tr>
				
				<tr>
					<td width="100" valign="top" style="text-align: right; padding-top: 5px;">
						<label style="font-weight: 900;">메모</label>
					</td>
					<td style="padding: 0 0 15px 10px;">
						<p style="margin-top: 1px; margin-bottom: 5px;">
							<textarea name="memo" id="form-memo" class="boxTA" style="width:93%; height: 70px;"></textarea>
						</p>
					</td>
				</tr>
				  
				<tr height="35">
					<td align="center" colspan="2">
						<button type="button" class="btn" id="btnScheduleSendOk">일정등록</button>
						<button type="reset" class="btn">다시입력</button>
						<button type="button" class="btn" id="btnScheduleSendCancel">등록취소</button>
					</td>
				</tr>
			</table>
		</form>
	</div>
	</c:if>
</main>

			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>