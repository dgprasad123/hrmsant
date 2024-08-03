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
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                hideOrderWindow();
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
            function showOrderWindow() {
                $(".windowbtn").hide();
                $(".allotment").show();
            }
            function hideOrderWindow() {
                $(".allotment").hide();
                $(".windowbtn").show();
            }
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
            function validateQuarterRepairOrder() {
                return true;
            }
            function editRepairOrder(allotmentOrderId) {
                $.post("unitWiseQuarterTypeDataJson.htm", {allotmentOrderId: allotmentOrderId})
                        .done(function (data) {
                            $(".windowbtn").hide();
                            $(".allotment").show();
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
                        <form:form action="quarterRepairOrder.htm" method="POST" commandName="fundSanctionOrderBean" class="form-horizontal">
                            <div class="panel-header">Order Details</div>
                            <div class="panel-body">
                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Order No</label>
                                    <div class="col-sm-4"><form:input path="orderNumber" cssClass="form-control" maxlength="20"/></div>
                                    <label class="control-label col-sm-2">Order Date</label>
                                    <div  class="col-sm-4"><form:input path="orderDate" cssClass="form-control" maxlength="20" readonly="true"/></div>
                                </div>                                
                                <div class="form-group allotment">
                                    <label class="control-label col-sm-2">Type of Work</label>
                                    <div  class="col-sm-4">
                                        <form:select path="typeofWork" cssClass="form-control">
                                            <form:option value="C">CiVil</form:option>
                                            <form:option value="P">PH</form:option>
                                            <form:option value="E">EI</form:option>
                                        </form:select>
                                    </div>
                                    <label class="control-label col-sm-2">Financial Year</label>
                                    <div  class="col-sm-4">
                                        <form:select path="financialYear" cssClass="form-control">
                                            <form:option value="2019-20">2019-20</form:option>
                                            <form:option value="2020-21">2020-21</form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-footer">
                                <button type="submit" name="action" class="btn btn-primary allotment" onclick="return validateQuarterRepairOrder()">Save</button>
                                <button type="button" name="action" class="btn btn-primary windowbtn" onclick="showOrderWindow()">New</button>
                                <button type="button" name="action" class="btn btn-primary allotment" onclick="hideOrderWindow()">Cancel</button>
                            </div>
                        </form:form>
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Order No</th>
                                            <th>Order Date</th>
                                            <th>Type of Work</th>
                                            <th>Financial</th>
                                            <th>Action</th>                                            
                                        </tr>
                                    </thead>
                                    <c:forEach items="${quarterRepairOrderList}" var="quarterRepairOrder" varStatus="cnt">
                                        <tbody id="wrrgrid">
                                            <tr>
                                                <td>${cnt.index+1}</td>
                                                <td>${quarterRepairOrder.orderNumber}</td>
                                                <td>${quarterRepairOrder.orderDate}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${quarterRepairOrder.typeofWork eq 'C'}">
                                                            Civil
                                                        </c:when>
                                                        <c:when test="${quarterRepairOrder.typeofWork eq 'P'}">
                                                            PH
                                                        </c:when>
                                                        <c:when test="${quarterRepairOrder.typeofWork eq 'E'}">
                                                            Electrical
                                                        </c:when>
                                                        <c:otherwise>
                                                            &nbsp;
                                                        </c:otherwise>
                                                    </c:choose>

                                                </td>
                                                <td>${quarterRepairOrder.financialYear}</td>
                                                <td>
                                                    <c:if test="${quarterRepairOrder.smssend eq 'N'}">
                                                        <!--<a class="btn btn-success" href="javascript:editRepairOrder(${quarterRepairOrder.allotmentOrderId})"> <span class="glyphicon glyphicon-pencil"></span> Edit</a>-->
                                                        <a class="btn btn-primary" href="quarterRepairOrderDetail.htm?allotmentOrderId=${quarterRepairOrder.allotmentOrderId}"><span class="glyphicon glyphicon-home"></span> Add Quarter</a>
                                                        <a class="btn btn-primary" href="quarterRepairOrdersendSMS.htm?allotmentOrderId=${quarterRepairOrder.allotmentOrderId}"><span class="glyphicon glyphicon-envelope"></span> Send SMS</a>
                                                    </c:if>
                                                    <c:if test="${quarterRepairOrder.smssend eq 'Y'}">
                                                        <a class="btn btn-primary" href="quarterRepairOrderDetail.htm?allotmentOrderId=${quarterRepairOrder.allotmentOrderId}"><span class="glyphicon glyphicon-search"></span> View</a>
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
