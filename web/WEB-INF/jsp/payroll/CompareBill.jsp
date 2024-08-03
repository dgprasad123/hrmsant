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
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form:form class="form-inline" action="compareBillDetails.htm" method="POST" onsubmit="return validate();" >
                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Compare Bill

                            </tr>


                            <tr>
                                <td align="right">Bill Group Name :<strong style='color:red'>*</strong></td>
                                <td colspan="3">
                                    <select name="billgroupid" id="sector"  size="1"  class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <c:forEach items="${getBillGroupList}" var="bill" >
                                            <option value="${bill.value}">${bill.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                               
                            </tr>      


                            <tr>
                                <td align="right">Year :<strong style='color:red'>*</strong></td>
                                <td >
                                    <select name="fromYear" id="fromYear"  size="1"  class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <option value="2016">2016 </option>
                                        <option value="2017">2017 </option>
                                        <option value="2018">2018 </option>
                                        <option value="2019">2019 </option>
                                        <option value="2020">2020 </option>
                                        <option value="2021">2021 </option>
                                        <option value="2022">2022 </option>
                                        <option value="2023">2023 </option>
                                        <option value="2024">2024 </option>
                                    </select>
                                </td>
                                <td align="right">Year :<strong style='color:red'>*</strong></td>
                                <td >
                                    <select name="toYear" id="toYear"  size="1"  class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <option value="2016">2016 </option>
                                        <option value="2017">2017 </option>
                                        <option value="2018">2018 </option>
                                        <option value="2019">2019 </option>
                                        <option value="2020">2020 </option>
                                        <option value="2021">2021 </option>
                                        <option value="2022">2022 </option>
                                        <option value="2023">2023 </option>
                                         <option value="2024">2024 </option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Month :<strong style='color:red'>*</strong></td>
                                <td >
                                    <select name="fromMonth" id="fromMonth"  size="1"  class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <option value="0">January </option>
                                        <option value="1">February</option>
                                        <option value="2">March</option>
                                        <option value="3">April</option>
                                        <option value="4">May</option>
                                        <option value="5">June</option>
                                        <option value="6">July</option>
                                        <option value="7">August</option>
                                        <option value="8">September</option>
                                        <option value="9">October</option>
                                        <option value="10">November</option>
                                        <option value="11">December</option>
                                    </select>
                                </td>
                                <td align="right">Month :<strong style='color:red'>*</strong></td>
                                <td >
                                    <select name="toMonth" id="toMonth"  size="1"  class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <option value="0">January </option>
                                        <option value="1">February</option>
                                        <option value="2">March</option>
                                        <option value="3">April</option>
                                        <option value="4">May</option>
                                        <option value="5">June</option>
                                        <option value="6">July</option>
                                        <option value="7">August</option>
                                        <option value="8">September</option>
                                        <option value="9">October</option>
                                        <option value="10">November</option>
                                        <option value="11">December</option>
                                    </select>
                                </td>

                            </tr>


                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" name="Searc" value="Search" class="btn btn-success" />
                           
                        </div>
                    </form:form>
                </div>

            </div>
        </div>
    </body>
</html>
