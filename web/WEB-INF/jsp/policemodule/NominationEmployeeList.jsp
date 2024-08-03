<%-- 
    Document   : NominationEmployeeList
    Created on : 19 Oct, 2020, 4:19:22 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript">

            function validateAdd() {
                var numberofchecked = $('input:checkbox:checked').length;
                if (numberofchecked > 0) {
                    var c = confirm('You have selected ' + numberofchecked + ' no of employees for nomination. Are you sure to add?');
                    if (c) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    alert('You have not selected any employee for nomination.');
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="createNominationroll.htm" commandName="EmpDetNom"> 
            <form:hidden path="nominationMasterId"/>
            <div class="panel panel-default">
                <h3 style="text-align:center"> Employee List </h3>

                <div class="panel-heading">
                    <input type="submit" name="action" value="Add Employee" class="btn btn-success" onclick="return validateAdd()"/>
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="sltpostName"> Select Rank <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-3">
                            ${postName} <form:hidden path="sltpostName"/>
                        </div>
                        <div class="col-lg-2">
                            <label for="sltpostName"> Nomination for <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-3">
                            ${nominationForPost}
                        </div>

                    </div>  
                    <c:if test="${empList.size() gt 0}">


                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="5%">#</th>
                                            <th width="5%">Select</th>
                                            <th width="15%">GPF No</th>
                                            <th width="30%">Employee Name</th>
                                            <th width="15%">DOB/DOS</th>
                                            <th width="30%">Fathers Name</th>

                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${empList}" var="emp" varStatus="counter">
                                            <tr>
                                                <td >${counter.count}</td>
                                                <td> 
                                                    <form:checkbox path="gpfno" id="employeeId" value="${emp.gpfno}"/> 

                                                </td>
                                                <td>${emp.gpfno}</td>
                                                <td>${emp.empName}</td>
                                                <td>
                                                    ${emp.dob} </br> <span style="color:red">${emp.dos}</span>
                                                </td>
                                                <td>${emp.fathersName}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:if>

                </div>
                <div class="panel-footer">
                    <input type="submit" name="action" value="Add Employee" class="btn btn-success" onclick="return validateAdd()"/>
                </div>
            </div>
        </form:form>
    </body>
</html>
