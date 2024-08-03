<%-- 
    Document   : AdminEmployeeGrievanceDtls
    Created on : Jan 15, 2018, 11:39:15 AM
    Author     : Manas 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .blank h2 {
                color: #68ae00;
                font-family: "Carrois Gothic",sans-serif;
                font-size: 2em;
                margin-bottom: 1em;
            }
        </style>
        <script type="text/javascript" src="js/tinymce/tinymce.js"></script>
        <script type="text/javascript">
            tinymce.init({
                selector: 'textarea',
                height: 300,
                menubar: false,
                plugins: [
                    'advlist autolink lists link image charmap print preview anchor',
                    'searchreplace visualblocks code fullscreen',
                    'insertdatetime media table contextmenu paste code'
                ],
                toolbar: 'undo redo | insert | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
                content_css: [
                    '//fonts.googleapis.com/css?family=Lato:300,300i,400,400i',
                    '//www.tinymce.com/css/codepen.min.css']
            });
        </script>
    </head>
    <body>
        <div class="inner-block" style="padding: 5px 15px 5px 15px;">
            <div class="blank" align="center">
                <h2>Grievance Details</h2>
                <table align="center" style="width:1000px" class="table table-bordered table-hover">
                    <thead>
                        <tr bgcolor="#1A2903" style="color:#FFFFFF;font-weight:bold;">
                            <td align="center" colspan="2">Grievance Details</td>
                        </tr> 
                    </thead>
                    <tbody>
                        <tr>						
                            <td width="200"><b>HRMSID:</b></td>
                            <td>${grievanceDetail.hrmsid}</td>
                        </tr>
                        <tr>
                            <td><b>EMP Name:</b></td>
                            <td>${grievanceDetail.fullname}</td>
                        </tr>
                        <tr>						
                            <td><b>Applied From:</b> </td>
                            <td>${grievanceDetail.source}</td>
                        </tr>
                        <tr>						
                            <td><b>Category:</b> </td>
                            <td>${grievanceDetail.category}</td>
                        </tr>
                        <tr>						
                            <td><b>Grievance Date Time:</b> </td>
                            <td>${grievanceDetail.grievanceTime}</td>
                        </tr>
                        <tr>						
                            <td><b>Grievance:</b> </td>
                            <td><p>${grievanceDetail.grievanceDetail}</p></td>
                        </tr>
                        <c:if test="${not empty grievanceDetail.attachementName}">
                            <tr>						
                                <td><b>Attachment:</b> </td>
                                <td><p><a target="_blank" href="downloadAttchment.htm?attachementId=${grievanceDetail.attachementId}">${grievanceDetail.attachementName}</a></p></td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
                <table align="center" style="width:1000px" class="table table-bordered table-hover">
                    <thead>
                        <tr bgcolor="#1A2903" style="color:#FFFFFF;font-weight:bold;">
                            <td align="center" colspan="6">All Communications in this Grievance</td>
                        </tr>
                        <tr bgcolor="#3E6A00" style="color:#FFFFFF;font-weight:bold;">
                            <td>#</td>
                            <td>From</td>
                            <td>To</td>
                            <td>Remarks</td>
                            <td>Time</td>
                            <td>Attachment</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${communicationList}" var="communication" varStatus="cnt">
                            <tr bgcolor="#FFFFFF">
                                <td>${cnt.index + 1}</td>
                                <td>${communication.fromEmployee}</td>
                                <td>${communication.toEmployee}</td>
                                <td><p>${communication.remark}</p></td>
                                <td>${communication.commTime}</td>
                                <td align="center">
                                    <c:if test="${not empty communication.attachementName}">
                                        <a target="_blank" href="downloadAttchment.htm?attachementId=${communication.attachementId}">${communication.attachementName}</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>                        
                    </tbody>
                </table>
                <c:if test="${(grievanceDetail.isforwarded != 'Y') and (grievanceDetail.isdisposed != 'Y') and (grievanceDetail.isrejected != 'Y')}">
                    <form:form action="adminGrievanceTakeAction.htm" commandName="GrievnceCommunicationForm" method="post"  enctype="multipart/form-data">
                        <table class="table table-bordered table-hover" style="width:1000px" align="center">					
                            <tr>						
                                <td colspan="2">Your Remarks<b>:</b> </td>

                            </tr>
                            <tr>						
                                <td  align="left" colspan="2">
                                    <form:hidden path="gid"/>
                                    <form:textarea path="remark" cols="100" rows="10"/>
                                </td>
                            </tr>
                            <tr>						
                                <td  align="left">Attach document(s) if any?:</td>
                                <td><input type="file" name="grivAttachment"/></td>
                            </tr>
                            <tr>						
                                <td align="left">Action<b>:</b> </td>
                                <td>
                                    <form:select path="actiontaken">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="dispose">Dispose</form:option>
                                        <form:option value="reject">Reject</form:option>
                                        <form:option value="forward">Forward</form:option>
                                    </form:select>	
                                </td>
                            </tr>
                            <tr>						
                                <td  align="left">Send To<b>:</b> </td>
                                <td>
                                    <form:select path="toEmployee">
                                        <c:forEach items="${authlist}" var="empBasicProfile">
                                            <form:option label="${empBasicProfile.fname} ${empBasicProfile.mname} ${empBasicProfile.lname} (${empBasicProfile.spn})" value="${empBasicProfile.empid}-${empBasicProfile.spc}"/>
                                        </c:forEach>                                  
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>
                                    <input type="submit" name="action" value="Submit" id="regBtn" class="btn btn-success" onclick="return checkForm()" />
                                    <input type="submit" name="action" value="Back" id="regBtn" class="btn btn-success" />
                                </td>
                            </tr>
                        </table>
                    </form:form>
                </c:if>
            </div>
        </div>
    </body>
</html>
