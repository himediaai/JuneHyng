package com.himedia.jbshop.orders;

import java.util.List;

public interface OrderService {
	public List<OrderVO> listMyOrderGoods(OrderVO orderVO) throws Exception;

	public void addNewOrder(List<OrderVO> myOrderList) throws Exception;

	public OrderVO findMyOrder(String order_id) throws Exception;

}
