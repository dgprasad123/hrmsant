
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Training</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function populateNoOfDays() {
                var x = $('#complDate').val().split("-");
                var y = $('#startDate').val().split("-");
                var month1 = convertMonthNameToNumber(x[1]);
                var month2 = convertMonthNameToNumber(y[1]);
                var date1 = new Date(x[2], month1, x[0]);
                var date2 = new Date(y[2], month2, y[0]);
                if (date1 && date2) {
                    var datediff = days_between(date1, date2);
                    $('#txttotday').val(datediff);
                }
            }
            function convertMonthNameToNumber(monthName) {
                var myDate = new Date(monthName + " 1, 2000");
                var monthDigit = myDate.getMonth();
                return isNaN(monthDigit) ? 0 : (monthDigit + 1);
            }
            function days_between(date1, date2) {
                var ONE_DAY = 1000 * 60 * 60 * 24;
                var date1_ms = date1.getTime();
                var date2_ms = date2.getTime();
                var difference_ms = Math.abs(date1_ms - date2_ms);
                return Math.round((difference_ms / ONE_DAY) + 1);
            }
            function saveCheck() {

                if ($('#slttraintype').val() == "") {
                    alert("Please select Training Type");
                    return false;
                }
                if ($('#slttitle').val() == "") {
                    alert("Please select Training Title");
                    return false;
                }
                if ($('input[type="radio"][name="underwent"]:checked').length == 0) {
                    alert("Please select Allowed or Underwent");
                    return false;
                }
                /*if ($('#startDate').val() == "") {
                    alert("Please enter From Date");
                    $('#txtNotOrdNo').focus();
                    return false;
                }
                if ($('#complDate').val() == "") {
                    alert("Please enter To Date");
                    return false;
                }
                if ($('#txtDays').val() == "") {
                    alert("Please enter No of Days");
                    $('#txttotday').focus();
                    return false;
                }*/
                return true;
            }

        </script>
    </head>
    <body>
        <form:form id="fm" action="trainingschedulelist.htm" method="post" name="myForm" commandName="traininglist">
            <form:hidden id="empId" path="empId"/>
            <form:hidden id="trainId" path="trainId"/>
            
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Training
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-4">
                            <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                        </div>
                        <div class="col-lg-3">   
                            <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/>
                        </div>
                        <div class="col-lg-3"></div>
                        <div class="col-lg-2"></div>
                    </div>
                    <div class="row" style="margin-top: 10px;margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="txtNotOrdNo">Order No<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">   
                            <form:input class="form-control" path="txtOrdNo" id="txtOrdNo"/>
                        </div>
                        <div class="col-lg-2">
                            <label for="txtNotOrdDt">Order Date<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date txtDate">
                                <form:input class="form-control txtDate" path="txtOrdDt" id="txtOrdDt" readonly="true"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;margin-top: 15px;">
                        <div class="col-lg-2">
                            <label for="spc">Training Type<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-5">
                            <form:select path="slttraintype" id="slttraintype" class="form-control">
                                <form:option value="">--Select--</form:option>
                                <form:options items="${trainingType}" itemValue="trainingtypeid" itemLabel="trainingtype"/>
                            </form:select>                           
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label >Training Title<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-5">                              
                            <form:select path="slttitle" id="slttitle" class="form-control">
                                <form:option value="">--Select--</form:option>
                                <form:options items="${trainingTitle}" itemValue="trainingslId" itemLabel="trainingTitle"/>
                            </form:select>  
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label>If Allowed or Underwent<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">   
                            <form:radiobutton path="underwent" value="U"/>&nbsp;Underwent
                        </div>
                        <div class="col-lg-2">
                            <form:radiobutton path="underwent" value="A"/>&nbsp;Allowed
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label>Training Duration</label>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            <label> From Date:
                                <!--<span style="color: red">*</span>-->
                            </label>
                        </div>
                        <div class="col-lg-2">   
                            <div class="input-group date txtDate">
                                <form:input class="form-control txtDate" path="startDate" id="startDate" readonly="true" />
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>   
                        </div>
                        <div class="col-lg-2">
                            <label> To Date:
                               <!-- <span style="color: red">*</span>-->
                            </label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date txtDate">
                                <form:input class="form-control txtDate" path="complDate" id="complDate" readonly="true" onblur="populateNoOfDays();"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>   
                        </div>
                    </div>
                    <div class="row" style="margin-bottom:7px;">
                        <div class="col-lg-2">
                            <label >Total no of Days</label>
                        </div>
                        <div class="col-lg-2">   
                            <form:input class="form-control" path="txttotday" id="txttotday"/>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom:7px;">
                        <div class="col-lg-2">
                            <label >Co-ordinator</label>
                        </div>
                        <div class="col-lg-5">   
                            <form:input class="form-control" path="txtcoordinator" id="txtcoordinator"/>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom:7px;">
                        <div class="col-lg-2">
                            <label>Training Place</label>
                        </div>
                        <div class="col-lg-5">   
                            <form:input class="form-control" path="txttrplace" id="txttrplace"/>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom:7px;">
                        <div class="col-lg-2">
                            <label>Update Cadre Status(TR)</label>
                        </div>
                        <div class="col-lg-5">   
                            <form:checkbox path="nisb" value="TR" />
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="note">Note(if any)</label>
                        </div>
                        <div class="col-lg-6">
                            <form:textarea class="form-control" path="txtnote" id="txtnote"/>
                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="save" value="Save" class="btn btn-primary" onclick="return saveCheck()" />
                        <input type="submit" name="cancel" value="Cancel" class="btn btn-primary"/> 
                    </div>    
                </div>
            </div>

        </form:form>
    </body>
</html>
