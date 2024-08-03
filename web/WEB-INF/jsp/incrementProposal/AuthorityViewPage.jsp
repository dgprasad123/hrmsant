<%-- 
    Document   : AuthorityViewPage
    Created on : 28 Jul, 2016, 11:53:09 AM
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
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/demo.css">

        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">

            $(document).ready(function() {
                propmastId = $("#proposalId").val();
                processStatus = $('#processStatus').val();
                $('#dg').datagrid({
                    url: 'authorityViewData.htm?proposalId=' + propmastId
                });

            });
            function submitForm() {
                propmastId = $("#proposalId").val();
                processStatus = $("#processStatus").combobox('getValue');
                note = $("#message").val();
                if(confirm("Are you sure you want to submit?"))
                {
                    $.ajax({
                        type: 'POST',
                        data: {propmastId: propmastId, status: processStatus, note: note, isVerified: ""},
                        url: 'authorityApproved.htm',
                        success: function(response) {
                            window.parent.$('#win').window('close');
                        }
                    });
                }
                else
                {
                    return false;
                }

            }
            function submitForm1()
            {
                propmastId = $("#proposalId").val();
                processStatus = $("#processStatus").val();
                note = $("#message").val();   
                if($('#isVerified1')[0].checked)
                    isVerified = 'Y';
                if($('#isVerified2')[0].checked)
                    isVerified = 'N';
                if (!$('#isVerified1')[0].checked && !$('#isVerified2')[0].checked)
                {
                    alert("Please verify the increment proposal before submitting.");
                    return false;
                }
                if ($('#message').val() == '')
                {
                    alert('Please enter your Remarks.');
                    return false;
                }
                if(confirm("Are you sure you want to submit?"))
                {                
                    $.ajax({
                        type: 'POST',
                        data: {propmastId: propmastId, status: processStatus, note: note, isVerified: isVerified},
                        url: 'authorityApproved.htm',
                        success: function(response) {
                            window.parent.$('#win').window('close');
                        }
                    });
                }
                else
                {
                    return false;
                }
            }


        </script>
    </head>
    <body>
        <input type="hidden" id="proposalId" name="proposalId" value="${incrementForm.proposalId}"/>
        
        <div style="margin:20px 0"></div>
        <table id="dg" class="easyui-datagrid" style="width:100%;height:300px;" title="Increment Proposal List" 
               data-options="rownumbers:true,fitColumns:true,singleSelect:true,method:'get'" toolbar="#toolbar">
            <thead>
                <tr>
                    <th data-options="field:'empId'">HRMS ID</th>
                    <th data-options="field:'gpfno'">GPF NO</th>
                    <th data-options="field:'empname'">Employee Name</th>
                    <th data-options="field:'post'">Substantive/ <br> Officiating</th>
                    <th data-options="field:'payscale'">Scale of Pay of Posts</th>
                    <th data-options="field:'presentpay'">Present pay</th>
                    <th data-options="field:'presentpaydate'">Date from which </br> present pay is drawn</th>
                    <th data-options="field:'nextincr'">Date of present </br> increment</th>
                    <th data-options="field:'futurepay'">Future pay</th>
                </tr> 
            </thead>
        </table>

        <div id="dlg-buttons">
            <table cellpadding="0" cellspacing="0" style="width:100%">
                <tr>
                    <td style="text-align:center">
                        <a href="generatepdf.htm?proposalId=${incrementForm.proposalId}" class="easyui-linkbutton" data-options="iconCls:'icon-pdf'" style="width:15%">Download</a>
                    </td>
                </tr>
            </table>
        </div>
        <c:if test="${incrementForm.processStatus == 15}">
            <input type="hidden" id="processStatus" name="processStatus" value="${incrementForm.processStatus}"/>
            <p style="color:#008900;font-size:12pt;">Is the Increment Proposal verified? And all the officials are eligible for increment?<br /> Select Yes to automatically forward this to the Reporting authority for approval.</p>
            <span><input type="radio" value="Y" id="isVerified1" name="isVerified" /> <label for="isVerified1" style="font-size:15pt;font-weight:bold;">Yes</label>
                <input type="radio" value="N" id="isVerified2" name="isVerified" /> <label for="isVerified2" style="font-size:15pt;font-weight:bold;">No</label></span><br />
            Remarks:<br />
            <input id="message" class="easyui-textbox" name="message" id="message" data-options="multiline:true" style="height:100px;width:500px;"></input> 
            <br /><a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm1()" style="width:80px">Submit</a>
        </c:if>
        <c:if test="${incrementForm.processStatus == 10}">
            <table>
                <tr>
                    <td width="20%">Take Action:</td>
                    <td width="20%">
                        <input id="processStatus" class="easyui-combobox" name="processStatus" style="width:100%;" data-options="
                               valueField: 'value',
                               textField: 'label',
                               url:'getStatusList.htm'
                               ">

                    </td>
                    <td width="10%">Note:</td>
                    <td width="40%"> 
                        <input id="message" class="easyui-textbox" name="message" data-options="multiline:true" style="height:60px;width:300px;"></input> 
                    </td>

                    <td width="10%"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px">Submit</a></td>
                </tr>
                <tr>

                </tr>
            </table>
        </c:if>
    </body>
</html>
