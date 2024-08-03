<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .table > tbody > tr > td, .table > tbody > tr > th{padding:0px 8px 0px 8px;}
        </style>
    </head>
    <body style="margin:10px;">
        <table style="font-size:10pt;" width="100%" class="table table-bordered">
            <tr>
                <td colspan="14" style="text-align:center;background:#EAEAEA;" bgcolor="#EAEAEA">
                    <c:if test="${not empty HeaderDataList}">
                        <c:forEach var="hdrData" items="${HeaderDataList}">
                            <b> REVISED ARREAR PAY BILL  FROM <c:out value="${hdrData.fromMonth}"/> / <c:out value="${hdrData.fromYear}"/> TO 
                                <c:out value="${hdrData.toMonth}"/> / <c:out value="${hdrData.toYear}"/> OF MISCELLANEOUS, 
                                <c:out value="${OffName}"/>, <c:out value="${DeptName}"/>
                                Bill No:- <c:out value="${hdrData.billDesc}"/> </b>
                            </c:forEach>
                        </c:if>                                
                </td>
            </tr>
            <tr>
                <td colspan="8" style="font-size:11pt;"><strong>Name : </strong>${arrAqMastBean.empName} <b style="color: #0000FF;">(${arrAqMastBean.empCode})</b></td>
                <td colspan="6" style="font-size:11pt;"><strong>Designation : </strong>${arrAqMastBean.curDesg}</td>
            </tr>
            <tr>
                <th rowspan="2"  width="3%">Sl No </th>
                <th rowspan="2" width="9%" style="text-align:center;">Month</th>
                <th colspan="4" width="18%" style="text-align:center;">Due</th>
                <th colspan="4" width="18%" style="text-align:center;">Drawn</th>
                <th rowspan="2" width="11%">Drawn Vide <br/> Bill No.</th>
                <th rowspan="2" width="8%" style="text-align:right;">Arrear 100%</th>                
            </tr>
            <tr>
                <th>Pay</th>
                <th>G.P</th>
                <th>D.A</th>
                <th>Total</th>
                <th>Pay</th>
                <th>G.P</th>
                <th>D.A</th>
                <th>Total</th>
            </tr>
            <c:if test="${not empty arrAqMastBean.arrDetails}">
                <c:forEach var="arrAqDtls" items="${arrAqMastBean.arrDetails}" varStatus="cnt">
                    <tr style="height: 25px" id="${arrAqDtls.aqslno}_${arrAqDtls.payMonth}_${arrAqDtls.payYear}">
                        <td>${cnt.index+1}</td>
                        <td nowrap style="text-align:center;"><c:out value="${arrAqDtls.payMonthName}"/> - <c:out value="${arrAqDtls.payYear}"/></td>
                        <td><c:out value="${arrAqDtls.duePayAmt}"/></td> 
                        <td><c:out value="${arrAqDtls.dueGpAmt}"/></td>
                        <td><c:out value="${arrAqDtls.dueDaAmt}"/></td>
                        <c:set var = "duetotal" value = "${arrAqDtls.dueTotalAmt}"/>
                        <td style="font-weight: bold;">${arrAqDtls.dueTotalAmt}</td> 

                        <td><c:out value="${arrAqDtls.drawnPayAmt}"/></td> 
                        <td><c:out value="${arrAqDtls.drawnGpAmt}"/></td>
                        <td><c:out value="${arrAqDtls.drawnDaAmt}"/></td>
                        <c:set var = "drwantotal" value="${arrAqDtls.drawnTotalAmt}"/>
                        <td style="font-weight: bold;">${arrAqDtls.drawnTotalAmt}</td>

                        <td><c:out value="${arrAqDtls.drawnBillNo}"/></td>

                        <td align="center" style="font-weight: bold;text-align:right;">${arrAqDtls.arrear100}</td>

                    </tr> 
                </c:forEach>
            </c:if>  
            <tr>
                <th colspan="11" style="text-align: right;"> Grand Total &nbsp;&nbsp;&nbsp;&nbsp;</th>
                <th width="8%" style="text-align: right;"><c:out value="${arrAqMastBean.grandTotArr100}"/></th>                
            </tr>
            <tr>
                <th colspan="11" style="text-align: right;"> Arrear ${arrAqMastBean.percentageArraer}% &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;"><c:out value="${arrAqMastBean.grandTotArr60}"/></th>                
            </tr>
            <tr>
                <th colspan="11" style="text-align: right;"> Income Tax &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;">${arrAqMastBean.incomeTaxAmt}</th>                 
            </tr>
            <tr>
                <th colspan="11" style="text-align: right;"> CPF &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;">${arrAqMastBean.cpfHead}</th>                 
            </tr>                          
            <tr>
                <th colspan="11" style="text-align: right;"> PT &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;">${arrAqMastBean.pt}</th>                 
            </tr>                        
            <tr>
                <th colspan="11" style="text-align: right;background-color: #F5F5F5;"> Net Payable &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;background-color: #F5F5F5;">${(arrAqMastBean.grandTotArr60) - (arrAqMastBean.incomeTaxAmt) - (arrAqMastBean.pt)-(arrAqMastBean.cpfHead)}</th>                
            </tr>
        </table>
    </body>
</html>