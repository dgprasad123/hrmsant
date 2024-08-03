<%-- 
    Document   : ioRemarkofInitingAuthority
    Created on : Sep 24, 2018, 4:15:32 PM
    Author     : manisha
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <form:form action="viewfinalIoReport.htm" method="post" commandName="ioReportBean">
                    <form:hidden path="ioRemarkId"/>
                    <form:hidden path="taskId"/>
                   
                    <div class="panel-body">

                        <div class="form-group">
                            <label class="control-label col-sm-2" >Remark</label>
                            <div class="col-sm-8"> 
                                <form:textarea class="form-control" path="ioRemark"/>

                            </div>
                        </div>
                    </div><br>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save remark" class="btn btn-default"/> 
                    </div>



                </form:form>
            </div>
        </div>
    </body>
</html>

