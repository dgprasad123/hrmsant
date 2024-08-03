<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Employee Loan Sanction
                </div>        
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Order No:
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${loanForm.orderno}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Order Date:
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${loanForm.orderdate}"/>
                            </strong>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Loan Name:
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${loanname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            BT ID:
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${loanForm.btid}"/>
                            </strong>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Sanctioning Authority:
                        </div>
                        <div class="col-lg-9">
                            Department:
                            <strong>
                                <c:out value="${sancdeptname}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Office: 
                            <strong>
                                <c:out value="${sancoffice}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${loanForm.authority}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Amount in Rs.:
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${loanForm.txtamount}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Account No :
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${loanForm.accountNo}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Bank Name:
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${bankname}"/>
                            </strong>                         
                        </div>
                        <div class="col-lg-2">
                            Branch Name :
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${branchname}"/>
                            </strong>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Treasury voucher No:
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${loanForm.voucherNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            <label>Date:</label>
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${loanForm.voucherDate}"/>
                            </strong>
                        </div>                        
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Treasury :
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${treasuryname}"/>
                            </strong>
                        </div>                                                
                    </div>
                    <div class="row" style="margin-bottom: 12px;margin-top: 65px;">
                        <div class="col-lg-2">
                            <label>Now Deduct :<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${loanForm.nowDeduct eq 'P'}">
                                <strong>
                                    PRINCIPAL
                                </strong>
                            </c:if>
                            <c:if test="${loanForm.nowDeduct eq 'I'}">
                                <strong>
                                    INTEREST
                                </strong>
                            </c:if>
                        </div>                                                
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-12">
                            <table width="100%">                                
                                <tr>                                    
                                    <td>Original Amount</td>
                                    <td>Total No of Instal.</td>
                                    <td>Instalment Amount</td>
                                    <td>Paid Instal No</td>
                                    <td>Monthly Instl No(if required)</td>
                                    <td>Cumulative Amount paid</td>
                                    <td>Completed Recovery</td>
                                </tr>
                                <tr>                                    
                                    <td>
                                        <strong>
                                            <c:out value="${loanForm.originalAmt}"/>
                                        </strong>
                                    </td>
                                    <td>
                                        <strong>
                                            <c:out value="${loanForm.totalNoOfInsl}"/>
                                        </strong>
                                    </td>
                                    <td>
                                        <strong>
                                            <c:out value="${loanForm.instalmentAmount}"/>
                                        </strong>
                                    </td>
                                    <td>
                                        <strong>
                                            <c:out value="${loanForm.lastPaidInstalNo}"/>
                                        </strong>
                                    </td>
                                    <td>
                                        <strong>
                                            <c:out value="${loanForm.monthlyinstlno}"/>
                                        </strong>
                                    </td>
                                    <td>
                                        <strong>
                                            <c:out value="${loanForm.cumulativeAmtPaid}"/>
                                        </strong>
                                    </td>
                                    <td>
                                        <strong>
                                            <c:out value="${loanForm.completedRecovery}"/>
                                        </strong>
                                    </td>
                                </tr>                                
                            </table>
                        </div>
                    </div>                                    
                </div>            
                <div class="panel-footer">
                    <a href="loansanction.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
            <div class="row" style="margin: 5px;">
                <h3>Loan Recovery History</h3>
                <p>The Below table will show the month wise recovery history of the employee</p>
            </div>
            <div class="row" style="margin: 5px;border: 1px solid #000000;">
                <table class="table table-hover">
                    <thead>
                        <tr class="success">
                            <th>Sl No</th>
                            <th>Month-year</th>
                            <th>Instalment Amount</th>
                            <th>Now Deduct</th>
                            <th>Ref</th>
                            <th>Total Amt. Paid</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${Monthwiseinstl}" var="instlment" varStatus="cnt">
                            <tr>
                                <td>${cnt.index+1}</td>
                                <td>${instlment.month}-${instlment.year}</td>
                                <td>${instlment.instalmentAmount}</td>
                                <td>${instlment.nowdedn}</td>
                                <td>${instlment.refDesc}</td>
                                <td>${instlment.cumulativeAmtPaid}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
