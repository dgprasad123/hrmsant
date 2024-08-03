<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Assign Batch Participants</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            .training_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
            table {border:1px solid #DADADA;}
            table td{padding:5px;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
        </style>
        <script type="text/javascript">
            function searchEmployee()
            {
                hrmsId = $('#hrmsId').val();
                gpfNumber = $('#gpfNumber').val();
                $('#loader').css('visibility', 'visible');
                $('#btn_search')[0].disabled = true;
                $.ajax({
                  url: 'SearchBatchEmployee.htm',
                  type: 'get',
                  data: 'gpfNumber='+$('#gpfNumber').val()+'&empId='+$('#empId').val(),
                  success: function(retVal) {
                       $('#result_blk').html(retVal); 
                       $('#loader').css('visibility', 'hidden');
                       $('#btn_search')[0].disabled = false;
                  }
                });
            }
            function addParticipant(empId, empName, designation)
            {
                $.ajax({
                  url: 'SaveBatchEmployee.htm',
                  type: 'get',
                  data: 'designation='+designation+'&empName='+empName+'&empId='+empId+'&trainingId='+$('#trainingId').val()+'&trainingBatchId='+$('#trainingBatchId').val(),
                  success: function(retVal) {
                      if(retVal == 'duplicate')
                      {
                          alert("Error: Participant is already been assigned to this Batch!");
                      }
                      self.location = 'AssignBatchEmployee.htm?batchId='+$('#trainingBatchId').val()+'&trainingId='+$('#trainingId').val();
                  }
                });
            }
            function deleteParticipant(trainingBatchId, empId)
            {
                if(confirm("Are you sure you want to delete the Participant from the Batch?"))
                {
                $.ajax({
                    url: 'DeleteBatchParticipant.htm',
                    type: 'get',
                    data: 'opt=delete&trainingBatchId='+trainingBatchId+'&empId='+empId,
                    success: function(retVal) {
                        self.location = 'AssignBatchEmployee.htm?batchId='+$('#trainingBatchId').val()+'&trainingId='+$('#trainingId').val();
                    }
                });

                }
            }
            </script>
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="TRAINING_CALENDAR" />
        </jsp:include>
                <h1 style="font-size:18pt;margin:0px;">Manage Batch Participants</h1>
                <form:form class="form-control-inline"  action="AddTrainingBatch.htm" method="POST" commandName="TrainingBatchForm" onsubmit="return false;">
                    <input type="hidden" name="trainingBatchId" id="trainingBatchId" value="${trainingBatchId}" />
                    <input type="hidden" name="trainingId" id="trainingId" value="${trainingId}" />
                    <p style="margin-top:10px;"><input type="button" value="&laquo; Back to Previous Page" name="save" class="btn btn-success btn-sm" onclick="javascript: self.location='ManageBatch.htm?trainingId='+$('#trainingId').val()" /></p>
                    <table width="100%" class="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                        <tr style="background:#EAEAEA;font-weight:bold;">
                            <td colspan="2">Quick Search Tool</td>
                        </tr>
                        <tr>
                            <td align="right" width="20%">GPF Number:</td>
                            <td><input type="text" name="gpfNumber" id="gpfNumber" maxlength="20" size="15" class="form-control-inline" />
                                Or HRMS ID: <input type="text" name="empId" id="empId" maxlength="10" size="15" class="form-control-inline" />
                            <input type="button" id="btn_search" value="Search" name="save" class="btn btn-primary btn-sm" onclick="javascript: searchEmployee()" />
                                <span id="loader" style="color:#666666;font-size:9pt;font-style:italic;visibility:hidden;"><img src="images/ajax-loader_1.gif" /> Please wait...</span></td>
                        </tr> 
                        <tr>
                            <td colspan="2"><div id="result_blk"></div></td>
                        </tr>
                    </table>
                </form:form>
                <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>
                    <tr style="font-weight:bold;background:#FFB9B9;color:#000000;font-size:13pt;">
                        <td colspan=4">Assigned Participants</td>
                    </tr>
                <tr style="font-weight:bold;background:#0A7CB2;color:#FFFFFF;">
                        <td width="5%">Sl No.</td>
                        <td>Participant Name</td>
                        <td>Designation</td>
                        <td width="5%" align="center">Delete</td>
                    </tr>
                <c:forEach items="${batchParticipantList}" var="bpList" varStatus="count">
                    <tr>
                        <td>${count.index + 1}</td>
                        <td>${bpList.participantName}</td>
                        <td>${bpList.designation}</td>
                        <td align="center"><a href="javascript:void(0)" onclick="javascript: deleteParticipant(${bpList.trainingProgramBatchId}, '${bpList.empId}')" /><img src="images/delete_icon.png" /></a></td>
                    </tr>                            
                </c:forEach>
            </table>
            </div>
        </div>
               
    </body>
</html>
