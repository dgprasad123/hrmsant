<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script>
            function validate() {
                if ($('#newmobile').val() == '') {
                    alert('Please enter New Mobile No.');
                    return false;
                }
                var result = confirm("Are you sure to change Mobile Number?");
                if (result) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="UpdateEmployeeMobileNumber.htm" method="post" commandName="emp">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Update Mobile Number of <c:out value="${emp.empName}"/> (HRMS ID - <c:out value="${emp.empid}"/>)
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                Current Mobile Number
                            </div>
                            <div class="col-lg-3">
                                <form:hidden path="mobile" id="mobile"/>
                                <c:out value="${emp.mobile}"/>
                            </div>
                            <div class="col-lg-3">
                                <c:if test="${not empty DUPLICATE}">
                                    <span style="font-size: 16px; font-weight: bold;color: #F00000;">
                                        Duplicate Mobile Number(HRMS ID - <c:out value="${DUPLICATE}"/>)
                                    </span>
                                </c:if>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                New Mobile Number
                            </div>
                            <div class="col-lg-3">   
                                <form:input path="newmobile" id="newmobile" class="form-control" autocomplete="off"/>
                            </div>
                        </div>    
                    </div>
                    <div class="panel-footer">
                        <a href="ShowEmployeeMobileNumberList.htm"><button type="button" name="back" value="Back" class="btn btn-primary">Back</button></a>
                        <button type="submit" name="Submit" value="Change" class="btn btn-success" onclick="return validate();">Change</button>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
