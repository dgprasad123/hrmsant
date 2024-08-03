<%-- 
    Document   : ConfigBillGroup
    Created on : Apr 14, 2018, 7:59:54 AM
    Author     : Manas
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script language="javascript" type="text/javascript">
            function savecheck() {
                if ($('#chkCol6Bill').attr('checked')) {
                    if (selectAll("sltCol6", "6") == false) {
                        return false;
                    }

                }

                if ($('#chkCol7Bill').attr('checked')) {
                    if (selectAll("sltCol7", "7") == false) {
                        return false;
                    }
                }
                if ($('#chkCol16Bill').attr('checked')) {
                    if (selectAll("sltCol16", "16") == false) {
                        return false;
                    }
                }
                if ($('#chkCol17Bill').attr('checked')) {
                    if (selectAll("sltCol17", "17") == false) {
                        return false;
                    }
                }
                if ($('#chkCol18Bill').attr('checked')) {
                    if (selectAll("sltCol18", "18") == false) {
                        return false;
                    }
                }

                if ($('#chkCol6Bill').attr('checked') && $('#chkCol7Bill').attr('checked')) {
                    var slt1 = alertDuplicateSelect("sltCol61");
                    var slt2 = alertDuplicateSelect("sltCol62");
                    var slt3 = alertDuplicateSelect("sltCol63");
                    var slt4 = alertDuplicateSelect("sltCol64");
                    var slt5 = alertDuplicateSelect("sltCol65");
                    var slt6 = alertDuplicateSelect("sltCol66");
                    var slt7 = alertDuplicateSelect("sltCol67");
                    var slt8 = alertDuplicateSelect("sltCol68");

                    if (slt1 == "0" || slt2 == "0" || slt3 == "0" || slt4 == "0" || slt5 == "0" || slt6 == "0" || slt7 == "0" || slt8 == "0") {
                        return false;
                    }

                }

            }

            function alertDuplicateSelect(compObj) {
                var sltcol6val = $("#" + compObj).val();
                var objName = "sltCol7";
                var sltt1 = $("#" + objName + "1").val();
                var sltt2 = $("#" + objName + "2").val();
                var sltt3 = $("#" + objName + "3").val();
                var sltt4 = $("#" + objName + "4").val();
                var sltt5 = $("#" + objName + "5").val();
                var sltt6 = $("#" + objName + "6").val();
                var sltt7 = $("#" + objName + "7").val();
                var sltt8 = $("#" + objName + "8").val();
                if (sltcol6val == sltt1 || sltcol6val == sltt2 || sltcol6val == sltt3 || sltcol6val == sltt4 || sltcol6val == sltt5 || sltcol6val == sltt6 || sltcol6val == sltt7 || sltcol6val == sltt8) {
                    alert("Duplicate Select");
                    return "0";
                } else {
                    return "1";
                }


            }

            function selectAll(objName, cloname) {
                var sltt1 = $("#" + objName + "1").val();
                var sltt2 = $("#" + objName + "2").val();
                var sltt3 = $("#" + objName + "3").val();
                var sltt4 = $("#" + objName + "4").val();
                var sltt5 = $("#" + objName + "5").val();
                var sltt6 = $("#" + objName + "6").val();
                var sltt7 = $("#" + objName + "7").val();
                var sltt8 = $("#" + objName + "8").val();
                if (cloname == "6" || cloname == "7") {
                    if (sltt1 == '' || sltt2 == '' || sltt3 == '' || sltt4 == '' || sltt5 == '' || sltt6 == '' || sltt7 == '' || sltt8 == '') {
                        alert('You have to select all 8 from column ' + cloname);
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (sltt1 == '' || sltt2 == '' || sltt3 == '' || sltt4 == '' || sltt5 == '') {
                        alert('You have to select all 5 from column ' + cloname);
                        return false;
                    } else {
                        return true;
                    }
                }


            }

            function checkDuplicateSelect(objName, serialNumber) {

                var sltt1 = $("#" + objName + "1").val();                
                var sltt2 = $("#" + objName + "2").val();
                var sltt3 = $("#" + objName + "3").val();
                var sltt4 = $("#" + objName + "4").val();
                var sltt5 = $("#" + objName + "5").val();
                var sltt6 = $("#" + objName + "6").val();
                var sltt7 = $("#" + objName + "7").val();
                var sltt8 = $("#" + objName + "8").val();
                if (serialNumber == "1" && sltt1 != '') {
                    if (sltt1 == sltt2 || sltt1 == sltt3 || sltt1 == sltt4 || sltt1 == sltt5 || sltt1 == sltt6 || sltt1 == sltt7 || sltt1 == sltt8) {
                        alert("Duplicate Select");
                        $("#" + objName + "1").val("");
                        return false;
                    }
                } else if (serialNumber == "2" && sltt2 != '') {
                    if (sltt2 == sltt1 || sltt2 == sltt3 || sltt2 == sltt4 || sltt2 == sltt5 || sltt2 == sltt6 || sltt2 == sltt7 || sltt2 == sltt8) {
                        alert("Duplicate Select");
                        $("#" + objName + "2").val("");
                        return false;
                    }
                } else if (serialNumber == "3" && sltt3 != '') {
                    if (sltt3 == sltt1 || sltt3 == sltt2 || sltt3 == sltt4 || sltt3 == sltt5 || sltt3 == sltt6 || sltt3 == sltt7 || sltt3 == sltt8) {
                        alert("Duplicate Select");
                        $("#" + objName + "3").val("");
                        return false;
                    }
                } else if (serialNumber == "4" && sltt4 != '') {
                    if (sltt4 == sltt1 || sltt4 == sltt2 || sltt4 == sltt3 || sltt4 == sltt5 || sltt4 == sltt6 || sltt4 == sltt7 || sltt4 == sltt8) {
                        alert("Duplicate Select");
                        $("#" + objName + "4").val("");
                        return false;
                    }
                } else if (serialNumber == "5" && sltt5 != '') {
                    if (sltt5 == sltt1 || sltt5 == sltt2 || sltt5 == sltt3 || sltt5 == sltt4 || sltt5 == sltt6 || sltt5 == sltt7 || sltt5 == sltt8) {
                        alert("Duplicate Select");
                        $("#" + objName + "5").val("");
                        return false;
                    }
                } else if (serialNumber == "6" && sltt6 != '') {
                    if (sltt6 == sltt1 || sltt6 == sltt2 || sltt6 == sltt3 || sltt6 == sltt4 || sltt6 == sltt5 || sltt6 == sltt7 || sltt6 == sltt8) {
                        alert("Duplicate Select");
                        $("#" + objName + "6").val("");
                        return false;
                    }
                }
                else if (serialNumber == "7" && sltt7 != '') {
                    if (sltt7 == sltt1 || sltt7 == sltt2 || sltt7 == sltt3 || sltt7 == sltt4 || sltt7 == sltt5 || sltt7 == sltt6 || sltt7 == sltt8) {
                        alert("Duplicate Select");
                        $("#" + objName + "7").val("");
                        return false;
                    }
                }
                else if (serialNumber == "8" && sltt8 != '') {
                    if (sltt8 == sltt1 || sltt8 == sltt2 || sltt8 == sltt3 || sltt8 == sltt4 || sltt8 == sltt5 || sltt8 == sltt6 || sltt8 == sltt7) {
                        alert("Duplicate Select");
                        $("#" + objName + "8").val("");
                        return false;
                    }
                }

            }

            function onCheckChange(chkbox, objName) {
                if ($("#" + chkbox).is(':checked')) {
                    $("#" + objName + "1").removeAttr('disabled');
                    $("#" + objName + "2").removeAttr('disabled');
                    $("#" + objName + "3").removeAttr('disabled');
                    $("#" + objName + "4").removeAttr('disabled');
                    $("#" + objName + "5").removeAttr('disabled');
                    $("#" + objName + "6").removeAttr('disabled');
                    $("#" + objName + "7").removeAttr('disabled');
                    $("#" + objName + "8").removeAttr('disabled');
                } else {
                    $("#" + objName + "1").attr('disabled', 'disabled');
                    $("#" + objName + "2").attr('disabled', 'disabled');
                    $("#" + objName + "3").attr('disabled', 'disabled');
                    $("#" + objName + "4").attr('disabled', 'disabled');
                    $("#" + objName + "5").attr('disabled', 'disabled');
                    $("#" + objName + "6").attr('disabled', 'disabled');
                    $("#" + objName + "7").attr('disabled', 'disabled');
                    $("#" + objName + "8").attr('disabled', 'disabled');
                }
            }

            
        </script>
    </head>
    <body>
        <form:form action="saveBillConfiguration.htm" commandName="billGroupConfig">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">

                        <table class="table table-bordered">                            
                            <tr>
                                <td colspan="2">
                                    <form:hidden path="billgroupid"/>
                                    <form:hidden path="officeCode"/>
                                    Bill Format : <form:radiobutton path="billFormat"  value="PDF" styleId="rdoBillFormatPDF"/> PDF Format &nbsp;
                                    <form:radiobutton path="billFormat" value="DMP"  styleId="rdoBillFormatDMP"/> DMP Format
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    Apply to all Bill : <form:checkbox path="applyToAllBill"  value="All" styleId="rdoBillFormatPDF"/>                                    
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Column 5 Configuration
                                </td>
                                <td>
                                    <form:checkbox value="16" path="chkCol6Bill" onclick="onCheckChange('chkCol6Bill','sltCol6')"/> Column 6 Configuration
                                </td>
                            </tr>
                            <tr>
                                <td width="49%">&nbsp;

                                </td>
                                <td width="49%">
                                    <form:select path="sltCol6sl1" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol6sl','1')" onkeydown="checkDuplicateSelect('sltCol6sl','1')"  onkeyup="checkDuplicateSelect('sltCol6sl','1')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col6List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol6sl2" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol6sl','2')" onkeydown="checkDuplicateSelect('sltCol6sl','2')"  onkeyup="checkDuplicateSelect('sltCol6sl','2')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col6List}" itemLabel="label" itemValue="value"/>                                  
                                    </form:select>
                                    <form:select path="sltCol6sl3" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol6sl','3')" onkeydown="checkDuplicateSelect('sltCol6sl','3')"  onkeyup="checkDuplicateSelect('sltCol6sl','3')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col6List}" itemLabel="label" itemValue="value"/>                                   
                                    </form:select>
                                    <form:select path="sltCol6sl4" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol6sl','4')" onkeydown="checkDuplicateSelect('sltCol6sl','4')"  onkeyup="checkDuplicateSelect('sltCol6sl','4')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col6List}" itemLabel="label" itemValue="value"/>                                   
                                    </form:select>
                                    <form:select path="sltCol6sl5" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol6sl','5')" onkeydown="checkDuplicateSelect('sltCol6sl','5')"  onkeyup="checkDuplicateSelect('sltCol6sl','5')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col6List}" itemLabel="label" itemValue="value"/>                                 
                                    </form:select>
                                    <form:select path="sltCol6sl6" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol6sl','6')" onkeydown="checkDuplicateSelect('sltCol6sl','6')"  onkeyup="checkDuplicateSelect('sltCol6sl','6')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col6List}" itemLabel="label" itemValue="value"/>                                    
                                    </form:select>
                                    <form:select path="sltCol6sl7" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol6sl','7')" onkeydown="checkDuplicateSelect('sltCol6sl','7')"  onkeyup="checkDuplicateSelect('sltCol6sl','7')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col6List}" itemLabel="label" itemValue="value"/>                                  
                                    </form:select>
                                    <form:select path="sltCol6sl8" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol6sl','8')" onkeydown="checkDuplicateSelect('sltCol6sl','8')"  onkeyup="checkDuplicateSelect('sltCol6sl','8')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col6List}" itemLabel="label" itemValue="value"/>                                    
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:checkbox value="7" path="chkCol7Bill" onclick="onCheckChange('chkCol7Bill', 'sltCol7')"/> Column 7 Configuration
                                </td>
                                <td>
                                    <form:checkbox value="16" path="chkCol16Bill" onclick="onCheckChange('chkCol16Bill', 'sltCol16')"/> Column 16 Configuration
                                </td>
                            </tr>
                            <tr>
                                <td width="50%">
                                    <form:select path="sltCol7sl1" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol7sl', '1')" onkeydown="checkDuplicateSelect('sltCol7sl', '1')"  onkeyup="checkDuplicateSelect('sltCol7sl', '1')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col7List}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                    <form:select path="sltCol7sl2" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol7sl', '2')" onkeydown="checkDuplicateSelect('sltCol7sl', '2')"  onkeyup="checkDuplicateSelect('sltCol7sl', '2')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col7List}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                    <form:select path="sltCol7sl3" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol7sl', '3')" onkeydown="checkDuplicateSelect('sltCol7sl', '3')"  onkeyup="checkDuplicateSelect('sltCol7sl', '3')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col7List}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                    <form:select path="sltCol7sl4" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol7sl', '4')" onkeydown="checkDuplicateSelect('sltCol7sl', '4')"  onkeyup="checkDuplicateSelect('sltCol7sl', '4')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col7List}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                    <form:select path="sltCol7sl5" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol7sl', '5')" onkeydown="checkDuplicateSelect('sltCol7sl', '5')"  onkeyup="checkDuplicateSelect('sltCol7sl', '5')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col7List}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                    <form:select path="sltCol7sl6" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol7sl', '6')" onkeydown="checkDuplicateSelect('sltCol7sl', '6')"  onkeyup="checkDuplicateSelect('sltCol7sl', '6')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col7List}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                    <form:select path="sltCol7sl7" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol7sl', '7')" onkeydown="checkDuplicateSelect('sltCol7sl', '7')"  onkeyup="checkDuplicateSelect('sltCol7sl', '7')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col7List}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                    <form:select path="sltCol7sl8" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol7sl', '8')" onkeydown="checkDuplicateSelect('sltCol7sl', '8')"  onkeyup="checkDuplicateSelect('sltCol7sl', '8')">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${col7List}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                </td>
                                <td width="50%">
                                    <form:select path="sltCol16sl1" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol16sl', '1')" onkeydown="checkDuplicateSelect('sltCol16sl', '1')"  onkeyup="checkDuplicateSelect('sltCol16sl', '1')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col16List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol16sl2" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol16sl', '2')" onkeydown="checkDuplicateSelect('sltCol16sl', '2')"  onkeyup="checkDuplicateSelect('sltCol16sl', '2')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col16List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol16sl3" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol16sl', '3')" onkeydown="checkDuplicateSelect('sltCol16sl', '3')"  onkeyup="checkDuplicateSelect('sltCol16sl', '3')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col16List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol16sl4" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol16sl', '4')" onkeydown="checkDuplicateSelect('sltCol16sl', '4')"  onkeyup="checkDuplicateSelect('sltCol16sl', '4')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col16List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol16sl5" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol16sl', '5')" onkeydown="checkDuplicateSelect('sltCol16sl', '5')"  onkeyup="checkDuplicateSelect('sltCol16sl', '5')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col16List}" itemLabel="label" itemValue="value"/>
                                    </form:select>                                                                          
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:checkbox value="17" path="chkCol17Bill" onclick="onCheckChange('chkCol17Bill','sltCol17')"/> Column 17 Configuration
                                </td>
                                <td>
                                    <form:checkbox value="18" path="chkCol18Bill" onclick="onCheckChange('chkCol18Bill','sltCol18')"/> Column 18 Configuration
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:select path="sltCol17sl1" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol17sl','1')" onkeydown="checkDuplicateSelect('sltCol17sl','1')"  onkeyup="checkDuplicateSelect('sltCol17sl','1')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col17List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol17sl2" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol17sl','2')" onkeydown="checkDuplicateSelect('sltCol17sl','2')"  onkeyup="checkDuplicateSelect('sltCol17sl','2')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col17List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol17sl3" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol17sl','3')" onkeydown="checkDuplicateSelect('sltCol17sl','3')"  onkeyup="checkDuplicateSelect('sltCol17sl','3')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col17List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol17sl4" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol17sl','4')" onkeydown="checkDuplicateSelect('sltCol17sl','4')"  onkeyup="checkDuplicateSelect('sltCol17sl','4')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col17List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol17sl5" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol17sl','5')" onkeydown="checkDuplicateSelect('sltCol17sl','5')"  onkeyup="checkDuplicateSelect('sltCol17sl','5')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col17List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </td>
                                <td>
                                    <form:select path="sltCol18sl1" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol18sl', '1')" onkeydown="checkDuplicateSelect('sltCol18sl', '1')"  onkeyup="checkDuplicateSelect('sltCol18sl', '1')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col18List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol18sl2" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol18sl', '2')" onkeydown="checkDuplicateSelect('sltCol18sl', '2')"  onkeyup="checkDuplicateSelect('sltCol18sl', '2')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col18List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol18sl3" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol18sl', '3')" onkeydown="checkDuplicateSelect('sltCol18sl', '3')"  onkeyup="checkDuplicateSelect('sltCol18sl', '3')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col18List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol18sl4" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol18sl', '4')" onkeydown="checkDuplicateSelect('sltCol18sl', '4')"  onkeyup="checkDuplicateSelect('sltCol18sl', '4')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col18List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    <form:select path="sltCol18sl5" class="form-control" style="width:70%;margin-bottom:2px;" onclick="checkDuplicateSelect('sltCol18sl', '5')" onkeydown="checkDuplicateSelect('sltCol18sl', '5')"  onkeyup="checkDuplicateSelect('sltCol18sl', '5')">
                                        <form:option value="">Select</form:option> 
                                        <form:options items="${col18List}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </td>
                            </tr>
                        </table>

                    </div>
                    <div class="panel-footer">                    
                        <input type="submit" name="action" value="Save" class="btn btn-success" />
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
