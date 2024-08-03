<%-- 
    Document   : PreviewSubmitAerReport
    Created on : 19 Jun, 2019, 2:17:21 PM
    Author     : Surendra
--%>

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
            function openRemarksModal(aerid, gpc, postname) {
                //alert("aerid is: "+aerid);
                //alert("gpc is: "+gpc);

                $('#gpc').val(gpc);
                $('#aerId').val(aerid);
                $('#postname').html(postname);
                $('#otherRemarks').val('');

                var url = 'getRemarks.htm?aerId=' + $('#aerId').val() + '&gpc=' + $('#gpc').val();
                $.getJSON(url, function(data) {
                    //alert(data.remarks);
                    $('#otherRemarks').val(data.remarks);
                });

                $('#remarksModal').modal("show");
            }

            function openChangeCadreModal(reportId, gpc, postname) {
                $('#gpc').val(gpc);
                $('#reportId').val(reportId);
                $('#postnameForCadre').html(postname);

                $('#changeCadreModal').modal("show");
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
            function saveRemarks() {
                $.ajax({
                    type: "GET",
                    url: "saveRemarks.htm",
                    data: {aerId: $('#aerId').val(), gpc: $('#gpc').val(), otherRemarks: $('#otherRemarks').val()},
                    success: function(data) {
                        alert('Remarks Saved.');
                        $('#remarksModal').modal("hide");
                        $('#label_' + $('#gpc').val()).text($('#otherRemarks').val());
                        $('#remark_' + $('#gpc').val()).html('<img src="images/edit.gif" width="15" height="15"/>');
                    },
                    error: function() {
                        alert('Error Occured');
                    }
                });
            }
            function changeCadre() {
                $.ajax({
                    type: "GET",
                    url: "changeCadre.htm",
                    data: {reportId: $('#reportId').val(), gpc: $('#gpc').val(), sltCadre: $('#sltCadre').val()},
                    success: function(data) {
                        alert('Cadre Changed.');
                        $('#changeCadreModal').modal("hide");
                    },
                    error: function() {
                        alert('Error Occured');
                    }
                });
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

        <form:form action="createAER.htm" method="POST" commandName="command">
            <form:hidden path="aerId" id="aerId"/>
            <form:hidden path="reportId" id="reportId"/>
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
                                    <th width="5%" rowspan="2"> Change Cadre  </th>
                                    <th width="5%" rowspan="2"> Remarks if any  </th>
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
                                        <a href="javascript:void(0);" onclick="openChangeCadreModal('${partAGrpA.reportId}', '<c:out value="${partAGrpA.postname}"/>', '<c:out value="${partAGrpA.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                    </td>
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
                                            <a href="javascript:void(0);" onclick="openChangeCadreModal('${partAGrpAAllIndiaService.reportId}', '<c:out value="${partAGrpAAllIndiaService.postname}"/>', '<c:out value="${partAGrpAAllIndiaService.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                        </td>
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
                                            <a href="javascript:void(0);" onclick="openChangeCadreModal('${partAJudiciaryObj.reportId}', '<c:out value="${partAJudiciaryObj.postname}"/>', '<c:out value="${partAJudiciaryObj.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                        </td>
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
                                            <a href="javascript:void(0);" onclick="openChangeCadreModal('${partAGrpAUGCObj.reportId}', '<c:out value="${partAGrpAUGCObj.postname}"/>', '<c:out value="${partAGrpAUGCObj.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                        </td>
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
                                    </tr>
                                </c:forEach>    
                            </c:if>
                            <tr>
                                <td> &nbsp; </td>
                                <td> <strong>Total</strong> </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> ${partATotalGrpASanctionedStrength} </td>
                                <td> ${partATotalGrpAMenInPosition} </td>
                                <td> ${partATotalGrpAVacancy} </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>



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
                                        <a href="javascript:void(0);" onclick="openChangeCadreModal('${partAGrpB.reportId}', '<c:out value="${partAGrpB.postname}"/>', '<c:out value="${partAGrpB.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                    </td>
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

                                </tr>
                            </c:forEach>
                            <tr>
                                <td> &nbsp; </td>
                                <td> <strong>Total</strong> </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> ${partATotalGrpBSanctionedStrength} </td>
                                <td> ${partATotalGrpBMenInPosition} </td>
                                <td> ${partATotalGrpBVacancy} </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
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
                                        <a href="javascript:void(0);" onclick="openChangeCadreModal('${partAGrpC.reportId}', '<c:out value="${partAGrpC.postname}"/>', '<c:out value="${partAGrpC.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                    </td>
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
                                </tr>
                            </c:forEach>
                            <tr>
                                <td> &nbsp; </td>
                                <td> <strong>Total</strong> </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> ${partATotalGrpCSanctionedStrength} </td>
                                <td> ${partATotalGrpCMenInPosition} </td>
                                <td> ${partATotalGrpCVacancy} </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
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
                                        <a href="javascript:void(0);" onclick="openChangeCadreModal('${partAGrpD.reportId}', '<c:out value="${partAGrpD.postname}"/>', '<c:out value="${partAGrpD.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                    </td>
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
                                </tr>
                            </c:forEach>       
                            <tr>
                                <td> &nbsp; </td>
                                <td> <strong>Total</strong> </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> ${partATotalGrpDSanctionedStrength} </td>
                                <td> ${partATotalGrpDMenInPosition} </td>
                                <td> ${partATotalGrpDVacancy} </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
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
                                    <th>Change Cadre</th>
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
                                            <a href="javascript:void(0);" onclick="openChangeCadreModal('${partBGrpA.reportId}', '<c:out value="${partBGrpA.postname}"/>', '<c:out value="${partBGrpA.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                        </td>
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
                                                <a href="javascript:void(0);" onclick="openChangeCadreModal('${partBGrpAAIS.reportId}', '<c:out value="${partBGrpAAIS.postname}"/>', '<c:out value="${partBGrpAAIS.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                            </td>
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
                                                <a href="javascript:void(0);" onclick="openChangeCadreModal('${partBJudiciaryObj.reportId}', '<c:out value="${partBJudiciaryObj.postname}"/>', '<c:out value="${partBJudiciaryObj.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                            </td>
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
                                                <a href="javascript:void(0);" onclick="openChangeCadreModal('${partBGrpAUGCObj.reportId}', '<c:out value="${partBGrpAUGCObj.postname}"/>', '<c:out value="${partBGrpAUGCObj.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                            </td>
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
                                        </tr>
                                    </c:forEach>    
                                </c:if>
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> <strong>Total</strong> </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> ${partBTotalGrpASanctionedStrength} </td>
                                    <td> ${partBTotalGrpAMenInPosition} </td>
                                    <td> ${partBTotalGrpAVacancy} </td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>



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
                                            <a href="javascript:void(0);" onclick="openChangeCadreModal('${partBGrpB.reportId}', '<c:out value="${partBGrpB.postname}"/>', '<c:out value="${partBGrpB.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                        </td>
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

                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> <strong>Total</strong> </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> ${partBTotalGrpBSanctionedStrength} </td>
                                    <td> ${partBTotalGrpBMenInPosition} </td>
                                    <td> ${partBTotalGrpBVacancy} </td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
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
                                            <a href="javascript:void(0);" onclick="openChangeCadreModal('${partBGrpC.reportId}', '<c:out value="${partBGrpC.postname}"/>', '<c:out value="${partBGrpC.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                        </td>
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
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> <strong>Total</strong> </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> ${partBTotalGrpCSanctionedStrength} </td>
                                    <td> ${partBTotalGrpCMenInPosition} </td>
                                    <td> ${partBTotalGrpCVacancy} </td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
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
                                            <a href="javascript:void(0);" onclick="openChangeCadreModal('${partBGrpD.reportId}', '<c:out value="${partBGrpD.postname}"/>', '<c:out value="${partBGrpD.gpc}"/>');"><span id="remark_${partAGrpA.postname}">Change</span></a>
                                        </td>
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
                                    </tr>
                                </c:forEach>  

                                <tr>
                                    <td> &nbsp; </td>
                                    <td> <strong>Total</strong> </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> ${partBTotalGrpDSanctionedStrength} </td>
                                    <td> ${partBTotalGrpDMenInPosition} </td>
                                    <td> ${partBTotalGrpDVacancy} </td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
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
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> <strong>Total</strong> </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> &nbsp; </td>
                                    <td> ${giaTotalSanctionedStrength} </td>
                                    <td> ${giaTotalMenInPosition} </td>
                                    <td> ${giaTotalVacancy} </td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
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

            <div id="remarksModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:500px;background:#FFFFFF;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Enter Remarks</h4>
                        </div>
                        <div class="modal-body">
                            <div id="postname" style="font-weight:bold;font-size:16px;"></div>
                            <hr />
                            <form:textarea path="otherRemarks" id="otherRemarks" rows="5" cols="50" maxlength="100"/><br />
                            <span style="color:red;">
                                Please enter remarks within 100 characters.
                            </span>
                        </div>
                        <div class="modal-footer">
                            <input type="button" name="btnAer" value="Save Remarks" class="btn btn-priamry" onclick="saveRemarks();"/>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="changeCadreModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:500px;background:#FFFFFF;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Change Cadre</h4>
                        </div>
                        <div class="modal-body">
                            <div id="postnameForCadre" style="font-weight:bold;font-size:16px;"></div>
                            <hr />
                            <form:select path="sltCadre" id="sltCadre" class="form-control">
                                <form:option value="">--Select--</form:option>
                                <form:option value="AIS">AIS</form:option>
                                <form:option value="OJS">OJS</form:option>
                                <form:option value="UGC">UGC</form:option>
                            </form:select>
                        </div>
                        <div class="modal-footer">
                            <input type="button" name="btnAer" value="Change Cadre" class="btn btn-priamry" onclick="changeCadre();"/>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

        </form:form>



    </body>
</html>
