<%-- 
    Document   : EmployeeLTAdvanceList
    Created on : 16 Sep, 2019, 2:37:36 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
        <script type="text/javascript"  src="js/jquery.min-1.9.1.js"></script>
        
        <style media="print">
            .hide-on-screen {display:none;}
            .yesPrint, .noPrint {display:block;}
        </style> 
        
        <script type="text/javascript">
            $(document).ready(function () {

            });

            function validate() {
                
            }

        </script>
    </head>
    <body>
        <div id="page-wrapper">     

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Long Term Advance List
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <div class="row"> 
                        <div class="col-sm-12 hide-on-screen">
                            <form:form action="getEmployeeLTAdvanceList.htm" commandName="ltAdvance" method="POST" onsubmit="return validate()">
                                <table border="0" width="80%" class="table" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                    <tr>
                                        <td>
                                            <strong>Financial Year:</strong>
                                            <form:select class="form-control" path="finYear" id="finYear">
                                                <form:option value="2022-2023">2022-2023</form:option>
                                                <form:option value="2021-2022">2021-2022</form:option>
                                                <form:option value="2020-2021">2020-2021</form:option>
                                                <form:option value="2019-2020">2019-2020</form:option>
                                                <form:option value="2018-2019">2018-2019</form:option>
                                                <form:option value="2017-2018">2017-2018</form:option>
                                            </form:select>
                                        </td>
                                        <td>
                                            <strong>Loan Type:</strong>
                                            <form:select class="form-control" path="loanType" id="loanType">
                                                <form:option value="">-ALL LOAN TYPES-</form:option>
                                                <form:option value="HBA">HBA</form:option>
                                                <form:option value="MCA">MCA</form:option>
                                                <form:option value="COMP">COMP</form:option>
                                            </form:select>
                                        </td>
                                                                       

                                        <%--<td><a href="ltaCorrection.htm?loanTp=${loantype.loanTp}" class="btn btn-primary" target="_blank">Search</a></td>                                        --%>
                                        <td>
                                            <br /><input type="submit" class="form-control" value="Search"/>                                        
                                        </td>
                                    </tr>
                                </table>
                            </form:form>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <div class="row">

                        <div class="col-sm-12">
                            <div class="table-responsive" style="min-height:300px;">
                                <c:if test="${not empty ltaList}">
                                    <h1 style="text-align:center;font-size:12pt;font-weight:bold;border-bottom:2px solid #222;padding-bottom:10px;">
                                        Office of the PR. Accountant General (A & E),
                                        Orissa, Bhubaneswar.<br />
                                        (Annual Long Term Advances Recovery Details)</h1>
                                        <c:forEach items="${ltaList}" var="lta2" varStatus="count" begin="0" end="0">
                                        <table style="border:1px solid #CCC;background:#FAFAFA;" width="100%" style="font-size:10pt;">
                                            <c:forEach items="${lta2.dataList}" var="lta1" varStatus="count" begin="0" end="0">
                                                <tr>
                                                    <td style="padding:5px;">${lta1.loaneeId}</td>
                                                    <td style="padding:5px;">${lta1.empName}</td>
                                                    <td style="padding:5px;">Financial Year:</td>
                                                    <td style="padding:5px;">${lta1.finYear}</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="4" align="center" style="padding:5px;font-weight:bold;font-size:10pt;">${lta2.lastDDo}, ${lta2.lastOfficeName}</td>
                                                </tr>
                                            </table>
                                        </c:forEach>
                                    </c:forEach>
                                    <c:forEach items="${ltaList}" var="lta2" varStatus="count">
                                        <table style="border:1px solid #CCC;margin-top:10px;" width="100%">
                                            <tr>
                                                <td style="padding:10px;background:#C4DEF0;border-bottom:1px solid #CCCCCC;font-size:10pt;" align="center"><strong>${lta2.amountType} (${lta2.loanName})</strong></td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td style="background:#EAEAEA;padding:5px;"><strong>Sanction No:</strong> ${lta2.sanctionNo}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <strong>Sanction Date:</strong> ${lta2.sanctionDt}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <strong>Release Amount:</strong> ${lta2.releaseAmount}
                                                </td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table class="table table-bordered table-hover table-striped">
                                                        <thead>
                                                            <tr>
                                                                <th>Acc Month</th>                                  
                                                                <th>Sal Month</th>
                                                                <th>Amount</th>
                                                                <th>Type</th>
                                                                <th>DDO</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach items="${lta2.dataList}" var="lta" varStatus="count">
                                                                <tr>                                               
                                                                    <td class="form-group" >
                                                                        <c:out value="${lta.accMonth}"/>
                                                                    </td>                                                            
                                                                    <td class="form-group">
                                                                        <c:out value="${lta.salMonth}"/>
                                                                    </td>
                                                                    <td class="form-group">
                                                                        <c:out value="${lta.installmentAmount}"/>
                                                                    </td>  
                                                                    <td class="form-group">
                                                                        <c:out value="${lta.amountType}"/>
                                                                    </td> 
                                                                    <td class="form-group">
                                                                        <c:out value="${lta.ddoCode}"/>
                                                                    </td>                                                         
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>    
                                                </td>
                                                <td width="30%" valign="top" style="padding:10px;">
                                                    <c:forEach items="${lta2.dataList}" var="lta1" varStatus="count" begin="0" end="0">
                                                        <table width="100%">
                                                            <tr>
                                                                <td style="padding:5px;font-style:italic;font-weight:bold;">Principal Opening Bal:</td>
                                                                <td style="padding:5px;" align="right">${lta1.advOpBal}</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding:5px;font-style:italic;font-weight:bold;">Adv Drawn:</td>
                                                                <td style="padding:5px;" align="right">${lta1.advDrawn}</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding:5px;font-style:italic;font-weight:bold;">Principal Recovered:</td>
                                                                <td style="padding:5px;" align="right">${lta1.advRepd}</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding:5px;font-style:italic;font-weight:bold;">Principal Closing Bal:</td>
                                                                <td style="padding:5px;" align="right">${lta1.advClBal}</td>
                                                            </tr>                                                           
                                                        </table>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </table>
                                    </c:forEach>

                                </c:if>
                                
                                <div style="border:1px solid #CCCCCC;background:#EAEAEA;color:#F00;text-align:center;font-weight:bold;line-height:70px;">
                                        N:B:-Any mismatch in the statement may be addressed to the Principal AG(A&E) through DDO with recovery particulars.

                                </div>
                            </div>


                        </div>

                    </div>

                </div>                  
            </div>
    </body>
</html>
