<%-- 
    Document   : EditOfficeDetails
    Created on : Oct 21, 2021, 12:35:09 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script type="text/javascript">
            function backUpAlert()
            {
                if (confirm("Are You sure to Create Back Up Office?"))
                {
                    setTimeout(function() {
                        $('#createbackup').html("<img src='./images/ajax-loader.gif' width='20' height='20'>");
                        return true;
                    }, 1500);
                } else {
                    return false;
                }
            }
            /*$(document).ready(function() {
             $(.button).on('click', function() {
             $(".loading-cl").removeClass("hide");
             $('.button').attr('disabled', true);
             $('.btnbackup').text("Creating.......");
             setTimeOut(function() {
             $(".loading-cl").addClass("hide");
             $('.button').attr('disabled', false);
             $('.btnbackup').text("Creating.......");
             
             })
             });
             
             });*/
            function checkMaxLength() {
                if ($('#offCode').val().length != 13) {
                    alert("Enter 13 digit Office Code");
                }
            }

        </script>      
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }
            @media (min-width: 800px) {
                .modal-dialog {
                    width: 600;
                    margin: 30px auto;
                }
                .modal-content {
                    -webkit-box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                    box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                }
                .modal-sm {
                    width: 300px;
                }
            }


            .buttonload {
                background-color: #04AA6D; /* Green background */
                border: none; /* Remove borders */
                color: white; /* White text */
                padding: 20px 20px; /* Some padding */
                font-size: 18px /* Set a font size */
            }
            @keyframes spin {
                100% {
                    transform: rotate(360deg);
                }
            }

            .center {
                position: absolute;
                top: 0;
                bottom: 0;
                left: 0;
                right: 0;
                margin: auto;
            }

        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">     

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
                        <div class="panel panel-heading" align="center" style="background-color: lightsteelblue;font-size:xx-small;">
                            <div style="text-align:center;border:2px;font-weight:bold;font-size: 20px">
                                OFFICE RENAME / BACKUP 
                            </div>
                        </div>
                        <div class="panel panel-body">
                            <div class="table table-bordered">
                                <form:form action="editOfficeDetails.htm" commandName="editOffice" method="post">
                                    <form:hidden id="hidOfficeCode" path="hidOfficeCode"/>
                                    <form:hidden id="hidDeptCode" path="hidDeptCode"/>
                                    <form:hidden id="hidDistCode" path="hidDistCode"/>
                                    <div class="row" style="align:center">
                                        <label class="control-label col-sm-3" style="font-size: 30px;text-align:center;margin-right:10px">Enter Office Code</label>
                                        <div class="col-sm-3" style="align:center">
                                            <form:input class="form-control" path="offCode" />                            
                                        </div>
                                        <input type="submit" name="search" value="Search" onclick="return checkMaxLength();" class="btn btn-primary" style="text-align:left;margin-left:100px"/> 
                                    </div>
                                    </br></br>

                                    <div class="row">
                                        <label class="control-label col-sm-3" style="font-size: 20px" >1.&emsp;&emsp;Office Name</label>
                                        <div class="col-sm-6">
                                            <form:input class="form-control" path="offEn"/>                            
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" style="font-size: 20px" >2.&emsp;&emsp;Category</label>
                                        <div class="col-sm-6">
                                            <form:input class="form-control" path="category"/>                            
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" style="font-size: 20px" >3.&emsp;&emsp;Suffix</label>
                                        <div class="col-sm-5">
                                            <form:input class="form-control" path="suffix"/>                            
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" style="font-size: 20px" >4.&emsp;&emsp;Online Bill submission</label>
                                        <div class="col-sm-5">
                                            <form:select id="onlineBillSubmission" path="onlineBillSubmission" class="form-control">
                                                <form:option value=" ">--Select--</form:option>
                                                <form:option value="Y">Yes</form:option> 
                                                <form:option value="N">No</form:option>                                             
                                            </form:select>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="control-label col-sm-3" style="font-size: 20px" >5.&emsp;&emsp;Is DDO</label>
                                    <div class="col-sm-5">
                                        <form:select id="isDdo" path="isDdo" class="form-control">
                                            <form:option value=" ">--Select--</form:option>
                                            <form:option value="Y">Yes</form:option> 
                                            <form:option value="N">No</form:option>                                             
                                        </form:select>
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="control-label col-sm-3" style="font-size: 20px" >6.&emsp;&emsp;Office Status</label>
                                    <div class="col-sm-5" style="size:100px">
                                        <form:select id="offStatus" path="offStatus" class="form-control">
                                            <form:option value=" ">--Select--</form:option>
                                            <form:option value="F">Functional</form:option> 
                                            <form:option value="NF">Non Functional</form:option>                                             
                                        </form:select>
                                    </div>
                                </div>
                                <%--<script>
                                    alert('${editOffice.hidMode}');
                                </script>--%>

                                <div class="panel-footer">
                                    <input type="submit" name="save" value="Save" class="btn btn-primary" style="align:center"/>
                                    <input type="submit" name="cancel" value="Cancel" class="btn btn-primary" style="align:center"/> 
                                    <c:if test="${editOffice.hidMode eq 'B'}">
                                        <a href="createOfcBackup"><button type="button" class="btn btn-primary" disabled="true">Create BackUp</button></a>
                                        <h4  style="width: 600px ; color: #008900; text-align: center">${bckupmsg}</h4>
                                        <h4  style="width: 600px ; color: #008900; text-align: center">${msgOfcUpdation}</h4>
                                    </c:if>
                                    <c:if test="${editOffice.hidMode ne 'B'}">
                                        <span id="createbackup">
                                            <a href="createOfcBackup.htm?hidOfficeCode=${editOffice.offCode}" onclick="return backUpAlert();"><button type="button" class="btn btn-primary"> Create BackUp</button></a>
                                        </span>
                                    </c:if>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
