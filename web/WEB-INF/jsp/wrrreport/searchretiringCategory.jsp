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
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function() {
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
                $('#id_tbl').DataTable({
                    "pageLength": 100,
                    "lengthMenu": [[100, 200, 300], [100, 200, 300, "All"]]

                });
                $('.openPopup').on('click', function() {
                    var dataURL = $(this).attr('data-href');
                    // alert();
                    $('.modal-body').load(dataURL, function() {
                        $('#myModal').modal({show: true});
                    });
                });
            });
        </script>

        <script type="text/javascript">
            $(document).ready(function() {
                if ($("#quarterunitarea").val() != "") {
                    showQuarterType();
                }
            });

            function showQuarterType() {
                var qrtrunit = $("#qrtrunit").val();
                $('#qrtrtype').empty();
                $.post("unitWiseQuarterTypeDataJson.htm", {quarterunitarea: qrtrunit})
                        .done(function(data) {
                            var unitAreaList = data.unitAreaList;

                            $.each(unitAreaList, function(i, obj) {
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
            function VacateNotice(cno, empid, otype) {
                var constatus = confirm("Are you sure you want to send vacation Notice?");
                if (constatus) {
                    window.location = "VacationNotice.htm?consumerNo=" + cno + "&empId=" + empid + "&occupationTypes=" + otype;

                }
            }
            function EvictionNoticeStatus(cno, empid, otype) {
                var constatus = confirm("Are you sure you want to send Eviction Notice?");
                if (constatus) {
                    window.location = "EvictionNotice.htm?consumerNo=" + cno + "&empId=" + empid + "&occupationTypes=" + otype;

                }
            }
            function document_submission(cno, empid, otype) {
                var constatus = confirm("Are you sure you want to send Document Submission Request?");
                if (constatus) {
                    window.location = "DocumentSubmissionRequest.htm?consumerNo=" + cno + "&empId=" + empid + "&occupationTypes=" + otype;

                }
            }
            function hide_records(cno, empid, otype) {
                var checkBox = document.getElementById(cno);
                if (checkBox.checked == true) {
                    var constatus = confirm("Are you sure you want to hide this information?");
                    if (constatus) {
                        window.location = "HideRecord.htm?consumerNo=" + cno + "&empId=" + empid + "&occupationTypes=" + otype;

                    }
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
                    <div align="center" style="margin-top:5px;margin-bottom:50px;">
                        <h2>Status of Allotted Government Quarters</h2>
                                  
                       
                        <div class="panel panel-primary">
                            <div class="panel-heading">Search by Date Range</div>

                            
                        </div>
<div class="row" style="margin-top:10px;margin-left:10px;">
<form:form role="form" action="searchretiringCategoryResult.htm" commandName="searchtransferCategory"  method="post">
                                    <input type="hidden" name="occupationTypes" value="7"/>                                
                                <div class="col-md-3">
                                    <div class="form-group row">
                                        <label class="col-sm-6 col-form-label" style="text-align:right;">From Date:</label>
                                        <div class="col-sm-6"> <div style="left:50px;" class='input-group date' >
                                                    <input type="text"  class="form-control" name="txtperiodFrom" id="txtperiodFrom" readonly="true"/>                                                   
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span>
                                                </div></div>
                                    </div>
                                </div>
<div class="col-md-3">
                                    <div class="form-group row">
                                        <label class="col-sm-6 col-form-label" style="text-align:right;">To Date:</label>
                                        <div class="col-sm-6"> <div class='input-group date' style="position:relative;z-index:100" >
                                                    <input type="text"  class="form-control" name="txtperiodTo" id="txtperiodTo" readonly="true"/>   
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span>
                                                </div></div>
                                    </div>
                                </div>   
                                    <div class="col-md-3">
                                        <input type="submit" class="btn btn-primary" value="Submit"  />
                                            &nbsp;
                                            <a href="getTransferList.htm"><input type="button" class="btn btn-primary" value="Back"  /></a>
                                    </div>
                                                                    </form:form> 
                            </div>                         

                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered" width="100%" cellspacing="0" id='id_tbl' >
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Current Posting</th>                                          

                                            <th>HRMS Id</th> 
                                            <th >Contact No</th> 
                                             <th >DOB</th>   
                                            <th>Date of Retirement</th>                                                
                                            <th>Employee Name</th>
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th>  
                                            <th>Office Name</th>
                                            <th>District Name</th>
                                                <c:if test = "${not empty UserName && UserName=='garentadmin'}">
                                                <th>Action</th>
                                                </c:if>
                                        </tr>
                                    </thead>
                                    <tbody id="wrrgrid">
                                        <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">

                                            <tr  >
                                                <td>${cnt.index+1}</td> 
                                                <td><a href="javascript:void(0);" data-href="EmployeeBasicProfile.htm?empid=${quarter.empId}" class="openPopup label label-success">View</td></td>                                              
                                                <td>${quarter.empId}</td> 
                                                <td ><strong>${quarter.mobileno}</strong></td>
                                                 <td>${quarter.dob}</td>
                                                 <td>${quarter.dos}</td>
                                                <td>${quarter.empName}</td>
                                                <td>${quarter.quarterNo}</td>                                                                                                
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.qrtrunit}</td> 
                                                <td>${quarter.offName}</td> 
                                                <td>${quarter.distName}</td> 
                                                <c:if test = "${not empty UserName && UserName=='garentadmin'}">
                                                    <th>
                                                        <c:if test = "${empty quarter.documentSubmission && ( occupationTypes eq '4'  ) }">
                                                            <a href="#" onclick="document_submission('${quarter.consumerNo}', '${quarter.empId}',${occupationTypes})"  class="btn btn-success">Request for Documents Submission</a>
                                                        </c:if>
                                                        <c:if test = "${ empty quarter.retentionStatus && (occupationTypes eq '1' || occupationTypes eq '3' || occupationTypes eq '5' || occupationTypes eq '6' || occupationTypes eq '7'  || (occupationTypes eq '4'   && not empty quarter.documentSubmission ) ) }">
                                                            <a href="QtrRetention.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-info" target="_blank">Create Retention Permission</a>
                                                        </c:if>


                                                        <!-- <c:if test = "${empty quarter.retentionStatus && (  occupationTypes eq '3'  ) }">
                                                             <a href="DownloadAutoQtrRetention.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-danger">View Auto Retention Permission</a>
                                                        </c:if>-->
                                                        <c:if test = "${empty quarter.retentionStatus && (  occupationTypes eq '1'  )  && not empty quarter.documentSubmission }">
                                                            <a href="DownloadAutoQtrRetention.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-info">Retention Permission</a>
                                                        </c:if>     

                                                        <c:if test = "${not empty quarter.retentionStatus && quarter.retentionStatus eq 'Y' }">
                                                            <a href="GeneratePDFRetention.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}" class="btn btn-info">
                                                                Retention Permission<br/></a>
                                                            </c:if> 
                                                            <c:if test = "${not empty quarter.retentionStatus && empty quarter.vacateNotice }">
                                                            <a href="#" onclick="VacateNotice('${quarter.consumerNo}', '${quarter.empId}',${occupationTypes})"  class="btn btn-danger">Vacation Notice</a>
                                                        </c:if> 

                                                        <!--<c:if test = "${ (not empty quarter.vacateNotice && empty quarter.vacateStatus) || (quarter.vacateStatus eq 'Yes' || quarter.vacateStatus eq 'No')  }">
                                                            <a href="GeneratePDFVacationNotice.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-warning">Download Vacation Notice</a>
                                                        </c:if>   -->   

                                                        <c:if test = "${not empty quarter.retentionStatus && not empty quarter.vacateNotice && empty quarter.vacateStatus }">
                                                            <a href="VacationStatus.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-warning">Vacation Status</a>
                                                        </c:if> 

                                                        <c:if test = "${ quarter.vacateStatus eq 'Yes'   }">
                                                            <a href="GeneratePDFIntimation.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-primary">View Intimation Letter</a>
                                                        </c:if>   

                                                        <c:if test = "${ quarter.vacateStatus eq 'No'   }">
                                                            <a href="GeneratePDFOPP.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-primary">View OPP Requisition</a>

                                                        </c:if> 

                                                        <!-- <c:if test = "${  not empty quarter.evictionNotice  &&  quarter.vacateStatus eq 'No' }">
                                                             <a href="GeneratePDFEvictionNotice.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-danger">Download Eviction Order(5(2))</a>
                                                        </c:if> -->

                                                        <c:if test = "${  not empty quarter.evictionNotice &&  quarter.vacateStatus eq 'No'  }">

                                                            <a href="VacationStatus.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-warning">Vacation Status</a>

                                                        </c:if>       




                                                    </th>
                                                </c:if>
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
        <div class="modal fade" id="myModal" role="dialog">
            <div class="modal-dialog">


                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Employee Basic Profile</h4>
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
