<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <h4>EMPLOYEE DEDUCTION REPORT</h4>     
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th  width="10%">SL NO</th>
                                <th  width="30%">Name</th>
                                <th  width="40%">Deduction Name</th>                              
                                <th  width="20%">Amount</th>

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${groupList}" var="group" varStatus="cnt">

                                <tr>

                                    <c:if test="${group.slno > 0}">
                                        <td >
                                            ${group.slno}
                                        </td >
                                        <td ><span style='color:green'>${group.empName}</span></td>

                                    </c:if>
                                    <c:if test="${group.slno == 0}">
                                        <td>
                                            &nbsp;
                                        </td>
                                        <td >
                                            &nbsp;
                                        </td>

                                    </c:if>    


                                    <td>${group.deductionType}</td>                                   
                                    <td>${group.fixValue}</td>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div align='center'><input type='button' name='button' value='Close'  class="btn btn-primary" onclick="Cancel_page()"></div>
                </div>

            </div>
        </div>
        <script>
            function Cancel_page() {
                window.location = "billGroupWiseDeductionList.htm";
            }
        </script>  
    </body>
</html>
