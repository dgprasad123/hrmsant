<%-- 
    Document   : TrainingProgram
    Created on : 22 Oct, 2016, 11:11:07 AM
    Author     : Manoj PC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Training Applications</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}
            #training_form td{padding:6px;}
            .form-control{height:30px;}
            .datagrid-header-row{font-weight:bold;}
            .panel-title{margin-bottom:5px;} 
            .window .window-header{background:#5593BC;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
        </style>
        <script type="text/javascript">
            $(document).ready(function() {

                $('#trainingdg').datagrid({
                    url: "GetTrainingApplications.htm"
                });
            });
        </script>
    </head>
    <body>
        <table id="trainingdg" class="easyui-datagrid" style="width:100%;height:600px;" title="My Training Applications" 
               data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true" toolbar="#toolbar">
            <thead>
                <tr>
                    <th data-options="field:'programName'" style="font-weight:bold;">Training Program Name</th>
                    <th data-options="field:'venue'" style="font-weight:bold;">Venue</th>
                    <th data-options="field:'fromDate'">From Date</th>
                    <th data-options="field:'toDate'">To Date</th>
                    <th data-options="field:'duration'">Duration</th>
                    <th data-options="field:'trainingProgramID',width:'10%',formatter:applyTraining">Apply</th>                        
                </tr> 
            </thead>
        </table>
       
    </body>
    <script type="text/javascript">
        function applyTraining(val, row)
        {
            if (row.status == '')
            {
                str = '<a href="javascript:void(0)" onclick="openapplyTraining(' + row.trainingProgramID + ')" title="Apply for Training">Apply</a>';
            }
            else
            {
                if (row.status == 'AGREED')
                    str = '<span style="color:#008900;font-weight:bold;">' + row.status + '</span>'
                else
                    str = '<span style="color:#FF0000;font-weight:bold;">' + row.status + '</span>'
            }
            return str;
        }
    </script>
</html>
