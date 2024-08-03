<%-- 
    Document   : ManageSanctionPost
    Created on : 17 May, 2019, 6:22:40 PM
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
            function openModal(mode, offcode, gpc, payscale_6th, level, post_group, gp) {
                //alert("Mode is: "+mode);
                $("#offCode").val(offcode);
                $('#gpc').val(gpc);
                $('#mode').val(mode);
                $('#scaleofPay').val(payscale_6th);
                $('#postgrp').val(post_group);
                $('#hidPostGrp').val(post_group);
                $('#paylevel').val(level);
                $('#gp').val(gp);

                $('#substantivePostModal').modal("show");
            }

            function verifyUpdate() {
                if ($('#gp').val() != '' && $('#gp').val() > 10000) {
                    alert("Grade Pay must be within 10000.");
                    return false;
                }
                if ($('#paylevel').val() == '') {
                    alert("Please select Level.");
                    return false;
                }

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
        <jsp:include page="aerTab.jsp">
            <jsp:param name="menuHighlight" value="PART_A" />            
        </jsp:include>
        <form:form action="createAER.htm" method="POST" commandName="command">
            <form:hidden path="aerId" id="aerId"/>
            <form:hidden path="gpc" id="gpc"/>
            <form:hidden path="offCode" id="offCode"/>
            <form:hidden path="hidPostGrp" id="hidPostGrp"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading"></div>
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
                                    <th  width="5%" rowspan="2">As per 7th Pay Commission</th>
                                    <th width="8%" rowspan="2">Sanctioned Strength</th>
                                    <th width="8%" rowspan="2">Persons-in-Position</th>
                                    <th width="9%" rowspan="2">Vacancy Position </th>
                                    <th width="5%" rowspan="2"> </th>
                                </tr>
                                <tr>
                                    <th>As per 6th Pay Commission</th>
                                    <th>GP</th>
                                </tr>
                            </thead>

                            <c:if  test="${not empty partAGrpAAllIndiaService}">
                                <tr>
                                    <td> </td>
                                    <td colspan="8"> <h4>All India Services</h4> </td>
                                </tr>
                                <c:forEach var="partAGrpAAllIndiaService" items="${partAGrpAAllIndiaService}">
                                    <tr>
                                        <td> &nbsp; </td>
                                        <td> ${partAGrpAAllIndiaService.gpc}&nbsp;<a href="ViewMappedSectionAgainstGPC.htm?postcode=${partAGrpAAllIndiaService.postname}" target="_blank">View</a></td>
                                        <td> ${partAGrpAAllIndiaService.scaleofPay} </td>
                                        <td> ${partAGrpAAllIndiaService.gp} </td>
                                        <td> ${partAGrpAAllIndiaService.scaleofPay7th} </td>
                                        <td> ${partAGrpAAllIndiaService.sanctionedStrength} </td>
                                        <td> ${partAGrpAAllIndiaService.meninPosition} </td>
                                        <td> ${partAGrpAAllIndiaService.vacancyPosition} </td>
                                        <td> 

                                            <a href="javascript:void(0);" onclick="openModal('single', '<c:out value="${partAGrpAAllIndiaService.offCode}"/>', '<c:out value="${partAGrpAAllIndiaService.postname}"/>', '<c:out value="${partAGrpAAllIndiaService.scaleofPay}"/>', '<c:out value="${partAGrpAAllIndiaService.scaleofPay7th}"/>', '<c:out value="${partAGrpAAllIndiaService.group}"/>', '<c:out value="${partAGrpAAllIndiaService.gp}"/>');">Edit</a> 

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
                                        <td> ${partAJudiciaryObj.gpc}&nbsp;<a href="ViewMappedSectionAgainstGPC.htm?postcode=${partAJudiciaryObj.postname}" target="_blank">View</a> </td>
                                        <td> ${partAJudiciaryObj.scaleofPay} </td>
                                        <td> ${partAJudiciaryObj.gp} </td>
                                        <td> ${partAJudiciaryObj.scaleofPay7th} </td>
                                        <td> ${partAJudiciaryObj.sanctionedStrength} </td>
                                        <td> ${partAJudiciaryObj.meninPosition} </td>
                                        <td> ${partAJudiciaryObj.vacancyPosition} </td>
                                        <td> 

                                            <a href="javascript:void(0);" onclick="openModal('single', '<c:out value="${partAJudiciaryObj.offCode}"/>', '<c:out value="${partAJudiciaryObj.postname}"/>', '<c:out value="${partAJudiciaryObj.scaleofPay}"/>', '<c:out value="${partAJudiciaryObj.scaleofPay7th}"/>', '<c:out value="${partAJudiciaryObj.group}"/>', '<c:out value="${partAJudiciaryObj.gp}"/>');">Edit</a> 

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
                                        <td> ${partAGrpAUGCObj.gpc} &nbsp;<a href="ViewMappedSectionAgainstGPC.htm?postcode=${partAGrpAUGCObj.postname}" target="_blank">View</a></td>
                                        <td> ${partAGrpAUGCObj.scaleofPay} </td>
                                        <td> ${partAGrpAUGCObj.gp} </td>
                                        <td> ${partAGrpAUGCObj.scaleofPay7th} </td>
                                        <td> ${partAGrpAUGCObj.sanctionedStrength} </td>
                                        <td> ${partAGrpAUGCObj.meninPosition} </td>
                                        <td> ${partAGrpAUGCObj.vacancyPosition} </td>
                                        <td> 

                                            <a href="javascript:void(0);" onclick="openModal('single', '<c:out value="${partAGrpAUGCObj.offCode}"/>', '<c:out value="${partAGrpAUGCObj.postname}"/>', '<c:out value="${partAGrpAUGCObj.scaleofPay}"/>', '<c:out value="${partAGrpAUGCObj.scaleofPay7th}"/>', '<c:out value="${partAGrpAUGCObj.group}"/>', '<c:out value="${partAGrpAUGCObj.gp}"/>');">Edit</a> 

                                        </td>
                                    </tr>
                                </c:forEach>    
                            </c:if>

                            <tr>
                                <td>  <h4>Group-A </h4></td>
                                <td colspan="8"> <h4>Other than UGC /Judiciary/ All India Services </h4></td>
                            </tr>
                            <c:forEach var="partAGrpA" items="${PartAGroupAlist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpA.gpc} &nbsp;<a href="ViewMappedSectionAgainstGPC.htm?postcode=${partAGrpA.postname}" target="_blank">View</a></td>
                                    <td> ${partAGrpA.scaleofPay} </td>
                                    <td> ${partAGrpA.gp} </td>
                                    <td> ${partAGrpA.scaleofPay7th} </td>
                                    <td> ${partAGrpA.sanctionedStrength} </td>
                                    <td> ${partAGrpA.meninPosition} </td>
                                    <td> ${partAGrpA.vacancyPosition} </td>
                                    <td> 

                                        <a href="javascript:void(0);" onclick="openModal('single', '<c:out value="${partAGrpA.offCode}"/>', '<c:out value="${partAGrpA.postname}"/>', '<c:out value="${partAGrpA.scaleofPay}"/>', '<c:out value="${partAGrpA.scaleofPay7th}"/>', '<c:out value="${partAGrpA.group}"/>', '<c:out value="${partAGrpA.gp}"/>');">Edit</a> 

                                    </td>
                                </tr>
                            </c:forEach>


                            <tr>
                                <td colspan="9"><h4> Group-B  </h4></td>
                            </tr>
                            <c:forEach var="partAGrpB" items="${PartAGroupBlist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpB.gpc} &nbsp;<a href="ViewMappedSectionAgainstGPC.htm?postcode=${partAGrpB.postname}" target="_blank">View</a></td>
                                    <td> ${partAGrpB.scaleofPay} </td>
                                    <td> ${partAGrpB.gp} </td>
                                    <td> ${partAGrpB.scaleofPay7th} </td>
                                    <td> ${partAGrpB.sanctionedStrength} </td>
                                    <td> ${partAGrpB.meninPosition} </td>
                                    <td> ${partAGrpB.vacancyPosition} </td>

                                    <td>

                                        <a href="javascript:void(0);" onclick="openModal('single', '<c:out value="${partAGrpB.offCode}"/>', '<c:out value="${partAGrpB.postname}"/>', '<c:out value="${partAGrpB.scaleofPay}"/>', '<c:out value="${partAGrpB.scaleofPay7th}"/>', '<c:out value="${partAGrpB.group}"/>', '<c:out value="${partAGrpB.gp}"/>');">Edit</a> 

                                    </td>

                                </tr>
                            </c:forEach> 
                            <tr>
                                <td colspan="9"><h4> Group-C  </h4></td>
                            </tr>
                            <c:forEach var="partAGrpC" items="${PartAGroupClist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpC.gpc} &nbsp;<a href="ViewMappedSectionAgainstGPC.htm?postcode=${partAGrpC.postname}" target="_blank">View</a></td>
                                    <td> ${partAGrpC.scaleofPay} </td>
                                    <td> ${partAGrpC.gp} </td>
                                    <td> ${partAGrpC.scaleofPay7th} </td>
                                    <td> ${partAGrpC.sanctionedStrength} </td>
                                    <td> ${partAGrpC.meninPosition} </td>
                                    <td> ${partAGrpC.vacancyPosition} </td>
                                    <td>

                                        <a href="javascript:void(0);" onclick="openModal('single', '<c:out value="${partAGrpC.offCode}"/>', '<c:out value="${partAGrpC.postname}"/>', '<c:out value="${partAGrpC.scaleofPay}"/>', '<c:out value="${partAGrpC.scaleofPay7th}"/>', '<c:out value="${partAGrpC.group}"/>', '<c:out value="${partAGrpC.gp}"/>');">Edit</a> 

                                    </td>
                                </tr>
                            </c:forEach>   
                            <tr>
                                <td colspan="9"><h4> Group-D </h4> </td>
                            </tr>
                            <c:forEach var="partAGrpD" items="${PartAGroupDlist}">
                                <tr>
                                    <td> &nbsp; </td>
                                    <td> ${partAGrpD.gpc} &nbsp;<a href="ViewMappedSectionAgainstGPC.htm?postcode=${partAGrpD.postname}" target="_blank">View</a></td>
                                    <td> ${partAGrpD.scaleofPay} </td>
                                    <td> ${partAGrpD.gp} </td>
                                    <td> ${partAGrpD.scaleofPay7th} </td>
                                    <td> ${partAGrpD.sanctionedStrength} </td>
                                    <td> ${partAGrpD.meninPosition} </td>
                                    <td> ${partAGrpD.vacancyPosition} </td>
                                    <td>

                                        <a href="javascript:void(0);" onclick="openModal('single', '<c:out value="${partAGrpD.offCode}"/>', '<c:out value="${partAGrpD.postname}"/>', '<c:out value="${partAGrpD.scaleofPay}"/>', '<c:out value="${partAGrpD.scaleofPay7th}"/>', '<c:out value="${partAGrpD.group}"/>', '<c:out value="${partAGrpD.gp}"/>');">Edit</a> 

                                    </td>
                                </tr>
                            </c:forEach>       

                        </table>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" name="btnAer" value="Back" class="btn btn-primary">Back to List</button>
                    </div>
                </div>

            </div>

            <div id="substantivePostModal" class="modal" role="dialog">
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

