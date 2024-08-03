
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function getEmployee() {
                var deptName = $('#deptname').val();
                var offName = $('#offCode').val();
                var postName = $('#postCode').val();
                if (deptName == '') {
                    alert("Please select a Department");
                    return false;
                }
                if (offName == '') {
                    alert("Please select an Office");
                    return false;
                }
                if (postName == '') {
                    alert("Please select a Post");
                    return false;
                }
                $('#employeepost').empty();
                var url = 'getEmployeeNameWithSPCJSON.htm?offcode=' + offName + '&postCode='+postName;
                $.getJSON(url, function (result) {
                    $.each(result, function (i, field) {
                        var trRow = '<tr>';
                            trRow = trRow + '<td><input type="radio" name="applyTo" value="'+field.empid+"-"+field.spc+'"></td>';
                            trRow = trRow + '<td>'+field.empname+'</td>';
                            trRow = trRow + '<td>'+field.spn+'</td>';
                            trRow = trRow + '</tr>';
                        $('#employeepost').append(trRow);                        
                    });
                });

            }
            $(document).ready(function () {

                $("#deptCode").change(function () {
                    $('#offCode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, field) {
                            $('#offCode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                    var url = 'getDeptWisePostListJSON.htm?deptCode=' + this.value;
                    $.getJSON(url, function (result) {
                        $('#postCode').empty();
                        $.each(result, function (i, field) {
                            $('#postCode').append($('<option>', {
                                value: field.postcode,
                                text: field.post
                            }));
                        });
                    });
                });


            });

        </script>
    </head>
    <body>
        <form:form action="submitPostProposal.htm" method="POST" class="form-horizontal" commandName="postProposal">
            <form:hidden path="porposalId"/>
            <div class="panel panel-default">
                <div class="panel-heading">Forward Proposal</div>
                <div class="panel-body">
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">Department:</label>
                        <div class="col-sm-10">
                            <form:select path="deptCode" id="deptCode" cssClass="form-control" style="width:450px;">
                                <form:option value="">Select</form:option>
                                <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                    
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">Office:</label>
                        <div class="col-sm-10">
                            <form:select path="offCode" id="offCode" cssClass="form-control" style="width:450px;">
                                <form:option value="">Select</form:option>
                                <form:options items="${officeList}" itemLabel="offName" itemValue="offCode"/>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">Post:</label>
                        <div class="col-sm-10">
                            <form:select path="postCode" id="postCode" cssClass="form-control" style="width:450px;"> 
                                <form:option value="">Select</form:option>
                                <form:options items="${postList}" itemLabel="post" itemValue="postcode"/>
                            </form:select>
                        </div>
                    </div>
                </div>
                <div class="panel-footer"><button type="button" class="btn btn-default" onClick="getEmployee()">Search</button></div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">AUTHORITY LIST</div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="5%">Select</th>
                                <th><span style="font-weight: bold;">NAME</span></th>
                                <th width="45%">Post</th>
                            </tr>
                        </thead>
                        <tbody id="employeepost">                                        
                            
                        </tbody>

                    </table>
                </div>

                <div class="panel-footer">

                    <input type="submit" name="action" value="Back"  class="btn btn-default"/>

                    <input type="submit" name="action" value="Submit" class="btn btn-default" onclick="return confirm('Are you sure to Submit?')"/>


                </div>
            </div>
        </form:form>
    </body>
</html>
