<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>

        <div id="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">CO Wise Post Termination  Report</div>

                        <div class="panel-body">
                            <div class="table-responsive">

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr style="background-color: #0071c5;color: #ffffff;">
                                                <th>Sl No</th>
                                                <th>Department</th>
                                                <th>Submitted to FD</th>
                                                <th>Total No CO</th>  
                                                <th>Total CO Submitted</th>

                                            </tr>
                                        </thead>
                                        <c:set var="GtotalEmp" value="${0}" /> 
                                        <c:set var="GtotalCOsubmitted" value="${0}" />    
                                        <c:forEach items="${AERDetails}" var="bgroup" varStatus="count">
                                            <c:set var="GtotalEmp" value="${GtotalEmp + bgroup.totalDDO}" />
                                            <c:set var="GtotalCOsubmitted" value="${GtotalCOsubmitted + bgroup.total}" />
                                            <tr onclick="show_div_details(${count.index + 1})">
                                                <td>${count.index + 1}</td>
                                                <td>${bgroup.departmentname}</td>
                                                <c:if test="${empty bgroup.status}">
                                                    <td>No </td> 
                                                </c:if>
                                                <c:if test="${not empty bgroup.status}">
                                                    <td><strong style='color:green'>Yes</strong></td> 
                                                </c:if>
                                                <td><a href="#" >${bgroup.totalDDO}</a></td>
                                                <td style='color:green'>${bgroup.total}</td>
                                            </tr>
                                            <tr style='display:none' id="${count.index + 1}">
                                                <td colspan="5">
                                                    <table class="table table-bordered table-hover table-striped">
                                                        <thead>
                                                            <tr style="background-color: #CCCCCC;color: #ffffff;">
                                                                <th>Sl No</th>
                                                                <th>Office Code</th>
                                                                <th>Office Name</th>
                                                                <th>No of Post Termination Proposal Submitted by DDO</th>
                                                                <th>Submitted To <br/>Department</th>  
                                                            </tr>
                                                        </thead>  
                                                        <c:forEach items="${bgroup.aerWiseDDO}" var="inGroup" varStatus="count">
                                                            <tr>
                                                                <td>${count.index + 1}</td>
                                                                <td>${inGroup.offCode}</td>
                                                                <td>${inGroup.offName}</td>
                                                                <td>${inGroup.totalDDO}</td>
                                                                <td>${inGroup.coStatus}</td>
                                                            </tr>   
                                                        </c:forEach>

                                                    </table> 	

                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                            <th>&nbsp;</th>
                                            <th>&nbsp;</th>                                           
                                            <th>&nbsp;</th>
                                            <th>${GtotalEmp}</th> 
                                            <th>${GtotalCOsubmitted}</th> 
                                        </tr> 

                                    </table>
                                </div>                
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script type="text/javascript">
                function  show_div_details(ids) {
                    $("#" + ids).toggle();
                }
            </script>    

    </body>
</html>
