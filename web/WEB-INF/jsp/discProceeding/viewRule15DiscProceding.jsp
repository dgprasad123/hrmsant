
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
    int i = 0;
    int j = 0;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>
        <link rel="stylesheet" type="text/css" href="css/hrmis.css" />

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <div align="center" style="padding: 3px;margin-top:5px;margin-bottom:10px;border-radius: 5px;border:1px solid #5095ce;width:98%; margin: 0px auto;">
            <div align="center" style="border-radius: 5px;">
                <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                    <tr>
                        <td style="background-color:#5095ce;color:#FFFFFF;padding:0px;font-weight:bold;" align="center">
                            <h2>DISCIPLINARY PROCEEDING</h2></td>
                    </tr>                        
                </table>
            </div>
        </div>
        <div style="padding: 3px;margin-top:7px;margin-bottom:5px;border-radius: 5px;border:1px solid #5095ce;width:98%; margin: 0px auto;">
            <div style="background-color:#5095ce;color:#FFFFFF;padding:3px;font-weight:bold;" align="left">MEMORANDUM</div>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tableview">
                <tr>
                    <td colspan="2" align="center" style="font-weight: bold;font-size: 15px;">Government of Odisha
                        <br/>[<c:out value="${viewDP.deptName}"/> DEPARTMENT]
                    </td>
                </tr> 
                <tr>
                    <td align="left" style="padding-left: 30px;"><b>Memorandum No: <c:out value="${viewDP.rule15OrderNo}"/></b></td>
                    <td align="center"><b>Date: <c:out value="${viewDP.rule15OrderDate}"/></b></td>
                </tr> 
                <tr>
                    <td colspan="2" align="center" style="font-weight: bold;font-size: 15px;text-decoration: underline">MEMORANDUM</td>
                </tr>
            </table>
 
            <table width="100%" cellpadding="0" cellspacing="0" class="tableview" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                <tr>
                    <td><p style="text-align: justify;line-height: 20px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sri / Smt 
                            <c:forEach items="${proceedingbean.employees}" var="employee">
                                            ${employee.fullname}
                                        </c:forEach>
                            <span style="font-weight: bold;">
                                <c:out value="${viewDP.doEmpName}"/></span> is hereby informed that it is proposed to hold an inquiry against 
                            him/her under Rule 15 of the Orissa Civil Services (Classification , Control and Appeal) Rules, 1962. The substance 
                            of the imputation in respect of which the inquery is proposed to be held is set out in the enclosed statement of 
                            articles of charges (Annexure-I). The statement of imputation in support ot the article of charges is enclosed 
                            (Annexure-II) along with Memo of Evidence (Annexure-III).
                        </p>
                    </td>
                </tr>
                <tr>
                    <td><p style="text-align: justify;padding-left: 10px;line-height: 20px;">2. Sri/ Smt. <span style="font-weight: bold;"><c:out value="${viewDP.doEmpName}"/></span> 
                            is directed to submit his/her written statement of defence within 30 dayf from the date of receipt of this memorandum 
                            and also to state if he/she desires to be heard in person. 
                        </p>
                    </td>
                </tr>
                <tr>
                    <td><p style="text-align: justify;padding-left: 10px;line-height: 20px;">3. If he/she fails to submit his/her written statement of defence within the stipulated 
                            period of 30 days from the date of receipt of this memorandum, it will be presumed that he/she has no explanation to 
                            offer and action will be taken as deemed proper ex-parte.
                        </p>
                    </td>
                </tr>
                <tr>
                    <td><p style="text-align: justify;padding-left: 10px;line-height: 20px;">4. The receipt of the memorandum should be acknowledged by Sri/Smt. 
                            <span style="font-weight: bold;"><c:out value="${viewDP.doEmpName}"/></span>.
                        </p>
                    </td>
                </tr>
            </table> 

            <table width="100%" cellpadding="0" cellspacing="0" class="tableview" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                <tr>
                    <td width="50%">&nbsp;</td>
                    <td width="50%" align="center">by Order of the Governor</td>
                </tr>
                <tr style="height:10px;">
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td align="center">Commissioner-cum-Secretary to Govt.</td>
                </tr>
            </table>
        </div>    
        
        <div style="padding: 3px;margin-top:7px;margin-bottom:5px;border-radius: 5px;border:1px solid #5095ce;width:98%; margin: 0px auto;">
            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">ANNEXURE-I</div>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tableview">
                <tr>
                    <td align="center" style="font-weight: bold;font-size: 15px;">ANNEXURE-I</td>
                </tr> 
                <tr>
                    <td align="center" style="font-weight: bold;font-size: 15px;">ARTICLES OF CHARGE</td>
                </tr> 
            </table>

            <table width="100%" cellpadding="0" cellspacing="0" class="tableview" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                <tr>
                    <td colspan="2"><p style="text-align: justify;padding-left: 20px;">
                            Sri <span style="font-weight: bold;"><c:out value="${viewDP.doEmpName}"/></span> has committed following irregularities.</p>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><p style="text-align: justify;padding-left: 20px;">
                            *(That <span style="font-weight: bold;"><c:out value="${viewDP.irrgularDetails}"/></span>)</p>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><p style="text-align: justify;padding-left: 20px;">Thus, the following articles of charge are framed against him/her for violation of Rule 15 (Major)
                                 of the Orissa Government Servants Conduct Rules, 1959.
                        </p>
                    </td>
                </tr>
                <c:forEach var="chargeList" items="${viewDP.chargeListOnly}">
                    <% i++;%>
                    <tr style="height:15px;">
                        <td width="2%" style="padding-left: 20px;"><%=i%>.</td>
                        <td width="90%"><c:out value="${chargeList.field}"/></td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="2"><hr/></td>
                </tr>    
                <tr>
                    <td colspan="2"><p style="text-align: justify;">
                            * To be used in cases where the appointing/Disciplinary Authority are the Government.</p>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><p style="text-align: justify;">
                            * Define and distinct article of charge drawn from substance of imputations of misconduct of misbehavior.</p>
                    </td>
                </tr>
            </table> 
        </div>  
                      
        <div style="padding: 3px;margin-top:7px;margin-bottom:5px;border-radius: 5px;border:1px solid #5095ce;width:98%; margin: 0px auto;">
            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">ANNEXURE-II</div>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tableview">
                <tr>
                    <td align="center" style="font-weight: bold;font-size: 15px;">ANNEXURE-II</td>
                </tr> 
                <tr>
                    <td align="center" style="font-weight: bold;font-size: 15px;">STATEMENT OF IMPUTATIONS OF MISCONDUCT</td>
                </tr> 
            </table>

            <table width="100%" cellpadding="0" cellspacing="0" class="tableview" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                <tr style="margin-top: 5px;">
                    <td colspan="2"><p style="text-align: justify;">&nbsp;&nbsp;&nbsp;Statement of imputations of misconduct in support of the articles of 
                            charge framed against Sri <span style="font-weight:bold;">
                                <c:out value="${viewDP.doEmpName}"/>, <c:out value="${viewDP.doEmpCurDegn}"/></span>.</p>
                    </td>
                </tr>
                
                <c:forEach var="chargeDtlsList" items="${viewDP.chargeDtlsList}">
                    <% j++;%>
                    <tr style="height:15px;">
                        <td width="2%" style="padding-left: 20px;"><%=j%>.</td>
                        <td width="90%"><c:out value="${chargeDtlsList.field}" escapeXml="false"/></td>
                    </tr>
                </c:forEach>
            </table> 
        </div>
        
        <div style="padding: 3px;margin-top:7px;margin-bottom:5px;border-radius: 5px;border:1px solid #5095ce;width:98%; margin: 0px auto;">
            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">ANNEXURE-III</div>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tableview">
                <tr>
                    <td align="center" style="font-weight: bold;font-size: 15px;">ANNEXURE-III</td>
                </tr> 
                <tr>
                    <td align="center" style="font-weight: bold;font-size: 15px;">MEMOS OF EVIDENCE</td>
                </tr> 
            </table>
            
            <table width="100%" cellpadding="1" cellspacing="1"  border="0" style="background-color: #5095CE;border: 2px solid #0A78D8;font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                <tr style="color:#FFFFFF;">
                    <th width="25%">Articles of Charge</th>
                    <th width="25%">Documents by which the charge is<br/> proposed to be sustained</th>
                    <th width="50%">Witness by whom the charge is proposed to sustained</th>
                </tr>
                <c:forEach var="eachCharge" items="${viewDP.chargeList}">
                    <tr style="background-color: #EEF2F7;">
                        <td><c:out value="${eachCharge.charge}"/></td>
                        <td><c:out value="${eachCharge.orgFileName}"/></td>
                        <td><c:out value="${eachCharge.witnessName}" escapeXml="false"/></td>
                    </tr>
                </c:forEach>
            </table>    
        </div>
        
    </body>
</html>

