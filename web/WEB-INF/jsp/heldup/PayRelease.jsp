<%-- 
    Document   : HeldUp
    Created on : Apr 7, 2018, 5:08:51 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
    </head>
    <body>

        <div class="col-md-8 col-sm-8" style="margin-top: 10px;">
            <div class="panel panel-default">
                <div class="panel-body">
                    <form:form class="form-horizontal" action="releasepay.htm" commandName="RegularHeldUpBean" method="post">
                        <form:hidden path="empIdSPC"/>
                        <form:hidden path="offcode"/>
                        <form:hidden path="billgroupid"/>
                        <form:hidden path="hrmsId"/>
                        <form:hidden path="heldupId"/>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Employee Name:</label>
                            <span class="col-sm-10">${RegularHeldUpBean.empname}</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Employee Id:</label>
                            <span class="col-sm-10">${RegularHeldUpBean.hrmsId}</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">Post:</label>
                            <span class="col-sm-10">${RegularHeldUpBean.post}</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="padding-top: 0px;">GPF/PRAN No:</label>
                            <span class="col-sm-10">${RegularHeldUpBean.gpfNo}</span>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2"  style="padding-top: 0px;">Held Up Date:</label>
                            <div class="col-sm-2 input-group date" id="heldupdatediv">
                                <input type="text" name="heldupdate" class="form-control" > 
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>
                        </div>
                        <input type="submit" name="action" value="Release" class="btn btn-primary"/>
                        <input type="submit" name="action" value="Back" class="btn btn-primary"/>
                    </form:form>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(function() {
                $('#heldupdatediv').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
            });
        </script>
    </body>
</html>
