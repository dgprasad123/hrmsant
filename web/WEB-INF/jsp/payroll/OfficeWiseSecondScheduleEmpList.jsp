<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" href="resources/css/colorbox.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function() {

                $('#emplistdg').datagrid({
                    onClickRow: function(index, row) {
                        if (row.submittedAuth == "Y") {
                            alert("Already Submitted.");
                            if (index != 1) {
                                //$(this).datagrid('unselectRow',index);
                            }
                        }
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

                $('#empDepartment').combobox('clear');
                $('#empOffice').combobox('clear');
                $('#empPost').combobox('clear');

                $('#empDepartment').combobox({
                    url: 'getDeptListJSON.htm',
                    onSelect: function(record) {
                        $('#empOffice').combobox('clear');
                        $('#empPost').combobox('clear');
                        var url = 'getOfficeListJSON.htm?deptcode=' + record.deptCode;
                        $('#empOffice').combobox('reload', url);
                    }
                });
                $('#empOffice').combobox({
                    onSelect: function(record) {
                        $('#empPost').combobox('clear');
                        var url = 'getPostCodeListJSON.htm?offcode=' + record.offCode;
                        $('#empPost').combobox('reload', url);
                    }
                });

                $('#btnOk').click(function() {
                    var billGrpId = $('#sltBillGroup').combobox('getValue');
                    if (billGrpId != '') {
                        $('#emplistdg').datagrid({
                            url: "GetOfficeWiseSecondScheduleEmployeeListJSON.htm?biliGrpId=" + billGrpId,
                            rowStyler: function(index, row) {
                                //alert(row.has_seen);
                                if (row.optionChosen != "") {
                                    return 'color: #33a5ff;font-weight:bold;';
                                }
                            }
                        });
                    } else {
                        alert("Please Select Bill Group");
                    }
                });

                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });




                $('#authSubmitBtn').click(function() {

                    var ids = [];
                    var rows = $('#emplistdg').datagrid('getSelections');
                    for (var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].empid);
                    }
                    $('#chkEmp').val(ids);

                    $('#authSubmitForm').form('submit', {
                        url: 'saveAuthData.htm',
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
                                $('#winRevisioningAuth').window('close');
                                $('#emplistdg').datagrid('reload');
                            }
                        }
                    });
                });
            });

            function changepost() {
                $('#winsubstantivepost').window('open');
            }

            function getPost() {

                $('#designation').textbox('setValue', $('#empPost').combobox('getText'));
                $('#hidPostCode').val($('#empPost').combobox('getValue'));

                $('#empDepartment').combobox('clear');
                $('#empOffice').combobox('clear');
                $('#empPost').combobox('clear');

                $('#winsubstantivepost').window('close');

            }

            /*function editSecondSchedule() {
             var row = $('#emplistdg').datagrid('getSelected');
             if (row) {
             var empid = row.empid;
             $('#secondscheduleEditform').form('clear');
             $('#winSecondScheduleForm').window('open');
             var url = "editSecondSchedule.htm?empid=" + empid;
             $('#secondscheduleEditform').form('load', url);
             } else {
             alert("Select a Row");
             }
             }*/
            function editSecondSchedule() {
                var row = $('#emplistdg').datagrid('getSelected');
                if (row) {
                    var rows = $('#emplistdg').datagrid('getSelections');
                    if (rows.length > 1) {
                        alert("Please select Single Row.");
                    } else if (row.submittedAuth == "Y") {
                        alert("Selected Employee is submitted.");
                    } else {
                        var empid = row.empid;
                        $('#secondscheduleEditform').form('clear');
                        $('#winSecondScheduleForm').window('open');
                        var url = "editSecondSchedule.htm?empid=" + empid;
                        $('#secondscheduleEditform').form('load', url);
                        $('#secondscheduleEditform').form({
                            onLoadSuccess: function(data) {
                                //$('#payscale').combobox('setValue',$('#hidPayScale').val());
                                //$('#gp').combobox('setValue',$('#hidGP').val());
                                $('#payscale').val($('#hidPayScale').val());
                                $('#gp').val($('#hidGP').val());
                            }
                        });
                    }
                } else {
                    alert("Select a Row");
                }
            }

            function printSecondSchedule() {
                var row = $('#emplistdg').datagrid('getSelected');
                if (row) {
                    if (row.optionChosen != "") {
                        var empid = row.empid;
                        window.open("officeWiseSecondSchedulePDF.htm?empid=" + empid, "_blank");
                    } else {
                        alert("Not Submitted");
                    }
                } else {
                    alert("Select a Row");
                }
            }

            function submitSecondScheduleForm() {
                $('#secondscheduleEditform').form('submit', {
                    url: 'saveSecondScheduleData.htm',
                    onSubmit: function() {
                        return saveCheck();
                    },
                    success: function(result) {
                        var result = eval('(' + result + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
                            $('#winSecondScheduleForm').window('close');
                            $('#emplistdg').datagrid('reload');
                        }
                    }
                });
            }

            function saveCheck() {
                if ($('input[name=rdOptionChosen]:checked').length <= 0) {
                    alert("Please Select one option!");
                    return false;
                }

                if ($('#designation').textbox('getValue') == '') {
                    alert("Please Enter Post");
                    return false;
                }
                if ($('#payscale').val() == '') {
                    alert("Please Enter Pay Scale");
                    return false;
                }
                if ($('#gp').val() == '') {
                    alert("Please Enter Grade Pay");
                    return false;
                }

                if ($('input[name=rdOptionChosen]:checked').val() == 2) {
                    if ($('.txtDate').val() == '') {
                        alert("Please Enter Date");
                        return false;
                    }
                }

                if (confirm('Are you sure to submit?')) {
                    return true;
                } else {
                    return false;
                }
            }

            function openRevisioningAuth() {
                var row = $('#emplistdg').datagrid('getSelected');
                var isPayRevisionSubmmited = "Y";
                var isAuthSubmmited = "N";
                if (row) {
                    var rows = $('#emplistdg').datagrid('getSelections');
                    for (var i = 0; i < rows.length; i++) {
                        if (rows[i].optionChosen == '') {
                            isPayRevisionSubmmited = "N";
                        }
                        if (rows[i].submittedAuth == 'Y') {
                            isAuthSubmmited = "Y";
                        }
                    }

                    if (isAuthSubmmited == "Y") {
                        alert("One of the Selected Employees are already submitted to Pay Fixation Officer.");
                    } else if (isPayRevisionSubmmited == "N") {
                        alert("One of the Selected Employees has not submitted.");
                    } else if (isAuthSubmmited == "N") {
                        $('#winRevisioningAuth').window("open");
                    }
                } else {
                    alert("Select a Row");
                }
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
            
            function showPendingAuth(val,row){
                return val;
            }
            function showPendingAuthName(val,row){
                return val;
            }
        </script>
        <style>
            body {
                font-family: Verdana,sans-serif;
            }
            .dg-nocheck .datagrid-cell-check{
                display: none;
            }
        </style>
    </head>
    <body>
        <div align="center">

            <table id="emplistdg" class="easyui-datagrid" style="width:100%;height:570px;overflow:auto;" title="Employye List"
                   rownumbers="true" nowrap="false" singleSelect="false" pagination="true" collapsible="false"
                   data-options="nowrap:false" toolbar="#emplistToolbar">
                <thead>
                    <tr>
                        <th data-options="field:'temp',checkbox:'true',styler:function(index,row){if (row.optionChosen == '' || row.submittedAuth == 'Y'){return {class:'dg-nocheck'};}}"></th>
                        <th data-options="field:'empid',width:80">HRMS ID</th>
                        <th data-options="field:'gpfno',width:120">GPF No/PRAN</th>
                        <th data-options="field:'empname',width:250">Employee Name</th>
                        <th data-options="field:'post',width:250">Present Post</th>
                        <th data-options="field:'payscale',width:120">Pay Scale</th>
                        <th data-options="field:'gp',width:80">Grade Pay</th>
                        <th data-options="field:'enteredDate',width:110">Pay Opted<br />Date</th>
                        <th data-options="field:'submittedAuth',width:80,formatter:showStatus">Status</th>
                        <th data-options="field:'pendingAt',width:80,formatter:showPendingAuth">Pending At</th>
                        <th data-options="field:'pendingAuthName',width:100,formatter:showPendingAuthName">Pending<br />Authority<br />Name</th>
                    </tr> 
                </thead>
            </table>
        </div>
        <div id="emplistToolbar" style="padding:3px">                    
            <a href="javascript:editSecondSchedule();" class="easyui-linkbutton" iconCls="icon-edit" plain="true">Edit</a>
            <a href="javascript:printSecondSchedule();" class="easyui-linkbutton" id="printBtn" iconCls="icon-ok" plain="true">Print</a>
            <a href="javascript:openRevisioningAuth();" class="easyui-linkbutton" id="printBtn" iconCls="icon-add" plain="true">Submit to Pay Fixation Officer</a>
            Bill Group:
            <input class="easyui-combobox" name="sltBillGroup" id="sltBillGroup" data-options="valueField:'billgroupid',textField:'billgroupdesc',url:'getBillGrpNameJSON.htm'" style="width:30%"/>&emsp;
            <button type="button" id="btnOk" class="easyui-linkbutton">Search</button>
        </div>
        <div align="center">
            <p>
                <span  style="font-family:Arial;font-size:16px;font-weight:bold">BLUE COLOURED EMPLOYEES HAVE SUBMITTED.</span>
            </p>
        </div>
        <div id="winSecondScheduleForm" class="easyui-window" title="Edit Second Schedule" style="width:700px;height:350px;padding:10px 20px;top:100px" closed="true" buttons="#searchdlg-buttons"
             data-options="iconCls:'icon-search',modal:true">
            <form id="secondscheduleEditform" method="POST" commandName="officeWiseSecondScheduleForm">

                <input type="hidden" name="empid"/>
                <input type="hidden" name="offcode"/>

                <input type="hidden" name="hidPayScale" id="hidPayScale"/>
                <input type="hidden" name="hidGP" id="hidGP"/>

                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr style="height:30px;">
                        <td width="20%">Name</td>
                        <td width="80%">
                            <input class="easyui-textbox" name="empname" id="empname" style="width:400px;" readonly="true"/>
                        </td>
                    </tr>
                    <tr style="height:30px;">
                        <td>Designation:</td>
                        <td>
                            <input type="hidden" name="hidPostCode" id="hidPostCode"/>
                            <input class="easyui-textbox" name="designation" id="designation" style="width:400px;" readonly="true"/>
                            <a href="javascript:void(0)" id="change" onclick="changepost()">
                                <button type="button">Change</button>
                            </a>
                        </td>
                    </tr>
                    <tr style="height:30px;">
                        <td>Pay Scale:</td>
                        <td>
                            <%--<input class="easyui-textbox" name="payscale" id="payscale" style="width:200px;"/>--%>
                            <select name="payscale" id="payscale" style="width:200px;">
                                <option value="">--Select--</option>
                                <option value="4750-14680">4750-14680</option>
                                <option value="4930-14680">4930-14680</option>
                                <option value="5200-20200">5200-20200</option>
                                <option value="9300-34800">9300-34800</option>
                                <option value="15600-39100">15600-39100</option>
                                <option value="37400-67000">37400-67000</option>
                                <option value="67000-79000">67000-79000</option>
                                <option value="75500-80000">75500-80000</option>
                                <option value="80000">80000</option>
                                <option value="90000">90000</option>
                            </select>
                        </td>
                    </tr>
                    <tr style="height:30px;">
                        <td>Grade Pay:</td>
                        <td>
                            <%--<input class="easyui-numberbox" name="gp" id="gp" style="width:100px;">--%>
                            <select name="gp" id="gp">
                                <option value="">--Select--</option>
                                <option value="0">0</option>
                                <option value="1700">1700</option>
                                <option value="1775">1775</option>
                                <option value="1800">1800</option>
                                <option value="1900">1900</option>
                                <option value="2000">2000</option>
                                <option value="2200">2200</option>
                                <option value="2400">2400</option>
                                <option value="2800">2800</option>
                                <option value="4200">4200</option>
                                <option value="4600">4600</option>
                                <option value="4800">4800</option>
                                <option value="5400">5400</option>
                                <option value="6000">6000</option>
                                <option value="6600">6600</option>
                                <option value="7000">7000</option>
                                <option value="7600">7600</option>
                                <option value="8000">8000</option>
                                <option value="8700">8700</option>
                                <option value="8800">8800</option>
                                <option value="8900">8900</option>
                                <option value="9000">9000</option>
                                <option value="10000">10000</option>
                            </select>
                        </td>
                    </tr>
                    <tr style="height:30px;">
                        <td>Option 1</td>
                        <td>
                            <input type="radio" name="rdOptionChosen" value="1"/>Pay Revision from 01-01-2016.
                        </td>
                    </tr>
                    <tr style="height:30px;">
                        <td>Option 2</td>
                        <td>
                            <input type="radio" name="rdOptionChosen" value="2"/>Pay Revision from opted date.
                            <input type="text" name="txtDateEntered" class="txtDate" size="15" readonly="true"/>
                        </td>
                    </tr>
                </table>
            </form>
            <div id="searchdlg-buttons">
                <a href="javascript:submitSecondScheduleForm();" class="easyui-linkbutton" iconCls="icon-ok" style="width:90px">Submit</a>
            </div>
        </div>

        <%-- Start - Content for Child Window --%>
        <div id="winsubstantivepost" class="easyui-window" title="Search" style="width:700px;height:400px;top:50px;padding:10px 20px" closed="true" buttons="#searchdlg-buttons"
             data-options="iconCls:'icon-search',modal:true">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr style="height:40px;">
                    <td width="20%">Department</td>
                    <td width="80%">
                        <input name="empDepartment" id="empDepartment" class="easyui-combobox" style="width:500px;" data-options="valueField:'deptCode',textField:'deptName',editable:false" />
                    </td>
                </tr>
                </tr>
                <tr style="height:40px;">
                    <td>Office Name</td>
                    <td>
                        <input name="empOffice" id="empOffice" class="easyui-combobox" style="width:500px;" data-options="valueField:'offCode',textField:'offName',editable:false"/>
                    </td>
                </tr>
                <tr style="height:40px;">
                    <td>Post Name</td>
                    <td>
                        <input name="empPost" id="empPost" class="easyui-combobox" style="width:400px;" data-options="valueField:'value',textField:'label',editable:false">
                    </td>
                </tr>
                <tr style="height:40px;">
                    <td>&nbsp;</td>
                    <td>
                        <button type="button" onclick="getPost()">Ok</button>
                    </td>
                </tr>
            </table>
        </div>
        <%-- End - Content for Child Window --%>

        <div id="winRevisioningAuth" class="easyui-window" title="Revisioning Authority" style="width:700px;height:400px;top:50px;padding:10px 20px" closed="true" buttons="#searchdlg-buttons"
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
                            <button type="button" id="authSubmitBtn" class="easyui-linkbutton">Submit</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
