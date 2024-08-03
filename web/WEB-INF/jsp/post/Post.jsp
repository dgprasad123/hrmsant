<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
    </head>
    <body>
        <table id="postdg" class="easyui-datagrid" nowrap="false" pagination="true" style="width:700px;height:500px;"
               data-options="rownumbers:true,singleSelect:true,url:'getSPCListJSONPaging.htm',method:'post',pageList:[50,100,200]">
            <thead>
                <tr>
                    <th data-options="field:'spc',formatter:radioFuncPost"></th>
                    <th data-options="field:'spn',width:630">Post</th>
                </tr>
            </thead>
        </table>
        <!--<div id="postdgtoolbar" align="center" style="padding:2px 5px;">
            Enter Office to Search&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-textbox" id="postsearch"/>
            &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" id="searchBtn" class="easyui-linkbutton c6" iconCls="icon-search" onclick="searchPost()" style="width:90px">Search</a>
        </div>-->
    </body>
</html>
