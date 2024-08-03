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
        <title>Manage Training Program</title>
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
        </style>
        <script type="text/javascript">
            function closeWindow()
            {
                $('#win').window('close');
            }
            $(document).ready(function () {
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
                if ($('#status_id').val() == 'success')
                {
                    window.parent.closeIframe();
                }
                hrmsId = $("#emp_hrms_id").val();
                $('#dg').datagrid({
                    url: "TrainingProgramListAction.htm?hrmsId=" + hrmsId + "&date=" + $('#program_date').val()
                });
                window.parent.refreshCalendar();
            });
            function changepost() {
                var url = 'BrowsePost.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "80%"});
            }
            function selectFaculty() {
                var url = 'BrowseFaculties.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "80%"});
            }
            function SelectFaculty(offName) {
                $.colorbox.close();
                arrTemp = offName.split('||');
                $('#govt_faculty').html(arrTemp[1]);
            }
            function displayEntry(displayType)
            {
                $('#faculty_other').css('display', 'none');
                $('#faculty_govt').css('display', 'none');
                if (displayType == 'GOVT')
                    $('#faculty_govt').css('display', 'block');
                else
                    $('#faculty_other').css('display', 'block');
            }
            function openWindow(linkurl, modname) {
                $("#winfram").attr("src", linkurl);
                $("#win").window("open");
                $("#win").window("setTitle", modname);

            }
            function addTrainingProgram(programDate)
            {
                openWindow('TrainingProgramController.htm?opt=list&date=' + programDate, 'Manage Training Program');
            }
            function closeIframe()
            {
                $('#win').dialog('close');
                $('#dg').datagrid('reload');
                window.parent.refreshCalendar();
                return false;
            }
        </script>
    </head>
    <body>
        <form class="form-control-inline" name="frmTraining"  id="frmTraining" action="TrainingProgramAction.htm" method="POST" commandName="TrainingProgramForm" onsubmit="javascript: return validateAdd()">
            <input type="hidden" name="emp_hrms_id" id="emp_hrms_id" value="${hrms_id}" />
            <input type="hidden" name="status_id" id="status_id" value="${status}" />
            <input type="hidden" name="program_date" id="program_date" value="${date}" />
            <table id="dg" class="easyui-datagrid" style="width:100%;height:280px;" title="Manage Training Program Detail" 
                   data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true" toolbar="#toolbar">
                <thead>
                    <tr>
                        <th data-options="field:'programName'">Training Program Name</th>
                        <th data-options="field:'fromDate'">From Date</th>
                        <th data-options="field:'toDate'">To Date</th>
                        <th data-options="field:'duration'">Duration</th>
                        <th data-options="field:'editProgram',width:'5%',formatter:editProgram"></th>                        
                        <th data-options="field:'deleteProgram',width:'5%',formatter:showProgramDetail"></th>
                        <th data-options="field:'assignCadre',width:'5%',formatter:assignCadre"></th>
                        <th data-options="field:'assignFaculty',width:'5%',formatter:assignFaculty"></th>
                    </tr> 
                </thead>
            </table>
            <input type="button" value="Add New Training Program &raquo;" class="btn btn-primary btn-sm" onclick="self.location='TrainingProgramController.htm?opt=list&date='+$('#program_date').val()" />
        </form>
        <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window'" closed="true" style="width:80%;height:400px;padding:5px;">
            <iframe id="winfram"  frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%"></iframe>
        </div>
        <script type="text/javascript">
            function showProgramDetail(val, row)
            {
                str = '<a href="javascript:void(0)" onclick="deleteProgram(' + row.trainingProgramID + ')" title="Delete Program"><img src="images/delete_icon.png" alt="Delete Program" /></a>';
                return str;
            }
            function editProgram(val, row)
            {
                str1 = '<a href="EditTrainingProgramAction.htm?opt=edit&trainingId='+row.trainingProgramID+'&date='+$('#program_date').val()+'" title="Edit Training Program"><img src="images/edit.png" alt="Edit Program" /></a>';
                return str1;
            } 
            function assignCadre(val, row)
            {
                str1 = '<a href="AssignCadre.htm?trainingId='+row.trainingProgramID+'&date='+$('#program_date').val()+'" title="Assign Cadre"><img src="images/cadre_icon.png" alt="Assign Cadre" /></a>';
                return str1;
            }  
            function assignFaculty(val, row)
            {
                str1 = '<a href="AssignFaculty.htm?trainingId='+row.trainingProgramID+'&date='+$('#program_date').val()+'" title="Assign Faculty"><img src="images/faculty.png" alt="Assign Faculty" width="18" /></a>';
                return str1;
            }              
            function deleteProgram(programID)
            {
                if (confirm("Are you sure you want to delete the Training Program?"))
                {
                    $.ajax({
                        type: 'GET',
                        url: 'DeleteTrainingProgram.htm?trainingId=' + programID + '&empId=' + $('#emp_hrms_id').val(),
                        success: function (response) {
                            $('#dg').datagrid('reload');
                            window.parent.refreshCalendar();
                        }
                    });
                }
            }
        </script>
    </body>
</html>
