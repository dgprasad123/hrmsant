<%-- 
    Document   : NewLoanSanction
    Created on : Oct 26, 2017, 12:54:11 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

    </head>
    <body>
        <form:form action="searchQuarterDetails.htm" method="post" >
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        My Quarter Details
                    </div>        
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="year">Select Year:</label>
                            </div>
                            <div class="col-lg-5">
                                <select name="year"  class="form-control" required="1">
                                    <option value="">Select Year</option>
                                    <option value="2011" <c:if test = "${not empty year && year=='2011'}"> <c:out value='selected="selected"'/></c:if>>2011 </option>
                                    <option value="2012" <c:if test = "${not empty year && year=='2012'}"> <c:out value='selected="selected"'/></c:if>>2012 </option>
                                    <option value="2013" <c:if test = "${not empty year && year=='2013'}"> <c:out value='selected="selected"'/></c:if>>2013 </option>
                                    <option value="2014" <c:if test = "${not empty year && year=='2014'}"> <c:out value='selected="selected"'/></c:if>>2014 </option>
                                    <option value="2015" <c:if test = "${not empty year && year=='2015'}"> <c:out value='selected="selected"'/></c:if>>2015 </option>
                                    <option value="2016" <c:if test = "${not empty year && year=='2016'}"> <c:out value='selected="selected"'/></c:if>>2016 </option>
                                    <option value="2017" <c:if test = "${not empty year && year=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017 </option>
                                    <option value="2018" <c:if test = "${not empty year && year=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                    <option value="2019" <c:if test = "${not empty year && year=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                    <option value="2020" <c:if test = "${not empty year && year=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                    <option value="2021" <c:if test = "${not empty year && year=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                    <option value="2022" <c:if test = "${not empty year && year=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                    <option value="2023" <c:if test = "${not empty year && year=='2023'}"> <c:out value='selected="selected"'/></c:if>>2023 </option>
                                    <option value="2024" <c:if test = "${not empty year && year=='2024'}"> <c:out value='selected="selected"'/></c:if>>2024 </option>
                                    </select>                            
                                </div><td><a href="downloadEmpQuarterStatus.htm?year=${year}" class="btn btn-primary"><img  alt="DownloadPdf" src="images/pdf.png" title="View/Download Pdf" height="20px"/>Download</a></td>
                            </div>
                            <div class="col-lg-1">
                                <button type="submit" class="btn btn-primary" >
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                            <div style='margin-top:30px;margin-bottom: 30px'></div>       

                        </div> 


                    </div>

                    <div class="row" >
                        <table class="table table-hover">
                            <thead>
                                <tr class="success">
                                    <th>Sl No</th>
                                    <th>Month</th>
                                    <th>Quarter No</th>
                                    <th>Quarter Address</th>
                                    <th>Quarter Rent</th>
                                    <th>HRR</th>
                                    <th>WRR</th>
                                    <th>SWR</th>
                                    <th>MRR</th>
                                    <th>Bill No</th>
                                    <th>Bill Date</th>
                                    <th>Voucher No</th>
                                    <th>Voucher Date</th>
                                </tr>
                            </thead>
                        <c:forEach var="empdata" items="${empdetails}" varStatus="theCount">
                            <tr>
                                <td>${theCount.index + 1}</td>
                                <td><c:out value="${empdata.stringmonth}"/></td>
                                <td><c:out value="${empdata.qtrno}"/></td>
                                <td><c:out value="${empdata.qtraddrs}"/></td>
                                <td><c:out value="${empdata.qtr_rent}"/></td>
                                <td><c:out value="${empdata.hrr}"/></td>
                                <td><c:out value="${empdata.wrr}"/></td>
                                <td><c:out value="${empdata.swr}"/></td>
                                <td><c:out value="${empdata.mrr}"/></td>
                                <td><c:out value="${empdata.bill_no}"/></td>
                                <td><c:out value="${empdata.bill_date}"/></td>
                                <td><c:out value="${empdata.vch_no}"/></td>
                                <td><c:out value="${empdata.vch_date}"/></td>

                            </tr>
                        </c:forEach>  

                    </table>
                </div>            


            </div>
        </form:form>

    </body>
</html>
