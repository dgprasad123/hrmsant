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
        <form:form action="newFTCEntry.htm" method="post" commandName="sFTCBean">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" style="color:#1C6CB7;font-weight:bold;font-size:14pt;">
                        FTC Entry List
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="9%">Notification Order No</th>
                                    <th width="10%">Notification Order Date</th>
                                    <th width="20%">DOE</th>
                                    <th>Home Town</th>
                                    <th width="10%">Period From</th>
                                    <th width="10%">Period To</th>
                                    <th width="10%">Block Year From</th>
                                    <th width="10%">Block Year To</th>                                    
                                    <th width="10%">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${ftclist}" var="lList">
                                    <tr>
                                        <td>${lList.txtNotOrdDt}</td>
                                        <td>${lList.txtNotOrdDt}</td>
                                        <td>${lList.dateofEntry}</td>
                                        <td>${lList.chkHomeTown}</td>
                                        <td>${lList.fromDate}</td>
                                        <td>${lList.toDate}</td>
                                        <td>${lList.fblYear}</td>
                                        <td>${lList.tblYear}</td>                                        
                                        <td><a href="editFTCEntry.htm?notId=${lList.hidNotId}">Edit</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empid" id="empid"/>
                        <button type="submit" class="btn btn-default">New FTC</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
