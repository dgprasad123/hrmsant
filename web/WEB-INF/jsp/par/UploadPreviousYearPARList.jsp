<%-- 
    Document   : UploadPreviousYearPARList
    Created on : 13 May, 2022, 11:07:34 AM
    Author     : Manisha
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>

    </head>

    <form:form action="uploadPreviousYearPARList.htm" method="POST" commandName="uploadPreviousPAR" class="form-inline" enctype="multipart/form-data">
        <div class="row">
            <div class="col-lg-12">
                <h2 style="text-align: center">Previous Year PAR Uploaded List</h2>
                <div class="table-responsive" style="height: 550px;overflow: auto;">
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th width="3%">#</th>
                                <th width="20%">PAR Uploaded By Employee Name</th>
                                <th width="20%">PAR Uploaded By Employee Post</th>
                                <th width="20%">PAR Uploaded For Employee Name</th>
                                <th width="20%">PAR Uploaded For Employee Post</th>
                                <th width="15%">PAR Uploaded On</th>
                                <th width="15%">Final Grading For PAR</th>
                                <th width="15%">Attachment</th>
                            </tr>
                        </thead>
                        <tbody>                                        
                            <c:forEach items="${uploadPARList}" var="uploadPAR" varStatus="count">
                                <tr>
                                    <td>${cnt.index + 1}</td>
                                    <td style="text-align: center">${uploadPAR.previousyrParUploadedbyempName}</td>
                                    <td style="text-align: center">${uploadPAR.previousyrParUploadedbypost}</td>
                                    <td style="text-align: center">${uploadPAR.empName}</td>
                                    <td style="text-align: center">${uploadPAR.postName}</td>
                                    <td style="text-align: center">${uploadPAR.previousyrParUploadOn}</td> 
                                    <td>${uploadPAR.finalGradingNameForPreviousYrPAR}</td> 
                                    <td>
                                        <a href="downloadAttachmentOfPreviousYearPAR.htm?parId=${uploadPAR.parId}" class="btn btn-default" >
                                            <span class="glyphicon glyphicon-paperclip"></span> ${uploadPAR.originalFileNameForpreviousYearPAR}
                                        </a>
                                    </td>                            
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <input type="submit" name="action" class="btn btn-default" value="Back"/>    
                </div>

            </div>
        </div>


    </form:form>
</html>
