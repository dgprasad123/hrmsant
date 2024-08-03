<%-- 
    Document   : PARStatusDetail
    Created on : 18 Apr, 2022, 11:17:42 AM
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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>       
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
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

            $(document).ready(function() {
                $('#parPeriodForAppraisee').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#parPeriodForReporting').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#parPeriodForReviewing').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#parPeriodForAccepting').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });

            function savePARStatusDetail() {
                if ($("#fiscalyear").val() == "") {
                    alert("Please select the Financial Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                if ($("#isClosedForAppraisee").val() == "") {
                    alert("Please select the Status For Appraisee");
                    $("#isClosedForAppraisee").focus();
                    return false;
                }
                if ($("#isClosedForAuthority").val() == "") {
                    alert("Please select the Status For Authority");
                    $("#isClosedForAuthority").focus();
                    return false;
                }
                if ($("#parPeriodForAppraisee").val() == "") {
                    alert("Please select the Last Date For Appraisee");
                    $("#parPeriodForAppraisee").focus();
                    return false;
                }
                if ($("#parPeriodForReporting").val() == "") {
                    alert("Please select the Last Date For Reporting Authority");
                    $("#parPeriodForReporting").focus();
                    return false;
                }
                if ($("#parPeriodForReviewing").val() == "") {
                    alert("Please select the Last Date For Reviewing Authority");
                    $("#parPeriodForReviewing").focus();
                    return false;
                }
                if ($("#parPeriodForAccepting").val() == "") {
                    alert("Please select the Last Date For Accepting Authority");
                    $("#parPeriodForAccepting").focus();
                    return false;
                }
                if ($("#uploadDocument").val() == "") {
                    alert("Please Upload Document For Changing PAR Status");
                    $("#uploadDocument").focus();
                    return false;
                }

            }



        </script>
    </head>



    <body style="margin-top:0px;background:#188B7A;">
        <jsp:include page="../tab/ParMenu.jsp"/> 
        <form:form action="parStatusManageByPARAdmin.htm" commandName="parStatusBean" method="post" class="form-horizontal" enctype="multipart/form-data">
            <div id="wrapper"> 
                <div id="page-wrapper" style="margin-top:80px;z-index:0;padding: 20px 19px;">

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="category">Financial Year</label>
                        </div>
                        <div class="col-lg-3">
                            <form:select path="fiscalyear" id="fiscalyear" class="form-control">
                                <option value="">Year</option>
                                <form:options items="${fiscyear}" itemValue="fy" itemLabel="fy"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="parstatus">Status Open/Close For Appraise</label>
                        </div>
                        <div class="col-lg-3">
                            <form:select path="isClosedForAppraisee" class="form-control">
                                <form:option value="">Select</form:option>
                                <form:option value="openforapp">Open</form:option> 
                                <form:option value="closeforapp">Close</form:option> 
                            </form:select> 
                        </div>
                        <div class="col-lg-2">
                            <label for="parstatus">Status Open/Close For Authority</label>
                        </div>
                        <div class="col-lg-3">
                            <form:select path="isClosedForAuthority" class="form-control">
                                <form:option value="">Select</form:option>
                                <form:option value="openforauth">Open</form:option> 
                                <form:option value="closeforauth">Close</form:option> 
                            </form:select> 
                        </div>
                    </div> 
                    <div class="row" style="margin-bottom: 7px;">

                        <div class="col-lg-2">
                            <label for="fullname">Extend PAR Period For Appraise</label>
                        </div>

                        <div class="col-lg-3">
                            <form:input path="parPeriodForAppraisee" id="parPeriodForAppraisee" class="form-control"/>  
                        </div> 
                        <div class="col-lg-2">
                            <label for="category">Extend PAR Period For Reporting Authority</label>
                        </div>
                        <div class="col-lg-3">
                            <form:input path="parPeriodForReporting" id="parPeriodForReporting" class="form-control"/>  
                        </div>

                    </div>
                    <div class="row" style="margin-bottom: 7px;">

                        <div class="col-lg-2">
                            <label for="fullname">Extend PAR Period For Reviewing Authority</label>
                        </div>

                        <div class="col-lg-3">
                            <form:input path="parPeriodForReviewing" id="parPeriodForReviewing" class="form-control"/>  
                        </div> 
                        <div class="col-lg-2">
                            <label for="category">Extend PAR Period For Accepting Authority</label>
                        </div>
                        <div class="col-lg-3">
                            <form:input path="parPeriodForAccepting" id="parPeriodForAccepting" class="form-control"/>  
                        </div>

                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="cadre Name">Upload Document</label>
                        </div>
                        <div class="col-lg-3">
                            <input type="file" name="parStatusdocument" id="uploadDocument"  class="form-control-file"/>
                        </div>
                    </div>

                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return savePARStatusDetail()"/>
                        <input type="submit" name="action" value="Get Change List" class="btn btn-default" onclick="return savePARStatusDetail()"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>


