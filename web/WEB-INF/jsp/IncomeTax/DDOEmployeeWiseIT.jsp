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

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">

            function validate() {
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
                $('#btn_submit')[0].disabled = true;
                $('#loader').css('visibility', 'visible');
            }

        </script>    
    </head>
    <body>


        <div id="page-wrapper">
            <form:form class="form-inline" action="DDOEmployeeWiseIT.htm" commandName="EmployeeWiseIT">
                <div class="container-fluid" style="padding-top: 5px;padding-bottom: 5px;">
                    <div class="panel panel-default">
                        <div class="row">
                            <div class="col-lg-12" align="center"> 
                                <b style="color:#0D8BE6;font-size:20px;"> OFFICE WISE IT DEDUCTION </b> 
                            </div>
                        </div>
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="form-group" style="width:20%;">
                                        <label for="sltYear">&nbsp;Year:</label>
                                        <form:select path="sltYear" id="sltYear" class="form-control">
                                            <option value="">Select</option>
                                            <form:options items="${YearList}" itemValue="value" itemLabel="label"/>                                        
                                        </form:select>
                                    </div>
                                    <div class="form-group" style="width:20%;">
                                        <label for="sltMonth">&nbsp;Month:</label>
                                        <form:select path="sltMonth" class="form-control">
                                            <option value="">Select</option>
                                            <form:option value="0">JANUARY</form:option>
                                            <form:option value="1">FEBRUARY</form:option>
                                            <form:option value="2">MARCH</form:option>
                                            <form:option value="3">APRIL</form:option>
                                            <form:option value="4">MAY</form:option>
                                            <form:option value="5">JUNE</form:option>
                                            <form:option value="6">JULY</form:option>
                                            <form:option value="7">AUGUST</form:option>
                                            <form:option value="8">SEPTEMBER</form:option>
                                            <form:option value="9">OCTOBER</form:option>
                                            <form:option value="10">NOVEMBER</form:option>
                                            <form:option value="11">DECEMBER</form:option>
                                        </form:select>
                                    </div>
                                    <button type="submit" class="btn btn-primary" id="btn_submit" onclick="javascript: return validate()">Submit</button>
                                    <span id="loader" style="font-size:8pt;font-style:italic;color:#777777;visibility: hidden;"> Please wait...</span>
                                </div>
                            </div>
                        </div>

                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th style="font-weight:bold;">Employee Name</th>
                                        <th style="font-weight:bold;">PAN</th>
                                        <th style="font-weight:bold;text-align:right;">Amount Deducted</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty ddoList}">
                                        <c:set var="total" value="${0}"/>
                                        <c:forEach items="${ddoList}" var="bill">
                                            <c:set var="total" value="${total + bill.amount}" />
                                            <tr>
                                                <td>${bill.empName}<br /><strong style="font-size:10pt;color:#890000;">${bill.postName}</strong></td>
                                                <td>${bill.pan}</td>
                                                <td style="text-align:right;">&#8377;${bill.amount}</td>
                                            </tr>
                                        </c:forEach>    
                                    </c:if>

                                    <tr style="font-weight:bold;background:#FFFFE1">
                                        <td colspan="2" align="right">Total:</td>
                                        <td align="right">&#8377;${total}</td>
                                    </tr>
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
