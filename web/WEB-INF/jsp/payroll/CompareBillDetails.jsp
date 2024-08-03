<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        

        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script type="text/javascript">
            function validate() {
                var billgroupid = $("#billgroupid").val();
                if (billgroupid == "") {
                    alert("Please Select Bill Group");
                    return false;
                }
                var fromYear = $("#fromYear").val();
                if (fromYear == "") {
                    alert("Year Field cannot be blank");
                    return false;
                }
                var toYear = $("#toYear").val();
                if (toYear == "") {
                    alert("Year Field cannot be blank");
                    return false;
                }
                var fromMonth = $("#fromMonth").val();
                if (fromMonth == "") {
                    alert("Month Field cannot be blank");
                    return false;
                }
                var toMonth = $("#toMonth").val();
                if (toMonth == "") {
                    alert("Month Field cannot be blank");
                    return false;
                }
            }
        </script>
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
                    <form:form class="form-inline" action="compareBillDetails.htm" method="POST" commandName="GroupSection" onsubmit="return validate();" >
                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA;">
                                <td colspan="4">Compare Bill</td>
                            </tr>
                            <tr>
                                <td align="right">Bill Group Name :<strong style='color:red'>*</strong></td>
                                <td colspan="3">
                                    <form:select path="billgroupid" id="sector" class="form-control" style="width:80%;">
                                        <option value="">-Select-</option>
                                        <form:options itemLabel="label" itemValue="value" items="${getBillGroupList}"/>
                                    </form:select>
                            </td>
                            </tr>
                            <tr>
                                <td align="right">Year :<strong style='color:red'>*</strong></td>
                                <td>
                                    <form:select path="fromYear" id="fromYear" class="form-control"  style="width:80%;">
                                <option value="">-Select-</option>
                                <form:option value="2016"> 2016 </form:option>
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
                            <td align="right">Year :<strong style='color:red'>*</strong></td>
                            <td>
                                <form:select path="toYear" id="toYear" class="form-control"  style="width:80%;">
                                <option value="">-Select-</option>
                                <form:option value="2016"> 2016 </form:option>
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
                            </tr>
                            <tr>
                                <td align="right">Month :<strong style='color:red'>*</strong></td>
                                <td>
                                    <form:select path="fromMonth" id="fromMonth" class="form-control"  style="width:80%;">
                                <option value="">-Select-</option>
                                <form:option value="0">January </form:option>
                                <form:option value="1">February</form:option>
                                <form:option value="2">March</form:option>
                                <form:option value="3">April</form:option>
                                <form:option value="4">May</form:option>
                                <form:option value="5">June</form:option>
                                <form:option value="6">July</form:option>
                                <form:option value="7">August</form:option>
                                <form:option value="8">September</form:option>
                                <form:option value="9">October</form:option>
                                <form:option value="10">November</form:option>
                                <form:option value="11">December</form:option>
                            </form:select>
                            </td>
                            <td align="right">Month :<strong style='color:red'>*</strong></td>
                            <td>
                                <form:select path="toMonth" id="toMonth"  class="form-control"  style="width:80%;">
                                <option value="">-Select-</option>
                                <form:option value="0">January </form:option>
                                <form:option value="1">February</form:option>
                                <form:option value="2">March</form:option>
                                <form:option value="3">April</form:option>
                                <form:option value="4">May</form:option>
                                <form:option value="5">June</form:option>
                                <form:option value="6">July</form:option>
                                <form:option value="7">August</form:option>
                                <form:option value="8">September</form:option>
                                <form:option value="9">October</form:option>
                                <form:option value="10">November</form:option>
                                <form:option value="11">December</form:option>
                            </form:select>
                            </td>

                            </tr>


                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" name="Searc" value="Search" class="btn btn-success" />

                        </div>
                    </form:form>

                    <div  class="alert alert-success"><h5 style='success'>Bill Compare Details</h5></div>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th colspan='4' align='center'><strong>${monthText},${fromYear}</strong></th>
                                <th colspan='2' align='center'><strong>${monthText1},${toYear}</strong></th>
                            </tr>    
                            <tr>
                                <th width="10%">SL NO</th>
                                <th width="20%">Employee</th>
                                <th width="15%">Gross Amount</th>
                                <th width="15%">Net Amount</th>
                                <th width="15%">Gross Amount</th>
                                <th width="15%">Net Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${compareData}" var="group" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${group.empName}</td>
                                    <td>${group.grossAmount}</td>
                                    <td>${group.netAmount}</td>
                                    <td>${group.grossAmount1}</td>
                                    <td>
                                        <c:if test="${group.netAmount ne group.netAmount1}">
                                            <span style="color:#FF0000;">${group.netAmount1}</span>
                                        </c:if>
                                        <c:if test="${group.netAmount eq group.netAmount1}">
                                            <span style="color:#008000;">${group.netAmount1}</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>   
                </div>
            </div>
        </div>
    </body>
</html>
