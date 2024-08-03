<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            
            function deleteAdInfo() {
                if (confirm("Are you sure you want to delete the Formula/Fixed Value?")) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-horizontal" action="saveAllowanceAndDeduction.htm" method="POST" commandName="command">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="addesc">Description:</label>
                                    <div class="col-sm-9">
                                        <form:hidden path="adcode"/>
                                        <form:hidden path="adtype"/>
                                        <form:hidden path="whereupdated"/>
                                        <form:hidden path="updationRefCode"/>
                                        <form:input path="addesc" id="addesc" class="form-control"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="advalue">Fixed or Formula:</label>
                                    <div class="col-sm-9">
                                        <form:select path="adamttype" id="adamttype" class="form-control">
                                            <form:option value="">--Select One--</form:option>
                                            <form:option value="1">FIXED</form:option>
                                            <form:option value="0">FORMULA</form:option>
                                        </form:select>                                        
                                    </div>
                                </div>                                    
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="advalue">Amount:</label>
                                    <div class="col-sm-9">                                        
                                        <form:input path="advalue" id="advalue" class="form-control"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="formula">Formula:</label>
                                    <div class="col-sm-9">                                        
                                        <form:select path="formula" id="formula" class="form-control">
                                            <form:option value="">--Select One--</form:option>
                                            <form:options items="${formulaList}" itemLabel="formulaName" itemValue="formulaName"/>
                                        </form:select>
                                    </div>
                                </div>   
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="advalue">BT ID/Object Head:</label>
                                    <div class="col-sm-9">
                                        <c:if test="${command.adcode eq '69' || command.adcode eq '278'}">
                                            <form:input path="head" id="head" class="form-control"/>
                                        </c:if>
                                        <c:if test="${command.adcode ne '69' && command.adcode ne '278'}">
                                            <form:input path="head" id="head" class="form-control" readonly="true"/>
                                        </c:if>
                                    </div>
                                </div>                                    
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" class="btn btn-default" name="action" value="Save"/>
                        <c:if test="${command.isupdated > 0}">
                            <input type="submit" class="btn btn-default" style="background:#890000;color:#FFFFFF;" name="action" value="Delete" onclick="javascript: return deleteAdInfo()" />
                        </c:if>
                        <input type="submit" class="btn btn-default" name="action" value="Cancel"/>                        
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>
