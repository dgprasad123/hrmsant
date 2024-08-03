<%-- 
    Document   : PARCemplist
    Created on : Mar 13, 2020, 1:36:24 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script> 
        <script language="javascript" src="js/servicehistory.js" type="text/javascript"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }
            #myInput {
                background-image: url('/images/searchicon.png'); /* Add a search icon to input */
                background-position: 10px 12px; /* Position the search icon */
                background-repeat: no-repeat; /* Do not repeat the icon image */
                width: 100%; /* Full-width */
                font-size: 16px; /* Increase font-size */
                padding: 12px 20px 12px 40px; /* Add some padding */
                border: 1px solid #ddd; /* Add a grey border */
                margin-bottom: 12px; /* Add some space below the input */
            }
        </style>
        <script type="text/javascript">
            /*var treviewedEmpId;
             var treviewedSpc;
             var selectedObj;
             $(document).ready(function() {
             $("#showDateWindow").hide();
             
             $('.datepickertxt').datetimepicker({
             format: 'D-MMM-YYYY',
             useCurrent: false,
             ignoreReadonly: true
             });
             $('.datepickerclass').datetimepicker({
             format: 'D-MMM-YYYY',
             useCurrent: false,
             ignoreReadonly: true
             });
             });
             function addEmployeeToGroupc(me, reviewedEmpId, reviewedSpc, groupCpromotionId, assessmentTypeReporting, periodFromReporting, periodToReporting) {
             var radioValue = $("input[name='assessmentTypeReporting']:checked").val();
             alert(radioValue);
             var url = "addgroupcEmpList.htm";
             $.post(url, {reviewedempId: treviewedEmpId, reviewedspc: treviewedSpc, groupCpromotionId: groupCpromotionId, assessmentTypeReporting: radioValue, periodFromReporting: periodFromReporting, periodToReporting: periodToReporting})
             .done(function(data) {
             $('#employeeDetail').modal('hide');
             alert("Saved Successfully");
             console.log($(selectedObj).html());
             $(selectedObj).parent().html("<span>Added</span>");
             });
             
             }
             function openEmpPeriodDetailWindow(me, reviewedEmpId, reviewedSpc) {
             treviewedEmpId = reviewedEmpId;
             treviewedSpc = reviewedSpc;
             selectedObj = me;
             $('#employeeDetail').modal('show');
             }
             function radioClicked() {
             $("#showDateWindow").hide();
             var radioValue = $("input[name='assessmentTypeReporting']:checked").val();
             if (radioValue == "fullPeriod") {
             alert("From Date Should be 1-04-2022 and To Date Should be 31-03-2022");
             alert("Are You Sure You Want to Choose ?");
             } else {
             $("#showDateWindow").show();
             }
             
             }*/
            function addEmployeeToGroupc(me, reviewedEmpId, reviewedSpc, groupCpromotionId) {
                var url = "addgroupcEmpList.htm";
                $.post(url, {reviewedempId: reviewedEmpId, reviewedspc: reviewedSpc, groupCpromotionId: groupCpromotionId})
                        .done(function(data) {
                            console.log($(me).html());
                            $(me).parent().html("<span>Added</span>");
                        });

            }
            function myFunction() {
                // Declare variables
                var input, filter, table, tr, td,td1, i, txtValue;
                input = document.getElementById("myInput");
                filter = input.value.toUpperCase();
                table = document.getElementById("myTable");
                tr = table.getElementsByTagName("tr");

                // Loop through all table rows, and hide those who don't match the search query
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[1];
                    td1 = tr[i].getElementsByTagName("td")[2];
                    td2 = tr[i].getElementsByTagName("td")[3];
                    td3 = tr[i].getElementsByTagName("td")[4];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        txtValue = txtValue + (td1.textContent || td1.innerText);
                        txtValue = txtValue + (td2.textContent || td2.innerText);
                        txtValue = txtValue + (td3.textContent || td3.innerText);
                        if (txtValue.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
        </script>        
    </head>
    <body>
        <input type="hidden" id="hidfiscalyear" value="${fiscalyear}"/>
        <form:form action="parCPromotionReport.htm" method="POST" commandName="groupCInitiatedbean" class="form-inline">
            <div class="panel panel-default">
                <h4 style="text-align: center"><b>Group C Employee List</b></h4>
                <h1><div style="padding: 0px 15px 0px 15px;">
                    <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for names..">
                    </div></h1>
                <div class="panel-body" style="height: 550px;overflow: auto;">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover table-striped" id="myTable">
                            <thead>
                                <tr>                                            
                                    <th width="3%">#</th>
                                    <th>Employee Name</th>
                                    <th>GPF NO</th>
                                    <th>Designation</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>                                        
                                <c:forEach items="${groupcemplist}" var="groupcemp" varStatus="count">
                                    <tr>                                                
                                        <td>${count.index + 1}</td>
                                        <td>${groupcemp.reviewedempname}</td>
                                        <td>${groupcemp.gpfno}</td>
                                        <td>${groupcemp.reviewedpost}</td>
                                        <td>
                                            <c:if test="${groupcemp.alreadyAdded eq 'Y'}">
                                                <span>Already Added</span>
                                            </c:if>
                                            <c:if test="${groupcemp.alreadyAdded eq 'N'}">
                                                <button type="button" onclick="addEmployeeToGroupc(this, '${groupcemp.reviewedempId}', '${groupcemp.reviewedspc}', '${groupCInitiatedbean.groupCpromotionId}')" class="btn btn-default">Add</button>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="panel-footer">
                    <form:hidden path="groupCpromotionId"/>
                    <input type="submit" name="action" class="btn btn-default" value="Get Selected Employee List"/>
                    <input type="submit" name="action" class="btn btn-default" value="Other Office"/>
                    <input type="submit" name="action" class="btn btn-default" value="Back"/>
                </div>
            </div>
        </form:form>  
        <div id="employeeDetail" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title" style="text:align center"><b>Choose Any Radio Button</b></h4>
                    </div>
                    <div class="modal-body"> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-10">
                                <span style="color: red"><b>(From Date Of <span style="color: red" id="treviewedempname"></span> is: <span style="color: red" id="tfromDateReporting"></span> and To Date is:  <span style="color: red" id="ttoDateReporting"></span>)</b></span>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">

                            <div class="col-lg-2">
                                <label for="fa">&nbsp;</label>
                            </div>

                            <div class="col-lg-2"> 
                                <input type="radio"  name="assessmentTypeReporting" value="fullPeriod" onclick="radioClicked()" /><b>Full Period</b>
                            </div>
                            <div class="col-lg-2">
                                <input type="radio"  name="assessmentTypeReporting" value="halfPeriod" onclick="radioClicked()" /><b>Partial Period</b>
                            </div>                                                                    
                        </div>
                        <div class="row" style="margin-bottom: 7px;" id="showDateWindow">
                            <div class="col-lg-1">
                                <label for="fa"></label>
                            </div>
                            <div class="col-lg-2">
                                <label for="from date">From Date</label>
                            </div>
                            <div class="col-lg-3">
                                <div class="input-group date">
                                    <input class="form-control datepickerclass" name="periodFromReporting" id="periodFromReporting" readonly="true"/> 
                                </div> 
                            </div>
                            <div class="col-lg-1">
                                <label for="To Date">To Date</label>
                            </div>
                            <div class="col-lg-3">
                                <div class="input-group date">
                                    <input class="form-control datepickerclass"  name="periodToReporting" id="periodToReporting" readonly="true"/> 
                                </div>  
                            </div>
                        </div>


                    </div>
                    <div class="modal-footer">                                                         
                        <button type="button"  onclick="addEmployeeToGroupc(this, '${groupCInitiatedbean.reviewedempId}', '${groupCInitiatedbean.reviewedspc}', '${groupCInitiatedbean.groupCpromotionId}', '${groupCInitiatedbean.assessmentTypeReporting}', '${groupCInitiatedbean.periodFromReporting}', '${groupCInitiatedbean.periodToReporting}')" class="btn btn-default">Save</button>
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>


    </body>
</html>


