<%@page contentType="text/html" pageEncoding="UTF-8" buffer="128kb"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function getEmployeePostedOfficeList() {
                var url = 'getEmployeePostedOfficeListJSON.htm?deptcode=' + $('#sltDept').val();
                $('#sltOffice').empty();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltOffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }
        </script>
    </head>
    <body>
        <form:form action="par/initiateOtherPAR.htm" method="POST" commandName="initiateOtherPARForm">
            <input type="hidden" name="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <form:hidden path="fiscalYear" id="fiscalYear" value='${parMastForm.fiscalyear}'/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row" style="margin-bottom: 7px;margin-top:15px;">
                            <div class="col-lg-3">
                                Department
                            </div>
                            <div class="col-lg-6">   
                                <form:select path="sltDept" id="sltDept" class="form-control" onchange="getEmployeePostedOfficeList();">
                                    <form:option value="">--Select Department--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-3"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;margin-top:15px;">
                            <div class="col-lg-3">
                                Posted Office
                            </div>
                            <div class="col-lg-6">   
                                <form:select path="sltOffice" id="sltOffice" class="form-control">
                                    <form:option value="">--Select Office--</form:option>
                                    <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-3">
                                <input type="submit" name="btnInitiateOtherPAR" value="Get List" class="btn btn-default"/>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div style="height:500px;overflow: auto;">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th width="10%" style="text-align: center;">Sl No</th>
                                        <th width="10%" style="text-align: center;">HRMS ID</th>
                                        <th width="30%" style="text-align: center;">Employee Name</th>
                                        <th width="40%" style="text-align: center;">Designation</th>
                                        <th width="10%" style="text-align: center;">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty emplist}">
                                        <c:forEach items="${emplist}" var="list" varStatus="count">
                                            <tr>
                                                <td>
                                                    ${count.index + 1}
                                                </td>
                                                <td style="text-align: center;">
                                                    <c:out value="${list.empid}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.empname}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.designation}"/>
                                                </td>
                                                <td><a href="initiateOtherPARRemarks.htm?appraiseeEmpId=<c:out value="${list.empid}"/>&fiscalYear=${initiateOtherPARForm.fiscalYear}" class="btn btn-success">Initiate</a></td>
                                                <%--<td>
                                                    <c:if test="${list.parCreated eq 0}">
                                                        <a href="initiateOtherPARRemarks.htm?appraiseeEmpId=<c:out value="${list.empid}"/>&fiscalYear=${initiateOtherPARForm.fiscalYear}" class="btn btn-success">Initiate</a>
                                                    </c:if>
                                                    <c:if test="${list.parCreated gt 0}">
                                                        <span>Already Initiated</span>
                                                    </c:if>
                                                </td> --%>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="panel-footer">

                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
