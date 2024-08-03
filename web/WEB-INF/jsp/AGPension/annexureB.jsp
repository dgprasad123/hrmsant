<%-- 
    Document   : annexureB
    Created on : 11 Oct, 2022, 4:59:55 PM
    Author     : Devikrushna
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
                <form:form action="annexureB.htm" commandName="agPensionForm">
                    <div class="container-fluid" style="margin-bottom:40px; margin-top: 30px;">
                 
                                 <h3>View Annexure-B    </h3>
                            
                        
                            <div class="panel-body row">      
                                <table class="table table-bordered table-striped">
                            <thead>
                                <tr style="background-color: aliceblue;">
                                    <th>Sl No</th>
                                    <th>Events</th>
                                    <th>Date</th>
                                    <th>Office</th>                                  
                                    <th>Post Held  </th>        
                                    <th>Scale of Pay/Pay Band/Pay Level and Cell No.</th>        
                                    <th>Pay fixed(Pay+GP)</th>  
                                    <th>Date of Next Increment (DNI)</th>        
                                    <th>Remarks</th>  
                                    
                                </tr>
                            </thead>
                            <tbody>
                              <c:forEach var="item" items="${annexureBList}" varStatus="count"> 
                                    <tr>
       
                                        <td> ${count.index + 1}</td>
                                        <td>${item.tabname} </td>
                                        <td>${item.wefChange} </td>
                                        <td>${item.offen} </td>
                                        <td>${item.spn} </td>
                                       
                                        <td>${item.payscale}  </td>         
                                                                             
                                        
                                          <td>${item.pay}</td>
                                          <td>${item.nextwefChange}</td>
                                          
                                        <td>${item.remarksForNoSV} </td>
                                                                                
                                    </tr>
                              </c:forEach>
                            </tbody>
                        </table>
                                                
                      </div>
                    </div>
                </form:form>
            </div>
            </div>
    </body>
</html>
