<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateSubmit() {
                if ($("#txtpranno").val() == '') {
                    alert("Please enter PRAN No.");
                    return false;
                }
                return true;
            }

            function validatePaySlip() {
                if ($("#txthrmsid").val() == '') {
                    alert("Please enter HRMS ID");
                    return false;
                }
                if ($("#sltPaySlipYear").val() == '') {
                    alert("Please enter Year.");
                    return false;
                }
                if ($("#sltPaySlipMonth").val() == '') {
                    alert("Please enter Month.");
                    return false;
                }                
            }

            function viewPaySlipDetail(aqslno) {
                var year = $('#sltPaySlipYear').val();
                var month = $('#sltPaySlipMonth').val();
                var url = "PaySlipDetail.htm?aqlsno=" + aqslno + "&sltYear=" + year + "&sltMonth=" + month + "&empid=" + $("#txthrmsid").val();
                window.open(url, "_blank");
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
                            <form:form action="EmployeeNPSDataReport.htm" commandName="EmployeeNPSDataReportForm">
                                <div class="row">
                                    <label class="control-label col-sm-1">PRAN No</label>
                                    <div class="col-sm-3">
                                        <form:input path="txtpranno" id="txtpranno" class="form-control"/>
                                    </div>
                                    <label class="control-label col-sm-1">Year</label>
                                    <div class="col-sm-2">
                                        <form:select path="sltYear" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:option value="2020">2020</form:option>
                                            <form:option value="2021">2021</form:option>
                                            <form:option value="2022">2022</form:option>
                                            <form:option value="2023">2023</form:option>
                                            <form:option value="2024">2024</form:option>
                                        </form:select>
                                    </div>
                                    <label class="control-label col-sm-1">Month</label>
                                    <div class="col-sm-2">
                                        <form:select path="sltMonth" class="form-control">
                                            <form:option value="">--Select--</form:option>
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
                                    <div class="col-sm-2">
                                        <input type="submit" name="submit" value="Show" class="btn btn-success" onclick="return validateSubmit();"/>
                                    </div>
                                </div><br />
                                <div class="row">
                                    <label class="control-label col-sm-1">HRMS ID</label>
                                    <div class="col-sm-3">
                                        <form:input path="txthrmsid" id="txthrmsid" class="form-control"/>
                                    </div>
                                    <label class="control-label col-sm-1">Year</label>
                                    <div class="col-sm-2">
                                        <form:select path="sltPaySlipYear" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:option value="2004">2004 </form:option>
                                            <form:option value="2005">2005 </form:option>
                                            <form:option value="2006">2006 </form:option>
                                            <form:option value="2007">2007 </form:option>
                                            <form:option value="2008">2008 </form:option>
                                            <form:option value="2009">2009 </form:option>
                                            <form:option value="2010">2010 </form:option>
                                            <form:option value="2011">2011 </form:option>
                                            <form:option value="2012">2012 </form:option>
                                            <form:option value="2013">2013 </form:option>
                                            <form:option value="2014">2014 </form:option>
                                            <form:option value="2015">2015 </form:option>
                                            <form:option value="2016">2016 </form:option>
                                            <form:option value="2017">2017 </form:option>
                                            <form:option value="2018">2018 </form:option>
                                            <form:option value="2019">2019 </form:option>
                                            <form:option value="2020">2020 </form:option>
                                            <form:option value="2021">2021 </form:option>
                                            <form:option value="2022">2022 </form:option>
                                            <form:option value="2023">2023 </form:option>
                                            <form:option value="2024">2024 </form:option>
                                        </form:select>
                                    </div>
                                    <label class="control-label col-sm-1">Month</label>
                                    <div class="col-sm-2">
                                        <form:select path="sltPaySlipMonth" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:option value="0">JANUARY</form:option>
                                            <form:option value="1">FEBRUARY</form:option>
                                            <form:option value="2">MARCH</form:option>
                                            <form:option value="3">APRIL</form:option>
                                            <form:option value="4">MAY</form:option>
                                            <form:option value="5">JUNE</form:option>
                                            <form:option value="6">JULY</form:option>
                                            <form:option value="7">AUGUST</form:option>
                                            <form:option value="8">SEPTEMBER</form:option>
                                            <form:option value="9">OCTOBER</form:option>
                                            <form:option value="10">NOVEMBER</form:option>
                                            <form:option value="11">DECEMBER</form:option>
                                        </form:select>                                
                                    </div>
                                    <div class="col-sm-2">
                                        <input type="submit" name="submit" value="Show Payslip" class="btn btn-success" onclick="return validatePaySlip();"/>
                                    </div>
                                    <div class="col-sm-4"></div>
                                </div>
                            </form:form>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <label class="control-label col-sm-1">EMPLOYEE NAME</label>
                                <div class="col-sm-3">
                                    <c:out value="${EmployeeNPSDataReportForm.empname}"/>
                                </div>
                                <label class="control-label col-sm-1">DESIGNATION</label>
                                <div class="col-sm-3">
                                    <c:out value="${EmployeeNPSDataReportForm.designation}"/>
                                </div>
                                <label class="control-label col-sm-1">DATE OF BIRTH</label>
                                <div class="col-sm-1">
                                    <c:out value="${EmployeeNPSDataReportForm.dob}"/>
                                </div>
                                <label class="control-label col-sm-1">DATE OF RETIREMENT</label>
                                <div class="col-sm-1">
                                    <c:out value="${EmployeeNPSDataReportForm.dos}"/>
                                </div>
                            </div>

                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#regularbill" data-toggle="tab">Regular Bill</a></li>
                                <li><a href="#arrearbill" data-toggle="tab">Arrear Bill</a></li>
                                <li><a href="#payslip" data-toggle="tab">Pay Slip</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane fade in active" id="regularbill">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Sl No</th>
                                                <th>Bill No</th>
                                                <th>Bill Desc</th>
                                                <th>Bill Year</th>
                                                <th>Bill Month</th>
                                                <th>Employee<br />Contribution</th>
                                                <th>Government<br />Contribution</th>
                                                <th>View Report</th>
                                            </tr>
                                        </thead>
                                        <tbody>                            
                                            <c:if test="${not empty EmployeeNPSDataReportForm.regularbillEmpdatalist}">
                                                <c:forEach items="${EmployeeNPSDataReportForm.regularbillEmpdatalist}" var="empdata" varStatus="count">
                                                    <tr>
                                                        <td>${count.index + 1}</td>
                                                        <td><c:out value="${empdata.billno}"/></td>
                                                        <td><c:out value="${empdata.billdesc}"/></td>
                                                        <td><c:out value="${empdata.billyear}"/></td>
                                                        <td><c:out value="${empdata.billmonth}"/></td>
                                                        <td><c:out value="${empdata.empcontribution}"/></td>
                                                        <td><c:out value="${empdata.governmentcontribution}"/></td>
                                                        <td>
                                                            <a href="BillContributionHTML.htm?annexure=annexure1&billNo=${empdata.billno}" target="_blank">Annexure-I</a><br />
                                                            <a href="BillContributionHTML.htm?annexure=annexure2&billNo=${empdata.billno}" target="_blank">Annexure-II</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="tab-pane fade in" id="arrearbill">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Sl No</th>
                                                <th>Bill No</th>
                                                <th>Bill Desc</th>
                                                <th>Bill Year</th>
                                                <th>Bill Month</th>
                                                <th>Employee<br />Contribution</th>
                                                <th>Government<br />Contribution</th>
                                                <th>View Report</th>
                                            </tr>
                                        </thead>
                                        <tbody>                            
                                            <c:if test="${not empty EmployeeNPSDataReportForm.arrearbillEmpdatalist}">
                                                <c:forEach items="${EmployeeNPSDataReportForm.arrearbillEmpdatalist}" var="empdata" varStatus="count">
                                                    <tr>
                                                        <td>${count.index + 1}</td>
                                                        <td><c:out value="${empdata.billno}"/></td>
                                                        <td><c:out value="${empdata.billdesc}"/></td>
                                                        <td><c:out value="${empdata.billyear}"/></td>
                                                        <td><c:out value="${empdata.billmonth}"/></td>
                                                        <td><c:out value="${empdata.empcontribution}"/></td>
                                                        <td><c:out value="${empdata.governmentcontribution}"/></td>
                                                        <td>
                                                            <a href="NPSArrear.htm?billNo=${empdata.billno}" target="_blank">Annexure-I</a><br />
                                                            <a href="NPSArrear2.htm?billNo=${empdata.billno}" target="_blank">Annexure-II</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="tab-pane fade in" id="payslip">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Month-Year</th>
                                                <th>Basic Pay</th>
                                                <th>Total<br>Allowances</th>
                                                <th>Gross Pay</th>
                                                <th>Total<br>Deductions</th>
                                                <th>Net Pay</th>
                                                <th>View</th>
                                            </tr>
                                        </thead>
                                        <tbody>                            
                                            <c:if test="${not empty emppayslip}">
                                                <c:forEach items="${emppayslip}" var="paysliplist">
                                                    <tr>
                                                        <td>
                                                            <c:out value="${paysliplist.month_year}"/>
                                                        </td>
                                                        <td>
                                                            <c:out value="${paysliplist.basic}"/>
                                                        </td>
                                                        <td>
                                                            <c:out value="${paysliplist.totallowance}"/>
                                                        </td>
                                                        <td>
                                                            <c:out value="${paysliplist.gross}"/>
                                                        </td>
                                                        <td>
                                                            <c:out value="${paysliplist.totdeduction}"/>
                                                        </td>
                                                        <td>
                                                            <c:out value="${paysliplist.netpay}"/>
                                                        </td>
                                                        <td>
                                                            <a href="javascript:viewPaySlipDetail('<c:out value="${paysliplist.aqslno}"/>')">View</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer"></div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
