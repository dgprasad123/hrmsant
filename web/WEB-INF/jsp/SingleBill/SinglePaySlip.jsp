<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            
            $(window).load(function () {
                $("#myModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
                
                showhideArrearBillType();
            })
            
            function showYear(me) {
                $.ajax({
                    type: "POST",
                    url: "getBillYearDdoWise.htm?billtype=" + $(me).val() + "&ddoName=" + $('#ddoName').val(),
                    success: function(data) {
                        $('#sltYear').empty();

                        if (data == '') {
                            $('#sltYear').append($('<option>', {
                                value: '',
                                text: 'Select Year'
                            }));
                        }
                        $.each(data, function(i, obj)
                        {
                            $('#sltYear').append($('<option>', {
                                value: obj.value,
                                text: obj.label
                            }));

                        });
                        showMonth();
                    }
                });

            }

            function showMonth() {
                var yearVal = $('#sltYear').val();
                $.ajax({
                    type: "POST",
                    url: "getSBillMonthYearWise.htm?sltYear=" + yearVal + "&ddoName=" + $('#ddoName').val() + "&billType=" + $('#billType').val(),
                    success: function(data) {
                        $('#sltMonth').empty();

                        if (data == '') {
                            $('#sltMonth').append($('<option>', {
                                value: '',
                                text: 'Select One'
                            }));
                        }
                        $.each(data, function(i, obj)
                        {
                            $('#sltMonth').append($('<option>', {
                                value: obj.value,
                                text: obj.label
                            }));

                        });
                    }
                });
            }

            function validate() {
                if($("#billType").val() == "ARREAR"){
                    if($("#arrearType").val() == ""){
                        alert("Please Select Arrear Type");
                        $("#arrearType").focus();
                        return false;
                    }
                }
                if ($("#ddoName").val() == "")
                {
                    alert("Please Select DDO Name");
                    $("#ddoName").focus();
                    return false;
                }
                if ($("#sltYear").val() == "")
                {
                    alert("Please Select Year");
                    $("#sltYear").focus();
                    return false;
                }
                if ($("#sltMonth").val() == "")
                {
                    alert("Please Select Month");
                    $("#sltMonth").focus();
                    return false;
                }
            }
            
            function showhideArrearBillType(){
                if($("#billType").val() == "ARREAR"){
                    $("#arreardiv").show();
                }else{
                    $("#arrearType").val('');
                    $("#arreardiv").hide();
                }
            }

        </script>    
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/> 
            <div id="page-wrapper">
                <form:form class="form-inline" action="showDdoDetails.htm" commandName="singleFormBean">
                    <div class="container-fluid" style="padding-top: 5px;padding-bottom: 5px;">
                        <div class="panel panel-default">
                            <div class="row">
                                <div class="col-lg-12" align="center"> 
                                    <b style="color:#0D8BE6;font-size:20px;"> SINGLE BILL VERIFICATION </b> 
                                </div>
                            </div>
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="form-group" style="width:15%;">
                                            <label for="billType">  Bill TYPE :</label>
                                            <form:select path="billType" id="billType" class="form-control" onchange="showhideArrearBillType();">
                                                <form:option value="PAY">Pay Bill</form:option>
                                                <form:option value="ARREAR">Arrear Bill</form:option>
                                            </form:select>               
                                        </div>
                                        <div class="form-group" style="width:20%;" id="arreardiv">
                                            <label for="arrearType">  Arrear TYPE :</label>
                                            <form:select path="arrearType" id="arrearType" class="form-control">
                                                <form:option value="">--Select--</form:option>
                                                <form:option value="OTHER_ARREAR">OTHER ARREAR</form:option>
                                                <form:option value="ARREAR">7th Pay Arrear(40% or 100%)</form:option>
                                                 <form:option value="ARREAR_J">7th Pay Arrear(Judiciary)(First 25% or 100%)</form:option>
                                                <form:option value="ARREAR_6">7th Pay Arrear(Percentage)</form:option>
                                                <form:option value="ARREAR_6_J">7th Pay Arrear(Judiciary)(Percentage)</form:option>
                                                <form:option value="ARREAR_NPS">NPS Arrear 5%(10% of rest 50%)</form:option>
                                            </form:select>               
                                        </div>
                                        <div class="form-group" style="width:15%;">
                                            <label for="ddoName">  DDO NAME :</label>
                                            <form:select path="ddoName" id="ddoName" class="form-control" style="width:70%;" onchange="showYear(this)">
                                                <option value="">Select</option>
                                                <form:options items="${DDOList}" itemValue="value" itemLabel="label"/>
                                            </form:select>                
                                        </div>
                                        <div class="form-group" style="width:15%;">
                                            <label for="sltYear">&nbsp;Year:</label>
                                            <form:select path="sltYear" id="sltYear" class="form-control" onchange="showMonth()">
                                                <option value="">Select</option>
                                                <form:options items="${YearList}" itemValue="value" itemLabel="label"/>                                        
                                            </form:select>
                                        </div>
                                        <div class="form-group" style="width:15%;">
                                            <label for="sltMonth">&nbsp;Month:</label>
                                            <form:select path="sltMonth" id="sltMonth" class="form-control" style="width:60%;">
                                                <option value="">Select</option>
                                                <form:options items="${MonthList}" itemValue="value" itemLabel="label"/>                                        
                                            </form:select>
                                        </div>
                                        <button type="submit" class="btn btn-primary" onclick="javascript: return validate()">Submit</button>
                                    </div>
                                </div>
                            </div>

                            <div class="panel-body">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th width="11%" style="font-family: monospace;font-size: 18px;">Bill No</th>
                                            <th width="13%" style="font-family: monospace;font-size: 18px;">Bill Description</th>
                                            <th width="7%" style="font-family: monospace;font-size: 18px;">Bill Type</th>
                                            <th width="7%" style="font-family: monospace;font-size: 18px;">Token No</th>
                                            <th width="7%" style="font-family: monospace;font-size: 18px;">Token Date</th>
                                            <th width="7%" style="font-family: monospace;font-size: 18px;">Print Bill</th>
                                            <th width="8%" style="font-family: monospace;font-size: 18px;">Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${not empty BillDataList}">
                                            <c:forEach items="${BillDataList}" var="bill">
                                                <tr>
                                                    <td>${bill.billDesc}</td>
                                                    <td>${bill.billGroupDesc}</td>
                                                    <td>${bill.typeOfBill}</td>
                                                    <td>${bill.tokenNo}</td>
                                                    <td>${bill.tokenDate}</td>
                                                    <td><a href="singleBillReportAction.htm?billNo=${bill.billNo}&billType=${bill.typeOfBill}" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-default">Print</a></td>
                                                    <td>
                                                        <c:if test="${bill.billStatusId == '4'}"> Error </c:if>     
                                                        <c:if test="${bill.billStatusId == '5'}"> Token Generated </c:if> 
                                                        <c:if test="${bill.billStatusId == '8'}"> Objection </c:if>    
                                                        <c:if test="${bill.billStatusId == '7'}"> Vouchered </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>    
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </form:form>        
            </div>
            
        <!-- PRINT BILL MODAL -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Bill Print Details</h4>
                    </div>
                    <div class="modal-body"> &nbsp; </div>
                    <div class="modal-footer">
                        <span id="msg"></span>                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>    
    </body>
</html>
