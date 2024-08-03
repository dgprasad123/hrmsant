<%-- 
    Document   : EmployeeEducationDetail
    Created on : Apr 5, 2019, 8:10:14 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#subject').keyup(function() {
                    $(this).val($(this).val().toUpperCase());
                });
                $('#institute').keyup(function() {
                    $(this).val($(this).val().toUpperCase());
                });
            });
            function validate() {
                if ($('#qualification').val() == "") {
                    alert("Please select Qualification.");
                    return false;
                }
                if ($('#subject').val() == '') {
                    alert("Please Enter Subject");
                    $('#subject').focus();
                    return false;
                }
                if ($('#institute').val() == '') {
                    alert("Please Enter Institute");
                    $('#lname').focus();
                    return false;
                }
            }
        </script>
    </head>
    <body>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="EDUCATIONPAGESB" />
        </jsp:include>
        <div id="profile_container">
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="saveEmployeeEducation.htm" method="post" class="form-horizontal" commandName="education">
                    <div style=" margin-bottom: 5px;" class="panel panel-info">
                        <div class="panel-body">
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Qualification:</label>
                                <div class="col-sm-4">
                                    <form:hidden path="empId"/>
                                    <form:hidden path="qfn_id"/>
                                    <form:select path="qualification" id="qualification" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${qualificationList}" itemLabel="qualification" itemValue="qualification"/>
                                    </form:select>                            
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Stream:</label>
                                <div class="col-sm-4">           
                                    <form:select path="faculty" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${facultyList}" itemLabel="facultyType" itemValue="facultyType"/>
                                    </form:select>                            
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Year of Pass:</label>
                                <div class="col-sm-4">
                                    <form:select path="yearofpass" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${yearofpasslist}"  itemLabel="label" itemValue="value"/>
                                    </form:select>                            
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Degree:</label>
                                <div class="col-sm-4">
                                    <form:select path="degree" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${degreeList}" itemValue="degreesl" itemLabel="degreeName"/>
                                    </form:select>                            
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Subject:</label>
                                <div class="col-sm-4">                            
                                    <form:input path="subject" class="form-control" maxlength="200"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Institute:</label>
                                <div class="col-sm-4">                            
                                    <form:input path="institute" class="form-control" maxlength="100"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Board / University:</label>
                                <div class="col-sm-4">                            
                                    <form:select path="board" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${universityList}" itemValue="boardserialNumber" itemLabel="boardName"/>
                                    </form:select> 
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <c:if test="${not empty empprofilecompletedstatus.dateOfProfileCompletion}">
                                <span style="display:block;text-align: center;font-weight: bold;font-size: 14px;color: red;">Profile completed on <c:out value="${empprofilecompletedstatus.dateOfProfileCompletion}"/> from IP <c:out value="${empprofilecompletedstatus.ipOfProfileCompletion}"/></span>
                            </c:if>
                            <c:if test="${empty empprofilecompletedstatus.dateOfProfileCompletion}">


                                <c:if test="${not empty education.qfn_id && education.qfn_id == 0}">
                                    <input type="submit" class="btn btn-success" name="action" value="Save" onclick="return educationValidate()"/>
                                </c:if>
                                <c:if test="${not empty education.qfn_id && education.qfn_id > 0}">
                                    <input type="submit" class="btn btn-success" name="action" value="Update" onclick="return educationValidate()"/>
                                    <input type="submit" class="btn btn-success" name="action" value="Delete" onclick="return confirm('Are you sure to Delete?')"/>                        
                                </c:if>
                            </c:if>   
                            <input type="submit" class="btn btn-success" name="action" value="Back"/>
                        </div>
                    </div>
                </form:form>   
            </div>
        </div>
    </body>
</html>
