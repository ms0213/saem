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
.table-form td {
	padding: 7px;
}

.table-form tr>td:first-child {
	min-width: 110px;
	text-align: center;
	background: #eee;
}

.table-form tr>td:nth-child(2) {
	padding-left: 10px;
	max-width: 500px;
}

.table-form input[type=text], .table-form input[type=file], .table-form textarea
	{
	width: 96%;
}

.table-form .img {
	width: 37px;
	height: 37px;
	border: none;
	vertical-align: middle;
}

.table-form .info {
	vertical-align: middle;
	font-size: 11px;
	color: #333;
}
</style>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/ckeditor5/ckeditor.js"></script>

<script type="text/javascript">
function sendOk() {
    var f = document.playerForm;
	var str;
	
    str = f.subject.value.trim();
    if(!str) {
        alert("제목을 입력하세요. ");
        f.subject.focus();
        return;
    }
    str = f.league.value.trim();
    if(!str) {
        alert("리그를 입력하세요. ");
        f.league.focus();
        return;
    }
    str = f.team.value.trim();
    if(!str) {
        alert("팀을 입력하세요. ");
        f.team.focus();
        return;
    }

    str = window.editor.getData().trim();
    if(! str) {
        alert("내용을 입력하세요. ");
        window.editor.focus();
        return;
    }
    f.content.value = str;
    
    str = window.editor2.getData().trim();
    if(! str) {
        alert("내용을 입력하세요. ");
        window.editor2.focus();
        return;
    }
	f.content2.value = str;
	
    str = window.editor3.getData().trim();
    if(! str) {
        alert("내용을 입력하세요. ");
        window.editor3.focus();
        return;
    }
	f.content3.value = str;
    
    var mode = "${mode}";
    if( (mode === "write") && (!f.selectFile.value) ) {
        alert("이미지 파일을 추가 하세요. ");
        f.selectFile.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/player/${mode}_ok.do";
    f.submit();
	}


$(function(){
	var img = "${dto.imageFilename}";
	if( img ) { // 수정인 경우
		img = "${pageContext.request.contextPath}/uploads/player/" + img;
		$(".table-form .img-viewer").empty();
		$(".table-form .img-viewer").css("background-image", "url("+img+")");
	}
	
	$(".table-form .img-viewer").click(function(){
		$("form[name=playerForm] input[name=selectFile]").trigger("click"); 
	});
	
	$("form[name=playerForm] input[name=selectFile]").change(function(){
		var file=this.files[0];
		if(! file) {
			$(".table-form .img-viewer").empty();
			if( img ) {
				img = "${pageContext.request.contextPath}/uploads/player/" + img;
				$(".table-form .img-viewer").css("background-image", "url("+img+")");
			} else {
				img = "${pageContext.request.contextPath}/resource/images/add_photo.png";
				$(".table-form .img-viewer").css("background-image", "url("+img+")");
			}
			return false;
		}
		
		if(! file.type.match("image.*")) {
			this.focus();
			return false;
		}
		
		var reader = new FileReader();
		reader.onload = function(e) {
			$(".table-form .img-viewer").empty();
			$(".table-form .img-viewer").css("background-image", "url("+e.target.result+")");
		}
		reader.readAsDataURL(file);
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
					<div class="body-container">
						<div class="body-title" style="padding-top: 50px;">
							<h3 style="font-size: 2.25em; font-weight: bold;">
								<i class="fas fa-futbol"></i> 선수 등록
							</h3>
						</div>

						<form name="playerForm" method="post"
							enctype="multipart/form-data">
							<table class="table table-border table-form">
								<colgroup>
									<col width="15%" />
									<col width="85%" />
								</colgroup>
								<tr>
									<td>선수&nbsp;&nbsp;&nbsp;&nbsp;이름</td>
									<td><input type="text" name="subject" maxlength="100"
										class="boxTF" value="${dto.subject}"></td>
								</tr>

								<tr>
									<td>리&nbsp;&nbsp;&nbsp;&nbsp;그</td>
									<td><input type="text" name="league" maxlength="100"
										class="boxTF" value="${dto.league}"></td>
								</tr>
								<tr>
									<td>팀</td>
									<td><input type="text" name="team" maxlength="100"
										class="boxTF" value="${dto.team}"></td>
								</tr>

								<tr>
									<td valign="top">선수&nbsp;&nbsp;&nbsp;&nbsp;프로필</td>
									<td>
										<div class="editor">${dto.content}</div> <input type="hidden"
										name="content">
									</td>
								</tr>

								<tr>
									<td valign="top">선수&nbsp;&nbsp;&nbsp;&nbsp;정보</td>
									<td>
										<div class="editor2">${dto.content2}</div> <input
										type="hidden" name="content2">
									</td>
								</tr>

								<tr>
									<td valign="top">리그&nbsp;&nbsp;&nbsp;&nbsp;순위표</td>
									<td>
										<div class="editor3">${dto.content3}</div> <input
										type="hidden" name="content3">
									</td>
								</tr>

								<tr>
									<td>이미지</td>
									<td><input type="file" name="selectFile" accept="image/*"
										class="boxTF"></td>
								</tr>

							</table>

							<table class="table">
								<tr>
									<td align="center">
										<button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
										<button type="reset" class="btn">다시입력</button>
										<button type="button" class="btn"
											onclick="location.href='${pageContext.request.contextPath}/player/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
										<c:if test="${mode=='update'}">
											<input type="hidden" name="num" value="${dto.num}">
											<input type="hidden" name="imageFilename"
												value="${dto.imageFilename}">
											<input type="hidden" name="page" value="${page}">
										</c:if>
									</td>
								</tr>
							</table>

						</form>

					</div>
				</main>

				<script>
		ClassicEditor
			.create( document.querySelector( '.editor' ), {
				fontFamily: {
		            options: [
		                'default',
		                '맑은 고딕, Malgun Gothic, 돋움, sans-serif',
		                '나눔고딕, NanumGothic, Arial'
		            ]
		        },
		        fontSize: {
		            options: [
		                9, 11, 13, 'default', 17, 19, 21
		            ]
		        },
				toolbar: {
					items: [
						'heading','|',
						'fontFamily','fontSize','bold','italic','fontColor','|',
						'alignment','bulletedList','numberedList','|',
						'imageUpload','insertTable','sourceEditing','blockQuote','mediaEmbed','|',
						'undo','redo','|',
						'link','outdent','indent','|',
					]
				},
				image: {
		            toolbar: [
		                'imageStyle:full',
		                'imageStyle:side',
		                '|',
		                'imageTextAlternative'
		            ],

		            // The default value.
		            styles: [
		                'full',
		                'side'
		            ]
		        },
				language: 'ko',
				ckfinder: {
			        uploadUrl: '${pageContext.request.contextPath}/image/upload.do' // 업로드 url (post로 요청 감)
			    }
			})
			.then( editor => {
				window.editor = editor;
			})
			.catch( err => {
				console.error( err.stack );
			});
		
		ClassicEditor
			.create( document.querySelector( '.editor2' ), {
				fontFamily: {
		            options: [
		                'default',
		                '맑은 고딕, Malgun Gothic, 돋움, sans-serif',
		                '나눔고딕, NanumGothic, Arial'
		            ]
		        },
		        fontSize: {
		            options: [
		                9, 11, 13, 'default', 17, 19, 21
		            ]
		        },
				toolbar: {
					items: [
						'heading','|',
						'fontFamily','fontSize','bold','italic','fontColor','|',
						'alignment','bulletedList','numberedList','|',
						'imageUpload','insertTable','sourceEditing','blockQuote','mediaEmbed','|',
						'undo','redo','|',
						'link','outdent','indent','|',
					]
				},
				image: {
		            toolbar: [
		                'imageStyle:full',
		                'imageStyle:side',
		                '|',
		                'imageTextAlternative'
		            ],

		            // The default value.
		            styles: [
		                'full',
		                'side'
		            ]
		        },
				language: 'ko',
				ckfinder: {
			        uploadUrl: '${pageContext.request.contextPath}/image/upload.do' // 업로드 url (post로 요청 감)
			    }
			})
			.then( editor => {
				window.editor2 = editor;
			})
			.catch( err => {
				console.error( err.stack );
			});
		
		ClassicEditor
			.create( document.querySelector( '.editor3' ), {
				fontFamily: {
		            options: [
		                'default',
		                '맑은 고딕, Malgun Gothic, 돋움, sans-serif',
		                '나눔고딕, NanumGothic, Arial'
		            ]
		        },
		        fontSize: {
		            options: [
		                9, 11, 13, 'default', 17, 19, 21
		            ]
		        },
				toolbar: {
					items: [
						'heading','|',
						'fontFamily','fontSize','bold','italic','fontColor','|',
						'alignment','bulletedList','numberedList','|',
						'imageUpload','insertTable','sourceEditing','blockQuote','mediaEmbed','|',
						'undo','redo','|',
						'link','outdent','indent','|',
					]
				},
				image: {
		            toolbar: [
		                'imageStyle:full',
		                'imageStyle:side',
		                '|',
		                'imageTextAlternative'
		            ],

		            // The default value.
		            styles: [
		                'full',
		                'side'
		            ]
		        },
				language: 'ko',
				ckfinder: {
			        uploadUrl: '${pageContext.request.contextPath}/image/upload.do' // 업로드 url (post로 요청 감)
			    }
			})
			.then( editor => {
				window.editor3 = editor;
			})
			.catch( err => {
				console.error( err.stack );
			});
</script>


			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>