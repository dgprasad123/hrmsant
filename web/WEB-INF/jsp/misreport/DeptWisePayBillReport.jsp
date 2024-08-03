<%-- 
    Document   : OfficeWiseEmployee
    Created on : Feb 9, 2017, 4:42:58 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>

        <div id="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Department Wise PayBill Report</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <form:form role="form" action="getDeptWisePayBillStatus.htm" commandName="deptVacantPostList"  method="post">
                                    <table class="table table-bordered table-hover table-striped">
                                        <tr>
                                            <td align="right">Month:</td>
                                            <td>
                                                <Select name="month"  class="form-control" required="1">
                                                    <option value="">Select Month</option>
                                                    <option value="0"  <c:if test = "${not empty selectmonth && selectmonth=='0'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                                    <option value="1"  <c:if test = "${not empty selectmonth && selectmonth=='1'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                                    <option value="2" <c:if test = "${not empty selectmonth && selectmonth=='2'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                                    <option value="3" <c:if test = "${not empty selectmonth && selectmonth=='3'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                                    <option value="4" <c:if test = "${not empty selectmonth && selectmonth=='4'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                                    <option value="5" <c:if test = "${not empty selectmonth && selectmonth=='5'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                                    <option value="6" <c:if test = "${not empty selectmonth && selectmonth=='6'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                                    <option value="7"  <c:if test = "${not empty selectmonth && selectmonth=='7'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                                    <option value="8" <c:if test = "${not empty selectmonth && selectmonth=='8'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                                    <option value="9" <c:if test = "${not empty selectmonth && selectmonth=='9'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                                    <option value="10" <c:if test = "${not empty selectmonth && selectmonth=='10'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                                    <option value="11" <c:if test = "${not empty selectmonth && selectmonth=='11'}"> <c:out value='selected="selected"'/></c:if>>December</option>
                                                    </select>
                                                </td>
                                                <td align="right">Year:</td>
                                                <td>
                                                    <Select name="year"  class="form-control" required="1">
                                                        <option value="">Select Year</option>
                                                        <option value="2018" <c:if test = "${not empty selectyear && selectyear=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                                    <option value="2019"  <c:if test = "${not empty selectyear && selectyear=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                                    <option value="2020"  <c:if test = "${not empty selectyear && selectyear=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                                    <option value="2021"  <c:if test = "${not empty selectyear && selectyear=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                                    <option value="2022"  <c:if test = "${not empty selectyear && selectyear=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                                    <option value="2023"  <c:if test = "${not empty selectyear && selectyear=='2023'}"> <c:out value='selected="selected"'/></c:if>>2023 </option>
                                                    <option value="2024"  <c:if test = "${not empty selectyear && selectyear=='2024'}"> <c:out value='selected="selected"'/></c:if>>2024 </option>
                                                    </select>   
                                                </td>
                                            </tr>                                         
                                            <tr>
                                                <td colspan="4" align="center"><input type="submit" class="btn btn-primary" value="Submit"  /></td>



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
                                            <th>DDO Prepared</th>
                                            <th>Bill Prepared</th>
                                            <th>DDO Submitted</th>
                                            <th>Bill Submitted</th>
                                            <th>Token Generated</th>
                                        </tr>
                                    </thead>
                                    <c:set var="GtotalDDO" value="${0}" />
                                    <c:set var="GddoPrepared" value="${0}" />
                                    <c:set var="GbillPrepared" value="${0}" />
                                    <c:set var="GddoSubmitted" value="${0}" />
                                    <c:set var="GbillSubmitted" value="${0}" />
                                    <c:set var="GtokenPrepared" value="${0}" />
                                    <c:forEach items="${billDetails}" var="bgroup" varStatus="count">
                                        <c:set var="GtotalDDO" value="${GtotalDDO + bgroup.totalDDO}" />
                                        <c:set var="GddoPrepared" value="${GddoPrepared + bgroup.ddoPrepared}" />
                                        <c:set var="GbillPrepared" value="${GbillPrepared + bgroup.billPrepared}" />
                                        <c:set var="GddoSubmitted" value="${GddoSubmitted + bgroup.ddoSubmitted}" />
                                        <c:set var="GbillSubmitted" value="${GbillSubmitted + bgroup.billSubmitted}" />
                                        <c:set var="GtokenPrepared" value="${GtokenPrepared + bgroup.tokenPrepared}" />
                                        <tr>
                                            <td>${count.index + 1}</td>
                                            <td><a href="DeptWiseOfficePayBill.htm?month=${selectmonth}&year=${selectyear}&dcode=${bgroup.deptCode}" target='blank'>${bgroup.departmentname}</a></td>
                                            <td>${bgroup.totalDDO}</td>
                                            <td>${bgroup.ddoPrepared}</td>
                                            <td>${bgroup.billPrepared}</td>
                                            <td>${bgroup.ddoSubmitted}</td>
                                            <td>${bgroup.billSubmitted}</td>
                                            <td>${bgroup.tokenPrepared}</td>
                                        </tr>
                                    </c:forEach>
                                    <tr style="background-color: #0071c5;color: #ffffff;">
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>${GtotalDDO}</th>
                                        <th>${GddoPrepared}</th>
                                        <th>${GbillPrepared}</th>
                                        <th>${GddoSubmitted}</th>
                                        <th>${GbillSubmitted}</th>
                                        <th>${GtokenPrepared}</th>
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
