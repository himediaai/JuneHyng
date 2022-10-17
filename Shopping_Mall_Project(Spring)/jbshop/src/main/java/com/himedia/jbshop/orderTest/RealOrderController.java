package com.himedia.jbshop.orderTest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RealOrderController {

	@Autowired
	private ApiService apiService;
	
	//가맹점ID
	private String merchantId="himedia";
	//Test용 baseURL
	private String base = "https://api.testpayup.co.kr";
	//비인증
	private String inPath = "/v2/api/payment/" + merchantId + "/keyin2";
	private String cancelPath = "/v2/api/payment/" + merchantId + "/cancel2";
	//인증
	private String certPath = "/ap/api/payment/" + merchantId + "/order";
	private String certCancelPath = "/ap/api/payment/" + merchantId + "/";
	//비인증 URL
	private String inUrl = base + inPath;
	private String cancelUrl = base + cancelPath;
	//인증 URL
	private String certUrl = base + certPath;
	private String certCancelUrl = base + certCancelPath;
	private String apiCertKey = "ac805b30517f4fd08e3e80490e559f8e";
	
	//비인증 결제 요청(카드번호,유효날짜,생년월일,카드비밀번호 앞2자리)
	@RequestMapping("/test/keyin.do")
	public Map<String, String> OrderTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//요청 입력
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderNumber", "TEST_12346");
		map.put("cardNo", "4119049001156477");
		map.put("expireMonth", "10");
		map.put("expireYear", "24");
		map.put("birthday", "910805");
		map.put("cardPw", "68");
		map.put("amount", "1000000");
		map.put("quota", "0");
		map.put("itemName", "노트북");
		map.put("userName", "이준형");
		map.put("timestamp", "20221012094700");
		// SHA-456 암호화하여 signature 입력
		String signatureInput = this.merchantId + "|" + map.get("orderNumber") + "|" + map.get("amount") + "|" + this.apiCertKey
				+ "|" + map.get("timestamp");
		String signature = apiService.encrypt(signatureInput);
		map.put("signature", signature);

		//요청-응답
		Map<String, String> resultMap=apiService.restApi(map, this.inUrl);
		System.out.println("transactionId: "+resultMap.get("transactionId"));
		return resultMap;
	}

	//비인증 결제 취소 요청(카드번호,유효날짜,생년월일,카드비밀번호 앞2자리)
	@RequestMapping("/test/cancleKey.do")
	public Map<String,String> CancelOrderTest(@RequestParam Map<String,String> dateMap) throws Exception {
		System.out.println(dateMap.toString());
		System.out.println("화면에서 보낸 거래번호"+dateMap.get("transactionId"));
		String transactionId = dateMap.get("transactionId");
		
		//요청 입력
		Map<String, String> map = new HashMap<String, String>();
		map.put("transactionId", transactionId);
		map.put("merchantId", this.merchantId);
		// SHA-456 암호화하여 signature 입력
		String signatureInput = this.merchantId + "|" + transactionId + "|" + this.apiCertKey;
		String signature = apiService.encrypt(signatureInput);
		map.put("signature", signature);
		
		//요청-응답
		Map<String, String> resultMap=apiService.restApi(map, this.cancelUrl);
		return resultMap;
	}
	
	//인증 결제 요청
	@RequestMapping("/test/cert.do")
	public ModelAndView certOrderTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//요청 입력
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderNumber", "TEST_12346");
		map.put("amount", "1000000");
		map.put("itemName", "노트북");
		map.put("userName", "이준형");
		map.put("userAgent", "WP");
		map.put("returnUrl", "http://www.google.com");
		map.put("timestamp", "20221012094700");
		// SHA-456 암호화하여 signature 입력
		String signatureInput = this.merchantId + "|" + map.get("orderNumber") + "|" + map.get("amount") + "|" + this.apiCertKey
				+ "|" + map.get("timestamp");
		String signature = apiService.encrypt(signatureInput);
		map.put("signature", signature);

		//요청-응답
		Map<String, String> resultMap=apiService.restApi(map, this.certUrl);
		System.out.println(resultMap);
		String viewName = (String) request.getAttribute("viewName");
		System.out.println(viewName);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("result",resultMap);
		return mav;
	}
	
	@RequestMapping("/test/kakaoOrder.do")
	public Map<String,String> kakaoOrder(@RequestParam Map<String,String> dateMap) throws Exception {
		System.out.println(dateMap);
		
		String merchantId="himedia";
		String base = "https://api.testpayup.co.kr";
		String Path = "/ep/api/kakao/" + merchantId + "/order";
		String Url = base + Path;
		String apiCertKey = "ac805b30517f4fd08e3e80490e559f8e";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderNumber", dateMap.get("orderNumber"));
		map.put("amount", "100");
		map.put("itemName", dateMap.get("itemName"));
		map.put("userName", dateMap.get("userName"));
		map.put("userAgent", "WP");
		map.put("returnUrl", "naver.com");
		map.put("timestamp", "20221012094700");
		// SHA-456 암호화하여 signature 입력
		String signatureInput = merchantId + "|" + map.get("orderNumber") + "|" + map.get("amount") + "|" + apiCertKey
				+ "|" + map.get("timestamp");
		String signature = apiService.encrypt(signatureInput);
		map.put("signature", signature);

		//요청-응답
		Map<String, String> resultMap=apiService.restApi(map,Url);
		System.out.println(resultMap);
		
		return resultMap;
	}
	
}
