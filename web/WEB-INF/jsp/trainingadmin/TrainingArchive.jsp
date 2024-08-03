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
                    url: "TrainingArchiveController.htm"
                });
            });
            function openWindow(linkurl, modname) {
                $("#winfram").attr("src", linkurl);
                $("#win").window("open");
                $("#win").window("setTitle", modname);

            }
            function closeIframe()
            {
                    $('#win').dialog('close');
                $('#dg').datagrid({
                    url: "TrainingListController.htm"
                });
                    return false;             
            }            
        </script>       
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="ARCHIVE" />
        </jsp:include>
                <h1 style="margin:0px;font-size:18pt;color:#333333;border-bottom:1px solid #DADADA;padding-bottom:5px;">Training Archives</h1>
                <form class="form-control-inline" name="frmTraining"  id="frmTraining" action="TrainingProgramAction.htm" method="POST" commandName="TrainingProgramForm" onsubmit="javascript: return validateAdd()">
                    <table id="dg" class="easyui-datagrid" style="width:100%;height:680px;" title="Training Archive List" 
                           data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true,pageSize:20" toolbar="#toolbar">
                        <thead>
                            <tr>
                                <th data-options="field:'programName',width:'26%'" style="font-weight:bold;">Training Program Name</th>
                                <th data-options="field:'facultyName'">Faculties</th>
                                <th data-options="field:'sponsorName'">Sponsors</th>
                                <th data-options="field:'fromDate'">From Date</th>
                                <th data-options="field:'toDate'">To Date</th>
                                <th data-options="field:'duration'">Duration</th>
                                <th data-options="field:'capacity'">Capacity</th>
                                <th data-options="field:'editProgram',width:'3%',formatter:editProgram" align='center'></th>                        
                                <th data-options="field:'assignCadre',width:'3%',formatter:assignCadre" align='center'></th>
                                <th data-options="field:'assignFaculty',width:'3%',formatter:assignFaculty" align='center'></th>
                                <th data-options="field:'assignSponsor',width:'3%',formatter:assignSponsor" align='center'></th>
                                <!--<th data-options="field:'viewDetail',width:'3%',formatter:viewDetail" align='center'></th>-->
                                <th data-options="field:'downloadPdf',width:'3%',formatter:downloadPdf" align='center'></th>
                                <th data-options="field:'showParticipants',width:'3%',formatter:showParticipants" align='center'></th>
                                <th data-options="field:'deleteProgram',width:'3%',formatter:showProgramDetail" align='center'></th>
                            </tr> 
                        </thead>
                    </table>
                </form>
                <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window'" closed="true" style="width:70%;height:600px;padding:5px;">
                    <iframe id="winfram"  frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%"></iframe>
                </div>
                </body>
                <script type="text/javascript">
                    function showProgramDetail(val, row)
                    {
                        str = '<a href="javascript:void(0)" onclick="deleteProgram(' + row.trainingProgramID + ')" title="Delete Program"><img src="images/delete_icon.png" alt="Delete Program" /></a>';
                        return str;
                    }
                    function editProgram(val, row)
                    {
                        str1 = '<a href="javascript:void(0)" onclick="openWindow(\'EditTrainingProgramAction.htm?opt=edit&trainingId=' + row.trainingProgramID + '\', \'Edit Training Program\')" title="Edit Training Program"><img src="images/edit.png" alt="Edit Program" /></a>';
                        return str1;
                    }
                    function assignCadre(val, row)
                    {
                        str1 = '<a href="javascript:void(0)" onclick="openWindow(\'AssignCadre.htm?trainingId='+row.trainingProgramID+'\',\'Assign Cadres\')" title="Assign Cadre"><img src="images/cadre_icon.png" alt="Assign Cadre" /></a>';
                        return str1;
                    }
                    function assignFaculty(val, row)
                    {
                        str1 = '<a href="javascript:void(0)" onclick="openWindow(\'AssignFaculties.htm?trainingId=' + row.trainingProgramID + '\', \'Assign Faculties\')" title="Assign Faculty"><img src="images/faculty.png" alt="Assign Faculty" width="18" /></a>';
                        return str1;
                    }
                    function assignSponsor(val, row)
                    {
                        str1 = '<a href="javascript:void(0)" onclick="openWindow(\'AssignSponsors.htm?trainingId=' + row.trainingProgramID + '\', \'Assign Sponsor\')" title="Assign Sponsor"><img src="images/sponsor.png" alt="Assign Sponsor" width="18" /></a>';
                        return str1;
                    }
                    function viewDetail(val, row)
                    {
                        str1 = '<a href="viewDetail.htm?trainingId=' + row.trainingProgramID + '&date=' + $('#program_date').val() + '" title="View Detail"><img src="images/view_icon.png" alt="View Detail" width="18" /></a>';
                        return str1;
                    }
                    function deleteProgram(programID)
                    {
                        if (confirm("Are you sure to delete the Training Program?"))
                        {
                            $.ajax({
                                type: 'GET',
                                url: 'DeleteTrainingProgram.htm?trainingId=' + programID,
                                success: function (response) {
                                    $('#dg').datagrid('reload');
                                                     }
                            });
                        }
                    }
                    function downloadPdf(val, row)
                    {
                        if(row.diskFileName == '')
                            str1 = '';
                        else
                            str1 = '<a href="downloadPdf.htm?trainingId=' + row.trainingProgramID + '" title="Download Document"><img src="images/pdf_icon.png" alt="Download Document" width="18" /></a>';
                        return str1;
                    }
                    function showParticipants(val, row)
                    {
                        str1 = '<a href="showParticipants.htm?trainingId=' + row.trainingProgramID + '" title="View Participants"><img src="images/participants.png" alt="View Participants" width="18" /></a>';
                        return str1;                        
                    }
                </script>             
            </div>
        </div>
    </body>
</html>
