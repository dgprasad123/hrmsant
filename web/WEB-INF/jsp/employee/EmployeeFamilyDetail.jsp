<%-- 
    Document   : EmployeeFamilyDetail
    Created on : Apr 4, 2019, 7:51:30 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#fname').keyup(function() {
                    $(this).val($(this).val().toUpperCase());
                });
                $('#mname').keyup(function() {
                    $(this).val($(this).val().toUpperCase());
                });
                $('#lname').keyup(function() {
                    $(this).val($(this).val().toUpperCase());
                });
                if ($('#nomGpf').is(":checked")) {
                    $('.id_arrearsPer').show();
                } else {
                    $('.id_arrearsPer').hide();
                }
                if ($('#nomCvpDcrgLta').is(":checked")) {
                    $('.id_arrearsAcc').show();
                } else {
                    $('.id_arrearsAcc').hide();
                }
                if ($('#is_minor').is(":checked")) {
                    $('.minor_guardian').show();
                } else {
                    $('.minor_guardian').hide();
                }
                if ($('#is_pwd').is(":checked")) {
                    $('.handicapped_type').show();
                } else {
                    $('.handicapped_type').hide();
                }
                if ($('#is_minor').is(":checked")) {
                    $('.minor_guardian').show();
                } else {
                    $('.minor_guardian').hide();
                }
            });

            $(function() {
                $('#dob').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    maxDate: new Date()
                });


            });

            function display_guardian(ids) {
                if ($('#' + ids).is(":checked")) {
                    $('.minor_guardian').show();
                } else {
                    $('.minor_guardian').hide();
                }

            }

            function display_data(ids) {
                if ($('#nomGpf').is(":checked")) {
                    $('.id_arrearsPer').show();
                } else {
                    $('.id_arrearsPer').hide();
                }
                if ($('#nomCvpDcrgLta').is(":checked")) {
                    $('.id_arrearsAcc').show();
                } else {
                    $('.id_arrearsAcc').hide();
                }

            }
            function validate() {
                var initialsStaus = $('#initials').val();
                if ($('#relation').val() == '') {
                    alert("Please Select Relation Type");
                    $('#relation').focus();
                    return false;
                }
                if ($('#initials').val() == '') {
                    alert("Please Select Initials");
                    $('#initials').focus();
                    return false;
                }
                if ($('#fname').val() == '') {
                    alert("Please Enter First Name");
                    $('#fname').focus();
                    return false;
                }

                if ($('#lname').val() == '') {
                    alert("Please Enter Last Name");
                    $('#lname').focus();
                    return false;
                }
                if ($('#identityDocType').val() == '' && (initialsStaus != 'LATE')) {
                    alert("Please Select Identity Type");
                    $('#identityDocType').focus();
                    return false;
                }
                if ($('#identityDocNo').val() == '' && (initialsStaus != 'LATE')) {
                    alert("Please Select Identity No");
                    $('#identityDocNo').focus();
                    return false;
                }
                if ($('#dob').val() == '' && (initialsStaus != 'LATE')) {
                    alert("Please Enter DOB");
                    $('#dob').focus();
                    return false;
                }
                if ($('#gender').val() == '' && (initialsStaus != 'LATE')) {
                    alert("Please Select Gender");
                    $('#gender').focus();
                    return false;
                }
                if ($('#marital_statuS').val() == '' && (initialsStaus != 'LATE')) {
                    alert("Please Select Marital Status");
                    $('#marital_statuS').focus();
                    return false;
                }
                if ($('#nomGpf').is(":checked") || $('#nomCvpDcrgLta').is(":checked")) {
                    //  alert($('#dob').val())

                    if ($('#nomGpf').is(":checked")) {
                        if ($('#arrearsPer').val() <= 0) {
                            alert("Please Enter percentage of Share of Arrears");
                            $('#arrearsPer').focus();
                            return false;
                        }
                    }

                    if ($('#nomCvpDcrgLta').is(":checked")) {
                        if ($('#arrearsAcc').val() <= 0) {
                            alert("Percentage of Share of Accumulation");
                            $('#arrearsAcc').focus();
                            return false;
                        }
                    }




                }

            }
            function display_handicapped(ids) {
                if ($('#' + ids).is(":checked")) {
                    $('.handicapped_type').show();
                } else {
                    $('.handicapped_type').hide();
                    $("#pwd_type").val("");
                }

            }
            function getBranchList(me) {

                $('option', $('#sltbranch')).not(':eq(0)').remove();
                $.ajax({
                    type: "POST",
                    url: "bankbranchlistJSON.htm?bankcode=" + $(me).val(),
                    success: function(data) {
                        $.each(data, function(i, obj)
                        {
                            $('#sltbranch').append($('<option>', {
                                value: obj.branchcode,
                                text: obj.branchname
                            }));

                        });
                    }});
            }
        </script>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="FAMILYPAGESB" />
        </jsp:include>
        <div id="profile_container">
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="employeeFamilySave.htm" method="post" class="form-horizontal" commandName="familyRelation">
                    <div style=" margin-bottom: 5px;" class="panel panel-info">
                        <div class="panel-body">
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Relation Type:<strong style="color:red">*</strong></label>
                                <div class="col-sm-10">
                                    <form:hidden path="empId"/>
                                    <form:hidden path="slno"/>
                                    <form:select path="relation" style="width:50%;" class="form-control" id='relation'>
                                        <form:option value="" label="Select" cssStyle="width:30%"/>                                    
                                        <form:options items="${familyRelationTypeList}"/>                                    
                                    </form:select> 
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Name:<strong style="color:red">*</strong></label>
                                <div class="col-sm-2">
                                    <form:select path="initials" class="form-control" id='initials'>  
                                        <form:option value="" label="Select" cssStyle="width:30%"/> 
                                        <form:options items="${titleType}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>
                                <div class="col-sm-2">
                                    <form:input path="fname" class="form-control" maxlength="30"/>
                                </div>
                                <div class="col-sm-2">
                                    <form:input path="mname" class="form-control" maxlength="30"/>
                                </div>
                                <div class="col-sm-2">
                                    <form:input path="lname" class="form-control" maxlength="30"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="type">&nbsp;</label>
                                <label class="control-label col-sm-1" for="ifalive"><form:checkbox path="ifalive" value="Y"  id="ifalive" onclick="display_data('ifalive')"/> Is Alive</label>
                                <label class="control-label col-sm-1" for="email"><form:checkbox path="is_minor" value="Y" id="is_minor"  onclick="display_guardian('is_minor')" /> Is Minor</label>
                                <label class="control-label col-sm-1" for="email"><form:checkbox path="isGuardian" value="Y" id="isGuardian"/> Is Guardian</label> 
                                <label class="control-label col-sm-1" for="email"><form:checkbox path="is_pwd" value="Y" id="is_pwd" onclick="display_handicapped('is_pwd')" /> Is Handicapped</label>
                                <label class="control-label col-sm-1" for="is_Nominee"><form:checkbox path="is_Nominee" value="Y" id="is_Nominee"  /> Is Nominee</label>
                            </div>

                            <div class="form-group"   >
                                <label class="control-label col-sm-2" for="type">Nomination For GPF:</label>
                                <div class="col-sm-1">
                                    <form:checkbox path="nomGpf" value="Y" id="nomGpf"  onclick="display_data('nomGpf')"/> </label>
                                </div>


                                <label class="control-label col-sm-2 id_arrearsPer" for="email" style='display:none'>Percentage of Share of Arrears:</label>
                                <div class="col-sm-4 id_arrearsPer" style='display:none'>
                                    <form:input path="arrearsPer" class="form-control"  id="arrearsPer"  maxlength="3"/>
                                </div> 


                            </div> 


                            <div class="form-group" id='id_arrearsAcc'  >
                                <label class="control-label col-sm-2" for="type">Nomination For Payment of CVP, DCRG & LTA:</label>
                                <div class="col-sm-1">
                                    <form:checkbox path="nomCvpDcrgLta" value="Y" id="nomCvpDcrgLta"  onclick="display_data('nomCvpDcrgLta')"/>
                                </div>   

                                <label class="control-label col-sm-2 id_arrearsAcc" for="email" style='display:none'>Percentage of Share of Accumulation:</label>
                                <div class="col-sm-4 id_arrearsAcc" style='display:none'>
                                    <form:input path="arrearsAcc" class="form-control"  id="arrearsAcc"  maxlength="3"/>
                                </div> 


                            </div> 

                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Identity Type:<strong style="color:red">*</strong></label>
                                <div class="col-sm-2">
                                    <form:select path="identityDocType"  class="form-control" id="identityDocType">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${identityTypeList}" var="identityDocType">
                                            <form:option value="${identityDocType.identityTypeId}" label="${identityDocType.identityType}"/>
                                        </c:forEach>                                  
                                    </form:select> 
                                </div>
                                <div class="col-sm-4">
                                    <label class="control-label col-sm-2" for="ino">Identity No:<strong style="color:red">*</strong></label>
                                    <form:input path="identityDocNo"  class="form-control" style="width:50%;" id="identityDocNo" placeholder="Enter Identity No" maxlength="40"/>
                                </div>

                                <div class="col-sm-2">
                                    <label class="control-label col-sm-2" for="dob">DOB:<strong style="color:red">*</strong></label>
                                    <div class='input-group date' id='do'>
                                        <form:input class="form-control"  id="dob" path="dob" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>


                            </div>  




                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Gender:<strong style="color:red">*</strong></label>
                                <div class="col-sm-2">
                                    <form:select path="gender" class="form-control" id='gender'>
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="M" label="Male"/>
                                        <form:option value="F" label="Female"/> 
                                        <form:option value="T">Transgender</form:option>                                 
                                    </form:select> 
                                </div>
                                <label class="control-label col-sm-2" for="email">Marital Status:<strong style="color:red">*</strong></label>
                                <div class="col-sm-2">
                                    <form:select id="marital_statuS" path="marital_statuS" style="width:34%;" class="form-control">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${maritallist}" var="marital">
                                            <form:option value="${marital.maritalId}" label="${marital.maritalStatus}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                </div>

                                <div class="col-sm-2">
                                    <form:input path="remarks" class="form-control" style="width:100%;" id="remarks" placeholder="Enter Remark if any" maxlength="150"/>
                                </div>


                            </div>


                            <div class="form-group handicapped_type" style="display:none">
                                <label class="control-label col-sm-2" for="email">Handicapped Type:</label>
                                ${pwd_type}    
                                <div class="col-sm-6">                                  
                                    <form:select path="pwd_type" id="pwd_type" class="form-control" style="width:60%;">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="P">Physically</form:option>
                                        <form:option value="M">Mentally</form:option>

                                    </form:select>
                                </div>                               

                            </div> 
                            <div class="form-group minor_guardian" style="display:none">
                                <label class="control-label col-sm-2" for="salutationFamilyGuardian">Minor Guardian Name:</label>
                                <div class="col-sm-1">
                                    <form:select path="salutationFamilyGuardian" class="form-control">  
                                        <form:option value="" label="Select" cssStyle="width:30%"/> 
                                        <form:options items="${titleType}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>
                                <div class="col-sm-6">
                                    <form:input path="minor_guardian"  class="form-control" style="width:50%;" id="minor_guardian" placeholder="Enter Minor Guardian Name" maxlength="50"/>
                                </div>
                            </div>
                            <div id="bankDetais">
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="email">Bank Name:</label>
                                    <div class="col-sm-6">
                                        <form:select path="sltBank" id="sltBank" class="form-control" style="width:60%;" onchange="getBranchList(this);">
                                            <form:option value="" label="Select" />
                                            <c:forEach items="${bankList}" var="bank">
                                                <form:option value="${bank.bankcode}" label="${bank.bankname}"/>
                                            </c:forEach>                                 
                                        </form:select> 
                                    </div>
                                    <div class="col-sm-2">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="email">Branch Name:</label>
                                    <div class="col-sm-6">
                                        <form:select path="branch_code" id="sltbranch" class="form-control" style="width:60%;">
                                            <form:option value="" label="Select" cssStyle="width:30%"/>
                                            <c:forEach items="${branchList}" var="branch">
                                                <form:option value="${branch.branchcode}" label="${branch.branchname}"/>
                                            </c:forEach>                                 
                                        </form:select> 
                                    </div>

                                    <div class="col-sm-2">

                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="email">Account Number:</label>
                                    <div class="col-sm-2">
                                        <form:input path="bank_acc_no" class="form-control" id="bankaccno" style="width:100%;" placeholder="Please Enter Bank Acc. No" maxlength="25"/>
                                    </div>
                                    <div class="col-sm-2">
                                        <label class="control-label col-sm-8">Mobile Number:</label>
                                    </div>
                                    <div class="col-sm-4">
                                        <form:input path="mobile" class="form-control" id="mobile" style="width:60%;" placeholder="Please Enter Mobile No" maxlength="10" onkeypress="return onlyIntegerRange(event)"/>
                                    </div>
                                </div>

                                <div class="col-sm-2">
                                    <label class="control-label col-sm-8">Address:</label>
                                </div>
                                <div class="col-sm-4">
                                    <form:textarea Class="form-control" path="address" id="address" /><br/>
                                </div>
                            </div>   
                            <div class="pull-left">                        
                                <span style=" color: red"> ${identity.printMsg}</span>
                            </div>
                        </div>
                        <div class="panel-footer">

                            <c:if test="${not empty empprofilecompletedstatus.dateOfProfileCompletion}">
                                <span style="display:block;text-align: center;font-weight: bold;font-size: 14px;color: red;">Profile completed on <c:out value="${empprofilecompletedstatus.dateOfProfileCompletion}"/> from IP <c:out value="${empprofilecompletedstatus.ipOfProfileCompletion}"/></span>
                            </c:if>
                            <c:if test="${empty empprofilecompletedstatus.dateOfProfileCompletion}">
                                <input type="submit" class="btn btn-success" name="action" value="Save" onclick="return validate()"/>
                                <c:if test="${not empty familyRelation.empId}">
                                    <input type="submit" class="btn btn-success" name="action" value="Delete"/>
                                </c:if>
                            </c:if>
                            <input type="submit" class="btn btn-success" name="action" value="Back"/>


                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
