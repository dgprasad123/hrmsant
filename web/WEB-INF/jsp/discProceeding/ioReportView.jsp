<%-- 
    Document   : ioReportView
    Created on : Sep 24, 2018, 1:54:39 PM
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
        <jsp:include page="/FinalReport.htm">
            <jsp:param name="daId" value="${ioReportBean.daId}"/>
        </jsp:include>
        <form:form action="sendNotice.htm" method="post" commandName="ioReportBean">
            <div class="container-fluid">
                <div class="panel panel-default">                
                    <form:hidden path="daioid"/>
                    <form:hidden path="taskId"/>
                    <div class="panel panel-body">
                        <p>
                            Issue First Notice to Delinquent Officer.
                        </p>
                        <p>
                        <c:if test="${not empty dpdetails.notice1ondate}">
                            Notice-1 Send on ${dpdetails.notice1ondate}
                        </c:if>
                        </p>
                    </div>
                    <div class="panel panel-footer">
                        <c:if test="${empty dpdetails.notice1ondate}">
                            <input type="submit" name="action" value="Send Notice-1"/>
                        </c:if>
                            
                        <c:if test="${not empty dpdetails.notice1ondate}">
                            <input type="submit" name="action" value="Send Notice-2"/>
                        </c:if>
                         
                    </div>
                </div>

            </div>
        </form:form>
    </div>
</div>
</body>
</html>

