<%-- 
    Document   : AuthorityInfo
    Created on : Sep 16, 2021, 5:21:51 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link rel="stylesheet" type="text/css" href="css/jquery.classyedit.css"/>
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/common.js" type="text/javascript"></script>


        <script type="text/javascript" src="js/jquery.classyedit.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('textarea').bind('cut copy paste', function (e) {
                    e.preventDefault(); //disable cut,copy,paste
                });
                $("#letterheader").ClassyEdit();
                $("#letterfooter").ClassyEdit();
               
            });
        </script>
    </head>
    <body>
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
                                <li>
                                    <i class="fa fa-file"></i> Transfer / Deputation Proposal 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="TransferProposal.htm">New Transfer / Deputation Proposal</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="PromotionProposal.htm">New Promotion Proposal</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-3"></div>
                        <div class="col-lg-6">
                            <form:form class="form-horizontal" action="saveOtherInputs.htm" commandName="TransferProposalForm">
                                <div class="form-group">
                                    <label class="control-label col-sm-4" for="email">Authority Name:</label>
                                    <div class="col-sm-8">
                                        <form:hidden path="proposalId"/>
                                        <form:input path="authorityName" class="form-control" maxlength="100"/>                                        
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-4" for="pwd">Authority Designation:</label>
                                    <div class="col-sm-8">
                                        <form:input path="authorityDesg" class="form-control" maxlength="100"/>                                        
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-4" for="pwd">Letter Header:</label>
                                    <div class="col-sm-8">
                                        <form:textarea path="letterheader" class="form-control"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-4" for="pwd">Letter Footer:</label>
                                    <div class="col-sm-8">
                                        <form:textarea path="letterfooter" class="form-control"/>                                        
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-4 col-sm-8">
                                        <button type="submit" class="btn btn-default">Save</button>
                                    </div>
                                </div>
                            </form:form> 
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
