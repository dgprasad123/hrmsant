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
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <form:form action="newPosting.htm" method="POST" commandName="postingform">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="20%">Date of Entry</th>
                                    <th width="20%">Notification Order No</th>
                                    <th width="20%">Notification Order Date</th>
                                    <th width="30%">Notification Type</th>
                                    <th width="10%" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${postingList}" var="plist">
                                    <tr>
                                        <td>${plist.doe}</td>
                                        <td>${plist.ordno}</td>
                                        <td>${plist.ordDate}</td>
                                        <td>${plist.notType}</td>
                                        <td>
                                            <c:if test="${plist.isValidated eq 'N'}">
                                                <a href="editPosting.htm?notId=${plist.hnotid}">Edit</a>
                                            </c:if>
                                            <c:if test="${plist.isValidated eq 'N'}">
                                                <a  href="javascript:void(0)" onclick="javascript: confirmDelete('${plist.hnotid}')">Delete</a>
                                            </c:if>                                                
                                            <c:if test="${plist.isValidated eq 'Y'}">
                                                <a href="viewPosting.htm?notId=${plist.hnotid}">View</a>
                                            </c:if>    
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" class="btn btn-default">New Posting</button>  
                    </div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript">
            function confirmDelete(notID)
            {
                if (confirm("Are you sure you want to delete the Posting record from the list?"))
                {
                    $.ajax({
                        url: 'DeletePosting.htm',
                        type: 'get',
                        data: 'not_id=' + notID,
                        success: function(retVal) {
                            window.location.reload();
                        }
                    });
                }
            }
        </script>        
    </body>
</html>
