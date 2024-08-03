<%-- 
    Document   : DeptWiseVoucherList
    Created on : Feb 15, 2020, 12:48:27 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js">            
        </script>
    </head>
    <body>

        <div id="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Department Wise Vouchered Status</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <form:form role="form" action="DeptWiseVoucherList.htm" commandName="BillStatus"  method="post">
                                    <table class="table table-bordered table-hover table-striped">
                                        <tr>
                                            <td align="left">
                                                <form:label path="billType">Bill Type:</form:label></td>
                                            <td><form:select path="billType" id="billType" class="form-control">
                                                    <form:option value="PAY">Pay Bill</form:option>
                                                    <form:option value="ARREAR">7th Pay Arrear</form:option>
                                                    <form:option value="ARREAR_6">7th Pay Arrear(60%)</form:option>
                                                    <form:option value="OTHER_ARREAR">OTHER Arrear</form:option>
                                                </form:select> 
                                            </td>
                                            <td align="right"> <form:label path="month">Month:</form:label></td>
                                                <td>
                                                <form:select class="form-control" id="month" path="month">
                                                    <form:option value="0">January</form:option>
                                                    <form:option value="1">February</form:option>
                                                    <form:option value="2">March</form:option>
                                                    <form:option value="3">April</form:option>
                                                    <form:option value="4">May</form:option>
                                                    <form:option value="5">June</form:option>
                                                    <form:option value="6">July</form:option>
                                                    <form:option value="7">August</form:option>
                                                    <form:option value="8">September</form:option>
                                                    <form:option value="9">October</form:option>
                                                    <form:option value="10">November</form:option>
                                                    <form:option value="11">December</form:option>                                                                    
                                                </form:select>
                                            </td>
                                            <td align="right"><form:label path="year">Year:</form:label></td>
                                                <td>
                                                <form:select class="form-control" path="year" id="year">
                                                    <form:option value="2015">2015</form:option>
                                                    <form:option value="2016">2016</form:option>
                                                    <form:option value="2017">2017</form:option>
                                                    <form:option value="2018">2018</form:option>
                                                    <form:option value="2019">2019</form:option>
                                                    <form:option value="2020">2020</form:option>
                                                    <form:option value="2021">2021</form:option>
                                                    <form:option value="2022">2022</form:option>
                                                    <form:option value="2023">2023</form:option>
                                                    <form:option value="2024">2024</form:option>
                                                </form:select>   
                                            </td>
                                        </tr>                                       
                                        <tr>
                                            <td colspan="4" align="center"><input type="submit" name="action" class="btn btn-primary" value="Submit" /></td>
                                        </tr>
                                    </table>
                                </form:form> 

                            </div>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                            <th>Sl No</th>
                                            <th>Department</th>
                                            <th>Total DDO</th>                                           
                                            <th>Voucher Generated</th>
                                        </tr>
                                    </thead>
                                    <c:set var="GtotalDDO" value="${0}" />                                    
                                    <c:set var="GvoucherPrepared" value="${0}" />
                                    <c:forEach items="${vchDetails}" var="bgroup" varStatus="count">
                                        <c:set var="GtotalDDO" value="${GtotalDDO + bgroup.totalDDO}" />
                                        <c:set var="GvoucherPrepared" value="${GvoucherPrepared + bgroup.voucherPrepared}" />
                                        <tr>
                                            <td>${count.index + 1}</td>
                                            <td>${bgroup.departmentname}</td>
                                            <td>${bgroup.totalDDO}</td>
                                            <td>${bgroup.voucherPrepared}</td>
                                        </tr>
                                    </c:forEach>
                                    <tr style="background-color: #0071c5;color: #ffffff;">
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>${GtotalDDO}</th>
                                        <th>${GvoucherPrepared}</th>
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

