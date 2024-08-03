<%-- 
    Document   : DeputationData
    Created on : 28 Apr, 2018, 10:34:40 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
            $(document).ready(function() {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtWEFrmDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtTillDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('.leavewefDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('.leavetillDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                //$('#hidNotifyingDeptCode').chosen();
                //$('#hidPostedDeptCode').chosen();
            });
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "A") {
                    deptcode = $('#hidNotifyingDeptCode').val();
                    $('#hidNotifyingOffCode').empty();
                    $('#hidNotiSpc').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                    $('#hidPostedSpc').empty();
                }
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                if (type == "A") {
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                }).done(function() {
                    if (type == "A") {
                        $('#hidNotifyingOffCode').chosen();
                        $("#hidNotifyingOffCode").trigger("chosen:updated");
                    } else if (type == "P") {
                        $('#hidPostedOffCode').chosen();
                        $("#hidPostedOffCode").trigger("chosen:updated");
                    }
                });
            }

            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "A") {
                    offcode = $('#hidNotifyingOffCode').val();
                    $('#hidNotiSpc').empty();
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#hidPostedSpc').empty();
                }
                var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                if (type == "A") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#hidNotiSpc').append('<option value="">--Select Post--</option>');
                } else if (type == "P") {
                    $('#hidPostedSpc').append('<option value="">--Select Post--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#hidNotiSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        }
                    });
                }).done(function() {
                    if (type == "A") {
                        $('#hidNotiSpc').chosen();
                        $("#hidNotiSpc").trigger("chosen:updated");
                    } else if (type == "P") {
                        $('#hidPostedSpc').chosen();
                        $("#hidPostedSpc").trigger("chosen:updated");
                    }
                });

                //Start Field Off Code
                if (type == "P") {
                    $('#sltFieldOffice').empty();
                    var url = 'transferGetFieldOffListJSON.htm?offcode=' + $('#hidPostedOffCode').val();
                    $('#sltFieldOffice').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltFieldOffice').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
                //End Field Off Code
            }

            function getPost(type) {
                if (type == "A") {
                    $('#notifyingSpc').val($('#hidNotiSpc option:selected').text());
                } else if (type == "P") {
                    $('#postedSpc').val($('#hidPostedSpc option:selected').text());
                }
            }

            function getSubCadreStatus() {
                $('#sltSubCadreStatus').empty();
                var url = 'deputationGetSubCadreStatusListJSON.htm?cadrestat=' + $('#sltCadreStatus').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltSubCadreStatus').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
            function saveCheck() {
                var txtNotOrdNo = $("#txtNotOrdNo").val();
                var txtNotOrdDt = $("#txtNotOrdDt").val();
                if (txtNotOrdNo == "") {
                    alert("Please Enter Notification Order No");
                    return false;
                }
                if (txtNotOrdDt == "") {
                    alert("Please Select Notification Order Date");
                    return false;
                }
                var notifyingSpc = $("#notifyingSpc").val();
                if (notifyingSpc == "") {
                    alert("Please Select Notifying Authority");
                    return false;
                }
                var txtWEFrmDt = $("#txtWEFrmDt").val();
                var sltWEFrmTime = $("#txtWEFrmDt").val();
                if (txtWEFrmDt == "") {
                    alert("Please Select With Effect From");
                    return false;
                }
                if (sltWEFrmTime == "") {
                    alert("Please Select With Effect Time");
                    return false;
                }
                var txtTillDt = $("#txtTillDt").val();
                var sltTillTime = $("#sltTillTime").val();
                if (txtTillDt == "") {
                    alert("Please Select Till Date");
                    return false;
                }
                if (sltTillTime == "") {
                    alert("Please Select Till Time");
                    return false;
                }
                // alert("HI");
                return true;
            }
            function saveLeaveCheck(ids, agtype) {
                var leavewefDate = $("#leavewefDate" + ids).val();
                var leavetillDate = $("#leavetillDate" + ids).val();
                var leaveAmount = $("#leaveAmount" + ids).val();
                if (leavewefDate == "") {
                    alert("Please Select " + agtype + " Date");
                    return false;
                }
                if (leavetillDate == "") {
                    alert("Please Select  " + agtype + " Till Date");
                    return false;
                }
                if (leaveAmount == "") {
                    alert("Please Enter Leave Amount");
                    return false;
                }
                if (isNaN(leaveAmount)) {
                    alert("Invalid " + agtype + " Amount");
                    return false;
                }

            }
            function delete_conId(cid, nid) {
                var mess = confirm('Are you sure to delete?');
                if (mess) {
                    window.location = "deleteAGDeputation.htm?cId=" + cid + "&notId=" + nid;
                }
            }
            
            function openNotifyingModal(){
                var orgType = $('input[name="radpostingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#deputationAuthorityModal").modal("show");
                } else if (orgType == 'GOI') {
                    $("#deputationOtherOrgModal").modal("show");
                }
            }
            
            function getOtherOrgPost() {
                $('#notifyingSpc').val($('#hidNotifyingOthSpc option:selected').text());
                
                $('#hidNotifyingDeptCode').val('');
                $('#hidNotifyingOffCode').val('');
                $('#hidNotiSpc').val('');

                $("#deputationOtherOrgModal").modal("hide");
            }
        </script>
    </head>
    <body>

        <form:form action="saveAGDeputation.htm" method="post" commandName="deputationForm">

            <form:hidden path="hidNotId"/>
            <form:hidden path="depId"/>
            <form:hidden path="hidTransferId"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee AG Endorsement Deputation
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <div class="row" style="margin-bottom: 7px;">
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
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt"> Notification Order Date<span style="color: red">*</span></label>
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
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="radpostingauthtype">Details of Notifying Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>
                                
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingSpc" id="notifyingSpc" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openNotifyingModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>


                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="depuDetails"> Deputation Period Details </label>
                            </div>
                        </div>  

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="chkExtnDptnPrd"> If Extension of Deputation Period </label>
                            </div>
                            <div class="col-lg-2">
                                <form:checkbox path="chkExtnDptnPrd" id="chkExtnDptnPrd" value=""/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtWEFrmDt">With Effect From<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtWEFrmDt" id="txtWEFrmDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="Time">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select class="form-control" path="sltWEFrmTime" id="sltWEFrmTime">
                                    <form:option value="">-Select-</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>  
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="fieldoffice">Till Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtTillDt" id="txtTillDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="Time">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select class="form-control" path="sltTillTime" id="sltTillTime">
                                    <form:option value="">-Select-</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>  
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note"> Note</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>

                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save AG Endorsement Deputation" class="btn btn-default" onclick="return saveCheck();"/>
                        <input type="submit" name="action" value="Back to AG Endorsement List Page" class="btn btn-default"/>
                        <c:if test="${not empty deputationForm.depId}">
                            <!--   <button type="button" name="action" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');">Delete</button>
                            -->
                        </c:if>
                    </div>
                </div>
            </div>


            <div id="deputationAuthorityModal" class="modal" role="dialog">
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
            
            <div id="deputationOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidNotifyingOthSpc">Notified By</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOthSpc" id="hidNotifyingOthSpc" class="form-control" onchange="getOtherOrgPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgOfflist}" itemValue="value" itemLabel="label"/>
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
        </form:form>
        <div class="container-fluid">            
            <c:if test="${ deputationForm.hidNotId > 0}">  

                <form action="saveLeaveContribution.htm" method="POST" commandName="deputationForm" onsubmit="return saveLeaveCheck(1, 'Leave Salary Contribution');" >             
                    <input type='hidden' name='hidNotId' value="${deputationForm.hidNotId}"/>
                    <input type='hidden' name='conType' value="L"/>
                    <input type='hidden' name='empid' value="${empid}"/>                    
                    <div class="panel panel-success ">
                        <div class="panel-heading"><strong>Leave Salary Contribution</strong></div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Slno</th>
                                        <th >WEF Date</th>
                                        <th >Till Date</th>
                                        <th >Amount</th> 
                                        <th >Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="slno" value="${0}" />
                                    <c:forEach items="${dptnformleave}" var="dlist">

                                        <c:if test="${dlist.conType=='L'}">
                                            <c:set var="slno" value="${slno +1}" />
                                            <tr>
                                                <td>${slno}</td>
                                                <td>${dlist.leavewefDate}</td>
                                                <td>${dlist.leavetillDate}</td>                                       
                                                <td>${dlist.leaveAmount}</td>

                                                <td><a href="#" onclick="delete_conId(${dlist.hidconId},${dlist.hidNotId})" >Delete</a></td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>

                                </tbody>
                            </table>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label for="leavewefdate">WEF Date</label>
                                </div>
                                <div class="col-lg-2">  

                                    <div class="input-group date" id="processDate1">
                                        <input type="text" class="form-control leavewefDate" name="leavewefDate" id="leavewefDate1" readonly="true"/>                                      
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div> 

                                </div>
                                <div class="col-lg-1">
                                    <label for="leavetillDate">Till Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate2">
                                        <input type="text" class="form-control leavetillDate" name="leavetillDate" id="leavetillDate1" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                                <div class="col-lg-1">
                                    <label for="leaveAmount">Amount<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">   
                                    <input type="text" class="form-control " name="leaveAmount" id="leaveAmount1" maxlength="10"/>

                                </div>
                                <div class="col-lg-2">
                                    <input type="submit" name="action" value="Save" class="btn btn-danger" />
                                </div>
                            </div>   


                        </div>
                    </div>  
                </form>

                <form action="saveLeaveContribution.htm" method="POST" commandName="deputationForm" onsubmit="return saveLeaveCheck(2, 'Pension Contribution');" >             
                    <input type='hidden' name='hidNotId' value="${deputationForm.hidNotId}"/>
                    <input type='hidden' name='conType' value="P"/>
                    <input type='hidden' name='empid' value="${empid}"/>                    
                    <div class="panel panel-info">
                        <div class="panel-heading"><strong>Pension Contribution</strong></div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Slno</th>
                                        <th >WEF Date</th>
                                        <th >Till Date</th>
                                        <th >Amount</th> 
                                        <th >Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="slno" value="${0}" />   
                                    <c:forEach items="${dptnformleave}" var="dlist">

                                        <c:if test="${dlist.conType=='P'}">
                                            <c:set var="slno" value="${slno +1}" />   
                                            <tr>
                                                <td>${slno}</td>
                                                <td>${dlist.leavewefDate}</td>
                                                <td>${dlist.leavetillDate}</td>                                       
                                                <td>${dlist.leaveAmount}</td>

                                                <td><a href="#" onclick="delete_conId(${dlist.hidconId},${dlist.hidNotId})">Delete</a></td>
                                            </tr>
                                        </c:if>  
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label for="leavewefdate">WEF Date</label>
                                </div>
                                <div class="col-lg-2">  

                                    <div class="input-group date" id="processDate1">
                                        <input type="text" class="form-control leavewefDate" name="leavewefDate" id="leavewefDate2" readonly="true"/>                                      
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div> 

                                </div>
                                <div class="col-lg-1">
                                    <label for="leavetillDate">Till Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate2">
                                        <input type="text" class="form-control leavetillDate" name="leavetillDate" id="leavetillDate2" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                                <div class="col-lg-1">
                                    <label for="leaveAmount">Amount<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">   
                                    <input type="text" class="form-control" name="leaveAmount" id="leaveAmount2" maxlength="10" />

                                </div>
                                <div class="col-lg-2">
                                    <input type="submit" name="action" value="Save" class="btn btn-danger" />
                                </div>
                            </div>   


                        </div>
                    </div>  
                </form> 

                <form action="saveLeaveContribution.htm" method="POST" commandName="deputationForm" onsubmit="return saveLeaveCheck(3, 'Pay History');" >             
                    <input type='hidden' name='hidNotId' value="${deputationForm.hidNotId}"/>
                    <input type='hidden' name='conType' value="Y"/>
                    <input type='hidden' name='empid' value="${empid}"/>                    
                    <div class="panel panel-success ">
                        <div class="panel-heading"><strong>Pay History (Per Month)</strong></div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Slno</th>
                                        <th >WEF Date</th>
                                        <th >Till Date</th>
                                        <th >Amount</th> 
                                        <th >Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="slno" value="${0}" />   
                                    <c:forEach items="${dptnformleave}" var="dlist">

                                        <c:if test="${dlist.conType=='Y'}">
                                            <c:set var="slno" value="${slno +1}" />
                                            <tr>
                                                <td>${slno}</td>
                                                <td>${dlist.leavewefDate}</td>
                                                <td>${dlist.leavetillDate}</td>                                       
                                                <td>${dlist.leaveAmount}</td>

                                                <td><a href="#" onclick="delete_conId(${dlist.hidconId},${dlist.hidNotId})">Delete</a></td>
                                            </tr>
                                        </c:if>  
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label for="leavewefdate"><strong>WEF Date</strong></label>
                                </div>
                                <div class="col-lg-2">  

                                    <div class="input-group date" id="processDate1">
                                        <input type="text" class="form-control leavewefDate" name="leavewefDate" id="leavewefDate3" readonly="true"/>                                      
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div> 

                                </div>
                                <div class="col-lg-1">
                                    <label for="leavetillDate">Till Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate2">
                                        <input type="text" class="form-control leavetillDate" name="leavetillDate" id="leavetillDate3" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                                <div class="col-lg-1">
                                    <label for="leaveAmount">Amount<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">   
                                    <input type="text" class="form-control" name="leaveAmount" id="leaveAmount3" maxlength="10"/>

                                </div>
                                <div class="col-lg-2">
                                    <input type="submit" name="action" value="Save" class="btn btn-danger" />
                                </div>
                            </div>   


                        </div>
                    </div>  
                </form> 

                <form action="saveLeaveContribution.htm" method="POST" commandName="deputationForm" onsubmit="return saveLeaveCheck(4, 'Special Pay');" >             
                    <input type='hidden' name='hidNotId' value="${deputationForm.hidNotId}"/>
                    <input type='hidden' name='conType' value="SP"/>
                    <input type='hidden' name='empid' value="${empid}"/>                    
                    <div class="panel panel-info">
                        <div class="panel-heading"><strong>Special Pay</strong></div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Slno</th>
                                        <th >WEF Date</th>
                                        <th >Till Date</th>
                                        <th >Amount</th> 
                                        <th >Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="slno" value="${0}" />   
                                    <c:forEach items="${dptnformleave}" var="dlist">

                                        <c:if test="${dlist.conType=='SP'}">
                                            <c:set var="slno" value="${slno +1}" />
                                            <tr>
                                                <td>${slno}</td>
                                                <td>${dlist.leavewefDate}</td>
                                                <td>${dlist.leavetillDate}</td>                                       
                                                <td>${dlist.leaveAmount}</td>

                                                <td><a href="#" onclick="delete_conId(${dlist.hidconId},${dlist.hidNotId})">Delete</a></td>
                                            </tr>
                                        </c:if>  
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label for="leavewefdate">WEF Date</label>
                                </div>
                                <div class="col-lg-2">  

                                    <div class="input-group date" id="processDate1">
                                        <input type="text" class="form-control leavewefDate" name="leavewefDate" id="leavewefDate4" readonly="true"/>                                      
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div> 

                                </div>
                                <div class="col-lg-1">
                                    <label for="leavetillDate">Till Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate2">
                                        <input type="text" class="form-control leavetillDate" name="leavetillDate" id="leavetillDate4" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                                <div class="col-lg-1">
                                    <label for="leaveAmount">Amount<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">   
                                    <input type="text" class="form-control" name="leaveAmount" id="leaveAmount4" maxlength="10"/>

                                </div>
                                <div class="col-lg-2">
                                    <input type="submit" name="action" value="Save" class="btn btn-danger" />
                                </div>
                            </div>   


                        </div>
                    </div>  
                </form>

                <form action="saveLeaveContribution.htm" method="POST" commandName="deputationForm" onsubmit="return saveLeaveCheck(5, 'Personal Pay');" >             
                    <input type='hidden' name='hidNotId' value="${deputationForm.hidNotId}"/>
                    <input type='hidden' name='conType' value="PP"/>
                    <input type='hidden' name='empid' value="${empid}"/>                    
                    <div class="panel panel-success ">
                        <div class="panel-heading"><strong>Personal Pay</strong></div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Slno</th>
                                        <th >WEF Date</th>
                                        <th >Till Date</th>
                                        <th >Amount</th> 
                                        <th >Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="slno" value="${0}" />   
                                    <c:forEach items="${dptnformleave}" var="dlist">

                                        <c:if test="${dlist.conType=='PP'}">
                                            <c:set var="slno" value="${slno +1}" />
                                            <tr>
                                                <td>${slno}</td>
                                                <td>${dlist.leavewefDate}</td>
                                                <td>${dlist.leavetillDate}</td>                                       
                                                <td>${dlist.leaveAmount}</td>

                                                <td><a href="#" onclick="delete_conId(${dlist.hidconId},${dlist.hidNotId})">Delete</a></td>
                                            </tr>
                                        </c:if>  
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label for="leavewefdate">WEF Date</label>
                                </div>
                                <div class="col-lg-2">  

                                    <div class="input-group date" id="processDate1">
                                        <input type="text" class="form-control leavewefDate" name="leavewefDate" id="leavewefDate5" readonly="true"/>                                      
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div> 

                                </div>
                                <div class="col-lg-1">
                                    <label for="leavetillDate">Till Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate2">
                                        <input type="text" class="form-control leavetillDate" name="leavetillDate" id="leavetillDate5" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                                <div class="col-lg-1">
                                    <label for="leaveAmount">Amount<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">   
                                    <input type="text" class="form-control" name="leaveAmount" id="leaveAmount5" maxlength="10"/>

                                </div>
                                <div class="col-lg-2">
                                    <input type="submit" name="action" value="Save" class="btn btn-danger" />
                                </div>
                            </div>   


                        </div>
                    </div>  
                </form> 
                <form action="saveLeaveContribution.htm" method="POST" commandName="deputationForm" onsubmit="return saveLeaveCheck(6, 'Other Pay');" >             
                    <input type='hidden' name='hidNotId' value="${deputationForm.hidNotId}"/>
                    <input type='hidden' name='conType' value="OP"/>
                    <input type='hidden' name='empid' value="${empid}"/>                    
                    <div class="panel panel-info">
                        <div class="panel-heading"><strong>Other Pay</strong></div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Slno</th>
                                        <th >WEF Date</th>
                                        <th >Till Date</th>
                                        <th >Amount</th> 
                                        <th >Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="slno" value="${0}" />   
                                    <c:forEach items="${dptnformleave}" var="dlist">

                                        <c:if test="${dlist.conType=='OP'}">
                                            <c:set var="slno" value="${slno +1}" />
                                            <tr>
                                                <td>${slno}</td>
                                                <td>${dlist.leavewefDate}</td>
                                                <td>${dlist.leavetillDate}</td>                                       
                                                <td>${dlist.leaveAmount}</td>

                                                <td><a href="#" onclick="delete_conId(${dlist.hidconId},${dlist.hidNotId})">Delete</a></td>
                                            </tr>
                                        </c:if>  
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label for="leavewefdate">WEF Date</label>
                                </div>
                                <div class="col-lg-2">  

                                    <div class="input-group date" id="processDate1">
                                        <input type="text" class="form-control leavewefDate" name="leavewefDate" id="leavewefDate6" readonly="true"/>                                      
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div> 

                                </div>
                                <div class="col-lg-1">
                                    <label for="leavetillDate">Till Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate2">
                                        <input type="text" class="form-control leavetillDate" name="leavetillDate" id="leavetillDate6" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                                <div class="col-lg-1">
                                    <label for="leaveAmount">Amount<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">   
                                    <input type="text" class="form-control" name="leaveAmount" id="leaveAmount6" maxlength="10"/>

                                </div>
                                <div class="col-lg-2">
                                    <input type="submit" name="action" value="Save" class="btn btn-danger" />
                                </div>
                            </div>   


                        </div>
                    </div>  
                </form>                    


            </c:if>   

        </div>    
    </body>
</html>
