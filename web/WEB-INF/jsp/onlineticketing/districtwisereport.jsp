<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Ticket System</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="GetDCMISReport.htm" commandName="onlineticketing">
                    <div class="container-fluid">
                        <div class="panel panel-primary">
                            <div class="panel-heading">District-Wise Online Ticket Status</div>
                            <div class="panel-body" style="margin-top:20px;">
                                <div class="row">
                                    <div class="col-lg-2">
                                        Select Month
                                    </div>
                                    <div class="col-lg-4">
                                        <form:select path="intMonth" class="form-control">
                                            <form:option value="1">JANUARY</form:option>
                                            <form:option value="2">FEBRUARY</form:option>
                                            <form:option value="3">MARCH</form:option>
                                            <form:option value="4">APRIL</form:option>
                                            <form:option value="5">MAY</form:option>
                                            <form:option value="6">JUNE</form:option>
                                            <form:option value="7">JULY</form:option>
                                            <form:option value="8">AUGUST</form:option>
                                            <form:option value="9">SEPTEMBER</form:option>
                                            <form:option value="10">OCTOBER</form:option>
                                            <form:option value="11">NOVEMBER</form:option>
                                            <form:option value="12">DECEMBER</form:option>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-2">
                                        Select Year
                                    </div>
                                    <div class="col-lg-2">
                                        <form:select path="intYear" class="form-control">

                                            <form:option value="2020"> 2020 </form:option>
                                            <form:option value="2021"> 2021 </form:option>
                                            <form:option value="2022"> 2022 </form:option>
                                            <form:option value="2023"> 2023 </form:option>
                                             <form:option value="2024"> 2024 </form:option>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-2">
                                        <input type="submit" value="Get Report" class="btn btn-danger"/>
                                    </div>
                                </div>
                                <h2 align="center">District-Wise Online Ticket Status</h2>
                                <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0" style='margin-top:10px'>
                                    <tr>
                                        <th >Slno</th>
                                        <th >Support Id</th>
                                        <th >District Name</th>
                                        <th >Received in the Current Month</th>
                                        <th >Resolved During the month</th>
                                        <th >Sent to State Team During the month</th>
                                    </tr>
                                    <c:set var="totalticket" value="${0}" />
                                    <c:set var="resolvedTicket" value="${0}" />
                                    <c:set var="stateTicket" value="${0}" />



                                    <c:forEach items="${onlineticketlist}" var="list" varStatus="count">
                                        <c:set var="totalticket" value="${totalticket + list.totalticketReceived}" />
                                        <c:set var="resolvedTicket" value="${resolvedTicket + list.totalticketdisposed}" />
                                        <c:set var="stateTicket" value="${stateTicket + list.totalticketsent}" />
                                        <tr>
                                            <td>
                                                ${count.index + 1}
                                            </td>
                                            <td>
                                                ${list.username}
                                            </td>
                                            <td>
                                                ${list.distCode}
                                            </td>
                                            <td>
                                                ${list.totalticketReceived}
                                            </td>
                                            <td>
                                                ${list.totalticketdisposed}
                                            </td>
                                            <td>
                                                ${list.totalticketsent}                                       

                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <tr style="background-color: #0071c5;color: #ffffff;">
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>${totalticket}</th>
                                        <th>${resolvedTicket}</th>
                                        <th>${stateTicket}</th>

                                    </tr>      

                                </table>
                            </div>
                            <div class="panel-footer"></div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
