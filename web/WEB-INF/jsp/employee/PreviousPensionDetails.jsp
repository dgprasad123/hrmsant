<%-- 
    Document   : PreviousPensionDetails
    Created on : 28 Sep, 2022, 2:49:28 PM
    Author     : Devikrushna
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
        <script type="text/javascript">
        </script>
    </head>
    <body>
        <form:form action="PreviousPensionDetails.htm" method="post" commandName="prevpensionForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                         Previous Pension Details
                    </div>
                    <div class="panel-body">
                        <form:hidden id='pensionId'  path='pensionId'/> 
                        <form:hidden class="form-control" path="empid" id="empid"/>
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr style="background-color: aliceblue;">
                                    <th>Sl No</th>
                                    <th>Pension Type</th>
                                    <th>Pension Amount</th>                                 
                                    <th>Payable Treasury</th>
                                    <th>Bank Name</th>
                                    <th>Bank Branch Name</th>
                                    <th>IFSC Code</th>
                                    <th>Pension Issuing Authority</th> 
                                    <th>Source</th>
                                    <th>PPO/FPPO No</th>
                                    <th>Pension Effective From Date</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${prevPensionlist}" varStatus="count">
                                    <tr>
                                        <td>${count.index + 1}</td>
                                        <td>
                                            <c:if test="${item.pensionType eq 'F'}">
                                                <span>Family Pension</span>
                                            </c:if>
                                            <c:if test="${item.pensionType eq 'N'}">
                                                <span>Normal Pension</span>
                                            </c:if>                                       
                                        </td>
                                        <td>${item.pensionAmount}</td>
                                        <td>${item.payableTreasury}</td>
                                        <td>${item.bankName}</td>
                                        <td>${item.branchName}</td>
                                        <td>${item.ifscCode}</td>
                                        <td>${item.pensionIssuingAuth}</td> 
                                        <td>
                                            <c:if test="${item.source eq 'C'}">
                                                <span>Civil</span>
                                            </c:if>
                                            <c:if test="${item.source eq 'M'}">
                                                <span>Military</span>
                                            </c:if>
                                            <c:if test="${item.source eq 'O'}">
                                                <span>Other</span>
                                            </c:if>                                        
                                        </td>
                                        <td>${item.pPOFPPONo}</td>     
                                        <td>${item.pensionEffectiveDate}</td>    
                                        <td>                                         
                                            <a href="editPrevPension.htm?pensionId=${item.pensionId}" class="btn btn-info">Edit <i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>                                                                                       
                                            <a href="#" onclick="deleteprevPension(${item.pensionId})" class="btn  btn-default btn-warning">Delete <i class="fa fa-trash" aria-hidden="true"></i></a>                                          
                                        </td>
                                    </tr>
                               </c:forEach>
                            </tbody>
                        </table>
                    </div>                       
                    <c:if test="${empty prevPensionlist}">
                       <div class="panel-footer">                      
                         <a href="addNewPreviousPension.htm">
                           <button type="button" class="btn btn-info">Add New Previous Pension Details</button>  </a>
                        </div>
                    </c:if>
                </div>
            </div>
        </form:form>
        
        <script type="text/javascript">
                function deleteprevPension(pensionId){
                    var con=confirm("Do you want to delete this Previous Pension Details ?");
                    if(con){
                        window.location="deleteprevPension.htm?pensionId="+pensionId;
                    }                  
                }
        </script>    
    </body>
</html>