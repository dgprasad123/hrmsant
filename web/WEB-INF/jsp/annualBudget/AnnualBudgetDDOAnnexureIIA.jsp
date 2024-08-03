<%-- 
    Document   : AnnualBudgetDDOAnnexureIA
    Created on : 10 Nov, 2020, 9:37:43 AM
    Author     : Surendra
--%>

<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>        
        <script type="text/javascript">
            $(document).ready(function() {

            });
            function openEmployeeEditModal(detailid) {
                $("#hidDetailId").val(detailid);

                var empid = $("#empid" + detailid).text();
                $("#hidEmpid").val(empid);

                var basic0103nextyear = $("#basic0103nextyear" + detailid).text();
                $("#txtBasicNextYear").val(basic0103nextyear);

                var da = $("#da" + detailid).text();
                $("#txtDA156").val(da);

                var hra = $("#hra" + detailid).text();
                $("#txtHRA403").val(hra);

                var arrear = $("#arrear" + detailid).text();
                $("#txtArrearPay").val(arrear);

                var oa = $("#oa" + detailid).text();
                $("#txtOA").val(oa);

                var rcm = $("#rcm" + detailid).text();
                $("#txtRCM").val(rcm);

                $("#employeeDataModal").modal("show");
            }
            function saveAnnexureIIAEmployeeData() {
                var budgetId = $("#hidBudgetId").val();
                var detailId = $("#hidDetailId").val();
                var empId = $("#hidEmpid").val();

                var basicNextYeardata = $("#txtBasicNextYear").val();
                var dadata = $("#txtDA156").val();

                var arreardata = $("#txtArrearPay").val();
                var oadata = $("#txtOA").val();
                var rcmdata = $("#txtRCM").val();
                var hraData = $("#txtHRA403").val();

                $.ajax({
                    type: "GET",
                    url: "saveAnnexureIEmployeeBudgetData.htm",
                    data: {budgetid: budgetId, detailid: detailId, arrear: arreardata, oa: oadata, rcm: rcmdata, empid: empId, basicNextYear: basicNextYeardata, da: dadata, hra: hraData},
                    success: function(data) {
                        alert('Data Saved.');
                        location.reload();
                    },
                    error: function() {
                        alert('Error Occured');
                    }
                });
            }

            function openAdditionalModal(id) {
                $('#hidAnnexureIIAPart3Id').val(id);

                var additionalamount = $("#additionalamount_" + id).text();
                $('#txtAdditionalAmount').val(additionalamount);

                $("#additionalAmountModal").modal("show");
            }
            function saveAdditionalAmountData() {
                var budgetId = $("#hidBudgetId").val();
                var detailId = $("#hidAnnexureIIAPart3Id").val();
                var txtAmount = $("#txtAdditionalAmount").val();
                $.ajax({
                    type: "GET",
                    url: "updateAdditionalAmountData.htm",
                    data: {budgetid: budgetId, detailid: detailId, amount: txtAmount},
                    success: function(data) {
                        alert('Data Saved.');
                        location.reload();
                    },
                    error: function() {
                        alert('Error Occured');
                    }
                });
            }

            function openExclusionModal(id) {
                $('#hidAnnexureIIAPart3Id').val(id);

                var exclusionamount = $("#exclusionamount_" + id).text();
                $('#txtExclusionAmount').val(exclusionamount);

                $("#exclusionAmountModal").modal("show");
            }
            function saveExclusionsData() {
                var budgetId = $("#hidBudgetId").val();
                var detailId = $("#hidAnnexureIIAPart3Id").val();
                var txtAmount = $("#txtExclusionAmount").val();
                $.ajax({
                    type: "GET",
                    url: "updateExclusionsData.htm",
                    data: {budgetid: budgetId, detailid: detailId, amount: txtAmount},
                    success: function(data) {
                        alert('Data Saved.');
                        location.reload();
                    },
                    error: function() {
                        alert('Error Occured');
                    }
                });
            }

            function openAbstractEditModal(id) {
                $("#hidAbstractId").val(id);

                var sanctionedstrength = $("#sanctionedStrength" + id).text();
                $('#hidSanctionedStrength').val(sanctionedstrength);

                var currentVacancy = $("#vacancy01032020" + id).text();
                $('#txtCurrentVacancy').val(currentVacancy);

                var anticipatedVacancy = $("#anticipatedVacancy01032020" + id).text();
                $('#txtanticipatedVacancy').val(anticipatedVacancy);

                var vacancytobefilled = $("#vacancytobefilled" + id).text();
                $('#txtvacancytobefilled').val(vacancytobefilled);

                var anticipatedMenInPosition = $("#anticipatedMenInPosition" + id).text();
                $('#txtanticipatedMenInPosition').val(anticipatedMenInPosition);

                $("#abstractModal").modal("show");
            }

            function saveBudgetAbstractData() {
                var budgetId = $("#hidBudgetId").val();
                var detailId = $("#hidAbstractId").val();

                var sanctionedStrengthData = $("#hidSanctionedStrength").val();

                var currentVacancyData = $("#txtCurrentVacancy").val();
                var anticipatedVacancyData = $("#txtanticipatedVacancy").val();
                var vacancytobefilledData = $("#txtvacancytobefilled").val();
                var anticipatedMenInPositionData = $("#txtanticipatedMenInPosition").val();

                $.ajax({
                    type: "GET",
                    url: "updateBudgetAbstractData.htm",
                    data: {budgetid: budgetId, detailid: detailId, anticipatedVacancy: anticipatedVacancyData, vacancytobefilled: vacancytobefilledData, anticipatedMenInPosition: anticipatedMenInPositionData, currentVacancy: currentVacancyData, sanctionedStrength: sanctionedStrengthData},
                    success: function(data) {
                        alert('Data Saved.');
                        location.reload();
                    },
                    error: function() {
                        alert('Error Occured');
                    }
                });
            }

            function onlyIntegerRange(e) {
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

            function validateReprocessBudget() {
                if (confirm("Are you sure to Reprocess?")) {
                    $("#btnReprocess").hide();
                    $("#reprocessLoader").show();
                } else {
                    return false;
                }
            }

            function openUploadWindow(type) {
                $("#uploadDedType").val(type);
                var dedtypetext = type;
                if (type == "PAYNEXT") {
                    dedtypetext = "Basic Pay Next Year";
                } else if (type == "ARR") {
                    dedtypetext = "Arrear Pay";
                }
                $("#dedname").text(dedtypetext);
                $("#uploadModal").modal("show");
            }

            function validateUpload() {
                if ($("#uploadedBudgetFile").val() == "") {
                    alert("Please Upload File");
                    return false;
                } else {
                    if (confirm("Are you sure to Upload?")) {
                        $("#btnUpload").hide();
                        $("#uploadLoader").show();
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        </script>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="AnnexureTabs.jsp">
            <jsp:param name="menuHighlight" value="ANNEXIIA" />
        </jsp:include><br />
        <div style="margin-left: 50px;">


        </div>
        <input type="hidden" id="hidBudgetId" value="${budgetid}"/>
        <div style="width:70%;margin:0px auto;">
            <h3 align="right">Annexure-IIA</h3>
            <h3 align="center">Sanctioned Strength, Vacancies and Person-in-position and salary requirement</h3>
            <table style="margin-bottom:10px;">
                <tr>
                    <td colspan="2">D.D.O of <strong><c:out value="${ddoname}"/></strong> (Name of the Establishment)</td>
                <tr>
                    <td width="20%">Category:</td>
                    <td>(Administrative EXP/Programme Expenditure/Disaster Management/Transfer from State)</td>
                </tr>
                <tr>
                    <td width="20%">Sub Category:</td>
                    <td>E.O.M/DSE/SSS/CS/CSS/NDRF/SDRF/SFC/CFC/</td>
                </tr>
                <tr>
                    <td width="20%">Sector:</td>
                    <td>State Sector/District<br />Sector</td>
                    <td style="text-align:right"> <span>
                            <c:if test="${isLocked ne 'Y'}">
                                <a href="ReProcessBudgetAnnexureIIAData.htm?financialYear=${financialyear}&budgetid=${budgetid}">
                                    <button type="button" id="btnReprocess" class="btn btn-primary" onclick="return validateReprocessBudget();">Reprocess</button>
                                </a>
                                <span id="reprocessLoader" style="display:none;">
                                    <img src="images/loading.gif"/>Reprocessing
                                </span>
                            </c:if>

                            <a href="budgetListForDDOController.htm">
                                <button type="button" class="btn btn-primary">Back</button>
                            </a>

                        </span>&nbsp;
                    </td>
                </tr>
            </table>
            <c:forEach items="${annexureIIAdataList}" var="annexureIIAdata" varStatus="counter">
                <div class="clearfix" style="height:30px;"></div>
                <table style="font-size:12pt;" border="1">
                    <tr>
                        <td width="20%" style="background-color: #D9F2FF;">Chart of Account:</td>
                        <td colspan="8" align="center" style="background-color: #D9F2FF;">
                            <c:out value="${annexureIIAdata.chartOfAcc}"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="9">
                            <h3 align="center">Abstract of Employee Strength</h3>
                        </td>
                    </tr>
                    <tr style="font-weight:bold;">
                        <td align="center">Sl No.</td>
                        <td align="center" width="15%">Group</td>
                        <td align="center">Sanctioned Strength</td>
                        <td align="center">Vacancy as on 01.03.2023</td>
                        <td align="center">Anticipated vacancy from 01.03.2023 to 01.03.2024</td>
                        <td align="center">Total Vacancy(4+5)</td>
                        <td align="center">Men in position as on 01.03.2024</td>
                        <td align="center">Vacancy likely to be filled up (+)/arise due to retirement etc.(-) during the next year</td>
                        <td align="center">Anticipated Men in position for whom budget provision is proposed</td>
                        <td align="center">Action</td>
                    </tr>
                    <tr>
                        <td align="center">1</td>
                        <td align="center">2</td>
                        <td align="center">3</td>
                        <td align="center">4</td>
                        <td align="center">5</td>
                        <td align="center">6</td>
                        <td align="center">7</td>
                        <td align="center">8</td>
                        <td align="center">9</td>
                        <td align="center">&nbsp;</td>
                    </tr>

                    <c:if test="${not empty annexureIIAdata.annexureIIAPart1GroupDetails}">
                        <c:forEach items="${annexureIIAdata.annexureIIAPart1GroupDetails}" var="groupdata">
                            <c:if test="${not empty groupdata.group && groupdata.group eq 'A'}">
                                <tr>
                                    <td align="right" style="padding-right:7px;">1</td>
                                    <td align="center">Group A</td>
                                    <td align="center" id="sanctionedStrength${groupdata.groupdetailid}"><c:out value="${groupdata.sanctionedStrength}"/></td>
                                    <td align="center" id="vacancy01032020${groupdata.groupdetailid}"><c:out value="${groupdata.vacancy01032020}"/></td>
                                    <td align="center" id="anticipatedVacancy01032020${groupdata.groupdetailid}"><c:out value="${groupdata.anticipatedVacancy01032020}"/></td>
                                    <td align="center">
                                        <c:out value="${groupdata.totalVacany}"/>
                                    </td>
                                    <td align="center">
                                        <c:out value="${groupdata.menInPosition01032020}"/>
                                    </td>
                                    <td align="center" id="vacancytobefilled${groupdata.groupdetailid}"><c:out value="${groupdata.vacancytobefilled}"/></td>
                                    <td align="center" id="anticipatedMenInPosition${groupdata.groupdetailid}"><c:out value="${groupdata.anticipatedMenInPosition}"/></td>
                                    <td align="center">
                                        <c:if test="${isLocked ne 'Y'}">
                                            <a href="javascript:openAbstractEditModal('<c:out value="${groupdata.groupdetailid}"/>');"><i class="fa fa-pencil-square-o fa-2x"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty groupdata.group && groupdata.group eq 'B'}">
                                <tr>
                                    <td align="right" style="padding-right:7px;">2</td>
                                    <td align="center">Group B</td>
                                    <td align="center" id="sanctionedStrength${groupdata.groupdetailid}"><c:out value="${groupdata.sanctionedStrength}"/></td>
                                    <td align="center" id="vacancy01032020${groupdata.groupdetailid}"><c:out value="${groupdata.vacancy01032020}"/></td>
                                    <td align="center" id="anticipatedVacancy01032020${groupdata.groupdetailid}"><c:out value="${groupdata.anticipatedVacancy01032020}"/></td>
                                    <td align="center">
                                        <c:out value="${groupdata.totalVacany}"/>
                                    </td>
                                    <td align="center">
                                        <c:out value="${groupdata.menInPosition01032020}"/>
                                    </td>
                                    <td align="center" id="vacancytobefilled${groupdata.groupdetailid}"><c:out value="${groupdata.vacancytobefilled}"/></td>
                                    <td align="center" id="anticipatedMenInPosition${groupdata.groupdetailid}"><c:out value="${groupdata.anticipatedMenInPosition}"/></td>
                                    <td align="center">
                                        <c:if test="${isLocked ne 'Y'}">
                                            <a href="javascript:openAbstractEditModal('<c:out value="${groupdata.groupdetailid}"/>');"><i class="fa fa-pencil-square-o fa-2x"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty groupdata.group && groupdata.group eq 'C'}">
                                <tr>
                                    <td align="right" style="padding-right:7px;">3</td>
                                    <td align="center">Group C</td>
                                    <td align="center" id="sanctionedStrength${groupdata.groupdetailid}"><c:out value="${groupdata.sanctionedStrength}"/></td>
                                    <td align="center" id="vacancy01032020${groupdata.groupdetailid}"><c:out value="${groupdata.vacancy01032020}"/></td>
                                    <td align="center" id="anticipatedVacancy01032020${groupdata.groupdetailid}"><c:out value="${groupdata.anticipatedVacancy01032020}"/></td>
                                    <td align="center">
                                        <c:out value="${groupdata.totalVacany}"/>
                                    </td>
                                    <td align="center">
                                        <c:out value="${groupdata.menInPosition01032020}"/>
                                    </td>
                                    <td align="center" id="vacancytobefilled${groupdata.groupdetailid}"><c:out value="${groupdata.vacancytobefilled}"/></td>
                                    <td align="center" id="anticipatedMenInPosition${groupdata.groupdetailid}"><c:out value="${groupdata.anticipatedMenInPosition}"/></td>
                                    <td align="center">
                                        <c:if test="${isLocked ne 'Y'}">
                                            <a href="javascript:openAbstractEditModal('<c:out value="${groupdata.groupdetailid}"/>');"><i class="fa fa-pencil-square-o fa-2x"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty groupdata.group && groupdata.group eq 'D'}">
                                <tr>
                                    <td align="right" style="padding-right:7px;">4</td>
                                    <td align="center">Group D</td>
                                    <td align="center" id="sanctionedStrength${groupdata.groupdetailid}"><c:out value="${groupdata.sanctionedStrength}"/></td>
                                    <td align="center" id="vacancy01032020${groupdata.groupdetailid}"><c:out value="${groupdata.vacancy01032020}"/></td>
                                    <td align="center" id="anticipatedVacancy01032020${groupdata.groupdetailid}"><c:out value="${groupdata.anticipatedVacancy01032020}"/></td>
                                    <td align="center">
                                        <c:out value="${groupdata.totalVacany}"/>
                                    </td>
                                    <td align="center">
                                        <c:out value="${groupdata.menInPosition01032020}"/>
                                    </td>
                                    <td align="center" id="vacancytobefilled${groupdata.groupdetailid}"><c:out value="${groupdata.vacancytobefilled}"/></td>
                                    <td align="center" id="anticipatedMenInPosition${groupdata.groupdetailid}"><c:out value="${groupdata.anticipatedMenInPosition}"/></td>
                                    <td align="center">
                                        <c:if test="${isLocked ne 'Y'}">
                                            <a href="javascript:openAbstractEditModal('<c:out value="${groupdata.groupdetailid}"/>');"><i class="fa fa-pencil-square-o fa-2x"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:if>

                    <c:if test="${not empty annexureIIAdata.annexureIIAPart1GroupDetails}">
                        <c:set var="rowCount" value="0" />
                        <c:set var="totalSanctionedStrength" value="0" />
                        <c:set var="totalVacancy01032020" value="0" />
                        <c:set var="totalAnticipatedVacancy01032020" value="0" />
                        <c:set var="totalTotalVacancy" value="0" />
                        <c:set var="totalMenInPosition01032020" value="0" />
                        <c:set var="totalVacancyToBeFilled" value="0" />
                        <c:set var="totalAnticipatedMenInPosition" value="0" />

                        <c:forEach items="${annexureIIAdata.annexureIIAPart1GroupDetails}" var="groupdata">
                            <c:set var="rowCount" value="${rowCount + 1}" />

                            <c:set var="totalSanctionedStrength" value="${totalSanctionedStrength + groupdata.sanctionedStrength}" />
                            <c:set var="totalVacancy01032020" value="${totalVacancy01032020 + groupdata.vacancy01032020}" />
                            <c:set var="totalAnticipatedVacancy01032020" value="${totalAnticipatedVacancy01032020 + groupdata.anticipatedVacancy01032020}" />
                            <c:set var="totalTotalVacancy" value="${totalTotalVacancy + groupdata.totalVacany}" />
                            <c:set var="totalMenInPosition01032020" value="${totalMenInPosition01032020 + groupdata.menInPosition01032020}" />
                            <c:set var="totalVacancyToBeFilled" value="${totalVacancyToBeFilled + groupdata.vacancytobefilled}" />
                            <c:set var="totalAnticipatedMenInPosition" value="${totalAnticipatedMenInPosition + groupdata.anticipatedMenInPosition}" />
                        </c:forEach>
                    </c:if>

                    <tr style="background-color: lightgreen;">
                        <td colspan="2" style="text-align: center; font-weight: bold;">Total</td>
                        <td align="center" id="totalSanctionedStrength" style="font-weight: bold;">${totalSanctionedStrength}</td>
                        <td align="center" id="totalVacancy01032020" style="font-weight: bold;">${totalVacancy01032020}</td>
                        <td align="center" id="totalAnticipatedVacancy01032020" style="font-weight: bold;">${totalAnticipatedVacancy01032020}</td>
                        <td align="center" id="totalTotalVacancy" style="font-weight: bold;">${totalTotalVacancy}</td>
                        <td align="center" id="totalMenInPosition01032020" style="font-weight: bold;">${totalMenInPosition01032020}</td>
                        <td align="center" id="totalVacancyToBeFilled" style="font-weight: bold;">${totalVacancyToBeFilled}</td>
                        <td colspan="2" align="center" id="totalAnticipatedMenInPosition" style="font-weight: bold;padding-right: 50px;">${totalAnticipatedMenInPosition}</td>
                    </tr>

                </table>
                <c:if test="${not empty annexureIIAdata.annexureIIAPart2EmployeeDetails}">
                    <table style="font-size:12pt;" border="1" style="margin-top:30px;">
                        <tr style="font-weight:bold;">
                            <td colspan="14">
                                <h3 align="center">Detailed calculation of Salary</h3>
                            </td>
                        </tr>
                        <tr style="font-weight:bold;">
                            <td colspan="14">
                                <span class="glyphicon glyphicon-download" style="color:red;"></span>Download&nbsp;
                                <span class="glyphicon glyphicon-upload" style="color:green;"></span>Upload&nbsp;
                                (<span style="color: #F00000;">
                                    Please Do Not change Key Id and HRMS ID value in downloaded Excel Sheet.
                                </span>)
                            </td>
                        </tr>
                        <tr style="font-weight:bold;">
                            <td colspan="5">&nbsp;</td>
                            <td align="center" width="10%">
                                <a href="downloadAnnualBudgetExcel.htm?budgetid=${budgetid}&chartacc=<c:out value="${annexureIIAdata.chartOfAcc}"/>&type=PAYNEXT">
                                    <span class="glyphicon glyphicon-download" style="color:red;"></span>
                                </a>
                                <a href="javascript:void(0);" onclick="openUploadWindow('PAYNEXT');">
                                    <span class="glyphicon glyphicon-upload" style="color:green;"></span>
                                </a>
                            </td>
                            <td align="center" width="13%">&nbsp;</td>
                            <td align="center">
                                <a href="downloadAnnualBudgetExcel.htm?budgetid=${budgetid}&chartacc=<c:out value="${annexureIIAdata.chartOfAcc}"/>&type=ARR">
                                    <span class="glyphicon glyphicon-download" style="color:red;"></span>
                                </a>
                                <a href="javascript:void(0);" onclick="openUploadWindow('ARR');">
                                    <span class="glyphicon glyphicon-upload" style="color:green;"></span>
                                </a>
                            </td>
                            <td align="center" width="7%">
                                <a href="downloadAnnualBudgetExcel.htm?budgetid=${budgetid}&chartacc=<c:out value="${annexureIIAdata.chartOfAcc}"/>&type=DA">
                                    <span class="glyphicon glyphicon-download" style="color:red;"></span>
                                </a>
                                <a href="javascript:void(0);" onclick="openUploadWindow('DA');">
                                    <span class="glyphicon glyphicon-upload" style="color:green;"></span>
                                </a>
                            </td>
                            <td align="center" width="7%">
                                <a href="downloadAnnualBudgetExcel.htm?budgetid=${budgetid}&chartacc=<c:out value="${annexureIIAdata.chartOfAcc}"/>&type=HRA">
                                    <span class="glyphicon glyphicon-download" style="color:red;"></span>
                                </a>
                                <a href="javascript:void(0);" onclick="openUploadWindow('HRA');">
                                    <span class="glyphicon glyphicon-upload" style="color:green;"></span>
                                </a>
                            </td>
                            <td align="center" width="7%">
                                <a href="downloadAnnualBudgetExcel.htm?budgetid=${budgetid}&chartacc=<c:out value="${annexureIIAdata.chartOfAcc}"/>&type=OA">
                                    <span class="glyphicon glyphicon-download" style="color:red;"></span>
                                </a>
                                <a href="javascript:void(0);" onclick="openUploadWindow('OA');">
                                    <span class="glyphicon glyphicon-upload" style="color:green;"></span>
                                </a>
                            </td>
                            <td align="center" width="7%">
                                <a href="downloadAnnualBudgetExcel.htm?budgetid=${budgetid}&chartacc=<c:out value="${annexureIIAdata.chartOfAcc}"/>&type=RCM">
                                    <span class="glyphicon glyphicon-download" style="color:red;"></span>
                                </a>
                                <a href="javascript:void(0);" onclick="openUploadWindow('RCM');">
                                    <span class="glyphicon glyphicon-upload" style="color:green;"></span>
                                </a>
                            </td>
                            <td align="center" width="7%">&nbsp;</td>
                            <td align="center" width="7%">&nbsp;</td>
                        </tr>
                        <tr style="font-weight:bold;">
                            <td align="center">Sl No.</td>
                            <td align="center" width="15%">Name of the incumbent</td>
                            <td align="center" width="7%">Employee<br />ID</td>
                            <td align="center" width="5%">Group</td>
                            <td align="center" width="7%">Current Basic Pay/<br />Grade Pay</td>
                            <td align="center" width="10%">Basic Pay(as on 1st March Next year)</td>
                            <td align="center" width="13%">Total yearly Requirement under Pay(136)(Col.5 x 12)</td>
                            <td align="center">Arrear Pay-855</td>
                            <td align="center" width="7%">DA<br />156</td>
                            <td align="center" width="7%">HRA<br />403</td>
                            <td align="center" width="7%">OA<br />523</td>
                            <td align="center" width="7%">RCM<br />516</td>
                            <td align="center" width="7%">Total</td>
                            <td align="center" width="7%">Action</td>
                        </tr>
                        <tr>
                            <td align="center">1</td>
                            <td align="center">2</td>
                            <td align="center">3</td>
                            <td align="center">4</td>
                            <td align="center">5</td>
                            <td align="center">6</td>
                            <td align="center">7</td>
                            <td align="center">8</td>
                            <td align="center">9</td>
                            <td align="center">10</td>
                            <td align="center">11</td>
                            <td align="center">12</td>
                            <td align="center">13</td>
                            <td align="center"></td>
                        </tr>
                        <c:forEach items="${annexureIIAdata.annexureIIAPart2EmployeeDetails}" var="employeedata" varStatus="counter">
                            <tr>
                                <td align="center">
                                    ${counter.count}
                                </td>
                                <td style="padding-left:10px;">
                                    <c:out value="${employeedata.empname}"/>
                                </td>
                                <td align="center" id="empid<c:out value="${employeedata.detailid}"/>"><c:out value="${employeedata.empid}"/></td>
                                <td align="center">
                                    <c:out value="${employeedata.group}"/>
                                </td>
                                <td align="center">
                                    <c:out value="${employeedata.curBasicPay}"/> /<br /><c:out value="${employeedata.gp}"/>
                                </td>
                                <td align="center" id="basic0103nextyear<c:out value="${employeedata.detailid}"/>"><c:out value="${employeedata.basic0103nextyear}"/></td>
                                <td align="center">
                                    <c:out value="${employeedata.totalYearlyRequirementPay136}"/>
                                </td>
                                <td align="center" id="arrear<c:out value="${employeedata.detailid}"/>"><c:out value="${employeedata.arrearPay855}"/></td>
                                <td align="center" id="da<c:out value="${employeedata.detailid}"/>"><c:out value="${employeedata.da156}"/></td>
                                <td align="center" id="hra<c:out value="${employeedata.detailid}"/>"><c:out value="${employeedata.hra403}"/></td>
                                <td align="center" id="oa<c:out value="${employeedata.detailid}"/>"><c:out value="${employeedata.oa523}"/></td>
                                <td align="center" id="rcm<c:out value="${employeedata.detailid}"/>"><c:out value="${employeedata.rcm516}"/></td>
                                <td align="center" id="total<c:out value="${employeedata.detailid}"/>"><c:out value="${employeedata.total}"/></td>
                                <td align="center">
                                    <c:if test="${isLocked ne 'Y'}">
                                        <a href="javascript:openEmployeeEditModal('<c:out value="${employeedata.detailid}"/>');"><i class="fa fa-pencil-square-o fa-2x"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td align="center">Total</td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.curBasicTotal}"/>
                            </td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.basic0103nextyearTotal}"/>
                            </td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.totalYearlyRequirementPay136Total}"/>
                            </td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.arrearPay855Total}"/>
                            </td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.da156Total}"/>
                            </td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.hra403Total}"/>
                            </td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.oa523Total}"/>
                            </td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.rcm516Total}"/>
                            </td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.totalTotal}"/>
                            </td>
                            <td align="center">&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="center">&nbsp;</td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="center">1</td>
                            <td align="center" colspan="3">Additional Amount due to increment/Arrear Pay not drawn earlier(+)</td>
                            <td align="center" id="additionalamount_<c:out value="${annexureIIAdata.annexureIIAPart3DetailId}"/>"><c:out value="${annexureIIAdata.additionalAmount}"/></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center">
                                <c:if test="${isLocked ne 'Y'}">
                                    <a href="javascript:openAdditionalModal('<c:out value="${annexureIIAdata.annexureIIAPart3DetailId}"/>');"><i class="fa fa-pencil-square-o fa-2x"></i>
                                    </a>
                                </c:if>
                            </td>
                        </tr>   
                        <tr>
                            <td align="center">2</td>
                            <td align="center" colspan="3">Exclusions for incumbents likely to be absent or on deputation(-)</td>
                            <td align="center" id="exclusionamount_<c:out value="${annexureIIAdata.annexureIIAPart3DetailId}"/>"><c:out value="${annexureIIAdata.exclusionAmount}"/></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center">
                                <c:if test="${isLocked ne 'Y'}">
                                    <a href="javascript:openExclusionModal('<c:out value="${annexureIIAdata.annexureIIAPart3DetailId}"/>');"><i class="fa fa-pencil-square-o fa-2x"></i>
                                    </a>
                                </c:if>
                            </td>
                        </tr>   
                        <tr>
                            <td align="center" colspan="4">Total Provision</td>
                            <td align="center">
                                <c:out value="${annexureIIAdata.totalAdditionalExclusionAmount}"/>
                            </td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                        </tr>
                    </table>
                </c:if>
            </c:forEach>
        </div>
        <div id="employeeDataModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:500px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Enter Data</h4>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="hidDetailId">
                        <input type="hidden" id="hidEmpid">
                        <table class="table">
                            <tr>
                                <td>Basic Pay(as on 1st March Next year)</td>
                                <td>
                                    <input type="text" name="txtBasicNextYear" id="txtBasicNextYear" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>DA 156</td>
                                <td>
                                    <input type="text" name="txtDA156" id="txtDA156" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>HRA 403</td>
                                <td>
                                    <input type="text" name="txtHRA403" id="txtHRA403" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Arrear Pay 855</td>
                                <td>
                                    <input type="text" name="txtArrearPay" id="txtArrearPay" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>OA 523</td>
                                <td>
                                    <input type="text" name="txtOA" id="txtOA" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>RCM 516</td>
                                <td>
                                    <input type="text" name="txtRCM" id="txtRCM" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" onclick="saveAnnexureIIAEmployeeData();">Save Data</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" id="hidAnnexureIIAPart3Id">
        <div id="additionalAmountModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:600px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Enter Data</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table">
                            <tr>
                                <td>Additional Amount due to increment/Arrear Pay not drawn earlier(+)</td>
                                <td>
                                    <input type="text" name="txtAdditionalAmount" id="txtAdditionalAmount" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" onclick="saveAdditionalAmountData();">Save Data</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="exclusionAmountModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:600px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Enter Data</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table">
                            <tr>
                                <td>Exclusions for incumbents likely to be absent or on deputation(-)</td>
                                <td>
                                    <input type="text" name="txtExclusionAmount" id="txtExclusionAmount" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" onclick="saveExclusionsData();">Save Data</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" id="hidAbstractId">
        <input type="hidden" id="hidSanctionedStrength">
        <div id="abstractModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:600px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Enter Data</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table">
                            <tr>
                                <td>Vacancy as on 01.03.2023</td>
                                <td>
                                    <input type="text" name="txtCurrentVacancy" id="txtCurrentVacancy" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Anticipated vacancy from 01.03.2023 to 01.03.2024</td>
                                <td>
                                    <input type="text" name="txtanticipatedVacancy" id="txtanticipatedVacancy" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Vacancy likely to be filled up (+)/arise due to retirement etc.(-) during the next year</td>
                                <td>
                                    <input type="text" name="txtvacancytobefilled" id="txtvacancytobefilled" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Anticipated Men in position for whom budget provision is proposed</td>
                                <td>
                                    <input type="text" name="txtanticipatedMenInPosition" id="txtanticipatedMenInPosition" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" onclick="saveBudgetAbstractData();">Save Data</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <form:form action="uploadAnnualBudgetExcel.htm" commandName="annualBudgetForm" enctype="multipart/form-data">
            <form:hidden path="budgetid" value="${budgetid}"/>
            <form:hidden path="uploadDedType" id="uploadDedType"/>
            <div id="uploadModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:600px;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Upload <span id="dedname">File</span></h4>
                        </div>
                        <div class="modal-body">
                            <input type="file" name="uploadedBudgetFile" id="uploadedBudgetFile" accept=".xls,.xlsx"/>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" id="btnUpload" class="btn btn-success" onclick="return validateUpload();">Upload</button>
                            <span id="uploadLoader" style="display:none;">
                                <img src="images/loading.gif" width="20" height="20"/>Uploading
                            </span>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
