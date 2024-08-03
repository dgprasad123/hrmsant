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
            $(document).ready(function () {
                $('#bytransferhead').on("paste", function (e) {
                    e.preventDefault();
                });
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
            function showChangePayHeadWindow(objectheadspay) {
                $('#billNoplcpay').text($('#billNo').val());
                $('#billMonthplcpay').text($('#sltMonth').val());
                $('#billYearplcpay').text($('#sltYear').val());
                $('#payhead').text(objectheadspay);
                $('#payHeadModal').modal('show');

            }
            function changePayHead() {
                adname = $('#payhead').text();
                tbillNo = $('#billNoplcpay').text();
                objectheadspay = $('#objectheadspay').val();
                var url = 'changePayHeadOfBill.htm?billNo=' + tbillNo + '&objectbthead=' + objectheadspay;
                $.getJSON(url, function (data) {
                    if (data.status == "S") {
                        $('#' + adname).text($("#objectheadspay").val());
                    }
                });
            }
            function showChangeObjectHeadWindow(objectHead) {
                $('#billNoplc').text($('#billNo').val());
                $('#billMonthplc').text($('#sltMonth').val());
                $('#billYearplc').text($('#sltYear').val());
                $('#allowanceHeadplc').text(objectHead);
                $('#objectHeadModal').modal('show');
            }
            function showChangeByTransferWindow(bytransferhead, btvalue) {
                $('#billNoplcb').text($('#billNo').val());
                $('#billMonthplcb').text($('#sltMonth').val());
                $('#billYearplcb').text($('#sltYear').val());
                $('#bytransferplcb').text(bytransferhead);
                $('#bytransferhead').val($('#' + bytransferhead).text());
                $('#btHeadModal').modal('show');

            }
            function changeByTransferHead() {
                adname = $('#bytransferplcb').text();
                tbillNo = $('#billNoplcb').text();
                tMonth = $('#billMonthplcb').text();
                tYear = $('#billYearplcb').text();
                tbytransferhead = $("#bytransferhead").val();

                var url = 'changeObjectBtHeadOfBill.htm?billNo=' + tbillNo + '&sltMonth=' + tMonth + '&sltYear=' + tYear + '&adcodename=' + adname + '&objectbthead=' + tbytransferhead;
                $.getJSON(url, function (data) {
                    if (data.status == "S") {
                        $('#' + adname).text($("#bytransferhead").val());
                    }
                });
            }
            function changeObjectHead() {
                adname = $('#allowanceHeadplc').text();
                tbillNo = $('#billNoplc').text();
                tMonth = $('#billMonthplc').text();
                tYear = $('#billYearplc').text();
                tobjectheads = $("#objectheads").val();

                var url = 'changeObjectBtHeadOfBill.htm?billNo=' + tbillNo + '&sltMonth=' + tMonth + '&sltYear=' + tYear + '&adcodename=' + adname + '&objectbthead=' + tobjectheads;
                $.getJSON(url, function (data) {
                    if (data.status == "S") {
                        $('#' + adname).text($("#objectheads").val());
                    }
                });
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
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-inline" action="unlockbill.htm" method="POST" commandName="command">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Chart of Account: ${command.chartofAcct} <span class="glyphicon glyphicon-pencil"></span>

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
                                            <form:select path="treasury" id="treasury" class="form-control" disabled="true">
                                                <form:option value=""> Select Treasury </form:option>
                                                <form:options items="${treasuryList}" itemValue="treasuryCode" itemLabel="treasuryName"/>
                                            </form:select>
                                        </td>                                
                                    </tr>
                                    <tr>
                                        <td  align="left">&nbsp;&nbsp;Bill No</td>
                                        <td>
                                            <form:input path="billdesc" id="billdesc" class="form-control"/>
                                        </td>
                                        <td align="left">DDO Name and Designation</td>
                                        <td align="left"></td>
                                    </tr>
                                    <tr>
                                        <td align="left">&nbsp;&nbsp;Bill Date (dd-MMM-yyyy) ex. 01-JAN-2018</td>
                                        <td>

                                            <form:input path="billDate" id="billDate" class="form-control"/> </td>

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

                                </table>
                            </div>
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
                                        <tr>
                                            <td>1</td>
                                            <td>${paydata.adname}</td>
                                            <td><span id="${paydata.adname}">${paydata.objecthead}</span>
                                                <c:if test="${command.status == 2 || command.status != 5 && command.status != 7}">
                                                    <a href="javascript:showChangePayHeadWindow('${paydata.adname}')"><span class="glyphicon glyphicon-pencil"></span></a></td>
                                                    </c:if>
                                            <td>${paydata.adamount}</td>
                                        </tr>
                                        <c:forEach items="${allowanceList}" var="allowance" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index+2}</td>
                                                <td>${allowance.adname}</td>
                                                <td><span id="${allowance.adname}">${allowance.objecthead}</span> 
                                                    <c:if test="${command.status == 2 || command.status != 5 && command.status != 7}">
                                                        <a href="javascript:showChangeObjectHeadWindow('${allowance.adname}')"><span class="glyphicon glyphicon-pencil"></span></a></td>
                                                        </c:if>
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
                                                <td><span id="${deduction.adname}">${deduction.objecthead}</span> 
                                                    <c:if test="${command.status == 2 || command.status != 5 && command.status != 7}">
                                                        <a href="javascript:showChangeByTransferWindow('${deduction.adname}','${deduction.objecthead}')">
                                                            <span class="glyphicon glyphicon-pencil"></span>
                                                        </a> 
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
                                        <c:forEach items="${pvtloanList}" var="pvtloan" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index+1}</td>
                                                <td>${pvtloan.adname}</td>
                                                <td>${pvtloan.objecthead}</td>
                                                <td>${pvtloan.adamount}</td>
                                            </tr>
                                        </c:forEach>
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
                        <c:if test="${command.status lt 2}"> <!-- if  Bill Not submitted and prepared  -->
                            <input type="submit" name="action" value="Reprocess" class="btn btn-default"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validateForm()"/>
                            <input type="submit" name="action" value="ChangeChartofAccount" class="btn btn-default"/>
                        </c:if>
                        <c:if test="${command.status == 4}"><!-- Bill Error Case  -->
                            <input type="submit" name="action" value="Reprocess" class="btn btn-default"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validateForm()"/>
                            <input type="submit" name="action" value="ChangeChartofAccount" class="btn btn-default"/>

                        </c:if>
                        <c:if test="${command.status == 8}"> <!-- Objection Error Case  -->
                            <input type="submit" name="action" value="Reprocess" class="btn btn-default"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validateForm()"/>
                            <input type="submit" name="action" value="ChangeChartofAccount" class="btn btn-default"/>

                        </c:if>
                        <input type="button" name="action" value="Close" class="btn btn-default" onclick="javascript:window.location = 'unlockbill.htm'"/>

                    </div>
                </div>
            </form:form>
        </div>

        <div id="btHeadModal" class="modal fade" role="dialog">
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
                                <div class="col-sm-8" id="billNoplcb"></div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-4">Month:</label>
                                <div class="col-sm-8" id="billMonthplcb"></div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-4">Year:</label>
                                <div class="col-sm-8" id="billYearplcb"></div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">By Transfer Id:</label>
                                <div class="col-sm-8" id="bytransferplcb"></div>
                            </div>
                            <%--<div class="form-group">
                                <label class="control-label col-sm-4">By Transfer Head:</label>
                                <div class="col-sm-8">
                                    <select id="bytransferhead" class="form-control">
                                        <option value="55026">55026</option>
                                        <option value="55545">55545</option>
                                        <option value="58816">58816</option>
                                        <option value="55832">55832</option>
                                        <option value="59546">59546</option>
                                        <option value="59469">59469</option>
                                        <option value="3043">3043</option>
                                        <option value="3320">3320</option>
                                        <option value="3321">3321</option>
                                        <option value="57649">57649 (AIS GPF)</option>
                                        <option value="58239">58239 (DAO GPF)</option>
                                        <option value="59361">59361</option>
                                        <option value="59361">59361</option>
                                        <option value="60141">60141 (excess pay)</option>

                                        <!--START FESTIVAL BTID NEW  -->
                                        <option value="60146">60146</option>
                                        <option value="60147">60147</option>
                                        <option value="60148">60148</option>
                                        <option value="60149">60149</option>
                                        <option value="60151">60151</option>
                                        <option value="60152">60152</option>
                                        <option value="60153">60153</option>
                                        <option value="60154">60154</option>
                                        <option value="60155">60155</option>
                                        <option value="60156">60156</option>
                                        <option value="60157">60157</option>
                                        <option value="60158">60158</option>
                                        <option value="60159">60159</option>
                                        <option value="60160">60160</option>
                                        <option value="60162">60162</option>
                                        <option value="60163">60163</option>
                                        <option value="60164">60164</option>
                                        <option value="60165">60165</option>
                                        <option value="60166">60166</option>
                                        <option value="60167">60167</option>
                                        <option value="60168">60168</option>
                                        <option value="60169">60169</option>
                                        <option value="60170">60170</option>
                                        <option value="60171">60171</option> 
                                        <option value="60172">60172</option>
                                        <option value="60173">60173</option>
                                        <option value="60174">60174</option>
                                        <option value="60175">60175</option>
                                        <option value="60176">60176</option>
                                        <option value="60177">60177</option>
                                        <option value="60178">60178</option>
                                        <option value="60179">60179</option>
                                        <option value="60180">60180</option>
                                        <option value="60181">60181</option>
                                        <option value="60182">60182</option>
                                        <option value="60183">60183</option>
                                        <option value="60184">60184</option>
                                        <option value="60185">60185</option>
                                        <option value="60186">60186</option>
                                        <option value="60187">60187</option>
                                        <option value="60188">60188</option>
                                        <option value="60192">60188</option>
                                       
                                        

                                        <!--END FESTIVAL BTID NEW  -->
                                    </select>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="control-label col-sm-4">By Transfer Head:</label>
                                <div class="col-sm-8">
                                    <input type="text" id="bytransferhead" maxlength="6" onkeypress="return onlyIntegerRange(event)"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="changeByTransferHead()">Change</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>
        <div id="payHeadModal" class="modal fade" role="dialog">
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
                                <div class="col-sm-8" id="billNoplcpay"></div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-4">Month:</label>
                                <div class="col-sm-8" id="billMonthplcpay"></div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-4">Year:</label>
                                <div class="col-sm-8" id="billYearplcpay"></div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Head:</label>
                                <div class="col-sm-8" id="payhead"></div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Object Heads:</label>
                                <div class="col-sm-8">
                                    <select id="objectheadspay" class="form-control">
                                        <option value="136">136</option>
                                        <option value="156">156</option>
                                        <option value="403">403</option>
                                        <option value="523">523</option>
                                        <option value="921">921</option>
                                        <option value="000">000</option>
                                        <option value="855">855</option>
                                        <option value="506">506</option>
                                    </select>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="changePayHead()">Change</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
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
    </body>
</html>
