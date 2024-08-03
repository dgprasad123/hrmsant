<%-- 
    Document   : PostEdit
    Created on : Feb 22, 2018, 4:02:36 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
                                    <i class="fa fa-file"></i> Post List
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Post Detail</h2>
                            <form:form commandName="post" action="savePost.htm"  method="post">
                                <div class="form-group">
                                    <label>Post Name</label>
                                    <form:hidden path="postcode"/>                                    
                                    <form:input path="post" cssClass="form-control" placeholder="Enter Post Name" autocomplete="off"/>                                    
                                </div>

                                <div class="form-group">
                                    <label>Department</label>
                                    <form:select path="deptcode" class="form-control">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${departmentList}" itemValue="deptCode" itemLabel="deptName"/>                                
                                    </form:select>                                     
                                </div>
                                
                                <div class="form-group">
                                    <label>Foreign Body</label>
                                    <form:select path="sltForeignBody" class="form-control">
                                        <form:option value="">Select</form:option>
                                        <form:option value="SGO">Government of Odisha</form:option>
                                    </form:select>                                     
                                </div>
                                <button type="submit" class="btn btn-default">Submit Button</button>
                                <button type="reset" class="btn btn-default">Reset Button</button>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
