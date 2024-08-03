<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function openPartAModal(mode, gpc, payscale_6th, level, post_group, gp) {
                //alert("Mode is: "+mode);

                $('#gpc').val(gpc);
                $('#mode').val(mode);
                $('#scaleofPay').val(payscale_6th);
                $('#postgrp').val(post_group);
                $('#hidPostGrp').val(post_group);
                $('#paylevel').val(level);
                $('#gp').val(gp);

                $('#substantivePostPartAModal').modal("show");
            }

            function openPartCModal(mode, gpc) {
                $('#gpc').val(gpc);
                $('#mode').val(mode);
                //alert("GPC is: "+$('#gpc').val());
                $('#substantivePostPartAModal').modal("show");
            }

            function onlyIntegerRange(e)
            {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }
        </script>
        <style type="text/css">
            .saveSuccess{
                color: #00cc66;
                font-weight: bold;
            }
            .saveError{
                color: #ff3333;
                font-weight: bold;
            }
            .row{
                margin-left:0px;
                margin-right:0px;
            }
        </style>
    </head>
    <body>

        <form:form action="ScheduleIChangeAtReviewer.htm" method="POST" commandName="command">
            <form:hidden path="aerId" id="aerId"/>
            <form:hidden path="gpc" id="gpc"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-footer">
                        <button type="submit" name="btnAer" value="Back" class="btn btn-primary">Back to List</button>
                        <c:if test="${Editable eq 'N'}">
                            <a href="submitEstablishmentReport.htm?aerId=${command.aerId}" class="btn btn-primary" role="button" onclick="return confirm('Are you sure to Submit AER ?')">Submit to Approver</a>
                            (You can Set Remarks by click on Remark link provided against each Post)
                        </c:if>  

                    </div>
                    <div class="panel-body">
                        <div class="jumbotron">
                            <h1>Part-A (Regular Establishment)</h1>
                        </div>

                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>
                                    <th width="10%" rowspan="2" >Category of Employee</th>
                                    <th width="25%" rowspan="2">Description of the Posts</th>
                                    <th width="25%" colspan="2">Pay Scale</th>
                                    <th  width="5%" rowspan="2">As per 7th Pay Commission</th>
                                    <th width="8%" rowspan="2">Sanctioned Strength</th>
                                    <th width="8%" rowspan="2">Persons-in-Position</th>
                                    <th width="9%" rowspan="2">Vacancy Position </th>
                                    <th width="5%" rowspan="2"> Remarks if any  </th>
                                    <th width="5%" rowspan="2"> Action </th>
                                </tr>
                                <tr>
                                    <th>As per 6th Pay Commission</th>
                                    <th>GP</th>
                                </tr>
                            </thead>
                            <tr>
                                <td>  <h4>Group-A </h4></td>
                                <td colspan="8"> <h4>Other than UGC /Judiciary/ All India Services </h4></td>
                            </tr>
                            <c:forEach var="partAGrpA" items="${PartAGroupAlist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpA.gpc} </td>
                                    <td> ${partAGrpA.scaleofPay} </td>
                                    <td> ${partAGrpA.gp} </td>
                                    <td> ${partAGrpA.scaleofPay7th} </td>
                                    <td> ${partAGrpA.sanctionedStrength} </td>
                                    <td> ${partAGrpA.meninPosition} </td>
                                    <td> ${partAGrpA.vacancyPosition} </td>
                                    <td> 

                                        <span id="label_${partAGrpA.postname}">${partAGrpA.otherRemarks}</span>
                                        <c:if test="${Editable eq 'N'}">
                                            <c:if test="${empty partAGrpA.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpA.postname}"/>', '<c:out value="${partAGrpA.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Remark</span></a>
                                            </c:if>
                                            <c:if test="${not empty partAGrpA.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpA.postname}"/>', '<c:out value="${partAGrpA.gpc}"/>');">
                                                    <img src="images/edit.gif" width="15" height="15"/>
                                                </a>
                                            </c:if>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partAGrpA.postname}"/>', '<c:out value="${partAGrpA.scaleofPay}"/>', '<c:out value="${partAGrpA.scaleofPay7th}"/>', '<c:out value="${partAGrpA.group}"/>', '<c:out value="${partAGrpA.gp}"/>');">Edit</a> 
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if  test="${not empty partAGrpAAllIndiaService}">
                                <tr>
                                    <td> </td>
                                    <td colspan="8"> <h4>All India Services</h4> </td>
                                </tr>
                                <c:forEach var="partAGrpAAllIndiaService" items="${partAGrpAAllIndiaService}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partAGrpAAllIndiaService.gpc} </td>
                                        <td> ${partAGrpAAllIndiaService.scaleofPay} </td>
                                        <td> ${partAGrpAAllIndiaService.gp} </td>
                                        <td> ${partAGrpAAllIndiaService.scaleofPay7th} </td>
                                        <td> ${partAGrpAAllIndiaService.sanctionedStrength} </td>
                                        <td> ${partAGrpAAllIndiaService.meninPosition} </td>
                                        <td> ${partAGrpAAllIndiaService.vacancyPosition} </td>
                                        <td> 

                                            <span id="label_${partAGrpAAllIndiaService.postname}">${partAGrpAAllIndiaService.otherRemarks}</span>
                                            <c:if test="${Editable eq 'N'}">
                                                <c:if test="${empty partAGrpAAllIndiaService.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpAAllIndiaService.postname}"/>', '<c:out value="${partAGrpAAllIndiaService.gpc}"/>');"><span id="remark_${partAGrpAAllIndiaService.postname}">Remark</span></a>
                                                </c:if>
                                                <c:if test="${not empty partAGrpAAllIndiaService.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpAAllIndiaService.postname}"/>', '<c:out value="${partAGrpAAllIndiaService.gpc}"/>');">
                                                        <img src="images/edit.gif" width="15" height="15"/>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partAGrpAAllIndiaService.postname}"/>', '<c:out value="${partAGrpAAllIndiaService.scaleofPay}"/>', '<c:out value="${partAGrpAAllIndiaService.scaleofPay7th}"/>', '<c:out value="${partAGrpAAllIndiaService.group}"/>', '<c:out value="${partAGrpAAllIndiaService.gp}"/>');">Edit</a> 
                                        </td>
                                    </tr>
                                </c:forEach>    
                            </c:if>
                            <c:if  test="${not empty partAJudiciary}">
                                <tr>
                                    <td> </td>
                                    <td colspan="8"> <h4>Judiciary</h4> </td>
                                </tr>
                                <c:forEach var="partAJudiciaryObj" items="${partAJudiciary}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partAJudiciaryObj.gpc} </td>
                                        <td> ${partAJudiciaryObj.scaleofPay} </td>
                                        <td> ${partAJudiciaryObj.gp} </td>
                                        <td> ${partAJudiciaryObj.scaleofPay7th} </td>
                                        <td> ${partAJudiciaryObj.sanctionedStrength} </td>
                                        <td> ${partAJudiciaryObj.meninPosition} </td>
                                        <td> ${partAJudiciaryObj.vacancyPosition} </td>
                                        <td> 

                                            <span id="label_${partAJudiciaryObj.postname}">${partAJudiciaryObj.otherRemarks}</span>
                                            <c:if test="${Editable eq 'N'}">
                                                <c:if test="${empty partAJudiciaryObj.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAJudiciaryObj.postname}"/>', '<c:out value="${partAJudiciaryObj.gpc}"/>');"><span id="remark_${partAJudiciaryObj.postname}">Remark</span></a>
                                                </c:if>
                                                <c:if test="${not empty partAJudiciaryObj.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAJudiciaryObj.postname}"/>', '<c:out value="${partAJudiciaryObj.gpc}"/>');">
                                                        <img src="images/edit.gif" width="15" height="15"/>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partAJudiciaryObj.postname}"/>', '<c:out value="${partAJudiciaryObj.scaleofPay}"/>', '<c:out value="${partAJudiciaryObj.scaleofPay7th}"/>', '<c:out value="${partAJudiciaryObj.group}"/>', '<c:out value="${partAJudiciaryObj.gp}"/>');">Edit</a> 
                                        </td>
                                    </tr>
                                </c:forEach>    
                            </c:if>           
                            <c:if  test="${not empty partAGrpAUGC}">
                                <tr>
                                    <td> </td>
                                    <td colspan="8"> <h4>UGC</h4> </td>
                                </tr>
                                <c:forEach var="partAGrpAUGCObj" items="${partAGrpAUGC}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partAGrpAUGCObj.gpc} </td>
                                        <td> ${partAGrpAUGCObj.scaleofPay} </td>
                                        <td> ${partAGrpAUGCObj.gp} </td>
                                        <td> ${partAGrpAUGCObj.scaleofPay7th} </td>
                                        <td> ${partAGrpAUGCObj.sanctionedStrength} </td>
                                        <td> ${partAGrpAUGCObj.meninPosition} </td>
                                        <td> ${partAGrpAUGCObj.vacancyPosition} </td>
                                        <td> 

                                            <span id="label_${partAGrpAUGCObj.postname}">${partAGrpAUGCObj.otherRemarks}</span>
                                            <c:if test="${Editable eq 'N'}">
                                                <c:if test="${empty partAGrpAUGCObj.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpAUGCObj.postname}"/>', '<c:out value="${partAGrpAUGCObj.gpc}"/>');"><span id="remark_${partAGrpAUGCObj.postname}">Remark</span></a>
                                                </c:if>
                                                <c:if test="${not empty partAGrpAUGCObj.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpAUGCObj.postname}"/>', '<c:out value="${partAGrpAUGCObj.gpc}"/>');">
                                                        <img src="images/edit.gif" width="15" height="15"/>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partAGrpAUGCObj.postname}"/>', '<c:out value="${partAGrpAUGCObj.scaleofPay}"/>', '<c:out value="${partAGrpAUGCObj.scaleofPay7th}"/>', '<c:out value="${partAGrpAUGCObj.group}"/>', '<c:out value="${partAGrpAUGCObj.gp}"/>');">Edit</a> 
                                        </td>
                                    </tr>
                                </c:forEach>    
                            </c:if>




                            <tr>
                                <td colspan="9"><h4> Group-B  </h4></td>
                            </tr>
                            <c:forEach var="partAGrpB" items="${PartAGroupBlist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpB.gpc} </td>
                                    <td> ${partAGrpB.scaleofPay} </td>
                                    <td> ${partAGrpB.gp} </td>
                                    <td> ${partAGrpB.scaleofPay7th} </td>
                                    <td> ${partAGrpB.sanctionedStrength} </td>
                                    <td> ${partAGrpB.meninPosition} </td>
                                    <td> ${partAGrpB.vacancyPosition} </td>

                                    <td>

                                        <span id="label_${partAGrpB.postname}">${partAGrpB.otherRemarks}</span>
                                        <c:if test="${Editable eq 'N'}">
                                            <c:if test="${empty partAGrpB.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpB.postname}"/>', '<c:out value="${partAGrpB.gpc}"/>');"><span id="remark_${partAGrpB.postname}">Remark</span></a>
                                            </c:if>
                                            <c:if test="${not empty partAGrpB.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpB.postname}"/>', '<c:out value="${partAGrpB.gpc}"/>');">
                                                    <img src="images/edit.gif" width="15" height="15"/>
                                                </a>
                                            </c:if>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partAGrpB.postname}"/>', '<c:out value="${partAGrpB.scaleofPay}"/>', '<c:out value="${partAGrpB.scaleofPay7th}"/>', '<c:out value="${partAGrpB.group}"/>', '<c:out value="${partAGrpB.gp}"/>');">Edit</a> 
                                    </td>
                                </tr>
                            </c:forEach> 
                            <tr>
                                <td colspan="9"><h4> Group-C  </h4></td>
                            </tr>
                            <c:forEach var="partAGrpC" items="${PartAGroupClist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpC.gpc} </td>
                                    <td> ${partAGrpC.scaleofPay} </td>
                                    <td> ${partAGrpC.gp} </td>
                                    <td> ${partAGrpC.scaleofPay7th} </td>
                                    <td> ${partAGrpC.sanctionedStrength} </td>
                                    <td> ${partAGrpC.meninPosition} </td>
                                    <td> ${partAGrpC.vacancyPosition} </td>
                                    <td>

                                        <span id="label_${partAGrpC.postname}">${partAGrpC.otherRemarks}</span>
                                        <c:if test="${Editable eq 'N'}">
                                            <c:if test="${empty partAGrpC.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpC.postname}"/>', '<c:out value="${partAGrpC.gpc}"/>');"><span id="remark_${partAGrpC.postname}">Remark</span></a>
                                            </c:if>
                                            <c:if test="${not empty partAGrpC.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpC.postname}"/>', '<c:out value="${partAGrpC.gpc}"/>');">
                                                    <img src="images/edit.gif" width="15" height="15"/>
                                                </a>
                                            </c:if>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partAGrpC.postname}"/>', '<c:out value="${partAGrpC.scaleofPay}"/>', '<c:out value="${partAGrpC.scaleofPay7th}"/>', '<c:out value="${partAGrpC.group}"/>', '<c:out value="${partAGrpC.gp}"/>');">Edit</a> 
                                    </td>
                                </tr>
                            </c:forEach>   
                            <tr>
                                <td colspan="9"><h4> Group-D </h4> </td>
                            </tr>
                            <c:forEach var="partAGrpD" items="${PartAGroupDlist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpD.gpc} </td>
                                    <td> ${partAGrpD.scaleofPay} </td>
                                    <td> ${partAGrpD.gp} </td>
                                    <td> ${partAGrpD.scaleofPay7th} </td>
                                    <td> ${partAGrpD.sanctionedStrength} </td>
                                    <td> ${partAGrpD.meninPosition} </td>
                                    <td> ${partAGrpD.vacancyPosition} </td>
                                    <td>

                                        <span id="label_${partAGrpD.postname}">${partAGrpD.otherRemarks}</span>
                                        <c:if test="${Editable eq 'N'}">
                                            <c:if test="${empty partAGrpD.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpD.postname}"/>', '<c:out value="${partAGrpD.gpc}"/>');"><span id="remark_${partAGrpD.postname}">Remark</span></a>
                                            </c:if>
                                            <c:if test="${not empty partAGrpD.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partAGrpD.postname}"/>', '<c:out value="${partAGrpD.gpc}"/>');">
                                                    <img src="images/edit.gif" width="15" height="15"/>
                                                </a>
                                            </c:if>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partAGrpD.postname}"/>', '<c:out value="${partAGrpD.scaleofPay}"/>', '<c:out value="${partAGrpD.scaleofPay7th}"/>', '<c:out value="${partAGrpD.group}"/>', '<c:out value="${partAGrpD.gp}"/>');">Edit</a> 
                                    </td>
                                </tr>
                            </c:forEach>       

                        </table>

                        <div class="jumbotron">
                            <h1>Part-B (G-I-A Establishment)</h1>
                        </div>
                        <table class="table table-bordered">
                            <thead>
                                <tr class="bg-info text-white">
                                    <th>Category of employee</th>
                                    <th>Description of the Posts</th>
                                    <th>Pay Scale</th>
                                    <th>As per 6th Pay Commission</th>
                                    <th>As per 7th Pay Commission</th>
                                    <th>Sanction Strength</th>
                                    <th>Persons-in-Position</th>
                                    <th>Vacancy</th>
                                    <th>Remarks</th>                          
                                </tr>
                            </thead>
                            <tbody>
                                <c:if  test="${not empty partBGrpAList}">
                                    <tr>
                                        <td>  <h4>Group-A </h4></td>
                                        <td colspan="8"> <h4>Other than UGC /Judiciary/ All India Services </h4></td>
                                    </tr>
                                </c:if>
                                <c:forEach var="partBGrpA" items="${partBGrpAList}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partBGrpA.gpc} </td>
                                        <td> ${partBGrpA.scaleofPay} </td>
                                        <td> ${partBGrpA.gp} </td>
                                        <td> ${partBGrpA.scaleofPay7th} </td>
                                        <td> ${partBGrpA.sanctionedStrength} </td>
                                        <td> ${partBGrpA.meninPosition} </td>
                                        <td> ${partBGrpA.vacancyPosition} </td>
                                        <td> 

                                            <span id="label_${partBGrpA.postname}">${partBGrpA.otherRemarks}</span>
                                            <c:if test="${Editable eq 'N'}">
                                                <c:if test="${empty partBGrpA.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpA.postname}"/>', '<c:out value="${partBGrpA.gpc}"/>');"><span id="remark_${partBGrpA.postname}">Remark</span></a>
                                                </c:if>
                                                <c:if test="${not empty partAGrpA.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpA.postname}"/>', '<c:out value="${partBGrpA.gpc}"/>');">
                                                        <img src="images/edit.gif" width="15" height="15"/>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partBGrpA.postname}"/>', '<c:out value="${partBGrpA.scaleofPay}"/>', '<c:out value="${partBGrpA.scaleofPay7th}"/>', '<c:out value="${partBGrpA.group}"/>', '<c:out value="${partBGrpA.gp}"/>');">Edit</a> 
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if  test="${not empty partBGrpAAllIndiaService}">
                                    <tr>
                                        <td> </td>
                                        <td colspan="8"> <h4>All India Services</h4> </td>
                                    </tr>
                                    <c:forEach var="partBGrpAAIS" items="${partBGrpAAllIndiaService}">
                                        <tr>
                                            <td> &nbsp; </td>
                                            <td> ${partBGrpAAIS.gpc} </td>
                                            <td> ${partBGrpAAIS.scaleofPay} </td>
                                            <td> ${partBGrpAAIS.gp} </td>
                                            <td> ${partBGrpAAIS.scaleofPay7th} </td>
                                            <td> ${partBGrpAAIS.sanctionedStrength} </td>
                                            <td> ${partBGrpAAIS.meninPosition} </td>
                                            <td> ${partBGrpAAIS.vacancyPosition} </td>
                                            <td> 

                                                <span id="label_${partBGrpAAIS.postname}">${partBGrpAAIS.otherRemarks}</span>
                                                <c:if test="${Editable eq 'N'}">
                                                    <c:if test="${empty partBGrpAAIS.otherRemarks}">
                                                        <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpAAIS.postname}"/>', '<c:out value="${partBGrpAAIS.gpc}"/>');"><span id="remark_${partBGrpAAIS.postname}">Remark</span></a>
                                                    </c:if>
                                                    <c:if test="${not empty partBGrpAAIS.otherRemarks}">
                                                        <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpAAIS.postname}"/>', '<c:out value="${partBGrpAAIS.gpc}"/>');">
                                                            <img src="images/edit.gif" width="15" height="15"/>
                                                        </a>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partBGrpAAIS.postname}"/>', '<c:out value="${partBGrpAAIS.scaleofPay}"/>', '<c:out value="${partBGrpAAIS.scaleofPay7th}"/>', '<c:out value="${partBGrpAAIS.group}"/>', '<c:out value="${partBGrpAAIS.gp}"/>');">Edit</a> 
                                            </td>
                                        </tr>
                                    </c:forEach>    
                                </c:if>
                                <c:if  test="${not empty partBJudiciary}">
                                    <tr>
                                        <td> </td>
                                        <td colspan="8"> <h4>Judiciary</h4> </td>
                                    </tr>
                                    <c:forEach var="partBJudiciaryObj" items="${partBJudiciary}">
                                        <tr>
                                            <td> &nbsp; </td>
                                            <td> ${partBJudiciaryObj.gpc} </td>
                                            <td> ${partBJudiciaryObj.scaleofPay} </td>
                                            <td> ${partBJudiciaryObj.gp} </td>
                                            <td> ${partBJudiciaryObj.scaleofPay7th} </td>
                                            <td> ${partBJudiciaryObj.sanctionedStrength} </td>
                                            <td> ${partBJudiciaryObj.meninPosition} </td>
                                            <td> ${partBJudiciaryObj.vacancyPosition} </td>
                                            <td> 

                                                <span id="label_${partBJudiciaryObj.postname}">${partBJudiciaryObj.otherRemarks}</span>
                                                <c:if test="${Editable eq 'N'}">
                                                    <c:if test="${empty partBJudiciaryObj.otherRemarks}">
                                                        <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBJudiciaryObj.postname}"/>', '<c:out value="${partBJudiciaryObj.gpc}"/>');"><span id="remark_${partBJudiciaryObj.postname}">Remark</span></a>
                                                    </c:if>
                                                    <c:if test="${not empty partBJudiciaryObj.otherRemarks}">
                                                        <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBJudiciaryObj.postname}"/>', '<c:out value="${partBJudiciaryObj.gpc}"/>');">
                                                            <img src="images/edit.gif" width="15" height="15"/>
                                                        </a>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partBJudiciaryObj.postname}"/>', '<c:out value="${partBJudiciaryObj.scaleofPay}"/>', '<c:out value="${partBJudiciaryObj.scaleofPay7th}"/>', '<c:out value="${partBJudiciaryObj.group}"/>', '<c:out value="${partBJudiciaryObj.gp}"/>');">Edit</a> 
                                            </td>
                                        </tr>
                                    </c:forEach>    
                                </c:if>           
                                <c:if  test="${not empty partBGrpAUGC}">
                                    <tr>
                                        <td> </td>
                                        <td colspan="8"> <h4>UGC</h4> </td>
                                    </tr>
                                    <c:forEach var="partBGrpAUGCObj" items="${partBGrpAUGC}">
                                        <tr>
                                            <td> &nbsp; </td>
                                            <td> ${partBGrpAUGCObj.gpc} </td>
                                            <td> ${partBGrpAUGCObj.scaleofPay} </td>
                                            <td> ${partBGrpAUGCObj.gp} </td>
                                            <td> ${partBGrpAUGCObj.scaleofPay7th} </td>
                                            <td> ${partBGrpAUGCObj.sanctionedStrength} </td>
                                            <td> ${partBGrpAUGCObj.meninPosition} </td>
                                            <td> ${partBGrpAUGCObj.vacancyPosition} </td>
                                            <td> 

                                                <span id="label_${partBGrpAUGCObj.postname}">${partBGrpAUGCObj.otherRemarks}</span>
                                                <c:if test="${Editable eq 'N'}">
                                                    <c:if test="${empty partBGrpAUGCObj.otherRemarks}">
                                                        <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpAUGCObj.postname}"/>', '<c:out value="${partBGrpAUGCObj.gpc}"/>');"><span id="remark_${partBGrpAUGCObj.postname}">Remark</span></a>
                                                    </c:if>
                                                    <c:if test="${not empty partBGrpAUGCObj.otherRemarks}">
                                                        <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpAUGCObj.postname}"/>', '<c:out value="${partBGrpAUGCObj.gpc}"/>');">
                                                            <img src="images/edit.gif" width="15" height="15"/>
                                                        </a>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partBGrpAUGCObj.postname}"/>', '<c:out value="${partBGrpAUGCObj.scaleofPay}"/>', '<c:out value="${partBGrpAUGCObj.scaleofPay7th}"/>', '<c:out value="${partBGrpAUGCObj.group}"/>', '<c:out value="${partBGrpAUGCObj.gp}"/>');">Edit</a> 
                                            </td>
                                        </tr>
                                    </c:forEach>    
                                </c:if>




                                <tr>
                                    <td colspan="9"><h4> Group-B  </h4></td>
                                </tr>
                                <c:forEach var="partBGrpB" items="${partBGrpBList}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partBGrpB.gpc} </td>
                                        <td> ${partBGrpB.scaleofPay} </td>
                                        <td> ${partBGrpB.gp} </td>
                                        <td> ${partBGrpB.scaleofPay7th} </td>
                                        <td> ${partBGrpB.sanctionedStrength} </td>
                                        <td> ${partBGrpB.meninPosition} </td>
                                        <td> ${partBGrpB.vacancyPosition} </td>

                                        <td>

                                            <span id="label_${partBGrpB.postname}">${partBGrpB.otherRemarks}</span>
                                            <c:if test="${Editable eq 'N'}">
                                                <c:if test="${empty partBGrpB.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpB.postname}"/>', '<c:out value="${partBGrpB.gpc}"/>');"><span id="remark_${partBGrpB.postname}">Remark</span></a>
                                                </c:if>
                                                <c:if test="${not empty partBGrpB.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpB.postname}"/>', '<c:out value="${partBGrpB.gpc}"/>');">
                                                        <img src="images/edit.gif" width="15" height="15"/>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partBGrpB.postname}"/>', '<c:out value="${partBGrpB.scaleofPay}"/>', '<c:out value="${partBGrpB.scaleofPay7th}"/>', '<c:out value="${partBGrpB.group}"/>', '<c:out value="${partBGrpB.gp}"/>');">Edit</a> 
                                        </td>
                                    </tr>
                                </c:forEach> 
                                <tr>
                                    <td colspan="9"><h4> Group-C  </h4></td>
                                </tr>
                                <c:forEach var="partBGrpC" items="${partBGrpCList}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partBGrpC.gpc} </td>
                                        <td> ${partBGrpC.scaleofPay} </td>
                                        <td> ${partBGrpC.gp} </td>
                                        <td> ${partBGrpC.scaleofPay7th} </td>
                                        <td> ${partBGrpC.sanctionedStrength} </td>
                                        <td> ${partBGrpC.meninPosition} </td>
                                        <td> ${partBGrpC.vacancyPosition} </td>
                                        <td>

                                            <span id="label_${partBGrpC.postname}">${partBGrpC.otherRemarks}</span>
                                            <c:if test="${Editable eq 'N'}">
                                                <c:if test="${empty partBGrpC.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpC.postname}"/>', '<c:out value="${partBGrpC.gpc}"/>');"><span id="remark_${partBGrpC.postname}">Remark</span></a>
                                                </c:if>
                                                <c:if test="${not empty partBGrpC.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpC.postname}"/>', '<c:out value="${partBGrpC.gpc}"/>');">
                                                        <img src="images/edit.gif" width="15" height="15"/>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partBGrpC.postname}"/>', '<c:out value="${partBGrpC.scaleofPay}"/>', '<c:out value="${partBGrpC.scaleofPay7th}"/>', '<c:out value="${partBGrpC.group}"/>', '<c:out value="${partBGrpC.gp}"/>');">Edit</a> 
                                        </td>
                                    </tr>
                                </c:forEach>   
                                <tr>
                                    <td colspan="9"><h4> Group-D </h4> </td>
                                </tr>
                                <c:forEach var="partBGrpD" items="${partBGrpDList}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partBGrpD.gpc} </td>
                                        <td> ${partBGrpD.scaleofPay} </td>
                                        <td> ${partBGrpD.gp} </td>
                                        <td> ${partBGrpD.scaleofPay7th} </td>
                                        <td> ${partBGrpD.sanctionedStrength} </td>
                                        <td> ${partBGrpD.meninPosition} </td>
                                        <td> ${partBGrpD.vacancyPosition} </td>
                                        <td>

                                            <span id="label_${partBGrpD.postname}">${partBGrpD.otherRemarks}</span>
                                            <c:if test="${Editable eq 'N'}">
                                                <c:if test="${empty partBGrpD.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpD.postname}"/>', '<c:out value="${partBGrpD.gpc}"/>');"><span id="remark_${partBGrpD.postname}">Remark</span></a>
                                                </c:if>
                                                <c:if test="${not empty partBGrpD.otherRemarks}">
                                                    <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partBGrpD.postname}"/>', '<c:out value="${partBGrpD.gpc}"/>');">
                                                        <img src="images/edit.gif" width="15" height="15"/>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" onclick="openPartAModal('single', '<c:out value="${partBGrpD.postname}"/>', '<c:out value="${partBGrpD.scaleofPay}"/>', '<c:out value="${partBGrpD.scaleofPay7th}"/>', '<c:out value="${partBGrpD.group}"/>', '<c:out value="${partBGrpD.gp}"/>');">Edit</a> 
                                        </td>
                                    </tr>
                                </c:forEach>  


                                <c:forEach items="${giaList}" var="aeGIA" varStatus="cnt">
                                    <tr>
                                        <td>${aeGIA.group}</td>
                                        <td>${aeGIA.otherPost}</td>  
                                        <td>${aeGIA.scaleofPay}</td>  
                                        <td>${aeGIA.gp}</td>
                                        <td>${aeGIA.other7thPay}</td>
                                        <td>${aeGIA.otherSS}</td>
                                        <td>${aeGIA.meninPosition}</td>
                                        <td>${aeGIA.otherVacancy}</td>

                                        <td>${aeGIA.otherRemarks}</td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>             

                        <div class="jumbotron">
                            <h1>Part-C (Non-Regular Establishment)</h1>
                        </div>
                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>
                                    <th width="10%">Category </th>
                                    <th width="25%">Description of the Posts</th>
                                    <th  width="25%">As per 7th Pay</th>
                                    <th width="8%">Persons-in-Position</th>
                                    <th width="52%">

                                        Remarks if any

                                    </th>

                                </tr>

                            </thead>
                            <tr>
                                <td colspan="9"> Contractual in lieu of regular post (As per GA & PG Department Resolution)/ Other Contractual Junior Engineer </td>
                            </tr>
                            <c:forEach var="partCGrp" items="${PartCGrouplist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partCGrp.gpc} </td>
                                    <td> ${partCGrp.scaleofPay7th} </td>
                                    <td> ${partCGrp.meninPosition} </td>
                                    <td> &nbsp;  
                                        <span id="label_${partCGrp.postname}">${partCGrp.otherRemarks}</span>
                                        <c:if test="${Editable eq 'N'}">
                                            <c:if test="${empty partCGrp.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partCGrp.postname}"/>', '<c:out value="${partCGrp.gpc}"/>');"><span id="remark_${partCGrp.postname}">Remark</span></a>
                                            </c:if>
                                            <c:if test="${not empty partCGrp.otherRemarks}">
                                                <a href="javascript:void(0);" onclick="openRemarksModal('${command.aerId}', '<c:out value="${partCGrp.postname}"/>', '<c:out value="${partCGrp.gpc}"/>');">
                                                    <img src="images/edit.gif" width="15" height="15"/>
                                                </a>
                                            </c:if>
                                        </c:if>
                                    </td>
                                    <td> &nbsp;  <c:if test="${Editable eq 'Y'}">
                                            <a href="javascript:void(0);" onclick="openPartCModal('single', '<c:out value="${partCGrp.postname}"/>');">Edit</a> 
                                        </c:if> 
                                    </td>
                                </tr>
                            </c:forEach>



                        </table>
                        <div class="jumbotron">
                            <h1>Part-D (Other Establishment)</h1>
                        </div>

                        <table class="table table-bordered">
                            <thead>
                                <tr class="bg-info text-white">

                                    <th>Category</th>
                                    <th>Number</th>
                                    <th>Group</th>  
                                    <th>Remarks</th>                          

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${aeDdetails}" var="ae" varStatus="cnt">
                                    <tr>

                                        <td>${ae.otherCategory}</td>  
                                        <td>${ae.otherSS}</td>
                                        <td>${ae.group}</td>
                                        <td>${ae.otherRemarks}</td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                        <div class="jumbotron">
                            <h1>Part-E (Out Source/on Contract)</h1>
                        </div>

                        <table class="table table-bordered">
                            <thead>
                                <tr class="bg-info text-white">
                                    <th>Category</th>
                                    <th>Number</th>
                                    <th>Remarks</th>                          
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${aeEdetails}" var="ae2" varStatus="cnt">
                                    <tr>
                                        <td>${ae2.otherCategory}</td>  
                                        <td>${ae2.otherSS}</td>
                                        <td>${ae2.otherRemarks}</td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>   
                    </div>
                    <div class="panel-footer">
                        <button type="submit" name="btnAer" value="Back" class="btn btn-primary">Back to List</button>
                        <c:if test="${Editable eq 'N'}">
                            <a href="submitEstablishmentReport.htm?aerId=${command.aerId}" class="btn btn-primary" role="button" onclick="return confirm('Are you sure to Submit AER ?')">Submit to Approver</a>

                            (You can Set Remarks by click on Remark link provided against each Post)
                        </c:if>    
                    </div>
                </div>

            </div>

            <div id="substantivePostPartAModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Select Pay Scale and Post Group</h4>
                        </div>
                        <div class="modal-body">

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="scaleofPay">Pay Scale(6th)</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="scaleofPay" id="scaleofPay" class="form-control">
                                        <form:option value="">--Select Pay Scale--</form:option>
                                        <form:options items="${payscaleList}" itemValue="payscale" itemLabel="payscale"/>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="gp">Grade Pay</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:input path="gp" id="gp" maxlength="5" class="form-control" onkeypress='return onlyIntegerRange(event)'/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="paylevel">As per 7th Pay(LEVEL)</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="paylevel" id="paylevel" class="form-control">
                                        <option value="">--Select Level--</option>
                                        <option value="0">0</option>
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                        <option value="7">7</option>
                                        <option value="8">8</option>
                                        <option value="9">9</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="12">12</option>
                                        <option value="13">13</option>
                                        <option value="14">14</option>
                                        <option value="15">15</option>
                                        <option value="16">16</option>
                                        <option value="17">17</option>
                                        <option value="18">18</option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="postgrp">Post Group</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="postgrp" id="postgrp" class="form-control">
                                        <option value="">--Select Post Group--</option>
                                        <option value="A">A</option>
                                        <option value="B">B</option>
                                        <option value="C">C</option>
                                        <option value="D">D</option>
                                    </form:select>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                </div>
                                <div class="col-lg-3">
                                    <button type="submit" name="btnAer" value="Update" class="btn btn-primary" onclick="return verifyUpdate();">Update</button>
                                </div>
                                <div class="col-lg-6">
                                    <span id="msg"></span>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>