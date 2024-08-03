<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                
            });

            
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">
                        <form:form action="WrrReportEmployeewise.htm" method="POST" commandName="waterRent">
                            <table border="0" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">                            
                                <tr style="height:40px;">
                                    <td align="center">
                                        <label>Employee</label>
                                    </td>
                                    <td>
                                        <form:input path="hrmsid"  cssClass="form-control"/>                                                                        
                                    </td>                                                                                            
                                    <td>
                                        <div class="btn-group" style="padding-left: 50px;">
                                            <button type="submit" value="Search" name="action" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Search</button>
                                            <button type="submit" value="Download" name="action" class="btn btn-primary"><span class="glyphicon glyphicon-export"></span> Download</button>
                                        </div>
                                    </td>                                    
                                </tr>
                            </table>
                        </form:form>
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Consumer No</th>
                                            <th>HRMS Id</th>
                                            <th>MONTH</th>
                                            <th>YEAR</th>
                                            <th>Employee Name</th>
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th>
                                            <th>DDO Code</th>
                                            <th>Office Name</th>
                                            <th>TV No</th>
                                            <th>TV Date</th>
                                            <th>Water Tax</th>
                                            <th>Sewerage Tax</th>                                            
                                            <th>Date of Allotment</th>
                                        </tr>
                                    </thead>
                                    <tbody id="wrrgrid">
                                        <c:forEach items="${waterRentlist}" var="wrr" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index+1}</td> 
                                                <td>${wrr.consumerNo}</td>
                                                <td>${wrr.hrmsid}</td>
                                                <td>${wrr.recoverymonth}</td>
                                                <td>${wrr.recoveryyear}</td>
                                                <td>${wrr.fname} ${wrr.mname} ${wrr.lname}</td>
                                                <td>${wrr.quarterNo}</td>                                                                                                
                                                <td>${wrr.qrtrtype}</td>
                                                <td>${wrr.qrtrunit}</td>
                                                <td>${wrr.ddocode}</td>
                                                <td>${wrr.officename}</td>
                                                <td>${wrr.tvno}</td>
                                                <td>${wrr.tvdate}</td>                                                
                                                <td>${wrr.wtax}</td>
                                                <td>${wrr.swtax}</td>                                                
                                                <th>${wrr.dateofallotment}</th>
                                            </tr>
                                        </c:forEach>                                        
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
