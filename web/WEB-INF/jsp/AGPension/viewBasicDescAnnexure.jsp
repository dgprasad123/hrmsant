<%-- 
    Document   : viewBasicDescAnnexure
    Created on : 11 Oct, 2022, 11:49:10 AM
    Author     : Surendra
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="font-awesome/css/font-awesome.min.css">        
        <link rel="stylesheet" href="css/sb-admin.css">

        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
           
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/agOdishaMenu.jsp"/>
            <div id="page-wrapper">
                <form:form action="viewBasicDescAnnexure.htm" commandName="agPensionForm">
                    <div class="container-fluid" style="margin-bottom:40px; margin-top: 30px;">
                        
                           
                                 <h3>View Basic Description Annexure    </h3>
                            
                        
                            <div class="panel-body row">      
                                <table class="table table-bordered table-striped">
                            <thead>
                                <tr style="background-color: aliceblue;">
                                    <th>Sl No</th>
                                    <th>Description</th>
                                    <th>Details</th>                                 
                                    
                                    
                                </tr>
                            </thead>
                            <tbody>
                              
                                    <tr>
       
                                        <td> ${empdata.empid} </td>
                                                <td> ${empdata.empname} </td>
                                        <td>1</td>
                                        <td>1</td>
                                                                                
                                    </tr>
                             
                            </tbody>
                        </table>
                           
                        </div>
                        
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
