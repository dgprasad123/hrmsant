
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
            $(document).ready(function () {
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
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                if (type == "A") {
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "A") {
                            $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                }).done(function () {
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
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "A") {
                            $('#hidNotiSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        }
                    });
                }).done(function () {
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
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
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
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
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
                
                if($('#chkHomeTown1').checked == false && $('#chkHomeTown2').checked == false)
                {
                    alert("Please check the Type.");
                    return false;
                }
                if($('#fromDate').val() == '')
                {
                    alert('Please select From Date.');
                    return false;
                }
                if($('#toDate').val() == '')
                {
                    alert('Please select To Date.');
                    return false;
                }
                if($('#fblYear').val() == '')
                {
                    alert("Please select Block Period From.");
                    return false;
                }
                if($('#tblYear').val() == '')
                {
                    alert("Please select Block Period To.");
                    return false;
                }
            }
            function saveLeaveCheck(ids) {
                var leavewefDate = $("#leavewefDate"+ids).val();
                var leavetillDate = $("#leavetillDate"+ids).val();
                var leaveAmount = $("#leaveAmount"+ids).val();
                if (leavewefDate == "") {
                    alert("Please Select  Leave Salary Contribution Date");
                    return false;
                }
                if (leavetillDate == "") {
                    alert("Please Select  Leave Salary Contribution Till Date");
                    return false;
                }
                if (leaveAmount == "") {
                    alert("Please Enter Leave Amount");
                    return false;
                }
                if(isNaN(leaveAmount)){
                    alert("Invalid Leave Amount");
                    return false;
                }

            }
            function delete_conId(cid,nid){
                var mess=confirm('Are you sure to delete?');
                if(mess){
                    window.location="deleteAGDeputation.htm?cId="+cid+"&notId="+nid;
                }
            }
        </script>
    </head>
    <body>

        <form:form action="UpdateLTCEntry.htm" method="post" commandName="sLTCBean">

            <form:hidden path="hidNotId"/>
            <form:hidden path="ltcId"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" style="color:#1C6CB7;font-weight:bold;font-size:14pt;">
                        Employee Leave Travel Concession
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <div class="row" style="margin-bottom: 7px;">
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
                            <div class="col-lg-2">
                                <label for="notifyingSpc">Details of Notifying Authority</label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingSpc" id="notifyingSpc" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#deputationAuthorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
 

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="chkHomeTown">Type</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="chkHomeTown" id="chkHomeTown1" value="H" /> <label for="chkHomeTown1">Home</label>
                                <form:radiobutton path="chkHomeTown" id="chkHomeTown2" value="A" /> <label for="chkHomeTown2">All India</label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtWEFrmDt">From Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="fromDate" id="txtWEFrmDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="fieldoffice">To Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="toDate" id="txtTillDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>                                    

                        </div>  
                        <div class="row" style="margin-bottom: 7px;">

                            <div class="col-lg-2">
                                <label for="Time">Block Period From:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select class="form-control" path="fblYear" id="fblYear">
                                    <form:option value="">-Select-</form:option>
                                    <c:forEach var = "i" begin = "${currentYear-50}" end = "${currentYear}">
                                    <form:option value="${i}">${i}</form:option>    
                                    </c:forEach>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="Time">Block Period To:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select class="form-control" path="tblYear" id="tblYear">
                                    <form:option value="">-Select-</form:option>
                                    <c:forEach var = "i" begin = "${currentYear-50}" end = "${currentYear}">
                                    <form:option value="${i}">${i}</form:option>    
                                    </c:forEach>
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
                        <input type="submit" name="action" value="Update LTC" class="btn btn-default" onclick="return saveCheck();"/>
                        <input type="submit" name="action" value="Back to LTC List" class="btn btn-default"/>

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

            <div id="deputationPostedModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Posted Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Posted Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDeptCode" id="hidPostedDeptCode" class="form-control" onchange="getDeptWiseOfficeList('P');">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Posted Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWisePostList('P');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${postedOffList}" itemValue="offCode" itemLabel="offName"/>
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
                                    <form:select path="hidPostedSpc" id="hidPostedSpc" class="form-control" onchange="getPost('P');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${postedPostList}" itemValue="spc" itemLabel="postname"/>
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
        
                          
                                    
 

        </div>    
    </body>
</html>
