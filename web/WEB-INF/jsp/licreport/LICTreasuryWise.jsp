<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script language="javascript" type="text/javascript">
            function saveCheck() {
                var treasury = $('#sltTreasury').combobox('getValue');
                var year = $('#sltYear').combobox('getValue');
                var month = $('#sltMonth').combobox('getValue');
                if (treasury == "") {
                    alert("Please select Treasury");
                    return false;
                }
                if (year == "") {
                    alert("Please select Year");
                    return false;
                }
                if (month == "") {
                    alert("Please select Month");
                    return false;
                }
                return true;
            }
            function downloadExcel() {
                if (saveCheck()) {
                    var treasury = $('#sltTreasury').combobox('getValue');
                    var year = $('#sltYear').combobox('getValue');
                    var month = $('#sltMonth').combobox('getValue');
                    window.open("DownloadExcelLICTreasuryWise.htm?treasury="+treasury+"&year="+year+"&month="+month,"_blank");
                }
            }
        </script>
    </head>
    <body>
        <div style="padding:10px;">
            <div id="tablist" class="easyui-tabs" fit="true" plain="true" border="false" style="height:650px;">
                <div title="LIC" style="overflow:auto;">
                    <div class="easyui-panel" style="padding: 5px;width:100%;">
                        <div id="licpanel" class="easyui-panel" title="TREASURY WISE LIC" style="width:100%;height:450px;padding:5px;">
                            <form action="LicTreasuryWise.htm">
                                <div align="center">
                                    <div class="empInfoDiv" align="center">
                                        <table border="0" width="80%" cellpadding="0" cellspacing="0" class="tableview">
                                            <tr style="height:40px;">
                                                <td>
                                                    Select Treasury
                                                </td>
                                                <td>
                                                    <input class="easyui-combobox" id="sltTreasury" name="sltTreasury" style="width:600px;" data-options="url:'getTreasuryListJSON.htm',valueField:'treasuryCode',textField:'treasuryName'">
                                                </td>
                                            </tr>
                                        </table>
                                        <table border="0" width="80%" cellpadding="0" cellspacing="0" class="tableview">
                                            <tr style="height:40px;">
                                                <td width="20%">
                                                    Select Year
                                                </td>
                                                <td width="20%">
                                                    <select name="sltYear" id="sltYear" class="easyui-combobox" style="width:100px;">
                                                        <option value="">--Select--</option>
                                                        <option value="2010">2010</option>
                                                        <option value="2011">2011</option>
                                                        <option value="2012">2012</option>
                                                        <option value="2013">2013</option>
                                                        <option value="2014">2014</option>
                                                        <option value="2015">2015</option>
                                                        <option value="2016">2016</option>
                                                        <option value="2017">2017</option>
                                                        <option value="2018">2018</option>
                                                        <option value="2019">2019</option>
                                                        <option value="2020">2020</option>
                                                        <option value="2021">2021</option>
                                                        <option value="2022">2022</option>
                                                        <option value="2023">2023</option>
                                                        <option value="2024">2024</option>
                                                    </select>
                                                </td>
                                                <td width="20%">
                                                    Select Month
                                                </td>
                                                <td width="30%">
                                                    <select name="sltMonth" id="sltMonth" class="easyui-combobox" style="width:100px;">
                                                        <option value="">--Select--</option>
                                                        <option value="0">JAN</option>
                                                        <option value="1">FEB</option>
                                                        <option value="2">MAR</option>
                                                        <option value="3">APR</option>
                                                        <option value="4">MAY</option>
                                                        <option value="5">JUN</option>
                                                        <option value="6">JUL</option>
                                                        <option value="7">AUG</option>
                                                        <option value="8">SEP</option>
                                                        <option value="9">OCT</option>
                                                        <option value="10">NOV</option>
                                                        <option value="11">DEC</option>
                                                    </select>
                                                </td>
                                                <td width="10%">
                                                    <a href="javascript:void(0)" class="easyui-linkbutton c6" onclick="downloadExcel()" style="width:90px">Download Excel</a>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>