<%-- 
    Document   : SelectedDelinquentOfficerListForongoingDP
    Created on : 3 Jan, 2021, 12:42:01 PM
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
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript"  src="js/basicjavascript.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
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

                $("#alreadyservedDiv").hide();
                $("#sendshowcauseDiv").hide();
                $("#DefenceRemarkacceptedDiv").hide();
                $("#appointmentofIo").hide();
                $("#appointmentofIoDetail").hide();
                $("#appointmentofIoMoDiv").hide();
                $("#ioremarksreviewDiv").hide();
                $("#ioRemarks").hide();
                $("#serveToDelinquent").hide();
                $("#reinquiryonDelinquentOfficerDiv").hide();
                $("#sendsecondshowcauseButton").hide();
                $("#secondShowcauseDetail").hide();
                $("#exonerationDetail").hide();
                $("#ioremarksDiv").hide();
                $("#sendserveDelinquentDiv").hide();
                $("#serveDelinquentDetail").hide();
                $("#presentationOfDelinquent").hide();
                $("#exonerationDetailoNpresentationOfDelinquent").hide();
                $("#PunishmentDetailONpresentationOfDO").hide();
                $("#PunishmentDetailONConcurrence").hide();
                $("#considerationByDA").hide();
                $("#punishmentOnConsiderationOfDA").hide();
                $("#representationOnpunishmentOnConsiderationOfDA").hide();
                $("#PunishmentDetailONpresentationOfDOOPSC").hide();
                $("#PunishmentDetailONpresentationOfDOOPSCConcurrence").hide();
                $("#exanarutiononConsiderationOfDAFinalOrder").hide();
                $("#MinorPenaltyONpresentationOfDOOPSC").hide();
                $("#MinorPenaltyONpresentationOfDOOPSCConcurrence").hide();
                $("#finalOrderONpresentationOfDOOPSCMinorPenalty").hide();


                $('.datepickerclass').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').empty();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }
            function getOfficeWisePostList() {
                var offcode = $('#hidAuthOffCode').val();
                $('#authSpc').empty();
                var url = 'getOfficeWithSPCList.htm?offcode=' + offcode;
                url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#authSpc').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }
            function selectauthtype(authtype) {
                tauthtype = authtype;
            }
            function radioClicked() {
                $("#alreadyservedDiv").hide();
                $("#sendshowcauseDiv").hide();
                var radioValue = $("input[name='showcause']:checked").val();
                if (radioValue == "AlreadyServed") {
                    $("#alreadyservedDiv").show();
                    $("#sendshowcauseDiv").hide();
                } else if (radioValue == "NotServed") {
                    $("#sendshowcauseDiv").show();
                    $("#alreadyservedDiv").hide();
                }
            }
            function radioClickedfordefenceremark() {
                var radioValue = $("input[name='defenceremark']:checked").val();
                if (radioValue == "Inquiry") {
                    $("#appointmentofIo").show();
                    $("#DefenceRemarkacceptedDiv").hide();
                    $("#exonerationDetail").hide();
                    $("#MinorPenaltyONpresentationOfDOOPSC").hide();
                    $("#MinorPenaltyONpresentationOfDOOPSCConcurrence").hide();
                    $("#finalOrderONpresentationOfDOOPSCMinorPenalty").hide();
                }
                else if (radioValue == "MinorPenalty") {
                    $("#MinorPenaltyONpresentationOfDOOPSC").show();
                    $("#MinorPenaltyONpresentationOfDOOPSCConcurrence").show();
                    $("#finalOrderONpresentationOfDOOPSCMinorPenalty").show();
                    $("#appointmentofIo").hide();
                    $("#exonerationDetail").hide();
                } else if (radioValue == "Exoneration") {
                    $("#exonerationDetail").show();
                    $("#appointmentofIo").hide();
                    $("#PunishmentDetailONpresentationOfDO").hide();
                    $("#PunishmentDetailONConcurrence").hide();
                     $("#MinorPenaltyONpresentationOfDOOPSC").hide();
                    $("#MinorPenaltyONpresentationOfDOOPSCConcurrence").hide();
                    $("#finalOrderONpresentationOfDOOPSCMinorPenalty").hide();
                }
            }
            function radioClickedforIoAppointment() {
                var radioValue = $("input[name='ioappointment']:checked").val();
                if (radioValue == "AlreadyAppointed") {
                    $("#appointmentofIoDetail").show();
                    $("#ioremarksDiv").show();
                    $("#ioremarksreviewDiv").show();
                    $("#appointmentofIoMoDiv").hide();
                }
                else if (radioValue == "NotAppointedYet") {
                    $("#appointmentofIoMoDiv").show();
                    $("#appointmentofIoDetail").hide();
                    $("#ioremarksDiv").hide();
                    $("#ioremarksreviewDiv").hide();
                }
            }
            function radioClickedforIoRemarks() {
                var radioValue = $("input[name='ioremarks']:checked").val();
                if (radioValue == "ioremarksGiven") {
                    $("#ioRemarks").show();
                    $("#ioremarksreviewDiv").show();
                    $("#ioremarksreviewDiv").show();
                }

            }
            function radioClickedforIoRemarksReview() {
                var radioValue = $("input[name='ioremarksreviewcheck']:checked").val();
                if (radioValue == "ioremarksAccepted") {
                    $("#serveToDelinquent").show();
                    $("#reinquiryonDelinquentOfficerDiv").hide();
                } else if (radioValue == "ioremarksNotAccepted") {
                    $("#reinquiryonDelinquentOfficerDiv").show();
                    $("#serveToDelinquent").hide();
                }
            }
            function radioClickedForSecondShowCause() {
                var radioValue = $("input[name='secondshowcause']:checked").val();
                if (radioValue == "AlreadyIssuedseconshowcause") {
                    $("#secondShowcauseDetail").show();
                    $("#sendsecondshowcauseButton").hide();
                } else if (radioValue == "NotIssuedYetseconshowcause") {
                    $("#sendsecondshowcauseButton").show();
                    $("#secondShowcauseDetail").hide();
                }
            }
            function radioClickedForserveDelinquent() {
                var radioValue = $("input[name='serveDelinquent']:checked").val();
                if (radioValue == "AlreadyServedDelinquent") {
                    $("#serveDelinquentDetail").show();
                    $("#presentationOfDelinquent").show();
                    $("#considerationByDA").show();
                    $("#sendserveDelinquentDiv").hide();
                } else if (radioValue == "NotServedDelinquent") {
                    $("#sendserveDelinquentDiv").show();
                    $("#serveDelinquentDetail").hide();
                    $("#presentationOfDelinquent").hide();
                    $("#considerationByDA").hide();
                }
            }
            function radioClickedforpresentationOfDO() {
                var radioValue = $("input[name='presentationOfDelinquentdIV']:checked").val();
                if (radioValue == "PunishmentONpresentationOfDO") {
                    $("#PunishmentDetailONpresentationOfDO").show();
                    $("#PunishmentDetailONConcurrence").show();
                    $("#exonerationDetailoNpresentationOfDelinquent").hide();
                } else if (radioValue == "ExonerationpresentationOfDO") {
                    $("#exonerationDetailoNpresentationOfDelinquent").show();
                    $("#PunishmentDetailONpresentationOfDO").hide();
                    $("#PunishmentDetailONConcurrence").hide();
                }
            }
            function radioClickedforpresentationOfDOconsideration() {
                var radioValue = $("input[name='ConsiderationOfDelinquentdIV']:checked").val();
                if (radioValue == "punishmentOnConsiderationOdDA") {
                    $("#punishmentOnConsiderationOfDA").show();
                    $("#representationOnpunishmentOnConsiderationOfDA").show();
                    $("#PunishmentDetailONpresentationOfDOOPSC").show();
                    $("#PunishmentDetailONpresentationOfDOOPSCConcurrence").show();
                    $("#finalOrderONpresentationOfDOOPSCConcurrence").show();
                    $("#exanarutiononConsiderationOfDAFinalOrder").hide();
                } else if (radioValue == "exanarutiononConsiderationOfDA") {
                    $("#exanarutiononConsiderationOfDAFinalOrder").show();
                    $("#punishmentOnConsiderationOfDA").hide();
                    $("#representationOnpunishmentOnConsiderationOfDA").hide();
                    $("#PunishmentDetailONpresentationOfDOOPSC").hide();
                    $("#PunishmentDetailONpresentationOfDOOPSCConcurrence").hide();
                    $("#finalOrderONpresentationOfDOOPSCConcurrence").hide();
                }
            }





        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <form:form action="editRule15ProceedingForOngoingDP.htm" method="post" commandName="ProceedingBean">
                    <div class="form-group">
                        <label class="control-label col-sm-2">Delinquent Officer Name:</label>
                        ${emp.fullname},${emp.spn}
                    </div>


                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Details Of Memorandum Of Charge
                        </div>
                        <div class="panel-body">
                            <div class="form-group row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Memorandum No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input class="form-control" path="showCauseOrdNo" maxlength="51"/> 
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Memorandum Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="spc">3.Disciplinary Authority Detail</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:input class="form-control" path="showCauseNotAuthority" readonly="true"/>                          
                                </div>
                                <div class="col-lg-1">
                                    <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('S')" data-target="#authorityModal">
                                        <span class="glyphicon glyphicon-search"></span> Search
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Article Of Charge, Statement Of Imputation, Memo Of Evidence
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">                            
                                <div class="col-lg-2">
                                    <div class="panel-footer">
                                        <form:hidden path="daId"/>
                                        <c:if test="${iseditable eq true}">
                                            <input type="submit" name="action" class="btn btn-default" value="Add New"/>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Whether Served Or Not
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"> 
                                <input type="radio" id="ShowcauseIssued" name="showcause" value="AlreadyServed" onclick="radioClicked()"> <b>Already Served</b>
                            </div>
                            <div class="col-lg-2">
                                <input type="radio" id="ShowcauseNotIssued" name="showcause" value="NotServed" onclick="radioClicked()">  <b>Not Served</b>
                            </div>  
                        </div>
                        <div class="panel-body" id="alreadyservedDiv" >
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label> 1.Date Of Receipt<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">2.Document</label>
                                </div>
                                <div class="form-group row" id="showcausedocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>
                        <div class="row" id="sendshowcauseDiv" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <input type ="button" value="Send Show Cause" />
                            </div>
                        </div>
                    </div>


                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Written Statement By Delinquent Officer
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label> 1.Date Of Receipt<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">2.Document<span style="color: red">*</span></label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">

                        <div class="col-lg-2"> 
                            <input type="radio" id="DefenceRemarkAccepted" name="defenceremark" value="Inquiry" onclick="radioClickedfordefenceremark()"><b>Inquiry</b>
                        </div>
                        <div class="col-lg-2">
                            <input type="radio" id="DefenceRemarkRejected" name="defenceremark" value="MinorPenalty" onclick="radioClickedfordefenceremark()"><b>Minor Penalty</b>
                        </div> 
                        <div class="col-lg-2">
                            <input type="radio" id="DefenceRemarkRejected" name="defenceremark" value="Exoneration" onclick="radioClickedfordefenceremark()"><b>Exoneration</b>
                        </div> 

                    </div>
                    <div class="row"  id="DefenceRemarkacceptedDiv" style="margin-bottom: 7px;">  
                        <div class="col-lg-2">
                            <input type="radio" id="ShowcauseIssued" name="punishment" value="acceptingthedefenceplea"> <b>accepting the defence plea</b>
                        </div>  
                        <div class="col-lg-2">
                            <input type="radio" id="ShowcauseIssued" name="punishment" value="exonerateDOofcharges"> <b>exonerate DO of charges</b>
                        </div>
                        <div class="col-lg-2">
                            <input type="radio" id="ShowcauseIssued" name="punishment" value="minorpenaltyproceeding"> <b>minor penalty proceeding</b>
                        </div>
                    </div>
                    <div class="panel panel-default" id="exonerationDetail">
                        <div class="panel-heading">
                            Final Order
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document<span style="color: red">*</span></label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>

                    </div>

                </div>


                <div class="panel panel-default" id="appointmentofIo">
                    <div class="panel-heading">
                        Appointment Of I.O(Inquiry Officer),P.O(Presenting Officer) and A.P.O(Additional Presenting Officer)
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">

                            <div class="col-lg-2"> 
                                <input type="radio" id="alreadyioAppointed" name="ioappointment" value="AlreadyAppointed" onclick="radioClickedforIoAppointment()"><b>Already Appointed</b>
                            </div>
                            <div class="col-lg-2">
                                <input type="radio" id="ioNotAppointed" name="ioappointment" value="NotAppointedYet" onclick="radioClickedforIoAppointment()"><b>Not Appointed Yet</b>
                            </div>  
                        </div>
                    </div>
                </div>


                <div class="panel panel-default" id="appointmentofIoDetail">
                    <div class="panel-heading">
                        I.O(Inquiry Officer) and P.O Detail
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label>1.Office Order No<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                        </div>
                        <div class="col-lg-2">
                            <label> 2.Office Order Date<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="spc">3. Details of Inquiry Officer</label>
                        </div>
                        <div class="col-lg-9">
                            <form:input class="form-control" path="showCauseNotAuthority" readonly="true"/>                          
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('S')" data-target="#authorityModal">
                                <span class="glyphicon glyphicon-search"></span> Search
                            </button>
                        </div>
                    </div>
                </div>

                <div class="panel panel-default" id="appointmentofIoMoDiv">
                    <div class="panel-heading">
                        Please Choose Any one
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-footer">
                            <input type="submit" name="action" class="btn btn-default" value="Add IO"/>
                            <input type="submit" name="action" class="btn btn-default" value="Add PO"/>
                        </div>
                    </div>
                </div>


                <div class="panel panel-default" id="ioremarksDiv">
                    <div class="panel-heading">
                        I.O'S Findings
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label>1.Letter Number<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                        </div>
                        <div class="col-lg-2">
                            <label>2.Date<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>
                    <div class="panel-body" id="ioRemarks">
                        <div class="row" style="margin-bottom: 7px;">  
                            <div class="col-lg-2">
                                <label for="document">3.Document<span style="color: red">*</span></label>
                            </div>
                            <div class="form-group row" id="writtenStatementByDOdocument">                            
                                <input type="file" name="" id=""  class="form-control-file"/>
                            </div> 
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;" id="ioremarksreviewDiv">

                        <div class="col-lg-2"> 
                            <input type="radio" id="ioremarkaccepted" name="ioremarksreviewcheck" value="ioremarksAccepted" onclick="radioClickedforIoRemarksReview()"><b>Accepted</b>
                        </div>
                        <div class="col-lg-2">
                            <input type="radio" id="ioremarkrejected" name="ioremarksreviewcheck" value="ioremarksNotAccepted" onclick="radioClickedforIoRemarksReview()"><b>Not Accepted</b>
                        </div>   
                    </div>
                </div>


                <div class="panel panel-default" id="serveToDelinquent">
                    <div class="panel-heading">
                        Notice to the Delinquent Officer
                    </div>
                    <div class="row" style="margin-bottom: 7px;">  
                        <div class="col-lg-2">
                            <input type="radio" id="alreadyServed" name="serveDelinquent" value="AlreadyServedDelinquent" onclick="radioClickedForserveDelinquent()"> <b>Already Served</b>
                        </div>  
                        <div class="col-lg-2">
                            <input type="radio" id="notServed" name="serveDelinquent" value="NotServedDelinquent" onclick="radioClickedForserveDelinquent()"> <b>Not Served Yet</b>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">

                        <div class="col-lg-2">
                            <label>Date Of Service<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>
                    <div class="row" id="sendserveDelinquentDiv" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <input type ="button" value="Send To Delinquent" />
                        </div>
                    </div>
                </div>


                <div class="panel panel-default" id="presentationOfDelinquent">
                    <div class="panel-heading">
                        Representation Of Delinquent Officer
                    </div>
                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                        <div class="col-lg-2">
                            <label>1.Letter No<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                        </div>
                        <div class="col-lg-2">
                            <label> 2.Date<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">  
                        <div class="col-lg-2">
                            <label for="document">Document<span style="color: red">*</span></label>
                        </div>
                        <div class="form-group row" id="writtenStatementByDOdocument">                            
                            <input type="file" name="" id=""  class="form-control-file"/>
                        </div> 
                    </div>


                </div>
                <div class="panel panel-default" id="considerationByDA">
                    <div class="panel-heading">
                        Consideration By the Disciplinary Authority
                    </div>
                    <div class="row" style="margin-bottom: 7px;">

                        <div class="col-lg-2">
                            <input type="radio" id="PunishmentONConsiderationOfDelinquentdIV" name="ConsiderationOfDelinquentdIV" value="punishmentOnConsiderationOdDA" onclick="radioClickedforpresentationOfDOconsideration()"><b>Punishment Proposed</b>
                        </div> 
                        <div class="col-lg-2">
                            <input type="radio" id="ExonerationConsiderationOfDelinquentdIV" name="ConsiderationOfDelinquentdIV" value="exanarutiononConsiderationOfDA" onclick="radioClickedforpresentationOfDOconsideration()"><b>Exoneration</b>
                        </div> 

                    </div>
                    <div class="panel panel-default" id="punishmentOnConsiderationOfDA">
                        <div class="panel-heading">
                            Notice to the  Delinquent Officer
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>1.Letter No</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Date</label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                    </div>

                </div>
                <div class="panel panel-default" id="representationOnpunishmentOnConsiderationOfDA">
                    <div class="panel-heading">
                        Representation Of the Delinquent Officer
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label>1.Letter No</label>
                        </div>
                        <div class="col-lg-2">
                            <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                        </div>
                        <div class="col-lg-2">
                            <label> 2.Date</label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>

                </div>
                <div class="panel panel-default" id="PunishmentDetailONpresentationOfDOOPSC">
                    <div class="panel-heading">
                        OPSC Consultation
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                            <div class="col-lg-2">
                                <label>1.Letter No</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Date</label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document</label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>


                    </div>

                </div>
                <div class="panel panel-default" id="PunishmentDetailONpresentationOfDOOPSCConcurrence">
                    <div class="panel-heading">
                        OPSC Concurrence
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                            <div class="col-lg-2">
                                <label>1.Letter No</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Date</label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document</label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default" id="finalOrderONpresentationOfDOOPSCConcurrence">
                        <div class="panel-heading">
                            Final Order
                        </div>
                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                            <div class="col-lg-2">
                                <label>1.Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document<span style="color: red">*</span></label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>

                    </div>

                </div>
                <div class="panel panel-default" id="exanarutiononConsiderationOfDAFinalOrder">
                    <div class="panel-heading">
                        Final Order
                    </div>
                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                        <div class="col-lg-2">
                            <label>1.Order No<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                        </div>
                        <div class="col-lg-2">
                            <label> 2.Date<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">  
                            <div class="col-lg-2">
                                <label for="document">Document<span style="color: red">*</span></label>
                            </div>
                            <div class="form-group row" id="writtenStatementByDOdocument">                            
                                <input type="file" name="" id=""  class="form-control-file"/>
                            </div> 
                        </div>
                    </div>

                </div>

                <div class="panel panel-default" id="MinorPenaltyONpresentationOfDOOPSC">
                    <div class="panel-heading">
                        OPSC Consultation
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                            <div class="col-lg-2">
                                <label>1.Letter No</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Date</label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document</label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>


                    </div>

                </div>
                <div class="panel panel-default" id="MinorPenaltyONpresentationOfDOOPSCConcurrence">
                    <div class="panel-heading">
                        OPSC Concurrence
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                            <div class="col-lg-2">
                                <label>1.Letter No</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Date</label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document</label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default" id="finalOrderONpresentationOfDOOPSCMinorPenalty">
                        <div class="panel-heading">
                            Final Order
                        </div>
                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                            <div class="col-lg-2">
                                <label>1.Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document<span style="color: red">*</span></label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>

                    </div>




                    <div class="panel panel-default" id="PunishmentDetailONpresentationOfDO">
                        <div class="panel-heading">
                            OPSC Reference
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                <div class="col-lg-2">
                                    <label>1.Letter No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Final Order
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;">  
                                        <div class="col-lg-2">
                                            <label for="document">Document<span style="color: red">*</span></label>
                                        </div>
                                        <div class="form-group row" id="writtenStatementByDOdocument">                            
                                            <input type="file" name="" id=""  class="form-control-file"/>
                                        </div> 
                                    </div>
                                </div>

                            </div>

                        </div>

                    </div>
                    <div class="panel panel-default" id="PunishmentDetailONConcurrence">
                        <div class="panel-heading">
                            OPSC Concurrence
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                <div class="col-lg-2">
                                    <label>1.Letter No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;">  
                                        <div class="col-lg-2">
                                            <label for="document">Document<span style="color: red">*</span></label>
                                        </div>
                                        <div class="form-group row" id="writtenStatementByDOdocument">                            
                                            <input type="file" name="" id=""  class="form-control-file"/>
                                        </div> 
                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Final Order
                                </div>
                                <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                    <div class="col-lg-2">
                                        <label>1.Letter No<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">
                                        <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                                    </div>
                                    <div class="col-lg-2">
                                        <label> 2.Date<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;">  
                                            <div class="col-lg-2">
                                                <label for="document">Document<span style="color: red">*</span></label>
                                            </div>
                                            <div class="form-group row" id="writtenStatementByDOdocument">                            
                                                <input type="file" name="" id=""  class="form-control-file"/>
                                            </div> 
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </div>

                    </div>


                    <div class="panel panel-default" id="exonerationDetailoNpresentationOfDelinquent">
                        <div class="panel-heading">
                            Final Order
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document<span style="color: red">*</span></label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>

                    </div>


                    <div class="panel panel-default" id="reinquiryonDelinquentOfficerDiv" >
                        <div class="panel-heading">
                            Reinquiry against Delinquent Officer
                        </div>


                        <div class="form-group row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>1.Office Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="showCauseOrdNo" maxlength="51"/> 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Office Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                    </div>


                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return confirm('Are you sure to Choose?')"/>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                    </div>
                </div>
            </div>

            <div id="authorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Sanctioning Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <option value="">--Select Department--</option>
                                        <c:forEach items="${deptlist}" var="dept">
                                            <option value="${dept.deptCode}">${dept.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <option value="">--Select Office--</option>
                                        <c:forEach items="${sancOfflist}" var="toffice">
                                            <option value="${toffice.offCode}">${toffice.offName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <select path="authSpc" id="authSpc" class="form-control" onchange="getPost();">
                                        <option value="">--Select Post--</option>
                                        <c:forEach items="${sancPostlist}" var="post">
                                            <option value="${post.spc}">${post.postname}</option>
                                        </c:forEach>                                         
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
        </form:form>
    </body>
</html>


