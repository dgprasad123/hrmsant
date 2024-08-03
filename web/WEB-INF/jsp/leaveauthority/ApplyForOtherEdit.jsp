
<%@ page contentType="text/html;charset=windows-1252"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<%    
    int i = 1;
%>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />  
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>        
        <title>Search Authority</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <script language="javascript1.2" type="text/javascript">
            $(document).ready(function() {
                $('#sltDept').combobox({url: 'getDeptComboData.htm',
                    onSelect: function(record) {
                        $('#sltOffice').combobox('clear');
                        var url = 'getOfficeComboData.htm?deptCode='+ record.value;
                        $('#sltOffice').combobox('reload', url);
                    }
                });
            });
            function getauthority() {
                var deptCode = $('#sltDept').combobox('getValue');
                var offCode = $('#sltOffice').combobox('getValue');
                $('#preferauthoritylist').datagrid({
                    url: "getOtherStaffListJSON.htm?deptCode=" + deptCode + "&offCode=" + offCode
                });
            }
            function saveData(row) {
                
                var ids = [];
                var rows = $('#preferauthoritylist').datagrid('getSelections');
              
                for (var i = 0; i < rows.length; i++) {
                    ids.push(rows[i].post_code);
                }
                $('#chkAuth').val(ids);
                
            }
//              function checkbox1(value) {
//              alert(value);
//              }
        </script>
    </head>
    <body >
        <form:form  action="selectotherstaff.htm" method="POST" commandName="leaveAuthorityForm">
            <input type="hidden" name="chkAuth" id="chkAuth"/>
            
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
                        <tr style="height: 40px" >    
                            <td width="20%" align="center">Select Department:</td>
                            <td width="70%"><input class="easyui-combobox"  id="sltDept" name="sltDept" data-options="valueField:'value',textField:'label'"  style="width:270px;height:25px"></td> 
                            <td width="10%">&nbsp;</td>
                        </tr>
                        <tr style="height: 40px" >    
                            <td width="20%" align="center">Select Office:</td>
                            <td width="70%"><input class="easyui-combobox"  id="sltOffice" name="sltOffice" data-options="valueField:'value',textField:'label'"  style="width:270px;height:25px"></td> 
                            <td width="10%">&nbsp;</td>
                        </tr>
                        <tr style="height: 40px" >    
                            <td colspan="3" align="center"><button type="button" onclick="getauthority()">Get Staff</button></td>
                           
                        </tr>
                    </table>
                </div>
            </div>
        <div>
            <table id="preferauthoritylist" class="easyui-datagrid" style="width:95%;height:300px;" title="Prefer Authority"
                   rownumbers="true" pagination="true" singleSelect="false">
                <thead>
                    <tr>
                       <th data-options="field:'post_code',width:50,checkbox:true"></th>
                       
                        <th data-options="field:'designation',width:550"> Authority</th>
                    </tr> 
                </thead>
            </table>
        </div>
        <div>
            <button type="submit" name="Save" onclick="return saveData()">Save</button>
        </div>
    </form:form>
</body>
</html>

