<%-- 
    Document   : AerAuthorization
    Created on : Jan 22, 2019, 1:34:13 PM
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
        <script type="text/javascript">

            function validation() {
                if ($("#financialyear").val() == "") {
                    alert("please Select the Year");
                    $("#financialyear").focus();
                    return false;
                }
            }
        </script>

        <style>
            th {
                background-color: #286090;
                color: white;
            } 
        </style>
    </head>
    <body style="padding:0px;">
        <form:form action="aerauthorizationlist.htm" method="POST" commandName="aerAuthorizationBean" class="form-inline">            
            <div class="panel panel-default">
                <div class="panel-heading" align="center"> 
                    <center><h2><b>Process Authorization List </b></h2></center>
                </div>
                <div class="panel-body">  
                    <%--<table class="table table-bordered table-hover table-striped">
                        <tr>
                            <td style="width: 30%;align:center;"><b>Financial Year  </b> </td>
                            <td style="width: 30%">
                                <form:hidden path="authoritytype"/>
                                <form:select path="financialyear"  class="form-control">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <form:options items="${fiscyear}" itemLabel="fy" itemValue="fy"/>                                                                    
                                </form:select>
                            </td>
                            <td><input type="submit" value="Search" name="action" class="btn btn-primary"/></td>
                        </tr>
                    </table>--%>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover">
                            <thead>
                                <tr> 
                                    <th width="3%">Sl No</th>
                                    <th width="10%">Process Name</th>
                                    <th width="20%">Name</th>
                                    <th width="35%">Post</th>
                                    <th width="15%">Role</th>
                                </tr>                            
                            </thead>
                            <tbody>
                                <c:forEach items="${aerauthorizationList}" var="aer" varStatus="cnt">
                                    <tr> 
                                        <td>${cnt.index+1}</td>
                                        <td>${aer.processName}</td>
                                        <td>${aer.empname}</td>
                                        <td>${aer.spn}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${aer.roletype=='DREVIEWER'}">REVIEWER <span style="font-style: italic;font-size: 12px;color: #943818;">(ADMINISTRATIVE OFFICE)</span></c:when>
                                                <c:when test="${aer.roletype=='DVERIFIER'}">VERIFIER <span style="font-style: italic;font-size: 12px;color: #943818;">(ADMINISTRATIVE OFFICE)</span></c:when>
                                                <c:otherwise>${aer.roletype}</c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="panel-footer">
                    <c:if test="${not empty aerauthorizationList}">
                        <form:hidden path="siNo"/>
                        <input type="submit" name="action" value="Edit" class="btn btn-primary"/> 
                    </c:if>   
                    <c:if test="${empty aerauthorizationList}">
                        <%--<c:if test="${param.action eq 'Search'}" >--%>
                            <input type="submit" name="action" value="Add New"  class="btn btn-primary" onclick="return validation()"/> 
                        <%--</c:if>--%>
                    </c:if>                    
                </div>
            </div>
        </form:form>
    </body>
</html>
