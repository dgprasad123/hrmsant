<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    int pageNo = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: ABSENTEE  STATEMENT ::</title>
        <style type="text/css">
            .pgHeader{
                font-size:15px;
                font-family:verdana;
                font-weight: bold;
            }
            .tblHeader{
                font-size:12px;
                font-family:verdana;
                font-weight: bold;
                border-top:1px solid black;
                border-bottom:1px solid black;
                border-left:1px solid black;
                border-right:1px solid black;
            }
            .printData{
                font-family:verdana; 
                font-size:12px;
                text-transform: none;
                text-align: left;
            }
        </style>
        <style type="text/css" media="print">            
            table#reportgrid{
                font-family: Verdana,Arial,Helvetica,sans-serif;
                color:#000000;
                empty-cells: show;
                border-collapse: collapse;
            }
            
            table#reportgrid th{
                background-color: #E9E9EA;
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 10px;
                height: 20px;
            }
            table#reportgrid td{                
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                padding-left:5px;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 10px;
                height: 20px;
            }
            .alternateTR{               
                background-color:#E2F4FA;                
            }
            .alternateNormalTR{               
                background-color:#FFFFFF;                
            }
            
            .lastcolumn{                               
                border-right: 1px solid #091470;               
            }
            .reportHeader{
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                border-right: 1px solid #091470;
            }
            .showinprinting{
            }
            .comboshow
            {
                 display:none;
            }
        </style>
        <style type="text/css" media="screen">
            table#reportgrid{
                empty-cells: show;
                border-collapse: collapse;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                color:#000000;
            }
            .showinprinting{
            }
            
            table#reportgrid th{
                background-color: #E9E9EA;
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 10px;
                height: 20px;
            }
            table#reportgrid td{                
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                padding-left:10px;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 10px;
                height: 20px;
            }
            .alternateTR{               
                background-color:#E2F4FA;                
            }
            .alternateNormalTR{               
                background-color:#FFFFFF;                
            }
            .lastcolumn{                               
                border-right: 1px solid #091470;               
            }            
            .reportHeader{
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                border-right: 1px solid #091470;
            }
        </style>
    </head>
    <body>
    <div style="width:90%;margin: 0 auto;">
        <table width="100%" border="0">
            <tr>
                <td style="text-align:center" class="pgHeader"><b>Form No. U.T.C. 23</b></td>
            </tr>
            <tr>
                <td style="text-align:center" class="pgHeader"><b>ABSENTEE STATEMENT</b></td>
            </tr>
            <tr>
                <td style="text-align:center" class="pgHeader"><b>[ See Subsidiary Rule 223 ]</b></td>
            </tr>
            <tr>
                <td><c:set var="pgNo" value="1" /></td>
            </tr>
        </table>
    </div>
    
    <div style="width:99%;margin: 0 auto;font-size:13px; font-family:verdana;">
        <table border="1" width="100%"  cellspacing="0" cellpadding="0" id="reportgrid" style="font-size:12px; font-family:verdana;">
            <thead></thead>
            <tr style="height: 30px" class="tblHeader">   
                <th rowspan="2">Name of Absentee</th>
                <th rowspan="2">Actual Rate of Pay</th>
                <th rowspan="2">Designation <br> and rate of <br> pay of <br> vaccant post</th>
                <th colspan="4">Nature of Absence</th>
                <th rowspan="2">Rate of <br> Absentee <br> allowance <br> per month</th>
                <th rowspan="2">(To be <br> filled <br> up by <br> audit <br> office)</th>
                <th colspan="4">Officiating Government Servant (if any)</th>
                <th rowspan="2">(To be <br> filled <br> up by <br> audit <br> office)</th>
            </tr>
            <tr style="height: 30px" class="showinprinting">
                <th>Kind</th>
                <th>Period</th>
                <th>From <br> A.M. <br> or <br> P.M.</th>
                <th>To <br> A.M. <br> or <br> P.M.</th>
                <th>Name</th>
                <th>Substantive <br> Post</th>
                <th>Substantive <br> Pay</th>
                <th>Officiating <br> Pay</th>
            </tr>
            <tr style="height: 30px" class="showinprinting">
                <th width="10%">1</th>
                <th width="5%">2</th>
                <th width="10%">3</th>
                <th width="4%">4</th>
                <th width="10%">5</th>
                <th width="4%">6</th>
                <th width="4%">7</th>
                <th width="5%">8</th>
                <th width="5%">9</th>
                <th width="10%">10</th>
                <th width="10%">11</th>
                <th width="10%">12</th>
                <th width="5%">13</th>
                <th width="8%">14</th>
            </tr>
            <c:if test="${not empty ASEmpList}">
                <c:forEach var="ASeachEmp" items="${ASEmpList}">
                    <c:if test="${not empty ASeachEmp.helperList}">
                        <c:forEach var="eachEmpData" items="${ASeachEmp.helperList}">
                            <tr style="height:20px">
                                <td align="left"><c:out value="${eachEmpData.nameofAbsentee}"/>/<br/><c:out value="${eachEmpData.gpfNo}"/>&nbsp;</td>
                                <td align="left">&nbsp;<c:out value="${eachEmpData.rateofPay}"/></td>
                                <td align="left">&nbsp;</td>
                                <td align="left">&nbsp;</td>
                                <td align="left">&nbsp;<c:out value="${eachEmpData.period}"/></td>
                                <td align="left">&nbsp;</td>
                                <td align="left">&nbsp;</td>
                                <td align="left">&nbsp;</td>
                                <td align="left">&nbsp;</td>
                                <td align="left">&nbsp;<c:out value="${eachEmpData.empname}"/></td>
                                <td align="left">&nbsp;<c:out value="${eachEmpData.subPost}"/></td>
                                <td align="left">&nbsp;</td>
                                <td align="left">&nbsp;</td>
                                <td align="left">&nbsp;</td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </c:forEach>
            </c:if>    
        </table>
    </div>  
</body>
</html>
