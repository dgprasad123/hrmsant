<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Batch</title>

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
            function validateAdd(trainingId)
            {
                $('#batchName').val($('#batchName').val().trim());
                $('#capacity').val($('#capacity').val().trim());
                if ($('#batchName').val().trim() == '')
                {
                    alert("Please enter Batch Name.");
                    $('#batchName')[0].focus();
                    return false;
                }
                if ($('#capacity').val().trim() == '')
                {
                    alert("Please enter Capacity.");
                    $('#capacity')[0].focus();
                    return false;
                }
                if (isNaN($('#capacity').val().trim()))
                {
                    alert("Please enter a valid integer.");
                    $('#capacity')[0].focus();
                    $('#capacity')[0].select();
                    return false;
                }                
                $.ajax({
                    url: 'SaveBatch.htm',
                    type: 'get',
                    data: 'opt=add&batchName='+$('#batchName').val()+'&trainingId='+trainingId+'&capacity='+$('#capacity').val(),
                    success: function(retVal) {
                        self.location = 'ManageBatch.htm?trainingId='+trainingId;
                    }
                });
            }
            function deleteBatch(batchId)
            {
                if(confirm("Are you sure you want to delete the Batch?"))
                {
                $.ajax({
                    url: 'DeleteBatch.htm',
                    type: 'get',
                    data: 'opt=delete&trainingBatchId='+batchId,
                    success: function(retVal) {
                        self.location = 'ManageBatch.htm?trainingId='+$('#trainingId').val();
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
                <h1 style="font-size:18pt;margin:0px;">Manage Batch</h1>
                <form:form class="form-control-inline"  action="AddTrainingBatch.htm" method="POST" commandName="TrainingBatchForm" onsubmit="return false;">
                    <input type="hidden" name="tpBatchId" id="tpBatchId" value="0" />
                    <input type="hidden" name="trainingId" id="trainingId" value="${trainingId}" />
                    <input type="hidden" name="opt" id="opt" value="" />
                    <p style="margin-top:10px;"><input type="button" value="&laquo; Back to Training Calendar" class="btn btn-success btn-sm" onclick="javascript: self.location='TrainingCalendarList.htm'" /></p>
                    <table width="100%" class="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                        <tr>
                            <td align="right" width="10%">Batch Name:</td>
                            <td><input type="text" name="batchName" id="batchName" maxlength="100" size="50" class="form-control-inline" />
                                Capacity: <input type="text" name="capacity" id="capacity" maxlength="5" size="10" class="form-control-inline" />
                            <input type="button" value="Save Batch" name="save" class="btn btn-primary btn-sm" onclick="return validateAdd(${trainingId})" />
                                <input type="button" value="Cancel" name="close" class="btn btn-primary btn-sm" onclick="self.location = 'ManageBatch.htm'" /></td>
                        </tr> 
                    </table>
                </form:form>
                <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>
                    <tr style="font-weight:bold;background:#0A7CB2;color:#FFFFFF;">
                        <td width="5%">Sl No.</td>
                        <td>Batch Name</td>
                        <td width="5%">Capacity</td>
                        <td width="15%">Assign Employee</td>
                        <td width="7%" align="center">Time Slot</td>
                        <td width="5%" align="center">Delete</td>
                    </tr>
                <c:forEach items="${batchList}" var="bList" varStatus="count">
                    <tr>
                        <td>${count.index + 1}</td>
                        <td>${bList.batchName}</td>
                        <td>${bList.capacity}</td>
                        <td align="center"><a href="AssignBatchEmployee.htm?batchId=${bList.trainingProgramBatchId}&trainingId=${trainingId}">Assign</a></td>
                        <td align="center"><a href="ManageTimeslot.htm?trainingBatchId=${bList.trainingProgramBatchId}&trainingId=${trainingId}" title="Manage Timeslots" /><img src="images/clock_icon.png" width="18" /></a></td>
                        <td align="center"><a href="javascript:void(0)" onclick="javascript: deleteBatch(${bList.trainingProgramBatchId})" /><img src="images/delete_icon.png" /></a></td>
                    </tr>                            
                </c:forEach>
            </table>
            </div>
        </div>
               
    </body>
</html>
