package com.himedia.jbshop.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.himedia.jbshop.members.MemberVO;
import com.himedia.jbshop.orderTest.ApiService;
import com.himedia.jbshop.orders.OrderVO;

@Service("myPageService")
@Transactional(propagation = Propagation.REQUIRED)
public class MyPageServiceImpl implements MyPageService {
	@Autowired
	private MyPageDAO myPageDAO;

	@Autowired
	private ApiService apiService;
	
	public List<OrderVO> listMyOrderGoods(String member_id) throws Exception {
		return myPageDAO.selectMyOrderGoodsList(member_id);
	}

	public List findMyOrderInfo(String order_id) throws Exception {
		return myPageDAO.selectMyOrderInfo(order_id);
	}

	public List<OrderVO> listMyOrderHistory(String member_id) throws Exception {
		return myPageDAO.selectMyOrderHistoryList(member_id);
	}

	public MemberVO modifyMyInfo(Map memberMap) throws Exception {
		String member_id = (String) memberMap.get("member_id");
		myPageDAO.updateMyInfo(memberMap);
		return myPageDAO.selectMyDetailInfo(member_id);
	}

	public String cancelOrder(String order_id) throws Exception {

		//결제취소 요청API
		String merchantId="himedia";
		String base = "https://api.testpayup.co.kr";
		String cancelPath = "/v2/api/payment/" + merchantId + "/cancel2";
		String cancelUrl = base + cancelPath;
		String apiCertKey = "ac805b30517f4fd08e3e80490e559f8e";
		String transactionId=myPageDAO.selectMyOrderTransactionId(order_id);
		Map<String, String> map = new HashMap<String, String>();
		map.put("transactionId", transactionId);
		map.put("merchantId", merchantId);
		String signatureInput = merchantId + "|" + transactionId + "|" + apiCertKey;
		String signature = apiService.encrypt(signatureInput);
		map.put("signature", signature);
		//요청-응답
		Map<String, String> resultMap=apiService.restApi(map, cancelUrl);
		System.out.println("응답코드: "+resultMap);
		//결제취소 요청API
		String responseCode=resultMap.get("responseCode");
		if("0000".equals(responseCode)) {
			myPageDAO.updateMyOrderCancel(order_id);
			return resultMap.get("responseMsg");
		}else {
			return resultMap.get("responseMsg");
		}
	}

	public MemberVO myDetailInfo(String member_id) throws Exception {
		return myPageDAO.selectMyDetailInfo(member_id);
	}
}
