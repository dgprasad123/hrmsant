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
        <form:form action="newPromotion.htm" method="post" commandName="promotionForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12 ">

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="15%">Date of Entry</th>
                                    <th width="10%">Notification Order No</th>
                                    <th width="15%">Notification Order Date</th>
                                    <th width="20%">Transaction Type</th>
                                    <th width="9%">Print in<br />Service Book</th>
                                    <th width="8%">Modified By</th>
                                    <th colspan="3" width="30%" style="text-align:center;">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${promotionlist}" var="plist">
                                    <%--<c:if test="${empty plist.promotionId}"> 
                                        <tr style="background-color:#989999">
                                            <td>${plist.doe}</td>
                                            <td>${plist.ordno}</td>
                                            <td>${plist.ordt}</td>
                                            <td>${plist.notType}</td>
                                            <td width="10%">
                                                <a href="editPromotion.htm?promotionId=${plist.promotionId}&notId=${plist.hnotid}">Edit</a>
                                            </td>
                                            <td width="10%">
                                                <a href="entryRelieve.htm?notId=${plist.hnotid}&rlvId=${plist.hrlvid}">Relieve</a>
                                            </td>
                                            <td width="10%">
                                                <a href="entryRelieve.htm?notId=${plist.hnotid}&rlvId=${plist.hrlvid}">Language</a>
                                            </td>
                                        </tr>
                                    </c:if>--%>
                                    <c:if test="${plist.printInServiceBook eq 'N'}">
                                        <tr style="background: #FFB9B9;">
                                        </c:if>
                                        <c:if test="${empty plist.printInServiceBook || plist.printInServiceBook eq 'Y'}">
                                        <tr>
                                        </c:if>
                                        <td>${plist.doe}</td>
                                        <td>${plist.ordno}</td>
                                        <td>${plist.ordt}</td>
                                        <td>${plist.notType}</td>
                                        <td>
                                            <c:if test="${plist.printInServiceBook eq 'N'}">
                                                No
                                            </c:if>
                                            <c:if test="${empty plist.printInServiceBook || plist.printInServiceBook eq 'Y'}">
                                                Yes
                                            </c:if>
                                        </td>
                                        <td>${plist.modifiedby}</td>
                                        <td width="10%">
                                            <c:if test="${plist.isValidated eq 'N'}">
                                                <a href="editPromotion.htm?promotionId=${plist.promotionId}&notId=${plist.hnotid}">Edit</a>
                                            </c:if>
                                            <c:if test="${plist.isValidated eq 'N'}">
                                                <a href="javascript:void(0)" onclick="javascript: confirmDelete('${plist.promotionId}', '${plist.hnotid}')">Delete</a>
                                            </c:if>                                                
                                            <c:if test="${plist.isValidated eq 'Y'}">
                                                <a href="viewPromotion.htm?promotionId=${plist.promotionId}&notId=${plist.hnotid}">View</a>
                                            </c:if>
                                        </td>
                                        <td width="10%">
                                            <a href="entryRelieve.htm?notId=${plist.hnotid}&rlvId=${plist.hrlvid}">Relieve</a>
                                        </td>
                                        <td width="10%">
                                            <%--<a href="entryRelieve.htm?notId=${plist.hnotid}&rlvId=${plist.hrlvid}">Language</a>--%>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empid" id="empid"/>
                        <button type="submit" class="btn btn-default">New Promotion</button>&nbsp;&nbsp;&nbsp;
                        <span style="color: #F00000; font-weight: bold; font-size: 15px;">Red Color row indicates not to display in Service Book</span>
                    </div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript">
            function confirmDelete(promotionId, notID)
            {
                if (confirm("Are you sure you want to delete the Promotion record from the list?"))
                {
                    $.ajax({
                        url: 'DeletePromotion.htm',
                        type: 'get',
                        data: 'promotion_id=' + promotionId + '&not_id=' + notID,
                        success: function(retVal) {
                            window.location.reload();
                        }
                    });
                }
            }
        </script>
    </body>
</html>
