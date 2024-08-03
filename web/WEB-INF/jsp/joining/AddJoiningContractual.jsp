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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function validateJoining() {

                var result = confirm("Are you sure to Join employee ?");

                if (result) {
                    if ($('#joiningOrdNo').val() == '') {
                        alert("Please Enter Order No");
                        $('#joiningOrdNo').focus();
                        return false;
                    }
                    if ($('#joiningOrdDt').val() == '') {
                        alert("Please Enter Order Date");
                        $('#joiningOrdDt').focus();
                        return false;
                    }

                    if ($('#hidPostNomenclature').val() == '') {
                        alert("Please Enter Post");
                        $('#hidPostNomenclature').focus();
                        return false;
                    }
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="saveEmployeeJoiningContractual.htm" method="post" commandName="jForm">
            <form:hidden path="hidempid"/>
            <form:hidden path="hidjoinId"/>
            <form:hidden path="hidPrimaryTransferId"/>
            <form:hidden path="hidForeignTransferId"/>

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Joining
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="joiningOrdNo">Order No.</label><span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">
                                <form:input path="joiningOrdNo" id="joiningOrdNo" class="form-control"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="joiningOrdDt">Order Date</label><span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">
                                <div class="input-group date" id="processDate">
                                    <form:input path="joiningOrdDt" id="joiningOrdDt" class="form-control txtDate"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="hidPostNomenclature">Joining POST</label>
                            </div>
                            <div class="col-lg-9">
                                <form:input path="hidPostNomenclature" id="hidPostNomenclature" />

                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                    </div>

                    <div class="panel-footer">
                        <c:if test="${empty jForm.hidjoinId}">
                            <input type="submit" name="action" value="Join" class="btn btn-default" onclick="return validateJoining();"/>
                        </c:if>
                        <c:if test="${not empty jForm.hidjoinId}">
                            <input type="submit" name="action" value="Update" class="btn btn-default" onclick="return validateJoining();"/>
                            <%--<input type="button" name="action" value="Delete" class="btn btn-default"/>--%>
                        </c:if>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
