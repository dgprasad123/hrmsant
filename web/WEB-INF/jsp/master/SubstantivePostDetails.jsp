<%-- 
       Document   : ModuleList
       Created on : Nov 21, 2016, 6:08:30 PM
       Author     : Manas Jena
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
         <link rel="stylesheet" href="css/chosen.css">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#deptCode').chosen();

            });
            function getOfficeList() {
                var url = 'getOfficeListJSON.htm?deptcode=' + $("#deptCode").val();
                var valText = $("#deptCode option:selected").html();
                $("#hiddenDeptName").val(valText);

                $.getJSON(url, function (data) {
                    $('#offCode').empty();
                    $('#postcode').empty();
                    $('#offCode').append($('<option>').text('Select Post').attr('value', ''));
                    $.each(data, function (i, obj) {
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
                    <form role="form" action="substantivePostDetails.htm" commandName="substantivePostDetails"  method="post">
                        <input type="hidden" name='hiddenDeptName' value='' id='hiddenDeptName'/>
                        <input type="hidden" name='hiddenOfficeName' value='' id='hiddenOfficeName'/>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>Substantive Post Details</h2>

                                <div class="form-group">
                                    <label>Department</label>                                    
                                    ${DeptName}                            
                                </div>

                                <div class="form-group">
                                    <label>Office</label>
                                    ${officeName}
                                </div>



                            </div>
                        </div>
                    </form>
                    <div class="row" style="margin-top: 10px;">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl</th>
                                        <th>Post Name</th>
                                        <th>Post Code</th>  
                                        <th>Availability </th>
                                        <th>&nbsp; </th>
                                    </tr>
                                </thead>
                                <tbody id="privilegeList">
                                    <c:if test = "${not empty subList}"> 
                                        <c:set var="slno" value="${0}" />
                                        <c:forEach items="${subList}" var="subpost">
                                            <c:set var="slno" value="${slno +1}" />
                                            <tr>
                                                <td>${slno}</td>    
                                                <td>${subpost.postname}</td>
                                                <td>${subpost.postCode}</td>
                                                <td><c:if test = "${subpost.totalPost == 0}">${subpost.totalPost}</c:if><c:if test = "${subpost.totalPost > 0}"><a href="postToggleList.htm?deptCode=${subpost.deptCode}&postCode=${subpost.postCode}&offCode=${subpost.offCode}&officeName=${officeName}&postname=${subpost.postname}" style="display:block;background:#008900;color:#FFFFFF;font-size:12pt;width:50%;text-align:center;border-radius:5px;text-decoration:none;font-weight:bold;">${subpost.totalPost}</a></c:if></td>
                                                    <th>
                                                       <a href='addPostdetails.htm?deptCode=${subpost.deptCode}&officeCode=${subpost.offCode}&postCode=${subpost.postCode}' class="btn btn-primary"> Add Post</a>

                                                    <!--<c:if test="${userName eq '65' || userName eq '61' || userName eq 'services1' }">-->
                                                       <!-- <a href='addPostdetails.htm?deptCode=${subpost.deptCode}&officeCode=${subpost.offCode}&postCode=${subpost.postCode}' class="btn btn-primary"> Add Post</a>-->
                                                        <!-- </c:if>-->
                                                    </th>

                                                </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test = "${empty subList}">
                                        <tr>
                                            <th colspan="5" style='color:red' align='center'><h3>Data is not available</h3></th>
                                    </tr>
                                </c:if> 
                                </tbody>
                            </table>
                        </div>
                    </div>         

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