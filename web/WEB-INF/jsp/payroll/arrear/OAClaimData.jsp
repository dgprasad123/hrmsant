<%-- 
    Document   : OAClaimData
    Created on : Oct 30, 2017, 11:42:46 PM
    Author     : Manas

--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> :: HRMS :: </title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            
            $(window).load(function () {
                
            })
            
            function onlyIntegerRange(e){
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }
            
            function UpdEmpOaData() {
                
                if ($("#fromMonth").val() == '') {
                    alert("Please Select From Month ");
                    $("#fromMonth").focus()
                    return false;
                } 
                if ($("#fromYear").val() == '') {
                    alert("Please Select From Year ");
                    $("#fromYear").focus()
                    return false;
                }
                if ($("#toMonth").val() == '') {
                    alert("Please Select To Month ");
                    $("#toMonth").focus()
                    return false;
                }
                if ($("#toYear").val() == '') {
                    alert("Please Select To Year ");
                    $("#toYear").focus()
                    return false;
                }
                if ($("#objectHead").val() == '') {
                    alert("Please Select Object Head Description ");
                    $("#objectHead").focus()
                    return false;
                }
                if ($("#txtOaAmount").val() == '') {
                    alert("Please Enter Amount ");
                    $("#txtOaAmount").focus()
                    return false;
                }
            }
            
        </script>
        
    </head>
    <body>
        <form:form id="fm" action="OaClaimReport.htm" method="post" name="myForm" commandName="OAClaimModel">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" style="background-color: #0E8DE8;border:1px solid #0E8DE8;color: #ffffff;font-size: 15px;"> 
                    UPDATE OA CLAIM 
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-3">
                            <label> FROM : <form:hidden path="hidType" id="hidType"/> </label>      
                        </div>
                        <div class="col-lg-4">
                            <form:select path="fromMonth" id="fromMonth" class="form-control">
                                <option value="">-- Select Month --</option>
                                <form:options items="${monthList}" itemValue="value" itemLabel="label"/>
                            </form:select>
                        </div>
                        <div class="col-lg-4">
                            <form:select path="fromYear" id="fromYear" class="form-control">
                                <option value="">-- Select Year --</option>
                                <form:options items="${yearList}" itemValue="value" itemLabel="label"/>
                            </form:select>
                        </div>
                        <div class="col-lg-1"> &nbsp; </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-3">
                            <label> TO : <form:hidden path="hidHrmsId" id="hidHrmsId"/></label>
                        </div>
                        <div class="col-lg-4">
                            <form:select path="toMonth" id="toMonth" class="form-control">
                                <option value="">-- Select Month --</option>
                                <form:options items="${monthList}" itemValue="value" itemLabel="label"/>
                            </form:select>
                        </div>
                        <div class="col-lg-4">
                            <form:select path="toYear" id="toYear" class="form-control">
                                <option value="">-- Select Year --</option>
                                <form:options items="${yearList}" itemValue="value" itemLabel="label"/>
                            </form:select>
                        </div>  
                        <div class="col-lg-1">  </div>       
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-3"> <label for="note"> Object Head Description :</label> </div>
                        <div class="col-lg-8"> 
                            <form:select path="objectHead" id="objectHead" class="form-control">
                                <option value="">-- Select Object Head --</option>
                                <form:options items="${objHeadList}" itemValue="value" itemLabel="label"/>
                            </form:select> </div>
                        <div class="col-lg-1"> &nbsp; </div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-3"> <label for="note"> Amount : <form:hidden path="billGroupName" id="billGroupName"/></label> </div>
                        <div class="col-lg-8"> <form:input class="form-control" path="txtOaAmount" id="txtOaAmount" maxlength="4" onkeypress="return onlyIntegerRange(event)"/> </div>
                        <div class="col-lg-1"> &nbsp; </div>
                    </div>
                </div> 
                <div class="pull-left">
                    <!-- <input type="submit" name="Update" value="Update" class="btn btn-primary" onclick=" return UpdEmpOaData()"/> -->
                    <input type="submit" name="action" value="Update" class="btn btn-primary" onclick=" return UpdEmpOaData()"/>
                </div>        
            </div>
        </div>
                        
        </form:form>                
    </body>
</html>
