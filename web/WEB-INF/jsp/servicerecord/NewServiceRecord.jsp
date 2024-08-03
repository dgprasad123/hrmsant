<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            
            function savecheck(){
                if($("input[name=rdModule]:checked").length == 0){
                    alert("please check any type of Service Record");
                    return false;
                } 
                if ($("input[name=rdModule]:checked").val() == "J"){
                    if ($('#txtJoiningDetails').val() == '') {
                        alert('Please Enter Details of Joining.');
                        $('#txtJoiningDetails').focus();
                        return false;
                    }
                    if ($('#txtJoiningFromDate').val() == '') {
                        alert('Please Enter From Date.');
                        $('#txtJoiningFromDate').focus();
                        return false;
                    }        
                }
                if ($("input[name=rdModule]:checked").val() == "S"){
                    if ($('#txtServiceRecordStation').val() == '') {
                        alert('Please Enter Station of Service Record.');
                        $('#txtServiceRecordStation').focus();
                        return false;
                    }
                    if ($('#txtServiceRecordDetails').val() == '') {
                        alert('Please Enter Details of Service Record.');
                        $('#txtServiceRecordDetails').focus();
                        return false;
                    }
                    if ($('#txtServiceRecordFromDate').val() == '') {
                        alert('Please Enter From Date of Service Record.');
                        $('#txtServiceRecordFromDate').focus();
                        return false;
                    }  
                }
                if ($("input[name=rdModule]:checked").val() == "L"){
                    if ($('#sltLeaveType').val() == '') {
                        alert('Please Select Leave Type of Leave Sanction.');
                        $('#sltLeaveType').focus();
                        return false;
                    }
                    if ($('#txtLeaveSanctionFromDate').val() == '') {
                        alert('Please Enter From Date of Leave Sanction.');
                        $('#txtLeaveSanctionFromDate').focus();
                        return false;
                    }        
                }
                
            }
        </script>
         <style>
            .row-margin{
                margin-bottom: 7px;
            }
        </style>
    </head>
    <body>
        <form:form action="SaveServiceRecord.htm" method="post" commandName="serviceRecordForm">
            <form:hidden path="srid"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Service Record
                    </div>
                    <div class="panel-body">
                        <div class="row row-margin">
                            <div class="col-lg-1" align="center">
                                <form:radiobutton path="rdModule" id="rdJoining" class="" value="J"/>
                            </div>
                            <div class="col-lg-11">
                                <label for="rdJoining">Joining Time<span style="color: red">*</span></label>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                a. Details <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:input path="txtJoiningDetails" id="txtJoiningDetails" class="form-control" checked="false"/>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                b. From Date <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtJoiningFromDate" id="txtJoiningFromDate" class="form-control txtDate" checked="false"/>
                            </div>
                            <div class="col-lg-2">
                                Time
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltJoiningFromTime" id="sltJoiningFromTime" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                c. To Date
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtJoiningToDate" id="txtJoiningToDate" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Time
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltJoiningToTime" id="sltJoiningToTime" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-1" align="center">
                                <form:radiobutton path="rdModule" id="rdServiceRecord" class="" value="S"/>
                            </div>
                            <div class="col-lg-11">
                                <label for="rdServiceRecord">Service Record</label>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                a. Station <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:input path="txtServiceRecordStation" id="txtServiceRecordStation" class="form-control"/>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                b. Details <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:input path="txtServiceRecordDetails" id="txtServiceRecordDetails" class="form-control"/>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                c. From Date <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtServiceRecordFromDate" id="txtServiceRecordFromDate" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Time
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltServiceRecordFromTime" id="sltServiceRecordFromTime" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                d. To Date
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtServiceRecordToDate" id="txtServiceRecordToDate" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Time
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltServiceRecordToTime" id="sltServiceRecordToTime" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-1" align="center">
                                <form:radiobutton path="rdModule" id="rdLeaveSanction" class="" value="L"/>
                            </div>
                            <div class="col-lg-11">
                                <label for="rdLeaveSanction">Leave Sanction</label>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                a. Leave Type <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:select path="sltLeaveType" id="sltLeaveType" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${leavetpyeList}" itemValue="tolid" itemLabel="tol"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                b. From Date <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSanctionFromDate" id="txtLeaveSanctionFromDate" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Time
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltLeaveSanctionFromTime" id="sltLeaveSanctionFromTime" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                c. To Date
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSanctionToDate" id="txtLeaveSanctionToDate" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Time
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltLeaveSanctionToTime" id="sltLeaveSanctionToTime" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                d. Prefix From
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSanctionPrefixFrom" id="txtLeaveSanctionPrefixFrom" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Prefix To
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSanctionPrefixTo" id="txtLeaveSanctionPrefixTo" class="form-control txtDate"/>
                            </div>
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                e. Suffix From
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSanctionSuffixFrom" id="txtLeaveSanctionSuffixFrom" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Suffix To
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSanctionSuffixTo" id="txtLeaveSanctionSuffixTo" class="form-control txtDate"/>
                            </div>
                        </div>   

                        <div class="row row-margin">
                            <div class="col-lg-1" align="center">
                                <form:radiobutton path="rdModule" id="rdLeaveSurrender" class="" value="LS"/>
                            </div>
                            <div class="col-lg-11">
                                <label for="rdLeaveSurrender">Leave Surrender</label>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                a. From Date
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSurrenderFromDate" id="txtLeaveSurrenderFromDate" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                To Date
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSurrenderToDate" id="txtLeaveSurrenderToDate" class="form-control txtDate"/>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                b. From Year
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSurrenderFromYear" id="txtLeaveSurrenderFromYear" class="form-control"/>
                            </div>
                            <div class="col-lg-2">
                                To Year
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtLeaveSurrenderToYear" id="txtLeaveSurrenderToYear" class="form-control"/>
                            </div>
                        </div>
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                c. No of Days
                            </div>
                            <div class="col-lg-6">
                                <form:input path="txtLeaveSurrenderNoDays" id="txtLeaveSurrenderNoDays" class="form-control"/>
                            </div>
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-1" align="center">
                                <form:radiobutton path="rdModule" id="rdUnavailedJoining" class="" value="UJTL"/>
                            </div>
                            <div class="col-lg-11">
                                <label for="rdUnavailedJoining">Unavailed Joining Time</label>
                            </div>
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                a. From Date
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtUnavailedJoiningFromDate" id="txtUnavailedJoiningFromDate" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Time
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltUnavailedJoiningFromTime" id="sltUnavailedJoiningFromTime" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                b. To Date
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtUnavailedJoiningToDate" id="txtUnavailedJoiningToDate" class="form-control txtDate"/>
                            </div>
                            <div class="col-lg-2">
                                Time
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltUnavailedJoiningToTime" id="sltUnavailedJoiningToTime" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>
                         
                        <div class="row row-margin">
                            <div class="col-lg-1"></div>
                            <div class="col-lg-3">
                                <label for="note">Note(if Any)</label>
                            </div>
                            <div class="col-lg-6">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save" class="btn btn-primary" onclick="return savecheck();"/>
                        <input type="submit" name="action" value="Back to List Page" class="btn btn-warning"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
