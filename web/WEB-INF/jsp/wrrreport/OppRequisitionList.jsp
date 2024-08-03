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
        <script language="javascript" type="text/javascript" >
            $(document).ready(function () {
                $('#id_tbl').DataTable( {
                     "pageLength": 100,
                      "lengthMenu": [[100, 200, 300], [100, 200, 300, "All"]]
                   
                } );
            });
        </script>

        <script type="text/javascript">
            $(document).ready(function () {
                if ($("#quarterunitarea").val() != "") {
                    showQuarterType();
                }
            });

            function showQuarterType() {
                var qrtrunit = $("#qrtrunit").val();
                $('#qrtrtype').empty();
                $.post("unitWiseQuarterTypeDataJson.htm", {quarterunitarea: qrtrunit})
                        .done(function (data) {
                            var unitAreaList = data.unitAreaList;

                            $.each(unitAreaList, function (i, obj) {
                                $('#qrtrtype').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                        })
            }
            function validate() {
                if ($("#quarterunitarea").val() == "") {
                    alert("Choose Unit/Area");
                    return false;
                }
            }
            function VacateNotice(cno,empid,otype){
                var constatus=confirm("Do you want to sent vacation Notice?");
                if(constatus){
                    window.location="VacationNotice.htm?consumerNo="+cno+"&empId="+empid+"&occupationTypes="+otype;
                    
                }                
            }
            function EvictionNoticeStatus(cno,empid,otype){
                 var constatus=confirm("Do you want to sent Eviction Notice?");
                if(constatus){
                    window.location="EvictionNotice.htm?consumerNo="+cno+"&empId="+empid+"&occupationTypes="+otype;
                    
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
                        <h2>OPP. requisition List</h2>
                     

                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered" width="100%" cellspacing="0" id='id_tbl' >
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Consumer No</th>
                                            <th>HRMS Id</th> 
                                            <th>Date of Retirement</th>                                           
                                           
                                            <th>Employee Name</th>
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th>  
                                            <th>Office Name</th>
                                            <th>Dist Name</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody id="wrrgrid">
                                        <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">

                                            <tr  >
                                                <td>${cnt.index+1}</td> 
                                                <td>${quarter.consumerNo}</td>
                                                <td>${quarter.empId}</td>  
                                                <td>${quarter.dos}</td>                                                
                                                <td>${quarter.empName}</td>
                                                <td>${quarter.quarterNo}</td>                                                                                                
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.qrtrunit}</td> 
                                                <td>${quarter.offName}</td> 
                                                <td>${quarter.distName}</td> 
                                               <th>
                                                
                                               
                                                  <c:if test = "${ quarter.vacateStatus eq 'No'   }">
                                                     <a href="GeneratePDFOPP.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-primary">Download OPP Requisition</a>
                                                      
                                                 </c:if> 
                                                 <c:if test = "${ not empty quarter.vacateStatus && empty quarter.evictionNotice && quarter.vacateStatus eq 'No'   }">
                                                     <a href="#" onclick="EvictionNoticeStatus(${quarter.consumerNo},${quarter.empId},${occupationTypes})"  class="btn btn-danger">Eviction Notice</a>
                                                 </c:if>
                                                  <c:if test = "${  not empty quarter.evictionNotice  &&  quarter.vacateStatus eq 'No' }">
                                                     <a href="GeneratePDFEvictionNotice.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-danger">Download Eviction Order</a>
                                                        
                                                 </c:if> 
                                                  
                                                     
                                                    
                                                    
                                                                                               
                                            </th>
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
