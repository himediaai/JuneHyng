package com.himedia.jbshop.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;

@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsControllerImpl implements GoodsController {
	@Autowired
	private GoodsService goodsService;
	
	//(상품ID)에 맞는 {상품정보,파일정보}mav리턴, 빠른메뉴에 상품정보넣기
	@RequestMapping(value="/goodsDetail.do" ,method = RequestMethod.GET)
	public ModelAndView goodsDetail(@RequestParam("goods_id") String goods_id,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		HttpSession session=request.getSession();
		ModelAndView mav = new ModelAndView(viewName);
		//상품정보및 파일정보 mav에 넣기
		Map goodsMap=goodsService.goodsDetail(goods_id);
		mav.addObject("goodsMap", goodsMap);
		//빠른메뉴에 상품정보넣기
		GoodsVO goodsVO=(GoodsVO)goodsMap.get("goodsVO");
		addGoodsInQuick(goods_id,goodsVO,session);
		return mav;
	}
	
	//(키워드)에 따라 [상품제목]리턴
	@RequestMapping(value="/keywordSearch.do",method = RequestMethod.GET,produces = "application/text; charset=utf8")
	public @ResponseBody String keywordSearch(@RequestParam("keyword") String keyword,
			                                  HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//키워드 없을시
		if(keyword == null || keyword.equals("")) {
			return null ;
		}
		//키워드 있을시 상품제목 풀네임 가져오기
		keyword = keyword.toUpperCase();
	    List<String> keywordList =goodsService.keywordSearch(keyword);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("keyword", keywordList);
	    String jsonInfo = jsonObject.toString();
	    System.out.println("검색키워드:"+keyword);
	    System.out.println(jsonInfo);
	    return jsonInfo ;
	}
	
	//(상품제목과 비슷한) [상품정보 및 파일이름]mav리턴
	@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		List<GoodsVO> goodsList=goodsService.searchGoods(searchWord);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsList", goodsList);
		return mav;
	}
	
	//빠른메뉴에 상품정보 및 상품갯수 세션에 저장
	private void addGoodsInQuick(String goods_id,GoodsVO goodsVO,HttpSession session){
		boolean already_existed=false;
		List<GoodsVO> quickGoodsList;
		quickGoodsList=(ArrayList<GoodsVO>)session.getAttribute("quickGoodsList");
		
		//세션에 상품정보 있을시
		if(quickGoodsList!=null){
			//세션에 상품정보 4개만저장
			if(quickGoodsList.size() < 4){
				//상품정보가 이미 존재할경우 저장X
				for(int i=0; i<quickGoodsList.size();i++){
					GoodsVO _goodsBean=(GoodsVO)quickGoodsList.get(i);
					if(goods_id.equals(_goodsBean.getGoods_id())){
						already_existed=true;
						break;
					}
				}
				//상품정보가 존재하지 않을경우 저장O
				if(already_existed==false){
					quickGoodsList.add(goodsVO);
				}
			}
		//세션에 상품정보 없을시
		}else{
			quickGoodsList=new ArrayList<GoodsVO>();
			quickGoodsList.add(goodsVO);
			
		}
		session.setAttribute("quickGoodsList",quickGoodsList);
		session.setAttribute("quickGoodsListNum", quickGoodsList.size());
	}

}
