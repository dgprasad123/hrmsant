<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            function checkEmpty() {
                if (($('#sltbillname').val() == "") && ($('#sltLoanlist').val() == "")) {
                    alert("Please select Bill Description and Loan Name");
                    return false;
                } else if (($('#sltbillname').val() != "") && ($('#sltLoanlist').val() == "")) {
                    alert("Please select Loan Name");
                    return false;
                } else if (($('#sltbillname').val() == "") && ($('#sltLoanlist').val() != "")) {
                    alert("Please select Bill Description");
                    return false;
                }
                return true;
            }
            function checkAll(isChecked)
            {
                if (document.getElementById('frmLoan').elements['chkupdtdemp'].length == 1)
                {
                    if (isChecked)
                        document.getElementById('frmLoan').elements['chkupdtdemp'].checked = true;
                    else
                        document.getElementById('frmLoan').elements['chkupdtdemp'].checked = false;
                }
                else
                {
                    for (var i = 0; i < document.getElementById('frmLoan').elements['chkupdtdemp'].length; i++)
                    {
                        if (isChecked)
                            document.getElementById('frmLoan').elements['chkupdtdemp'][i].checked = true;
                        else
                            document.getElementById('frmLoan').elements['chkupdtdemp'][i].checked = false;
                    }
                }
            }
            function checkSelect(orgVal, idx)
            {
                if (orgVal && orgVal != '0')
                {
                    $('#chkupdtdemp_' + idx)[0].checked = true;
                }
                else
                {
                    $('#chkupdtdemp_' + idx)[0].checked = false;
                }
            }
            function validateForm()
            {
                isSelected = false;
                for (var i = 0; i < document.getElementById('frmLoan').elements['slids'].length; i++)
                {
                    idx = document.getElementById('frmLoan').elements['slids'][i].value;
                    if ($('#chkupdtdemp_' + idx)[0].checked)
                    {
                        isSelected = true;
                        if ($('#orgloanamt_' + idx).val() == '' || $('#orgloanamt_' + idx).val() == '0' || isNaN($('#orgloanamt_' + idx).val()))
                        {
                            alert("Please enter Original Amount. Must be a valid Integer Value.");
                            $('#orgloanamt_' + idx)[0].focus();
                            $('#orgloanamt_' + idx)[0].select();
                            return false;
                        }
                        if ($('#totalinstlno_' + idx).val() == '' || $('#totalinstlno_' + idx).val() == '0' || isNaN($('#totalinstlno_' + idx).val()))
                        {
                            alert("Please enter Total Instal Number. Must be a valid Integer Value.");
                            $('#orgloanamt_' + idx)[0].focus();
                            $('#orgloanamt_' + idx)[0].select();
                            return false;
                        }
                        if ($('#instlamt_' + idx).val() == '' || $('#instlamt_' + idx).val() == '0' || isNaN($('#instlamt_' + idx).val()))
                        {
                            alert("Please enter Installment Amount. Must be a valid Integer Value.");
                            $('#orgloanamt_' + idx)[0].focus();
                            $('#orgloanamt_' + idx)[0].select();
                            return false;
                        }
                    }
                }
                if (!isSelected)
                {
                    alert("Please check at least one employee to create a new loan.");
                    return false;
                }
                $('#loader_wrap').css('display', 'block');
                $('#loader').css('display', 'block');
                postData = updateHiddenValue();
                $.ajax({
                    url: 'SaveNewGroupLoan.htm',
                    type: 'post',
                    data: 'data=' + postData,
                    success: function(retVal) {
                        //$('#loader_wrap').css('display', 'none');
                        //$('#loader').css('display', 'none');
                        self.location = 'GroupLoan.htm?loan=' + $('#sltLoanlist').val() + '&billname=' + $('#sltbillname').val();
                    }
                });
            }
            function updateHiddenValue()
            {
                postData = '';
                for (var i = 0; i < document.getElementById('frmLoan').elements['slids'].length; i++)
                {
                    fieldValues = '';
                    idx = document.getElementById('frmLoan').elements['slids'][i].value;
                    if ($('#chkupdtdemp_' + idx)[0].checked)
                    {
                        if ($('#lastpaidinstlno_' + idx).val() == '')
                        {
                            $('#lastpaidinstlno_' + idx).val('0');
                        }
                        if ($('#cumulativeamtpaid_' + idx).val() == '')
                        {
                            $('#cumulativeamtpaid_' + idx).val('0');
                        }
                        fieldValues = $('#sltbillname').val() + '|' + $('#sltLoanlist').val() + '|' + $('#empid_' + idx).val() + '|' + $('#chkNowDeduct_' + idx).val() + '|' + $('#orgloanamt_' + idx).val() + '|' + $('#totalinstlno_' + idx).val() + '|' + $('#instlamt_' + idx).val() + '|' + $('#lastpaidinstlno_' + idx).val() + '|' + $('#cumulativeamtpaid_' + idx).val();
                        //Create the value
                        postData += (postData == '') ? (fieldValues) : '@@' + (fieldValues);
                    }
                }
                return postData;
            }
            function checkClickValue(isChecked, idx)
            {
                if (!isChecked)
                {
                    $('#orgloanamt_' + idx).val('0');
                    $('#totalinstlno_' + idx).val('0');
                    $('#instlamt_' + idx).val('0');
                }
            }
            function startStopLoan(pref, idx, loanStatus)
            {
                $('#loader'+pref+'_'+idx).css('display', 'block');
                $.ajax({
                    url: 'StartStopLoan.htm',
                    type: 'get',
                    data: 'loanid='+$('#loanid_' + idx).val()+ '&status=' + loanStatus,
                    success: function(retVal) {
                        $('#loader'+pref+'_'+idx).css('display', 'none');
                        if(pref == 2)
                        {
                            $('#start_stop_blk_'+idx).html('<input type="button" class="btn btn-success btn-sm" style="background:#890000;" name="action" value="Start Loan" onclick="javascript: startStopLoan(1, '+idx+', \'N\')" /><span id="loader1_'+idx+'" style="display:none;font-size:8pt;color:#888888;font-style:italic;"><img src="images/ajax-loader.gif" /><br />Please wait...</span>');
                        }
                        else
                        {
                            $('#start_stop_blk_'+idx).html('<input type="button" class="btn btn-success btn-sm" style="background:#007EAF;" name="action" value="Stop Loan" onclick="javascript: startStopLoan(2, '+idx+', \'Y\')" /><span id="loader2_'+idx+'" style="display:none;font-size:8pt;color:#888888;font-style:italic;"><img src="images/ajax-loader.gif" /><br />Please wait...</span>');
                        }
                    }
                });
            }
        </script>
    </head>
    <body>
        <div id="loader_wrap" style="display:none;position:fixed;width:100%;height:100%;top:0;left:0;background:#000;opacity:0.7;z-index:10;">
        </div>
        <div id="loader" style="display: none;position:fixed;left:45%;top:40%;z-index:10;color:#00A2E0;font-style:italic;text-align:center;">
            <img src="images/square_loader.gif" /><br />
            Processing, Please wait...
        </div>
        <form:form class="form-inline" id="frmLoan" action="GroupLoan.htm" method="POST" commandName="groupLoanForm">
            <form:hidden path="hidselectedempforUpdt" id="hidselectedempforUpdt" />
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-2" style="text-align:right;">Bill Name:</div>
                            <div class="col-lg-3">
                                <form:select path="sltbillname" id="sltbillname" class="form-control">
                                    <form:option value="">--Select One--</form:option>
                                    <form:options items="${sectionList}" itemLabel="label" itemValue="value"/>
                                </form:select>

                            </div>
                            <div class="col-lg-2" style="text-align:right;">Select Loan:</div>
                            <div class="col-lg-5"><form:select path="sltLoanlist" id="sltLoanlist" class="form-control">
                                    <form:option value="">--Select One--</form:option>
                                    <form:options items="${loanList}" itemLabel="label" itemValue="value"/>
                                </form:select>
                                <input type="submit" class="btn btn-success" name="action" value="Search" onclick="return checkEmpty()"/>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <input type="button" class="btn btn-success" style="background:#007EAF;" name="action" value="Save New Loan" onclick="javascript:validateForm()" />
                        <table class="table table-bordered" style="font-size:10pt;">
                            <thead>
                                <tr>
                                    <th width="3%">Sl</th>
                                    <th width="7%" valign="top" style="text-align:center;">
                                        <input type="checkbox" id="selectAll" onclick="javascript: checkAll(this.checked)">
                                        Check All
                                    </th>
                                    <th width="7%">HRMS ID/<br />GPF No</th>
                                    <th width="25%">Name</th>
                                    <th width="10%">Now Deduct</th>
                                    <th width="9%">Original Amt</th>
                                    <th width="9%">Total No<br>of Instl</th>
                                    <th width="9%">Inst Amt</th>
                                    <th width="8%">Last Paid<br>Instl No</th>
                                    <th width="8%">Cumulative Amt<br> Paid</th>
                                    <th>
                                        Stop/Start Loan
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${empList}" var="empList" varStatus="cnt">
                                    <tr<c:if test="${not empty empList.orgloanamt}"> style="color:#008900"</c:if>>
                                        <td>${cnt.index+1}</td>
                                        <td style="text-align:center;"><c:if test="${not empty empList.orgloanamt}"><input type="hidden" name="loanid" id="loanid_${cnt.index+1}" value="${empList.loanId}" /></c:if>
                                            <c:if test="${empty empList.orgloanamt}"><form:checkbox path="chkupdtdemp" id="chkupdtdemp_${cnt.index+1}" value="" onclick="javascript: checkClickValue(this.checked, ${cnt.index+1})" />
                                                <input type="hidden" name="empid" id="empid_${cnt.index+1}" value="${empList.empid}" />
                                                <input type="hidden" name="slids" value="${cnt.index+1}" />
                                            </c:if></td>
                                        <td>${empList.empid}/<br />${empList.gpfno}</td>
                                        <td>${empList.empname}</td>
                                        <td><c:if test="${not empty empList.chkNowDeduct}">
                                                <c:if test="${empList.chkNowDeduct == 'P'}"><strong>PRINCIPAL</strong></c:if>
                                                <c:if test="${empList.chkNowDeduct == 'I'}"><strong>INTEREST</strong></c:if>
                                            </c:if>
                                            <c:if test="${empty empList.chkNowDeduct}"><form:select size="1" path="chkNowDeduct" id="chkNowDeduct_${cnt.index+1}" class="form-control">
                                                    <form:option value="P">PRINCIPAL</form:option>
                                                    <form:option value="I">INTEREST</form:option>
                                                </form:select></c:if>                                        </td>
                                        <td><c:if test="${not empty empList.orgloanamt}">${empList.orgloanamt}</c:if>
                                            <c:if test="${empty empList.orgloanamt}"><form:input path="orgloanamt" id="orgloanamt_${cnt.index+1}" value="0" class="form-control" size="10" onfocus="javascript: this.select();" onblur="javascript: checkSelect(this.value, ${cnt.index+1})" /></c:if></td>
                                        <td><c:if test="${not empty empList.totalinstlno}">${empList.totalinstlno}</c:if>
                                            <c:if test="${empty empList.totalinstlno}"><form:input path="totalinstlno" id="totalinstlno_${cnt.index+1}" value="0" class="form-control" size="10" onfocus="javascript: this.select();"  onblur="javascript: checkSelect(this.value, ${cnt.index+1})" /></c:if></td>
                                        <td><c:if test="${not empty empList.instlamt}">${empList.instlamt}</c:if>
                                            <c:if test="${empty empList.instlamt}"><form:input path="instlamt" id="instlamt_${cnt.index+1}" value="0" class="form-control" size="10" onfocus="javascript: this.select();"  onblur="javascript: checkSelect(this.value, ${cnt.index+1})" /></c:if></td>
                                        <td><c:if test="${not empty empList.lastpaidinstlno}">${empList.lastpaidinstlno}</c:if>
                                            <c:if test="${empty empList.lastpaidinstlno}"><form:input path="lastpaidinstlno" value="0" id="lastpaidinstlno_${cnt.index+1}" class="form-control" size="10" onfocus="javascript: this.select();" /></c:if></td>
                                        <td><c:if test="${not empty empList.cumulativeamtpaid}">${empList.cumulativeamtpaid}</c:if>
                                            <c:if test="${empty empList.cumulativeamtpaid}"><form:input path="cumulativeamtpaid" value="0" id="cumulativeamtpaid_${cnt.index+1}" class="form-control" size="10" onfocus="javascript: this.select();" /></c:if></td>
                                        <td style="text-align:center;"><c:if test="${not empty empList.orgloanamt}">
                                                <div id="start_stop_blk_${cnt.index+1}">
                                                <c:if test="${empList.stoploan == 'Y'}"><input type="button" class="btn btn-success btn-sm" style="background:#890000;" name="action" value="Start Loan" onclick="javascript: startStopLoan(1, ${cnt.index+1}, 'N')" />
                                                    <span id="loader1_${cnt.index+1}" style="display:none;font-size:8pt;color:#888888;font-style:italic;"><img src="images/ajax-loader.gif" /><br />Please wait...</span></c:if>
                                                <c:if test="${empty empList.stoploan || empList.stoploan == 'N'}"><input type="button" class="btn btn-success btn-sm" style="background:#007EAF;" name="action" value="Stop Loan" onclick="javascript: startStopLoan(2, ${cnt.index+1}, 'Y')" />
                                                    <span id="loader2_${cnt.index+1}" style="display:none;font-size:8pt;color:#888888;font-style:italic;"><img src="images/ajax-loader.gif" /><br />Please wait...</span>
                                                </c:if>
                                                </div>
                                            </c:if></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <input type="button" class="btn btn-success" style="background:#007EAF;" name="action" value="Save New Loan" onclick="javascript:validateForm()" />
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
