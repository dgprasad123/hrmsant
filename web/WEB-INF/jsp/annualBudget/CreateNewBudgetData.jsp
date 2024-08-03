<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateCreateBudget() {
                if (confirm("Are you sure to Prepare Budget?")) {
                    $("#btnSubmit").hide();
                    $("#budgetLoader").show();
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="ProcessBudgetData.htm" commandName="annualBudgetForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h4>Create New Annual Budget</h4>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h3 align="center"> Financial Year 
                                <form:select path="financialYear" id="financialYear">
                                    <%--<form:option value="2021-22" label="2021-22" cssStyle="width:30%"/>--%>
                                 <%--   <form:option value="2022-23" label="2022-23" cssStyle="width:30%"/>--%>
                                 <%--   <form:option value="2023-24" label="2023-24" cssStyle="width:30%"/> --%>
                                 <form:option value="2024-25" label="2024-25" cssStyle="width:30%"/>
                                    <%--<form:options items="${financialYearList}" itemLabel="label" itemValue="value"/>--%>
                                </form:select>
                            </h3> 
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="table-responsive">

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${not empty createStatus && createStatus ne 'Y'}">
                            <input type="submit" name="action" id="btnSubmit" class="btn btn-default btn-success" style="width:150px" value="Process Budget Data" onclick="return validateCreateBudget();"/>
                            <span id="budgetLoader" style="display:none;">
                                <img src="images/loading.gif"/>Preparing Budget
                            </span>
                        </c:if>
                        <c:if test="${not empty createStatus && createStatus eq 'Y'}">
                            <span id="msg" style="font-weight: bold; color: red;">
                                Annual Budget for selected Financial Year is already Created.
                            </span>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
