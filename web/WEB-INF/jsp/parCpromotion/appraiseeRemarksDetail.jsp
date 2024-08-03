<%-- 
    Document   : appraiseeRemarksDetail
    Created on : Jun 16, 2020, 11:00:54 AM
    Author     : manisha
--%>

<%-- 
    Document   : ReportingForwardedreportOfGroupCemp
    Created on : Jun 3, 2020, 5:24:33 PM
    Author     : manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        
    </head>
    <form:form action="ReportingForwardedreportOfGroupCemp.htm" method="POST" commandName="groupCEmployee" class="form-inline">
        <div class="row">
            <div class="col-lg-12">
                <h2>Remarks For Apprisee</h2>
                <div class="table-responsive">
                     <h1>The Performance is Adversed By  Reporting Authority   </h1>
                     Remarks: 
                     Attachment:
                </div>
                <div class="panel-footer">

                    <input type="submit" name="action" class="btn btn-default" value="Forward"/>
                </div>
            </div>
        </div>
    </form:form>
</html>
