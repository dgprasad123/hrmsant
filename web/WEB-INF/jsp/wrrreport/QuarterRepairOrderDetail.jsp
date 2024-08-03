<%-- 
    Document   : QuarterRepairOrderDetail
    Created on : Jun 29, 2020, 5:42:17 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript" src="js/basicjavascript.js"></script>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function showQuarterType() {
                var qrtrunit = $("#qrtrunit").val();
                $('#qrtrtype').empty();
                $('#quarterNo').empty();
                $('#quarterNo').append($('<option>').text('Select').attr('value', ''));
                $.post("unitWiseQuarterTypeDataJson.htm", {quarterunitarea: qrtrunit})
                        .done(function (data) {
                            var unitAreaList = data.unitAreaList;
                            $('#qrtrtype').append($('<option>').text('Select').attr('value', ''));
                            $.each(unitAreaList, function (i, obj) {
                                $('#qrtrtype').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                        })
            }

            function showQuarterNumber() {
                var qrtrunit = $("#qrtrunit").val();
                var qrtrtype = $("#qrtrtype").val();
                $('#quarterNo').empty();
                $.post("unitWiseQuarterTypeWiseQuarterNumberDataJson.htm", {qrtrunit: qrtrunit, qrtrtype: qrtrtype})
                        .done(function (data) {
                            var quarterList = data.quarterList;

                            $.each(quarterList, function (i, obj) {
                                $('#quarterNo').append($('<option>').text(obj.quarterNo).attr('value', obj.quarterNo));
                            });
                        })
            }

            function addQuarterToOrder() {
                var allotmentOrderId = $("#allotmentOrderId").val();
                var typeofWork = $("#typeofWork").val();
                var financialYear = $("#financialYear").val();
                var sanctionamt = $("#sanctionamt").val();
                var qrtrunit = $("#qrtrunit").val();
                var qrtrtype = $("#qrtrtype").val();
                var quarterNo = $("#quarterNo").val();
                var sanctionamt = $("#sanctionamt").val();
                 
                $.post("saveQuarterRepairOrderDetail.htm", {qrtrunit: qrtrunit, qrtrtype: qrtrtype, quarterNo: quarterNo, sanctionamt: sanctionamt, 
                    allotmentOrderId: allotmentOrderId, typeofWork:typeofWork, financialYear:financialYear})
                        .done(function (data) {
                            var html = '<tr>';
                            html = html + '<td>#</td>';
                            html = html + '<td>Qrs No-' + qrtrunit + ',' + qrtrtype + ',' + quarterNo + '</td>';
                            html = html + '<td>Rs.' + sanctionamt + '</td>';
                            html = html + '<td>&nbsp;</td>';
                            html = html + '<td>&nbsp;</td>';
                            html = html + '<td>&nbsp;</td>';
                            html = html + '<td>&nbsp;</td>';
                            html = html + '<tr>';
                            $("#orderdetailtbl").append(html);
                            var quarterList = data.quarterList;

                            $.each(quarterList, function (i, obj) {
                                $('#quarterNo').append($('<option>').text(obj.quarterNo).attr('value', obj.quarterNo));
                            });
                        })
            }
            function deleteData(me, fundid) {
                var cofirmMsg = confirm("Are you sure want to Delete!");
                if (cofirmMsg == true) {
                    $.post("deleteQuarterRepairOrderDetail.htm", {fundid: fundid})
                            .done(function (data) {
                                if (data.msg == "S") {
                                    $(me).parents('tr').remove();
                                } else if (data.msg == "F") {
                                    alert("Error Occured");
                                }
                            })

                }
            }
            function updateSMSString() {
                var smsstring = $("#smsstring").val();
                if (smsstring.trim() != "") {
                    $("input:checkbox[name=fundid]:checked").each(function () {
                        fundid = $(this).val();
                        var t = $(this).parent().siblings()[5];
                        $.post("updateQuarterRepairOrderSMS.htm", {fundid: fundid, smsstring: smsstring})
                                .done(function (data) {
                                    if (data.msg == "S") {
                                        $(t).html(smsstring);
                                    } else if (data.msg == "F") {
                                        $(t).html("Error Occured");
                                    }
                                })
                    });
                } else {
                    alert("SMS String Cannot be blank");
                    $("#smsstring").focus();
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <form:form action="quarterRepairOrder.htm" method="POST" commandName="fundSanctionOrderBean" class="form-horizontal">
                            <div class="panel-header">Order Details</div>
                            <div class="panel-body">
                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Order No</label>
                                    <div class="col-sm-4">
                                        <b>${fundSanctionOrderBean.orderNumber}</b>
                                        <form:hidden path="allotmentOrderId"/>
                                    </div>
                                </div>
                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Order Date</label>
                                    <div  class="col-sm-4"><b>${fundSanctionOrderBean.orderDate}</b></div>
                                </div>
                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Type of Work</label>
                                    <div  class="col-sm-4">
                                        <b>${fundSanctionOrderBean.typeofWork}</b>
                                        <form:hidden path="typeofWork"/>
                                    </div>
                                </div>
                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Financial Year</label>
                                    <div  class="col-sm-4">
                                        <b>${fundSanctionOrderBean.financialYear}</b>
                                        <form:hidden path="financialYear"/>
                                    </div>
                                </div>
                            </div>

                        </form:form>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-body">                            
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>#</th>
                                            <th>Quarter No</th>
                                            <th>Sanction Amount</th>
                                            <th>Emp Id</th>
                                            <th>Emp Name</th>
                                            <th>Mobile</th>
                                            <th>SMS String</th>
                                                <c:if test="${fundSanctionOrderBean.smssend ne 'Y'}">
                                                <th>Delete</th>
                                                </c:if>
                                                <c:if test="${fundSanctionOrderBean.smssend eq 'Y'}">
                                                <th>Delivery Status</th>
                                                </c:if>
                                        </tr>
                                    </thead>
                                    <tbody id="orderdetailtbl">
                                        <c:forEach items="${quarterRepairOrderDetailList}" var="quarterRepairOrderDetail" varStatus="cnt">
                                            <tr>
                                                <td><input type="checkbox" value="${quarterRepairOrderDetail.fundid}" name="fundid"/></th>
                                                <td>${cnt.index + 1}</th>
                                                <td>${quarterRepairOrderDetail.qrtrunit}-${quarterRepairOrderDetail.qrtrtype}/${quarterRepairOrderDetail.quarterNo}</td>
                                                <td>${quarterRepairOrderDetail.sanctionamt}</td>
                                                <td>${quarterRepairOrderDetail.empid}</td>
                                                <td>${quarterRepairOrderDetail.empname}</td>
                                                <td>${quarterRepairOrderDetail.mobileno}</td>
                                                <td>${quarterRepairOrderDetail.smsstring}</td>
                                                <td>                                                    
                                                    <c:if test="${fundSanctionOrderBean.smssend ne 'Y'}">
                                                        <input type="button" value="Delete" class="btn btn-danger" onclick="deleteData(this,${quarterRepairOrderDetail.fundid})"/>
                                                    </c:if>
                                                    <c:if test="${fundSanctionOrderBean.smssend eq 'Y'}">
                                                        ${quarterRepairOrderDetail.smsDeliveryStatus} 
                                                    </c:if>                                                        
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>                        
                    </div>
                    <div class="panel panel-default" style="padding: 5px 10px;">
                        <form:form action="quarterRepairOrderDetail.htm" commandName="fundSanctionOrderBean" class="form-inline">
                            <c:if test="${fundSanctionOrderBean.smssend ne 'Y'}">
                                <div class="form-group">
                                    <label>Unit/Area:</label>
                                    <form:select path="qrtrunit" cssClass="form-control" onchange="showQuarterType()">
                                        <form:option value="">All</form:option>
                                        <c:forEach items="${quarterUnitAreaList}" var="quarterUnitArea">
                                            <form:option value="${quarterUnitArea.value}">${quarterUnitArea.label}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <div class="form-group">
                                    <label>Quarter Type:</label>
                                    <form:select path="qrtrtype" cssClass="form-control" onchange="showQuarterNumber()">
                                        <form:option value="">Select</form:option>                                                                               
                                    </form:select>
                                </div>
                                <div class="form-group">
                                    <label>Quarter No:</label>
                                    <form:select path="quarterNo" cssClass="form-control">
                                        <form:option value="">Select</form:option>                                                                                
                                    </form:select>
                                </div>
                                <div class="form-group">
                                    <label>Sanction Amount:</label>
                                    <form:input path="sanctionamt" cssClass="form-control" onkeypress="return onlyNumbers(event)" maxlength="8"/>
                                </div>
                                <div class="form-group">
                                    <button type="button" name="action" class="btn btn-primary allotment" onclick="return addQuarterToOrder()">Add</button>
                                    <input type="submit" name="action" value="Back" class="btn btn-primary"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <button type="button" name="action" class="btn btn-primary allotment" data-toggle="modal" data-target="#myModal">Change SMS String</button>
                                </div>
                            </c:if>
                            <c:if test="${fundSanctionOrderBean.smssend eq 'Y'}">
                                <input type="submit" name="action" value="Back" class="btn btn-primary allotment"/>
                            </c:if>
                        </form:form>

                    </div>
                </div>
            </div>


            <!-- Modal -->
            <div class="modal fade" id="myModal" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">SMS String</h4>
                        </div>
                        <div class="modal-body">
                            <input type="text" name="smsstring" id="smsstring" class="form-control" maxlength="160"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default" onclick="updateSMSString()">Update</button>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>
