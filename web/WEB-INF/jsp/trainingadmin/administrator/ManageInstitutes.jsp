<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Previous Training Detail</title>

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
            .tblres td{padding:5px;}
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
        <div style="width:100%;margin:0px auto;"><h1 style="font-size:18pt;margin:0px;margin-bottom:10px;">Manage Institutes</h1>
            
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
                <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>
            <tr style='font-weight:bold;background:#0D508E;color:#FFFFFF;'>
                <td>Sl No.</td>
                <td>Institute Name</td>
                <td>Location</td>
                <td>Website</td>
                <td>Email</td>
                <td>Phone</td>
                <td>Contact Person</td>
            </tr>
            <c:forEach items="${instituteList}" var="il" varStatus="cnt">
                <tr style='background:#FFFFFF'>
                    <td>${cnt.index+1}</td>
                    <td>${il.instituteName}</td>
                    <td>${il.location}</td>
                    <td>${il.website}</td>
                    <td>${il.email}</td>
                    <td>${il.phone}</td>
                    <td>${il.contactPerson}</td>
                </tr>
            </c:forEach>
           </table>         
            
            
            
        </div>
        </div>
        </div>  
    </body>
</html>