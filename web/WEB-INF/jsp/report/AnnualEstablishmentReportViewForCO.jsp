<%-- 
    Document   : AnnualEstablishmentReportViewForCO
    Created on : 24 Jun, 2019, 2:56:31 PM
    Author     : Surendra
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
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
        <script type="text/javascript">
            function submitValidation() {
                var count = $('#pendingApproveCount').val();
                if (parseInt(count) > 0) {
                    alert("Please Approve all Operator Offices or Decline them if you don't want to Approve before Submission to Acceptor.");
                    return false;
                } else {
                    if (confirm('Are you sure to Submit?')) {
                        $('#btnAcceptor').hide();
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        </script>

    </head>
    <body>
        <form:form action="displayAERlistForControllingAuthority.htm" method="POST" commandName="command">
            <form:hidden path="aerId" id="aerId"/>
            <form:hidden path="gpc" id="gpc"/>
            <form:hidden path="financialYear"/>
            <form:hidden path="coOffCode"/>
            <input type="hidden" id="pendingApproveCount" value="${pendingApproveCount}"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-footer">
                        <button type="submit" name="btnAer" value="Back" class="btn btn-primary">Back to List</button>
                        <c:if test="${showsubmitbtn eq 'N'}">
                            <%--<button type="submit" name="btnAer" value="SubmitToAcceptor" class="btn btn-primary"  onclick="return confirm('Are you sure to Submit?')">Submit to Acceptor</button>--%>
                            <c:if test="${empty command.coOffCode}">
                                <button type="submit" name="btnAer" value="SubmitToAcceptor" id="btnAcceptor" class="btn btn-primary" onclick="return submitValidation();">Submit to Acceptor</button>
                            </c:if>
                            <c:if test="${not empty command.coOffCode}">
                                <button type="submit" name="btnAer" value="SubmitToAcceptorMultipleCO" id="btnAcceptor" class="btn btn-primary" onclick="return submitValidation();">Submit to Acceptor</button>
                            </c:if>
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
                                    <td> ${partAGrpA.postname} </td>
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
                                        <td> ${partAGrpAAllIndiaService.postname} </td>
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
                                        <td> ${partAJudiciaryObj.postname} </td>
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
                                        <td> ${partAGrpAUGCObj.postname} </td>
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
                            </tr>




                            <tr>
                                <td colspan="9"><h4> Group-B  </h4></td>
                            </tr>
                            <c:forEach var="partAGrpB" items="${PartAGroupBlist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpB.postname} </td>
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
                            </tr>
                            <tr>
                                <td colspan="9"><h4> Group-C  </h4></td>
                            </tr>
                            <c:forEach var="partAGrpC" items="${PartAGroupClist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpC.postname} </td>
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
                            </tr>
                            <tr>
                                <td colspan="9"><h4> Group-D </h4> </td>
                            </tr>
                            <c:forEach var="partAGrpD" items="${PartAGroupDlist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpD.postname} </td>
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
                            </tr>

                        </table>

                        <div class="jumbotron">
                            <h1>Part-B (G-I-A Establishment)</h1>
                        </div>
                        <table class="table table-bordered">
                            <thead>
                                <tr class="bg-info text-white">
                                    <th>Category of employee</th>
                                    <th>Post Description</th>
                                    <th>Scale of Pay</th>
                                    <th>As per 6th Pay Commission</th>
                                    <th>As per 7th Pay Commission</th>
                                    <th>Sanction Strength</th>
                                    <th>Persons-in-Position</th>
                                    <th>Vacancy</th>
                                    <th>Remarks</th>                          

                                </tr>
                            </thead>
                            <tbody>




                                <tr>
                                    <td>  <h4>Group-A </h4></td>
                                    <td colspan="8"> <h4>Other than UGC /Judiciary/ All India Services </h4></td>
                                </tr>
                                <c:forEach var="partBGrpA" items="${partBgrpAList}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partBGrpA.postname} </td>
                                        <td> ${partBGrpA.scaleofPay} </td>
                                        <td> ${partBGrpA.gp} </td>
                                        <td> ${partBGrpA.scaleofPay7th} </td>
                                        <td> ${partBGrpA.sanctionedStrength} </td>
                                        <td> ${partBGrpA.meninPosition} </td>
                                        <td> ${partBGrpA.vacancyPosition} </td>
                                        <td> 

                                            <span id="label_${partBGrpA.postname}">${partBGrpA.otherRemarks}</span>

                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if  test="${not empty partAGrpAAllIndiaService}">
                                    <tr>
                                        <td> </td>
                                        <td colspan="8"> <h4>All India Services</h4> </td>
                                    </tr>
                                    <c:forEach var="partBGrpAAllIndiaService" items="${partBGrpAAllIndiaService}">
                                        <tr>
                                            <td> &nbsp; </td>
                                            <td> ${partBGrpAAllIndiaService.postname} </td>
                                            <td> ${partBGrpAAllIndiaService.scaleofPay} </td>
                                            <td> ${partBGrpAAllIndiaService.gp} </td>
                                            <td> ${partBGrpAAllIndiaService.scaleofPay7th} </td>
                                            <td> ${partBGrpAAllIndiaService.sanctionedStrength} </td>
                                            <td> ${partBGrpAAllIndiaService.meninPosition} </td>
                                            <td> ${partBGrpAAllIndiaService.vacancyPosition} </td>
                                            <td> 

                                                <span id="label_${partBGrpAAllIndiaService.postname}">${partBGrpAAllIndiaService.otherRemarks}</span>

                                            </td>
                                        </tr>
                                    </c:forEach>    
                                </c:if>
                                <c:if  test="${not empty partAJudiciary}">
                                    <tr>
                                        <td> </td>
                                        <td colspan="8"> <h4>Judiciary</h4> </td>
                                    </tr>
                                    <c:forEach var="partBJudiciaryObj" items="${partBJudiciary}">
                                        <tr>
                                            <td> &nbsp; </td>
                                            <td> ${partBJudiciaryObj.postname} </td>
                                            <td> ${partBJudiciaryObj.scaleofPay} </td>
                                            <td> ${partBJudiciaryObj.gp} </td>
                                            <td> ${partBJudiciaryObj.scaleofPay7th} </td>
                                            <td> ${partBJudiciaryObj.sanctionedStrength} </td>
                                            <td> ${partBJudiciaryObj.meninPosition} </td>
                                            <td> ${partBJudiciaryObj.vacancyPosition} </td>
                                            <td> 

                                                <span id="label_${partBJudiciaryObj.postname}">${partBJudiciaryObj.otherRemarks}</span>

                                            </td>
                                        </tr>
                                    </c:forEach>    
                                </c:if>           
                                <c:if  test="${not empty partAGrpAUGC}">
                                    <tr>
                                        <td> </td>
                                        <td colspan="8"> <h4>UGC</h4> </td>
                                    </tr>
                                    <c:forEach var="partBGrpAUGCObj" items="${partBGrpAUGC}">
                                        <tr>
                                            <td> &nbsp; </td>
                                            <td> ${partBGrpAUGCObj.postname} </td>
                                            <td> ${partBGrpAUGCObj.scaleofPay} </td>
                                            <td> ${partBGrpAUGCObj.gp} </td>
                                            <td> ${partBGrpAUGCObj.scaleofPay7th} </td>
                                            <td> ${partBGrpAUGCObj.sanctionedStrength} </td>
                                            <td> ${partBGrpAUGCObj.meninPosition} </td>
                                            <td> ${partBGrpAUGCObj.vacancyPosition} </td>
                                            <td> 

                                                <span id="label_${partBGrpAUGCObj.postname}">${partBGrpAUGCObj.otherRemarks}</span>

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
                                <td> ${partBtotalGrpASanctionedStrength} </td>
                                <td> ${partBtotalGrpAMenInPosition} </td>
                                <td> ${partBtotalGrpAVacancy} </td>
                                <td>&nbsp;</td>
                            </tr>


                                <tr>
                                    <td colspan="9"><h4> Group-B  </h4></td>
                                </tr>
                                <c:forEach var="partBGrpB" items="${partBgrpBList}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partBGrpB.postname} </td>
                                        <td> ${partBGrpB.scaleofPay} </td>
                                        <td> ${partBGrpB.gp} </td>
                                        <td> ${partBGrpB.scaleofPay7th} </td>
                                        <td> ${partBGrpB.sanctionedStrength} </td>
                                        <td> ${partBGrpB.meninPosition} </td>
                                        <td> ${partBGrpB.vacancyPosition} </td>

                                        <td>

                                            <span id="label_${partBGrpB.postname}">${partBGrpB.otherRemarks}</span>

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
                            </tr>
                                <tr>
                                    <td colspan="9"><h4> Group-C  </h4></td>
                                </tr>
                                <c:forEach var="partBGrpC" items="${partBgrpCList}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partBGrpC.postname} </td>
                                        <td> ${partBGrpC.scaleofPay} </td>
                                        <td> ${partBGrpC.gp} </td>
                                        <td> ${partBGrpC.scaleofPay7th} </td>
                                        <td> ${partBGrpC.sanctionedStrength} </td>
                                        <td> ${partBGrpC.meninPosition} </td>
                                        <td> ${partBGrpC.vacancyPosition} </td>
                                        <td>

                                            <span id="label_${partBGrpC.postname}">${partBGrpC.otherRemarks}</span>

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
                            </tr>
                                <tr>
                                    <td colspan="9"><h4> Group-D </h4> </td>
                                </tr>
                                <c:forEach var="partBGrpD" items="${partBgrpDList}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partBGrpD.postname} </td>
                                        <td> ${partBGrpD.scaleofPay} </td>
                                        <td> ${partBGrpD.gp} </td>
                                        <td> ${partBGrpD.scaleofPay7th} </td>
                                        <td> ${partBGrpD.sanctionedStrength} </td>
                                        <td> ${partBGrpD.meninPosition} </td>
                                        <td> ${partBGrpD.vacancyPosition} </td>
                                        <td>

                                            <span id="label_${partBGrpD.postname}">${partBGrpD.otherRemarks}</span>

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
                                    <td> ${partCGrp.postname} </td>
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



        </form:form>
    </body>
</html>
