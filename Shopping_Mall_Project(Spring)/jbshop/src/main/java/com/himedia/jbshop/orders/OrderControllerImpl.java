package com.himedia.jbshop.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.himedia.jbshop.common.BaseController;
import com.himedia.jbshop.goods.GoodsVO;
import com.himedia.jbshop.members.MemberVO;
import com.himedia.jbshop.orderTest.ApiService;

@Controller("orderController")
@RequestMapping(value = "/order")
public class OrderControllerImpl extends BaseController implements OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderVO orderVO;
	@Autowired
	private ApiService apiService;

	//상품 주문하나 하기
	@RequestMapping(value = "/orderEachGoods.do", method = RequestMethod.POST)
	public ModelAndView orderEachGoods(@ModelAttribute("orderVO") OrderVO _orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		Boolean isLogOn = (Boolean) session.getAttribute("isLogOn");
		String action = (String) session.getAttribute("action");
		
		if (isLogOn == null || isLogOn == false) {
			session.setAttribute("orderInfo", _orderVO);//로그인X시 로그인폼으로
			session.setAttribute("action", "/order/orderEachGoods.do");
			return new ModelAndView("redirect:/member/loginForm.do");
		} else {//로그인시
			if (action != null && action.equals("/order/orderEachGoods.do")) {
				orderVO = (OrderVO) session.getAttribute("orderInfo");//직전주문정보담기
				session.removeAttribute("action");
			} else {
				orderVO = _orderVO;//주문정보담기
			}
		}

		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		List myOrderList = new ArrayList<OrderVO>();
		myOrderList.add(orderVO);
		MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
		session.setAttribute("myOrderList", myOrderList);//세션 주문정보담기
		session.setAttribute("orderer", memberInfo);//세션 회원정보담기
		return mav;
	}

	//장바구니의 모든 상품주문하기
	@RequestMapping(value = "/orderAllCartGoods.do", method = RequestMethod.POST)
	public ModelAndView orderAllCartGoods(@RequestParam("cart_goods_qty") String[] cart_goods_qty,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session = request.getSession();
		Map cartMap = (Map) session.getAttribute("cartMap");
		List myOrderList = new ArrayList<OrderVO>();

		List<GoodsVO> myGoodsList = (List<GoodsVO>) cartMap.get("myGoodsList");
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");

		for (int i = 0; i < cart_goods_qty.length; i++) {
			String[] cart_goods = cart_goods_qty[i].split(":");
			for (int j = 0; j < myGoodsList.size(); j++) {
				GoodsVO goodsVO = myGoodsList.get(j);
				int goods_id = goodsVO.getGoods_id();
				if (goods_id == Integer.parseInt(cart_goods[0])) {
					OrderVO _orderVO = new OrderVO();
					String goods_title = goodsVO.getGoods_title();
					int goods_sales_price = goodsVO.getGoods_sales_price();
					String goods_fileName = goodsVO.getGoods_fileName();
					_orderVO.setGoods_id(goods_id);
					_orderVO.setGoods_title(goods_title);
					_orderVO.setGoods_sales_price(goods_sales_price);
					_orderVO.setGoods_fileName(goods_fileName);
					_orderVO.setOrder_goods_qty(Integer.parseInt(cart_goods[1]));
					myOrderList.add(_orderVO);
					break;
				}
			}
		}
		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberVO);
		System.out.println("주문리스트: "+session.getAttribute("myOrderList"));
		System.out.println("주문자: "+session.getAttribute("orderer"));
		return mav;
	}

	//여러개 주문하기{주문정보,배송정보 리턴}
	@RequestMapping(value = "/payToOrderGoods.do", method = RequestMethod.POST)
	public ModelAndView payToOrderGoods(@RequestParam Map<String, String> receiverMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("orderer");
		String member_id = memberVO.getMember_id();
		String orderer_name = memberVO.getMember_name();
		String orderer_hp = memberVO.getHp1() + "-" + memberVO.getHp2() + "-" + memberVO.getHp3();
		List<OrderVO> myOrderList = (List<OrderVO>) session.getAttribute("myOrderList");

		//-------------------결제 API--------------------
		String orderNumber="";
		String amount="";
		String itemName="";
		String userName="";
		
		for(OrderVO vo:myOrderList) {
			orderNumber=String.valueOf(vo.getOrder_seq_num());
			amount=String.valueOf(vo.getGoods_sales_price());
			itemName=vo.getGoods_title();
			userName=memberVO.getMember_name();
		}
		
		String merchantId="himedia";
		String base = "https://api.testpayup.co.kr";
		String inPath = "/v2/api/payment/" + merchantId + "/keyin2";
		String inUrl = base + inPath;
		String apiCertKey = "ac805b30517f4fd08e3e80490e559f8e";
		
		System.out.println("주문자 정보:"+receiverMap);
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderNumber", orderNumber);
		map.put("cardNo", receiverMap.get("cardNo"));
		map.put("expireMonth", receiverMap.get("expireMonth"));
		map.put("expireYear", receiverMap.get("expireYear"));
		map.put("birthday", receiverMap.get("birthday"));
		map.put("cardPw", receiverMap.get("cardPw"));
		map.put("amount", amount);
		map.put("quota", "0");
		map.put("itemName", itemName);
		map.put("userName", userName);
		map.put("timestamp", "20221012094700");
		
		// SHA-456 암호화하여 signature 입력
		String signatureInput = merchantId + "|" + map.get("orderNumber") + "|" + map.get("amount") + "|" + apiCertKey
				+ "|" + map.get("timestamp");
		String signature = apiService.encrypt(signatureInput);
		map.put("signature", signature);

		//요청-응답
		Map<String, String> resultMap=apiService.restApi(map,inUrl);
		System.out.println("응답코드: "+resultMap);
		System.out.println("transactionId: "+resultMap.get("transactionId"));
		String responseCode=resultMap.get("responseCode");
		System.out.println("응답메시지: "+resultMap.get("responseMsg"));
		//-------------------결제 API--------------------
		
		
		//주문리스트가져와서 주문정보 리스트 채우기
		//"0000"==responseCode (X)
		if("0000".equals(responseCode)) {
			for (int i = 0; i < myOrderList.size(); i++) {
				OrderVO orderVO = (OrderVO) myOrderList.get(i);
				orderVO.setMember_id(member_id);
				orderVO.setOrderer_name(orderer_name);
				orderVO.setReceiver_name(receiverMap.get("receiver_name"));

				orderVO.setReceiver_hp1(receiverMap.get("receiver_hp1"));
				orderVO.setReceiver_hp2(receiverMap.get("receiver_hp2"));
				orderVO.setReceiver_hp3(receiverMap.get("receiver_hp3"));
				orderVO.setReceiver_tel1(receiverMap.get("receiver_tel1"));
				orderVO.setReceiver_tel2(receiverMap.get("receiver_tel2"));
				orderVO.setReceiver_tel3(receiverMap.get("receiver_tel3"));

				orderVO.setDelivery_address(receiverMap.get("delivery_address"));
				orderVO.setDelivery_message(receiverMap.get("delivery_message"));
				orderVO.setDelivery_method("일반택배");
				orderVO.setGift_wrapping("no");
				orderVO.setPay_method("카드결제");
				orderVO.setCard_com_name("카드");
				orderVO.setCard_pay_month("일시불");
				orderVO.setPay_orderer_hp_num(receiverMap.get("pay_orderer_hp_num"));
				orderVO.setOrderer_hp(orderer_hp);
				orderVO.setTransactionId(resultMap.get("transactionId"));
				myOrderList.set(i, orderVO);
			} //주문리스트가져와서 주문정보 리스트 채우기

			orderService.addNewOrder(myOrderList);
			
			mav.addObject("cardName", resultMap.get("cardName"));
			mav.addObject("authNumber", resultMap.get("authNumber"));
			mav.addObject("authDateTime", resultMap.get("authDateTime"));
			
			mav.addObject("myOrderInfo", receiverMap);
			mav.addObject("myOrderList", myOrderList);
			return mav;
		}else {
			mav.addObject("msg",resultMap.get("responseMsg"));
			mav.setViewName("/order/payAlert");
			return mav;
		}
		
	}

	@Override
	@RequestMapping(value = "/payToOrderGoodsKakao.do", method = RequestMethod.POST)
	public ModelAndView kakaoOrders(@RequestParam Map<String, String> orderMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		System.out.println("카카오 인증데이터 확인: "+orderMap.toString());
		//-------------------결제 API--------------------
		
		String merchantId = "himedia";
		String base = "https://api.testpayup.co.kr";
		String inPath = "/ep/api/kakao/" + merchantId + "/pay";
		String inUrl = base + inPath;

		Map<String, String> map = new HashMap<String, String>();
		map.put("res_cd", orderMap.get("res_cd"));
		map.put("enc_info", orderMap.get("enc_info"));
		map.put("enc_data", orderMap.get("enc_data"));
		map.put("tran_cd", orderMap.get("tran_cd"));
		map.put("card_pay_method",orderMap.get("card_pay_method"));
		map.put("ordr_idxx", orderMap.get("ordr_idxx"));
		
		// 요청-응답
		Map<String, String> resultMap = apiService.restApi(map, inUrl);
		System.out.println("응답코드: " + resultMap);
		System.out.println("transactionId: " + resultMap.get("transactionId"));
		String responseCode = resultMap.get("responseCode");
		System.out.println("응답메시지: " + resultMap.get("responseMsg"));
		// -------------------결제 API--------------------

		// "0000"==responseCode (X)
		if ("0000".equals(responseCode)) {
			mav.setViewName("/order/payToOrderGoodsKakao");
		} else {
			mav.addObject("msg", resultMap.get("responseMsg"));
			mav.setViewName("/order/payAlert");
		}
		
		return mav;
	}

}
