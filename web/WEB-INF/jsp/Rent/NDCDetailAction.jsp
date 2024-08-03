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
            function validateForm()
            {
                if ($('#forwardingAuthority').val() == '')
                {
                    alert('Please select Authority.');
                    return false;
                }
                if ($('#remarks').val() == '')
                {
                    alert("Please enter Remarks.");
                    return false;
                }
                if ($('#empGpc').val() == '110135' || $('#empGpc').val() == '110318')
                {
                    if (!$('#jsNeverOccupied')[0].checked && !$('#jsVacated')[0].checked && !$('#jsPendingDues')[0].checked && !$('#jsNotVacated')[0].checked)
                    {
                        alert("Please check the Verification Report.");
                        return false;
                    }
                }
                if (confirm("Confirm if you want to submit?"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            function validateNDC()
            {
                if ($('#applicationStatus').val() == '')
                {
                    alert("Please select Application Status.");
                    return false;
                }
                if ($('#applicationStatus').val() == 'Approved' && $('#issueType').val() == '')
                {
                    alert("Please select Issue Type.");
                    return false;
                }
                if ($('#issueType').val() == 'Recovery Intimation' && $('#recoveryAmount').val() == '')
                {
                    alert("Please enter Recovery Amount.");
                    return false;
                }
                if ($('#issueType').val() == 'No-NDC' && $('#nondcRemark').val() == '')
                {
                    alert("Please enter Reason for No NDC.");
                    return false;
                }
                if (confirm("Confirm if you want to submit?"))
                {
                    $('#frmQuarter')[0].action = 'UpdateNDCStatus.htm';
                    $('#frmQuarter')[0].submit();
                }
            }
            function changeButtonValue(btnValue)
            {
                if (btnValue == 'NDC')
                {
                    $('#btn_ndc').attr('value', 'Generate NDC');
                    $('#recovery_amt_blk').css('display', 'none');
                    $('#nondc_remark_blk').css('display', 'none');
                }
                if (btnValue == 'Recovery Intimation')
                {
                    $('#btn_ndc').attr('value', 'Generate Payment Intimation');
                    $('#recovery_amt_blk').css('display', 'block');
                    $('#nondc_remark_blk').css('display', 'none');
                }
                if (btnValue == 'No-NDC')
                {
                    $('#btn_ndc').attr('value', 'No NDC');
                    $('#recovery_amt_blk').css('display', 'none');
                    $('#nondc_remark_blk').css('display', 'block');
                }
            }
            function calculateTotalAmount()
            {
                if($('#ledgerAmount').val() == '')
                    $('#ledgerAmount').val('0');
                totalAmount = parseInt($('#ledgerAmount').val()) + parseInt($('#outstandingAmount').val());
                $('#recoveryAmount').val(totalAmount);
            }
        </script>
        <style type="text/css">
            body{margin:0px;font-family: 'Roboto', sans-serif;background:#F7F7F7}
            .training_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
            table {border:1px solid #DADADA;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
            .tblres td{padding:5px;}
        </style>        
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
        </jsp:include>        
        <form:form action="SaveNDCAuthority.htm" id="frmQuarter" method="POST" commandName="QuarterBean" onsubmit="javascript: return validateForm()">
            <input type="hidden" id="empGpc" value="${gpc}" />
            <div class="container">
                <h2>Application Detail for Quarters NDC (GA Pool Quarters)</h2>
                <h3><input type="button" value="Print Application" class="btn btn-primary" onclick="javascript: window.open('PrintAdminNDC.htm?id=${qBean.applicationId}', '', 'width=800,height=700')" /></h3>
                <div class="panel-group">

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Personal Details:</div>
                        <input type="hidden" name="applicationId" id="applicationId" value="${qBean.applicationId}" />
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr>
                                    <td align="right" width="25%">Application ID:</td>
                                    <td><strong>RNTAPP-${qBean.applicationId}</strong></td>
                                </tr>                                
                                <tr>
                                    <td align="right" width="25%">Full Name:</td>
                                    <td><strong>${qBean.fullName}</strong></td>
                                </tr>
                                <tr>
                                    <td align="right">Father&rsquo;s Name:</td>
                                    <td><strong>${qBean.fatherName}</strong></td>
                                </tr>
                                <tr>
                                    <td align="right">Present Designation at the time of Retirement:</td>
                                    <td><strong>${qBean.designation}</strong></td>
                                </tr>
                                <tr>
                                    <td align="right">Date of Retirement:</td>
                                    <td><strong>${qBean.dateOfRetirement}</strong></td>
                                </tr>
                                <tr>
                                    <td align="right">Department from which retired:</td>
                                    <td><strong>${qBean.retirementDept}</strong></td>
                                </tr> 
                                <tr>
                                    <td align="right">Pension Sanctioning Authority:</td>
                                    <td>${qBean.pensionSanctioningAuthority}</td>
                                </tr>
                                <tr>
                                    <td align="right" width="30%">Mobile:</td>
                                    <td>${qBean.mobile}</td>
                                </tr>
                                <tr>
                                    <td align="right">Email:</td>
                                    <td>${qBean.email}</td>
                                </tr>  
                            </table>      

                        </div>
                    </div>
                    <c:if test="${qBean.hasOccupied == 'No'}">
                        <p style="color:#FF0000;font-weight:bold;margin:10px 0px;">The applicant had not occupied any G.A. Pool Quarters at Bhubaneswar & Cuttack during the entire period of his/her service</p>
                    </c:if>
                    <c:if test="${qBean.hasOccupied == 'Yes'}">
                    
                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Quarter Information:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
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
                                    <td colspan="2"><strong>Quarter Vacated?</strong> Yes</td>
                                </tr>
                                <tr>
                                    <td colspan="2"><strong>Outstanding amount as per the eQuarter Ledger:</strong> <span style="color:#008900;font-weight:bold;">Rs. ${qBean.ledgerAmount}</span>
                                    <input type="hidden" id="ledgerAmount" value="${qBean.ledgerAmount}" /></td>
                                </tr>
                                <tr>
                                    <td width="350"><strong>Outstanding amount in respect of other Quarters (if any):</strong></td>
                                    <td><input type="text" class="form-control" name="outstandingAmount" id="outstandingAmount" style="width:200px;" onblur="javascript: calculateTotalAmount()" /></td>
                                </tr>                                
                            </table>
                        </div>
                    </div>
                                
                                            
<div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Online Payment Transactions:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr style="font-weight:bold;background:#EAEAEA;">
                                    <td>Bank Name</td>
                                    <td>Transaction ID</td>
                                    <td>Challan Ref No.</td>
                                    <td>Payment Mode</td>
                                    <td>Transaction Status</td>
                                    <td>Amount</td>
                                </tr>
                                <c:forEach items="${tList}" var="tList" varStatus="count">
                                    <tr>
                                        <td>${tList.bankName}</td>
                                        <td>${tList.bankTransactionId}</td>
                                        <td>${tList.challanRefNo}</td>
                                        <td>${tList.paymentMode}</td>
                                        <td>${tList.transactionstatus}</td>
                                        <td>${tList.transactionAmount}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>    
                                </c:if>
                    <div class="panel panel-danger" style="display:none;">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Application Status:</div>
                        <div class="panel-body">

                            <c:forEach items="${qBean.workflowLogList}" var="wList" varStatus="count">
                                <table class="table" style="font-size:12pt;border:1px solid #CACACA;">
                                    <tr>
                                        <td style="font-size:9pt;" width="50%">From: <strong>${wList.fromName} <span style="color:#008900;">[${wList.fromDesignation}]</span></strong><br /><i>on ${wList.dateCreated}</i></td>
                                        <td style="font-size:9pt;">Forwarded to: <strong>${wList.toName} <span style="color:#008900;">[${wList.toDesignation}]</span></strong></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <c:if test="${wList.gpc == '110135' || wList.gpc == '110318'}">
                                                <c:if test="${qBean.hasVerificationReport == 'Yes'}">
                                                    <strong style="font-size:13pt;text-decoration:underline;">Verification Report</strong>
                                                    <p style="line-height:22px;font-size:11pt;">
                                                        The Applicant has never occupied any G.A. Quarters at Cuttack/Bhubaneswar. <strong>${qBean.jsNeverOccupied}</strong><br />
                                                        The Applicant had occupied quarters, vacated the same and cleared all outstanding dues. <strong>${qBean.jsVacated}</strong><br />
                                                        The Applicant occupied the Quarters and vacated but not cleared the outstanding dues. <strong>${qBean.jsPendingDues}</strong><br />
                                                        The Applicant has not yet vacated the quarters. <strong>${qBean.jsNotVacated}</strong></p>
                                                    </c:if>
                                                </c:if>
                                                    <strong>Remarks:</strong><br />${wList.remarks}
                                        </td>
                                    </tr>
                                </table>
                            </c:forEach>


                            <input type="button" value="Cancel" class="btn btn-danger" onclick="javascript: self.location = 'NDCApplicationList.htm'" />
                        </div>                                        
                    </div>      

                    <c:if test="${!empty qBean.applicationStatus && displayForm == 'Y'}">
                        <div class="panel panel-danger" style="display:none;">
                            <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Take Action:</div>
                            <div class="panel-body">
                                <table class="table" style="font-size:12pt;">
                                    <c:if test="${gpc == '110135' || gpc == '110318'}">                                    
                                        <tr>
                                            <td colspan="2">
                                                <strong style="font-size:13pt;text-decoration:underline;">Verification Report</strong>
                                                <p style="font-style:italic;">I Checked the e-quarters system, Building Register and other records and found that:</p>
                                                <p style="line-height:22px;font-size:11pt;">
                                                    <input type="checkbox" value="Y" name="jsNeverOccupied" id="jsNeverOccupied" /> <label for="jsNeverOccupied">The Applicant has never occupied any G.A. Quarters at Cuttack/Bhubaneswar.</label><br />
                                                    <input type="checkbox" value="Y" name="jsVacated" id="jsVacated" /> <label for="jsVacated">The Applicant had occupied quarters, vacated the same and cleared all outstanding dues.</label><br />
                                                    <input type="checkbox" value="Y" name="jsPendingDues" id="jsPendingDues" /> <label for="jsPendingDues">The Applicant occupied the Quarters and vacated but not cleared the outstanding dues.</label><br />
                                                    <input type="checkbox" value="Y" name="jsNotVacated" id="jsNotVacated" /> <label for="jsNotVacated">The Applicant has not yet vacated the quarters.</label></p>
                                            </td>
                                        </tr>
                                    </c:if>                                    
                                    <tr>
                                        <td align="right" width="20%">Forward to:</td>
                                        <td><select name="forwardingAuthority" id="forwardingAuthority" class="form-control">
                                                <option value="">-Select-</option>
                                                <c:forEach items="${authList}" var="aList" varStatus="count">
                                                    <option value="${aList.value}">${aList.label}</option>
                                                </c:forEach>
                                            </select></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Remarks:</td>
                                        <td><textarea rows="3" cols="50" class="form-control" name="remarks" id="remarks"></textarea></td>
                                    </tr>

                                    <tr>
                                        <td></td>
                                        <td><input type="submit" value="Submit" class="btn btn-primary" />
                                            <input type="button" value="Cancel" class="btn btn-danger" onclick="self.location = 'NDCApplicationList.htm'" /></td>
                                    </tr>
                                </table>
                            </div>                                        
                        </div>
                    </c:if>
                                            
                    <c:if test="${!empty qBean.applicationStatus && qBean.isRentOfficer == 'Y'}">
                        <div class="panel panel-success">
                            <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Generate Final NDC:</div>
                            <div class="panel-body">
                                <table class="table" style="font-size:12pt;">
                                    <tr>
                                        <td align="right">Issue Type:</td>
                                        <td><input type="hidden" name="applicationStatus" id="applicationStatus" value="Approved" />

                                            <select name="issueType" id="issueType" class="form-control" onchange="javascript: changeButtonValue(this.value)">
                                                <option value="">-Select-</option>
                                                <option value="NDC">Issue NDC</option>
                                                <option value="Recovery Intimation">Issue Payment Intimation</option>
                                                <option value="No-NDC">No NDC</option>
                                            </select></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <div class="row" id="recovery_amt_blk" style="display:none;">
                                                <div class="col-lg-3" style="text-align:right;">Recovery Amount:</div>
                                                <div class="col-lg-4"><input type="text" class="form-control" name="recoveryAmount" id="recoveryAmount" readonly="readonly" /></div>
                                            </div>
                                            <div class="row" id="nondc_remark_blk" style="display:none;">
                                                <div class="col-lg-3" style="text-align:right;">Reason for No NDC:</div>
                                                <div class="col-lg-9"><input type="text" class="form-control" name="nondcRemark" id="nondcRemark" /></div>
                                            </div>                                            
                                        </td>

                                    </tr>                                    
                                    <tr>
                                        <td></td>
                                        <td><input type="button" value="Generate NDC" id="btn_ndc" class="btn btn-primary" onclick="javascript: validateNDC()" />
                                            <input type="button" value="Cancel" class="btn btn-danger" onclick="self.location = 'NDCApplicationList.htm'" /></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </c:if>

                </div>

            </div>
        </div>
    </form:form>
</div>
</div>   
</body>
</html>
