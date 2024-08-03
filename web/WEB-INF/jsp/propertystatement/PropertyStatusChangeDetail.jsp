<%-- 
    Document   : PropertyStatusChangeDetail
    Created on : 2 Jul, 2022, 11:18:49 AM
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
                $('#propertyPeriodExtendedFor').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $.post("GetFiscalYearListCYWiseJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
            });

            function savePropertyStatusDetail() {
                if ($("#fiscalyear").val() == "") {
                    alert("Please select the Financial Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                if ($("#isClosedProperty").val() == "") {
                    alert("Please select the Status For Property");
                    $("#isClosedProperty").focus();
                    return false;
                }
                if ($("#propertyPeriodExtendedFor").val() == "") {
                    alert("Please select the date For Property");
                    $("#propertyPeriodExtendedFor").focus();
                    return false;
                }
                if ($("#uploadDocument").val() == "") {
                    alert("Please upload Document For Property");
                    $("#uploadDocument").focus();
                    return false;
                }

            }



        </script>
    </head>



    <body style="margin-top:0px;background:#188B7A;">
        <jsp:include page="../tab/ParMenu.jsp"/> 
        <form:form action="propertyStatementStatusManageByPARAdmin.htm" commandName="propertyStatementStatusBean" method="post" class="form-horizontal" enctype="multipart/form-data">
            <div id="wrapper"> 
                <div id="page-wrapper" style="margin-top:80px;z-index:0;padding: 20px 19px;">
                    <h3 style="text-align:center"><b> Property Statement Status Report</b></h3>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="category">Financial Year</label><span style="color: red">*</span>
                        </div>
                        <div class="col-lg-3">
                            <select name="fiscalyear" id="fiscalyear" class="form-control">
                                <option value="">Year</option>
                            </select> 
                        </div>
                    </div>
                    
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="fullname">Extend Property Period For</label><span style="color: red">*</span>
                        </div>

                        <div class="col-lg-3">
                            <form:input path="propertyPeriodExtendedFor" id="propertyPeriodExtendedFor" class="form-control"/>  
                        </div> 
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="document">Upload Document</label><span style="color: red">*</span>
                        </div>
                        <div class="col-lg-3">
                            <input type="file" name="propertyStatusdocument" id="uploadDocument"  class="form-control-file"/>
                        </div>
                    </div>

                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return savePropertyStatusDetail()"/>
                        <input type="submit" name="action" value="Get Change List" class="btn btn-default"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>


