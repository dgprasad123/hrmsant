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
        <script type="text/javascript">
            function confirmLock() {
                if (confirm('Are you sure to Lock selected Bill?')) {
                    return true;
                } else {
                    return false;
                }
            }
            function deleteArrear25Data(billno, vchno) {
                var empid = $('#empCode').val();
                //var vchNO = $('#vchno').val();
                //alert("Bill No is: "+billno);
                //alert("EMP ID is: "+empid);
                //alert(billno);
                if (vchno != "") {
                    alert("Vouchered Bill Cann't Be Deleted");
                }

                if (empid != "" && vchno == "") {
                    if (confirm('Are you sure to Delete this Bill?')) {
                        window.location = "DeleteArrear25Data.htm?billNo=" + billno + "&empid=" + empid;
                    } else {
                        return false;
                    }
                }
            }
            function deleteArrear25DuplicateData(aqslno, billno) {
                var empid = $('#empCode').val();
                if (empid != "") {
                    if (confirm('Are you sure to Delete Duplicate Data?')) {
                        window.location = "DeleteArrear40DuplicateMonth.htm?aqslno=" + aqslno + "&billNo=" + billno + "&empid=" + empid;
                    } else {
                        return false;
                    }
                }
                else {
                    alert("HRMS ID is Blank!");
                    return false;
                }
            }
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <form:form action="Arrear25VerificationDetailData.htm" method="POST" commandName="command">
                <div class="panel panel-default">
                    <div class="panel-heading">
                       
                        <div class="row">
                            <div class="col-lg-4"></div>
                            <div class="col-lg-3">
                                <h3>
                                    VIEW 25% ARREAR DATA
                                </h3>
                            </div>
                            <div class="col-lg-5"></div>
                        </div>
                        <br />
                        <div class="row">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2">
                                Enter HRMS ID
                            </div>
                            <div class="col-lg-2">
                                <form:input path="empCode" id="empCode" class="form-control"/>
                            </div>
                            <div class="col-lg-4">
                                <input type="submit" name="btnArrear25" value="Get Data" class="btn btn-danger"/>
                            </div>
                        </div>

                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="5%">Sl No</th>
                                    <th width="10%">Bill No</th>
                                    <th width="20%">Bill Name</th>
                                    <th width="10%">DDO Code</th>
                                    <th width="10%">From Month-Year</th>
                                    <th width="10%">To Month-Year</th>
                                    <th width="8%">Bill Month</th>
                                    <th width="12%">Status</th>
                                    <th width="12%">Arrear Type</th>
                                    <th width="12%">percentage</th>
                                    <th width="30%" colspan="3">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${billList}" var="list" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${list.billNo}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.billDesc}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.offDdo}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.fromMonth}"/>-<c:out value="${list.fromYear}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.toMonth}"/>-<c:out value="${list.toYear}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.payMonth}"/>-<c:out value="${list.payYear}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.billstatus}"/>
                                        </td>
                                        <td><c:out value="${list.arrtype}"/></td>
                                        <td><c:out value="${list.percentageArraer}"/></td>
                                        <td>
                                            <%--<a href="browseArrAq25DataReport.htm?aqslno=${list.aqSlNo}&billNo=${list.billNo}" target="_blank">View</a>--%>
                                        </td>
                                        <td>
                                            <c:if test="${list.billstatusid ne 5 && list.billstatusid ne 7 && list.billstatusid ne 2 && list.billstatusid ne 3}">
                                                <a href="javascript:void(0);" onclick="deleteArrear25Data('${list.billNo}', '${list.vchno}');">Delete Employee From Bill</a>
                                            </c:if>
                                        </td>
                                        
                                        <td>   
                                            <c:if test="${list.billstatusid >= 2}">
                                               <!-- <a href="javascript:void(0);" onclick="deleteArrear40DuplicateData('${list.aqSlNo}', '${list.billNo}');">Delete Duplicate Month</a> -->
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <%--<input type="submit" name="btnArrear40" value="Lock" class="btn btn-danger" onclick="return confirmLock();"/>
                        <span style="color: #F00000;">
                            Lock only one bill which is 40% Arrear correct. Then Process 10% or 60% Arrear.
                        </span>--%>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>
