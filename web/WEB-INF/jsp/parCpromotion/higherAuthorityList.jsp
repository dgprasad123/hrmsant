<%-- 
    Document   : higherAuthorityList
    Created on : Mar 31, 2020, 11:27:20 PM
    Author     : manisha
--%>




<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>
        <script type="text/javascript">
            function validation() {
                var radioValue = $("input[name='reportingempId']:checked").val();
                if (!radioValue) {
                    alert("Select Employee");
                    return false;
                }
            }


        </script>
    </head>

    <form:form action="parCForwardReport.htm" method="POST" commandName="groupCEmployee" class="form-inline">
        <div class="row">
            <div class="col-lg-12">
                <h2>Authority List</h2>
                <div class="table-responsive" style="height: 550px;overflow: auto;">
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>

                                <th>#</th>
                                <th>Employee Name</th>
                                <th>Designation</th>
                            </tr>
                        </thead>
                        <tbody>                                        
                            <c:forEach items="${authoritylist}" var="higherAuthority" varStatus="count">
                                <tr>

                                    <td><input type = "radio" name="reportingempId" value='${higherAuthority.reportingempId}-${higherAuthority.reportingspc}'> </td>
                                    <td>${higherAuthority.reportingempname}</td>
                                    <td>${higherAuthority.reportingpost}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:hidden path="groupCpromotionId"/>
                    <form:hidden path="remarkauthoritytype"/>
                    <form:hidden path="taskId"/>
                    <input type="submit" name="action" class="btn btn-default" value="Back"/>    
                    <input type="submit" name="action" class="btn btn-default" value="Submit" onclick="return validation()"/> 
                    <input type="submit" name="action" class="btn btn-default" value="Search Authority"/> 
                </div>

            </div>
        </div>


    </form:form>
</html>
