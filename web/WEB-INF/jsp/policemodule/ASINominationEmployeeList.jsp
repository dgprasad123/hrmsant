<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <script type="text/javascript">

            function validateAdd() {
                var numberofchecked = $("input:checkbox:checked").length;
                if (numberofchecked > 0) {
                    var c = confirm('You have selected ' + numberofchecked + ' no of employees for nomination. Are you sure to add?');
                    if (c) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    alert("You have not selected any employee for nomination.");
                    return false;
                }
            }
            function getEmployeeListToAdd() {
                var url = "getEmployeeListToAdd.htm?sltpostName=" + $('#sltpostName').val();
                var row = "";
                var slno = 0;
                $('#employeelist').empty();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        slno = slno + 1;
                        row = '<tr><td>' + slno + '</td><td><input type="checkbox" name="empId" id="employeeId" value="' + obj.empId + '"/></td><td>' + obj.empId + '</br>' + obj.gpfno + '</td><td>' + obj.empName + '</td><td>' + obj.dob + '</br><span style="color:red">' + obj.dos + '</span></td><td style="text-align: center">' + obj.doj + '</td></tr>';
                        $('#employeelist').append(row);
                    });
                });
            }

            function searchFunction() {
                // Declare variables
                var input, filter, table, tr, td, i, txtValue;
                input = document.getElementById("myInput");
                filter = input.value.toUpperCase();
                table = document.getElementById("employeelist");
                tr = table.getElementsByTagName("tr");

                // Loop through all table rows, and hide those who don't match the search query
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[3];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        if (txtValue.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
        </script>
    </head>
    <body>
        <div class="panel panel-default">
            <form:form action="createASINomination.htm" commandName="EmpDetNom">
                <h3 style="text-align:center"> Employee List </h3>
                <form:hidden path="nominationMasterId"/>
                <div class="panel-heading">
                    <input type="submit" name="action" value="Add Employee" class="btn btn-success" onclick="return validateAdd()"/>
                </div>
                <div class="panel-body">

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="sltpostName"> Select Rank <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-3">
                            <form:select class="form-control" path="sltpostName" id="sltpostName">
                                <form:option value="">--Select--</form:option>
                                <%--<form:options items="${ranknameList}" itemLabel="label" itemValue="value"/>--%>
                                <form:option value="140883">COMMANDO</form:option>
                                <form:option value="140529">CONSTABLE</form:option>
                                <form:option value="140070">CONSTABLE</form:option>
                                <form:option value="140846">CONSTABLE ARMED POLICE RESERVE</form:option>
                                <form:option value="140983">CONSTABLE(GENERAL)</form:option>
                                <form:option value="140845">CONSTABLE (ORDINARY RESERVE)</form:option>
                                <form:option value="200347">CONSTABLE (OHRC)</form:option>
                                <form:option value="141077">CONSTABLE(TRAFFIC)</form:option>
                                <form:option value="110035">CONSTABLE(VIGILANCE)</form:option>
                                <form:option value="140897">CONSTABLE WILD LIFE</form:option>
                                <form:option value="140854">OR CONSTABLE</form:option>
                                <form:option value="140469">APR CONSTABLE</form:option>
                                <form:option value="140181">LANCE NAIK</form:option>
                                <form:option value="140470">APR HAVILDAR</form:option>
                                <form:option value="140051">C.I. HAVILDAR</form:option>
                                <form:option value="140896">CRIME HAVILDAR</form:option>
                                <form:option value="140124">HAVILDAR</form:option>
                                <form:option value="110070">HAVILDAR(VIGILANCE OFFICE)</form:option>
                                <form:option value="140844">HAVILDAR(ARMED POLICE RESERVE)</form:option>
                                <form:option value="140860">HAVILDAR (COUNTER INTELLIGENCE)</form:option>
                                <form:option value="140535">CRIME INTELLIGENCE HAVILDAR</form:option>
                                <form:option value="141050">HAVILDAR (CRIME)</form:option>
                                <form:option value="140843">HAVILDAR(CRIME INVESTIGATION)</form:option>
                                <form:option value="140831">HAVILDAR(GEN)</form:option>
                                <form:option value="141231">HAVILDAR OR</form:option>
                                <form:option value="141230">HAVILDAR OR</form:option>
                                <form:option value="140842">HAVILDAR(ORDINARY RESERVE)</form:option>
                                <form:option value="141078">HAVILDAR(TRAFFIC)</form:option>
                                <form:option value="140895">HAVILDAR WILD LIFE</form:option>
                                <form:option value="140130">HEAD CONSTABLE</form:option>
                                <form:option value="141370">WOMAN CONSTABLE</form:option>
                                <form:option value="140330">WOMAN CONSTABLE</form:option>
                                <form:option value="141090">WC CONSTABLE</form:option>
                                <form:option value="140017">ARMOUR CONSTABLE</form:option>
                                <form:option value="140777">LNK</form:option>
                                <form:option value="141020">LANCE NAIK (ODRAF)</form:option>
                            </form:select>
                        </div>
                        <div class="col-lg-2">
                            <label for="sltNominationForPost"> Select Nomination for <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-3">
                            <form:select class="form-control" path="sltNominationForPost" id="sltNominationForPost">
                                <form:option value="140858">ASSISTANT SUB INSPECTOR OF POLICE</form:option>
                            </form:select>
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${not empty EmpDetNom.nominationMasterId}">
                                <input type="button" name="action" value="Get Employee" class="btn btn-primary" onclick="getEmployeeListToAdd();"/>
                            </c:if>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">

                        </div>
                        <div class="col-lg-5" style="padding: 0px 15px 0px 15px;margin-bottom: 10px;">
                            <input type="text" id="myInput" class="form-control" onkeyup="searchFunction();" placeholder="Search for names..">
                        </div>
                    </div>
                    <div class="table-responsive">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped" id="tabid">
                                <thead>
                                    <tr>
                                        <th width="5%">#</th>
                                        <th width="5%">Select</th>
                                        <th width="15%">HRMS ID/ GPF No</th>
                                        <th width="45%">Employee Name</th>
                                        <th width="15%">DOB/DOS</th>
                                        <th width="15%">Date of Joining</th>
                                    </tr>
                                </thead>
                                <tbody id="employeelist">
                                    <%--<c:forEach items="${empList}" var="emp" varStatus="counter">
                                        <tr>
                                            <td>${counter.count}</td>
                                            <td><input type="checkbox" name="empId" id="employeeId" value="${emp.empId}"/></td>
                                            <td>${emp.empId} </br> ${emp.gpfno}</td>
                                            <td>${emp.empName}</td>
                                            <td>
                                                ${emp.dob} </br> <span style="color:red">${emp.dos}</span>
                                            </td>
                                            <td style="text-align: center">${emp.doj}</td>
                                        </tr>
                                    </c:forEach>--%>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
                <div class="panel-footer">
                    <input type="submit" name="action" value="Add Employee" class="btn btn-success" onclick="return validateAdd()"/>
                </div>
            </form:form>
        </div>
    </body>
</html>
