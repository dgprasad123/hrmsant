<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script  type="text/javascript">
            function deleteCandidate(candidateId, categoryId)
            {
                if (confirm("Are you sure you want to delete the Candidate?"))
                    self.location = 'deleteCandidate.htm?candidateId=' + candidateId + '&categoryId=' + categoryId;
            }

        </script>
        <style type="text/css">
            table.form-table td{padding:5px;}
        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptmismenu.jsp"/>               
            <!-- Site wrapper -->
            <div class="wrapper">            
                <div id="page-wrapper">
                    <div class="container-fluid">
                        <!-- Page Heading -->
                        <div class="row">
                            <div class="col-lg-12">                            

                            </div>
                        </div>
                        <div class="row" style="min-height:600px">
                            <div class="col-lg-12">
                                <h2 style="margin-top:0px;">Department Wise Rehabilitation Scheme Summary </h2>

                               
                                     
                                    <table id="account" class="table table-bordered table-striped" >
                                        <thead>

                                            <tr style='background-color:black;color:white;font-size:14px'>
                                                <th>#</th>
                                                <th valign='top'>Department Name</th>
                                                <th valign='top'>Total No.of cases under R.A Scheme</th>
                                                <th valign='top'>Cumulative No. of cases considered for appointment</th>
                                                <th valign='top'>Cumulative No. of cases considered and rejected </th>
                                                <th valign='top'>Total no of cases cleared</th>
                                                <th valign='top'>Balance no of cases pending</th>



                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="totalCaseTotal" value="${0}" />
                                            <c:set var="totalPendingTotal" value="${0}" />
                                            <c:set var="totalAppointmentTotal" value="${0}" />

                                            <c:set var="totalRejectedTotal" value="${0}" />
                                            <c:set var="totalCleared" value="${0}" />
                                            <c:set var="slno" value="${0}" />
                                              <c:set var="totalAppointment" value="${0}" />
                                               <c:set var="totalRejected" value="${0}" />
                                                 <c:set var="totalCle" value="${0}" />
                                                 <c:set var="totalpend" value="${0}" />

                                            <c:if test = "${not empty raschemeList}"> 
                                                <c:forEach items="${raschemeList}" var="rascheme">
                                                     <c:set var="totalAppointment" value="${ rascheme.totalAppointment + rascheme.totalAppoLastmonth}" />
                                                      <c:set var="totalRejected" value="${rascheme.totalRejected + rascheme.totalRejLastmonth}" />
                                                      <c:set var="totalCle" value="${totalAppointment+totalRejected}" />
                                                       <c:set var="totalpend" value="${rascheme.totalCase-totalCle}" />
                                                      
                                                    <c:set var="totalCaseTotal" value="${totalCaseTotal + rascheme.totalCase}" />
                                                    <c:set var="totalPendingTotal" value="${totalPendingTotal + totalAppointment}" />
                                                   
                                                    <c:set var="totalAppointmentTotal" value="${totalAppointmentTotal +  totalRejected}" />
                                                    <c:set var="totalRejectedTotal" value="${totalRejectedTotal + totalCle}" />
                                                  
                                                    
                                                    <c:set var="totalCleared" value="${totalCleared +totalpend}" />
                                                    <c:set var="slno" value="${slno +1}" />
                                                    <tr>
                                                        <td>${slno}</td>
                                                        <td>${rascheme.deptName}</td>
                                                        <td>${rascheme.totalCase}</td>
                                                        <td>${totalAppointment}</td>
                                                        <td>${totalRejected}</td>
                                                        <td>${totalCle}</td>
                                                        <td>${totalpend}</td>	


                                                    </tr>
                                                </c:forEach>
                                                    <tr style='background-color:black;color:white;font-size:14px'>
                                                    <th>&nbsp;</th>
                                                     <th>&nbsp;</th>
                                                    <th>${totalCaseTotal}</th>
                                                    <th>${totalPendingTotal}</th>
                                                    <th>${totalAppointmentTotal}</th>
                                                    <th>${totalRejectedTotal}</th>
                                                    <th>${totalCleared}</th>
                                                    
                                                </tr>
                                            </c:if>
                                            <c:if test = "${empty raschemeList}">
                                                <tr>
                                                    <th colspan="10" style='color:red' align='center'><h3>Data is not available</h3></th>
                                            </tr>
                                        </c:if>     
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
