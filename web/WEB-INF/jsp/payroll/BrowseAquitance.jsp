<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(window).load(function () {
                // Fill modal with content from link href
                $("#viewModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });

                $("#editModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-content").load(link.attr("href"));
                });
            })
            function reprocess() {
                var aqslnoval = $('input[name=aqslno]:checked').val();
                var billNoval = $("#billNo").val();
                if (aqslnoval === undefined) {
                    alert("Please Check Employee to Process");
                } else {
                    $("#btnreprocess").prop('disabled', true);
                    $.post("reprocessPayAqMast.htm", {aqslno: aqslnoval, billNo: billNoval})
                            .done(function (data) {
                                $("#btnreprocess").prop('disabled', false);
                                if (data.processed == 1) {
                                    $("#reprocessmsg").html('<span class="alert alert-success">Processed</span>');
                                } else {
                                    $("#reprocessmsg").html('<span class="alert alert-danger">Error Occurred</span>');
                                }
                            });
                }
            }
        </script>
        <style type="text/css">
            .fixed-panel {
                min-height: 500;
                max-height: 500;
                overflow-y: scroll;
            }
            .table tbody tr > td.warning {
                background-color: #fcf8e3 !important;
            }
        </style>
    </head>
    <body>
        <input type="hidden" name="billNo" id="billNo" value="${billNo}"/>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                     <a href="getPayBillList.htm?billNo=${billNo}" class="btn btn-primary">Return</a>
                </div>
                <div class="panel-body fixed-panel">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>&nbsp;</th>
                                <th width="3%">Sl No</th>
                                <th width="25%">Employee Id </br> Account Number</th>
                                <th width="25%">Employee Name</th>
                                <th width="22%">Designation</th>
                                <th width="15%">Pay Scale</th>
                                <th width="5%">Basic</th>
                                <th width="5%">Gross</th>
                                <th width="5%">Deduction</th>
                                <th width="5%">Pvt. Deduction</th>
                                <th width="5%">View</th>
                                <th width="5%">Edit</th>
                                <th width="5%">Stop Pay</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${aquitanceList}" var="aquitance" varStatus="cnt">
                                <c:if test="${aquitance.employeeDepStatus eq 'HELDUP' }">
                                    <tr style="background-color:#FF0000;">
                                </c:if>
                                <c:if test="${aquitance.employeeDepStatus ne 'HELDUP' }">
                                    <tr>
                                </c:if>
                                    <td><input type="radio" name="aqslno" value="${aquitance.aqslno}"/></td>
                                    <td>${cnt.index+1}</td>
                                    <td>${aquitance.empcode} </br> ${aquitance.gpfaccno}</td>
                                    <td>${aquitance.empname}</td>
                                    <td>${aquitance.curdesg}</td>
                                    <td>${aquitance.payscale}</td>
                                    <td>${aquitance.curbasic}</td>
                                    <td>${aquitance.grossamt}</td>
                                    <td>${aquitance.dedamt}</td>
                                    <td>${aquitance.prvdedamt}</td>
                                    <td>
                                        <a href="browseAquitanceDataView.htm?aqslno=${aquitance.aqslno}&billNo=${billNo}" class="btn btn-default">View</a>
                                    </td>
                                    <td>
                                        
                                        <c:if test="${stopSingleProcess eq 'N'}" >
                                            &nbsp;&nbsp;<a href="browseAquitanceData.htm?aqslno=${aquitance.aqslno}&billNo=${billNo}" class="btn btn-default">Edit</a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${billstatus < 2|| billstatus ==4 || billstatus ==8}">
                                            <a href="stoppay.htm?aqslno=${aquitance.aqslno}&billNo=${billNo}" class="btn btn-default" onclick="return confirm('Are you sure to stop the pay of current employee')">Stop pay</a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}">                                        
                                            <a href="javascript:reprocessArrAqMast('${arrAqMast.aqSlNo}','${billNo}')" class="btn btn-default">Reproess</a>
                                        </c:if>    
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    
                    <c:if test="${stopSingleProcess eq 'N'}" >
                        <input type="button" id="btnreprocess" onclick="reprocess()" value="Reprocess" class="btn btn-default"/>
                    
                    </c:if>
                    <span id="reprocessmsg"></span>            


                </div>
            </div>
        </div>

        <!-- Print Bill Modal -->
        <div id="viewModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">View Aquitance Details</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">
                        <span id="msg"><span class="alert alert-danger">Error Occurred</span></span>                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>

        <!-- Print Bill Modal -->
        <div id="editModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Edit Aquitance Details</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">
                        <span id="msg"></span>                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>
