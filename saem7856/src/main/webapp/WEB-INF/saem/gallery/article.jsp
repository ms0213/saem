<%-- 작업중 --%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>saem 7856</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp" />

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/slider/css/slider.min.css">
<style type="text/css">
.ui-widget-header { /* 타이틀바 */
	background: none;
	border: none;
	border-bottom: 1px solid #ccc;
	border-radius: 0;
}

.ui-dialog .ui-dialog-title {
	padding-top: 5px;
	padding-bottom: 5px;
}

.ui-widget-content { /* 내용 */
	/* border: none; */
	border-color: #ccc;
}

.table-article tr>td {
	padding-left: 5px;
	padding-right: 5px;
}

.img-box img {
	width: 600px;
	height: auto;
	cursor: pointer;
}

.photo-layout img {
	width: 570px;
	height: 450px;
}

.reply {
	clear: both;
	padding: 20px 0 10px;
}

.reply .bold {
	font-weight: 600;
}

.reply .form-header {
	padding-bottom: 7px;
}

.reply-form  td {
	padding: 2px 0 2px;
}

.reply-form textarea {
	width: 100%;
	height: 75px;
}

.reply-form button {
	padding: 8px 25px;
}

.reply .reply-info {
	padding-top: 25px;
	padding-bottom: 7px;
}

.reply .reply-info  .reply-count {
	color: #3EA9CD;
	font-weight: 700;
}

.reply .reply-list tr td {
	padding: 7px 5px;
}

.reply .reply-list .bold {
	font-weight: 600;
}

.reply .deleteReply, .reply .deleteReplyAnswer {
	cursor: pointer;
}

.reply .notifyReply {
	cursor: pointer;
}

.reply-list .list-header {
	border: 1px solid #ccc;
	background: #eee;
}

.reply-list td {
	padding-left: 7px;
	padding-right: 7px;
}

.reply-answer {
	display: none;
}

.reply-answer .answer-left {
	float: left;
	width: 5%;
}

.reply-answer .answer-right {
	float: left;
	width: 95%;
}

.reply-answer .answer-list {
	border-top: 1px solid #ccc;
	padding: 0 10px 7px;
}

.reply-answer .answer-form {
	clear: both;
	padding: 3px 10px 5px;
}

.reply-answer .answer-form textarea {
	width: 100%;
	height: 75px;
}

.reply-answer .answer-footer {
	clear: both;
	padding: 0 13px 10px 10px;
	text-align: right;
}

.answer-article {
	clear: both;
}

.answer-article .answer-article-header {
	clear: both;
	padding-top: 5px;
}

.answer-article .answer-article-body {
	clear: both;
	padding: 5px 5px;
	border-bottom: 1px solid #ccc;
}
</style>

<script type="text/javascript">
	function deletePhoto() {
		if (confirm("게시글을 삭제하시겠습니까?")) {
			var query = "num=${dto.num}&page=${page}";
			var url = "${pageContext.request.contextPath}/photo/delete.do?"
					+ query;
			location.href = url;
		}
	}
	
	function imageViewer(img) {
		var viewer = $(".photo-layout");
		var s = "<img src='"+img+"'>";
		viewer.html(s);

		$(".dialog-photo").dialog({
			title : "image",
			width : 600,
			height : 530,
			modal : true
		});
	}

	$(function() {
		$(".slider").slider({
			speed : 500,
			delay : 2500
		/* ,paginationType : 'thumbnails' */// 아래부분에 작은 이미지 출력
		});
	});

	function login() {
		location.href = "${pageContext.request.contextPath}/member/login.do";
	}

	function ajaxFun(url, method, query, dataType, fn) {
		$.ajax({
			type : method,
			url : url,
			data : query,
			dataType : dataType,
			success : function(data) {
				fn(data);
			},
			beforeSend : function(jqXHR) {
				jqXHR.setRequestHeader("AJAX", true);
			},
			error : function(jqXHR) {
				if (jqXHR.status === 403) {
					login();
					return false;
				} else if (jqXHR.status === 405) {
					alert("잘못된 접근입니다.");
					return false;
				}

				console.log(jqXHR.responseText);
			}
		});
	}

	// 게시글 공감
	$(function() {
		$(".btnSendPhotoLike")
				.click(
						function() {
							var isNoLike = $(this).find("i").css("color") == "rgb(0, 0, 0)";
							var $i = $(this).find("i");
							var msg = isNoLike ? "게시글에 공감하시겠습니까?"
									: "게시글 공감을 취소하시겠습니까?";

							if (!confirm(msg)) {
								return false;
							}

							var url = "${pageContext.request.contextPath}/photo/insertPhotoLike.do";
							var num = "${dto.num}";
							var query = "num=" + num + "&isNoLike=" + isNoLike;

							var fn = function(data) {
								var state = data.state;
								if (state === "true") {
									var color = "black";
									if (isNoLike) {
										color = "blue";
									}
									$i.css("color", color);
									var count = data.photoLikeCount;
									$("#photoLikeCount").text(count);
								} else if (state === "failLike") {
									alert("공감은 게시글 당 한 번만 가능합니다.")
								}
							};
							ajaxFun(url, "post", query, "json", fn);
						});
	});

	// 댓글 리스트
	$(function() {
		listPage(1);
	});

	function listPage(page) {
		var url = "${pageContext.request.contextPath}/photo/listReply.do";
		var query = "num=${dto.num}&pageNo=" + page;
		var selector = "#listReply";

		var fn = function(data) {
			$(selector).html(data);
		};
		ajaxFun(url, "get", query, "html", fn);
	}

	// 댓글 등록
	$(function() {
		$(".btnSendReply")
				.click(
						function() {
							var num = "${dto.num}";
							var $tb = $(this).closest("table");
							var content = $tb.find("textarea").val().trim();
							if (!content) {
								$tb.find("textarea").focus();
								return false;
							}
							content = encodeURIComponent(content);

							var url = "${pageContext.request.contextPath}/photo/insertReply.do";
							var query = "num=" + num + "&content=" + content
									+ "&answer=0";

							var fn = function(data) {
								var state = data.state;

								$tb.find("textarea").val("");

								if (state === "true") {
									listPage(1);
								} else {
									alert("댓글 등록에 실패했습니다.");
								}
							};
							ajaxFun(url, "post", query, "json", fn);

						});
	});

	// 댓글 삭제
	$(function() {
		$("body")
				.on(
						"click",
						".deleteReply",
						function() {
							if (!confirm("댓글을 삭제하시겠습니까?")) {
								return false;
							}
							var replyNum = $(this).attr("data-replyNum");
							var pageNo = $(this).attr("data-pageNo");

							var url = "${pageContext.request.contextPath}/photo/deleteReply.do";
							var query = "replyNum=" + replyNum;

							var fn = function(data) {
								listPage(pageNo);
							};
							ajaxFun(url, "post", query, "json", fn);
						});
	});

	// 댓글 좋아요/싫어요
	$(function() {
		$("body")
				.on(
						"click",
						".btnSendReplyLike",
						function() {
							var replyNum = $(this).attr("data-replyNum");
							var replyLike = $(this).attr("data-replyLike");
							var $btn = $(this); // 이벤트를 발생시킨 객체 찾기

							var msg = "게시글이 마음에 들지 않으십니까?";
							if (replyLike === "1") {
								msg = "게시글에 공감하십니까?";
							}
							if (!confirm(msg)) {
								return false;
							}
							var url = "${pageContext.request.contextPath}/photo/insertReplyLike.do";
							var query = "replyNum=" + replyNum + "&replyLike="
									+ replyLike;

							var fn = function(data) {
								var state = data.state;
								if (state === "true") {
									var likeCount = data.likeCount;
									var disLikeCount = data.disLikeCount;

									$btn.parent("td").children().eq(0).find(
											"span").html(likeCount);
									$btn.parent("td").children().eq(1).find(
											"span").html(disLikeCount);
								} else if (state === "liked") {
									alert("게시물 공감은 한 번만 가능합니다.")
								} else {
									alert("게시물 공감 처리에 실패했습니다.");
								}
							};
							ajaxFun(url, "post", query, "json", fn);
						});
	});

	// 댓글별 답글 리스트
	function listReplyAnswer(answer) {
		var url = "${pageContext.request.contextPath}/photo/listReplyAnswer.do";
		var query = "answer=" + answer;
		var selector = "#listReplyAnswer" + answer;

		var fn = function(data) {
			$(selector).html(data);
		};
		ajaxFun(url, "get", query, "html", fn);
	}

	// 댓글별 답글 갯수
	function countReplyAnswer(answer) {
		var url = "${pageContext.request.contextPath}/photo/countReplyAnswer.do";
		var query = "answer=" + answer;

		var fn = function(data) {
			var count = data.count;
			var selector = "#answerCount" + answer;
			$(selector).html(count);
		};
		ajaxFun(url, "post", query, "json", fn);
	}

	// 답글 버튼
	$(function() {
		$("body").on("click", ".btnReplyAnswerLayout", function() {
			var $tr = $(this).closest("tr").next();

			var isVisible = $tr.is(":visible");
			var replyNum = $(this).attr("data-replyNum");

			if (isVisible) {
				$tr.hide();
			} else {
				$tr.show();

				// 답글 리스트
				listReplyAnswer(replyNum);
				// 답글 갯수
				countReplyAnswer(replyNum);
			}
		});
	});

	// 답글 등록 버튼
	$(function() {
		$("body")
				.on(
						"click",
						".btnSendReplyAnswer",
						function() {
							var num = "${dto.num}";
							var replyNum = $(this).attr("data-replyNum");
							var $td = $(this).closest("td");

							var content = $td.find("textarea").val().trim();
							if (!content) {
								$td.find("textarea").focus();
								return false;
							}
							content = encodeURIComponent(content);

							var url = "${pageContext.request.contextPath}/photo/insertReplyAnswer.do";
							var query = "num=" + num + "&content=" + content
									+ "&answer=" + replyNum;

							var fn = function(data) {
								$td.find("textarea").val("");

								var state = data.state;
								if (state === "true") {
									listReplyAnswer(replyNum);
									countReplyAnswer(replyNum);
								}
							};
							ajaxFun(url, "post", query, "json", fn);
						});
	});

	// 답글 삭제
	$(function() {
		$("body")
				.on(
						"click",
						".deleteReplyAnswer",
						function() {
							if (!confirm("답글을 삭제하시겠습니까?")) {
								return false;
							}
							var replyNum = $(this).attr("data-replyNum");
							var answer = $(this).attr("data-answer");

							var url = "${pageContext.request.contextPath}/photo/deleteReplyAnswer.do";
							var query = "replyNum=" + replyNum;

							var fn = function(data) {
								listReplyAnswer(answer);
								countReplyAnswer(answer);
							};
							ajaxFun(url, "post", query, "json", fn);
						});
	});
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
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"></jsp:include>
				</header>

				<!-- Section -->
				<div class="title" style="margin: 30px 0 30px 0">
					<h1>
						<i class="far fa-image"></i> 포토 앨범
					</h1>
					<div class="title" style="margin: 30px 0 30px 0">

						<table class="table table-border table-article">
							<tr>
								<td colspan="2" align="center">${dto.subject}</td>
							</tr>

							<tr>
								<td align="right">${dto.reg_date}|조회${dto.hitCount}</td>
							</tr>

							<tr>
								<td colspan="2" style="background-color: white;">
									<c:choose>
										<c:when test="${listFile.size() > 1}">
												<ul class="slider">
													<c:forEach var="dto" items="${listFile}">
														<li data-num="${dto.num}"><img
															src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}"
															title="${dto.subject}" alt="${dto.subject}"
															onclick="imageViewer('${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}');"></li>
													</c:forEach>
												</ul>
										</c:when>
										<c:otherwise>
											<div class="img-box" align="center">
												<c:forEach var="dto" items="${listFile}">
													<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}"
														title="${dto.subject}" alt="${dto.subject}"
														onclick="imageViewer('${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}');">
												</c:forEach>
											</div>
										</c:otherwise>
									</c:choose>
									<p style="text-align: center;">(사진을 클릭하면 새창으로 열립니다.)</p>
									<p>${dto.content}</p>
									<p align="center">
										<button type="button" class="btn btnSendPhotoLike" title="좋아요">
											<i class="fas fa-thumbs-up"
												style="color: ${isUserLike?'blue':'black'}"></i>&nbsp;&nbsp;<span
												id="photoLikeCount">${dto.photoLikeCount}</span>
										</button>
									</p>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="background-color: white;">다음글 : <c:if
										test="${not empty preReadDto}">
										<a
											href="${pageContext.request.contextPath}/photo/article.do?num=${preReadDto.num}&page=${page}">${preReadDto.subject}</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="background-color: white;">이전글 : <c:if
										test="${not empty nextReadDto}">
										<a
											href="${pageContext.request.contextPath}/photo/article.do?num=${nextReadDto.num}&page=${page}">${nextReadDto.subject}</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td width="50%"><c:choose>
										<c:when test="${sessionScope.member.userId=='admin'}">
											<button type="button" class="btn"
												onclick="location.href='${pageContext.request.contextPath}/photo/update.do?num=${dto.num}&page=${page}';">수정</button>
										</c:when>
										<c:otherwise>
											<button type="button" class="btn" disabled="disabled">수정</button>
										</c:otherwise>
									</c:choose> <c:choose>
										<c:when test="${sessionScope.member.userId=='admin'}">
											<button type="button" class="btn" onclick="deletePhoto();">삭제</button>
										</c:when>
										<c:otherwise>
											<button type="button" class="btn" disabled="disabled">삭제</button>
										</c:otherwise>
									</c:choose></td>
								<td align="right">
									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/photo/list.do?page=${page}';">리스트</button>
								</td>
							</tr>
						</table>
						<div class="dialog-photo">
							<div class="photo-layout"></div>
						</div>
						<div class="reply">
							<form name="replyForm" method="post">
								<div class='form-header'>
									<span class="bold">댓글쓰기</span><span> - 타인을 비방하거나 개인정보를
										유출하는 글의 게시를 삼가주시기 바랍니다.</span>
								</div>

								<table class="table reply-form">
									<tr>
										<td><textarea class='boxTA' name="content"></textarea></td>
									</tr>
									<tr>
										<td align='right'>
											<button type='button' class='btn btnSendReply' style='text-align: center;'>댓글 등록</button>
										</td>
									</tr>
								</table>
							</form>

							<div id="listReply"></div>
						</div>
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
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/slider/js/slider.js"></script>
</body>
</html>