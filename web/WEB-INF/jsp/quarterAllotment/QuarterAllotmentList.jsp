<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type='text/javascript'>
             function newquarteallotment() {
                $('#saveBtn').show();
                $('#dlg').dialog('open');
                $('#RetrenchmentForm').form('clear');
            }

        </script>    
          </head>
    <body>
         <div id="Quarte AllotmentTool" style="padding:3px">                    
            <a href="javascript:newquarteallotment();" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true" id="addBtn">Add New</a>
            <a href="javascript:newquarteallotment();" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true" id="editBtn">Edit</a>
        </div>
        <div style='clear:both'></div>
        <table id="Resignationdg" class="easyui-datagrid" style="width:100%;height:400px;" title="Quarte Allotment List"
               rownumbers="true" singleSelect="true" url="GetQuarterAllotmentListJSON.htm" singleSelect="true" pagination="true" collapsible="false"
               data-options="nowrap:false,fitColumns:false" toolbar="#quarteallotmentTool">
            <thead>
                <tr>
                    <th data-options="field:'notId',hidden:true"></th>
                    <th data-options="field:'doe',width:150">Date of Entry</th>
                    <th data-options="field:'doe',width:150">Date of Allotment</th>
                     <th data-options="field:'doe',width:150">Date of Possession</th>
                    <th data-options="field:'notOrdNo',width:150">Quarter No</th>
                    <th data-options="field:'notOrdDt',width:150">Quarter<br />Rete</th>
                    <th data-options="field:'notType',width:150">Surrendered</th>
                   
                    
                </tr> 
            </thead>
        </table>
       
      
        <%-- Start - Content for Child Window --%>
       
        
       
       
        <%-- End - Content for Child Window --%>
    </body>
</html>