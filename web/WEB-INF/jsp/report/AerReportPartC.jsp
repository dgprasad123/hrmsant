<%-- 
    Document   : AerReportPartC
    Created on : May 24, 2019, 5:22:59 PM
    Author     : Manas
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
            function openModal(mode, offcode, gpc) {
                $("#offCode").val(offcode);
                $('#gpc').val(gpc);
                $('#mode').val(mode);
                //alert("GPC is: "+$('#gpc').val());
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
            <jsp:param name="menuHighlight" value="PART_C" />            
        </jsp:include>
        <form:form action="updatePartCPost.htm" method="POST" commandName="command">
            <form:hidden path="aerId" id="aerId"/>
            <form:hidden path="gpc" id="gpc"/>
            <form:hidden path="offCode" id="offCode"/>
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
                                    <th width="10%">Category </th>
                                    <th width="25%">Description of the Posts</th>
                                    <th  width="5%">As per 7th Pay</th>
                                    <th width="8%">Persons-in-Position</th>
                                    <th width="5%">
                                        <c:if test="${Editable eq 'N'}">
                                            Remarks if any
                                        </c:if>
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
                                    <td> &nbsp;  <c:if test="${Editable eq 'Y'}">
                                            <a href="javascript:void(0);" onclick="openModal('single', '<c:out value="${partCGrp.offCode}"/>', '<c:out value="${partCGrp.postname}"/>');">Edit</a> 
                                        </c:if> 
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

