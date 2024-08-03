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
        <title>JSP Page</title>
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
                if($('#main_counter').val() == 1)
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


        </script>
    </head>
    <body>
        <form:form action="newProposalMaster.htm" id="frmEmployee" commandName="IncrementProposal" method="post" onsubmit="return validateForm();">

            <form:hidden id="proposalId" path="proposalId"/>
            <form:hidden id="proposalMonth" path="proposalMonth"/>
            <form:hidden id="proposalYear" path="proposalYear"/>
            <input type="hidden" id="tempVal" value="" />
            <form:hidden id="opt" path="opt" />


            <div class="container-fluid">
                <div class="panel panel-default" style="height:600px;overflow:auto;">
                    <div class="panel-footer" style="position:fixed;right:30px;">
                        <input type="submit" name="action" id="btn_add" onclick="$('#tempVal').val(1)" class="btn btn-success" value="Add Employee to Proposal List"/>
                        <c:if test="${IncrementProposal.opt eq 'edit'}"><input type="button" name="action" id="btn_back" onclick="self.location = 'EditIncrementProposal.htm?proposalId=' + $('#proposalId').val() + '&pmonth=' + $('#proposalMonth').val() + '&pyear=' + $('#proposalYear').val();" class="btn btn-success" value="Back to Proposal List"/> </c:if>
                        <c:if test="${IncrementProposal.opt eq 'add'}"><input type="submit" name="action" id="btn_add" class="btn btn-success" value="Back to Proposal List"/></c:if>
                        </div>   

                        <div class="panel-heading" style="background:#004D95;color:#FFFFFF;font-weight:bold;margin-top:60px;">
                            ADD EMPLOYEES TO INCREMENT PROPOSAL FOR THE MONTH OF : ${IncrementProposal.monthasString}
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr style="background:#EAEAEA;">
                                    <th width="3%"></th>
                                    <th width="5%">Sl No.</th>
                                    <th width="15%"> Employee ID</th>
                                    <th>Employee Name</th>
                                    <th width="10%"> GPF/PRAN </th>
                                    <th width="10%">Next Increment</th>
                                    <th>Designation</th>
                                    <th>View Pay</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${EmpList}" var="list" varStatus="slnoCnt">
                                        <tr>
                                            <td><input type="checkbox" name="empId" id="empId_${list.empId}" value="${list.empId}" />
                                            <td>${slnoCnt.index+1}</td>
                                            <td>${list.empId}</td>
                                            <td>${list.fullName}</td>
                                            <td>${list.gpfno}</td>
                                            <td>${list.strIncrDate}</td>
                                            <td>${list.substantivePost.spn}</td>
                                            <td><a href="javascript:void(0)" onclick="window.open('ViewEmpLastYearPay.htm?empId=${list.empId}', '', 'width=600,height=600,left=400,top=200')">View Pay</a></td>
                                        </tr>
                                </c:forEach>
                                         <input type="hidden" id="main_counter" value="${EmpList.size()}" />
                            </tbody>                            
                        </table>

                    </div>

                </div>
            </div>
        </form:form>
        <script type="text/javascript">
            var obj = document.getElementById('frmEmployee');
        </script>

    </body>
</html>
