<%-- 
    Document   : ChooseRecommendationType
    Created on : 16 Oct, 2020, 4:56:50 PM
    Author     : Manisha
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




    <body>
        <form:form action="recommendationDetailReport.htm">
            <div class="container container-fluid" style="padding-top: 50px;">
                <div class="row">
                    <div class="col-1">&nbsp</div>
                    <div class="col-5">
                        <input type="submit" name="recommendation" class="btn btn-default" value="a) Premature retirement" onclick="return confirm('Are you sure to Choose?')"/>
                    </div>
                    <div class="col-1">&nbsp</div>
                </div>
                <div class="row">
                    <div class="col-1">&nbsp</div>
                    <div class="col-5">
                        <input type="submit" name="recommendation" class="btn btn-default" value="b) Out of turn promotion(within and across the batches)" onclick="return confirm('Are you sure to Choose?')"/>
                    </div>
                   <div class="col-1">&nbsp</div>
                </div>
                <div class="row">
                     <div class="col-1">&nbsp</div>
                    <div class="col-5">
                        <input type="submit" name="recommendation" class="btn btn-default" value="c) Award of incentives" onclick="return confirm('Are you sure to Choose?')"/>
                    </div>
                    <div class="col-1">&nbsp</div>
                   
                </div>
            </div>
        </form:form>
    </body>
</html>
