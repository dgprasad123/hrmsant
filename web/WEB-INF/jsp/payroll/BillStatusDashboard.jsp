
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .shadow {
                -webkit-box-shadow: 1px 1px 2px 3px #ccc;  /* Safari 3-4, iOS 4.0.2 - 4.2, Android 2.3+ */
                -moz-box-shadow:    1px 1px 2px 3px #ccc;  /* Firefox 3.5 - 3.6 */
                box-shadow:         1px 1px 2px 3px #ccc;  /* Opera 10.5, IE 9, Firefox 4+, Chrome 6+, iOS 5 */
            }            
        </style>
    </head>
    <c:choose>
        <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/AlfaMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                        <div class="container-fluid" style="min-height:450px;background:url('images/logo_fade.png') no-repeat top center">
                        </c:when>
                        <c:otherwise>
                            <body>
                                <div id="wrapper">
                                    <jsp:include page="../tab/hrmsadminmenu.jsp"/>
                                    <div id="page-wrapper">
                                        <div class="container-fluid">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="row">
                                        <div class="col-lg-12">                            
                                            <ol class="breadcrumb">
                                                <li>
                                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                                </li>
                                                <li class="active">
                                                    <i class="fa fa-file"></i> Bill Status Dashboard 
                                                </li>
                                            </ol>
                                        </div>
                                    </div> 
                                    <h1 style="font-size:18pt;margin-top:0px;">Bill Status Dashboard</h1>
                                    <h2 style="color:#008900;">Bills in HRMS Process Queue:</h2>
                                    <div class="row">
                                        <div class="col-lg-3" style="cursor:pointer" onclick="javascript: self.location='BillStatusDashboardDetail.htm?type=1'">
                                            <div style="width:96%;height:130px;background:#FF0000;border-radius:5px;" class="shadow">
                                                <div style="height:70px;border-bottom:1px solid #FFFFFF;font-size:35pt;color:#FFFFFF;font-weight:bold;text-align:center;line-height:70px;text-shadow: 1px 1px #000000;">${totalUnderProcess}</div>
                                                <div style="height:60px;text-align:center;line-height:60px;font-size:14pt;color:#FFFFFF;font-weight:bold;">Total Bills Under Process</div>
                                            </div>
                                        </div>
                                        <div class="col-lg-3" style="cursor:pointer" onclick="javascript: self.location='BillStatusDashboardDetail.htm?type=2'">
                                            <div style="width:96%;height:130px;background:#008900;border-radius:5px;" class="shadow">
                                                <div style="height:70px;border-bottom:1px solid #FFFFFF;font-size:35pt;color:#FFFFFF;font-weight:bold;text-align:center;line-height:70px;text-shadow: 1px 1px #000000;">${totalUnderProcessLess3}</div>
                                                <div style="height:60px;text-align:center;line-height:30px;font-size:14pt;color:#FFFFFF;font-weight:bold;">Bills Under Process<br />(<= 3 Days)</div>                                
                                            </div>
                                        </div>   
                                        <div class="col-lg-3" style="cursor:pointer" onclick="javascript: self.location='BillStatusDashboardDetail.htm?type=3'">
                                            <div style="width:96%;height:130px;background:#000089;border-radius:5px;" class="shadow">
                                                <div style="height:70px;border-bottom:1px solid #FFFFFF;font-size:35pt;color:#FFFFFF;font-weight:bold;text-align:center;line-height:70px;text-shadow: 1px 1px #000000;">${totalUnderProcessLess30}</div>
                                                <div style="height:60px;text-align:center;line-height:30px;font-size:14pt;color:#FFFFFF;font-weight:bold;">Bills Under Process<br />(> 3 & <= 30 days)</div>                                                                
                                            </div>
                                        </div>   
                                        <div class="col-lg-3" style="cursor:pointer" onclick="javascript: self.location='BillStatusDashboardDetail.htm?type=4'">
                                            <div style="width:96%;height:130px;background:#008989;border-radius:5px;" class="shadow">
                                                <div style="height:70px;border-bottom:1px solid #FFFFFF;font-size:35pt;color:#FFFFFF;font-weight:bold;text-align:center;line-height:70px;text-shadow: 1px 1px #000000;">${totalUnderProcessGreat30}</div>
                                                <div style="height:60px;text-align:center;line-height:30px;font-size:14pt;color:#FFFFFF;font-weight:bold;">Bills Under Process<br />(> 30 days)</div>                                                                
                                            </div>
                                        </div>   
                                    </div>
                                                    <h2 style="color:#008900;">Bills Submitted to Treasury and under Process:</h2>
                                    <div class="row" style="margin-top:20px;">
                                        <div class="col-lg-3" style="cursor:pointer" onclick="javascript: self.location='BillTreasuryDashboardDetail.htm?type=1'">
                                            <div style="width:96%;height:130px;background:#FF0000;border-radius:5px;" class="shadow">
                                                <div style="height:70px;border-bottom:1px solid #FFFFFF;font-size:35pt;color:#FFFFFF;font-weight:bold;text-align:center;line-height:70px;text-shadow: 1px 1px #000000;">${trTotalUnderProcess}</div>
                                                <div style="height:60px;text-align:center;line-height:60px;font-size:14pt;color:#FFFFFF;font-weight:bold;">Total Bills Submitted to Treasury</div>
                                            </div>
                                        </div>
                                        <div class="col-lg-3" style="cursor:pointer" onclick="javascript: self.location='BillTreasuryDashboardDetail.htm?type=2'">
                                            <div style="width:96%;height:130px;background:#008900;border-radius:5px;" class="shadow">
                                                <div style="height:70px;border-bottom:1px solid #FFFFFF;font-size:35pt;color:#FFFFFF;font-weight:bold;text-align:center;line-height:70px;text-shadow: 1px 1px #000000;">${trTotalUnderProcessLess3}</div>
                                                <div style="height:60px;text-align:center;line-height:30px;font-size:14pt;color:#FFFFFF;font-weight:bold;">Bills Submitted to Treasury<br />(<= 3 Days)</div>                                
                                            </div>
                                        </div>   
                                        <div class="col-lg-3" style="cursor:pointer" onclick="javascript: self.location='BillTreasuryDashboardDetail.htm?type=3'">
                                            <div style="width:96%;height:130px;background:#000089;border-radius:5px;" class="shadow">
                                                <div style="height:70px;border-bottom:1px solid #FFFFFF;font-size:35pt;color:#FFFFFF;font-weight:bold;text-align:center;line-height:70px;text-shadow: 1px 1px #000000;">${trTotalUnderProcessLess30}</div>
                                                <div style="height:60px;text-align:center;line-height:30px;font-size:14pt;color:#FFFFFF;font-weight:bold;">Bills Submitted to Treasury<br />(> 3 & <= 30 days)</div>                                                                
                                            </div>
                                        </div>   
                                        <div class="col-lg-3" style="cursor:pointer" onclick="javascript: self.location='BillTreasuryDashboardDetail.htm?type=4'">
                                            <div style="width:96%;height:130px;background:#008989;border-radius:5px;" class="shadow">
                                                <div style="height:70px;border-bottom:1px solid #FFFFFF;font-size:35pt;color:#FFFFFF;font-weight:bold;text-align:center;line-height:70px;text-shadow: 1px 1px #000000;">${trTotalUnderProcessGreat30}</div>
                                                <div style="height:60px;text-align:center;line-height:30px;font-size:14pt;color:#FFFFFF;font-weight:bold;">Bills Submitted to Treasury<br />(> 30 days)</div>                                                                
                                            </div>
                                        </div>   
                                    </div>   
                                                <div style="height:300px;"></div>

                                </div>
                            </div>
                    </body>
                    </html>
