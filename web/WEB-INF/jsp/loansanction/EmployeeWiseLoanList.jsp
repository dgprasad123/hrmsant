<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                            <h3>Loan Details </h3>    
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                               <th width="5%">Sl No</th>
                                <th >Loan Name</th>
                                <th >Amount</th>
                                <th>Is Recovered</th>

                            </tr>
                        </thead>
                        <tbody>
                           <c:forEach items="${loanSancList}" var="loanSanc" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${loanSanc.sltloan}</td>
                                    <td>${loanSanc.txtamount}</td>
                                    <td>
                                        <c:if test="${loanSanc.completedRecovery == 1}">
                                            <span style="color: #ff0000;">Recovered</span>
                                        </c:if>
                                        <c:if test="${loanSanc.completedRecovery == 0}">
                                            <span style="color: #00cccc;">Continuing</span>
                                        </c:if>
                                    </td>
                                  
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </body>
</html>
