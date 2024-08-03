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
        <title>Update Order Number</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>     

        <script type="text/javascript">
             $(document).ready(function() {
                
                
                $('.oDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });                
                
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

            function returnveify(){
                data = $("#dg").datagrid("getRows");
                if(data.length>$('#rowcount').val()){
                    if(confirm("Your proposal List not save. Are you want to save?"))
                    {
                        return false;
                    }
                }
            }
            function deleteEmp(empId, proposalDetailId)
            {
                if(confirm("Are you sure you want to remove this Employee?"))
                {
                    $.ajax({
                        type: "get",
                        url: 'removeProposalList.htm',
                        data: 'empId='+empId+'&proposalDetailId='+proposalDetailId,
                        cache: false,
                        success: function(retVal) {
                            self.location = 'EditIncrementProposal.htm?proposalId='+$('#proposalId').val()+'&pmonth='+$('#proposalMonth').val()+'&pyear='+$('#proposalYear').val();
                        }
                    });
                }
            }
            function getCells(empId, level, presentPay, cell)
            {
                if(level != '')
                {
                    $('#pay_commission_'+empId).val('7');
                    $.ajax({
                        type: 'GET',
                        data: {empId: empId, level: level, presentPay: presentPay},
                        url: 'getCellList.htm',
                        success: function (response) {
                            $('#cell_blk_'+empId).html(response);
                            $('#matrix_cell_'+empId).val(cell);
                        }
                    });
                }
            }
            function getFuturePay(empId, presentPay)
            {
                level = $('#matrix_level_'+empId).val();
                cell = $('#matrix_cell_'+empId).val();
                payCommission = $('#pay_commission_'+empId).val();
                if((payCommission == 7 && cell != '' && level != ''))
                {
                    $.ajax({
                        type: 'GET',
                        data: {cell: cell, level: level, empId: empId, presentPay: presentPay, payCommission: payCommission},
                        url: 'getIncrementPay.htm',
                        success: function (response) {
                            $('#future_pay_'+empId).html(response);
                        }
                    });
                }
                if(payCommission == 6)
                {
                    $.ajax({
                        type: 'GET',
                        data: {cell: 1, level: 1, empId: empId, presentPay: presentPay, payCommission: payCommission},
                        url: 'getIncrementPay.htm',
                        success: function (response) {
                            $('#future_pay_'+empId).html(response);
                        }
                    });                    
                }
            }  
            function validateSubmit()
            {
               for(i = 0; i < $('#frmOrder')[0].elements['emplist'].length;i++)
               {
                   if($('#order_number_'+$('#frmOrder')[0].elements['emplist'][i].value).val() == '')
                   {
                       alert("Please enter Order Number for all the employees.");
                       return false;
                   }
                   if($('#memo_number_'+$('#frmOrder')[0].elements['emplist'][i].value).val() == '')
                   {
                       alert("Please enter Memo Number for all the employees.");
                       return false;
                   }
                   if($('#order_date_'+$('#frmOrder')[0].elements['emplist'][i].value).val() == '')
                   {
                       alert("Please enter Order Date for all the employees.");
                       return false;
                   }
                   
               }
            }
           
        </script>
    </head>
    <body>
        <form:form action="SaveEmpOrders.htm" commandName="EmpOrderBean" method="post" id="frmOrder">
            
            
            <form:hidden id="proposalId" path="proposalId"/>          
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" style="background:#004D95;color:#FFFFFF;font-weight:bold;">
                        UPDATE ORDER DETAILS FOR INCREMENT PROPOSAL LIST FOR THE MONTH OF : ${monthName}
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="5%">Sl No.</th>
                                    <th width="10%"> HRMS ID </th>
                                    <th width="20%">Employee Name</th>
                                    <th width="20%">Substantive/ <br> Officiating</th>
                                    <th width="5%">Present pay</th>
                                    <th width="5%">Future pay</th>
                                    <th>Order Number</th>
                                    <th>Memo Number</th>
                                    <th>Order Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${EmpList}" var="list" varStatus="counter">
                                    <tr>
                                        <td>${counter.count}</td>
                                        <td>${list.empId}
                                            <input type="hidden" name="emplist" value="${list.empId}" />
                                            <input type="hidden" name="proposalDetailId" value="${list.proposaldetailId}" />
                                        </td>
                                        <td>${list.empname}</td>
                                        <td>${list.post}</td>
                                        <td>${list.presentpay}<br /><strong>GP: ${list.gp}</strong></td>
                                        <td><span id="future_pay_${list.empId}">${list.futurepay}</span></td>
                                        <td><c:if test="${empty list.ordno}"><input type="text" name="orderNumber" id="order_number_${list.empId}" class="form-control" /></c:if>
                                            <c:if test="${!empty list.ordno}">${list.ordno}</c:if></td>
                                        <td><c:if test="${empty list.memoNo}"><input type="text" name="memoNumber" id="memo_number_${list.empId}" class="form-control" /></c:if>
                                        <c:if test="${!empty list.memoNo}">${list.memoNo}</c:if></td>
                                        <td><c:if test="${empty list.orderDate}">
                                              <div class='input-group date proDate' id='processDate'><input type="text"  id="order_date_${list.empId}" name="orderDate" readonly="readonly" class="oDate form-control" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span></div>                                                     
                                                 </c:if>
                                        <c:if test="${!empty list.orderDate}">${list.orderDate}</c:if>
                                            </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" class="btn btn-default" value="Update Employee Orders" onclick="return validateSubmit()"/>
                        <input type="button" class="btn btn-default" value="Back to Proposals" onclick="self.location='displayProposalListpage.htm?offCode='"/> 
                    </div>
                </div>
            </div>
        </form:form>
        
        
    </body>
</html>
