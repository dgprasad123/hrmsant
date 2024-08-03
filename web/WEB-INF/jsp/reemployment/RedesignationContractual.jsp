<%-- 
    Document   : addNewEnrollmentToInsurance
    Created on : 13 Dec, 2021, 3:36:26 PM
    Author     : Devi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
            function saveCheck() {
                if ($('#newContpost').val() == "") {
                    alert("Please enter New Post");
                    $('#newContpost').focus();
                    return false;
                }else if( !confirm('Are you sure that you want to submit the form') ) 
            event.preventDefault();
                
    
                
            }
            
            
           
               

        </script>
        <style>
            .row-margin{
                margin-bottom: 20px;
            }
        </style>

    </head>
    <body>
        <form:form action="saveEmployeeCurrentpostContractual.htm" commandName="redesgContform" method="post" id="myform">
            <form:hidden path="empid" />
            <form:hidden path="ename" />
            <form:hidden path="contpost" />
             <form:hidden path="depcode" />

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Change Designation
                    </div>
                    <div class="panel-body">

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txtNotOrdNo">HRMS ID<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                ${redesgContform.empid}
                            </div>
                            <div class="col-lg-3">
                                <label for="orddate"> Name<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <div class="input-group date" id="processDate">
                                    ${redesgContform.ename} 

                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="curBasic">Current Post<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3"> 
                                ${redesgContform.contpost}
                            </div>
                            <div class="col-lg-3">
                                <label for="newBasic"> New Post <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="newContpost" id="newContpost" />                              
                            </div>
                        </div>

                    </div>
                    <div class="panel-footer" style="text-align:center;">
                        <input type="submit" name="btnTransferCntr" value="Save" class="btn btn-success" id="btnSave" onclick="return saveCheck();"/>
                   <c:if test="${not empty msg}">
                            <span style="font-weight: bold; color: #00ee00;">
                                Successfully Updated.
                            </span>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>
    </body>

</html>
