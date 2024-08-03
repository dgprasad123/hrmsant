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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
          
        </script>
    </head>
    <body>
        <form:form class="form-inline" action="getLeaveList.htm" method="POST" commandName="clupdateform">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="align-center">
                            <div class="align-center">
                                <h2 class="align-center">
                                     <p class="fs-1"> ${clupdateform.empName} (HRMS ID-${clupdateform.empid})</p>
                                     <form:hidden path="offCode"/>
                                    <form:hidden path="postCode"/>
                                     <form:hidden path="empid"/>
                                  
                                </h2>
                            </div>
                        </div>
                    </div>

                    <div class="panel-body" >
                        <table class="table table-bordered" width="100%" >
                            <thead>
                                <tr>
                                    <th width="10%">Sl No</th>
                                    <th width="20%">Leave Type</th>
                                    <th width="20%">Leave Id</th>
                                    <th width="20%">Total Balance</th>
                                    <th width="20%">Available Balance</th>
                                    <th width="20%">Update Balance</th>
                                </tr>
                               
                            </thead>
                            <tbody>
                                <c:if test="${not empty empWiseLeaveList}">
                                    <c:forEach items="${empWiseLeaveList}" var="empLeaveist" varStatus="count">

                                        <tr>
                                            <td><c:out value="${count.index + 1}"/></td>
                                            <td>${empLeaveist.leaveType}</td>
                                            <td>${empLeaveist.tolId}</td>
                                            <td>${empLeaveist.totalBalance}</td>
                                            <td>${empLeaveist.totalAvailable}</td>
                                            <td>
                                             <a class="btn btn-primary" href="getLeaveBalance.htm?empId=${clupdateform.empid}&postCode=${clupdateform.postCode}&offCode=${clupdateform.offCode}&tolId=${empLeaveist.tolId}">Update (${empLeaveist.tolId}) </a>  
                                            </td>
                                           

                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" value="Back" name="btn" class="btn btn-danger" />
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
