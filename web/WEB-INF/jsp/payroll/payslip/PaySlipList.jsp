<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PaySlip List Page</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function showPaylist() {
                var year = $('#sltYear').val();
                var month = $('#sltMonth').val();

                if (year == "") {
                    alert("Please select Year");
                    return false;
                }

                if (month == "") {
                    alert("Please select Month");
                    return false;
                }
            }

            function viewPaySlipDetail(aqslno) {
                var year = $('#sltYear').val();
                var month = $('#sltMonth').val();
                var url = "PaySlipDetail.htm?aqlsno=" + aqslno + "&sltYear=" + year + "&sltMonth=" + month + "&empid=" + $('#empid').val();
                window.open(url, "_blank");
                //return "<a href='PaySlipDetail.htm?aqlsno=" + aqslno + "&sltYear=" + year + "&sltMonth=" + month + "&empid=" + $('#empid').val() + "' target='_blank'>View</a>";
            }
        </script>
    </head>
    <body>
        <form:form action="GetPaySlip.htm" method="POST" commandName="payslipform">
            <form:hidden path="empId" id="empid" value="${empid}"/>
           
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <c:if test="${payslipform.isforeignBody eq 'Y'}">
                            <h4 style="text-align: center"><u>${employeeDetails.empName} </u></h4>
                            <h4 style="text-align: center"><u>${employeeDetails.gpfNo} / ${employeeDetails.empid} </u></h4>
                        </c:if>
                        <table class="table table-bordered">                           
                            
                            <tr>
                                <td width="20%" align="center">
                                    Select Year:
                                </td>
                                <td width="20%">
                                    <form:select path="sltYear" id="sltYear" class="form-control">
                                        <form:option value="">--Select--</form:option>
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
                                </td>
                                <td width="20%" align="center">
                                    Select Month:
                                </td>
                                <td width="20%">
                                    <form:select path="sltMonth" id="sltMonth" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="0">Jan</form:option>
                                        <form:option value="1">Feb</form:option>
                                        <form:option value="2">Mar</form:option>
                                        <form:option value="3">Apr</form:option>
                                        <form:option value="4">May</form:option>
                                        <form:option value="5">Jun</form:option>
                                        <form:option value="6">Jul</form:option>
                                        <form:option value="7">Aug</form:option>
                                        <form:option value="8">Sep</form:option>
                                        <form:option value="9">Oct</form:option>
                                        <form:option value="10">Nov</form:option>
                                        <form:option value="11">Dec</form:option>
                                    </form:select>
                                </td>
                                <td width="20%" align="center">
                                    <button type="submit" class="btn btn-danger" onclick="showPaylist()">Ok</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <%--<thead>
                                <tr>
                                    <th data-options="field:'aqslno',width:20,hidden:true">AQSLNo</th>
                                    <th data-options="field:'month_year',width:80">Month-Year</th>
                                    <th data-options="field:'basic',width:150">Basic Pay</th>
                                    <th data-options="field:'totallowance',width:150">Total<br>Allowances</th>
                                    <th data-options="field:'gross',width:200">Gross Pay</th>
                                    <th data-options="field:'totdeduction',width:150">Total<br>Deductions</th>
                                    <th data-options="field:'netpay',width:100">Net Pay</th>
                                    <th data-options="field:'temp',width:100,formatter:viewPaySlipDetail">View</th>
                                </tr> 
                            </thead>--%>
                            <thead>
                                <tr>
                                    <th width="15%">Month-Year</th>
                                    <th width="15%">Basic Pay</th>
                                    <th width="15%">Total<br>Allowances</th>
                                    <th width="15%">Gross Pay</th>
                                    <th width="15%">Total<br>Deductions</th>
                                    <th width="15%">Net Pay</th>
                                    <th width="15%">View</th>
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

                    <div class="panel-footer">
                        <span style="color:red">Pay Slips of only Token Generated Bills are shown in the list.</span>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

