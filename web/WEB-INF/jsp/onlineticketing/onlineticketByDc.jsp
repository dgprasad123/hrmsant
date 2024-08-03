<%-- 
    Document   : ModuleList
    Created on : Nov 21, 2016, 6:08:30 PM
    Author     : Manas Jena
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script type="text/javascript">
                function validate(){
                   var status=$("#status").val();
                   if(status==""){
                       alert("Please select Ticket Status!!");
                       return false;
                   }
                    var message=$("#message").val();
                   if(message==""){
                       alert("Reply box cannot be Blank!!");
                       return false;
                   }
                  
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
                                    <i class="fa fa-file"></i> Online Ticket System 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    
                    <form:form action="SaveonlineticketByDc.htm" commandName="onlineticketing" enctype="multipart/form-data" onsubmit="return validate();">
                        <input type='hidden' name='deptCode' value='${deptCode}'/>
                        <input type='hidden' name='distCode' value='${distCode}'/>
                        <input type='hidden' name='username' value='${onlineticketing.username}'/>
                      
                            <div class="panel panel-primary">
                                <div class="panel-heading">Online Ticket</div>
                                <div class="row">
                                    <div class="col-md-4 col-md-push-4">

                                        <div class="form-group" style='margin-top:10px'>
                                            <label for="form_name">Ticket By :</label><br>
                                            <c:out value="${onlineticketing.username}" />
                                            <html:hidden path="userId"/>
                                            <!-- <form:input path="userId" class="form-control" id="userId" placeholder="Enter Ticket No"/>-->
                                        </div>
                                        <div class="form-group">
                                            <label for="form_name">Topic</label>
                                            <form:select path="topicId" class="form-control" id='topicid'>
                                                <form:option value="" label="Select"/>
                                                <c:forEach items="${topiclist}" var="topic">
                                                    <form:option value="${topic.topicId}" label="${topic.topic}"/>
                                                </c:forEach>                                 
                                            </form:select> 
                                        </div>
                                        <div class="form-group">
                                            <label for="form_name">Forward</label>
                                            <form:select path="forward" class="form-control" id='forward'>
                                                <form:option value="" label="Select"/>                                               
                                                    <form:option value="Self Closed"/>
                                                    <form:option value="Forward to State Team"/>                                                                               
                                            </form:select> 
                                            
                                          
                                        </div>
                                        <div class="form-group">
                                            <label for="form_name">Message</label>
                                            <form:textarea path="message" class="form-control" id="message" rows="3"/>
                                        </div>
                                        <div class="form-group">
                                            <table>
                                                <tr>
                                                    <td><label for="exampleInputFile">File Upload</label></td>

                                                    <td>
                                                        <input type="file" name="file" />
                                                        <!-- <input type="file" class="form-control-file" name="file" id="file" aria-describedby="fileHelp">-->
                                                        <small id="fileHelp" class="form-text text-muted"></small></td>
                                                    <td></td>
                                                <tr>
                                            </table>
                                        </div>
                                        <div class="col-md-8 col-md-push-4" style='margin-bottom:10px;'>
                                            <input type="submit" value="Save" name="save" class="btn btn-primary"/>
                                            <input type="button" value="Cancel" name="cancel" class="btn btn-primary" onclick="Cancel_page()"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                                
                    </form:form>

                </div>
            </div>
        </div>
        <script>
            function Cancel_page() {
                window.location = "onlineticketlistDC.htm";
            }
        </script>
    </body>
</html>
