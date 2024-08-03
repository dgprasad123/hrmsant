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
             $(document).ready(function() {
                $('#id_tbl').DataTable({
                    "pageLength": 100,
                    "lengthMenu": [[100, 200, 300], [100, 200, 300, "All"]]

                });
                
            });
            function updateSplcaseStatus(trackid, empId, val) {
                if (val == 1) {
                    var status = "Allow";
                } else {
                    var status = "reject";
                }
                var conText = confirm("Do you want to " + status + " this Special Case?");
              
                if(conText){     
                   // alert("UpdateSplCaseStatus.htm?trackingId=" + trackid + "&splCaseStatus=" + val + "&empId=" + empId);
                    window.location = "UpdateSplCaseStatus.htm?trackingId=" + trackid + "&splCaseStatus=" + val + "&empId=" + empId;
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
                        <h2>
                            Special cases for extension of retention period
                        </h2>   


                        <div class="panel panel-default " >
                            <div class="panel-heading">&nbsp;</div>
                            <div class="panel-body">
                                <table class="table table-bordered" id='id_tbl'>
                                    <thead>
                                        <tr bgcolor="#FAFAFA">
                                            <th>HRMS ID</th>
                                            <th>Name</th>
                                            <th>Date of Request</th>
                                            <th>Consumer No</th>
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th>
                                            <th>Address</th>                                 
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">
                                            <tr>
                                                <td>${quarter.empId}</td>
                                                <td>${quarter.empName}</td>
                                                <td>${quarter.wefFromDate}</td>
                                                <td>${quarter.consumerNo}</td>
                                                <td>${quarter.quarterNo}</td>
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.qrtrunit}</td>
                                                <td>${quarter.address}</td> 

                                                <td>  
                                                     <c:if test = "${not empty quarter.extensionFromDate}">
                                                        <a href="downloadExtensionRequest.htm?consumerNo=${quarter.consumerNo}&empId=${quarter.empId}"  class="btn btn-danger">Download Extension Request</a>
                                                    </c:if> 
                                                     <c:if test = "${not empty quarter.originalFilename}">
                                                        <a href="downloadSplQrtCase.htm?trackingId=${quarter.trackingId}&consumerNo=${quarter.consumerNo}&empId=${quarter.empId}"  class="btn btn-warning">Attachment</a>
                                                    </c:if>    
                                                        

                                                </td> 
                                                <td>
                                                    <c:if test = "${ empty quarter.splCaseStatus  || quarter.splCaseStatus eq '0'}">
                                                        <input type="radio" name="splCaseStatus" value="1" onclick="updateSplcaseStatus(${quarter.trackingId},'${quarter.empId}', 1)" /><strong class="text-success">&nbsp;Extension Allowed</strong><br/>
                                                        <input type="radio" name="splCaseStatus" value="2"  onclick="updateSplcaseStatus(${quarter.trackingId},'${quarter.empId}', 2)"/><strong class="text-danger">&nbsp;Extension Rejected</strong><br/> 
                                                    </c:if>
                                                    <c:if test = "${quarter.splCaseStatus eq '1'}">
                                                        <h5 class="text-success">&nbsp;Extension Allowed</h5>
                                                        <c:if test = "${ empty quarter.orderNumber }">
                                                            <a href="ExtensionRetention.htm?consumerNo=${quarter.consumerNo}&trackingId=${quarter.trackingId}&empId=${quarter.empId}" class="btn btn-info">
                                                               Create Extended Retention Permission
                                                             </a>
                                                        </c:if>
                                                        <c:if test = "${not empty quarter.orderNumber}">
                                                            <a href="GeneratePDFExtendedRetention.htm?consumerNo=${quarter.consumerNo}&empId=${quarter.empId}" class="btn btn-warning">
                                                                    View Extended Retention Permission<br/></a>
                                                        </c:if>
                                                        
                                                    </c:if>
                                                    <c:if test = "${quarter.splCaseStatus eq '2'}">
                                                        <h5 class="text-danger">&nbsp;Extension Rejected</h5>
                                                    </c:if>


                                                </td

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
