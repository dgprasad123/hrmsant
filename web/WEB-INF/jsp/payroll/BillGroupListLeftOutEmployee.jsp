<%-- 
    Document   : BillGroupListLeftOutEmployee
    Created on : 5 Sep, 2018, 4:37:14 PM
    Author     : Surendra
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
        <script type='text/javascript'>

        </script>
    </head>
    <body>
        
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                Copy Chart of Account        
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th> Select One </th>
                                    <th  width="40%">DESCRIPTION</th>
                                    <th  width="50%">CHART OF ACCOUNT</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${groupList}" var="group" varStatus="cnt">
                                    <tr>
                                        <td> <input type="radio" name="billgroupid" id="billgroupid${cnt.index+1}" value="${group.billgroupid}"/> </td>
                                        <td>${group.billgroupdesc}</td>
                                        <td>
                                            ${group.chartofaccount}
                                            <input type="hidden" name="hidchartofAcc" id="hidchartofAcc${cnt.index+1}" value="${group.chartofaccount}"/>
                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">                    
                        Copy Chart of Account          
                    </div>
                </div>
            </div>
        
    </body>
</html>
