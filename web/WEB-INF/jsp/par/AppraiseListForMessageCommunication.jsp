<%-- 
    Document   : AppraiseListForMessageCommunication
    Created on : 18 May, 2022, 12:58:17 PM
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
        <div id="wrapper"> 
            <div id="page-wrapper" style="margin-top:80px;z-index:0;padding: 20px 19px;">
                <div class="row">
                    <div class="col-lg-12">                            
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                            </li>
                            <li class="active">
                                <i class="fa fa-file"></i> Message Communication 
                            </li>                                
                        </ol>
                    </div>
                </div>
                <h3 style="text-align:center"> Message Communication Detail For PAR</h3>
                <form:form action="appraiseeListForMessageCommunication.htm" method="post" commandName="parMessageCommunication">

                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th width="3%">#</th>
                                <th width="25%">Appraise Name</th>
                                <th width="25%">Appraise Spc</th>
                                <th width="25%">Mobile Number</th>
                            </tr>
                        </thead>
                        <tbody>                                        
                            <c:forEach items="${appraiseList}" var="appraiseDetail" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index + 1}</td>
                                    <td>${appraiseDetail.appraiseName}</td>
                                    <td>${appraiseDetail.appraisePost}</td>
                                    <td>${appraiseDetail.mobileNumber}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="panel-footer" id="add-new">
                        <form:hidden path="fiscalyear"/>
                        <input type="submit" name="action" value="Sent Message" class="btn btn-info" onclick="return confirm('Are you sure to Sent Message?');">
                        <input type="submit" name="action" value="Back">    
                    </div>
                </form:form>
            </div>
        </div>

    </body>
</html>
