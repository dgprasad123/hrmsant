<%-- 
    Document   : ReprocessBill
    Created on : Oct 28, 2017, 9:40:13 AM
    Author     : Manas
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#btn-process-child').click(function () {
                    $('#btn-process-child').hide();
                });
            });
        </script>
    </head>
    <body>
        <form:form class="form-inline" action="prepareNewBillform.htm" method="POST" commandName="command" >

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="modal-title">Reprocess Bill</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <form:hidden path="billgroupId"/>
                        <form:hidden path="sltYear"/>
                        <form:hidden path="sltMonth"/>
                        <form:hidden path="offCode"/>
                        <form:hidden path="billNo"/>
                        <form:hidden path="txtbilltype"/> 
                        <label for="processDate">Process Date:</label>
                        <div class='input-group date' id='processDate'>
                            <form:input class="form-control" id="processDate" path="processDate" />
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-time"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <input type="submit" class="btn btn-default" name="action" value="Process" id="btn-process-child"/>
                </div>
            </form:form>
    </body>
    <script type="text/javascript">
        $(function () {
            $('#processDate').datetimepicker({
                format: 'D-MMM-YYYY'
            });
        });
    </script>
</html>
