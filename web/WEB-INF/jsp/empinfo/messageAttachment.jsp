<%-- 
    Document   : messageAttachment
    Created on : Nov 20, 2019, 1:01:43 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }
        </style>
    </head>
    <body>
        <table class="table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>File Name</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${messageattachments}" var="attachment" varStatus="cnt">
                <tr>
                    <td>${cnt.index+1}</td>
                    <td>${attachment.attachementname}</td>
                    <td><a href="downloadEmployeeAttachment.htm?attachmentid=${attachment.attachmentid}">Download</a></td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
