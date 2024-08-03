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

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $(".txtNum").keypress(function(e) {
                    //if the letter is not digit then display error and don't type anything
                    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                        //display error message
                        $("#errmsg").html("Digits Only").show().fadeOut("slow");
                        return false;
                    }
                });
            });
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
            tr.strikeout td:before {
                content: " ";
                position: absolute;
                top: 50%;
                left: 0;
                border-bottom: 1px solid #111;
                width: 100%;
            }
        </style>
    </head>
    <body>        
        <form:form action="newTransfer.htm" method="post" commandName="transferForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">

                                <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;layout: fixed;color:#000000;font-weight:bold;">
                                    <thead> </thead>
                                    <tr>
                                        <td width="20%" align="right">
                                            Employee Name:                    
                                        </td>
                                        <td width="38%" style="text-transform:uppercase;" align="left">
                                            <b> ${SelectedEmpObj.fullName} </b>
                                        </td>
                                        <td width="16%" align="right">
                                            HRMS ID:                    
                                        </td>
                                        <td width="26%">
                                            ${SelectedEmpObj.empId} 
                                        </td>
                                    </tr>

                                    <tr>
                                        <td align="right">Current Post: </td>
                                        <td>
                                            &nbsp; ${SelectedEmpObj.postname} 
                                        </td>
                                        <td align="right">GPF/ PPAN No:</td>
                                        <td><b style="text-transform:uppercase;"> ${SelectedEmpObj.gpfno}     &nbsp;</b></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Current Cadre: </td>
                                        <td align="left"><b> ${SelectedEmpObj.cadrename}   &nbsp;</b></td>
                                        <td align="right">Current Status:</td>
                                        <td><b> ${SelectedEmpObj.depstatus}&nbsp;</b></td>
                                    </tr>
                                </table>

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="15%">Date of Entry</th>
                                    <th width="15%">Notification Order No</th>
                                    <th width="10%">Notification Order Date</th>
                                    <th width="10%">Notification Type</th>
                                    <th width="20%"> Transfered to Office </th>
                                    <th width="8%">Print in<br />Service Book</th>
                                    <th width="10%">Modified By</th>
                                    <th colspan="2" width="15%" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${transferlist}" var="tlist">
                                    <c:if test="${tlist.ifVisible eq 'N'}">
                                        <tr style="background: #FFB9B9;">
                                        </c:if>
                                        <c:if test="${empty tlist.ifVisible || tlist.ifVisible eq 'Y'}">
                                        <tr>
                                        </c:if>
                                        <td>${tlist.doe}</td>
                                        <td>${tlist.ordno}</td>
                                        <td>${tlist.ordt}</td>
                                        <td>${tlist.notType}</td>
                                        <td>${tlist.transferToOffice}</td>
                                        <td>
                                            <c:if test="${tlist.ifVisible eq 'N'}">
                                                No
                                            </c:if>
                                            <c:if test="${empty tlist.ifVisible || tlist.ifVisible eq 'Y'}">
                                                Yes
                                            </c:if>
                                        </td>
                                        <td>${tlist.modifiedBy}</td>
                                        <td>
                                            <c:if test="${tlist.isValidated eq 'N'}">
                                                <a href="editTransfer.htm?transferId=${tlist.transferId}&notId=${tlist.hnotid}">Edit</a>
                                            </c:if>
                                            <c:if test="${tlist.isValidated eq 'N'}">
                                                <a href="javascript:void(0)" onclick="javascript: confirmDelete('${tlist.transferId}', '${tlist.hnotid}')">Delete</a>
                                            </c:if><br />
                                            <c:if test="${empty tlist.canceltype}">
                                                <a href="SupersedeTransfer.htm?notId=${tlist.hnotid}">Supersede</a>
                                                <a href="CancelTransfer.htm?notId=${tlist.hnotid}">Cancel</a>
                                            </c:if>
                                            <c:if test="${not empty tlist.canceltype}">
                                                <c:if test="${tlist.canceltype eq 'TRANSFER'}">
                                                    <a href="SupersedeTransfer.htm?notId=0&supersedeid=${tlist.cancelnotid}">Supersede</a>
                                                </c:if>
                                                <c:if test="${tlist.canceltype eq 'CANCELLATION'}">
                                                    <a href="CancelTransfer.htm?notId=0&cancelnotId=${tlist.cancelnotid}">Cancel</a>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${tlist.isValidated eq 'Y'}">
                                                <a href="viewTransfer.htm?transferId=${tlist.transferId}&notId=${tlist.hnotid}">View</a>
                                            </c:if>
                                        </td>
                                        <c:if test="${not empty tlist.hrlvid}">
                                            <td><a href="entryRelieve.htm?notId=${tlist.hnotid}&rlvId=${tlist.hrlvid}">Relieve</a></td>
                                        </c:if>
                                        <c:if test="${tlist.transferId gt 0}">
                                            <c:if test="${empty tlist.hrlvid}">
                                                <td><a href="entryRelieve.htm?notId=${tlist.hnotid}&rlvId=${tlist.hrlvid}">Relieve</a></td>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${empty tlist.transferId}">
                                            <td style="color:red; "> Transfer not properly done, so edit and save again.</td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empid" id="empid"/>
                        <button type="submit" class="btn btn-default">New Transfer</button>&nbsp;&nbsp;&nbsp;
                        <span style="color: #F00000; font-weight: bold; font-size: 15px;">Red Color row indicates not to display in Service Book</span>
                    </div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript">
            function confirmDelete(transferId, notID)
            {
                if (confirm("Are you sure you want to delete the Transfer record from the list?"))
                {
                    $.ajax({
                        url: 'DeleteTransferRecord.htm',
                        type: 'get',
                        data: 'transfer_id=' + transferId + '&not_id=' + notID,
                        success: function(retVal) {
                            window.location.reload();
                        }
                    });
                }
            }
        </script>
    </body>
</html>
