<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Institute Profile</title>

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
                        .profile_form td{padding:5px;}
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
                $('#venueName').val($('#venueName').val().trim());
                if ($('#venueName').val().trim() == '')
                {
                    alert("Please enter Venue Name.");
                    $('#venueName').focus();
                    return false;
                }
            }
 
        </script>
    </head>
    <body>
                <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="PROFILE" />
        </jsp:include>
                <h1 style="margin:0px;font-size:18pt;color:#333333;border-bottom:1px solid #DADADA;padding-bottom:5px;">Edit Profile</h1>
                <form:form class="form-control-inline"  action="UpdateInstituteProfile.htm" method="POST" commandName="InstituteProfile">
                    <input type="hidden" name="instituteId" id="instituteId" value="${InstituteForm.instituteId}" />
                    <table width="100%" class="profile_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                        <tr>
                            <td align="right" width="25%">Institute Name:</td>
                            <td><input type="text" name="instituteName" id="instituteName" value="${InstituteForm.instituteName}" size="50" class="form-control-inline" /></td>
                        </tr>
                        <tr>
                            <td align="right" width="25%">Location:</td>
                            <td><input type="text" name="location" id="location" value="${InstituteForm.location}" size="50" class="form-control-inline" /></td>
                        </tr>  
                        <tr>
                            <td align="right" width="25%">Website:</td>
                            <td><input type="text" name="website" id="website" value="${InstituteForm.website}" size="50" class="form-control-inline" /></td>
                        </tr>
                        <tr>
                            <td align="right" width="25%">Email:</td>
                            <td><input type="text" name="email" id="email" value="${InstituteForm.email}" size="50" class="form-control-inline" /></td>
                        </tr>
                        <tr>
                            <td align="right" width="25%">Phone:</td>
                            <td><input type="text" name="phone" id="phone" value="${InstituteForm.phone}" size="50" class="form-control-inline" /></td>
                        </tr>
                        <tr>
                            <td align="right" width="25%">Contact Person:</td>
                            <td><input type="text" name="contactPerson" id="contactPerson" value="${InstituteForm.contactPerson}" size="50" class="form-control-inline" /></td>
                        </tr>                        
                        <tr>
                            <td colspan="2" align="center"><input type="submit" value="Update Profile" name="save" class="btn btn-primary btn-sm" onclick="return validateUpdate()" /></td>
                        </tr>                       
                    </table>
                </form:form>
                
            </div>
        </div>
             
    </body>
</html>
