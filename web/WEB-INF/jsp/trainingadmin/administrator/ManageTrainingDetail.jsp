<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Training Calendar:: HRMS</title>
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
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
            .tblres td{padding:5px;}
            #training_form td{padding:4px;}
            .form-control{height:30px;}
        </style>
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="MANAGE_TRAINING" />
        </jsp:include>
        <h1 style="margin:0px;font-size:18pt;color:#777777;border-bottom:1px solid #DADADA;padding-bottom:5px;">Manage Participants</h1>
        <p style='margin:0px;margin-top:10px;'><input type="button" value="&laquo; Back to Active Training List" class="btn btn-primary btn-md" onclick="self.location='TAManageTrainingList.htm'" /></p>
<form class="form-control-inline" name="frmTraining"  id="frmTraining" action="SaveApplicants.htm" method="POST" commandName="ApplicantForm" onsubmit="javascript: return saveApplicants()">
            <table width="100%" id="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;" align="center">
                <tr style="font-weight:bold;background:#FAFAFA;">
                    <td colspan="4" style="border-bottom:1px solid #CCCCCC;">Training Program Detail:</td>
                </tr>
                <tr>
                    <td colspan="4">
                        <span style="color:#156F9B;font-size:12pt;font-weight:bold;">${TrainingProgramForm.trainingProgram}</span>
                        <strong style="color:#000000;font-size:10pt;">At</strong>  <span style="color:#890000;font-size:11pt;font-weight:bold;">${TrainingProgramForm.instituteName}, ${TrainingProgramForm.venueName}</span><br />
                        <strong style="color:#156F9B;font-size:10pt;">From</strong> ${TrainingProgramForm.fromDate} to ${TrainingProgramForm.toDate} for ${TrainingProgramForm.duration} Days<br />
                        <!--<strong style="color:#156F9B;font-size:10pt;">Sponsored by</strong> ${TrainingProgramForm.sponsorName}<br />
                        <strong style="color:#156F9B;font-size:10pt;">Resource Persons:</strong> ${TrainingProgramForm.facultyName}<br />-->
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
           <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>
            <tr style='font-weight:bold;background:#0D508E;color:#FFFFFF;'>
                <td>Sl No.</td>
                <td>Participant Name</td>
                <td>Status</td>
                <td width="100">Previous Training</td>
            </tr>
            <c:forEach items="${participantList}" var="pl" varStatus="cnt">
                <tr style='background:#FFFFFF'>
                    <td><input type="checkbox" value="${pl.participantId}" <c:if test="${pl.status=='SHORTLISTED'}"> checked="checked"</c:if> name="participants" /></td>
                    <td>${pl.participantName}</td>
                    <td>${pl.status}</td>
                    <td><a href="TAViewPreviousTraining.htm?empId=${pl.empId}&trainingId=${trainingId}" title="View Previous Training"><img src="images/view_detail_icon.png" alt="View Previous Training" width="20" /></a></td>
                </tr>
            </c:forEach>
           </table>
            <p style='margin-top:10px;'><input type="submit" value="Save Selected Applicants" name="close1" class="btn btn-primary btn-md" style='background:#008900;' /></p>
        </form>
              
 </div>
</div>
</body>
</html>