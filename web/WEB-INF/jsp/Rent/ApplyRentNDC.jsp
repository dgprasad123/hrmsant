<%-- 
    Document   : ApplyNDC
    Created on : 1 Sep, 2020, 11:04:55 AM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply Quarter NDC</title>
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
            function getDeptWiseOfficeList() {
                var deptcode = $('#sltDept').val();

                $('#sltOffice').empty();
                $('#sltPost').empty();

                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                $('#sltOffice').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltOffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode = $('#sltOffice').val();
                $('#sltPost').empty();

                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#sltPost').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        arrTemp = obj.postname.split(',');
                        $('#sltPost').append('<option value="' + obj.spc + '">' + arrTemp[0] + '</option>');
                    });
                });
            }

            function getPost() {
                //$('#pensionSanctioningAuthority').val($('#sltPost option:selected').text());
            }
            function checkTwinCity(isChecked)
            {
                $('#twin_city_blk').css('display', 'none');
                if (isChecked == 'Y')
                {
                    $('#twin_city_blk').css('display', 'block');
                }
            }
            function checkOccupy(isChecked)
            {
                $('#has_occupied_blk').css('display', 'none');
                if (isChecked == 'Y')
                {
                    $('#has_occupied_blk').css('display', 'block');
                }
            }
            function checkOutstanding(isChecked)
            {
                $('#has_cleared_outstanding_blk').css('display', 'none');
                $('#not_cleared_blk').css('display', 'none');
                if (isChecked == 'Y')
                {
                    $('#has_cleared_outstanding_blk').css('display', 'block');
                }
                if (isChecked == 'N')
                {
                    $('#not_cleared_blk').css('display', 'block');
                }
            }
            function gratuityChecked(isChecked)
            {
                $('#rent_deposit').css('display', 'none');
                $('#btnSubmit').attr('disabled', false);
                if (isChecked == 'N')
                {
                    $('#rent_deposit').css('display', 'block');
                    $('#btnSubmit').attr('disabled', true);
                }
            }
            function validateForm()
            {               
                if ($('#designation').val() == '')
                {
                    alert("Please enter your designation at the time of retirement.");
                    return false;
                }
                if ($('#retirementDept').val() == '')
                {
                    alert("Please select your Department at the time of retirement.");
                    return false;
                }                
                //alert($('#pensionSanctioningAuthority').val());return false;
                if ($('#pensionSanctioningAuthority').val() == '')
                {
                    alert("Please select Pension Sanctioning Authority.");
                    return false;
                }
                if(!document.getElementById('has_occupied1').checked && !document.getElementById('has_occupied2').checked)
                {
                    alert("Please check whether you ever occupy any GA quarters in Cuttack/Bhubaneswar during your service period?")
                    return false;
                }                 
                if($('#hasQDetails').val() == 'N' && document.getElementById('has_occupied1').checked == true)
                {                
                    if ($('#quarterUnit').val() == '')
                    {
                        alert("Please select the Unit.");
                        return false;
                    }
                    if ($('#quarterType').val() == '')
                    {
                        alert("Please select the Type.");
                        return false;
                    }
                    if ($('#buildingNo').val() == '')
                    {
                        alert("Please select the Building No.");
                        return false;
                    }
                    if ($('#hasVacated').val() == 'N')
                    {
                        alert("Please Vacate your GA Quarter before applying NDC.");
                        return false;
                    }
                }
            }
            function showQuarterType() {
                var qrtrunit = $("#quarterUnit").val();
                $('#quarterType').empty();
                $.post("unitWiseQuarterTypeRentJson.htm", {quarterunitarea: qrtrunit})
                        .done(function(data) {
                            var unitAreaList = data.unitAreaList;
                            $('#quarterType').append($('<option>').text('-Select-').attr('value', ''));
                            $.each(unitAreaList, function(i, obj) {
                                $('#quarterType').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                            $("#quarterType").val($("#quarterType").val());
                        });
            }
            function showBuildingNo()
            {
                var qrtrunit = $("#quarterUnit").val();
                var quarterType = $("#quarterType").val();
                //alert(qrtrunit);
                //alert(quarterType);
                $('#buildingNo').empty();
                $.post("GetBuildingNoDataJson.htm", {qtrunit: qrtrunit, qtrtype: quarterType})
                        .done(function(data) {
                            var unitAreaList = data.unitAreaList;
                            $('#buildingNo').append($('<option>').text('-Select-').attr('value', ''));
                            $.each(unitAreaList, function(i, obj) {
                                $('#buildingNo').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                            $("#buildingNo").val($("#buildingNo").val());
                        });
            }
            function checkOccupy(isChecked)
            {
                $('#quarter_entry_blk').css('display', 'none');
                if(isChecked == 'Y')
                {
                    $('#quarter_entry_blk').css('display', 'block');
                }
            }            
        </script>
    </head>
    <body>
        <c:if test="${!qBean.isApplied}">
            <form:form action="SaveNDCRentApplication.htm" name="form2" method="POST" commandName="QuarterBean" enctype="multipart/form-data" onsubmit="javascript: return validateForm()">
                <input type="hidden" name="msg" value="${msg}" />
                <div class="container">
                    <h2>Applying for Quarters NDC (GA Pool Quarters)</h2>
                    <div class="panel-group">


                        <div class="panel panel-success">
                            <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Personal Details:</div>
                            <div class="panel-body">
                                <table class="table" style="font-size:12pt;">
                                    <tr>
                                        <td align="right" width="25%">Full Name:</td>
                                        <td><strong>${qBean1.fullName}</strong><input type="hidden" name="fullName" value="${qBean1.fullName}" /></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Father&rsquo;s Name:</td>
                                        <td><strong>${user.fname}</strong><input type="hidden" name="fatherName" value="${user.fname}" /></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Present Designation at the time of Retirement:</td>
                                        <td><input type="text" name="designation" id="designation" value="${user.postname}" class="form-control" /></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Date of Retirement:</td>
                                        <td><strong>${user.empDos}</strong><input type="hidden" name="dateOfRetirement" value="${user.empDos}" /></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Department from which retired:</td>
                                        <td>
                                            <form:select path="retirementDept" id="retirementDept" class="form-control">
                                            <form:option value="">--Select Department--</form:option>
                                            <form:options items="${deptlist}" itemValue="deptName" itemLabel="deptName"/>
                                        </form:select>
                                        
                                        </td>
                                    </tr> 
                                    <tr>
                                        <td align="right">Pension Sanctioning Authority:</td>
                                        <td><input type="text" class="form-control" name="pensionSanctioningAuthority" id="pensionSanctioningAuthority"/>
</td>
                                    </tr>
                                    <tr>
                                        <td align="right" width="30%">Mobile:</td>
                                        <td><input type="text" name="mobile" id="mobile" class="form-control" value="${user.mobile}" readonly /></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Email:</td>
                                        <td><input type="text" name="email" id="email" class="form-control" value="${user.lname}" /></td>
                                    </tr>  
                                </table>      

                            </div>
                        </div>

                        <div class="panel panel-success">
                            <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Quarter Information:</div>
                            <div class="panel-body">
                                <table class="table" style="font-size:12pt;">
                                    
                                <tr>
                                    <td colspan="2">Did you ever occupy any GA quarters in Cuttack/Bhubaneswar during your service period?
                                        <span style="font-weight:bold;"><input type="radio" name="hasOccupied" id="has_occupied1"<c:if test="${address ne ''}"> checked="checked"</c:if> value="Y" onclick="javascript: checkOccupy(this.value)" /> <label for="has_occupied1">Yes</label>
                                            <input type="radio" name="hasOccupied" id="has_occupied2" value="N" onclick="javascript: checkOccupy(this.value)" /> <label for="has_occupied2">No</label></span></td>
                                </tr>                                    
                                </table>
                                <div id="quarter_entry_blk">
                                <table class="table" style="font-size:12pt;">
                                    <c:if test="${address ne ''}"><span style="font-size:14pt;font-weight:bold;">Last Quarter Details: <br />
                                            <input type="hidden" value="Y" id="hasQDetails" />
                                            <span style="font-size:13pt;">
                                            <span style="color:#008900;">Address: ${address}</span><br />
                                            <span>Unit: ${quarterUnit}</span><br />
                                            <span>Type: ${quarterType}</span><br />
                                            <span>Building No: ${buildingNo}</span>
                                        </span></span>
                                        <input type="hidden" value="${quarterUnit}" name="quarterUnit" id="quarterUnit" />
                                        <input type="hidden" value="${quarterType}" name="quarterType" id="quarterType" />
                                        <input type="hidden" value="${buildingNo}" name="buildingNo" id="buildingNo" />
                                    </c:if>
                                    <c:if test="${address eq ''}">
                                    <tr>
                                        <td colspan="2"><strong>Enter Last Quarter Details:</strong><input type="hidden" value="N" id="hasQDetails" />
                                            <table class="table table-bordered">
                                                <tr style="background:#EAEAEA;font-weight:bold;">
                                                    <td>Units/Area</td>
                                                    <td>Quarter Type</td>
                                                    <td>Building No</td>
                                                </tr>
                                                <tr>
                                                    <td><form:select path="quarterUnit" id="quarterUnit" class="form-control" onchange="showQuarterType()">
                                                            <form:option value="">-Select-</form:option>
                                                            <form:options items="${unitList}" itemValue="value" itemLabel="label"/>
                                                        </form:select></td>
                                                    <td><select name="quarterType" id="quarterType" class="form-control" onchange="showBuildingNo()">
                                                            <option value="">-Select-</option>
                                                        </select></td>
                                                    <td><select name="buildingNo" id="buildingNo" class="form-control">
                                                            <option value="">-Select-</option>
                                                        </select></td>
                                                </tr>             


                                            </table>
                                        </td>
                                    </tr> 
                                    </c:if>
                                    <tr>
                                        <td width="200"><strong>Quarter Vacated?</strong></td>
                                        <td><select name="hasVacated" id="hasVacated" class="form-control" style="width:200px;">
                                                <option value="Y">Yes</option>
                                                <option value="N">No</option>
                                            </select></td>
                                    </tr>


                                </table>

                                </div>
                                <table class="table" style="font-size:12pt;">
                                                                    <tr>
                                        <td colspan="2" style="font-weight:bold;" align="center"><input type="submit" value="Request for NDC" id="btnSubmit" class="btn btn-success" />
                                            <input type="button" value="Cancel" class="btn btn-danger" /></td>
                                    </tr>  
                                </table>

                            </div>
                        </div>    




                    </div>
                </div>

                <div id="pensionAuthorityModal" class="modal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Pension Authority</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="sltDept">Department</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="sltDept" id="sltDept" class="form-control" onchange="getDeptWiseOfficeList();">
                                            <form:option value="">--Select Department--</form:option>
                                            <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="sltOffice">Office</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="sltOffice" id="sltOffice" class="form-control" onchange="getOfficeWisePostList();">
                                            <form:option value="">--Select Office--</form:option>
                                            <form:options items="${offList}" itemValue="offCode" itemLabel="offName"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="sltPost">Post</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="sltPost" id="sltPost" class="form-control" onchange="getPost();">
                                            <form:option value="">--Select Post--</form:option>
                                            <form:options items="${postList}" itemValue="spc" itemLabel="postname"/>
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
        </c:if>
        <c:if test="${qBean.isApplied}">
            <div class="container">
                <h2>Application Detail for Quarters NDC (GA Pool Quarters)</h2>
                <h3><input type="button" value="Print Application" class="btn btn-primary" onclick="javascript: window.open('PrintNDC.htm', '', 'width=800,height=700')" /></h3>
                <div class="panel-group">
                    <c:if test="${isPaymentDone == 'Y'}">
                        <div class="panel panel-success">
                            <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Transaction Details:</div>
                            <div class="panel-body">
                                <table class="table" style="font-size:12pt;">
                                    <tr>
                                        <td align="right" width="25%">Challan Reference Number:</td>
                                        <td>${iBean.challanRefNo}</td>
                                    </tr>
                                    <tr>
                                        <td align="right" width="25%">Amount:</td>
                                        <td>${qBean.recoveryAmount}</td>
                                    </tr>                                    
                                    <tr>
                                        <td align="right">Payment Mode:</td>
                                        <td>${iBean.paymentMode}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">Bank Name:</td>
                                        <td>${iBean.bankName}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">Bank Transaction ID:</td>
                                        <td>${iBean.bankTransactionId}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">Transaction Status:</td>
                                        <td>${iBean.transactionstatus}</td>
                                    </tr> 

                                    <tr>
                                        <td align="right" width="30%">Transaction Message:</td>
                                        <td>${iBean.transactionMsg}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">Transaction Time:</td>
                                        <td>${iBean.transactionTime}</td>
                                    </tr>  
                                </table>      

                            </div>
                        </div>
                    </c:if>
                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Personal Details:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr>
                                    <td align="right" width="25%">Application ID:</td>
                                    <td><strong>RNTAPP-${qBean.applicationId}</strong><input type="hidden" name="fullName" value="${qBean1.fullName}" /></td>
                                </tr>
                                <tr>
                                    <td align="right" width="25%">Full Name:</td>
                                    <td><strong>${qBean1.fullName}</strong><input type="hidden" name="fullName" value="${qBean1.fullName}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Father&rsquo;s Name:</td>
                                    <td><strong>${user.fname}</strong><input type="hidden" name="fatherName" value="${user.fname}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Present Designation at the time of Retirement:</td>
                                    <td><strong>${qBean.retSPC}</strong><input type="hidden" name="designation" value="${qBean.retSPC}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Date of Retirement:</td>
                                    <td><strong>${user.empDos}</strong><input type="hidden" name="dateOfRetirement" value="${user.empDos}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Department from which retired:</td>
                                    <td><strong>${qBean.retirementDept}</strong><input type="hidden" name="retirementDept" value="${qBean.retirementDept}" /></td>
                                </tr> 
                                <tr>
                                    <td align="right">Pension Sanctioning Authority:</td>
                                    <td>${qBean.pensionSanctioningAuthority}</td>
                                </tr>
                                <tr>
                                    <td align="right" width="30%">Mobile:</td>
                                    <td>${user.mobile}</td>
                                </tr>
                                <tr>
                                    <td align="right">Email:</td>
                                    <td>${qBean.email}</td>
                                </tr>  
                            </table>      

                        </div>
                    </div>

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Other Information:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">

                                    <c:if test="${qBean.hasOccupied == 'Yes'}">
                                <tr>
                                    <td colspan="2"><strong>Last Quarter Details:</strong>
                                        <table class="table table-bordered">
                                            <tr style="background:#EAEAEA;font-weight:bold;">
                                                <td>Unit</td>
                                                <td>Quarter Type</td>
                                                <td>Building No</td>
                                            </tr>
                                            <tr>
                                                <td>${qBean.quarterUnit}</td>
                                                <td>${qBean.quarterType}</td>
                                                <td>${qBean.buildingNo}</td>
                                            </tr>             


                                        </table>
                                    </td>
                                </tr> 
                                <tr>
                                    <td colspan="2"><strong>Quarter Vacated?</strong> 
                                        ${qBean.hasVacated}</td>
                                </tr>
</c:if>
                            </table>
                        </div>
                    </div>    

                    <div class="panel panel-danger">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Application Status:</div>
                        <div class="panel-body">
                            ${qBean.applicationStatus}
                            <c:if test="${qBean.applicationStatus == 'No NDC'}"><br /><span style="font-size:12pt;color:#890000;"><strong>Reason:</strong> ${qBean.nondcRemark}<span></c:if>
                            <c:if test="${qBean.applicationStatus == 'Recovery Intimation Generated'}">
                                <br /><strong>Outstanding amount in respect of other Quarters:</strong> ${qBean.outstandingAmount}
                                <br /><strong>Recovery Amount:</strong> ${qBean.recoveryAmount}
                                <c:if test="${qBean.hasFinalNDC == 'Y'}"><br /><a href="downloadNDC.htm?applicationId=${qBean.applicationId}" target="_blank">Download Recovery Intimation</a></c:if>
                                <br /><strong>Address:</strong> ${addr.address}<br />
                                <strong>District:</strong> ${addr.distCode}<br />
                                <strong>State:</strong> ${addr.stateCode}<br />
                                <strong>Pin:</strong> ${addr.pin}<br />
                                <c:if test="${empty(addr.address) || empty(addr.distCode) || empty(addr.stateCode) || empty(addr.pin)}">
                                    <span style="color:#FF0000;font-weight:bold;">Please update your Address, District, State and Pin Code before online Payment.</span>
                                </c:if>
                                <c:if test="${isPaymentDone == 'N'}">
                                    <c:if test="${!empty(addr.address) && !empty(addr.distCode) && !empty(addr.stateCode) && !empty(addr.pin)}">
                                        <div style="text-align:center;">
                                            <form action="https://www.odishatreasury.gov.in/echallan/dept-intg" method="POST" name="form1">
                                                <input type="hidden" name="msg" value="${encryptedText}" />
                                                <input type="hidden" name="deptCode"  value="GAQ"/><!--Mendatory--><!-- Code for GAD Integration-->

                                                <input type="submit" value="Pay Outstanding Online" class="btn btn-success btn-xlg"/>

                                            </form>
                                        </div>                                
                                    </c:if>
                                </c:if>
                            </c:if>
                            <c:if test="${qBean.hasFinalNDC == 'Y'}">
                                <br /><a href="downloadNDC.htm?applicationId=${qBean.applicationId}" target="_blank"><c:if test="${qBean.applicationStatus == 'Recovery Intimation Generated'}">Download Payment Intimation</c:if><c:if test="${qBean.applicationStatus != 'Recovery Intimation Generated'}">Download Final NDC</c:if></a>

                            </c:if>


                        </div>                                        
                    </div> 


                </div>
            </div>

        </c:if>
    </body>
</html>
