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
        <title>Edit Increment Proposal List</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script> 


        <script type="text/javascript">
            $(document).ready(function () {
                /*$('#dlg').dialog('close');
                 propmastId = $("#proposalId").val();
                 $('#dg').datagrid({
                 url: "proposalListAction.htm?proposalId=" + propmastId
                 });*/

            });

            function addEmployeeintoList() {
                month = $("#proposalMonth").val();
                year = $("#proposalYear").val();
                propmastId = $("#proposalId").val();
                emplist = $("#dgAddEmp").datagrid("getChecked");
                var ids = '';
                for (var i = 0; i < emplist.length; i++) {
                    if (ids != '') {
                        ids = ids + ',' + emplist[i].empId;
                    } else {
                        ids = emplist[i].empId;
                    }

                }
                if (emplist.length > 0) {
                    $.ajax({
                        type: 'POST',
                        data: {emplist: ids, month: month, year: year, proposlMastId: propmastId},
                        url: 'addProposedEmployee.htm',
                        success: function (response) {
                            $('#dlg').dialog('close');
                            $('#dg').datagrid('reload');
                        }
                    });
                } else {
                    alert('Nothing to Add');
                }
            }

            function saveproposal() {
                $(".easyui-linkbutton").attr('disabled', true);
                data = $("#dg").datagrid("getRows");
                var ids = '';
                for (var i = 0; i < data.length; i++) {
                    if (ids != '') {
                        ids = ids + ',' + data[i].empId;
                    } else {
                        ids = data[i].empId;
                    }

                }
                proposalId = $("#proposalId").val();
                month = $("#proposalMonth").val();
                year = $("#proposalYear").val();
                if (data.length > 0) {
                    $.ajax({
                        type: 'POST',
                        data: {proposalId: proposalId, emplist: ids, month: month, year: year},
                        url: 'saveProposalList.htm',
                        success: function (response) {
                            alert('Proposal Created Successfully');
                            window.location.href = "displayProposalListpage.htm";
                        }
                    });
                } else {
                    alert('Nothing to Save');
                }
            }

            function removeProposal(data) {
                var result = confirm("Are you sure want to remove from Proposal List?");
                if (result) {
                    $.ajax({
                        type: 'POST',
                        url: 'removeProposalList.htm?' + data,
                        success: function (response) {
                            $('#dg').datagrid('reload');
                        }
                    });
                } else {
                    return false;
                }

            }

            function returnveify() {
                data = $("#dg").datagrid("getRows");
                if (data.length > $('#rowcount').val()) {
                    if (confirm("Your proposal List not save. Are you want to save?"))
                    {
                        return false;
                    }
                }
            }
            function deleteEmp(empId, proposalDetailId)
            {
                if (confirm("Are you sure you want to remove this Employee?"))
                {
                    $.ajax({
                        type: "get",
                        url: 'removeProposalList.htm',
                        data: 'empId=' + empId + '&proposalDetailId=' + proposalDetailId,
                        cache: false,
                        success: function (retVal) {
                            self.location = 'EditIncrementProposal.htm?proposalId=' + $('#proposalId').val() + '&pmonth=' + $('#proposalMonth').val() + '&pyear=' + $('#proposalYear').val();
                        }
                    });
                }
            }
            function getCells(empId, level, presentPay, cell)
            {
                if (level != '0')
                {
                    $('#pay_commission_' + empId).val('7');
                    $.ajax({
                        type: 'GET',
                        data: {empId: empId, level: level, presentPay: presentPay},
                        url: 'getCellList.htm',
                        success: function (response) {
                            $('#sp_lvl_blk_' + empId).css('visibility', 'visible');
                            $('#cell_blk_' + empId).css('visibility', 'visible');
                            $('#cell_blk_' + empId).html(response);
                            $('#matrix_cell_' + empId).val(cell);
                        }
                    });
                }
                if ($('#pay_commission_' + empId).val() == '6')
                {
                    $('#sp_lvl_blk_' + empId).css('visibility', 'hidden');
                    $('#cell_blk_' + empId).css('visibility', 'hidden');
                }
            }
            function getFuturePay(empId, presentPay)
            {
                level = $('#matrix_level_' + empId).val();
                cell = $('#matrix_cell_' + empId).val();
                payCommission = $('#pay_commission_' + empId).val();
                if ((payCommission == 7 && cell != '' && level != ''))
                {
                    $.ajax({
                        type: 'GET',
                        data: {cell: cell, level: level, empId: empId, presentPay: presentPay, payCommission: payCommission},
                        url: 'getIncrementPay.htm',
                        success: function (response) {
                            $('#sp_lvl_blk_' + empId).css('visibility', 'visible');
                            $('#cell_blk_' + empId).css('visibility', 'visible');
                            $('#future_pay_' + empId).html(response);
                        }
                    });
                }
                if (payCommission == 6)
                {
                    $.ajax({
                        type: 'GET',
                        data: {cell: 1, level: 1, empId: empId, presentPay: presentPay, payCommission: payCommission},
                        url: 'getIncrementPay.htm',
                        success: function (response) {
                            $('#future_pay_' + empId).html(response);
                            $('#sp_lvl_blk_' + empId).css('visibility', 'hidden');
                            $('#cell_blk_' + empId).css('visibility', 'hidden');
                        }
                    });
                }
            }
            function validateSubmit()
            {
                if ($('#main_counter').val() < 1)
                {
                    // alert('0');
                    return false;
                }
                else if ($('#main_counter').val() == 1)
                {
                    if ($('#wef_date_' + $('#frmIncrement')[0].emplist.value).val() == '')
                    {
                        alert("Please select Increment Date for all employees.");
                        $('#wef_date_' + $('#frmIncrement')[0].emplist.value).focus();
                        return false;
                    }
                    if ($('#pay_commission_' + $('#frmIncrement')[0].emplist.value).val() == '0')
                    {
                        alert("Please select Pay Commission for all the employees.");
                        return false;
                    }
                    if ($('#pay_commission_' + $('#frmIncrement')[0].emplist.value).val() == '7')
                    {
                        if ($('#matrix_level_' + $('#frmIncrement')[0].emplist.value).val() == '0')
                        {
                            alert("Please select Level.");
                            $('#matrix_level_' + $('#frmIncrement')[0].emplist.value).focus();
                            return false;
                        }
                        if ($('#matrix_cell_' + $('#frmIncrement')[0].emplist.value).val() == '0')
                        {
                            alert("Please select Cell for HRMS ID " + $('#frmIncrement')[0].emplist.value);
                            $('#matrix_cell_' + $('#frmIncrement')[0].emplist.value).focus();
                            return false;
                        }
                    }
                    // return false;
                }
                else
                {
                    for (i = 0; i < $('#frmIncrement')[0].elements['emplist'].length; i++)
                    {
                        if ($('#wef_date_' + $('#frmIncrement')[0].elements['emplist'][i].value).val() == '')
                        {
                            alert("Please select Increment Date for all employees.");
                            $('#wef_date_' + $('#frmIncrement')[0].elements['emplist'][i].value).focus();
                            return false;
                        }
                        if ($('#pay_commission_' + $('#frmIncrement')[0].elements['emplist'][i].value).val() == '0')
                        {
                            alert("Please select Pay Commission for all the employees.");
                            return false;
                        }
                        if ($('#pay_commission_' + $('#frmIncrement')[0].elements['emplist'][i].value).val() == '7')
                        {
                            if ($('#matrix_level_' + $('#frmIncrement')[0].elements['emplist'][i].value).val() == '0')
                            {
                                alert("Please select Level.");
                                $('#matrix_level_' + $('#frmIncrement')[0].elements['emplist'][i].value).focus();
                                return false;
                            }
                            if ($('#matrix_cell_' + $('#frmIncrement')[0].elements['emplist'][i].value).val() == '0')
                            {
                                alert("Please select Cell for HRMS ID " + $('#frmIncrement')[0].elements['emplist'][i].value);
                                $('#matrix_cell_' + $('#frmIncrement')[0].elements['emplist'][i].value).focus();
                                return false;
                            }
                        }
                    }
                }
            }
            $(document).ready(function () {
                if ($('#main_counter').val() == 1)
                {
                    empId = $('#frmIncrement')[0].emplist.value;
                    cell = $('#previous_cell_' + empId).val();
                    level = $('#matrix_level_' + empId).val()
                    presentPay = $('#present_pay_' + empId).val();
                    if (level != '0')
                    {
                        getCells(empId, level, presentPay, cell);
                    }
                    if ($('#pay_commission_' + empId).val() == '6')
                    {
                        $('#sp_lvl_blk_' + empId).css('visibility', 'hidden');
                        $('#cell_blk_' + empId).css('visibility', 'hidden');
                    }
                }
                else
                {
                    for (i = 0; i < $('#frmIncrement')[0].elements['emplist'].length; i++)
                    {
                        empId = $('#frmIncrement')[0].elements['emplist'][i].value;
                        cell = $('#previous_cell_' + empId).val();
                        level = $('#matrix_level_' + empId).val()
                        presentPay = $('#present_pay_' + empId).val();
                        if (level != '0')
                        {
                            getCells(empId, level, presentPay, cell);
                        }
                        if ($('#pay_commission_' + empId).val() == '6')
                        {
                            $('#sp_lvl_blk_' + empId).css('visibility', 'hidden');
                            $('#cell_blk_' + empId).css('visibility', 'hidden');
                        }
                    }
                }
            });
            var curdate = new Date();
            $(document).ready(function () {


                $('.wDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    minDate: new Date(2020, 0, 1),
                    maxDate: curdate,
                    ignoreReadonly: true
                });

            });
        </script>
    </head>
    <body>
        <form:form action="finalfutureproposallist.htm" commandName="IncrementProposal" method="post" id="frmIncrement">





            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" style="background:#004D95;color:#FFFFFF;font-weight:bold;">
                        INCREMENT PROPOSAL LIST FOR THE MONTH OF : ${IncrementProposal.monthasString}
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="5%">Sl No.</th>
                                    <th width="5%"> HRMS ID </th>
                                    <th width="10%"> GPF NO/PRAN </th>
                                    <th width="20%">Employee Name</th>
                                    <th width="20%">Substantive/ <br> Officiating</th>

                                    <th width="10%">Present Pay </th>
                                    <th width="10%">Date from which </br> present pay is drawn</th>
                                    <th width="10%">Date of present </br> increment</th>
                                    <th width="10%">Future pay</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${EmpList}" var="list" varStatus="counter">
                                    <tr>
                                        <td>${counter.count}</td>
                                        <td>${list.empId}
                                            <input type="hidden" name="emplist" value="${list.empId}" />
                                            <input type="hidden" id="present_pay_${list.empId}" value="${list.presentpay}" />

                                            <input type="hidden" id="previous_cell_${list.empId}" value="${list.matrixCell}" /></td>
                                        <td>${list.gpfno}</td>
                                        <td>${list.empname}</td>
                                        <td>${list.post}</td>
                                        <td>${list.presentpay}<br><b><span style="color:red;font-size:12px;">(Level: ${list.matrixLevel},Cell: ${list.matrixCell} )</span></b></td>
                                        <td>${list.presentpaydate}</td>
                                        <td >${list.nextincr}</td>         
                                        <td >${list.futurepay}<br><b><span style="color:red;font-size:12px;">(Level: ${list.matrixLevel},Cell:${list.nextMatrixCell})</span></b></td>

                                    </tr>
                                </c:forEach>
                            <input type="hidden" id="main_counter" value="${EmpList.size()}" />
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" class="btn btn-default" value="Generate PDF"/>
                    </div>
                </div>
            </div>
        </form:form>


    </body>
</html>
