<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Loan Release Data</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(function() {
                $('#txtreorderdt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList(type) {
                var deptcode = $('#sltdept').val();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#sltoffice').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltoffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode = $('#sltoffice').val();

                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#sltauth').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltauth').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost(type) {
                $('#sancAuthName').val($('#sltauth option:selected').text());
            }
            function saveLoanrelease() {
                if (document.getElementById("txtreorderno").value == "") {
                    alert("Please enter the Order No");
                    document.getElementById("txtreorderno").focus();
                    return false;
                }
                if (document.getElementById("txtreorderdt").value == "") {
                    alert("Please enter the Order Date");
                    document.getElementById("txtreorderdt").focus();
                    return false;
                }
                if (document.getElementById("sltloan").value == "") {
                    alert("Please select Loan Type");
                    return false;
                }
                var exp = /^[0-9]+$/;
                if (document.getElementById("txtinstno").value == "") {
                    alert("Please enter the Instalment No");
                    document.getElementById("txtinstno").focus();
                    return false;
                }
                if (document.getElementById("txtinstno").value != "" && !document.getElementById("txtinstno").value.match(exp)) {
                    alert("Please enter only numbers for Instalment No");
                    document.getElementById("txtinstno").focus();
                    return false;
                }
                if (document.getElementById("txtamount").value == "") {
                    alert("Please enter the Instalment Amount");
                    document.getElementById("txtamount").focus();
                    return false;
                }
                if (document.getElementById("txtamount").value != "" && !document.getElementById("txtamount").value.match(exp)) {
                    alert("Please enter only numbers for Instalment Amount");
                    document.getElementById("txtamount").focus();
                    return false;
                }
                return true;
            }

        </script>
    </head>
    <body>
        <form:form id="fm" action="loanrelease.htm" method="post" name="myForm" commandName="loanrelease">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Release Of Loan
                        <form:hidden id="hrepid" path="hrepid" value="${loanrelease.hrepid}"/>
                        <form:hidden id="hnid" path="hnid" value="${loanrelease.hnid}"/>
                        <form:hidden id="txtEid" path="txtEid" value="${loanrelease.txtEid}"/>
                    </div>
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
                    <div class="row" style="margin-bottom: 7px;margin-top: 15px;">
                        <div class="col-lg-2">
                            <label for="txtNotOrdNo">Order No<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">   
                            <form:input class="form-control" path="txtreorderno" id="txtreorderno"/>
                        </div>
                        <div class="col-lg-2">
                            <label for="txtNotOrdDt">Order Date<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                <form:input class="form-control" path="txtreorderdt" id="txtreorderdt" readonly="true"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="spc">Details Of Authority</label>
                        </div>
                        <div class="col-lg-5">
                            <form:input class="form-control" path="sancAuthName" id="sancAuthName" readonly="true"/>                           
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#loanReleaseAuthorityModal">
                                <span class="glyphicon glyphicon-search"></span> Search
                            </button>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="spc">Loan Type<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-5">
                            <form:select path="sltloan" id="sltloan" class="form-control">
                                <form:option value="">--Select--</form:option>
                                <form:options items="${loanTypeArray}" itemValue="loanType" itemLabel="loanName"/>
                            </form:select>                           
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="txtNotOrdNo">Release Type<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">   
                            <form:radiobutton path="ptype" value="F"/>Full
                        </div>
                        <div class="col-lg-2">
                            <form:radiobutton path="ptype" value="I"/>Installment
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="txtNotOrdNo">Installment No<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">   
                            <form:input class="form-control" path="txtinstno" id="txtinstno"/>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="txtNotOrdNo">Release Amount<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">   
                            <form:input class="form-control" path="txtamount" id="txtamount"/>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="note">Note(if any)</label>
                        </div>
                        <div class="col-lg-6">
                            <form:textarea class="form-control" path="txtnote" id="txtnote"/>
                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="save" value="Save" class="btn btn-primary" onclick="return saveLoanrelease();" />
                        <input type="submit" name="cancel" value="Cancel" class="btn btn-primary" onclick="return cancelLoanrelease();" /> 
                    </div>    
                </div>
            </div>
            <div id="loanReleaseAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Authority Details</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltdept" id="sltdept" class="form-control" onchange="getDeptWiseOfficeList('N');">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${deptArray}" itemValue="deptCode" itemLabel="deptName"/>
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
                                    <form:select path="sltoffice" id="sltoffice" class="form-control" onchange="getOfficeWisePostList('N');">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${offArray}" itemValue="offCode" itemLabel="offName"/>
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
                                    <form:select path="sltauth" id="sltauth" class="form-control" onchange="getPost('N');">
                                        <form:option value="">--Select--</form:option>
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
        </form:form>
    </body>
</html>
