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

        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           


        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>

        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>

        <script>
            $(document).ready(function() {
                $('#id_tbl').DataTable({
                    "pageLength": 100,
                    "lengthMenu": [[100, 200, 300], [100, 200, 300, "All"]]

                });


            });
            $(function() {
                $('#txtperiodFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtperiodTo').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function approve_vacate() {
                var conf = confirm("Do you want approve this Vacate Quarter");

            }
          
            function OPP_send_court(cno,empid){
                var conf=confirm("DO you want to send this OPP Case to Estate Court?");
                if(conf){
                    window.location="UpdateOPPSendStatus.htm?consumerNo="+cno+"&empId="+empid;
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
                    <div align="center" style="margin-top:10px;margin-bottom:7px;">
                        <h2>OPP Case List</h2>
                        <div class="panel panel-primary">
                            <div class="panel-heading">Search Results</div>
                            <div class="panel-body">
                                <form:form autocomplete="off" role="form" action="#" commandName="empQuarterBean"  method="post">
                                    <input type="hidden" name="vstatus" value="${vstatus}"/>
                                    <div class="row" >
                                        <div class="col-lg-1"  style="left:50px;">From Date:</div>

                                        <div class="col-lg-2" >

                                            <div style="left:50px;" class='input-group date' id='processDate'>

                                                <form:input class="form-control" path="txtperiodFrom" id="txtperiodFrom" readonly="true"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-lg-1" style="left:50px;" >To Date:</div>
                                        <div class="col-lg-2" >
                                            <div class='input-group date' id='processDate'>
                                                <form:input class="form-control" path="txtperiodTo" id="txtperiodTo" readonly="true"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-lg-2" >
                                            <input type="submit" name="action" value="Search" class="btn btn-success"/>
                                        </div>
                                    </div> 


                                </form:form>  

                                <hr/>    

                                <div class="table-responsive">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped" id='id_tbl' style="font-size: 14px">
                                    <thead>
                                        <tr>
                                            <th>#</th> 
                                            <th>OPP Case No</th>                                           
                                            <th>HRMS Id</th>                                            
                                            <th>Employee Name</th>
                                            <th>Mobile</th>
                                            <th>DOB</th>
                                            <th>Post</th>
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th>  
                                            <th>OPP Send Status</th>
                                            <th>Action</th>

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
                                                <td>${quarter.designation}</td>
                                                <td>${quarter.quarterNo}</td>                                                                                                
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.qrtrunit}</td>
                                                <td>
                                                    <c:if test = "${  quarter.opp_display eq 'N'   }">
                                                        <strong style='color:red'>NO</strong>
                                                    </c:if>
                                                    <c:if test = "${  quarter.opp_display eq 'Y'   }">
                                                        <strong style='color:green'>YES</strong>
                                                    </c:if>    
                                                </td>
                                                <td>
                                                    <c:if test = "${ empty quarter.vacateStatus   }">
                                                        <a href="notVacationStatus.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${quarter.occupationTypes}&empId=${quarter.empId}"  class="btn btn-info">Generate OPP Requisition</a>
                                                    </c:if>

                                                    <c:if test = "${ quarter.vacateStatus eq 'No'   }">
                                                        <a href="GeneratePDFOPP.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-primary">Download OPP Requisition</a>

                                                    </c:if> 
                                                    <c:if test = "${  quarter.opp_display eq 'N'   }">
                                                        <a href="#" onclick="OPP_send_court('${quarter.consumerNo}', '${quarter.empId}')"  class="btn btn-info">OPP Send to Estate Court</a>
                                                    </c:if>
                                                  
                                                        <a href="VacateOPPqrt.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-warning">Vacation Intimation</a>
                                                  
                                                </td>


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
            </div>
        </div>
    </body>
</html>
