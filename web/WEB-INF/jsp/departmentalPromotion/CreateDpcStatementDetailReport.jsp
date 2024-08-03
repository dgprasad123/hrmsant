<%-- 
    Document   : createNrcStatementDetailReport
    Created on : Feb 19, 2020, 12:54:48 PM
    Author     : manisha
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>

        <script type="text/javascript">            
            function validation() {
                
                var FiscalyearFrom = document.getElementById("fiscalYearFrom").selectedIndex;
                var FiscalyearTo = document.getElementById("fiscalYearTo").selectedIndex;
               
                if (FiscalyearFrom < FiscalyearTo) {
                    alert("FiscalyearFrom is greater then FiscalyearTo.please Choose FiscalyearFrom Less then FiscalyearTo");
                    return false;
                }
                 if ($("#cadrecode").val() == "") {
                    alert("please Enter cadre");
                    $("#cadrecode").focus();
                    return false;
                }
            }
            
             

        </script>
    </head>
    <body style="background-color:#FFFFFF">

        <form:form action="createdpcStatementDetailReport.htm" method="POST" commandName="departmentPromotionBean" class="form-inline">  
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-body">   


                        <div class="row">
                            <label class="control-label col-sm-2 ">Fiscal Year From:</label>
                            <div class="col-sm-4">
                                 <form:hidden path="dpcId"/>
                                <form:select path="fiscalYearFrom" class="form-control">
                                    <form:option value="" >Year</form:option>
                                    <form:options items="${fiscyear}" itemLabel="fy" itemValue="fy"/>
                                </form:select> 
                               
                            </div>

                            <label class="control-label col-sm-1">Fiscal Year To:</label>
                            <div class="col-sm-5">
                                <form:select path="fiscalYearTo" class="form-control">
                                    <form:option value="">Year</form:option>
                                    <form:options items="${fiscyear}" itemLabel="fy" itemValue="fy"/>
                                </form:select>  
                            </div>
                        </div><br>
                        <div class="row">
                            <label class="control-label col-sm-2">Cadre List:</label>
                            <div class="col-sm-4">
                                <select name="cadrecode" id="cadrecode" class="form-control">
                                    <option value="">ALL</option>
                                    <c:forEach items="${cadreList}" var="cadre">
                                        <option value="${cadre.cadrecode}">${cadre.cadrename}-${cadre.postGroupType}</option>
                                    </c:forEach>
                                </select>
                               
                            </div>
                            <label class="control-label col-sm-1">DPC Name:</label>
                            <div class="col-sm-5">
                                <form:input path="dpcName" class="form-control" />
                                   
                            </div>
                        </div>
                        <%--<div class="row">
                            <label class="control-label col-sm-2">Post Group:</label>
                            <div class="col-sm-4">
                                <form:select path="postGroupType">
                                    <form:option value="A">A</form:option>
                                    <form:option value="B">B</form:option>
                                </form:select>
                            </div>
                        </div> --%>

                    </div>
                    <div class="panel-footer">
                        <input type="submit" class="btn btn-primary" name="action" value="Save"  onclick="return validation()"/>
                         <input type="submit" class="btn btn-primary" name="action" value="Back" />
                    </div>
                </div>
            </div>
        </form:form>   

    </body>
</html>


