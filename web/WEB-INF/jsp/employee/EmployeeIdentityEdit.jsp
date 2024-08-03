<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(function() {
                $('#issueDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#expiryDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });
            function setDocType() {
                var empId = $('#empId').val();
                var docType = $('#identityDocType').val();
                $('#hidIdentityId').val(empId + "," + docType);
                //alert($('#hidIdentityId').val($('#empId').val()+","+$('#identityDocType').val()));
            }
            function saveIdentity() {
                var idNo = $('#identityDocNo').val();
                var idDocType = $('#hidIdentityType').val();
                if (idDocType == '') {
                    alert("Please enter Identity Type");
                    return false;
                } else if (idDocType == 'AADHAAR' && idNo.length < 12) {
                    alert("Please enter valid Aadhaar No");
                    return false;
                } else if (idDocType == 'PAN') {
                    if (idNo.length < 10 || idNo.length > 10) {
                        alert("Please enter valid PAN No");
                        $('#identityDocNo').focus();
                        return false;
                    } else if (idNo.length == 10) {
                        var alphanum = /^[0-9a-zA-Z]+$/;
                        if (!idNo.match(alphanum)) {
                            alert("Please enter valid PAN No");
                            $('#identityDocNo').focus();
                            return true;
                        }
                    }
                }
                if (idNo == '') {
                    alert("Please enter Identity No");
                    return false;
                } else {
                    return true;
                }
            }
        </script>
    </head>
    <body>
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="IDENTITYPAGESB" />
        </jsp:include>
        <div id="profile_container">
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form id="fm" action="saveEmployeeIdentity.htm" method="post" name="myForm" commandName="identity">
                    <form:hidden id="empId" path="empId" value="${identity.empId}"/>
                    <form:hidden id="hidIdentityId" path="hidIdentityId" value="${identity.hidIdentityId}"/>

                    <div style=" margin-bottom: 5px;" class="panel panel-info">
                        <div class="panel-body">
                            <table border="0" cellpadding="0" cellspacing="0"  width="100%">
                                <tr>
                                    <td width="5%">&nbsp;</td>
                                    <td width="15%" >&nbsp;</td>
                                    <td width="30%" align="center">&nbsp;</td>
                                    <td width="40%" align="center">&nbsp;</td>
                                    <td width="10%" align="center">&nbsp;</td>
                                </tr>
                                <tr height="40px">
                                    <td align="center"><%=i++%>.</td>
                                    <td>Identity Type:<strong style="color:red">*</td>
                                    <td>
                                        <c:if test="${empty identity.hidIdentityId}">
                                            <form:select path="identityDocType" style="width:50%;" class="form-control" onchange="setDocType()">
                                                <form:option value="" label="Select" cssStyle="width:30%"/>
                                                <c:forEach items="${identityTypeList}" var="identityDocType">
                                                    <form:option value="${identityDocType.identityTypeId}" label="${identityDocType.identityType}"/>
                                                </c:forEach>                                 
                                            </form:select>
                                        </c:if>
                                        <c:if test="${not empty identity.hidIdentityId}">
                                            <c:out value="${identity.hidIdentityId}"/>
                                            <input type="hidden" id="hidIdentityType" value="${identity.identityDocType}"/>
                                        </c:if>
                                    </td>
                                    <td>&nbsp; </td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr height="40px">
                                    <td align="center"><%=i++%>.</td>
                                    <td>Identity No:<strong style="color:red">*</td>
                                    <td>
                                        <form:input path="identityDocNo" class="form-control" style="width:50%;" id="identityDocNo" placeholder="Enter Identity No" maxlength="40"/>
                                        <c:if test="${identity.identityDocType eq 'PAN'}">
                                            <c:if test="${fn:length(identity.identityDocNo) ne 10}">
                                                <span style="color:red;">Invalid PAN</span>
                                            </c:if>
                                        </c:if>
                                    </td>
                                    <td>&nbsp; </td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr height="40px">
                                    <td align="center"><%=i++%>.</td>
                                    <td>Place of Issue:</td>
                                    <td>
                                        <form:input path="placeOfIssue" class="form-control" style="width:50%;" id="placeOfIssue" placeholder="Enter Place of Issue" maxlength="30"/>
                                    </td>
                                    <td>&nbsp; </td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr height="40px">
                                    <td align="center"><%=i++%>.</td>
                                    <td>Date of Issue:</td>
                                    <td>
                                        <div class='input-group date' style="width:40%;" id='issueDate'>
                                            <form:input class="form-control"  id="issueDate" path="issueDate" />
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td>&nbsp; </td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr height="40px">
                                    <td align="center"><%=i++%>.</td>
                                    <td>Date of Expiry:</td>
                                    <td>
                                        <div class='input-group date' style="width:40%;" id='expiryDate'>
                                            <form:input class="form-control"  id="expiryDate" path="expiryDate" />
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td>&nbsp; </td>
                                    <td>&nbsp; </td>
                                </tr>
                            </table>                            
                        </div>
                        <div class="panel panel-footer">
                            <c:if test="${not empty empprofilecompletedstatus.dateOfProfileCompletion}">
                                <span style="display:block;text-align: center;font-weight: bold;font-size: 14px;color: red;">Profile completed on <c:out value="${empprofilecompletedstatus.dateOfProfileCompletion}"/> from IP <c:out value="${empprofilecompletedstatus.ipOfProfileCompletion}"/></span>
                            </c:if>
                            <c:if test="${empty empprofilecompletedstatus.dateOfProfileCompletion}">  
                                <c:if test="${empty  identity.hidIdentityId}">
                                    <input type="submit" name="action" value="Save" class="btn btn-primary" onclick="return saveIdentity();" />
                                </c:if>
                                <c:if test="${not empty  identity.hidIdentityId}">
                                    <input type="submit" name="action" value="Update" class="btn btn-primary" onclick="return saveIdentity();" />
                                    <input type="submit" name="action" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')" />
                                </c:if>
                            </c:if>
                            <input type="submit" name="action" value="Back" class="btn btn-primary"/>
                            <span style=" color: red"> ${identity.printMsg}</span>
                        </div>
                    </div>  
                </form:form>
            </div>
        </div>
    </body>
</html>
