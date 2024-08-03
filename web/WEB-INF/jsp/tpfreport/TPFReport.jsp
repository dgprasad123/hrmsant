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
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            
            function getSubTreasuryAndDDOListFromParentTreasury() {
                var parentTrCode = $('#sltParentTreasury').val();

                var url = "getSubTreasuryListJSON.htm?parentTrCode=" + parentTrCode;
                $('#sltSubTreasury').empty();
                $('#sltSubTreasury').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltSubTreasury').append('<option value="' + obj.treasuryCode + '">' + obj.treasuryName + '</option>');
                    });
                });

                var subTrCode = $('#sltSubTreasury').val();

                url = "treasuryDDOListJSON.htm?parentTrCode=" + parentTrCode + "&subTrCode=" + subTrCode;
                $('#ddocode').empty();
                $('#ddocode').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#ddocode').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getDDOListFromSubTreasury() {
                var parentTrCode = $('#sltParentTreasury').val();

                var subTrCode = $('#sltSubTreasury').val();

                url = "treasuryDDOListJSON.htm?parentTrCode=" + parentTrCode + "&subTrCode=" + subTrCode;
                $('#ddocode').empty();
                $('#ddocode').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#ddocode').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getBillAmount() {
                var treasury = $('#sltParentTreasury').val();
                var ddocode = $('#ddocode').val();
                var year = $('#sltYear').val();
                var month = $('#sltMonth').val();

                if (treasury == '') {
                    alert("Please select Treasury");
                } else if (ddocode == '') {
                    alert("Please select DDO");
                } else if (year == '') {
                    alert("Please enter Year");
                } else if (month == '') {
                    alert("Please select Month");
                } else {
                    $('#loaderImg').show();
                    var url = 'treasuryGetBillAmtJSON.htm?ddocode=' + ddocode + '&year=' + year + '&month=' + month;
                    //$('#billAmt').combobox('reload', url);
                    $('#billAmt').empty();
                    $('#billAmt').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#billAmt').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    }).done(function(result) {
                        $('#loaderImg').hide();
                    });
                }
            }

            function openTPFReport() {
                var treasury = $('#sltParentTreasury').val();
                var ddocode = $('#ddocode').val();
                var year = $('#sltYear').val();
                var month = $('#sltMonth').val();
                var billid = $('#billAmt').val();

                if (treasury == '') {
                    alert("Please select Treasury");
                } else if (ddocode == '') {
                    alert("Please select DDO");
                } else if (year == '') {
                    alert("Please enter Year");
                } else if (month == '') {
                    alert("Please select Month");
                } else if (billid == '') {
                    alert("Please select Bill Amount");
                } else {
                    var url = "http://apps.hrmsodisha.gov.in/TPFScheduleHTML.htm?billNo=" + billid;
                    window.open(url, "_blank");
                }
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

                <div class="container-fluid">
                    <div class="row" align="center" style="margin-top:5px;margin-bottom:7px;">
                        <table class="table" border="0" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
                            <tr style="height:40px;">
                                <td width="15%" align="center">
                                    TRANSACTION MONTH
                                </td>
                                <td width="35%">
                                    <select id="sltMonth" name="sltMonth" style="width:200px;" class="form-control">
                                        <option value="">--Select--</option>
                                        <option value="0">JANUARY</option>
                                        <option value="1">FEBUARY</option>
                                        <option value="2">MARCH</option>
                                        <option value="3">APRIL</option>
                                        <option value="4">MAY</option>
                                        <option value="5">JUNE</option>
                                        <option value="6">JULY</option>
                                        <option value="7">AUGUST</option>
                                        <option value="8">SEPTEMBER</option>
                                        <option value="9">OCTOBER</option>
                                        <option value="10">NOVEMBER</option>
                                        <option value="11">DECEMBER</option>
                                    </select>
                                </td>
                                <td width="15%" align="center">
                                    TRANSACTION YEAR
                                </td>
                                <td width="30%">
                                    <input type="text" name="sltYear" id="sltYear" class="form-control" style="width:200px;" maxlength="4" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr style="height:40px;">
                                <td align="center">
                                    PARENT TREASURY
                                </td>
                                <td>
                                    <%--<input class="easyui-combobox" id="treasury" name="treasury" style="width:300px;" data-options="valueField:'treasuryCode',textField:'treasuryName',url:'getTreasuryListJSON.htm'">--%>

                                    <select name="sltParentTreasury" id="sltParentTreasury" class="form-control" onchange="getSubTreasuryAndDDOListFromParentTreasury();">
                                        <option value="">--Select--</option>
                                        <c:if test="${not empty parentTreasuryList}">
                                            <c:forEach items="${parentTreasuryList}" var="ptlist">
                                                <option value="${ptlist.treasuryCode}">${ptlist.treasuryName}</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </td>
                                <td align="center">
                                    SUB TREASURY
                                </td>
                                <td>
                                    <select name="sltSubTreasury" id="sltSubTreasury" class="form-control" onchange="getDDOListFromSubTreasury();">
                                        <option value="">--Select--</option>
                                    </select>
                                </td>
                            </tr>

                            <tr style="height:40px;">
                                <td align="center">
                                    DDO NAME
                                </td>
                                <td colspan="2">
                                    <%--<input type="text" name="ddocode" id="ddocode" class="form-control" data-options="valueField:'value',textField:'label'">--%>
                                    <select name="ddocode" id="ddocode" class="form-control">
                                        <option value="">--Select--</option>
                                    </select>
                                </td>
                                <td>
                                    <a href="javascript:getBillAmount()" class="btn btn-danger" id="btnGetBill"/>GET BILL</a>
                                    <span id="loaderImg" style="display:none;">
                                        <img src="images/ajax-loader.gif"/>
                                    </span>
                                </td>
                            </tr>
                            <tr style="height:40px;">
                                <td align="center">
                                    Bill Amount
                                </td>
                                <td>
                                    <%--<input class="easyui-combobox" id="billAmt" name="billAmt" style="width:400px;" data-options="valueField:'value',textField:'label'">--%>
                                    <select name="billAmt" id="billAmt" class="form-control">
                                        <option value="">--Select--</option>
                                    </select>
                                </td>
                                <td colspan="2">
                                    <a href="javascript:openTPFReport()" class="btn btn-danger" id="btnGetReport">VIEW REPORT</a>&nbsp;
                                    <%--<a href="javascript:openTPFPaymentReport()" class="easyui-linkbutton" iconCls="icon-add">VIEW PAYMENT REPORT</a>--%>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
