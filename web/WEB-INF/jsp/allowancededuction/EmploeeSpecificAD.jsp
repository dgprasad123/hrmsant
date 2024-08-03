<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            function validateForm(){
                if($("#adtype").val() == ""){
                    alert("Please Select Allowance/Deduction");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-4">&nbsp;</div>
                        <div class="col-lg-4">
                            <form:form class="form-inline" action="employeeWiseUpdatedADAction.htm" method="POST" commandName="command">
                                <div class="form-group">
                                    <label for="billType">Allowance /Deduction:</label>
                                    <form:select path="adtype" id="adtype" class="form-control">
                                        <form:option value="">Select</form:option>
                                        <form:option value="A">Allowance</form:option>
                                        <form:option value="D">Deduction</form:option>
                                        <form:option value="P">Pvt. Deduction</form:option>
                                    </form:select>
                                </div>
                                <button type="submit" name="Ok" class="btn btn-default" onclick="return validateForm()">Ok</button>
                            </form:form>
                        </div>
                        <div class="col-lg-4">&nbsp;</div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="5%">Sl No</th>
                                <th width="15%">NAME OF THE HEAD</th>
                                <th width="10%">BT/OBJECT HEAD</th>
                                <th width="35%">FORMULA</th>
                                <th width="5%">FIXED VALUE</th>
                                <th width="5%">Edit</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${adlist}" var="ad" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${ad.addesc} <span style="color: #00cccc">(${ad.adcodename})</span></td>
                                    <td>${ad.head}</td>
                                    <td>${ad.formula}</td>
                                    <td>${ad.advalue}</td>
                                    <td><a href="allowanceAndDeductionEdit.htm?adcode=${ad.adcode}&whereupdated=${command.whereupdated}">Edit</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>
    </body>
</html>
