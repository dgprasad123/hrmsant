<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/color.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <link  rel="stylesheet" type="text/css"  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <script type="text/javascript">
            function closeIframeWindow() {
                // alert('hi');
                window.parent.closeIframe();
                //  alert('rashmi');
            }
            $(document).ready(function () {
                $('#loan_status').combobox({
                    onSelect: function (record) {

                        if (record.value == 29 || record.value == 27 || record.value == 92 || record.value == 97) {
                            $("#id_forward_div").show();
                        } else {
                            $("#id_forward_div").hide();
                        }
                    }
                });
            });


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
            function laonsaction() {

                var statusID = $("#statusID").val();
                if (statusID == 97) {
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

                if (statusID == 27) {

                    var samount = $('#samount').val();
                    if (samount == "") {
                        alert("Please enter Sanction Amount");
                        return false;
                    }
                    if (isNaN(samount)) {
                        alert("Invalid Sanction Amount");
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
                if (statusID == 92) {

                    var billno = $('#billno').val();
                    var billdate = $('#billdate').val();
                    if (billno == "") {
                        alert("Please enter Bill no");
                        return false;
                    }
                    if (billdate == "") {
                        alert("Please enter Bill date");
                        return false;
                    }

                    var billAmount = $('#billAmount').val();
                    var chatofAccount = $('#chatofAccount').val();

                    if (billAmount == "") {
                        alert("Please enter Bill Amount");
                        return false;
                    }
                    if (isNaN(billAmount)) {
                        alert("Invalid Bill Amount");
                        return false;
                    }
                    if (chatofAccount == "") {
                        alert("Please enter Chat of Account");
                        return false;
                    }
                    var billDescription = $('#billDescription').val();
                    if (billDescription == "") {
                        alert("Please enter Bill Description");
                        return false;
                    }

                }
                var loan_status = $("#loan_status").val();

                if (loan_status == "") {
                    alert("Please select Action Taken");
                    document.getElementById("loan_status").focus();
                    return false;
                }
                if (loan_status == 29 || loan_status == 27 || loan_status == 92) {
                    var forwardto = $("#forwardto").val();
                    if (forwardto == "") {
                        alert("Please select next loan authority details ");
                        document.getElementById("forwardto").focus();
                        return false;
                    }
                }
                // return false;
                // calculate_prinicipal();
                //calculate_rate_interest();
                // calculate_interest_emi();
                // return false;
            }
            function changepost() {
                var url = 'ChangePostLoanController.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "50%"});
            }
            function openWindow(linkurl, modname) {
                // $("#winfram").attr("src", linkurl);
                //  $("#win").window("open");
                //  $("#win").window("setTitle", modname);
                alert(linkurl);

            }
            $(document).ready(function () {
                $('.recdate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });

            });
            function downlaod_pdf(loanid) {
                var url = "downloadLoanTypeI.htm?loanid=" + loanid;
                window.location = url;

            }
            function sanction_operator(vals) {
                alert(vals);
            }
            function release_amount(val) {
                $('#ramount').val(val);
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
                        si = si.toFixed(2);
                        si = Math.round(si);
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
        <form action="saveApproveLoan.htm" method="POST" commandName="LoanForm" onsubmit="return laonsaction()">
            <input type="hidden" name="loanId" id="loanid" value="${LoanForm.loanId}"/>
            <input type="hidden" name="taskid" id="taskid" value="${LoanForm.taskid}"/>
            <input type="hidden" name="empID" id="empID" value="${LoanForm.empID}"/>
            <input type="hidden" name="statusID" id="statusID" value="${LoanForm.statusId}"/>
            <input type="hidden" name="approvedBy" id="approvedBy" value="${LoanForm.approvedBy}"/>
            <input type="hidden" name="approvedSpc" id="approvedSpc" value="${LoanForm.approvedSpc}"/>
            <input type="hidden" name="hidOffCode" id="hidOffCode"/>
            <input type="hidden" name="hidOffName" id="hidOffName"/>
            <input type="hidden" name="hidSPC" id="hidSPC"/>
            <input type="hidden" name="forwardtoHrmsid" id="forwardtoHrmsid"/>
            <div id="tbl-container" class="easyui-panel" title="Apply Loan"  style="width:100%;overflow: auto;padding-left:10px">
                <div align="left" style="padding-left:10px">
                    <h2 style="text-transform: uppercase">Application form for Advance for the purpose of Motor car/motor cycle/Moped/personal computer</h2>
                    <a href="#part1" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">Loan Detail informations</a>     
                    <table style="width:100%;line-height: 40px"  id="part1" class="collapse in">
                        <tr>
                            <td style="width:30%">1. Name</td>
                            <td style="width:70%"> 
                                ${LoanForm.empName}
                            </td>
                        </tr>
                        <tr>
                            <td>2. Designation</td>
                            <td> 
                                ${LoanForm.designation}
                            </td>
                        </tr>
                        <tr>
                            <td>3. Office address</td>
                            <td> 
                                ${LoanForm.offaddress}
                            </td>
                        </tr>
                        <tr>
                            <td>4. Job Type</td>
                            <td> 
                                ${LoanForm.jobType}
                            </td>
                        </tr>
                        <tr>
                            <td>5. Basic  salary</td>
                            <td> 
                                ${LoanForm.basicsalary}
                            </td>
                        </tr>
                        <tr>
                            <td>6. Net  salary</td>
                            <td> 
                                ${LoanForm.netsalary}
                            </td>
                        </tr>
                        <tr>
                            <td>7.DOB</td>
                            <td> 
                                ${LoanForm.empdob}
                            </td>
                        </tr>
                        <tr>
                            <td>8.Date of Superannuation</td>
                            <td> 
                                ${LoanForm.superannuation}
                            </td>
                        </tr>
                        <tr>
                            <td>9.Loan Apply For</td>
                            <td> 
                                ${LoanForm.loanapplyfor}
                            </td>
                        </tr>
                        <tr>
                            <td>10.Anticipated Price</td>
                            <td> 
                                ${LoanForm.antprice}
                            </td>
                        </tr>
                        <tr>
                            <td>11.Purchase Type</td>
                            <td> 
                                ${LoanForm.purtype}
                            </td>
                        </tr>
                        <tr>
                            <td>12.Amount of advance required</td>
                            <td> 
                                ${LoanForm.amountadv}
                            </td>
                        </tr>
                        <tr>
                            <td>13.No of installments</td>
                            <td> 
                                ${LoanForm.instalments}
                            </td>
                        </tr>
                        <tr>
                            <td>14.Whether advance for similar purpose was availed previously?</td>
                            <td>
                                ${LoanForm.previousAvail}
                            </td>   
                        </tr>
                        <tr id="13a" >
                            <td>15.Whether for Motor Car/Cycle/Moped</td>
                            <td>
                                ${LoanForm.preAdvPur}
                            </td>   
                        </tr>
                        <tr  id="13b">
                            <td>16.Amount of  advance</td>
                            <td>
                                ${LoanForm.amounpretadv}
                            </td>   
                        </tr>
                        <tr id="13c">
                            <td>17.Date of drawal advance</td>
                            <td>
                                ${LoanForm.dateofdrawal}
                            </td>   
                        </tr>
                        <tr>
                            <td>18.Principal along with Interest paid in Full? </td>
                            <td>
                                ${LoanForm.intpaidfull}
                            </td>   
                        </tr>
                        <tr id="13e" >
                            <td>19.Amount of principal/interest standing</td>
                            <td>
                                ${LoanForm.amountstanding}
                            </td>   
                        </tr>
                        <tr>
                            <td>20.Whether the officer is on leave or is about to proceed?</td>
                            <td>
                                ${LoanForm.officerleave}
                            </td>   
                        </tr>
                        <tr>
                            <td>21.Date of commencement leave</td>
                            <td>
                                ${LoanForm.datecommleave}
                            </td>   
                        </tr>
                        <tr>
                            <td>22.Date of expire leave</td>
                            <td>
                                ${LoanForm.dateexpireleave}
                            </td>   
                        </tr>
                        <tr>
                            <td>23.Attachment</td>
                            <td>
                                <!--<input class="easyui-textbox" id="file_att" type="file" name="file" style="width:300px;height:25px"   ></input>-->
                                <c:if test="${ not empty LoanForm.diskFileName}">
                                    <c:set var="lid" value="${LoanForm.loanId}"/>
                                    <a href="DownloadLoanAttch.htm?lid=${LoanForm.loanId}" target="_blank" >Download</a>
                                </c:if>
                                <c:if test="${empty LoanForm.diskFileName}">
                                    <strong> No File Attached</strong>
                                </c:if>   

                            </td>   
                        </tr>
                        <tr>
                            <td>24.Forwarded to</td>
                            <td>
                                ${LoanForm.forwardtoHrmsid}
                            </td>   
                        </tr>
                        <tr>
                            <td>25.Notes</td>
                            <td>
                                ${LoanForm.notes}
                            </td>   
                        </tr>


                    </table>

                    <c:if test="${ LoanForm.statusId  == 27}">


                        <a href="#part2" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left;margin-top:10px">Loan Sanction Detail informations</a>     
                        <table style="width:100%;line-height: 40px"  id="part2" class="collapse in">

                            <tr>
                                <td  valign="top"  style="width:20%">Sanction Amount:</td>
                                <td >
                                    <input name="samount" class="form-control" id="samount" style='width:250px' onkeyup="release_amount(this.value)"/>
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Release Amount:</td>
                                <td >
                                    <input name="ramount" class="form-control" id="ramount" style='width:250px' readonly/>
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
                    <c:if test="${ LoanForm.statusId  == 97}">

                        <a href="#part2" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left;margin-top:10px">Loan Sanction Detail informations</a>     
                        <table style="width:100%;line-height: 40px"  id="part2" class="collapse in">

                            <tr>
                                <td  valign="top"  style="width:20%">Sanction Amount:</td>
                                <td >
                                    ${LoanForm.samount}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Release Amount:</td>
                                <td >
                                    ${LoanForm.releaseAmount}                               
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">No. of installments(Principal):</td>
                                <td >
                                    ${LoanForm.emiPrincipal}                                    
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Installment Amount(Principal):</td>
                                <td >
                                    ${LoanForm.principalAmount}                                   
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Interest Type (Simple/Compound):</td>
                                <td >
                                    ${LoanForm.interestType}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Rate of Interest:</td>
                                <td >
                                    ${LoanForm.rateInterest}                                  
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Total Interest Amount:</td>
                                <td >
                                    ${LoanForm.totalInterestamount}                              
                                </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">No. of installments(Interest):</td>
                                <td >
                                    ${LoanForm.emiInterest}                                    
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Installment Amount(Interest):</td>
                                <td >
                                    ${LoanForm.interestAmount}

                                </td>
                            </tr>


                            <tr>
                                <td  valign="top"  style="width:20%">Last Installment Amount:</td>
                                <td >
                                    ${LoanForm.lastInstallment}                                
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Penal Rate of Interest:</td>
                                <td >
                                    ${LoanForm.penalRate} 

                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Moratorium Period Required:</td>
                                <td >
                                    ${LoanForm.moratoriumRequired}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Moratorium Period(In Months):</td>
                                <td >
                                    ${LoanForm.moratoriumPeriod}                                  
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Recovery Start Date:</td>
                                <td >
                                    ${LoanForm.recDate} 

                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Insurance Flag:</td>
                                <td >
                                    ${LoanForm.insuranceFlag} 

                                </td>
                            </tr>
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


                    <c:if test="${ LoanForm.statusId  == 92}">


                        <a href="#part2" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left;margin-top:10px">Loan Sanction Detail informations</a>     
                        <table style="width:100%;line-height: 40px"  id="part2" class="collapse in">

                            <tr>
                                <td  valign="top" style="width:20%">Letter No:</td>
                                <td >                                  
                                    ${LoanForm.letterNo}
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top" style="width:20%">Letter Date:</td>
                                <td >
                                    ${LoanForm.letterDate}                                 

                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Sanction Amount:</td>
                                <td >
                                    ${LoanForm.samount}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Release Amount:</td>
                                <td >
                                    ${LoanForm.releaseAmount}                               
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">No. of installments(Principal):</td>
                                <td >
                                    ${LoanForm.emiPrincipal}                                    
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Installment Amount(Principal):</td>
                                <td >
                                    ${LoanForm.principalAmount}                                   
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Interest Type (Simple/Compound):</td>
                                <td >
                                    ${LoanForm.interestType}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Rate of Interest:</td>
                                <td >
                                    ${LoanForm.rateInterest}                                  
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Total Interest Amount:</td>
                                <td >
                                    ${LoanForm.totalInterestamount}                              
                                </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">No. of installments(Interest):</td>
                                <td >
                                    ${LoanForm.emiInterest}                                    
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Installment Amount(Interest):</td>
                                <td >
                                    ${LoanForm.interestAmount}

                                </td>
                            </tr>


                            <tr>
                                <td  valign="top"  style="width:20%">Last Installment Amount:</td>
                                <td >
                                    ${LoanForm.lastInstallment}                                
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Penal Rate of Interest:</td>
                                <td >
                                    ${LoanForm.penalRate} 

                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Moratorium Period Required:</td>
                                <td >
                                    ${LoanForm.moratoriumRequired}                                   
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Moratorium Period(In Months):</td>
                                <td >
                                    ${LoanForm.moratoriumPeriod}                                  
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Recovery Start Date:</td>
                                <td >
                                    ${LoanForm.recDate} 

                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top">Insurance Flag:</td>
                                <td >
                                    ${LoanForm.insuranceFlag} 

                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                        <a href="#part3" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left;margin-top:10px">Bill Detail informations</a>     
                        <table style="width:100%;line-height: 40px"  id="part3" class="collapse in">
                            <tr>
                                <td  valign="top" style="width:20%">Chat Of Account:</td>
                                <td >
                                    ${LoanForm.chatofAccount} 
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top" style="width:20%">Major head:</td>
                                <td >
                                    ${LoanForm.majorhead}
                                </td>
                            </tr>

                            <tr>
                                <td  valign="top" style="width:20%">Demand No:</td>
                                <td >
                                    ${LoanForm.demandNo}
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top" style="width:20%">Bill Id</td>
                                <td >
                                    ${LoanForm.billid}
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top" style="width:20%">Bill No:</td>
                                <td >
                                    <input name="billno" class="form-control" id="billno" style='width:250px' />
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top" style="width:20%">Bill Date:</td>
                                <td >
                                    <input class="txtDate form-control" id="billdate" type="text" name="billdate" style='width:250px'  ></input>

                                </td>
                            </tr>
                            <tr>
                                <td  valign="top"  style="width:20%">Bill Amount:</td>
                                <td >
                                    <input name="billAmount" class="form-control" id="billAmount" style='width:250px' value="${LoanForm.releaseAmount}"/>
                                </td>
                                <td>&nbsp;</td>
                                <td  valign="top"  style="width:20%">Bill Description</td>
                                <td >
                                    <input name="billDescription" class="form-control" id="billDescription" style='width:250px'/>
                                </td>
                            </tr>

                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                            </tr>
                            <c:if test="${ LoanForm.statusId  == 92}">
                                <input type='hidden' name="loan_status" value='26'/>

                            </c:if>
                        </table>    
                    </c:if>   


                    <table style="width:60%;line-height: 40px"  >  
                        <tr><td>&nbsp</td></tr>
                        <c:if test="${ LoanForm.statusId  != 92}">
                            <tr>

                                <td >26.Action Taken:</td>
                                <td colspan="4">
                                    <c:if test="${ LoanForm.statusId  == 25 ||  LoanForm.statusId  == 28  ||  LoanForm.statusId  == 24 ||  LoanForm.statusId  == 29}">
                                        <input class="easyui-combobox"  id="loan_status" name="loan_status" data-options="valueField:'value',textField:'label',url:'getprocessList.htm?processid=6&taskidlist=28,24,29,27'" style="width:300px;"  onchange="display_forward(this.value)">
                                    </c:if>

                                    <c:if test="${ LoanForm.statusId  == 27 }">
                                        <input class="easyui-combobox"  id="loan_status" name="loan_status" data-options="valueField:'value',textField:'label',url:'getprocessList.htm?processid=6&taskidlist=97'" style="width:300px;"  onchange="display_forward(this.value)">
                                    </c:if> 
                                    <c:if test="${ LoanForm.statusId  == 97 }">
                                        <input class="easyui-combobox"  id="loan_status" name="loan_status" data-options="valueField:'value',textField:'label',url:'getprocessList.htm?processid=6&taskidlist=92'" style="width:300px;"  onchange="display_forward(this.value)">
                                    </c:if>     



                                </td>
                            </tr>
                        </c:if>
                        <tr  id="id_forward_div" style="display: none">
                            <td>27.Forward to</td>
                            <td>
                                <input id="forwardto" type="text" name="forwardto" style="width:300px;height:25px" readonly="true"   />
                                <a href="javascript:void(0)" id="change" onclick="changepost()">
                                    <button type="button" class="easyui-linkbutton C5" style="width:120px;height:30px">Search</button>
                                </a>
                            </td>   
                        </tr>
                        <tr><td>&nbsp</td></tr>
                        <tr >

                            <td >28.Comments:</td>
                            <td colspan="4">
                                <!--<input class="easyui-textarea" type="textarea" name="loancomments"  styleId="txtcomments" style="width:40%;height:60px;;" styleClass="textareacolor" required/> -->
                                <textarea rows="4" cols="40" class="easyui-textarea" name="loancomments"  id="loancomments" styleId="txtcomments" style="width:40%;height:60px;;" styleClass="textareacolor" ></textarea>
                            </td>
                        </tr>
                    </table>   
                    <div style="text-align:center;margin:20px 0">
                        <input class="easyui-linkbutton C1" type="submit" name="Save" value="Submit"  style="width:120px;height:30px" />&nbsp;
                      
                        <c:if test="${ LoanForm.statusId  == 27}">
                            <!--   <input class="easyui-linkbutton C5" type="button" name="Download" value=" <!--Download PDF Form" onclick="downlaod_pdf(${LoanForm.loanId})"   style="width:120px;height:30px"/>
                           <a href="javascript:void(0)" onclick="window.open('SactionOrder.htm?taskid=${LoanForm.taskid}&loanid=${LoanForm.loanId}', 'Saction Order', 'width=600,height=600')" title="Saction Order "> <input class="easyui-linkbutton C2" type="button" name="gorder" value="Generate Order" style="width:120px;height:30px"/></a>
                            -->
                        </c:if>

                    </div>      
                </div>
            </div>
        </form>
    </body>
</html>