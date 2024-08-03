<%-- 
    Document   : PayBillReport
    Created on : Aug 19, 2016, 1:37:11 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS ::</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <style type="text/css">
            .reportHeader{
                background-color:#B1C242;
                font-family:Arial,Helvetica;
            }
        </style>
        <script type="text/javascript">
          
        </script>     
    </head>
    <body>
        <h3 style="text-align:center;color: #F00000">
            Please edit and save the Bill before generating PDFs.
        </h3>
        <table class="table table-hover">
            <thead>	
                <tr>
                    <c:if test="${LoginUserBean.loginusertype ne 'F'}">
                        <th  width="5%">Sl No.</th>
                        <th width="60%">Report List</th>
                        <th width="10%">Action</th> 
                        <th width="10%">PDF LINK</th>    
                    </c:if>
                    <c:if test="${LoginUserBean.loginusertype eq 'F'}"> 
                        <th  width="5%">Sl No.</th>
                        <th width="60%">Report List</th>
                        <th width="10%">PDF LINK</th>
                    </c:if>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${reportList}" var="report" varStatus="loop">
                    <tr>
                        <td>${loop.index+1}</td>
                        <td>${report.reportName}</td>
                        <c:if test="${not empty report.actionPath}">
                            <td><a target="_blank" href="${report.actionPath}"><img border="0" height="25" alt="HTML" src="images/html_icon.gif"></a></td> 
                        </c:if>
                        <c:if test="${empty report.actionPath}">
                            <td>&nbsp;</td>
                        </c:if>
                        <c:if test="${not empty report.pdfLink}">
                            <td>   
                                <c:if test="${empty report.reportRefSlno && allowEsign eq 'Y'}">
                                    <a target="_blank" href="${report.pdfLink}&slNo=${report.slNo}"><input type="button" name='button' value="Generate PDF" class="btn btn-danger" /></a>
                                </c:if> 
                                <c:if test="${not empty report.reportRefSlno && allowEsign eq 'Y' && not empty report.unsignedpdfPath && empty report.signedpdfpath}">
                                    <%--<a  href="downlaodBillBrowserPDFFile.htm?esignLogId=${report.esignLogId}"><img border="0" alt="PDF" src="images/pdf.png" height="20"></a>--%>
                                    <a target="_blank" href="${report.pdfLink}&slNo=${report.slNo}"><img border="0" alt="PDF" src="images/pdf.png" height="20"/></a>
                                </c:if>                                  
                                <c:if test="${not empty report.reportRefSlno && allowEsign eq 'Y' && not empty report.unsignedpdfPath && not empty report.signedpdfpath}">
                                   |
                                    <a href="downlaodSignedPDFFile.htm?esignLogId=${report.esignLogId}"><img border="0" alt="PDF" src="images/digital-pdf.png"/></a>
                                </c:if>
                                <c:if test="${allowEsign eq 'N'}">
                                    <a target="_blank" href="${report.pdfLink}&slNo=${report.slNo}"><img border="0" alt="PDF" src="images/pdf.png" height="20"/></a>
                                </c:if>           
                            </td>
                        </c:if>
                        <c:if test="${empty report.pdfLink}">
                            <td>&nbsp;</td>
                        </c:if>  
                    </tr>
                </c:forEach>               
            </tbody>
        </table>
    </body>
</html>