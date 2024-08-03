<%-- 
    Document   : chataccountList
    Created on : Oct 26, 2021
    Author     : Devikrushna
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                $('#ddoCode').chosen();
            })
             
             
             
            function getOfficeList() {
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + $("#deptCode").val();
                var valText = $("#deptCode option:selected").html();
                $("#hiddenDeptName").val(valText);

                $.getJSON(url, function (data) {
                    $('#ddoCode').empty();
                    
                     $('#ddoCode').append("<option value=\"\">--Select--</option>");
                    
                   // $('#ddoCode').append($('<option>').text('Select Office').attr('value', ''));
                    $.each(data, function (i, obj) {
                        $('#ddoCode').append($('<option>').text(obj.offName).attr('value', obj.ddoCode));
                    });
                }).done(function() {
                    $('#ddoCode').chosen();
                    $("#ddoCode").trigger("chosen:updated");
                });
            }
            
           function searchCheck(){
               if ($('#deptCode').val() == "")
                {
                    alert("Please select Department Name");
                    $('#deptCode').focus();
                    return false;
                }  
               if ($('#ddoCode').val() == "")
                {
                    alert("Please select Office Name");
                    $('#ddoCode').focus();
                    return false;
                }  
               
           }
           
           
         
        </script>
    </head>
    <body style="margin-top: 68px;">
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/> 
            <div id="page-wrapper">
                 <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Chart Of Account
                                </li>                            
                            </ol>
                        </div>
                    </div>
                <form:form  class="form-horizontal" action="ChatOfAccount.htm" commandName="chatOfAccount"  method="post">
                 
                    <div class="row">
                        <div class="col-lg-12">
                            <h3>Add Chart Of Account</h3>

                            <div class="form-group">
                                <label class="control-label col-sm-2">Department Name: </label>
                                <div class="col-sm-8">
                                    <form:select class="form-control" path="hiddendeptCode" id="deptCode" onchange="getOfficeList()" >
                                        <form:option value="">Select</form:option>
                                        <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                                                               
                                    </form:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Office Name</label>
                                <div class="col-sm-8">
                                    <form:select class="form-control" path="ddoCode" id="ddoCode">
                                        <option value="">Select</option>
                                        <form:options items="${officelist}" itemLabel="offName" itemValue="ddoCode"/>
                                    </form:select>
                                </div>
                            </div>
                            <div class="text-center">
                                <button type="submit" id="search-btn" 
                                        name="action" value="search" class="btn btn-primary" style="margin-bottom: 50px;" onclick="return searchCheck()">Search</button>
                            </div>
                        </div>
                    </div>
                   
                    <c:if test="${not empty chatOfAccount.ddoCode}">
                        <div class="text-center" id="add-new">
                            <a href="addNewChartOfAccount.htm?ddoCode=${chatOfAccount.ddoCode}&hiddendeptCode=${chatOfAccount.hiddendeptCode}">
                                <button type="button" class="btn btn-primary">Add Chart Of Account</button></a>

                            <%-- <a href="addallNewChartofAccount.htm?ddoCode=${chatOfAccount.ddoCode}&hiddendeptCode=${chatOfAccount.hiddendeptCode}">
                                <button type="button" class="btn btn-primary" style="width: 8%;">Add New</button></a> --%>
                        </div> 
                    </c:if>
                </form:form>
                <div class="panel-body">  
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th>Sl No</th>
                                <th>Demand Number</th>
                                <th>Major Head</th>
                                <th>Sub Major Head</th>
                                <th>Minor Head</th>
                                <th>Sub Head</th>
                                <th>Detail Head</th>
                                <th>Charge Voted</th>
                                <th>Object Head</th>
                                <th>Edit</th>
                            </tr>
                        </thead>
                        <tbody>  
                        <c:if test="${not empty chatOfAccount.ddoCode}">
                            <c:if test="${empty ChartOfAccountList}">
                                <div class="text-center" style="margin: 30px;">
                                    <h4 style="color: #071c59ab;font-size: 20px;">Chart Of Account not available, Add as required.</h4>
                                </div>
                            </c:if>
                        </c:if>
                        <c:if test="${not empty ChartOfAccountList}">            
                            <c:forEach var="item" items="${ChartOfAccountList}" varStatus="count">
                                 <c:if test="${not empty item.subMajorHead and not empty item.minorHead and not empty item.subHead  and not empty item.detailHead and not empty item.chargeVoted  and not empty item.objectHead}">  
                                <tr>                                         
                                    <td>${count.index + 1}</td>
                                    <td>${item.demandno}</td>
                                    <td>${item.majorHead}</td>                                     
                                    <td>${item.subMajorHead}</td>                               
                                    <td>${item.minorHead}</td>
                                    <td>${item.subHead}</td>
                                    <td>${item.detailHead}</td>
                                    <td>${item.chargeVoted}</td>
                                    <td>${item.objectHead}</td>                                   
                                    <td>
                                        <a href="editChartOfAccount.htm?ddoCode=${chatOfAccount.ddoCode}&chatId=${item.chatId}&hiddendeptCode=${chatOfAccount.hiddendeptCode}">
                                            <button type="submit" name="action" value="Edit" class="btn btn-primary">Edit<i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                                        </a>
                                    </td>
                                </tr>
                                 </c:if>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>

                </div> 

                </body>
                
                </html>


