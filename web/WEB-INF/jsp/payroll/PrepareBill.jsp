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
                    $('#btn-process').hide();
                }


            }
            function validateForm() {
                if ($("#processDate1").val() == "") {
                    alert("Bill Date Cannot Be Blank");
                    return false;
                }
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
        <form:form class="form-inline" action="prepareNewBillform.htm" method="post" commandName="BillBrowserbean" >
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">

                                <div class="form-group">
                                    <label for="billType">Bill Type:</label>
                                    <form:select path="txtbilltype" id="txtbilltype" class="form-control">
                                        <form:option value="PAY">Pay Bill</form:option>
                                    </form:select>                
                                </div>
                                <div class="form-group">
                                    <label for="sltYear">Year:</label>
                                    <form:select path="sltYear" id="sltYear" class="form-control">
                                        <form:options items="${billYears}" itemValue="value" itemLabel="label"/> 
                                    </form:select>
                                </div>
                                <div class="form-group">
                                    <label for="sltMonth">Month:</label>

                                    <form:select path="sltMonth" id="sltMonth" class="form-control">
                                        <form:options items="${billMonths}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="form-group">
                                    <label for="processDate">Process Date:</label>
                                    <div class='input-group date' id='processDate'>
                                        <form:input class="form-control" id="processDate1" path="processDate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>
                                <input type="submit" name="action" value="Ok" class="btn btn-default" onclick="return validateForm()"/>

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="5%">Select Bill</th>
                                    <th width="30%">Bill Name</th>                                
                                    <th width="10%">Chart of Account</th>                                
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${billGroupList}" var="billattr">
                                    <tr>
                                        <td><input type="checkbox" name="billgroupId" value="${billattr.billgroupId}"/></td>
                                        <td>${billattr.billDesc}</td>                                
                                        <td>${billattr.chartofAcc}</td>                                
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                        <c:if test="${not empty billGroupList}">
                            <input type="submit" class="btn btn-default" name="action" value="Process" id="btn-process" onclick="return validate()"/>
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
        <script type="text/javascript">
            $(function() {
                $('#processDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    minDate: new Date(finyear1, '3', '1'),
                    maxDate: new Date(finyear2, '2', '31')
                });
            });
        </script>
    </body>
</html>
