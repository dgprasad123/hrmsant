<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <script type="text/javascript">
            function childWindowFunc(){
                alert("Child Window");
            }
        </script>
    </head>
    <body onload="childWindowFunc()">
        <table id="officedg" class="easyui-datagrid" title="Office List" nowrap="false" pagination="true" style="width:700px;height:500px;"
               data-options="rownumbers:true,singleSelect:true,url:'getOfficeListJSONPaging.htm',method:'post',pageList:[50,100,200]" toolbar="#officedgtoolbar">
            <thead>
                <tr>
                    <th data-options="field:'value',formatter:radioFuncOffice"></th>
                    <th data-options="field:'label',width:650">Office Name</th>
                </tr>
            </thead>
        </table>
        <div id="officedgtoolbar" align="center" style="padding:2px 5px;">
            Enter Office to Search&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-textbox" id="officesearch"/>
            &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" id="searchBtn" class="easyui-linkbutton c6" iconCls="icon-search" onclick="searchOffice()" style="width:90px">Search</a>
        </div>
    </body>
</html>
