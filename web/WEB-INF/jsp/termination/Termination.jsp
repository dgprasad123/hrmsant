<%-- 
    Document   : Retirement
    Created on : Apr 9, 2018, 4:20:18 PM
    Author     : Manas
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
            function toggleDropDown(me) {

            }
            function getDeptWiseOfficeList(type) {
                var transactionStatus = "";

                var deptcode;
                if (type == "A") {
                    deptcode = $('#authDeptCode').val();
                    $('#authOfficeCode').empty();
                } else if (type == "T") {
                    if ($("input[name=rdTransaction]:checked").length == 0) {
                        alert("Please select Transaction type");
                        return false;
                    }
                    transactionStatus = $("input[name=rdTransaction]:checked").val();
                    if (transactionStatus == "S") {
                        deptcode = $('#retiredfromdeptCode').val();
                        $('#retiredfromofficeCode').empty();
                    }
                }
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                if (type == "A") {
                    $('#authOfficeCode').append('<option value="">--Select Office--</option>');
                } else if (type == "T") {
                    if (transactionStatus == "S") {
                        url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                        $('#retiredfromofficeCode').append('<option value="">--Select Office--</option>');
                    }
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#authOfficeCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "T") {
                            if (transactionStatus == "S") {
                                $('#retiredfromofficeCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                            }
                        }
                    });
                    if (type == "A") {
                        $('#authOfficeCode').val('${Termination.authOfficeCode}');
                        getOfficeWisePostList('A');
                    } else if (type == "T") {
                        $('#retiredfromofficeCode').val('${Termination.retiredfromofficeCode}');
                        getOfficeWisePostList('T');
                    }
                });
            }
            function getOfficeWisePostList(type) {
                
                var transactionStatus = "";
                
                var offcode;
                if (type == "A") {
                    offcode = $('#authOfficeCode').val();
                    $('#authSpc').empty();
                } else if (type == "T") {
                    if ($("input[name=rdTransaction]:checked").length == 0) {
                        alert("Please select Transaction type");
                        return false;
                    }
                    transactionStatus = $("input[name=rdTransaction]:checked").val();
                    if (transactionStatus == "S") {
                        offcode = $('#retiredfromofficeCode').val();
                        $('#retiredfromspc').empty();
                    }
                }
                var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                if (type == "A") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#authSpc').append('<option value="">--Select Post--</option>');
                } else if (type == "T") {
                    if (transactionStatus == "S") {
                        $('#retiredfromspc').append('<option value="">--Select Post--</option>');
                    }
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                            $('#authSpc').val('${Termination.authSpc}');
                        } else if (type == "T") {
                            if (transactionStatus == "S") {
                                $('#retiredfromspc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                            }
                        }
                    });
                });
            }
            function savecheck() {

                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }

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
                    $("#authDeptCode")[0].focus();
                    return false;
                }
                if ($("#authOfficeCode").val() == "") {
                    alert("Please Select Authority Office");
                    $("#authOfficeCode")[0].focus();
                    return false;
                }
                if ($("#authSpc").val() == "") {
                    alert("Please Select Authority");
                    $("#authSpc")[0].focus();
                    return false;
                }
                if ($("#rettype").val() == "") {
                    alert("Please Select Retirement Type");
                    $("#rettype")[0].focus();
                    return false;
                }
                if ($("#duedate").val() == "") {
                    alert("Please Select Termination Date.");
                    $("#duedate")[0].focus();
                    return false;
                }

            }
            $(document).ready(function() {
                getDeptWiseOfficeList('A');

            });
        </script>
    </head>
    <body>
        <div class="col-md-8 col-sm-8" style="margin-top: 10px;">
            <div class="panel panel-default">
                <div class="panel-body">
                    <form:form class="form-horizontal" action="SaveEmployeeTermination.htm" commandName="Termination" method="post">
                        <form:hidden path="retiredEmpid"/>
                        <form:hidden path="curspc"/>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Select type of Transaction</label>

                            <span class="col-sm-3">  
                                <form:radiobutton path="rdTransaction" value="S"/> Service Book Entry(Backlog)
                            </span>
                            <span class="col-sm-5">
                                <form:radiobutton path="rdTransaction" value="C"/> Current Transaction(will effect current Pay or Post)
                            </span>
                            <div class="col-lg-4"></div>
                        </div>
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
                                <form:input path="ordDate" class="form-control" id="ordDate"/> 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                            
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Authority Type:</label>
                            <span class="col-sm-3"><form:radiobutton path="notType"  value="GOO" onclick="toggleDropDown(this)"/>&nbsp;Government of Orissa</span>

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
                                <form:select path="authOfficeCode" id="authOfficeCode" class="form-control" onchange="getOfficeWisePostList('A');">

                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(c) Authority:</label>
                            <span class="col-sm-10">
                                <form:select path="authSpc" id="authSpc" class="form-control">

                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Terminated From:</label>
                            <span class="col-sm-3"><form:radiobutton path="retauthtyp" value="GOO" onclick="toggleDropDown(this)"/>&nbsp;Government of Orissa</span>

                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(a) Department <span style="color: red">*</span>:</label>
                            <span class="col-sm-10">
                                <form:select path="retiredfromdeptCode" id="retiredfromdeptCode" class="form-control" onchange="getDeptWiseOfficeList('T');">
                                    <form:option value="">--Select One--</form:option>
                                    <form:options items="${departmentlist}" itemLabel="deptName" itemValue="deptCode"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(b) Office <span style="color: red">*</span>:</label>
                            <span class="col-sm-10">
                                <form:select path="retiredfromofficeCode" id="retiredfromofficeCode" class="form-control" onchange="getOfficeWisePostList('T');">
                                    <form:options items="${retFromOfficeList}" itemLabel="offName" itemValue="offCode"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(c)Termination Post <span style="color: red">*</span>:</label>
                            <span class="col-sm-10">
                                <form:select path="retiredfromspc" id="retiredfromspc" class="form-control">
                                    <form:options items="${retFromSPCList}" itemLabel="spn" itemValue="spc"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">                                                         
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Date of Termination<span style="color: red">*</span>:</label>
                            <span class="col-sm-1">
                                <form:checkbox path="retired" value="Y" class="form-control" style="display:none;"/>
                            </span>
                            <div class="col-sm-2 input-group date">
                                <form:input path="duedate" class="form-control"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Note(If Any):</label>
                            <span class="col-sm-10">
                                <form:textarea path="note" class="form-control"/>
                            </span>
                        </div>
                        <c:if test="${Termination.retid == null}">
                            <input type="submit" name="action" value="Save" class="btn btn-primary" onclick="return savecheck();"/>
                        </c:if>
                        <c:if test="${not empty Termination.retid}">
                            <input type="submit" name="action" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')"/>
                        </c:if>
                    </form:form>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(function() {
                $('#ordDate').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
                $('#duedate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false
                });
            }
            );
        </script>
    </body>
</html>
