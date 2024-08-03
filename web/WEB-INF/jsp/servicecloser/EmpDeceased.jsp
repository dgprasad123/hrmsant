<%-- 
    Document   : EmpDeceased
    Created on : Apr 9, 2018, 1:22:08 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
    </head>
    <body>
        <div class="col-md-8 col-sm-8" style="margin-top: 10px;">
            <div class="panel panel-default">
                <div class="panel-body">
                    <form:form class="form-horizontal" action="SaveEmployeeDeceased.htm" commandName="EmployeeDeceased" method="post">
                        <form:hidden path="empId"/>
                        <form:hidden path="curspc"/>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Employee Name:</label>
                            <span class="col-sm-10">${employee.fullname}</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Employee Id:</label>
                            <span class="col-sm-10">${employee.empid}</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Post:</label>
                            <span class="col-sm-10">${employee.post}</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">GPF/PRAN No:</label>
                            <span class="col-sm-10">${employee.gpfno}</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2"  style="padding-top: 0px;">Deceased Date:</label>
                            <div class="col-sm-2 input-group date">
                                <form:input path="deceasedDate" class="form-control" id="deceasedDate"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2"  style="padding-top: 0px;">Time:</label>
                            <div class="col-sm-2 input-group date">
                                <form:select path="deceasedDate" class="form-control" id="deceasedDate">
                                    <form:option value="FN">FORENOON</form:option>
                                    <form:option value="AN">AFTERNOON</form:option>                                    
                                </form:select>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2"  style="padding-top: 0px;">Deceased Cause:</label>
                            <div class="col-sm-2 input-group date">
                                <form:select path="deceasedType" class="form-control" id="deceasedType">
                                    <form:option value="Accidental" >Accidental</form:option>
                                    <form:option value="Natural">Natural</form:option>
                                    <form:option value="Medical">Medical</form:option>                                    
                                </form:select>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2"  style="padding-top: 0px;">Deceased Note(if Any):</label>
                            <div class="col-sm-2 input-group date">
                                <form:textarea path="deceasednote" class="form-control"/>                                
                            </div>
                        </div>
                        <c:if test="${EmployeeDeceased.deceasedDate == null}">
                            <input type="submit" name="action" value="Save" class="btn btn-primary"/>
                        </c:if>
                        <c:if test="${not empty EmployeeDeceased.deceasedDate}">
                            <input type="submit" name="action" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                        </c:if>
                    </form:form>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(function() {
                $('#deceasedDate').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
            });
        </script>
    </body>
</html>
