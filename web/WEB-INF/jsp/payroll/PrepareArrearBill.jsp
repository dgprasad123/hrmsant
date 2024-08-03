<%-- 
    Document   : PrepareArrearBill
    Created on : 17 Mar, 2018, 11:23:04 AM
    Author     : Surendra
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
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

            $(document).ready(function() {
                if ($('#txtbilltype').val() != 'ARREAR_6' && $('#txtbilltype').val() != 'ARREAR_6_J') {
                    $("#percentageArraer").hide();
                    $("#percentageArraer").val('');
                    $("#percentageArraerJudiciary").hide();
                    $("#percentageArraerJudiciary").val('');
                } else if ($('#txtbilltype').val() == 'ARREAR_6') {
                    $("#percentageArraerJudiciary").hide();
                    $("#percentageArraerJudiciary").val('');
                } else if ($('#txtbilltype').val() == 'ARREAR_6_J') {
                    $("#percentageArraer").hide();
                    $("#percentageArraer").val('');
                }

                $('#txtbilltype').change(function() {
                    var sltBillType = $('#txtbilltype').val();
                    if (sltBillType == 'ARREAR_6') {
                        $("#percentageArraer").show();
                        $("#percentageArraerJudiciary").hide();
                        $("#percentageArraerJudiciary").val('');
                        $("#processFromDate").val('1-JAN-2016');
                        $("#processToDate").val('31-AUG-2017');
                        $('#processFromDate').attr('readonly', true);
                        $('#processToDate').attr('readonly', true);
                    } else if (sltBillType == 'ARREAR_6_J') {
                        $("#percentageArraerJudiciary").show();
                        $("#percentageArraer").hide();
                        $("#percentageArraer").val('');
                        $("#processFromDate").val('1-JAN-2016');
                        //$("#processToDate").val('31-DEC-2022');
                        $('#processFromDate').attr('readonly', true);
                        //$('#processToDate').attr('readonly', true);
                    } else {
                        $("#percentageArraerJudiciary").hide();
                        $("#percentageArraerJudiciary").val('');
                        $("#percentageArraer").hide();
                        $("#percentageArraer").val('');
                        $("#processFromDate").val('');
                        $("#processToDate").val('');
                        $('#processFromDate').attr('readonly', false);
                        $('#processToDate').attr('readonly', false);
                    }
                });
                validateBillDate();
            });

            function validate() {
                var checkBoxlength = $("input[name=billgroupId]:checked").length;
                var submitStatus = true;
                if (checkBoxlength == 0) {
                    alert("Please select bill.");
                    submitStatus = false;
                    return false;
                }
                if ($("#processDate1").val() == "") {
                    alert("Bill Date Cannot Be Blank");
                    submitStatus = false;
                    return false;
                }
                if (submitStatus == true) {
                    $('#btn-reprocess').hide();
                }
            }
            function validateForm() {

                var sltBillType = $('#txtbilltype').val();

                if (sltBillType == '') {
                    alert('Please select bill Type.');
                }
                if (sltBillType == 'ARREAR_6') {
                    if ($("#percentageArraer").val() == '') {
                        alert('Please select Arrear Percentage.');
                        $("#percentageArraer").focus();
                        return false;
                    }
                }
                if (sltBillType == 'ARREAR_6_J') {
                    if ($("#percentageArraerJudiciary").val() == '') {
                        alert('Please select Judiciary Arrear Percentage.');
                        $("#percentageArraerJudiciary").focus();
                        return false;
                    }
                }

                if ($("#processDate1").val() == "") {
                    alert("Bill Date Cannot Be Blank");
                    return false;
                }

                if ($("#processFromDate").val() == "") {
                    alert("From Date Cannot Be Blank");
                    return false;
                }

                if ($("#processToDate").val() == "") {
                    alert("To Date Cannot Be Blank");
                    return false;
                }
            }
            function viewEmpNotForArrear(billgroupId) {
                var finTrData = '';

                $('#EmpList').empty();
                $('#empHeading').empty();
                var url = 'viewEmployeeNotForArrearJSON.htm?billGroupId=' + billgroupId;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        finTrData = finTrData + "<tr>" +
                                "<td>" + (i + 1) + "</td>" +
                                "<td>" + obj.empid + "</td>" +
                                "<td>" + obj.empName + "</td>" +
                                "<td>" + obj.dob + "</td>" +
                                "<td>" + obj.dor + "</td>" +
                                "<td>" + obj.accttype + "</td>" +
                                "<td>" + obj.gpfno + "</td>" +
                                "<td>" + obj.empNonPran + "</td>" +
                                "<td>" + obj.empType + "</td>" +
                                "</tr>";
                    });
                    if (finTrData == "") {
                        alert("Employee Not Mapped");
                    }
                    //$('#empHeading').append("HRMS ID:").append($("#finalEmpId").val());
                    $('#EmpList').append(finTrData);
                });

            }

            var finyear1;
            var finyear2;
            function validateBillDate() {
                var curyear = new Date().getFullYear();
                var curmonth = new Date().getMonth();

                if (curmonth < 3) {
                    finyear1 = curyear - 1;
                    finyear2 = curyear;
                } else if (curmonth >= 3) {
                    finyear1 = curyear;
                    finyear2 = curyear + 1;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="prepareNewArrearBillform.htm" method="post" commandName="BillBrowserbean" >
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">                            
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="billType">Bill Type:</label>
                                    <form:select path="txtbilltype" id="txtbilltype" class="form-control">
                                        <form:option value=""> -- Select Bill Type --</form:option>
                                        <form:option value="ARREAR">7th Pay Arrear(40% or 100%)</form:option>
                                        <form:option value="ARREAR_J">7th Pay Arrear(Judiciary)(First 25% or 100%)</form:option>
                                        <form:option value="ARREAR_6">7th Pay Arrear(Percentage)</form:option>
                                        <form:option value="ARREAR_6_J">7th Pay Arrear(Judiciary)(Percentage)</form:option>
                                        <form:option value="OTHER_ARREAR">OTHER Arrear</form:option>
                                        <form:option value="ARREAR_NPS">NPS Arrear 5%(10% of rest 50%)</form:option>
                                    </form:select>                
                                </div>
                            </div>

                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="processDateArr">Process Date:</label>
                                    <div class='input-group date' id='processDate'>
                                        <form:input class="form-control" id="processDate1" path="processDateArr" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">                            
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="percentage">  </label>
                                    <form:select path="percentageArraer" id="percentageArraer" class="form-control">
                                        <form:option value=""> -- Select Arrear Percent --</form:option>
                                        <form:option value="10"> 10% </form:option>
                                        <form:option value="50"> 50% </form:option>
                                        <form:option value="60"> 60% </form:option>
                                        <form:option value="30"> 30% </form:option>
                                        <form:option value="20"> 20% </form:option>
                                    </form:select>                
                                </div>
                            </div>
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="percentageArraerJudiciary">  </label>
                                    <form:select path="percentageArraerJudiciary" id="percentageArraerJudiciary" class="form-control">
                                        <form:option value=""> -- Select Judiciary Arrear Percent --</form:option>
                                        <form:option value="25"> 25% </form:option>
                                        <form:option value="50"> 50% </form:option>
                                    </form:select>                
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-lg-3">

                                <div class="form-group">
                                    <label for="sltFromYearArr">From Date:</label>
                                    <div class='input-group date' id='processFromDate1'>
                                        <form:input class="form-control" id="processFromDate" path="processFromDate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="sltFromMonthArr">To Date:</label>

                                    <div class='input-group date' id='processToDate1'>
                                        <form:input class="form-control" id="processToDate" path="processToDate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>

                                <input type="submit" name="action" value="Ok" class="btn btn-default" onclick="return validateForm()"/>

                            </div>
                        </div>
                        <div class="clearfix"> </div>                     

                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="5%">Select Bill</th>
                                    <th width="30%">Bill Name</th>                                
                                    <th width="10%">Chart of Account</th>
                                    <th width="10%">View</th>
                                    <th width="10%">Remarks</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${billGroupList}" var="billattr"  >
                                    <c:if test="${billattr.noteligible gt 0}" >                                 
                                        <tr style="color:red;">                                     
                                            <td><input type="checkbox" name="billgroupId1" value="${billattr.billgroupId}" disabled="True"/></td>
                                            <td>${billattr.billDesc}</td>                                
                                            <td>${billattr.chartofAcc}</td>
                                            <td><a href="#" onclick="viewEmpNotForArrear('${billattr.billgroupId}');" data-remote="false" data-toggle="modal" title="View Status" data-target="#viewEmployeeModal" class="btn btn-default">Employee Not Eligible For Arrear</a> </td>                                                                  
                                            <td>Bill Can't Be Processed</td>
                                        </tr>
                                    </c:if>                             
                                    <c:if test="${billattr.noteligible eq null}" >
                                        <tr>
                                            <td><input type="checkbox" name="billgroupId" value="${billattr.billgroupId}"/></td>
                                            <td>${billattr.billDesc}</td>                                
                                            <td>${billattr.chartofAcc}</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                        <c:if test="${not empty billGroupList}">
                            <input type="submit" class="btn btn-default" name="action" value="Process" id="btn-reprocess" onclick="return validate()"/>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form> 
        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
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

        <!-- Employee Verification Modal -->
        <div id="viewEmployeeModal" class="modal fade" role="dialog">
            <div class="modal-dialog"  style="width:65%;">
                <!-- Modal content-->
                <div class="modal-content modal-lg" style="width:125%;">
                    <div class="modal-header" style="text-align: center;">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h3 class="table-active"><u>Employee Not Eligible For Arrear</u></h3>
                        <h4 id="empHeading" class="empheading"><B><U></U></B></h4>
                    </div>
                    <div class="modal-body" >
                        <table class="table" border="1"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1"> 
                            <thead>
                                <tr>
                                    <th style="text-align:center;">Sl No</th>
                                    <th style="text-align:center;">HrmsId</th>
                                    <th style="text-align:center;">Employee Name</th>
                                    <th style="text-align:center;">Date of<br>Birth</th>
                                    <th style="text-align:center;">Date of<br>Superannuation</th>
                                    <th style="text-align:center;">Account Type</th>
                                    <th style="text-align:center;">Account No</th>                                   
                                    <th style="text-align:center;">If Gpf Assumed</th>
                                    <th style="text-align:center;">Employee Type</th>
                                </tr>
                            </thead>
                            <tbody id="EmpList">                                                    

                            </tbody>                           

                        </table> 
                        <span style="color:red;">1. Date of Birth and Date of Superannuation should not be Blank.<br/>
                            2. The Gap between Date of Birth and Date of Superannuation must not be less than 59 yrs. and 11 months.<br/>
                            3. Dummy Account no. of Regular employees belongs to GPF/TPF/PRAN category not allowed to process Arrear Bill.<br/>
                            4. Regular salary must be done before preparation of Arrear bill.
                            </span>

                    </div>
                    <div class="modal-footer">                       
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<script type="text/javascript">
    $(function() {
        $('#processDate').datetimepicker({
            format: 'D-MMM-YYYY',
            minDate: new Date(finyear1, '3', '1'),
            maxDate: new Date(finyear2, '2', '31')
        });

        $('#processFromDate1').datetimepicker({
            format: 'D-MMM-YYYY'
        });

        $('#processToDate1').datetimepicker({
            format: 'D-MMM-YYYY'
        });
    });
</script>
<style>    
    .empheading
    {
        font-weight: bold;
        font-size: 25px;
    }
</style>
</body>
</html>

