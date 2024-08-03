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

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#contractualheldupdatagrid').datagrid({
                   rowStyler: function(index,row){
                       if(row.isheldup == 'Y'){
                           return 'background-color:#FF0000;color:#FFFFFF;font-weight:bold;';
                       }
                   }
                });
            });
            function heldUp() {
                var row = $('#contractualheldupdatagrid').datagrid('getSelected');
                if (row) {
                    var url = "EmpHeldUpForm.htm?empid=" + row.empid;
                    $('#helup-dlg').dialog("open");
                    $('#heldupform').form('load', url);
                    //alert("Value of Emp Id is: " + $('#hidempid').val());
                } else {
                    alert("Select a Row");
                }
            }
            function saveHeldUp() {
                $('#heldupform').form('submit', {
                    url: 'saveheldupdata.htm',
                    success: function(result) {
                        var result = eval('(' + result + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
                            $('#helup-dlg').dialog('close'); // close the dialog
                            $('#contractualheldupdatagrid').datagrid('reload');
                        }
                    }
                });
            }
        </script>
    </head>
    <body>
        <table id="contractualheldupdatagrid" class="easyui-datagrid" style="width:100%;height:700px;overflow:auto;" title="Employee List"
               rownumbers="true" singleSelect="true" url="ContractualEmpListJSON.htm" singleSelect="true" pagination="true" collapsible="false"
               data-options="nowrap:false,pageSize:50,pageList:[100,200]" toolbar="#helduptool">
            <thead>
                <tr>
                    <th data-options="field:'isheldup',hidden:true"></th>
                    <th data-options="field:'empid',width:100">HRMS ID</th>
                    <th data-options="field:'empname',width:250">Employee Name</th>
                    <th data-options="field:'empdesg',width:300">Designation</th>
                </tr> 
            </thead>
        </table>
        <div id="helup-dlg" class="easyui-dialog" title="HeldUp" 
             data-options="iconCls:'icon-add',closed: true,modal: true" 
             style="width:600px;height:400px;padding:10px" buttons="#dlg-buttons">
            <form method="POST" id="heldupform">
                <input type="hidden" name="hidempid" id="hidempid"/>
                <div align="center">
                    <table border="0" cellspacing="0" cellpadding="0" style="margin-top:100px;">
                        <tr style="height:40px;">
                            <td>
                                <input class="easyui-textbox" name="heldupdt" data-options="label:'Held Up Date',labelWidth:'100',width:'250'"/>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td align="center">
                                <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-save" onclick="saveHeldUp()" style="width:90px">Save</a>
                            </td>
                        </tr>
                    </table>
                </div>
            </form>
        </div>
        <div id="helduptool">
            <a href="javascript:void(0)" id="heldupBtn" class="easyui-linkbutton c6" iconCls="icon-save" onclick="heldUp()" style="width:90px">Held Up</a>
        </div>
    </body>
</html>
