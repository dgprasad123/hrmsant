<%-- 
    Document   : Superannuation
    Created on : Dec 19, 2023, 12:55:26 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#ordDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $('#duedate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });
            function toggleDropDown(me) {

            }
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "A") {
                    deptcode = $('#authDeptCode').val();
                    $('#authOfficeCode').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                }
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                if (type == "A") {
                    $('#authOfficeCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#authOfficeCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                });
            }
            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "A") {
                    offcode = $('#authOfficeCode').val();
                    $('#authSpc').empty();
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#postedspc').empty();
                }
                var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                if (type == "A") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#authSpc').append('<option value="">--Select Post--</option>');
                } else if (type == "P") {
                    $('#postedspc').append('<option value="">--Select Post--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } else if (type == "P") {
                            $('#postedspc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        }
                    });
                });
            }

            function toggleButton() {
                var rettype = $('#rettype').val();
                //alert("rettype is: "+rettype);
                $('#retireId').html("");
                if (rettype != "") {
                    if (rettype != "SR" && $('#hidRestrictUser').val() == 'Y') {
                        $('#btnNSave').show();
                    } else if (rettype == "SR" && $('#hidRestrictUser').val() == 'Y') {
                        $('#btnNSave').hide();
                        $('#retireId').append("You can not retire.  N:B: check superannuation date.");
                    } else if (rettype == "SR" && $('#hidRestrictUser').val() == 'N') {
                        $('#btnNSave').show();
                    }


                }
            }

            function savecheck() {
                var ordNo = document.getElementById('ordno');
                var orddate = document.getElementById("ordDate");
                if (ordNo.value != '') {
                    if (/[^A-Za-z\d\-\/&(),. ]/.test(ordNo.value)) {
                        alert("Only letter and numeric characters and / & ( ) , . are allowed");
                        ordNo.focus();
                        ordNo.select();
                        return false;
                    }
                } else {
                    alert("Order No Cannot be Blank");
                    return false;
                    ordNo.focus();
                    ordNo.select();
                    return false;
                }


                if (orddate.value == '') {
                    alert("Order Date Cannot be Blank");
                    return false;

                }
                if ($("#authDeptCode").val() == "") {
                    alert("Please Select Authority Department");
                    $("#authDeptCode").focus();
                    return false;
                }
                if ($("#authOfficeCode").val() == "") {
                    alert("Please Select Authority Office");
                    $("#authOfficeCode").focus();
                    return false;
                }
                if ($("#authSpc").val() == "") {
                    alert("Please Select Authority");
                    $("#authSpc").focus();
                    return false;
                }
                if ($("#retPay").val() == "") {
                    alert("Please Update Last pay of Superannuation");
                    $("#retPay").focus();
                    return false;
                }
            }

            function isNumber(evt) {
                evt = (evt) ? evt : window.event;
                var charCode = (evt.which) ? evt.which : evt.keyCode;
                if (charCode > 31 && (charCode < 48 || charCode > 57)) {
                    alert("Only numerics are allowed");
                    return false;

                }
                return true;
            }
        </script>
    </head>
    <body>
        <div class="col-md-9 col-sm-9" style="margin-top: 50px;margin-left: 140px;">
            <div class="panel panel-default">
                <div class="panel-body">
                    <h2 style="text-align: center;"><u>Superannuation Form</u></h2><br/>
                            <form:form class="form-horizontal" action="SaveEmpSuperannuation.htm" commandName="Retirement" method="post" onsubmit="return savecheck()">
                        <input type="hidden" id="hidRestrictUser" value="<c:out value='${restrictUser}'/>"/>
                        <form:hidden path="retiredEmpid"/>
                        <form:hidden path="curspc"/>
                        <form:hidden path="retid"/>
                        <form:hidden path="notId"/>
                        <form:hidden path="relieveId"/>
                      
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Employee Id:</label>
                            <span class="col-sm-2">${employee.empid}</span>
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Employee Name:</label>
                            <span class="col-sm-4">${employee.fullname}</span>
                        </div>                        
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">GPF/PRAN No:</label>
                            <span class="col-sm-2">${employee.gpfno}</span>
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Post:</label>
                            <span class="col-sm-4">${employee.post}</span>
                        </div>                                               
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Order No:</label>
                            <span class="col-sm-2">
                                <form:input path="ordno" class="form-control"/>
                            </span>
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Order Date:</label>
                            <div class="col-sm-2 input-group date">
                                <form:input path="ordDate" class="form-control" id="ordDate" readonly="true"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                            
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Authority Type: </label>
                            <span class="col-sm-3">&nbsp;Government of Orissa</span>
                            <%--<span class="col-sm-3"><form:radiobutton path="notType"  value="GOO" onclick="toggleDropDown(this)"/>&nbsp;Government of Orissa</span>--%>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(a) Department:</label>
                            <span class="col-sm-10">
                                <form:select path="authDeptCode" class="form-control" onchange="getDeptWiseOfficeList('A');">
                                    <form:option value="">--Select One--</form:option>
                                    <form:options items="${departmentlist}" itemLabel="deptName" itemValue="deptCode"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(b) Office:</label>
                            <span class="col-sm-10">
                                <form:select path="authOfficeCode" class="form-control" onchange="getOfficeWisePostList('A');">
                                    <form:option value="">--Select One--</form:option>
                                    <form:options items="${authOfficeList}" itemLabel="offName" itemValue="offCode"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(c) Authority:</label>
                            <span class="col-sm-10">
                                <form:select path="authSpc" class="form-control">
                                    <form:option value="">--Select One--</form:option>
                                    <form:options items="${authSPCList}" itemLabel="spn" itemValue="spc"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(d) Last Pay As On <br/>Retirement Date<br/> <span style="font-size: 15px;">${employee.dor}</span> :</label>
                            <span class="col-sm-2">
                                <form:input path="retPay" class="form-control" onkeypress="return isNumber(event)"/>
                            </span>
                        </div>


                        <div class="form-group" style="text-align: center;">
                            <c:if test="${empty Retirement.notId || Retirement.notId eq null || Retirement.notId eq ''}">
                                <input type="submit" name="action" value="Save" id="btnYSave" class="btn btn-success" style="width:100px;font-weight:bold" onclick="return confirm('Are you sure to Save. ?')"/>&nbsp;
                            </c:if>
                        </div>


                    </form:form>
                </div>
            </div>
        </div>

    </body>
</html>
