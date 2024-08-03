<%-- 
    Document   : HeldupPay
    Created on : Apr 7, 2018, 12:28:45 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="col-md-12 col-sm-12" align="right" style="margin:10px;">
            <div style="margin-bottom:8px;" class="row">
                <div class="col-md-8 col-sm-8">
                    <form:form commandName="RegularHeldUpBean" method="post" action="getbillgroupwiseemployee.htm">
                        <input type="hidden" value="44556677" id="my_id" name="my_id">
                        <b>Select BillGroup:</b>                                               
                        <form:select path="billgroupid"  style="height:38px;border:2px solid #C5C5C5;width:220px;">                            
                            <form:options items="${billgrouplist}" itemValue="billgroupid" itemLabel="billgroupdesc"/>
                        </form:select>                        
                        <input type="submit" style="height:38px;border:1px solid #034303;width:100px;" class="btn-success" value="Ok">                        
                    </form:form>
                </div>

            </div>
        </div>

        <div class="panel panel-default">
            <div class="panel-body">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr style="font-weight:bold;background:#3E6A00;color:#FFFFFF;">
                            <th width="5%">Sl No</th>
                            <th width="10%">HRMS ID</th>
                            <th width="30%">EMPLOYEE NAME</th>
                            <th width="20%">GPF NO.</th>
                            <th width="20%">POST</th>
                            <th width="20%">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${emplist}" var="emp">
                            <tr>
                                <td>${emp.slno}</td>
                                <td>${emp.hrmsId}</td>
                                <td>${emp.empname}</td>
                                <td>${emp.gpfNo}</td>
                                <td>${emp.post}</td>
                                <td>
                                    <c:if test="${emp.heldupStatus == 'Y'}">
                                        <a href="heldupemployeedetail.htm?empIdSPC=${emp.empIdSPC}&billgroupid=${RegularHeldUpBean.billgroupid}&hrmsId=${emp.hrmsId}&offcode=${RegularHeldUpBean.offcode}">Held Up</a>
                                    </c:if>
                                    <c:if test="${emp.heldupStatus == 'N'}">
                                        <a href="releaseemployeedetail.htm?heldupId=${emp.heldupId}&billgroupid=${RegularHeldUpBean.billgroupid}&hrmsId=${emp.hrmsId}" class="label label-danger">Release Pay</a>
                                    </c:if>
                                    
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
