<%-- 
    Document   : BillGroupWiseBillVerificationReport
    Created on : Jun 19, 2023, 2:00:16 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>       

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery.freezeheader.js"></script>
        <script src="js/bootstrap.min.js"></script>


        <script type="text/javascript">
            $(document).ready(function() {
                $("#btnUnderprocess1").hide();
                $("#btnprocess").show();

                $("#dataTab").freezeHeader();

                /*$("#btnExport2Tabl").click(function() {
                 var x = $("#dttabl").clone();
                 $("#dttabl").find("tr td a").replaceWith(function() {
                 return $.text([this]);
                 });
                 
                 tableToExcel("dttabl", "BillGroupWiseReport", "BillGroupWiseReport.xls");
                 $("#dttabl").html(x);
                 });*/

            });


            function viewPrivStatus(officeCode) {
                var finTrData = "";
                //alert(officeCode);
                $('#empList').empty();
                var url = 'viewprivilegeEmpJSON.htm?offcode=' + officeCode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        finTrData = finTrData + "<tr>" +
                                "<td>" + (i + 1) + "</td>" +
                                "<td>" + obj.aqmastEmpNm + "</td>" +
                                "<td>" + obj.aqmastMobile + "</td>" +
                                "</tr>";
                    });
                    if (finTrData == "") {
                        alert("Privilege Not Assigned");
                    }
                    //$('#empHeading').append("HRMS ID:").append($("#finalEmpId").val());
                    $('#empList').append(finTrData);
                });

            }

            function processAllBills(type, processVal, billmonth, billyear) {
                var billgrpid = $('#hidBillGrpId').val();
                var offcode = processVal;
                //alert(processVal);
                if (confirm("Are you sure to process the bill ? ")) {
                    var url1 = 'checkNewBillGeneratedorNot.htm?billgroupId=' + billgrpid+ '&billmonth=' + billmonth + '&billyear=' + billyear;
                }

                $.ajax({
                    url: url1,
                    dataType: "json",
                    success: function(data1) {
                        if (data1.billno != 'N') {
                            alert(data1.billno);
                            if (type == 'A') {
                                var url = 'processbillsIntoQueue.htm?billgroupId=' + billgrpid + '&billno=' + data1.billno + '&processValue=' + 'All' + '&billmonth=' + billmonth + '&billyear=' + billyear;
                                $.ajax({
                                    url: url,
                                    dataType: "json",
                                    success: function(data) {
                                        if (data.msg == 'Y') {
                                            $("#billmsg").text("Bills Are Under Process.........\n\
                                Check after sometimes.");
                                            //self.location = 'getOfficeBillVerificationStatus.htm';
                                        } /*else if (data.msg == 'N'){
                                         $("#billmsgerror").text("Bills cann't be processed");
                                         }*/
                                    }
                                })
                            } else if (type == 'S') {
                                var url2 = 'processbillsIntoQueue.htm?billgroupId=' + billgrpid + '&billno=' + data1.billno + '&processValue=' + offcode + '&billmonth=' + billmonth + '&billyear=' + billyear;
                                ;
                                $.ajax({
                                    url: url2,
                                    dataType: "json",
                                    success: function(data) {
                                        if (data.msg == 'Y') {
                                            $("#billmsg").text("Bills is Under Process.\n\
                                Reload the Search Button.");
                                            $("#btnUnderprocess1").show();
                                            $("#btnprocess").hide();
                                            //self.location = 'getOfficeBillVerificationStatus.htm';
                                        } /*else if (data.msg == 'N'){
                                         $("#billmsgerror").text("Bills cann't be processed");
                                         }*/
                                    }
                                })
                            }

                        } else {
                            $("#billmsgerror").text("Bill is Locked/Submitted To TReasury/Tokened/Vouchered/New ID not Created.");
                            $("#billmsgerror1").text("Bill Cann't be processed. Check Status In Bill Browser.");

                        }

                    }
                })


            }
            function deleteOfficeBill(billno, offCode) {
                if (confirm("Are you sure to delete this office ?")) {
                    $.ajax({
                        url: 'DeleteOfficeFromBillDHE.htm',
                        data: 'billNo=' + billno + '&offcode=' + offCode,
                        type: 'get',
                        success: function(data) {
                            self.location = 'getOfficeBillVerificationStatus.htm';

                        }
                    });

                }
            }
            function processBill(billNo, officeCode) {
                if (confirm("Are you sure to process/reprocess the bill")) {
                    var url = "saveBill.htm?action=Reprocess&billNo=" + billNo + "&offCode=" + officeCode + "&isDIR=" + 'Y';
                    window.location = url;
                }

            }


        </script>
        <style>
            table{
                border-collapse: collapse;
                border: 1px solid;
            }
            table, th, td {
                border: 1px solid black;
            }
        </style>
    </head>
    <body>

        <form:form role="form" action="getOfficeBillVerificationStatus.htm" commandName="billBean" method="post" >
            <form:hidden id="hidBillGrpId" path="hidBillGrpId"/> 

            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">

                        <h3 style="color:green; text-align: center;"><u>Bill Group Wise Status Report</u></h3> 

                        <br/><br/>
                        <span id="billmsg" style="font-size: 18px; text-align:center;color: #008000;font-weight: bold;"></span>
                        <span id="billmsgerror" style="font-size: 18px; text-align:center;color: #F00000;font-weight: bold;"></span>
                        <br/>
                        <span id="billmsgerror1" style="font-size: 18px; text-align:center;color: #F00000;font-weight: bold;"></span>

                        <div class="row" style="margin-bottom: 7px; ">
                            <div class="col-lg-1">

                            </div>
                            <div class="col-lg-2">
                                <label>Select Bill Group<span style="color: red">*</span></label>
                            </div>

                            <div class="col-lg-2">
                                <form:select path="sltbillgroupId" id="sltbillgroupId"  class="form-control">
                                    <option value="">---Select Bill Group----</option>
                                    <form:options items="${billGrouplist}" itemLabel="billgroupdesc" itemValue="billgroupid"/>
                                </form:select>
                            </div>

                            <div class="col-lg-1">
                                <label >Month:<span style="color: red">*</span></label>
                            </div>

                            <div class="col-lg-2">                               
                                <form:select id="billMonth" path="billMonth" class="form-control">
                                    <form:option value="111" >--Select Month--</form:option>
                                    <form:option value="0">January</form:option>
                                    <form:option value="1">February</form:option>
                                    <form:option value="2">March</form:option>
                                    <form:option value="3">April</form:option>
                                    <form:option value="4">May</form:option>
                                    <form:option value="5">June</form:option>
                                    <form:option value="6">July</form:option>
                                    <form:option value="7">August</form:option>
                                    <form:option value="8">September</form:option>
                                    <form:option value="9">October</form:option>
                                    <form:option value="10">November</form:option>
                                    <form:option value="11">December</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-1">
                                <label>To Date:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">                               
                                <form:select id="billYear" path="billYear" class="form-control">
                                    <form:option value="222">--Select Year--</form:option>
                                    <form:option value="2023">2023</form:option>
                                    <form:option value="2024">2024</form:option>
                                </form:select>
                            </div>

                            <div class="col-lg-1">
                                <input type="submit" name="action" value="Search" class="btn btn-success"/>
                            </div>
                        </div>
                        <div class="col-lg-1">
                            <%-- <input type="button" id="btnExport2Tabl" class="btn btn-primary" value="Export to Excel"/>--%>
                            <c:if test="${allowProcess eq 'Y'}">
                                <input type="button" id="btnExport2Tabl" onclick="processAllBills('A', '${aqgroup}', '${billmonth}', '${billyear}');" class="btn btn-success" style="width:120%" value="Process All"/>
                            </c:if>   
                        </div>
                        <br/><br/>

                        <div class="row">
                            <div class="col-lg-12">

                                <div class="table-responsive" style="overflow:auto;">
                                    <table class="table table-striped" id="dataTab">
                                        <thead>
                                            <tr>
                                                <th style="background-color:lightblue;color: #000000; ">SL NO</th>
                                                <th style="background-color:lightblue;color: #000000; ">OFFICE CODE</th>
                                                <th style="background-color:lightblue;color: #000000; ">OFFICE NAME</th>
                                                <th style="background-color:lightblue;color: #000000; ">DISTRICT NAME</th>
                                                <th style="background-color:lightblue;color: #000000; text-align:center; ">NO. OF EMPLOYEES</th>
                                                <th style="background-color:lightblue;color: #000000; ">PRIVILEGE DETAILS</th>
                                                <th style="background-color:lightblue;color: #000000; ">BILL PROCESSED/VERIFIED</th>
                                                <th style="background-color:lightblue;color: #000000; ">Gross</th>
                                                <th style="background-color:lightblue;color: #000000; ">Deduction</th>
                                                <th style="background-color:lightblue;color: #000000; ">Net Pay</th>
                                                <th style="background-color:lightblue;color: #000000; ">View AQReport</th>
                                                <th style="background-color:lightblue;color: #000000; ">Bill Process</th>
                                                <th style="background-color:lightblue;color: #000000; ">Delete Office</th>
                                            </tr>
                                        </thead>
                                        <tbody> 
                                            <c:forEach items="${billGroupwiseOfcMappedList}" var="ofclist" varStatus="Count">

                                                <c:if test="${ofclist.noempMappedinbill gt 0}">
                                                    <tr style="font-family:serif;background-color: lightgray; ">
                                                        <td style="text-align:center;"><c:out value="${Count.index+1}"/></td>
                                                        <td><c:out value="${ofclist.offCode}"/></td>
                                                        <td><c:out value="${ofclist.offDdo}"/></td>
                                                        <td><c:out value="${ofclist.offDist}"/></td>
                                                        <td style="width:10%; font-size:18px ;font-weight: bold;text-align:center;"><c:out value="${ofclist.noempMappedinbill}"/></td>                                                        
                                                        <td><a href="#" onclick="viewPrivStatus('${ofclist.offCode}');" data-remote="false" data-toggle="modal" data-target="#viewPrivlegeStatus">
                                                                <button type="button" style="width:90%" class="btn btn-primary" >Privilege Details</button></a></td>
                                                        <td><c:out value="${ofclist.isbillVerified}"/></td>
                                                        <td><c:out value="${ofclist.grossAmt}"/></td>
                                                        <td><c:out value="${ofclist.dedAmt}"/></td>
                                                        <td><c:out value="${ofclist.netAmt}"/></td>
                                                        <td><a href="aqbillreportOfcWise.htm?format=f2&billNo=${ofclist.billNo}&offcode=${ofclist.offCode}" target="_blank">AQ REPORT-2</a></td>
                                                        <td><c:if test="${ofclist.taskid gt 0}">
                                                                <button type="button" style="width:100%" class="btn btn-default" disabled="true">Under Process</button>
                                                            </c:if>
                                                            <c:if test="${ofclist.taskid eq null || ofclist.taskid eq ''}">
                                                                <%--<a href="#"><button type="button" style="width:90%" class="btn btn-primary">ReProcess</button></a>
                                                                 <input type="submit" name="action" value="Reprocess" id="btn-reprocess" class="btn btn-default"/>--%>
                                                            </c:if>
                                                        </td>
                                                        <td><c:if test="${ofclist.taskid eq null || ofclist.taskid eq ''}">
                                                                <a href="javascript:void(0)" onclick="javascript : deleteOfficeBill('${ofclist.billNo}', '${ofclist.offCode}')">Delete</a>  
                                                            </c:if>
                                                            <c:if test="${ofclist.taskid gt 0}">
                                                                Delete
                                                            </c:if>
                                                        </td>

                                                    </tr>
                                                </c:if>
                                                <c:if test="${ofclist.noempMappedinbill eq 0}">
                                                    <tr style="font-family:serif ">
                                                        <td style="text-align:center;"><c:out value="${Count.index+1}"/></td>
                                                        <td><c:out value="${ofclist.offCode}"/></td>
                                                        <td><c:out value="${ofclist.offDdo}"/></td>
                                                        <td><c:out value="${ofclist.offDist}"/></td>
                                                        <td style="width:10%; font-size: 18px;font-weight: bold;text-align:center;"><c:out value="${ofclist.noempMappedinbill}"/></td>
                                                        <%--<td><c:out value="${ofclist.aqmastEmpNm}"/></td>
                                                        <td><c:out value="${ofclist.aqmastMobile}"/></td>  --%>  
                                                        <td><a href="#" onclick="viewPrivStatus('${ofclist.offCode}');" data-remote="false" data-toggle="modal" data-target="#viewPrivlegeStatus">
                                                                <button type="button" style="width:90%" class="btn btn-primary" >Privilege Details</button></a></td>                                                                                                             
                                                        <td><c:out value="${ofclist.isbillVerified}"/></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td>NA</td>
                                                        <td>
                                                            <c:if test="${ofclist.taskid gt 0}">
                                                                <button type="button" style="width:100%" class="btn btn-default" id="btnUnderprocess" disabled="true">Under Process</button>
                                                                <%--<input type="button" id="btnProcess" onclick="processAllBills('S', '${ofclist.offCode}');" class="btn btn-danger" style="width:90%" value="Process"/>--%>
                                                            </c:if>
                                                            <c:if test="${empty ofclist.taskid || ofclist.taskid eq ''}">
                                                                <%--<a href="javascript:void(0);" onclick="javascript: processBill('${ofclist.billNo}', '${ofclist.offCode}');"><button type="button" style="width:80%"  class="btn btn-danger">Process</button></a>--%>
                                                                <%--<button type="button" style="width:100%" class="btn btn-default" id="btnUnderprocess1" disabled="true">Under Process</button>--%>
                                                                <input type="button" id="btnProcess" onclick="processAllBills('S', '${ofclist.offCode}', '${billmonth}', '${billyear}');" class="btn btn-danger" style="width:90%" value="Process"/>
                                                            </c:if>
                                                        </td>
                                                        <td></td>
                                                    </tr>
                                                </c:if>

                                            </c:forEach>

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="viewPrivlegeStatus" class="modal fade" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content modal-lg">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h3 class="table-active"><u>My Office Privilege Details</u></h3>
                            <h4 id="empHeading" class="empheading"><B><U></U></B></h4>
                        </div>
                        <div class="modal-body" >
                            <table class="table" border="1"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1"> 
                                <thead>
                                    <tr >
                                        <th style="text-align:center;">Sl No</th>
                                        <th style="text-align:center;">Employee Name</th>
                                        <th style="text-align:center;">Mobile</th>
                                    </tr>
                                </thead>
                                <tbody id="empList">                                                    

                                </tbody>
                            </table>  

                        </div>
                        <div class="modal-footer">                       
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>


