<%-- 
    Document   : ForceForwardDetailList
    Created on : 16 Apr, 2022, 4:29:16 PM
    Author     : Manisha
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>

    </head>



    <body style="margin-top:0px;background:#188B7A;">
        <jsp:include page="../tab/ParMenu.jsp"/> 
        <form:form action="parForceForwardDetail.htm" commandName="parForceForwardBean" method="post" class="form-horizontal">
            <div id="wrapper"> 
                <div id="page-wrapper" style="margin-top:80px;z-index:0;padding: 20px 19px;">
                    <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Force Forward All</div>
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th width="3%">#</th>
                                <th width="8%"><center>Financial Year</center></th>
                        <th width="10%"><center>From Authority</center></th>
                        <th width="10%"><center>To Authority</center></th>
                        <th width="10%"><center>Force Forward On</center></th>
                        <th width="10%"><center>Force Forward Type</center></th>
                        <th width="20%"><center>Action</center></th>
                        </tr>
                        </thead>
                        <tbody>                                        
                            <c:forEach items="${forceForwardDetailOfALLCadre}" var="forceForwardALL" varStatus="cnt">
                                <tr>
                                    <td><center>${cnt.index + 1}</center></td>
                            <td><center>${forceForwardALL.fiscalyear}<center></td>
                                    <td><center>${forceForwardALL.fromAuthority}</center></td>
                                    <td><center>${forceForwardALL.toAuthority}</center></td>
                                    <td><center>${forceForwardALL.forceforwardOn}</center></td>
                                    <td><center>${forceForwardALL.forceForwardType}</center></td>
                                    <td><center><a href="">Force Forward</a></center></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                                </table>
                                <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Force Forward Cadre Wise</div>
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="3%">#</th>
                                            <th width="8%"><center>Financial Year</center></th>
                                    <th width="20%"><center>Department Name</center></th>
                                    <th width="18%"><center>Cadre Name</center></th>
                                    <th width="10%"><center>From Authority</center></th>
                                    <th width="10%"><center>To Authority</center></th>
                                    <th width="10%"><center>Force Forward On</center></th>
                                    <th width="10%"><center>Force Forward Type</center></th>
                                    <th width="20%"><center>Action</center></th>
                                    </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${forceForwardDetail}" var="forceForward" varStatus="cnt">
                                            <tr>
                                                <td><center>${cnt.index + 1}</center></td>
                                        <td><center>${forceForward.fiscalyear}<center></td>
                                                <td><center>${forceForward.deptName}</center></td>
                                                <td><center>${forceForward.cadreName}</center></td>
                                                <td><center>${forceForward.fromAuthority}</center></td>
                                                <td><center>${forceForward.toAuthority}</center></td>
                                                <td><center>${forceForward.forceforwardOn}</center></td>
                                                <td><center>${forceForward.forceForwardType}</center></td>
                                                <td><center><a href="">Force Forward</a></center></td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                            </table>
                                            <div class="panel-footer">
                                                <input type="submit" name="action" value="Back" class="btn btn-default"/>
                                            </div>
                                            </div>
                                            </div>
                                        </form:form>
                                        </body>
                                        </html>


