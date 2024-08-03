<%@ page contentType="text/html;charset=windows-1252" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>        
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $("#deptName").change(function() {
                    $('#offcode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;

                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));

                        });
                    });
                    //alert($('#hidUserype').val());
                });
                $('.openPopup').on('click', function() {
                    var dataURL = $(this).attr('data-href');
                    $('.modal-body').load(dataURL, function() {
                        $('#myModal').modal({show: true});
                    });
                });

            });

            function showRankList() {
                if ($("#criteria").val() == "FNAME") {
                    $("#sltRank").show();
                } else {
                    $("#sltRank").hide();
                    $("#sltRank").val('');
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="LocatePoliceEmployee.htm" commandName="searchPolice" method="POST" autocomplete="off">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-sm-2">
                                    <label class="control-label">Search criteria</label>
                                    <form:select class="form-control" path="criteria" id="criteria" onchange="showRankList();">
                                        <form:option value="">Select</form:option>
                                        <form:option value="GPFNO">GPF NO</form:option>
                                        <form:option value="HRMSID">HRMS ID</form:option>
                                        <form:option value="FNAME">FIRST NAME</form:option>
                                        <form:option value="MOBILE">Mobile Number</form:option>
                                    </form:select>&nbsp;
                                    <c:if test="${empty searchPolice.sltRank}">
                                        <form:select class="form-control" path="sltRank" id="sltRank" style="display:none;">
                                            <form:option value="">Select</form:option>
                                            <form:options items="${postlist}" itemValue="postcode" itemLabel="post"/>
                                        </form:select>
                                    </c:if>
                                    <c:if test="${not empty searchPolice.sltRank}">
                                        <form:select class="form-control" path="sltRank" id="sltRank">
                                            <form:option value="">Select</form:option>
                                            <form:options items="${postlist}" itemValue="postcode" itemLabel="post"/>
                                        </form:select>
                                    </c:if>
                                </div>
                                <div class="col-sm-4">
                                    <label class="control-label">Search String</label>
                                    <form:input path="searchString" class="form-control"/>
                                </div>
                                <div class="col-sm-3" style="padding-top:20px;">
                                    <input type="submit" value="Search" class="btn btn-danger"/>
                                </div>
                                <div class="col-sm-3"></div>
                            </div>
                        </div>

                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="5%">SL No</th>
                                            <th width="13%">HRMS ID/GPF No</th>
                                            <th width="18%">Employee Name</th>
                                            <th width="8%">Date of Birth</th>
                                            <th width="25%">Current Post</th>
                                            <th width="10%">Date of Joining<br />in Government</th>
                                            <th width="10%">View Posting Details</th>
                                            <th width="11%"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${empSearchResult.employeeList}" var="employee" varStatus="counter">
                                            <tr>
                                                <td>${counter.count}</td>
                                                <td>${employee.empid}<br />${employee.gpfno}</td>
                                                <td>${employee.fname} ${employee.mname} ${employee.lname}</td>
                                                <td>${employee.dob}</td>                                
                                                <td>${employee.post}</td>
                                                <td>${employee.doeGov}</td>
                                                <td>
                                                    <!--<a href="javascript:void(0);" data-href="PolicePostingDetails.htm?empid=${employee.empid}" target="_blank" class="label label-success">View</a>-->
                                                    <a href="PolicePostingDetails.htm?empid=${employee.empid}" target="_blank" class="label label-success">View</a>
                                                </td>  
                                                <td></td>                                           
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
            <div class="modal fade" id="myModal" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Posting Details</h4>
                        </div>
                        <div class="modal-body"></div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>  
    </body>
</html>


