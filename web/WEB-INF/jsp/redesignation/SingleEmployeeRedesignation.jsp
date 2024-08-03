<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
            $(document).ready(function() {
                $('.regularRow').hide();
                $('.contractualRow').hide();
            });
            function toggleEmployeeType() {
                if ($('#sltRecruitmentType').val() == "Y") {
                    $('.regularRow').show();
                    $('.contractualRow').hide();
                } else if ($('#sltRecruitmentType').val() == "N") {
                    $('.regularRow').hide();
                    $('.contractualRow').show();
                }
            }
            function getSectionWiseSPNList() {
                var sectionid = $('#sltSection').val();

                $('#postedspc').empty();
                $('#postedspc').append('<option value="">--Select Substantive Post--</option>');

                var url = "getSectionWiseSubstantivePostListJSON.htm?sectionId=" + sectionid;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#postedspc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                    });
                });
            }
            function validateDetails() {
                if ($('#gpfno').val() == '') {
                    alert("Please enter GPF No");
                    $('#gpfno').focus();
                    return false;
                }
            }
            function validateSubmit() {
                if ($('#sltRecruitmentType').val() == "") {
                    alert("Please select Recruitment Type");
                    return false;
                } else if ($('#sltRecruitmentType').val() == "Y") {
                    if ($('#postedspc').val() == '') {
                        alert("Please select Post");
                        return false;
                    }
                    if ($('#txtBasic').val() == '') {
                        alert("Please enter Basic");
                        $('#txtBasic').focus();
                        return false;
                    }
                } else if ($('#sltRecruitmentType').val() == "N") {
                    if ($('#postnomenclature').val() == '') {
                        alert("Please enter Post Nomenclature");
                        $('#postnomenclature').focus();
                        return false;
                    }
                }
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
        </script>
    </head>
    <body>
        <%--<div class="col-lg-12" style="text-align:center;">
            <h2><span style="color:red">This page is not available due to some maintenance.</span></h2>
        </div>--%>
        <form:form action="SingleEmployeeRedesignation.htm" method="POST" commandName="redesignationForm">
            <form:hidden path="htrid" id="htrid" value="${retType}"/> 
             <form:hidden path="hidPostedOffCode" id="hidPostedOffCode"/>   

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row" style="margin-top:20px;margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-1">
                                <label for="gpfno">Enter GPF NO</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="gpfno" id="gpfno"/>
                            </div>
                            <div class="col-lg-2">
                                <input type="submit" name="btnSubmit" value="Get Details" class="btn btn-danger" onclick="return validateDetails();"/>
                            </div>
                            <div class="col-lg-4"></div>                                
                        </div>
                    </div>
                    <div class="panel-body" style="text-align: center;">
                        <table class="table table-bordered" width="80%">
                            <tbody>
                                <c:if test="${not empty empid && empid ne 'NA'}">
                                    <form:hidden path="empid" value="${empid}"/>
                                    <tr>
                                        <td width="30%">Type of Recruitment</td>
                                        <td width="50%">
                                            <form:select path="sltRecruitmentType" id="sltRecruitmentType" onchange="toggleEmployeeType();" class="form-control">
                                                <form:option value="">--Select--</form:option>
                                                <form:option value="Y">Regular</form:option>
                                                <form:option value="N">Contractual</form:option>
                                            </form:select>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${not empty empid && empid eq 'NA'}">
                                    <tr>
                                        <td colspan="2" style="text-align:center;color:#FF0000;">
                                            <h2>Employee has not Retired / Resigned.</h2>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${not empty retVal && retVal > 0}">
                                    <tr>
                                        <td colspan="2" style="text-align:center;color:#FF0000;">
                                            <h3>ReEmployment Successful.</h3>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${retType ne 'RG' && retType ne 'RP'}">
                                    <tr class="regularRow">
                                        <td>Section</td>
                                        <td>
                                            <form:select path="sltSection" id="sltSection" class="form-control" onchange="getSectionWiseSPNList();">
                                                <form:option value="">--Select Section--</form:option>
                                                <form:options items="${sectionlist}" itemLabel="billgroup" itemValue="sectionId"/>
                                            </form:select>
                                        </td>
                                    </tr>
                                    <tr class="regularRow">
                                        <td>Substantive Post</td>
                                        <td>
                                            <form:select path="postedspc" id="postedspc" class="form-control">
                                                <form:option value="">--Select Substantive Post--</form:option>
                                            </form:select>
                                        </td>
                                    </tr>
                                    <%--<tr class="regularRow">
                                        <td>Basic</td>
                                        <td>
                                            <form:input class="form-control" path="txtBasic" id="txtBasic" maxlength="7" onkeypress="return onlyIntegerRange(event)"/>
                                        </td>
                                    </tr>--%>
                                </c:if>
                                <tr class="contractualRow">
                                    <td>Post Nomenclature</td>
                                    <td>
                                        <form:input class="form-control" path="postnomenclature" id="postnomenclature" maxlength="200"/>
                                    </td>
                                </tr>

                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnSubmit" value="Save ReEmployment" class="btn btn-default" onclick="return validateSubmit();"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
