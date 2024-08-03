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
            function export_excel() {
                var vstatus = $("#vstatus").val();
                var txtperiodFrom = $("#txtperiodFrom").val();
                var txtperiodTo = $("#txtperiodTo").val();
               // alert("downloadVacationOccupationExcel.htm?vstatus="+vstatus+"&txtperiodFrom="+txtperiodFrom+"&txtperiodTo="+txtperiodTo);
               
               window.location="downloadVacationOccupationExcel.htm?vacateStatus="+vstatus+"&txtperiodFrom="+txtperiodFrom+"&txtperiodTo="+txtperiodTo;
            }
             function print_page() {
                var vstatus = $("#vstatus").val();
                var txtperiodFrom = $("#txtperiodFrom").val();
                var txtperiodTo = $("#txtperiodTo").val();
               // alert("downloadVacationOccupationExcel.htm?vstatus="+vstatus+"&txtperiodFrom="+txtperiodFrom+"&txtperiodTo="+txtperiodTo);
               
               window.location="occupationVacationPrint.htm?vacateStatus="+vstatus+"&txtperiodFrom="+txtperiodFrom+"&txtperiodTo="+txtperiodTo;
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
                        <h2>${vstatus} Quarter List</h2>
                        <div class="panel panel-primary">
                            <div class="panel-heading">Search Results</div>
                            <div class="panel-body">
                                <form:form autocomplete="off" role="form" action="#" commandName="empQuarterBean"  method="post">
                                    <input type="hidden" name="vstatus" value="${vstatus}" id='vstatus'/>
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
                                <p style="margin-right:10px" align="right">
                               <!--  <a href="#" class="btn btn-primary"  onclick="export_excel()">Export to Excel</a>&nbsp;-->
                                    <a href="#" class="btn btn-danger"  onclick="print_page()" target="_blank">Print</a>
                                
                                </p>
                                <div class="table-responsive">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped" id='id_tbl'  style="font-size: 14px">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>DOE</th>                                                    
                                                    <th>HRMS Id</th>                                            
                                                    <th>Employee Name</th>
                                                    <th>Mobile</th>
                                                    <th>Post</th>
                                                    <th>Unit/Area</th>
                                                    <th>QRS Type</th>
                                                    <th>QRS NO</th>
                                                    <th>Status</th>
                                                    <th>${vstatus} Date</th>

                                                </tr>
                                            </thead>
                                            <tbody id="wrrgrid">
                                                <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">

                                                    <tr  >
                                                        <td>${cnt.index+1}</td> 
                                                        <td>${quarter.orderDate}</td>

                                                        <td>${quarter.empId}</td>                                                
                                                        <td>${quarter.empName}</td>
                                                        <td>${quarter.mobileno}</td>
                                                        <td>${quarter.designation}</td>
                                                        <td>${quarter.qrtrunit}</td>                                                                                                
                                                        <td>${quarter.qrtrtype}</td>
                                                        <td>${quarter.quarterNo}</td>
                                                        
                                                        <td>

                                                            <c:if test="${quarter.vacateStatus eq 'V'}">
                                                                Vacated
                                                            </c:if>
                                                            <c:if test="${quarter.vacateStatus eq 'O'}">
                                                                Occupied
                                                            </c:if>
                                                        </td>
                                                        <td>${quarter.dos}</td>
                                                        <!--<td style='color:green'><a href="#" onclick="approve_vacate()"><strong>Approve</strong></a></td>
                                                        -->
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