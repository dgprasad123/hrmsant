<%-- 
    Document   : AerAuthorizationDetail
    Created on : Jan 24, 2019, 11:05:00 AM
    Author     : manisha
--%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(window).load(function() {
                // Fill modal with content from link href
                $("#myModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
                $("table").on('click', '.removeBtn', function() {
                    if(confirm("Do you want to Remove this Employee?")){
                        $(this).parent().parent().remove();
                    }
                });
            })
            function selectemployee(me) {
                var rownum = $("#clickedrownum").val();
                if ($("#action").val() == "Operator") {
                    $("#operator").val($(me).val());
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#operatorname").val(empName + ', ' + empPost);
                } else if ($("#action").val() == "Approver") {
                    $("#approver").val($(me).val());
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#approvername").val(empName + ', ' + empPost);
                } else if ($("#action").val() == "Reviewer") {
                    $("#reviewer_" + rownum).val($(me).val());
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#reviewername_" + rownum).val(empName + ', ' + empPost);
                } else if ($("#action").val() == "Verifier") {
                    $("#verifier_" + rownum).val($(me).val());
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#verifiername_" + rownum).val(empName + ', ' + empPost);
                } else if ($("#action").val() == "DReviewer") {
                    if (rownum == 0) {
                        $("#dreviewer").val($(me).val());
                    } else if (rownum > 0) {
                        $("#dreviewer_" + rownum).val($(me).val());
                    }
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#dreviewername_" + rownum).val(empName + ', ' + empPost);
                } else if ($("#action").val() == "DVerifier") {
                    if (rownum == 0) {
                        $("#dverifier").val($(me).val());
                    } else if (rownum > 0) {
                        $("#dverifier_" + rownum).val($(me).val());
                    }
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#dverifiername_" + rownum).val(empName + ', ' + empPost);
                } else if ($("#action").val() == "Acceptor") {
                    $("#acceptor").val($(me).val());
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#acceptorname").val(empName + ', ' + empPost);
                } else if ($("#action").val() == "ProfileApprover") {
                    $("#profileApprover").val($(me).val());
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#profileApprovername").val(empName + ', ' + empPost);
                }
                
                else if ($("#action").val() == "ValidatorServiceBook") {
                    if (rownum == 0) {
                        $("#serviceBookValidator").val($(me).val());
                    } else if (rownum > 0) {
                        $("#serviceBookValidator_" + rownum).val($(me).val());
                    }
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#validatorName_" + rownum).val(empName + ', ' + empPost);
                }
               
                else if ($("#action").val() == "SBEntry") {
                    
                    if (rownum == 0) {
                        $("#serviceBookentry").val($(me).val());
                    } else if (rownum > 0) {                     
                        $("#serviceBookentry_" + rownum).val($(me).val());
                    }
                    arrTemp = $('#post_' + $(me).val()).val().split('<==>');
                    empName = arrTemp[0];
                    empPost = arrTemp[1];
                    $("#serviceentryName_" + rownum).val(empName + ', ' + empPost);         
                }
            }

        </script>
        <style>
            .add-btn{
                width: 44%;
            }
        </style>
        <script type="text/javascript">

            function validation() {
                if ($("#operatorname").val() == "") {
                    alert("Please Select the Operator");
                    $("#operatorname").focus();
                    return false;
                }
                if ($("#approvername").val() == "") {
                    alert("Please Select the Approver");
                    $("#approvername").focus();
                    return false;
                }
            }
            function validationEmpProfileApprover() {
                if ($("#profileApprovername").val() == "") {
                    alert("Please Select the Approver");
                    $("#profileApprovername").focus();
                    return false;
                }
            }
            function validationAERReviewer() {
                var reviewerlength = $('input[name=reviewer]').length;
                for (var i = 0; i < reviewerlength; i++) {
                    if ($("#reviewername_" + i).val() == "") {
                        alert("Please Select the Reviewer");
                        $("#reviewername_" + i).focus();
                        return false;
                    }
                }
                var verifierlength = $('input[name=verifier]').length;
                for (var i = 0; i < verifierlength; i++) {
                    if ($("#verifiername_" + i).val() == "") {
                        alert("Please Select the Verifier");
                        $("#verifiername_" + i).focus();
                        return false;
                    }
                }
            }
            function validationAERAcceptor() {
                var dreviewerlength = $('input[name=dreviewer]').length;
                for (var i = 0; i < dreviewerlength; i++) {
                    if ($("#dreviewer_" + i).val() == "") {
                        alert("Please Select the Reviewer for Department");
                        $("#dreviewer_" + i).focus();
                        return false;
                    }
                }
                var dverifierlength = $('input[name=dverifier]').length;
                for (var i = 0; i < dverifierlength; i++) {
                    if ($("#dverifier_" + i).val() == "") {
                        alert("Please Select the Verifier for Department");
                        $("#dverifier_" + i).focus();
                        return false;
                    }
                }
                if ($("#acceptorname").val() == "") {
                    alert("Please Select the Acceptor");
                    $("#acceptorname").focus();
                    return false;
                }
            }

             function addValidator() {
                var childlength = $("table.sbvalidatorpanel tr").length;
                var rowcount = childlength;
                $("#clickedrownum").val(rowcount);
                var formstring = "<tr id='sbvalidatorpanel_tr_" + childlength + "'>";
                formstring = formstring + "<td width='16%'><a href='searchaerauthorization.htm?action=ValidatorServiceBook' data-remote='false' data-toggle='modal' data-target='#myModal' class='btn btn-primary' onclick=\"openModal('SERVICE BOOK VALIDATOR','" + rowcount + "');\">Validator</a></td>";
                formstring = formstring + "<td width='54%'><input type='hidden' name='serviceBookValidator' id='serviceBookValidator_" + childlength + "'/><input type='text' name='validatorName' id='validatorName_" + childlength + "' readonly='true' class='form-control'/></td>";
                formstring = formstring + "<td width='10%'><button type='button' class='btn btn-danger removeBtn'>Remove</button></td><td width='13%'>&nbsp;</td>";
                formstring = formstring + "</tr>";

                $("table.sbvalidatorpanel > tbody:last").append(formstring);
            }
            function addSBEntryBy(){
                
                var childlength = $("table.sbentrypanel tr").length;
                var rowcount = childlength;
                $("#clickedrownum").val(rowcount);
                var formstring = "<tr id='sbentrypanel_tr_" + childlength + "'>";
                formstring = formstring + "<td width='16%'><a href='searchaerauthorization.htm?action=SBEntry' data-remote='false' data-toggle='modal' data-target='#myModal' class='btn btn-primary' onclick=\"openModal('SERVICE BOOK ENTRY','" + rowcount + "');\">SB Entry By</a></td>";
                formstring = formstring + "<td width='54%'><input type='hidden' name='serviceBookentry' id='serviceBookentry_" + childlength + "'/><input type='text' name='serviceentryName' id='serviceentryName_" + childlength + "' readonly='true' class='form-control'/></td>";
                formstring = formstring + "<td width='10%'><button type='button' class='btn btn-danger removeBtn'>Remove</button></td><td width='13%'>&nbsp;</td>";
                formstring = formstring + "</tr>";

                $("table.sbentrypanel > tbody:last").append(formstring); 
            }
            
            
            
            function addDReviewer() {
                var childlength = $("table.dreviewerpanel tr").length;
                var rowcount = childlength;
                $("#clickedrownum").val(rowcount);
                var formstring = "<tr id='dreviewerpanel_tr_" + childlength + "'>";
                formstring = formstring + "<td width='16%'><a href='searchaerauthorization.htm?action=DReviewer' data-remote='false' data-toggle='modal' data-target='#myModal' class='btn btn-primary' onclick=\"openModal('DREVIEWER','" + rowcount + "');\">Reviewer</a></td>";
                formstring = formstring + "<td width='54%'><input type='hidden' name='dreviewer' id='dreviewer_" + childlength + "'/><input type='text' name='dreviewername' id='dreviewername_" + childlength + "' readonly='true' class='form-control'/></td>";
                formstring = formstring + "<td width='10%'><button type='button' class='btn btn-danger removeBtn'>Remove</button></td><td width='13%'>&nbsp;</td>";
                formstring = formstring + "</tr>";

                $("table.dreviewerpanel > tbody:last").append(formstring);
            }

            function removeDReviewer() {
                $(this).parent().parent().remove();
            }

            function addDVerifier() {
                var childlength = $("table.dverifierpanel tr").length;
                var rowcount = childlength;
                $("#clickedrownum").val(rowcount);
                var formstring = "<tr id='dverifierpanel_tr_" + childlength + "'>";
                formstring = formstring + "<td width='16%'><a href='searchaerauthorization.htm?action=DVerifier' data-remote='false' data-toggle='modal' data-target='#myModal' class='btn btn-primary' onclick=\"openModal('DVERIFIER','" + rowcount + "');\">Verifier</a></td>";
                formstring = formstring + "<td width='54%'><input type='hidden' name='dverifier' id='dverifier_" + childlength + "'/><input type='text' name='dverifiername' id='dverifiername_" + childlength + "' readonly='true' class='form-control'/></td>";
                formstring = formstring + "<td width='10%'><button type='button' class='btn btn-danger removeBtn'>Remove</button></td><td width='13%'>&nbsp;</td>";
                formstring = formstring + "</tr>";

                $("table.dverifierpanel > tbody:last").append(formstring);
            }

            function addReviewer() {
                var childlength = $("table.reviewerpanel tr").length;
                var rowcount = childlength;
                $("#clickedrownum").val(rowcount);
                var formstring = "<tr id='reviewerpanel_tr_" + childlength + "'>";
                formstring = formstring + "<td width='16%'><a href='searchaerauthorization.htm?action=Reviewer' data-remote='false' data-toggle='modal' data-target='#myModal' class='btn btn-primary' onclick=\"openModal('REVIEWER','" + rowcount + "');\">Reviewer</a></td>";
                formstring = formstring + "<td width='54%'><input type='hidden' name='reviewer' id='reviewer_" + childlength + "'/><input type='text' name='reviewername' id='reviewername_" + childlength + "' readonly='true' class='form-control'/></td>";
                formstring = formstring + "<td width='10%'><button type='button' class='btn btn-danger removeBtn'>Remove</button></td><td width='13%'>&nbsp;</td>";
                formstring = formstring + "</tr>";

                $("table.reviewerpanel > tbody:last").append(formstring);
            }

            function addVerifier() {
                var childlength = $("table.verifierpanel tr").length;
                var rowcount = childlength;
                $("#clickedrownum").val(rowcount);
                var formstring = "<tr id='verifierpanel_tr_" + childlength + "'>";
                formstring = formstring + "<td width='16%'><a href='searchaerauthorization.htm?action=Verifier' data-remote='false' data-toggle='modal' data-target='#myModal' class='btn btn-primary' onclick=\"openModal('VERIFIER','" + rowcount + "');\">Verifier</a></td>";
                formstring = formstring + "<td width='54%'><input type='hidden' name='verifier' id='verifier_" + childlength + "'/><input type='text' name='verifiername' id='verifiername_" + childlength + "' readonly='true' class='form-control'/></td>";
                formstring = formstring + "<td width='10%'><button type='button' class='btn btn-danger removeBtn'>Remove</button></td><td width='13%'>&nbsp;</td>";
                formstring = formstring + "</tr>";

                $("table.verifierpanel > tbody:last").append(formstring);
            }

            function openModal(type, count) {
                if (type == "DREVIEWER") {
                    $("#clickedrownum").val(count);
                } else if (type == "DVERIFIER") {
                    $("#clickedrownum").val(count);
                } else if (type == "REVIEWER") {
                    $("#clickedrownum").val(count);
                } else if (type == "VERIFIER") {
                    $("#clickedrownum").val(count);
                }else if (type == "SERVICE BOOK VALIDATOR") {
                    $("#clickedrownum").val(count);
                }else if (type == "SERVICE BOOK ENTRY") {
                    $("#clickedrownum").val(count);
                }
            }
            function validationserviceBookValidator() {
                
                var validator = $('input[name=serviceBookValidator]').length;
                for (var i = 0; i < validator; i++) {
                    if ($("#serviceBookValidator_" + i).val() == "") {
                        alert("Please Select the Validator Name");
                        $("#serviceBookValidator_" + i).focus();
                        return false;
                    }
                }                
            }
            function validationserviceBookentry() {
                var sbentry = $('input[name=serviceBookentry]').length;
                for (var i = 0; i < sbentry; i++) {
                    if ($("#serviceBookentry_" + i).val() == "") {
                        alert("Please Select Service Book Entry By");
                        $("#serviceBookentry_" + i).focus();
                        return false;
                    }                                       
                }                
            }
        </script>
    </head>
    <body>
        <input type="hidden" id="clickedrownum" value="0">
        <form:form action="saveValidatorServiceBookAuth.htm" commandName="aerAuthorizationBean" method="post">
            <div class="container-fluid">
                <div class="panel panel-info">
                    <div class="panel-heading">
                       Service Book Validator
                    </div>
                    <div class="panel-body">           
                    <table class="table table-borderless sbvalidatorpanel" width="100%">
                                <tbody>
                                    <c:if test="${not empty aerAuthorizationBean.validatorlist}">
                                        <c:forEach items="${aerAuthorizationBean.validatorlist}" var="validatorlist" varStatus="counter">
                                            <tr id="sbvalidatorpanel_tr_${counter.count - 1}">
                                                <td width="16%">
                                                    <a href="searchaerauthorization.htm?action=ValidatorServiceBook" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('SERVICE BOOK VALIDATOR', '${counter.count - 1}');">Validator</a>
                                                </td>
                                                <td width="52%">
                                                    <form:hidden path="financialyear"/>
                                                    <form:hidden class="form-control" path="serviceBookValidator" value="${validatorlist.serviceBookValidator}"/>
                                                    <form:input path="validatorName" id="validatorName_${counter.count - 1}" readonly="true" cssClass="form-control" value="${validatorlist.validatorName}"/>
                                                </td>
                                                <td width="10%">
                                                    <c:if test="${counter.count eq 1}">
                                                        <button type="button" class="btn btn-danger add-btn" onclick="addValidator();">Add</button>
                                                    </c:if>
                                                    <c:if test="${counter.count ne 1}">
                                                        <button type="button" class="btn btn-danger removeBtn">Remove</button>
                                                    </c:if>
                                                </td>
                                                <td width="13%"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty aerAuthorizationBean.validatorlist}">
                                        <tr id="sbvalidatorpanel_tr_0">
                                            <td width="16%">
                                                <a href="searchaerauthorization.htm?action=ValidatorServiceBook" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('SERVICE BOOK VALIDATOR', '0');">Validator</a>
                                            </td>
                                            <td width="52%">
                                                <form:hidden path="financialyear"/>
                                                <form:hidden class="form-control" path="serviceBookValidator"/>
                                                <form:input path="validatorName" id="validatorName_0" readonly="true" cssClass="form-control"/>
                                            </td>
                                            <td width="10%">
                                                <button type="button" class="btn btn-danger add-btn" onclick="addValidator();">Add</button>
                                            </td>
                                            <td width="13%"></td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>                       
                    </div>
                    <div class="panel-footer">
                        <c:if test="${empty aerAuthorizationBean.validatorlist}">
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validationserviceBookValidator()"/>  
                        </c:if>
                        <c:if test="${not empty aerAuthorizationBean.validatorlist}">
                            <input type="submit" name="action" value="Update" class="btn btn-default" onclick="return validationserviceBookValidator()"/>  
                        </c:if>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                    </div>
                </div>
            </div>
        </form:form>
        <form:form action="saveentryServiceBookAuth.htm" commandName="aerAuthorizationBean" method="post">
            <div class="container-fluid">
                <div class="panel panel-info">
                    <div class="panel-heading">
                       Service Book Entry
                    </div>
                    <div class="panel-body">  
                        
                     <table class="table table-borderless sbentrypanel" width="100%">
                                <tbody>
                                    <c:if test="${not empty aerAuthorizationBean.serviceBookentrylist}">
                                        <c:forEach items="${aerAuthorizationBean.serviceBookentrylist}" var="serviceBookentrylist" varStatus="counter">
                                            <tr id="sbentrypanel_tr_${counter.count - 1}">
                                                <td width="16%">
                                                    <a href="searchaerauthorization.htm?action=SBEntry" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('SERVICE BOOK ENTRY', '${counter.count - 1}');">SB Entry By</a>
                                                </td>
                                                <td width="52%">
                                                    <form:hidden path="financialyear"/>
                                                    <form:hidden class="form-control" path="serviceBookentry" value="${serviceBookentrylist.serviceBookentry}"/>
                                                    <form:input path="serviceentryName" id="serviceentryName_${counter.count - 1}" readonly="true" cssClass="form-control" value="${serviceBookentrylist.serviceentryName}"/>
                                                </td>
                                                <td width="10%">
                                                    <c:if test="${counter.count eq 1}">
                                                        <button type="button" class="btn btn-danger add-btn" onclick="addSBEntryBy();">Add</button>
                                                    </c:if>
                                                    <c:if test="${counter.count ne 1}">
                                                        <button type="button" class="btn btn-danger removeBtn">Remove</button>
                                                    </c:if>
                                                </td>
                                                <td width="13%"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty aerAuthorizationBean.serviceBookentrylist}">
                                        <tr id="sbentrypanel_tr_0">
                                            <td width="16%">
                                                <a href="searchaerauthorization.htm?action=SBEntry" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('SERVICE BOOK ENTRY', '0');">SB Entry By</a>
                                            </td>
                                            <td width="52%">
                                                <form:hidden path="financialyear"/>
                                                <form:hidden class="form-control" path="serviceBookentry"/>
                                                <form:input path="serviceentryName" id="serviceentryName_0" readonly="true" cssClass="form-control"/>
                                            </td>
                                            <td width="10%">
                                                <button type="button" class="btn btn-danger add-btn" onclick="addSBEntryBy();">Add</button>
                                            </td>
                                            <td width="13%"></td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>                                                                
                    </div>
                    <div class="panel-footer">
                        <c:if test="${empty aerAuthorizationBean.serviceBookentrylist}">
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validationserviceBookentry()"/>  
                        </c:if>
                        <c:if test="${not empty aerAuthorizationBean.serviceBookentrylist}">
                            <input type="submit" name="action" value="Update" class="btn btn-default" onclick="return validationserviceBookentry()" />  
                        </c:if>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                    </div>
                </div>
            </div>
        </form:form>
        <c:if test="${officedetails.lvl eq '01'}">
            <form:form action="saveaerauthorizationapprover.htm" commandName="aerAuthorizationBean" method="post" onclick="return validationAERAcceptor()">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            AER Authorization (Acceptor)
                        </div>
                        <div class="panel-body">
                            <table class="table table-borderless dreviewerpanel" width="100%">
                                <tbody>
                                    <c:if test="${not empty aerAuthorizationBean.dreviewerlist}">
                                        <c:forEach items="${aerAuthorizationBean.dreviewerlist}" var="dreviewerlist" varStatus="counter">
                                            <tr id="dreviewerpanel_tr_${counter.count - 1}">
                                                <td width="16%">
                                                    <a href="searchaerauthorization.htm?action=DReviewer" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('DREVIEWER', '${counter.count - 1}');">Reviewer</a>
                                                </td>
                                                <td width="52%">
                                                    <form:hidden class="form-control" path="dreviewer" value="${dreviewerlist.dreviewer}"/>
                                                    <form:input path="dreviewername" id="dreviewername_${counter.count - 1}" readonly="true" cssClass="form-control" value="${dreviewerlist.dreviewername}"/>
                                                </td>
                                                <td width="10%">
                                                    <c:if test="${counter.count eq 1}">
                                                        <button type="button" class="btn btn-danger add-btn" onclick="addDReviewer();">Add</button>
                                                    </c:if>
                                                    <c:if test="${counter.count ne 1}">
                                                        <button type="button" class="btn btn-danger removeBtn">Remove</button>
                                                    </c:if>
                                                </td>
                                                <td width="13%"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty aerAuthorizationBean.dreviewerlist}">
                                        <tr id="dreviewerpanel_tr_0">
                                            <td width="16%">
                                                <a href="searchaerauthorization.htm?action=DReviewer" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('DREVIEWER', '0');">Reviewer</a>
                                            </td>
                                            <td width="52%">
                                                <form:hidden class="form-control" path="dreviewer"/>
                                                <form:input path="dreviewername" id="dreviewername_0" readonly="true" cssClass="form-control"/>
                                            </td>
                                            <td width="10%">
                                                <button type="button" class="btn btn-danger add-btn" onclick="addDReviewer();">Add</button>
                                            </td>
                                            <td width="13%"></td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                            <br/>
                            <table class="table table-borderless dverifierpanel" width="100%">
                                <tbody>
                                    <c:if test="${not empty aerAuthorizationBean.dverifierlist}">
                                        <c:forEach items="${aerAuthorizationBean.dverifierlist}" var="dverifierlist" varStatus="counter">
                                            <tr id="dverifierpanel_tr_${counter.count - 1}">
                                                <td width="16%">
                                                    <a href="searchaerauthorization.htm?action=DVerifier" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('DVERIFIER', '${counter.count - 1}');">Verifier</a>
                                                </td>
                                                <td width="52%">
                                                    <form:hidden class="form-control" path="dverifier" value="${dverifierlist.dverifier}"/>
                                                    <form:input path="dverifiername" id="dverifiername_${counter.count - 1}" readonly="true" cssClass="form-control" value="${dverifierlist.dverifiername}"/>
                                                </td>
                                                <td width="10%">
                                                    <c:if test="${counter.count eq 1}">
                                                        <button type="button" class="btn btn-danger add-btn" onclick="addDVerifier();">Add</button>
                                                    </c:if>
                                                    <c:if test="${counter.count ne 1}">
                                                        <button type="button" class="btn btn-danger removeBtn">Remove</button>
                                                    </c:if>
                                                </td>
                                                <td width="13%"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty aerAuthorizationBean.dverifierlist}">
                                        <tr id="dverifierpanel_tr_0">
                                            <td width="16%">
                                                <a href="searchaerauthorization.htm?action=DVerifier" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('DVERIFIER', '0');">Verifier</a>
                                            </td>
                                            <td width="52%">
                                                <form:hidden class="form-control" path="dverifier"/>
                                                <form:input path="dverifiername" id="dverifiername_0" readonly="true" cssClass="form-control"/>
                                            </td>
                                            <td width="10%">
                                                <button type="button" class="btn btn-danger add-btn" onclick="addDVerifier();" >Add</button>
                                            </td>
                                            <td width="13%"></td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                            <br/>
                            <div class="form-group"> 
                                <div class="col-sm-2" >
                                    <a href="searchaerauthorization.htm?action=Acceptor" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary">Acceptor</a>
                                </div>
                                <div class="col-sm-7"> 
                                    <form:hidden path="financialyear"/>
                                    <form:hidden path="authoritytype"/>
                                    <form:hidden class="form-control" path="acceptor"/>
                                    <form:input path="acceptorname" id="acceptorname" readonly="true" cssClass="form-control"/>                            
                                </div>
                                <div class="col-sm-1">

                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-sm-8"> 
                                    <span style="font-size: 15px;color: firebrick;font-style: italic;">Note : Head of Office will authorize one 
                                        officer of Office Establishment Branch in HRMS as <b>'Acceptor'</b> for preparing the consolidated information 
                                        submitted by the Heads of Departments (Directorates).
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <c:if test="${empty aerAuthorizationBean.acceptor && empty aerAuthorizationBean.dreviewerlist && empty aerAuthorizationBean.dverifierlist}">
                                <input type="submit" name="action" value="Save" class="btn btn-default"/>  
                            </c:if>
                            <c:if test="${not empty aerAuthorizationBean.acceptor || not empty aerAuthorizationBean.dreviewerlist || not empty aerAuthorizationBean.dverifierlist}">
                                <input type="submit" name="action" value="Update" class="btn btn-default"/>  
                            </c:if>
                            <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                        </div>
                    </div>
                </div>
            </form:form>
        </c:if>
        <c:if test="${officedetails.lvl eq '01' or officedetails.lvl eq '02'}">
            <form:form action="saveaerauthorizationreviewer.htm" commandName="aerAuthorizationBean" method="post" onsubmit="return validationAERReviewer()">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            AER Authorization (Reviewer and Verifier)
                        </div>
                        <div class="panel-body">
                            <table class="table table-borderless reviewerpanel" width="100%">
                                <tbody>
                                    <c:if test="${not empty aerAuthorizationBean.reviewerlist}">
                                        <c:forEach items="${aerAuthorizationBean.reviewerlist}" var="reviewerlist" varStatus="counter">
                                            <tr id="reviewerpanel_tr_${counter.count - 1}">
                                                <td width="16%">
                                                    <a href="searchaerauthorization.htm?action=Reviewer" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick=" openModal('REVIEWER', '${counter.count - 1}');">Reviewer</a>
                                                </td>
                                                <td width="52%">
                                                    <form:hidden path="financialyear"/>
                                                    <form:hidden path="authoritytype"/>
                                                    <form:hidden class="form-control" path="reviewer" id="reviewer_${counter.count - 1}" value="${reviewerlist.reviewer}"/>
                                                    <form:input path="reviewername" id="reviewername_${counter.count - 1}" readonly="true" cssClass="form-control" value="${reviewerlist.reviewername}"/> 
                                                </td>
                                                <td width="10%">
                                                    <c:if test="${counter.count eq 1}">
                                                        <button type="button" class="btn btn-danger add-btn" onclick="addReviewer();">Add</button>
                                                    </c:if>
                                                    <c:if test="${counter.count ne 1}">
                                                        <button type="button" class="btn btn-danger removeBtn">Remove</button>
                                                    </c:if>
                                                </td>
                                                <td width="13%"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty aerAuthorizationBean.reviewerlist}">
                                        <tr id="reviewerpanel_tr_0">
                                            <td width="16%">
                                                <a href="searchaerauthorization.htm?action=Reviewer" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick=" openModal('REVIEWER', '0');">Reviewer</a>
                                            </td>
                                            <td width="52%">
                                                <form:hidden path="financialyear"/>
                                                <form:hidden path="authoritytype"/>
                                                <form:hidden class="form-control" path="reviewer" id="reviewer_0"/>
                                                <form:input path="reviewername" id="reviewername_0" readonly="true" cssClass="form-control"/> 
                                            </td>
                                            <td width="10%">
                                                <button type="button" class="btn btn-danger add-btn" onclick="addReviewer();">Add</button>
                                            </td>
                                            <td width="13%"></td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                            <br>
                            <table class="table table-borderless verifierpanel" width="100%">
                                <tbody>
                                    <c:if test="${not empty aerAuthorizationBean.verifierlist}">
                                        <c:forEach items="${aerAuthorizationBean.verifierlist}" var="verifierlist" varStatus="counter">
                                            <tr id="verifierpanel_tr_${counter.count - 1}">
                                                <td width="16%">
                                                    <a href="searchaerauthorization.htm?action=Verifier" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('VERIFIER', '${counter.count - 1}');">Verifier</a>
                                                </td>
                                                <td width="52%">
                                                    <form:hidden class="form-control" path="verifier" id="verifier_${counter.count - 1}" value="${verifierlist.verifier}"/>
                                                    <form:input path="verifiername" id="verifiername_${counter.count - 1}" readonly="true" cssClass="form-control" value="${verifierlist.verifiername}"/>
                                                </td>
                                                <td width="10%">
                                                    <c:if test="${counter.count eq 1}">
                                                        <button type="button" class="btn btn-danger add-btn" onclick="addVerifier();">Add</button>
                                                    </c:if>
                                                    <c:if test="${counter.count ne 1}">
                                                        <button type="button" class="btn btn-danger removeBtn">Remove</button>
                                                    </c:if>
                                                </td>
                                                <td width="13%"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty aerAuthorizationBean.verifierlist}">
                                        <tr id="verifierpanel_tr_0">
                                            <td width="16%">
                                                <a href="searchaerauthorization.htm?action=Verifier" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary" onclick="openModal('VERIFIER', '0');">Verifier</a>
                                            </td>
                                            <td width="52%">
                                                <form:hidden class="form-control" path="verifier" id="verifier_0"/>
                                                <form:input path="verifiername" id="verifiername_0" readonly="true" cssClass="form-control"/>
                                            </td>
                                            <td width="10%">
                                                <button type="button" class="btn btn-danger add-btn" onclick="addVerifier();">Add</button>
                                            </td>
                                            <td width="13%"></td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                            <br>
                            <div class="form-group">
                                <div class="col-sm-8"> 
                                    <span style="font-size: 15px;color: firebrick;font-style: italic;">Note : Head of Office of the Head of Department (Directorate) 
                                        will authorize two employees to work as <b>'Reviewer'</b> and <b>'Verifier'</b> of AER data submitted by Head of Office of subordinate/District Offices.
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <c:if test="${empty aerAuthorizationBean.reviewerlist && empty aerAuthorizationBean.verifierlist}">
                                <input type="submit" name="action" value="Save" class="btn btn-default"/>  
                            </c:if>
                            <c:if test="${not empty aerAuthorizationBean.reviewerlist || not empty aerAuthorizationBean.verifierlist}">
                                <input type="submit" name="action" value="Update" class="btn btn-default"/>  
                            </c:if>
                            <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                        </div>
                    </div>
                </div>
            </form:form>
        </c:if>
        <form:form action="aerauthorizationlist.htm" commandName="aerAuthorizationBean" method="post" onsubmit=" return validation()">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        AER Authorization (Operator and Approver)
                    </div>
                    <div class="panel-body">
                        <div class="form-group"> 
                            <div class="col-sm-2" >
                                <a href="searchaerauthorization.htm?action=Operator" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary">Operator</a>
                            </div>
                            <div class="col-sm-7"> 
                                <form:hidden path="financialyear"/>
                                <form:hidden path="authoritytype"/>
                                <form:hidden class="form-control" path="operator"/>
                                <form:input path="operatorname" id="operatorname" readonly="true" cssClass="form-control"/>                            
                            </div>
                        </div><br><br>
                        <div class="form-group">
                            <div class="col-sm-2" >
                                <a href="searchaerauthorization.htm?action=Approver" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary">Approver</a>
                            </div>
                            <div class="col-sm-7" > 
                                <form:hidden class="form-control" path="approver"/>
                                <form:input path="approvername" id="approvername" readonly="true" cssClass="form-control"/>                            
                            </div>
                        </div><br><br>
                    </div>
                    <div class="panel-footer">

                        <c:if test="${empty aerAuthorizationBean.operator}">

                            <input type="submit" name="action" value="Save" class="btn btn-default"/>  
                        </c:if>
                        <c:if test="${not empty aerAuthorizationBean.operator}">
                            <input type="submit" name="action" value="Update" class="btn btn-default"/>  
                        </c:if>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                    </div>
                </div>
            </div>
        </form:form>

        <form:form action="saveEmpApproverAuth.htm" commandName="aerAuthorizationBean" method="post">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Profile Approver Authorization
                    </div>
                    <div class="panel-body">                                               
                        <div class="form-group">
                            <div class="col-sm-2" >
                                <a href="searchaerauthorization.htm?action=ProfileApprover" data-remote="false" data-toggle="modal" data-target="#myModal" class="btn btn-primary">Approver</a>
                            </div>
                            <div class="col-sm-7" > 
                                <form:hidden path="financialyear"/>
                                <form:hidden class="form-control" path="profileApprover"/>
                                <form:input path="profileApprovername" id="profileApprovername" readonly="true" cssClass="form-control"/>                            
                            </div>
                        </div><br><br>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${empty aerAuthorizationBean.profileApprover}">
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validationEmpProfileApprover()"/>  
                        </c:if>
                        <c:if test="${not empty aerAuthorizationBean.profileApprover}">
                            <input type="submit" name="action" value="Update" class="btn btn-default" onclick="return validationEmpProfileApprover()"/>  
                        </c:if>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                    </div>
                </div>
            </div>
        </form:form>
        
        
          
        
        
        <div class="modal" id="myModal">
            <div class="modal-dialog  modal-lg">

                <div class="modal-content">

                    <!-- Modal Header -->


                    <!-- Modal body -->
                    <div class="modal-body">
                        Modal body..
                    </div>

                    <!-- Modal footer -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>

                </div>
            </div>
        </div>    
    </div>
</body>
</html>
