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
                            <h2 align="center">
                                ADMIN Superannuation Projection Report
                            </h2>
                        </div>
                    </div>
                    <hr />
                    <form:form action="ViewAdminSuperannuationProjectionReport.htm" commandName="command">
                        <div class="row">
                            <div class="col-lg-2">From Month</div>
                            <div class="col-lg-3">
                                <form:select path="sltFromMonth" class="form-control">
                                    <form:option value="-1">--Select--</form:option>
                                    <form:option value="1">JANUARY</form:option>
                                    <form:option value="2">FEBRUARY</form:option>
                                    <form:option value="3">MARCH</form:option>
                                    <form:option value="4">APRIL</form:option>
                                    <form:option value="5">MAY</form:option>
                                    <form:option value="6">JUNE</form:option>
                                    <form:option value="7">JULY</form:option>
                                    <form:option value="8">AUGUST</form:option>
                                    <form:option value="9">SEPTEMBER</form:option>
                                    <form:option value="10">OCTOBER</form:option>
                                    <form:option value="11">NOVEMBER</form:option>
                                    <form:option value="12">DECEMBER</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">From Year</div>
                            <div class="col-lg-3">
                                <form:select path="sltFromYear" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="2019"> 2019 </form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                        <div class="row">
                            <div class="col-lg-2">To Month</div>
                            <div class="col-lg-3">
                                <form:select path="sltToMonth" class="form-control">
                                    <form:option value="-1">--Select--</form:option>
                                    <form:option value="1">JANUARY</form:option>
                                    <form:option value="2">FEBRUARY</form:option>
                                    <form:option value="3">MARCH</form:option>
                                    <form:option value="4">APRIL</form:option>
                                    <form:option value="5">MAY</form:option>
                                    <form:option value="6">JUNE</form:option>
                                    <form:option value="7">JULY</form:option>
                                    <form:option value="8">AUGUST</form:option>
                                    <form:option value="9">SEPTEMBER</form:option>
                                    <form:option value="10">OCTOBER</form:option>
                                    <form:option value="11">NOVEMBER</form:option>
                                    <form:option value="12">DECEMBER</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">To Year</div>
                            <div class="col-lg-3">
                                <form:select path="sltToYear" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="2019"> 2019 </form:option>
                                    <form:option value="2020"> 2020 </form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-1">
                                <button type="submit" class="form-control btn btn-danger">View</button>
                            </div>
                        </div>
                    </form:form>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" width="100%">

                        <thead>
                            <tr>
                                <c:if test="${not empty distlist}">
                                    <td>&nbsp;</td>
                                    <c:forEach items="${distlist}" var="distList" varStatus="count">
                                        <td style="font-size: 10px;">
                                            <c:out value="${distList.distName}"/>
                                        </td>
                                    </c:forEach>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty datalist}">
                                <c:forEach items="${datalist}" var="deptlist" varStatus="count">
                                    <tr>
                                        <td style="font-size: 10px;">
                                            <c:out value="${deptlist.deptName}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.angData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.blgData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.blsData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.bgrData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.bbsrData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.bdkData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.bdhData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.ctcData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.dgrData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.dklData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.gjpData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.gjmData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.jspData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.jprData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.jsdData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.kldData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.plbData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.kpdData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.kjrData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.krdData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.kptData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.mkgData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.mbjData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.nbrData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.ngrData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.npdData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.priData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.rgdData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.sbpData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.snpData}"/>
                                        </td>
                                        <td>
                                            <c:out value="${deptlist.sngData}"/>
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
    </body>
</html>
