<%-- 
    Document   : CentreAllotmentForASIExam
    Created on : 21 Nov, 2022, 5:06:49 PM
    Author     : Manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>
        <script type="text/javascript">


            function openCenterAllotmentWindow() {
                var radioValue = $("input[name='centerCode']:checked").val();
                alert(radioValue);
                if (!radioValue) {
                    alert("Please Select the Center Name");
                } else if (radioValue) {
                    $('#setAllotment').modal('show');
                }
            }

            function assignCenterPrivilage1() {
                tCentercode = $("input[name='centerCode']:checked").val();
                tdistrict = $("input:checkbox[name='homedistrictName[]']:checked");
                alert(tdistrict);
                if (tdistrict && tCentercode) {
                    $.post("assignCenterPrivilage.htm", {officeCode: tdistrict, centerCode: tCentercode}, "json")
                            .done(function(data) {
                                if (data.msg == "Y") {
                                    alert("Saved Sucessfully");
                                }
                            })
                }
            }
            function assignCenterPrivilage(me, tdistrict, tCentercode) {
                tCentercode = $("input[name='centerCode']:checked").val();
                alert(tdistrict);
                var url = "assignCenterPrivilage.htm";
                $.post(url, {officeCode: tdistrict, centerCode: tCentercode})
                        .done(function(data) {
                            console.log($(me).html());
                            $(me).parent().html("<span>Assigned</span>");
                        });

            }

        </script>
        <style type="text/css">
            .table > tbody > tr > td{
                font-size: 12px;
            }
        </style>
    </head>



    <body>
        <jsp:include page="../tab/hrmsadminmenu.jsp"/> 
        <div id="wrapper"> 
            <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                <div class="row">
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i>  <a href="index.html">Center Allotment Detail</a>
                        </li>

                    </ol>
                    <div class="col-lg-2">
                        <button type="button" class="btn btn-primary" onclick="openCenterAllotmentWindow()">Set Center Allotment</button>
                    </div>
                    <div class="col-lg-2">
                        <form:form action="getCenterPrivilageList.htm" commandName="nfm">
                            <input type="submit" name="action" class="btn btn-primary" value="Center Allotment Detail List">
                         </form:form>
                    </div>
                </div>


                <div class="row" id="showCadreDetail">
                    <div class="col-lg-12">
                        <h2>Center List</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="1%"></th> 
                                        <th>Sl No</th>
                                        <th>Center Name</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${centerList}" var="center" varStatus="count">
                                        <tr>
                                            <td><input type="radio" name="centerCode" value="${center.value}" /> </td>
                                            <td>${count.index + 1}</td>
                                            <td>${center.label}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>


            <div id="setAllotment" class="modal fade" >
                <div class="modal-dialog modal-lg">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Set Allotment List</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label class="control-label col-sm-3">District LIST </label>
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th width="3%">Serial No.</th>
                                                    <%--<th width="25%"></th> --%>
                                                <th width="25%">District Name</th>
                                                <th width="25%">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>                                        
                                            <c:forEach items="${districtList}" var="district" varStatus="cnt">
                                                <tr>
                                                    <td>${cnt.index + 1}</td>
                                                    <%--<td>
                                                        <input type="checkbox" name="homedistrictName" value="${district.value}" class="form-control"/>
                                                        
                                                    </td>--%>
                                                    <td>${district.label}</td>
                                                    <td>   <button type="button" onclick="assignCenterPrivilage(this, '${district.value}')" class="btn btn-default">Assign</button></td>                      
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>                               


                            </form>
                        </div>
                        <div class="modal-footer">

                            <button type="button" class="btn btn-primary" onclick="assignCenterPrivilage()">Assign Center</button>                            
                            <button type="button" class="btn btn-danger" data-dismiss="modal">cancel</button>
                        </div>

                    </div>
                </div>
            </div>



        </div>
    </body>
</html>


