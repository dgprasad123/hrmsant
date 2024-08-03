<%-- 
    Document   : otherhrmsWitnessList
    Created on : Aug 21, 2018, 3:03:48 PM
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
            <form:form action="saveotherhrmswitness.htm" commandName="DiscWitnessBean" method="post" >
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body">                       
                            <div class="form-group">
                                <label class="control-label col-sm-3" >At:</label>
                                <div class="col-sm-6" style="width:20%"> 
                                    <form:hidden path="dacid"/>
                                    <form:hidden path="daId"/>
                                    <form:input class="form-control" path="addressat" />
                                    
                                                               
                                </div>
                            </div><br>
                            <div class="form-group">
                                <label class="control-label col-sm-3" >Post</label>
                                <div class="col-sm-6" style="width:20%"> 
                                    <form:input class="form-control" path="addresspo" />
                                  

                                </div>
                            </div><br>
                            <div class="form-group">
                                <label class="control-label col-sm-3" >Police Station</label>
                                <div class="col-sm-6" style="width:20%"> 
                                     <form:input class="form-control" path="addressps" />
                                    

                                </div>
                            </div><br>
                            <div class="form-group">
                                <label class="control-label col-sm-3" >District</label>
                                <div class="col-sm-6" style="width:20%"> 
                                    <form:input class="form-control" path="dist" />
                                    

                                </div>
                            </div><br>
                            <div class="form-group">
                                <label class="control-label col-sm-3" >pin code:</label>
                                <div class="col-sm-6" style="width:20%"> 
                                    <form:input class="form-control" path="pincode" />
                                    

                                </div>
                            </div><br>
                            <div class="form-group">
                                <label class="control-label col-sm-3" >Mobile No</label>
                                <div class="col-sm-6" style="width:20%"> 
                                    <form:input class="form-control" path="mobile" />
                                   

                                </div>
                            </div><br>
                            <div class="form-group">
                                <label class="control-label col-sm-3" >Email id</label>
                                <div class="col-sm-6" style="width:20%"> 
                                    <form:input class="form-control" path="email" />
                                    

                                </div>
                            </div>
                        </div>


                        <div class="panel-footer">
                            <input type="submit" name="action" value="Save" class="btn btn-default"/>  
                             <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>