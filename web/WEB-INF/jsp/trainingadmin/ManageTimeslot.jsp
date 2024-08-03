<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Assign Batch Timeslots</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
                <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
                        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
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
            function validateAdd()
            {
                $('#trainingDate').val($('#trainingDate').val().trim());
                $('#roomNumber').val($('#roomNumber').val().trim());
                if ($('#trainingDate').val().trim() == '')
                {
                    alert("Please enter Training Date.");
                    $('#trainingDate')[0].focus();
                    return false;
                }
                
                if ($('#fromHour').val().trim() == '')
                {
                    alert("Please select Start Time (both hour and minute).");
                    $('#fromHour')[0].focus();
                    return false;
                }
                if ($('#fromMinute').val().trim() == '')
                {
                    alert("Please select Start Time (both hour and minute).");
                    $('#fromMinute')[0].focus();
                    return false;
                }
                if ($('#toHour').val().trim() == '')
                {
                    alert("Please select End Time (both hour and minute).");
                    $('#toHour')[0].focus();
                    return false;
                }
                if ($('#toMinute').val().trim() == '')
                {
                    alert("Please select End Time (both hour and minute).");
                    $('#toMinute')[0].focus();
                    return false;
                }
                if(Date.parse('01/01/2011 '+$('#fromHour').val()+':'+$('#fromMinute').val()+':00') >= Date.parse('01/01/2011 '+$('#toHour').val()+':'+$('#toMinute').val()+':00'))
                {
                    alert("End Time must be greater than Start Time.");
                    return false;
                }
                isChecked = false;
                $("input[name='faculties']:checked").each(function (){
                    //strChecked+=(strChecked == '')?$(this).val():(','+$(this).val());
                    isChecked = true;
                });
                if(!isChecked)
                {
                    alert("Please browse and select at least one Faculty Member for the Training Batch.")
                    return false;
                }
                postData = $('#frmSlot').serialize();
                $.ajax({
                    url: 'SaveTimeslot.htm',
                    type: 'post',
                    data: postData,
                    success: function(retVal) {
                        self.location = 'ManageTimeslot.htm?trainingBatchId='+$('#trainingBatchId').val()+'&trainingId='+$('#trainingId').val();
                    }
                });
            }
            function updateFacultyList(facultyCode)
            {
                var strChecked = '';
                var strLabel = '';
                $("input[name='faculties']:checked").each(function (){
                    strChecked+=(strChecked == '')?$(this).val():(','+$(this).val());
                    //alert($('#faculty_label_'+$(this).val())[0].value);
                    strLabel+=(strLabel == '')?$('#faculty_label_'+$(this).val())[0].value:('<br />'+$('#faculty_label_'+$(this).val())[0].value);
                });
                $('#facultyList').val(strChecked);
                $('#faculty_label').html(strLabel);
             }
             function deleteSlot(slotID, batchId, trainingId)
             {
                 if(confirm("Are you sure you want to delete the Timeslot?"))
                 {
$.ajax({
                    url: 'DeleteTimeslot.htm',
                    type: 'get',
                    data: 'slotId='+slotID,
                    success: function(retVal) {
                     self.location = 'ManageTimeslot.htm?trainingBatchId='+batchId+'&trainingId='+trainingId;   
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
                <h1 style="font-size:18pt;margin:0px;">Manage Batch Time Slot</h1>
                <form:form class="form-control-inline" id="frmSlot" action="AddTrainingBatch.htm" method="POST" commandName="TrainingBatchForm" onsubmit="return false;">
                    <input type="hidden" name="trainingBatchId" id="trainingBatchId" value="${trainingBatchId}" />
                    <input type="hidden" name="trainingId" id="trainingId" value="${trainingId}" />
                    <p style="margin-top:10px;"><input type="button" value="&laquo; Back to Previous Page" name="save" class="btn btn-success btn-sm" onclick="javascript: self.location='ManageBatch.htm?trainingId='+$('#trainingId').val()" /></p>
                    <table width="70%" class="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                        <tr style="background:#EAEAEA;font-weight:bold;">
                            <td colspan="4">Add/Edit Timeslot</td>
                        </tr>
                        <tr>
                            <td align="right" width="20%">Date:</td>
                            <td><div class='input-group date' id='trainingDate1'><input type="text" value="" id="trainingDate" name="trainingDate" path="trainingDate1" class="form-control-inline" />
                                    </div>
                            </td>
                            <td align="right" width="20%">Room Number:</td>
                            <td><input type="text" name="roomNumber" id="roomNumber" maxlength="20" size="15" class="form-control-inline" /></td>                            
                        </tr>
                        <tr>
                            <td align="right">Start Time:</td>
                            <td><select name="fromHour" id="fromHour" size="1"  class="form-control-inline">
                                    <option value="">hh</option>
                                    <c:forEach items="${hourList}" var="hList" varStatus="count">
                                        <option value="${hList.value}">${hList.label}</option>
                                    </c:forEach>
                                </select>:<select name="fromMinute" id="fromMinute" size="1"  class="form-control-inline">
                                    <option value="">mm</option>
                                    <c:forEach items="${minuteList}" var="mList" varStatus="count">
                                        <option value="${mList.value}">${mList.label}</option>
                                    </c:forEach>
                                </select></td>
                            <td align="right">End Time:</td>
                            <td><select name="toHour" size="1" id="toHour"  class="form-control-inline">
                                    <option value="">hh</option>
                                    <c:forEach items="${hourList}" var="hList" varStatus="count">
                                        <option value="${hList.value}">${hList.label}</option>
                                    </c:forEach>
                                </select>:<select name="toMinute" size="1" id="toMinute" class="form-control-inline">
                                    <option value="">mm</option>
                                    <c:forEach items="${minuteList}" var="mList" varStatus="count">
                                        <option value="${mList.value}">${mList.label}</option>
                                    </c:forEach>
                                </select></td>
                        </tr>
                        <tr>
                            <td align="right">Assign Faculties:</td>
                            <td colspan="3"><input type="hidden" name="facultyList" id="facultyList" />
                                <input type="button" value="Browse Faculties" data-toggle="modal" data-target="#manageTimeSlotModal"/><br />
                                <span id="faculty_label"></span>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="4">
                            <input type="button" id="btn_save" value="Save Time Slot" name="save" class="btn btn-primary btn-sm" onclick="javascript: validateAdd()" />
                                <span id="loader" style="color:#666666;font-size:9pt;font-style:italic;visibility:hidden;"><img src="images/ajax-loader_1.gif" /> Please wait...</span>
                            </td>
                        </tr> 
                        <tr>
                            <td colspan="4"><div id="result_blk"></div></td>
                        </tr>
                    </table>
                    
                </form:form>
                <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>
                    <tr style="font-weight:bold;background:#FFB9B9;color:#000000;font-size:13pt;">
                        <td colspan="8">Time Slots</td>
                    </tr>
                <tr style="font-weight:bold;background:#0A7CB2;color:#FFFFFF;">
                        <td width="5%">Sl No.</td>
                        <td>Training Program</td>
                        <td>Batch</td>
                        <td>Date</td>
                        <td>From Time</td>
                        <td>To Time</td>
                        <td>Faculties</td>
                        <td width="5%" align="center">Delete</td>
                    </tr>
 <c:forEach items="${timeslotList}" var="tList" varStatus="count">
                    <tr>
                        <td>${count.index + 1}</td>
                        <td>${tList.trainingProgram}</td>
                        <td>${tList.batchName}</td>
                        <td>${tList.trainingDate}</td>
                        <td>${tList.fromTime}</td>
                        <td>${tList.toTime}</td>
                        <td>${tList.faculties}</td>
                        <td align="center"><a href="javascript:void(0)" onclick="javascript: deleteSlot(${tList.timeslotId}, ${trainingBatchId}, ${trainingId})" /><img src="images/delete_icon.png" /></a></td>
                    </tr>                            
                </c:forEach>
            </table>
            </div>
        </div>
        <div id="manageTimeSlotModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Assign Faculty Members</h4>
                        </div>
                        <div class="modal-body">
                            <c:forEach items="${facultyList}" var="fList" varStatus="count">
                                <div><input type="checkbox" value="${fList.facultyCode}" name="faculties" id="faculty_${fList.facultyCode}"  onclick="javascript: updateFacultyList(${fList.facultyCode})" />
                                    <input type="hidden" id="faculty_label_${fList.facultyCode}" value="<strong>${fList.facultyName}</strong>, ${fList.designation}" />
                                    <label for="faculty_${fList.facultyCode}" style="font-weight:normal;"><strong>${fList.facultyName}</strong>, ${fList.designation}</label>
                                </div>
                            </c:forEach>
                              
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary btn-sm" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        <script type="text/javascript">
            $(function() {
                $('#trainingDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            </script>
    </body>
</html>
