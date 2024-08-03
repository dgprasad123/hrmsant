<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Release Of Loan</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
    </head>
    <body>
        <form:form id="fm" action="loanrepayment.htm" method="post" name="myForm" commandName="loanrepayment">
            <div style=" margin-bottom: 5px;" class="panel panel-info">
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th>#</th>
                                <th>Date Of Entry<br> in the<br> Service Book</th>
                                <th>Loan Type</th>
                                <th>Repayment Type</th>
                                <th>Installment No</th>
                                <th>Repayment Amount</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${loanRepaymentList}" var="loanRepaymentList" varStatus="cnt">
                                <tr>
                                    <th scope="row">${cnt.index+1}</th>
                                    <td>&nbsp;</td>
                                    <td>${loanRepaymentList.sltloan}</td>
                                    <td>${loanRepaymentList.ptype}</td>
                                    <td>${loanRepaymentList.txtinstno}</td>
                                    <td>${loanRepaymentList.txtamount}</td>
                                    <td>
                                        <a href="getloanrepaymentdata.htm?repid=${loanRepaymentList.hrepid}&notid=${loanRepaymentList.hnid}">Edit</a>
                                        <a href="deleteloanrepaymentdata.htm?repid=${loanRepaymentList.hrepid}&notid=${loanRepaymentList.hnid}">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <table class="table table-bordered">
                        <tr>
                        <input type="submit" name="new" value="New" class="btn btn-primary" onclick="return newLoanrelease();" /> 
                        </tr>
                    </table>
                </div>
            </div>

        </form:form>
    </body>
</html>
