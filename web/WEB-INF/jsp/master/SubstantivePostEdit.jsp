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
        <script type="text/javascript" src="js/jquery.min.js"></script>  
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                if ($('#status').val() != '') {
                    $('#substantivePostModal').modal("show");
                    if ($('#status').val() > 0) {
                        $('#msg').removeClass();
                        $('#msg').addClass("saveSuccess");
                        $('#msg').text("Updated.");
                    } else {
                        $('#msg').removeClass();
                        $('#msg').addClass("saveError");
                        $('#msg').text("Error Occured!");
                    }
                }
                
                $('#orderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#terminationOrderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                
            });

            function openModal(mode, spc, payscale_6th, payscale_7th, post_group, level, gp, cadreCode, deptCode) {
                //alert("Mode is: "+mode);
                if ($('#postCode').val() == '') {
                    alert("Please select Post");
                    return false;
                }
                $('#spc').val(spc);
                $('#mode').val(mode);
                $('#payscale').val(payscale_6th);
                $('#payscale_7th').val(payscale_7th);
                $('#postgrp').val(post_group);
                $('#paylevel').val(level);
                $('#gp').val(gp);
                $('#dept').val(deptCode);
                if (deptCode != '') {
                    $('#dept').val(deptCode);
                    getDeptWiseCadreList();
                    $(document).ajaxStop(function () {
                        $('#cadre').val(cadreCode);
                    });
                }

                $('#substantivePostModal').modal("show");
            }

            function openAddPostModal() {
                if ($('#postCode').val() == '') {
                    alert("Please select Post");
                    return false;
                }
                $('#addSubstantivePostModal').modal("toggle");
            }

            function openRemovePostModal(spc) {
                $('#spc').val(spc);
                $('#removeSubstantivePostModal').modal("toggle");
            }

            function removeFunc(spc) {
                //alert(spc);
                if (confirm("Are you sure to Remove?")) {
                    window.location = "saveSubstantivePost.htm?btnSpc=Remove&spc=" + spc + "&postCode=" + $('#postCode').val() + "&aerId=" + $('#aerId').val();
                }
            }

            function verifyUpdate() {

                if ($('#payscale').val() == '' && $('#payscale_7th').val() == '') {
                    alert("Please select at least one Pay Scale.");
                    return false;
                }

                if ($('#payscale_7th').val() != '' && $('#paylevel').val() == '') {
                    alert("Please select Level.");
                    return false;
                }

                if (!$('#chkGrantInAid').is(":checked")) {
                    if ($('#postgrp').val() == '') {
                        alert("Please Select Post Group");
                        return false;
                    }
                }

                if ($('#payscale').val() != '' && $('#gp').val() == '') {
                    alert("Please enter Grade Pay.");
                    return false;
                }

                if ($('#gp').val() != '' && $('#gp').val() > 10000) {
                    alert("Grade Pay must be within 10000.");
                    return false;
                }

            }

            function verifyAdd() {
                var allowedExtensions = /(\.pdf)$/i;
                if ($('#txtNoOfPost').val() == '') {
                    alert("Number of Post canot be blank");
                    return false;
                }
                if ($('#txtNoOfPost').val() > 10) {
                    alert("Number of Post must be within 10");
                    return false;
                }
                if($('#orderNo').val() == ""){
                    alert("Please enter Order No");
                    return false;
                }
                if($('#orderDate').val() == ""){
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#spcFileAttch').val() == '') {
                    alert("Please upload Order Copy");
                    return false;
                } else if ($('#spcFileAttch').val() != '') {
                    if (!allowedExtensions.exec($('#spcFileAttch').val())) {
                        alert('Please upload file having extensions .pdf only.');
                        $("#spcFileAttch").val('');
                        return false;
                    }
                }
            }

            function verifyTerminatePost() {
                var allowedExtensions = /(\.pdf)$/i;
                
                if($('#terminationOrderNo').val() == ""){
                    alert("Please enter Order No");
                    return false;
                }
                if($('#terminationOrderDate').val() == ""){
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#spcRemoveFileAttch').val() == '') {
                    alert("Please upload Order Copy");
                    return false;
                } else if ($('#spcRemoveFileAttch').val() != '') {
                    if (!allowedExtensions.exec($('#spcRemoveFileAttch').val())) {
                        alert('Please upload file having extensions .pdf only.');
                        $("#spcRemoveFileAttch").val('');
                        return false;
                    }
                }

                if (confirm("Are you sure to Terminate the Post?")) {
                    return true;
                } else {
                    return false;
                }
            }

            function onlyIntegerRange(e)
            {
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

            function getDeptWiseCadreList() {
                $('#cadre').empty();
                var url = 'getCadreListJSON.htm?deptcode=' + $('#dept').val();
                $('#cadre').append('<option value="">Select Cadre</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#cadre').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
        </script>
        <style type="text/css">
            .saveSuccess{
                color: #00cc66;
                font-weight: bold;
            }
            .saveError{
                color: #ff3333;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <form:form action="saveSubstantivePost.htm" method="POST" commandName="substantivePost" enctype="multipart/form-data">
            <form:hidden path="aerId" id="aerId"/>
            <div class="container-fluid">
                <div class="row" style="margin-top:20px; margin-bottom: 7px;">
                    <div class="col-lg-3" align="right">
                        <label>Select Post</label>
                    </div>
                    <div class="col-lg-6">
                        <form:select path="postCode" id="postCode" class="form-control">
                            <option value="">--Select Post--</option>
                            <form:options items="${postlist}" itemValue="postCode" itemLabel="postname"/>
                        </form:select>
                    </div>
                    <div class="col-lg-3">
                        <button type="submit" name="btnSpc" value="List" class="btn btn-primary">Get List</button>
                    </div>
                </div>
            </div>
           

                <input type="hidden" name="spc" id="spc"/>
                <input type="hidden" name="mode" id="mode"/>
                <input type="hidden" id="status" value="${retVal}"/>

                <div class="panel panel-default">

                    <div class="panel-body">
                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>
                                    <th width="5%">Sl No</th>
                                    <th width="30%">Designation</th>
                                    <th width="20%">Mapped Employee</th>
                                    <th width="5%">Post Group</th>
                                    <th width="10%">Pay Scale<br />(6th Pay)</th>
                                    <th width="5%">Grade<br />Pay</th>
                                    <th width="8%">Pay Scale<br />(7th Pay)</th>
                                    <th width="5%">Level</th>
                                    <th width="15%">Action</th>
                                </tr>
                            </thead>
                            <hr />
                            <c:if test="${not empty spnList}">
                                <tbody>
                                    <c:forEach var="list" items="${spnList}" varStatus="count">
                                        <tr>
                                            <td>
                                                ${count.index + 1}
                                            </td>
                                            <td>
                                                <c:out value="${list.spn}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.empname}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.postgrp}"/> 
                                            </td>
                                            <td>
                                                <c:out value="${list.payscale}"/>

                                            </td>
                                            <td>
                                                <c:out value="${list.gp}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.payscale_7th}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.paylevel}"/>
                                            </td>
                                            <td>
                                                <a href="#" onclick="openModal('single', '<c:out value="${list.spc}"/>', '<c:out value="${list.payscale}"/>', '<c:out value="${list.payscale_7th}"/>', '<c:out value="${list.postgrp}"/>', '<c:out value="${list.paylevel}"/>', '<c:out value="${list.gp}"/>', '<c:out value="${list.cadre}"/>', '<c:out value="${list.dept}"/>');">Change</a>
                                                &nbsp;
                                                <c:if test="${empty list.empname}">
                                                    <a href="#" onclick="openRemovePostModal('<c:out value="${list.spc}"/>');">Terminate</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </c:if>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button type="button" class="btn btn-primary" onclick="openModal('all', '', '', '', '', '', '', '', '');">Apply to All</button>&nbsp;&nbsp;
                    <button type="button" class="btn btn-primary" onclick="openAddPostModal();">Add Post</button>
                    <a href="getAvailablePostList.htm"><button type="button" class="btn btn-primary">Back to Post List</button></a>
                    </div>
                </div>

                <div id="substantivePostModal" class="modal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Select Pay Scale and Post Group</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="payscale">Grant-in-Aid</label>
                                    </div>
                                    <div class="col-lg-8">
                                        <form:checkbox path="chkGrantInAid" id="chkGrantInAid" value="Y"/>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="chkTeachingPost">Teaching Post</label>
                                    </div>
                                    <div class="col-lg-8">
                                        <form:checkbox path="chkTeachingPost" id="chkTeachingPost" value="T"/>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="payscale">Plan</label>
                                    </div>
                                    <div class="col-lg-8">
                                        <form:checkbox path="chkPlanOrNonPlan" id="chkPlanOrNonPlan" value="P"/>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="payscale">Pay Scale(6th)</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="payscale" id="payscale" class="form-control">
                                            <option value="">--Select Pay Scale--</option>
                                            <form:options items="${payscaleList}" itemValue="payscale" itemLabel="payscale"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="gp">Grade Pay</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:input path="gp" id="gp" maxlength="5" class="form-control" onkeypress='return onlyIntegerRange(event)'/>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="payscale_7th">Pay Scale(7th)</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="payscale_7th" id="payscale_7th" class="form-control">
                                            <form:option value="" label="--Select Pay Scale--"/>
                                            <form:option value="16600-52400" label="16600-52400"/>
                                            <form:option value="17200-54600" label="17200-54600"/>
                                            <form:option value="18000-56900" label="18000-56900"/>
                                            <form:option value="19900-63200" label="19900-63200"/>
                                            <form:option value="21700-69100" label="21700-69100"/>
                                            <form:option value="23600-74800" label="23600-74800"/>
                                            <form:option value="25500-81100" label="25500-81100"/>
                                            <form:option value="29200-92300" label="29200-92300"/>
                                            <form:option value="33300-105800" label="33300-105800"/>
                                            <form:option value="35400-112400" label="35400-112400"/>
                                            <form:option value="44900-142400" label="44900-142400"/>
                                            <form:option value="47600-151100" label="47600-151100"/>
                                            <form:option value="53100-167800" label="53100-167800"/>
                                            <form:option value="56100-177500" label="56100-177500"/>
                                            <form:option value="61300-193900" label="61300-193900"/>
                                            <form:option value="67700-208700" label="67700-208700"/>
                                            <form:option value="69400-207200" label="69400-207200"/>
                                            <form:option value="78800-209200" label="78800-209200"/>
                                            <form:option value="118500-214100" label="118500-214100"/>
                                            <form:option value="121200-212400" label="121200-212400"/>
                                            <form:option value="123100-215900" label="123100-215900"/>
                                            <form:option value="127100-216300" label="127100-216300"/>
                                            <form:option value="130600-215900" label="130600-215900"/>
                                            <form:option value="131100-216600" label="131100-216600"/>
                                            <form:option value="135100-216800" label="135100-216800"/>
                                            <form:option value="139600-217600" label="139600-217600"/>
                                            <form:option value="144200-218200" label="144200-218200"/>
                                            <form:option value="182200-224100" label="182200-224100"/>
                                            <form:option value="205400-224400" label="205400-224400"/>
                                            <form:option value="225000" label="225000"/>
                                            <form:option value="250000" label="250000"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="postgrp">Post Group</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="postgrp" id="postgrp" class="form-control">
                                            <option value="">--Select Post Group--</option>
                                            <option value="A">A</option>
                                            <option value="B">B</option>
                                            <option value="C">C</option>
                                            <option value="D">D</option>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="paylevel">LEVEL</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="paylevel" id="paylevel" class="form-control">
                                            <option value="">--Select Level--</option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="5A">5A</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                            <option value="9">9</option>
                                            <option value="10">10</option>
                                            <option value="10B">10B</option>
                                            <option value="11">11</option>
                                            <option value="12">12</option>
                                            <option value="12A">12A</option>
                                            <option value="13">13</option>
                                            <option value="13A">13A</option>
                                            <option value="14">14</option>
                                            <option value="15">15</option>
                                            <option value="16">16</option>
                                            <option value="17">17</option>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>

                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="dept">Department</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="dept" id="dept" class="form-control" onchange="getDeptWiseCadreList();">
                                            <form:options items="${deptlist}" itemLabel="deptName" itemValue="deptCode"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>

                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="cadre">Cadre</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="cadre" id="cadre" class="form-control"></form:select>
                                        </div>
                                        <div class="col-lg-1">
                                        </div>
                                    </div>

                                    <div class="row" style="margin-bottom: 7px;">
                                        <div class="col-lg-2">
                                        </div>
                                        <div class="col-lg-3">
                                            <button type="submit" name="btnSpc" value="Update" class="btn btn-primary" onclick="return verifyUpdate();">Update</button>
                                        </div>
                                        <div class="col-lg-7">
                                            <span id="msg"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="addSubstantivePostModal" class="modal" role="dialog">
                        <div class="modal-dialog">
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title"></h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row" style="margin-bottom: 7px;">
                                        <div class="col-lg-5">
                                            <label for="txtNoOfPost">Enter Number of Post to Add</label>
                                        </div>
                                        <div class="col-lg-3">
                                        <form:input path="txtNoOfPost" id="txtNoOfPost" class="form-control" maxlength="2"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-5">
                                        <label for="orderNo">Order No</label>
                                    </div>
                                    <div class="col-lg-4">
                                        <form:input path="orderNo" id="orderNo" class="form-control"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-5">
                                        <label for="orderDate">Order Date</label>
                                    </div>
                                    <div class="col-lg-4">
                                        <form:input path="orderDate" id="orderDate" class="form-control" readonly="true"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-5">
                                        <label for="spcFileAttch">Upload Order Copy</label>
                                    </div>
                                    <div class="col-lg-3">
                                        <input type="file" name="spcFileAttch" id="spcFileAttch"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-4" align="center">
                                        <button type="submit" name="btnSpc" value="AddPost" class="btn btn-primary" onclick="return verifyAdd();">Add</button>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                    </div>
                                    <div class="col-lg-3">

                                    </div>
                                    <div class="col-lg-7">
                                        <span id="msg"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="removeSubstantivePostModal" class="modal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title"></h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-5">
                                        <label for="terminationOrderNo">Order No</label>
                                    </div>
                                    <div class="col-lg-4">
                                        <form:input path="terminationOrderNo" id="terminationOrderNo" class="form-control"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-5">
                                        <label for="terminationOrderDate">Order Date</label>
                                    </div>
                                    <div class="col-lg-4">
                                        <form:input path="terminationOrderDate" id="terminationOrderDate" class="form-control" readonly="true"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-5">
                                        <label for="spcTerminateFileAttch">Upload Order Copy</label>
                                    </div>
                                    <div class="col-lg-4">
                                        <input type="file" name="spcTerminateFileAttch" id="spcRemoveFileAttch"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-4" align="center">
                                        <button type="submit" name="btnSpc" value="Remove" class="btn btn-primary" onclick="return verifyTerminatePost();">Terminate</button>
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
