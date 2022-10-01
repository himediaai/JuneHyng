<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<nav>
	<ul>
		<c:choose>
			<c:when test="${side_menu=='admin_mode' }">
				<li>
					<H3>주요관리기능</H3>
					<ul>
						<li><a href="${contextPath}/admin/goods/adminGoodsMain.do">상품관리</a></li>
						<li><a href="${contextPath}/admin/order/adminOrderMain.do">주문관리</a></li>
						<li><a href="${contextPath}/admin/member/adminMemberMain.do">회원관리</a></li>
					</ul>
				</li>
			</c:when>
			<c:when test="${side_menu=='my_page' }">
				<li>
					<h3>마이페이지</h3>
					<ul>
						<li><a href="${contextPath}/mypage/listMyOrderHistory.do">주문내역/배송조회</a></li>
						<li><a href="${contextPath}/mypage/myDetailInfo.do">회원정보관리</a></li>
					</ul>
				</li>
			</c:when>
			<c:otherwise>
				<li>
					<h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;국내외 도서</h3>
					<ul>
						<li><a href="${contextPath}/goods/goodsList.do">IT/인터넷</a></li>
						<li><a href="#">도서 준비중입니다...</a></li>
					</ul>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</nav>
<div class="clear"></div>
<DIV id="notice">
	<H2>공지사항</H2>
	<UL>
		<li><a href="#">공지사항입니다.${i}</a></li>
	</ul>
</div>
<div id="banner">
	<img width="190" height="200"
		src="${contextPath}/resources/image/advertise.jpg">
</div>
</html>