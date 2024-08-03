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
            $(document).ready(function() {
            });

            function openMenu(obj) {
                $('#licpanel').panel('open');
            }

            function saveCheck() {
                var division = $('#sltDivision').combobox('getValue');
                var year = $('#sltYear').combobox('getValue');
                var month = $('#sltMonth').combobox('getValue');
                var isForward = true;
                if (division == "") {
                    alert("Please select Division");
                    isForward = false;
                    return false;
                }
                if (year == "") {
                    alert("Please select Year");
                    isForward = false;
                    return false;
                }
                if (month == "") {
                    alert("Please select Month");
                    isForward = false;
                    return false;
                }
                
                if(isForward == true){
                    $('#treasurylist').datagrid('load', {
                        divisionCode: division
                    });
                }
            }
            
            function callDownload(val,row){
                var data = row.treasuryCode;
                var division = $('#sltDivision').combobox('getValue');
                var year = $('#sltYear').combobox('getValue');
                var month = $('#sltMonth').combobox('getValue');
                //return "<a href='DownloadExcelLICDivisionWise.htm?trcode="+data+"&year="+year+"&month="+month+"'><img src='images/excel.jpg' width='20' height='20'></a>";
                return "<a href='DownloadExcelLICDivisionWise.htm?trcode="+data+"&year="+year+"&month="+month+"&division="+division+"'><img src='images/excel.jpg' width='20' height='20'></a>";
            }
            
            function downloadExcel(trcode){
                alert("TR CODE is: "+trcode);
                $('#myform').form('submit',{
                   url:"JSP/LicDivisionWise.do?submit=Ok",
                    onSubmit: function(param){
                        trCode = trcode;
                    }
                });
            }
        </script>
    </head>
    <body>
        <div style="padding:10px;">
            <div id="tablist" class="easyui-tabs" fit="true" plain="true" border="false" style="height:650px;">
                <div title="LIC" style="overflow:auto;">
                    <div class="easyui-panel" style="padding: 5px;width:100%;">
                        <div id="licpanel" class="easyui-panel" title="DIVISION WISE LIC" style="width:100%;height:450px;padding:5px;">
                            <form action="LicDivisionWise.htm" styleId="myform">
                                <div align="center">
                                    <div class="empInfoDiv" align="center">
                                        <table border="0" width="80%" cellpadding="0" cellspacing="0" class="tableview">
                                            <tbody>
                                                <tr style="height:40px;">
                                                    <td>
                                                        Select Division
                                                    </td>
                                                    <td>
                                                        <input class="easyui-combobox" id="sltDivision" name="sltDivision" style="width:600px;" data-options="url:'GetDivisionListJSON.htm',valueField:'value',textField:'label'">
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <table border="0" width="80%" cellpadding="0" cellspacing="0" class="tableview">
                                            <tbody>
                                                <tr style="height:40px;">
                                                    <td width="20%">
                                                        Select Year
                                                    </td>
                                                    <td width="20%">
                                                        <input class="easyui-combobox" id="sltYear" name="sltYear" style="width:200px;" data-options="url:'GetDivisionYearListJSON.htm',valueField:'value',textField:'label'">
                                                    </td>
                                                    <td width="20%" align="center">
                                                        Select Month
                                                    </td>
                                                    <td width="30%">
                                                        <select name="sltMonth" id="sltMonth" class="easyui-combobox" style="width:100px;">
                                                            <option value="">--Select--</option>
                                                            <option value="0">JANUARY</option>
                                                            <option value="1">FEBRUARY</option>
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
                                                    <td width="10%">
                                                        <input type="button" value="Ok" class="easyui-linkbutton c6" onclick="return saveCheck();"/>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <table id="treasurylist" class="easyui-datagrid" style="width:80%;height:100%;" data-options="url:'GetDivisionWiseTreasuryName.htm',fitColumns:true,singleSelect:true">
                                            <thead>
                                                <tr>
                                                    <th data-options="field:'treasuryName',width:500">Treasury Name</th>
                                                    <th data-options="field:'link',width:200,formatter:callDownload">Download</th>
                                                    <th data-options="field:'treasuryCode',hidden:true">Treasury Code</th>
                                                </tr> 
                                            </thead>
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
