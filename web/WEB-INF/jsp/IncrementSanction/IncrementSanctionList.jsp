<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <style type="text/css">
            table tr{
                font-family:Verdana;
                font-size:13px;
            }
        </style>
    </head>
    <body>
        <form:form action="addIncrement.htm" method="POST" commandName="incrementForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <span style="color:red;font-weight: bold;font-size:16px;">
                                    Six Years Contractual Employees who have joined between Year 2013 to 2016 under General Administration Department and converted through Regularization of Service Contractual Link then they should fill up the Third Schedule Form(Third Schedule List for Contractual 6 Years To Regular Employee)available in Establishment Office/DDO login who is having my office interface for Automatic Insertion of Incremental amount.
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="13%">Date of Entry</th>
                                    <th width="13%">W.E.F Date</th>
                                    <th width="13%">W.E.F Time</th>
                                    <th width="13%">Increment<br/>Amount(Rs.)</th>
                                    <th width="13%">New Basic(Rs.)</th>
                                    <th width="8%">Grade Pay</th>
                                    <th width="8%">Print in<br />Service Book</th>
                                    <th width="8%" align="center">Action</th>
                                    <th width="11%">Modified By</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${incrlist}" var="ilist">
                                    <c:if test="${ilist.printInServiceBook eq 'No'}">
                                        <tr style="background: #FFB9B9;">
                                        </c:if>
                                        <c:if test="${ilist.printInServiceBook eq 'Yes'}">
                                        <tr>
                                        </c:if>
                                        <td>${ilist.doe}</td>
                                        <td>${ilist.effDate}</td>
                                        <td>${ilist.effTime}</td>
                                        <td>${ilist.incrAmt}</td>
                                        <td>${ilist.newBasic}</td>
                                        <td>${ilist.gradePay}</td>
                                        <td>${ilist.printInServiceBook}</td>
                                        <td>
                                            <c:if test="${ilist.isValidated eq 'N'}">
                                                <a href="editIncrement.htm?notId=${ilist.hnotid}&incrId=${ilist.hidIncrId}">Edit</a>
                                                <a  href="javascript:void(0)" onclick="javascript: confirmDelete('${ilist.hidIncrId}', '${ilist.hnotid}')">Delete</a><br />
                                                <c:if test="${empty ilist.canceltype}">
                                                    <a href="SupersedeIncrementSanction.htm?notId=${ilist.hnotid}">Supersede</a>
                                                    <a href="CancelIncrementSanction.htm?notId=${ilist.hnotid}">Cancel</a>
                                                </c:if>
                                                <c:if test="${not empty ilist.canceltype}">
                                                    <c:if test="${ilist.canceltype eq 'INCREMENT'}">
                                                        <a href="SupersedeIncrementSanction.htm?notId=0&supersedeid=${ilist.cancelnotid}">Supersede</a>
                                                    </c:if>
                                                    <c:if test="${ilist.canceltype eq 'CANCELLATION'}">
                                                        <a href="CancelIncrementSanction.htm?notId=0&cancelnotId=${ilist.cancelnotid}">Cancel</a>
                                                    </c:if>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${ilist.isValidated eq 'Y'}">
                                                | <a href="viewIncrement.htm?notId=${ilist.hnotid}&incrId=${ilist.hidIncrId}">View</a>
                                            </c:if>
                                        </td>
                                        <td>${ilist.modifiedBy}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" class="btn btn-default">Add Increment</button>&nbsp;&nbsp;&nbsp;
                        <span style="color: #F00000; font-weight: bold; font-size: 15px;">Red Color row indicates not to display in Service Book</span><br />
                    </div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript">
            function confirmDelete(incrementId, notID)
            {
                if (confirm("Are you sure you want to delete the Increment Sanction record from the list?"))
                {
                    $.ajax({
                        url: 'DeleteIncrementSaction.htm',
                        type: 'get',
                        data: 'incr_id=' + incrementId + '&not_id=' + notID,
                        success: function (retVal) {
                            window.location.reload();
                        }
                    });
                }
            }
        </script>    
    </body>
</html>