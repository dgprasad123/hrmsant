<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;layout: fixed;color:#000000;font-weight:bold;">
                                <thead> </thead>
                                <tr>
                                    <td width="20%" align="right">
                                        Employee Name:&nbsp;&nbsp;                    
                                    </td>
                                    <td width="38%" style="text-transform:uppercase;" align="left">
                                        <b> ${SelectedEmpObj.fullName} </b>
                                    </td>
                                    <td width="16%" align="right">
                                        HRMS ID:&nbsp;&nbsp;                    
                                    </td>
                                    <td width="26%">
                                        ${SelectedEmpObj.empId} 
                                    </td>
                                </tr>

                                <tr>
                                    <td align="right">Current Post:&nbsp;&nbsp; </td>
                                    <td >
                                        ${SelectedEmpObj.postname} 
                                    </td>
                                    <td align="right">GPF/ PPAN No:&nbsp;&nbsp;</td>
                                    <td><b style="text-transform:uppercase;"> ${SelectedEmpObj.gpfno}&nbsp;</b></td>
                                </tr>
                                <tr>
                                    <td align="right">Current Cadre:&nbsp;&nbsp; </td>
                                    <td align="left"><b> ${SelectedEmpObj.cadrename}   &nbsp;</b></td>
                                    <td align="right">Current Status:&nbsp;&nbsp;</td>
                                    <td><b> ${SelectedEmpObj.depstatus}&nbsp;</b></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="5%" style="text-align: center;">Sl No.</th>
                                <th width="15%" style="text-align: center;">Station</th>
                                <th width="20%" style="text-align: center;">Details</th>
                                <th width="10%" style="text-align: center;">From Date</th>
                                <th width="10%" style="text-align: center;">To Date</th>
                                <th colspan="2" width="10%" style="text-align: center;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${servicerecordlist}" var="srlist" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${srlist.station}</td>
                                    <td>${srlist.details}</td>
                                    <td>${srlist.fromDate}</td>
                                    <td>${srlist.toDate}</td>                                    
                                    <td><a href="editServiceRecord.htm?srid=${srlist.srid}">Edit</a></td>
                                    <td> <a href="deleteServiceRecorddata.htm?srid=${srlist.srid}" onclick="return confirm('Are you sure to Delete Service Record Data?')">Delete</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:form action="NewServiceRecord.htm" method="post" commandName="serviceRecordForm">
                        <button type="submit" class="btn btn-info">Add New Service Record</button>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
