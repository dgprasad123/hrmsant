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
                /*$('.txtDate').datetimepicker({
                 timepicker: false,
                 format: 'd-M-Y',
                 closeOnDateSelect: true,
                 validateOnBlur: false
                 });*/
                $('#emplistthirdscheduledg').datagrid({
                    url: "getThirdScheduleEmpListJSON.htm",
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

                $('#authDepartment').combobox('clear');
                $('#authOffice').combobox('clear');
                $('#authPost').combobox('clear');
                $('#authEmp').combobox('clear');

                $('#authDepartment').combobox({url: 'getDeptListJSON.htm',
                    onSelect: function(record) {
                        $('#authOffice').combobox('clear');
                        $('#authPost').combobox('clear');
                        $('#authEmp').combobox('clear');
                        var url = 'getOfficeListJSON.htm?deptcode=' + record.deptCode;
                        $('#authOffice').combobox('reload', url);
                    }
                });
                $('#authOffice').combobox({
                    onSelect: function(record) {
                        $('#authPost').combobox('clear');
                        $('#authEmp').combobox('clear');
                        var url = 'getPostCodeListJSON.htm?offcode=' + record.offCode;
                        $('#authPost').combobox('reload', url);
                    }
                });
                $('#authPost').combobox({
                    onSelect: function(record) {
                        $('#authEmp').combobox('clear');
                        var url = 'getPostCodeWiseEmployeeListJSON.htm?postcode=' + record.value +'&offcode='+$('#authOffice').combobox('getValue');
                        $('#authEmp').combobox('reload', url);
                    }
                });
            });

            function viewThirdSchedule() {
                var row = $('#emplistthirdscheduledg').datagrid('getSelected');
                if (row) {
                    var rows = $('#emplistthirdscheduledg').datagrid('getSelections');
                    if (rows.length > 1) {
                        alert("Select a Single row");
                    } else {
                        if (row.isCheckingAuthSubmitted == "Y") {
                            alert("Already Submitted!");
                        } else {
                            $('#winThirdScheduleAuth').window('refresh', 'viewThirdSchedule.htm?empid=' + row.empId);
                            $('#winThirdScheduleAuth').window("open");
                        }
                    }
                } else {
                    alert("Select a row");
                }
            }

            function viewThirdSchedulePDF() {
                var row = $('#emplistthirdscheduledg').datagrid('getSelected');
                if (row) {
                    var rows = $('#emplistthirdscheduledg').datagrid('getSelections');
                    if (rows.length > 1) {
                        alert("Select a Single row");
                    } else {
                        window.open("ThirdschedulePDF.htm?empid=" + row.empId, "_blank");
                    }
                } else {
                    alert("Select a row");
                }
            }

            function addRow() {
                var markup = "<tr><td><input type='text' name='incrDt' class='txtDate' readonly='true'/></td><td><input type='text' name='incrCell' id='incrCell' size='5' maxlength='2'/>&nbsp;<input type='text' name='revisedbasic' maxlength='6' size='7' onkeypress='return onlyIntegerRange(event)'/>&nbsp;<input type='text' name='pp' maxlength='6' size='7' onkeypress='return onlyIntegerRange(event)'/></td><td><input type='text' name='incrLevel' id='incrLevel' size='5' maxlength='2' onkeypress='return onlyIntegerRange(event)'/><button type=\"button\" id=\"delete\">Delete</button></td></tr>";
                $("table#incrTable tbody").append(markup);
            }

            function addRowIAS() {
                var markup = "<tr><td><input type='text' name='incrDt' class='txtDate' readonly='true'/></td><td><input type='text' name='revisedbasic' maxlength='6' size='7' onkeypress='return onlyIntegerRange(event)'/>&nbsp;&nbsp;<input type='text' name='incrCell' id='incrCell' size='5' maxlength='2' onkeypress='return onlyIntegerRange(event)'/>&nbsp;&nbsp;<input type='text' name='incrLevel' id='incrLevel' size='5' maxlength='3'/><button type=\"button\" id=\"delete\" class=\"easyui-linkbutton\">Delete</button></td></tr>";
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
                        url: 'saveThirdScheduleData.htm?approve=' + isApproved,
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

            function openCheckingAuth() {
                var row = $('#emplistthirdscheduledg').datagrid('getSelected');
                var isApproved = "Y";
                var isCheckingAuthSubmitted = "N";
                if (row) {
                    var rows = $('#emplistthirdscheduledg').datagrid('getSelections');
                    for (var i = 0; i < rows.length; i++) {
                        if (rows[i].isApproved != 'Y') {
                            isApproved = "N";
                        }
                        if (rows[i].isCheckingAuthSubmitted == 'Y') {
                            isCheckingAuthSubmitted = "Y";
                        }
                    }
                    if (isApproved == "N") {
                        alert("One of the Selected Employees are not Approved!");
                    } else if (isCheckingAuthSubmitted == "Y") {
                        alert("One of the Selected Employees are already Submitted to Checking Authority!");
                    } else {
                        $('#winCheckingAuth').window("open");
                    }
                } else {
                    alert("Select a Row");
                }
            }
            function submitAuth() {
                var ids = [];
                var rows = $('#emplistthirdscheduledg').datagrid('getSelections');
                for (var i = 0; i < rows.length; i++) {
                    ids.push(rows[i].empId);
                }
                $('#chkEmp').val(ids);

                $('#authSubmitForm').form('submit', {
                    url: 'saveCheckingAuthData.htm',
                    onSubmit: function() {
                        return submitAuthCheck();
                    },
                    success: function(result) {
                        var result = eval('(' + result + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
                            $('#winCheckingAuth').window('close');
                            $('#emplistthirdscheduledg').datagrid('reload');
                        }
                    }
                });
            }

            function submitAuthCheck() {
                if ($('#authEmp').combobox('getValue') == '') {
                    alert("Please Select Authority Name");
                    return false;
                }
                return true;
            }
            function showStatus(val, row) {
                if (val == "Y") {
                    return "Submitted";
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
                    var isCheckingAuthSubmitted = "N";
                    for (var i = 0; i < rows.length; i++) {
                        if (rows[i].isCheckingAuthSubmitted == 'Y') {
                            isCheckingAuthSubmitted = "Y";
                        } else {
                            ids.push(rows[i].empId);
                        }
                    }
                    $('#chkEmp').val(ids);
                    if (isCheckingAuthSubmitted == "Y") {
                        alert("One of the selected Employees are already submitted to Checking Authority and cannot be reverted!");
                        $('#chkEmp').val('');
                    } else if (confirm("Are you sure to Revert the selected Employees?")) {
                        $('#authSubmitForm').form('submit', {
                            url: 'revertPayFixationEmp.htm',
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

            function showPendingAuth(val, row) {
                return val;
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
            .dg-nocheck .datagrid-cell-check{
                display: none;
            }
        </style>
    </head>
    <body>
        <div align="center">
            <table id="emplistthirdscheduledg" class="easyui-datagrid" style="width:100%;height:400px;overflow:auto;" title="Employye List"
                   rownumbers="true" nowrap="false" url="getThirdScheduleEmpListJSON.htm" singleSelect="false" pagination="true" collapsible="false"
                   data-options="nowrap:false" toolbar="#emplistToolbar">
                <thead>
                    <tr>
                        <th data-options="field:'temp',checkbox:'true',styler:function(index,row){if (row.isApproved != 'Y' || row.isCheckingAuthSubmitted == 'Y'){return {class:'dg-nocheck'};}}"></th>
                        <th data-options="field:'empId',width:80">HRMS ID</th>
                        <th data-options="field:'gpfno',width:100">GPF No/PRAN</th>
                        <th data-options="field:'empname',width:250">Employee Name</th>
                        <th data-options="field:'designation',width:250">Post as on Option Exercised Date</th>
                        <th data-options="field:'payscale',width:120">Pay Scale</th>
                        <th data-options="field:'gp',width:80">Grade Pay</th>
                        <th data-options="field:'isCheckingAuthSubmitted',width:80,formatter:showStatus">Status</th>
                        <th data-options="field:'chkAuthName',width:80,formatter:showPendingAuth">Pending At</th>
                    </tr> 
                </thead>
            </table>
        </div>
        <div align="center">
            <p>
                <span  style="font-family:Arial;font-size:16px;font-weight:bold">BLUE COLOURED EMPLOYEES HAVE SUBMITTED.</span>
            </p>
        </div>
        <div id="emplistToolbar" style="padding:3px">                    
            <a href="javascript:viewThirdSchedule();" class="easyui-linkbutton" iconCls="icon-edit" plain="true">Edit/View</a>
            <a href="javascript:viewThirdSchedulePDF();" class="easyui-linkbutton" iconCls="icon-pdf" plain="true">Download</a>
            <a href="javascript:openCheckingAuth();" class="easyui-linkbutton" iconCls="icon-add" plain="true">Submit to Checking Authority</a>
            <a href="javascript:openRevert();" class="easyui-linkbutton" iconCls="icon-remove" plain="true">Revert</a>
        </div>

        <div id="winThirdScheduleAuth" class="easyui-window" title="Third Schedule Authority" style="width:1200px;height:500px;padding:5px;" closed="true"
             data-options="iconCls:'icon-search',fit:true">
        </div>

        <div id="winCheckingAuth" class="easyui-window" title="Checking Authority" style="width:700px;height:400px;top:50px;padding:10px 20px" closed="true" buttons="#searchdlg-buttons"
             data-options="iconCls:'icon-search',modal:true">

            <form id="authSubmitForm" method="POST">
                <input type="hidden" name="chkEmp" id="chkEmp"/>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr style="height:40px;">
                        <td width="20%">Department</td>
                        <td width="80%">
                            <input name="authDepartment" id="authDepartment" class="easyui-combobox" style="width:500px;" data-options="valueField:'deptCode',textField:'deptName',editable:false" />
                        </td>
                    </tr>
                    </tr>
                    <tr style="height:40px;">
                        <td>Office Name</td>
                        <td>
                            <input name="authOffice" id="authOffice" class="easyui-combobox" style="width:500px;" data-options="valueField:'offCode',textField:'offName',editable:false"/>
                        </td>
                    </tr>
                    <tr style="height:40px;">
                        <td>Post Name</td>
                        <td>
                            <input class="easyui-combobox" id="authPost" name="post" style="width:400px;" data-options="valueField:'value',textField:'label',editable:false">
                        </td>
                    </tr>
                    <tr style="height:40px;">
                        <td>Authority Name</td>
                        <td>
                            <input class="easyui-combobox" name="authEmp" id="authEmp" name="post" style="width:400px;" data-options="valueField:'value',textField:'label',editable:false">
                        </td>
                    </tr>
                    <tr style="height:40px;">
                        <td>&nbsp;</td>
                        <td>
                            <button type="button" id="authSubmitBtn" class="easyui-linkbutton" onclick="submitAuth();">Submit</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
