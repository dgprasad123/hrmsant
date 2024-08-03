<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="512kb"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function getAllowanceDeductionData() {
                var url = 'getAllowanceDeductionData.htm?adtype=' + $('#sltAllowanceDeduction').val();
                $('#sltAllowanceDeductionData').empty();
                $('#sltAllowanceDeductionData').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltAllowanceDeductionData').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function checkAll() {
                var allRows = document.getElementsByTagName('input');
                var selectChk = document.getElementById('chkAllEmployee');
                if (selectChk.checked == true) {
                    for (var i = 0; i < allRows.length; i++) {
                        if (allRows[i].type == 'checkbox') {
                            allRows[i].checked = true;
                        }
                    }
                } else {
                    for (var i = 0; i < allRows.length; i++) {
                        if (allRows[i].type == 'checkbox') {
                            allRows[i].checked = false;
                        }
                    }
                }
            }

            function deleteCheck() {
                var checkBoxlength = $("input[name=chkEmployee]:checked").length;
                if (checkBoxlength == 0) {
                    alert("Please select any one of the employee(s).");
                    return false;
                } else {
                    var ret;
                    ret = confirm("Are you sure to delete selected employee(s)?");
                    if (ret == true) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        </script>
    </head>
    <body>
        <form:form action="ManageADEmployeeWise.htm" method="POST" commandName="manageADEmployeeForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row" style="margin-bottom: 7px;margin-top:15px;">
                            <div class="col-lg-3">
                                Select Allowance/Deduction
                            </div>
                            <div class="col-lg-3">   
                                <form:select path="sltAllowanceDeduction" id="sltAllowanceDeduction" class="form-control" onchange="getAllowanceDeductionData();">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="A">Allowance</form:option>
                                    <form:option value="D">Deduction</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-4">
                                <form:select path="sltAllowanceDeductionData" id="sltAllowanceDeductionData" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${adlist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <input type="submit" name="btnManageADEmployee" value="Get List" class="btn btn-primary"/>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div style="height:500px;overflow: auto;">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th width="10%" style="text-align: center;">Sl No</th>
                                        <th width="10%" style="text-align: center;">
                                            <input type="checkbox" id="chkAllEmployee" value="0" onclick="checkAll()"/>
                                        </th>
                                        <th width="60%" style="text-align: center;">Name</th>
                                        <th width="20%" style="text-align: center;">Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty emplist}">
                                        <c:forEach items="${emplist}" var="list" varStatus="count">
                                            <tr>
                                                <td>
                                                    ${count.index + 1}
                                                </td>
                                                <td style="text-align: center;">
                                                    <%--<form:checkbox path="chkEmployee" value="${list.empid}"/>--%>
                                                    <input type="checkbox" name="chkEmployee" value="${list.empid}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${list.empname}"/>
                                                </td>
                                                <td style="text-align: right;">
                                                    <c:out value="${list.amt}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnManageADEmployee" value="Delete" class="btn btn-danger" onclick="return deleteCheck();"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
