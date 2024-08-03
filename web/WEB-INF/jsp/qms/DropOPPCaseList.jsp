<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>
        

    </head>
    <body>
        <jsp:include page="header_quarter.jsp">
            <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
        </jsp:include>    
        <div id="wrapper">

            <div id="page-wrapper">
                <div class="container-fluid">
                    <div align="center" style="margin-top:10px;margin-bottom:7px;">
                        <h2 style='color:red'>${textMessage}</h2>

                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th> 
                                            <th>OPP Case No</th>                                           
                                            <th>HRMS Id</th>                                            
                                            <th>Employee Name</th>
                                            <th>Mobile</th>
                                            <th>DOB</th>                                          
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th> 
                                            <th>Drop OPP Case Date</th>  
                                          <!--  <th>Notice Issue Status</th>-->                                          

                                        </tr>
                                    </thead>
                                    <tbody id="wrrgrid">
                                        <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">

                                            <tr  >
                                                <td>${cnt.index+1}</td>                                                 
                                                <td>${quarter.orderNumber}</td>
                                                <td>${quarter.empId}</td>                                                
                                                <td>${quarter.empName}</td>
                                                <td>${quarter.mobileno}</td>
                                                <td>${quarter.dos}</td>                                               
                                                <td>${quarter.quarterNo}</td>                                                                                                
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.qrtrunit}</td>
                                                <td>${quarter.dor}</td>
                                             


                                            </tr>
                                        </c:forEach>                                        
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
