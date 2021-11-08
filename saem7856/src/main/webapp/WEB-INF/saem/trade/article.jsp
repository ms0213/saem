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
/* 모달대화상자 */
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

.img-box {
	max-width: 100%;
	padding: 5px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
}
.img-box img {
	width: 400px; height: 100%;
	flex: 0 0 auto;
	cursor: pointer;
}

.photo-layout img {
	width: 570px; height: 600px;
}

</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
$(function() {
   $("body").on("click",".btnSendReply", function() {
      var num = "10";
      var $tb = $(this).closest("table");
      var content = $tb.find("textarea").val().trim();
      
      if(! content){
         $tb.find("textarea").focus();
         return false;
      }
      
      content = encodeURIComponent(content);
      
      var query = "num="+num+"&content="+content;
      alert(query);
      
   });
});

//답글버튼
$(function() {
   $("body").on("click",".btnReplyAnswerLayout", function() {
      var $tr = $(this).closest("tr").next();
      var replyNum = $(this).attr("data-replyNum");
      
      var isVisible = $tr.is(":visible");
      if(isVisible){
         $tr.hide();
      } else {
         $tr.show();
      }
      
   });
});

// 답글 등록 버튼
$(function(){
	$("body").on("click",".btnSendReplyAnswer",function(){
		var num = "10";
		var replyNum = $(this).attr("data-replyNum");
		
		var $td = $(this).closest("td");
		var content = $td.find("textarea").val().trim();
		if(!content){
			$td.find("textarea").focus();
			return false;
		}
		content = encodeURIComponent(content);
		var query = "num="+num+"&content="+content+"&answer="+replyNum;
		alert(query);
	});	
});

function imageViewer(img) {
	var viewer = $(".photo-layout");
	var s="<img src='"+img+"'>";
	viewer.html(s);
	
	$(".dialog-photo").dialog({
		title:"이미지",
		width: 600,
		height: 530,
		modal : true
	});
}

function deleteBoard(){
	if (confirm("게시글을 삭제하시겠습니까?")) {
		var query = "num=${dto.num}&${query}";
		var url = "${pageContext.request.contextPath}/trade/delete.do?"+query;
		location.href= url;
	}
}
</script>
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
				
				<!-- Section -->						
				<div class="title" style="margin:30px 0 30px 0">
					<h1>중고거래</h1>
				</div>
				<table class="table table-border table-article">
					<tr>
						<td colspan="2" align="center">
							<span style = "color: tomato;">[${dto.type}]</span> ${dto.subject}
						</td>
					</tr>
					
					<tr>
						<td width="50%">
							이름 : ${dto.userName}
						</td>
						<td align="right">
							${dto.reg_date} | 조회 ${dto.hitCount}
						</td>
					</tr>
					<tr>
						<td  colspan="2" >
							금액 : 
							<c:if test="${dto.pay != ''}">
								${dto.pay}원							
							</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2" valign="top" height="200">
							${dto.content}
						</td>
					</tr>
					
					<c:if test="${listFile.size() > 0 }">
						<tr>
							<td colspan="2" height="200" style="padding:0; padding-top:10px">
								<div class="img-box">
									<c:forEach var="vo" items="${listFile}">
										<img src="${pageContext.request.contextPath}/uploads/trade/${vo.imageFilename}"
											onclick="imageViewer('${pageContext.request.contextPath}/uploads/trade/${vo.imageFilename}');">
									</c:forEach>
								</div>
							</td>	
						</tr>
					</c:if>
				</table>
				
				<table class="table">
					<tr>
						<td width="50%">
							<c:choose>
								<c:when test="${sessionScope.member.userId==dto.userId  || dto.userId=='admin'}">
									<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/trade/update.do?num=${dto.num}&page=${page}';">수정</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="btn" disabled="disabled">수정</button>
								</c:otherwise>
							</c:choose>
					    	
							<c:choose>
					    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin' || dto.userId=='admin'}">
					    			<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
					    		</c:when>
					    		<c:otherwise>
					    			<button type="button" class="btn" disabled="disabled">삭제</button>
					    		</c:otherwise>
					    	</c:choose>
						</td>
						<td align="right">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/trade/list.do?${query}';">리스트</button>
						</td>
					</tr>
				</table>
				
				<div>
					<table>
			          <tr height='30'> 
			           <td align='left' >
			              <span style='font-weight: bold;' >댓글쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가 주세요.</span>
			           </td>
			          </tr>
			          <tr>
			             <td style='padding:5px 0 5px 5px;'>
			                  <textarea class='boxTA' style='width:100%; resize:none; height: 70px;'></textarea>
			              </td>
			          </tr>
			          <tr>
			             <td align='right'>
			                  <button type='button' class='btn btnSendReply' style='padding:10px 20px;'>댓글 등록</button>
			              </td>
			          </tr>
			     </table>
	          
			     <div id="listReply">
			      <table style='width: 100%; margin: 10px auto 30px; border-spacing: 0;'>
			         <thead id="listReplyHeader">
			            <tr height="35">
			                <td colspan='2'>
			                   <div style="clear: both;">
			                       <div style="float: left;"><span style="color: #3EA9CD; font-weight: bold;">댓글 50개</span> <span>[댓글 목록, 1/10 페이지]</span></div>
			                       <div style="float: right; text-align: right;"></div>
			                   </div>
			                </td>
			            </tr>
			         </thead>
			         <tbody id="listReplyBody">
		
			             <tr height='35' style='background: #eeeeee;'>
			                <td width='50%' style='padding:5px 5px; border:1px solid #cccccc; border-right:none;'>
			                    <span><b>가가가</b></span>
			                 </td>
			                <td width='50%' style='padding:5px 5px; border:1px solid #cccccc; border-left:none;' align='right'>
			                    <span>2017-10-10</span> |
			                    <span class="deleteReply" style="cursor: pointer;" data-replyNum='309' data-pageNo='1'>삭제</span>
			                 </td>
			             </tr>
			             <tr>
			                 <td colspan='2' valign='top' style='padding:5px 5px;'>
			                       안녕 하세요.
			                 </td>
			             </tr>
			             
			             <tr>
			                 <td style='padding:7px 5px;'>
			                     <button type='button' class='btn btnReplyAnswerLayout' data-replyNum='309'>답글 <span id="answerCount309">1</span></button>
			                 </td>
			             </tr>
			         
			             <tr class='replyAnswer' style='display: none;'>
			                 <td colspan='2'>
			                     <div id='listReplyAnswer309' class='answerList' style='border-top: 1px solid #cccccc;'>
			                     
			                      <div class='answer' style='padding: 0px 10px;'>
			                          <div style='clear:both; padding: 10px 0px;'>
			                              <div style='float: left; width: 5%;'>└</div>
			                              <div style='float: left; width:95%;'>
			                                  <div style='float: left;'><b>후후후</b></div>
			                                  <div style='float: right;'>
			                                      <span>2017-11-22</span> |
			                                      <span class='deleteReplyAnswer' style='cursor: pointer;' data-replyNum='315' data-answer='309'>삭제</span>
			                                  </div>
			                              </div>
			                          </div>
			                          <div style='clear:both; padding: 5px 5px 5px 5%; border-bottom: 1px solid #ccc;'>
			                              답글 입니다.
			                          </div>
			                      </div>                     
			                     
			                     </div>
			                     <div style='clear: both; padding: 10px 10px;'>
			                         <div style='float: left; width: 5%;'>└</div>
			                         <div style='float: left; width:95%'>
			                             <textarea cols='72' rows='12' class='boxTA' style='width:98%; height: 70px;'></textarea>
			                          </div>
			                     </div>
			                      <div style='padding: 0px 13px 10px 10px; text-align: right;'>
			                         <button type='button' class='btn btnSendReplyAnswer' data-replyNum='309'>답글 등록</button>
			                     </div>
			                 
			                 </td>
			             </tr>
		
				        </tbody>
				        <tfoot id="listReplyFooter">
				           <tr height='40' align="center">
				                 <td colspan='2' >
				                   1 2 3
				                 </td>
				              </tr>
				       	</tfoot>
				    </table>
				</div>
		    </div>
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