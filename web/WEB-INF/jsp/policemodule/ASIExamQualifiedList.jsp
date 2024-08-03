<%-- 
    Document   : ASIExamQualifiedList
    Created on : 8 Dec, 2020, 5:35:16 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
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
        <script type="text/javascript">
            $(document).ready(function () {

            })
            function validate() {

            }

        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">



                <div class="panel panel-default">
                    <h3 style="text-align:center"> List of ASI Exam Qualified Candidates</h3>

                    <div class="panel-heading">


                    </div>
                    <div class="panel-body">

                        <c:if test="${qualifiedList.size() gt 0}">

                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Hall Ticket No</th>
                                                <th>Employee Name</th>
                                                <th>DOB</th>
                                                <th>Category</th>
                                                <th>Father's Name</th>
                                                <th style="text-align: center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${qualifiedList}" var="emp" varStatus="counter">
                                                <tr>
                                                    <td width="5%" >${counter.count}</td>
                                                    <td width="15%">${emp.admitCardRollNo}</td>
                                                    <td width="25%">${emp.fullname}</td>
                                                    <td width="10%">
                                                        ${emp.dob} 
                                                    </td>
                                                    <td width="10%">${emp.category}</td>
                                                    <td width="25%">${emp.fathersname}</td>

                                                    <td width="10%" style="text-align: center">
                                                        <a href="ASINominationProceddingDataForm.htm?nominationMasterId=${emp.nominationMasterId}&nominationDetailId=${emp.nominationDetailId}"><i class="fa fa-pencil-square-o  fa-2x"></i></a>
                                                    </td>

                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="panel-footer">


                    </div>
                </div>


            </div>
        </div>
    </body>
</html>
