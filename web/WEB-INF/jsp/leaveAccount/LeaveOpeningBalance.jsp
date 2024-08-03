<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
                <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
                <script type="text/javascript" src="js/moment.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $(".txtNum").keypress(function(e) {
                    //if the letter is not digit then display error and don't type anything
                    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                        //display error message
                        $("#errmsg").html("Digits Only").show().fadeOut("slow");
                        return false;
                    }
                });
            });
            function confirmDelete(lobId)
            {
                if (confirm("Are you sure you want to delete?"))
                {
                    $.ajax({
                        url: 'DeleteOpeningBalance.htm',
                        type: 'get',
                        data: 'lobId=' + lobId,
                        success: function(retVal) {
                            self.location = 'LeaveOpeningBalance.htm';
                        }
                    });

                }
            }
            function editOpeningBalance(lotId, leaveId, openingBalanceDate, time, openingBalance) {
                $("#lotId").val(lotId);
                $("#leaveId").val(leaveId);
                $('#AddModal').modal('show');
                $('#openingBalanceDate').val(openingBalanceDate);
                $('#time').val(time);
                $('#openingBalance').val(openingBalance);
            }
            function CheckDecimal(inputtxt) 
            { 
                var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
                if(inputtxt.match(decimal)) 
                { 
                alert('Correct, try another...')
                return true;
                }
                else
                { 
                alert('Wrong...!')
                return false;
                }
            } 
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <form:form action="LeaveOpeningBalance.htm" method="post" commandName="LeaveOpeningBalanceForm">
            <input type="hidden" name="lotId" id="lotId" value="" />
            <input type="hidden" name="leaveId" id="leaveId" value="" />
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 style="font-size:15pt;margin:0px;">Leave Opening Balance</h1>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered" style="font-size:10pt;">
                            <thead>
                                <tr style="background:#EAEAEA;">
                                    <th>Leave Code</th>
                                    <th>Leave Type</th>
                                    <th>Date of Opening Balance</th>
                                    <th>Time</th>
                                    <th>Opening Balance</th>
                                    <th style="text-align:center;">Edit</th>
                                    <th style="text-align:center;">Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${obList}" var="tlist">
                                    <tr>
                                        <td>${tlist.leaveId}</td>
                                        <td>${tlist.leaveType}</td>
                                        <td>${tlist.openingBalanceDate}</td>
                                        <td>${tlist.time}</td>
                                        <td>${tlist.openingBalance}</td>
                                        <td align="center"><a href="javascript:void(0)"  onclick="javascript: editOpeningBalance('${tlist.lobId}', '${tlist.leaveId}', '${tlist.openingBalanceDate}', '${tlist.time}', '${tlist.openingBalance}')">Edit</a></td>
                                        <td align="center"><c:if test="${not empty tlist.lobId}"><a href="javascript: void(0)" onclick="javascript: confirmDelete(${tlist.lobId})">Delete</a></c:if></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div id="AddModal" class="modal fade" role="dialog">
                        <div class="modal-dialog" style="border: 1px solid #0000FF;">
                            <!-- Modal content-->
                            <div class="modal-content" >
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title" style="color:#0067C4">Edit Opening Balance</h4>
                                </div>
                                <div class="modal-body">
                                    <table align="center" cellpadding="2" cellspacing="2" style="font-size:11pt;">
                                        <tr style="height: 40px">
                                            <th>Date of Opening Balance: </th>
                                            <td><div class='input-group date' id='obDate'><input type="text" id="openingBalanceDate" name="openingBalanceDate" path="openingBalanceDate" class="form-control" />
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span></div></td>
                                        </tr>
                                        <tr style="height: 40px">
                                            <th>Time : </th>
                                            <td><select name="time" id="time" class="form-control">
                                    <option value="">-Select-</option>
                                    <option value="FN">Fore Noon</option>
                                    <option value="AN">After Noon</option>
                                                </select></td>
                                        </tr>

                                        <tr style="height: 40px">
                                            <th>Opening Balance: </th>
                                            <td><input type="text" name="openingBalance" id="openingBalance" class="form-control" onclick="javascript: this.select()" /></td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" onclick="javascript: saveOpeningBalance()">Save</button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>



                    <div class="panel-footer">
                    </div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript">
            $(function() {
                $('#openingBalanceDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
               
            });
            function saveOpeningBalance()
            {
                if ($('#openingBalanceDate').val() == '')
                {
                    alert("Please select Opening Balance Date.");
                    $('#openingBalanceDate')[0].focus();
                    return false;
                }
                if ($('#time').val() == '')
                {
                    alert("Please select Time.");
                    $('#time')[0].focus();
                    return false;
                }
                if ($('#openingBalance').val() == '')
                {
                    alert("Please enter Opening Balance.");
                    $('#openingBalance')[0].focus();
                    return false;
                }
                if(isNaN($('#openingBalance').val()))
                {
                    alert("Please enter a valid decimal value.");
                    $('#openingBalance')[0].focus();
                    $('#openingBalance')[0].select();
                    return false;
                }
                //CheckDecimal($('#openingBalance').val()) ;
                
                $.ajax({
                    url: 'SaveOpeningBalance.htm',
                    type: 'get',
                    data: 'lotId=' + $('#lotId').val() + '&leaveId=' + $('#leaveId').val()+'&openingBalanceDate='
                            +$('#openingBalanceDate').val()
                            +'&time='+$('#time').val()
                            +'&openingBalance='+$('#openingBalance').val(),
                    success: function(retVal) {
                        self.location = 'LeaveOpeningBalance.htm';
                    }
                });
    }
            </script>
    </body>
</html>
