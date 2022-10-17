package com.himedia.jbshop.main;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.himedia.jbshop.goods.GoodsService;
import com.himedia.jbshop.goods.GoodsVO;


@Controller("mainController")
@EnableAspectJAutoProxy
public class MainController {
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value= {"/*","/main/main.do"} ,method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		HttpSession session=request.getSession();
//		session.setAttribute("side_menu", "user");
		ModelAndView mav=new ModelAndView();
//		String viewName=(String)request.getAttribute("viewName");
		String viewName="/main/main";
		mav.setViewName(viewName);
		//상품8행 정보 내보내기
		Map<String,List<GoodsVO>> goodsMap=goodsService.listGoods();
		mav.addObject("goodsMap", goodsMap);
		mav.addObject("side_menu", "user");//side를 user로 default
		return mav;
	}
}
