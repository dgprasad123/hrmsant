<%-- 
    Document   : backlogOffice
    Created on : Nov 8, 2021, 3:35:30 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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
        <script type="text/javascript">
            var mode = "B";
            function getOfficeList() {
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + $("#deptCode").val();
                var valText = $("#deptCode option:selected").html();
                $("#hiddenDeptName").val(valText);
               // alert($("#deptCode").val());
                $.getJSON(url, function(data) {
                    $('#offCode').empty();
                    //$('#postcode').empty();
                    $('#offCode').append($('<option>').text('Select Post').attr('value', ''));
                    $.each(data, function(i, obj) {
                        $('#offCode').append($('<option>').text(obj.offName).attr('value', obj.offCode));
                    });
                });
            }
            function saveNewOfficeCreation() {
                //var dptCode =$('#deptCode');

                if ($('#offEn').val() == "") {
                    alert("Office Name Cann't be Blank");
                    return false;
                }

                if ($('#category').val() == "") {
                    alert("Category Cann't be Blank");
                    return false;
                }
                if ($('#deptCode').val() == "") {
                    alert("Department Name Cann't be Blank");
                    return false;
                }
                if ($('#distCode').val() == "") {
                    alert("District Name Cann't be Blank");
                    return false;
                }
                if ($('#lvl').val() == "") {
                    alert("Office Level Cann't be Blank");
                    return false;
                }
                if ($('#offStatus').val() == "") {
                    alert("Office Status Cann't be Blank");
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

        </script>
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
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">

                    </div>
                    <form:form action="backlogOfficeCreation.htm" commandName="backlogOfcForm"  method="post">
                        <form:hidden path="maxOfcCode"/>
                        <form:hidden path="hidDeptCode"/>
                        <form:hidden path="hidOfficeCode"/>

                        <div class="row">
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
                                <div class="panel panel-heading">
                                    <div class="col-lg-12">
                                        <div class="row bg-primary" style="padding: 15px;size:80px">Backlog Office Creation</div>
                                        <br/><br/>
                                        <div class="row" style="align:center;">
                                            <label class="col-sm-2" style="font-size:20px; alignment: left;">Department: </label>
                                            <div class="col-sm-8">
                                                <c:if test="${not empty backlogOfcForm.maxOfcCode}">
                                                    <form:select path="deptCode" id="deptCode" class="form-control" disabled="true">
                                                        <form:option value="" label="--Select--"/>
                                                        <form:options items="${DeptList}" itemLabel="deptName" itemValue="deptCode"/>
                                                    </form:select>
                                                </c:if>
                                                <c:if test="${empty backlogOfcForm.maxOfcCode}">
                                                    <form:select path="deptCode" id="deptCode" class="form-control" onchange="getOfficeList();" >
                                                        <form:option value="" label="--Select--"/>
                                                        <form:options items="${DeptList}" itemLabel="deptName" itemValue="deptCode"/>
                                                    </form:select>
                                                </c:if>
                                            </div>
                                        </div>  
                                        <br/>
                                        <div class="row" style="align:center;">
                                            <label class="col-sm-2" style="font-size:20px; alignment: left;">Office:</label>
                                            <div class="col-sm-8" style="">
                                                <c:if test="${not empty backlogOfcForm.maxOfcCode}">
                                                    <form:select  path="offCode" id="offCode" class="form-control" >
                                                        <form:option value="" label="--Select--"/>  
                                                        <form:options items="${offList}" itemLabel="offName" itemValue="offCode"/>
                                                    </form:select>
                                                </c:if>
                                                <c:if test="${empty backlogOfcForm.maxOfcCode}">
                                                    <form:select  path="offCode" id="offCode" class="form-control" >
                                                        <form:option value="" label="--Select--"/>  
                                                        <form:options items="${offList}" itemLabel="offName" itemValue="offCode"/>
                                                    </form:select>
                                                </c:if>
                                            </div>
                                            <%-- <c:if test="${not empty backlogOfcForm.maxOfcCode && not empty hiddenmode}">
                                                 <div class="row" style="align:center;">
                                                     <label class="col-sm-2" style="font-size:20px; alignment: left;">Department: </label>
                                                     <div class="col-sm-8">
                                                         <form:select  path="deptCode" id="deptCode" class="form-control" disabled="true">
                                                             <form:option value="" label="--Select--"/>  
                                                             <form:options items="${DeptList}" itemLabel="deptName" itemValue="deptCode"/>
                                                         </form:select>
                                                     </div>
                                                 </div>  
                                                 <br/>
                                                 <div class="row" style="align:center;">
                                                     <label class="col-sm-2" style="font-size:20px; alignment: left;">Office:</label>
                                                     <div class="col-sm-8" style="">
                                                         <form:select  path="offCode" id="offCode" class="form-control" disabled="true">
                                                             <form:option value="" label="--Select--"/>  
                                                             <form:options items="${offList}" itemLabel="offName" itemValue="offCode"/>
                                                         </form:select>
                                                     </div>
                                                 </c:if>--%>
                                            <input type="submit" name="search" value="Generate New Office Code" class="btn btn-primary" style="text-align:left;margin-left:100px"/> 
                                        </div> 
                                        <br/><br/>
                                        <div style="align:center;" class="row bg-success">
                                            <label class="col-sm-5" style="font-size:20px; text-align: right;">New BackLog Office Code:</label>                                                                                             
                                            <div  class="col-sm-5" style="font-size:20px; font-style: bold;text-align: left;" >
                                                <c:out value="${backlogOfcForm.maxOfcCode}"/>
                                            </div>
                                            <div class="col-sm-2">                                                    
                                                <%--<input type="submit" name="createOffice" value="Create Office" class="btn btn-primary" style="text-align:left;margin-left:100px"/>--%>
                                                <a href="createNonexistBacklogOfc.htm?maxOfcCode=${backlogOfcForm.maxOfcCode}&deptCode=${backlogOfcForm.deptCode}&offCode=${backlogOfcForm.offCode}"><button type="button" class=" btn btn-success" >Create Office</button></a>
                                                <%--<c:if test="${backlogOfcForm.maxOfcCode ne ''}">
                                                    <jsp:include page="../master/NewOfficeCreation.jsp"/>
                                                </c:if>--%>
                                            </div>
                                        </div>
                                        <br/>
                                        <%----------------------------------------------------------------------------------------Create New Back Log Office---------------------------------------------------------------%>                                    
                                        <c:if test="${not empty backlogOfcForm.maxOfcCode && not empty hiddenmode}">

                                            <div class="container-fluid">
                                                <div class="panel panel-default">
                                                    <div class="panel panel-heading">
                                                        <%--<div style="text-align:center;border:2px;font-weight: bold;font-size: 20px ">
                                                            NEW OFFICE CREATION
                                                        </div>--%>

                                                        <table class="table-bordered">
                                                            </br>                                                                

                                                            </br>
                                                            <div class="row" style="align:center;">
                                                                <label class="control-label col-sm-3" style="font-size:20px;alignment-adjust:left;">1.&emsp;Office Name <span style="color: red">*</span></label>
                                                                <div class="col-sm-5">
                                                                    <form:input class="form-control" path="offEn" id="offEn"/>
                                                                </div>
                                                            </div>
                                                            </br>
                                                            <div class="row" style="align:center;">
                                                                <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">2.&emsp;Category <span style="color: red">*</span></label>
                                                                <div class="col-sm-5">
                                                                    <form:input  path="category" id="category" class="form-control1"/>
                                                                </div>
                                                            </div> 
                                                            </br>
                                                            <div class="row" style="align:center;">
                                                                <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">3.&emsp;Suffix</label>
                                                                <div class="col-sm-5">
                                                                    <form:input class="form-control1" path="suffix"/>
                                                                </div>
                                                            </div>                                                                      
                                                            </br>
                                                            <div class="row" style="align:center;">
                                                                <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">4.&emsp;District Name <span style="color: red">*</span></label>
                                                                <div class="col-sm-5">
                                                                    <form:select  path="distCode" id="distCode" class="form-control1">
                                                                        <form:option value="" label="--Select--"/>
                                                                        <form:options items="${DistList}" itemLabel="distName" itemValue="distCode"/>
                                                                    </form:select>
                                                                </div>
                                                            </div>
                                                            </br>

                                                            <div class="row" style="align:center;">
                                                                <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">5.&emsp;Office Level <span style="color: red">*</span></label>
                                                                <div class="col-sm-5">
                                                                    <form:select path="lvl" id="lvl" class="form-control1">
                                                                        <form:option value="">--Select--</form:option>  
                                                                        <form:options items="${ofcLevelList}" itemLabel="lvlDesc" itemValue="lvl"/>

                                                                    </form:select>
                                                                </div> 
                                                            </div>
                                                            </br>

                                                            <div class="row" style="align:center;">
                                                                <label class="control-label col-sm-3" style="font-size:20px; alignment-adjust: center;">6.&emsp;Office Status <span style="color: red">*</span></label>
                                                                <div class="col-sm-5">
                                                                    <form:select  path="offStatus" id="offStatus" class="form-control1">
                                                                        <form:option value="">--Select--</form:option>
                                                                        <form:option value="F">Functional</form:option> 
                                                                        <form:option value="NF">Non Functional</form:option>                                             
                                                                    </form:select>
                                                                </div> 
                                                            </div>
                                                            </br>

                                                            <br/><br/>                                    
                                                        </table>
                                                        <div class="panel-footer">
                                                            <input type="submit" value="Save" name="savebacklogOffice" class="btn btn-primary" onclick="return saveNewOfficeCreation();"/>    
                                                            <input type="submit" value="Cancel" name="cancel" class="btn btn-primary"/>
                                                        </div>

                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>