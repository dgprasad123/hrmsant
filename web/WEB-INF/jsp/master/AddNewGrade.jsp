<%-- 
    Document   : AddNewState
    Created on : 5 May, 2022, 12:09:40 PM
    Author     : Devikrushna
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
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function saveCheck() {


                if ($('#gradename').val() == "")
                {

                    alert("Please enter Grade Name");
                    $('#gradename').focus();
                    return false;
                }
                if ($('#is_visible').val() == "")
                {
                    alert("Please enter whether visible");
                    $('#is_visible').focus();
                    return false;
                }
                if ($('#is_obsolate').val() == "")
                {
                    alert("Please enter whether obsolate");
                    $('#is_obsolate').focus();
                    return false;
                }
                return true;

            }
            function getCadreList() {
                var url = 'getCadreListJSON.htm?deptcode=' + $("#deptCode").val();
                var valText = $("#deptCode option:selected").html();
                $("#hiddenDeptName").val(valText);
                +
                        $.getJSON(url, function(data) {
                            $('#cadreCode').empty();

                            $('#cadreCode').append("<option value=\"\">--Select--</option>");

                            // $('#cadreCode').append($('<option>').text('Select Office').attr('value', ''));
                            $.each(data, function(i, obj) {
                                $('#cadreCode').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                        }).done(function() {

                });
            }
            $(document).ready(function() {
                $("#uid").blur(function() {
                    $.get("check_availability.java", {usr: $(this).val()}, function(data) {
                        if (data == 'available') {
                            data = "<font color='#00CC33'><img src='images/pass.png' align='absmiddle'/>&nbsp; Available</font>";
                        }
                        if (data == 'exist') {
                            $("#uid").val('');
                            data = "<font color='#ff0000'><img src='images/fail.png' align='absmiddle'/>&nbsp;Already Exist</font>";
                        }
                        $('#loading').html(data)
                    });//function
                });
            });
        </script>
        <style>
            .row-margin{
                margin-bottom: 20px;
                margin-top: 30px;
            }
            .first-row{
                margin-top: 20px;
            }
            .heading-color{
                color: #337ab7;
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
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>                                                           
                            </ol>
                        </div>
                    </div>
                    <div class="container-fluid">                      
                        <form:form action="saveGrade.htm" commandName="grade" method="post" onsubmit="return saveCheck()"> 
                            <form:hidden id='deptCode'  path='deptCode'/>                    
                            <form:hidden id='cadreCode'  path='cadreCode'/> 
                        </div>                        
                    </div>
                    <div class="row">

                        <div class="col-sm-4" style="font-weight: bold;">
                            Department Name:-
                            ${grade.deptName}                                    
                        </div>

                        <div class="col-sm-4" style="font-weight: bold;">
                            Cadre Name:-
                            ${grade.cadreName}       
                        </div> 
                    </div>
                    <div class="row row-margin first-row">
                        <label class="control-label col-sm-3">Grade Name</label> 
                        <div class="col-sm-6">
                            <form:input path="grade" id="gradename" class="form-control" style="margin-bottom:5px"/>
                        </div>
                    </div> 
                    <div class="row row-margin first-row">
                        <label class="control-label col-sm-3">No Of Sanction Post</label> 
                        <div class="col-sm-6" >                            
                            <form:input path="sanction" id="sanction" class="form-control" style="margin-bottom:5px"/>
                        </div>
                    </div> 
                    <div class="row">
                        <label class="control-label col-sm-3">Grade Level</label> 
                        <div class="col-sm-6" >                            
                            <form:input path="gradeLevel" class="form-control" style="margin-bottom:5px"/>
                        </div>
                    </div>         
                    <div class="row">
                        <label class="control-label col-sm-3">is visible?</label> 
                        <div class="col-sm-6" >   
                            <form:select path="is_visible"  class="form-control">
                                <form:option value="">Select</form:option>
                                <form:option value="Y">Yes</form:option>
                                <form:option value="N">No</form:option>
                            </form:select> 

                            </div>
                        </div> 
                        <div class="row">
                            <label class="control-label col-sm-3">is obsolete?</label> 
                            <div class="col-sm-6" >   
                                <form:select path="is_obsolate"  class="form-control">
                                <form:option value="">Select</form:option>
                                <form:option value="Y">Yes</form:option>
                                <form:option value="N">No</form:option>
                            </form:select> 
                                
                            </div>
                        </div> 
                        <div class="form-group col-sm-12" style="margin-top:20px">
                            <label class="control-label col-sm-1"></label>
                            <div class="text-center col-sm-12" >     
                                <input type="submit" class="btn btn-primary" name="action" value="Save Grade"/>
                                <a href="CadreWiseGrade.htm?action=search&deptCode=${grade.deptCode}&cadreCode=${grade.cadreCode}"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>
                            </div>
                        </div>   
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
