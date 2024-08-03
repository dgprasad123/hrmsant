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
            $(document).ready(function(){
                $('#deptname').combobox({url:'GetPARDeptListJSON.htm',
                    onSelect: function (record) {
                        $('#officename').combobox('clear');
                        var url = 'GetPAROfficeListJSON.htm?deptcode=' + record.deptCode;
                        $('#officename').combobox('reload', url);
                    }
                });
            });
            function getauthority(){
                var deptCode = $('#deptname').combobox('getValue');
                var offCode = $('#officename').combobox('getValue');
                //alert("deptCode is: "+deptCode);
                //alert("offCode is: "+offCode);
                $('#preferauthoritylist').datagrid({
                    url: "getPARPostListJSON.htm?deptCode="+deptCode+"&offCode="+offCode+"&spc="+$('#hidspc').val()
                });
            }
            function saveData(){
                var ids = [];
                var rows = $('#preferauthoritylist').datagrid('getSelections');
                for(var i=0;i < rows.length;i++){
                    ids.push(rows[i].postcode);
                }
                $('#chkAuth').val(ids);
                //alert($('#chkAuth').val());
            }
        </script>
    </head>
    <body>
        <form action="addAuthoritySPC.htm" method="POST" commandName="WorkflowAuthority">
            <input type="hidden" name="chkAuth" id="chkAuth"/>
            <input type="hidden" name="spc" id="hidspc" value='${spc}'/>
            <input type="hidden" name="hidprocessCode" id="hidprocessCode" value='${processCode}'/>
            <input type="hidden" name="hidfslYear" id="hidfslYear" value='${fiscalYear}'/>
            <input type="hidden" name="hidauthType" id="hidprocessCode" value='${authType}'/>
            <input type="hidden" name="hidrowid" id="hidfslYear" value='${rowid}'/>
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
                        <tr style="height:40px">
                            <td width="15%" align="center">
                                Department
                            </td>
                            <td width="70%">
                                <input name="hidDeptCode" id="deptname" class="easyui-combobox" style="width:500px;" data-options="valueField:'deptCode',textField:'deptName',editable:false" />
                            </td>
                            <td width="15%">
                                &nbsp;
                            </td>
                        </tr>
                        <tr style="height:40px">
                            <td align="center">
                                Office
                            </td>
                            <td>
                                <input name="hidOffCode" id="officename" class="easyui-combobox" style="width:500px;" data-options="valueField:'offCode',textField:'offName',editable:false"/>
                            </td>
                            <td>
                                <button type="button" onclick="getauthority()">Get Authority</button>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div>
                <table id="preferauthoritylist" class="easyui-datagrid" style="width:80%;height:300px;" title="Prefer Authority"
                    rownumbers="true" singleSelect="false">
                    <thead>
                        <tr>
                            <th data-options="field:'postcode',width:50,checkbox:true"></th>
                            <th data-options="field:'post',width:550"></th>
                        </tr> 
                    </thead>
                </table>
            </div>
            <div>
                <button type="submit" onclick="return saveData()">Save</button>
            </div>
        </form>
    </body>
</html>
