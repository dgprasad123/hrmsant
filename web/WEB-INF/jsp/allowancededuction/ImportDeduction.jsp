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

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateForm() {
                if ($('#pvtDedn').val() == '') {
                    alert("Please select Private Deduction.");
                    return false;
                }
                if ($('#uploadedFile').val() == '') {
                    alert("Please attach a valid Excel File.");
                    return false;
                }
                var ext = $('#uploadedFile').val().split('.').pop();
                if (ext != 'xlsx' && ext != 'xls') {
                    alert("Please attach a valid Excel File.");
                    return false;
                }
                if (confirm("Are you sure to upload the Excel file?")) {
                    return true;
                } else {
                    return false;
                }
            }
            function validateDeductionForm() {
                if ($('#deduction').val() == '') {
                    alert("Please select Deduction.");
                    return false;
                }
                if ($('#uploadedDednFile').val() == '') {
                    alert("Please attach a valid Excel File.");
                    return false;
                }
                var ext = $('#uploadedDednFile').val().split('.').pop();
                if (ext != 'xlsx' && ext != 'xls') {
                    alert("Please attach a valid Excel File.");
                    return false;
                }
                if (confirm("Are you sure to upload the Excel file?")) {
                    return true;
                } else {
                    return false;
                }
            }
            
            function validateAllowanceForm(){
                if ($('#allowance').val() == '') {
                    alert("Please select Allowance.");
                    return false;
                }
                if($("#sltAllowanceFormula").val() == ""){
                    alert("Please select HRA Formula");
                    return false;
                }
                if ($('#uploadedAllowanceFile').val() == '') {
                    alert("Please attach a valid Excel File.");
                    return false;
                }
                var ext = $('#uploadedAllowanceFile').val().split('.').pop();
                if (ext != 'xlsx' && ext != 'xls') {
                    alert("Please attach a valid Excel File.");
                    return false;
                }
                if (confirm("Are you sure to upload the Excel file?")) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="UploadDeductionExcel.htm" method="POST" commandName="AllowanceDeductionbean" enctype="multipart/form-data">
            <div class="container-fluid">
                <div class="panel panel-default" style="margin-top:50px;">
                    <div class="panel-heading">
                        <h1 style="margin:0px;font-size:15pt;">Import Private Deduction / Deduction Data</h1>
                    </div>
                    <div class="panel-body">
                        <div class="col-lg-4" style="color:green">
                            <table  align="left" border="2"  width="100%" height="80%" cellspacing="1" style="font-size:12px; font-family:verdana;">
                                <tr>
                                <span><b>Select Private Deduction</b></span>
                                <form:select path="pvtDedn" id="pvtDedn" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${privateDednList}" itemValue="value" itemLabel="label"/>
                                </form:select>
                                </tr>
                                <tr>
                                    &emsp;
                                <input type="file" name="uploadedFile" id="uploadedFile" class="form-control"/>
                                <span style="color:#FF0000;font-weight:bold;">(Please attach Excel file only)</span>
                                </tr>
                            </table>
                        </div>
                        <div class="col-lg-4" style="color:green"> 
                            <table align="right" border="2" width="100%" height="80%" cellspacing="1" style="font-size:12px; font-family:verdana;">
                                <tr>
                                <span><b>Select Deduction</b></span>
                                <form:select path="deduction" id="deduction" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deductionList}" itemValue="value" itemLabel="label"/>
                                </form:select>
                                </tr>
                                <tr>
                                    &emsp;
                                <input type="file" name="uploadedDednFile" id="uploadedDednFile" class="form-control"/>
                                <span style="color:#FF0000;font-weight:bold;">(Please attach Excel file only)</span>
                                </tr>
                            </table>
                        </div>
                        <div class="col-lg-4" style="color:green"> 
                            <table align="right" border="2" width="100%" height="100%" cellspacing="1" cellpadding="2" style="font-size:12px; font-family:verdana;">
                                <tr>
                                    <span><b>Select Allowance</b></span>
                                    <form:select path="allowance" id="allowance" class="form-control">
                                        <form:option value="">--Select Allowance--</form:option>
                                        <form:option value="53">HRA</form:option>
                                    </form:select>
                                </tr>
                                <br />
                                <tr>                                    
                                    <form:select path="sltAllowanceFormula">
                                        <form:option value="">--Select Formula--</form:option>
                                        <form:options items="${hrafommulalist}" itemLabel="label" itemValue="value"/>                                     
                                    </form:select>
                                </tr>
                                <br />
                                <tr>
                                    &emsp;
                                    <input type="file" name="uploadedAllowanceFile" id="uploadedAllowanceFile" class="form-control"/>
                                    <span style="color:#FF0000;font-weight:bold;">(Please attach Excel file only)</span>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-lg-4">                            
                                <input type="submit" class="btn btn-primary" name="action" value="Save Private Deduction" onclick="return validateForm();"/>
                                <c:if test="${status == 'S'}">
                                    <span style="color:#00cc66;font-weight:bold;">Private Deduction Data Imported</span>
                                </c:if>
                            </div>
                            <div class="col-lg-4">                        
                                <%--<a href="javascript:void(0);"><button type="button" class="btn btn-default" onclick="return validateDeductionForm();">Save Deduction</button></a>--%>
                                <input type="submit" class="btn btn-primary" name="action" value="Save Deduction" onclick="return validateDeductionForm();"/>
                                <c:if test="${status == 'D'}">
                                    <span style="color:#00cc66;font-weight:bold;">Deduction Data Imported</span>
                                </c:if>
                            </div>
                            <div class="col-lg-4">                                
                                <input type="submit" class="btn btn-primary" name="action" value="Save Allowance" onclick="return validateAllowanceForm();"/>
                                <c:if test="${status == 'A'}">
                                    <span style="color:#00cc66;font-weight:bold;">Allowance Data Imported</span>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <br/><br/>
            <div class="row-centered">
                <div class="col-lg-12" align="center">
                    <img src="images/Sample_Data.jpg"/>
                    <a href="images/Sample_Data.xls">Click to Download Sample data</a>
                </div>
            </div>
        </div>
    </form:form>
</body>
</html>
