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

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#spCategoryID').hide();
                $('#orderdate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                // $('#spCategoryID').prop("disabled",true);
                //document.getElementById("spCategoryID").disabled = true;
            });
            function getDepartmentWiseOfficeList() {
                var deptcode = $('#deptcode').val();

                $('#offCode').empty();
                $('#offCode').append('<option value="">--Select Office--</option>');

                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#offCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList() {

                var offcode = $('#offCode').val();
                $('#sanctionauthority').empty();
                $('#sanctionauthority').append('<option value="">--Select Post--</option>');
                var url = "getOfficeWithSPCList.htm?offcode=" + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sanctionauthority').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                    });
                });
            }
            function saveCheck() {
                var trchecked = $("input[name=checkEmployed]:checked").val();
                if ($('#orderno').val() == "") {
                    alert("Please enter Order No");
                    return false;
                }
                if ($('#orderdate').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }



                if ($("input[name=radnotifyingauthtype]:checked").length == 0) {
                    alert("Please select Notifying Authority type");
                    return false;
                }
                if (trchecked == 'Y') {
                    if ($('#reservationCategory').val() == "") {
                        alert("Please enter Reservation Category");
                        return false;
                    }
                }
                if ($('#deptcode').val() == "") {
                    alert("Please enter Notifying Department");
                    return false;
                }
                if ($('#offCode').val() == "") {
                    alert("Please enter Notifying Office");
                    return false;
                }
                if ($('#sanctionauthority').val() == "") {
                    alert("Please enter Sanctioning Authority ");
                    return false;
                }
                if ($('#reservationCategory').val() == "") {
                    alert("Please enter Reservation Category ");
                    return false;
                }

            }
            function changecategory() {
                if ($('#reservationCategory').val() == 'SC/PWD' || $('#reservationCategory').val() == 'PH/PWD' || $(reservationCategory).val() == 'GENERAL (PWD)') {
                    $('#spCategoryID').show();
                } else {
                    $('#spCategoryID').hide();
                }
            }
        </script>
    </head>
    <body>
        <form:form action="SaveReservationCategory.htm" method="POST" commandName="reservedCategory">
            <form:hidden path="hidnotid"/>

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4>Reservation Category Information</h4>
                    </div>
                    <br/>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-1"></div>
                        <div class="col-lg-3">
                            <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                        </div>
                        <div class="col-lg-2">   
                            <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/> 
                        </div>
                        <div class="col-lg-3"></div>
                        <div class="col-lg-2"></div>
                    </div>
                    <%--<div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-4">
                            <label for="rdTransaction">Select type of Transaction</label>
                        </div>
                        <div class="col-lg-3">   
                            <form:radiobutton path="rdTransaction" id="rdTransaction" value="S" onclick="removePostingData();"/> Service Book Entry(Backlog)
                        </div>
                        <div class="col-lg-3">
                            <form:radiobutton path="rdTransaction" id="rdTransaction" value="C" onclick="removePostingData();"/> Current Transaction(will effect current Pay or Post)
                        </div>
                        <div class="col-lg-2"></div>
                    </div>--%>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">1. Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="orderno" id="orderno"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt">2. Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="orderdate" id="orderdate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">

                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-5">
                                <label for="detailsnotifyingauthority">3. Details of Notifying Authority<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-5">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">

                            <div class="col-lg-2">
                                <label for="detailsnotifyingauthority">a. Authority Type<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <%--<div class="col-lg-3">
                               <form:radiobutton path="radnotifyingauthtype" value="GOO" id="postedGOO"/> 
                               <label for="postedGOO"> Government of India </label>
                           </div>
                          <div class="col-lg-3">
                               <form:radiobutton path="radnotifyingauthtype" value="GOO" id="postedGOO"/> 
                               <label for="postedGOO"> Foreign Body </label>
                           </div>--%>
                        </div>

                        <%--  <div class="row" style="margin-bottom: 7px;">
                              <div class="col-lg-2">

                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> State Govt.Organization </label>
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Other State Govt.Organization </label>
                            </div>
                        </div>--%>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="deptcode"> b. Department<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="deptcode" id="deptcode" class="form-control" onchange="getDepartmentWiseOfficeList();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>                           
                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="offCode"> c. Office<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="offCode" id="offCode" class="form-control" onchange="getOfficeWisePostList();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                </form:select>                           
                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="detailsnotifyingauthority"> d. Sanctioning Authority<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sanctionauthority" id="sanctionauthority" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${spclist}" itemValue="spc" itemLabel="spn"/>
                                </form:select>                           
                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkJoinedAsSuch"> 4. Whether Employed Under Any Reservation</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="checkEmployed" id="checkEmployed" value="Y"/>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo"> 5.  (a) Reservation Category under which Employed<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-4">   
                                <form:select path="reservationCategory" id="reservationCategory" class="form-control" onchange="changecategory();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${categorylist}" itemValue="categoryid" itemLabel="categoryName"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;" id="spCategoryID" >
                            <div class="col-lg-2">
                                <label for="disableCategory">  &nbsp;&nbsp;&nbsp;(b) Specific Category for Person with Disability<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-4">   
                                <form:select path="disableCategory" id="disableCategory" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${Specialcategorylist}" itemValue="disabilityCode" itemLabel="disabilityName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                Specific Code <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="specific_Code" id="specific_Code" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${SpecificCodelist}" itemLabel="specificName" itemValue="specificCode"/>
                                </form:select>                                
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="rehabilationCategory"> 6. Whether Employed Under Rehabilitation Assistance</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="rehabilationCategory" value="Y"/>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note"> 7. Note(if any)</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                        <div class="panel-footer">
                            <input type="submit" name="btnPromotion" value="Save" class="btn btn-default" onclick="return saveCheck();"/>
                        </div>


                    </div>
                </div> 
            </div>
        </div>
    </form:form>
</body>
</html>
