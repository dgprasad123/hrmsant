<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>::HRMS::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body>
        <form:form class="form-inline" action="aerstatuslist.htm" commandName="aerstatuslist"  method="GET">
             <div class="modal-header">
                    <h4 class="modal-title">Office List(AER Submitted)</h4>
                </div>
         <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                <thead>
                    <tr>
                        <th>Sl No</th>
                        <th>Office Name </th>
                        <th> &nbsp; </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="aersubmittedofflist" items="${offList}" varStatus="theCount">
                        <tr>
                            <td>${theCount.index + 1}</td>
                            <td>${aersubmittedofflist.offName}</td> 
                            <td> <c:if test="${aersubmittedofflist.aerId gt 0}">
                                    <a href="downloadEstablishmentReport.htm?aerId=${aersubmittedofflist.aerId}" target="_blank"> <img border="0" alt="PDF" src="images/pdf.png" height="20"> </a>
                                </c:if>  
                            </td>
                       </tr>
                    </c:forEach>
                </table>
        </form:form>
    </body>
</html>
