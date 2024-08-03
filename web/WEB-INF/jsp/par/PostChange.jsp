<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
                $('#officeDialog').dialog('close');
                $('#gpcDialog').dialog('close');
                $('#postDialog').dialog('close');


                $('#deptname').textbox({
                    onClickButton: function() {                        
                        $('#deptDialog').panel('open');
                        $('#officeDialog').dialog('close');
                        $('#gpcDialog').dialog('close');
                        $('#postDialog').dialog('close');
                    }
                });

                $('#officename').textbox({
                    onClickButton: function() {
                        deptcode = $('#hidDeptCode').val();
                        if (deptcode == '') {
                            alert('Please select Department');
                        } else {                            
                            $('#officeDialog').panel('open');
                            $('#deptDialog').dialog('close');
                            $('#gpcDialog').dialog('close');
                            $('#postDialog').dialog('close');
                            $('#officedg').datagrid({
                                url: "GetOfficeListJSON.htm?deptcode=" + deptcode
                            });
                        }
                    }
                });




                $('#gpcname').textbox({
                    onClickButton: function() {
                        deptcode = $('#hidDeptCode').val();
                        if (deptcode == '') {
                            alert('Please select Office');
                        } else {                            
                            $('#gpcDialog').panel('open');
                            $('#deptDialog').dialog('close');
                            $('#officeDialog').dialog('close');
                            $('#postDialog').dialog('close');
                            $('#gpcdg').datagrid({
                                url: "getGPCListOfficeWiseJSON.htm?deptcode=" + deptcode
                            });
                        }
                    }
                });



                $('#postname').textbox({
                    onClickButton: function() {
                        gpc = $('#hidgpc').val();
                        offcode = $('#hidOffCode').val();
                        if (gpc == '') {
                            alert('Please select Generic Post');
                        } else {                            
                            $('#postDialog').panel('open');
                            $('#deptDialog').dialog('close');
                            $('#officeDialog').dialog('close');
                            $('#gpcDialog').dialog('close');
                            $('#postdg').datagrid({
                                url: "getPostListGPCWiseAuthorityJSON.htm?gpc=" + gpc + "&offcode=" + offcode
                            });
                        }
                    }
                });
            });





            function getPost() {
                var deptCode = $('#hidDeptCode').val();
                var offCode = $('#hidOffCode').val();
                var offName = $('#officename').textbox('getValue');
                var spc = $('#hidPost').val();

                var spn = $('#postname').textbox('getValue');
                if (spc == '') {
                    alert('please select Post.');
                } else {
                    window.opener.document.getElementById('office').innerHTML = offName;
                    window.opener.document.getElementById('hidOffice').value = offCode;
                    window.opener.document.getElementById('post').innerHTML = spn;
                    window.opener.document.getElementById('hidspc').value = spc;
                    window.close();
                }
            }


            function radioFuncDept(val, row) {
                var data = row.deptCode + ":" + row.deptName;
                return "<input type='radio' name='rddeptid' id='rddeptid' value='" + data + "' onclick='SelectDepartment(\"" + data + "\")'/>";
            }

            function radioFuncOffice(val, row) {
                var data = row.offCode + ":" + row.offName;
                data = data.replace("'", "&rsquo;");
                return "<input type='radio' name='rdoffcode' id='rdoffcode' value='" + data + "' onclick='SelectOffice(\"" + data + "\")'/>";
            }

            function radioFuncGenericPost(val, row) {
                var data = row.postcode + ":" + row.post;
                data = data.replace("'", "&rsquo;");
                return "<input type='radio' name='rdogpc' id='rdgpc' value='" + data + "' onclick='SelectGenericPost(\"" + data + "\")'/>";
            }

            function radioFuncPost(val, row) {
                var data = row.spc + ":" + row.spn;
                data = data.replace("'", "&rsquo;");
                return "<input type='radio' name='rdopostcode' id='rdopostcode' value='" + data + "' onclick='SelectPost(\"" + data + "\")'/>";
            }


            function SelectDepartment(data) {

                var radspl = data.split(':');
                var deptcode = radspl[0];
                var deptname = radspl[1];
                $('#deptDialog').panel('close');
                $('#deptname').textbox('setValue', deptname);
                $('#hidDeptCode').val(deptcode);
            }

            function SelectOffice(data) {

                var radspl = data.split(':');
                var offcode = radspl[0];
                var offname = radspl[1];
                $('#officeDialog').dialog('close');
                $('#officename').textbox('setValue', offname);
                $('#hidOffCode').val(offcode);
            }

            function SelectGenericPost(data) {

                var radspl = data.split(':');
                var gpcode = radspl[0];
                var post = radspl[1];
                $('#gpcDialog').dialog('close');
                $('#gpcname').textbox('setValue', post);
                $('#hidgpc').val(gpcode);
            }


            function SelectPost(data) {

                var radspl = data.split(':');
                var spccode = radspl[0];
                var spn = radspl[1];
                $('#postDialog').dialog('close');
                $('#postname').textbox('setValue', spn);
                $('#hidPost').val(spccode);
            }



        </script>
    </head>
    <body>
        

        
        <form action="addAuthoritySPC.htm" method="POST">
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
                                Office
                            </td>
                            <td align="left">
                                <input name="hidOffCode" id="hidOffCode" type="hidden"/>
                                <input id="officename" class="easyui-textbox" data-options="editable:false,buttonText:'Search',buttonAlign:'right',buttonIcon:'icon-search',required:true,prompt:'Click on search icon to select Office...'" style="width:100%;height:32px;">
                            </td>
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                        <tr style="height:40px" align="center">
                            <td>
                                Generic Post
                            </td>
                            <td align="left">
                                <input name="hidgpc" id="hidgpc" type="hidden"/>
                                <input id="gpcname" class="easyui-textbox" data-options="editable:false,buttonText:'Search',buttonAlign:'right',buttonIcon:'icon-search',required:true,prompt:'Click on search icon to select Generic Post...'" style="width:100%;height:32px;">
                            </td>
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                        <tr style="height:40px" align="center">
                            <td>
                                Post
                            </td>
                            <td align="left">
                                <!--<input name="hidPost" id="hidPost" class="easyui-combobox" style="width:500px;" data-options="valueField:'spc',textField:'spn'"/> -->
                                <input id="postname" class="easyui-textbox" data-options="editable:false,buttonText:'Search',buttonAlign:'right',buttonIcon:'icon-search',required:true,prompt:'Click on search icon to select Post...'" style="width:100%;height:32px;">
                                <input type="hidden" name="hidPost" id="hidPost"/> 
                            </td>
                            <td>
                                <button type="button" onclick="getPost()">Ok</button>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </form>
        <div id="deptDialog" class="easyui-panel" title="Select Department" style="width:100%;height:400px;padding:10px;" data-options="iconCls:'icon-save',closed:true">
            <table id="deptdg" class="easyui-datagrid" nowrap="false" title="..." style="width:420px;"
                   data-options="rownumbers:true,singleSelect:true,url:'GetDeptListJSON.htm',method:'post'">
                <thead>
                    <tr>
                        <th data-options="field:'deptCode',formatter:radioFuncDept"></th>
                        <th data-options="field:'deptName',width:300">Department Name</th>

                    </tr>
                </thead>
            </table>
        </div>
        
        <div id="officeDialog" class="easyui-panel" title="Select Office" style="width:100%;height:400px;padding:10px;" data-options="iconCls:'icon-save',closed:true">
            <table id="officedg" class="easyui-datagrid" nowrap="false" title="..." style="width:420px;"
                   data-options="rownumbers:true,singleSelect:true,method:'post'">
                <thead>
                    <tr>
                        <th data-options="field:'offCode',formatter:radioFuncOffice"></th>
                        <th data-options="field:'offName',width:300">Office Name</th>

                    </tr>
                </thead>
            </table>
        </div>

        <div id="gpcDialog" class="easyui-panel" title="Select Generic Post" style="width:100%;height:400px;padding:10px;" data-options="iconCls:'icon-save',closed:true">
            <table id="gpcdg" class="easyui-datagrid" nowrap="false" title="..." style="width:420px;"
                   data-options="rownumbers:true,singleSelect:true,method:'post'">
                <thead>
                    <tr>
                        <th data-options="field:'postcode',formatter:radioFuncGenericPost"></th>
                        <th data-options="field:'post',width:300">Generic Post</th>

                    </tr>
                </thead>
            </table>
        </div>
        <div id="postDialog" class="easyui-panel" title="Select Post" style="width:100%;height:400px;padding:10px;" data-options="iconCls:'icon-save',closed:true">
            <table id="postdg" class="easyui-datagrid" nowrap="false" style="width:420px;"
                   data-options="rownumbers:true,singleSelect:true,method:'post'">
                <thead>
                    <tr>
                        <th data-options="field:'spc',formatter:radioFuncPost"></th>
                        <th data-options="field:'spn',width:300">Post</th>

                    </tr>
                </thead>
            </table>
        </div>

    </body>
</html>
