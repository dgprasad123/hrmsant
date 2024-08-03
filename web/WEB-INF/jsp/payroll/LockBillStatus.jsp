<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12"></div>
                    </div>
                </div>
                <div class="panel-body">
                    <table width="60%" class="table table-bordered">
                        <tbody>
                            <tr>
                                <td width="30%">
                                    No. of Beneficiary
                                </td>
                                <td width="30%">
                                    <span style="color:red"><c:out value="${beneficiarydata.label}"/></span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Beneficiary Net Amount
                                </td>
                                <td>
                                    <span style="color:red"> Rs.<c:out value="${beneficiarydata.value}"/></span>
                                </td>
                            </tr>
                        </tbody>
                    </table><br />
                    <c:if test="${not empty lockerrordatalist && lockerrordatalist.size() ne 0}">
                        <c:forEach items="${lockerrordatalist}" var="errordata">
                            <div style="color: red"> ${errordata} </div>
                        </c:forEach>
                    </c:if>
                </div>
                <c:if test="${not empty mismatch && mismatch.size() ne 0}">
                    <div> <span style="color:red"> Below given Beneficiaries Account information found mismatch with previous month salary </span>  </div>                
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="25%">HRMS ID/GPF ACC No</th>
                                    <th width="25%">Beneficiary Name</th>
                                    <th width="25%">Account Number</th>
                                    <th width="25%">IFS Code</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${mismatch}" var="obj">
                                    <tr style="color:#FFFFFF;background-color:#F00">
                                        <td>${obj.empCode}/${obj.gpfAccNo} </td>
                                        <td>${obj.empName}</td>
                                        <td>${obj.bankAccNo}</td>
                                        <td>${obj.ifscCode}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                
                <c:if test="${not empty mismatchGPFData && mismatchGPFData.size() ne 0}">
                    <div> <span style="color:red"> Below given Beneficiaries have GPF Subscription more than Rs.5,00,000 in current financial Year.</span></div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="25%">HRMS ID/GPF ACC No</th>
                                    <th width="25%">Beneficiary Name</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${mismatchGPFData}" var="obj">
                                    <tr style="color:#FFFFFF;background-color:#F00">
                                        <td>${obj.empCode}/${obj.gpfAccNo} </td>
                                        <td>${obj.empName}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                    
                <div class="panel-footer">
                    <c:if test="${empty lockerrordatalist || lockerrordatalist.size() eq 0}">
                        <c:if test="${empty mismatchGPFData}">
                            <a href="javascript:void(0);" onclick="lockBill(${billno},${bbbean.sltMonth}, ${bbbean.sltYear}, '${bbbean.txtbilltype}');" class="btn btn-default">Lock Your Bill</a>&nbsp;<span style="color:red;font-weight: bold;">(Please edit and save the bill before you Lock the Bill.)</span>
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>
