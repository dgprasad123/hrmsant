<%-- 
    Document   : ModuleList
    Created on : Nov 21, 2016, 6:08:30 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" href="css/chosen.css">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
             $(document).ready(function() {
                $('#deptCode').chosen();
            });
            function getOfficeList() {
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + $("#deptCode").val();
                var valText = $("#deptCode option:selected").html();
                $("#hiddenDeptName").val(valText);

                $.getJSON(url, function(data) {
                    $('#offCode').empty();
                    $('#postcode').empty();
                    $('#offCode').append($('<option>').text('Select Office').attr('value', ''));
                    $.each(data, function(i, obj) {
                        $('#offCode').append($('<option>').text(obj.offName).attr('value', obj.offCode));
                    });
                }).done(function() {
                    $('#offCode').chosen();
                    $("#offCode").trigger("chosen:updated");
                });
            }
            function getPostList() {

                var valText = $("#offCode option:selected").html();
                $("#hiddenOfficeName").val(valText);
            }
            function auto_select_result(vals) {
                vals = vals.toUpperCase();
                document.getElementById('id_search').value = vals;
                var Ocode = document.getElementById('offCode').value;
                vals = vals + "0000";
                // $("#hiddenOfficeName").val(vals);

                $('#offCode').val(vals);
                getPostList();
                //   alert(Ocode);
                //alert(vals);
            }
            function duplicateSpc() {
                var offcode = $("#offCode").val();
                var url = "duplicateSPC.htm?officeCode=" + offcode;

                //window.location = url;
                window.open(url, '_blank');
            }

        </script>
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
                                <li class="active">
                                    <i class="fa fa-file"></i> Privilege List 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <form role="form" class="form-horizontal" action="substantivePostDetails.htm" commandName="substantivePostDetails"  method="post">
                        <input type="hidden" name='hiddenDeptName' value='' id='hiddenDeptName'/>
                        <input type="hidden" name='hiddenOfficeName' value='' id='hiddenOfficeName'/>
                        <input type="hidden" name='hidOffCode' value='' id='hidOffCode'/>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>Substantive Post</h2>

                                <div class="form-group">
                                    <label class="control-label col-sm-1">Department</label>
                                    <div class="col-sm-10">
                                        <select class="form-control"  name="deptCode" id="deptCode" onchange="getOfficeList()" required="1">
                                            <option value=''>Select Department</option>
                                            <c:forEach items="${deptList}" var="dept" >
                                                <option value="${dept.deptCode}">${dept.deptName}</option>
                                            </c:forEach>                                        
                                        </select>   
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-sm-1">Office</label>
                                    <div class="col-sm-8">
                                        <select class="form-control"  name="offCode" id="offCode"   onchange="getPostList()" required="1">                             
                                            <option value=''>Select Office</option>
                                        </select>
                                    </div>
                                    <div  class="col-sm-2">
                                        <input class="form-control" type="text" id='id_search' onblur="auto_select_result(this.value)" maxlength="9"/>
                                    </div>
                                </div>
                                <input type='submit' name='Submit' value='Submit' class="btn btn-primary" />
                                &nbsp;&nbsp;&nbsp;
                                <%--<input type='submit' name='Submit' value='CHECK DUPLICACY' class="btn btn-danger" />--%>
                                <c:if test="${userType=='A'}">
                                    <span>
                                        <a href="javascript:duplicateSpc();"><button type="button" class="btn btn-danger"> CHECK DUPLICACY</button></a>
                                    </span>
                                </c:if>


                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>

        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">


            </div>
        </div>
    </body>
</html>
