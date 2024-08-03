<%-- 
    Document   : NominationRollDetailPage
    Created on : 17 Oct, 2020, 1:52:26 PM
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
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#addemployeeModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
                $("#addMoreemployeeModal").on("show.bs.modal", function(e) {
                    if ($("#sltpostName").val() == '') {
                        alert('Please Select Rank and press Get Employee then click on  ADD More Employee.');
                        return false;
                    } else if ($("#sltNominationForPost").val() == '') {
                        alert('Please Select Nomination for and press Get Employee then click on  ADD More Employee.');
                        return false;
                    } else {
                        var link = $(e.relatedTarget);
                        $(this).find(".modal-body").load(link.attr("href"));
                    }
                });
                searchEmployeeListForAddToNominationEmpty();
                $('input[name="action"]').hide();

            })


            //$("#addmore").hide();

            function validateGetEmployee() {

                if ($("#sltpostName").val() == '') {
                    alert('Please Select Rank');
                    document.getElementById('sltpostName').focus();
                    return false;
                }
                if ($("#sltNominationForPost").val() == '') {
                    alert('Please Select Nomination for');
                    document.getElementById('sltNominationForPost').focus();
                    return false;
                } else {

                    return true;
                }



            }



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

            function searchEmployeeListForAddToNomination() {
                if ($("#sltpostName").val() == '') {
                    alert('Please Select Rank');
                    document.getElementById('sltpostName').focus();
                    return false;
                }
                if ($("#sltNominationForPost").val() == '') {
                    alert('Please Select Nomination for');
                    document.getElementById('sltNominationForPost').focus();
                    return false;
                }
                var sltpostName = $("#sltpostName").val();
                $.post("getselectedEmployeeForNomination.htm", {sltpostName: sltpostName})
                        .done(function(data) {
                            populateDataGridnotemptyId(data);
                        })

            }



            function searchEmployeeListForAddToNominationEmpty() {
                //var fiscalyear = $("#fiscalyear").val();
                //var sltNominationForPost = $("#sltNominationForPost").val();
                var nominationMasterId = $("#nominationMasterId").val();
                var sltpostName = $("#sltpostName").val();
                if (nominationMasterId == "") {
                    $.post("getselectedEmployeeForNomination.htm", {sltpostName: sltpostName})
                            .done(function(data) {
                                populateDataGrid(data);
                                if (data.length > 0) {
                                    $('input[name="action"]').show();
                                }
                            })
                } else {
                    $.post("getselectedEmployeeForNomination.htm", {sltpostName: sltpostName, nominationMasterId: nominationMasterId})
                            .done(function(data) {
                                populateDataGridnotemptyId(data);
                                if (data.length > 0) {
                                    $('input[name="action"]').show();
                                }
                            })
                }

            }
            function populateDataGrid(empList) {
                $("#employeedatagridnominated").empty();
                for (var i = 0; i < empList.length; i++) {
                    var row = '<tr>';
                    row = row + '<td>' + (i + 1) + '</td>';
                    <%--if (empList[i].sltpostName == '140070') { 
                        row = row + '<td> <input type="checkbox"  name="gpfno" id="chkEmpId" value="' + empList[i].empId + '"/> </td>';
                    } else {--%>
                        row = row + '<td> <input type="checkbox"  name="gpfno" id="chkEmpId" value="' + empList[i].gpfno + '"/></td>';
                    
                    row = row + '<td>' + empList[i].empId + '<br/>' + empList[i].gpfno + '</td>';
                    row = row + '<td>' + empList[i].empName + '</td>';
                    row = row + '<td>' + empList[i].dob + '<br/>' + '<span style="color:red"> ' + empList[i].dos + '</span></td>';
                    row = row + '<td>' + empList[i].fathersName + '</td>';
                    row = row + '</tr>';
                    $("#employeedatagridnominated").append(row);
                }
            }
            function populateDataGridnotemptyId(empList) {
                $("#employeedatagridnominated").empty();
                var tsltpostName = $("#sltpostName").val();
                var tsltNominationForPost = $("#sltNominationForPost").val();
                for (var i = 0; i < empList.length; i++) {
                    var row = '<tr>' + '<td>' + (i + 1) + '</td>';
                    <%--if (empList[i].sltpostName == '140070') {
                        row = row + '<td>' + empList[i].empId + '</td>';
                    } else { --%>
                        row = row + '<td>' + empList[i].gpfno + '</td>';
                    
                    row = row + '<td>' + empList[i].empName + '</td>';
                    row = row + '<td>' + empList[i].dob + '<br/><span style="color:red"> ' + empList[i].dos + '</span> </td>';
                    if (empList[i].formCompletionStatus == "Y") {
                        row = row + '<td> <span>Completed</span></td>';
                    } else {
                        row = row + '<td>&nbsp;</td>';
                    }
                    row = row + '<td> <a href="nominationFormController.htm?nominationMasterId=' + empList[i].nominationMasterId + '&nominationDetailId=' + empList[i].nominationDetailId + '&sltpostName=' + tsltpostName + '&sltNominationForPost=' + tsltNominationForPost + '"><i class="fa fa-pencil-square-o fa-2x"></i></a></td>';
                    row = row + '<td> <a href="downloadNominationFormController.htm?nominationMasterId=' + empList[i].nominationMasterId + '&nominationDetailId=' + empList[i].nominationDetailId + '"><i class="fa fa-file-pdf-o  fa-2x"></i></a></td>';
                    row = row + '</tr>';
                    $("#employeedatagridnominated").append(row);
                }
            }
        </script>

        <title>JSP Page</title>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <form:form action="createNominationroll.htm" commandName="EmpDetNom">
                    <form:hidden path="nominationMasterId"/>                    
                    <form:hidden path="fiscalyear"/>
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> Nomination Detail Employee List</h3>

                        <div class="panel-heading">
                            <a href="submitNominationForFieldOffice.htm?action=Search&sltpostName=${EmpDetNom.sltpostName}&sltNominationForPost=${EmpDetNom.sltNominationForPost}&fiscalyear=${EmpDetNom.fiscalyear}"><input type="button" class="btn btn-primary" value="Back"/></a> 

                           <%-- <c:if test="${empty EmpDetNom.nominationMasterId && not empty empList }"> --%>

                                <input type="submit" name="action" value="Create Nomination" class="btn btn-success"  onclick="return validate()"/>

                            <c:if test="${not empty EmpDetNom.nominationMasterId}">
                                <%--<c:if test="${EmpDetNom.sltpostName ne '140070'}"> --%>
                                    <a href="nominationEmployeeListforAdd.htm?nominationMasterId=${EmpDetNom.nominationMasterId}&sltpostName=${EmpDetNom.sltpostName}&sltNominationForPost=${EmpDetNom.sltNominationForPost}" data-remote="false" data-toggle="modal" title="Add Employee" data-target="#addemployeeModal"><input type="button" value="Add Employee" class="btn btn-success"/></a>
                                  <%--  </c:if>   --%>
                                <input type="submit" name="action" value="Delete Nomination" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                            </c:if>
                            <c:if test="${empty EmpDetNom.nominationMasterId}">
                                <a href="searchEmployeeFornomination.htm?sltpostName=${EmpDetNom.sltpostName}&sltNominationForPost=${EmpDetNom.sltNominationForPost}" data-remote="false" data-toggle="modal" title="Add More Employee" data-target="#addMoreemployeeModal" id="addmore"><input type="button" value="Add More Employee" class="btn btn-success"/></a>
                                </c:if>
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltpostName"> Select Rank <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="sltpostName">
                                        <form:options items="${currentRankList}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltpostName"> Select Nomination for <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="sltNominationForPost" id="sltNominationForPost">
                                        <form:options items="${newRanklist}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <c:if test="${empty EmpDetNom.nominationMasterId}">
                                        <input type="button" name="action" value="Get Employee" class="btn btn-primary" onclick="return searchEmployeeListForAddToNominationEmpty()"/>
                                    </c:if>
                                </div>
                            </div>  
                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <c:if test="${not empty EmpDetNom.nominationMasterId}">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                       <%-- <c:if test="${EmpDetNom.sltpostName eq '140070'}">
                                                        <th>EMP ID</th>
                                                      </c:if>--%>
                                                       <%-- <c:if test="${EmpDetNom.sltpostName ne '140070'}">--%>
                                                        <th>GPF No</th>
                                                        <%-- </c:if>--%>
                                                    <th>Employee Name</th>
                                                    <th>DOB/DOS</th>
                                                    <th>Form Status</th>
                                                    <th colspan="3" style="text-align: center">Action</th>

                                                </tr>
                                            </thead>
                                        </c:if>
                                        <c:if test="${empty EmpDetNom.nominationMasterId}">
                                            <thead>
                                                <tr>
                                                    <th width="5%">#</th>
                                                    <th width="5%"> Select </th>
                                                        <%-- <c:if test="${EmpDetNom.sltpostName eq '140070'}"> 
                                                        <th width="15%">EMP ID</th>
                                                         </c:if>  --%>
                                                         <%--<c:if test="${EmpDetNom.sltpostName ne '140070'}">--%>
                                                    <th width="15%">GPF No</th>
                                                        <%-- </c:if>--%>
                                                    <th width="30%">Employee Name</th>
                                                    <th width="15%">DOB/DOS</th>
                                                    <th width="30%">Fathers Name</th>
                                                </tr>
                                            </thead>
                                        </c:if>
                                        <tbody id="employeedatagridnominated">

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <a href="submitNominationForFieldOffice.htm?action=Search&sltpostName=${EmpDetNom.sltpostName}&sltNominationForPost=${EmpDetNom.sltNominationForPost}&fiscalyear=${EmpDetNom.fiscalyear}"><input type="button" class="btn btn-primary" value="Back"/></a> 

                            <input type="submit" name="action"  value="Create Nomination" class="btn btn-success"  onclick="return validate()"/>
                           

                            <c:if test="${not empty EmpDetNom.nominationMasterId}">
                               <%-- <c:if test="${EmpDetNom.sltpostName ne '140070'}"> --%>
                                    <a href="nominationEmployeeListforAdd.htm?nominationMasterId=${EmpDetNom.nominationMasterId}&sltpostName=${EmpDetNom.sltpostName}&sltNominationForPost=${EmpDetNom.sltNominationForPost}" data-remote="false" data-toggle="modal" title="Add Employee" data-target="#addemployeeModal"><input type="button" value="Add Employee" class="btn btn-success"/></a>
                                  <%--  </c:if> --%>
                                <input type="submit" name="action" value="Delete Nomination" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                            </c:if>
                            <c:if test="${empty EmpDetNom.nominationMasterId}">
                                <a href="searchEmployeeFornomination.htm?sltpostName=${EmpDetNom.sltpostName}&sltNominationForPost=${EmpDetNom.sltNominationForPost}" data-remote="false" data-toggle="modal" title="Add More Employee" data-target="#addMoreemployeeModal" id="addmore"><input type="button" value="Add More Employee" class="btn btn-success"/></a>
                                </c:if>
                                <%--<c:if test="${not empty EmpDetNom.nominationMasterId}">
                                    <a href="nominationEmployeeListforAdd.htm?nominationMasterId=${EmpDetNom.nominationMasterId}&sltpostName=${EmpDetNom.sltpostName}&sltNominationForPost=${EmpDetNom.sltNominationForPost}" data-remote="false" data-toggle="modal" title="Add Employee" data-target="#addemployeeModal"><input type="button" value="Add Employee" class="btn btn-success"/></a>
                                    <input type="submit" name="action" value="Delete Nomination" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                                </c:if>
                                <c:if test="${empty EmpDetNom.nominationMasterId}">
                                    <a href="searchEmployeeFornomination.htm?sltpostName=${EmpDetNom.sltpostName}&sltNominationForPost=${EmpDetNom.sltNominationForPost}" data-remote="false" data-toggle="modal" title="Add More Employee" data-target="#addMoreemployeeModal" id="addmore"><input type="button" value="Add More Employee" class="btn btn-success"/></a>
                                </c:if>--%>
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
                <div id="addMoreemployeeModal" class="modal fade" role="dialog">
                    <div class="modal-dialog" style="width:1000px;">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Employee Search Panel</h4>
                            </div>
                            <div class="modal-body">

                            </div>
                            <div class="modal-footer">                       
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
