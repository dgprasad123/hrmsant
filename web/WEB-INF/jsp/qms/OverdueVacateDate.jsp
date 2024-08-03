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

        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script type="text/javascript">
            function VacateNotice(cno, empid, otype) {
                var constatus = confirm("Are you sure you want to send vacation Notice?");
                if (constatus) {
                    window.location = "RetentionOverDueNotice.htm?consumerNo=" + cno + "&empId=" + empid + "&occupationTypes=" + otype;

                }
            }
        </script>    
    </head>
    <body>
        <jsp:include page="header_quarter.jsp">
            <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
        </jsp:include>    
        <div id="wrapper">

            <div id="page-wrapper">
                <div class="container-fluid">
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">
                        <h3>
                            ${textMessage}
                        </h3>   


                        <div class="panel panel-default " >
                            <div class="panel-heading">&nbsp;</div>
                            <div class="panel-body">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr bgcolor="#FAFAFA">
                                            <th>HRMS ID</th>
                                            <th>Name</th>
                                            <th>Mobile</th>
                                             <th>Message Status</th>
                                            <th>Occupation Type</th>
                                            <th>QRS Vacate Date</th>                                          
                                            <th>QRS Unit</th>
                                            <th>QRS Type</th>
                                            <th>QRS No</th>                                          
                                           
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">
                                            <tr>
                                                <td>${quarter.empId}</td>
                                                <td>${quarter.empName}</td>
                                                <td>${quarter.mobileno}</td>
                                                <td>
                                                    <c:if test = "${not empty quarter.vacateStatus }">
                                                        <strong style="color:green">Sent</strong>
                                                    </c:if>
                                                        &nbsp;
                                                </td>    
                                                 <td>${quarter.occupationTypes}</td>
                                                 <td>${quarter.dor}</td>                                               
                                                
                                                <td>${quarter.qrtrunit}</td>
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.quarterNo}</td> 
                                                <td><a href="#" onclick="VacateNotice('${quarter.consumerNo}','${quarter.empId}',${quarter.occuId})"  class="btn btn-danger">Vacation Notice</a></td>
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
