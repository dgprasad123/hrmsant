<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String downloadlink = "";
    String deleteAttach = "";
    String loanid = "";
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/color.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <link  rel="stylesheet" type="text/css"  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function applyloan()
            {

                var confirmloan = confirm("Do you want to apply GPF Long term advance ");
                if (!confirmloan) {
                    return false;
                }


            }


            function changepost() {
                var url = 'ChangePostLoanController.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "50%"});
            }

            function SelectSpn(offCode, spc, offName, authName, spc_hrmsid)
            {
                $.colorbox.close();
                $('#hidSPC').val(spc);
                $('#hidOffCode').val(offCode);
                $("#hidOffName").val(offName);
                $("#forwardto").val(authName);
                $("#forwardtoHrmsid").val(spc_hrmsid);
            }

            $(document).ready(function () {
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });




            });

            $(document).ready(function () {

                $('#loan_status').combobox({
                    onSelect: function (record) {

                        if (record.value == 45 || record.value == 47 || record.value == 99) {
                            $("#id_forward_div").show();
                        } else {
                            $("#id_forward_div").hide();
                        }
                    }
                });
            });
            function laonsaction() {


                var statusID = $("#statusID").val();
                if (statusID == 99) {
                    var letterno = $('#letter_no').val();
                    var letter_date = $('#letterDate').val();
                    if (letterno == "") {
                        alert("Please enter Sanction no");
                        return false;
                    }
                    if (letter_date == "") {
                        alert("Please enter Sanction date");
                        return false;
                    }
                }
                if (statusID == 45) {

                    var samount = $('#samount').val();
                    if (samount == "") {
                        alert("Please enter Sanction Amount");
                        return false;
                    }
                    if (isNaN(samount)) {
                        alert("Invalid Sanction Amount");
                        return false;
                    }
                    var ramount = $('#ramount').val();
                    if (ramount == "") {
                        alert("Please enter Release Amount");
                        return false;
                    }
                    if (isNaN(ramount)) {
                        alert("Invalid Release Amount");
                        return false;
                    }
                    var emi_principal = $('#emi_principal').val();
                    if (emi_principal == "") {
                        alert("Please enter  No. of installments(Principal)");
                        return false;
                    }
                    if (isNaN(emi_principal)) {
                        alert("Invalid No. of installments(Principal)");
                        return false;
                    }
                    var principal_amount = $('#principal_amount').val();
                    if (principal_amount == "") {
                        alert("Please enter  No. of installments(Principal)");
                        return false;
                    }
                    if (isNaN(principal_amount)) {
                        alert("Invalid Installment Amount(Principal)");
                        return false;
                    }
                    /*  var interest_type = $('#interest_type').val();
                     if (interest_type == "") {
                     alert("Please enter Interest Type");
                     return false;
                     }*/
                    var rate_interest = $('#rate_interest').val();
                    if (rate_interest == "") {
                        alert("Please enter Rate of Interest");
                        return false;
                    }
                    if (isNaN(rate_interest)) {
                        alert("Invalid Rate of Interest");
                        return false;
                    }



                    var interestamount = $('#totalInterestamount').val();
                    // var penal_rate = $('#penal_rate').val();
                    if (interestamount == "") {
                        alert("Please enter Interest Amount");
                        return false;
                    }
                    if (isNaN(interestamount)) {
                        alert("Invalid Interest Amount");
                        return false;
                    }

                    var emi_interest = $('#emi_interest').val();
                    if (emi_interest == "") {
                        alert("Please enter  No. of installments(Interest)");
                        return false;
                    }
                    if (isNaN(emi_interest)) {
                        alert("Invalid No. of installments(Interest");
                        return false;
                    }

                    var interest_amount = $('#interest_amount').val();
                    if (interest_amount == "") {
                        alert("Please enter  Installment Amount(Interest)");
                        return false;
                    }
                    if (isNaN(interest_amount)) {
                        alert("Invalid Installment Amount(Interest)");
                        return false;
                    }



                    var penal_rate = $('#penal_rate').val();
                    if (penal_rate == "") {
                        alert("Please enter Penal Rate of Interest");
                        return false;
                    }
                    if (isNaN(penal_rate)) {
                        alert("Invalid Penal Rate of Interest");
                        return false;
                    }

                    var radios = document.getElementsByName("moratoriumRequired");
                    var formValid = false;
                    var i = 0;
                    while (!formValid && i < radios.length) {
                        if (radios[i].checked)
                            formValid = true;
                        i++;
                    }
                    if (!formValid) {
                        alert("Please choose Moratorium Period Required");
                        return false;
                    }
                    var moratorium_period = $('#moratorium_period').val();
                    if (moratorium_period == "") {
                        alert("Please enter Moratorium Period(In Months)");
                        return false;
                    }
                    var recdate = $('#recDate').val();
                    if (recdate == "") {
                        alert("Please enter Recovery Start Date");
                        return false;
                    }
                    var radios1 = document.getElementsByName("insuranceFlag");
                    var formValid1 = false;

                    var i = 0;
                    while (!formValid1 && i < radios1.length) {
                        if (radios1[i].checked)
                            formValid1 = true;
                        i++;
                    }
                    if (!formValid1) {
                        alert("Please choose Insurance Flag");
                        return false;
                    }
                }

                var loan_status = $("#loan_status").val();
                if (loan_status == "") {
                    alert("Please select Loan status");
                    document.getElementById("loan_status").focus();
                    return false;
                }
                if (loan_status == 45 || loan_status == 47 || loan_status == 99 || loan_status == 100) {
                    var forwardto = $("#forwardto").val();
                    if (forwardto == "") {
                        alert("Please select next loan authority details ");
                        document.getElementById("forwardto").focus();
                        return false;
                    }
                }

                if (loan_status != 44) {
                    var loancomments = $("#loancomments").val();
                    if (loancomments == "" || loancomments.length < 5) {

                        alert("Please write your comments");
                        return false;
                    }
                }

            }
            function calculate_prinicipal() {
                var samount = $('#samount').val();
                var emi_principal = $('#emi_principal').val();
                if (samount != "" && !isNaN(samount) && emi_principal != '' && !isNaN(emi_principal)) {
                    var emi_pra = parseInt(samount) / parseInt(emi_principal);
                    emi_pra = Math.round(emi_pra);
                    $('#principal_amount').val(emi_pra);
                }
            }
            function calculate_rate_interest() {
                var samount = $('#samount').val();
                var emi_principal = $('#emi_principal').val();
                var rateInterest = $('#rate_interest').val();
                var interestType = $('input[name="interestType"]:checked').val();
                if (interestType == "Simple") {
                    if (samount != "" && !isNaN(samount) && emi_principal != '' && !isNaN(emi_principal) && rateInterest != '') {
                        var si = (parseFloat(samount) * parseFloat(rateInterest) * (parseInt(emi_principal) / 12)) / 100;
                        si = Math.round(si);
                        si = si.toFixed(2);
                        $('#totalInterestamount').val(si);
                    }
                }
                if (interestType == "Compound") {
                    if (samount != "" && !isNaN(samount) && emi_principal != '' && !isNaN(emi_principal) && rateInterest != '') {
                        var si = Math.pow((1 + (parseFloat(rateInterest) / 100) / 12), parseInt(emi_principal));
                        var siValue = parseFloat(samount) * parseFloat(si);
                        siValue = Math.round(siValue);
                        siValue = parseFloat(siValue) - parseFloat(samount);
                        siValue = siValue.toFixed(2);
                        siValue = Math.round(siValue);
                        $('#totalInterestamount').val(siValue);
                    }
                }
            }
            function calculate_interest_emi() {
                var samount = $('#totalInterestamount').val();
                var emi_principal = $('#emi_interest').val();
                if (samount != "" && !isNaN(samount) && emi_principal != '' && !isNaN(emi_principal)) {
                    var emi_pra = parseInt(samount) / parseInt(emi_principal);
                    emi_pra = Math.round(emi_pra);
                    $('#interest_amount').val(emi_pra);
                }

            }
        </script>    
    </head>

    <body>       
        <form action="savegpfApproveLoan.htm" method="POST" commandName="LoanForm" onsubmit="return laonsaction()" enctype="multipart/form-data">
            <input type="hidden" name="loanId" id="loanid" value="${LoanGPFForm.loanId}"/>
            <input type="hidden" name="taskid" id="taskid" value="${LoanGPFForm.taskid}"/>
            <input type="hidden" name="empId" id="empId" value="${LoanGPFForm.empId}"/>
            <input type="hidden" name="statusID" id="statusID" value="${LoanGPFForm.statusId}"/>
            <input type="hidden" name="approvedBy" id="approvedBy" value="${LoanGPFForm.approvedBy}"/>
            <input type="hidden" name="approvedSpc" id="approvedSpc" value="${LoanGPFForm.approvedSpc}"/>
            <input type="hidden" name="hidOffCode" id="hidOffCode"/>
            <input type="hidden" name="hidOffName" id="hidOffName"/>
            <input type="hidden" name="hidSPC" id="hidSPC"/>
            <input type="hidden" name="forwardtoHrmsid" id="forwardtoHrmsid"/>
            <div id="tbl-container" class="easyui-panel" title="GPF Loan Authority"  style="width:100%;overflow: auto;">
                <a href="#part1" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">Proforma for application of ${LoanGPFForm.gpftype} withdrawal From GPF</a>     
                <div align="left" style="padding-left:10px" id="part1" class="collapse in">

                    <table style="width:100%">  
                        <tr>
                            <td style="width:10%">1.</td>
                            <td style="width:30%">Name of the subscriber</td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.empName}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">2.</td>
                            <td style="width:30%">Account Number</td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.gpfno}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">3.</td>
                            <td style="width:30%">Post</td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.designation}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">4.</td>
                            <td style="width:30%">Pay</td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.pay}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">5(i).</td>
                            <td style="width:30%">Date of Joining Service</td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.doj}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">5(ii).</td>
                            <td style="width:30%">Date of Superannuation </td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.supperannuation}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">6.</td>
                            <td style="width:30%">GPF Type </td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.gpftype}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">7.</td>
                            <td style="width:30%">Balance at the credit of the subscriber on the date of application </td>
                            <td style="width:60%"> 
                                RS ${LoanGPFForm.balanceCredit}/-
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">i).</td>
                            <td style="width:30%">Closing balance as per statement for the Year <strong> ${LoanGPFForm.cyear} </strong> </td>
                            <td style="width:60%"> 
                                RS ${LoanGPFForm.closingbalance}/-
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">ii).</td>
                            <td style="width:30%">Credit from <strong>${LoanGPFForm.creditForm}</strong> to <strong>${LoanGPFForm.creditTo} </strong> </td>
                            <td style="width:60%"> 
                                RS ${LoanGPFForm.creditAmount}/-
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">iii).</td>
                            <td style="width:30%">Refunds made to the fund after the closing balance </td>
                            <td style="width:60%"> 
                                RS ${LoanGPFForm.refund}/-
                            </td>
                        </tr>

                        <tr>
                            <td style="width:10%">iv).</td>
                            <td style="width:30%">Withdrawal during the period from  <strong>${LoanGPFForm.withdrawfrom}</strong> to <strong>${LoanGPFForm.withdrawto} </strong> </td>
                            <td style="width:60%"> 
                                RS ${LoanGPFForm.withdrawalAmount}/-
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">v).</td>
                            <td style="width:30%"> Net Balance at credit on date of application. </td>
                            <td style="width:60%"> 
                                RS ${LoanGPFForm.netbalance}/-
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">8.</td>
                            <td style="width:30%">Amount of withdrawal required. </td>
                            <td style="width:60%"> 
                                RS ${LoanGPFForm.withdrawalreq}/-
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">9(a).</td>
                            <td style="width:30%"> Purpose for which the withdrawal is required </td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.purpose}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">9(b).</td>
                            <td style="width:30%"> Rule under which the request is covered </td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.requestcovered}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">10. </td>
                            <td style="width:30%">Weather any withdrawal was taken for the same purpose earlier if so, indicate the amount and the year </td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.withdrawaltaken}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">11. </td>
                            <td style="width:30%">Attachment </td>
                            <td style="width:60%"> 
                                <c:if test="${ not empty LoanGPFForm.diskFileName}">
                                    <c:set var="lid" value="${LoanGPFForm.loanId}"/>
                                    <a href="DownloadLoanAttch.htm?lid=${LoanGPFForm.loanId}" target="_blank" >Download</a>
                                </c:if>
                                <c:if test="${empty LoanGPFForm.diskFileName}">
                                    <strong> No File Attached</strong>
                                </c:if>   
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">12. </td>
                            <td style="width:30%">Name of the account officer maintaining the provident Fund account </td>
                            <td style="width:60%"> 
                                ${LoanGPFForm.accountOfficer}
                            </td>
                        </tr>
                        <c:if test="${ not empty LoanGPFForm.loancomments}">

                            <tr>
                                <td style="width:10%">13. </td>
                                <td style="width:10%">Notes:</td>
                                <td colspan="4">${LoanGPFForm.loancomments}</td>
                            </tr> 

                        </c:if>  
                    </table>  

                    <c:if test="${ LoanGPFForm.statusId  == 45}">


                        <a href="#part2" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left;margin-top:10px">Loan Sanction Detail informations</a>     
                        <table style="width:100%;line-height: 40px"  id="part2" class="collapse in">

                            <tr>
                                <td  valign="top"  style="width:20%">Sanction Amount:</td>
                                <td >
                                    <input name="samount" class="form-control" id="samount" style='width:250px' />
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Release Amount:</td>
                                <td >
                                    <input name="ramount" class="form-control" id="ramount" style='width:250px' />
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">No. of installments(Principal):</td>
                                <td >
                                    <input name="emiPrincipal" class="form-control" id="emi_principal" style='width:250px' onblur="calculate_prinicipal()"/>
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Installment Amount(Principal):</td>
                                <td >
                                    <input name="principalAmount" class="form-control" id="principal_amount" style='width:250px'/>
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Interest Type:</td>
                                <td >
                                    <input type="radio" class="form-check-input" name="interestType"  value="Simple" checked="checked" onclick="calculate_rate_interest()" />&nbsp;Simple
                                    <input type="radio" class="form-check-input" name="interestType"  value="Compound"  onclick="calculate_rate_interest()"/>&nbsp;Compound
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Rate of Interest:</td>
                                <td >
                                    <input name="rateInterest" class="form-control" id="rate_interest" style='width:250px' onblur="calculate_rate_interest()"/>
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Total Interest Amount:</td>
                                <td >
                                    <input name="totalInterestamount" class="form-control" id="totalInterestamount" style='width:250px'/>
                                </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">No. of installments(Interest):</td>
                                <td >
                                    <input name="emiInterest" class="form-control" id="emi_interest" style='width:250px' onblur="calculate_interest_emi()"/>
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Installment Amount(Interest):</td>
                                <td >
                                    <input name="interestAmount" class="form-control" id="interest_amount" style='width:250px'/>
                                </td>
                            </tr>


                            <tr>
                                <td  valign="top"  style="width:20%">Last Installment Amount:</td>
                                <td >
                                    <input name="lastInstallment" class="form-control" id="last_installment" style='width:250px'/>
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Penal Rate of Interest:</td>
                                <td >
                                    <input name="penalRate" class="form-control" id="penal_rate" style='width:250px'/>
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Moratorium Period Required:</td>
                                <td >
                                    <input type="radio" class="form-check-input" name="moratoriumRequired"   value="Yes"  />&nbsp;YES
                                    <input type="radio" class="form-check-input" name="moratoriumRequired"  value="No"  />&nbsp;NO
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Moratorium Period(In Months):</td>
                                <td >
                                    <input name="moratoriumPeriod" class="form-control" id="moratorium_period" style='width:250px'/>
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Recovery Start Date:</td>
                                <td >
                                    <input class="txtDate form-control" id="recDate" type="text" name="recDate" style='width:250px'  ></input>

                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Insurance Flag:</td>
                                <td >
                                    <input type="radio" class="form-check-input" name="insuranceFlag"  value="Yes"  />&nbsp;YES
                                    <input type="radio" class="form-check-input" name="insuranceFlag"  value="No"  />&nbsp;NO
                                </td>
                            </tr>

                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                        </table>  
                    </c:if>
                    <c:if test="${ LoanGPFForm.statusId  == 99}">

                        <a href="#part2" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left;margin-top:10px">Loan Sanction Detail informations</a>     
                        <table style="width:100%;line-height: 40px"  id="part2" class="collapse in">

                            <tr>
                                <td  valign="top"  style="width:20%">Sanction Amount:</td>
                                <td >
                                    ${LoanGPFForm.samount}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Release Amount:</td>
                                <td >
                                    ${LoanGPFForm.releaseAmount}                               
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">No. of installments(Principal):</td>
                                <td >
                                    ${LoanGPFForm.emiPrincipal}                                    
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Installment Amount(Principal):</td>
                                <td >
                                    ${LoanGPFForm.principalAmount}                                   
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Interest Type (Simple/Compound):</td>
                                <td >
                                    ${LoanGPFForm.interestType}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Rate of Interest:</td>
                                <td >
                                    ${LoanGPFForm.rateInterest}                                  
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Total Interest Amount:</td>
                                <td >
                                    ${LoanGPFForm.totalInterestamount}                              
                                </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">No. of installments(Interest):</td>
                                <td >
                                    ${LoanGPFForm.emiInterest}                                    
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Installment Amount(Interest):</td>
                                <td >
                                    ${LoanGPFForm.interestAmount}

                                </td>
                            </tr>


                            <tr>
                                <td  valign="top"  style="width:20%">Last Installment Amount:</td>
                                <td >
                                    ${LoanGPFForm.lastInstallment}                                
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Penal Rate of Interest:</td>
                                <td >
                                    ${LoanGPFForm.penalRate} 

                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Moratorium Period Required:</td>
                                <td >
                                    ${LoanGPFForm.moratoriumRequired}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Moratorium Period(In Months):</td>
                                <td >
                                    ${LoanGPFForm.moratoriumPeriod}                                  
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Recovery Start Date:</td>
                                <td >
                                    ${LoanGPFForm.recDate} 

                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Insurance Flag:</td>
                                <td >
                                    ${LoanGPFForm.insuranceFlag} 

                                </td>
                            </tr>
                          </table>
                          <a  href="#part3" class="btn btn-info" style="width:100%;text-align:left;margin-top:10px">Loan Sanction Detail informations</a>             
                          <table style="width:100%;line-height: 40px"  id="part3" class="collapse in">           
                           
                            <tr>
                                <td  valign="top" style="width:20%">Letter No:</td>
                                <td >
                                    <input name="letterNo" class="form-control" id="letter_no" style='width:250px'/>
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top" style="width:20%">Letter Date:</td>
                                <td >
                                    <input class="txtDate form-control" id="letterDate" type="text" name="letterDate" style='width:250px'  ></input>

                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                        </table>   
                    </c:if> 


                    <table style="width:60%;line-height: 40px"  >  
                        <tr><td>&nbsp</td></tr> 
                        <c:if test="${ LoanGPFForm.statusId  != 99}">
                            <tr>
                                <td style="width:10%">A.</td>
                                <td style="width:10%">Action Taken:</td>
                                <td colspan="4">
                                    <c:if test="${ LoanGPFForm.statusId  == 43 ||  LoanGPFForm.statusId  == 46  ||  LoanGPFForm.statusId  == 48 ||  LoanGPFForm.statusId  == 47 ||  LoanGPFForm.statusId  == 35}">
                                        <input class="easyui-combobox"  id="loan_status" name="loan_status" data-options="valueField:'value',textField:'label',url:'getprocessList.htm?processid=8&taskidlist=46,48,47,45'" style="width:300px;"  onchange="display_forward(this.value)">
                                    </c:if> 
                                    <c:if test="${ LoanGPFForm.statusId  == 45}">
                                        <input class="easyui-combobox"  id="loan_status" name="loan_status" data-options="valueField:'value',textField:'label',url:'getprocessList.htm?processid=8&taskidlist=99'" style="width:300px;"  onchange="display_forward(this.value)">
                                    </c:if>     


                                </td>
                            </tr>  
                        </c:if>   
                        <c:if test="${ LoanGPFForm.statusId  == 99}">
                            <input type='hidden' name="loan_status" value='44'/>  

                        </c:if>

                        <tr  style="display:none" id="id_forward_div">
                            <td style="width:10%">B.</td>
                            <td style="width:30%">forward To</td>
                            <td>
                                <input id="forwardto" type="text" name="forwardto" style="width:300px;height:25px" readonly="true"   ></input>
                                <a href="javascript:void(0)" id="change" onclick="changepost()">
                                    <button type="button">Search</button>
                                </a>
                            </td>   
                        </tr>
                        <tr><td>&nbsp</td></tr>   
                        <tr>
                            <td style="width:10%">C.</td>
                            <td style="width:30%">Comments</td>
                            <td colspan="4">
                                <!--<input class="easyui-textarea" type="textarea" name="loancomments"  styleId="txtcomments" style="width:40%;height:60px;;" styleClass="textareacolor" required/> -->
                                <textarea rows="4" cols="40" class="easyui-textarea" name="loancomments"  id="loancomments" styleId="txtcomments" style="width:40%;height:60px;;" styleClass="textareacolor" ></textarea>
                            </td>
                        </tr>


                    </table>

                    <div style="text-align:center;padding:5px">
                        <input class="easyui-linkbutton C1" type="submit" name="Save" value="Submit"   style="width:120px;height:30px"/>


                    </div>      
                </div>
            </div>
        </form>
    </body>
</html>