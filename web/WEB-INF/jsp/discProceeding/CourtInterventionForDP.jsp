<%-- 
    Document   : CourtInterventionForDP
    Created on : 12 Feb, 2021, 11:41:46 AM
    Author     : Manisha
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
        <script type="text/javascript">
            $(document).ready(function() {
                $(".loader").hide();
                $.post("GetFiscalYearListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
            });

            function validation() {
                if ($("#caseNo").val() == "") {
                    alert("please Enter the Case Number");
                    $("#caseNo").focus();
                    return false;
                }
                if ($("#caseName").val() == "") {
                    alert("please Enter the Court Name");
                    $("#caseName").focus();
                    return false;
                }
                if ($("#courtDocumentForDP").val() == "") {
                    alert("please upload document");
                    $("#courtDocumentForDP").focus();
                    return false;
                }



            }
        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <form:form action="courtCaseForDP.htm" commandName="courtCaseBean" method="post" class="form-horizontal" enctype="multipart/form-data">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body">  
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="3%">#</th>
                                        <th width="25%">Case Number</th>
                                        <th width="25%">Case Year</th>
                                        <th width="25%">Court Name</th>
                                        <th width="15%">Document(Court Intervention)</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${courtcaseList}" var="caselist" varStatus="cnt">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>${caselist.caseNo}</td>
                                            <td>${caselist.fiscalyear}</td>
                                            <td>${caselist.caseName}</td>
                                            <td>
                                                <c:if test="${not empty caselist.courtDocumentorgFileName}">
                                                        <a href="downloadcourtCaseForDP.htm?caseNo=${caselist.caseNo}">
                                                            <span class="glyphicon glyphicon-paperclip"></span> ${caselist.courtDocumentorgFileName}</a>
                                                    </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="form-group">
                                <label class="control-label col-sm-2" >Case Number<span style="color: red">*</span> </label> 
                                <div class="col-sm-4"> 
                                    <form:hidden path="daId"/>
                                    <form:hidden path="caseNo"/>
                                    <form:hidden path="caseId"/>
                                    <form:input class="form-control" path="caseNo"/><br/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" >Case Year:</label> 
                                <div class="col-lg-4">
                                    <select name="fiscalyear" id="fiscalyear" class="form-control">
                                        <option value="">Year</option>
                                    </select>                            
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2" >Court Name<span style="color: red">*</span> </label>
                                <div class="col-sm-4">
                                    <form:input class="form-control" path="caseName"/><br/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2">Document(Court Intervention)</label>
                                <div class="col-sm-4"> 
                                    <input type="file" name="courtDocumentForDP" id="courtDocumentForDP"  class="form-control-file"/>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return savedocumentsforarticlecharge()"/>
                            <input type="submit" name="action" value="Back" class="btn btn-default"/>
                        </div>

                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>