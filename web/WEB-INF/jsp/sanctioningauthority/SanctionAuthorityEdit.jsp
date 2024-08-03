
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
            function getauthority() {
                var offCode = $('#sltOffice').combobox('getValue');
                $('#preferauthoritylist').datagrid({
                    url: "getSpcListJSON.htm?offcode=" + offCode
                });
            }
             function radio(value, options, rowObject) {
                var radioHtml = '<input type="radio" id="radempid" value="' + value + '" name="radempid" onclick="SelectPost()"/>';
                 return radioHtml;
            }
             function SelectPost() {
              var selected = $("input[name='radempid']:checked").val();
               var deptCode = $('#sltDept').val();
               var offCode = $('#sltOffice').val();
                var radspl = selected.split('|');
                var spc = radspl[0];
                var empid = radspl[1];
                var post = radspl[2];
                var empname = radspl[3];
                parent.SelectSpn(spc, empid, post, empname,deptCode,offCode);
            }
           
        </script>
    </head>
    <body >
        <form:form  action="sancauthority.htm" method="POST" commandName="sancAuthorityForm">
            <input type="hidden" name="chkAuth" id="chkAuth"/>
            
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
                        <tr style="height: 40px" >    
                            <td width="20%" align="center">Select Department:</td>
                            <td width="70%"><input class="easyui-combobox"  id="sltDept" name="sltDept" data-options="valueField:'deptCode',textField:'deptName',url:'deptListJSON.htm',
                           onSelect: function (record) {
                           $('#sltOffice').combobox('clear');
                           var url = 'getOfficeListJSON.htm?deptcode='+record.deptCode;
                           $('#sltOffice').combobox('reload', url);
                           },id:'sltDept'"  style="width:270px;height:25px"></td> 
                            <td width="10%">&nbsp;</td>
                        </tr>
                        <tr style="height: 40px" >    
                            <td width="20%" align="center">Select Office:</td>
                            <td width="70%"><input class="easyui-combobox"  id="sltOffice" name="sltOffice" data-options="valueField:'offCode',textField:'offName',id:'sltOffice'"  style="width:270px;height:25px"></td> 
                            <td width="10%">&nbsp;</td>
                        </tr>
                        <tr style="height: 40px" >    
                            <td colspan="3" align="center"><button type="button" onclick="getauthority()">Get Authority</button></td>
                           
                        </tr>
                    </table>
                </div>
            </div>
        <div>
            <table id="preferauthoritylist" class="easyui-datagrid" style="width:95%;height:300px;" title="Prefer Authority"
                   rownumbers="true" pagination="true" singleSelect="false">
                <thead>
                    <tr>
                        <th data-options="field:'spcHrmsId'" formatter="radio">Select</th>
                       <th data-options="field:'empname',width:550,id:'empname'"> Name</th>
                        <th data-options="field:'postname',width:550"> Post</th>
                    </tr> 
                </thead>
            </table>
        </div>
    </form:form>
</body>
</html>

