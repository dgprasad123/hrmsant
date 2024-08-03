<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Set Additional Charge For Office Details</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            function getDeptWiseOfficeList(type) {
                var deptcode = $('#hidAuthDeptCode').val();

                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                $('#hidAuthOffCode').empty();
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {

                var offcode = $('#hidAuthOffCode').val();
                $('#authSpc').empty();

                var url = 'getPostCodeListJSON.htm?offcode=' + offcode;
                $('#authSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getPost() {
                $('#authPostName').val($('#authSpc option:selected').text());
            }
        </script>
    </head>
    <body>
    <form:form action="saveAdditionalChargeForOfficeDetails.htm" method="POST" commandName="additionChargeForm">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">

                </div>
                <div class="panel-body">
                    <div class="modal-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltDept">Department</label>
                            </div>
                            <div class="col-lg-9">
                                <form:select path="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                    <form:option value="">--Select Department--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Additional Charge Office</label>
                            </div>
                            <div class="col-lg-9">
                                <form:select path="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                    <form:option value="">--Select Office--</form:option>
                                    <form:options items="${sancOfflist}" itemValue="offCode" itemLabel="offName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Additional Charge Post</label>
                            </div>
                            <div class="col-lg-9">
                                <form:select path="authSpc" id="authSpc" class="form-control">
                                    <form:option value="">--Select Post--</form:option>
                                    <form:options items="${sancPostlist}" itemValue="spc" itemLabel="postname"/>
                                </form:select>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Provide HRMS ID of DDO</label>
                            </div>
                            <div class="col-lg-3">
                                <form:input path="empid" maxlength="8" class="form-control"/>
                            </div>
                            <div class="col-lg-9">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" name="submit" value="Save" class="btn btn-default" onclick="return confirm('Are you sure to Update?');">Save</button>
                    </div>
                </div>
            </div>
    </form:form>
</body>
</html>
