<%-- 
    Document   : AnnualBudgetDDOAnnexureIIIC
    Created on : 10 Nov, 2020, 10:27:33 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>        
        <script type="text/javascript">
        $(document).ready(function () {
            
        });
    </script> 
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="AnnexureTabs.jsp">
            <jsp:param name="menuHighlight" value="ANNEXIIIC" />
        </jsp:include>
        
    </body>
    
</html>
