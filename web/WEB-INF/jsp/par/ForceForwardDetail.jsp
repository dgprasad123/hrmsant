<%-- 
    Document   : ForceForwardDetail
    Created on : 16 Apr, 2022, 1:48:47 PM
    Author     : Manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>
        <script type="text/javascript">

            $(document).ready(function() {
                $("#DepartmentDiv").hide();
                $("#CadreDiv").hide();
                $(".loader").hide();
                $("#deptcode").change(function() {
                    $('#cadreCode').empty();
                    var url = 'getCadreListForPARJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, obj) {
                            $('#cadreCode').append('<option value="' + obj.cadreCode + '">' + obj.cadreName + '</option>');
                        });
                    });
                });


            });

            function radioClicked() {
                var radioValue = $("input[name='forceForwardType']:checked").val();
                if (radioValue == "cadrewiseforward") {
                    $("#DepartmentDiv").show();
                    $("#CadreDiv").show();
                } else if (radioValue == "forwardall") {
                    $("#DepartmentDiv").hide();
                    $("#CadreDiv").hide();
                }

            }
            function saveForceforwardDetail() {
                var radioValue = $("input[name='forceForwardType']:checked").val();
                if (radioValue == "cadrewiseforward") {
                    if ($("#deptcode").val() == "") {
                        alert("Please Choose the Department");
                        $("#deptcode").focus();
                        return false;
                    }
                    if ($("#cadreCode").val() == "") {
                        alert("Please Choose the Cadre");
                        $("#cadreCode").focus();
                        return false;
                    }
                }
                if ($("#fiscalyear").val() == "") {
                    alert("Please select the Financial Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                if (radioValue == null) {
                    alert("Please Choose Forceforward Type");
                    $("#radioValue").focus();
                    return false;
                }

                if ($("#fromAuthority").val() == "") {
                    alert("Please select From Authority type");
                    $("#fromAuthority").focus();
                    return false;
                }
                if ($("#toAuthority").val() == "") {
                    alert("Please select To Authority type");
                    $("#toAuthority").focus();
                    return false;
                }
                if ($("#fromAuthority").val() == "reporting" && $("#toAuthority").val() == "accepting") {
                    alert("Please select To Authority type reviewing");
                    $("#toAuthority").focus();
                    return false;
                }
                if ($("#fromAuthority").val() == "reviewing" && $("#toAuthority").val() == "reviewing") {
                    alert("Please select To Authority type accepting");
                    $("#toAuthority").focus();
                    return false;
                } else {
                    alert("Are You Sure To Forceforward the PAR");
                }

            }
            function forceforwardErrorCheck() {
                alert("hlo");
                $.post("forceforwardCheckError.htm");

            }



        </script>
    </head>



    <body style="margin-top:0px;background:#188B7A;">
        <jsp:include page="../tab/ParMenu.jsp"/> 
        <form:form action="parForceForwardDetail.htm" commandName="parForceForwardBean" method="post" class="form-horizontal">
            <div id="wrapper"> 
                <div id="page-wrapper" style="margin-top:80px;z-index:0;padding: 20px 19px;">

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="category">Financial Year <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-3">
                            <form:select path="fiscalyear" id="fiscalyear" class="form-control">
                                <option value="">Year</option>
                                <form:options items="${fiscalcyear}" itemValue="fy" itemLabel="fy"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="fa"><b>Force forward Detail <span style="color: red">*</span></b></label>
                        </div>
                        <%--<div class="col-lg-2"> 
                            <input type="radio" id="cadrewiseforward" name="forceForwardType" value="cadrewiseforward" onclick="radioClicked()"> <b>Cadrewise Forward</b>
                        </div> --%>
                        <div class="col-lg-2">
                            <input type="radio" id="forwardall" name="forceForwardType" value="forwardall" onclick="radioClicked()">  <b>Forward ALL</b>
                        </div>                                                                    
                    </div>
                    <div class="row" style="margin-bottom: 7px;" id="DepartmentDiv">
                        <div class="col-lg-2">
                            <label for="department Name">Department Name <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-3">

                            <select class="form-control" name="deptcode" id="deptcode">
                                <option value="">Select</option>
                                <c:forEach items="${departmentList}" var="department">
                                    <option value="${department.deptCode}">${department.deptName}</option>
                                </c:forEach>                                        
                            </select>
                        </div>
                        <%--<div class="col-lg-2"><button type="submit" class="form-control">Submit</button> </div> --%>


                        <div class="col-lg-2">
                            <label for="cadre Name">Cadre List</label>
                        </div>
                        <div class="col-lg-3">
                            <select class="form-control" name="cadreCode" id="cadreCode">
                                <option value="">Select</option>                                            
                            </select>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="fullname">From Authority <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-3">
                            <form:select path="fromAuthority"  class="form-control">
                                <%--<form:option value="reporting">Reporting</form:option> --%>
                                <form:option value="reviewing">Reviewing</form:option>
                            </form:select>  
                        </div>
                        <div class="col-lg-2">
                            <label for="fullname">To Authority <span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-3">
                            <form:select path="toAuthority" class="form-control">
                               <%-- <form:option value="reviewing">Reviewing</form:option> --%>
                                 <form:option value="accepting">Accepting</form:option>
                            </form:select> 
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Force Forward" class="btn btn-default" onclick="return saveForceforwardDetail()"/>
                        <input type="submit" name="action" value="Get Force forward List" class="btn btn-default"/> 
                        <a href="transferForceforwardDetailToLogtable.htm" class="btn btn-default">Transfer Force forward Detail To Log table</a> 
                        <input type="button" value="Check Force forward Error" class="btn btn-default" onclick="forceforwardErrorCheck()"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>


