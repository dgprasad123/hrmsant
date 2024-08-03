<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}
            #training_form td{padding:6px;}
            .form-control{height:30px;}
        </style>
        <script type="text/javascript">
            function validateAdd()
            {
                $('#faculty_name').val($('#faculty_name').val().trim());
                $('#designation').val($('#designation').val().trim());
                $('#faculty_code').val($('#faculty_code').val().trim());
                if($('#faculty_name').val().trim() == '')
                {
                    
                    alert("Please enter Faculty Name.");
                    $('#faculty_name').focus();
                    return false;
                }
                if($('#designation').val().trim() == '')
                {
                    
                    alert("Please enter Designation.");
                    $('#designation').focus();
                    return false;
                } 
                if($('#faculty_code').val().trim() == '')
                {
                    
                    alert("Please enter Faculty Code.");
                    $('#faculty_code').focus();
                    return false;
                }                 
            }
        </script>
    </head>
    <body>
        <form:form class="form-control-inline"  action="AddTrainingFaculty.htm" method="POST" commandName="TrainingFacultyForm" onsubmit="return validateAdd()">
            <table width="100%" id="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                <tr style="font-weight:bold;background:#EAEAEA;">
                    <td colspan="2">Please fill out all the information</td>
                </tr>
                <tr>
                    <td align="right" width="25%">Faculty Name:</td>
                    <td><input type="text" name="faculty_name" id="faculty_name" size="50" class="form-control-inline" /></td>
                </tr> 
                <tr>
                    <td align="right" width="25%">Designation:</td>
                    <td><input type="text" name="designation" id="designation" size="50" class="form-control-inline" /></td>
                </tr>
                <tr>
                    <td align="right" width="25%">Faculty Code:</td>
                    <td><input type="text" name="faculty_code" id="faculty_code" size="50" class="form-control-inline" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Save Faculty" name="save" class="btn btn-primary btn-sm" />
                        <input type="submit" value="Cancel" name="close" class="btn btn-primary btn-sm" onclick="window.parent.closeIframe();" />
                    </td>
                </tr>

            </table>
        </form:form>
    </body>
</html>
