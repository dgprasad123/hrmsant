<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
    </head>
    <body>
        <table id="deptdg" class="easyui-datagrid" title="Department List" style="width:420px;height:500px;"
               data-options="rownumbers:true,singleSelect:true,url:'GetDeptListJSON.htm',method:'post'">
            <thead>
                <tr>
                    <th data-options="field:'deptCode',formatter:radioFuncDept"></th>
                    <th data-options="field:'deptName',width:300">Department Name</th>
                </tr>
            </thead>
        </table>
    </body>
</html>
