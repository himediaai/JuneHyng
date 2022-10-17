<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	isELIgnored="false"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<body>
<script type="text/javascript">
	function m_Completepayment(FormOrJson, closeEvent) {
		var frm = document.kcp_order_info;
		GetField(frm, FormOrJson);
		console.log(frm);
		if (frm.res_cd.value == "0000") {
			frm.submit();
		} else {
			alert("[" + frm.res_cd.value + "] " + frm.res_msg.value);
			closeEvent();
		}
	}
	function jsf__pay() {
		try {
			var form = document.kcp_order_info;
			KCP_Pay_Execute(form);
		} catch (e) {
		}
	}
</script>
<script type="text/javascript" src="https://pay.kcp.co.kr/plugin/payplus_web.jsp"></script>
	<form name="kcp_order_info" action="${result.Ret_URL }" method="post" accept-charset="euc-kr">
		<input type="hidden" name="ordr_idxx" value="${result.ordr_idxx }"> 
		<input type="hidden" name="good_name" value="${result.good_name }"> 
		<input type="hidden" name="good_mny" value="${result.good_mny }"> 
		<input type="hidden" name="buyr_name" value="${result.buyr_name }"> 
		<input type="hidden" name="site_cd" value="${result.site_cd }"> 
		<input type="hidden" name="req_tx" value="pay"> 
		<input type="hidden" name="pay_method" value="100000000000"> 
		<input type="hidden" name="site_name" value="payup" /> 
		<input type="hidden" name="res_cd" value="" /> 
		<input type="hidden" name="res_msg" value="" /> 
		<input type="hidden" name="enc_info" value="" /> 
		<input type="hidden" name="enc_data" value="" /> 
		<input type="hidden" name="ret_pay_method" value="" /> 
		<input type="hidden" name="tran_cd" value="" /> 
		<input type="hidden" name="use_pay_method" value="" /> 
		<input type="hidden" name="buyr_mail" value=""> 
		<input type="hidden" name="ordr_chk" value="" /> 
		<input type="hidden" name="good_expr" value="0"> 
		<input type="hidden" name="module_type" value="01" />
		<input type="hidden" name="currency" value="WON" />
	</form>
</body>