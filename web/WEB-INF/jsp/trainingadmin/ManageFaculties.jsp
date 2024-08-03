<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Faculties</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            .training_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
            table {border:1px solid #DADADA;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
        </style>
        <script type="text/javascript">
            function validateAdd()
            {
                $('#facultyName').val($('#facultyName').val().trim());
                $('#designation').val($('#designation').val().trim());
                if ($('#facultyName').val().trim() == '')
                {

                    alert("Please enter Faculty Name.");
                    $('#facultyName').focus();
                    return false;
                }
                if ($('#designation').val().trim() == '')
                {
                    alert("Please enter Designation.");
                    $('#designation').focus();
                    return false;
                }
            }
            $(document).ready(function () {

                $('#dg').datagrid({
                    url: "getFacultyList.htm"
                });
            });

        </script>
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="FACULTIES" />
        </jsp:include>
                <h1 style="font-size:18pt;margin:0px;">Manage Faculties</h1>
                <form:form class="form-control-inline"  action="AddTrainingFaculty.htm" method="POST" commandName="TrainingFacultyForm">
                    <input type="hidden" name="facultyCode" id="facultyCode" value="0" />
                    <input type="hidden" name="opt" id="opt" value="" />
                    <table width="100%" class="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">

                        <tr>
                            <td align="right" width="25%">Faculty Name:</td>
                            <td><input type="text" name="facultyName" id="facultyName" size="50" class="form-control-inline" /></td>
                            <td align="right" width="25%">Designation:</td>
                            <td><input type="text" name="designation" id="designation" size="50" class="form-control-inline" /></td>                            
                        </tr> 
                        <tr>
                            <td colspan="4" align="center"><input type="submit" value="Save Faculty" name="save" class="btn btn-primary btn-sm" onclick="return validateAdd()" />
                                <input type="button" value="Cancel" name="close" class="btn btn-primary btn-sm" onclick="self.location = 'ManageFaculties.htm'" />
                            </td>
                        </tr>                       
                    </table>
                </form:form>
                <table id="dg" class="easyui-datagrid" style="width:100%;height:400px;" title="Manage Faculties" 
                       data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true" toolbar="#toolbar">
                    <thead>
                        <tr>
                            <th data-options="field:'facultyName',width:'30%'" style="font-weight:bold;">Faculty Name</th>
                            <th data-options="field:'designation',width:'30%'">Designation</th>
                            <th data-options="field:'editFaculty',width:'3%',formatter:editFaculty" align='center'></th>                        
                            <th data-options="field:'deleteFaculty',width:'3%',formatter:deleteFaculty" align='center'></th>
                        </tr> 
                    </thead>
                </table>
            </div>
        </div>
        <script type="text/javascript">
            function editFaculty(val, row)
            {
                str1 = '<a href="javascript:void(0)" onclick="javascript: editFacultyAction(' + row.facultyCode + ', \'' + row.facultyName + '\', \'' + row.designation + '\')" title="Edit Faculty"><img src="images/edit.png" alt="Edit Faculty" /></a>';
                return str1;
            }
            function deleteFaculty(val, row)
            {
                str1 = '<a href="javascript:void(0)" onclick="javascript: deleteFacultyAction(' + row.facultyCode + ')" title="Delete Faculty"><img src="images/delete_icon.png" alt="Delete Faculty" /></a>';
                return str1;
            }
            function deleteFacultyAction(facultyID)
            {
                if (confirm("Are you sure you want to delete the Faculty?"))
                {
                    $.ajax({
                        type: 'GET',
                        url: 'DeleteFaculty.htm?facultyCode=' + facultyID,
                        success: function (response) {
                            $('#dg').datagrid('reload');
                        }
                    });
                }
            }

            function editFacultyAction(facultyCode, facultyName, designation)
            {
                $('#facultyName').val(facultyName);
                $('#designation').val(designation);
                $('#facultyCode').val(facultyCode);
            }
        </script>         
    </body>
</html>
