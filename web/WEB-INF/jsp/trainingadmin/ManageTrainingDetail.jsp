<%-- 
    Document   : ManageTrainingDetail
    Created on : 13 Jan, 2017, 10:53:52 AM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shortlist Applicants</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <script type="text/javascript" src="js/common.js"></script>
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}
            #training_form td{padding:4px;}
            .form-control{height:30px;}
        </style>       
        <script type="text/javascript">
            $(document).ready(function() {

                $('#dg').datagrid({
                    url: "GetAppliedParticipantList.htm?trainingId=" + $('#trainingId').val()
                });
            });
            function saveApplicants()
            {
                isSelected = false;
                if (obj.participants.checked)
                {
                    isSelected = true;
                }
                else
                {
                    for (var i = 0; i < obj.elements['participants'].length; i++)
                    {
                        if (obj.elements['participants'][i].checked)
                        {
                            isSelected = true;
                            i = obj.elements['participants'].length;
                        }
                    }
                }
                if (!isSelected)
                {
                    alert("Please select at least one Participant.");
                    return false;
                }
                if (!confirm("Are you sure you want to shortlist the selected Applicants?\nOnce shortlisted it could not be reverted back."))
                {
                    return false;
                }
            }
        </script>          
    </head>
    <body>
        <h1>Shortlist Applicants</h1>

        <form class="form-control-inline" name="frmTraining"  id="frmTraining" action="SaveApplicants.htm" method="POST" commandName="ApplicantForm" onsubmit="javascript: return saveApplicants()">
            <table width="100%" id="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;" align="center">
                <tr style="font-weight:bold;background:#FAFAFA;">
                    <td colspan="4">Training Program Detail:</td>
                </tr>
                <tr>
                    <td colspan="4">
                        <span style="color:#156F9B;font-size:12pt;font-weight:bold;">${TrainingProgramForm.trainingProgram}</span><br />
                        <strong style="color:#156F9B;font-size:10pt;">At</strong>  <span style="color:#890000;font-size:11pt;font-weight:bold;">${TrainingProgramForm.instituteName}, ${TrainingProgramForm.venueName}</span><br />
                        <strong style="color:#156F9B;font-size:10pt;">From</strong> ${TrainingProgramForm.fromDate} to ${TrainingProgramForm.toDate} for ${TrainingProgramForm.duration} Days<br />
                        <strong style="color:#156F9B;font-size:10pt;">Sponsored by</strong> ${TrainingProgramForm.sponsorName}<br />
                        <strong style="color:#156F9B;font-size:10pt;">Resource Persons:</strong> ${TrainingProgramForm.facultyName}<br />
                        <strong style="color:#156F9B;font-size:10pt;">Capacity:</strong> <span style="color:#008900;font-size:12pt;font-weight:bold;">${TrainingProgramForm.capacity}</span>
                        <strong style="color:#156F9B;font-size:10pt;">Total Applied:</strong> <span style="color:#FF0000;font-size:12pt;font-weight:bold;">${TrainingProgramForm.numApplied}</span>
                        <strong style="color:#156F9B;font-size:10pt;">Applicants Shortlisted:</strong> <span style="color:#FF0000;font-size:12pt;font-weight:bold;">0</span><br />
                        <strong>Selection Indicator: </strong>
                        <span style="display:block;width:200px;height:20px;border:1px solid #CCCCCC;text-align:center;color:#888888;position:relative;">${TrainingProgramForm.numShortlisted} out of ${TrainingProgramForm.capacity}
                            <span style="position:absolute;left:0px;top:0px;height:18px;background:#00FF00;display:block;width:${width}px;"></span></span>
                    </td>
                </tr>

            </table>  
            <input type="hidden" name="trainingId" id="trainingId" value="${trainingId}" />
            <table id="dg" class="easyui-datagrid" style="width:100%;height:500px;" title="Participants" 
                   data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true, rowStyler: function(index,row){
                    if (row.status == 'SHORTLISTED'){
                        return 'background-color:#DBF0FF;font-weight:bold;';
                    }
                }" toolbar="#toolbar">
                <thead>
                    <tr>
                        <th data-options="field:'participantId',formatter: showCheckbox" style="font-weight:bold;"></th>
                        <th data-options="field:'participantName',width:'80%'" style="font-weight:bold;">Participant Name</th>
                        <th data-options="field:'status',formatter: checkStatus">Status</th>
                    </tr> 
                </thead>
            </table>
            <p style='margin:8px;margin-top:0px;'><input type="submit" value="Save Selected Applicants" name="close1" class="btn btn-primary btn-md" style='background:#008900;' /></p>
        </form>
        <script type="text/javascript">
            var obj = document.frmTraining;
            function showCheckbox(val, row)
            {
                str1 = '';
                if (row.status == 'AGREED' || row.status == 'SHORTLISTED')
                {
                    str1 = '<input type="checkbox" value="' + row.participantId + '"' + ((row.status == 'SHORTLISTED') ? ' checked="checked"' : '') + ' name="participants" />';
                }
                return str1;
            }
            function checkStatus(val, row)
            {
                str1 = '';
                if (row.status == 'AGREED')
                    str1 = '<span style="color:#008900;font-weight:bold;">' + row.status + '</span>';
                else if (row.status == 'SHORTLISTED')
                {
                    str1 = '<span style="color:#890000;font-weight:bold;">' + row.status + '</span>';
                }
                else
                    str1 = '<span style="color:#FF0000;font-weight:bold;">' + row.status + '</span>';
                return str1;
            }
        </script>
    </body>
</html>
