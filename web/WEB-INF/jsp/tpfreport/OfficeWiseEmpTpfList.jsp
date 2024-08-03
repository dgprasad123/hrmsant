<%-- 
    Document   : EmployeeWiseTpfList
    Created on : Oct 29, 2018, 12:06:30 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="panel-heading" align="center" style="background-color: lightsteelblue ;color: #000;font-size: 20px;">Employee Teachers Provident Fund Details</div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <form:form action="EmployeeTpfInfo.htm" commandName="empTpfInfo" method="post">
                            <table class="table table-bordered table-hover table-striped">
                                <div class="container-fluid">
                                    <div class="row">
                                        <label class="control-label col-sm-3">Transaction Month</label>
                                        <div class="col-sm-3">
                                            <form:select class="form-control" path="month">
                                                <form:option value="000">Select</form:option>
                                                <form:option value="0">JANUARY</form:option>
                                                <form:option value="1">FEBRUARY</form:option>
                                                <form:option value="2">MARCH</form:option>
                                                <form:option value="3">APRIL</form:option>
                                                <form:option value="4">MAY</form:option>
                                                <form:option value="5">JUNE</form:option>
                                                <form:option value="6">JULY</form:option>
                                                <form:option value="7">AUGUST</form:option>
                                                <form:option value="8">SEPTEMBER</form:option>
                                                <form:option value="9">OCTOBER</form:option>
                                                <form:option value="10">NOVEMBER</form:option>
                                                <form:option value="11">DECEMBER</form:option>
                                            </form:select>
                                        </div>
                                        <label class="control-label col-sm-2">Transaction Year</label>
                                        <div class="col-sm-3">
                                            <form:select class="form-control" path="year">
                                                <form:option value="0">Select</form:option>
                                                <form:option value="2012">2012</form:option>
                                                <form:option value="2013">2013</form:option>
                                                <form:option value="2014">2014</form:option>
                                                <form:option value="2015">2015</form:option>
                                                <form:option value="2016">2016</form:option>
                                                <form:option value="2017">2017</form:option>
                                                <form:option value="2018">2018</form:option>
                                                <form:option value="2019">2019</form:option>
                                                <form:option value="2020">2020</form:option>
                                            </form:select>
                                        </div>
                                    </div>
                                    &nbsp;&nbsp;
                                    <div class="row">
                                        <label class="control-label col-sm-3">DDO CODE </label>

                                        <div class="col-sm-3">
                                            <form:input path="ddoCode" class="form-control"/>
                                        </div>
                                        <div class="col-sm-2">
                                            <input type="submit" value="Search"/>
                                        </div>
                                    </div>&nbsp;&nbsp;
                                    <div class="row">
                                        <label class="control-label col-sm-3">OFFICE NAME</label>
                                        <div  style="font-size:17px; color: #f94877; font-style: bold;" >
                                            ${offNm}  
                                        </div>
                                    </div>
                            </table>
                            <div class="panel-footer">
                                <div class="table-responsive">
                                    <div class="table-responsive">
                                        <table class="table table-striped table-bordered table-success" >
                                            <thead>
                                                <tr>
                                                    <th>SL NO</th>
                                                    <th>ACCOUNT NO</th>
                                                    <th>NAME OF THE SUBSCRIBER</th>
                                                    <th>DESIGNATION</th>
                                                    <th>MONTHLY SUBSCRIPTION</th>
                                                    <th id="refundAmt">REFUND OF WITHDRAWALS AMT</br>
                                                        (NO OF INST.)</th>
                                                    <th>TOTAL RELEASED</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${tpfList}" var="tpflists" varStatus="count">
                                                    <tr>
                                                        <td>${count.index + 1}</td>                                                    
                                                        <td>${tpflists.gpfNo}</td>
                                                        <td>${tpflists.empname}</td>
                                                        <td>${tpflists.curDesg}</td>
                                                        <td>${tpflists.monthlySubAmt}</td>
                                                        <%--<c:if test="${not empty refundAmt}">
                                                            <td>${tpflists.towardsLoan}   (${tpflists.instCnt})</td>
                                                        </c:if>
                                                            <c:if test="${empty refundAmt}">
                                                            <td>${tpflists.towardsLoan}</td>
                                                        </c:if>--%>
                                                        <td>${tpflists.towardsLoan}   (${tpflists.instCnt})</td>
                                                        <td>${tpflists.totalReleased}</td>
                                                    </tr>
                                                </c:forEach>

                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </form:form>
                    </div>                
                </div> 
            </div>
    </body>
</html>


