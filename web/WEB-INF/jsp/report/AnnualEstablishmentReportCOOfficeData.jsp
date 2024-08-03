<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <style type="text/css">
            .dropdown-submenu {
                position: relative;
            }

            .dropdown-submenu .dropdown-menu {
                top: 0;
                left: 100%;
                margin-top: -1px;
            }
            .dropdown-menu{
                min-width: 120px;
            }
        </style>
    </head>
    <body>
        <form:form action="displayAERlistForControllingAuthority.htm" commandName="command">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center"> ${OffName} </h1>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year 
                                    <form:select path="financialYear" id="financialYear">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <form:options items="${fiscyear}" itemLabel="fy" itemValue="fy"/>
                                        <%--<form:option value="2017-18"> </form:option>
                                        <form:option value="2018-19"> </form:option>--%>
                                    </form:select>
                                    <button type="submit" name="btnAer" value="GetCOData" class="btn btn-primary" onclick="return validate()">Show AER List</button>   
                                </h3> 
                            </div>
                        </div>    
                    </div><br />
                    <div style="font-weight:bold;font-size: 20px;text-align: center">
                        Please select Controlling Office to view AER List
                    </div>
                    <div class="panel-body" style="height:400px; overflow: auto;">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl No</th>
                                    <th>Controlling Office Name</th>
                                    <th>DDO Code</th>
                                    <th>Submitted On</th>
                                    <th>Status</th>
                                    <th align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${coofficelist}" var="offlist" varStatus="counter">
                                    <tr>
                                        <td>${counter.count}</td>
                                        <td><strong>${offlist.offName}</strong></td>
                                        <td>${offlist.ddoCode}</td>
                                        <td>${offlist.submittedDate}</td>
                                        <td>
                                            <c:if test="${not empty offlist.coaerid}">
                                                SUBMITTED
                                            </c:if>
                                            <c:if test="${empty offlist.coaerid}">
                                                PENDING
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:if test="${not empty offlist.coaerid}">
                                                <a class="btn btn-primary" href="downloadaerPDFReportForScheduleII.htm?aerId=${offlist.coaerid}&financialYear=${selectedFiscalYear}" target="_blank">
                                                    <span class="fa fa-file-pdf-o" style="color:red"> </span> Download Schedule-II
                                                </a>
                                            </c:if>
                                            <a class="btn btn-danger" href="displayAERlistForControllingAuthority.htm?btnAer=Search&coOffCode=${offlist.offCode}&financialYear=${selectedFiscalYear}">
                                                <span class="fa fa-edit" style="color:#F0F0EE;"> </span>View
                                            </a>
                                        </td>    
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer"></div>
                </div>
            </div>
        </form:form>
    </body>
</html>
