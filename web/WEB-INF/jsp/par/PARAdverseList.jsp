<%-- 
    Document   : PARAdverseList
    Created on : 27 Aug, 2020, 5:27:31 PM
    Author     : Manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
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
        <form:form action="getSearchAdversedPARList.htm" method="POST" commandName="parAdminProperties" class="form-inline">

            <div class="panel panel-default">
                <div class="panel-heading">Employee List</div>
                <div class="panel-body" style="height: 550px;overflow: auto;">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                                <tr>                                            
                                    <th width="3%">#</th>
                                    <th>PAR Id</th>
                                    <th>Employee Name</th>
                                    <th>Designation</th>
                                    <th>Group</th>
                                  
                                </tr>
                            </thead>
                            <tbody>                                        
                                <c:forEach items="${adverselist}" var="adverselist" varStatus="count">
                                    <tr>                                                
                                        <td>${count.index + 1}</td>
                                        <td>${adverselist.parId}</td>
                                        <td>${adverselist.empName}</td>
                                        <td>${adverselist.postName}</td>
                                        <td>${adverselist.postName}</td>
                                           
                                        <td></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                
            </div>
            
        </form:form>            
    </body>
</html>


