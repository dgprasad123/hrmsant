<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Training</title>
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
                    url: "ManageTrainingList.htm"
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
                <form class="form-control-inline" name="frmTraining"  id="frmTraining" action="TrainingProgramAction.htm" method="POST" commandName="TrainingProgramForm" onsubmit="javascript: return validateAdd()">
                    <table id="dg" class="easyui-datagrid" style="width:100%;height:400px;" title="Manage Training" 
                           data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true" toolbar="#toolbar">
                        <thead>
                            <tr>
                                <th data-options="field:'viewDetail',formatter:viewDetail" align='center'></th>
                                <th data-options="field:'instituteName',width:'30%'" style="font-weight:bold;">Institute</th>
                                <th data-options="field:'programName'">Training Program Name</th>
                                <th data-options="field:'fromDate'">From Date</th>
                                <th data-options="field:'toDate'">To Date</th>
                                <th data-options="field:'duration'">Duration</th>
                                <th data-options="field:'capacity',formatter:formatCapacity" align="center">Capacity</th>
                                <th data-options="field:'numApplicants',formatter:formatApplied" align="center">Total Applied</th>
                            </tr> 
                        </thead>
                    </table>
                </form>
                <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window'" closed="true" style="width:70%;height:600px;padding:5px;">
                    <iframe id="winfram"  frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%"></iframe>
                </div>
                </body>
                <script type="text/javascript">
                    function viewDetail(val, row)
                    {
                        str = '<a href="ManageTrainingDetail.htm?trainingId='+row.trainingProgramID+'"><img src="images/view_detail_icon.png" alt="View Detail" width="20" /></a>';
                        return str;
                    }
                   function formatCapacity(val, row)
                   {
                       str = '<strong style="font-size:11pt;color:#009900;">'+row.capacity+'</strong>';
                       return str;
                   }
                   function formatApplied(val, row)
                   {
                       str = '<strong style="font-size:11pt;color:#009900;">'+row.numApplicants+'</strong>';
                       return str;
                   }                   
                </script>             
            </div>
        </div>
    </body>
</html>
