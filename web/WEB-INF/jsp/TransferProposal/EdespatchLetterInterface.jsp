<%-- 
    Document   : EdespatchLetterInterface
    Created on : Jul 20, 2021, 1:13:09 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/common.js" type="text/javascript"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script language="javascript" type="text/javascript" >
            function getAddresseeList(me) {
                $.ajax({
                    url: 'getAddresseeList.htm',
                    type: 'get',
                    data: 'addresseeGroupCode=' + $(me).val(),
                    success: function (data) {
                        $('#addresseelistdiv').empty();
                        $.each(data, function (i, obj) {
                            $('#addresseelistdiv').append('<li class="list-group-item" rel="'+obj.addressee_name+'">' + obj.addressee_name + ' <a href="#" onclick="addaddressee(this,\'' + obj.addressee_code + '\')")><span class="glyphicon glyphicon-plus"></span> Add </a></li>');
                            
                        });
                    }
                });
            }
            function removeaddressee(me,addressee_code){
                $(me).parent().remove();
            }
            function addaddressee(me,addressee_code){
                $(me).parent().remove();                
                $('#addresseelistassigneediv').append('<li class="list-group-item" rel="'+$(me).parent().attr('rel')+'">'+$(me).parent().attr('rel')+' <a href="#" onclick="removeaddressee(this,\'' + addressee_code + '\')")><span class="glyphicon glyphicon-remove"></span> Remove </a></li>');
            }
            function memoText(){
                $('#addresseelistassigneediv li').each(function(i){
                    memeotext = $(this).attr('rel'));
                })
            }
        </script>
        
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <form:form action="sendLettereDespatch.htm" class="form-horizontal" commandName="eDespatchBean" method="post">
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
                            <div class="col-lg-3">Date<span>*</span></div>
                            <div class="col-lg-3"><form:input path="letterdate" class="form-control" readonly="true"/> </div>
                            <div class="col-lg-3">Letter Type<span>*</span></div>
                            <div class="col-lg-3">
                                
                                <form:hidden path="proposalId"/>
                                <form:select path="lettertype" class="form-control">
                                    <form:option value="13">Notification</form:option>
                                    <form:option value="14">Order</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-3">Section<span>*</span></div>
                            <div class="col-lg-3">
                                <form:select path="section" class="form-control">
                                    <form:option value="">Select</form:option>
                                    <form:options items="${sectionList}" itemLabel="section_name" itemValue="section_code"/>                                    
                                </form:select>                                
                            </div>
                            <div class="col-lg-3">File No<span>*</span></div>
                            <div class="col-lg-3"><form:input path="fileno" class="form-control"/></div>
                        </div>
                        <div class="row">
                            <div class="col-lg-3">Signing Authority<span>*</span></div>
                            <div class="col-lg-3">
                                <form:select path="sentfrom" class="form-control">
                                    <form:option value="">Select</form:option>
                                    <form:options items="${signingAuthorityList}" itemLabel="signing_authorityName" itemValue="Signing_authorityCode"/>
                                </form:select>      
                            </div>
                            <div class="col-lg-3">Letter No<span>*</span></div>
                            <div class="col-lg-3"><form:input path="letterno" class="form-control" readonly="true"/></div>
                        </div>
                        <hr/>
                        <div class="panel">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-lg-3">
                                        Group
                                    </div>
                                    <div class="col-lg-3">
                                        <select name="group" class="form-control" onchange="getAddresseeList(this)">
                                            <c:forEach items="${groupList}" var="egroup">
                                                <option value="${egroup.addresseeGroup_code}">${egroup.addresseeGroup_name}</option>
                                            </c:forEach>                                            
                                        </select>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-5" style="height: 250px;overflow: auto;">
                                        <ul id="addresseelistdiv" class="list-group">
                                           
                                        </ul>
                                    </div>                                    
                                    <div class="col-lg-6">
                                        <ul id="addresseelistassigneediv" class="list-group">

                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <div class="row">
                            <div class="col-lg-3">
                                <input type="submit" value="Submit" name="action"/>
                                
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
