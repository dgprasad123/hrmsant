<%-- 
    Document   : enquiry Report
    Created on : Jul 24, 2018, 4:11:27 PM
    Author     : manisha
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
    </head>
    <body>
        <div id="page-wrapper">
            <form:form action="saveenquiry.htm" commandName="enquiryBean" method="post" class="form-horizontal" enctype="multipart/form-data">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body">                       
                            <div class="form-group">
                                <label class="control-label col-sm-2" >Enquiry Report Submission</label>
                                <div class="col-sm-8"> 
                                    
                                    <form:textarea class="form-control" path="enquiryRemark"/>                                    
                                </div>
                            </div>
                           
                            <div class="form-group">
                                <label class="control-label col-sm-2">Browse</label>
                                <div class="col-sm-2"> 
                                    <input type="file" name="uploadDocument"  class="form-control-file"/>
                                </div>
                            </div>

                        </div>
                        <div class="panel-footer">
                            <input type="submit" name="action" value="Save" class="btn btn-default"/>
                            
                            <input type="submit" name="action" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?')"/>
                        </div>

                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>