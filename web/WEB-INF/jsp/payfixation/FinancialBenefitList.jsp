<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>

        <form:form action="NewFinancialBenefit.htm" method="POST" commandName="payFixation">
            <form:hidden path="notType" id="notType"/>
            <div class="container-fluid">
                <div class="row" style="text-align:center">
                    <h1>eService Book</h1>
                    <h2>Financial details of an employee for Pensioner Benefits</h2>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="panel-heading" style="font-size:20pt;font-weight:bold;">
                                    <strong>HRMS ID: </strong>${payFixation.empid} <br><strong>Date of Joining: </strong>${emplyeedoegov}
                                </div><br />
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="15%">Date of Financial benefits</th>
                                    <th width="15%">Basic Pay</th>

                                    <th width="15%">Pay Scale</th>


                                    <th width="15%">Remarks</th>
                                </tr>
                            </thead>
                            <tbod
                                
                                
                                
                                y>
                                <c:forEach items="${payfixationList}" var="plist">
                                    <tr>
                                        <td>${plist.wefDate}<br />${plist.wefTime}</td>
                                        <td>${plist.curbasic}</td>
                                        <td>${plist.revPayScale}</td>
                                        <td>${plist.notType}</td>
                                      
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                   
                        <c:if test="${payFixation.notType == 'PAYFIXATION'}">
                            <button type="submit" class="btn btn-default">New Pay Fixation</button>
                        </c:if>
                        <c:if test="${payFixation.notType == 'PAYREVISION'}">
                            <button type="submit" class="btn btn-default">New Pay Revision</button>
                        </c:if>
                        <c:if test="${payFixation.notType == 'STEPUP'}">
                            <button type="submit" class="btn btn-default">New Step-up of Pay</button>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
