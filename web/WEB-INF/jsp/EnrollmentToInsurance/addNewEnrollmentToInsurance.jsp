<%-- 
    Document   : addNewEnrollmentToInsurance
    Created on : 13 Dec, 2021, 3:36:26 PM
    Author     : Devi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
             $(document).ready(function () {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#witheffectdate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });             
            });       
            
            function openEnrollmentNotifyingModal() {
                var orgType = $('input[name="radnotifyingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#enrollmentnAuthorityModalGOO").modal("show");
                } else if (orgType == 'GOI') {
                    $("#enrollmentNotifyingGOIModal").modal("show");
                }
            }
            
            function getNotifyingOtherOrgPost() {
                $("#notifyingSpc").val($('#hidNotifyingOthSpc option:selected').text());
                $("#enrollmentNotifyingGOIModal").modal("hide");
            }
            
             function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "A") {
                    deptcode = $('#hidNotifyingDeptCode').val();
                    $('#hidNotifyingOffCode').empty();
                    $('#hidNotiSpc').empty();
                } 
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                
                var url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                } else {
                    url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                }
                
                if (type == "A") {
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                } 
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "A") {
                            $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } 
                    });
                }).done(function () {
                    if (type == "A") {
                        $('#hidNotifyingOffCode').chosen();
                        $("#hidNotifyingOffCode").trigger("chosen:updated");
                    } 
                });
            }
            
            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "A") {
                    offcode = $('#hidNotifyingOffCode').val();
                    $('#hidNotiSpc').empty();
                } 
                var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                if (type == "A") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#hidNotiSpc').append('<option value="">--Select Post--</option>');
                } 
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "A") {
                            $('#hidNotiSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } 
                    });
                }).done(function () {
                    if (type == "A") {
                        $('#hidNotiSpc').chosen();
                        $("#hidNotiSpc").trigger("chosen:updated");
                    } 
                });       
            }
             function getPost(type) {
                if (type == "A") {
                    $('#notifyingSpc').val($('#hidNotiSpc option:selected').text());
                } 
            }
            
             function saveCheck() {          
                if ($('#txtNotOrdNo').val() == '') {
                    alert('Notification Order No should not be blank.');
                    $('#txtNotOrdNo').focus();
                    return false;
                }

                if ($('#txtNotOrdDt').val() == '') {
                    alert('Notification Order Date should not be blank.');
                    $('#txtNotOrdDt').focus();
                    return false;
                }

                if ($('#schemename').val() == '') {
                    alert('Scheme Name should not be blank.');
                    $('#schemename').focus();
                    return false;
                }

                if ($('#insaccountno').val() == '') {
                    alert('Insurance Account Number should not be blank.');
                    $('#insaccountno').focus();
                    return false;
                }

                if ($('#witheffectdate').val() == '') {
                    alert('Select With Effect Date.');
                    $('#witheffectdate').focus();
                    return false;
                }

                if ($('#subamount').val() == '') {
                    alert('Monthly Subscription Amount should not be blank.');
                    $('#subamount').focus();
                    return false;
                }
            }

            
        </script>
        <style>
            .row-margin{
                margin-bottom: 20px;
            }
        </style>
        
    </head>
    <body>
        <form:form action="saveEnrollment.htm" method="post" commandName="enrollmentForm">
             <form:hidden path="hidNotId"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                       Add New Enrollment
                    </div>
                    <div class="panel-body">
                         <form:hidden path="empid" id="empid"/>
                         <form:hidden path="hidschemeid" id="hidschemeid"/>
                          <form:hidden path="schemetype" id="schemetype"/>
                       <div class="row row-margin">
                            <div class="col-lg-4">
                                <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/> 
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-2"></div>
                        </div>
                            
                        <div class="row row-margin" >
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt"> Notification Order Date <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtNotOrdDt" id="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                                    
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="radnotifyingauthtype">Details of Notifying Authority </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO" onclick="toggleOrganisationType()" id="notifyingGOO"/> 
                                <label for="notifyingGOO"> Government of Odisha </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOI" onclick="toggleOrganisationType()" id="notifyingGOI"/> 
                                <label for="notifyingGOI"> Government of India </label>
                            </div>                         
                        </div>    
                                
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-6">
                                <form:input class="form-control" path="notifyingSpc" id="notifyingSpc" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openEnrollmentNotifyingModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>  
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="scheme">Name of Scheme <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-2">
                                    <form:select class="form-control" path="schemename" id="schemename">
                                        <form:option value="" label="Select"/>
                                        <form:options items="${schemelist}" itemLabel="label" itemValue="value"/>  
                                    </form:select> 
                             </div>
                        </div>   
                            
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="accountno">Insurance Account Number <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-2">
                                    <form:input cssClass="form-control" path="insaccountno" id="insaccountno" />
                             </div>
                        </div>  
                         <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="effectdate">With Effect Date <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-2">
                                   <div class="input-group date">
                                    <form:input class="form-control" path="witheffectdate" id="witheffectdate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>    
                             </div>
                        </div>  
                                    
                        <div class="row row-margin" >
                            <div class="col-lg-2">
                                <label for="subamount">Monthly Subscription Amount <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-2">
                                    <form:input cssClass="form-control" path="subamount" id="subamount" />
                             </div>
                        </div>   
                             
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="note">Note(If any)</label>
                            </div>
                            <div class="col-sm-6">
                                    <form:textarea Class="form-control" path="note" id="note" />
                             </div>
                        </div>         
                            
                    </div>
                    <div class="panel-footer">
                             <button type="submit" class="btn btn-primary" name="action" value="Save Enrollment" onclick="return saveCheck()">Save</button>  
                              <a href="EnrollmentToInsurance.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                    </div>
                </div>
            </div>
                        
                 <div id="enrollmentnAuthorityModalGOO" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="getDeptWiseOfficeList('A');">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWisePostList('A');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${sancOfflist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotiSpc" id="hidNotiSpc" class="form-control" onchange="getPost('A');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${sancPostlist}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
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
                        
              <div id="enrollmentNotifyingGOIModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingOthSpc">Authority</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOthSpc" id="hidNotifyingOthSpc" class="form-control" onchange="getNotifyingOtherOrgPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgfflist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
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
