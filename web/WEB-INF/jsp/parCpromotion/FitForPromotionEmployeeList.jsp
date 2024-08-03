<%-- 
    Document   : FitForPromotionEmployeeList
    Created on : Mar 19, 2020, 3:54:11 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>

    </head>
    <form:form action="parCPromotionReport.htm" method="POST" commandName="groupCEmployee" class="form-inline">
        <div class="row">
            <div class="col-lg-12">
                <h2>Fit For Shouldering Higher Responsibility Employee List</h2>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th width="1%"></th> 
                                <th>Sl No</th>
                                <th>Employee Name</th>
                                <th>Designation</th>
                            </tr>
                        </thead>
                        <tbody>                                        
                             <c:forEach items="${fitForPronotionempList}" var="fitfotpronotionemployee" varStatus="count">
                            <tr>
                                <td></td>
                                <td>${count.index + 1}</td>
                                <td>${fitfotpronotionemployee.reviewedempname}</td>
                                <td>${fitfotpronotionemployee.reviewedpost}</td>
                            </tr>
                             </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <input type="submit" name="action" class="btn btn-default" value="Back"/>                        
                </div>
            </div>
        </div>
    </form:form>
</html>
