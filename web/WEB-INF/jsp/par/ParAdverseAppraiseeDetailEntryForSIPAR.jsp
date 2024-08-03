<%-- 
    Document   : ParAdverseAppraiseeDetailEntryForSIPAR
    Created on : 18 Oct, 2023, 12:13:45 PM
    Author     : Manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script> 
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        
        <style type="text/css">
            .star {
                color: #FF0000;
                font-size: 19px;
            }
        </style>
        
        <script type="text/javascript">
            $(document).ready(function() {
                $(this).bind("contextmenu", function(e) {
                    e.preventDefault();
                });
                
                var curdate = new Date();
                $('#adverseCommunicationOnDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    maxDate: curdate,
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            
            function saveCheck() {
                
                if ($("#custodianEmpId").val() == "") {
                    alert("Please Enter Custodian HRMS Id");
                    $("#custodianEmpId").focus();
                    return false;
                }
                if ($("#custodianFullName").val() == "") {
                    alert("Please Enter Custodian Full Name");
                    $("#custodianFullName").focus();
                    return false;
                }
                if ($("#custodianDesignation").val() == "") {
                    alert("Please Enter Custodian Designation");
                    $("#custodianDesignation").focus();
                    return false;
                }
                if ($("#fiscalYear").val() == "") {
                    alert("Please Select Financial Year");
                    $("#fiscalYear").focus();
                    return false;
                }
                if ($("#custodianAddress").val() == "") {
                    alert("Please Enter Custodian Office Address");
                    $("#custodianAddress").focus();
                    return false;
                }
                if ($("#remarksdetail").val() == "") {
                    alert("Please Enter Adverse Remarks detail");
                    $("#remarksdetail").focus();
                    return false;
                }
                if ($("#finalGrading").val() == "") {
                    alert("Please Select Final Grading");
                    $("#finalGrading").focus();
                    return false;
                }
                if ($("#adverseCommunicationOnDate").val() == "") {
                    alert("Please Select Adverse Communication On Date");
                    $("#adverseCommunicationOnDate").focus();
                    return false;
                }
                if ($("#webAddress").val() == "") {
                    alert("Please Enter Web Site Address");
                    $("#webAddress").focus();
                    return false;
                }
                if ($("#webSite").val() == "") {
                    alert("Please Enter e-Mail");
                    $("#webSite").focus();
                    return false;
                }
                if ($("#telFaxNo").val() == "") {
                    alert("Please Enter Tel./Fax No");
                    $("#telFaxNo").focus();
                    return false;
                }
                if ($("#pinCode").val() == "") {
                    alert("Please Enter PinCode");
                    $("#pinCode").focus();
                    return false;
                }
                if ($("#appraisePostingDetail").val() == "") {
                    alert("Please Enter Appraise Posting Detail");
                    $("#appraisePostingDetail").focus();
                    return false;
                }
            }
            
            function numbersOnly(evt) {
                evt = (evt) ? evt : window.event;
                var charCode = (evt.which) ? evt.which : evt.keyCode;
                if (charCode > 31 && (charCode < 48 || charCode > 57)) {
                    return false;
                } else {
                    return true;
                }
            }
        </script>

    </head>
    <body>
        <div class="container-fluid">
            <form:form action="saveAdverseRemarksDetailForAppraise.htm" method="POST" commandName="parAdverseCommunicationDetail" class="form-horizontal" autocomplete="off">
                <form:hidden path="detailIdForCustodianEntry" />
                <div class="panel panel-primary">
                    <div class="panel-heading" style="margin-top:10px">Par Adverse Remark Custodian Detail</div>
                        <div class="panel-body">
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3 control-label"> <label><b class="star">*</b> Custodian HRMS Id </label> </div>
                            <div class="col-lg-3"> <form:input class="form-control" path="custodianEmpId" maxlength="8" onkeypress="return numbersOnly(event)"/> </div>
                            <div class="col-lg-2 control-label"> <label> <b class="star">*</b> Custodian Full Name </label> </div>
                            <div class="col-lg-4"> <form:input class="form-control" path="custodianFullName" id="custodianFullName" maxlength="90"/> </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3 control-label"> <label><b class="star">*</b> Custodian Designation </label> </div>
                            <div class="col-lg-3"> <form:input class="form-control" path="custodianDesignation" id="custodianDesignation" maxlength="50"/> </div>
                            <div class="col-lg-2 control-label"> <label> <b class="star">*</b> Financial Year </label> </div>
                            <div class="col-lg-4"> 
                                <form:select path="fiscalYear" id="fiscalYear" class="form-control">
                                    <option value=""> -- Select Fiscal Year -- </option>
                                    <form:options items="${FiscYear}" itemValue="fy" itemLabel="fy"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3 control-label"> <label><b class="star">*</b> Custodian Office Address </label> </div>
                            <div class="col-lg-9"> <form:input class="form-control" path="custodianAddress" id="custodianAddress" maxlength="90"/> </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3 control-label"> <label><b class="star">*</b> Adverse Remarks Detail </label> </div>
                            <div class="col-lg-9"> <form:textarea class="form-control" path="remarksdetail" id="remarksdetail" 
                                        rows="3" cols="30" style="resize: none;width: 100%;"/> 
                            </div>
                        </div>    
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3 control-label"> <label><b class="star">*</b> Final Grading </label> </div>
                            <div class="col-lg-3"> 
                                <form:select path="finalGrading" id="finalGrading" class="form-control">
                                    <form:option value=""> -- Select -- </form:option>
                                    <form:option value="Out Standing">Out Standing</form:option>
                                    <form:option value="Very Good">Very Good</form:option>
                                    <form:option value="Good">Good</form:option>
                                    <form:option value="Average">Average</form:option>
                                    <form:option value="Below Average">Below Average</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2 control-label"> <label> <b class="star">*</b> Communication On Date </label> </div>
                            <div class="col-lg-4"> <form:input class="form-control" readonly="true" path="adverseCommunicationOnDate" id="adverseCommunicationOnDate"/> </div>
                        </div>    
                            
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3 control-label"> <label><b class="star">*</b> Web Site Address </label> </div>
                            <div class="col-lg-3"> <form:input class="form-control" path="webAddress" id="webAddress" maxlength="29"/> </div>
                            <div class="col-lg-2 control-label"> <label> <b class="star">*</b> e-Mail </label> </div>
                            <div class="col-lg-4"> <form:input class="form-control" path="webSite" id="webSite" maxlength="29"/> </div>
                        </div>
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3 control-label"> <label><b class="star">*</b> Tel./Fax No </label> </div>
                            <div class="col-lg-3"> <form:input class="form-control" path="telFaxNo" id="telFaxNo" maxlength="19"/> </div>
                            <div class="col-lg-2 control-label"> <label> <b class="star">*</b> Pin Code </label> </div>
                            <div class="col-lg-4"> <form:input class="form-control" path="pinCode" id="pinCode" maxlength="6" onkeypress="return numbersOnly(event)"/> </div>
                        </div>
                        
                    </div>
                </div>

                <div class="panel panel-primary">
                    <div class="panel-heading">Par Adverse Remark Appraise Detail</div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2" align="right"> <b>Appraise Name</b> </div>
                            <div class="col-lg-2"> <b style="color:#0000FF;">${parAdverseCommunicationDetail.appraiseFname}</b> </div>
                            <div class="col-lg-2" align="right"> <b>Appraise HRMS Id</b> </div>
                            <div class="col-lg-2"> <b style="color:#0000FF;">${parAdverseCommunicationDetail.appraiseHrmsId}</b> </div>
                            <div class="col-lg-2" align="right"> <b>Appraise Mobile Number</b> </div>
                            <div class="col-lg-2"> <b style="color:#0000FF;">${parAdverseCommunicationDetail.mobileNo} </b></div>
                        </div>
                        
                        <div class="form-group">
                            <label class="control-label col-sm-2" ><b class="star">*</b>Appraise Posting Detail </label>
                            <div class="col-sm-10"> <form:input class="form-control" path="appraisePostingDetail" id="appraisePostingDetail"/>
                            </div>
                        </div>
                    </div>
                            
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-lg-3"> <form:hidden path="parId"/> </div>
                            <div class="col-lg-2"> <form:hidden path="appraiseFname"/> </div>
                            <div class="col-lg-1" align="center"> <input type="submit" name="action" value="Save" class="btn btn-primary btn-lg" onclick="return saveCheck()"/> </div>
                            <div class="col-lg-1" align="center"> <input type="submit" name="action" class="btn btn-danger btn-lg" value="Cancel"/> </div>
                            <div class="col-lg-2"> <form:hidden path="appraiseHrmsId"/> </div>
                            <div class="col-lg-3"> <form:hidden path="mobileNo"/> </div>
                        </div>
                    </div>
                        
                </div>
            </form:form>  

        </div>
    </body>
</html>