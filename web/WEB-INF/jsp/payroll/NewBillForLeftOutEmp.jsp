<%-- 
    Document   : PrepareBill
    Created on : Oct 23, 2017, 5:39:33 PM
    Author     : Manas
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
                $('#processDate1').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            
            
            function showAddChartOfAccountWindow() {

                $('#chartofAccModal').modal('show');
                $('#chartofAccModal').find(".modal-body").load('billGroupListForLeftOutEmployee.htm');
            }
            
            
            function showAddEmployeeWindow() {

                $('#empModal').modal('show');
                $('#empModal').find(".modal-body").load('getEmpListForLeftOutEmployee.htm');
                
            }
            
            function addEmployee(){
                
                var newRowContent='<tr><td>'+'<input type ="hidden" name="empId" value="'+$("#employeeId").val()+'"/>' +$("#td1").html()+'</td><td width="30%">'+$("#td2").html()+'</td><td>'+$("#td3").html()+'</td></tr>';
                $("#emptblEnt tbody").append(newRowContent);
                $("#employeeId").val('');
                $("#td1").html('');
                $("#td2").html('');
                $("#td3").html('');
                $('#empModal').modal('hide');
                
            }

            function addChartofAccount() {
                $('#chaAacc').html('');
                var selValue = $('input[name=billgroupid]:checked').val();
                var idsel = $('input[name=billgroupid]:checked').attr('id');
                thenum = idsel.match(/\d+/)[0];
                $('#billgroupId').val(selValue);
                var chart = $("#hidchartofAcc" + thenum).val();
                $('#chaAacc').append(chart);
                $('#chartofAccModal').modal('hide');

            }


        </script>
    </head>
    <body>
        <form:form action="prepareNewBillForLeftOut.htm" method="post" commandName="BillBrowserbean" >

            <form:hidden path="offCode" id="offcode"/>
            <form:hidden path="billgroupIdASString" id="billgroupId"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                Chart of Account: <span id="chaAacc"> </span>   <a href="javascript:showAddChartOfAccountWindow()"> <span class="glyphicon glyphicon-plus"></span> </a> 
                            </div>
                        </div>
                    </div>
                    <div class="panel-heading">    
                        <div class="row">
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="billType">Bill Type:</label>
                                    <form:select path="txtbilltype" id="txtbilltype" class="form-control">
                                        <form:option value="PAY">Pay Bill</form:option>
                                    </form:select>                
                                </div>
                                
                            </div>
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="sltYear">Year:</label>
                                    <form:select path="sltYear" id="sltYear" class="form-control">
                                        <form:options items="${billYears}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    
                                        
                                </div>
                                
                            </div>
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="sltMonth">Month:</label>
                                    <form:select path="sltMonth" id="sltMonth" class="form-control">
                                        <form:options items="${billMonths}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                    
                                </div>
                                
                            </div>
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="processDate">Process Date:</label>
                                    <div class='input-group date' id='processDate1'>
                                        <form:input class="form-control" id="processDate" path="processDate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>
                                
                            </div>
                            
                                
                                
                                
                                
                                

                            
                        </div>
                    </div>
                    <div class="panel-body">
                        <div>  Add Employee <a href="javascript:showAddEmployeeWindow()"> <span class="glyphicon glyphicon-plus"></span> </a>   </div>
                        <table id="emptblEnt" class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="10%">HRMS ID/ GPF No.</th>
                                    <th width="30%">Employee Name</th>                                
                                    <th width="30%">Designation</th> 
                                </tr>
                            </thead>
                            <tbody>
                                
                            </tbody>
                            <div id="empModal" class="modal fade" role="dialog">
                                <div class="modal-dialog">

                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title">Chart of Account </h4>
                                        </div>
                                        <div class="modal-body">
                                            


                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" onclick="addEmployee()">Add</button>
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                        <input type="submit" class="btn btn-default" name="action" value="Process" onclick="return validate()"/>
                        
                    </div>
                </div>
            </div>
        </form:form> 
        <!-- Modal -->
        <div id="chartofAccModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Chart of Account </h4>
                    </div>
                    <div class="modal-body">



                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="addChartofAccount()">Add</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>
        <script type="text/javascript">

        </script>
    </body>
</html>
