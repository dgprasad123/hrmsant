<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <script type="text/javascript" src="js/webcam.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>


    </head>

    <c:choose>
        <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/AlfaMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                    </c:when>
                    <c:when test = "${fn:contains(LoginUserBean.loginusername, 'paradmin')}">
                        <body style="margin-top:0px;background:#188B7A;">
                            <jsp:include page="../tab/ParMenu.jsp"/>  
                            <div id="wrapper"> 
                                <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                                </c:when>
                                <c:otherwise>
                                    <body>
                                        <div id="wrapper">
                                            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
                                            <div id="page-wrapper">
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="container-fluid" style="padding-top: 125px;padding-bottom: 125px;">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            <h2 style="color:  #0071c5;" align="center"> Annual Establishment Review</h2>
                                                            <h4 style="color:  #0071c5;" align="center"><strong> <u>SANCTIONED STRENGTH (Both Plan & Non-Plan)</u></strong></h4>
                                                        </div>
                                                    </div>

                                                    <form:form action="DeptWiseGroupAERSanCoWise.htm" method="POST" commandName="command">
                                                        <div class="row">
                                                            <div class="col-lg-12">
                                                                <h3 align="center"> Financial Year 
                                                                    <form:select path="financialYear" id="financialYear">
                                                                        <form:option value="2023-24"> </form:option>
                                                                        <form:option value="2022-23"> </form:option>
                                                                        <form:option value="2021-22"> </form:option>
                                                                        <form:option value="2020-21"> </form:option>
                                                                        <form:option value="2019-20"> </form:option>      
                                                                        <form:option value="2018-19"> </form:option>
                                                                    </form:select>

                                                                    <button type="submit" name="btnAer" value="Search" class="btn btn-primary">Show AER List</button>
                                                                    <button type="submit" name="btnAer" value="Download Excel" class="btn btn-primary">Download Excel</button>   
                                                                </h3> 
                                                            </div>
                                                        </div>  

                                                    </form:form>           


                                                </div>
                                                <div class="panel-body">
                                                    <div class="table-responsive">

                                                        <div class="table-responsive">
                                                            <table class="table table-bordered table-hover table-striped">
                                                                <thead>
                                                                    <tr style="background-color: #0071c5;color: #ffffff;">
                                                                        <th>Slno</th>
                                                                        <th>Demand No</th>
                                                                        <th>Department</th>                                                
                                                                        <th>Group A</th>
                                                                        <th>Group B</th>
                                                                        <th>Group C</th>
                                                                        <th>Group D</th>
                                                                        <th>Total(A+B+C+D)</th>
                                                                        <th>Grant in Aid</th>
                                                                        <th>Grant Total</th>

                                                                    </tr>
                                                                </thead>
                                                                <c:set var="GtotalEmp" value="${0}" />
                                                                <c:set var="grandTotalDDO" value="${0}" />
                                                                <c:set var="grandTotalSubmittedDDO" value="${0}" />
                                                                <c:set var="grandTotalA" value="${0}" />
                                                                <c:set var="grandTotalB" value="${0}" />

                                                                <c:set var="grandTotalC" value="${0}" />
                                                                <c:set var="grandTotalD" value="${0}" />
                                                                <c:set var="grandTotalAll" value="${0}" /> 
                                                                <c:set var="AllgrantinAid" value="${0}" /> 
                                                                <c:set var="AllgrandTotalAll" value="${0}" /> 
                                                                <c:forEach items="${AERDetails}" var="bgroup" varStatus="count">
                                                                    <c:set var="grandTotalDDO" value="${grandTotalDDO+bgroup.totalDDO}" />
                                                                    <c:set var="grandTotalSubmittedDDO" value="${grandTotalSubmittedDDO+bgroup.approverSubmitted}" />
                                                                    <c:set var="grandTotalA" value="${grandTotalA + bgroup.totalACnt}" />
                                                                    <c:set var="grandTotalB" value="${grandTotalB + bgroup.totalBCnt}" />

                                                                    <c:set var="grandTotalC" value="${grandTotalC + bgroup.totalCCnt}" />
                                                                    <c:set var="grandTotalD" value="${grandTotalD + bgroup.totalDCnt}" />
                                                                    <c:set var="grandTotalAll" value="${grandTotalAll + bgroup.totalallCnt}" />
                                                                    <c:set var="AllgrantinAid" value="${AllgrantinAid + bgroup.grantinAid}" />
                                                                    <c:set var="AllgrandTotalAll" value="${AllgrandTotalAll + bgroup.grandTotal}" />
                                                                    <tr>
                                                                        <td>${count.index + 1}</td>
                                                                        <td>${bgroup.demandNo}</td>
                                                                        <td><a href="viewCODeptWise.htm?deptCode=${bgroup.deptCode}&fy=${bgroup.financialYear}" target="_blank">${bgroup.departmentname}</a></td>
                                                                        <td>${bgroup.totalACnt}</td>
                                                                        <td>${bgroup.totalBCnt}</td>
                                                                        <td>${bgroup.totalCCnt}</td>
                                                                        <td>${bgroup.totalDCnt}</td>
                                                                        <td>${bgroup.totalallCnt}</td>
                                                                        <td>${bgroup.grantinAid}</td>
                                                                        <td>${bgroup.grandTotal}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                                <tr style="background-color: #0071c5;color: #ffffff;">
                                                                    <th>&nbsp;</th>
                                                                    <th>&nbsp;</th>
                                                                    <th>&nbsp;</th>                             
                                                                    <th>${grandTotalA}</th>
                                                                    <th>${grandTotalB}</th>
                                                                    <th>${grandTotalC}</th>
                                                                    <th>${grandTotalD}</th>
                                                                    <th>${grandTotalAll}</th>
                                                                    <th>${AllgrantinAid}</th>
                                                                    <th>${AllgrandTotalAll}</th>

                                                                </tr>      
                                                            </table>
                                                        </div>                
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                            </body>
                            </html>
