<%-- 
    Document   : AddNewBoard
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
            function saveCheck(){                
                
                if ($('#facultyType').val() == "")
                {
                    alert("Please enter degree Name");
                    $('#facultyType').focus();
                    return false;
                }else if($('#facultyType').val() !=""){
                  alert("Are you sure want to save ?");   
                }
                
                
            }
            
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
                        <form:form action="SubjectIdentityList.htm" commandName="subjectIdentityBean" method="post">
                            
                             <form:hidden path="subjectsl"/>  
                            <div class="row row-margin">
                                <div class="col-lg-2">
                                    <label for="note">Subject Name: <span style="color: red">*</span></label></label>
                                </div>
                                <div class="col-sm-6">
                                    <form:input cssClass="form-control" path="subjectname"  placeholder="Enter Subject Name" />
                                </div>
                            </div>  
                            
                           
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                 <div class="text-center col-sm-12" > 
                                  <c:if test="${subjectIdentityBean.getSubjectsl() > 0}">
                                <input type="submit" name="action" value="Update" class="btn btn-info" onclick="return saveCheck();">
                            </c:if>
                            <c:if test="${subjectIdentityBean.getSubjectsl() <= 0}">
                                <input type="submit" name="action" value="Save" class="btn btn-info" onclick="return saveCheck();">
                            </c:if>
                                
                                    
                                    <a href="SubjectIdentityList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>
                                </div>
                               
                                
                                   
                            </div>   
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>


