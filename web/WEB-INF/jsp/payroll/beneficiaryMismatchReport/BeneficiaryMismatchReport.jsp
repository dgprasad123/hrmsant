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

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>
                                Beneficiary Mismatch Report
                            </h2>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" border="5" width="100%">
                        <thead>
                            <tr>
                                <th colspan="7"><h2 align="center">HRMS</h2></th>
                        <th colspan="3"><h2 align="center">IFMS(As on 29-Oct-2019)</h2></th>
                        </tr>
                        <tr>
                            <th width="5%">Sl No</th>
                            <th width="10%">HRMS ID</th>
                            <th width="10%">GPF No</th>
                            <th width="20%">Name</th>
                            <th width="7%">Mobile</th>
                            <th width="10%">Bank Acc No</th>
                            <th width="10%">IFS Code</th>
                            <th width="7%">Mobile</th>
                            <th width="10%">Bank Acc No</th>
                            <th width="10%">IFS Code</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty beneficiaryList}">
                                <c:forEach items="${beneficiaryList}" var="billGroup">
                                    <tr style="font-weight:bold;background:#FFD1AA;">
                                        <td colspan="13">${billGroup.billGroupName}</td>
                                    </tr>
                                    <c:forEach items="${billGroup.empList}" var="list" varStatus="count">
                                        <c:if test="${empty list.mismatchFound}">
                                            <tr style="background-color: #F00000">
                                            </c:if>
                                            <c:if test="${not empty list.mismatchFound}">
                                            <tr>
                                            </c:if>
                                            <td><c:out value="${count.index + 1}"/></td>
                                            <td>
                                                <c:out value="${list.empid}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.gpfno}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.fname}"/> <c:out value="${list.mname}"/> <c:out value="${list.lname}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.mobile}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.bankaccno}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.sltbranch}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.ifmsMobile}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.ifmsBankAccNo}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.ifmsIFSCode}"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                    
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>
    </body>
</html>
