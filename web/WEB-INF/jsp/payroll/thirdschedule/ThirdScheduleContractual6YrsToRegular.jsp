<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            $(document).ready(function() {
                $("#ThirdScheduleModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
            })
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <form:form action="ThirdScheduleContractual6YrsToRegular.htm" method="post" commandName="thirdScheduleForm" class="form-inline">
                        <div class="row">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-8">
                                Form for Fixation of Pay of employees regularized as per the provisions of the Odisha Group "B","C" and Group "D" posts(Repeal and Special Provisions)Rules, 2022
                            </div>
                            <div class="col-lg-2"></div>
                        </div><br />
                        <div class="row">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <label for="sltBillGroup">Select Bill Group</label>
                                <form:select path="sltBillGroup" class="form-control" style="width:70%">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${billGrpList}" itemLabel="billgroupdesc" itemValue="billgroupid"></form:options>
                                </form:select>
                            </div>
                            <div class="col-lg-3">
                                <input type="submit" name="btnSubmit" value="Get Data" class="btn btn-success"/>
                            </div>
                        </div>
                    </form:form>
                </div>
                <div class="panel-body">
                    <table class="table">
                        <thead>
                            <tr style="background-color: blue; color: white;">
                                <td>Sl No</td>
                                <td>HRMS ID</td>
                                <td>GPF No</td>
                                <td>Employee Name</td>
                                <td>Action</td>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty emplist}">
                                <c:forEach items="${emplist}" var="elist" varStatus="count">
                                    <tr>
                                        <td>${count.index + 1}</td>
                                        <td>
                                            <c:out value="${elist.empId}"/>
                                        </td>
                                        <td>
                                            <c:out value="${elist.gpfno}"/>
                                        </td>
                                        <td>
                                            <c:out value="${elist.empname}"/>
                                        </td>
                                        <td>
                                            <a href="GetCont6YrsToRegularThirdScheduleData.htm?billGroupId=${thirdScheduleForm.sltBillGroup}&empid=${elist.empId}" data-remote="false" data-toggle="modal" data-target="#ThirdScheduleModal" class="btn btn-danger">Enter Data</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div id="ThirdScheduleModal" class="modal" role="dialog">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        Form for Fixation of Pay of employees regularized as per the provisions of the Odisha Group "B","C" and Group "D" posts(Repeat and Special Provisions)Rules, 2022
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
