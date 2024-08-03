<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
         <script type="text/javascript">
                    function checkYearMonth() {
                   if($('#sltyear').val() == ' '){
                    alert("Please select Year");
                            return false;
                    }
                     if($('#sltmonth').val() == ' '){
                    alert("Please select Month");
                            return false;
                    }
                }
        </script>
    </head>
    <body>

        <div id="page-wrapper">
            <form:form class="form-inline" action="leaveStatusReport.htm" method="POST" commandName="clupdateform">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="align-center">
                                <div class="align-center">
                                    <h2 class="align-center">
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-3">
                                                Leave Status Report 
                                            </div>
                                            <div class="col-lg-1" style="font-size: 20px;">
                                                Year 
                                            </div>
                                            <div class="col-lg-2">
                                                <form:select path="sltyear" id="sltyear" class="form-control">
                                                    <form:option value=" ">Select Year</form:option>
                                                    <form:option value="2022">2022</form:option>
                                                    <form:option value="2023">2023</form:option>
                                                      <form:option value="2024">2024</form:option>
                                                </form:select> 
                                            </div>
                                            <div class="col-lg-1"  style="font-size: 20px;">
                                                Month 
                                            </div> 
                                            <div class="col-lg-1" >
                                                <form:select path="sltmonth" id="sltmonth" class="form-control">
                                                    <form:option value=" ">Select Month</form:option>
                                                    <form:option value="01">January</form:option>
                                                    <form:option value="02">February</form:option>
                                                    <form:option value="03">March</form:option>
                                                    <form:option value="04">April</form:option>
                                                    <form:option value="05">May</form:option>
                                                    <form:option value="06">June</form:option>
                                                    <form:option value="07">July</form:option>
                                                    <form:option value="08">August</form:option>
                                                    <form:option value="09">September</form:option>
                                                    <form:option value="10">October</form:option>
                                                    <form:option value="11">November</form:option>
                                                    <form:option value="12">December</form:option>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1" >
                                                <input type="submit" value="Ok" name="btn" class="btn btn-danger" onclick="return checkYearMonth()" />
                                            </div>
                                            <div class="col-lg-1" >
                                                <input type="submit" value="Excel Format" name="btn" class="btn btn-danger" onclick="return checkYearMonth()"/>
                                            </div>
                                            <form:hidden path="offCode"/>
                                            <form:hidden path="postCode"/>
                                    </h2>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="panel-body" >
                        <table class="table table-bordered" width="100%" >
                            <thead>
                                <tr>
                                    <th width="5%">Sl No1</th>
                                    <th width="5%">Biometric <br> Emp Id</th>
                                    <th width="5%">HRMS Id</th>
                                    <th width="15%">Employee Name</th>
                                    <th width="10%"  >Total CL</th>
                                    <th width="10%">Available CL</th>
                                    <th width="10%">Total Absent <br> Current Month</th>
                                    <th width="10%">Total Late <br> In-Punch</th>
                                    <th width="10%">Total Missing <br> Out-Punch</th>
                                    <th width="10%">Leave Balance <br> As on Date </th>
                                    <th width="10%">No. Of days <br> (Every 3 days Late) </th>
                                    <th width="10%">Working<br>Hour <br>As on Date</th>
                                </tr>

                            </thead>
                            <tbody>
                                <c:if test="${not empty empList}">
                                    <c:forEach items="${empList}" var="empList" varStatus="count">
                                        <tr>
                                            <td><c:out value="${count.index + 1}"/></td>
                                            <td>${empList.biometricId}</td>
                                            <td>${empList.empid}</td>
                                            <td>${empList.empName}</td>
                                            <td>${empList.totalCl}</td>
                                            <td>${empList.totalClAvail}</td>
                                            <td>
                                                ${empList.totAbsentCurMonth}
                                            </td>
                                            <td>
                                                ${empList.totLateInpunch}
                                            </td>
                                            <td>
                                                ${empList.totMissingOutPunch}
                                            </td>
                                            <td>
                                                ${empList.totBalanceAsOnDate}
                                            </td>
                                            <td>
                                                ${empList.totDaysForThreeDaysLate}
                                            </td>
                                            <td>
                                                <a href="viewWorkingHourDetail.htm?userId=${empList.biometricId}&year=${clupdateform.sltyear}&month=${clupdateform.sltmonth}">View</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">

                    </div>
                </div>
            </div>
        </form:form>
    </div>

</body>
</html>
