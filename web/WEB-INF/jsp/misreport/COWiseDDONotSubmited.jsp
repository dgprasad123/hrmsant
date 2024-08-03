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
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">CO Wise DDO Not Submitted AER Report(Fy:2023-24)</div>
                        <div align='right' style='margin:10px'><a href="TreasuryWiseAERStatus.htm"><input type='button' value="Treasury Wise AER Report " class="btn btn-primary"  name='btn1'  /></a>&nbsp;&nbsp;<a href="COWiseDeptAERStatus.htm"><input type='button' value="CO Wise AER Status" class="btn btn-primary"  name='btn1'  /></a>&nbsp;&nbsp;<a href="DeptWiseAERStatus.htm"><input type='button' value="Department Wise AER Status" class="btn btn-primary"  name='btn1'  /></a>&nbsp;&nbsp;<a href="DistWiseAERReport.htm"><input type='button' class="btn btn-primary"  name='btn2' value="District Wise AER Status"/></a></div>
                        <div class="panel-body">
                            <div class="table-responsive">

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr style="background-color: #0071c5;color: #ffffff;">
                                                <th>Sl No</th>
                                                <th>Office Code</th>
                                                <th>Office Name</th>
                                               <th>Submitted To <br/>CO</th>  

                                            </tr>
                                        </thead>
                                        <c:set var="GtotalEmp" value="${0}" /> 
                                        <c:set var="GtotalCOsubmitted" value="${0}" />    
                                        <c:forEach items="${AERDetails}" var="bgroup" varStatus="count">
                                             <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${bgroup.offCode}</td>
                                                <td>${bgroup.offName}</td>                                              
                                                <td>${bgroup.coStatus}</td>
                                            </tr> 
                                          
                                           
                                        </c:forEach>
                                       

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
