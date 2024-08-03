<%-- 
    Document   : MessageSentDetail
    Created on : 11 May, 2022, 12:02:19 PM
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
        <script type="text/javascript">
          
            function saveCheck() {
                if ($('#fiscalyear').val() == "") {
                    alert("Please select Financial Year");
                    $('#fiscalyear').focus();
                    return false;
                }
            }
        </script>
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
                <form:form action="sendMessageForPARList.htm" method="post" commandName="parMessageCommunication">
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-4">
                            <label for="lastdate">Fiscal Year</label>
                        </div>
                        <div class="col-lg-2">
                            <form:select path="fiscalyear"  class="form-control">
                                <form:option value="" label="Select" cssStyle="width:30%"/>
                                <c:forEach items="${fiscyear}" var="fiscyear">
                                    <form:option value="${fiscyear.fy}" label="${fiscyear.fy}"/>
                                </c:forEach>                                 
                            </form:select>


                        </div>
                        <div class="col-lg-2">
                            <input type="submit" name="action" value="Search">                           
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">                        
                        <div class="col-lg-4">
                            <label for="lastdate">Last Date Of Submission PAR For Appraise:</label>
                        </div>
                        <div class="col-lg-6">
                            <c:forEach items="${parStatusForMessageCommunication}" var="parStatus">
                                ${parStatus.parPeriodForAppraisee}
                            </c:forEach>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-4">
                            <label for="lastdate">Last Date Of Submission PAR For Reporting Authority:</label>
                        </div>
                        <div class="col-lg-6">
                            <c:forEach items="${parStatusForMessageCommunication}" var="parStatus">
                                ${parStatus.parPeriodForReporting}
                            </c:forEach>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-4">
                            <label for="lastdate">Last Date Of Submission PAR For Reviewing Authority:</label>
                        </div>
                        <div class="col-lg-6">
                            <c:forEach items="${parStatusForMessageCommunication}" var="parStatus">
                                ${parStatus.parPeriodForReviewing}
                            </c:forEach>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-4">
                            <label for="lastdate">Last Date Of Submission PAR For Accepting Authority:</label>
                        </div>
                        <div class="col-lg-6">
                            <c:forEach items="${parStatusForMessageCommunication}" var="parStatus">
                                ${parStatus.parPeriodForAccepting}
                            </c:forEach>
                        </div>
                    </div>

                    <div class="panel-footer" id="add-new">
                        <a href="appraiseeListForMessageCommunication.htm?fiscalyear=${parMessageCommunication.fiscalyear}"><button type="button" class="btn btn-info" onclick="return saveCheck()">Get Appraise List For Communication</button></a>
                        <a href=""><button type="button" class="btn btn-info">Get Authority List For Communication</button></a> 
                        <input type="submit" name="action" value="Run Thread For Message Communication" class="btn btn-info" onclick="return confirm('Are you sure to Run the Thread?');">
                      <%--  <a href="runThreadForMessageCommunication.htm"><button type="button" class="btn btn-info" onclick="return saveCheck()">Run Thread For Message Communication</button></a>--%>
                    </div>
                </form:form>
            </div>
        </div>

    </body>
</html>


