<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>        
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>

        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">       // allotment                 
            $(document).ready(function () {
                $(".allotment").hide();
                var min_year = "2019";
                var max_year = "2020";
                $('#orderDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    //minDate: new Date(min_year, 3, 1),
                    //maxDate: new Date(max_year, 2, 31),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            });
            function showSMSWindow() {
                $(".windowbtn").hide();
                $(".allotment").show();
            }
            function hideSMSWindow() {
                $(".allotment").hide();
                $(".windowbtn").show();
            }
            function sendSMS() {
                var qaId = $("#qaId").val();
                var empId = $("#empId").val();
                var mobileno = $("#mobileno").val();
                var orderno = $("#orderno").val();
                var orderDate = $("#orderDate").val();
                var allotedamt = $("#allotedamt").val();

                $.post("saveFundAllotment.htm", {qaId: qaId, empId: empId, mobileno: mobileno, orderNumber: orderno, orderDate: orderDate, quarterRent: allotedamt})
                        .done(function (data) {
                            if (data.msg == "S") {
                                alert("SMS Send Sucessfully");
                            } else {
                                alert("Error Occured");
                            }
                        })
            }
            function viewLog() {
                var qaId = $("#qaId").val();
                $('#wrrgrid').empty();
                $.post("unitWiseQuarterTypeWiseSMSLogJson.htm", {qaId: qaId})
                        .done(function (data) {
                            var fundAllotmentLogList = data.fundAllotmentLogList;
                            
                            $.each(fundAllotmentLogList, function (i, obj) {
                                var html = '<tr>';
                                html = html + '<td>'+(i+1)+'</td>';
                                html = html + '<td>'+obj.allotedAmt+'</td>';
                                html = html + '<td>'+obj.employeeName+'</td>';
                                html = html + '<td>'+obj.mobileno+'</td>';
                                html = html+'</tr>';                                
                                $("#wrrgrid").append(html);
                            });
                            
                        })
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <form:form action="saveFundAllotment.htm" method="POST"  commandName="empQuarterBean">
                            <div class="panel-header">Quarter Details</div>
                            <div class="panel-body">
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Unit/Area</label>
                                    <div  class="col-2">${quarterBeandetail.qrtrunit}</div >
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Quarter Type</label>
                                    <div  class="col-2">${quarterBeandetail.qrtrtype}</div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Quarter No</label>
                                    <div  class="col-2">${quarterBeandetail.quarterNo}</div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Employee Name</label>
                                    <div  class="col-2"><b>${quarterBeandetail.empName}</b>, ${quarterBeandetail.designation}</div>
                                </div>                                
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Mobile No</label>
                                    <div  class="col-2">
                                        <input type="hidden" name="qaId" id="qaId" value="${quarterBeandetail.qaId}"/>
                                        <input type="hidden" name="empId" id="empId" value="${quarterBeandetail.empId}"/>
                                        <input type="hidden" name="mobileno" id="mobileno" value="${quarterBeandetail.mobileno}"/>
                                        ${quarterBeandetail.mobileno}
                                    </div>
                                </div>

                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Order No</label>
                                    <div  class="col-2"><input type="text" class="form-control" id="orderno"></div>
                                </div>
                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Order Date</label>
                                    <div  class="col-2"><input type="text" class="form-control" id="orderDate" readonly="true"></div>
                                </div>
                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Alloted Amount</label>
                                    <div  class="col-2"><input type="text" class="form-control" id="allotedamt"></div>
                                </div>
                            </div>
                            <div class="panel-footer">
                                <!--
                                <button type="button" name="action" class="btn btn-primary windowbtn" onclick="showSMSWindow()">Sent SMS</button>
                                <button type="button" name="action" class="btn btn-primary allotment" onclick="sendSMS()">Send</button>
                                <button type="button" name="action" class="btn btn-primary allotment" onclick="hideSMSWindow()">Cancel</button>
                                -->
                                <button type="button" name="action" class="btn btn-primary windowbtn" onclick="viewLog()">View Log</button>
                            </div>
                        </form:form>
                    </div>
                    <div class="table-responsive">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Allotment Amount</th>
                                        <th>Employee Name</th> 
                                        <th>Mobile No</th>
                                    </tr>
                                </thead>
                                <tbody id="wrrgrid">

                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>
