<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Applications</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            .institute_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
            table {border:1px solid #DADADA;}
            .tblres td{padding:5px;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
        </style>
        <script type="text/javascript">
            function searchResults()
            {
                self.location = 'OnlineApplications.htm?institute=' + $('#institute').val() + '&appType=' + $('#applicationType').val()+'&status='+$('#applicationStatus').val();
            }
            function updateStatus(appStatus, applicationId)
            {
                $.ajax({
                    url: 'updateApplicationStatus.htm',      
                    type: 'get',
                    data: 'applicationId='+applicationId+'&status='+appStatus,
                    success: function(retVal) {

                    }
                });

            }
        </script>
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="APPLICATIONS" />
        </jsp:include>
        <div style="width:100%;margin:0px auto;"><h1 style="font-size:18pt;margin:0px;margin-bottom:10px;">Manage Online Applications</h1>
            <p><strong>Browse By:</strong>
                <select name="institute" id="institute" size="1" onchange="javascript: searchResults();">
                    <option value="">-All Institutes-</option>
                    <c:forEach items="${instituteList}" var="iList">
                        <option value="${iList.instituteId}"<c:if test = "${iList.instituteId == institute}"> selected="selected"</c:if>>${iList.instituteName} (${iList.courseName})</option>
                    </c:forEach>                                    
                </select>
                <select name="applicationType" id="applicationType" size="1" onchange="javascript: searchResults();">
                    <option value="">-All Applications-</option>
                    <option value="Receipt"<c:if test = "${applicationType == 'Receipt'}"> selected="selected"</c:if>>Applications with Money Receipt</option>    
                    <option value="Certificate"<c:if test = "${applicationType == 'Certificate'}"> selected="selected"</c:if>>Applications with Certificates</option>    
                    <option value="WOMRC"<c:if test = "${applicationType == 'WOMRC'}"> selected="selected"</c:if>>Applications without Receipt/Certificates</option>    
                    </select>
                    <select name="applicationStatus" id="applicationStatus" size="1" onchange="javascript: searchResults();">
                        <option value="">-All Staus-</option>
                        <option value="PENDING"<c:if test = "${applicationType == 'PENDING'}"> selected="selected"</c:if>>Pending</option>    
                    <option value="APPROVED"<c:if test = "${applicationType == 'APPROVED'}"> selected="selected"</c:if>>Approved</option>    
                    <option value="REIMBURSED"<c:if test = "${applicationType == 'REIMBURSED'}"> selected="selected"</c:if>>Reimbursed</option>    
                    </select>                    
                </p>
                <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>
                    <tr style="font-weight:bold;background:#EAEAEA;">
                        <td>Sl#</td>
                        <td>Employee</td>
                        <td>Institute</td>
                        <td>Course Name</td>
                        <td>From Date</td>
                        <td>To Date</td>
                        <td>Course Fee</td>
                        <td>Date Applied</td>
                        <td align="center">Documents Attached</td>
                        <td align="center">Status</td>
                    </tr>
                <c:forEach items="${trainingList}" var="list" varStatus="cnt">
                    <tr>
                        <td>${cnt.index+1}</td>
                        <td><strong style="color:#333333;">${list.empName}</strong><br /><span style="font-size:8pt;color:#008900;">${list.empDesignation}</span><br />
                            <p style="margin-top:10px;margin-bottom:2px;"><span style="color:#888888;font-style:italic;">Authority Status:</span> 
                                <c:if test = "${list.applicationStatus eq 'PENDING'}"><span style="color:#0008B9;font-weight:bold;">${list.applicationStatus}</span></c:if>
                                <c:if test = "${list.applicationStatus eq 'REJECTED'}"><span style="color:#890000;font-weight:bold;">${list.applicationStatus}</span></c:if>
                                <c:if test = "${list.applicationStatus eq 'APPROVED'}"><span style="color:#008900;font-weight:bold;">${list.applicationStatus}</span></c:if>
                        </td>
                        <td>${list.instituteName}</td>
                        <td>${list.courseName}</td>
                        <td>${list.fromDate}</td>
                        <td>${list.toDate}</td>                        
                        <td>${list.txtAmount}</td>
                        <td>${list.dateApplied}</td>
                        <td align="center">
                            <c:if test = "${empty list.certificatePath && empty list.receiptPath}"><span style="color:#FF0000;">N/A</span></c:if>
                            <c:if test = "${!empty list.intimationPath}"><a href="downloadOnlinePdf.htm?trainingId=${list.onlineTrainingId}&fileType=intimation" target="_blank" style="color:#008900;">Download Intimation Letter</a><br /></c:if>
                            <c:if test = "${!empty list.certificatePath}"><a target="_blank" href="downloadOnlinePdf.htm?trainingId=${list.onlineTrainingId}&fileType=certificate" style="color:#008900;">Download Certificate</a><br /></c:if>
                            <c:if test = "${!empty list.receiptPath}"><a href="downloadOnlinePdf.htm?trainingId=${list.onlineTrainingId}&fileType=receipt" target="_blank" style="color:#008900;">Download Money Receipt</a><br /></c:if>
                            <c:if test = "${!empty list.bankPath}"><a href="downloadOnlinePdf.htm?trainingId=${list.onlineTrainingId}&fileType=bank" target="_blank" style="color:#008900;">Download Bank Account Details</a></c:if>
                        </td>
                        <td><select name="applicationStatus" id="applicationStatus" size="1" onchange="javascript: updateStatus(this.value, ${list.onlineTrainingId});">
                                <option value="PENDING"<c:if test = "${list.applicationStatus == 'PENDING'}"> selected="selected"</c:if>>PENDING</option>    
                                <option value="APPROVED"<c:if test = "${list.applicationStatus == 'APPROVED'}"> selected="selected"</c:if>>APPROVED</option>    
                                <option value="REIMBURSED"<c:if test = "${list.applicationStatus == 'REIMBURSED'}"> selected="selected"</c:if>>REIMBURSED</option>    
                                </select>  </td>
                        </tr>                    
                </c:forEach>  
            </table>         



        </div>
    </div>
</div>  
</body>
</html>