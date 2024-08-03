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
        <title>Property Statement</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>

        <!-- LAYOUT v 1.3.0 -->
        <link rel="stylesheet" type="text/css" href="../demo.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript"  src="js/common.js"></script>

        <style type="text/css">
            body {
                font-family: Verdana,sans-serif;
            }
        </style>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function() {
                $("#immovableBlankDiv").hide();
                $("#movableBlankDiv").hide();
            });
            var tyearlyPropId = "";
            var theMonths = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
            /*function newPropertyStatement() {
             var url = 'getEmployeePayProfile.htm';
             $.getJSON(url, function(data) {
             $("#payscale").textbox('setValue', data.payScale);
             $("#curbasicsalary").textbox('setValue', data.curbasicsalary);
             $("#fromdate").textbox('setValue', data.fromdate);
             $("#todate").textbox('setValue', data.todate);
             $('#newfinyeardlg').dialog('open');
             $('#prevyearProperty').combobox({
             url: 'GetPreviousFiscalYearPropertyStatementDataJSON.htm',
             valueField: 'fy',
             textField: 'fy',
             method: 'post'
             });
             });
             
             
             }*/
            function newPropertyStatement() {
                var url = 'getEmployeePayProfile.htm';
                $.getJSON(url, function(data) {
                    $("#payscale").textbox('setValue', data.payScale);
                    $("#curbasicsalary").textbox('setValue', data.basic);
                    $("#gradepay").textbox('setValue', data.gp);
                    $('#newfinyeardlg').dialog('open');
                    $('#prevyearProperty').combobox({
                        url: 'GetPreviousFiscalYearPropertyStatementDataJSON.htm',
                        valueField: 'fy',
                        textField: 'fy',
                        method: 'post'
                    });
                });


            }

            function editBasicInfo() {
                var row = $('#dgpropstmt').datagrid('getSelected');//newfinyeardlg
                if (row) {
                    if (row.statusid == 0) {
                        var url = 'getPropertyStmt.htm?yearlyPropId=' + row.yearlyPropId;
                        $.getJSON(url, function(data) {
                            if (data.isclosed == "N") {
                                $('#fm').form('clear');
                                $('#newfinyeardlg').dialog('open').dialog('center').dialog('setTitle', 'Edit Basic Information');
                                $('#fm').form('load', 'getPropertyStmt.htm?yearlyPropId=' + row.yearlyPropId);
                            } else if (data.isclosed == "Y") {
                                alert("Property Statement For this Financial Year is closed");
                            }
                        });
                    } else {
                        alert("Property Statement is Submitted, You Cannot be able to edit");
                    }
                } else {
                    alert("Please Choose a row");
                }
            }

            function savePropertyStatement() {
                var selectedfiscalyear = $("#fiscalyear").combobox('getValue');
                var splitedFiscalyear = selectedfiscalyear.split("-");

                var dtfinfromdate;
                var dtfintodate;
                if (parseInt(splitedFiscalyear[0]) < 2020) {
                    dtfinfromdate = new Date(splitedFiscalyear[0], 3, 1);
                    dtfintodate = new Date(parseInt(splitedFiscalyear[0]) + 1, 2, 31);
                } else if (parseInt(splitedFiscalyear[0]) == 2020) {
                    dtfinfromdate = new Date(splitedFiscalyear[0], 3, 1);
                    dtfintodate = new Date(splitedFiscalyear[0], 11, 31);
                } else if (parseInt(splitedFiscalyear[0]) > 2020) {
                    dtfinfromdate = new Date(splitedFiscalyear[0], 0, 1);
                    dtfintodate = new Date(splitedFiscalyear[0], 11, 31);
                }

                var fromdate = $("#fromdate").textbox('getValue');
                var todate = $("#todate").textbox('getValue');
                fDateStr = fromdate.split("-");
                var dtfromdate = new Date(fDateStr[2], ((theMonths.indexOf(fDateStr[1]))), fDateStr[0]);
                tDateStr = todate.split("-");
                var dttodate = new Date(tDateStr[2], ((theMonths.indexOf(tDateStr[1]))), tDateStr[0]);
                if (fromdate == "") {
                    alert("From Date Cannot be Empty");
                } else if (todate == "") {
                    alert("To Date Cannot be Empty");
                } else if (dtfromdate.getTime() != dtfinfromdate.getTime() && parseInt(splitedFiscalyear[0]) <= 2020) {
                    alert("From Date should 01-Apr-" + splitedFiscalyear[0]);
                } else if (dtfromdate.getTime() != dtfinfromdate.getTime() && parseInt(splitedFiscalyear[0]) > 2020) {
                    alert("From Date should 01-Jan-" + splitedFiscalyear[0]);
                } else if (dttodate.getTime() != dtfintodate.getTime() && parseInt(splitedFiscalyear[0]) >= 2020) {
                    alert("To Date should 31-Dec-" + splitedFiscalyear[0]);
                } else if (dttodate.getTime() != dtfintodate.getTime() && parseInt(splitedFiscalyear[0]) < 2020) {
                    alert("To Date should 31-Mar-" + "20" + splitedFiscalyear[1]);
                } else if (dtfromdate < dttodate) {
                    if ($("#yearlyPropId").val() > 0) {
                        $.post('updatePropertStatement.htm', $('#fm').serialize()).done(function(data) {
                            //$('#fm').form('clear');
                            $('#newfinyeardlg').dialog('close');
                            //$('#dgpropstmt').datagrid('reload');
                        });
                    } else {
                        $.post('savePropertStatement.htm', $('#fm').serialize()).done(function(data) {
                            if (data.msg == "Sucessfully Saved") {
                                alert(data.msg1);
                                alert(data.msg);
                                $('#newfinyeardlg').dialog('close'); // close the dialog
                                $('#dgpropstmt').datagrid('reload');
                            } else {
                                alert(data.msg);
                            }
                        });
                    }
                } else {
                    alert("Todate cannot be less than from date");
                }
            }

            function updatePropertyStatement() {
                var row = $('#dgpropstmt').datagrid('getSelected');
                $.post('updatePropertStatement.htm', $('#fmEditBasic').serialize())
                        .done(function(data) {
                            $('#fmEditBasic').form('clear');

                            $('#editfinyeardlg').dialog('close');
                        });


            }

            function editPropertyStatement() {
                var row = $('#dgpropstmt').datagrid('getSelected');
                if (row) {
                    if (row.statusid == 0) {
                        var url = 'getPropertyStmt.htm?yearlyPropId=' + row.yearlyPropId;
                        $.getJSON(url, function(data) {
                            if (data.submissiontype == "FS" && data.isclosed == "N") {
                                $('#propertyformsubmission').dialog('open');
                                tyearlyPropId = row.yearlyPropId;
                                $('#dgimpprt').datagrid('load', {yearlyPropId: row.yearlyPropId});
                                $('#dgmpprt').datagrid('load', {yearlyPropId: row.yearlyPropId});
                            } else if (data.submissiontype == "DS" && data.isclosed == "N") {
                                $('#propertydocsubmission').dialog('open');
                            } else if (data.isclosed == "Y") {
                                alert("Property Statement is closed");
                            }
                        });
                    } else {

                    }
                } else {
                    alert("Please choose a row");
                }
            }
            function deletePropertyStatement() {
                var conf = confirm("Are you sure to delete this Property? ")
                if (conf) {
                    var row = $('#dgpropstmt').datagrid('getSelected');
                    if (row) {
                        if (row.statusid == 0) {
                            var url = 'deletePropertyStmt.htm?yearlyPropId=' + row.yearlyPropId;
                            $.getJSON(url, function(data) {
                                $('#dgpropstmt').datagrid('reload');
                                if (data.isDeleted) {
                                    alert("Sucessfully Deleted");
                                }
                            });
                        } else {
                            alert("Cannot Deleted After Submission");
                        }
                    } else {
                        alert("Please choose a row");
                    }
                }
            }
            function viewPropertyStatement() {
                var row = $('#dgpropstmt').datagrid('getSelected');
                if (row) {
                    window.open('viewpropertystatement.htm?yearlyPropId=' + row.yearlyPropId);
                } else {
                    alert("Please choose a row");
                }
            }
            function downloadPropertyStatement() {
                var row = $('#dgpropstmt').datagrid('getSelected');
                if (row) {
                    window.open('viewPropertyStatementindividualLoginPDF.htm?yearlyPropId=' + row.yearlyPropId + '&fiscalyear='+row.fiscalyear);
                } else {
                    alert("Please choose a row");
                }
            }
            function formatSubmissionType(val, row) {
                if (val == "FS") {
                    return '<span style="color:blue;">Form Submission</span>';
                } else if (val == "DS") {
                    return '<span style="color:red;">Document Submission</span>';
                }
            }
            function formatStatus(val, row) {
                if (val == "1") {
                    return '<span style="color:green;"><strong>Submitted</strong></span>';
                } else if (val == "0") {
                    return '<span style="color:red;"><strong>Not Submitted</strong></span>';
                }
            }
            function newMovProperty() {
                $('#dlgmov').dialog('open');
                $('#mvpropertyform').form('clear');
                $('#movyearlyPropId').val(tyearlyPropId);
            }
            function newImmovProperty() {
                $('#dlgimv').dialog('open');
                $('#imvpropertyform').form('clear');
                $('#imvyearlyPropId').val(tyearlyPropId);
            }
            function saveImvProperty() {
                if ($('#imvpropertyform').form('validate')) {
                    $.post('saveImmovablePropertyDetail.htm', $('#imvpropertyform').serialize())
                            .done(function(data) {
                                $('#imvpropertyform').form('clear');
                                $('#dgimpprt').datagrid('reload');
                                $('#dgimpprt').datagrid('resize');
                                $('#dlgimv').dialog('close');
                            });
                }
            }
            function saveMvProperty() {
                if ($('#mvpropertyform').form('validate')) {
                    $.post('saveMovablePropertyDetail.htm', $('#mvpropertyform').serialize())
                            .done(function(data) {
                                $('#mvpropertyform').form('clear');
                                $('#dgmpprt').datagrid('reload');
                                $('#dgmpprt').datagrid('resize');
                                $('#dlgmov').dialog('close');
                            });
                }
            }
            function getImvPropertyDtl() {
                var row = $('#dgimpprt').datagrid('getSelected');
                if (row) {
                    $('#imvpropertyform').form('clear');
                    $('#imvyearlyPropId').val(tyearlyPropId);
                    $('#dlgimv').dialog('open').dialog('center').dialog('setTitle', 'Edit Immovable Property Detail');
                    $('#imvpropertyform').form('load', 'getImmovableProperty.htm?propertyDtlsId=' + row.propertyDtlsId);
                }
            }
            function deleteImmovProperty() {
                $.messager.confirm('My Property Statement', 'Are you sure to Delete?', function(r) {
                    if (r) {
                        var row = $('#dgimpprt').datagrid('getSelected');
                        if (row) {
                            $.post("deleteImmovableProperty.htm", {propertyDtlsId: row.propertyDtlsId}).done(function(data) {
                                $('#dgimpprt').datagrid('reload');
                            });

                        }
                    }
                });
            }
            function getMvPropertyDtl() {
                var row = $('#dgmpprt').datagrid('getSelected');
                if (row) {
                    $('#mvpropertyform').form('clear');
                    $('#movyearlyPropId').val(tyearlyPropId);
                    $('#dlgmov').dialog('open').dialog('center').dialog('setTitle', 'Edit Movable Property Detail');
                    $('#mvpropertyform').form('load', 'getMovableProperty.htm?propertyDtlsId=' + row.propertyDtlsId);
                    $('#mvpropertyform').form({
                        onLoadSuccess: function(data) {
                            $('#descofitemmv').textbox('setValue', data.descofothitem);
                        }
                    });
                }
            }
            function deleteMovProperty() {
                $.messager.confirm('My Property Statement', 'Are you sure to Delete?', function(r) {
                    if (r) {
                        var row = $('#dgmpprt').datagrid('getSelected');
                        if (row) {
                            $.post("deleteMovableProperty.htm", {propertyDtlsId: row.propertyDtlsId}).done(function(data) {
                                $('#dgmpprt').datagrid('reload');
                            });
                        }
                    }
                });
            }
            function confirmPropertyStatement() {
                $.messager.confirm('My Property Statement', 'Once the Property Is Submitted, It cannot be Editable?', function(r) {
                    var checkedValueImmovable = document.getElementById("isImmovableBlank");
                    var checkedValueMovable = document.getElementById("isMovableBlank");
                    if(checkedValueImmovable.checked == true) {
                        tIsImmovableBlank = 'Y';
                    } else {
                        tIsImmovableBlank = 'N';
                    }
                    if(checkedValueMovable.checked == true) {
                        tIsMovableBlank = 'Y';
                    } else {
                        tIsMovableBlank = 'N';
                    }
                    
                    if (r) {
                        $.post("submitPropertyStatement.htm", {yearlyPropId: tyearlyPropId, isImmovableBlank: tIsImmovableBlank, isMovableBlank: tIsMovableBlank}).done(function(data) {
                            if (data.isImmovableAvailable == 'N' && checkedValueImmovable.checked == false) {
                                alert("Please Click the Check box For Immovable Property Blank Submission");
                                $("#immovableBlankDiv").show();
                                $("#immovableBlankDiv").focus();

                            } else if (data.isMovableAvailable == 'N' && checkedValueMovable.checked == false) {
                                alert("Please Click the Check box For Movable Property Blank Submission");
                                $("#movableBlankDiv").show();
                                $("#movableBlankDiv").focus();
                            } 
                            if (data.isSubmitted == "S") {
                                $("#propertySubmitBtn").hide();
                                $('#dgpropstmt').datagrid('reload');
                                alert("Property Statement Submitted Sucessfully");
                                window.location = "viewpropertystatementlist.htm";
                            } else if (data.msg && data.msg != "") {
                                alert(data.msg);
                            }
                        });
                    }
                });
            }
            function showOtherTxtImv(record) {
                if (record.propertyId == 9) {
                    $('#descofitemimv').textbox('readonly', false);
                } else {
                    $('#descofitemimv').textbox('clear');
                    $('#descofitemimv').textbox('readonly', true);
                }
            }
            function showOtherRelationTxtImv(record) {
                if (record.ownerTypeId == 5 || record.ownerTypeId == 12) {
                    $('#otherPropertyOwnerimv').textbox('readonly', false);
                } else {
                    $('#otherPropertyOwnerimv').textbox('clear');
                    $('#otherPropertyOwnerimv').textbox('readonly', true);
                }
            }
            function showOtherRelationTxtmv(record) {

                if (record.ownerTypeId == 5 || record.ownerTypeId == 12) {
                    $('#otherPropertyOwnermv').textbox('readonly', false);
                } else {
                    $('#otherPropertyOwnermv').textbox('clear');
                    $('#otherPropertyOwnermv').textbox('readonly', true);
                }
            }
            function showOtherTxtmv(record) {
                if (record.propertyId == 10) {
                    $('#descofitemmv').textbox('readonly', false);
                } else {
                    $('#descofitemmv').textbox('clear');
                    $('#descofitemmv').textbox('readonly', false);
                }
            }
            function formatDate(val, row) {
                if (val) {
                    var splitedDate = val.split("-");
                    var monName = theMonths[parseInt(splitedDate[1]) - 1];
                    var formattedDate = splitedDate[2] + "-" + monName + "-" + splitedDate[0];
                    return formattedDate;
                } else {
                    return val;
                }
            }
            $(document).ready(function() {
                $("#propertyLocation").textbox('textbox').attr('maxlength', 99);
                $("#descofitemimv").textbox('textbox').attr('maxlength', 200);
                $("#otherPropertyOwnerimv").textbox('textbox').attr('maxlength', 200);
                $("#otherPropertyOwnermv").textbox('textbox').attr('maxlength', 200);
                $("#propertyArea").textbox('textbox').attr('maxlength', 9);
                $("#propertyNature").textbox('textbox').attr('maxlength', 50);
                $("#interest").textbox('textbox').attr('maxlength', 100);
                $("#propertyValue").textbox('textbox').attr('maxlength', 10);
                $("#manner").textbox('textbox').attr('maxlength', 99);
                $("#remark").textbox('textbox').attr('maxlength', 600);




            });
        </script>
    </head>
    <body>        
        <div style="margin:20px 0;"></div>

        <table class="easyui-datagrid" title="Property Statement" style="width:100%;height:450px" id="dgpropstmt"
               data-options="singleSelect:true,url:'getPropertStatementList.htm',method:'post'">
            <thead>
                <tr>
                    <th data-options="field:'slNo'" width="5%">Sl No</th>
                    <th data-options="field:'fiscalyear'" width="15%">Fiscal Year</th>
                    <th data-options="field:'fromdate',formatter:formatDate" width="15%">From date</th>   
                    <th data-options="field:'todate',formatter:formatDate" width="15%">To date</th>
                    <th data-options="field:'submissiontype',formatter:formatSubmissionType" width="15%">Submission Type</th>
                    <th data-options="field:'statusid',align:'center',formatter:formatStatus" width="15%">Status</th>
                </tr>
            </thead>
        </table>
        <div style="padding:5px 5px;" class="panel-header">
            <a href="javascript:newPropertyStatement()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="background: green;color:white">Add New</a>
            <a href="javascript:editBasicInfo()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" style="background: #008900;color:white">Edit Basic Information</a>
            <a href="javascript:editPropertyStatement()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">Edit/View</a>
            <a href="javascript:deletePropertyStatement()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" style="background: red;color:white">Delete</a>
            <a href="javascript:viewPropertyStatement()" class="easyui-linkbutton">View</a>
            <a href="javascript:downloadPropertyStatement()" class="easyui-linkbutton"  style="background: blue;color:white"> Download</a>
        </div>
        <div style="padding:5px 5px;">
            <span style="font-style: bold;color:red; font-size:24px;"><strong>NB:(To Change The Basic Information Click on Edit Basic Information Button)</strong></span>
        </div>

        <div id="newfinyeardlg" class="easyui-dialog" title="Employee Information" data-options="iconCls:'icon-save',closed: true,modal: true, buttons: [{
             text:'Ok',
             iconCls:'icon-ok',
             handler:function(){
             savePropertyStatement();
             }
             },{
             text:'Cancel',
             handler:function(){
             $('#newfinyeardlg').dialog('close');
             }
             }]" style="width:700px;height:400px;padding:10px">
            <form id="fm" method="post" novalidate style="margin:0;padding:10px 50px">
                <input type="hidden" id="yearlyPropId" name="yearlyPropId"/>
                <div style="margin-bottom:10px;font-size:14px;border-bottom:1px solid #ccc">Financial Year Information</div>                
                <div style="margin-bottom:10px">
                    <select name="fiscalyear" id="fiscalyear" class="easyui-combobox" data-options="editable: false,valueField:'fy',textField:'fy',url:'GetPFiscalYearListJSON.htm',method:'post'" required="true" style="width:80%;">                      

                    </select>                               
                </div>
                <div style="margin-bottom:10px">
                    <input name="payscale" id="payscale" class="easyui-textbox" required="true" label="Pay Scale: " style="width:80%" value="${propertyStatement.payscale}">
                </div>
                <div style="margin-bottom:10px">
                    <input name="curbasicsalary" id="curbasicsalary" class="easyui-textbox" required="true" label="Basic: " style="width:80%" value="${propertyStatement.curbasicsalary}">
                </div>
                <div style="margin-bottom:10px">
                    <input name="gradepay" id="gradepay" class="easyui-textbox" required="true" label="Grade Pay: " style="width:80%" value="${propertyStatement.gradepay}">
                </div>
                <div style="margin-bottom:10px">
                    <input name="fromdate" id="fromdate" class="easyui-datebox" data-options="label:'From :',required:true,formatter:myformatter,parser:myparser" editable="false" style="width:80%" value="${propertyStatement.fromdate}">
                </div>
                <div style="margin-bottom:10px">
                    <input name="todate" id="todate" class="easyui-datebox" data-options="label:'To :',required:true,formatter:myformatter,parser:myparser" editable="false" style="width:80%" value="${propertyStatement.todate}">
                </div>
                <div style="margin-bottom:10px">
                    <select name="submissiontype" class="easyui-combobox"  label="Choose Type of Submission: " style="width:80%" data-options="editable:false">
                        <option value="FS">Form Submission</option> 
                    </select>
                </div>
                <div style="margin-bottom:10px">
                    <select name="prevyearProperty" id="prevyearProperty" class="easyui-combobox" label="Copy from Previous Year: " style="width:80%">

                    </select>
                </div>
            </form>
        </div> 



        <div id="propertyformsubmission" class="easyui-dialog" title="Form Submission" data-options="iconCls:'icon-save',closed: true,modal: true">
            <table class="easyui-datagrid" title="IMMOVABLE PROPERTY" style="width:100%;height:250px" id="dgimpprt" toolbar="#toolbarimv" data-options="singleSelect:true,url:'getImmovablePropertyDetailList.htm',method:'post',queryParams: {yearlyPropId: '0'}">
                <thead>
                    <tr>
                        <th data-options="field:'slno'" width="5%">Sl No</th>
                        <th data-options="field:'propertyName'" width="15%">Property Name</th>
                        <th data-options="field:'descofothitem'" width="15%">Description of Item</th>   
                        <th data-options="field:'propertyLocation'" width="15%">Precise Location</th>
                        <th data-options="field:'propertyValue',align:'center'" width="15%">Value</th>
                        <th data-options="field:'dateOfAcq',align:'center',formatter:formatDate" width="15%">Date of Acquisition or Disposal</th>
                        <th data-options="field:'remark',align:'center'" width="15%">Remarks</th>
                    </tr>
                </thead>
            </table>
            <div id="immovableBlankDiv">
                <input type="checkbox" class="easyui-checkbox" name="isImmovableBlank" id="isImmovableBlank" value="Yes" data-options="required:true">
                <span style="font-style: bold;color:red; font-size:15px;"><strong>I Agree that I have submit Blank Property Statement For Immovable Property</strong></span>

            </div>
            <div id="toolbarimv">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newImmovProperty()">New Property</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="getImvPropertyDtl()">Edit Property</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteImmovProperty()" style="background: red;color:white">Remove Property</a>
            </div>

            <table class="easyui-datagrid" title="MOVABLE PROPERTY" style="width:100%;height:250px" id="dgmpprt" toolbar="#toolbarmv" data-options="singleSelect:true,url:'getMovablePropertyDetailList.htm',method:'post',queryParams: {yearlyPropId: '0'}">
                <thead>
                    <tr>                        
                        <th data-options="field:'slno'" width="5%">Sl No</th>
                        <th data-options="field:'propertyName'" width="15%">Property Name</th>
                        <th data-options="field:'descofothitem'" width="15%">Description of Item</th>
                        <th data-options="field:'propertyValue',align:'center'" width="15%">Value</th>
                        <th data-options="field:'dateOfAcq',align:'center',formatter:formatDate" width="15%">Date of Acquisition or Disposal</th>
                        <th data-options="field:'loan',align:'center'" width="10%">Loan</th>
                        <th data-options="field:'remark',align:'center'" width="15%">Remarks</th>
                    </tr>
                </thead>
            </table>
            <div id="movableBlankDiv">
                <input type="checkbox" class="easyui-checkbox" id="isMovableBlank" name="isMovableBlank" value="Yes">
                <span style="font-style: bold;color:red; font-size:15px;"><strong>I Agree that I have submit Blank Property Statement For Movable Property</strong></span>

            </div>
            <div id="toolbarmv">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newMovProperty()">New Property</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="getMvPropertyDtl()">Edit Property</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteMovProperty()" style="background: red;color:white">Remove Property</a>
            </div>
            <div style="padding:5px 5px;" class="panel-footer">   
                <a href="javascript:confirmPropertyStatement()" class="easyui-linkbutton" id="propertySubmitBtn" data-options="iconCls:'icon-ok'" style="background: green;color:white">
                    <strong>Submit</strong>
                </a><span style="font-style: bold;color:red; font-size:24px;"><strong>(Once the Property Is Submitted, It cannot be Editable)</strong></span>

            </div>
        </div>
        <div id="propertydocsubmission" class="easyui-dialog" title="Document Submission" data-options="iconCls:'icon-save',closed: true,modal: true" style="width:600px;">
            <form id="fmupload" method="post" novalidate style="margin:0;padding:10px 50px" enctype="multipart/form-data">
                <div style="margin-bottom:20px">
                    <input class="easyui-filebox" label="File :" labelPosition="top" data-options="prompt:'Choose a file...'" style="width:100%">
                </div>
                <div>
                    <a href="#" class="easyui-linkbutton">Upload</a>
                </div>
            </form>
        </div>
        <div id="dlgmov" class="easyui-dialog" style="width:800px;height:450px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
            <div style="margin-bottom:10px;font-size:14px;border-bottom:1px solid #ccc">Property Information</div>
            <form id="mvpropertyform">
                <input type="hidden"  name="propertyDtlsId" id="propertyDtlsId2" />  
                <input type="hidden" id="movyearlyPropId" name="yearlyPropId"/>
                <div style="margin-bottom:10px">                    
                    <input id="propertyTypeId" class="easyui-combobox" name="propertyTypeId" data-options="label:'Description of Item:',required:true,labelWidth:'200px',editable: false,valueField:'propertyId',textField:'propertyName',url:'getPropertyMaster.htm?propertyTypeId=1',onSelect:function(record){showOtherTxtmv(record);}" style="width:80%">
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="descofothitem" id="descofitemmv" class="easyui-textbox" label="Description of items:" data-options="readonly:false" labelWidth="200px" style="width:80%">
                </div>
                <div style="margin-bottom:10px">                    
                    <input name="propertyValue" id="propertyValue" class="easyui-numberbox" label="Value: " data-options="required:true" labelWidth="200px"  style="width:80%">                                
                </div>
                <div style="margin-bottom:10px">                    
                    <input name="loan" class="easyui-numberbox" label="Loan Amount: " labelWidth="200px" style="width:80%">                                
                </div>
                <div style="margin-bottom:10px;">                    
                    <input id="propertyOwner" class="easyui-combobox" name="propertyOwner" data-options="label:'Whose Name the Asset is or was:',required:true,labelWidth:'200px',valueField:'ownerTypeId',textField:'ownerTypeName',url:'getPropertyOwnerList.htm',onSelect:function(record){showOtherRelationTxtmv(record);}" style="width:80%">                    
                </div>
                <div style="margin-bottom:10px;"> 
                    <input id="otherPropertyOwnermv" class="easyui-textbox" name="otherPropertyOwner" data-options="prompt:'If Other Mention',label:' ',labelWidth:'200px',readonly:true"  style="width:80%;margin-top: 5px;">
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="dateOfAcq" class="easyui-datebox" data-options="label:'Date of Acquisition or Disposal:',labelWidth:'200px',formatter:myformatter,parser:myparser" style="width:80%">
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="manner" class="easyui-textbox" label="Manner of Acquisition or Disposal:" labelWidth="200px" style="width:80%">                                
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="remark" class="easyui-textbox" label="Remarks:" labelWidth="200px" style="width:80%">                                
                </div>
            </form>
        </div>
        <div id="dlg-buttons">
            <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveMvProperty()" style="width:90px">Save</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgmov').dialog('close')" style="width:90px">Cancel</a>
        </div>
        <!-- Immovable Property Detail Form Start-->
        <div id="dlgimv" class="easyui-dialog" style="width:700px;padding:10px 20px" closed="true" buttons="#dlg-buttonsImv">
            <div class="ftitle">Property Information</div>
            <form id="imvpropertyform">
                <input  type="hidden" hidden="propertyDtlsId"  name="propertyDtlsId" id="propertyDtlsId" /> 
                <div style="margin-bottom:10px;">
                    <input type="hidden" id="imvyearlyPropId" name="yearlyPropId"/>
                    <input id="propertyTypeId" class="easyui-combobox" name="propertyTypeId" data-options="label:'Description of Item:',labelWidth:'200px',required:true,editable: false,valueField:'propertyId',textField:'propertyName',url:'getPropertyMaster.htm?propertyTypeId=2',onSelect:function(record){showOtherTxtImv(record);}" style="width:80%">
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="descofothitem" id="descofitemimv" class="easyui-textbox" label="Description of items:" data-options="readonly:true" labelWidth="200px" style="width:80%">
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="propertyLocation" type="text" id="propertyLocation" class="easyui-textbox" label="Precise Location:" labelWidth="200px" style="width:80%" >
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="propertyArea" id="propertyArea"  class="easyui-numberbox" label="Area:" data-options="min:0,precision:3" labelWidth="200px" style="width:60%">
                    <select class="easyui-combobox" name="areaunit" id="areaunit" style="width:20%;">
                        <option selected="selected" value="Sqr. ft">Sqr. ft</option>
                        <option value="Sqr. Meter">Sqr. Meter</option>
                        <option value="Acre">Acre</option>
                        <option value="Decimal">Decimal</option>
                    </select>
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="propertyNature" id="propertyNature" class="easyui-textbox" label="Nature of Land:" labelWidth="200px"  style="width:80%">
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="interest" id="interest" class="easyui-textbox" label="Extent of Interest:" labelWidth="200px" style="width:80%">                                
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="propertyValue" id="propertyValue"  class="easyui-numberbox" label="Value:" labelWidth="200px" data-options="required:true" style="width:80%">                                
                </div>
                <div style="margin-bottom:10px;">                    
                    <input id="propertyOwner" class="easyui-combobox" name="propertyOwner" data-options="required:true,label:'Whose Name the Asset is or was:',labelWidth:'200px',valueField:'ownerTypeId',textField:'ownerTypeName',url:'getPropertyOwnerList.htm',onSelect:function(record){showOtherRelationTxtImv(record);}" style="width:80%">
                </div>
                <div style="margin-bottom:10px;"> 
                    <input id="otherPropertyOwnerimv" class="easyui-textbox" name="otherPropertyOwner" data-options="prompt:'If Other Mention',label:' ',labelWidth:'200px',readonly:true"  style="width:80%;margin-top: 5px;">
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="dateOfAcq" class="easyui-datebox"  data-options="label:'Date of Acquisition or Disposal:',labelWidth:'200px',formatter:myformatter,parser:myparser" style="width:80%">                                
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="manner" id="manner" class="easyui-textbox" label="Manner of Acquisition or Disposal:" labelWidth="200px" style="width:80%">                                
                </div>
                <div style="margin-bottom:10px;">                    
                    <input name="remark" id="remark" class="easyui-textbox" label="Remarks:" labelWidth="200px" style="width:80%">                                
                </div>
            </form>
        </div>
        <div id="dlg-buttonsImv">
            <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveImvProperty()" style="width:90px">Save</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgimv').dialog('close')" style="width:90px">Cancel</a>
        </div>
        <!-- Immovable Property Detail Form End-->
    </body>
</html>
