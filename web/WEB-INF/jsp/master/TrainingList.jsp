<%-- 
    Document   : BankList
    Created on : Aug 1, 2018, 1:02:31 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
            function editTraining(trainingId) {           
               $('#newTraining').modal('toggle');
                $.getJSON("edittrainingMaster.htm?trainingId="+trainingId, function(data) {        
                    $('#hiddentrainingid').val(data.trainingId);
                    $('#trainingType').val(data.trainingType);
                    $('#trainingTitle').val(data.trainingTitle);

                }).done(function() {
                });
            }
            function removeval(){
                document.getElementById("trainingType").value = "";
                document.getElementById("trainingTitle").value = "";
            }
        </script>
        <style>
            .close {
                color: white;
                float: right;
                font-size: 28px;
                font-weight: bold;
            }
            .close:hover,
            .close:focus {
                color: #000;
                text-decoration: none;
                cursor: pointer;
            }
            .main-modal{
                margin-top: 100px;
                padding-right: 240px;
            }
            .modal-title{
                padding: 5px;
                font-family: verdana;
            }
            
            .modal-header {
                padding: 2px 16px;
                background-color: lightblue;
                color: white;
            }
            .modal-body {padding: 5px 30px;height: 100px;}

            .modal-footer {
                padding: 2px 16px;
                background-color: lightblue;
                color: white;
            }

            #myInput {
                background-image: url('/css/searchicon.png');
                background-position: 10px 10px;
                background-repeat: no-repeat;
                width: 100%;
                font-size: 16px;
                padding: 12px 20px 12px 40px;
                border: 1px solid #ddd;
                margin-bottom: 12px;
            }
            .table-main{  
                font-size:12px; 
                font-family:verdana;
                margin-top: 30px;
            }
            #myTable {
                border-collapse: collapse;
                width: 100%;
                border: 1px solid #ddd;
                font-size: 16px;
            }

            #myTable th, #myTable td {
                text-align: left;
                padding: 12px;
            }

            #myTable tr {
                border-bottom: 1px solid #ddd;
            }

            #myTable tr.header {
                background-color: darkslategrey;
                color: #FFFFFF;
            }
        </style>
    </head>
    <body>
        <form:form action="saveTrainingMaster.htm" commandName="trainingModel">
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
                                        <i class="fa fa-file"></i> Training List 
                                    </li>                                
                                </ol>
                            </div>
                        </div>                   
                         <form:hidden id="hiddentrainingid" path="hiddentrainingid"/>
                        <div class="row" style="margin-top: 10px;">
                            <div class="table-responsive">
                                <table id="myTable" class="table table-striped table-bordered table-success" width="100%" cellspacing="0">
                                    <thead>                                    
                                    <div class="col-50">
                                        <div class="pull-left">                                                        
                                            <h3>LIST OF Training</h3>                                            
                                        </div>
                                    </div>
                                    <div class="pull-right"> 
                                        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#newTraining"  style="width:150px">Add New Training</button>                                        
                                    </div>
                                    <tr class="header">
                                        <th>SL NO</th>
                                        <th>TRAINING CODE</th>
                                        <th>TRAINING NAME</th>
                                        <th>ACTION</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="traininglist" items="${training_list}" varStatus="count">
                                            <tr>
                                                <td><c:out value="${count.index+1}"/></td>
                                                <td><c:out value="${traininglist.trainingType}"/></td>
                                                <td><c:out value="${traininglist.trainingTitle}"/></td>                                           
                                                <td style="width:10%"><button type="button" class="btn btn-default btn-info" data-target="#newTraining"  onclick="editTraining(${traininglist.trainingId})" >Edit</button>     
                                                &nbsp;
                                                <a href="#" onclick="deleteTraining(${traininglist.trainingId})" class="btn  btn-default btn-warning"><span class="glyphicon glyphicons-remove" ></span>Delete</a>
                                                </td>                                    
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
                <div class="modal fade main-modal" id="newTraining" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->

                    <div class="modal-content modal-lg">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" onclick = "return removeval();">&times;</button>
                            <h4 class="modal-title">Add New Training</h4>
                        </div>
                        <div class="modal-body">
                            <div class=row">
                                <table border="0" width="100%"  cellspacing="0" class="table-main">

                                    <tr>                                         
                                        <td style="text-align:left; font-size: 18px;padding-left:5px">
                                            <form:label path="trainingType">TRAINING TYPE</form:label>
                                            </td>
                                            <td style="padding-right:5px">
                                            <form:select path="trainingType" id="trainingType" cssClass="form-control" style="width:450px;">
                                                <form:option value="">Select</form:option>
                                                <form:options items="${trainingTypeList}" itemLabel="trainingType" itemValue="trainingTitleId"/>                                    
                                            </form:select>                                                 
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>                                         
                                        <td style="text-align:left; font-size: 18px;padding-left:5px">
                                            <form:label path="trainingTitle">ENTER TRAINING TITLE</form:label>
                                            </td>
                                            <td style="padding-right:5px">
                                            <form:input path="trainingTitle" class="form-control" id="trainingTitle" style="width: 84%;"/>                                                     
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <br>
                        <div class="modal-footer" style="margin-top: 30px;">
                            <input type="submit" name="submit" class="btn btn-default" style="width:70px" value="Save"/> 
                            <button type="button" class="btn btn-default" data-dismiss="modal" style="width:70px" onclick = "return removeval();">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript">
                function deleteTraining(trainingId){
                    var con=confirm("Do you want to delete this Training");
                    if(con){
                        window.location="deleteTrainingMaster.htm?trainingId="+trainingId;
                    }                  
                }
        </script>    
    </body>
</html>