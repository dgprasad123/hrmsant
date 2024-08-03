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

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $("#addemployeeModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
            })
            function validate() {
                var numberofchecked = $('input:checkbox:checked').length;
                if (numberofchecked > 0) {
                    var c = confirm('You have selected ' + numberofchecked + ' no of employees for nomination. Are you sure to continue?');
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
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <form:form action="createASINomination.htm" commandName="EmpDetNom">
                    <form:hidden path="nominationMasterId"/>
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> List of eligible and willing candidates</h3>

                        <div class="panel-heading">
                            <a href="asiNominationList.htm"><input type="button" class="btn btn-primary" value="Back"/>
                            </a> 

                            <c:if test="${empty EmpDetNom.nominationMasterId}">
                                <input type="submit" name="action" value="Create Nomination" class="btn btn-success"  onclick="return validate()"/>
                            </c:if>
                            <c:if test="${not empty EmpDetNom.nominationMasterId}">
                                <a href="asiNominationEmployeeListToAdd.htm?nominationMasterId=${EmpDetNom.nominationMasterId}&sltNominationForPost=${EmpDetNom.sltNominationForPost}" data-remote="false" data-toggle="modal" title="Add Employee" data-target="#addemployeeModal">
                                    <input type="button" value="Add Employee" class="btn btn-success"/>
                                </a>
                                <input type="submit" name="action" value="Delete Nomination" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                            </c:if>

                        </div>
                        <div class="panel-body">
                            <c:if test="${empty EmpDetNom.nominationMasterId}">
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
                                        <c:if test="${empty EmpDetNom.nominationMasterId}">
                                            <input type="submit" name="action" value="Get Employee" class="btn btn-primary"/>
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
                            </c:if>
                            <c:if test="${empList.size() gt 0}">
                                <c:if test="${not empty EmpDetNom.nominationMasterId}">
                                    <div class="table-responsive">
                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <thead>
                                                    <tr>
                                                        <th>#</th>
                                                        <th>HRMS ID/ GPF No</th>
                                                        <th>Employee Name</th>
                                                        <th>DOB/DOS</th>
                                                        <th>Date of Joining</th>
                                                        <th>Current Rank</th>
                                                        <th>Form Status</th>
                                                        <th colspan="3" style="text-align: center">Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${empList}" var="emp" varStatus="counter">
                                                        <tr>
                                                            <td width="5%" >${counter.count}</td>
                                                            <td width="10%">${emp.empId} <br /> ${emp.gpfno}</td>
                                                            <td width="25%">${emp.empName}</td>
                                                            <td width="10%">
                                                                ${emp.dob} <br /> <span style="color:red">${emp.dos}</span>
                                                            </td>
                                                            <td width="10%" style="text-align: center">${emp.doj}</td>
                                                            <td width="15%" style="text-align: center">${emp.sltpostName}</td>
                                                            <td width="10%">
                                                                <c:if test="${emp.formCompletionStatus eq 'Y'}"> Completed </c:if>
                                                                </td>
                                                                <td width="10%" style="text-align: center">
                                                                    <a href="ASINominationApplicationForm.htm?nominationMasterId=${emp.nominationMasterId}&nominationDetailId=${emp.nominationDetailId}"><i class="fa fa-pencil-square-o  fa-2x"></i></a>
                                                            </td>
                                                            <td width="10%" style="text-align: center">
                                                                <a href="deleteEmployeeFromASINominationList.htm?nominationMasterId=${emp.nominationMasterId}&nominationDetailId=${emp.nominationDetailId}" onclick="return confirm('Are you sure to delete?')"><i class="fa fa-trash fa-2x" aria-hidden="true"></i></a>
                                                            </td>
                                                            <td width="10%" style="text-align: center">
                                                                <%--<a href="downloadNominationFormController.htm?nominationMasterId=${emp.nominationMasterId}&nominationDetailId=${emp.nominationDetailId}"><i class="fa fa-file-pdf-o  fa-2x"></i></a>--%>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${empty EmpDetNom.nominationMasterId}">
                                    <div class="table-responsive">
                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <thead>
                                                    <tr>
                                                        <th width="5%">#</th>
                                                        <th width="5%"> Select </th>
                                                        <th width="15%">HRMS ID/ GPF No</th>
                                                        <th width="45%">Employee Name</th>
                                                        <th width="15%">DOB/DOS</th>
                                                        <th width="15%">Date of Joining</th>

                                                    </tr>
                                                </thead>
                                                <tbody id="employeelist">
                                                    <c:forEach items="${empList}" var="emp" varStatus="counter">
                                                        <tr>
                                                            <td >${counter.count}</td>
                                                            <td><input type="checkbox" name="empId" value="${emp.empId}"/></td>
                                                            <td>${emp.empId} <br /> ${emp.gpfno}</td>
                                                            <td>${emp.empName}</td>
                                                            <td>
                                                                ${emp.dob} <br /> <span style="color:red">${emp.dos}</span>
                                                            </td>
                                                            <td style="text-align: center">${emp.doj}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </c:if>
                            </c:if>
                        </div>
                        <div class="panel-footer">
                            <a href="asiNominationList.htm"><input type="button" class="btn btn-primary" value="Back"/>
                            </a> 

                            <c:if test="${empty EmpDetNom.nominationMasterId}">
                                <input type="submit" name="action" value="Create Nomination" class="btn btn-success" onclick="return validate()"/>
                            </c:if>
                            <c:if test="${not empty EmpDetNom.nominationMasterId}">
                                <a href="asiNominationEmployeeListToAdd.htm?nominationMasterId=${EmpDetNom.nominationMasterId}&sltNominationForPost=${EmpDetNom.sltNominationForPost}" data-remote="false" data-toggle="modal" title="Add Employee" data-target="#addemployeeModal">
                                    <input type="button" value="Add Employee" class="btn btn-success"/>
                                </a>
                                <input type="submit" name="action" value="Delete Nomination" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                            </c:if>

                        </div>
                    </div>

                    <div id="addemployeeModal" class="modal fade" role="dialog">
                        <div class="modal-dialog" style="width:1000px;">
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Add Employee</h4>
                                </div>
                                <div class="modal-body">

                                </div>
                                <div class="modal-footer">                       
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>