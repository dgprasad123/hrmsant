<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>LeaveApplyList</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/color.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>

        <script language="javascript" type="text/javascript" >
            var incr = 1;
            $(document).ready(function() {
                $(".thickbox").colorbox({iframe: true, width: "50%", height: "50%",
                });
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false,
                    scrollMonth: false,
                    scrollInput: false
                });

                $('input[type=radio][name=rdFullPartPeriod]').change(function() {
                    var rdValue = $(this).val();
                    if (rdValue == "Full") {
                        $('#txtApproveFrom').hide();
                        $('#txtApproveTo').hide();
                        $('#txtApproveFrom').val('');
                        $('#txtApproveTo').val('');
                    } else if (rdValue == "Part") {
                        $('#txtApproveFrom').show();
                        $('#txtApproveTo').show();
                        $('#txtApproveFrom').attr("readonly", true);
                        $('#txtApproveTo').attr("readonly", true);
                    }
                });
            });
            function UploadFile() {
                var url = 'UploadDocumentAction.htm?KeepThis=true&TB_iframe=true&height=375&width=452&scrollwin=true';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false});
            }
            function editDetail(value, row, index) {
                var href = 'leaveViewData.htm?taskId=' + row.taskId;
                return '<a href="' + href + '" plain="true">View</a>';
            }
            function editJoining(value, row, index) {
                if (row.tolId != "CL") {
                    if (value == "Y") {
                        return '<a href="javascript:openJoiningDlg()" plain="true">Joining</a>';
                    }
                } else {
                    return 'N/A';
                }
            }

            function cancelLeaveFunc(value, row, index) {
                //var url = "cancelCasualLeaveAfterApproval.htm?taskid=" + row.taskId;
                //alert("URL is: "+url);
                //alert(row.statusId+""+value)
                /*if (value == "Y" && row.statusId != 102) {
                 return '<a href="javascript:openCancelWindow();" plain="true">Cancel</a>';
                 } else {
                 return 'N/A';
                 }*/

                if (row.statusId == 5) {

                    return '<a href="javascript:openCancelWindow();" plain="true">Cancel</a>';


                } else {
                    return 'N/A';
                }
            }

            function openCancelWindow() {
                var row = $('#dgleaveapply').datagrid('getSelected');
                if (row) {
                    //alert('Item ID:' + row.taskId + "\nPrice:" + row.fromDate);
                    $('#tempFrmDate').val(row.fromDate);
                    $('#txtApproveFrom').val(row.fromDate);
                    $('#tempToDate').val(row.toDate);
                    $('#txtApproveTo').val(row.toDate);
                    $('#dlgCLCancelWindow').dialog('open');
                }
               
            }

            function saveCLCancelDetails() {
                var row = $('#dgleaveapply').datagrid('getSelected');
                //alert(Date.now());
                //alert(Date.parse($('#tempFrmDate').val()));
                //
                //Compare the current date with leave start date
                if(Date.now() > Date.parse($('#tempFrmDate').val()))
                {
                    alert("Leave can not be cancelled.");
                }else{
                    
                    var appliedFrmDate = new Date($('#tempFrmDate').val());
                    var enteredFrmDate = new Date($('#txtApproveFrom').val());

                    if (appliedFrmDate.getTime() > enteredFrmDate.getTime()) {
                        alert("Enter within From Date and To Date");
                        return false;
                    }
                    var appliedToDate = new Date($('#txtApproveTo').val());
                    var enteredToDate = new Date($('#txtApproveTo').val());
                    if (appliedToDate.getTime() < enteredToDate.getTime()) {
                        alert("Enter within From Date and To Date");
                        return false;
                    }
                    $('#clForm').form('submit', {
                        url: 'saveCLCancelData.htm?hidTaskId=' + row.taskId,
                        success: function(result) {
                            //alert(result);
                            //var result = eval('(' + result + ')');
                            if (result.errorMsg) {
                                $.messager.show({
                                    title: 'Error',
                                    msg: result.errorMsg
                                });
                            } else {
                                $('#dlgCLCancelWindow').dialog('close'); // close the dialog
                                $('#dgleaveapply').datagrid('reload'); // reload the user data
                            }
                        }

                    });
                }//Date compare Else close
            }

            function openJoiningDlg() {
                // $('#dlgjoiningrpt').dialog('open');
                var row = $('#dgleaveapply').datagrid('getSelected');
                if (row) {
                    $('#dlgjoiningrpt').dialog('open');
                    $('#ff').form('load', 'joiningData.htm?taskId=' + row.taskId);
                    $('#dlgjoiningrpt').dialog('reload');
                }

            }
            function leaveExtensionReq(value, row, index) {
                var href = 'leaveextension.htm?taskId=' + row.taskId;
                if (value == "Y") {
                    return '<a href="' + href + '" plain="true">Request</a>';
                } else {
                    return 'N/A';
                }
            }

            function searchAuthority() {
                var url = 'leaveauthority.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "50%", height: "50%", overlayClose: false});
            }
            function SelectSpn(empId, empName, desig, spc)
            {
                $.colorbox.close();
                $('#txtSancAuthority').textbox('setValue', empName + "," + desig);
                $('#hidAuthEmpId').val(empId);
                $('#hidSpcAuthCode').val(spc);
            }
            function saveJoiningDetails() {
                if ($('#joiningDate').val() == '') {
                    alert("Please enter Joining Date");
                    return false;
                }
                $('#ff').form('submit', {
                    url: 'saveJoiningData.htm',
                    success: function(result) {
                        var result = eval('(' + result + ')');
                        if (result.errorMsg) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.errorMsg
                            });
                        } else {
                            $('#dlgjoiningrpt').dialog('close'); // close the dialog
                            $('#dgleaveapply').datagrid('reload'); // reload the user data
                        }
                    }

                });
            }
            function appendToUploadFileList(fileId, fileName) {
                var divId = incr;
                //  objhidAddRow = document.getElementById("hidTaskId");
                //  var strobjhidAddRow = objhidAddRow.name.split(".");
                var html = "<div id='" + divId + "'>";
                html = html + '<a href="javascript:removeTempAttachment(' + fileId + ',' + divId + ')">Delete<\/a>';
                html = html + "&nbsp;&nbsp;&nbsp;"
                html = html + "<input type='hidden' name='attachmentid' value='" + fileId + "' \/>";
                html = html + fileName;
                html = html + "<\/div>";
                $("#uploadfilelist").append(html);
                incr++;
            }
            function removeTempAttachment(fileId, divId) {
                $("#" + divId).remove();
                $.ajax({
                    type: 'POST',
                    url: 'removeuploadfile.htm?tempAttachment=' + fileId,
                    success: function(response) {
                        $("#" + divId).remove();
                    }
                });
            }
        </script>
        <style type="text/css">
            #fm{
                margin:0;
                padding:10px 30px;
            }
            .ftitle{
                font-size:14px;
                font-weight:bold;
                padding:5px 0;
                margin-bottom:10px;
                border-bottom:1px solid #ccc;
            }
            .fitem{
                margin-bottom:20px;
            }
            .fitem{
                margin-left:5px;
            }
            .fitem label{
                display:inline-block;
                width:20%;
            }
            .fitem1 label{
                display:inline-block;
                width:80%;
            }
        </style>
    </head>   
    <body >
        <form:form  action="leaveapply.htm" method="POST" commandName="leaveForm">
            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                <tbody>
                    <tr style="height: 20px" >
                        <td width="100%" align="left">
                            Balance of leave available as on: <b><c:out value="${curdate}"/></b>
                        </td>
                    </tr>
                    <tr style="height: 20px">
                        <td align="left">
                            Casual Leave : <b><c:out value="${clBalance}" /></b>
                            Earned Leave :<b><c:out value="${elBalance}" /></b>,
                            Half Pay Leave : <b><c:out value="${hplBalance}"/></b>
                            Commuted Leave : <b><c:out value="${colBalance}"/></b>
                        </td>
                    </tr>                        
                </tbody>
            </table>
            <div   style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;font-size:12px; font-family:verdana;"> 
                <table id="dgleaveapply" class="easyui-datagrid" style="width:100%;height:360px;font-size:12px; font-family:verdana;" title="My Leave"
                       rownumbers="true" pagination="true" singleSelect="true"  
                       toolbar="#toolbar" data-options="singleSelect:true,collapsible:true" url="leaveapplylist.htm?empId=${leaveForm.hidempId}">
                    <thead>

                        <tr>

                            <th hidden="leaveId" field="leaveId"></th>
                            <th hidden="taskId" field="taskId"></th>
                            <th hidden="statusId" field="statusId"></th>
                            <th data-options="field:'dateOfInitiation'" width="10%">Initiated On</th>
                            <th data-options="field:'leaveType'" width="15%">Leave Type</th>
                            <th data-options="field:'fromDate'" width="10%">From<br> Date</th>
                            <th data-options="field:'toDate'" width="10%">To <br> Date</th>
                            <th data-options="field:'submittedTo'" width="25%">Submitted To</th>
                            <th data-options="field:'status'" width="10%">Status</th>
                            <th field="leaveExtensionReq" formatter="leaveExtensionReq"  width="10%">Leave <br> Extension </th>
                            <th field="enableJoingRpt" formatter="editJoining"  width="5%">Joining</th>
                            <th field="cancelFlag" formatter="cancelLeaveFunc" width="5%">Cancel</th>
                            <th field="itemid" formatter="editDetail"  width="5%">View</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div id="toolbar">
                <a href="addleaveapply.htm?empId=${leaveForm.hidempId}&offCode=${leaveForm.offCode}" class="easyui-linkbutton c1" iconCls="icon-add" plain="true">Apply Leave</a>
            </div>
        </form:form>

        <div id="dlgjoiningrpt" class="easyui-dialog" title="Leave Joining Report" data-options="iconCls:'icon-save',fit:true" closed="true" style="width:100%;height:95%;top:5px;padding:5px 10px;font-size:12px; font-family:verdana;">
            <form id="ff" method="post">
                <input type="hidden" name="leaveId" id="leaveId" value="${leaveForm.leaveId}"/>
                <input type="hidden" name="hidTaskId" id="hidTaskId" value="${leaveForm.hidTaskId}"/>
                <input type="hidden" name="statusId" id="statusId" value="${leaveForm.statusId}"/>
                <input type="hidden" name="hidAuthEmpId" id="hidAuthEmpId" />
                <input type="hidden" name="hidSpcAuthCode" id="hidSpcAuthCode" />
                <input type="hidden" name="hidempId" id="hidempId" value="${leaveForm.hidempId}"/>
                <input type="hidden" name="offCode" id="hidempId" value="${leaveForm.offCode}"/>
                <div class = "fitem" >
                    <label >Joining Date :</label>
                    <input id="joiningDate" name="joiningDate"  readonly="true"  style="width:80px;" class="txtDate"  />
                </div>
                <div class = "fitem" >
                    <label >Issuing Authority :</label>
                    <input class="easyui-textbox" id="txtSancAuthority"  type="text" name="txtSancAuthority" style="width:40%"    readonly="true"></input>
                    <a href="javascript:void(searchAuthority())" class="easyui-linkbutton"> Search </a>
                </div>
                <div  class = "fitem">
                    <label >Attach Document :</label>
                    <c:forEach var="attachList" items="${leaveList}">
                        <c:out value="${attachList.originalFileName}"/>
                    </c:forEach>
                    <a href="javascript:UploadFile(0)" class="atag">Attach File</a><br/>
                    <div id="uploadfilelist"></div>
                </div>
                <div  class = "fitem">
                    <label>Note(If Any):</label>
                    <input class="easyui-textbox" name="joiningNote" data-options="multiline:true,width:'40%'"  id="joiningNote"  />                                     
                </div>

                <div style="text-align:center;padding:5px 0">
                    <a href="javascript:void(0)" id="btn" class="easyui-linkbutton c1" style="width:120px;height:40px" onclick="saveJoiningDetails()"  style="width:80px">Submit</a>
                </div>
            </form>
        </div>
        <div id="dlgCLCancelWindow" class="easyui-dialog" title="Leave Cancel Window" data-options="iconCls:'icon-save'" closed="true" style="width:50%;height:50%;top:5px;padding:5px 10px;font-size:12px; font-family:verdana;">
            <form id="clForm" method="post">
                <input type="hidden" id="tempFrmDate"/>
                <input type="hidden" id="tempToDate"/>
                <div style="margin-left:50px;padding:5px 0">
                    <input type="radio" name="rdFullPartPeriod" class="rdFullPartPeriod" value="Full" label="Full:" checked="checked">Full Period&nbsp;&nbsp;&nbsp;
                    <!--<input type="radio" name="rdFullPartPeriod" class="rdFullPartPeriod" value="Part" label="Part:">Partial Period-->
                </div>
                <div style="margin-left:50px;padding:5px 0">
                    <span>From Date</span>
                    <span>
                        <input type="text" name="txtApproveFrom" id="txtApproveFrom" readonly="readonly" style="width:80px;" class="txtDate"/>
                    </span>

                    <span>To Date</span>
                    <span>
                        <input name="txtApproveTo" id="txtApproveTo" readonly="readonly" style="width:80px;" class="txtDate"/>
                    </span>
                </div>
                <div style="text-align:center;padding:5px 0">
                    <a href="javascript:void(0)" class="easyui-linkbutton c1" style="width:120px;height:40px" onclick="saveCLCancelDetails()" style="width:80px">Request to Cancel</a>
                </div>
            </form>
        </div>
    </body>
</html>
