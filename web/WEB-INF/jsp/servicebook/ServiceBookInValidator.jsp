<%-- 
    Document   : ServiceBookInValidator
    Created on : Jun 23, 2021, 4:35:26 PM
    Author     : Madhusmita
--%>

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

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateServiceBookEntry(count, empid, id) {
                var url = "ValidateServiceBookEntry.htm?empId=" + empid + "&id=" + id;
                $.getJSON(url, function(data) {
                    if (data.status == "1") {
                        alert("Validated Successfully");
                        $("#sb_" + count).text("Validated");
            <%--  $("#sbiv_" + count).html("<a href=\"javascript:inValidateServiceBookEntry('" + count + "','" + empid + "','" + id + "');\" onclick=\"return confirm('Are You sure to InValidate?');\">Invalidate</a>");--%>
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
            <%-- $("#sbiv_" + count).html("<a href=\"javascript:inValidateNoNotificationServiceBookEntry('" + count + "','" + empid + "','" + id + "','" + tabname + "');\" onclick=\"return confirm('Are You sure to InValidate?');\">Invalidate</a>");--%>
                        $("#sb_" + count).css("font-weight", "Bold");
                    }
                });
            }
            function inValidateServiceBookEntry(count, empid, id) {
                var url = "InValidateServiceBookEntry.htm?empId=" + empid + "&id=" + id;
                $.getJSON(url, function(data) {
                    if (data.status == "1") {
                        alert("Invalidated Successfully");
                        $("#sbiv_" + count).text("InValidated");
                        $("#sbiv_" + count).css("font-weight", "Bold");
                    }
                });
            }
            function inValidateNoNotificationServiceBookEntry(count, empid, id, tabname) {
                var url = "InValidateNoNotificationServiceBookEntry.htm?empId=" + empid + "&id=" + id + "&tabname=" + tabname;
                $.getJSON(url, function(data) {
                    if (data.status == "1") {
                        alert("Invalidated Successfully");
                        $("#sbiv_" + count).text("InValidated");
                        $("#sbiv_" + count).css("font-weight", "Bold");
                    }
                });
            }
            /*function unlockSBTransactionType(empid, id) {
             if (confirm("Are you sure to unlock Transaction Type")) {
             alert('DO');
             var url = "unlockServiceBookEntryType.htm?empId=" + empid + "$id=" + id;
             $.getJSON(url, function(data) {
             if (data.status == "1") {
             alert("Unlocked");
             }
             
             });
             } else {
             return false;
             }
             }*/
            /*function unlockServiceBookEntryType(empid, id) {
             if (confirm("Are you sure to Unlock Entry Type?")) {
             
             var url = "unlockServiceBookEntryType.htm?empId=" + empid + "&id=" + id;
             $.getJSON(url, function(data) {
             if (data.status == "1") {                           
             alert("Unlocked");
             }
             
             }).done(function(result) {
             self.location = "ServiceBookInValidator.htm?empid=" + empid;
             });
             
             return true;
             } else {
             return false;
             }
             }*/

            function unlockServiceBookEntryType(empid, id) {
                if (confirm("Are you sure to Unlock Transaction Type?")) {
                    $.ajax({
                        url: 'unlockServiceBookEntryType.htm',
                        type: 'get',
                        data: 'empId=' + empid + '&id=' + id,
                        success: function(retVal) {
                            self.location = "ServiceBookInValidator.htm?empid=" + empid;
                            //msg.concat("Unlocked");
                            alert('Unlocked');
                        }
                    });
                }

            }


        </script>
        <style>
            .tooltip {
                position: relative;
                display: inline-block;
                border-bottom: 1px dotted black;
            }

            .tooltip .tooltiptext {
                visibility: hidden;
                width: 120px;
                background-color: black;
                color: #fff;
                text-align: center;
                border-radius: 6px;
                padding: 5px 0;

                /* Position the tooltip */
                position: absolute;
                z-index: 1;
            }

            .tooltip:hover .tooltiptext {
                visibility: visible;
            }
        </style>
    </head>
    <body>

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
                    <div class="col-lg-3" style="text-align:center;font-weight: bold;"></div>
                    <div class="col-lg-9">
                        <h2><c:out value="${empid}"/> - <c:out value="${empprofile.empName}"/></h2>
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
                                            <th style="text-align:center;font-size:16px;width: 10%;" class="printLabelHeader">Order Date</th>
                                            <th style="text-align:center;font-size:16px;width: 10%;" class="printLabelHeader">With Effect From</th>
                                            <th style="text-align:center;font-size:16px;width: 50%;" class="printLabelHeader">Transaction</th>
                                            <th style="text-align:center;font-size:16px;width: 10%;" class="printLabelHeader">Type of Transaction</th>
                                            <th style="text-align:center;font-size:16px;width: 10%;" class="printLabelHeader">Action</th>
                                        </tr>
                                    </thead>
                                    <c:forEach items="${esb.empsbrecord}" var="servicehistory" varStatus="cnt">
                                        <tr height="50px">
                                            <td style="text-align: center;">
                                                ${cnt.index + 1}
                                            </td>
                                            <td style="text-align: center;">
                                                ${servicehistory.ordDate}
                                            </td>
                                            <td style="text-align: center;">                                    
                                                ${servicehistory.wefChange}
                                            </td>
                                            <td style="text-align: left;">
                                                <b><u>${servicehistory.category}</u></b><br />
                                                ${servicehistory.sbdescription}<br/>
                                                <c:if test="${not empty servicehistory.moduleNote}">
                                                    Note - <c:out value="${servicehistory.moduleNote}"/>
                                                </c:if>
                                            </td>
                                            <c:if test="${usrTyp == 'A'}">
                                                <td style="text-align: center;">
                                                    <a href="javascript:unlockServiceBookEntryType('${empid}','${servicehistory.noteId}');" title="Unlock Transaction Type"><strong>${servicehistory.entryType}</strong><br/></a>                                                

                                                </td>
                                            </c:if>
                                            <c:if test="${usrTyp != 'A'}">
                                                <td style="text-align: center;">
                                                    <strong>${servicehistory.entryType}</strong><br/>                                                

                                                </td>
                                            </c:if>

                                            <td style="text-align: center;">
                                                <span id="sb_${cnt.index + 1}">
                                                    <%--<c:if test="${servicehistory.isValidated eq 'N'}">
                                                        <c:if test="${servicehistory.tabname eq 'JOINING' || servicehistory.tabname eq 'TRAINING' || servicehistory.tabname eq 'SERVICE VERIFICATION CERTIFICATE' || servicehistory.tabname eq 'RELIEVE' || servicehistory.tabname eq 'EXAMINATION' || servicehistory.tabname eq 'REINSTATEMENT' || servicehistory.tabname eq 'SUSPENSION' || servicehistory.tabname eq 'PERMISSION' || servicehistory.tabname eq 'EDUCATION' || servicehistory.tabname eq 'RETIREMENT' || servicehistory.tabname eq 'MISC'}">
                                                            <a href="javascript:validateNoNotificationServiceBookEntry('${cnt.index + 1}','${empid}','${servicehistory.noteId}','${servicehistory.tabname}');" onclick="return confirm('Are You sure to Validate?');">Validate</a>
                                                        </c:if>
                                                        <c:if test="${servicehistory.tabname ne 'JOINING' && servicehistory.tabname ne 'TRAINING' && servicehistory.tabname ne 'SERVICE VERIFICATION CERTIFICATE' && servicehistory.tabname ne 'RELIEVE' && servicehistory.tabname ne 'EXAMINATION' && servicehistory.tabname ne 'REINSTATEMENT' && servicehistory.tabname ne 'SUSPENSION' && servicehistory.tabname ne 'PERMISSION' && servicehistory.tabname ne 'EDUCATION' && servicehistory.tabname ne 'RETIREMENT' && servicehistory.tabname ne 'MISC'}">
                                                            <a href="javascript:validateServiceBookEntry('${cnt.index + 1}','${empid}','${servicehistory.noteId}');" onclick="return confirm('Are You sure to Validate?');">Validate</a>
                                                        </c:if>
                                                    </c:if>--%>
                                                    <c:if test="${servicehistory.isValidated eq 'Y'}">
                                                        <strong>Validated</strong>
                                                    </c:if>
                                                    <c:if test="${servicehistory.isValidated eq 'N'}">
                                                        <strong>Not Validated</strong>
                                                    </c:if>
                                                </span>
                                            </td>
                                            <td>
                                                <span id="sbiv_${cnt.index + 1}">
                                                    <c:if test="${servicehistory.isValidated eq 'Y'}">
                                                        <c:if test="${servicehistory.tabname eq 'JOINING' || servicehistory.tabname eq 'TRAINING' || servicehistory.tabname eq 'SERVICE VERIFICATION CERTIFICATE' || servicehistory.tabname eq 'RELIEVE' || servicehistory.tabname eq 'EXAMINATION' || servicehistory.tabname eq 'REINSTATEMENT' || servicehistory.tabname eq 'SUSPENSION' || servicehistory.tabname eq 'PERMISSION' || servicehistory.tabname eq 'EDUCATION' || servicehistory.tabname eq 'RETIREMENT' || servicehistory.tabname eq 'MISC'}">
                                                            <a href="javascript:inValidateNoNotificationServiceBookEntry('${cnt.index + 1}','${empid}','${servicehistory.noteId}','${servicehistory.tabname}');" onclick="return confirm('Are You sure to InValidate?');">InValidate</a>                                                           
                                                        </c:if>
                                                        <c:if test="${servicehistory.tabname ne 'JOINING' && servicehistory.tabname ne 'TRAINING' && servicehistory.tabname ne 'SERVICE VERIFICATION CERTIFICATE' && servicehistory.tabname ne 'RELIEVE' && servicehistory.tabname ne 'EXAMINATION' && servicehistory.tabname ne 'REINSTATEMENT' && servicehistory.tabname ne 'SUSPENSION' && servicehistory.tabname ne 'PERMISSION' && servicehistory.tabname ne 'EDUCATION' && servicehistory.tabname ne 'RETIREMENT' && servicehistory.tabname ne 'MISC'}">
                                                            <a href="javascript:inValidateServiceBookEntry('${cnt.index + 1}','${empid}','${servicehistory.noteId}');" onclick="return confirm('Are You sure to InValidate?');" onchange="return back_ground_color_invalidated();">InValidate</a>

                                                        </c:if>

                                                    </c:if>
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>

    </body>
</html>
