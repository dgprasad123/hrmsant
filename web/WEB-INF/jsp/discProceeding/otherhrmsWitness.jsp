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
        <script type="text/javascript">

            function validation() {
                if ($("#mobile").val() == "") {
                    alert("please Enter the Mobile Number");
                    $("#mobile").focus();
                    return false;
                }
            }
            function validation() {
                if ($("#dist").val() == "") {
                    alert("please Enter the district");
                    $("#dist").focus();
                    return false;
                }
            }
            function validation() {
                if ($("#addressat").val() == "") {
                    alert("please Enter the Address");
                    $("#addressat").focus();
                    return false;
                }
            }
        </script>

    </head>
    <body>
        <form:form action="saveotherhrmswitness.htm" commandName="witnessbean" method="post">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-body">

                        <div class="form-group">
                            <label class="control-label col-sm-2" >Name and Designation Of Witness</label>
                            <div class="col-sm-8" > 
                                <form:input class="form-control" path="witnessName" maxlength="50" />
                            </div>
                        </div><br><br>
                        <div class="form-group">
                            <label class="control-label col-sm-2" >Village</label>
                            <div class="col-sm-8" > 
                                <form:hidden path="dacid"/>
                                <form:hidden path="daId"/>
                                <form:input class="form-control" path="addressat" maxlength="20" />                                    
                            </div>
                        </div><br><br>

                        <div class="form-group">
                            <label class="control-label col-sm-2" >Post</label>
                            <div class="col-sm-8" > 
                                <form:input class="form-control" path="addresspo" maxlength="50" />
                            </div>
                        </div><br><br>

                        <div class="form-group">
                            <label class="control-label col-sm-2" >Police Station</label>
                            <div class="col-sm-8" > 
                                <form:input class="form-control" path="addressps" maxlength="50" />
                            </div>
                        </div><br><br>
                        <div class="form-group">
                            <label class="control-label col-sm-2" >District</label>
                            <div class="col-sm-8" > 
                                <form:input class="form-control" path="dist" maxlength="20" />
                            </div>
                        </div><br><br>

                        <div class="form-group">
                            <label class="control-label col-sm-2" >Pin Code</label>
                            <div class="col-sm-8" > 
                                <form:input class="form-control" path="pincode" maxlength="20"/>
                            </div>
                        </div><br><br>
                        <div class="form-group">
                            <label class="control-label col-sm-2" >Mobile No</label>
                            <div class="col-sm-8" > 
                                <form:input class="form-control" path="mobile" maxlength="10"/>
                            </div>
                        </div><br><br>
                        <div class="form-group">
                            <label class="control-label col-sm-2" >Email Id</label>
                            <div class="col-sm-8" > 
                                <form:input class="form-control" path="email" maxlength="30" />
                            </div>
                        </div><br><br>
                    </div>


                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validation()"/>  
                        <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>