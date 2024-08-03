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
                $('#joiningdg').datagrid({
                    onClickRow: function(index, row) {
                        //alert("Join Id is: "+row.joinid);
                        //alert("Addl Chrg is: "+row.additionalCharge);
                        if (row.joinid != "") {
                            if (row.additionalCharge != 'Y') {
                                $('#joinAddlBtn').linkbutton('enable');
                            } else {
                                $('#joinAddlBtn').linkbutton('disable');
                            }
                            $('#joinEditBtn').linkbutton('enable');
                            $('#joinPostBtn').linkbutton('disable');
                        } else {
                            $('#joinPostBtn').linkbutton('enable');
                            $('#joinAddlBtn').linkbutton('disable');
                            $('#joinEditBtn').linkbutton('disable');
                        }
                    }
                });

                $('#department').combobox({
                    onSelect: function(record) {
                        $('#office').combobox('clear');
                        $('#genericpost').combobox('clear');
                        $('#substantivepost').combobox('clear');
                        var url = 'getOfficeListJSON.htm?deptcode=' + record.deptCode;
                        $('#office').combobox('reload', url);
                    }
                });
                $('#office').combobox({
                    onSelect: function(record) {
                        $('#genericpost').combobox('clear');
                        $('#substantivepost').combobox('clear');
                        var url = 'joiningGetGenericPostListJSON.htm?offcode=' + record.value;
                        $('#genericpost').combobox('reload', url);
                    }
                });
                $('#genericpost').combobox({
                    onSelect: function(record) {
                        alert("Hi");
                        $('#substantivepost').combobox('clear');
                        var url = 'joiningGetGPCWiseSPCListJSON.htm?offcode=' + $('#office').combobox('getValue') + '&gpc=' + record.value;
                        $('#substantivepost').combobox('reload', url);
                    }
                });
                $('#sltFieldOffice').combobox({
                    url: 'joiningGetFieldOffListJSON.htm?offcode='
                });

                if ($('#ujtFrmDt').datebox('getValue') != '') {
                    $('#chkujt').prop('checked', true);
                }
                if ($('#ujtToDt').datebox('getValue') != '') {
                    $('#chkujt').prop('checked', true);
                }

                $('#chkujt').change(function() {
                    if ($(this).is(":checked")) {
                        $('#ujtFrmDt').datebox('enable');
                        $('#ujtToDt').datebox('enable');
                    } else {
                        $('#ujtFrmDt').datebox('disable');
                        $('#ujtFrmDt').datebox('setValue', null);
                        $('#ujtToDt').datebox('disable');
                        $('#ujtToDt').datebox('setValue', null);
                    }
                })
            });
            function changepost() {
                $('#winsubstantivepost').window('open');
            }
            function openAddPostWindow() {
                $('#winAddPost').window('open');
            }
            function getPost() {
                var deptCode = $('#department').combobox('getValue');
                var offCode = $('#office').combobox('getValue');
                var spc = $('#substantivepost').combobox('getValue');
                var spn = $('#substantivepost').combobox('getText');

                $('#hidDeptCode').val(deptCode);
                $('#hidOffCode').val(offCode);
                $('#spc').val(spc);
                $('#spn').val(spn);
                $('#sltFieldOffice').combobox({
                    url: 'joiningGetFieldOffListJSON.htm?offcode=' + offCode
                });
                $('#winAddPost').window('close');
                $('#winsubstantivepost').window('close');
            }
            function SelectPost() {
                var deptCode = $('#hidDeptCode').val();
                var offCode = $('#hidOffCode').val();
                var spc = $('#sltPost').combobox('getValue');
                var spn = $('#sltPost').combobox('getText');

                $('#hidDeptCode').val(deptCode);
                $('#hidOffCode').val(offCode);
                $('#spc').val(spc);
                $('#spn').val(spn);
                $('#sltFieldOffice').combobox({
                    url: 'joiningGetFieldOffListJSON.htm?offcode=' + offCode
                });
                $('#winAddPost').window('close');
                $('#winsubstantivepost').window('close');
            }
            function joinPost() {
                var row = $('#joiningdg').datagrid('getSelected');
                if (row) {
                    var url = 'enterJoiningData.htm?notId=' + row.notid + '&rlvId=' + row.rlvid + '&leaveId=' + row.lcrid + '&addl=&jId=';
                    //$('#dlg').dialog('refresh', url);
                    alert("URL is: " + url);
                    $('#dlg').dialog('open');
                    $('#joiningDataForm').form('load', url);
                    //return "<a href='"+data+"'>Edit</a>";
                } else {
                    alert("Select a Row");
                }
                //return "<a href='" + data + "'>Relieve From Post</a>";
            }

            function joinAdditionalCharge() {
                var row = $('#joiningdg').datagrid('getSelected');
                if (row) {
                    var url = 'enterJoiningData.htm?notId=' + row.notid + '&rlvId=' + row.rlvid + '&leaveId=' + row.lcrid + '&addl=Y&jId=';
                    //$('#dlg').dialog('refresh', url);
                    $('#dlg').dialog('open');
                    $('#joiningDataForm').form('load', url);
                    //return "<a href='"+data+"'>Edit</a>";
                } else {
                    alert("Select a Row");
                }
                //return "<a href='" + data + "'>Relieve From Post</a>";
            }

            function editJoining() {
                var row = $('#joiningdg').datagrid('getSelected');
                if (row) {
                    //var url = 'enterJoiningData.htm?notId=' + row.notid + '&rlvId=' + row.rlvid + '&jid=' + row.joinid +'&leaveId=' + row.lcrid + '&addl='+row.additionalCharge;
                    var url = 'enterJoiningData.htm?notId=' + row.notid + '&rlvId=' + row.rlvid + '&leaveId=' + row.lcrid + '&addl=' + row.additionalCharge + '&jId=' + row.joinid;
                    alert("URL is: " + url);
                    //$('#dlg').dialog('refresh', url);
                    $('#dlg').dialog('open');
                    $('#joiningDataForm').form('load', url);
                    //return "<a href='"+data+"'>Edit</a>";
                } else {
                    alert("Select a Row");
                }
                //return "<a href='" + data + "'>Relieve From Post</a>";
            }
            function saveCheck() {
                if ($('#joiningOrdNo').val() == '') {
                    alert("Please Enter Joining No");
                    $('#joiningOrdNo').focus();
                    return false;
                }
                var joiningOrdDt = $('#joiningOrdDt').datebox('getValue');
                if (joiningOrdDt == '') {
                    alert("Please Enter Joining Order Date");
                    $('#joiningOrdDt').focus();
                    return false;
                }
                var joinDt = $('#joiningDt').datebox('getValue');
                if (joinDt == '') {
                    alert("Please Enter Joined on Date");
                    $('#joiningDt').focus();
                    return false;
                }
                var joinTime = $('#sltJoiningTime').combobox('getValue');
                if (joinTime == '') {
                    alert("Please Enter Joining Time");
                    return false;
                }
                return true;
            }
            function saveJoining() {
                //if (saveCheck()) {
                $('#joiningDataForm').form('submit', {
                    url: 'saveJoining.htm',
                    onSubmit: function() {
                        return saveCheck();
                    },
                    success: function(result) {
                        var result = eval('(' + result + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
                            $('#dlg').dialog('close'); // close the dialog
                            $('#joiningdg').datagrid('reload'); // reload the user data
                        }
                    }
                });
                //}
            }
            function delJoining() {
                $('#joiningDataForm').form('submit', {
                    url: 'deleteJoining.htm',
                    success: function(result) {
                        var result = eval('(' + result + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
                            $('#dlg').dialog('close'); // close the dialog
                            $('#joiningdg').datagrid('reload'); // reload the user data
                        }
                    }
                });
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th width="15%">Notification Type</th>
                                <th width="8%">Notification Order No</th>
                                <th width="8%">Notification Order<br /> Date</th>
                                <th width="8%">Joining Due Date</th>
                                <th width="8%">Joining Due Time</th>
                                <th width="8%">Joining Order No</th>
                                <th width="8%">Joining Order Date</th>
                                <th width="8%">Joining Date</th>
                                <th width="8%">Joining Time</th>
                                <th width="8%">Print in<br />Service Book</th>
                                <th width="8%">Modified By</th>
                                <th width="9%">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${joininglist}" var="jlist">
                                <c:if test="${jlist.notType eq 'TRANSFER' || jlist.notType eq 'PROMOTION' &&  not empty jlist.rlvid}">
                                    <c:if test="${jlist.printInServiceBook eq 'N'}">
                                        <tr style="background: #FFB9B9;">
                                        </c:if>
                                        <c:if test="${empty jlist.printInServiceBook || jlist.printInServiceBook eq 'Y'}">
                                        <tr>
                                        </c:if>
                                        <td>${jlist.notType}</td>
                                        <td>${jlist.notOrdNo}</td>
                                        <td>${jlist.notOrdDt}</td>
                                        <td>${jlist.joiningDueDt}</td>
                                        <c:if test="${not empty jlist.joiningDueTime && jlist.joiningDueTime == 'FN'}">
                                            <td>FORE NOON</td>
                                        </c:if>
                                        <c:if test="${not empty jlist.joiningDueTime && jlist.joiningDueTime == 'AN'}">
                                            <td>AFTER NOON</td>
                                        </c:if>
                                        <c:if test="${empty jlist.joiningDueTime}">
                                            <td>&nbsp;</td>
                                        </c:if>
                                        <td>${jlist.joiningOrdNo}</td>
                                        <td>${jlist.joiningOrdDt}</td>
                                        <td>${jlist.joiningDt}</td>
                                        <c:if test="${not empty jlist.joiningTime && jlist.joiningTime == 'FN'}">
                                            <td>FORE NOON</td>
                                        </c:if>
                                        <c:if test="${not empty jlist.joiningTime && jlist.joiningTime == 'AN'}">
                                            <td>AFTER NOON</td>
                                        </c:if>
                                        <c:if test="${empty jlist.joiningTime}">
                                            <td>&nbsp;</td>
                                        </c:if>
                                        <td>
                                            <c:if test="${jlist.printInServiceBook eq 'N'}">
                                                No
                                            </c:if>
                                            <c:if test="${empty jlist.printInServiceBook || jlist.printInServiceBook eq 'Y'}">
                                                Yes
                                            </c:if>
                                        </td>
                                        <td>${jlist.modifiedby}</td>
                                        <td>
                                            <c:if test="${empty jlist.joinid}"> 
                                                <a href="enterJoiningData.htm?notId=${jlist.notid}&rlvId=${jlist.rlvid}&leaveId=${jlist.lcrid}&jId=${jlist.joinid}&addl=">Join in Post</a>
                                            </c:if>
                                            <c:if test="${not empty jlist.joinid}">
                                                <c:if test="${jlist.isValidated eq 'N'}">
                                                    <a href="enterJoiningData.htm?notId=${jlist.notid}&rlvId=${jlist.rlvid}&leaveId=${jlist.lcrid}&jId=${jlist.joinid}&addl=">Edit</a>
                                                </c:if>
                                                <c:if test="${jlist.isValidated eq 'Y'}">
                                                    <a href="viewJoiningData.htm?notId=${jlist.notid}&rlvId=${jlist.rlvid}&leaveId=${jlist.lcrid}&jId=${jlist.joinid}&addl=">View</a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${jlist.notType ne 'TRANSFER' && jlist.notType ne 'PROMOTION'}"> 
                                    <c:if test="${jlist.printInServiceBook eq 'N'}">
                                        <tr style="background: #FFB9B9;">
                                        </c:if>
                                        <c:if test="${empty jlist.printInServiceBook || jlist.printInServiceBook eq 'Y'}">
                                        <tr>
                                        </c:if>
                                        <td>${jlist.notType}</td>
                                        <td>${jlist.notOrdNo}</td>
                                        <td>${jlist.notOrdDt}</td>
                                        <td>${jlist.joiningDueDt}</td>
                                        <c:if test="${not empty jlist.joiningDueTime && jlist.joiningDueTime == 'FN'}">
                                            <td>FORE NOON</td>
                                        </c:if>
                                        <c:if test="${not empty jlist.joiningDueTime && jlist.joiningDueTime == 'AN'}">
                                            <td>AFTER NOON</td>
                                        </c:if>
                                        <c:if test="${empty jlist.joiningDueTime}">
                                            <td>&nbsp;</td>
                                        </c:if>
                                        <td>${jlist.joiningOrdNo}</td>
                                        <td>${jlist.joiningOrdDt}</td>
                                        <td>${jlist.joiningDt}</td>
                                        <c:if test="${not empty jlist.joiningTime && jlist.joiningTime == 'FN'}">
                                            <td>FORE NOON</td>
                                        </c:if>
                                        <c:if test="${not empty jlist.joiningTime && jlist.joiningTime == 'AN'}">
                                            <td>AFTER NOON</td>
                                        </c:if>
                                        <c:if test="${empty jlist.joiningTime}">
                                            <td>&nbsp;</td>
                                        </c:if>
                                        <td>
                                            <c:if test="${jlist.printInServiceBook eq 'N'}">
                                                No
                                            </c:if>
                                            <c:if test="${empty jlist.printInServiceBook || jlist.printInServiceBook eq 'Y'}">
                                                Yes
                                            </c:if>
                                        </td>
                                        <td>${jlist.modifiedby}</td>
                                        <td>
                                            <c:if test="${empty jlist.joinid}"> 
                                                <a href="enterJoiningData.htm?notId=${jlist.notid}&rlvId=${jlist.rlvid}&leaveId=${jlist.lcrid}&jId=${jlist.joinid}&addl=">Join in Post</a>
                                            </c:if>
                                            <c:if test="${not empty jlist.joinid}">
                                                <c:if test="${jlist.isValidated eq 'N'}">
                                                    <a href="enterJoiningData.htm?notId=${jlist.notid}&rlvId=${jlist.rlvid}&leaveId=${jlist.lcrid}&jId=${jlist.joinid}&addl=">Edit</a>
                                                </c:if>
                                                <c:if test="${jlist.isValidated eq 'Y'}">
                                                    <a href="viewJoiningData.htm?notId=${jlist.notid}&rlvId=${jlist.rlvid}&leaveId=${jlist.lcrid}&jId=${jlist.joinid}&addl=">View</a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach><br />
                        <tr>
                            <td colspan="11">
                                <span style="color: #F00000; font-weight: bold; font-size: 15px;">Red Color row indicates not to display in Service Book</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>