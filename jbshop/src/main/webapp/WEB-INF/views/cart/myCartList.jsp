<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="myCartList" value="${cartMap.myCartList}" />
<c:set var="myGoodsList" value="${cartMap.myGoodsList}" />

<c:set var="totalGoodsNum" value="0" />
<!--주문 개수 -->
<c:set var="totalDeliveryPrice" value="0" />
<!-- 총 배송비 -->
<c:set var="totalDiscountedPrice" value="0" />
<!-- 총 할인금액 -->
<head>
<script type="text/javascript">

function calcGoodsPrice(bookPrice,obj){
	var totalPrice,final_total_price,totalNum;
	var goods_qty=document.getElementById("select_goods_qty");
	//alert("총 상품금액"+goods_qty.value);
	var p_totalNum=document.getElementById("p_totalNum");
	var p_totalPrice=document.getElementById("p_totalPrice");
	var p_final_totalPrice=document.getElementById("p_final_totalPrice");
	var h_totalNum=document.getElementById("h_totalNum");
	var h_totalPrice=document.getElementById("h_totalPrice");
	var h_totalDelivery=document.getElementById("h_totalDelivery");
	var h_final_total_price=document.getElementById("h_final_totalPrice");
	if(obj.checked==true){
	//	alert("체크 했음")
		
		totalNum=Number(h_totalNum.value)+Number(goods_qty.value);
		//alert("totalNum:"+totalNum);
		totalPrice=Number(h_totalPrice.value)+Number(goods_qty.value*bookPrice);
		//alert("totalPrice:"+totalPrice);
		final_total_price=totalPrice+Number(h_totalDelivery.value);
		//alert("final_total_price:"+final_total_price);

	}else{
	//	alert("h_totalNum.value:"+h_totalNum.value);
		totalNum=Number(h_totalNum.value)-Number(goods_qty.value);
	//	alert("totalNum:"+ totalNum);
		totalPrice=Number(h_totalPrice.value)-Number(goods_qty.value)*bookPrice;
	//	alert("totalPrice="+totalPrice);
		final_total_price=totalPrice-Number(h_totalDelivery.value);
	//	alert("final_total_price:"+final_total_price);
	}
	
	h_totalNum.value=totalNum;
	
	h_totalPrice.value=totalPrice;
	h_final_total_price.value=final_total_price;
	
	p_totalNum.innerHTML=totalNum;
	p_totalPrice.innerHTML=totalPrice;
	p_final_totalPrice.innerHTML=final_total_price;
}

//상품 갯수 수정 START
function modify_cart_qty(goods_id,bookPrice,index){

   	var length=document.frm_order_all_cart.cart_goods_qty.length;
   	var _cart_goods_qty=0;
   	
	if(length>1){ //카트에 제품이 한개인 경우와 여러개인 경우 나누어서 처리한다.
		_cart_goods_qty=document.frm_order_all_cart.cart_goods_qty[index].value;		
	}else{
		_cart_goods_qty=document.frm_order_all_cart.cart_goods_qty.value;
	}
	
	var cart_goods_qty=Number(_cart_goods_qty);

	$.ajax({
		type : "post",
		async : false, //false인 경우 동기식으로 처리한다.
		url : "${contextPath}/cart/modifyCartQty.do",
		data : {
			goods_id:goods_id,
			cart_goods_qty:cart_goods_qty
		},
		success : function(data, textStatus) {
			console.log('받은데이터값: '+data)
			if(data.trim()=='modify_success'){
				alert("수량을 변경했습니다!!");	
			}else{
				alert("다시 시도해 주세요!!");	
			}
		},
		error : function(data, textStatus) {
			alert("에러가 발생했습니다."+data);
		},
		complete : function(data, textStatus) {
			//alert("작업을완료 했습니다");
		}
	});
}
//상품 갯수 수정 END

//상품 장바구니삭제 START
function delete_cart_goods(cart_id){
	var cart_id=Number(cart_id);
	var formObj=document.createElement("form");//form과 input태그 생성
	var i_cart = document.createElement("input");
	i_cart.name="cart_id";//input태그 설정
	i_cart.value=cart_id;
	
	formObj.appendChild(i_cart);//form태그 설정
    document.body.appendChild(formObj); 
    formObj.method="post";
    formObj.action="${contextPath}/cart/removeCartGoods.do";
    formObj.submit();
}
//상품 장바구니삭제 END

//상품 주문하기 START
function fn_order_each_goods(goods_id,goods_title,goods_sales_price,fileName){
	var total_price,final_total_price,_goods_qty;
	var cart_goods_qty=document.getElementById("cart_goods_qty");
	//주문 폼생성
	_order_goods_qty=cart_goods_qty.value; //장바구니에 담긴 개수 만큼 주문한다.
	var formObj=document.createElement("form");
	var i_goods_id = document.createElement("input"); 
    var i_goods_title = document.createElement("input");
    var i_goods_sales_price=document.createElement("input");
    var i_fileName=document.createElement("input");
    var i_order_goods_qty=document.createElement("input");
    //주문폼에 이름생성
    i_goods_id.name="goods_id";
    i_goods_title.name="goods_title";
    i_goods_sales_price.name="goods_sales_price";
    i_fileName.name="goods_fileName";
    i_order_goods_qty.name="order_goods_qty";
    //주문폼에 값생성
    i_goods_id.value=goods_id;
    i_order_goods_qty.value=_order_goods_qty;
    i_goods_title.value=goods_title;
    i_goods_sales_price.value=goods_sales_price;
    i_fileName.value=fileName;
    
    formObj.appendChild(i_goods_id);
    formObj.appendChild(i_goods_title);
    formObj.appendChild(i_goods_sales_price);
    formObj.appendChild(i_fileName);
    formObj.appendChild(i_order_goods_qty);
	//주문정보 보내기
    document.body.appendChild(formObj); 
    formObj.method="post";
    formObj.action="${contextPath}/order/orderEachGoods.do";
    formObj.submit();
}
//상품 주문하기 END

//장바구니의 모든 상품주문하기 START
function fn_order_all_cart_goods(){
	var order_goods_qty;
	var order_goods_id;
	var objForm=document.frm_order_all_cart;
	var cart_goods_qty=objForm.cart_goods_qty;
	var h_order_each_goods_qty=objForm.h_order_each_goods_qty;
	var checked_goods=objForm.checked_goods;
	var length=checked_goods.length;
	
	//체크된 주문(주문ID:주문갯수 String[]으로 cart_goods_qty반환)
	if(length>1){//체크된 주문갯수 2개이상일시
		for(var i=0; i<length;i++){
			if(checked_goods[i].checked==true){
				order_goods_id=checked_goods[i].value;
				order_goods_qty=cart_goods_qty[i].value;
				cart_goods_qty[i].value="";
				cart_goods_qty[i].value=order_goods_id+":"+order_goods_qty;
				//alert(select_goods_qty[i].value);
				console.log(cart_goods_qty[i].value);
			}
		}	
	}else{//체크된 주문갯수 1개일시
		order_goods_id=checked_goods.value;
		order_goods_qty=cart_goods_qty.value;
		cart_goods_qty.value=order_goods_id+":"+order_goods_qty;
	}

 	objForm.method="post";
 	objForm.action="${contextPath}/order/orderAllCartGoods.do";
	objForm.submit();
}
//장바구니의 모든 상품주문하기 END

</script>
</head>
<body>
	<table class="list_view">
		<tbody align=center>
			<tr style="background: #33ff00">
				<td class="fixed">구분</td>
				<td colspan=2 class="fixed">상품명</td>
				<td>정가</td>
				<td>판매가</td>
				<td>수량</td>
				<td>합계</td>
				<td>주문</td>
			</tr>
			<form name="frm_order_all_cart">
			<c:choose>
				<c:when test="${empty myCartList}">
					<tr>
						<td colspan=8 class="fixed"><strong>장바구니에 상품이 없습니다.</strong></td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="item" items="${myGoodsList}" varStatus="cnt">
						<tr>
							<c:set var="cart_goods_qty" value="${myCartList[cnt.count-1].cart_goods_qty}" />
							<c:set var="cart_id" value="${myCartList[cnt.count-1].cart_id}" />
							<td>
								<input type="checkbox" name="checked_goods" checked value="${item.goods_id}"
									onClick="calcGoodsPrice(${item.goods_sales_price},this)">
							</td>
							<td class="goods_image">
								<a href="${contextPath}/goods/goodsDetail.do?goods_id=${item.goods_id}">
								<img width="75" alt="" src="${contextPath}/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}" />
								</a>
							</td>
							<td>
								<h2>
									<a href="${contextPath}/goods/goodsDetail.do?goods_id=${item.goods_id}">${item.goods_title}</a>
								</h2>
							</td>
							<td class="price">
								<span>${item.goods_price}원</span>
							</td>
							<td>
								<strong> 
									<fmt:formatNumber value="${item.goods_sales_price*0.9}" type="number" var="discounted_price" /> 
									${discounted_price}원(10%할인)
								</strong>
							</td>
							<td>
								<input type="text" id="cart_goods_qty" name="cart_goods_qty" size=3 value="${cart_goods_qty}"><br>
								<a href="javascript:modify_cart_qty(${item.goods_id},${item.goods_sales_price*0.9 },${cnt.count-1});">
									<img width=25 alt="" src="${contextPath}/resources/image/btn_modify_qty.jpg">
								</a>
							</td>
							<td>
								<strong> 
									<fmt:formatNumber value="${item.goods_sales_price*0.9*cart_goods_qty}" type="number" var="total_sales_price" />
									${total_sales_price}원
								</strong>
							</td>
							<td>
								<a href="javascript:fn_order_each_goods('${item.goods_id }','${item.goods_title }','${item.goods_sales_price}','${item.goods_fileName}');">
									<img width="75" alt="" src="${contextPath}/resources/image/btn_order.jpg">
								</a><br> 
								<a href="javascript:delete_cart_goods('${cart_id}');"">
									<img width="75" alt="" src="${contextPath}/resources/image/btn_delete.jpg">
								</a>
							</td>
						</tr>
					</c:forEach>
					<c:set var="totalGoodsPrice" value="${totalGoodsPrice+item.goods_sales_price*0.9*cart_goods_qty}" />
					<c:set var="totalGoodsNum" value="${totalGoodsNum+1 }" />
				</c:otherwise>
			</c:choose>
			</form>
		</tbody>
	</table>
	<div class="clear"></div>
	<br><br>
	<center>
		<br><br>
		<a href="javascript:fn_order_all_cart_goods()">
			<img width="75" alt="" src="${contextPath}/resources/image/btn_order_final.jpg">
		</a>
</body>