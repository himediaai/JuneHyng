package com.himedia.jbshop.orders;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public interface OrderController {
	public ModelAndView orderEachGoods(@ModelAttribute("orderVO") OrderVO _orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	public ModelAndView orderAllCartGoods(@RequestParam String[] cart_goods_qty, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	public ModelAndView payToOrderGoods(@RequestParam Map<String, String> orderMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public ModelAndView kakaoOrders(@RequestParam Map<String, String> orderMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}
