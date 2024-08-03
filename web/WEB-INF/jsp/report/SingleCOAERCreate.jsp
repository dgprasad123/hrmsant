<%-- 
    Document   : SingleCOAERCreate
    Created on : 5 Jun, 2018, 12:12:59 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                if ($('#groupAData').val() == 0) {
                    $('#groupAData').val('');
                }
                if ($('#groupBData').val() == 0) {
                    $('#groupBData').val('');
                }
                if ($('#groupCData').val() == 0) {
                    $('#groupCData').val('');
                }
                if ($('#groupDData').val() == 0) {
                    $('#groupDData').val('');
                }

            });

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

            function validateForm() {
                if ($('#financialYear').val() == '') {
                    alert("Please select Financial Year");
                    return false;
                }
                if ($('#groupAData').val() == '' || $('#groupBData').val() == '' || $('#groupCData').val() == '' || $('#groupDData').val() == '') {
                    alert("Please enter at least one Sanctioned Post");
                    return false;
                }

                return true;
            }

        </script>
    </head>
    <body>

        <jsp:include page="aerTab.jsp">
            <jsp:param name="menuHighlight" value="PART_A" />
        </jsp:include>
        <form:form action="finishAER.htm" commandName="command">
            <div class="container-fluid">
                <form:hidden path="singleCO"/>
                <form:hidden path="aerId"/>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center"> ${OffName} </h1>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year ${command.financialYear}
                                    <form:hidden path="financialYear"/>
                                </h3> 
                            </div>
                        </div>    
                        <div class="row">
                            <div class="col-xs-6"><c:if test="${command.singleCO eq 'Y'}">
                                    <input type="submit" name="action" id="btnSave" value="Finish" class="btn btn-primary" onclick="return validateForm();"/>
                                </c:if>
                                <c:if test="${command.singleCO eq 'N'}">
                                    <input type="submit" name="action" id="btnSave" value="Next" class="btn btn-primary"/>
                                </c:if>
                            </div>
                            <div class="col-xs-6">
                                <div class="pull-right">
                                    <input type="submit" name="action" id="btnSave" value="Back" class="btn btn-primary"/>
                                </div>
                            </div>
                        </div>        
                    </div>

                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-6">D.D.O Code</div>
                            <div class="col-lg-6"></div>
                        </div>
                        <div class="row">
                            <div class="col-lg-6">Controlling Officer/HoDs  Code</div>
                            <div class="col-lg-6"></div>
                        </div>
                        <div class="row">
                            <div class="col-lg-6">Administrative Department Code</div>
                            <div class="col-lg-6"></div>
                        </div>
                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>
                                    <th width="10%" rowspan="2" >Category of Employee</th>
                                    <th width="25%" rowspan="2">Description of the Posts</th>
                                    <th width="25%" colspan="2">Pay Scale</th>
                                    <th  width="5%" rowspan="2">As per 7th Pay</th>
                                    <th width="8%" rowspan="2">Sanctioned Strength</th>
                                    <th width="8%" rowspan="2">Persons-in-Position</th>
                                    <th width="9%" rowspan="2">Vacancy Position </th>
                                    <th width="5%" rowspan="2">Remarks if any</th>

                                </tr>
                                <tr>
                                    <th>As per 6th Pay Commission</th>
                                    <th>GP</th>

                                </tr>
                            </thead>
                            <tr>
                                <td colspan="9"> Group-A </td>
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
                                    <td> &nbsp; </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="9"> Group-B </td>
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
                                    <td> &nbsp; </td>
                                </tr>
                            </c:forEach> 
                            <tr>
                                <td colspan="9"> Group-C </td>
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
                                    <td> &nbsp; </td>
                                </tr>
                            </c:forEach>   
                            <tr>
                                <td colspan="9"> Group-D </td>
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
                                    <td> &nbsp; </td>
                                </tr>
                            </c:forEach>       

                        </table>
                    </div>

                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-xs-6">
                                <c:if test="${command.singleCO eq 'Y'}">
                                    <input type="submit" name="action" id="btnSave" value="Finish" class="btn btn-primary"/>
                                </c:if>
                                <c:if test="${command.singleCO eq 'N'}">
                                    <input type="submit" name="action" id="btnSave" value="Next" class="btn btn-primary"/>
                                </c:if>
                            </div>
                            <div class="col-xs-6">
                                <div class="pull-right">
                                    <input type="submit" name="action" id="btnSave" value="Back" class="btn btn-primary"/>
                                </div>
                            </div>
                        </div>


                    </div>                


                </div>
            </div>
        </form:form>

    </body>
</html>
