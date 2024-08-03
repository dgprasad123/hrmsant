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
        
        <script type="text/javascript">
            function popupNOc(me) {
                currentRow = me;
                var dataURL = $(me).attr('data-href');
               // alert(dataURL);
                $("#pensionModal").modal('show');
                $('.modal-body').load(dataURL, function () {
                    $('#pensionModal').modal({show: true});
                });
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
                        <h2>PHD(Commercial) NOC List</h2>

                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>DOR</th>
                                            <th>Consumer No</th>
                                            <th>HRMS Id</th>                                            
                                            <th>Employee Name</th>
                                            <th>Post</th>
                                            <th>QTRS NO</th>
                                            <th>QTRS Type</th>
                                            <th>Unit/Area</th>
                                            <th>Tentative QTRS <br/>vacation date</th>
                                            <th>Water Rent Clearance Details</th>
                                            <th>Status/Action</th>

                                        </tr>
                                    </thead>
                                    <tbody id="wrrgrid">
                                        <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">

                                            <tr  >
                                                <td>${cnt.index+1}</td> 
                                                <td>${quarter.orderDate}</td>
                                                <td>${quarter.consumerNo}</td>
                                                <td>${quarter.empId}</td>                                                
                                                <td>${quarter.empName}</td>
                                                <td>${quarter.designation}</td>
                                                <td>${quarter.quarterNo}</td>                                                                                                
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.qrtrunit}</td>
                                                <td>${quarter.tvd}</td>
                                                <td>                                                  

                                                   <c:if test="${not empty quarter.wateroriginalFilename &&  quarter.waterNocStatus eq 'Y'  }">
                                                        <a href="downloadQrtNoc.htm?nocId=${quarter.nocId}&empId=${quarter.empId}&nocType=WaterRent" class="btn btn-default" >
                                                            <span class="glyphicon glyphicon-paperclip"></span>  Download document<br/>
                                                        </a> 
                                                    </c:if>
                                                     <c:if test="${not empty quarter.waterNocReason &&  quarter.waterNocStatus eq 'Y'  }">
                                                         <strong class='text-danger'>${quarter.waterNocReason}</strong>
                                                    </c:if>
                                                 
                                                 
                                                 

                                                </td>
                                                <td>
                                                    <c:if test="${empty quarter.waterNocReason && not empty quarter.wateroriginalFilename &&  quarter.waterNocStatus eq 'Y'   }">
                                                        <strong class=" text-success"> Water Rent Clearance is Submitted</strong>
                                                    </c:if>
                                                    
                                                    <c:if test="${not empty quarter.waterNocReason }">
                                                        <strong class='text-danger'> Water Rent Clearance is Declined</strong>                                                                                
                                                    </c:if>
                                                  
                                                   
                                                    <c:if test="${quarter.waterNocStatus eq 'N' || empty quarter.waterNocStatus  }">
                                                        <a href="javascript:void(0);" data-href="QrtWaterClearanceUpload.htm?consumerNo=${quarter.consumerNo}&nocId=${quarter.nocId}&empId=${quarter.empId}&nocType=WaterRent" onclick="popupNOc(this)" id='id_view' data-target="#pensionModal" class='text-danger'>Upload Water Rent Clearance</a>

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
        <div id="pensionModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">&nbsp;</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">                       
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>                
    </body>
</html>
