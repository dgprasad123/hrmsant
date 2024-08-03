<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee List Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/dataTables.bootstrap5.min.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.10.24/js/dataTables.bootstrap5.min.js"></script>
        <script>
            $(document).ready(function() {
                $('#employeeTable').DataTable();
            });
        </script>
        <script>
            function generateSerialNumbers() {
                var table = document.getElementById("datatable");
                var rows = table.querySelectorAll("tbody tr");
                rows.forEach(function(row, index) {
                    var serialNumberCell = row.insertCell(0);
                    serialNumberCell.textContent = index + 1;
                });
            }
            window.onload = generateSerialNumbers;
        </script>
        <script>
            // Function to store input field values before form submission
            function storeFieldValues() {
                localStorage.setItem('firstName', document.getElementById('firstName').value);
                localStorage.setItem('lastName', document.getElementById('lastName').value);
                localStorage.setItem('dob', document.getElementById('dob').value);
                localStorage.setItem('designation', document.getElementById('designation').value);
                localStorage.setItem('departmentName', document.getElementById('departmentName').value);
                localStorage.setItem('fatherName', document.getElementById('fatherName').value);
            }

            // Function to populate input fields with stored values
            function populateFields() {
                document.getElementById('firstName').value = localStorage.getItem('firstName') || '';
                document.getElementById('lastName').value = localStorage.getItem('lastName') || '';
                document.getElementById('dob').value = localStorage.getItem('dob') || '';
                document.getElementById('designation').value = localStorage.getItem('designation') || '';
                document.getElementById('departmentName').value = localStorage.getItem('departmentName') || '';
                document.getElementById('fatherName').value = localStorage.getItem('fatherName') || '';
            }

            // Call populateFields function when the page loads
            window.onload = populateFields;
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <form id="employeeDetailsForm" action="SearchEmployeeDetails.htm" method="POST" commandName="SearchEmployeeDetails" onsubmit="storeFieldValues()">
                        <div class="panel-heading">
                            <h3 style="text-align:center; font-weight:bold;">EMPLOYEE &nbsp; DETAILS</h3>
                        </div>
                        <table class="table table-bordered">
                            <tr>
                                <td width="20%" align="center">First Name:</td>
                                <td width="20%"><input type="text" id="firstName" name="firstName" oninput="this.value = this.value.toUpperCase()"></td>
                                <td width="20%" align="center">Last Name:</td>
                                <td width="20%"><input type="text" id="lastName" name="lastName" oninput="this.value = this.value.toUpperCase()"></td>
                                <td width="20%" align="center">DOB :</td>
                                <td width="20%"><input type="text" id="dob" name="dob"></td>
                            </tr>
                            <tr>
                                <td width="20%" align="center">Designation:</td>
                                <td width="20%"><input type="text" id="designation" name="designation" oninput="this.value = this.value.toUpperCase()"></td>
                                <td width="20%" align="center">Father Name:</td>
                                <td width="20%"><input type="text" id="fatherName" name="fatherName" oninput="this.value = this.value.toUpperCase()"></td>

                                <td width="20%" align="center">Department:</td>
                                <td width="20%">
                                    <select id="departmentName" name="departmentName" class="form-control" style="width: 400px;">
                                        <option value="">Select Department</option>
                                        <c:forEach var="department" items="${departmentList}">
                                            <option value="${department}">${department}</option>
                                        </c:forEach>
                                    </select>
                                </td>

                            </tr>

                        </table>
                        <div class="float-end">
                            <input type="submit" class="btn btn-success btn-lg" id="btn_Search" name="btnSearch" value="Search">
                        </div>

                </div>

                <h1>&nbsp</h1>

                </form>
                <div class="panel-body">
                    <table id="employeeTable" class="table table-bordered table-hover table-striped dataTable no-footer">
                        <thead>
                            <tr>
                                <th width="5%">SL NO.</th>
                                <th width="15%">Employee Id</th>
                                <th width="15%">Employee Name</th>
                                <th width="15%">Father Name</th>
                                <th width="15%">Designation</th>
                                <th width="15%">Department</th>
                                <th width="15%">DOB</th>
                            </tr>
                        </thead>
                        <tbody id="employee">
                            <c:forEach var="employeeList" items="${employeeList}" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${employeeList.empId}</td>
                                    <td>${employeeList.firstName} ${employeeList.middleName} ${employeeList.lastName}</td>
                                    <td>${employeeList.fatherName}</td>
                                    <td>${employeeList.designation}</td>
                                    <td>${employeeList.departmentName}</td>
                                    <td>${employeeList.dob}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
