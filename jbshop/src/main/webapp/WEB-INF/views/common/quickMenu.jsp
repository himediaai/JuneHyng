<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script>

	//장바구니 빠른메뉴 START
	var array_index = 0;
	var SERVER_URL = "${contextPath}/thumbnails.do";
	
	//다음 상품선택
	function fn_show_next_goods() {
		var img_sticky = document.getElementById("img_sticky");
		var cur_goods_num = document.getElementById("cur_goods_num");
		var _h_goods_id = document.frm_sticky.h_goods_id;
		var _h_goods_fileName = document.frm_sticky.h_goods_fileName;
		if (array_index < _h_goods_id.length - 1)
			array_index++;

		var goods_id = _h_goods_id[array_index].value;
		var fileName = _h_goods_fileName[array_index].value;
		img_sticky.src = SERVER_URL + "?goods_id=" + goods_id + "&fileName="
				+ fileName; //상품 썸네일 바꾸기
		cur_goods_num.innerHTML = array_index + 1; //상품 페이지번호 바꾸기
	}
	
	//이전 상품선택
	function fn_show_previous_goods() {
		var img_sticky = document.getElementById("img_sticky");
		var cur_goods_num = document.getElementById("cur_goods_num");
		var _h_goods_id = document.frm_sticky.h_goods_id;
		var _h_goods_fileName = document.frm_sticky.h_goods_fileName;

		if (array_index > 0)
			array_index--;

		var goods_id = _h_goods_id[array_index].value;
		var fileName = _h_goods_fileName[array_index].value;
		img_sticky.src = SERVER_URL + "?goods_id=" + goods_id + "&fileName="
				+ fileName;//상품 썸네일 바꾸기
		cur_goods_num.innerHTML = array_index + 1;//상품 페이지번호 바꾸기
	}

	//상품정보 바로가기
	function goodsDetail() {
		var cur_goods_num = document.getElementById("cur_goods_num");
		arrIdx = cur_goods_num.innerHTML - 1;

		var img_sticky = document.getElementById("img_sticky");
		var h_goods_id = document.frm_sticky.h_goods_id;
		var len = h_goods_id.length;

		if (len > 1) {
			goods_id = h_goods_id[arrIdx].value;
		} else {
			goods_id = h_goods_id.value;
		}

		var formObj = document.createElement("form");
		var i_goods_id = document.createElement("input");

		i_goods_id.name = "goods_id";
		i_goods_id.value = goods_id;

		formObj.appendChild(i_goods_id);
		document.body.appendChild(formObj);
		formObj.method = "get";
		formObj.action = "${contextPath}/goods/goodsDetail.do?goods_id="+ goods_id;
		formObj.submit();

	}
	//장바구니 빠른메뉴 END
</script>

<body>
	<div id="sticky">
		<div class="recent">
			<h3>최근 본 상품</h3>
			<ul>
				<c:choose>
					<%--상품선택없을시--%>
					<c:when test="${empty quickGoodsList}">
						<strong>상품이 없습니다.</strong>
					</c:when>
					<%--상품선택시--%>
					<c:otherwise>
						<form name="frm_sticky">
							<c:forEach var="item" items="${quickGoodsList }" varStatus="itemNum">
								<c:choose>
									<%--첫번째 상품보기--%>
									<c:when test="${itemNum.count==1}">
										<a href="javascript:goodsDetail();"> 
											<img width="75" height="95" id="img_sticky"
											src="${contextPath}/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
										</a>
										<input type="hidden" name="h_goods_id"
											value="${item.goods_id}" />
										<input type="hidden" name="h_goods_fileName"
											value="${item.goods_fileName}" />
										<br>
									</c:when>
									<%--나머지 상품정보--%>
									<c:otherwise>
										<input type="hidden" name="h_goods_id"
											value="${item.goods_id}" />
										<input type="hidden" name="h_goods_fileName"
											value="${item.goods_fileName}" />
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</form>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<div>
			<c:choose>
				<%--상품선택없을시--%>
				<c:when test="${ empty quickGoodsList }">
					<h5>&nbsp; &nbsp; &nbsp; &nbsp; 0/0 &nbsp;</h5>
				</c:when>
				<%--상품선택시 번호--%>
				<c:otherwise>
					<h5>
						<a href='javascript:fn_show_previous_goods();'> 이전 </a> &nbsp; 
						<span id="cur_goods_num">1</span>/${quickGoodsListNum} &nbsp;
						<a href='javascript:fn_show_next_goods();'> 다음 </a>
					</h5>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>


