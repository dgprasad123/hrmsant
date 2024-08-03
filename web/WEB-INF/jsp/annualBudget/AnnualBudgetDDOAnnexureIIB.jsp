<%-- 
    Document   : AnnualBudgetDDOAnnexureIIB
    Created on : 10 Nov, 2020, 10:18:16 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            function openContractualPostEditModal(detailid) {
                $("#hidDetailId").val(detailid);

                var incrDcrMenInPositionData = $("#increaseDecreaseMenInPosition" + detailid).text();
                var txtTotalMenInPositionData = $("#totalMenInPosition" + detailid).text();
                var actualExp201920Data = $("#actualExpenditure201920" + detailid).text();
                var actualExp202021Data = $("#actualExpenditure202021" + detailid).text();
                var revisedEstimate202021Data = $("#revisedEstimate202021" + detailid).text();
                var be202122Data = $("#be202122" + detailid).text();

                $("#txtIncrDcrMenInPosition").val(incrDcrMenInPositionData);
                $("#txtTotalMenInPosition").val(txtTotalMenInPositionData);
                $("#txtActualExp201920").val(actualExp201920Data);
                $("#txtActualExp202021").val(actualExp202021Data);
                $("#txtRevisedEstimate202021").val(revisedEstimate202021Data);
                $("#txtBE202122").val(be202122Data);

                $("#contractualEmployeeDataModal").modal("show");
            }
            function saveContractualData() {
                var budgetId = $("#hidBudgetId").val();
                var detailId = $("#hidDetailId").val();

                var incrDcrMenInPositionData = $("#txtIncrDcrMenInPosition").val();
                var txtTotalMenInPositionData = $("#txtTotalMenInPosition").val();
                var actualExp201920Data = $("#txtActualExp201920").val();
                var actualExp202021Data = $("#txtActualExp202021").val();
                var revisedEstimate202021Data = $("#txtRevisedEstimate202021").val();
                var be202122Data = $("#txtBE202122").val();

                $.ajax({
                    type: "GET",
                    url: "updateContractualData.htm",
                    data: {budgetid: budgetId, detailid: detailId, incrDcrMenInPosition: incrDcrMenInPositionData, actualExp201920: actualExp201920Data, actualExp202021: actualExp202021Data, revisedEstimate202021: revisedEstimate202021Data, be202122: be202122Data, txtTotalMenInPosition: txtTotalMenInPositionData},
                    success: function(data) {
                        alert("Data Saved.");
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
        </script> 
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="AnnexureTabs.jsp">
            <jsp:param name="menuHighlight" value="ANNEXIIB" />
        </jsp:include><br />
        <div style="margin-left: 50px;">


        </div>
        <input type="hidden" id="hidBudgetId" value="${budgetid}"/>
        <div style="width:70%;margin:0px auto;">
            <h3 align="right">Annexure-IIB</h3>
            <h3 align="center">Information on Contractual Employees</h3>
            <table class="table" style="margin-bottom:10px;">
                <tr>
                    <td colspan="2">D.D.O of <strong><c:out value="${ddoname}"/></strong> (Name of the Establishment)</td>
                <tr>
                    <td width="20%">Category:</td>
                    <td>(Administrative EXP/Programme Expenditure/Disaster Management/Transfer form State)</td>
                </tr>
                <tr>
                    <td width="20%">Sub Category:</td>
                    <td>E.O.M/DSE/SSS/CS/CSS/NDRF/SDRF/SFC/CFC/</td>
                </tr>
                <tr>
                    <td width="20%">Sector:</td>
                    <td>Sector/District<br />Sector</td>
                </tr> 
                <tr>
                    <td colspan="2" style="text-align:right">
                        <span>
                            <c:if test="${isLocked ne 'Y'}">
                                <a href="ReProcessBudgetAnnexureIIBData.htm?financialYear=${financialyear}&budgetid=${budgetid}" onclick="return validateReprocessBudget();">
                                    <button type="button" id="btnReprocess" class="btn btn-primary">Reprocess</button>
                                </a>
                                <span id="reprocessLoader" style="display:none;">
                                    <img src="images/loading.gif"/>Reprocessing
                                </span>
                            </c:if>&nbsp;
                            <a href="budgetListForDDOController.htm">
                                <button type="button" class="btn btn-primary">Back</button>
                            </a>
                        </span>
                    </td>
                </tr>
            </table>
            <c:if test="${not empty annexureIIBdataList}">
                <c:forEach items="${annexureIIBdataList}" var="annexureIIBdata" varStatus="counter">
                    <table class="table" style="margin-bottom:10px;">
                        <tr>
                            <td width="20%">Chart of Account:</td>
                            <td>
                                <c:out value="${annexureIIBdata.chartOfAcc}"/>
                            </td>
                        </tr>
                    </table>
                    <c:if test="${not empty annexureIIBdata.annexureIIBpostlist}">
                        <h3 align="center">Details of Contractual Employees engaged</h3>
                        <table style="font-size:12pt;" border="1">
                            <tr style="font-weight:bold;">
                                <td align="center">Name of the Posts</td>
                                <td align="center">No.of post as on 31.03.2023</td>
                                <td align="center">Increase(+) or Decrease (-) in Man in Position during 01.04.23 to 31.03.2024</td>
                                <td align="center">Total man in position as on 01.04.2024</td>
                                <td align="center">Actual Exp during 2022-23</td>
                                <td align="center">Actual Exp During 2023-24 upto____</td>
                                <td align="center">2023-24 Revised Estimate</td>
                                <td align="center">2024-25 B.E.</td>
                                <td align="center"></td>
                            </tr>
                            <c:forEach items="${annexureIIBdata.annexureIIBpostlist}" var="annexureIIBpostlist" varStatus="counter">
                                <tr>
                                    <td align="center">
                                        <c:out value="${annexureIIBpostlist.postname}"/>
                                    </td>
                                    <td align="center">
                                        <c:out value="${annexureIIBpostlist.noOfPost31032020}"/>
                                    </td>
                                    <td align="center">
                                        <span id="increaseDecreaseMenInPosition${annexureIIBpostlist.detailid}"><c:out value="${annexureIIBpostlist.increaseDecreaseMenInPosition01042020}"/></span>
                                    </td>
                                    <td align="center">
                                        <span id="totalMenInPosition${annexureIIBpostlist.detailid}"><c:out value="${annexureIIBpostlist.totalMenInPosition01042021}"/></span>
                                    </td>
                                    <td align="center">
                                        <span id="actualExpenditure201920${annexureIIBpostlist.detailid}"><c:out value="${annexureIIBpostlist.actualExpenditure201920}"/></span>
                                    </td>
                                    <td align="center">
                                        <span id="actualExpenditure202021${annexureIIBpostlist.detailid}"><c:out value="${annexureIIBpostlist.actualExpenditure202021}"/></span>
                                    </td>
                                    <td align="center">
                                        <span id="revisedEstimate202021${annexureIIBpostlist.detailid}"><c:out value="${annexureIIBpostlist.revisedEstimate202021}"/></span>
                                    </td>
                                    <td align="center">
                                        <span id="be202122${annexureIIBpostlist.detailid}"><c:out value="${annexureIIBpostlist.be202122}"/></span>
                                    </td>
                                    <td align="center">
                                        <c:if test="${isLocked ne 'Y'}">
                                            <a href="javascript:openContractualPostEditModal('<c:out value="${annexureIIBpostlist.detailid}"/>');"><i class="fa fa-pencil-square-o fa-2x"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>
                </c:forEach>
            </c:if>
        </div>
        <div id="contractualEmployeeDataModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:500px;background:#FFFFFF;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Enter Data</h4>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="hidDetailId">
                        <table class="table">
                            <tr>
                                <td>Increase(+) or Decrease (-) in Man in Position during 01.04.23 to 31.03.2024</td>
                                <td>
                                    <input type="text" name="txtIncrDcrMenInPosition" id="txtIncrDcrMenInPosition" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Total man in position as on 01.04.2024</td>
                                <td>
                                    <input type="text" name="txtTotalMenInPosition" id="txtTotalMenInPosition" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Actual Exp during 2022-23</td>
                                <td>
                                    <input type="text" name="txtActualExp201920" id="txtActualExp201920" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Actual Exp During 2023-24 upto____</td>
                                <td>
                                    <input type="text" name="txtActualExp202021" id="txtActualExp202021" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>2023-24 Revised Estimate</td>
                                <td>
                                    <input type="text" name="txtRevisedEstimate202021" id="txtRevisedEstimate202021" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                            <tr>
                                <td>2024-25 B.E.</td>
                                <td>
                                    <input type="text" name="txtBE202122" id="txtBE202122" onkeypress="return onlyIntegerRange(event)"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" onclick="saveContractualData();">Save Data</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
