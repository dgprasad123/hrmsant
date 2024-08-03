<%-- 
    Document   : UserPrivilegemap
    Created on : Dec 19, 2018, 10:56:10 AM
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
        <script>
            function revokeprivilege(privmapid) {
                if (confirm('Are you sure to Revoke?')) {
                    window.location = "userprivilegemap.htm?action=Revoke&privmapid=" + privmapid+"&username="+$("#username").val();
                }
            }
        </script>
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
            <div class="panel panel-default">
                <div class="panel-heading" align="center"> <b>Assign Privilege For User</b><br></div>
                <div class="panel-body">   
                    <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                        <tr>
                            <td>                                                                                                                        
                                <div class="form-group">
                                    <label class="control-label col-sm-2" >User Name:</label>
                                    <div class="col-sm-4"> 
                                        <form:input class="form-control" path="username"/><br/>
                                    </div>
                                </div>
                                <input type="submit" name="action" value="Search" class="btn btn-default" />
                                <input type="submit" name="action" value="Assign Privilege" class="btn btn-default" />
                                <br><br>
                            </td>
                        </tr>
                    </table>
                    <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                        <c:forEach items="${Privileges}" var="privilege" varStatus="cnt">
                            <tr>
                                <td>  ${cnt.index + 1}.</td>
                                <td>  ${privilege.modname}</td>
                                <td> ${privilege.privmapid}<input type="button" name="action" value="Revoke" onclick="revokeprivilege('${privilege.privmapid}')" class="btn btn-default"/></td>
                            </tr>


                        </c:forEach>
                    </table>
                </div>  
            </div>

        </form:form>
    </body>
</html>
