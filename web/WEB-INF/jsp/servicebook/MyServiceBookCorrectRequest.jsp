<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }
            .tblTrColor{
                background: rgb(174,238,209);
                background: radial-gradient(circle, rgba(174,238,209,0.9976191160057774) 0%, rgba(148,231,233,1) 100%);
                color: #000000;
                font-weight: bold;
            }
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}

            .loading .spinner {
                border: 16px solid #f3f3f3;
                border-radius: 50%;
                border-top: 16px solid #3498db;
                width: 50px;
                height: 50px;
                margin: 0 auto;
                -webkit-animation: spin 2s linear infinite; /* Safari */
                animation: spin 2s linear infinite;
                display: none;
            }

            /* Safari */
            @-webkit-keyframes spin {
                0% { -webkit-transform: rotate(0deg); }
                100% { -webkit-transform: rotate(360deg); }
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        </style>

        <script type="text/javascript">
            var spn = "";
            var cadre = "";
            var payscale = "";
            var pay = "";
            var wefchange = "";
            var category = "";
            var sbdescription = "";
            var sblanguagerequested = "";
            var entrytype = "";
            var editlink = "";
            var isprocessed = "";
            var isrejected = "";
            $(document).ready(function() {
                //var table = $('#datatable').DataTable();
                $(".loader").hide();
                $("#btnAddNew").hide();

                $("#openRequestModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });

                //$("#servicebookdiv").load("jsp/servicebook/tarunpaul.html");
                //if ($("#hiddatalistsize").val() == "1") {
                $(".loading .spinner").css("display", "block");
                /*$.getJSON("GetEmployeeServiceBookDataForSBCorrection.htm", function(data) {
                    $.each(data, function(i, obj1) {
                        $('#servicebooktbody').empty();
                        $.each(obj1, function(j, obj2) {
                            //alert(obj2.tabname);
                            if (obj2.spn != undefined) {
                                spn = obj2.spn;
                            }
                            if (obj2.cadre != undefined) {
                                cadre = obj2.cadre;
                            }
                            if (obj2.payscale != undefined) {
                                payscale = obj2.payscale;
                            }
                            if (obj2.pay != undefined) {
                                pay = obj2.pay;
                            }
                            if (obj2.wefChange != undefined) {
                                wefchange = obj2.wefChange;
                            }
                            if (obj2.category != undefined) {
                                category = obj2.category;
                            }
                            if (obj2.sbdescription != undefined) {
                                sbdescription = obj2.sbdescription;
                            }
                            $('#servicebooktbody').append("<tr>" +
                                    "<td>" + spn + "\n" + cadre + "\n" + payscale + "</td>" +
                                    "<td>" + pay + "</td>" +
                                    "<td>" + wefchange + "</td>" +
                                    "<td>" + category + "\n" + sbdescription + "</td>" +
                                    "<td></td>" +
                                    "<td></td>" +
                                    "</tr>");
                        });
                    });
                });*/
                //}
                getModuleDataList();
            });

            function validateModule() {
                if ($("#sbReqModuleName").val() == "") {
                    alert("Please select the Module Name");
                    $("#sbReqModuleName").focus();
                    return false;
                } else {
                    getModuleDataList();
                }
                $(".loader").show();
            }

            function getModuleDataList() {
                $('#moduledatalist').empty();
                var count = 0;                
                $.getJSON("GetSBCorrectionModuleDataList.htm?moduleName=" + $("#sbReqModuleName").val(), function(data) {
                    $.each(data, function(i, obj) {
                        count = count + 1;
                        spn = "";
                        cadre = "";
                        payscale = "";
                        pay = "";
                        wefchange = "";
                        category = "";
                        sbdescription = "";
                        sblanguagerequested = "";
                        entrytype = "";
                        editlink = "";
                        isprocessed = "";
                        isrejected = "";
                        //alert(obj2.tabname);
                        if (obj.spn != '' && obj.spn != undefined) {
                            spn = obj.spn;
                        }
                        //alert(obj.tabName);
                        if (obj.cadre != '' && obj.cadre != undefined) {
                            cadre = obj.cadre;
                        }
                        if (obj.payscale != '' && obj.payscale != undefined) {
                            payscale = obj.payscale;
                        }
                        if (obj.pay != '' && obj.pay != undefined) {
                            pay = obj.pay;
                        }
                        if (obj.wefChange != '' && obj.wefChange != undefined) {
                            wefchange = obj.wefChange;
                        }
                        if (obj.category != '' && obj.category != undefined) {
                            category = obj.category;
                        }
                        if (obj.sbDescription != '' && obj.sbDescription != undefined) {
                            sbdescription = obj.sbDescription + "<hr />";
                        }
                        if (obj.isProcessed != '' && obj.isProcessed != undefined) {
                            isprocessed = obj.isProcessed;
                        }
                        if (obj.isRejected != '' && obj.isRejected != undefined) {
                            isrejected = obj.isRejected;
                        }
                        if (obj.sbLanguageRequested != '' && obj.sbLanguageRequested != undefined) {
                            /*if(isprocessed == "Y"){
                                sblanguagerequested = "<br /><b style=\"color:#FF4500;\">" + obj.sbLanguageRequested + "</b><i class=\"fa fa-check\" style=\"color:green;font-size:24px\"></i>";
                            }else if(isrejected == "Y"){
                                sblanguagerequested = "<br /><b style=\"color:#FF4500;\">" + obj.sbLanguageRequested + "</b><span <i class='fa fa-times' style=\"color:red;font-size:24px\"></i>";
                            }else{*/
                                sblanguagerequested = "<br /><b style=\"color:#FF4500;\">" + obj.sbLanguageRequested + "</b>";
                            //}
                        }
                        if (obj.entrytype != '' && obj.entrytype != undefined && obj.entrytype == "NEW") {
                            entrytype = obj.entrytype;
                        }                        
                        if(!obj.isSubmitted || obj.isSubmitted == 'N'){
                            //alert("obj.tabName is: "+obj.tabName);
                            if (obj.tabName == 'INCREMENT') {
                                $("#btnAddNew").show();
                                $("#addnewmsg").hide();
                                editlink = "<a href='EmployeeEditIncrementSBCorrection.htm?mode=E&incrId=" + obj.moduleid + "&notId=" + obj.noteIdEnc + "&correctionid=" + obj.correctionid + "'" +
                                        "data-remote='false' data-toggle='modal' title='Click to Open'" +
                                        "data-target='#openRequestModal'> <b style='color: #0000FF;'></b> Edit" +
                                        "</a>";
                            } else if (obj.tabName == 'PAYFIXATION' || obj.tabName == 'PAYREVISION') {
                                $("#btnAddNew").show();
                                $("#addnewmsg").hide();
                                editlink = "<a href='EditPayFixationSBCorrection.htm?mode=E&notId=" + obj.noteIdEnc + "&correctionid=" + obj.correctionid + "'" +
                                        "data-remote='false' data-toggle='modal' title='Click to Open'" +
                                        "data-target='#openRequestModal'> <b style='color: #0000FF;'></b> Edit" +
                                        "</a>";
                            } else if (obj.tabName == 'PROMOTION') {
                                $("#btnAddNew").show();
                                $("#addnewmsg").hide();
                                editlink = "<a href='EditPromotionSBCorrection.htm?mode=E&notId=" + obj.noteIdEnc + "&promotionId=" + obj.moduleid + "&correctionid=" + obj.correctionid + "'" +
                                        "data-remote='false' data-toggle='modal' title='Click to Open'" +
                                        "data-target='#openRequestModal'> <b style='color: #0000FF;'></b> Edit" +
                                        "</a>";
                            } else if (obj.tabName == 'RELIEVE') {
                                $("#btnAddNew").hide();
                                $("#addnewmsg").show();
                                editlink = "<a href='EditRelieveSBCorrection.htm?mode=E&notId=" + obj.noteIdEnc + "&rlvId=" + obj.moduleid + "&correctionid=" + obj.correctionid + "'" +
                                        "data-remote='false' data-toggle='modal' title='Click to Open'" +
                                        "data-target='#openRequestModal'> <b style='color: #0000FF;'></b> Edit" +
                                        "</a>";
                            } else if (obj.tabName == 'JOINING') {
                                $("#btnAddNew").hide();
                                $("#addnewmsg").show();
                                editlink = "<a href='EditJoiningSBCorrection.htm?mode=E&notId=" + obj.noteIdEnc + "&rlvId=" + obj.moduleRefId + "&jId=" + obj.moduleid + "&correctionid=" + obj.correctionid + "'" +
                                        "data-remote='false' data-toggle='modal' title='Click to Open'" +
                                        "data-target='#openRequestModal'> <b style='color: #0000FF;'></b> Edit" +
                                        "</a>";
                            } else if (obj.tabName == 'SERVICE VERIFICATION CERTIFICATE') {
                                $("#btnAddNew").show();
                                $("#addnewmsg").hide();
                                editlink = "<a href='EditServiceVerifySBCorrection.htm?mode=E&txtsvid=" + obj.moduleid + "&correctionid=" + obj.correctionid + "'" +
                                        "data-remote='false' data-toggle='modal' title='Click to Open'" +
                                        "data-target='#openRequestModal'> <b style='color: #0000FF;'></b> Edit" +
                                        "</a>";
                            }else if (obj.tabName == 'LOAN_SANC') {
                                $("#btnAddNew").show();
                                $("#addnewmsg").hide();
                                editlink = "<a href='EditLoanDataSBCorrection.htm?mode=E&loanid=" + obj.moduleid + "&correctionid=" + obj.correctionid + "'" +
                                        "data-remote='false' data-toggle='modal' title='Click to Open'" +
                                        "data-target='#openRequestModal'> <b style='color: #0000FF;'></b> Edit" +
                                        "</a>";
                            }
                        }else{
                            if(isprocessed == "Y"){
                                editlink = "<br /><b style=\"color:green;\">" + obj.approvalStatus + "</b>";
                            }else if(isrejected == "Y"){
                                editlink = "<br /><b style=\"color:red;\">" + obj.approvalStatus + "</b>";
                            }else{
                                editlink = "<br /><b style=\"color:#FF4500;\">" + obj.isSubmitted + "</b>";
                            }
                        }
                        $('#moduledatalist').append("<tr>" +
                                "<td>" + count + "</td>" +
                                "<td>" + spn + "<br />" + cadre + "<br />" + payscale + "</td>" +
                                "<td>" + pay + "</td>" +
                                "<td>" + wefchange + "</td>" +
                                "<td>" + category + "<br />" + sbdescription + sblanguagerequested + "</td>" +
                                "<td>" + entrytype + "</td>" +
                                "<td>" + editlink + "</td>" +
                                "</tr>");

                    });
                    $(".loader").hide();
                });                
            }

            function addModule() {
                var link = "";
                if ($("#sbReqModuleName").val() == "INCREMENT") {
                    link = "EmployeeEditIncrementSBCorrection.htm?mode=E&entrytype=NEW";
                } else if ($("#sbReqModuleName").val() == "PAYREVISION" || $("#sbReqModuleName").val() == "PAYFIXATION") {
                    link = "EditPayFixationSBCorrection.htm?mode=E&notType=" + $("#sbReqModuleName").val() + "&entrytype=NEW";
                } else if ($("#sbReqModuleName").val() == "PROMOTION") {
                    link = "EditPromotionSBCorrection.htm?mode=E&notType=" + $("#sbReqModuleName").val() + "&entrytypeSBCorrection=NEW";
                } else if ($("#sbReqModuleName").val() == "JOINING") {
                    link = "EditJoiningSBCorrection.htm?mode=E&notType=" + $("#sbReqModuleName").val() + "&entrytypeSBCorrection=NEW";
                } else if ($("#sbReqModuleName").val() == "RELIEVE") {
                    link = "EditRelieveSBCorrection.htm?mode=E&notType=" + $("#sbReqModuleName").val() + "&entrytypeSBCorrection=NEW";
                } else if ($("#sbReqModuleName").val() == "SERVICE VERIFICATION CERTIFICATE") {
                    link = "EditServiceVerifySBCorrection.htm?mode=E&notType=" + encodeURI($("#sbReqModuleName").val()) + "&entrytypeSBCorrection=NEW";
                } else if ($("#sbReqModuleName").val() == "LOAN") {
                    link = "EditLoanDataSBCorrection.htm?mode=E&notType=" + encodeURI($("#sbReqModuleName").val()) + "&entrytypeSBCorrection=NEW";
                }
                $("#AddNewOpenRequestModal").modal("show");
                $("#AddNewOpenRequestModal").find(".modal-body").load(link);
            }
            
            function openServiceBook(){
                window.open("myserviceBookPrePage.htm", "Service Book", "width=1000,height=800");
            }
        </script>
    </head>

    <body style="margin-top:0px;background:#E4EFFF; overflow-x:auto;">
        <c:if test="${not empty LoadModuleDataList}">
            <input type="hidden" id="hiddatalistsize" value="1"/>
        </c:if>
        <c:if test="${empty LoadModuleDataList}">
            <input type="hidden" id="hiddatalistsize" value="0"/>
        </c:if>
        <div style="width:100%;position:relative;">
            <%--<button type="button" id="btnSlide" onclick="slideDivs();">Slide</button>--%>
            <div id="correctionrequestdiv">
                <form:form id="sbReqForm" action="sbCorrectRequest.htm" method="post" commandName="SbCorReqBean">
                    <div id="wrapper" style="padding-left: 0px;"> 
                        <div id="page-wrapper">  

                            <div class="row" style="margin-bottom: 10px;">                        
                                <div class="col-lg-1"> &nbsp; </div>

                                <div class="col-lg-4">
                                    <select name="sbReqModuleName" id="sbReqModuleName" class="form-control">
                                        <option value=""> -- Select Module Name -- </option>
                                        <option value="INCREMENT" <c:if test="${ModType eq 'INCREMENT'}">selected="selected"</c:if>>INCREMENT</option>
                                        <option value="JOINING" <c:if test="${ModType eq 'JOINING'}">selected="selected"</c:if>>JOINING</option>                                        
                                        <option value="PAYREVISION" <c:if test="${ModType eq 'PAYREVISION'}">selected="selected"</c:if>>PAY REVISION</option>
                                        <option value="PAYFIXATION" <c:if test="${ModType eq 'PAYFIXATION'}">selected="selected"</c:if>>PAY FIXATION</option>
                                        <option value="PROMOTION" <c:if test="${ModType eq 'PROMOTION'}">selected="selected"</c:if>>PROMOTION</option>
                                        <option value="RELIEVE" <c:if test="${ModType eq 'RELIEVE'}">selected="selected"</c:if>>RELIEVE</option>
                                        <option value="SERVICE VERIFICATION CERTIFICATE" <c:if test="${ModType eq 'SERVICE VERIFICATION CERTIFICATE'}">selected="selected"</c:if>>SERVICE VERIFICATION CERTIFICATE</option>
                                        <option value="LOAN" <c:if test="${ModType eq 'LOAN'}">selected="selected"</c:if>>LOAN</option>
                                        <%--<c:forEach items="${LoadModuleNameList}" var="eachSbMod">
                                            <option value="${eachSbMod.value}" <c:if test="${eachSbMod.value eq ModType}">selected="selected"</c:if>>${eachSbMod.label}</option>
                                        </c:forEach>--%>
                                    </select>                            
                                </div>
                                <div class="col-lg-3" align="left">
                                    <input type="button" class="btn btn-warning btn-md" id="searchbtn" name="btnRequest" value="Search" onclick="return validateModule();"/>&nbsp;
                                    <%--<c:if test="${not empty SbCorReqBean.sbReqModuleName && (SbCorReqBean.sbReqModuleName ne 'RELIEVE' && SbCorReqBean.sbReqModuleName ne 'JOINING')}">--%>
                                        <input type="button" class="btn btn-primary btn-md" id="btnAddNew" value="Add New" onclick="return addModule();"/>
                                        <span id="addnewmsg" style="display:none;color:red; font-weight:bold; font-size: 12px;">Contact DDO for New Entry</span>
                                    <%--</c:if>--%>
                                    <a href="javascript:openServiceBook();"><button type="button" class="btn btn-danger">View Service Book</button></a>
                                    <a href="https://drive.google.com/file/d/1phOXLNkw5XsfLhx08bC9qvvFBOugjyM9/view?usp=sharing" target="_blank"><button type="button" class="btn btn-success">View User Manual</button></a>
                                </div>
                                <div class="col-lg-1"> <div class="loader"></div> </div>
                                <div class="col-lg-1"></div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">                            
                                    <div class="table-responsive">
                                        <table id="datatable" class="table table-bordered table-hover table-striped" width="100%">
                                            <thead class="tblTrColor">
                                                <tr>
                                                    <th width="2%"> # </th> 
                                                    <th width="15%"> POST/ CADRE/ SCALE of PAY </th>
                                                    <th width="7%"> PAY </th> 
                                                    <th width="10%"> WEF </th>                    
                                                    <th width="45%"> Entry in the Service Book </th>
                                                    <th width="10%"> Entry Type </th>
                                                    <th width="15%"> Action </th>  
                                                </tr>
                                            </thead>

                                            <tbody id="moduledatalist">
                                                <c:if test="${not empty LoadModuleDataList}">
                                                    <c:forEach items="${LoadModuleDataList}" var="eachData" varStatus="cnt">
                                                        <c:set var="ModuleName" value="${eachData.tabName}"/>
                                                        <tr>
                                                            <td>${cnt.index + 1}</td>
                                                            <td>
                                                                <c:if test="${not empty eachData.spn}"> ${eachData.spn} </br> </c:if>  
                                                                <c:if test="${not empty eachData.cadre}"> ${eachData.cadre} </br>  </c:if>
                                                                <c:if test="${not empty eachData.payscale}">
                                                                    ${eachData.payscale} 
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${not empty eachData.pay}">
                                                                    PAY: Rs.${eachData.pay}/- 
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                ${eachData.wefChange}
                                                            </td>
                                                            <td>
                                                                <c:if test="${not empty eachData.sbDescription}">
                                                                    ${eachData.sbDescription}<br /><hr />
                                                                </c:if>
                                                                <b style="color:#FF4500;">${eachData.sbLanguageRequested}</b>
                                                            </td>
                                                            <td>
                                                                <c:if test="${eachData.entrytype eq 'NEW'}">
                                                                    ${eachData.entrytype}
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${empty eachData.isSubmitted || eachData.isSubmitted eq 'N'}">
                                                                    <c:if test="${eachData.tabName eq 'INCREMENT'}">
                                                                        <a href="EmployeeEditIncrement.htm?mode=E&incrId=${eachData.moduleid}&notId=${eachData.noteIdEnc}" 
                                                                           data-remote="false" data-toggle="modal" title="Click to Open" 
                                                                           data-target="#openRequestModal"> <b style="color: #0000FF;"></b> Edit
                                                                        </a>
                                                                    </c:if>
                                                                    <c:if test="${eachData.tabName eq 'PAYFIXATION' || eachData.tabName eq 'PAYREVISION'}">
                                                                        <a href="EditPayFixationSBCorrection.htm?mode=E&notId=${eachData.noteIdEnc}" 
                                                                           data-remote="false" data-toggle="modal" title="Click to Open" 
                                                                           data-target="#openRequestModal"> <b style="color: #0000FF;"> Edit
                                                                        </a>
                                                                    </c:if>
                                                                    <c:if test="${eachData.tabName eq 'PROMOTION'}">
                                                                        <a href="EditPromotionSBCorrection.htm?mode=E&notId=${eachData.noteIdEnc}&promotionId=${eachData.moduleid}" 
                                                                           data-remote="false" data-toggle="modal" title="Click to Open" 
                                                                           data-target="#openRequestModal"> <b style="color: #0000FF;"> Edit
                                                                        </a>
                                                                    </c:if>
                                                                    <c:if test="${eachData.tabName eq 'RELIEVE'}">
                                                                        <a href="EditRelieveSBCorrection.htm?mode=E&notId=${eachData.noteIdEnc}&rlvId=${eachData.moduleid}" 
                                                                           data-remote="false" data-toggle="modal" title="Click to Open" 
                                                                           data-target="#openRequestModal"> <b style="color: #0000FF;"> Edit
                                                                        </a>
                                                                    </c:if>
                                                                    <c:if test="${eachData.tabName eq 'JOINING'}">
                                                                        <a href="EditJoiningSBCorrection.htm?mode=E&notId=${eachData.noteIdEnc}&rlvId=${eachData.moduleRefId}&jId=${eachData.moduleid}" 
                                                                           data-remote="false" data-toggle="modal" title="Click to Open" 
                                                                           data-target="#openRequestModal"> <b style="color: #0000FF;"> Edit
                                                                        </a>
                                                                    </c:if>
                                                                    <c:if test="${eachData.tabName eq 'SERVICE VERIFICATION CERTIFICATE'}">
                                                                        <a href="EditServiceVerifySBCorrection.htm?mode=E&txtsvid=${eachData.moduleid}" 
                                                                           data-remote="false" data-toggle="modal" title="Click to Open" 
                                                                           data-target="#openRequestModal"> <b style="color: #0000FF;"> Edit
                                                                        </a>
                                                                    </c:if>
                                                                </c:if>
                                                                <c:if test="${not empty eachData.isSubmitted && eachData.isSubmitted eq 'Y'}">
                                                                    <c:if test="${empty eachData.approvalStatus}">
                                                                        
                                                                    </c:if>
                                                                    <c:if test="${not empty eachData.approvalStatus}">
                                                                        
                                                                    </c:if>
                                                                </c:if>
                                                            </td>    
                                                        </tr>
                                                    </c:forEach>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div id="openRequestModal" class="modal fade" role="dialog">
                                <div class="modal-dialog" style="width:80%;">
                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title"> <b style="color:#FF4500;">${ModuleName}</b> </h4>
                                        </div>
                                        <div class="modal-body" style="overflow-y: auto;max-height: 500px;"> &nbsp; </div>
                                        <div class="modal-footer">
                                            <div id='preview'></div>
                                            <div class="col-md-4 form-group"> &nbsp; </div>
                                            <div class="col-md-4 form-group" align="center"></div>
                                            <div class="col-md-4 form-group"> &nbsp; </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <div id="AddNewOpenRequestModal" class="modal fade" role="dialog">
                                <div class="modal-dialog" style="width:80%;">
                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title"> <b style="color:#FF4500;">${ModuleName}</b> </h4>
                                        </div>
                                        <div class="modal-body" style="overflow-y: auto;max-height: 500px;"> &nbsp; </div>
                                        <div class="modal-footer">
                                            <div id='preview'></div>
                                            <div class="col-md-4 form-group"> &nbsp; </div>
                                            <div class="col-md-4 form-group" align="center"></div>
                                            <div class="col-md-4 form-group"> &nbsp; </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </form:form>
            </div>
            <%--<div style="float:left;width:calc( 40% - 0px );overflow-y: auto;" id="servicebookdiv">
                <div class="row">
                    <div class="col-lg-12">                            
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped" width="100%">
                                <thead class="tblTrColor">
                                    <tr>
                                        <th width="2%"> # </th> 
                                        <th width="20%"> POST/ CADRE/ SCALE of PAY </th>
                                        <th width="7%"> PAY </th> 
                                        <th width="10%"> WEF </th>                    
                                        <th width="45%"> Entry in the Service Book </th>
                                        <th width="20%"> Entry Type </th>
                                        <th width="5%"> Action </th>  
                                    </tr>
                                </thead>

                                <tbody id="servicebooktbody">
                                    <tr>
                                        <td colspan="7" class="loading">
                                            <div class="spinner"></div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>--%>
        </div>
    </body>
</html>
