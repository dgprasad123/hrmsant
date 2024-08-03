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
            .datagrid-header-row{font-weight:bold;}
            .panel-title{margin-bottom:5px;} 
            .window .window-header{background:#5593BC;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .custom_table td{padding:4px;}
            .pagination{font-size:11pt;}
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                if ($('#hasSelected').val() == 'true')
                {
                    $('#previous_btn').css('display', 'none');
                }
                $('#trainingdg').datagrid({
                    url: "ApplyTrainingList.htm"
                });

                $('#mytrainingdg').datagrid({
                    url: "GetTrainingApplications.htm?page=1&rows=30"
                });
                
                $('#department').combobox({
                    onSelect: function(record) {
                        $('#office').combobox('clear');
                        $('#post').combobox('clear');
                        var url = 'GetTrainingOfficeListJSON.htm?deptcode=' + record.deptCode;
                        $('#office').combobox('reload', url);
                    }
                });
                $('#office').combobox({
                    onSelect: function(record) {
                        $('#post').combobox('clear');
                        var url = 'getPostListTrainingJSON.htm?deptcode='+$('#department').combobox('getValue')+'&offcode=' + record.offCode;
                        $('#post').combobox('reload', url);
                    }
                });
                $.ajax({
                    url: "GetApplyPreviousTraining.htm",
                    success: function(result) {
                        $('#prevtraining_blk').html(result); // reload the user data
                    }});
            });
            function withdrawTrainingAction(trId)
            {
                if(confirm("Are you sure you want to withdraw your application?\nYou can apply again if you want before due date."))
                {
                    $.ajax({
                        url: "witdrawTrainingAction.htm",
                        data: "trainingId=" + trId + '&empId=' + $('#trainingEmpId').val(),
                        type: 'post',
                        success: function(result) {
                            $('#mytrainingdg').datagrid('reload'); // reload the user data
                            $('#trainingdg').datagrid('reload'); // reload the user data
                        }});
                }
            }
            function openapplyTraining(trid) {
                
                
                //Get the training detail
                $.ajax({
                    url: "showTrainingDetail.htm?trainingId=" + trid,
                    success: function(result) {
                        $('#training_detail').html(result);
                    }});
                $('#hidTrId').val(trid);
                $('#winApplyTraining').window('open');
            }
            function formatItem(row) {
                var s = '<span style="font-weight:bold">' + row.label + '</span><br/>' +
                        '<span style="color:#228B22">' + row.desc + '</span>';
                return s;
            }

            function updateTrainingOption(trId, empId, trainingOption)
            {
                $.ajax({
                    url: "updateTrainingOption.htm?trainingId=" + trId + '&empId=' + empId + '&trainingOption=' + trainingOption,
                    success: function(result) {
                        $('#mytrainingdg').datagrid('reload'); // reload the user data
                    }});
            }
            function submitTraining() {
                hasSelected = false;
                strIds = '';
                if (document.getElementById('trainingOption').value == '')
                {
                    alert("Please select Training Option.\n(Option 1 for first priority, Option 2 for Second Priority and Option 3 for Third Priority.");
                    document.getElementById('trainingOption').focus();
                    return false;
                }
                if (document.getElementById('frmTraining').mobile.value == '')
                {
                    alert("Please enter Mobile Number.");
                    document.getElementById('frmTraining').mobile.focus();
                    return false;
                }
                if (document.getElementById('frmTraining').email.value == '')
                {
                    alert("Please enter Email.");
                    document.getElementById('frmTraining').email.focus();
                    return false;
                }
                if ($('#department').combobox('getValue') == '')
                {
                    alert("Please select Department.");
                    return false;
                }
                if ($('#office').combobox('getValue') == '')
                {
                    alert("Please select Office.");
                    return false;
                }
                if ($('#post').combobox('getValue') == '')
                {
                    alert("Please select Post.");
                    return false;
                }
                arrTemp1 = $('#post').combobox('getValue').split("|");
                if(arrTemp1[1] == $('#trainingEmpId').val())
                {
                    alert("Please select correct Recommending Authority. You could not apply to yourself.");
                    return false;
                }
                if (!document.getElementById("declaration").checked)
                {
                    alert("Please accept the declaration before applying.");
                    return;
                }

                if (confirm("Are you sure you want to apply for the Training Program?"))
                {
                    $('#frmTraining').form('submit', {
                        url: 'saveTrainingProgram.htm',
                        success: function(result) {
                            //var result = eval('(' + result + ')');
                            //alert(result);
                            if (result.message) {
                                $.messager.show({
                                    title: 'Error',
                                    msg: result.message
                                });
                            } else {
                                //alert('x');
                                $('#department').combobox('clear');
                                $('#office').combobox('clear');
                                $('#post').combobox('clear');
                                $('#winApplyTraining').window('close');
                                $('#trainingdg').datagrid('reload'); // reload the user data
                                $('#mytrainingdg').datagrid('reload'); // reload the user data
                            }
                        }
                    });
                }
            }
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
                location.reload();
                return false;
            }
        </script>
    </head>
    <body>
        <div style="background:#FAFAFA;border:1px solid #DADADA;margin:10px;font-weight:bold;text-align:center;">Please read the User Manual carefully before 
            applying for any of the Training Programs. Manual also includes instructions for Recommending Authority as well.<br /><a href="https://drive.google.com/file/d/0B6DGe7Iwnj3vTEw0MHdhdW9lOWs/view?usp=sharing" target="_blank"><img src="images/download-pdf-icon.png" /></a></div>
        <table id="mytrainingdg" class="easyui-datagrid" style="width:100%;" title="My Training Applications" 
               data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: false" toolbar="#toolbar">
            <thead>
                <tr>
                    <th data-options="field:'programName',width:'20%'" style="font-weight:bold;">Training Program Name</th>
                    <th data-options="field:'venue',width:'15%'" style="font-weight:bold;">Venue</th>
                    <th data-options="field:'fromDate'">From Date</th>
                    <th data-options="field:'toDate'">To Date</th>
                    <th data-options="field:'duration'">Duration</th>
                    <th data-options="field:'strTrainingOption'">Training Option</th>
                    <th data-options="field:'trainingProgramID',width:'10%',formatter:applyTraining,align:'center'">Authority Status</th>
                    <th data-options="field:'isShortlisted',width:'8%',align:'center'">Shortlisted</th>
                    <th data-options="field:'isSelected',width:'8%',align:'center'">Selected</th>
                    <th data-options="field:'withdrawTraining',width:'8%',formatter:withdrawTraining,align:'center'">Withdraw</th>
                </tr> 
            </thead>
        </table>                
        <table id="trainingdg" class="easyui-datagrid" style="width:100%;"  title="Apply for Upcoming Training Programs" 
               data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true" toolbar="#toolbar">
            <thead>
                <tr>
                    <th data-options="field:'programName'" style="font-weight:bold;">Training Program Name</th>
                    <th data-options="field:'venue'" style="font-weight:bold;">Venue</th>
                    <th data-options="field:'fromDate'">From Date</th>
                    <th data-options="field:'toDate'">To Date</th>
                    <th data-options="field:'capacity'">Capacity</th>
                    <th data-options="field:'duration'">Duration</th>
                    <th data-options="field:'trainingProgramID',width:'10%',formatter:applyTraining,align:'center'">Apply</th>                        
                </tr> 
            </thead>
        </table>

        <div id="winApplyTraining" class="easyui-window" title=" Apply Training" style="width:70%;height:500px;padding:10px" closed="true">
            <form id="frmTraining" method="POST" commandName="trainingProgramForm">
                <input type="hidden" name="hidTrId" id="hidTrId"/>
                <input type="hidden" name="trainingEmpId" id="trainingEmpId" value="${trainingEmpId}" />
                <table width="100%" cellpadding="4" cellspacing="1" style="font-size:9pt;" class="custom_table">
                    <tr>
                        <td colspan="2" bgcolor="#EAEAEA" style="padding-left:5px;"><span style="font-size:11pt;font-weight:bold;">Training Program you are applying for</span></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div id="training_detail"></div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" bgcolor="#EAEAEA" style="padding-left:5px;"><span style="font-size:11pt;font-weight:bold;">Personal Information</span></td>
                    </tr>                    
                    <tr style="height: 30px;">
                        <td width="30%">Mobile:</td>
                        <td width="70%">
                            <c:if test="${empty mobile}">
                                <input type="text" id="mobile" name="mobile" style="width:400px;">
                            </c:if>
                            <c:if test="${not empty mobile}">
                                <input type="hidden" id="mobile" name="mobile" value="${mobile}" style="width:400px;">
                                <span style="color:#008900;font-weight:bold;">${mobile}</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 30px;">
                        <td width="30%">Email:</td>
                        <td width="70%">
                            <c:if test="${empty email}">
                                <input type="text" id="email" name="email" style="width:400px;">
                                </c:if>
                            <c:if test="${not empty email}">
                                <input type="hidden" id="email" name="email" value="${email}" style="width:400px;">
                                <span style="color:#008900;font-weight:bold;">${email}</span>
                            </c:if>
                        </td>
                    </tr>                    
                    <tr>
                        <td colspan="2" bgcolor="#EAEAEA" style="padding-left:5px;"><span style="font-size:11pt;font-weight:bold;">Select Recommending Authority</span></td>
                    </tr>                                        
                    <tr style="height: 30px;">
                        <td width="30%">Department:</td>
                        <td width="70%">
                            <input class="easyui-combobox" id="department" name="department" style="width:400px;" data-options="valueField:'deptCode',textField:'deptName',url:'GetTrainingDeptListJSON.htm'">
                        </td>
                    </tr>
                    <tr style="height: 30px;">
                        <td>Office</td>
                        <td>
                            <input class="easyui-combobox" id="office" name="office" style="width:400px;" data-options="valueField:'offCode',textField:'offName'">
                        </td>
                    </tr>
                    <tr>
                        <td>Post:</td>
                        <td>
                            <input class="easyui-combobox" id="post" style="width:400px;" name="post" style="width:50%;" data-options="
                                   valueField: 'value',
                                   textField: 'label',
                                   formatter: formatItem">
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2"><div id="previous_training">
                                <input type="hidden" name="tpids" id='tpids' value="" />
                                <input type="hidden" name="hasSelected" id="hasSelected" value="${hasSelected}" />
                                <input type="hidden" name="optionCount" id="optionCount" value="${optionCount}" />
                            </div></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div id="prevtraining_blk">

                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"><h3 style="font-size:12pt;margin:0px;color:#008900;font-weight:bold;">Declaration</h3><input type="checkbox" id="declaration" name="declaration" value="1" /> 
                            <label for="declaration">I do here by declare that I have never gone through the above Training Program I am applying for.</label></td>
                    </tr>                    
                    <tr style="height: 30px;">
                        <td>&nbsp;</td>
                        <td>
                            <button type="button" onclick="javascript: submitTraining()" class="btn btn-md btn-primary">Apply</button>
                        </td>
                    </tr>                    
                    <tr>
                        <td colspan="2"><span style="color:#FF0000;font-weight:bold;">N.B.: After approval from your Recommending Authority, you will be eligible for shortlisting.</span></td>
                    </tr>

                </table>
            </form>
        </div>
        <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window'" closed="true" style="width:70%;height:600px;padding:5px;">
            <iframe id="winfram"  frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%"></iframe>
        </div>
    </body>
    <script type="text/javascript">
        function applyTraining(val, row)
        {
            
                if (row.status == '')
                {

                    if(row.isExpired == true)
                        str = '<span style="color:#FF0000;font-weight:bold;">CLOSED</span>';
                    else
                    str = '<a href="javascript:void(0)" onclick="openapplyTraining(' + row.trainingProgramID + ')" title="Apply for Training" class="btn btn-sm btn-primary"><strong>Apply</strong></a>';
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
        function withdrawTraining(val, row)
        {
            str = '<a href="javascript:void(0)" onclick="javascript: withdrawTrainingAction(' + row.trainingProgramID + ')" title="Withdraw Application and Apply again" class="btn btn-sm btn-danger"><strong>Withdraw</strong></a>';
            return str;
        }        
    </script>
</html>
