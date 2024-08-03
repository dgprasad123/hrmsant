<%-- 
    Document   : EmployeeGrievanceEntry
    Created on : Jan 6, 2018, 7:16:44 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        <script type="text/javascript">
            function showOtherBlk(val) {
                if (val == 15) {
                    $("#other_blk").show();
                } else {
                    $("#other_blk").hide();
                }
            }
            function validate() {
                if ($.trim($("#grievanceDetail").val()) == "") {
                    alert("Grievance Detail Cannot be Blank");
                    return false;
                }
                if($("#authCode").val() == ""){
                    alert("Please Select Authority");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div class="panel panel-default">
            <div class="panel-heading">New Grievance Entry</div>
            <div class="panel-body">
                <form:form action="saveEmployeeGrievance.htm" commandName="EmployeeGrievanceForm" method="post" enctype="multipart/form-data">
                    <table class="table table-bordered table-hover" style="width:1000px;" align="center">
                        <tr style="font-weight:bold;background:#3E6A00;color:#FFFFFF;">
                            <td colspan="2" style="color:#FFFFFF;font-weight:bold;">Enter Grievance Details</td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>NAME:</td>
                            <td>
                                <form:hidden path="gid"/>
                                <form:hidden path="hrmsid"/>
                                <form:hidden path="spc"/>
                                <form:hidden path="appoffcode"/>
                                <form:hidden path="source"/>
                                <form:hidden path="appmobile"/>
                                ${sessionScope.LoginUserBean.loginname}
                            </td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>Category:</td>
                            <td>
                                <form:select path="categoryCode" onchange='showOtherBlk(this.value)'>
                                    <form:options items="${categorylist}" itemValue="categoryCode" itemLabel="category"/>                                
                                </form:select>	
                                <span style="display:none;" id="other_blk"><input type="text" name="other_category" class="tbox" size="30"/> <small>(Enter your Category Here)</small></span>
                            </td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>Grievance Details:</td>
                            <td><form:textarea path="grievanceDetail" cols="90" rows="5"/></td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>Attachment(s) if any?:</td>
                            <td>
                                <input type="file" name="attachment" id="attachment" />
                                <c:if test="${not empty grievanceDetail.attachementName}">
                                    <a target="_blank" href="downloadAttchment.htm?attachementId=${grievanceDetail.attachementId}">${grievanceDetail.attachementName}</a>
                                </c:if>
                            </td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>Authority:</td>
                            <td>
                                <form:select path="authCode">
                                    <c:forEach items="${authlist}" var="empBasicProfile">
                                        <form:option label="${empBasicProfile.fname} ${empBasicProfile.mname} ${empBasicProfile.lname} (${empBasicProfile.spn})" value="${empBasicProfile.empid}-${empBasicProfile.spc}"/>
                                    </c:forEach>                                    
                                </form:select>	                               
                            </td>
                        </tr>

                        <tr bgcolor="#FFFFFF">
                            <td></td>
                            <td>
                                <input type="submit" name="action" value="Save" class="btn btn-success" onclick="return validate()"/>
                                <input type="submit" name="action" value="Cancel" class="btn btn-success"/>
                                <br />
                                <span style="color:#888888;font-style:italic;display:none;" id="loader1">Please wait...</span>	
                            </td>	
                        </tr>
                    </table>
                </form:form>
            </div>
        </div>
    </body>
</html>
