<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">

            $(document).ready(function() {
                $('#empId').chosen();
                $("#empId").trigger("chosen:updated");
                $("#empdataloader").hide();
            });

            function validateServiceBookEntry(count, empid, id) {
                var url = "ValidateServiceBookEntry.htm?empId=" + empid + "&id=" + id;
                $.getJSON(url, function(data) {
                    if (data.status == "1") {
                        alert("Validated Successfully");
                        $("#sb_" + count).text("Validated");
                        $("#sb_" + count).css("font-weight", "Bold");
                    }
                });
            }

            function validateNoNotificationServiceBookEntry(count, empid, id, tabname) {
                var url = "ValidateNoNotificationServiceBookEntry.htm?empId=" + empid + "&id=" + id + "&tabname=" + tabname;
                $.getJSON(url, function(data) {
                    if (data.status == "1") {
                        alert("Validated Successfully");
                        $("#sb_" + count).text("Validated");
                        $("#sb_" + count).css("font-weight", "Bold");
                    }
                });
            }
            
            function validateSubmit(){
                if($("#empId").val() == ""){
                    alert("Please select Employee");
                    return false;
                }else{
                    $("#btnSubmit").hide();
                    $("#empdataloader").show();
                }
              return true;  
            }
        </script>
    </head>
    <body>
        <form:form action="ServiceBookValidator.htm" commandName="empService">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h4>Service Book Validator</h4>
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-top:50px;">
                        <div class="col-lg-2" style="text-align:center;font-weight: bold;">
                            Employee List
                        </div>
                        <div class="col-lg-6">
                            <form:select path="empId" id="empId" class="form-control">
                                <form:option value="">--Select Employee--</form:option>
                                <form:options items="${emplist}" itemValue="value" itemLabel="label"/>
                            </form:select>
                        </div>
                        <div class="col-lg-4">
                            <input type="submit" name="btnSubmit" value="Get Data" id="btnSubmit" class="btn btn-danger" onclick="return validateSubmit();"/>&nbsp;
                            <span id="empdataloader">
                                <img src='images/ajax-loader.gif' width='25' height='25'/>
                            </span>
                        </div>
                    </div>
                    <div class="panel-body" style="margin-bottom:50px;">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="table-responsive">
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr class="alternateTD">
                                                <th style="text-align:center;font-size:16px;width: 10%;" class="printLabelHeader">Sl No</th>
                                                <th style="text-align:center;font-size:16px;width: 15%;" class="printLabelHeader">Order Date</th>
                                                <th style="text-align:center;font-size:16px;width: 15%;" class="printLabelHeader">WEF</th>
                                                <th style="text-align:center;font-size:16px;width: 50%;" class="printLabelHeader">Transaction</th>
                                                <th style="text-align:center;font-size:16px;width: 10%;" class="printLabelHeader">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody id="empdatabody">
                                            <c:forEach items="${esb.empsbrecord}" var="servicehistory" varStatus="cnt">
                                                <tr height="50px">
                                                    <td>
                                                        ${cnt.index + 1}
                                                    </td>
                                                    <td>
                                                        ${servicehistory.ordDate}
                                                    </td>
                                                    <td>                                    
                                                        ${servicehistory.wefChange}
                                                    </td>
                                                    <td>
                                                        <b><u>${servicehistory.category}</u></b><br />
                                                        ${servicehistory.sbdescription}<br/>
                                                        <c:if test="${not empty servicehistory.moduleNote}">
                                                            Note - <c:out value="${servicehistory.moduleNote}"/>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span id="sb_${cnt.index + 1}">
                                                            <c:if test="${servicehistory.isValidated eq 'N'}">
                                                                <c:if test="${servicehistory.tabname eq 'JOINING' || servicehistory.tabname eq 'TRAINING' || servicehistory.tabname eq 'SERVICE VERIFICATION CERTIFICATE' || servicehistory.tabname eq 'RELIEVE' || servicehistory.tabname eq 'EXAMINATION' || servicehistory.tabname eq 'REINSTATEMENT' || servicehistory.tabname eq 'SUSPENSION' || servicehistory.tabname eq 'PERMISSION' || servicehistory.tabname eq 'EDUCATION' || servicehistory.tabname eq 'RETIREMENT' || servicehistory.tabname eq 'MISC'}">
                                                                    <a href="javascript:validateNoNotificationServiceBookEntry('${cnt.index + 1}','${empid}','${servicehistory.noteId}','${servicehistory.tabname}');" onclick="return confirm('Are You sure to Validate?');">Validate</a>
                                                                </c:if>
                                                                <c:if test="${servicehistory.tabname ne 'JOINING' && servicehistory.tabname ne 'TRAINING' && servicehistory.tabname ne 'SERVICE VERIFICATION CERTIFICATE' && servicehistory.tabname ne 'RELIEVE' && servicehistory.tabname ne 'EXAMINATION' && servicehistory.tabname ne 'REINSTATEMENT' && servicehistory.tabname ne 'SUSPENSION' && servicehistory.tabname ne 'PERMISSION' && servicehistory.tabname ne 'EDUCATION' && servicehistory.tabname ne 'RETIREMENT' && servicehistory.tabname ne 'MISC'}">
                                                                    <a href="javascript:validateServiceBookEntry('${cnt.index + 1}','${empid}','${servicehistory.noteId}');" onclick="return confirm('Are You sure to Validate?');">Validate</a>
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${servicehistory.isValidated eq 'Y'}">
                                                                <strong>Validated</strong>
                                                            </c:if>
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">

                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
