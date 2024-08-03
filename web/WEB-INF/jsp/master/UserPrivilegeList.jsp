<%-- 
    Document   : UserPrivilegeList
    Created on : Dec 21, 2018, 11:21:00 AM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>

        <style type="text/css">

            .headr{
                font-weight: bold;
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 16px;
            }
        </style>


    </head>
    <body style="padding:0px;">

        <form:form action="userprivilegemap.htm" method="POST" commandName="module" class="form-inline">
            <form:hidden path="username"/>
            <div class="panel panel-default">
                <div class="panel-heading" align="center"> <b>Assign Privilege For User</b><br></div>
                <div class="panel-body">   
                    <table class="table table-bordered">
                        <thead>
                            <tr> 
                                <th width="5%">Select</th>
                                <th width="20%">Mod Name</th>
                            </tr>                            
                        </thead>
                        <tbody>
                            <c:forEach items="${Privileges}" var="modname">
                                <tr> 
                                    <td><input type="checkbox" name="modname" value="${modname.modid}" ></td>
                                    <td>${modname.modname}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>  
                
                 <div class="panel-footer">
                    
                     <input type="submit" name="action" class="btn btn-default" value="Assign"/>
                     <input type="submit" name="action" class="btn btn-default" value="Back"/>
                   </div>
               </div>

        </form:form>
    </body>
</html>
