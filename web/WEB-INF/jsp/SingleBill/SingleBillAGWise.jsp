<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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

            $(window).load(function() {
                $("#myModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
            })

            function getMajorHeadList() {
                $('#majorhead').empty();
                var url = "getMajorHeadListTreasuryWise.htm?trcode="+$('#treasury').val()+"&aqyear="+$('#sltYear').val()+"&aqmonth="+$('#sltMonth').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                       $('#majorhead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function validateForm() {
                if ($("#treasury").val() == ""){
                    alert("Please Select Treasury");
                    $("#treasury").focus();
                    return false;
                }
                if ($("#sltYear").val() == ""){
                    alert("Please Select Year");
                    $("#sltYear").focus();
                    return false;
                }
                if ($("#sltMonth").val() == ""){
                    alert("Please Select Month");
                    $("#sltMonth").focus();
                    return false;
                }
                if ($("#majorhead").val() == ""){
                    alert("Please Select Major Head");
                    $("#majorhead").focus();
                    return false;
                }
               return true; 
            }
        </script>    
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/> 
            <div id="page-wrapper">
                <form:form class="form-inline" action="getAGWiseBillDetails.htm" commandName="singleFormBean">
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
                                        <div class="form-group" style="width:20%;">
                                            <label for="treasury">  Treasury :</label>
                                            <form:select path="treasury" id="treasury" class="form-control">
                                                <form:option value="">--Select--</form:option>
                                                <form:options items="${agwisetrlist}" itemValue="value" itemLabel="label"/>
                                            </form:select>               
                                        </div>
                                        <div class="form-group" style="width:20%;">
                                            <label for="sltYear">&nbsp;Year:</label>
                                            <form:select path="sltYear" id="sltYear" class="form-control">
                                                <form:option value="">--Select--</form:option>
                                                <form:options items="${yearlist}" itemValue="value" itemLabel="label"/>                                        
                                            </form:select>
                                        </div>
                                        <div class="form-group" style="width:20%;">
                                            <label for="sltMonth">&nbsp;Month:</label>
                                            <form:select path="sltMonth" id="sltMonth" class="form-control" style="width:60%;" onchange="getMajorHeadList();">
                                                <form:option value="">--Select--</form:option>
                                                <form:option value="0">JAN</form:option>
                                                <form:option value="1">FEB</form:option>
                                                <form:option value="2">MAR</form:option>
                                                <form:option value="3">APR</form:option>
                                                <form:option value="4">MAY</form:option>
                                                <form:option value="5">JUN</form:option>
                                                <form:option value="6">JUL</form:option>
                                                <form:option value="7">AUG</form:option>
                                                <form:option value="8">SEP</form:option>
                                                <form:option value="9">OCT</form:option>
                                                <form:option value="10">NOV</form:option>
                                                <form:option value="11">DEC</form:option>
                                            </form:select>
                                        </div>
                                        <div class="form-group" style="width:30%;">
                                            <label for="majorhead">  Major Head :</label>
                                            <form:select path="majorhead" id="majorhead" class="form-control" style="width:70%;">
                                                <form:option value="">--Select--</form:option>
                                                <form:options items="${majorheadlist}" itemValue="value" itemLabel="label"/>
                                            </form:select>                
                                        </div>
                                        <input type="submit" name="btnSubmit" value="Submit" class="btn btn-primary" onclick="return validateForm()"/>
                                    </div>
                                </div>
                            </div>

                            <div class="panel-body">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th width="10%" style="font-family: monospace;font-size: 18px;">Bill No</th>
                                            <th width="20%" style="font-family: monospace;font-size: 18px;">Bill Description</th>
                                            <th width="10%" style="font-family: monospace;font-size: 18px;">Bill Type</th>
                                            <th width="20%" style="font-family: monospace;font-size: 18px;">Print Bill</th>
                                            <th width="20%" style="font-family: monospace;font-size: 18px;">Voucher Number</th>
                                            <th width="20%" style="font-family: monospace;font-size: 18px;">Voucher Date</th>
                                    
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${not empty BillDataList}">
                                            <c:forEach items="${BillDataList}" var="bill">
                                                <tr>
                                                    <td>${bill.billDesc}</td>
                                                    <td>${bill.billGroupDesc}</td>
                                                    <td>${bill.typeOfBill}</td>
                                                    <td>
                                                        <a href="singleBillReportAction.htm?billNo=${bill.billNo}&billType=${bill.typeOfBill}" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-default">Print</a>
                                                    </td>
                                                    <td>
                                                        ${bill.voucherNo}
                                                    </td>
                                                    <td>
                                                        ${bill.voucherDate}
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
