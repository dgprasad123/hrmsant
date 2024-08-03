<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>

        <script type="text/javascript">
            $(window).load(function() {
                // Fill modal with content from link href
                $("#myModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
                $("#submitToIFMS").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
                $('#submitToIFMS').on('hidden.bs.modal', function() {
                    if($("#hidxmlfilecounterror").val() != "Y"){
                        location.reload(); // submitToIFMS
                    }
                });
                $("#billStatusModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
                $("#reprocessModal").on("show.bs.modal", function(e) {
                    if ($('#sltMonth').val() != '') {
                        var link = $(e.relatedTarget);
                        $(this).find(".modal-content").load(link.attr("href"));
                        $('#processDate').datetimepicker({
                            format: 'D-MMM-YYYY'
                        });
                    } else {
                        alert('Please select bill month.');
                        return false;
                    }
                });
                $("#failedTransaction").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
                $("#lockbillModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
                
                if($("#hidxmlfilecounterror").val() == "Y"){
                    showUploadBillModalWithXMLFileError();
                }
            })
            
            function showUploadBillModalWithXMLFileError(){
                $("#submitToIFMS").find(".modal-body").load("submitToIFMS.htm?billNo="+$("#uploaderrorbillno").val()+"&xmlfilecounterror=Y");
                $("#submitToIFMS").modal("toggle");
            }


            function showMonth(me) {

                $.ajax({
                    type: "POST",
                    url: "getBillMonthYearWise.htm?sltYear=" + $(me).val() + "&txtbilltype=" + $('#txtbilltype').val(),
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

            function showMonthDHE(me) {
                //alert('so');
                $.ajax({
                    type: "POST",
                    url: "getBillMonthYearWiseDHE.htm?sltYear=" + $(me).val() + "&txtbilltype=" + $('#txtbilltype').val(),
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



            function lockBill(billId, billMonth, billYear, paytype) {
                /*alert("billId is: "+billId);
                 alert("billMonth is: "+billMonth);
                 alert("billYear is: "+billYear);
                 alert("paytype is: "+paytype);*/
                var r = confirm("Are you sure to lock bill?");
                if (r == true) {
                    location.href = 'lockBill.htm?billNo=' + billId + '&sltMonth=' + billMonth + '&sltYear=' + billYear + '&txtbilltype=' + paytype;
                }
            }

            function verifyDataForBillLock(billId, billMonth, billYear, paytype) {
                //
                var url = 'verifyDataForLockBill.htm?billNo=' + billId;
                var r = confirm("Are you sure to lock bill. ");
                if (r == true) {
                    $.getJSON(url, function(data) {

                        if (data.status != "") {
                            alert(data.status);
                            return false;
                        } else {
                            location.href = 'lockBill.htm?billNo=' + billId + '&sltMonth=' + billMonth + '&sltYear=' + billYear + '&txtbilltype=' + paytype;
                        }
                    });
                } else {
                    return false;
                }

            }


            function validate() {
                var today = new Date();
                var processDt = document.getElementById('txtprocessdt');
                if (processDt.value == '') {
                    alert('Please enter Process Date.');
                    processDt.focus();
                    return false;
                } else {
                    var prdt = new Date(processDt.value);
                    if (prdt > today) {
                        alert("Process Date should not be greater than Today's Date");
                        processDt.focus();
                        processDt.select();
                        return false;
                    }
                }
            }

            function validateMonthYear() {

                if ($('#sltYear').val() == '') {
                    alert('please select Year.');
                    return false;
                }
                if ($('#sltMonth').val() == '') {
                    alert('please select Month.');
                    return false;
                }

                return true;
            }

            function open10ArrearGuide() {
                $('#ArrearUserGuideModal').modal('show');
            }
            function finalizeBill(billno, month, year) {
                if (confirm("Are you sure to Finalise and Lock the Bill?")) {
                    window.location = "veryfyBill.htm?billNo=" + billno + "&aqmonth=" + month + "&aqyear=" + year;
                }
            }
        </script>
        <style>
            @media (min-width: 800px) {
                .modal-dialog {
                    width: 600;
                    margin: 30px auto;
                }
                .modal-content {
                    -webkit-box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                    box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                }
                .modal-sm {
                    width: 300px;
                }
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <form:form class="form-inline" action="getPayBillList.htm" method="POST" commandName="command">
                        <input type="hidden" id="hidxmlfilecounterror" value="${xmlfilecounterror}"/>
                        <input type="hidden" id="uploaderrorbillno" value="${uploaderrorbillno}"/>
                        <div class="row">
                            <%--<h3 style="color: red; text-align: center;">Arrear bills are temporarily unavailable due to some necessary changes.</h3>--%>
                        </div>

                        <div class="row">
                            <div class="col-lg-12">

                                <div class="form-group">
                                    <label for="billType">Bill Type:</label>
                                    <form:select path="txtbilltype" id="txtbilltype" class="form-control">
                                        <form:option value="PAY">Pay Bill</form:option>                                       
                                        <form:option value="OTHER_ARREAR">OTHER ARREAR</form:option>
                                       <form:option value="ARREAR">7th Pay Arrear(40% or 100%)</form:option>
                                        <form:option value="ARREAR_J">7th Pay Arrear(Judiciary)(First 25% or 100%)</form:option>
                                        <form:option value="ARREAR_6">7th Pay Arrear(Percentage)</form:option>
                                        <form:option value="ARREAR_6_J">7th Pay Arrear(Judiciary)(Percentage)</form:option>
                                        <form:option value="ARREAR_NPS">NPS Arrear 5%(10% of rest 50%)</form:option>
                                    </form:select>                
                                </div>

                                <div class="form-group">
                                    <label for="sltYear">Year:</label>
                                    <c:if test="${isDDODHE ne 'Y' && isDDODHE ne 'B' }">
                                        <form:select path="sltYear" id="sltYear" class="form-control" onchange="showMonth(this)">
                                            <option value="">Select</option>
                                            <form:options items="${billYears}" itemValue="value" itemLabel="label"/>                                        
                                        </form:select>
                                    </c:if>
                                    <c:if test="${isDDODHE eq 'Y' || isDDODHE eq 'B'}">
                                        <form:select path="sltYear" id="sltYear" class="form-control" onchange="showMonthDHE(this)">
                                            <option value="">Select</option>
                                            <form:options items="${billYears}" itemValue="value" itemLabel="label"/>                                        
                                        </form:select>
                                    </c:if>
                                </div>
                                <div class="form-group">
                                    <label for="sltMonth">Month:</label>
                                    <form:select path="sltMonth" id="sltMonth" class="form-control" style="width:60%;">
                                        <option value="">Select</option>
                                        <form:options items="${billMonths}" itemValue="value" itemLabel="label"/>                                        
                                    </form:select>
                                </div>
                                <button type="submit" class="btn btn-default" onclick="return validateMonthYear()">Submit</button>

                            </div>
                        </div>
                        <c:if test="${command.txtbilltype == 'ARREAR'}">
                            <div class="row">
                                <div class="col-lg-2">


                                    <div class="form-group">
                                        <label for="sltYear">From Year:</label>
                                        <form:select path="sltFromYear" class="form-control">
                                            <form:option value="2000"> 2000 </form:option>
                                            <form:option value="2001"> 2001 </form:option>
                                            <form:option value="2002"> 2002 </form:option>
                                            <form:option value="2003"> 2003 </form:option>
                                            <form:option value="2004"> 2004 </form:option>
                                            <form:option value="2005"> 2005 </form:option>
                                            <form:option value="2006"> 2006 </form:option>
                                            <form:option value="2007"> 2007 </form:option>
                                            <form:option value="2008"> 2008 </form:option>
                                            <form:option value="2009"> 2009 </form:option>
                                            <form:option value="2010"> 2010 </form:option>
                                            <form:option value="2011"> 2011 </form:option>
                                            <form:option value="2012"> 2012 </form:option>
                                            <form:option value="2013"> 2013 </form:option>
                                            <form:option value="2014"> 2014 </form:option>
                                            <form:option value="2015"> 2015 </form:option>
                                            <form:option value="2016"> 2016 </form:option>
                                            <form:option value="2017"> 2017 </form:option>
                                            <form:option value="2018"> 2018 </form:option>
                                            <form:option value="2019"> 2019 </form:option>
                                            <form:option value="2020"> 2020 </form:option>
                                            <form:option value="2021"> 2021 </form:option>
                                            <form:option value="2022"> 2022 </form:option>
                                            <form:option value="2023"> 2023 </form:option>
                                             <form:option value="2024"> 2024 </form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="col-lg-2">
                                    <div class="form-group">
                                        <label for="sltMonth">From Month:</label>

                                        <form:select path="sltFromMonth" id="sltFromMonth" class="form-control">
                                            <form:option value="1">JANUARY</form:option>
                                            <form:option value="2">FEBRUARY</form:option>
                                            <form:option value="3">MARCH</form:option>
                                            <form:option value="4">APRIL</form:option>
                                            <form:option value="5">MAY</form:option>
                                            <form:option value="6">JUNE</form:option>
                                            <form:option value="7">JULY</form:option>
                                            <form:option value="8">AUGUST</form:option>
                                            <form:option value="9">SEPTEMBER</form:option>
                                            <form:option value="10">OCTOBER</form:option>
                                            <form:option value="11">NOVEMBER</form:option>
                                            <form:option value="12">DECEMBER</form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="clearfix"> </div>                     
                            <div class="clearfix"> </div>                
                            <div class="row">
                                <div class="col-lg-2">
                                    <div class="form-group">
                                        <label for="sltYear">To Year:</label>
                                        <form:select path="sltToYear" id="sltToYear" class="form-control">
                                            <form:option value="2000"> 2000 </form:option>
                                            <form:option value="2001"> 2001 </form:option>
                                            <form:option value="2002"> 2002 </form:option>
                                            <form:option value="2003"> 2003 </form:option>
                                            <form:option value="2004"> 2004 </form:option>
                                            <form:option value="2005"> 2005 </form:option>
                                            <form:option value="2006"> 2006 </form:option>
                                            <form:option value="2007"> 2007 </form:option>
                                            <form:option value="2008"> 2008 </form:option>
                                            <form:option value="2009"> 2009 </form:option>
                                            <form:option value="2010"> 2010 </form:option>
                                            <form:option value="2011"> 2011 </form:option>
                                            <form:option value="2012"> 2012 </form:option>
                                            <form:option value="2013"> 2013 </form:option>
                                            <form:option value="2014"> 2014 </form:option>
                                            <form:option value="2015"> 2015 </form:option>
                                            <form:option value="2016"> 2016 </form:option>
                                            <form:option value="2017"> 2017 </form:option>
                                            <form:option value="2018"> 2018 </form:option>
                                            <form:option value="2019"> 2019 </form:option>
                                            <form:option value="2020"> 2020 </form:option>
                                            <form:option value="2021"> 2021 </form:option>
                                            <form:option value="2022"> 2022 </form:option>
                                            <form:option value="2023"> 2023 </form:option>
                                             <form:option value="2024"> 2024 </form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="col-lg-2">
                                    <div class="form-group">
                                        <label for="sltMonth">To Month:</label>
                                        <form:select path="sltToMonth" id="sltToMonth" class="form-control">
                                            <form:option value="1">JANUARY</form:option>
                                            <form:option value="2">FEBRUARY</form:option>
                                            <form:option value="3">MARCH</form:option>
                                            <form:option value="4">APRIL</form:option>
                                            <form:option value="5">MAY</form:option>
                                            <form:option value="6">JUNE</form:option>
                                            <form:option value="7">JULY</form:option>
                                            <form:option value="8">AUGUST</form:option>
                                            <form:option value="9">SEPTEMBER</form:option>
                                            <form:option value="10">OCTOBER</form:option>
                                            <form:option value="11">NOVEMBER</form:option>
                                            <form:option value="12">DECEMBER</form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </form:form> 
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="10%">Bill No</th>
                                    <th width="10%">Bill Description</th>
                                    <th width="5%">Bill Type</th>
                                    <th width="10%">Acquaintance Roll</th>
                                    <th width="5%">Print Bill</th>
                                    <th width="5%">Edit</th>
                                    <th width="5%">Esign PDFs</th>
                                    <th width="10%">Submit To i-OTMS</th>
                                    <th width="10%">No of Beneficiary</th>
                                    <th width="10%">Failed<br />Transaction</th>
                                    <th width="5%">Status</th>
                                    <th width="5%">Bill History</th>
                                    <th width="10%">Lock Bill</th>
                                        <c:if test="${is_DDODHE ne 'Y' && is_DDODHE eq 'B' }">
                                        <th width="10%">Bill Verified</th>
                                        </c:if>
                                    <th width="10%">Manage PVT Deduction</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${billList}" var="bill">
                                    <c:if test="${bill.selectedBilltype eq 'PAY'}">
                                        <tr>
                                            <td>${bill.billdesc}</td>
                                            <td>${bill.billGroupDesc}</td>
                                            <td>
                                                <c:if test="${command.txtbilltype == 'PAY'}">
                                                    ${bill.billtype}
                                                </c:if>
                                                <c:if test="${command.txtbilltype ne 'PAY'}">
                                                    ARREAR <c:if test="${bill.percentageArraer gt 0}"> ${bill.percentageArraer} %</c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${bill.isbillPrepared == 'Y'}">
                                                    <c:if test="${not empty bill.billno}">
                                                        <c:if test="${command.txtbilltype == 'PAY'}">

                                                            <a href="browseAquitance.htm?billNo=${bill.billno}">Browse Aquitance</a>
                                                        </c:if>
                                                        <c:if test="${command.txtbilltype ne 'PAY'  && command.txtbilltype ne 'PAYADV' }">
                                                            <c:if test="${bill.lockBill lt 2 or bill.lockBill eq 4  or bill.lockBill eq 6 or bill.lockBill eq 8 }">
                                                                <a href="browseAquitanceArr.htm?billNo=${bill.billno}">Browse Arrear</a>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${bill.isbillPrepared == 'Y'}">
                                                    <c:if test="${not empty bill.billno}">
                                                        <a href="payBillReportAction.htm?billNo=${bill.billno}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-default">Print</a>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${not empty bill.billno}">
                                                    <c:if test="${bill.isbillPrepared == 'Y'}">
                                                        <c:if test="${command.txtbilltype == 'PAY'}">
                                                            <c:if test="${not empty bill.billno}">
                                                                <a href="editBill.htm?billNo=${bill.billno}">Edit</a>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${command.txtbilltype ne 'PAY'   && command.txtbilltype ne 'PAYADV' }">
                                                            <a href="editBillArrear.htm?billNo=${bill.billno}">Edit Arrear Bill</a>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <!--   <c:if test="${allowEsign eq 'Y' && ddohrmsid }">
                                                       <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false"  title="View Pdf Files"  class="btn btn-danger">View Pdf Files</a>
                                                </c:if>-->

                                                <c:if test="${bill.checkESignStatus eq 'N'}">
                                                    <c:if test="${bill.lockBill ge 2}">
                                                        <c:if test="${command.txtbilltype ne 'PAY'}">
                                                            <a href="viewArrearPDFBillDetails.htm?billNo=${bill.billno}&frontpageslno=${bill.frontpageslno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                        </c:if>
                                                        <c:if test="${command.txtbilltype eq 'PAY'}">
                                                            <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>  

                                                <c:if test="${bill.checkESignStatus eq 'Y'}">
                                                    <c:if test="${bill.lockBill ge 2}">
                                                        <c:if test="${command.txtbilltype ne 'PAY'}">
                                                            <a href="viewArrearPDFBillDetails.htm?billNo=${bill.billno}&frontpageslno=${bill.frontpageslno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                        </c:if>
                                                        <c:if test="${command.txtbilltype eq 'PAY'}">
                                                            <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false" title="View Pdf Files" class="btn btn-success">View Pdf Files For Dsign</a>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${bill.onlinebillapproved == 'Y'}">
                                                    <c:if test="${not empty bill.billno}">
                                                        <c:if test="${bill.showLink == 'Y'}">
                                                            <c:if test="${bill.isbillPrepared == 'Y'}">
                                                                <%--<c:if test="${bill.percentageArraer ne '30'}">--%>
                                                                <a href="submitToIFMS.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#submitToIFMS" class="btn btn-default">Submit to IFMS</a>
                                                                <%--</c:if>--%>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${bill.showLink == 'N'}">
                                                            <c:if test="${bill.lockBill == '5'}">
                                                                Token Generated
                                                            </c:if>
                                                            <c:if test="${bill.lockBill == '7'}">
                                                                Vouchered
                                                            </c:if>
                                                            <c:if test="${bill.lockBill == '3'}">
                                                                SUBMITTED
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                ${bill.nooofEmployee}
                                            </td>
                                            <td>
                                                <c:if test="${bill.ftcount ne 0}">
                                                    <a href="getFailedTransactionData.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Failed Transaction" data-target="#failedTransaction" class="btn btn-default">View</a>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${bill.isbillPrepared == 'Y'}">
                                                    <c:if test="${bill.lockBill == '4'}">
                                                        <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Error</a>
                                                    </c:if>
                                                    <c:if test="${bill.lockBill == '8'}">
                                                        <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Objected</a>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${empty bill.billno}">
                                                    <c:if test="${command.txtbilltype == 'PAY'}">
                                                        <a href="processIndividualBill.htm?billNo=${bill.billno}&billgroupId=${bill.billgroupId}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=PAY" data-remote="false" data-toggle="modal" data-target="#reprocessModal" class="btn btn-default">Process Bill</a>
                                                    </c:if>
                                                    <c:if test="${command.txtbilltype ne 'PAY'  && command.txtbilltype ne 'PAYADV' }">
                                                        <a href="processArrearIndividualBill.htm?billNo=${bill.billno}&billgroupId=${bill.billgroupId}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" data-target="#reprocessModal" class="btn btn-default">Process Arrear Bill</a>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${bill.isbillPrepared == 'N'}">
                                                    <c:if test="${not empty bill.billno}">
                                                        Under Process
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${not empty bill.billno}">
                                                    <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Status</a>
                                                </c:if>   
                                            </td>
                                            <td>
                                                <c:if test="${collegeUnderDhe eq 'B'}">                                                
                                                </c:if>
                                                <c:if test="${collegeUnderDhe ne 'B'}">
                                                    <c:if test="${bill.isbillPrepared == 'Y'}">
                                                        <c:if test="${bill.lockBill lt 2}">
                                                            <c:if test="${not empty bill.billno}">
                                                                <%--<a href="#" onclick="return verifyDataForBillLock(${bill.billno}, ${command.sltMonth}, ${command.sltYear}, '${command.txtbilltype}')">Lock Your Bill</a>--%>
                                                                <a href="verifyDataForLockBill.htm?billNo=${bill.billno}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" title="Lock Bill" data-target="#lockbillModal" class="btn btn-default">Lock Your Bill</a>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${bill.lockBill gt 2 && bill.lockBill!=3 && bill.lockBill!=5 && bill.lockBill!=7}">
                                                            Bill Unlocked
                                                        </c:if>
                                                        <c:if test="${bill.lockBill eq 2 || ( bill.lockBill==3 && bill.lockBill==5 && bill.lockBill==7)}">
                                                            Locked
                                                        </c:if>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <c:if test="${is_DDODHE ne 'Y' && is_DDODHE eq 'B' }">
                                                <td>
                                                    <c:if test="${bill.isbillVerified eq 'N' || bill.isbillVerified eq 'Y' }">
                                                        <c:if test="${not empty bill.billno}">
                                                            <c:if test="${command.txtbilltype == 'PAY'}">
                                                                <c:if test="${bill.isbillVerified eq 'Y'}">
                                                                    Verified
                                                                </c:if>
                                                                <c:if test="${bill.isbillVerified eq 'N'}">
                                                                    <a href="javascript:finalizeBill(${bill.billno},${command.sltMonth},${command.sltYear})">Not Verified</a>
                                                                </c:if>

                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                            </c:if>
                                            <td>
                                                <c:if test="${command.txtbilltype == 'PAY'}">
                                                    <c:if test="${not empty bill.billno}">
                                                        <c:if test="${bill.lockBill eq 0 || bill.lockBill eq 1 || bill.lockBill eq 2 || bill.lockBill eq 4 || bill.lockBill eq 8}">
                                                            <c:if test="${bill.isbillPrepared == 'Y'}">
                                                                <a href="GetPvtDeductionData.htm?billNo=${bill.billno}">Manage PVT Deduction(DDO to DDO)</a>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${bill.selectedBilltype ne 'PAY'}">
                                        <c:if test="${bill.noteligible gt 0}" > 
                                            <tr style="color:red;">  
                                                <td>${bill.billdesc}</td>
                                                <td>${bill.billGroupDesc}</td>
                                                <td>
                                                    <c:if test="${command.txtbilltype == 'PAY'}">
                                                        ${bill.billtype}
                                                    </c:if>
                                                    <c:if test="${command.txtbilltype ne 'PAY'}">
                                                        ARREAR <c:if test="${bill.percentageArraer gt 0}"> ${bill.percentageArraer} %</c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${bill.isbillPrepared == 'Y'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            <c:if test="${command.txtbilltype == 'PAY'}">

                                                                <a href="browseAquitance.htm?billNo=${bill.billno}">Browse Aquitance</a>
                                                            </c:if>
                                                            <c:if test="${command.txtbilltype ne 'PAY'  && command.txtbilltype ne 'PAYADV' }">
                                                                <c:if test="${bill.lockBill lt 2 or bill.lockBill eq 4  or bill.lockBill eq 6 or bill.lockBill eq 8 }">
                                                                    <a href="browseAquitanceArr.htm?billNo=${bill.billno}">Browse Arrear</a>
                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${bill.isbillPrepared == 'Y'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            <a href="payBillReportAction.htm?billNo=${bill.billno}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-default">Print</a>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty bill.billno}">
                                                        <c:if test="${bill.isbillPrepared == 'Y'}">
                                                            <c:if test="${command.txtbilltype == 'PAY'}">
                                                                <c:if test="${not empty bill.billno}">
                                                                    <a href="editBill.htm?billNo=${bill.billno}">Edit</a>
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${command.txtbilltype ne 'PAY'   && command.txtbilltype ne 'PAYADV' }">
                                                                <a href="editBillArrear.htm?billNo=${bill.billno}">Edit Arrear Bill</a>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <!--   <c:if test="${allowEsign eq 'Y' && ddohrmsid }">
                                                           <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false"  title="View Pdf Files"  class="btn btn-danger">View Pdf Files</a>
                                                    </c:if>-->

                                                    <c:if test="${bill.checkESignStatus eq 'N'}">
                                                        <c:if test="${bill.lockBill ge 2}">
                                                            <c:if test="${command.txtbilltype ne 'PAY'}">
                                                                <a href="viewArrearPDFBillDetails.htm?billNo=${bill.billno}&frontpageslno=${bill.frontpageslno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                            </c:if>
                                                            <c:if test="${command.txtbilltype eq 'PAY'}">
                                                                <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>  

                                                    <c:if test="${bill.checkESignStatus eq 'Y'}">
                                                        <c:if test="${bill.lockBill ge 2}">
                                                            <c:if test="${command.txtbilltype ne 'PAY'}">
                                                                <a href="viewArrearPDFBillDetails.htm?billNo=${bill.billno}&frontpageslno=${bill.frontpageslno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                            </c:if>
                                                            <c:if test="${command.txtbilltype eq 'PAY'}">
                                                                <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false" title="View Pdf Files" class="btn btn-success">View Pdf Files For Dsign</a>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${bill.onlinebillapproved == 'Y'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            <c:if test="${bill.showLink == 'Y'}">
                                                                <c:if test="${bill.isbillPrepared == 'Y'}">
                                                                    <%--<c:if test="${bill.percentageArraer ne '30'}">--%>
                                                                    <a href="submitToIFMS.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#submitToIFMS" class="btn btn-default">Submit to IFMS</a>
                                                                    <%--</c:if>--%>
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${bill.showLink == 'N'}">
                                                                <c:if test="${bill.lockBill == '5'}">
                                                                    Token Generated
                                                                </c:if>
                                                                <c:if test="${bill.lockBill == '7'}">
                                                                    Vouchered
                                                                </c:if>
                                                                <c:if test="${bill.lockBill == '3'}">
                                                                    SUBMITTED
                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    ${bill.nooofEmployee}
                                                </td>
                                                <td>
                                                    <c:if test="${bill.ftcount ne 0}">
                                                        <a href="getFailedTransactionData.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Failed Transaction" data-target="#failedTransaction" class="btn btn-default">View</a>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${bill.isbillPrepared == 'Y'}">
                                                        <c:if test="${bill.lockBill == '4'}">
                                                            <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Error</a>
                                                        </c:if>
                                                        <c:if test="${bill.lockBill == '8'}">
                                                            <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Objected</a>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${empty bill.billno}">
                                                        <c:if test="${command.txtbilltype == 'PAY'}">
                                                            <a href="processIndividualBill.htm?billNo=${bill.billno}&billgroupId=${bill.billgroupId}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=PAY" data-remote="false" data-toggle="modal" data-target="#reprocessModal" class="btn btn-default">Process Bill</a>
                                                        </c:if>
                                                        <c:if test="${command.txtbilltype ne 'PAY'  && command.txtbilltype ne 'PAYADV' }">
                                                        <%--    <a href="processArrearIndividualBill.htm?billNo=${bill.billno}&billgroupId=${bill.billgroupId}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" data-target="#reprocessModal" class="btn btn-default">Process Arrear Bill</a>--%>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${bill.isbillPrepared == 'N'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            Under Process
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty bill.billno}">
                                                        <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Status</a>
                                                    </c:if>   
                                                </td>
                                                <td>
                                                    <c:if test="${collegeUnderDhe eq 'B'}">                                                
                                                    </c:if>
                                                    <c:if test="${collegeUnderDhe ne 'B'}">
                                                        <c:if test="${bill.isbillPrepared == 'Y'}">
                                                            <c:if test="${bill.lockBill lt 2}">
                                                                <c:if test="${not empty bill.billno}">
                                                                    <%--<a href="#" onclick="return verifyDataForBillLock(${bill.billno}, ${command.sltMonth}, ${command.sltYear}, '${command.txtbilltype}')">Lock Your Bill</a>--%>
                                                                    <%--<a href="verifyDataForLockBill.htm?billNo=${bill.billno}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" title="Lock Bill" data-target="#lockbillModal" class="btn btn-default">Lock Your Bill</a>--%>
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${bill.lockBill gt 2 && bill.lockBill!=3 && bill.lockBill!=5 && bill.lockBill!=7}">
                                                                Bill Unlocked
                                                            </c:if>
                                                            <c:if test="${bill.lockBill eq 2 || ( bill.lockBill==3 && bill.lockBill==5 && bill.lockBill==7)}">
                                                                Locked
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <c:if test="${is_DDODHE ne 'Y' && is_DDODHE eq 'B' }">
                                                    <td>
                                                        <c:if test="${bill.isbillVerified eq 'N' || bill.isbillVerified eq 'Y' }">
                                                            <c:if test="${not empty bill.billno}">
                                                                <c:if test="${command.txtbilltype == 'PAY'}">
                                                                    <c:if test="${bill.isbillVerified eq 'Y'}">
                                                                        Verified
                                                                    </c:if>
                                                                    <c:if test="${bill.isbillVerified eq 'N'}">
                                                                        <a href="javascript:finalizeBill(${bill.billno},${command.sltMonth},${command.sltYear})">Not Verified</a>
                                                                    </c:if>

                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </td>
                                                </c:if>
                                                <td>
                                                    <c:if test="${command.txtbilltype == 'PAY'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            <c:if test="${bill.lockBill eq 0 || bill.lockBill eq 1 || bill.lockBill eq 2 || bill.lockBill eq 4 || bill.lockBill eq 8}">
                                                                <c:if test="${bill.isbillPrepared == 'Y'}">
                                                                    <a href="GetPvtDeductionData.htm?billNo=${bill.billno}">Manage PVT Deduction(DDO to DDO)</a>
                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:if>
                                        <c:if test="${bill.noteligible eq null}" > 
                                            <tr>  
                                                <td>${bill.billdesc}</td>
                                                <td>${bill.billGroupDesc}</td>
                                                <td>
                                                    <c:if test="${command.txtbilltype == 'PAY'}">
                                                        ${bill.billtype}
                                                    </c:if>
                                                    <c:if test="${command.txtbilltype ne 'PAY'}">
                                                        ARREAR <c:if test="${bill.percentageArraer gt 0}"> ${bill.percentageArraer} %</c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${bill.isbillPrepared == 'Y'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            <c:if test="${command.txtbilltype == 'PAY'}">

                                                                <a href="browseAquitance.htm?billNo=${bill.billno}">Browse Aquitance</a>
                                                            </c:if>
                                                            <c:if test="${command.txtbilltype ne 'PAY'  && command.txtbilltype ne 'PAYADV' }">
                                                                <c:if test="${bill.lockBill lt 2 or bill.lockBill eq 4  or bill.lockBill eq 6 or bill.lockBill eq 8 }">
                                                                    <a href="browseAquitanceArr.htm?billNo=${bill.billno}">Browse Arrear</a>
                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${bill.isbillPrepared == 'Y'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            <a href="payBillReportAction.htm?billNo=${bill.billno}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-default">Print</a>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty bill.billno}">
                                                        <c:if test="${bill.isbillPrepared == 'Y'}">
                                                            <c:if test="${command.txtbilltype == 'PAY'}">
                                                                <c:if test="${not empty bill.billno}">
                                                                    <a href="editBill.htm?billNo=${bill.billno}">Edit</a>
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${command.txtbilltype ne 'PAY'   && command.txtbilltype ne 'PAYADV' }">
                                                                <a href="editBillArrear.htm?billNo=${bill.billno}">Edit Arrear Bill</a>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <!--   <c:if test="${allowEsign eq 'Y' && ddohrmsid }">
                                                           <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false"  title="View Pdf Files"  class="btn btn-danger">View Pdf Files</a>
                                                    </c:if>-->

                                                    <c:if test="${bill.checkESignStatus eq 'N'}">
                                                        <c:if test="${bill.lockBill ge 2}">
                                                            <c:if test="${command.txtbilltype ne 'PAY'}">
                                                                <a href="viewArrearPDFBillDetails.htm?billNo=${bill.billno}&frontpageslno=${bill.frontpageslno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                            </c:if>
                                                            <c:if test="${command.txtbilltype eq 'PAY'}">
                                                                <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>  

                                                    <c:if test="${bill.checkESignStatus eq 'Y'}">
                                                        <c:if test="${bill.lockBill ge 2}">
                                                            <c:if test="${command.txtbilltype ne 'PAY'}">
                                                                <a href="viewArrearPDFBillDetails.htm?billNo=${bill.billno}&frontpageslno=${bill.frontpageslno}" data-remote="false" title="View Pdf Files" class="btn btn-danger">View Pdf Files For Dsign</a>
                                                            </c:if>
                                                            <c:if test="${command.txtbilltype eq 'PAY'}">
                                                                <a href="viewPDFBillDetails.htm?billNo=${bill.billno}" data-remote="false" title="View Pdf Files" class="btn btn-success">View Pdf Files For Dsign</a>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${bill.onlinebillapproved == 'Y'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            <c:if test="${bill.showLink == 'Y'}">
                                                                <c:if test="${bill.isbillPrepared == 'Y'}">
                                                                    <%--<c:if test="${bill.percentageArraer ne '30'}">--%>
                                                                    <a href="submitToIFMS.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#submitToIFMS" class="btn btn-default">Submit to IFMS</a>
                                                                    <%--</c:if>--%>
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${bill.showLink == 'N'}">
                                                                <c:if test="${bill.lockBill == '5'}">
                                                                    Token Generated
                                                                </c:if>
                                                                <c:if test="${bill.lockBill == '7'}">
                                                                    Vouchered
                                                                </c:if>
                                                                <c:if test="${bill.lockBill == '3'}">
                                                                    SUBMITTED
                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    ${bill.nooofEmployee}
                                                </td>
                                                <td>
                                                    <c:if test="${bill.ftcount ne 0}">
                                                        <a href="getFailedTransactionData.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Failed Transaction" data-target="#failedTransaction" class="btn btn-default">View</a>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${bill.isbillPrepared == 'Y'}">
                                                        <c:if test="${bill.lockBill == '4'}">
                                                            <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Error</a>
                                                        </c:if>
                                                        <c:if test="${bill.lockBill == '8'}">
                                                            <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Objected</a>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${empty bill.billno}">
                                                        <c:if test="${command.txtbilltype == 'PAY'}">
                                                            <a href="processIndividualBill.htm?billNo=${bill.billno}&billgroupId=${bill.billgroupId}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=PAY" data-remote="false" data-toggle="modal" data-target="#reprocessModal" class="btn btn-default">Process Bill</a>
                                                        </c:if>
                                                        <c:if test="${command.txtbilltype ne 'PAY'  && command.txtbilltype ne 'PAYADV' }">
                                                            <a href="processArrearIndividualBill.htm?billNo=${bill.billno}&billgroupId=${bill.billgroupId}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" data-target="#reprocessModal" class="btn btn-default">Process Arrear Bill</a>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${bill.isbillPrepared == 'N'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            Under Process
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty bill.billno}">
                                                        <a href="showUploadBillStatus.htm?billNo=${bill.billno}" data-remote="false" data-toggle="modal" title="View Status" data-target="#billStatusModal" class="btn btn-default">Status</a>
                                                    </c:if>   
                                                </td>
                                                <td>
                                                    <c:if test="${collegeUnderDhe eq 'B'}">                                                
                                                    </c:if>
                                                    <c:if test="${collegeUnderDhe ne 'B'}">
                                                        <c:if test="${bill.isbillPrepared == 'Y'}">
                                                            <c:if test="${bill.lockBill lt 2}">
                                                                <c:if test="${not empty bill.billno}">
                                                                    <%--<a href="#" onclick="return verifyDataForBillLock(${bill.billno}, ${command.sltMonth}, ${command.sltYear}, '${command.txtbilltype}')">Lock Your Bill</a>--%>
                                                                    <a href="verifyDataForLockBill.htm?billNo=${bill.billno}&sltMonth=${command.sltMonth}&sltYear=${command.sltYear}&txtbilltype=${command.txtbilltype}" data-remote="false" data-toggle="modal" title="Lock Bill" data-target="#lockbillModal" class="btn btn-default">Lock Your Bill</a>
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${bill.lockBill gt 2 && bill.lockBill!=3 && bill.lockBill!=5 && bill.lockBill!=7}">
                                                                Bill Unlocked
                                                            </c:if>
                                                            <c:if test="${bill.lockBill eq 2 || ( bill.lockBill==3 && bill.lockBill==5 && bill.lockBill==7)}">
                                                                Locked
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <c:if test="${is_DDODHE ne 'Y' && is_DDODHE eq 'B' }">
                                                    <td>
                                                        <c:if test="${bill.isbillVerified eq 'N' || bill.isbillVerified eq 'Y' }">
                                                            <c:if test="${not empty bill.billno}">
                                                                <c:if test="${command.txtbilltype == 'PAY'}">
                                                                    <c:if test="${bill.isbillVerified eq 'Y'}">
                                                                        Verified
                                                                    </c:if>
                                                                    <c:if test="${bill.isbillVerified eq 'N'}">
                                                                        <a href="javascript:finalizeBill(${bill.billno},${command.sltMonth},${command.sltYear})">Not Verified</a>
                                                                    </c:if>

                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </td>
                                                </c:if>
                                                <td>
                                                    <c:if test="${command.txtbilltype == 'PAY'}">
                                                        <c:if test="${not empty bill.billno}">
                                                            <c:if test="${bill.lockBill eq 0 || bill.lockBill eq 1 || bill.lockBill eq 2 || bill.lockBill eq 4 || bill.lockBill eq 8}">
                                                                <c:if test="${bill.isbillPrepared == 'Y'}">
                                                                    <a href="GetPvtDeductionData.htm?billNo=${bill.billno}">Manage PVT Deduction(DDO to DDO)</a>
                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:if>                                       
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-sm-2">
                                <form:form action="newBill.htm" method="post">
                                    <button type="submit" class="btn btn-primary">New Bill</button>
                                </form:form>
                            </div>
                            <%--
                            <div class="col-sm-1">
                                <form:form action="newleftOutBill.htm" method="post">
                                    <button type="submit" class="btn btn-default">New Arrear Bill</button>
                                </form:form>
                            </div>
                            --%>
                            <div class="col-sm-2">
                                <form:form action="newArrearBill.htm" method="post">
                                    <button type="submit" class="btn btn-default">New Arrear Bill</button>
                                </form:form>
                            </div>
                            <c:if test="${hideExtraDutyProcess eq 'N'}">
                                <div class="col-sm-1">

                                </div>
                            </c:if>
                            <div class="col-sm-2">
                                <a href ="Arrear25Verification.htm" class="btn btn-info" target="_blank"> Check 25% Arrear </a>
                            </div>
                            <div class="col-sm-2">
                                <a href ="Arrear40Verification.htm" class="btn btn-warning" target="_blank"> Check 40% Arrear </a>
                            </div>
                            <div class="col-sm-2">
                                <a href ="javascript:void(0);" class="btn btn-default" onclick="open10ArrearGuide();"> 10% Arrear User Guide</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <c:if test="${accessBills eq false}">
                    <h3 style="color: red; text-align: center;">You are not authorized to view the Bills Or You do not have Sufficient Privilege to view Bills.  </h3>
                </c:if>
            </div>




            <!-- Submit to IFMS Verification Modal -->
            <div id="submitToIFMS" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:1000px;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Bill Upload</h4>
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

            <!-- Bill Status Modal -->
            <div id="billStatusModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:1000px;">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Bill Status</h4>
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

            <!-- Print Bill Modal -->
            <div id="myModal" class="modal fade" role="dialog">billYears
                <div class="modal-dialog" style="width:1000px;">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Bill Print Details</h4>
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
            <!-- Reprocess Bill Modal -->
            <div id="reprocessModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:1000px;">

                    <!-- Modal content-->

                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Reprocess Bill</h4>
                        </div>
                        <div class="modal-body">

                        </div>
                        <div class="modal-footer">
                            <span id="msg"></span>
                            <button type="submit" class="btn btn-default" name="Process">Process</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>
            <div id="ArrearUserGuideModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:1000px;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h3 class="modal-title">User Guide for 10% Arrear</h3>
                        </div>
                        <div class="modal-body">
                            <table class="table">
                                <tr>
                                    <td>
                                        1) 40% Arrear should be prepared in HRMS. 
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        2) If multiple 40% Arrear is prepared for an employee then check which 40% is correct, rest should be deleted.
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        3) Click "Check 40% Arrear" Button where you can find out the no of bills prepared of an employee.
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        4) If no bills found of that employee then you have to prepare 40% in HRMS. 
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        5) If multiple bills found correct of that employee then 10% will automatically extract the amount from 40% arrear bill.
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        6) You can adjust 40% revised in HRMS by creating another 40% arrear manually in HRMS. If one 40% is previously prepared and voucher.
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        7) No 40% bills should be submitted to treasury as bill has already been passed previously by manually or through HRMS.
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        After following the above instruction 10% arrear bill of employee should be prepared. There should be no correction provision in 10 % arrear.
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="failedTransaction" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:1300px;">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Failed Transactions</h4>
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
            <div id="lockbillModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:1000px;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Lock Bill Status</h4>
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
