<%-- 
    Document   : ErrorMessage
    Created on : Oct 23, 2017, 4:52:35 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                     <h1>${gbs}</h1>
                    <c:forEach items="${billAttr}" var="battr">
                        <h1>${battr.msg}</h1>
                    </c:forEach>
                    
                </div>
                <div class="panel-footer">
                    
                </div>
            </div>
        </div>
    </body>
</html>
