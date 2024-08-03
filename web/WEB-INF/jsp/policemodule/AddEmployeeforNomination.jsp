<%-- 
    Document   : AddEmployeeforNomination
    Created on : 27 Nov, 2020, 8:03:04 AM
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

            function getEmployeeListToAdd() {
                var url = "getEmployeeListToAddForNomination.htm?empId=" + $('#empId').val();
                var row = "";
                var slno = 0;
                $('#employeelist').empty();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {

                        
                            slno = slno + 1;
                            row = '<tr><td>' + slno + '</td><td><input type="checkbox" name="gpfno" id="employeeId" value="' + obj.gpfno + '"/></td><td>' + obj.empId + '</br>' + obj.gpfno + '</td><td>' + obj.empName + '</td><td>' + obj.dob + '</br><span style="color:red">' + obj.dos + '</span></td><td style="text-align: center">' + obj.doj + '</td></tr>';
                            $('#employeelist').append(row);
                       

                    });
                });
            }

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
            function addEmployeeForNomination(me) {
                var url = "addNominationroll.htm";
                sltNominationForPost = $("#sltNominationForPost").val();
                sltpostName = $("#sltpostName").val();
                fiscalyear = $("#fiscalyear").val();
                gpfno = $("#employeeId").val();
                $.post(url, {gpfno: gpfno, sltpostName: sltpostName, sltNominationForPost: sltNominationForPost, fiscalyear: fiscalyear})
                        .done(function(data) {
                            alert("added successfully");
                            $('#addMoreemployeeModal').modal('toggle');
                            searchEmployeeListForAddToNominationEmpty();
                        });

            }
        </script>
    </head>
    <body>
        <form:form action="createNominationroll.htm" commandName="EmpDetNom"> 
            <form:hidden path="sltpostName"/>
            <form:hidden path="sltNominationForPost"/>

            <div class="panel panel-default">
                <h3 style="text-align:center"> Employee List </h3>
                <div class="panel-heading">
                    <button type="button" class="btn btn-success" onclick="addEmployeeForNomination()">Add</button>
                    <%-- <input type="submit" name="action" value="Add" class="btn btn-success" onclick="return validateAdd()"/> --%>
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-4">
                            <label for="sltpostName"> Select Employee By GPF/PRAN number  <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-4">
                            <input type="text" name="empId" id="empId" style="width:90%" /> 
                        </div>
                        <div class="col-lg-2">
                            <input type="button" value="Search" onclick="getEmployeeListToAdd()"/>
                        </div>
                    </div>  
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-4">

                        </div>
                        <div class="col-lg-8">
                            <span style="color: red">GPF number should be upper case, no space allowed, alphabet first</span>
                        </div>

                    </div>  


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
                                <tbody id="employeelist">

                                </tbody>
                            </table>
                        </div>
                    </div>


                </div>
                <div class="panel-footer">
                    <button type="button" class="btn btn-success" onclick="addEmployeeForNomination()">Add</button>
                    <%-- <input type="submit" name="action" value="Add" class="btn btn-success"/> --%>
                </div>
            </div>
        </form:form>
    </body>
</html>

