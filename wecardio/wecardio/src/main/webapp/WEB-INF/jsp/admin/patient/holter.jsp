<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/headModule.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="admin.patient.list.invite"/></title>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/upHolter.css" />
<script type="text/javascript" src="${path}/resources/admin/patient/js/holterUp.js"></script>
<script type="text/javascript">
    var searchAccountMsg = "<spring:message code="admin.patient.list.searchAccount"/>";
    var searchTipAccountMsg = "<spring:message code="admin.patient.list.searchTipAccount"/>";
</script>
</head>

<body>
		
        <div class="addressBar">
            <a class="leader" href="${path}/${sessionScope.userTypePath}/home"><spring:message code="admin.bread.home"/></a>
            <span class="arrowText">></span>
            <span class="normal"><spring:message code="common.btn.patient.holterupBtn"/></span>
            <a class="goback" onclick="history.go(-1);"><spring:message code="admin.bread.back"/></a>
        </div>
        
		<div class="upextrea">
			<div id="uploader" class="upextreamain">
	    		 <div class="btns">
	   			 	 <div id="pick"><spring:message code="doctor.bindPatient.selectFile"/></div>
	       		     <div id="ctlBtn" class="btn btn-default"><spring:message code="doctor.bindPatient.startUpload"/></div>
	   			 </div>
	   			 <div id="thelist" class="uploader-list"></div>
			</div>
		</div>	
</body>
</html>
  