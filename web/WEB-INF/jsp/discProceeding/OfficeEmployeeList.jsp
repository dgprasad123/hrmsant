<%-- 
    Document   : OfficeEmployeeList
    Created on : 16 Feb, 2017, 3:53:03 PM
    Author     : Prashant
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>PROCEEDINGS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        
        <script type="text/javascript">
            $(document).ready(function () {
                
                $('#officeDialog').dialog('close');
                $('#officename').textbox({
                    onClickButton: function () {
                        deptcode = $('#hidDeptCode').val();
                        if (deptcode != '') {
                            $('#officeDialog').dialog('open');
                            $('#officedg').datagrid({
                                url: "GetOfficeListJSON.htm?deptcode=" + deptcode
                            });
                        }
                    }
                });
            });
            
            function getEmployee() {
                var offName= $('#officename').val();
                if(offName != ''){
                    $('#empListDg').datagrid('load', {
                        deptCode: $('#hidDeptCode').val(),
                        offCode: $('#hidOffCode').val()
                    });
                }else{
                    alert("Select an Office");
                    return false;
                }
            }
            
            function radioFuncOffice(val, row) {
                var data = row.offCode + ":" + row.offName;
                return "<input type='radio' name='rdoffcode' id='rdoffcode' value='" + data + "' onclick='SelectOffice(\"" + data + "\")'/>";
            }
            function SelectDepartment(data) {

                var radspl = data.split(':');
                var deptcode = radspl[0];
                var deptname = radspl[1];
                $('#deptDialog').dialog('close');
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
            
            function disciplinary(){
                var row = $('#empListDg').datagrid('getSelected');
                if (row) {
                    $('#chkEmpId').val(row.doHrmsId);
                    $('#discplinaryBox').dialog('open');
                } else {
                    alert("Select an Employee");
                }
            }
            
            function saveRule15(){
                var url="rule15Controller.htm?DOHRMSID="+$('#chkEmpId').val()+"&offCode="+$('#hidOffCode').val();
                self.location = url;
            }
            
        </script> 
    </head>   
    <body >
        <form:form  action="DiscProceding.htm" method="POST" commandName="DiscProceedingBean">
            <input type="hidden" name="chkEmpDtls" id="chkEmpId"/>
            <input name="hidDeptCode" id="hidDeptCode" type="hidden" value='${deptCode}'/> 
            
            <div id="officeDialog" class="easyui-dialog" title="Select Office" style="width:80%;height: 400px;" data-options="iconCls:'icon-save',modal:true">
                <table id="officedg" class="easyui-datagrid" nowrap="false" style="width:580px;"
                       data-options="rownumbers:true,singleSelect:true,method:'post'">
                    <thead>
                        <tr>
                            <th data-options="field:'offCode',formatter:radioFuncOffice"></th>
                            <th data-options="field:'offName',width:500">Office Name</th>
                        </tr>
                    </thead>
                </table>
            </div>
            
            <div align="center" style="margin-top:5px;margin-bottom:10px;border:1px solid #95B8E7;width:98%;">
                <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
                    <tr style="height:40px" align="center">
                        <td style="font-weight: bold;">Office:</td>
                        <td align="left">
                            <input name="hidOffCode" id="hidOffCode" type="hidden"/>
                            <input id="officename" class="easyui-textbox" 
                                   data-options="editable:false,buttonText:'Search',buttonAlign:'right',buttonIcon:'icon-search',required:true,prompt:'Click on search icon to select Office...'" 
                                   style="width:100%;height:32px;">
                        </td>
                        <td><button type="button" onclick="getEmployee()">Ok</button>&nbsp;</td>
                    </tr>
                </table>
            </div>
            
            <div style="width:100%;"  id="tbl-container">
                <table id="empListDg" class="easyui-datagrid" style="width:100%;height:440px;" title="EMPLOYEE LIST" rownumbers="true" 
                         url="GetEMPListJSON.htm" singleSelect="true" collapsible="false" toolbar="#toolbar">
                    <thead>
                        <tr> 
                            <th data-options="field:'doHrmsId',hidden:true"></th>
                            <th data-options="field:'doEmpName',width:230"><span style="font-weight: bold;">EMPLOYEE NAME</span></th>
                            <th data-options="field:'doEmpCurDegn',width:350"><span style="font-weight: bold;">DESIGNATION</span></th>
                        </tr> 
                    </thead>
                </table>

                <div id="toolbar">
                    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-add'" onclick="return disciplinary()">
                        <span style="font-size:15px;font-weight: bold;">DISCIPLINARY</span></a>
                    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-add'">
                        <span style="font-size:15px;font-weight: bold;">SUSPENSION</span></a>
                    
                    <!-- <span>Product ID:</span>
                        <input id="doEmpName:" style="line-height:26px;border:1px solid #ccc">
                        <a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">Search</a> -->
                       
                </div>

                <div id="discplinaryBox" class="easyui-dialog" title="Memorandum Option" closed="true" style="width:300px;height:100px;padding:10px">
                    <table width="100%" border="0" > 
                        <tr>
                            <td align="center" width="50%">
                                <button type="submit" class="easyui-linkbutton" onclick="return saveRule15()">
                                    <span style="font-weight: bold;">RULE 15 (Major)</span></button>
                            </td>
                            <td align="center" width="50%">
                                <button type="submit" class="easyui-linkbutton"><span style="font-weight: bold;">RULE 16 (Minor)</span></button>
                            </td>
                        </tr>    
                    </table>
                </div>
            </div>
                
        </form:form>
    </body>
</html>