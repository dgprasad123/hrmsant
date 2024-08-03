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
            .institute_form td{padding:6px;}
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
                if ($('#instituteName').val().trim() == '')
                {
                    alert("Please enter Institute Name.");
                    $('#instituteName').focus();
                    return false;
                }
                if ($('#location').val().trim() == '')
                {
                    alert("Please enter Location.");
                    $('#location').focus();
                    return false;
                }
                if ($('#username').val().trim() == '')
                {
                    alert("Please enter User Name.");
                    $('#username').focus();
                    return false;
                }
                if ($('#password').val().trim() == '')
                {
                    alert("Please enter Password.");
                    $('#password').focus();
                    return false;
                }
            }
            $(document).ready(function () {

                $('#dg').datagrid({
                    url: "getInstituteList.htm"
                });
            });

        </script>
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="INSTITUTES" />
        </jsp:include>
                <h1 style="font-size:18pt;margin:0px;">Manage Institutes</h1>
                <form:form class="form-control-inline"  action="AddTrainingInstitutes.htm" method="POST" commandName="TrainingInstituteForm">
                    <input type="hidden" name="instituteId" id="instituteId" value="0" />
                    <input type="hidden" name="opt" id="opt" value="" />
                     <table width="100%" class="institute_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                        <tr>
                            <td align="right" width="15%">Institute Name:</td>
                            <td><input type="text" name="instituteName" id="instituteName" value="${InstituteForm.instituteName}" size="50" class="form-control-inline" /></td>
                            <td align="right" width="15%">Location:</td>
                            <td><input type="text" name="location" id="location" value="${InstituteForm.location}" size="50" class="form-control-inline" /></td>
                        </tr>  
                        <tr>
                            <td align="right">Website:</td>
                            <td><input type="text" name="website" id="website" value="${InstituteForm.website}" size="50" class="form-control-inline" /></td>
                            <td align="right">Email:</td>
                            <td><input type="text" name="email" id="email" value="${InstituteForm.email}" size="50" class="form-control-inline" /></td>
                        </tr>
                        <tr>
                            <td align="right">Phone:</td>
                            <td><input type="text" name="phone" id="phone" value="${InstituteForm.phone}" size="50" class="form-control-inline" /></td>
                            <td align="right">Contact Person:</td>
                            <td><input type="text" name="contactPerson" id="contactPerson" value="${InstituteForm.contactPerson}" size="50" class="form-control-inline" /></td>
                        </tr>
                        <tr>
                            <td align="right">Territory:</td>
                                    <td colspan="3"><select name="outsideTerritory" id="outsideTerritory" size="1">
                                            <option value="INSIDE">Inside State</option>
                                            <option value="OUTSIDE_STATE">Outside State</option>
                                            <option value="FOREIGN">Foreign</option>
                                </select></td>
                        </tr>                        
                        <tr>
                            <td align="right">User Name:</td>
                            <td><input type="text" name="username" id="username" value="${InstituteForm.username}" size="50" class="form-control-inline" /></td>
                            <td align="right">Password:</td>
                            <td><input type="password" name="password" id="password" value="${InstituteForm.password}" size="50" class="form-control-inline" /></td>
                        </tr>                          
                        <tr>
                            <td colspan="4" align="center"><input type="submit" value="Save Institute" name="save" class="btn btn-primary btn-sm" onclick="return validateAdd()" /></td>
                        </tr>                       
                    </table>
                </form:form>
                <table id="dg" class="easyui-datagrid" style="width:100%;height:400px;" title="Manage Institutes" 
                       data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true" toolbar="#toolbar">
                    <thead>
                        <tr>
                            <th data-options="field:'editInstitute',width:'3%',formatter:editInstitute" align='center'></th>
                            <th data-options="field:'instituteName',width:'30%'" style="font-weight:bold;">Institute Name</th>
                            <th data-options="field:'location'">Location</th>
                            <th data-options="field:'website'">Website</th>
                            <th data-options="field:'email'">Email</th>
                            <th data-options="field:'phone'">Phone</th>
                            <th data-options="field:'contactPerson'">Contact Person</th>
                        </tr> 
                    </thead>
                </table>
            </div>
        </div>
        <script type="text/javascript">
            function editInstitute(val, row)
            {
                str1 = '<a href="javascript:void(0)" onclick="javascript: editInstituteAction(' + row.instituteId + ', \'' + row.instituteName + '\', \'' + row.location + '\', \'' + row.email + '\', \'' + row.website + '\', \'' + row.phone + '\', \'' + row.contactPerson + '\', \'' + row.outsideTerritory + '\')" title="Edit Institute"><img src="images/edit.png" alt="Edit Institute" /></a>';
                return str1;
            }
            function deleteInstitute(val, row)
            {
                str1 = '<a href="javascript:void(0)" onclick="javascript: deleteFacultyAction(' + row.facultyCode + ')" title="Delete Faculty"><img src="images/delete_icon.png" alt="Delete Faculty" /></a>';
                return str1;
            }
            function deleteInstituteAction(facultyID)
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

            function editInstituteAction(instituteCode, instituteName, location, email, website, phone, contactPerson, outsideTerritory)
            {
                $('#instituteName').val(instituteName);
                $('#location').val(location);
                $('#instituteId').val(instituteCode);
                $('#email').val(email);
                $('#website').val(website);
                $('#phone').val(phone);
                if(contactPerson != 'undefined')
                    $('#contactPerson').val(contactPerson);
                $('#outsideTerritory').val(outsideTerritory);
            }
        </script>         
    </body>
</html>
