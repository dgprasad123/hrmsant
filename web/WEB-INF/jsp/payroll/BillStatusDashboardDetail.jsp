
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
                                                <li>
                                                    <i class="fa fa-dashboard"></i>  <a href="BillStatusDashboard.htm">Bill Status Dashboard</a>
                                                </li>                                                
                                                <li class="active">
                                                    <i class="fa fa-file"></i> Bill Status Dashboard Detail
                                                </li>
                                            </ol>
                                        </div>
                                    </div> 
                                    <h1 style="font-size:18pt;margin-top:0px;">Bill Status Dashboard Detail</h1>
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped">
                                            <thead>
                                                <tr style="background-color: #0071c5;color: #ffffff;">
                                                    <th>Sl No</th>
                                                    <th>Bill No</th>
                                                    <th>Bill Description</th>
                                                    <th>DDO Code</th>
                                                    <th>Major Head</th>
                                                    <th>Treasury Code</th>
                                                    <th>Process Date</th>
                                                    <th>No. of Days Since Processed</th>
                                                </tr>
                                            </thead>

                                            <c:forEach items="${billListDetails}" var="blist" varStatus="count">
                                                <tr>  
                                                    <td>${count.index + 1}</td>
                                                    <td>${blist.billNo}</td>
                                                    <td>${blist.billGroupDesc}</td>
                                                    <td>${blist.ddoCode}</td>
                                                    <td>${blist.majorHead}</td>
                                                    <td>${blist.trCode}</td>
                                                    <td>${blist.tkndate}</td>
                                                    <td>${blist.vchNo}</td>
                                                </tr>
                                            </c:forEach>

                                        </table>
                                    </div>      
                                    <div style="height:200px;"></div>

                                </div>
                            </div>
                    </body>
                    </html>
