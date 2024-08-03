<%-- 
    Document   : DeptWiseNoOfEmployeesReport
    Created on : Jul 25, 2022, 1:26:39 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <script type="text/javascript" src="js/webcam.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
    </head>
    <jsp:include page="../tab/hrmsadminmenu.jsp"/>   
    <div id="wrapper">
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Department Wise No Of Salaried Employees</div>
                    <div class="panel-body">
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                            <th>Sl No</th>
                                            <th>Department Name</th>                                                
                                            <th>Regular</th>
                                            <th>Contractual 6Yrs.</th>
                                            <th>Contractual</th>
                                            <th>Wages</th>
                                            <th>Work-Charged</th>
                                            <th>Level-V(Ex-Cadre)</th>
                                        <tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${deptwiseEmplist}" var="dlist" varStatus="cntSlNo">
                                            <tr>
                                                <td>${cntSlNo.index+1}</td>
                                                <td><c:out value="${dlist.deptName}"/></td>
                                                <td><c:out value="${dlist.cntRegular}"/></td>
                                                <td><c:out value="${dlist.cntContractual}"/></td>
                                                <td><c:out value="${dlist.cntCont6Yr}"/></td>
                                                <td><c:out value="${dlist.cntWages}"/></td>
                                                <td><c:out value="${dlist.cntWorkcharged}"/></td>
                                                <td><c:out value="${dlist.cntlvlVExcadre}"/></td>
                                            </tr>
                                        </c:forEach> 
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
</html>
