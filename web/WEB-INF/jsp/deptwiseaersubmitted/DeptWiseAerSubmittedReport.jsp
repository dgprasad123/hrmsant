<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/common.js" type="text/javascript"></script>
    </head>
    <body>
        <form:form id="fm" action="deptwiseaersubmitted.htm" method="post" name="myForm" commandName="deptwiseaersubmitted">
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <center><h2>DEPARTMENT WISE AER SUBMITTED </h2>
                                <h3>${offname}</h3></center>

                            <table class="table table-bordered table-hover table-striped">
                                <tr>
                                    <td>&nbsp; </td>                                            
                                    <td style="width: 30%;align:center;">Financial Year   </td>
                                    <td style="width: 30%">
                                        <form:select path="fiscalYear"  class="form-control">
                                            <form:option value="" label="Select" cssStyle="width:30%"/>
                                            <c:forEach items="${fiscyear}" var="fiscyear">
                                                <form:option value="${fiscyear.fy}" label="${fiscyear.fy}"/>
                                            </c:forEach>                                 
                                        </form:select>
                                    </td>
                                    <td><input type="submit" value="Search" name="search" class="btn btn-primary"/></td>
                                </tr>
                            </table>

                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>                                            
                                            <th>Office Name</th>
                                            <th>AER Submitted<br>(YES/NO)</th>
                                            <th>Download</th>
                                        </tr>
                                    </thead>
                                    <tbody>  
                                        <c:forEach items="${aersubmittedofflist}" var="aersubmittedofflist" varStatus="counter">
                                            <tr>
                                                <th scope="row">${counter.index+1}</th>                                                
                                                <td>${aersubmittedofflist.offName} </td>     
                                                <td>${aersubmittedofflist.status}</td>
                                                <td>
                                                    <c:if test="${aersubmittedofflist.status=='COMPLETED'}">
                                                        <a href="downloadEstablishmentReport.htm?aerId=${aersubmittedofflist.aerId}" target="_blank"> <img border="0" alt="PDF" src="images/pdf.png" height="20"> </a>
                                                    
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
