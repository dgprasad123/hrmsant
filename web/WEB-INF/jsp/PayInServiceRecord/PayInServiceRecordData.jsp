<%-- 
    Document   : PayInServiceRecordData
    Created on : Apr 18, 2022, 12:25:58 PM
    Author     : Madhusmita
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#wefdate').datetimepicker({
                    format: 'DD-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function saveCheck() {
                if ($('#wefdate').val() == "") {
                    alert("Enter With Effect Date");
                    return false;
                }
                return true;
            }

        </script>
    </head>
    <body>
        <form:form action="savePisrData.htm" method="post" commandName="pisrForm">
            <form:hidden id="hidSrpId" path="hidSrpId"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div style=" margin-bottom: 5px;" class="panel panel-info">
                        <div class="panel-heading" style="text-align: center">
                            <h3>Pay In Service Record</h3>
                        </div>
                        <div class="panel-body">
                            <div class="row">

                                <div class="col-lg-12">
                                    <h4><b><u>1. Pay Details :</u></b></h4>
                                    <br>
                                    <div class="form-group">

                                        <label class="control-label col-sm-2" for="pisr">a) WEF Date :<span style="color: red;">*</span></label>
                                        <div class="col-sm-3">
                                            <div class="input-group date">
                                                <form:input class="form-control" path="wefdate" id="wefdate" readonly="true"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>                                
                                        </div>
                                    </div>
                                    <br><br>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="pisr">b) Pay :</label>
                                        <div class="col-sm-3">
                                            <form:input path="pay" class="form-control"/>
                                        </div>
                                    </div>
                                    <br><br>
                                    <div class="form-group">

                                        <label class="control-label col-sm-2" for="pisr">c) Scale Of Pay :</label>
                                        <div class="col-sm-5">
                                            <form:select path="sltPayScale" id="sltPayScale" class="form-control" >
                                                <form:option value="">--Select--</form:option>
                                                <form:options items="${payScaleList}" itemValue="payscale" itemLabel="payscale"/>
                                            </form:select>
                                        </div>

                                    </div>
                                    <br><br>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="pisr">d) Special Pay :</label>
                                        <div class="col-sm-3">
                                            <form:input path="specialPay" class="form-control"/>
                                        </div>

                                        <label class="control-label col-sm-2" for="pisr">e) Personal Pay :</label>
                                        <div class="col-sm-3">
                                            <form:input path="personalPay" class="form-control"/>
                                        </div>

                                    </div>
                                    <br><br>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="pisr">g) Other Pay Description :</label>
                                        <div class="col-sm-3">
                                            <form:input path="othPayDescr" class="form-control"/>
                                        </div>
                                        <label class="control-label col-sm-2" for="pisr">f) Other Emoluments Falling Under Pay(Rs.) :</label>
                                        <div class="col-sm-3">
                                            <form:input path="othEmoulment" class="form-control"/>
                                        </div>
                                    </div>
                                    <br><br>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="pisr">h) DA :</label>
                                        <div class="col-sm-3">
                                            <form:input path="da" class="form-control"/>
                                        </div>

                                        <label class="control-label col-sm-2" for="pisr">i) Additional DA :</label>
                                        <div class="col-sm-3">
                                            <form:input path="ada" class="form-control"/>
                                        </div>

                                    </div>
                                    <br><br>
                                    <div class="form-group">                                       
                                        <label class="control-label col-sm-2" for="pisr">j) Leave Salary :</label>
                                        <div class="col-sm-3">
                                            <form:input path="leaveSal" class="form-control"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <c:if test="${not empty pisrForm.hidSrpId  && pisrForm.hidSrpId ne ''}">
                                            <label class="control-label col-sm-2" for="pisr">k) Total Pay :</label>
                                            <div class="col-sm-3" >
                                                <form:input path="totPay" class="form-control" disabled="true" />
                                            </div>
                                        </c:if>
                                    </div>
                                    </br></br>
                                    <div class="row" style="margin-bottom: 7px;">
                                        <h4><b><u>2. Note(If Any) :</u></b></h4>
                                        <br> <br>
                                        <div class="col-lg-10">
                                            <form:textarea class="form-control" path="note" id="note"/>
                                        </div>                                        
                                    </div>
                                    <br>

                                </div>
                            </div>
                        </div>
                    </div></div>
                <script>
                    <%--alert('${pisrForm.hidSrpId}');--%>
                </script>

            </div>
            <div class="panel-footer">
                <c:if test="${empty pisrForm.hidSrpId || pisrForm.hidSrpId eq ''}">
                    <input type="submit" name="Save" value="Save" class="btn btn-success" onclick="return saveCheck();" />
                </c:if>
                <c:if test="${not empty pisrForm.hidSrpId  && pisrForm.hidSrpId ne ''}">
                    <input type="submit" name="Update" value="Update" class="btn btn-success"/>
                </c:if>
                <input type="submit" name="Cancel" value="Cancel" class="btn btn-primary"/>
            </div>
        </form:form>
    </body>
</html>
