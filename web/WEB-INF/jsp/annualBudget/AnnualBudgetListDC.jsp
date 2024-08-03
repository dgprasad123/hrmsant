<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="css/sb-admin.css">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateSearch(){
                if($("#ddocode").val() == ""){
                    alert("Please enter DDO Code");
                    $("#ddocode").focus();
                    return false;
                }
              return true;  
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <form:form action="UnlockDDOAnnualBudgetPageDC.htm" commandName="annualBudgetFormDC">
                    <div class="container-fluid">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <h4>UnLock Annual Budget</h4>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-4">
                                        <label>Enter DDO Code</label>
                                    </div>
                                    <div class="col-lg-4">
                                        <form:input path="ddocode" id="ddocode" class="form-control" maxlength="9"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <input type="submit" name="btnSubmit" value="Search" class="form-control" onclick="return validateSearch();"/>
                                    </div>
                                    <div class="col-lg-3"></div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="table-responsive">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th rowspan="2">Sl No</th>
                                                        <th rowspan="2">Financial Year</th>
                                                        <th rowspan="2">Created On</th>
                                                        <th colspan="3"></th>
                                                    </tr>
                                                    <tr>
                                                        <th> Locked On </th>
                                                        <th> Locked Status</th>
                                                        <th> Submit Status</th>
                                                    </tr>
                                                </thead>
                                                <tbody>                                        
                                                    <c:forEach items="${budgetlist}" var="blist" varStatus="counter">
                                                        <tr>
                                                            <td>${counter.count}</td>
                                                            <td>${blist.fy}</td>
                                                            <td>${blist.createdDate}</td>
                                                            <td>${blist.lockdate}</td>
                                                            <td>
                                                                <c:if test="${blist.isLocked eq 'Y' && blist.isSubmitted ne 'Y'}">
                                                                    <a href="UnlockDDOAnnualBudgetDC.htm?ddocode=${annualBudgetFormDC.ddocode}&budgetid=${blist.budgetid}" onclick="return confirm('Are you sure to Unlock');">Unlock</a>
                                                                </c:if>
                                                                <c:if test="${blist.isLocked eq 'N'}">
                                                                    Un-Locked
                                                                </c:if>
                                                            </td>
                                                            <td>${blist.status}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-footer"></div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
