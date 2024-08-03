<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#txtChallanDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function getSubTreasuryFromParentTreasury() {
                var parentTrCode = $('#sltParentTreasury').val();

                var url = "getSubTreasuryListJSON.htm?parentTrCode=" + parentTrCode;
                $('#sltSubTreasury').empty();
                $('#sltSubTreasury').append('<option value="">--Select--</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#sltSubTreasury').append('<option value="' + obj.treasuryCode + '">' + obj.treasuryName + '</option>');
                    });
                });
            }

            function submitValidate() {

                if ($('#sltTreasuryCode').val() == '') {
                    alert("Select Treasury");
                    return false;
                }
                if ($('#txtChallanNo').val() == '') {
                    alert("Enter Challan No");
                    return false;
                }
                if ($('#txtChallanDate').val() == '') {
                    alert("Enter Challan Date");
                    return false;
                }
                return true;
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
        <div id="wrapper">

            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="TPFReportByChallanNoList.htm" commandName="command">
                    <div class="container-fluid">
                        <table class="table" border="0" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
                            <tr style="height:40px;">
                                <td width="10%" align="center">
                                    PARENT TREASURY
                                </td>
                                <td width="25%">
                                    <form:select path="sltParentTreasury" id="sltParentTreasury" class="form-control" onchange="getSubTreasuryFromParentTreasury();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${parentTreasuryList}" itemLabel="treasuryName" itemValue="treasuryCode"/>
                                    </form:select>
                                </td>
                                <td width="10%" align="center">
                                    SUB-TREASURY
                                </td>
                                <td width="25%">
                                    <form:select path="sltSubTreasury" id="sltSubTreasury" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${subTreasuryList}" itemLabel="treasuryName" itemValue="treasuryCode"/>
                                    </form:select>
                                </td>
                                <td width="10%">&nbsp;</td>
                            </tr>
                            <tr style="height:40px;">
                                <td align="center">
                                    CHALLAN NO
                                </td>
                                <td width="10%">
                                    <form:input path="txtChallanNo" id="txtChallanNo" class="form-control" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                                <td align="center">
                                    CHALLAN DATE
                                </td>
                                <td>
                                    <div style="position:relative;"><form:input path="txtChallanDate" id="txtChallanDate" class="form-control" readonly="true"/></div>
                                </td>
                                <td>
                                    <input type="submit" name="submit" value="Get List" class="btn btn-danger" onclick="return submitValidate();"/>
                                </td>
                            </tr>
                        </table>
                        <div align="center">
                            <h2>
                                <c:out value="${officeName}"/>
                            </h2>
                        </div>           
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>SL NO</th>
                                        <th>ACCOUNT NO</th>
                                        <th>NAME OF THE SUBSCRIBER</th>
                                        <th>DESIGNATION</th>
                                        <th>MONTHLY SUBSCRIPTION</th>
                                        <th id="refundAmt">REFUND OF WITHDRAWALS AMT</br>
                                            (NO OF INST.)</th>
                                        <th>TOTAL RELEASED</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:if test="${not empty tpfList}">
                                        <c:forEach items="${tpfList}" var="tpflists" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>                                                    
                                                <td>${tpflists.gpfNo}</td>
                                                <td>${tpflists.empname}</td>
                                                <td>${tpflists.curDesg}</td>
                                                <td>${tpflists.monthlySubAmt}</td>
                                                <td>${tpflists.towardsLoan}   (${tpflists.instCnt})</td>
                                                <td>${tpflists.totalReleased}</td>
                                            </tr>
                                        </c:forEach>
                                    <hr />
                                    <tr>
                                        <td colspan="5">&nbsp;</td>
                                        <td>Challan Amount</td>
                                        <td>
                                            <c:out value="${totalAmount}"/>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${empty tpfList}">
                                    <tr>
                                        <td colspan="7">
                                            <span style="display:block;text-align: center;font-weight: bold; font-size:14px; color: red;">
                                                NO RECORDS
                                            </span>
                                        </td>
                                    </tr>
                                </c:if>
                                </tbody>
                            </table>  
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
