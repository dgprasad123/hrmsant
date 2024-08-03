<%-- 
    Document   : bankAccountChangeReqList
    Created on : 15 Sep, 2020, 8:13:24 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <form:form action="updateRequestbankaccountPage.htm" method="post" commandName="AqmastModel">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12 ">
                                <h3 style ="color:red" > </h3>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="10%">Sl no</th>
                                    <th width="20%">Bank Account Number</th>
                                    <th width="20%">IFSC Code</th>
                                    <th width="15%">Updated On</th>
                                    <th width="20%">Updated By</th>
                                    <th width="15%">Document Proof</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${updateList}" var="obj" varStatus="count">
                                    <tr>
                                        <td>${count.index+1}</td>
                                        <td>${obj.bankAccNo}</td>
                                        <td>${obj.bankName}, ${obj.branchName} (${obj.ifscCode})</td>
                                        <td>${obj.bankaccUpdatedOn}</td>
                                        <td>${obj.bankaccUpdatedBy}</td>
                                        <td style="text-align:center"> 
                                            <a href="downloadAttachmentOfBankProof.htm?filename=${obj.bankdocProofFilename}"><span class="glyphicon glyphicon-paperclip"></span></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" class="btn btn-primary">Click for Update</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
