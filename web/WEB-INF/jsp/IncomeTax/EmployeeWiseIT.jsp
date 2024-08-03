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
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/> 
            <div id="page-wrapper">
                <form:form class="form-inline" action="EmployeeWiseIT.htm" commandName="EmployeeWiseIT">
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
                                            <form:select path="sltYear" id="sltYear" class="form-control" onchange="showMonth()">
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
                                            <th width="11%" style="font-weight:bold;">DDO Name</th>
                                            <th width="7%" style="font-weight:bold;">Num of Employees</th>
                                            <th width="5%" style="font-weight:bold;text-align:right;">IT Deducted</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${not empty ddoList}">
                                            <c:forEach items="${ddoList}" var="bill">
                                                <tr>
                                                    <td>${bill.officeName}</td>
                                                    <td>${bill.totalEmployees}</td>
                                                    <td style="text-align:right;">&#8377;${bill.totalAmount}</td>
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
