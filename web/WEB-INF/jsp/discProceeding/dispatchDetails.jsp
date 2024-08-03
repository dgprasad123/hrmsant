<%-- 
    Document   : dispatchDetails
    Created on : Aug 20, 2018, 1:24:11 PM
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
            <form:form action="savedispatchdetails.htm" commandName="DispatchDetailsBean" method="post" class="form-horizontal" enctype="multipart/form-data">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body">                       
                            <div class="form-group">
                                <label class="control-label col-sm-2" >Post Office Name</label>
                                <div class="col-sm-8">                                     
                                    <form:hidden path="ddid"/>
                                    <form:hidden path="comType"/>
                                    <form:hidden path="comTypeReference"/>
                                    <form:textarea class="form-control" path="postoffcName"/>                                    
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" >Ems No.</label>
                                <div class="col-sm-8"> 
                                    <form:textarea class="form-control" path="emsNo"/>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Ems Copy Attach</label>
                                <div class="col-sm-2"> 
                                    <input type="file" name="emsCopyAttach"  class="form-control-file"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2"> Attach Copy of track Receipt</label>
                                <div class="col-sm-2"> 
                                    <input type="file" name="emsTrackReportFilename"  class="form-control-file"/>
                                </div>
                            </div>
                        </div>


                        <div class="panel-footer">
                            <form:hidden path="daId"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default"/> 
                            <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>