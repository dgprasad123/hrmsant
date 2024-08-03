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
            function updateSplcaseStatus(trackid, empId, val) {
                if (val == 1) {
                    var status = "Allow";
                } else {
                    var status = "reject";
                }
                var conText = confirm("Do you want to " + status + " this Special Case?");
                window.location = "UpdateSplCaseStatus.htm?trackingId=" + trackid + "&splCaseStatus=" + val + "&empId=" + empId;
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
                                            <th>Date of Request</th>
                                            <th>Consumer No</th>
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th>
                                            <th>Address</th> 
                                            <th>Date of Submission</th>
                                            <th>Status</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">
                                            <tr>
                                                <td>${quarter.empId}</td>
                                                <td>${quarter.empName}</td>
                                                <td>${quarter.dor}</td>
                                                <td>${quarter.consumerNo}</td>
                                                <td>${quarter.quarterNo}</td>
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.qrtrunit}</td>
                                                <td>${quarter.address}</td> 
                                                <td>${quarter.dos}</td>
                                                <td>
                                                    <c:if  test="${quarter.dsStatus eq 'N'}">
                                                        <strong class='text-danger'>Document Submission Pending</strong>
                                                    </c:if>
                                                         <c:if  test="${quarter.dsStatus eq 'Y'}">
                                                        <strong class='text-success'>Document Submitted</strong>
                                                    </c:if>
                                                 
                                                
                                                </td>
                                                <td>
                                                    <c:if  test="${quarter.dsStatus eq 'Y'}">
                                                        <a href="downloadsubmissionDocument.htm?consumerNo=${quarter.consumerNo}&empId=${quarter.empId}&oppCaseId=0" class="btn btn-default" >
                                                            <span class="glyphicon glyphicon-paperclip"></span>  Download Document Submission
                                                        </a> 
                                                    </c:if>
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

    </body>
</html>
