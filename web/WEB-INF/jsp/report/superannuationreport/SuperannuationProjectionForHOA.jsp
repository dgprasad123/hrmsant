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
                // var sacctType=$("#sltAcctType").val();
                window.location = "ViewSupperannuationProjectionReportOnlyDept.htm?month=" + smonth + "&year=" + syear;
            }
            $(document).ready(function() {
                var accttype=$("#sltAcctType").val();
                //alert($("#sltAcctType").val());
            });
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
                                        Department Wise Superannuation Projection Report 1
                                    </h2>
                                </div>
                            </div>
                            <hr />
                            <form:form action="superannuationProjectionReportForAOOList.htm" commandName="command">
                                <div class="row">
                                    <div class="col-lg-1">
                                        <label>Select Account Type:</label>
                                    </div>

                                    <div class="col-lg-2">
                                        <form:select path="sltAcctType" id="sltAcctType" class="form-control">
                                            <form:option value="ALL">ALL</form:option>
                                            <form:option value="GPF">GPF</form:option>
                                            <form:option value="TPF">TPF</form:option>
                                            <form:option value="PRAN">PRAN</form:option>
                                        </form:select>
                                    </div>
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
                                            <form:options itemLabel="label" itemValue="value" items="${yearList}"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                        <input type="submit" value="View" name="View" class="form-control btn btn-danger">
                                        <%--<button type="submit" class="form-control btn btn-danger">View</button>--%>
                                    </div>
                                    <div class="col-lg-2">
                                        <input type="submit" name="Download" value="Download" class="btn btn-danger"/>

                                    </div>

                                </div>
                            </form:form>
                        </div>
                        <div class="panel-body">
                            <table class="table table-bordered" width="100%">
                                <thead>
                                    <tr>
                                        <th width="5%">Sl No</th>
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
                                                <c:out value="${list.departmentname}"/>
                                            </td>
                                            <td>
                                                <a href="getsuperannuationEmployeeList.htm?offCode=${list.deptCode}&month=${command.sltMonth}&year=${command.sltYear}&acctType=${command.sltAcctType}" target="_blank"> <c:out value="${list.totalRecord}"/></a>
                                            </td>
                                            <td>
                                                <a href="getsuperannuationEmployeeListGroupWise.htm?offCode=${list.deptCode}&month=${command.sltMonth}&year=${command.sltYear}&postGrp=A&acctType=${command.sltAcctType}" target="_blank"><c:out value="${list.groupA}"/></a>
                                            </td>
                                            <td>
                                                <a href="getsuperannuationEmployeeListGroupWise.htm?offCode=${list.deptCode}&month=${command.sltMonth}&year=${command.sltYear}&postGrp=B&acctType=${command.sltAcctType}" target="_blank"><c:out value="${list.groupB}"/></a>
                                            </td>
                                            <td>
                                                <a href="getsuperannuationEmployeeListGroupWise.htm?offCode=${list.deptCode}&month=${command.sltMonth}&year=${command.sltYear}&postGrp=C&acctType=${command.sltAcctType}" target="_blank"><c:out value="${list.groupC}"/></a>
                                            </td>
                                            <td>
                                                <a href="getsuperannuationEmployeeListGroupWise.htm?offCode=${list.deptCode}&month=${command.sltMonth}&year=${command.sltYear}&postGrp=D&acctType=${command.sltAcctType}" target="_blank"><c:out value="${list.groupD}"/></a>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                    <tr style='color:green;font-weight:bold'>
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
