<%-- 
    Document   : ParCPromotionReport
    Created on : May 28, 2020, 12:53:17 PM
    Author     : manisha
--%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function () {
                $("#offcode").change(function () {
                    var url = 'getApprovingSPCOfficeWiseListJSON.htm?offcode=' + this.value;
                    $.getJSON(url, function (result) {
                        $('#postCode').empty();
                        $.each(result, function (i, field) {
                            $('#postCode').append($('<option>', {
                                value: field.spc + ',' + field.empid,
                                text: field.postname
                            }));
                        });
                    });
                });


                $(document).ready(function () {
                    $(".iframe").colorbox({iframe: true, width: "80%", height: "80%"});
                });
            });
            function openaddGroupCWindow() {
                $('#empDetail').modal('show');
            }
            function openAuthorityListWindow(recommendationId) {
                var url = 'checkRecommendationData.htm';
                $.post(url, {recommendationId: recommendationId})
                        .done(function (data) {                            
                            if (data.dataIntegrity == 'Y') {
                                $("#recommendationId").val(recommendationId)
                                $('#setAuthority').modal('show');
                            }else{
                                alert("Mandatory Fields are not filled up");
                            }
                        });

            }

            function openEmployeeListWindow() {
                $('#setEmployee').modal('show');
            }
            function openaddNewRecommendation() {
                $('#empRecommendations').modal('show');
            }
            function validate() {
                return true;
            }
            function showEmployeeList() {
                var offCode = $("#offCode").val();
                var url = 'getOfficeEmployeeList.htm?offcode=' + offCode;
                $.getJSON(url, function (result) {
                    $('#empdatatable').empty();
                    $.each(result, function (i, field) {
                        tPost = "";
                        if (field.post) {
                            tPost = field.post;
                        }
                        $('#empdatatable').append('<tr><td>' + field.fullname + '</td><td>' + tPost + '</td><td><input type="button" value="Add"/></td></tr>');
                    });
                });
            }
            function submitToDepartment(me, recommendationId) {
                var url = 'submitRecommendationListToDepartment.htm';
                if (confirm('Are you sure you want to submit?')) {
                    $.post(url, {recommendationId: recommendationId})
                            .done(function (data) {
                                //if (data.msg == "Y") {
                                $(me).parent().html(data.msg);
                                //} else {
                                //alert("Some Error Occured");
                                //}
                            });
                }
            }
            function validateForm() {
                if ($("#offcode").val() == "") {
                    alert("Please Select Office");
                    $("#offcode").focus();
                    return false;
                }
                if ($("#postCode").val() == "") {
                    alert("Please Select Substantive Post");
                    $("#postCode").focus();
                    return false;
                }

                return confirm('Are you sure you want to submit?')
            }
        </script>
    </head>
    <body>
        <form:form action="createNewRecommendationList.htm" method="post" commandName="recommendationDetailBean" class="form-horizontal">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Nomination
                    </div>
                    <c:if test="${not empty recommendationList}">                    
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>                                                                
                                        <th width="2%">#</th>
                                        <th width="15%">Created By (Authority with Designation)</th>
                                        <th width="8%">Created On Date</th>
                                        <th width="8%">Submitted On Date</th>
                                        <th width="8%">Nomination Type</th>
                                        <th width="8%">For The Year</th>
                                        <th width="12%">Action</th>
                                        <th width="10%">Sent to</th>                                    
                                    </tr>                            
                                </thead>
                                <tbody>
                                    <c:forEach items="${recommendationList}" var="recommendationBean" varStatus="count">
                                        <tr>
                                            <td>${count.index+1}</td>
                                            <td>${recommendationBean.initiatedByempname},${recommendationBean.initiatedBypost}</td>
                                            <td>${recommendationBean.createdondate}</td>
                                            <td>${recommendationBean.submittedondate}</td>
                                            <td style="text-transform: capitalize;">${recommendationBean.recommenadationType}</td>     
                                            <td>${recommendationBean.foryear}</td>
                                            <td>
                                                <a href="viewRecommendedEmployeeList.htm?recommendationId=${recommendationBean.recommendationId}&recommenadationType=${recommendationBean.recommenadationType}" class="btn-default">
                                                    <button type="button" class="btn btn-primary">Detail <span class="badge">${recommendationBean.noofnominations}</span></button>
                                                </a>
                                                <c:if  test="${recommendationBean.taskId eq '0' && empty recommendationBean.isSubmittedToDept}">
                                                    <a href="recommendationEmployeeList.htm?recommendationId=${recommendationBean.recommendationId}" class="btn-default">
                                                        <button type="button" class="btn btn-default">Add Employee</button>
                                                    </a>
                                                </c:if>                                                                                      
                                            </td>                                        
                                            <td>
                                                <c:if test="${office.lvl eq '02' || office.category eq 'DISTRICT COLLECTORATE'}">
                                                    <c:if  test="${recommendationBean.taskId eq '0' && recommendationBean.isSubmittedToDept ne 'Y'}">
                                                        <button type="button" class="btn btn-primary" onclick="submitToDepartment(this,${recommendationBean.recommendationId})">Submit To Department</button>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${office.lvl eq '01'}">
                                                    <c:if  test="${recommendationBean.taskId eq 0 && recommendationBean.isSubmittedToDept ne 'Y'}">
                                                        <button type="button" class="btn btn-primary" onclick="submitToDepartment(this,${recommendationBean.recommendationId})">Submit</button>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${office.lvl ne '01' && office.lvl ne '02' && office.category ne 'DISTRICT COLLECTORATE'}">
                                                    <c:if  test="${recommendationBean.taskId eq 0}">
                                                        <button type="button" class="btn btn-primary" onclick="openAuthorityListWindow(${recommendationBean.recommendationId})">Choose Authority</button>
                                                    </c:if>
                                                </c:if>
                                                <c:if  test="${office.lvl ne '01' && office.lvl != '02' && office.category != 'DISTRICT COLLECTORATE'}">
                                                    <c:if  test="${recommendationBean.taskId gt 0}">
                                                        Submitted 
                                                    </c:if>
                                                </c:if>

                                                <c:if test="${office.lvl eq '01' || office.lvl eq '02' || office.category eq 'DISTRICT COLLECTORATE'}">
                                                    <c:if  test="${recommendationBean.isSubmittedToDept eq 'Y'}">
                                                        Submitted to Department
                                                    </c:if>
                                                </c:if>

                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>

                    <div class="panel-footer">                    
                        <div class="btn-group">
                            <c:if test="${office.lvl eq '01' || office.lvl eq '02' || office.category eq 'DISTRICT COLLECTORATE'}">
                                <button type="button" class="btn btn-primary" onclick="openaddNewRecommendation()">Do You want to Add more Nomination ?</button> 
                            </c:if>                            

                            <c:if test="${office.lvl ne '01' && office.lvl ne '02' && office.category ne 'DISTRICT COLLECTORATE'}">
                                <button type="button" class="btn btn-primary" onclick="openaddNewRecommendation()">Add New</button> 
                            </c:if>

                        </div>
                    </div>

                </div>
            </div>


            <!-- modal for save Authority-->
            <div id="setAuthority" class="modal fade" role="dialog">
                <div class="modal-dialog  modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Choose Office Name and Authority</h4>
                        </div>
                        <div class="modal-body"> 

                            <div class="form-group">
                                <label class="control-label col-lg-3">Office Name</label>
                                <div class="col-lg-8">                                                                       
                                    <select class="form-control" name="authoffcode" id="offcode">
                                        <option value="">Select</option> 
                                        <c:forEach items="${offlist}" var="office">
                                            <option value="${office.offCode}">${office.offName}</option>
                                        </c:forEach> 
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-lg-3">Substantive Post:</label>
                                <div class="col-lg-8">
                                    <select class="form-control" name="authspc" id="postCode">
                                        <option value="">Select</option>                                            
                                    </select>
                                </div>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <form:hidden path="recommendationId"/> 
                            <input type="submit" class="btn btn-primary" name="action" value="Submit" onclick="return validateForm()"/>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>



            <!-- modal for Assign New Recommendation-->

            <div id="empRecommendations" class="modal fade" role="dialog">
                <div class="modal-dialog  modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Choose Any Of the One Option</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label class="control-label col-sm-4">Choose Nomination Type </label>
                                <div class="col-sm-6">

                                    <select name="recommenadationType" id="recommenadationType" class="form-control">
                                        <option value="within batch promotion">Out Of turn Promotion(within the batch)</option>
                                        <option value="across batch promotion">Out Of turn Promotion(across the batches)</option>  
                                        <option value="incentives award">Award Of Incentives</option>
                                        <option value="Premature Retirement">Premature Retirement</option>
                                    </select>  

                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">

                            <div class="col-sm-10" >
                                <input type="submit" class="btn btn-primary" name="action" value="Create"/> 
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </form:form>
    </body>
</html>
