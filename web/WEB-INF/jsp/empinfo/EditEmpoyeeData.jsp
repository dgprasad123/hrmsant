<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>  
        <%--<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>--%>

        <script src="js/moment.js" type="text/javascript"></script>

        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#usrcatg").hide();
                $("#usrcatgnm").hide();
                //document.getElementById('usrcatg').disabled = true;
                //document.getElementById('usrcatgnm').disabled = true;
                $("#empType").click(function() {
                    if ($("#empType").val() == 'A') {
                        $("#usrcatgnm").focus();
                        $("#usrcatg").show();
                        $("#usrcatgnm").show();
                    } else {
                        $("#usrcatg").hide();
                        $("#usrcatgnm").hide();
                    }

                });

                var moditems = '';
                //$(":input").css("background-color", "grey");
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $(':input').on('change', function() {
                    //alert("Updated:::" + $(this).attr("name") + "=" + $(this).val() + "   ::Count=" + $(this).attr("id"));
                    if (moditems == "") {
                        moditems = $(this).attr("name") + "=" + $(this).val();
                    } else {
                        moditems = moditems + "," + $(this).attr("name") + "=" + $(this).val();

                    }

                    $('#hidmsg').val(moditems);

                    //alert($('#hidmsg').val());
                });

            });


            function display_type(obj) {
                /*if (obj == "GIA") {
                 alert("You cannot change Acct Type to GIA.");
                 $("#accountType").val($("#hidaccttype").val());
                 return false;
                 }*/
            }

            function hasSpace(s)
            {
                // alert('hasSpace');
                if ($("#empType").val() == 'A') {
                    if ($("#spluserCategory").val() == "") {
                        alert("Enter Special User Category");
                        return false;
                    }
                }

                var hasSpaceInGpf = $("#gpfno").val();
                retSpace = new RegExp(/\s/g);

                /*for (var i = 0; i < moditems.length; i++) {
                 //moditems.push($(this).eq(i).val());
                 alert("Values:" + $(this).eq(i).val());
                 }*/
                if (retSpace.test(hasSpaceInGpf)) {
                    alert("Spaces Are Not Allowed in Account No");
                    return false;
                }
                var numeric = /^[0-9]+$/;
                if ($("#accountType").val() == "PRAN" && !$('#gpfno').val().match(numeric) && $("#sltGPFAssmued").val() == "N") {
                    alert("PRAN No should be numeric.");
                    return false;
                }
                if ($('#mobile').val() != "") {
                    if (retSpace.test($('#mobile').val())) {
                        alert("Spaces Are Not Allowed in Mobile No");
                        return false;
                    }
                }
                if ($('#panno').val() != '') {
                    if ($('#panno').val().length < 10 || $('#panno').val().length > 10) {
                        alert("Please enter valid PAN No");
                        $('#panno').focus();
                        return false;
                    } else if ($('#panno').val().length == 10) {
                        var alphanum = /^[0-9a-zA-Z]+$/;
                        if (!$('#panno').val().match(alphanum)) {
                            alert("Please enter valid PAN No");
                            $('#panno').focus();
                            return true;
                        }
                    }
                }

                if ($('#sltBank').val() == "") {
                    alert("Please select Bank");
                    return false;
                }
                if ($('#sltbranch').val() == "") {
                    alert("Please select Branch");
                    return false;
                }
                if ($('#bankaccno').val() == "") {
                    alert("Please enter Bank Acc No");
                    return false;
                } else if ($('#bankaccno').val() != "") {
                    var string = $('#bankaccno').val();
                    /*for (var i = 0; len = string.length; i < len; ++i) {
                     if (string.charAt(i) === ' ') {
                     alert('Bank Acc No should not have spaces!');
                     return false;
                     }
                     }*/
                }
                if ($('#marital').val() == "") {
                    alert("Please select Marital Status");
                    return false;
                }
                if ($('#category').val() == "") {
                    alert("Please select Category");
                    return false;
                }
                if ($('#doeGov').val() == "") {
                    alert("Please enter Date of Joining");
                    return false;
                }
                if ($('#joindategoo').val() == "") {
                    alert("Please enter Date of Joining in Government");
                    return false;
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
                    }
                });
            }

            function AvoidSpace(event) {
                var k = event ? event.which : window.event.keyCode;
                if (k == 32)
                    return false;
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


            /* function changeUserCatg(type) {
             alert('hi');
             
             if (type == 'A') {
             alert("Enter User Category");
             if ($("#empType").val() == "A") {
             alert('hiii');
             $("#usrcatgnm").focus();
             $("#usrcatg").show();
             $("#usrcatgnm").show();
             } else {
             $("#usrcatg").hide();
             $("#usrcatgnm").hide();
             }
             } else {
             $("#usrcatg").hide();
             $("#usrcatgnm").hide();
             }
             // alert($('#empType').val());
             
             
             }*/

        </script>

    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form:form class="form-inline" action="updateEmployeeData.htm" method="POST" commandName="EmpoyeeData">
                        <form:hidden path="empid"/>
                        <form:hidden path="hidmsg" id="hidmsg"/>
                        <c:if test = "${not empty isduplicate && isduplicate eq 'Y'}"> 
                            <div class="alert alert-danger"><strong>This Employee already exists</strong></div>
                        </c:if>
                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Manage Employee Details
                            </tr>
                            <tr>
                                <td align="right">Title :<strong style='color:red'>*</strong>  </td>
                                <td>
                                    <form:select path="intitals" id="title"  size="1" class="form-control"     >
                                        <form:option value="">-Select-</form:option>
                                        <form:options items="${empTitleList}" itemLabel="label" itemValue="value"/>                                                                                
                                    </form:select> 

                                </td>
                                <td align="right">First Name :<strong style='color:red'>*</strong></td>
                                <td>
                                    <!--<form:input path="fname" id='firstnmae' class="form-control" readonly="readonly" />-->
                                    ${EmpoyeeData.fname}
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Middle Name :</td>
                                <td>
                                    <!--<form:input path="mname" class="form-control"/>-->
                                    ${EmpoyeeData.mname}
                                </td>
                                <td align="right">Last Name :<strong style='color:red'>*</strong></td>
                                <td>
                                    <!-- <form:input path="lname" id='lname' class="form-control"/>-->
                                    ${EmpoyeeData.lname}
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Gender :<strong style='color:red'>*</strong></td>
                                <td>
                                    <form:select path="gender" id='gender'   class="form-control">
                                        <form:option value="">-Select-</form:option>
                                        <form:option value="M">Male</form:option>
                                        <form:option value="F">Female</form:option>
                                        <form:option value="T">Transgender</form:option>
                                    </form:select>
                                </td> 
                                <td align="right">Mobile :<strong style='color:red'>*</strong></td>
                                <td>
                                    <c:if test="${loginusertype eq 'A' && (username eq '311' || username eq '312')}">
                                        <form:input path="mobile" id="mobile" class="form-control" maxlength="10" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${username ne '311' && username ne '312'}">
                                        <input type="hidden" id="hidmobile" name="mobile" value="${EmpoyeeData.mobile}"/>
                                        <form:input path="mobile" id="mobile" class="form-control" maxlength="10" onkeypress="return onlyIntegerRange(event)" disabled="true"/>
                                    </c:if>
                                </td>
                            </tr>


                            <!-- <td align="right" ><input type='radio' name='relation' value='Father' required>Father Name/<input type='radio' name='relation' value='Husband' required>Husband Name :<strong style='color:red'>*</strong></td>
                              <td colspan='4'>
                                  <input name="faname" id="faname" class="form-control" required="true"  type='text'>
                              </td>-->


                            <tr height="40px">

                                <td align="right">GPF/TPF/PRAN Assumed(Set Yes for Dummy): <strong style='color:red'>*</strong></td>
                                <td>
                                    <c:if test="${loginusertype eq 'A' && (username eq '311'|| username eq '312')}">
                                        <form:select path="sltGPFAssmued" id="sltGPFAssmued" style="width:34%;" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:option value="Y">Yes</form:option>
                                            <form:option value="N">No</form:option>
                                        </form:select>
                                    </c:if>
                                    <c:if test="${username ne '311' && username ne '312'}">
                                        <input type="hidden" id="sltGPFAssmued" name="sltGPFAssmued" value="${EmpoyeeData.sltGPFAssmued}"/>
                                        <form:select path="sltGPFAssmued" id="sltGPFAssmued" style="width:34%;" class="form-control" disabled="true">
                                            <form:option value="">--Select--</form:option>
                                            <form:option value="Y">Yes</form:option>
                                            <form:option value="N">No</form:option>
                                        </form:select>
                                    </c:if>
                                </td>

                                <td align="right">Post Group</td>
                                <td>
                                    <form:select path="postGrpType" style="width:34%;" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="A">A</form:option>
                                        <form:option value="B">B</form:option>
                                        <form:option value="C">C</form:option>
                                        <form:option value="D">D</form:option>
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Account Type :<strong style='color:red'>*</strong></td>
                                <td>


                                    <%--<span id="accountTypeAuthusr">--%>
                                    <c:if test="${loginusertype eq 'A' && (username eq '311'|| username eq '312')}">
                                        <form:select path="accttype" id='accountType' class="form-control" onchange="display_type(this.value)">
                                            <form:option value="">-Select-</form:option>
                                            <form:option value="GPF">GPF</form:option>
                                            <form:option value="PRAN">PRAN</form:option>
                                            <form:option value="TPF">TPF</form:option>
                                            <form:option value="EPF">EPF</form:option>
                                            <form:option value="GIA">GIA</form:option>
                                        </form:select>
                                    </c:if>
                                    <c:if test="${username ne '311' && username ne '312'}">
                                        <input type="hidden" id="hidaccttype" name="accttype" value="${EmpoyeeData.accttype}"/>
                                        <form:select path="accttype" id='accountType' class="form-control" onchange="display_type(this.value)" disabled="true">
                                            <form:option value="">-Select-</form:option>
                                            <form:option value="GPF">GPF</form:option>
                                            <form:option value="PRAN">PRAN</form:option>
                                            <form:option value="TPF">TPF</form:option>
                                            <form:option value="EPF">EPF</form:option>
                                            <form:option value="GIA">GIA</form:option>
                                        </form:select>
                                    </c:if>

                                </td>  
                                <td align="right">GPF/PRAN/TPF Number :<strong style='color:red'>*</strong></td>
                                <td>
                                    <c:if test="${loginusertype eq 'A' && (username eq '311'|| username eq '312')}">
                                        <form:input path="gpfno" id='gpfno' class="form-control"/>
                                    </c:if>
                                    <c:if test="${username ne '311' &&  username ne '312'}">
                                        <%--<input type="hidden" id="hidgpfno" name="gpfno" value="${EmpoyeeData.gpfno}"/>--%>
                                        <form:input path="gpfno" id='gpfno' class="form-control"  readonly="true"/>
                                    </c:if>
                                </td>

                            </tr>

                            <tr>
                                <td align="right">DOB :</td>
                                <td>

                                    <div class="input-group date" id="processDate">
                                        <!--  <form:input path="dob" id="dob" readonly="true" class="form-control"/>
                                          <span class="input-group-addon">
                                              <span class="glyphicon glyphicon-time"></span>
                                          </span>-->
                                        ${EmpoyeeData.dob}
                                    </div>
                                </td>
                                <td align="right">DOS :</td>
                                <td>

                                    <div class="input-group date" id="processDate">
                                        <form:input path="dos" id="dos" readonly="true" class="form-control"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Aadhaar Number : </td>
                                <td><form:input path="aadhaarno" id='aadhaarno' class="form-control"/></td>
                                <td align="right">Pan Number: </td>
                                <td>
                                    <form:input path="panno" id="panno" class="form-control"/>
                                    <c:if test="${EmpoyeeData.panno ne ''}">
                                        <c:if test="${fn:length(EmpoyeeData.panno) ne 10}">
                                            <span style="color:red;">Invalid PAN</span>
                                        </c:if>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Is UGC for 7th Pay Revision Option : </td>
                                <td>
                                    <form:select path="chkUGC" class="form-control">
                                        <form:option value="">No</form:option>
                                        <form:option value="UGC">Yes</form:option>
                                    </form:select>
                                </td>
                                <td align="right">Allotment Year : </td>
                                <td><form:input path="empAllotmentYear" id='empAllotmentYear' class="form-control" maxlength="4"/></td>
                            </tr>
                            <tr height="40px">
                                <td align="right">Personal Identification Mark :</td>
                                <td><form:input path="idmark" class="form-control" style="width:100%;" maxlength="200"/></td>

                                <td align="right">Marital Status:<strong style="color:red">*</strong></td>
                                <td>
                                    <form:select id="marital" path="marital" style="width:34%;" class="form-control">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${maritallist}" var="marital">
                                            <form:option value="${marital.maritalId}" label="${marital.maritalStatus}"/>
                                        </c:forEach>                                 
                                    </form:select>
                                </td>
                            </tr>
                            <tr height="40px">
                                <td align="right">Category:<strong style="color:red">*</strong></td>
                                <td>
                                    <form:select id="category" path="category" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>                                 
                                    </form:select>
                                </td>

                                <td align="right">Blood Group:<strong style="color:red">*</strong></td>
                                <td>
                                    <form:select id="bloodgrp" path="bloodgrp" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${bloodGroupList}" var="bloodGrp">
                                            <form:option value="${bloodGrp.bloodgrpId}" label="${bloodGrp.bloodgrp}"/>
                                        </c:forEach>                                 
                                    </form:select>
                                </td>
                            </tr>
                            <tr height="40px">
                                <td align="right">Religion:<strong style="color:red">*</strong></td>
                                <td>
                                    <form:select id="religion" path="religion" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${religionList}" var="religion">
                                            <form:option value="${religion.religionId}" label="${religion.religion}"/>
                                        </c:forEach>                                 
                                    </form:select>
                                </td>
                                <td align="right"></td>
                                <td> 
                                </td>
                            </tr>
                            <tr height="40px">
                                <c:choose>
                                    <c:when test="${(username eq '311' || username eq '312')}">
                                        <c:if test="${EmpoyeeData.empType eq 'Y' || EmpoyeeData.empType eq 'W' || EmpoyeeData.empType eq 'G' || EmpoyeeData.empType eq 'A' || EmpoyeeData.empType eq 'N' || EmpoyeeData.empType eq 'B'}">
                                            <td align="right">Change Employee Type:<strong style="color:red">*</strong></td>
                                            <td>
                                                <form:select id="empType" path="empType" class="form-control" style="width:60%;">
                                                    <form:option value="Y" label="REGULAR" id="emptype_Y" onclick="changeUserCatg('Y');"/>
                                                    <form:option value="G" label="WAGES" id="emptype_G" onclick="changeUserCatg('G');"/>
                                                    <form:option value="W" label="WORK CHARGE" id="emptype_W" onclick="changeUserCatg('W');"/>
                                                    <%-- <form:option value="L" label="BLOCK GRANT"/> --%>
                                                    <form:option value="A" label="SPECIAL CATEGORY" id="emptype_A" onclick="changeUserCatg('A');"/>
                                                    <form:option value="N" label="CONTRACTUAL" id="emptype_N" onclick="changeUserCatg('N');"/>
                                                    <form:option value="B" label="LEVEL-V(EX-CADRE)" id="emptype_B" onclick="changeUserCatg('B');"/>
                                                </form:select>
                                            </td>
                                        <div>
                                            <td align="right" id="usrcatg">Special User Category:</td>
                                            <td id="usrcatgnm"> 
                                                <form:select id="spluserCategory" path="spluserCategory" class="form-control" style="width:60%;">
                                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                                    <c:forEach items="${splSubCatgList}" var="splcatg">
                                                        <form:option value="${splcatg.spluserCategory}" label="${splcatg.spluserCategory}"/>
                                                    </c:forEach>                                 
                                                </form:select>
                                            </td>
                                        </div>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${EmpoyeeData.empType eq 'Y' || EmpoyeeData.empType eq 'W' || EmpoyeeData.empType eq 'G' }">
                                        <td align="right">Change Employee Type:<strong style="color:red">*</strong></td>
                                        <td>
                                            <form:select id="empType" path="empType" class="form-control" style="width:60%;">
                                                <form:option value="Y" label="REGULAR" id="emptype1_Y" />
                                                <form:option value="G" label="WAGES" id="emptype1_G" />
                                                <form:option value="W" label="WORK CHARGE" id="emptype1_W"/>                                                
                                            </form:select>
                                        </td>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                                        

                            <%--<c:if test="${EmpoyeeData.empType ne 'Y' && EmpoyeeData.empType ne 'W' && EmpoyeeData.empType ne 'G'  }">
                                <form:hidden path="empType"/>
                            </c:if>--%>
                            <c:if test="${EmpoyeeData.empType ne 'Y'}">
                                <td colspan="2">&nbsp;</td>
                            </c:if>
                            </tr>
                            <tr height="40px">
                                <td align="right">Date of entry into Govt. service:<strong style="color:red">*</strong></td>
                                <td>
                                    <div class="input-group date" id="processDate">
                                        <form:input path="doeGov" id="doeGov" readonly="true" class="form-control txtDate"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </td>
                                <td align="right">Time &nbsp;<span style="color: red">*</span></td>
                                <td>  
                                    <form:select id="timeOfEntryGoo" class="form-control" path="timeOfEntryGoo" style="width:30%;">
                                        <c:if test="${EmpoyeeData.timeOfEntryGoo!='FN' && EmpoyeeData.timeOfEntryGoo!='AN'}">
                                    <option value="">--Select One--</option>
                                    <option value="FN">FORENOON</option>
                                    <option value="AN">AFTERNOON</option>
                                </c:if>
                                <c:if test="${EmpoyeeData.timeOfEntryGoo=='FN'}">
                                    <option value="">--Select One--</option>
                                    <option value="FN" selected>FORENOON</option>
                                    <option value="AN">AFTERNOON</option> 
                                </c:if>
                                <c:if test="${EmpoyeeData.timeOfEntryGoo=='AN'}">
                                    <option value="">--Select One--</option>
                                    <option value="FN">FORENOON</option>
                                    <option value="AN"  selected>AFTERNOON</option> 
                                </c:if>
                            </form:select> 
                            </td>
                            </tr>
                            <tr height="40px">
                                <td align="right">Date of Joining in Government:<strong style="color:red">*</strong></td>
                                <td>
                                    <div class="input-group date" id="processDate">
                                        <form:input path="joindategoo" id="joindategoo" readonly="true" class="form-control txtDate"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </td>
                                <td align="right" >Time &nbsp;<span style="color: red">*</span></td>
                                <td>
                                    <form:select id="txtwefTime" class="form-control" path="txtwefTime" style="width:30%;">
                                        <c:if test="${EmpoyeeData.txtwefTime!='FN' && EmpoyeeData.txtwefTime!='AN'}">
                                    <option value="">--Select One--</option>
                                    <option value="FN">FORENOON</option>
                                    <option value="AN">AFTERNOON</option>
                                </c:if>
                                <c:if test="${EmpoyeeData.txtwefTime=='FN'}">
                                    <option value="">--Select One--</option>
                                    <option value="FN" selected>FORENOON</option>
                                    <option value="AN">AFTERNOON</option> 
                                </c:if>
                                <c:if test="${EmpoyeeData.txtwefTime=='AN'}">
                                    <option value="">--Select One--</option>
                                    <option value="FN">FORENOON</option>
                                    <option value="AN"  selected>AFTERNOON</option> 
                                </c:if>
                            </form:select>   
                            </td>
                            </tr>
                            <tr height="40px">
                                <td align="right">Height:</td>
                                <td><form:input path="height" class="form-control" style="width:100%;" maxlength="6"/></td>
                                <td align="right">Domicile:</td>
                                <td><form:input path="domicile" class="form-control" maxlength="100"/></td>
                            </tr>
                            <tr height="40px">
                                <td align="right">Home Town:</td>
                                <td><form:input path="homeTown" class="form-control" maxlength="100"/></td>
                                <td align="right">Email:</td>
                                <td><form:input path="email" class="form-control" maxlength="50"/></td>
                            </tr>
                            <tr>
                                <td align="right">GIS TYPE :</td>
                                <td>
                                    <form:select id="gisType" path="gisType" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${gisNumberList}" var="gisType">
                                            <form:option value="${gisType.schemeId}" label="${gisType.schemeName}"/>
                                        </c:forEach>                                 
                                    </form:select>
                                </td> 
                                <td align="right">GIS NUMBER :</td>
                                <td><form:input path="gisNo" id="gisNo" class="form-control"/></td>
                            </tr>
                            <tr height="40px">
                                <td align="right">Is Employee under any Reservation Category:</td>
                                <td>
                                    <form:select path="ifReservation" style="width:34%;" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="Y">Yes</form:option>
                                        <form:option value="N">No</form:option>
                                    </form:select>
                                </td>
                                <td align="right">Is Employee under Rehabilitation Assistance Scheme:</td>
                                <td>
                                    <form:select path="ifRehabiltation" style="width:34%;" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="Y">Yes</form:option>
                                        <form:option value="N">No</form:option>
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Brass Number :</td>
                                <td><form:input path="brassno" id="brassno" class="form-control"/></td>
                                <c:if test="${EmpoyeeData.empType eq 'Y' || EmpoyeeData.empType eq 'W' || EmpoyeeData.empType eq 'G' || EmpoyeeData.empType eq 'C'||EmpoyeeData.empType eq 'D'}">
                                    <td align="right">Pay Commission :</td>                               
                                    <td>
                                        <form:select path="sltPayCommission" id="sltPayCommission" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:option value="5">5</form:option>                                        
                                            <form:option value="6">6</form:option>
                                            <form:option value="7">7</form:option>
                                        </form:select>
                                    </td>
                                </c:if>
                            </tr>
                            <c:if test="${loginusertype eq 'A' && (username eq '65' || username eq '60'|| username eq '59'|| username eq '61')}">
                                <tr>
                                    <td align="right">If Re-engaged :</td>
                                    <td>
                                        <form:select path="isReengaged" id="isReengaged" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:option value="N">No</form:option>                                        
                                            <form:option value="Y">Yes</form:option>
                                        </form:select>
                                    </td>
                                    <td align="right">EMP Non-PRAN :</td>                               
                                    <td>
                                        <form:select path="empNonPran" id="empNonPran" class="form-control">
                                            <form:option value="N">No</form:option>                                        
                                            <form:option value="Y">Yes</form:option>
                                        </form:select>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${loginusertype ne 'A' && username ne '65' && username ne '60' && username ne '59' && username ne '61'}">
                                <form:hidden path="isReengaged"/>
                                <form:hidden path="empNonPran"/>
                            </c:if>
                            <tr>                                
                                <c:if test="${EmpoyeeData.empType eq 'Y' || EmpoyeeData.empType eq 'W' || EmpoyeeData.empType eq 'G' || EmpoyeeData.empType eq 'C'}">
                                    <td align="right">Stop NPS Deduction :</td>                               
                                    <td>
                                        <c:out value="${EmpoyeeData.stopPayNPS}"/>
                                    </td>
                                </c:if>
                                <td align="right">Is Employee under Law Department applicable to take New Pay Fixation:</td>
                                <td>
                                    <form:select path="lawLevel" style="width:34%;" class="form-control">
                                        <form:option value="Y">Yes</form:option>
                                        <form:option value="N">No</form:option>
                                    </form:select>
                                </td>
                            </tr>
                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" value="Save Employee Details" class="btn btn-success" name="action" onclick="javascript:return hasSpace();"  />
                        </div>
                    </form:form>
                </div>
            </div>

        </div>
    </div>

    <%--<script type="text/javascript">
                    
        $(function() {
            //$('#dob').datepicker({dateFormat: 'yy-mm-dd'});
            //$('#doj').datepicker({dateFormat: 'yy-mm-dd'});
            ;
            $("#dob").datepicker({
                changeMonth: true,
                changeYear: true,
                dateFormat: 'yy-mm-dd',
                yearRange: '1955:2025',
            });
        });
        function validate() {


                var etitle = $("#title").val();


                if (etitle == "") {
                    alert("Please select your title");
                    return false;
                }
                var firstnmae = $("#firstnmae").val();
                if (firstnmae == "") {
                    alert("Please Enter First Name");
                    return false;
                }
                var lname = $("#lname").val();
                if (lname == "") {
                    alert("Please Enter your Last Name");
                    return false;
                }
                var gender = $("#gender").val();
                if (gender == "") {
                    alert("Please select your Gender");
                    return false;
                }
                var mobile = $("#mobile").val();
                if (mobile == "") {
                    alert("Please Enter Your Mobile No");
                    return false;
                }
                if (isNaN(mobile)) {
                    alert("Invalid Mobile No");
                    return false;
                }
                var accountType = $("#accountType").val();
                if (accountType == "") {
                    alert("Please Select your Account Type");
                    return false;
                }
                var gpfno = $("#gpfno").val();
                if (gpfno == "") {
                    alert("Please Enter GPF/PRAN/TPF Number");
                    return false;
                }

                var dob = $("#dob").val();
                ;
                if (dob == "") {
                    alert("Please Select DOB");
                    return false;
                }

                return true;

            }
        </script>--%>
</body>
<script type="text/javascript">
    $(function() {
        $('#dob').datetimepicker({
            format: 'D-MMM-YYYY',
            useCurrent: false,
            ignoreReadonly: true
        });

        $('#dos').datetimepicker({
            format: 'D-MMM-YYYY',
            useCurrent: false,
            ignoreReadonly: true
        });
    });
</script>
</html>
