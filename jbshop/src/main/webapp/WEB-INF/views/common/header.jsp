<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script type="text/javascript">

	//키워드 검색 스크립트 START
	var loopSearch = true;
	
	//키워드값 컨트롤러를 통해 상품제목 jsonInfo로 가져오기
	function keywordSearch() {
		if (loopSearch == false){
			return;
		}
		var value = document.frmSearch.searchWord.value;
		$.ajax({
			type : "get",
			async : true, //false인 경우 동기식으로 처리한다.
			url : "${contextPath}/goods/keywordSearch.do",
			data : {
				keyword : value//인풋 데이터를 Keyword에 value값으로 저장
			},
			success : function(data, textStatus) {
				var jsonInfo = JSON.parse(data);
				displayResult(jsonInfo);
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다." + data);
			},
			complete : function(data, textStatus) {
				//alert("작업을완료 했습니다");
			}
		});
	}

	//suggest창에 검색된 상품제목 링크태그html 추가
	function displayResult(jsonInfo) {
		var count = jsonInfo.keyword.length;
		if (count > 0) {
			var html = '';
			for ( var i in jsonInfo.keyword) {
				html += "<a href=\"javascript:select('" + jsonInfo.keyword[i]
						+ "')\">" + jsonInfo.keyword[i] + "</a><br/>";
			}//키워드주입된 select함수 링크태크 html추가
			var listView = document.getElementById("suggestList");
			listView.innerHTML = html;
			show('suggest');
		} else {
			hide('suggest');
		}
	}

	//문자열 검색창에 넣고 suggest창 닫기
	function select(selectedKeyword) {
		document.frmSearch.searchWord.value = selectedKeyword;
		loopSearch = false;
		hide('suggest');
	}

	//키워드 있을경우 보이기
	function show(elementId) {
		var element = document.getElementById(elementId);
		if (element) {
			element.style.display = 'block';
		}
	}

	//키워드 없을경우 가리기
	function hide(elementId) {
		var element = document.getElementById(elementId);
		if (element) {
			element.style.display = 'none';
		}
	}
	//키워드 검색 스크립트 END
</script>
<body>
	<div id="logo">
		<a href="${contextPath}/main/main.do"> 
			<img width="176" height="80" alt="booktopia" 
			src="${contextPath}/resources/image/Booktopia_Logo.jpg">
		</a>
	</div>
	<div id="head_link">
		<ul>
			<c:choose>
				<%-- 사용자 로그인시 --%>
				<c:when test="${isLogOn==true and not empty memberInfo }">
					<li><a href="${contextPath}/member/logout.do">로그아웃</a></li>
					<li><a href="${contextPath}/mypage/myPageMain.do">마이페이지</a></li>
					<li><a href="${contextPath}/cart/myCartList.do">장바구니</a></li>
				</c:when>
				<%-- 디폴트값 --%>
				<c:otherwise>
					<li><a href="${contextPath}/member/loginForm.do">로그인</a></li>
					<li><a href="${contextPath}/member/memberForm.do">회원가입</a></li>
				</c:otherwise>
			</c:choose>
			<%-- admin 로그인시 추가 --%>
			<c:if test="${isLogOn==true and memberInfo.member_id =='admin' }">
				<li class="no_line"><a
					href="${contextPath}/admin/goods/adminGoodsMain.do">관리자</a></li>
			</c:if>
		</ul>
	</div>
	<br>
	<%--검색창--%>
	<div id="search">
		<form name="frmSearch" action="${contextPath}/goods/searchGoods.do">
			<input name="searchWord" class="main_input" type="text" onKeyUp="keywordSearch()"> 
			<input type="submit" name="search" class="btn1" value="검 색">
		</form>
	</div>
	<%--검색키워드 출력--%>
	<div id="suggest">
		<div id="suggestList"></div>
	</div>
	<div class="clear"></div>
	<%--주메뉴UI--%>
	<div id="head_side_link" style="float: right">
		<ul style="list-style: none;">
			<li style="float: left"><a>상품목록</a></li>
			<li style="float: left"><a>게시판</a></li>
			<li style="float: left"><a href="${contextPath}/mypage/myPageMain.do">마이페이지</a></li>
			<li style="float: left"><a>고객센터</a></li>
		</ul>
	</div>
</body>