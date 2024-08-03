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
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
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
            $(document).ready(function() {
                
                if ($('#status_id').val() == 'success')
                {
                    window.parent.closeIframe();
                }
                if ($('#status_id').val() == 'update_success')
                {

                    self.location = 'ManageTrainingProgramAction.htm?opt=list&date=' + $('#programDate').val();
                }            
                $('#venueId').combobox('setValue',${venueId});
                $('#fromDate').datebox('setValue','${TrainingProgramForm.fromDate}');
                $('#toDate').datebox('setValue','${TrainingProgramForm.toDate}');
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
                        var url = 'getTrainingPostListJSON.htm?offcode=' + record.offCode;
                        $('#post').combobox('reload', url);
                    }
                });                
            });
            function getExt(filename)
            {
                var ext = filename.split('.').pop();
                if (ext == filename)
                    return "";
                return ext;
            }
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
            function validateAdd()
            {
                $('#trainingProgram').val($('#trainingProgram').val().trim());
                if ($('#trainingProgram').val() == '')
                {
                    alert("Please enter Training Program.");
                    $('#trainingProgram').focus();
                    return false;
                }
                            
                           
                if ($('#fromDate').combobox('getValue') == '')
                {
                    alert("Please select From Date.");
                    return false;
                }
                if ($('#toDate').combobox('getValue') == '')
                {
                    alert("Please select To Date.");
                    return false;
                }
                arrTemp1 = $('#fromDate').combobox('getValue').split('-');
                arrTemp2 = $('#toDate').combobox('getValue').split('-');
                newFromDate = arrTemp1[1] + ' ' + arrTemp1[0] + ', ' + arrTemp1[2];
                newToDate = arrTemp2[1] + ' ' + arrTemp2[0] + ', ' + arrTemp2[2];
                var today = new Date();
                /*if(Date.parse(newFromDate) < Date.parse(today))
                {
                    alert("Program From Date must be greater than or equals to current date.");
                    return false;
                }
               
                if (Date.parse(newFromDate) > Date.parse(newToDate))
                {
                    alert("From Date must be less than To Date.")
                    return false;
                }   */             
                if ($('#venueId').combobox('getValue') == '')
                {
                    alert("Please select venue.");
                    return false;
                }
                if ($('#capacity').val() == '' || isNaN($('#capacity').val()) || $('#capacity').val() == 0)
                {
                    alert("Please enter a valid number for Capacity. Must be greater than zero.");
                    return false;
                }
                if ($('#trainingAuthority').val() == '')
                {
                    alert("Please select Training Authority.");
                    return false;
                }                
                if($('#documentFile').val())
                {
                    ext = getExt($('#documentFile').val());
                    if(ext != 'pdf')
                    {
                        alert("Please attach PDF files only.");
                        return false;
                    }
                }
                

            }
            function deleteDocument(trainingId)
            {
                if(confirm("Are you sure you want to delete the Document File?"))
                {
            	$.ajax({
	  url: 'deleteDocument.htm',
	  type: 'get',
          data: 'trainingId='+trainingId,
	  success: function(retVal) {
              alert('Document has been deleted successfully.');
	  }
	});                    
                }
            }
                        function openWindow() {
                $('#winApplyTraining').window('open');
            }
            function formatItem(row) {
                var s = '<span style="font-weight:bold">' + row.label + '</span><br/>' +
                        '<span style="color:#228B22">' + row.desc + '</span>';
                return s;
            }
            function selectAuthority(){
                $('#strTrainingAuthority').val($('#post').combobox('getText') +', '+$('#office').combobox('getText'));
                arrTemp = $('#post').combobox('getValue').split('|');
                $('#trainingAuthority').val($('#post').combobox('getValue'));
                $('#winApplyTraining').window('close');
            }
        </script>
    </head>
    <body>
        <form class="form-control-inline"  id="frmTraining" action="TrainingProgramAction.htm" method="POST" commandName="TrainingProgramForm" onsubmit="javascript: return validateAdd()" enctype="multipart/form-data">
            <input type="hidden" name="emp_hrms_id" id="emp_hrms_id" value="" />
            <input type="hidden" name="status_id" id="status_id" value="${status}" />
            <input type="hidden" name="opt" id="opt" value="${opt}" />
            <input type="hidden" name="programDate" id="programDate" value="${programDate}" />
            <input type="hidden" name="trainingId" id="trainingId" value="${TrainingProgramForm.trainingId}" />
            <table width="100%" id="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                <tr style="font-weight:bold;background:#EAEAEA;">
                    <td colspan="2">Please fill out all the information</td>
                </tr>
                <tr>
                    <td align="right" width="25%"><strong>Training Program:</strong></td>
                    <td><input type="text" name="trainingProgram" id="trainingProgram" value="${TrainingProgramForm.trainingProgram}" size="50" class="form-control-inline" /></td>
                </tr> 
                <tr>
                    <td align="right"><strong>From Date:</strong></td>
                    <td><input class="easyui-datebox" id="fromDate" name="fromDate" style="width:150px" data-options="required:true,formatter:myformatter,parser:myparser" editable="false">
                        <strong>To Date:</strong>
                        <input class="easyui-datebox" id="toDate" name="toDate" style="width:150px" data-options="required:true,formatter:myformatter,parser:myparser" editable="false"></td>
                </tr>
                <tr>
                    <td align="right"><strong>Venue:</strong></td>
                    <td><input class="easyui-combobox" id="venueId" name="venueId" style="width:50%;height:26px;" data-options="valueField:'venueCode',textField:'venueName',url:'getVenueList.htm'"></td>
                </tr>
                <tr>
                    <td align="right"><strong>Capacity:</strong></td>
                    <td><input id="capacity" name="capacity" value="${TrainingProgramForm.capacity}" style="width:20%;height:26px;"></td>
                </tr>                
                <tr>
                    <td align="right"><strong>Attach Training Document (if any):</strong></td>
                    <td><input type="file" id="documentFile" name="documentFile" style="width:50%;height:26px;" class="form-control-inline" />
                        <span style="color:#777777;font-size:8pt;font-style:italic;">(Only PDF files allowed)</span><br />
                    ${strFile}</td>
                </tr>  
                <tr>
                    <td align="right"><strong>Training Authority:</strong></td>
                    <td><input id="strTrainingAuthority" name="strTrainingAuthority" value="${TrainingProgramForm.strTrainingAuthority}" readonly="readonly" style="width:50%;height:26px;">
                        <input type="button" value="Browse" onclick="javascript: openWindow()" />
                        <input type="hidden" name="trainingAuthority" id="trainingAuthority" value="${TrainingProgramForm.trainingAuthority}" /></td>
                </tr>                
                <!--<tr>
                    <td align="right"><strong>Faculty:</strong></td>
                    <td><div id="faculty_govt" style="display:none"><span id="govt_faculty"></span>
                            <a href="javascript:void(0)" id="change" onclick="changepost()"><button type="button">Browse Government Officials</button></a>
                        </div>
                        <div id="faculty_other" style="display:none"><a href="javascript:void(0)" id="change" onclick="javascript: selectFaculty()"><button type="button">Browse Faculties</button></a></div></td>
                </tr>-->
                <tr>
                    <td align="right"><strong>Archived?:</strong></td>
                            <td><select name="isArchived" id="isArchived" size="1">
                                    <option value="N">No<option>
                                        <option value="Y">Yes<option>
                        </select></td>
                </tr>                
                <tr>
                    <td></td>
                    <td><input type="submit" value="Save Training Program" name="save" class="btn btn-primary btn-sm" />
                        <input type="button" value="Cancel" name="close" class="btn btn-primary btn-sm" onclick="javascript: window.parent.closeIframe();" />
                    </td>
                </tr>

            </table>
        </form>
<div id="winApplyTraining" class="easyui-window" title="SelectAuthority" style="width:600px;height:300px;padding:10px" closed="true">
            <form id="frmTraining1" method="POST" commandName="authorityForm">
                <table width="100%" cellpadding="0" cellspacing="0" style="font-size:9pt;">
                    <tr style="height: 30px;">
                        <td width="30%">Department:</td>
                        <td width="70%">
                            <input class="easyui-combobox" id="department" name="department" style="width:400px;" data-options="valueField:'deptCode',textField:'deptName',url:'getDeptListJSON.htm'">
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
                    <tr style="height: 30px;">
                        <td>&nbsp;</td>
                        <td>
                            <button type="button" onclick="javascript: selectAuthority()">Save & Exit</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>                
    </body>
</html>
