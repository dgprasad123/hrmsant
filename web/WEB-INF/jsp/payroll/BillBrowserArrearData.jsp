<%-- 
    Document   : BillBrowserData
    Created on : Oct 30, 2017, 11:42:46 PM
    Author     : Manas

--%>

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
            $(document).ready(function() {
                $('#btn-reprocess').click(function() {
                    $('#btn-reprocess').hide();
                });
                validateBillDate();
            });
            function validateForm() {

                if ($("#billdesc").val() == '') {
                    alert('Please enter Bill number.');
                    $("#billdesc").focus();
                    return false;
                }
                if ($("#billDate").val() == '') {
                    alert('Please enter Bill Date.');
                    $("#billDate").focus();
                    return false;
                } else {
                    var obj = $("#billDate").val();
                    ret = isDate($("#billDate").val(), 'Incorrect date');
                    if (ret == false) {
                        $("#billDate").focus();
                        return false;
                    }
                }

                if ($("#treasury").val() == '') {
                    alert('Please Select Treasury .');
                    $("#treasury").focus();
                    return false;
                }

                if ($("#sltCOList").val() == '') {
                    alert('CO name should not be blank. ');
                    return false;
                }

            }


            function isDate(date, msg) {
                // dd-mmm-yyyy format

                if (date.length == 0) {
                    return true; // Ignore null value
                }
                if (date.length == 10) {
                    date = "0" + date; // Add a leading zero
                }
                if (date.length != 11) {
                    alert(msg);
                    return false;
                }
                day = date.substring(0, 2);
                month = date.substring(3, 6).toUpperCase();
                year = date.substring(7, 11);
                if (isNaN(day) || (day < 0) || isNaN(year) || (year < 1)) {
                    alert(msg);
                    return false;
                }

                // Ensure valid month and set maximum days for that month...
                if ((month == "JAN") || (month == "jan") || (month == "MAR") || (month == "mar") || (month == "MAY") || (month == "may") ||
                        (month == "JUL") || (month == "jul") || (month == "AUG") || (month == "aug") || (month == "OCT") || (month == "oct") ||
                        (month == "DEC")) {
                    monthdays = 31
                }
                else if ((month == "APR") || (month == "apr") || (month == "JUN") || (month == "jun") || (month == "sep") || (month == "SEP") ||
                        (month == "NOV") || (month == "nov")) {
                    monthdays = 30
                }
                else if ((month == "FEB") || (month == "feb")) {
                    monthdays = ((year % 4) == 0) ? 29 : 28;
                }
                else {
                    alert(msg);
                    return false;
                }
                if (day > monthdays) {
                    alert(msg);
                    return false;
                }
                return true;
            }
            function showChangeObjectHeadWindow(objectHead) {
                $('#billNoplc').text($('#billNo').val());
                $('#billMonthplc').text($('#sltMonth').val());
                $('#billYearplc').text($('#sltYear').val());
                $('#allowanceHeadplc').text(objectHead);
                $('#objectHeadModal').modal('show');
            }

            function changeObjectHead() {
                adname = $('#allowanceHeadplc').text();
                tbillNo = $('#billNoplc').text();
                tMonth = $('#billMonthplc').text();
                tYear = $('#billYearplc').text();
                tobjectheads = $("#objectheads").val();

                var url = 'changeObjectBtHeadOfArrearBill.htm?billNo=' + tbillNo + '&sltMonth=' + tMonth + '&sltYear=' + tYear + '&adcodename=' + adname + '&objectbthead=' + tobjectheads;
                $.getJSON(url, function(data) {
                    if (data.status == "S") {
                        $('#objectHeadModal').modal('hide');
                        $('#' + adname).text($("#objectheads").val());
                    }
                });
            }

            function showChangeBTHeadWindow(objectHeadNm, objHead) {
                //$('#billNoplc').text($('#billNo').val());
                // $('#billMonthplc').text($('#sltMonth').val());
                //$('#billYearplc').text($('#sltYear').val());
                var btHead = '';
                //$("#newBThead").empty();
                if (objectHeadNm == "OR") {
                    btHead = "OTHER RECOVERY";
                }
                if (objectHeadNm == "DR") {
                    btHead = "DDO RECOVERY";
                }
                //$('#dedHeadname').text(btHead);
                $('#dedHeadname').text(objectHeadNm);
                $('#newBThead').val(objHead);
                $('#btHeadModal').modal('show');
                //alert(objectHead);
            }

            function changeBTHead() {
                var tobthead = $('#newBThead').val();
                var dedHeadnm = $('#dedHeadname').text();
                //var dedHead = $().val();
                var tbillNo = ${command.billNo};

                //alert(tobthead);
                if (tobthead != '' && tobthead.length >= 3) {
                    var url = 'changeBtHeadOfArrearBill.htm?billNo=' + tbillNo + '&adcodename=' + dedHeadnm + '&objectbthead=' + tobthead;
                    $.getJSON(url, function(data) {
                        if (data.status == "S") {
                            $('#btHeadModal').modal('hide');
                            //$('#newBThead').empty();
                            $('#' + dedHeadnm).text($("#newBThead").val());
                            $('#dedHd').text($("#newBThead").val());
                        }
                    });

                    return true;
                } else {
                    alert("Enter By Transfer Head");
                    return false;
                }



            }


            var finyear1;
            var finyear2;
            function validateBillDate() {
                var curyear = new Date().getFullYear();
                var curmonth = new Date().getMonth();

                if (curmonth < 3) {
                    finyear1 = curyear - 1;
                    finyear2 = curyear;
                } else if (curmonth >= 3) {
                    finyear1 = curyear;
                    finyear2 = curyear + 1;
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-inline" action="saveBillArrear.htm" method="POST" commandName="command">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Chart of Account: ${command.chartofAcct}           
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered">
                                    <tr>
                                        <td width="15%" align="left">&nbsp;&nbsp;Bill ID
                                            <form:hidden path="billNo"/>
                                            <form:hidden path="txtbilltype"/>
                                            <form:hidden path="sltYear"/>
                                            <form:hidden path="sltMonth"/>
                                        </td>
                                        <td width="15%" >
                                            ${command.billNo}                      
                                        </td>

                                        <td width="15%" align="left">Treasury</td>
                                        <td width="45%" align="left">
                                            <form:select path="treasury" id="treasury" class="form-control">
                                        <option value=""> Select Treasury </option>
                                        <form:options items="${treasuryList}" itemValue="treasuryCode" itemLabel="treasuryName"/>
                                    </form:select>
                                    </td>                                
                                    </tr>
                                    <tr>
                                        <td  align="left">&nbsp;&nbsp;Bill No</td>
                                        <td>
                                            <form:input path="billdesc" id="billdesc" class="form-control"  maxlength="20"/>
                                        </td>
                                        <td align="left">DDO Name and Designation</td>
                                        <td align="left"></td>
                                    </tr>
                                    <tr>
                                        <td align="left">Bill Date (dd-MMM-yyyy) ex. 01-JAN-2018</td>
                                        <td>
                                            <c:if test="${command.status ne 0 && command.status ne 1}">
                                                <c:if test="${usrtname ne 'A'}">
                                                    <c:out value="${command.billDate}"/>
                                                    <form:hidden path="billDate"/>
                                                </c:if>
                                                <c:if test="${usrtname eq 'A'}">
                                                    <div class='input-group date' id='processDate'>
                                                        <form:input class="form-control" id="billDate" path="billDate" />
                                                        <span class="input-group-addon">
                                                            <span class="glyphicon glyphicon-time"></span>
                                                        </span>
                                                    </div>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${command.status eq 0 || command.status eq 1}">
                                                <div class='input-group date' id='processDate'>
                                                    <form:input class="form-control" id="billDate" path="billDate" />
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span>
                                                </div>
                                            </c:if>
                                        </td>
                                        <td align="left">TV No & Date</td>
                                        <td align="left">

                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left">&nbsp;&nbsp;Bill Type</td>
                                        <td>${billDtls.billType}</td>                       
                                        <td  align="left">Received Rs.</td>
                                        <td  align="left">
                                            &nbsp;
                                        </td>
                                    </tr>
                                    <tr>
                                        <td  align="left">&nbsp;&nbsp;Description</td>
                                        <td>${command.billdesc}</td>
                                        <td align="left">Instrument No</td>
                                        <td align="left">&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td align="left">&nbsp;&nbsp;AG Bill Type</td>
                                        <td>${billDtls.agbillTypeId}</td>
                                        <td align="left">Bill Type</td>
                                        <td align="left">${billDtls.typeofBillString}</td>
                                    </tr>
                                    <tr>
                                        <td align="left">&nbsp;&nbsp;From Date</td>
                                        <td>${billDtls.salFromdate}</td>
                                        <td align="left">To Date</td>
                                        <td align="left">${billDtls.salTodate}</td>
                                    </tr>
                                    <c:if test="${not empty billDtls.ddoccode}">
                                        <tr>
                                            <td align="left">&nbsp;&nbsp;Ddo Code</td>
                                            <td colspan="3">
                                                <form:hidden path="sltddoCode"/> 
                                                ${billDtls.ddoccode}
                                            </td>

                                        </tr>
                                    </c:if>
                                    <c:if test="${empty billDtls.ddoccode && billDtls.billStatusId lt 2}">
                                        <tr>
                                            <td align="left">Select Ddo Code</td>
                                            <td colspan="3">
                                                <form:select path="sltddoCode" class="form-control" id="sltddoCode">
                                            <option value="">--Select--</option>
                                            <form:options items="${ddoCodeList}" itemValue="value" itemLabel="label"/>
                                        </form:select>
                                        </td>

                                        </tr>
                                    </c:if>
                                    <c:if test="${not empty billDtls.coCode}">
                                        <tr>
                                            <td align="left">&nbsp;&nbsp;Co Code</td>
                                            <td>
                                                ${billDtls.coCode}
                                                <form:hidden path="sltCOList" /> 
                                            </td>
                                            <td align="left">Co Name</td>
                                            <td align="left">${billDtls.coName}</td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${empty billDtls.coCode}">
                                        <tr>
                                            <td align="left">&nbsp;&nbsp;Select Co Name</td>
                                            <td colspan="3">
                                                <select name="sltCOList" class="form-control">
                                                    <option value="">--Select--</option>
                                                    <c:if test="${not empty colist}">
                                                        <c:forEach items="${colist}" var="list" >
                                                            <option value="${list.value}">${list.label}</option>
                                                        </c:forEach>
                                                    </c:if>
                                                </select>
                                            </td>

                                        </tr>
                                    </c:if>
                                </table>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-4">
                                <div align="center"><b>** Bill Gross:  ${billDtls.grossAmount}</b></div>

                            </div>
                            <div class="col-lg-4">                                
                                <div align="center"><b>** Net Amount:  ${billDtls.netAmount}</b></div>
                            </div>
                            <div class="col-lg-4">
                                <div align="center" > </div>

                            </div>
                        </div>    

                        <div class="row">
                            <div class="col-lg-12 text-danger"> ** Note: Bill Gross and Net will be found correct when you press <b>Save Button</b>. (After correction of any amount you have to click <b>Save Button</b> again) </div>  
                        </div>  
                        <div class="row">
                            <div class="col-lg-4">
                                <div align="center"><b>ALLOWANCE</b></div>
                                <table class="table table-bordered">
                                    <thead>
                                        <tr height="30px">
                                            <th width="10%" align="center" >SL NO.</th>
                                            <th width="45%" align="center">ALLOWANCES</th>
                                            <th width="20%" align="center">OBJECT HEAD</th>
                                            <th width="25%" align="right" >AMOUNT</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${allowanceList}" var="allowance" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index+1}</td>
                                                <td>${allowance.adname}</td>
                                                <td>
                                                    <span id="${allowance.adname}">${allowance.objecthead}</span> 
                                                    <c:if test="${command.status == 2 || command.status != 5 && command.status != 7}">
                                                        <a href="javascript:showChangeObjectHeadWindow('${allowance.adname}')"><span class="glyphicon glyphicon-pencil"></span></a>
                                                        </c:if>
                                                </td>
                                                <td>${allowance.adamount}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="col-lg-4">
                                <div align="center"><b>DEDUCTION</b></div>
                                <table class="table table-bordered">
                                    <tr height="30px">
                                        <th width="10%" align="center">SL No.</th>
                                        <th width="40%" align="center" >DEDUCTIONS</th>
                                        <th width="20%" align="center">BY TRANSFER</th>
                                        <th width="30%" align="right">AMOUNT</th>
                                    </tr>
                                    <tbody>
                                        <c:forEach items="${deductionList}" var="deduction" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index+1}</td>
                                                <td>${deduction.adname}</td>
                                                <td>                                                   
                                                    <span id="${deduction.adname}">${deduction.objecthead}</span>
                                                    <c:if test="${deduction.adname eq 'OR' || deduction.adname eq 'DR'}">
                                                        <a href="javascript:showChangeBTHeadWindow('${deduction.adname}','${deduction.objecthead}')">
                                                            <c:if test="${command.status == null || command.status == 0 || command.status == 1 || command.status == 4 || command.status == 8}">
                                                                <span class="glyphicon glyphicon-pencil"></span></a>
                                                            </c:if>
                                                        </c:if>                                                                                                               
                                                </td>
                                                <td>${deduction.adamount}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="col-lg-4">
                                <div align="center"><b>PRIVATE LOAN</b></div>
                                <table class="table table-bordered">
                                    <thead>
                                        <tr height="30px">
                                            <th width="10%" align="center">SL No.</th>
                                            <th width="60%" align="center" >LOAN</th>
                                            <th width="30%" align="right">AMOUNT</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%-- <c:forEach items="${pvtloanList}" var="pvtloan" varStatus="cnt">
                                             <tr>
                                                 <td>${cnt.index+1}</td>
                                                 <td>${pvtloan.adname}</td>
                                                <td>${pvtloan.objecthead}</td>
                                                 <td>${pvtloan.adamount}</td>
                                             </tr>
                                         </c:forEach>--%>
                                        <c:if test="${pvtDeductionAmt ne ''}">
                                            <tr>
                                                <td>${cnt.index+1}</td>
                                                <td>DDO RECOVERY</td>
                                                <%--<td>${pvtloan.objecthead}</td>--%>
                                                <td>${pvtDeductionAmt}</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered">
                                    <tr style="background-color:#E9E9EA;">
                                        <th width="87" scope="col"><div align="center" class="style9">HEAD</div></th>
                                    <th width="102" scope="col"><div align="center" class="style9">GPF/GPF ADV.(P) </div></th>
                                    <th width="56" scope="col"><div align="center" class="style9">AISGPF</div></th>
                                    <th width="67" scope="col"><div align="center" class="style9">CPF/NPS</div></th>
                                    <th width="52" scope="col"><div align="center" class="style9">AISGIS</div></th>

                                    <th width="94" scope="col"><div align="center" class="style9">TPF/TPF ADV(P) </div></th>
                                    <th width="32" scope="col"><div align="center" class="style9">LIC</div></th>
                                    <th width="53" scope="col"><div align="center" class="style9">SHBA(P)</div></th>
                                    <th width="53" scope="col"><div align="center" class="style9">HBA(P)</div></th>
                                    <th width="112" scope="col"><div align="center" class="style9">MC/MOPED/CAR(P)</div></th>
                                    <th width="77" scope="col"><div align="center" class="style9">BICY ADV(P) </div></th>
                                    <th width="77" scope="col"><div align="center" class="style9">GIS ADV(P) </div></th>

                                    <th width="77" scope="col"><div align="center" class="style9">CMPTR ADV(P) </div></th>
                                    <th width="77" scope="col"><div align="center" class="style9">PLI </div></th>
                                    <th width="77" scope="col"><div align="center" class="style9">IT </div></th>
                                    <th width="77" scope="col"><div align="center" class="style9">DAO/ALLIED GPF </div></th>
                                    </tr>
                                    <tr style="background-color:#E2F4FA;">
                                        <th scope="row"><span class="style7">Previous BT ID </span></th>
                                        <td><div align="center"><span class="style3">8690</span></div></td>
                                        <td><div align="center"><span class="style3">8692</span></div></td>
                                        <td><div align="center"><span class="style3">30594</span></div></td>
                                        <td><div align="center"><span class="style3">8693</span></div></td>

                                        <td><div align="center"><span class="style3">7058</span></div></td>
                                        <td><div align="center"><span class="style3">7100</span></div></td>
                                        <td><div align="center"><span class="style3">7049</span></div></td>
                                        <td><div align="center"><span class="style3">8678</span></div></td>
                                        <td><div align="center"><span class="style3">8679</span></div></td>
                                        <td><div align="center"><span class="style3">30013</span></div></td>
                                        <td><div align="center"><span class="style3">8680</span></div></td>

                                        <td><div align="center"><span class="style3">30015</span></div></td>
                                        <td><div align="center"><span class="style3">7108</span></div></td>
                                        <td><div align="center"><span class="style3">7112</span></div></td>
                                        <td><div align="center"><span class="style3">30020</span></div></td>
                                    </tr>
                                    <tr>
                                        <th scope="row"><span class="style7">New BT ID </span></th>
                                        <td><div align="center"><span class="style3">55545</span></div></td>
                                        <td><div align="center"><span class="style3">57649</span></div></td>
                                        <td><div align="center"><span class="style3">57740</span></div></td>
                                        <td><div align="center"><span class="style3">58829</span></div></td>

                                        <td><div align="center"><span class="style3">55550</span></div></td>
                                        <td><div align="center"><span class="style3">55832</span></div></td>
                                        <td><div align="center"><span class="style3">55522</span></div></td>
                                        <td><div align="center"><span class="style3">55521</span></div></td>
                                        <td><div align="center"><span class="style3">55525</span></div></td>
                                        <td><div align="center"><span class="style3">57633</span></div></td>
                                        <td><div align="center"><span class="style3">57639</span></div></td>

                                        <td><div align="center"><span class="style3">57635</span></div></td>
                                        <td><div align="center"><span class="style3">58600</span></div></td>
                                        <td><div align="center"><span class="style3">58816</span></div></td>
                                        <td><div align="center"><span class="style3">58239</span></div></td>

                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>                
                    <div class="panel-footer">
                        <c:if test="${command.status lt 2}">
                            <input type="submit" name="action" value="Reprocess" id="btn-reprocess" class="btn btn-default"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validateForm()"/>
                            <input type="submit" name="action" value="ChangeChartofAccount" class="btn btn-default"/>
                        </c:if>
                        <c:if test="${command.status == 4}">
                            <input type="submit" name="action" value="Reprocess" id="btn-reprocess" class="btn btn-default"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validateForm()"/>
                            <input type="submit" name="action" value="ChangeChartofAccount" class="btn btn-default"/>
                        </c:if>
                        <c:if test="${command.status == 5}">                            
                            &nbsp;
                        </c:if>
                        <c:if test="${command.status == 8}">
                            <input type="submit" name="action" value="Reprocess" id="btn-reprocess" class="btn btn-default"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validateForm()"/>
                            <input type="submit" name="action" value="ChangeChartofAccount" class="btn btn-default"/>
                        </c:if>
                        <input type="submit" name="action" value="Cancel" class="btn btn-default"/>

                    </div>
                </div>
            </form:form>
        </div>
        <div id="objectHeadModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Change Object Head</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" action="/action_page.php">
                            <div class="form-group">
                                <label class="control-label col-sm-4">Bill No:</label>
                                <div class="col-sm-8" id="billNoplc"></div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-4">Month:</label>
                                <div class="col-sm-8" id="billMonthplc"></div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-4">Year:</label>
                                <div class="col-sm-8" id="billYearplc"></div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Allowance Head:</label>
                                <div class="col-sm-8" id="allowanceHeadplc"></div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Object Heads:</label>
                                <div class="col-sm-8">
                                    <select id="objectheads" class="form-control">
                                        <option value="136">136</option>
                                        <option value="156">156</option>
                                        <option value="403">403</option>
                                        <option value="523">523</option>
                                        <option value="921">921</option>
                                        <option value="000">000</option>
                                        <option value="401">401</option>
                                        <option value="855">855</option>
                                        <option value="506">506</option>
                                    </select>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="changeObjectHead()">Change</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="btHeadModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Change By Transfer Head</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" action="/action_page.php">                            
                            <div class="form-group">
                                <div class="col-sm-6"><label class="control-label">Deduction Head:</label></div>
                                <div class="col-sm-4" id="dedHeadname"></div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-6"><label class="control-label">By Transfer Heads:</label></div>
                                <div class="col-sm-4"><input name="newBThead" id="newBThead"  class="form-control"  maxlength="5"></div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="changeBTHead()">Change</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript">
            $(function() {
                $('#processDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    minDate: new Date(finyear1, '3', '1'),
                    maxDate: new Date(finyear2, '2', '31')
                });
            });
        </script>
    </body>
</html>
