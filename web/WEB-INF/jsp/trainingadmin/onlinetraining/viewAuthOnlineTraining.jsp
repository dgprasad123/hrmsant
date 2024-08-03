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
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <script type="text/javascript" src="js/common.js"></script>
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}
            #training_form td{padding:6px;}
            .form-control{height:30px;}
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#department').combobox({
                    onSelect: function(record) {
                        $('#office').combobox('clear');
                        $('#post').combobox('clear');
                        var url = 'getOfficeListJSON.htm?deptcode=' + record.deptCode;
                        $('#office').combobox('reload', url);
                    }
                });
                $('#office').combobox({
                    onSelect: function(record) {
                        $('#post').combobox('clear');
                        var url = 'getTrainingPostListJSON.htm?offcode=' + record.value;
                        $('#post').combobox('reload', url);
                    }
                });
            });
            function formatItem(row) {
                var s = '<span style="font-weight:bold">' + row.label + '</span><br/>' +
                        '<span style="color:#228B22">' + row.desc + '</span>';
                return s;
            }
            function saveApproval() {
                var status = $('#sltApproveStatus').combobox('getValue');
                if (status == ''){
                    alert("Please select Action.");
                    return false;
                }else{
                    formsubmit();
                }
            }
            function forwardTraining(){
                $('#forwardEmpid').val($('#post').combobox('getValue'));
                formsubmit();
            }
            function formsubmit(){
                $('#frmTraining').form('submit', {
                    url: 'saveOnlineTrainingApprove.htm?taskId=${taskId}',
                    success: function(response) {
                        var result = eval('(' + response + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
                            $('#winApplyTraining').window('close');
                            window.parent.$('#win').window('close');
                        }
                    }
                });
            }
        </script>
    </head>
    <body>
        <h1 style="color:#2C5A83;margin-top:0px;">Online Training Program Request</h1>
        
        <table width="100%" id="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
            <tr style="font-weight:bold;background:#EAEAEA;">
                <td colspan="2">Applicant Detail:</td>
            </tr>
            <tr>
                <td width="180" align="right">Applied By:</td>
                <td><strong>${onlineTrainingBean.empName}, ${onlineTrainingBean.empDesignation}</strong></td>
            </tr>                 
        </table>  
            
        <table width="100%" id="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
            <tr style="font-weight:bold;background:#EAEAEA;">
                <td colspan="4">Online Course Detail:</td>
            </tr>
            <tr>
                <td width="180" align="right">Course Name:</td>
                <td><strong>${onlineTrainingBean.courseName}</strong></td>
                <td width="180" align="right">Institute Name:</td>
                <td><strong>${onlineTrainingBean.instituteName}</strong></td>
            </tr>
            <tr>
                <td width="180" align="right">From Date:</td>
                <td><strong>${onlineTrainingBean.fromDate}</strong></td>
                <td width="180" align="right">To Date:</td>
                <td><strong>${onlineTrainingBean.toDate}</strong></td>
            </tr>                
            <tr>
                <td width="180" align="right">Course Type:</td>
                <td><strong>${onlineTrainingBean.courseType}</strong></td>
                <td width="180" align="right">Date Applied:</td>
                <td><strong>${onlineTrainingBean.dateApplied}</strong></td>
            </tr> 
            <tr>
                <td width="180" align="right">Website:</td>
                <td colspan="3"><strong>${onlineTrainingBean.website}</strong></td>

            </tr>                 
        </table>        

        <form id="frmTraining" action="" method="POST" commandName="TrainingProgramForm">
            <input type="hidden" name="status_id" id="status_id" value="${status}" />
            <input type="hidden" name="opt" id="opt" value="${opt}" />
            
            <table width="100%" id="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                <tr style="font-weight:bold;background:#EAEAEA;">
                    <td colspan="2">Take Action</td>
                </tr>
                <tr>
                    <td width="180" align="right">Action:</td>
                    <td>
                        <input id="sltApproveStatus" name="sltApproveStatus" class="easyui-combobox" data-options="
                               valueField: 'value',
                               textField: 'label',
                               url:'getOnlineTrainingStatusJSON.htm'" />
                    </td>
                </tr>
                <tr>
                    <td width="180" align="right">Note:</td>
                    <td><textarea name="note" id="note" rows="4" cols="50"></textarea></td>
                </tr>                
                <tr>
                    <td></td>
                    <td>
                        <input type="button" value="Submit" class="btn btn-primary btn-sm" onclick="javascript: saveApproval()" />
                    </td>
                </tr>
            </table>

        </form>
    </body>
</html>
