<%-- 
    Document   : NominationRollForGroupD2JuniorClerk
    Created on : 6 Dec, 2021, 1:11:38 PM
    Author     : Manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">     
        <link href="css/bootstrap.min.css" rel="stylesheet"  type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>      
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <title>JSP Page</title>
        <script type="text/javascript">
            $(document).ready(function() {


                $(document).on("click", ".open-submitFormModal", function() {
                    var nominationId = $(this).data('id');
                    $(".modal-body #nominationId").val(nominationId);
                });
            });

            function getNominationRankList(obj) {
                var sltpostName = obj.value;
                if (sltpostName != '') {
                    $('#sltNominationForPost').empty();
                    var url = 'getNominationForRankListJSON.htm?sltpostName=' + sltpostName;
                    $('#sltNominationForPost').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltNominationForPost').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
            }
            function searchNominationList() {
                var fiscalyear = $("#fiscalyear").val();
                var sltNominationForPost = $("#sltNominationForPost").val();
                var sltpostName = $("#sltpostName").val();
                if ($("#sltpostName").val() == "") {
                    alert("please select Current Post");
                    $("#sltpostName").focus();
                    return false;
                }
                if ($("#sltNominationForPost").val() == "") {
                    alert("please select Nomination For Post");
                    $("#sltNominationForPost").focus();
                    return false;
                }
                if ($("#fiscalyear").val() == "") {
                    alert("please select Financial Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                $.post("getsearchNominationForFieldOffice.htm", {sltpostName: sltpostName, sltNominationForPost: sltNominationForPost, fiscalyear: fiscalyear})
                        .done(function(data) {
                            populateDataInGrid(data);
                            $(".newnominationbtn").show();
                        })


            }
            function populateDataInGrid(nominationList) {
                $("#nominationdatagrid").empty();
                for (var i = 0; i < nominationList.length; i++) {

                    var row = '<tr>' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + nominationList[i].createdOn + '</td>' +
                            '<td>' + nominationList[i].sltpostName + '</td>' +
                            '<td>' + nominationList[i].sltNominationForPost + '</td>' +
                            '<td>' + nominationList[i].submittedOn + '</td>' +
                            '<td>' + nominationList[i].submittedToOffice + '</td>';
                    if (nominationList[i].submittedStatusForFieldOffice == "Y") {
                        row = row + '<td> <a href = "viewNominationrollList.htm?nominationMasterId=' + nominationList[i].nominationMasterId + '" title = "Click to View"><i class="fa fa-check-square-o fa-2x"></i></a>' +
                                '</td>';
                    } else {
                        row = row + '<td> <a href="editNominationroll.htm?nominationMasterId=' + nominationList[i].nominationMasterId + '" title="Click to Edit"><i class="fa fa-pencil-square-o  fa-2x"></i></a>' +
                                '</td>';
                    }
                    if (nominationList[i].submittedStatusForFieldOffice != "Y" && nominationList[i].formCompletionStatus == "Y") {
                        row = row + '<td> <a href="javascript:void(0);" data-toggle="modal" data-id=' + nominationList[i].nominationMasterId + ' class="open-submitFormModal" data-target="#submitFormModal" title="Click to Submit"><i class="fa fa-arrow-circle-right  fa-2x"></i></a>' +
                                '</td>';
                    } else if (nominationList[i].submittedStatusForFieldOffice == "Y") {
                        row = row + '<td> <span>Submitted</span>' + '</td>'
                    } else {
                        row = row + '<td> </td>'
                    }
                    row = row + '<td> <a href="downloadNominationFormController.htm?nominationMasterId=' + nominationList[i].nominationMasterId + '&nominationDetailId=' + nominationList[i].nominationDetailId + '" title="Click to Download"><i class="fa fa-file-pdf-o text-red fa-2x"/></i></a>';

                    row + '</tr>';
                    $("#nominationdatagrid").append(row);
                }
            }

            function validateSubmit() {
                if ($("#sltRangeOffice").val() == '') {
                    alert('Please select range Office');
                    return false;
                } else {
                    return true;
                }
            }
        </script>


    </head>
    <body style="background-color:#FFFFFF;margin-top:0px;">
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">

                <form:form action="submitNominationForFieldOffice.htm" commandName="EmpDetNom">

                    <div class="panel panel-default">
                        <h3 style="text-align:center"> Nomination List</h3>

                        <%--<div class="panel-heading">
                            <c:if test="${not empty EmpDetNom.sltNominationForPost}">
                                <a href="submitNominationForFieldOffice.htm"><button class="btn btn-success">New Nomination</button></a>   
                            </c:if>
                        </div> --%>
                        <div class="panel-heading">
                            <input type="submit" name="action" value="New Nomination" class="btn btn-success" onclick="return searchNominationList()"/>                                                          
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="sltpostName"> Select Current rank </label>
                                </div>
                                <div class="col-lg-3" style="text-align: left">
                                    <form:select path="sltpostName" id="sltpostName" class="form-control">
                                        <form:option value="000000"> GROUP D </form:option>
                                        <%-- <form:options items="${currentRankList}" itemLabel="label" itemValue="value"/> --%>
                                    </form:select>
                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="sltNominationForPost">  Select Nomination For </label>
                                </div>
                                <div class="col-lg-3" style="text-align: left">
                                    <form:select path="sltNominationForPost" id="sltNominationForPost" class="form-control">
                                        <form:option value="140161"> JUNIOR CLERK </form:option>
                                        <%-- <form:options items="${newRanklist}" itemLabel="label" itemValue="value"/> --%>
                                    </form:select>
                                </div>
                            </div> 
                            <div class="row">
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="financialyear"> DPC For Year <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3" style="text-align: left">
                                    <form:select path="fiscalyear" id="fiscalyear" class="form-control">
                                        <form:option value="">Year</form:option>
                                        <form:option value="2023">2023</form:option>
                                    </form:select>
                                </div>


                                <div class="col-lg-1" style="text-align: left">
                                    <button type="button" class="form-control btn-primary"  onclick="searchNominationList()">Search</button> <br>
                                    <%--<input type="submit" name="action" value="Search" class="btn btn-primary" onclick="return validate()"/> --%>
                                </div>
                            </div>
                            <div class="clearfix"></div>
                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Created On</th>
                                                <th>Current Rank</th>
                                                <th>Nomination For</th>
                                                <th>Submitted On</th>
                                                <th>Submitted To</th>
                                                <th colspan="3" style="text-align: center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody id="nominationdatagrid"> 
                                            <c:forEach items="${nominationList}" var="obj" varStatus="counter">
                                                <tr>
                                                    <td width="5%">${counter.count}</td>
                                                    <td width="10%">${obj.createdOn}</td>
                                                    <td width="20%">${obj.sltpostName}</td>
                                                    <td width="20%">${obj.sltNominationForPost}</td>
                                                    <td width="10%">${obj.submittedOn}</td>
                                                    <td width="20%">${obj.submittedToOffice}</td>
                                                    <td width="5%" style="text-align: center">
                                                        <c:if test="${obj.submittedStatusForFieldOffice ne 'Y'}">
                                                            <a href="editNominationroll.htm?nominationMasterId=${obj.nominationMasterId}" title="Click to Edit"><i class="fa fa-pencil-square-o  fa-2x"></i>
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${obj.submittedStatusForFieldOffice eq 'Y'}">
                                                            <a href="viewNominationrollList.htm?nominationMasterId=${obj.nominationMasterId}" title="Click to View"><i class="fa fa-check-square-o fa-2x"></i>

                                                            </a>
                                                        </c:if>


                                                    </td>
                                                    <td width="5%" style="text-align: center"> 
                                                        <c:if test="${obj.submittedStatusForFieldOffice ne 'Y' && obj.formCompletionStatus eq 'Y'}">
                                                            <a href="javascript:void(0);" data-toggle="modal" data-id="${obj.nominationMasterId}" class="open-submitFormModal" data-target="#submitFormModal" title="Click to Submit"><i class="fa fa-arrow-circle-right  fa-2x"></i>
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${obj.submittedStatusForFieldOffice eq 'Y'}">
                                                            Submitted
                                                        </c:if>
                                                    </td>
                                                    <td width="5%" style="text-align: center">
                                                        <a href="downloadAnnextureAForDSPRankController.htm?nominationMasterId=${obj.nominationMasterId}" title="Click to Download"><i class="fa fa-file-pdf-o text-red fa-2x"/></i>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>


                        </div>
                        <div class="panel-footer">

                            <input type="submit" name="action" value="New Nomination" class="btn btn-success" onclick="return searchNominationList()"/>   

                        </div>
                        <%-- <div class="panel-footer">
                             <c:if test="${not empty EmpDetNom.sltNominationForPost}">
                                 <a href="submitNominationForFieldOffice.htm"><button class="btn btn-success">New Nomination</button></a>   
                             </c:if>
                         </div> --%>
                    </div>
                    <div class="modal fade" id="submitFormModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <!-- Modal Header -->
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">
                                        <span aria-hidden="true">&times;</span>
                                        <span class="sr-only">Close</span>
                                    </button>
                                    <h4 class="modal-title" id="myModalLabel">
                                        Submit Nomination Form
                                    </h4>
                                </div>

                                <!-- Modal Body -->
                                <div class="modal-body">
                                    <p class="reqStatusMsg"></p>
                                    <form:hidden path="nominationId"/>
                                    <div class="form-group">
                                        <label  class="col-sm-4 control-label" for="reqName">Select Range Office</label>
                                        <div class="col-sm-6">
                                            <form:select path="sltRangeOffice" class="form-control" id="sltRangeOffice">
                                                <form:option value="">--Select One--</form:option>
                                                <form:options items="${rangeOffList}" itemLabel="label" itemValue="value"/>
                                            </form:select>
                                        </div>
                                    </div>



                                </div>

                                <!-- Modal Footer -->
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <input type="submit" name="action" class="btn btn-primary" value="Submit" onclick="return validateSubmit()">
                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <!-- Request Modal -->

    </body>
</html>
