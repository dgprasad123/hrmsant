<%-- 
    Document   : NotFitForPromotionRemarkList
    Created on : Mar 26, 2020, 12:15:32 PM
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
    <div id="page-wrapper">
            <form:form action="editremarkOfNotFitForPromotionReport.htm" method="post" commandName="LoginUserBean" class="form-horizontal" enctype="multipart/form-data">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body">                       

                            <div class="form-group">
                                <label class="control-label col-sm-2" >Remarks</label>
                                <div class="col-sm-8"> 
                                    <form:hidden path="reviewedempId"/>
                                    <form:hidden path="reviewedspc"/>
                                    <form:textarea class="form-control" path="reportingRemarks"/>             
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2">Document</label>
                                <div class="col-sm-2"> 
                                    <input type="file" name="uploadDocument" id="uploadDocument"  class="form-control-file"/>
                                </div>
                            </div> 
                        </div>
                        <div class="panel-footer">
                            <input type="submit" name="action" value="Save" class="btn btn-default" />
                        </div>

                    </div>
                </div>
            </form:form>
        </div>
</html>
