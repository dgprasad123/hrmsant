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
             function newReemployment() {
                $('#saveBtn').show();
                $('#dlg').dialog('open');
                $('#ReemploymentForm').form('clear');
            }

        </script>    
          </head>
    <body>
         <div id="deputationTool" style="padding:3px">                    
            <a href="javascript:newReemployment();" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true" id="addBtn">Add New</a>
            <a href="javascript:newReemployment();" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true" id="editBtn">Edit</a>
        </div>
        <div style='clear:both'></div>
       
      
        <%-- Start - Content for Child Window --%>
       
        
       
       
        <%-- End - Content for Child Window --%>
    </body>
</html>