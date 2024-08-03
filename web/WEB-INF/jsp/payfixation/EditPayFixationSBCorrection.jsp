<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('.date').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                togglePayCommission();

                $(document).on("click", ".rdoincrPaycomm", function(event) {
                    var rdvalue = $(this).val();
                    var rdidattr = $(this).attr("id");
                    //alert("rdidattr is: "+rdidattr);
                    var rdidattrnum = rdidattr.replace(/^\D+/g, '');
                    //alert("rdidattrnum is: "+rdidattrnum);
                    if (rdidattr.indexOf("six") > -1) {
                        $("#paycomsix" + rdidattrnum).show();
                        $("#paycomseven" + rdidattrnum).hide();
                    } else if (rdidattr.indexOf("seven") > -1) {
                        $("#paycomsix" + rdidattrnum).hide();
                        $("#paycomseven" + rdidattrnum).show();
                    }
                });
            });
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidNotifyingDeptCode').val();
                $('#hidNotifyingOffCode').empty();
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                var url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                } else {
                    url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                }

                $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList() {
                var offcode = $('#hidNotifyingOffCode').val();
                $('#notifyingSpc').empty();
                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#notifyingSpc').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost() {
                $('#notifyingPostName').val($('#notifyingSpc option:selected').text());
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
                if (payCommVal == '6') {
                    var gp = $('#txtGP').val();
                    if (gp == '') {
                        alert("Enter Grade Pay");
                        return false;
                    }
                }

                if (payCommVal == '7') {
                    if ($("#payLevel").val() == '') {
                        alert('Please select Pay Level.');
                        return false;
                    }
                    if ($("#payCell").val() == '') {
                        alert('Please select Pay Cell.');
                        return false;
                    }
                }
                if ($('#txtBasic').val() == "") {
                    alert("Please enter Pay");
                    $('#txtBasic').focus();
                    return false;
                }
                if ($('#txtWEFDt').val() == "") {
                    alert("Please enter Date of Effect of Pay");
                    return false;
                }
                if ($("#notType").val() == "PAYFIXATION") {
                    if ($('#sltPayFixationReason').val() == "") {
                        alert("Please select Reason of Pay Fixation");
                        return false;
                    }
                }
            }

            function fixIds(elem, cntr) {
                $(elem).find("[id]").add(elem).each(function() {
                    this.id = this.id.replace(/\d+$/, "") + cntr;
                    if (this.name != undefined) {
                        var objname = this.name;
                        index = cntr - 1;
                        index2 = index - 1;
                        objname = objname.replace('[' + index2 + ']', '[' + index + ']');
                        this.name = objname;
                    }
                })
            }

            function cloneRow() {
                var cloneCntr = 0;
                var $div = $('div[id^="addRow"]:last');

                // Read the Number from that DIV's ID (i.e: 3 from "addRow3")
                // And increment that number by 1
                var num = parseInt($div.prop("id").match(/\d+/g), 10) + 1;
                cloneCntr = num;

                // Clone it and assign the new ID (i.e: from num 4 to ID "addRow4")

                var clone = $div.clone();
                fixIds(clone, cloneCntr);
                clone.find('input').val("");

                var $addrow = clone.prop('id', 'addRow' + num);

                // Finally insert addRow wherever you want
                $div.after($addrow.append());
                $("#prId" + num).val('0');
                $("#incrId" + num).val('0');
                $("#notId" + num).val('0');
                $("#payrecorid" + num).val('0');
                $("#dividslno" + num).html(num);
                cloneCntr++;
                $('.datepickertxt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            }

            function togglePayCommission() {
                var rdoPaycomm = $("input[name='rdoPaycomm']:checked").val();


                if (rdoPaycomm == '6' || rdoPaycomm == '5') {
                    $('#div6pay').show();

                    $("#div7pay").hide();

                } else {
                    $('#div6pay').hide();
                    $("#div7pay").show();
                    $("#sltPayScale").val('');

                }
            }

            function deleteRetrospectiveIncrement(obj) {
                //alert("Not Id object is: "+obj);
                var objvalue = $("#" + obj).val();
                //alert("Not Id value is: "+objvalue);
                if (confirm("Are you sure to Delete?")) {
                    self.location = "deleteRetrospectivePayFixation.htm?notId=" + objvalue + "&nottype=" + $('#notType').val();
                }
            }

            function openNotifyingAuthorityModal() {

                var orgType = $('input[name="radnotifyingauthtype"]:checked').val();                
                if (orgType == 'GOO') {
                    $("#payFixationAuthorityModal").css("display", "block");
                    $("#payFixationOtherOrgModal").css("display", "none");
                } else if (orgType == 'GOI') {
                    $("#payFixationAuthorityModal").css("display", "none");
                    $("#payFixationOtherOrgModal").css("display", "block");
                }

            }

            function getOtherOrgPost() {
                $('#notifyingPostName').val($('#hidNotifyingOthSpc option:selected').text());

                $('#hidNotifyingDeptCode').val('');
                $('#hidNotifyingOffCode').val('');
                $('#notifyingSpc').val('');

                $("#payFixationOtherOrgModal").modal("hide");
            }

            function approveCheck() {
                if (confirm("Do you want to Approve?")) {
                    return true;
                } else {
                    return false;
                }
            }

            function closeInnerModal(id) {
                $("#" + id).modal("hide");
            }
        </script>
        <style type="text/css">
            .col-md-1 {
                width: 10%;
            }
            .col-md-2{
                width: 13%;
            }
            .slno{
                width: 3% !important;
            }
        </style>
    </head>
    <body>
        <form:form action="savePayFixationSBCorrection.htm" method="POST" commandName="payFixation">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <c:if test="${payFixation.notType eq 'PAYFIXATION'}">
                            Employee Pay Fixation
                        </c:if>
                        <c:if test="${payFixation.notType eq 'PAYREVISION'}">
                            Employee Pay Revision
                        </c:if>
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="notId" id="notId"/>
                        <form:hidden path="notType" id="notType"/>
                        <form:hidden path="payid" id="payid"/>
                        <form:hidden path="payRecordId" id="payRecordId"/>
                        <form:hidden path="correctionid"/>
                        <form:hidden path="entrytype"/>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt">Notification Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date">
                                    <form:input class="form-control" path="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="notifyingPostName">Notifying Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO" id="postedGOO" onclick="openNotifyingAuthorityModal();"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOI" id="postedGOI" onclick="openNotifyingAuthorityModal();"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingPostName" id="notifyingPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div id="payFixationAuthorityModal" style="display:none;">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="notifyingSpc">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="notifyingSpc" id="notifyingSpc" class="form-control" onchange="getPost();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${postlist}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                        </div>

                        <div id="payFixationOtherOrgModal" style="display:none;">
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
                                <div class="col-lg-1"></div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>Details of Pay</label>
                            </div>
                            <div class="col-lg-10"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="rdoPaycomm">Select Pay Commission <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:radiobutton class="custom-control-input" id="rdoPaycomm" path="rdoPaycomm" value="5" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="defaultUnchecked">5th Pay</label>
                            </div>                            
                            <div class="col-lg-2">   
                                <form:radiobutton class="custom-control-input" id="rdoPaycomm" path="rdoPaycomm" value="6" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="defaultUnchecked">6th Pay</label>
                            </div>
                            <div class="col-lg-6">
                                <form:radiobutton class="custom-control-input" id="rdoPaycomm" path="rdoPaycomm" value="7" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="defaultUnchecked">7th Pay</label>
                            </div>
                        </div>    
                        <div class="row" style="margin-bottom: 7px;" id="div6pay">
                            <div class="col-lg-2">
                                (a) Revised Scale of Pay/Pay Band
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltPayScale" id="sltPayScale" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${payscalelist}" itemValue="payscale" itemLabel="payscale"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                (b) Grade Pay
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtGP" id="txtGP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;"  id="div7pay">
                            <div class="col-lg-2">
                                <label for="payLevel">Pay Level<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="payLevel" id="payLevel" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${paylevelList}" itemLabel="label" itemValue="value"/>
                                </form:select>   

                            </div>
                            <div class="col-lg-2">
                                <label for="payCell"> Pay Cell <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="payCell" id="payCell" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${payCellList}" itemLabel="label" itemValue="value"/>
                                </form:select>                                
                            </div>
                        </div>    

                        <div class="row" style="margin-bottom: 7px;">

                            <div class="col-lg-2">
                                (c) Revised Basic<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtBasic" id="txtBasic" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (d) Personal Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPP" id="txtPP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-2">
                                (e) Special Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtSP" id="txtSP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (f) Other Emoluments Falling Under Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtOP" id="txtOP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (g) Description of Other Pay
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="txtDescOP" id="txtDescOP"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (h) With Effect From Date<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date'>
                                    <form:input class="form-control" path="txtWEFDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                With Effect From Time<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltWEFTime" id="sltWEFTime" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (i) Date of Next Increment
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date">
                                    <form:input class="form-control" path="txtNextIncrementDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-8"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Note(if any)</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                        <c:if test="${payFixation.notType eq 'PAYFIXATION'}">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltPayFixationReason">Reason of Pay Fixation<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="sltPayFixationReason" id="sltPayFixationReason" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="FIRST APPOINTMENT">FIRST APPOINTMENT</form:option>
                                        <form:option value="PROMOTION">PROMOTION</form:option>
                                        <form:option value="TBA">TBA</form:option>
                                        <form:option value="ACP">ACP</form:option>
                                        <form:option value="1st MACP">1st MACP</form:option>
                                        <form:option value="2nd MACP">2nd MACP</form:option>
                                        <form:option value="3rd MACP">3rd MACP</form:option>
                                        <form:option value="4th MACP">4th MACP</form:option>
                                        <form:option value="1st RACP">1st RACP</form:option>
                                        <form:option value="2nd RACP">2nd RACP</form:option>
                                        <form:option value="3rd RACP">3rd RACP</form:option>
                                        <form:option value="4th RACP">4th RACP</form:option>
                                        <form:option value="1st DACP">1st DACP</form:option>
                                        <form:option value="2nd DACP">2nd DACP</form:option>
                                        <form:option value="3rd DACP">3rd DACP</form:option>
                                        <form:option value="6th Pay">6th Pay</form:option> 
                                        <form:option value="7th Pay">7th Pay</form:option>
                                        <form:option value="Other">Other</form:option> 
                                    </form:select>
                                </div>
                                <div class="col-lg-7"></div>
                            </div>
                        </c:if>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${mode eq 'E'}">
                            <input type="submit" name="btnPayFixation" value="Save" class="btn btn-success" onclick="return saveCheck();"/>
                            <input type="submit" name="btnPayFixation" value="Submit" id="saveBtn" class="btn btn-default" onclick="return confirm('Are you sure to submit?');"/>
                        </c:if>
                        <c:if test="${mode eq 'D'}">
                            <input type="submit" name="btnPayFixation" value="Approve" class="btn btn-success" onclick="return approveCheck();"/>
                        </c:if>
                    </div>
                </div>
            </div>



        </form:form>
    </body>
</html>
