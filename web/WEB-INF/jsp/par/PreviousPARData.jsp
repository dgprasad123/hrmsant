<%-- 
    Document   : PreviousPARData
    Created on : 27 Jul, 2022, 5:38:37 PM
    Author     : Manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="form-group">
            <table class="table table-bordered table-hover table-striped">
                <thead>
                    <tr>                        
                        <th width="5%">Financial Year</th>
                        <th width="95%">Previous Year Data</th>                        
                    </tr>
                </thead>
                <tbody>                                        
                <c:forEach items="${otherDetailsList}" var="parOtherDetails">
                    <tr>                        
                        <td>${parOtherDetails.key}</td>
                        <td>${parOtherDetails.value}</td>                        
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
