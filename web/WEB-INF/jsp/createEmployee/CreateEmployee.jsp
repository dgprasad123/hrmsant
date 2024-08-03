<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Create Employee ::</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        

        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script type="text/javascript">

            $(document).ready(function() {
                $("#giaType").hide();
                $("#gptNo").hide();
                $("#giano").hide();
                $("#gpftpf").show();
                $("#gpftpfno").show();
                $("#ifassumed").prop("checked", true);
                if ($("#empType").val() == 'D') {
                    $("#epfacct").hide();
                }
            });
            
            function differenceInYears(date1, date2) {
                const diffMilliseconds = Math.abs(date1 - date2);
                const millisecondsInYear = 1000 * 60 * 60 * 24 * 365.25; // Average milliseconds in a year (considering leap years)
                const differenceYears = diffMilliseconds / millisecondsInYear;
                return Math.floor(differenceYears);
            }
            
            function validate() {
                var atype = $("#accountType").val();
                var gtype = $("#gpfType").val();
                var ttype = $("#tpfType").val();
                var etitle = $("#title").val();
                var aadharNo = $("#aadharNo").val();
                if (etitle == "") {
                    alert("Please select  Title");
                    return false;
                }
                var firstnmae = $("#firstnmae").val();
                if (firstnmae == "") {
                    alert("Please Enter First Name");
                    return false;
                }
                /*var lname = $("#lname").val();
                 if (lname == "") {
                 alert("Please Enter Last Name");
                 return false;
                 }*/
                var gender = $("#gender").val();
                if (gender == "") {
                    alert("Please select Gender");
                    return false;
                }
                var mobile = $("#mobile").val();
                if (mobile == "") {
                    alert("Please Enter Mobile No");
                    return false;
                }
                if (isNaN(mobile)) {
                    alert("Invalid Mobile No");
                    return false;
                }
                var empType = $("#empType").val();
                if (empType == "") {
                    alert("Please Select Employee Type");
                    return false;
                } else if (empType == 'N') {
                    if ($("#post").val() == '') {
                        alert('Please provide Post');
                        $("#post").focus();
                        return false;
                    }
                }
                if ($("#basicPay").val() == '') {
                    alert("Please enter which will reflect in salary");
                    $("#basicPay").focus();
                    return false;
                }

                var accountType = $("#accountType").val();
                if (accountType == "") {
                    alert("Please Select your Account Type");
                    return false;
                }
                var rTitle = $("#rTitle").val();
                if (rTitle == "") {
                    alert("Please select your Father/Husband Title");
                    return false;
                }
                var rFirstnmae = $("#rFirstnmae").val();
                if (rFirstnmae == "") {
                    alert("Please Enter  Father/Husband First Name");
                    return false;
                }
                var rLname = $("#rLname").val();
                if (rLname == "") {
                    alert("Please Enter Father/Husband Last Name");
                    return false;
                }
                var gptNumber = $("#gptNumber").val();
                var acctType = $("#accountType").val();
                if (acctType == 'GPF' || acctType == 'TPF' || acctType == 'PRAN' || acctType == 'EPF') {
                    if (gptNumber == "") {
                        alert("Please Enter GPF/PRAN/TPF Number");
                        return false;
                    }
                }

                if (isNaN(gptNumber)) {
                    alert("Please Enter Only Numeric Value in GPF/PRAN/TPF Number");
                    return false;
                }

                if (atype == "GPF" && gtype == "") {
                    alert("Please Select GPF Type");
                    return false;
                }

                if (atype == "TPF" && ttype == "") {
                    alert("Please Select TPF Type");
                    return false;
                }

                var dob = $("#dob").val();
                if (dob == "") {
                    alert("Please Select DOB");
                    return false;
                } else {
                    var dobArr = dob.split("-");
                    var dobYear = dobArr[2];
                    var dobMonth = dobArr[1];
                    var dobDay = dobArr[0];

                    var dobDate = new Date(dobDay + "-" + dobMonth + "-" + dobYear);
                    var today = new Date();
                    var diff = differenceInYears(today,dobDate);
                    if(parseInt(diff) < 18 || parseInt(diff) > 80){
                        alert("Employee must have minimum age of 18 and maximum age of 80 for entry.");
                        return false;
                    }else{
                        return true;
                    }
                }
                var doj = $("#doj").val();
                if (doj == "") {
                    alert("Please Select DOJ");
                    return false;
                }

                if (aadharNo == "") {
                    alert("Please Enter Aadhar No");
                    return false;
                }

                if ($("#postGroup").val() == '') {
                    alert('Please select Post Group');
                    $("#postGroup").focus();
                    return false;
                }

                if ($('#sltPayCommission').val() == '') {
                    alert('Please select Pay Commission');
                    $("#sltPayCommission").focus();
                    return false;
                }
                return false;
            }

            function display_type(vals) {
                if (vals != 'PRAN' && vals != 'EPF' && vals != 'GIA' && vals != 'BGT') {
                    $("#id_gpf_tpf").show();
                    $("#giaType").hide();
                    $("#giano").hide();
                    if (vals == "GPF") {
                        $(".gpf_type").show();
                        $(".tpf_type").hide();
                        $("#giaType").hide();
                        $("#giano").hide();
                        $("#naEpfMsg").hide();
                    }
                    if (vals == "TPF") {
                        $(".gpf_type").hide();
                        $(".tpf_type").show();
                        $("#giaType").hide();
                        $("#giano").hide();
                        $("#naEpfMsg").hide();
                        $("#id_gpf_tpf").show();
                    }
                    $("#giaType").hide();
                    $("#giano").hide();
                    $("#gpftpf").show();
                    $("#gpftpfno").show();
                    $("#naEpfMsg").hide();
                } else if (vals == 'GIA' || vals == 'BGT') {
                    //$("#giaType").show();
                    $("#giaType").hide();
                    $("#giano").show();
                    $("#gpftpf").hide();
                    $("#gpftpfno").hide();
                    //$("#gptNo").show();
                    $("#gptNo").hide();
                    $("#naEpfMsg").hide();

                    $.ajax({
                        url: 'generateNewAcctNo.htm?acctType=' + vals,
                        dataType: "json",
                        success: function(data) {
                            if (data.status != '') {
                                $("#gptNo").val(data.status);
                            }
                        }
                    });
                }
                else if (vals == 'EPF') {
                    if (${ddoCatg eq 'B'}) {
                        $("#naEpfMsg").show().text("You Are Not Allowed in EPF Category");
                        $("#gpftpf").hide();
                        $("#gpftpfno").hide();
                        $("#giaType").hide();
                        $("#giano").hide();
                        $("#accountType").val("");
                        alert("Select Account Type Other Than EPF");
                    } else {
                        $("#naEpfMsg").hide();
                        $("#giaType").hide();
                        $("#giano").hide();
                        $("#gpftpf").show();
                        $("#gpftpfno").show();
                    }
                }
                else {
                    $("#id_gpf_tpf").hide();
                    $("#gpftpf").show();
                    $("#gpftpfno").show();
                    $("#giaType").hide();
                    $("#giano").hide();
                }
            }
            function PostTextBox(vals) {
                // alert(vals);
                if (vals == 'N' || vals == 'D') {
                    $(".post_details").show();
                } else {
                    $(".post_details").hide();
                    $("#post").val("");
                }
            }
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
                    <form class="form-inline" action="saveEmployeeData.htm" method="POST" onsubmit="return validate();" >
                        <input type='hidden' name='offCode' value='${offcode}'/>
                        <c:if test = "${not empty isduplicate && isduplicate=='Y'}"> 
                            <div class="alert alert-danger" style="font-size: 13px; color: red"><strong>GPF/PRAN/TPF/EPF Number Is Duplicate</strong></div>
                        </c:if>
                        <c:if test = "${not empty isduplicate && isduplicate=='N'}"> 
                            <div class="alert alert-success" style="font-size: 13px; color: green"><strong>New Employee is created . </strong><br/></div>
                        </c:if>
                        <c:if test="${ddoCatg eq 'B'}">
                            <span id="naEpfMsg" style="font-size: 20px; color: red; text-align: center; font-weight:bold; display: block"></span>
                        </c:if>

                        <c:if test="${not empty isduplicate && isduplicate=='A'}">
                            <div class="alert alert-danger" style="font-size: 13px; color: red"><strong>Aadhaar No already exits</strong></div>                             
                        </c:if>
                        <c:if test="${not empty isduplicate && isduplicate=='M'}">
                            <div class="alert alert-danger" style="font-size: 13px; color: red"><strong>Mobile No already exits</strong></div>                             
                        </c:if>
                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Manage Employee Details
                            </tr>
                            <tr>
                                <td align="right">Title :<strong style='color:red'>*</strong>  </td>
                                <td>
                                    <select name="title" id="title" size="1" class="form-control">
                                        <option value="">-Select-</option>
                                        <c:forEach items="${empTitleList}" var="etitle" >
                                            <option value="${etitle.value}">${etitle.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td align="right">First Name :<strong style='color:red'>*</strong></td>
                                <td> <input name="firstnmae" id="firstnmae" class="form-control" type='text'></td>
                            </tr>
                            <tr>
                                <td align="right">Middle Name :</td>
                                <td> <input name="mname" id="mname" class="form-control" type='text'></td>
                                <td align="right">Last Name :<strong style='color:red'>*</strong></td>
                                <td> <input name="lname" id="lname" class="form-control" type='text'></td>
                            </tr>
                            <tr>
                                <td align="right">Gender :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="gender" id="gender" size="1" class="form-control">
                                        <option value="">-Select-</option>
                                        <option value="M">Male</option>
                                        <option value="F">Female</option>
                                        <option value="T">Transgender</option>
                                    </select>
                                </td> 
                                <td align="right">Mobile :<strong style='color:red'>*</strong></td>
                                <td> <input name="mobile" id="mobile" class="form-control" type='text' maxlength="10" ></td>
                            <tr>
                                <td align="right">Employee Type :<strong style='color:red'>*</strong></td>
                                <td>
                                    <c:if test="${ddoCatg ne 'B'}">
                                        <select name="empType" id="empType" size="1" class="form-control" onchange="PostTextBox(this.value)"  >
                                            <option value="">-Select-</option>
                                            <option value="Y">Regular</option>
                                            <option value="C">6 Years Contractual</option>
                                            <option value="W">Work Charged (Regular Salary)</option>
                                            <option value="G">Wages(Regular Salary)</option>
                                            <option value="B">EX-CADRE</option>
                                            <option value="N">Contractual</option>                                          
                                        </select>
                                    </c:if>
                                    <c:if test="${ddoCatg eq 'B'}">
                                        <select name="empType" id="empType" size="1" class="form-control" onchange="PostTextBox(this.value)">
                                            <option value="D">NON GOVT. AIDED</option>
                                        </select>
                                    </c:if>
                                </td> 
                                <td align="right" class='post_details'>Post :<strong style='color:red'>*</strong></td>
                                <td class='post_details'> <input name="post" id="post" class="form-control" type='text'></td>
                            </tr>
                            <tr>
                                <td align="right">Basic Pay : <strong style='color:red'>*</strong></td>
                                <td> <input name="basicpay" id="basicpay" class="form-control" type='text' value=""></td>
                                <td align="right">Grade Pay :<strong style='color:red'>*</strong></td>
                                <td> <input name="gradePay" id="gradePay" class="form-control" type='text' value="0"></td>
                            </tr>


                            <!-- <td align="right" ><input type='radio' name='relation' value='Father' required>Father Name/<input type='radio' name='relation' value='Husband' required>Husband Name :<strong style='color:red'>*</strong></td>
                              <td colspan='4'>
                                  <input name="faname" id="faname" class="form-control" required="true"  type='text'>
                              </td>-->
                            </tr>
                            <tr>
                                <td align="right">
                                    <input type='radio' name='relation' value="FATHER" checked="1">Father 
                                    <input type='radio' name='relation' value="HUSBAND" >Husband Title :<strong style='color:red'>*</strong>
                                </td>
                                <td>
                                    <select name="rTitle" id="rTitle" size="1" class="form-control">
                                        <option value="">-Select-</option>
                                        <c:forEach items="${empTitleList}" var="etitle" >
                                            <option value="${etitle.value}">${etitle.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td align="right">First Name :<strong style='color:red'>*</strong></td>
                                <td> <input name="rFirstnmae" id="rFirstnmae" class="form-control" type='text'></td>
                            </tr>
                            <tr>
                                <td align="right">Middle Name :</td>
                                <td> <input name="rMname" id="rMname" class="form-control" type='text'></td>
                                <td align="right">Last Name :<strong style='color:red'>*</strong></td>
                                <td> <input name="rLname" id="rLname" class="form-control" type='text'></td>
                            </tr>
                            <tr>
                                <td align="right">Account Type :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="accountType" id="accountType"  size="1" class="form-control" onchange="display_type(this.value)"  >
                                        <option value="">-Select-</option>
                                        <option value="GPF">GPF</option>
                                        <option value="PRAN">PRAN</option>
                                        <option value="TPF">TPF</option>
                                        <%-- <option value="EPF" id="epfacct">EPF</option>--%>
                                        <option value="BGT">BLOCK GRANT</option>
                                        <option value="GIA">GIA</option>
                                        <%--<option value="OTH">OTHERS</option>--%>
                                    </select>
                                </td>
                                <td align="right" id="giaType">Account Number :<strong style='color:red'>*</strong></td>
                                <td id="giano" style="font-size: 16px; font-weight: bold"><input name="othNumber" id="gptNo" class="form-control" type="text" readonly="true"></td>

                                <td align="right" id="gpftpf">GPF/PRAN/TPF/EPF Number :<strong style='color:red'>*</strong></td>
                                <td id="gpftpfno"> <input name="gptNumber" id="gptNumber" class="form-control" type='number'>&nbsp;&nbsp;&nbsp;<input type='checkbox' name='assumed' value='Y'/>&nbsp;Is Assumed?</td>
                            </tr>
                            <tr id='id_gpf_tpf' style='display:none'>
                                <td align="right" class='gpf_type'>GPF Type :<strong style='color:red'>*</strong></td>
                                <td class='gpf_type'>
                                    <select name="gpfType" id="gpfType" size="1" class="form-control" style="width:80%;"  >
                                        <option value="">-Select-</option>
                                        <c:forEach items="${gpfList}" var="gpfListdetails" >
                                            <option value="${gpfListdetails.value}">${gpfListdetails.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>  

                                <td align="right" class='tpf_type'>TPF Type :<strong style='color:red'>*</strong></td>
                                <td class='tpf_type'>
                                    <select name="tpfType" id="tpfType" size="1" class="form-control" style="width:80%;"  >
                                        <option value="">-Select-</option>
                                        <c:forEach items="${gpfList}" var="gpfListdetails" >
                                            <option value="${gpfListdetails.value}">${gpfListdetails.label}</option>
                                        </c:forEach>
                                    </select>
                                </td> 
                            </tr>
                            <tr>
                                <td align="right">DOB :</td>
                                <td> <input name="dob" readonly id="dob" class="form-control" type='text'></td>
                                <td align="right">DOJ in Government :<strong style='color:red'>*</strong></td>
                                <td> <input name="doj" readonly id="doj" class="form-control" type='text'></td>
                            </tr>
                            <tr>
                                <td align="right">AADHAR NO :<strong style="color:red">*</strong></td>
                                <td>
                                    <%--<input name="aadharNo" id="aadharNo" class="form-control" type="text" value="${aadhaarno}" readonly="true"/>--%>
                                    <input type="hidden" name="aadharNo" value="${aadhaarno}"/>
                                    <c:out value="${aadhaarno}"/>
                                </td>
                                <td align="right">Select Post Group :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="postGroup" id="postGroup" size="1" class="form-control">
                                        <option value="">-Select-</option>
                                        <option value="A">Group-A</option>
                                        <option value="B">Group-B</option>
                                        <option value="C">Group-C</option>
                                        <option value="D">Group-D</option>
                                    </select>
                                </td>   
                            </tr>
                            <tr>
                                <td align="right">Select Pay Commission :<strong style="color:red">*</strong></td>
                                <td>
                                    <select name="sltPayCommission" id="sltPayCommission" class="form-control">
                                        <option value="">-Select-</option>
                                        <option value="6">6th Pay</option>
                                        <option value="7">7th Pay</option>
                                    </select>
                                </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" value="Save Employee Details" class="btn btn-success"/>
                            <div class="alert alert-primary" style="color: red;font-size: 14px"><strong>(N.B.: If employee type is regular/contractual 6 yr./contractual and pran no. not allotted, then select Account type as PRAN and check the Is assumed option inside GPF/TPF/PRAN/EPF Number.)</strong></div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="/resources/demos/style.css">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script>
                                $(function() {
                                    //$('#dob').datepicker({dateFormat: 'yy-mm-dd'});
                                    //$('#doj').datepicker({dateFormat: 'yy-mm-dd'});


                                    var dtDate = new Date();
                                    var month = dtDate.getMonth();
                                    var day = dtDate.getDate();
                                    var year = dtDate.getFullYear();
                                    if (month < 0) {
                                        month = '0' + month.toString();
                                    }
                                    if (day < 0) {
                                        day = '0' + day.toString;
                                    }
                                    var maxDate = month + "/" + day + "/" + year;

                                    $("#dob").datepicker({
                                        changeMonth: true,
                                        changeYear: true,
                                        dateFormat: 'yy-mm-dd',
                                        yearRange: '1900:maxDate',
                                        maxDate: new Date(year, month, day)
                                    });
                                    $("#doj").datepicker({
                                        changeMonth: true,
                                        changeYear: true,
                                        dateFormat: 'yy-mm-dd',
                                        yearRange: '1900:maxDate',
                                        maxDate: new Date(year, month, day)
                                    });



                                });
        </script>
    </body>
</html>
