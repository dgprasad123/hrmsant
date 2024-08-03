<%-- 
    Document   : IncrementListContractual
    Created on : 8 May, 2020, 12:35:30 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>        
        <form:form action="editIncrementContractualData.htm" method="post" commandName="jForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">

                                <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;layout: fixed;color:#000000;font-weight:bold;">
                                    <thead> </thead>
                                    <tr>
                                        <td width="20%" align="right">
                                            Employee Name:                    
                                        </td>
                                        <td width="38%" style="text-transform:uppercase;" align="left">
                                            <b> ${SelectedEmpObj.fullName} </b>
                                        </td>
                                        <td width="16%" align="right">
                                            HRMS ID:                    
                                        </td>
                                        <td width="26%">
                                            ${SelectedEmpObj.empId} 
                                        </td>
                                    </tr>

                                    <tr>
                                        <td align="right">Current Post: </td>
                                        <td >
                                            &nbsp; ${SelectedEmpObj.postname} 
                                        </td>
                                        <td align="right">GPF/ PPAN No:</td>
                                        <td><b style="text-transform:uppercase;"> ${SelectedEmpObj.gpfno}     &nbsp;</b></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Current Cadre: </td>
                                        <td align="left"><b> ${SelectedEmpObj.cadrename}   &nbsp;</b></td>
                                        <td align="right">Current Status:</td>
                                        <td><b> ${SelectedEmpObj.depstatus}&nbsp;</b></td>
                                    </tr>
                                </table>

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    
                                    <th width="20%">Increment Order No</th>
                                    <th width="20">Increment Order Date</th>
                                    <th width="20%">Previous Consolidated Pay</th>
                                    <th width="20%">New Consolidated Pay</th>
                                    <th width="20" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${incrList}" var="tlist">
                                    <tr>
                                        <td>${tlist.ordno}</td>
                                        <td>${tlist.orddate}</td>
                                        <td>${tlist.curBasic}</td>
                                        <td>${tlist.newBasic}</td>
                                        <td><a href="editIncrementContractualData.htm?incrId=${tlist.incrId}">Edit</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empId" id="empId"/>
                        <button type="submit" class="btn btn-default">Add Increment</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>