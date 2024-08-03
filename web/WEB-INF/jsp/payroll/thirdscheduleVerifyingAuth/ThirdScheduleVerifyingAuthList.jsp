<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function() {

                $('#emplistthirdscheduledg').datagrid({
                    url: "getVerifyingAuthThirdScheduleEmpListJSON.htm?offcode=",
                    rowStyler: function(index, row) {
                        if (row.isApproved == "Y") {
                            return 'color: #33a5ff;font-weight:bold;';
                        }
                    }
                });

                $('body').on('click', ".txtDate", function() {
                    $(this).datetimepicker({
                        timepicker: false,
                        format: 'd-M-Y',
                        closeOnDateSelect: true,
                        validateOnBlur: false
                    });
                });
                $('body').on("cut copy paste", "input", function(e) {
                    e.preventDefault();
                });
                
                $('body').on('click', '#delete', function(e) {
                    if (confirm("Want to delete?")) {
                        var cur_tr = $(this).parents('tr');
                        cur_tr.remove();
                    }
                });
            });
            
            function doSearch() {
                var offcode = $('#sltOffCode').combobox('getValue');
                //alert("Off Code is: "+offcode);
                $('#emplistthirdscheduledg').datagrid('load', {
                    offcode: offcode
                });
            }
            
            function viewThirdSchedule() {
                var row = $('#emplistthirdscheduledg').datagrid('getSelected');
                if (row) {
                    $('#winThirdScheduleAuth').window('refresh', 'viewVerifyingAuthThirdSchedule.htm?empid=' + row.empId);
                    $('#winThirdScheduleAuth').window("open");
                } else {
                    alert("Select a row");
                }
            }

            function viewThirdSchedulePDF() {
                var row = $('#emplistthirdscheduledg').datagrid('getSelected');
                if (row) {
                    window.open("ThirdscheduleVerifyingAuthPDF.htm?empid=" + row.empId, "_blank");
                } else {
                    alert("Select a row");
                }
            }

            function addRow() {
                var markup = "<tr><td><input type='text' name='incrDt' class='txtDate' readonly='true'/></td><td><input type='text' name='incrCell' id='incrCell' size='5' maxlength='2'/>&nbsp;<input type='text' name='revisedbasic' maxlength='6' onkeypress='return onlyIntegerRange(event)'/>&nbsp;<input type='text' name='pp' maxlength='6' size='7' onkeypress='return onlyIntegerRange(event)'/></td><td><input type='text' name='incrLevel' id='incrLevel' size='5' maxlength='2'/><button type=\"button\" id=\"delete\">Delete</button></td></tr>";
                $("table#incrTable tbody").append(markup);
            }
            
            function addRowIAS() {
                var markup = "<tr><td><input type='text' name='incrDt' class='txtDate' readonly='true'/></td><td><input type='text' name='revisedbasic' maxlength='6' size='7' onkeypress='return onlyIntegerRange(event)'/>&nbsp;&nbsp;<input type='text' name='incrCell' id='incrCell' size='5' maxlength='2' onkeypress='return onlyIntegerRange(event)'/>&nbsp;&nbsp;<input type='text' name='incrLevel' id='incrLevel' size='5' maxlength='3'/><button type=\"button\" id=\"delete\">Delete</button></td></tr>";
                $("table#incrIASTable tbody").append(markup);
            }
            
            function saveThirdSchedule(isApproved) {
                //alert("isApproved is: "+isApproved)
                var status = "";
                if (isApproved == 'N') {
                    status = confirm("Are you sure to Save?");
                } else if (isApproved == 'Y') {
                    status = confirm("Are you sure to Approve?");
                }
                if (status) {
                    $('#thirdScheduleDataForm').form('submit', {
                        url: 'saveThirdVerifyingAuthScheduleData.htm?approve=' + isApproved,
                        success: function(result) {
                            var result = eval('(' + result + ')');
                            if (result.message) {
                                $.messager.show({
                                    title: 'Error',
                                    msg: result.message
                                });
                            } else {
                                $('#winThirdScheduleAuth').window('close');
                                $('#emplistthirdscheduledg').datagrid('reload');
                            }
                        }
                    });
                }
            }
            function onlyIntegerRange(e)
            {
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
            function showStatus(val, row) {
                if (val == "Y") {
                    return "Approved";
                }
            }
            function getDeptWisePostList() {
                $('#entryGpc').empty();
                var url = 'getDeptWisePostListJSON.htm?deptCode=' + $('#entryDept').val();
                $('#entryGpc').append('<option value="">Select Post</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#entryGpc').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                    });
                });
            }
            function getDeptWisePostListIAS() {
                $('#hooSpc').empty();
                var url = 'getDeptWisePostListJSON.htm?deptCode=' + $('#deptCodeIAS').val();
                $('#hooSpc').append('<option value="">Select Post</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hooSpc').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                    });
                });
            }
            
            function openRevert() {
                var row = $('#emplistthirdscheduledg').datagrid('getSelected');
                if (row) {
                    var ids = [];
                    var rows = $('#emplistthirdscheduledg').datagrid('getSelections');
                    for (var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].empId);
                    }
                    $('#chkEmp').val(ids);
                    if (confirm("Are you sure to Revert the selected Employees?")) {
                        $('#authSubmitForm').form('submit', {
                            url: 'revertVerifyingAuthEmp.htm',
                            success: function(result) {
                                var result = eval('(' + result + ')');
                                if (result.message) {
                                    $.messager.show({
                                        title: 'Error',
                                        msg: result.message
                                    });
                                } else {
                                    $('#emplistthirdscheduledg').datagrid('reload');
                                }
                            }
                        });
                    }
                } else {
                    alert("Select a row");
                }
            }
        </script>
        <style>
            body {
                font-family: Verdana,sans-serif;
            }

            table {               
                border-collapse: collapse;
            }
            table tr td {
                border: 1px solid black;
                padding: 5px;
            }
        </style>
    </head>
    <body>
        <div align="center" style="margin-top:20px;">
            <table border="0" cellpadding="0" cellspacing="0" width="80%">
                <tr style="height:40px;">
                    <td width="30%" align="center">
                        Select Office
                    </td>
                    <td width="70%">
                        <input name="sltOffCode" id="sltOffCode" class="easyui-combobox" style="width:500px;" data-options="url:'getVerifyingEmployeesOfficeListJSON.htm',valueField:'value',textField:'label',editable:false" />&emsp;
                        <button type="button" id="btnOk" class="easyui-linkbutton" onclick="doSearch();">Ok</button>
                    </td>
                </tr>
            </table>
        </div>
        
        <div align="center">
            <table id="emplistthirdscheduledg" class="easyui-datagrid" style="width:100%;height:400px;overflow:auto;" title="Employye List"
                   rownumbers="true" nowrap="false" singleSelect="true" pagination="true" collapsible="false"
                   data-options="nowrap:false" toolbar="#emplistToolbar">
                <thead>
                    <tr>
                        <th data-options="field:'empId',width:80">HRMS ID</th>
                        <th data-options="field:'gpfno',width:100">GPF No/PRAN</th>
                        <th data-options="field:'empname',width:250">Employee Name</th>
                        <th data-options="field:'designation',width:250">Post as on Option Exercised Date</th>
                        <th data-options="field:'payscale',width:120">Pay Scale</th>
                        <th data-options="field:'gp',width:80">Grade Pay</th>
                        <th data-options="field:'isApproved',width:80,formatter:showStatus">Status</th>
                    </tr> 
                </thead>
            </table>
        </div>
        <div align="center">
            <p>
                <span  style="font-family:Arial;font-size:16px;font-weight:bold">BLUE COLOURED EMPLOYEES ARE APPROVED.</span>
            </p>
        </div>
        <div id="emplistToolbar" style="padding:3px">                    
            <a href="javascript:viewThirdSchedule();" class="easyui-linkbutton" iconCls="icon-edit" plain="true">Edit/View</a>
            <a href="javascript:viewThirdSchedulePDF();" class="easyui-linkbutton" iconCls="icon-pdf" plain="true">Download</a>
            <%--<a href="javascript:openRevert();" class="easyui-linkbutton" iconCls="icon-remove" plain="true">Revert</a>--%>
        </div>

        <div id="winThirdScheduleAuth" class="easyui-window" title="Third Schedule Authority" style="width:1200px;height:500px;padding:5px;" closed="true"
             data-options="iconCls:'icon-search',fit:true">
        </div>

        <form id="authSubmitForm" method="POST">
            <input type="hidden" name="chkEmp" id="chkEmp"/>
        </form>
    </body>
</html>
