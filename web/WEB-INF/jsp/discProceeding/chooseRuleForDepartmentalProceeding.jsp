<%-- 
    Document   : chooseRuleForDepartmentalProceeding
    Created on : Sep 7, 2018, 11:40:55 AM
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
    <body>
        <form:form action="DiscProceding1.htm" commandName="pbean">
            <div class="container container-fluid" style="padding-top: 50px;">
                <div class="row">
                    <div class="col-lg-2">
                        <label for="txtDescOP">Choose Rule:</label>
                    </div>
                    <div class="col-7">
                        <form:select path="rule">
                            <form:option value="rule15" >Rule 15</form:option> 
                            <form:option value="rule16" >Rule 16</form:option> 
                            <form:option value="rule17" >Rule 17</form:option>
                            <form:option value="rule7/ocs(Pention Rule)" >Rule 7/ocs(Pention Rule)</form:option>
                            <form:option value="rule15 and  17" >Rule 15 and 17</form:option>
                            <form:option value="rule15 and 7" >Rule 15 and 7</form:option>
                            <form:option value="rule15,17 and 7" >Rule 15 and 17 and 7 </form:option>
                            <form:option value="rule16 and 17" >Rule 16 and 17</form:option>
                            <form:option value="rule16 and 7" >Rule 16 and 7</form:option>
                            <form:option value="rule16,17 and 7" >Rule 16 and 17 and 7</form:option>
                        </form:select>
                        <form:hidden path="mode"/>
                        <input type="submit" name="action" class="btn btn-default" value="Select" onclick="return confirm('Are you sure to Choose?')"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
