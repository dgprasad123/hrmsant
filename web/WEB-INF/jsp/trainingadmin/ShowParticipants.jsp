<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Training Calendar:: HRMS</title>
        <style type="text/css">
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
        </style>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            .window .window-header{background:#5593BC;}
            table {border:1px solid #DADADA;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
        </style>
        <script type="text/javascript">
            $(document).ready(function () {

                $('#dg').datagrid({
                    url: "GetParticipantList.htm?trainingId="+$('#trainingId').val()
                });
            });
          
        </script>       
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="CALENDAR" />
        </jsp:include>
                <h1 style="margin:0px;font-size:18pt;color:#333333;border-bottom:1px solid #DADADA;padding-bottom:5px;">View Participant List</h1>
                <div style="padding:10px 0px;">
                    <a href="TrainingCalendarList.htm" style="font-weight:bold;"><img src="images/listview.png" alt="" width="20" style="vertical-align:middle;" /> &laquo; Back to Training Calendar</a>
                </div>
                <form class="form-control-inline" name="frmTraining"  id="frmTraining" action="TrainingProgramAction.htm" method="POST" commandName="TrainingProgramForm" onsubmit="javascript: return validateAdd()">
                    <input type="hidden" name="trainingId" id="trainingId" value="${trainingId}" />
                    <table id="dg" class="easyui-datagrid" style="width:100%;height:400px;" title="Participants" 
                           data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true" toolbar="#toolbar">
                        <thead>
                            <tr>
                                <th data-options="field:'participantName',width:'30%'" style="font-weight:bold;">Participant Name</th>
                                <th data-options="field:'designation'">Designation</th>
                                <th data-options="field:'status'">Status</th>
                            </tr> 
                        </thead>
                    </table>
                </form>
                <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window'" closed="true" style="width:70%;height:600px;padding:5px;">
                    <iframe id="winfram"  frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%"></iframe>
                </div>
                        
            </div>
        </div>
    </body>
</html>
