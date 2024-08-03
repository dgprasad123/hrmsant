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
        <form:form id="fm" action="loanrelease.htm" method="post" name="myForm" commandName="loanrelease">
            <div style=" margin-bottom: 5px;" class="panel panel-info">
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th>#</th>
                                <th>Date Of Entry<br> in the<br> Service Book</th>
                                <th>Loan Type</th>
                                <th>Release Type</th>
                                <th>Installment No</th>
                                <th>Release Amount</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${loanreleaseList}" var="loanreleaseList" varStatus="cnt">
                                <tr>
                                    <th scope="row">${cnt.index+1}</th>
                                    <td>&nbsp;</td>
                                    <td>${loanreleaseList.sltloan}</td>
                                    <td>${loanreleaseList.ptype}</td>
                                    <td>${loanreleaseList.txtinstno}</td>
                                    <td>${loanreleaseList.txtamount}</td>
                                    <td>
                                        <a href="getloanreleasedata.htm?repid=${loanreleaseList.hrepid}&notid=${loanreleaseList.hnid}">Edit</a>
                                        <a href="deleteloanreleasedata.htm?repid=${loanreleaseList.hrepid}&notid=${loanreleaseList.hnid}">Delete</a>
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
