<%-- 
    Document   : EmployeeAbsentee
    Created on : May 25, 2018, 5:46:22 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            function validateForm() {
                if ($("#adtype").val() == "") {
                    alert("Please Select Allowance/Deduction");
                    return false;
                }
            }
            function deleteAbsentee(absid){
                //alert(absid);
                if(confirm("Are you sure to Delete?")){
                    window.location = "DeleteAbsenteeList.htm?year="+$('#sltyear').val()+"&month="+$('#sltmonth').val()+"&absid="+absid;
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-inline" action="getAbseneteeList.htm" method="POST" commandName="absentee">
                <div class="panel panel-default">
                    <div class="panel-heading">

                        <div class="row">
                            <div class="col-lg-2"><b>Select Year:</b>&nbsp;<span style="color: red">*</span></div>
                            <div class="col-lg-2">
                                <form:select path="sltyear" id="sltyear" class="form-control">
                                    <form:option value="0">Select</form:option>
                                    <form:options items="${yearlist}" itemLabel="label" itemValue="value"/>

                                </form:select>
                            </div>
                            <div class="col-lg-2"><b>Select Month:</b>&nbsp;<span style="color: red">*</span></div>
                            <div class="col-lg-2">
                                <form:select path="sltmonth" id="sltmonth" class="form-control">
                                    <form:option value="00">January</form:option>
                                    <form:option value="01">February</form:option>
                                    <form:option value="02">March</form:option>
                                    <form:option value="03">April</form:option>
                                    <form:option value="04">May</form:option>
                                    <form:option value="05">June</form:option>
                                    <form:option value="06">July</form:option>
                                    <form:option value="07">August</form:option>
                                    <form:option value="08">September</form:option>
                                    <form:option value="09">October</form:option>
                                    <form:option value="10">November</form:option>
                                    <form:option value="11">December</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <input type="submit" name="action" value="Ok" class="btn btn-success"/>
                            </div>
                        </div>

                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="25%">From Date</th>
                                    <th width="25%">To Date</th>
                                    <th width="25%">Total Days</th>                           
                                    <th width="25%">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${absenteelist}" var="absentee">
                                    <tr>
                                        <td>${absentee.fromDate}</td>
                                        <td>${absentee.toDate}</td>
                                        <td>${absentee.totaldays}</td>
                                        <td>
                                            <a href="editEmployeeAbsentee.htm?absid=${absentee.absid}" class="btn btn-success"><span class="glyphicon glyphicon-pencil"></span> Edit</a>&nbsp;
                                            <a href="javascript:deleteAbsentee('${absentee.absid}')" class="btn btn-danger"><span class="glyphicon glyphicon-pencil"></span> Delete</a>
                                        </td>
                                    </tr>                                
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-lg-2">
                                <input type="submit" name="action" value="New Absentee" class="btn btn-success"/>
                            </div>
                            <div class="col-lg-10">
                                <c:if test="${not empty status}">
                                    <span style="color:#FF0000;font-weight:bold;font-size:16px;"><c:out value="${status}"/></span>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>
