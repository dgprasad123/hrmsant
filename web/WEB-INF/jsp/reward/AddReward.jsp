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
                showHideMoneyReward();
            });

            function getDeptWiseOfficeList(type) {
                var deptcode = $('#hidAuthDeptCode').val();
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode = $('#hidAuthOffCode').val();
                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#authSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost(type) {
                $('#authPostName').val($('#authSpc option:selected').text());
            }

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

            function showHideMoneyReward() {
                if ($("#rewardType").val() == "01" || $("#rewardType").val() == "13" || $("#rewardType").val() == "17") {
                    $("#moneyReward").attr("readonly", false);
                } else {
                    $("#moneyReward").attr("readonly", true);
                    $("#moneyReward").val("0");
                }
            }

            function saveCheck() {
                if ($('#txtNotOrdNo').val() == "") {
                    alert("Please enter Order No");
                    $('#txtNotOrdNo').focus();
                    return false;
                }
                if ($('#txtNotOrdDt').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#authPostName').val() == "") {
                    alert("Please select Sanctioning Authority");
                    return false;
                }

                return true;
            }

            function deleteRewardAttachment(notid) {
                if(confirm('Are you sure to Delete this Attachment?')){
                    $.ajax({
                        type: "POST",
                        data: {notId: notid},
                        url: "deleteRewardAttachment.htm",
                        dataType: "json",
                        success: function (data) {
                            //alert(data.deletestatus);
                            if(data.deletestatus == 1){
                                alert("Attachment Deleted");
                                $("#attachfile").hide();
                                $("#uploadfile").show();
                            }else{
                                alert("Deletion Failed");
                            }
                        }
                    });
                }
            }
            
            function openAuthorityModal(){
                var orgType = $('input[name="radpostingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#authorityModal").modal("show");
                } else if (orgType == 'GOI') {
                    $("#rewardOtherOrgModal").modal("show");
                }
            }
            
            function getOtherOrgPost() {
                $('#authPostName').val($('#hidOthSpc option:selected').text());

                $('#hidAuthDeptCode').val('');
                $('#hidAuthOffCode').val('');

                $("#rewardOtherOrgModal").modal("hide");
            }
        </script>
    </head>
    <body>
        <form:form action="saveReward.htm" method="post" commandName="rewardForm" enctype="multipart/form-data">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Reward
                    </div>
                    <div class="panel-body">
                        <form:hidden path="rewardId" id="rewardId" value="${rewardForm.rewardId}"/>
                        <form:hidden path="empid" id="empid" value="${rewardForm.empid}"/>                       
                        <form:hidden path="hnotid" id="hnotid" value="${rewardForm.hnotid}"/>                       

                        <form:hidden path="hidTempAuthOffCode" id="hidTempAuthOffCode"/>
                        <form:hidden path="hidTempAuthPost" id="hidTempAuthPost"/>

                        <form:hidden path="hidTempPostedOffCode" id="hidTempPostedOffCode"/>
                        <form:hidden path="hidTempPostedPost" id="hidTempPostedPost"/>
                        <form:hidden path="hidTempPostedFieldOffCode" id="hidTempPostedFieldOffCode"/>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-1" >
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo" maxlength="50"/>
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
                                <label for="radpostingauthtype">Sanctioning Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                            <div class="col-lg-5"></div>
                        </div>
                                
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="authPostName" id="authPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <%--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#authorityModal">--%>
                                <button type="button" class="btn btn-primary" onclick="openAuthorityModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
                                
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="rewardLevel">Reward Level</label>
                            </div>
                            <div class="col-lg-10">
                                <form:select path="rewardLevel" id="rewardLevel" class="form-control">
                                    <option value="">-Select-</option>
                                    <form:option value="DISTRICT">DISTRICT</form:option>
                                    <form:option value="NATIONAL">NATIONAL</form:option>
                                    <form:option value="STATE">STATE</form:option>
                                    <form:option value="BATTALION">BATTALION</form:option>
                                    <form:option value="OTHER">OTHER</form:option>
                                </form:select>
                            </div>
                        </div>
                                
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="rewardType">Reward Type</label>
                            </div>
                            <div class="col-lg-9">
                                <form:select path="rewardType" id="rewardType" class="form-control" onchange="showHideMoneyReward();">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${empRewardTypeList}" itemLabel="label" itemValue="value"/> 
                                </form:select>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                                
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="moneyReward">Cash Amount</label>
                            </div>
                            <div class="col-lg-3">
                                <form:input path="moneyReward" id="moneyReward" class="form-control" maxlength="10" readonly="true" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7"></div>
                        </div>
                            
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Good Work Done Note(if any)</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note" maxlength="2000"/>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                            
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-9">
                                <span style="color: red">Please Enter only 2000 characters</span>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                            
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="appreciationAttch">Appreciation</label>
                            </div>
                            <div class="col-lg-3">
                                <c:if test="${not empty rewardForm.originalFileNameAppreciationAttch}">
                                    <span id="attachfile">
                                        <a href="downloadAttachedDocumentCopy.htm?notid=${rewardForm.hnotid}" target="_blank">${rewardForm.originalFileNameAppreciationAttch}</a>&nbsp;&nbsp;&nbsp;
                                        <a href="javascript:deleteRewardAttachment('${rewardForm.hnotid}');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                    </span>
                                    <span id="uploadfile" style="display:none;">
                                        <input type="file" name="appreciationAttch" id="appreciationAttch"/>
                                    </span>
                                </c:if>
                                <c:if test="${empty rewardForm.originalFileNameAppreciationAttch}">
                                    <input type="file" name="appreciationAttch" id="appreciationAttch"/>
                                </c:if>
                            </div>
                            <div class="col-lg-7"></div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <a href="RewardList.htm"><button type="button" class="btn btn-primary">Back</button></a>
                        <c:if test="${not empty rewardForm.hnotid}">
                            <button type="submit" name="btnReward" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?');">Delete</button>
                        </c:if>
                        <button type="submit" name="btnReward" value="Save" class="btn btn-success" onclick="return saveCheck();">Save Reward</button>
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
                                    <label for="hidAuthDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList('N');">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptArray}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList('N');">
                                        <option value="">--Select Office--</option>
                                        <form:options items="${offArray}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authSpc">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authSpc" id="authSpc" class="form-control" onchange="getPost('N');">
                                        <option value="">--Select Post--</option>
                                        <form:options items="${authArray}" itemValue="spc" itemLabel="postname"/>
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
            
            <div id="rewardOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Select Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidOthSpc">Sanctioned By</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidOthSpc" id="hidOthSpc" class="form-control" onchange="getOtherOrgPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgOfflist}" itemValue="value" itemLabel="label"/>
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
    <script type="text/javascript">
        $(function() {
            $('#txtNotOrdDt').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
            /*  $('#txtWEFDt').datetimepicker({
             format: 'D-MMM-YYYY',
             useCurrent: false,
             ignoreReadonly: true
             });*/
        });
    </script>
</html>
