
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%

    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />  
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>        
        <title>:Search Authority::</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript1.2" type="text/javascript">
            $(document).ready(function() {
                $('#dgleaveauthority').datagrid('resize');
                $('#dgleaveauthority').datagrid({
                    url: 'SelectOtherStaffAction.htm',
                    method: 'post'
                });
            });
            function radio(value, options, rowObject) {
                var radspl = value.split(':');
                var empId = radspl[0];
                var name = radspl[1];
                var desg = radspl[2];
                var spc = radspl[3];
                var radioHtml = '<input type="radio" id="radempid" value="' + value + '" name="radempid" onclick="SelectPost()"/>';
                 return radioHtml;
            }
            function SelectPost() {
                var selected = $("input[name='radempid']:checked").val();
                var radspl = selected.split(':');
                var empId = radspl[0];
                var name = radspl[1];
                var desg = radspl[2];
                var spc = radspl[3];
                parent.SelectEmp(empId, name, desg, spc);
            }
        </script>
    </head>
    <body >
        <form:form  action="selectotherstaff.htm" method="POST" commandName="leaveAuthorityForm">
            <div  id="tbl-container" style="width:100%;overflow: auto;border:1px ">
                <div  align="center">
                    <div  style="width:99%;">                        
                        <div   style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;"> 
                            <table id="dgleaveauthority" class="easyui-datagrid" style="width:100%;height:360px;" title="My Leave"
                                   rownumbers="true" pagination="true" singleSelect="true"  
                                   toolbar="#toolbar" data-options="singleSelect:true,collapsible:true" toolbar="#tb">
                                <thead>
                                    <tr>
                                        <th data-options="field:'radempid'" formatter="radio">Select</th>
                                        <th data-options="field:'name'" width="40%">Name of the Employee</th>
                                        <th data-options="field:'designation'" width="35%">Substantive Post</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div id="toolbar">
                <input class="easyui-linkbutton" type="submit" name="Search" value="Search"/>
            </div>
        </form:form>
    </body>
</html>

