<%@ page contentType="text/html;charset=windows-1252" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function downloadpolicelistexcel() {
                var url = "DownloadPolicePoliceListExcel.htm?sltDistrict=" + $("#sltDistrict").val() + "&sltRank=" + $("#sltRank").val();
                window.location = url;
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <form:form action="PolicePostingList.htm" commandName="searchPolice" method="POST" autocomplete="off">
                            <div class="row">
                                <div class="col-sm-3">
                                    <label class="control-label">District</label>
                                    <form:select class="form-control" path="sltDistrict" id="sltDistrict">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${establishmentList}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-sm-4">
                                    <label class="control-label">Post</label>
                                    <form:select class="form-control" path="sltRank" id="sltRank">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${postlist}" itemValue="postcode" itemLabel="post"/>
                                    </form:select>
                                </div>
                                <div class="col-sm-3" style="padding-top:20px;">
                                    <input type="submit" value="Search" class="btn btn-danger"/>                                    
                                    &nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="downloadpolicelistexcel();">
                                        <img src="./images/excel_icon.png"/>
                                    </a>
                                </div>
                                <div class="col-sm-2" style="padding-top:20px;"></div>
                            </div>
                        </form:form>
                    </div>
                    <div class="panel-body">
                        <div class="container-fluid">                            
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="5%">SL No</th>
                                        <th width="10%">HRMS ID/GPF No</th>
                                        <th width="15%">Employee Name</th>
                                        <th width="8%">Date of Birth</th>
                                        <th width="10%">Date of Joining</th>
                                        <th width="10%">Date of<br />Retirement</th>
                                        <th width="20%">Current Post</th>
                                        <th width="20%">Office Establishment</th>
                                        <th width="15%">Category</th>
                                        <th width="11%">Service Book</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${PolicePostingList}" var="employee" varStatus="counter">
                                        <tr>
                                            <td>${counter.count}</td>
                                            <td>${employee.empid}<br />${employee.gpfno}</td>
                                            <td>${employee.empname}</td>
                                            <td>${employee.dob}</td>
                                            <td>${employee.doj}</td>
                                            <td>${employee.dos}</td>
                                            <td>${employee.designation}</td>
                                            <td>${employee.offname}</td>
                                            <td>${employee.category}</td>
                                            <td>
                                                <a href="PoliceServiceBook.htm?empid=${employee.empid}" target="_blank">View</a>
                                            </td>                                           
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
