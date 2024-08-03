<%-- 
    Document   : AddIncrementContractual
    Created on : 8 May, 2020, 12:36:19 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            

            
            
            

            function onlyIntegerRange(e) {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }

            function saveCheck() {
                var result = confirm("Are you sure to add Increment for employee ?");

                if (result) {
                    if ($('#txtNotOrdNo').val() == "") {
                        alert("Please enter Order No");
                        $('#txtNotOrdNo').focus();
                        return false;
                    }
                    if ($('#txtNotOrdDt').val() == "") {
                        alert("Please enter Order Date");
                        return false;
                    }
                    if ($('#curBasic').val() == "") {
                        alert("Please enter Current Consolidated Pay.");
                        return false;
                    }
                    
                    if ($('#newBasic').val() == "") {
                        alert("Please enter New Consolidated Pay.");
                        return false;
                    }
                } else {
                    return false;
                }

                $('#btnSave').hide();
                return true;
            }
        </script>
    </head>
    <body>
        <form:form action="saveEmployeeIncrementContractual.htm" method="post" commandName="jForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Transfer
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empId" id="empid"/>
                        <form:hidden path="incrId" id="incrId"/>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="ordno" id="txtNotOrdNo"/>
                            </div>
                            <div class="col-lg-3">
                                <label for="orddate"> Notification Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="orddate" id="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="curBasic">Current Consolidated Pay<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="curBasic" id="curBasic" onkeypress="onlyIntegerRange()"/>
                            </div>
                            <div class="col-lg-3">
                                <label for="newBasic"> New Consolidated Pay <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="newBasic" id="newBasic" onkeypress="onlyIntegerRange()"/>                              
                            </div>
                        </div>
                        
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnTransferCntr" value="Save" class="btn btn-default" id="btnSave" onclick="return saveCheck();"/>
                        <a href="incrementContractualList.htm">
                        <input type="button" name="btnTransferCntr" value="Back" class="btn btn-default"/>
                        </a>
                    </div>
                </div>
            </div>

            
        </form:form>
    </body>
</html>
