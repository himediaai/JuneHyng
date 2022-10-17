package com.himedia.jbshop.mypage;

import java.util.List;
import java.util.Map;

import com.himedia.jbshop.members.MemberVO;
import com.himedia.jbshop.orders.OrderVO;

public interface MyPageService {
	public List<OrderVO> listMyOrderGoods(String member_id) throws Exception;

	public List findMyOrderInfo(String order_id) throws Exception;

	public List<OrderVO> listMyOrderHistory(String member_id) throws Exception;

	public MemberVO modifyMyInfo(Map memberMap) throws Exception;

	public String cancelOrder(String order_id) throws Exception;

	public MemberVO myDetailInfo(String member_id) throws Exception;

}
