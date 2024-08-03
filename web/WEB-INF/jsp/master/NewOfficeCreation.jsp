<%-- 
    Document   : NewOfficeCreation
    Created on : Oct 30, 2021, 11:48:06 AM
    Author     : Madhusmita
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <style>
            .form-control1 {
                display: block;
                width: 70%;
                height: 34px;
                padding: 6px 12px;
                font-size: 14px;
                line-height: 1.42857143;
                color: #555;
                background-color: #fff;
                background-image: none;
                border: 1px solid #ccc;
                border-radius: 4px;
                -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
                box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
                -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
                -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
                transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
            }

        </style>
        <script type="text/javascript">
            function saveNewOfficeCreation() {
                //var dptCode =$('#deptCode');

                if ($('#offEn').val() == "") {
                    alert("Office Name Can't be Blank");
                    return false;
                }
                
                if ($('#category').val() == "") {
                    alert("Category Can't be Blank");
                    return false;
                }
                if ($("#stateCode").val() == "21" && $('#deptCode').val() == "") {
                    alert("Department Name Can't be Blank");
                    return false;
                }
                if($("#stateCode").val() == ""){
                    alert("Please select State");
                    return false;
                }
                if ($("#stateCode").val() == "21" && $('#distCode').val() == "") {
                    alert("District Name Can't be Blank");
                    return false;
                }
                if ($('#isDdo').val() == "") {
                    alert("Select Status of Is DDO");
                    return false;
                }
                if ($("#stateCode").val() == "21" && $('#lvl').val() == "") {
                    alert("Office Level Can't be Blank");
                    return false;
                }
                if ($('#ddoCode').val() == "") {
                    alert("DDO Code Can't be Blank");
                    return false;
                }
                if ($('#offStatus').val() == "") {
                    alert("Office Status Can't be Blank");
                    return false;
                }
                //return true;
                if (confirm("Are You sure to Create New Office?"))
                {
                    return true;

                }
                else {
                    return false;
                }
            }
            function checkMaxLength(){
                if($('#offCode').val().length!=13){
                    alert("Enter 13 digit Office Code");
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <form:form action="newofficecreation.htm" commandName="newOffice">                    
                    <div class="container-fluid">
                        <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newofficecreation.htm">New Office</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="editOfficeDetails.htm">Office Rename</a>
                                </li>
                                <li class="active">
                                    <%--<i class="fa fa-file"></i> <a href="newofficecreation.htm?&hidOfcType=B">Backlog Office</a>--%>
                                    <i class="fa fa-file"></i> <a href="backlogOfficeCreation.htm">Backlog Office</a>                                    
                                </li>
                            </ol>
                        </div>
                    </div>
                        <div class="panel panel-default">
                            <div class="panel panel-heading">
                                <div style="text-align:center;border:2px;font-weight: bold;font-size: 20px ">
                                    NEW OFFICE CREATION
                                </div>
                                <div class="row bg-primary" style="padding: 10px;">Office Information</div>
                                <table class="table-bordered">
                                    </br>
                                    <div class="row" style="align:left;">
                                        <h3 style="color: red;size: 80px;text-align:center">${msgofcexist}</h3>
                                        <h3 style="color: green;size: 80px;text-align:center">${msgofceNotxist}</h3>
                                        <%--<h2 style="color: blue;size: 100px;text-align:center">${msgOfcCreated}</h2>--%>
                                    </div>
                                    </br>
                                    
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">1.&emsp;Office Code <span style="color: red">*</span></label>
                                        <div class="col-sm-3">
                                            <form:input class="form-control" id="offCode" maxlength="13" path="offCode"/>
                                        </div>
                                        <input type="submit" name="search" value="Search Before Creation" class="btn btn-primary" onclick="return checkMaxLength();" style="text-align:left;margin-left:100px"/> 
                                    </div> 
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px;alignment-adjust:left;">2.&emsp;Office Name <span style="color: red">*</span></label>
                                        <div class="col-sm-5">
                                            <form:input class="form-control" path="offEn" id="offEn"/>
                                        </div>
                                    </div>
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">3.&emsp;Category <span style="color: red">*</span></label>
                                        <div class="col-sm-5">
                                            <form:input  path="category" id="category" class="form-control1"/>
                                        </div>
                                    </div> 
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">4.&emsp;Suffix</label>
                                        <div class="col-sm-5">
                                            <form:input class="form-control1" path="suffix"/>
                                        </div>
                                    </div> 
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">5.&emsp;Department Name <span style="color: red">*</span></label>
                                        <div class="col-sm-5">
                                            <form:select  path="deptCode" id="deptCode" class="form-control">
                                                <form:option value="" label="--Select--"/>  
                                                <form:options items="${DeptList}" itemLabel="deptName" itemValue="deptCode"/>
                                            </form:select>
                                        </div>
                                    </div>  
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">6.&emsp;State</label>
                                        <div class="col-sm-5" style="size:20">
                                            <form:select id="stateCode" path="stateCode" class="form-control1">
                                                <form:option value="">--Select--</form:option>
                                                <form:options items="${statelist}" itemLabel="statename" itemValue="statecode"/>                                        
                                            </form:select>
                                        </div>
                                    </div>
                                    <br />
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">7.&emsp;District Name <span style="color: red">*</span></label>
                                        <div class="col-sm-5">
                                            <form:select  path="distCode" id="distCode" class="form-control1">
                                                <form:option value="" label="--Select--"/>
                                                <form:options items="${DistList}" itemLabel="distName" itemValue="distCode"/>
                                            </form:select>
                                        </div>
                                    </div>
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">8.&emsp;Is DDO <span style="color: red">*</span></label>
                                        <div class="col-sm-5">
                                            <form:select path="isDdo" id="isDdo" class="form-control1">
                                                <form:option value="">--Select--</form:option>
                                                <form:option value="Y">Yes</form:option> 
                                                <form:option value="N">No</form:option>                                             
                                            </form:select>
                                        </div> 
                                    </div>
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">9.&emsp;Office Level <span style="color: red">*</span></label>
                                        <div class="col-sm-5">
                                            <form:select path="lvl" id="lvl" class="form-control1">
                                                <form:option value="">--Select--</form:option>  
                                                <form:options items="${ofcLevelList}" itemLabel="lvlDesc" itemValue="lvl"/>
                                            </form:select>
                                        </div> 
                                    </div>
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">10.&emsp;DDO Code <span style="color: red">*</span></label>
                                        <div class="col col-sm-5">
                                            <form:input class="form-control1" path="ddoCode" id="ddoCode" maxlength="9"  size="20"/>
                                        </div>
                                    </div>
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">11.&emsp;Parent Office Code</label>
                                        <div class="col col-sm-5">
                                            <form:input class="form-control1" path="poffCode"  maxlength="13" size="20"/>
                                        </div>
                                    </div> 
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">12.&emsp;Office Status <span style="color: red">*</span></label>
                                        <div class="col-sm-5">
                                            <form:select  path="offStatus" id="offStatus" class="form-control1">
                                                <form:option value="">--Select--</form:option>
                                                <form:option value="F">Functional</form:option> 
                                                <form:option value="NF">Non Functional</form:option>                                             
                                            </form:select>
                                        </div> 
                                    </div>
                                    </br>
                                    <div class="row" style="align:center;">
                                        <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">13.&emsp;Online Bill Submission</label>
                                        <div class="col-sm-5" style="size:20">
                                            <form:select id="onlineBillSubmission" path="onlineBillSubmission" class="form-control1" >
                                                <form:option value="">--Select--</form:option>
                                                <form:option value="Y">Yes</form:option> 
                                                <form:option value="N">No</form:option>                                             
                                            </form:select>
                                        </div>
                                    </div>
                                    <br/><br/>                                    
                                </table>
                                <div class="panel-footer">
                                    <input type="submit" value="Create" name="saveOffice" class="btn btn-primary" onclick="return saveNewOfficeCreation();"/>    
                                    <input type="submit" value="Exit" name="exit" class="btn btn-primary"/>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
