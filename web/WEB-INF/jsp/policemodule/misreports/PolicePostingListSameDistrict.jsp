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
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <form:form action="PolicePostingListSameDistrict.htm" commandName="searchPolice" method="POST" autocomplete="off">
                            <div class="row">
                                <div class="col-sm-4">
                                    <label class="control-label">Post</label>
                                    <form:select class="form-control" path="sltRank" id="sltRank">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${postlist}" itemValue="postcode" itemLabel="post"/>
                                    </form:select>
                                </div>
                                <div class="col-sm-4">
                                    <label class="control-label">District</label>
                                    <form:select class="form-control" path="sltDistrict" id="sltDistrict">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${districtList}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-sm-3" style="padding-top:20px;">
                                    <input type="submit" value="Search" class="btn btn-danger"/>
                                </div>
                                <div class="col-sm-1"></div>
                            </div>
                        </form:form>
                    </div>
                    <div class="panel-body">
                        <div class="container-fluid">
                            <h2>List of employees posted in same district for more than 5 years</h2>
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="5%">SL No</th>
                                        <th width="13%">HRMS ID/GPF No</th>
                                        <th width="18%">Employee Name</th>
                                        <th width="8%">Date of Birth</th>
                                        <th width="25%">Current Post</th>
                                        <th width="10%">Current Post Joining Date</th>
                                        <th width="11%"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${PolicePostingListSameDistrict}" var="employee" varStatus="counter">
                                        <tr>
                                            <td>${counter.count}</td>
                                            <td>${employee.empid}<br />${employee.gpfno}</td>
                                            <td>${employee.empname}</td>
                                            <td>${employee.dob}</td>                                
                                            <td>${employee.designation}</td>
                                            <td>${employee.curPostJoiningDate}</td>
                                            <td></td>                                           
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
