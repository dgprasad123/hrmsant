<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link href="css/sb-admin.css" rel="stylesheet">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $("#coModal").on("show.bs.modal", function (e) {
                    $('#deptCode').val('');
                    $('#poffCode').val('');
                });
            });

            function validateSearch() {
                if ($('#offCode').val() == "") {
                    alert("Please enter Office Code");
                    return false;
                }else if($('#offCode').val() != ""){
                    if($('#offCode').val().length < 13 || $('#offCode').val().length > 13){
                        alert("Office Code must be 13 digits");
                        return false;
                    }
                }
            }
            function validateAdd() {
                if ($('#poffCode').val() == "") {
                    alert("Please select Controlling Office");
                    return false;
                }

                if (confirm("Are you sure to Add?")) {
                    return true;
                } else {
                    return false;
                }
            }
            function getDepartmentWiseCOOfficeList() {
                var url = 'getControllingOfficelistToADD.htm?deptcode=' + $('#deptCode').val();
                $('#poffCode').empty();
                $('#poffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#poffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <form:form action="ControllingOfficeData.htm" method="POST" commandName="office">

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="row" style="margin-top:30px;margin-bottom: 20px;">
                                    <div class="col-lg-12">
                                        <span style="color: #F00000;font-size: 20px;display:block;text-align: center;">
                                            ADD NEW CONTROLLING OFFICE 
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-top:30px;margin-bottom: 20px;">
                                    <div class="col-lg-3"></div>
                                    <div class="col-lg-2">
                                        ENTER OFFICE CODE
                                    </div>
                                    <div class="col-lg-3">
                                        <form:input path="offCode" id="offCode" class="form-control"/>
                                    </div>
                                    <div class="col-lg-3">
                                        <input type="submit" name="btnSubmit" value="Search" class="btn btn-info" onclick="return validateSearch();"/>
                                    </div>
                                    <div class="col-lg-1"></div>
                                </div>
                                <div style="text-align: center;">
                                    <c:if test="${not empty retVal && retVal > 0}">
                                        <span style="color: green; font-weight: bold; font-size: 16px;">
                                            <c:out value="Controlling Office Added"/>
                                        </span>
                                    </c:if>
                                </div>
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th width="15%">SL No</th>
                                            <th width="20%">CO Name</th>
                                            <th width="20%">CO Code</th>
                                        </tr>
                                    </thead>
                                    <c:if test="${not empty colist}">
                                        <c:forEach items="${colist}" var="list" varStatus="cnt">
                                            <c:if test="${list.value eq 'ERROR'}">
                                                <tr>
                                                    <td colspan="3" align="center">
                                                        <span style="color: red; font-weight: bold; font-size: 16px;">
                                                            <c:out value="${list.label}"/>
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:if>
                                            <c:if test="${list.value ne 'ERROR'}">
                                                <tr>
                                                    <td>
                                                        ${cnt.index + 1}
                                                    </td>
                                                    <td><c:out value="${list.label}"/></td>
                                                    <td><c:out value="${list.value}"/></td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </table>
                            </div>
                            <div class="panel-footer">
                                <c:if test="${not empty office.offCode}">
                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#coModal">
                                        <span class="glyphicon glyphicon-plus"></span> Add
                                    </button>
                                </c:if>
                            </div>
                        </div>
                        <div id="coModal" class="modal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Select Controlling Office</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="deptcode">Department</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="deptCode" id="deptCode" class="form-control" onchange="getDepartmentWiseCOOfficeList();">
                                                    <option value="">Select Department</option>
                                                    <form:options items="${departmentList}" itemValue="deptCode" itemLabel="deptName"/>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1"></div>
                                        </div>

                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="office">Office</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="poffCode" id="poffCode" class="form-control">
                                                    <option value="">--Select Office--</option>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1"></div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <input type="submit" name="btnSubmit" value="Add" class="btn btn-info" onclick="return validateAdd();"/>
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
