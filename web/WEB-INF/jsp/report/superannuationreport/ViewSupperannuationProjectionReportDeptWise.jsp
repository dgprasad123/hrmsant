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
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function deptDetails() {
                var smonth = $("#sltMonth").val();
                var syear = $("#sltYear").val();
                window.location = "ViewSupperannuationProjectionReportOnlyDept.htm?month=" + smonth + "&year=" + syear;
            }
        </script>    
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-lg-12">
                                    <h2 align="center">
                                        Department Wise Superannuation Projection Report 
                                    </h2>
                                </div>
                            </div>
                            <hr />
                            <form:form action="ViewSupperannuationProjectionReportDeptWise.htm" commandName="command">
                                <div class="row">
                                    <div class="col-lg-2"></div>
                                    <div class="col-lg-3">
                                        <form:select path="sltMonth" class="form-control">
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
                                    <div class="col-lg-3">
                                        <form:select path="sltYear" class="form-control">
                                            <form:option value="">--Select--</form:option>                                    
                                            <form:option value="2020"> 2020 </form:option>
                                            <form:option value="2021"> 2021 </form:option>
                                            <form:option value="2022"> 2022 </form:option>
                                            <form:option value="2023"> 2023 </form:option> 
                                            <form:option value="2024"> 2024 </form:option>
                                            <form:option value="2025"> 2025 </form:option> 
                                            <form:option value="2026"> 2026 </form:option> 
                                            <form:option value="2027"> 2027 </form:option> 
                                            <form:option value="2028"> 2028 </form:option> 
                                            <form:option value="2029"> 2029 </form:option>
                                            <form:option value="2030"> 2030 </form:option> 
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
                                        <th width="5%">Sl No</th>
                                        <th width="10%">Department Code</th>
                                        <th width="30%">Department Name</th>                               
                                        <th width="10%">No of Employees</th>
                                        <th width="10%">GROUP A</th>
                                        <th width="10%">GROUP B</th>
                                        <th width="10%">GROUP C</th>
                                        <th width="10%">GROUP D</th>

                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty emplist}">
                                        <c:set var="GtotalPost" value="${0}" />
                                        <c:set var="grpA" value="${0}" />
                                        <c:set var="grpB" value="${0}" />
                                        <c:set var="grpC" value="${0}" />
                                        <c:set var="grpD" value="${0}" />
                                        <c:forEach items="${emplist}" var="list" varStatus="count">
                                            <c:set var="GtotalPost" value="${GtotalPost+list.totalRecord}" />
                                            <c:set var="grpA" value="${grpA+list.groupA}" />
                                            <c:set var="grpB" value="${grpB+list.groupB}" />
                                            <c:set var="grpC" value="${grpC+list.groupC}" />
                                            <c:set var="grpD" value="${grpD+list.groupD}" />
                                            <tr>
                                                <td><c:out value="${count.index + 1}"/></td>
                                                <td>
                                                    <c:out value="${list.deptCode}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.departmentname}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.totalRecord}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.groupA}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.groupB}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.groupC}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.groupD}"/>
                                                </td>

                                            </tr>
                                        </c:forEach>
                                        <tr style='color:green;font-weight:bold'>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>Total</td>
                                            <td>${GtotalPost}</td>
                                            <td>${grpA}</td>
                                            <td>${grpB}</td>
                                            <td>${grpC}</td>
                                            <td>${grpD}</td>
                                        </tr>

                                    </c:if>
                                    <c:if test="${empty emplist}">
                                        <tr>
                                            <td colspan="7" align="center">
                                                <h3>
                                                    NO RECORDS
                                                </h3>
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                        <div class="panel-footer">

                        </div>
                    </div>
                </div>
            </div>
        </div>      
    </body>
</html>
