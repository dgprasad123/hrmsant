<%-- 
    Document   : InvestmentDeclaraionList
    Created on : 21 Dec, 2018, 11:55:16 AM
    Author     : Surendra
--%>

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
        <form:form action="itdeclarationData.htm" method="POST" commandName="command">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <button type="submit" class="btn btn-default">Create Declaration</button>  
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="30%">Financial Year</th>
                                    <th width="30%">Date of Submission</th>
                                    <th width="20%">Edit/View</th>
                                    <th width="20%">Submit</th>
                                    
                                </tr>
                            </thead>
                            <tbody>
                                
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" class="btn btn-default">Create Declaration</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
