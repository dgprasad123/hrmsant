<%-- 
    Document   : BEmployeeList
    Created on : 11 Sep, 2023, 4:09:16 PM
    Author     : Hp
--%>

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

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
       
            function enableActivate(empid) {
                $("#emptype_" + empid).show();
            }

            function saveEmployee(empid) {
                var employeetype = $("#sltEmpType_" + empid).val();
                //alert("selectval is: " + selectval);
                $.ajax({
                    type: 'GET',
                    url: "saveEmployeeSanitize.htm?empid=" + empid + "&employeetype=" + employeetype,
                    success: function(data) {
                        alert("Employee type has been succesfully changed.");
                    }
                });
            }
            function changeStatus(empid, cStatus) {
                empName = $("#name_"+empid).text();
                if (cStatus == 'Y')
                {
                    str = "Are you sure you want to activate the Employee?"
                }
                if (cStatus == 'N')
                {
                    str = 'The HRMS ID ' + empid + ' of ' + empName + ' showing against your office will be deactivated. Please ensure that He/She is no longer belongs to your present office before deactivating.';
                }
                if (confirm(str))
                {
                    //alert("selectval is: " + selectval);
                    $("#loader_" + empid).css('display', 'block');
                    $.ajax({
                        type: 'GET',
                        url: "changeEmployeeStatus.htm?empid=" + empid + "&status=" + cStatus,
                        success: function(data) {
                            if (cStatus == 'Y')
                            {
                                $('#status_cont_' + empid).html('<span style="color:#007800;font-weight:bold;">Active</span>');
                                $('#isactive_' + empid).html('<button type="button" class="btn btn-danger" onclick="changeStatus(\'' + empid + '\',\'N\');">Deactivate</button>');
                                $("#emptype_" + empid).css('display', 'block');
                            }
                            if (cStatus == 'N')
                            {
                                // alert('<button type="button" class="btn btn-danger" onclick="changeStatus(\''+empid+'\',\'N\');">Deactivate</button>');
                                $('#status_cont_' + empid).html('<span style="color:#ff0000;font-weight:bold;">Inactive</span>');
                                $('#isactive_' + empid).html('<button type="button" class="btn btn-success" onclick="changeStatus(\'' + empid + '\',\'Y\');">Activate</button>');
                                $("#emptype_" + empid).css('display', 'none');
                            }
                            $("#loader_" + empid).css('display', 'none');
                        }
                    });
                }
            }
        </script>

    </head>
    <body style ="margin:10px;">
        <form:form action="EmpSantizeAdd.htm" method="POST" commandName="santizeModel">
            <!--Add List here-->
            <div class="container-fluid">
                <!-- Body-->
                <div class="panel panel-default">

                    <!-- Header-->
                    <div class="panel-heading">
                        Employee Sanitize
                    </div>  
                    <!-- Header End-->

                    <!--Inside body-->
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="20%">HRMS Id/Full Name</th>
                                    <th>Designation</th>
                                    <th>Mobile</th>

                                    <th  width="7%" style="text-align:center">Status</th>
                                    <th  width="7%" style="text-align:center">Action</th>
                                    <th  width="18%" style="text-align:center">Employee Type</th>

                                </tr>
                            </thead>
                            <tbody>

                                <c:forEach items="${emplist}" var="slist">
                                    <tr>
                                        <td><strong>${slist.empId}</strong><br /><span id="name_${slist.empId}">${slist.empFullName}</span></td>


                                        <td>${slist.designation}</td>
                                        <td>${slist.mobile}</td>

                                        <td style="text-align:center">
                                            <div id="status_cont_${slist.empId}">
                                                <c:if test="${slist.isActive eq 'Y'}"><span style="color:#007800;font-weight:bold;">Active</span> 
                                                </c:if>
                                                <c:if test="${slist.isActive eq 'N'}"><span style="color:#ff0000;font-weight:bold;">Inactive</span></c:if>
                                                </div>




                                            </td>
                                            <td>
                                                <div id="isactive_${slist.empId}">
                                                <span id="loader_${slist.empId}" style="display:none"><img src="images/loading.gif" /></span>
                                                <c:if test="${slist.isActive eq 'N'}"><button type="button" class="btn btn-success" onclick="changeStatus('${slist.empId}', 'Y');">Activate</button>  
                                                </c:if>
                                                <c:if test="${slist.isActive eq 'Y'}"><button type="button" class="btn btn-danger" onclick="changeStatus('${slist.empId}', 'N');">Deactivate</button></c:if> 
                                                </div>
                                            </td>
                                            <td>
                                                <div id="emptype_${slist.empId}" <c:if test="${slist.isActive eq 'N'}">style="display:none"</c:if>>
                                                <select name="sltEmpType" id="sltEmpType_${slist.empId}" class="form-control" style="float:left;width:200px">
                                                    <option value="">--Select--</option>
                                                    <c:forEach items="${empTypeList}" var="tlist">
                                                        <option value="${tlist.typeid}" <c:if test="${slist.employeeType eq tlist.typeid}">selected="selected"</c:if>>${tlist.empType}</option>
                                                    </c:forEach>
                                                </select>&nbsp;

                                                <button type="button" class="btn btn-primary" onclick="saveEmployee('${slist.empId}');" value="Save" size="10" />Save</button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </tbody>


                        </table>
                    </div>
                </div>
            </div>


        </form:form>      
    </body>
</html>



