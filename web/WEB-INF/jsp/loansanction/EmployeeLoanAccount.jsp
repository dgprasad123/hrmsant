<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
        <script type="text/javascript"  src="js/jquery.min-1.9.1.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <form:form action="loanaccount.htm" commandName="loanacount"  method="POST">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablegrid" style="font-size:12px;margin-top: 15px;">
                            <tr style="height:30px;">
                                <td align="right" width="40%" style="font-size:15px;font-weight:bold;">Loan Name</td>
                                <td align="center" width="20%">
                                    <form:select path = "sltloan" cssClass="form-control">
                                        <form:option value = "" label = "Select"/>
                                        <form:options items = "${loanTypeList}" itemValue="loanType" itemLabel="loanName"/>
                                    </form:select> 

                                </td>
                                <td align="left" width="40%" style="font-size:12px;"><button type="submit" class="btn btn-success">Ok</button></td>
                            </tr>
                        </table>
                    </form:form>
                </div>
                <div class="panel-body">
                    <div align="center" style="width:100%;">
                        <div style="height:30px;">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;margin-top: 15px;">
                                <tr>
                                    <td align="center" width="50%"align="center" style="font-family:Verdana;font-size:12px;">
                                        <span style="font-weight:bold">Employee Name</span>:&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${loanacount.empName}"/>
                                    </td>
                                    <td align="center" width="50%" style="font-family:Verdana;font-size:12px;">
                                        <span style="font-weight:bold">GPF No</span>:&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${loanacount.gpfNo}"/>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="row" style="font-weight: bold;font-size: larger;text-decoration: underline;margin-bottom: 10px;" align="center">${loanName.loanName}</div>
                    <c:forEach var="loanDeatil" items="${empLoanDetail}">                        
                        <div class="row">
                            <div class="col-lg-3"><b>Loan Date: ${loanDeatil.loandate}</b></div>
                            <div class="col-lg-3"><b>Loan Amount: ${loanDeatil.loanamt}</b></div>
                            <div class="col-lg-3"><b>Deduction Type: ${loanDeatil.now_ded}</b></div>
                            <div class="col-lg-3"><b>Status: ${loanDeatil.iscompleted}</b></div>
                        </div>
                        <div class="table-responsive">
                            <table id="grouploanlist" class="table table-striped table-bordered" width="100%" cellspacing="0">
                                <thead>
                                    <tr style="height:40px">
                                        <th width="4%">Sl No</th>
                                        <th width="12%">Deduction Month-Year</th>
                                        <th width="12%">Instalment Number</th>
                                        <th width="12%"> Deduction Amount</th>
                                        <th width="12%">Balance</th>
                                        <th width="12%">Bill No</th>
                                        <th width="12%">Bill Date</th>
                                        <th width="12%"> Voucher No</th>
                                        <th width="12%">Voucher Date</th>
                                    </tr>
                                </thead>
                                <tbody> 
                                    <c:forEach var="loanList" items="${loanDeatil.loanAccList}" varStatus="theCount" >
                                        <tr>
                                            <td align="center">
                                                ${theCount.count}
                                            </td>
                                            <td align="center">
                                                ${loanList.dedmonth}-${loanList.dedyear}
                                            </td>
                                            <td align="center">
                                                ${loanList.inslno}
                                            </td>
                                            <td align="center">
                                                ${loanList.dedamt}
                                            </td>
                                            <td align="center">
                                                ${loanList.bal} 
                                            </td>
                                            <td align="center">
                                                ${loanList.billNo}
                                            </td>
                                            <td align="center">
                                                ${loanList.billdate}
                                            </td>
                                            <td align="center">
                                                ${loanList.vchno}
                                            </td> 
                                            <td align="center">
                                                ${loanList.vchdate}
                                            </td>  
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
</html>
