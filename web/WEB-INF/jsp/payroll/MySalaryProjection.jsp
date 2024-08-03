<%-- 
    Document   : MySalaryProjection
    Created on : Aug 1, 2023, 11:18:58 AM
    Author     : Madhusmita
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min-1.9.1.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.freezeheader.js"></script>


        <script type="text/javascript">
            $(document).ready(function() {
                $("#dataTab").freezeHeader();
            });
            function downloadExcel() {
                window.location = "AnnualIncomeTAXExcelReport.htm?fyear=" + $('#finyear').val() + "&empid=" + $('#hidempid').val();
            }

        </script>
        <style>
            table th {
                padding-top: 12px;
                padding-bottom: 12px;
                //text-align: left;
                background-color: #a2d4ed;
                //color: white;
            }
            .tablRow{
                 padding-top: 12px;
                padding-bottom: 12px;
                //text-align: left;
                background-color: #a2d4ed;
            }
            span {
                content: "\20B9";
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <form:form action="mysalaryProjectionReport.htm" commandName="BillBean" method="post">
                        <form:hidden id="hidempid" path="hidempid"/>
                        <div class="row">
                            <div class="form-group">
                                <div style="text-align:center;"><h2><u>Drawal Particulars</u></h2> </div>
                            </div>
                        </div>
                        <div class="row">
                            <div style="height:30px;">
                                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;margin-top: 15px;">
                                    <tr>
                                        <td align="center" width="50%"style="font-family:Verdana;font-size:15px;font-weight: bold;">
                                            <span>Employee Name</span>:&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${empnm}"/>
                                        </td>
                                        <td align="center" width="50%" style="font-family:Verdana;font-size:15px;font-weight: bold;">
                                            <span>GPF No</span>:&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${gpfNo}"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div></br>
                        <div class="row">
                            <table>
                                <tr>
                                    &nbsp;&nbsp;
                                    <td  align="right" class="col-lg-5" style="font-family:Verdana;font-size:15px;font-weight: bold;">
                                        <label class="control-label">Select Financial Year:</label></td>                      
                                    <td align="center" class="col-lg-4" style="font-weight:bold;font-size: 14px">
                                        <form:select path="finyear" id="finyear" class="form-control">
                                            <form:option value = "" label = "-----Select Year-----"/>
                                            <%-- <form:options items = "${billyear}" itemValue="billYear" itemLabel="billYear"/>--%>
                                            <form:option value="2014-2015">2014-2015</form:option>
                                            <form:option value="2015-2016">2015-2016</form:option>
                                            <form:option value="2016-2017">2016-2017</form:option>
                                            <form:option value="2017-2018">2017-2018</form:option>
                                            <form:option value="2018-2019">2018-2019</form:option>
                                            <form:option value="2019-2020">2019-2020</form:option>
                                            <form:option value="2020-2021">2020-2021</form:option>
                                            <form:option value="2021-2022">2021-2022</form:option>
                                            <form:option value="2022-2023">2022-2023</form:option>
                                        </form:select>
                                    </td> 
                                    <td class="col-lg-1" align="center"> 
                                        <button type="submit" class="btn btn-success" ><span style="color:black;">Search</span></button>
                                    </td>
                                    <td class="col-lg-2" align="center">
                                        <button type="button" name="btnDownload" value="" class="btn btn-primary" onclick="downloadExcel()">Download Excel</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <br/>
                </form:form>

                <div class="panel-body">                                
                    <table  class="table table-bordered table-hover" id="dataTab">
                        <thead>  
                            <tr>
                                <th  style="text-align:center;">Sl No</th>
                                <th  style="text-align:center;">Bill No.</th>
                                <th  style="text-align:center;">Month / Year</th>
                                <th  style="text-align:center;">Voucher No/<br/>Voucher Date</th>
                                <th  style="text-align:center;">Bill Type</th>
                                <th  style="text-align:center;">Gross<br/>In <span>&#8377;</span> </th>
                                <th  style="text-align:center;">Deduction <br/>In <span>&#8377;</th>
                                <th  style="text-align:center;">Net Pay <br/>In <span>&#8377;</th>
                                    <%--<th  style="text-align:center;">Increment Type</th>
                                    <th  style="text-align:center;">Incremented Pay</th>--%>
                                <th  style="text-align:center;">View Details</th>
                            </tr>
                        </thead>
                        <tbody  style="overflow-y: auto">
                            <c:set var="totGross" value="${0}"/>
                            <c:set var="totDed" value="${0}"/>
                            <c:set var="totNet" value="${0}"/>
                            <c:forEach var="salList" items="${salDetails}" varStatus="count">
                                <c:if test="${salList.hasIncrement eq 'Y'}">
                                    <tr style="font-size: 14px;font-family:serif;background:#ff9999;">
                                        <td style="text-align:center;"><c:out value="${count.index+1}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billno}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billmonthNm}"/>
                                        <td style="text-align:center;"><c:out value="${salList.voucherno}"/><br/><c:out value="${salList.voucherdate}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billtype}"/><br/>(<c:out value="${salList.incrementReason}"/>)</td>
                                        <td style="text-align:center;"><c:out value="${salList.billGross}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billDed}"/></td>                                            
                                        <td style="text-align:center;"><c:out value="${salList.billNet}"/></td>
                                        <%--<td style="text-align:center;"><c:out value="${salList.incrementReason}"/></td>                                            
                                        <td style="text-align:center;"><c:out value="${salList.incrBasic}"/></td>--%>
                                        <c:if test="${salList.billtype ne 'PAY'}">
                                            <td style="text-align:center;"><a href="browseArrAqData.htm?aqslno=${salList.aqslno}&billNo=${salList.billno}" class="btn btn-default" target="_blank">
                                                    <img src="images/view_icon.png" alt="View Detail"/></a> </td>
                                                </c:if>
                                                <c:if test="${salList.billtype eq 'PAY'}">
                                            <td style="text-align:center;"><a href="PaySlipDetail.htm?aqlsno=${salList.aqslno}&sltYear=${salList.billYear}&sltMonth=${salList.billMonth}&empid=${empid}" class="btn btn-default" target="_blank">
                                                    <img src="images/view_icon.png" alt="View Detail"/></a></td>

                                        </c:if>

                                    </tr>

                                </c:if>
                                <c:if test="${salList.hasIncrement ne 'Y'}">
                                    <tr style=" font-size: 14px;font-family:serif; ">
                                        <td style="text-align:center;"><c:out value="${count.index+1}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billno}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billmonthNm}"/>
                                        <td style="text-align:center;"><c:out value="${salList.voucherno}"/><br/><c:out value="${salList.voucherdate}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billtype}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billGross}"/></td>
                                        <td style="text-align:center;"><c:out value="${salList.billDed}"/></td>                                            
                                        <td style="text-align:center;"><c:out value="${salList.billNet}"/></td>
                                        <%--<td style="text-align:center;"><c:out value="${salList.incrementReason}"/></td>                                            
                                        <td style="text-align:center;"><c:out value="${salList.incrBasic}"/></td>--%>
                                        <c:if test="${salList.billtype ne 'PAY'}">
                                            <td style="text-align:center;"><a href="browseArrAqData.htm?aqslno=${salList.aqslno}&billNo=${salList.billno}" class="btn btn-default" target="_blank">
                                                    <img src="images/view_icon.png" alt="View Detail"/></a> </td>
                                                </c:if>
                                                <c:if test="${salList.billtype eq 'PAY'}">
                                            <td style="text-align:center;"><a href="PaySlipDetail.htm?aqlsno=${salList.aqslno}&sltYear=${salList.billYear}&sltMonth=${salList.billMonth}&empid=${empid}" class="btn btn-default" target="_blank">
                                                    <img src="images/view_icon.png" alt="View Detail"/></a> </td>
                                                </c:if>
                                    </tr>
                                </c:if>
                                <c:set var="totGross" value="${totGross+salList.billGross}"/>
                                <c:set var="totDed" value="${totDed+salList.billDed}"/>
                                <c:set var="totNet" value="${totNet+salList.billNet}"/>
                            </c:forEach>
                            <tr style="font-size: 14px;font-family:serif;background:#a2d4ed;">
                                <td  style="text-align:center; font-weight: bold;" colspan="5">Total</td>
                                <td style="text-align:center; font-weight: bold;">${totGross}</td>
                                <td style="text-align:center; font-weight: bold;">${totDed}</td>
                                <td style="text-align:center; font-weight: bold;">${totNet}</td>
                                <td colspan="1"></td> 
                            </tr>
                     
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
