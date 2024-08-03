<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {

                $('#deptDialog').dialog('close');
                $('#cadreDialog').dialog('close');
                $('#deptname').textbox({
                    onClickButton: function() {
                        $('#deptDialog').dialog('open');
                    }
                });

                $('#cadrename').textbox({
                    onClickButton: function() {
                        deptcode = $('#hidDeptCode').val();
                        if (deptcode == '') {
                            alert('Please select Department');
                        } else {
                            $('#cadreDialog').dialog('open');
                            $('#cadredg').datagrid({
                                url: "getCadreListJSON.htm?deptcode=" + deptcode
                            });
                        }
                    }
                });
            });


            function getCadre() {
                var cadreCode = $('#hidCadreCode').val();
                var cadreName = $('#cadrename').textbox('getValue');

                if (cadreCode == '') {
                    alert('please select Cadre.');
                } else {
                    window.opener.document.getElementById('cadre').innerHTML = cadreName;
                    window.opener.document.getElementById('cadreCode').value = cadreCode;
                    window.close();
                }
            }


            function radioFuncDept(val, row) {
                var data = row.deptCode + ":" + row.deptName;
                return "<input type='radio' name='rddeptid' id='rddeptid' value='" + data + "' onclick='SelectDepartment(\"" + data + "\")'/>";
            }

            function radioFuncCadre(val, row) {
                var data = row.value + ":" + row.label;
                return "<input type='radio' name='rdcdrcode' id='rdcdrcode' value='" + data + "' onclick='SelectCadre(\"" + data + "\")'/>";
            }

            function SelectDepartment(data) {
                var radspl = data.split(':');
                var deptcode = radspl[0];
                var deptname = radspl[1];
                $('#deptDialog').dialog('close');
                $('#deptname').textbox('setValue', deptname);
                $('#hidDeptCode').val(deptcode);
            }

            function SelectCadre(data) {
                var radspl = data.split(':');
                var cadrecode = radspl[0];
                var cadrename = radspl[1];
                $('#cadreDialog').dialog('close');
                $('#cadrename').textbox('setValue', cadrename);
                $('#hidCadreCode').val(cadrecode);
            }
        </script>
    </head>
    <body onblur="javascript:window.close()">
        <div align="center" style="margin-top:5px;margin-bottom:10px;">
            <div align="center">
                <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
                    <tr style="height:40px">
                        <td width="20%" align="center">
                            Department
                        </td>
                        <td width="70%">
                            <input name="hidDeptCode" id="hidDeptCode" type="hidden" /> 
                            <input id="deptname" class="easyui-textbox" data-options="editable:false,buttonText:'Search',buttonAlign:'right',buttonIcon:'icon-search',required:true,prompt:'Click on search icon to select Department...'" style="width:100%;height:32px;">
                        </td>
                        <td width="10%">
                            &nbsp;
                        </td>
                    </tr>
                    <tr style="height:40px" align="center">
                        <td>
                            Cadre
                        </td>
                        <td align="left">
                            <input name="hidCadreCode" id="hidCadreCode" type="hidden"/>
                            <input id="cadrename" class="easyui-textbox" data-options="editable:false,buttonText:'Search',buttonAlign:'right',buttonIcon:'icon-search',required:true,prompt:'Click on search icon to select Cadre...'" style="width:100%;height:32px;">
                        </td>
                        <td>
                            &nbsp;
                        </td>
                    </tr>
                    <tr style="height:40px" align="center">
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>
                            <button type="button" onclick="getCadre()">Ok</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div id="deptDialog" class="easyui-dialog" title="Select Department" style="width:80%;" data-options="iconCls:'icon-save',modal:true">
            <table id="deptdg" class="easyui-datagrid" title="..." style="width:420px;"
                   data-options="rownumbers:true,singleSelect:true,url:'GetDeptListJSON.htm',method:'post'">
                <thead>
                    <tr>
                        <th data-options="field:'deptCode',formatter:radioFuncDept"></th>
                        <th data-options="field:'deptName',width:300">Department Name</th>
                    </tr>
                </thead>
            </table>
        </div>

        <div id="cadreDialog" class="easyui-dialog" title="Select Cadre" style="width:80%;" data-options="iconCls:'icon-save',modal:true">
            <table id="cadredg" class="easyui-datagrid" nowrap="false" style="width:420px;"
                   data-options="rownumbers:true,singleSelect:true,method:'post'">
                <thead>
                    <tr>
                        <th data-options="field:'value',formatter:radioFuncCadre"></th>
                        <th data-options="field:'label',width:300">Cadre Name</th>
                    </tr>
                </thead>
            </table>
        </div>
    </body>
</html>
