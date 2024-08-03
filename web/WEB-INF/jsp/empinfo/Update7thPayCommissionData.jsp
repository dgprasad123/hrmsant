<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link href="css/sb-admin.css" rel="stylesheet">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateSearch() {
                if ($('#empid').val() == "") {
                    alert("Please enter HRMS ID");
                    return false;
                }
            }
            function validateUpdate() {
                if ($('#empid').val() == "") {
                    alert("Please enter HRMS ID");
                    return false;
                }
                if ($('#sltPayCommission').val() == "") {
                    alert("Please select Pay Commission");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <form:form action="Update7thPayCommissionData.htm" method="POST" commandName="employee">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="row" style="margin-top:30px;margin-bottom: 20px;">
                                    <div class="col-lg-12">
                                        <span style="color: #F00000;font-size: 20px;display:block;text-align: center;">
                                            UPDATE EMPLOYEE PAY COMMISSION 
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-top:30px;margin-bottom: 20px;">
                                    <div class="col-lg-3"></div>
                                    <div class="col-lg-2">
                                        Enter HRMS ID
                                    </div>
                                    <div class="col-lg-3">
                                        <form:input path="empid" id="empid" class="form-control"/>
                                    </div>
                                    <div class="col-lg-3">
                                        <input type="submit" name="btnSubmit" value="Search" class="btn btn-info" onclick="return validateSearch();"/>
                                    </div>
                                    <div class="col-lg-1"></div>
                                </div>
                                <c:if test="${not empty errMsg && (errMsg eq 'N' || errMsg eq '6')}">
                                    <div class="row">
                                        <div class="col-lg-3"></div>
                                        <div class="col-lg-2">
                                            Select
                                        </div>
                                        <div class="col-lg-3">
                                            <form:select path="sltPayCommission" id="sltPayCommission" class="form-control">
                                                <form:option value="">--Select--</form:option>
                                                <c:if test="${not empty errMsg && errMsg eq '6'}">
                                                    <form:option value="6">6th Pay Commission</form:option>
                                                </c:if>
                                                <c:if test="${not empty errMsg && errMsg ne '6'}">
                                                    <form:option value="7">7th Pay Commission</form:option>
                                                </c:if>
                                            </form:select>
                                        </div>
                                        <div class="col-lg-3">
                                            <input type="submit" name="btnSubmit" value="Update" class="btn btn-success" onclick="return validateUpdate();"/>
                                        </div>
                                        <div class="col-lg-1"></div>
                                    </div>
                                </c:if>
                                <c:if test="${not empty errMsg && (errMsg ne 'N' && errMsg ne '6')}">
                                    <span style="color: #F00000;font-size: 15px;display:block;text-align: center;">
                                        <c:out value="${errMsg}"/>
                                    </span>
                                </c:if>
                                <c:if test="${not empty retVal && retVal > 0}">
                                    <span style="color: #F00000;font-size: 15px;display:block;text-align: center;">
                                        Pay Commission Updated.
                                    </span>
                                </c:if>
                            </div>
                            <div class="panel-footer">

                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
