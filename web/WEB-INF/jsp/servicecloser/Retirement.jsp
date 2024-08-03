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

                    if (rettype == "SR") {
                        $('#duedate').unbind();
                        $(".duedateclass").removeAttr("id");
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
                if ($("#rettype").val() == "") {
                    alert("Please Select Retirement Type");
                    $("#rettype").focus();
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div class="col-md-8 col-sm-8" style="margin-top: 10px;">
            <div class="panel panel-default">
                <div class="panel-body">
                    <form:form class="form-horizontal" action="SaveEmployeeRetirment.htm" commandName="Retirement" method="post" onsubmit="return savecheck()">
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
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Type of Retirement:</label>
                            <span class="col-sm-10">
                                <form:select path="rettype" id="rettype" class="form-control" onchange="toggleButton();">
                                    <form:option value="">--Select One--</form:option>
                                    <form:option value="VR">VOLUNTARY RETIREMENT</form:option>
                                    <form:option value="SR">RETIREMENT ON SUPERANNUATION</form:option>
                                    <form:option value="CR">PREMATURE RETIREMENT</form:option>
                                    <form:option value="IV">ON INVALIDATION</form:option>
                                    <form:option value="AD">ABSCONDING</form:option>
                                </form:select>
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Retired From:</label>
                            <span class="col-sm-3"><form:radiobutton path="retauthtyp" value="GOO" onclick="toggleDropDown(this)"/>&nbsp;Government of Orissa</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(a) Department <span style="color: red">*</span>:</label>
                            <span class="col-sm-10">
                                <form:select path="retiredfromdeptCode" class="form-control">
                                    <form:option value="">--Select One--</form:option>
                                    <form:options items="${departmentlist}" itemLabel="deptName" itemValue="deptCode"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(b) Office <span style="color: red">*</span>:</label>
                            <span class="col-sm-10">
                                <form:select path="retiredfromofficeCode" class="form-control">
                                    <form:options items="${retFromOfficeList}" itemLabel="offName" itemValue="offCode"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">(c)Retired Post <span style="color: red">*</span>:</label>
                            <span class="col-sm-10">
                                <form:select path="retiredfromspc" class="form-control">
                                    <form:options items="${retFromSPCList}" itemLabel="spn" itemValue="spc"/>
                                </form:select> 
                            </span>
                        </div>
                        <div class="form-group">                                                         
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Retired <span style="color: red">*</span>:</label>
                            <span class="col-sm-1">
                                <form:checkbox path="retired" value="Y" class="form-control"/>
                            </span>
                            <label class="control-label col-sm-3" style="padding-top: 0px;">Due Date of Retirement <span style="color: red">*</span>:</label>
                            <div class="col-sm-2 input-group date">
                                <form:input path="duedate" id="duedate" class="form-control duedateclass" readonly="true"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>

                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" style="padding-top: 0px;">Note(If Any):</label>
                        <span class="col-sm-10">
                            <form:textarea path="note" class="form-control"/>
                        </span>
                    </div>

                    <div class="form-group">
                        <c:if test="${Retirement.retid == null}">
                            <c:if test="${restrictUser eq 'N'}">
                                <input type="submit" name="action" value="Save" id="btnYSave" class="btn btn-success" onclick="return confirm('Are you sure to Save. ?')"/>
                            </c:if>
                            <c:if test="${restrictUser eq 'Y'}">
                                <input type="submit" name="action" value="Save" id="btnNSave" style="display:none;" class="btn btn-success" onclick="return confirm('Are you sure to Save. ?')"/>
                            </c:if>
                            <span style="color:red" id="retireId">
                                <c:if test="${restrictUser eq 'Y'}">
                                    You can not retire.  N:B: check superannuation date. 
                                </c:if>
                            </span>
                        </c:if>
                        <c:if test="${not empty Retirement.retid && empty Retirement.sbDescription}">
                            <input type="submit" name="action" value="Save" id="btnYSave" class="btn btn-success" onclick="return confirm('Are you sure to Save. ?')"/>&nbsp;
                        </c:if>
                        <c:if test="${not empty Retirement.retid}">
                            <input type="submit" name="action" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to Delete.?')"/>
                        </c:if>
                    </div>

                </form:form>
            </div>
        </div>
    </div>

</body>
</html>
