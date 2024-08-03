<%-- 
    Document   : NominationFinal
    Created on : Dec 27, 2020, 8:54:26 PM
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

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
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
        </script>
    </head>
    <body>
        <form:form action="saveNominationCategoryData.htm" method="POST" commandName="nominationCategoryForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Premature Retirement, Out of turn promotion (within the batch and across the batch) <br/> Incentives
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-3">
                                Name of the Department/HOD/District:
                            </div>
                            <div class="col-lg-9">
                                <strong><c:out value="${officename}"/></strong>
                            </div>
                        </div>
                        <div class="row">
                            <table class="table table-bordered">
                                <tr>
                                    <td rowspan="2">Sl No</td>
                                    <td colspan="2" rowspan="2">Category</td>
                                    <td colspan="5">No of Cases Finalized </td>
                                </tr>
                                <tr>
                                    <td>Group-A</td>
                                    <td>Group-B</td>
                                    <td>Group-C</td>
                                    <td>Group-D</td>
                                    <td>Total</td>
                                </tr>
                                <c:if test="${isSubmitted ne 'Y'}">
                                    <tr>
                                        <td rowspan="3">1</td>
                                        <td rowspan="3">Premature Retirement </td>
                                        <td>Department Level </td>
                                        <td><input type="text" name="prDeptGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prDeptGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prDeptGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prDeptGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>Heads of Department Level </td>
                                        <td><input type="text" name="prHodGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prHodGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prHodGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prHodGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>District Level </td>
                                        <td><input type="text" name="prDistGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prDistGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prDistGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="prDistGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="3">2</td>
                                        <td rowspan="3">Out of turn promotion (within the batch) </td>
                                        <td>Department Level </td>
                                        <td><input type="text" name="otpwbDeptGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbDeptGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbDeptGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbDeptGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>Heads of Department Level </td>
                                        <td><input type="text" name="otpwbHodGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbHodGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbHodGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbHodGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>District Level </td>
                                        <td><input type="text" name="otpwbDistGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbDistGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbDistGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpwbDistGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="3">3</td>
                                        <td rowspan="3">Out of turn promotion (across the batch) </td>
                                        <td>Department Level </td>
                                        <td><input type="text" name="otpabDeptGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabDeptGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabDeptGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabDeptGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>Heads of Department Level </td>
                                        <td><input type="text" name="otpabHodGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabHodGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabHodGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabHodGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>District Level </td>
                                        <td><input type="text" name="otpabDistGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabDistGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabDistGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="otpabDistGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="3">4</td>
                                        <td rowspan="3">Incentives</td>
                                        <td>Department Level </td>
                                        <td><input type="text" name="incentiveDeptGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveDeptGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveDeptGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveDeptGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>Heads of Department Level </td>
                                        <td><input type="text" name="incentiveHodGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveHodGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveHodGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveHodGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>District Level </td>
                                        <td><input type="text" name="incentiveDistGroupA" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveDistGroupB" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveDistGroupC" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td><input type="text" name="incentiveDistGroupD" class="form-control" onkeypress="return onlyIntegerRange(event)"/></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                </c:if>
                                <c:if test="${isSubmitted eq 'Y'}">
                                    <tr>
                                        <td rowspan="3">1</td>
                                        <td rowspan="3">Premature Retirement </td>
                                        <td>Department Level </td>
                                        <td>${nominationCategoryForm.prDeptGroupA}</td>
                                        <td>${nominationCategoryForm.prDeptGroupB}</td>
                                        <td>${nominationCategoryForm.prDeptGroupC}</td>
                                        <td>${nominationCategoryForm.prDeptGroupD}</td>
                                        <td>${nominationCategoryForm.prDeptGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td>Heads of Department Level </td>
                                        <td>${nominationCategoryForm.prHodGroupA}</td>
                                        <td>${nominationCategoryForm.prHodGroupB}</td>
                                        <td>${nominationCategoryForm.prHodGroupC}</td>
                                        <td>${nominationCategoryForm.prHodGroupD}</td>
                                        <td>${nominationCategoryForm.prHodGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td>District Level </td>
                                        <td>${nominationCategoryForm.prDistGroupA}</td>
                                        <td>${nominationCategoryForm.prDistGroupB}</td>
                                        <td>${nominationCategoryForm.prDistGroupC}</td>
                                        <td>${nominationCategoryForm.prDistGroupD}</td>
                                        <td>${nominationCategoryForm.prDistGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="3">2</td>
                                        <td rowspan="3">Out of turn promotion (within the batch) </td>
                                        <td>Department Level </td>
                                        <td>${nominationCategoryForm.otpwbDeptGroupA}</td>
                                        <td>${nominationCategoryForm.otpwbDeptGroupB}</td>
                                        <td>${nominationCategoryForm.otpwbDeptGroupC}</td>
                                        <td>${nominationCategoryForm.otpwbDeptGroupD}</td>
                                        <td>${nominationCategoryForm.otpwbDeptGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td>Heads of Department Level </td>
                                        <td>${nominationCategoryForm.otpwbHodGroupA}</td>
                                        <td>${nominationCategoryForm.otpwbHodGroupB}</td>
                                        <td>${nominationCategoryForm.otpwbHodGroupC}</td>
                                        <td>${nominationCategoryForm.otpwbHodGroupD}</td>
                                        <td>${nominationCategoryForm.otpwbHodGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td>District Level </td>
                                        <td>${nominationCategoryForm.otpwbDistGroupA}</td>
                                        <td>${nominationCategoryForm.otpwbDistGroupB}</td>
                                        <td>${nominationCategoryForm.otpwbDistGroupC}</td>
                                        <td>${nominationCategoryForm.otpwbDistGroupD}</td>
                                        <td>${nominationCategoryForm.otpwbDistGroupToal}</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="3">3</td>
                                        <td rowspan="3">Out of turn promotion (across the batch) </td>
                                        <td>Department Level </td>
                                        <td>${nominationCategoryForm.otpabDeptGroupA}</td>
                                        <td>${nominationCategoryForm.otpabDeptGroupB}</td>
                                        <td>${nominationCategoryForm.otpabDeptGroupC}</td>
                                        <td>${nominationCategoryForm.otpabDeptGroupD}</td>
                                        <td>${nominationCategoryForm.otpabDeptGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td>Heads of Department Level </td>
                                        <td>${nominationCategoryForm.otpabHodGroupA}</td>
                                        <td>${nominationCategoryForm.otpabHodGroupB}</td>
                                        <td>${nominationCategoryForm.otpabHodGroupC}</td>
                                        <td>${nominationCategoryForm.otpabHodGroupD}</td>
                                        <td>${nominationCategoryForm.otpabHodGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td>District Level </td>
                                        <td>${nominationCategoryForm.otpabDistGroupA}</td>
                                        <td>${nominationCategoryForm.otpabDistGroupB}</td>
                                        <td>${nominationCategoryForm.otpabDistGroupC}</td>
                                        <td>${nominationCategoryForm.otpabDistGroupD}</td>
                                        <td>${nominationCategoryForm.otpabDistGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="3">4</td>
                                        <td rowspan="3">Incentives</td>
                                        <td>Department Level </td>
                                        <td>${nominationCategoryForm.incentiveDeptGroupA}</td>
                                        <td>${nominationCategoryForm.incentiveDeptGroupB}</td>
                                        <td>${nominationCategoryForm.incentiveDeptGroupC}</td>
                                        <td>${nominationCategoryForm.incentiveDeptGroupD}</td>
                                        <td>${nominationCategoryForm.incentiveDeptGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td>Heads of Department Level </td>
                                        <td>${nominationCategoryForm.incentiveHodGroupA}</td>
                                        <td>${nominationCategoryForm.incentiveHodGroupB}</td>
                                        <td>${nominationCategoryForm.incentiveHodGroupC}</td>
                                        <td>${nominationCategoryForm.incentiveHodGroupD}</td>
                                        <td>${nominationCategoryForm.incentiveHodGroupTotal}</td>
                                    </tr>
                                    <tr>
                                        <td>District Level </td>
                                        <td>${nominationCategoryForm.incentiveDistGroupA}</td>
                                        <td>${nominationCategoryForm.incentiveDistGroupB}</td>
                                        <td>${nominationCategoryForm.incentiveDistGroupC}</td>
                                        <td>${nominationCategoryForm.incentiveDistGroupD}</td>
                                        <td>${nominationCategoryForm.incentiveDistGroupTotal}</td>
                                    </tr>
                                </c:if>
                            </table>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${isSubmitted ne 'Y'}">
                            <input type="submit" class="btn btn-primary" name="action" value="Submit" onclick="return confirm('Are you sure to Submit?')"/>
                        </c:if>
                        <c:if test="${isSubmitted eq 'Y'}">
                            <a href="DownloadNominationCategoryDataPDF.htm">
                                <button type="button" class="btn btn-primary">Download</button>
                            </a>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
