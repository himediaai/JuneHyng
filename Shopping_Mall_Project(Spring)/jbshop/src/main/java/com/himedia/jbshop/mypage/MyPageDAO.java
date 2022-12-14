package com.himedia.jbshop.mypage;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.himedia.jbshop.members.MemberVO;
import com.himedia.jbshop.orders.OrderVO;

public interface MyPageDAO {
	public List<OrderVO> selectMyOrderGoodsList(String member_id) throws DataAccessException;

	public List selectMyOrderInfo(String order_id) throws DataAccessException;

	public List<OrderVO> selectMyOrderHistoryList(String member_id) throws DataAccessException;

	public void updateMyInfo(Map memberMap) throws DataAccessException;

	public MemberVO selectMyDetailInfo(String member_id) throws DataAccessException;

	public void updateMyOrderCancel(String order_id) throws DataAccessException;
	
	public String selectMyOrderTransactionId(String order_id) throws DataAccessException;
}
