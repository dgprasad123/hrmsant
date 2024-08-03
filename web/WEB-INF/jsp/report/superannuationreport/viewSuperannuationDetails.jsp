<%-- 
    Document   : viewSuperannuationDetails
    Created on : Sep 11, 2023, 12:31:26 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">

            $(function() {
                $('#txtperiodFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtperiodTo').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <h2 align="center">
                                <u>District Wise Pension Report </u>
                            </h2>
                            
                        </div>
                    </div>
                    <hr />
                    <form:form action="ViewSupperannuationReportDistWise.htm" commandName="command">


                        <div class="row" >
                            <div class="col-lg-2"  style="left:50px;">From Date:</div>

                            <div class="col-lg-2" >

                                <div style="left:50px;" class='input-group date' >

                                    <form:input class="form-control" path="txtperiodFrom" id="txtperiodFrom" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2" style="left:50px;" >To Date:</div>
                            <div class="col-lg-2" >
                                <div class='input-group date' >
                                    <form:input class="form-control" path="txtperiodTo" id="txtperiodTo" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2" >
                                <input type="submit" name="action" value="Search" class="btn btn-success"/>
                            </div>
                        </div> 
                    </form:form>  

                </div>
                <div class="panel-body">
                     <table class="table table-bordered" width="100%">
                         <thead>
                             <tr>
                                 <th width="10%" style="text-align: center;">Sl No</th>                                
                                 <th width="40%" >District Name</th>                               
                                 <th width="15%" style="text-align: center;">Total No of Employee</th>
                             </tr>
                         </thead>
                         <tbody>
                             <c:if test="${not empty emplist}">
                                 <c:set var="GtotalPost" value="${0}" />
                                 <c:forEach items="${emplist}" var="list" varStatus="count">
                                     <c:set var="GtotalPost" value="${GtotalPost+list.totalRecord}" />
                                     <tr>
                                         <td style="text-align: center;"><c:out value="${count.index + 1}"/></td>

                                        <td>
                                            <c:out value="${list.districtname}"/>
                                        </td>
                                        <td style="text-align: center;">
                                            <a href="ViewSupperannuationDetailsOfficeWise.htm?deptCode=${deptCode}&districtCode=${list.districtCode}&districtNm=${list.districtname}&fdate=${fromdate}&tdate=${todate}" target="_blank"><c:out value="${list.totalRecord}"/></a>
                                        </td>

                                    </tr>
                                </c:forEach>
                                <tr style='color:green;font-weight:bold'>
                                    <td>&nbsp;</td>

                                    <td>Total</td>
                                    <td style="text-align: center;">${GtotalPost}</td>
                                </tr>

                            </c:if>
                            <c:if test="${empty emplist}">
                              
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>
    </body>
</html>
