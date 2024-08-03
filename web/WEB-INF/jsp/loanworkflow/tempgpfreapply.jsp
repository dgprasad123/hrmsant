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

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript">
            function choose_previous_loan(val) {
                if (val == "NO") {

                    $(".radiovalue").hide();
                    $("#amount_drawal").val('');
                    $("#date_drawal").val('');
                    $("#purpose_drawal").val('');
                    $("#date_repaid").val('');
                    $("#date_drawal_sanction").val('');
                    $("#date_drawal_cadvance").val('');
                    $("#balance_outstanding").val('');
                    $("#rate_recovery").val('');
                    $("#final_payment").val('');

                } else {

                    $(".radiovalue").show();
                }
            }
            function applyloan()
            {
                var radiovalue = "";
                var radios = document.getElementsByName("advance_taken");
                var formValid = false;


                var i = 0;
                while (!formValid && i < radios.length) {
                    if (radios[i].checked)
                        formValid = true;
                    radiovalue = radios[i].value;
                    i++;
                }
                if (!formValid) {
                    alert("Please mention weather Weather any advance was taken previously !!");
                    return false;
                }
                if (radiovalue == "YES") {

                    var amount_drawal = $("#amount_drawal").val();
                    var date_drawal = $("#date_drawal").val();
                    var purpose_drawal = $("#purpose_drawal").val();
                    var date_repaid = $("#date_repaid").val();
                    var date_drawal_sanction = $("#date_drawal_sanction").val();
                    var date_drawal_cadvance = $("#date_drawal_cadvance").val();
                    var balance_outstanding = $("#balance_outstanding").val();
                    var rate_recovery = $("#rate_recovery").val();
                    var final_payment = $("#final_payment").val();

                    if (amount_drawal == "" || isNaN(amount_drawal)) {

                        alert("Please mention Amount of its drawa!!");
                        return false;
                    }
                    if (date_drawal == "") {

                        alert("Please mention Date of its drawa!!");
                        return false;
                    }
                    if (purpose_drawal == "") {

                        alert("Please mention purpose of its drawa!!");
                        return false;
                    }
                    if (date_repaid == "") {

                        alert("Please mention the date on which such advance was finally repaid!!");
                        return false;
                    }
                }

                var amount_adv=$("#amount_adv").val();
                if(amount_adv=="" || isNaN(amount_adv)){
                     alert("Please mention Amount of advance now applied for!!");
                        return false;
                }
                 var noofinst=$("#noofinst").val();
                if(noofinst=="" || isNaN(noofinst)){
                     alert("Please mention Number of instalments in which it is proposed to reply!!");
                        return false;
                }


                var fup = document.getElementById('file_att');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();
                if(fileName!=''){
                    if ((ext != "pdf" && ext != "zip")) {
                        alert("Upload pdf/zip files only");
                        fup.focus();
                        return false;
                    }
            }
                var forwardto = $("#forwardto").val();
                if (forwardto == "") {
                    alert("Please select your loan authority ");
                    document.getElementById("forwardto").focus();
                    return false;
                }
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
              function delAttach(obj,val_id) {
                if (confirm("Are you sure to Delete?")) {
                    var dataString = 'loanId=' + obj+'&vals=' +val_id;
                    //alert("dataString is: " + dataString);
                    $.ajax({
                        type: "POST",
                        url: 'deleteHBALoanAttachment.htm',
                        data: dataString,
                        dataType: "json"
                    }).done(function(serverResponse) {
                        $.messager.alert(serverResponse.msgType, serverResponse.msg);
                    });
                }
            }

        </script>    
    </head>

    <body>       
        <form action="savetempgpfreapply.htm" method="POST" commandName="LoanForm" onsubmit="return applyloan()" enctype="multipart/form-data">
              <input type="hidden" name="loanId" id="loanid" value="${LoanTempGPFForm.loanId}"/>
            <input type="hidden" name="taskid" id="taskid" value="${LoanTempGPFForm.taskid}"/>
             <input type="hidden" name="approvedBy" id="approvedBy" value="${LoanTempGPFForm.approvedBy}"/>
              <input type="hidden" name="approvedSpc" id="approvedSpc" value="${LoanTempGPFForm.approvedSpc}"/>
              <input type="hidden" name="hidOffCode" id="hidOffCode"/>
            <input type="hidden" name="hidOffName" id="hidOffName"/>
            <input type="hidden" name="hidSPC" id="hidSPC"/>
            <input type="hidden" name="forwardtoHrmsid" id="forwardtoHrmsid"/>

            <input type="hidden" name="accountOfficer" id="accountOfficer" value="${LoanTempGPFForm.ddocode}"/>
            <input type="hidden" name="empSPC" id="empSPC" value="${LoanTempGPFForm.empSPC}"/>
            <div id="tbl-container" class="easyui-panel" title="Reapply Loan"  style="width:100%;overflow: auto;">

                <div align="left" style="padding-left:10px">
                    <h2 style="text-transform: uppercase">Form of application for temporary withdrawal of money from the General Provident fund.</h2>
                    <table style="width:100%">  
                        <tr>
                            <td style="width:10%">1.</td>
                            <td style="width:30%">Name of applicant</td>
                            <td style="width:60%"> 
                                ${LoanTempGPFForm.empName}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">2.</td>
                            <td style="width:30%">Designation</td>
                            <td style="width:60%"> 
                                ${LoanTempGPFForm.designation}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">3.</td>
                            <td style="width:30%">Account Number</td>
                            <td style="width:60%"> 
                                ${LoanTempGPFForm.gpfno}
                            </td>
                        </tr>

                        <tr>
                            <td style="width:10%">4.</td>
                            <td style="width:30%">Pay</td>
                            <td style="width:60%"> 
                                ${LoanTempGPFForm.pay}
                            </td>
                        </tr>
                    </table>


                    <table style="width:100%">        
                        <h2>5.Amount standing at credit of the applicant up to date application</h2>   
                        <tr>
                            <td style="width:10%">(i).</td>
                            <td style="width:30%">Amount at credit as per last Annual Account furnished by AG</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="amount_credit" value="${LoanTempGPFForm.amount_credit}" type="number" name="amount_credit" style="width:300px;height:25px" required /> 
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">(ii).</td>
                            <td style="width:30%">Add. recoveries of subscription and on account of previous advance, if any from 1st April to date.</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="amount_subscription" value="${LoanTempGPFForm.amount_subscription}" type="number" name="amount_subscription" style="width:300px;height:25px"  required/> 
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">(iii).</td>
                            <td style="width:30%">Deduct advance drawn, if any subsequent to 31st March</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="deduct_adv" value="${LoanTempGPFForm.deduct_adv}" type="number" name="deduct_adv" style="width:300px;height:25px"  required/> 
                            </td>
                        </tr>
                    </table>
                    <table style="width:100%">        

                        <tr>
                            <td style="width:10%">6.</td>
                            <td style="width:30%">Weather any advance was taken previously </td>
                            <td style="width:60%"> 
                                 <c:set var="loanadvance_taken" value="${LoanTempGPFForm.advance_taken}"/>
                                <input type="radio" class="easyui-radio" name="advance_taken"  <c:if test="${loanadvance_taken == 'YES'}"> checked='checked'</c:if>   value="YES"  onclick="choose_previous_loan(this.value)"/>YES<br/>
                                <input type="radio" class="easyui-radio" name="advance_taken"  <c:if test="${loanadvance_taken == 'NO'}"> checked='checked'</c:if>  value="NO"  onclick="choose_previous_loan(this.value)"/>NO<br/>
                            </td>
                        </tr>
                        <tr class="radiovalue">
                            <td style="width:10%">6(i).</td>
                            <td style="width:30%">The amount of its drawal</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox loantaken" id="amount_drawal"  value="${LoanTempGPFForm.amount_drawal}" type="text" name="amount_drawal" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                        <tr  class="radiovalue">
                            <td style="width:10%">(ii).</td>
                            <td style="width:30%">Date of its drawal</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="date_drawal" value="${LoanTempGPFForm.date_drawal}" type="text" name="date_drawal" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                        <tr  class="radiovalue">
                            <td style="width:10%">(iii).</td>
                            <td style="width:30%">The purpose for which the advance was granted</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="purpose_drawal" value="${LoanTempGPFForm.purpose_drawal}" type="text" name="purpose_drawal" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                        <tr  class="radiovalue">
                            <td style="width:10%">(iv).</td>
                            <td style="width:30%">The date on which such advance was finally repaid</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="date_repaid" value="${LoanTempGPFForm.date_repaid}" type="text" name="date_repaid" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                        <tr  class="radiovalue">
                            <td style="width:10%">(v).</td>
                            <td style="width:30%">In case consolidation of more than one advance was sanctioned, the date of such sanction has been repaid</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="date_drawal_sanction" value="${LoanTempGPFForm.date_drawal_sanction}" type="text" name="date_drawal_sanction" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                        <tr  class="radiovalue">
                            <td style="width:10%">(vi).</td>
                            <td style="width:30%">In case consolidation of more than one advance was sanctioned, the date of such consolidated advance has been repaid</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="date_drawal_cadvance" type="text" name="date_drawal_cadvance" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                        <tr  class="radiovalue">
                            <td style="width:10%">(vii).</td>
                            <td style="width:30%">Balance outstanding, if any of the advance or consolidated advance, as the case may be</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="balance_outstanding" value="${LoanTempGPFForm.balance_outstanding}" type="text" name="balance_outstanding" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                        <tr  class="radiovalue">
                            <td style="width:10%">(viii).</td>
                            <td style="width:30%">The rate/rates of recovery of outstanding Advance/Advances</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="rate_recovery"  value="${LoanTempGPFForm.rate_recovery}" type="text" name="rate_recovery" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                        <tr  class="radiovalue">
                            <td style="width:10%">(ix).</td>
                            <td style="width:30%">Weather final payment application has been submitted <br/>
                                (In case of subscriber, who is due to retire within one year)
                            </td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="final_payment" value="${LoanTempGPFForm.final_payment}" type="text" name="final_payment" style="width:300px;height:25px"  /> 
                            </td>
                        </tr>
                    </table>
                    <table style="width:100%">  
                        <tr>
                            <td style="width:10%">7.</td>
                            <td style="width:30%">Amount of advance now applied for(this should not ordinarily exceed three month's pay)</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="amount_adv" value="${LoanTempGPFForm.amount_adv}"  type="number" name="amount_adv" style="width:300px;height:25px"  required/> 
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">8.</td>
                            <td style="width:30%">Full particulars of the purpose for which the present advance applied for is required</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="purpose" value="${LoanTempGPFForm.purpose}" type="text" name="purpose" style="width:300px;height:25px"  required/> 
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">9.</td>
                            <td style="width:30%">Total amount of advance, including outstanding balance, if any plus the advance applied for</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="total_advance" value="${LoanTempGPFForm.total_advance}" type="number" name="total_advance" style="width:300px;height:25px" required /> 
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">10.</td>
                            <td style="width:30%">Number of instalments in which it is proposed to reply the present/consolidated advance</td>
                            <td style="width:60%"> 
                                <input class="easyui-textbox" id="noofinst" value="${LoanTempGPFForm.noofinst}"  type="number" name="noofinst" style="width:300px;height:25px" required /> 
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">11. </td>
                            <td style="width:30%">Attachment </td>
                            <td style="width:60%"> 
                                <input id="file_att" type="file" name="file_att" style="width:300px;height:25px"/>
                                  <c:if test="${ not empty LoanTempGPFForm.diskFileName}">
                                    <c:set var="lid" value="${LoanTempGPFForm.loanId}"/>
                                    <a href="DownloadtempgpfLoanAttch.htm?lid=${LoanTempGPFForm.loanId}" target="_blank" >Download</a>
                                    <a href="javascript:void(0)" style="text-decoration:none;" onclick="delAttach(${LoanTempGPFForm.loanId})">
                                       <img src="images/Delete-icon.png"/>
                                    </a>
                               </c:if>
                            </td>
                        </tr>
                         <tr>
                            <td style="width:10%">12. </td>
                            <td style="width:30%">Comments</td>
                            <td valign="top">
                                  <textarea rows="4" cols="40" class="easyui-textarea" name="loancomments"  id="loancomments" styleId="txtcomments" style="width:40%;height:60px;;" styleClass="textareacolor" >${LoanTempGPFForm.loancomments}</textarea>
                            </td>   
                        </tr>
                        <tr>
                            <td style="width:10%">13. </td>
                            <td style="width:30%">forward To</td>
                            <td>
                                <input id="forwardto" type="text" name="forwardto" style="width:300px;height:25px" readonly="true"   ></input>
                                <a href="javascript:void(0)" id="change" onclick="changepost()">
                                    <button type="button">Search</button>
                                </a>
                            </td>   
                        </tr>

                        

                    </table>
                    <div style="text-align:center;padding:5px">
                        <input class="easyui-linkbutton" type="submit" name="Save" value="Submit" />
                        <input class="easyui-linkbutton" type="button" name="Back" value="Back" onclick="self.location = 'loanList.htm'"/>

                    </div>      
                </div>
            </div>
        </form>
    </body>
</html>