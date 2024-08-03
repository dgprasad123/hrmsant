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
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                if ($("#quarterunitarea").val() != "") {
                    showQuarterType();

                }
            });

            function showQuarterType() {
                var qrtrunit = $("#qrtrunit").val();
                $('#qrtrtype').empty();
                $.post("unitWiseQuarterTypeDataJson.htm", {quarterunitarea: qrtrunit})
                        .done(function (data) {
                            var unitAreaList = data.unitAreaList;

                            $.each(unitAreaList, function (i, obj) {
                                $('#qrtrtype').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                            $("#qrtrtype").val($("#tqrtrtype").val());
                        })
            }
            function validate() {
                if ($("#quarterunitarea").val() == "") {
                    alert("Choose Unit/Area");
                    return false;
                }
            }
            function EmployeeList() {
                window.location = "getTransferList.htm";
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">
                        <input type="hidden" name="tqrtrtype" id="tqrtrtype" value="${waterRent.qrtrtype}"/>
                        <form:form action="quarterMaster.htm" method="POST" commandName="empQuarterBean">
                            <table border="0" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">                            
                                <tr style="height:40px;">
                                    <td align="center">
                                        <label>Unit/Area</label>
                                    </td>
                                    <td>
                                        <form:select path="qrtrunit" cssClass="form-control" onchange="showQuarterType()">
                                            <form:option value="">All</form:option>
                                            <c:forEach items="${quarterUnitAreaList}" var="quarterUnitArea">
                                                <form:option value="${quarterUnitArea.value}">${quarterUnitArea.label}</form:option>
                                            </c:forEach>
                                        </form:select>                                                                                                             
                                    </td>
                                    <td align="center">
                                        <label>Quarter Type</label>
                                    </td>
                                    <td>
                                        <form:select path="qrtrtype" cssClass="form-control">
                                            <form:option value="">All</form:option>
                                            <c:forEach items="${unitAreaList}" var="unitArea">
                                                <form:option value="${unitArea.value}">${unitArea.label}</form:option>
                                            </c:forEach>                                            
                                        </form:select>
                                    </td>
                                    <td>
                                        <div class="btn-group" style="padding-left: 50px;">
                                            <button type="submit" value="Search" name="action" class="btn btn-primary" onclick="return validate()"><span class="glyphicon glyphicon-search"></span> Search</button>                                            

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
                                            <th>Employee Name</th>
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th>                                                                                        
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody id="wrrgrid">
                                        <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">

                                            <tr <c:if test="${quarter.empId eq quarter.transferId}"> style="background: red;color:white" </c:if> >
                                                <td>${cnt.index+1}</td> 
                                                <td>${quarter.consumerNo}</td>
                                                <td>${quarter.empId}</td>                                                
                                                <td>${quarter.empName}</td>
                                                <td>${quarter.quarterNo}</td>                                                                                                
                                                <td>${quarter.qrtrtype}</td>
                                                <td>${quarter.qrtrunit}</td>                                                                                                                                               
                                                <th>
                                                    <c:if test="${not empty quarter.empId}">
                                                        <a href="viewAllotmentPage.htm?qaId=${quarter.qaId}" target="_blank">View</a> | 
                                                    </c:if>
                                                     <a href="editAllotment.htm?qaId=${quarter.qaId}">Edit</a>
                                                    &nbsp;                                                  
                                                </th>
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
