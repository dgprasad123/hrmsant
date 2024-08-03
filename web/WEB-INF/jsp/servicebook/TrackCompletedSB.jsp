<%-- 
    Document   : AddIncrementMasterData
    Created on : 28 Jun, 2016, 3:46:09 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UPDATE THE SERVICE BOOK UPDATE STATUS FOR THE EMPLOYEE</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $('#dlg').dialog('close');
                propmastId = $("#proposalId").val();
                $('#dg').datagrid({
                    url: "proposalListAction.htm?proposalId=" + propmastId
                });

            });

            function validateForm()
            {
                if ($('#main_counter').val() == 1)
                {
                    isChecked = false;
                    if (obj.elements['empId'].checked)
                    {
                        isChecked = true;
                    }
                    if (!isChecked)
                    {
                        alert("Please check at least one employee to add.");
                        $('#tempVal').val("");
                        return false;
                    }
                }
                else
                {
                    if ($('#tempVal').val() == 1)
                    {
                        isChecked = false;
                        for (var i = 0; i < obj.elements['empId'].length; i++)
                        {
                            if (obj.elements['empId'][i].checked)
                            {
                                isChecked = true;
                                break;
                            }
                        }
                        if (!isChecked)
                        {
                            alert("Please check at least one employee to add.");
                            $('#tempVal').val("");
                            return false;
                        }
                    }
                }
                return true;
            }
            function updateSBStatus(empId, sbStatus)
            {
                if (sbStatus == 'Y')
                {
                    msg = "Are you sure you want to update the Service Book Updation Status as Complete?";
                }
                else
                {
                    msg = "Are you sure you want to update the Service Book Updation Status as Incomplete?";
                }
                if (confirm(msg))
                {
                    $.ajax({
                        url: "UpdateServiceBookStatus.htm?empId=" + empId + '&sbStatus=' + sbStatus,
                        success: function(result) {
                            if (sbStatus == 'Y')
                            {
                                if (result == "Y") {
                                    $('#row_' + empId).css('background-color', '#FFFFE1');
                                    $('#status_blk_' + empId).html('<span style="color:#008900;font-weight:bold;"><img src="images/verified.png" style="vertical-align:middle;width:20px;" /> Completed</span><br /><a href="javascript:void(0)" onclick="javascript: updateSBStatus(\'' + empId + '\', \'N\')">Mark this as Incomplete</a>');
                                } else if (result == "N") {
                                    alert("Service Book entries are pending for Validation!");
                                }
                            }
                            else
                            {
                                $('#row_' + empId).css('background-color', '#FFFFE1');
                                $('#status_blk_' + empId).html('<a href="javascript:void(0)" onclick="javascript: updateSBStatus(\'' + empId + '\', \'Y\')">Mark this as Complete</a>');
                            }
                        }});
                }
            }

        </script>
    </head>
    <body>
        <form  id="frmEmployee" method="post">



            <div class="container-fluid">
                <div class="panel panel-default" style="height:600px;overflow:auto;">


                    <div class="panel-heading" style="background:#004D95;color:#FFFFFF;font-weight:bold;margin-top:60px;">
                        UPDATE THE SERVICE BOOK UPDATE STATUS FOR THE EMPLOYEE
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr style="background:#EAEAEA;">
                                    <th width="5%">Sl No.</th>
                                    <th width="15%"> Employee ID</th>
                                    <th>Employee Name</th>
                                    <th width="10%"> GPF/PRAN </th>
                                    <th>Designation</th>
                                    <th>DOS</th>
                                    <th style="text-align:center;">Service Book Updation Completed?</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${EmpList}" var="list" varStatus="slnoCnt">
                                    <tr id="row_${list.empId}"<c:if test="${list.ifmsCode <= 2021}"> bgcolor="#F0FFF7"</c:if>>
                                        <td>${slnoCnt.index+1}</td>
                                        <td>${list.empId}</td>
                                        <td>${list.fullName}</td>
                                        <td>${list.gpfno}</td>
                                        <td>${list.substantivePost.spn}</td>
                                        <td>${list.dos}</td>
                                        <td align="center" id="status_blk_${list.empId}"><c:if test="${list.hasPrivilages == 'Y'}"><span style="color:#008900;font-weight:bold;"><img src="images/verified.png" style="vertical-align:middle;width:20px;" /> Completed</span><br /><a href="javascript:void(0)" onclick="javascript: updateSBStatus('${list.empId}', 'N')">Mark this as Incomplete</a></c:if><c:if test="${list.hasPrivilages == 'N'}"><a href="javascript:void(0)" onclick="javascript: updateSBStatus('${list.empId}', 'Y')">Mark this as Complete</a></c:if></td>
                                        </tr>
                                </c:forEach>
                            <input type="hidden" id="main_counter" value="${EmpList.size()}" />
                            </tbody>                            
                        </table>

                    </div>

                </div>
            </div>
        </form>
        <script type="text/javascript">
            var obj = document.getElementById('frmEmployee');
        </script>

    </body>
</html>
