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
                $('#wefdate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList(type) {
                var deptcode;

                deptcode = $('#hidNotifyingDeptCode').val();
                $('#hidNotifyingOffCode').empty();
                $('#notifyingSpc').empty();


                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');



                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }



            function getOfficeWisePostList(type) {
                var offcode;
                offcode = $('#hidNotifyingOffCode').val();
                $('#notifyingSpc').empty();
                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });


                $('#hidTempAuthOffice').val($('#hidNotifyingOffCode').val());
            }

            function getPost() {

                $('#notifyingPostName').val($('#notifyingSpc').val());
                $('#hidTempAuthSpc').val($('#notifyingSpc option:selected').text());

                $('#sltOffice').val($('#hidNotifyingOffCode').val());
                $('#sltDept').val($('#hidNotifyingDeptCode').val());




            }

            function validateForm() {

                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }

                if ($("#txtNotOrdNo").val() == '') {
                    alert('Please enter Order Number.');
                    $("#txtNotOrdNo").focus();
                    return false;
                }
                if ($("#txtNotOrdDt").val() == '') {
                    alert('Please enter Order Date.');
                    $("#txtNotOrdDt").focus();
                    return false;
                }

                if ($("#hidTempAuthSpc").val() == '') {
                    alert('Please Select Authority.');
                    $("#hidTempAuthSpc").focus();
                    return false;
                }

                if ($("input[name=rdTransaction]:checked").val() == "C") {
                    var r = confirm("Are you sure to continue with Current Transaction ? ");
                    if (r) {
                        return true;
                    } else {
                        return false;
                    }
                }



            }



        </script>
    </head>
    <body>
        <form:form action="saveReinstatement.htm" method="POST" commandName="reinstatementForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Reinstatement
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="hnotid" id="hnotid"/>
                        <form:hidden path="spId" id="spId"/>
                        <form:hidden path="notifyingPostName" id="notifyingPostName"/>
                        <form:hidden path="sltOffice" id="sltOffice"/>
                        <form:hidden path="sltDept" id="sltDept"/>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="chkNotSBPrint" >Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/>
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-2"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="rdTransaction">Select type of Transaction</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:radiobutton path="rdTransaction" value="S"/> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" value="C"/> Current Transaction(will effect current Pay or Post)
                            </div>
                            <div class="col-lg-2"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Reinstatement Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo" maxlength="49"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt">Order Date<span style="color: red">*</span></label>
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
                                <label for="notifyingPostName">Authority</label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="hidTempAuthSpc" id="hidTempAuthSpc" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#reinstatementAuthorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="wefdate">With effect from Date <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="wefdate" id="wefdate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div> 
                            </div>
                            <div class="col-lg-2">
                                <label for="sltweftime"> Time <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltweftime" class="form-control">
                                    <form:option value="">--Select One--</form:option>
                                    <form:option value="FN">FORE NOON</form:option>
                                    <form:option value="AN">AFTER NOON</form:option>
                                </form:select>                             
                            </div>
                            <div class="col-lg-4"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Note(if any)</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Reinstate" class="btn btn-default" onclick="return validateForm();"/>
                        <c:if test="${not empty reinstatementForm.spId}">
                            <input type="button" name="action" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>
                        </c:if>
                    </div>
                </div>
            </div>

            <div id="reinstatementAuthorityModal" class="modal" role="dialog">
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
                                    <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="getDeptWiseOfficeList('N');">
                                        <form:option value="">--Select--</form:option>
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
                                    <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWisePostList('N');">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
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
                                    <form:select path="notifyingSpc" id="notifyingSpc" class="form-control" onchange="getPost('N');">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${postlist}" itemValue="spc" itemLabel="postname"/>
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

        <script type="text/javascript">
            $(function() {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });
        </script>
    </body>
</html>
