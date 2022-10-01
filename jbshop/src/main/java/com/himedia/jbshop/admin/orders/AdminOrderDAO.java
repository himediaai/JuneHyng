package com.himedia.jbshop.admin.orders;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.himedia.jbshop.members.MemberVO;
import com.himedia.jbshop.orders.OrderVO;

public interface AdminOrderDAO {
	public ArrayList<OrderVO> selectNewOrderList(Map condMap) throws DataAccessException;
	public void  updateDeliveryState(Map deliveryMap) throws DataAccessException;
	public ArrayList<OrderVO> selectOrderDetail(int order_id) throws DataAccessException;
	public MemberVO selectOrderer(String member_id) throws DataAccessException;
}
