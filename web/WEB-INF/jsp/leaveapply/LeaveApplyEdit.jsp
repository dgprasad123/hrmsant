<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<% String url = "";
    String myempId = "";
    String attachId = "";
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
    int i = 0;
%>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Leave Apply</title>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />          
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/color.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <script language="javascript" src="js/servicehistory.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script language="javascript" type="text/javascript" >
            var incr = 1;
//            function UploadFile() {
//                var url = 'fileUploadForm.htm';
//                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%",overlayClose:false,onClosed:refreshImage});
//            }
            function searchAuthority() {
                var url = 'leaveauthority.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "450px", top: "10px", overlayClose: false});
            }
            function searchIssuingAuthority() {
                var url = 'leaveissuingauthority.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "450px", top: "10px", overlayClose: false});
            }

            function applyFor() {
                var url = 'leaveauthority1.htm?id=1';
                $.colorbox({href: url, iframe: true, open: true, width: "50%", height: "50%", overlayClose: false});
            }

            function UploadFile() {
                var url = 'UploadDocumentAction.htm?KeepThis=true&TB_iframe=true&height=375&width=452&scrollwin=true';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false});
            }
            function actionEvent(rec) {
                // var selectedLeave = $('#sltleaveType').combobox('getValue');
                if ((rec == "ML") || (rec == "PL")) {
                    $('#sltChild').combo('readonly', false);
                } else {
                    $('#sltChild').combo('readonly', true);
                }
                if ((rec == "HDL")) {
                    $('#sltPartofday').combo('readonly', false);

                } else {
                    $('#sltPartofday').combo('readonly', true);
                }
            }
            $(document).ready(function () {
                // $('#txtIssuingAuthority').textbox('setText', "SUBHASIS RATH   , SENIOR HR EXECUTIVE");
                if ($('#offCode').val() == 'OLSGAD0010001') {
                    $('#txtIssuingAuthority').textbox('setValue', "SUBHASIS RATH   , SENIOR HR EXECUTIVE");
                    $('#hidIssuingAuthEmpId').val("97000029");
                    $('#hidIssuingSpcAuthCode').val("OLSGAD00100011104180001");
                }
                //$('#txtIssuingAuthority').textbox('SUBHASIS RATH   , SENIOR HR EXECUTIVE');
                $('#sltleaveType').combobox({
                    editable: false
                });
                $('#applySearch').hide();
                $('#sltChild').combo('readonly', true);
                $('#sltPartofday').combo('readonly', true);
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
                document.getElementById('txtprefixFrom').disabled = true;
                document.getElementById('txtprefixTo').disabled = true;
                document.getElementById('txtsuffixFrom').disabled = true;
                document.getElementById('txtsuffixTo').disabled = true;

            });


            function removeTempAttachment(fileId, divId) {
                $("#" + divId).remove();
                $.ajax({
                    type: 'POST',
                    url: 'removeuploadfile.htm?tempAttachment=' + fileId,
                    success: function (response) {
                        $("#" + divId).remove();
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
            function closeWindow() {
                if (confirm('Are you sure want to  Exit?'))
                    parent.dhxWins.window("w1").close();
            }

            function applycheck()
            {
                var sanctionauth = document.getElementById("txtSancAuthority");
                if (sanctionauth.value == "")
                {
                    alert("Please enter Leave Sanctioning Authority");
                    sanctionauth.focus();
                    return false;
                }
                var issuingauth = document.getElementById("txtIssuingAuthority");
                if (issuingauth.value == "")
                {
                    alert("Please enter Leave Issuing Authority");
                    issuingauth.focus();
                    return false;
                }
                var selectedLeave = $('#sltleaveType').combobox('getValue');
                if (selectedLeave == "")
                {
                    alert("Please Select Type of Leave");
                    return false;
                }
                var fromdate = document.getElementById("txtperiodFrom");
                if (fromdate.value == "")
                {
                    alert("Please enter from date.");
                    fromdate.focus();
                    return false;
                }

                var todate = document.getElementById("txtperiodTo");
                if (todate.value == "")
                {
                    alert("Please enter to date.");
                    todate.focus();
                    return false;
                }
                var sltleaveType = document.getElementById("sltleaveType");
                if (sltleaveType.value == "HDL") {
                    var ret = chksamedate(fromdate, todate);
                    if (ret == false) {
                        return false;
                    }
                }

                var ret = chkdate(fromdate, todate, "From date ", "To date");
                if (ret == false) {
                    return false;
                }
                var prefixFrom = document.getElementById("txtprefixFrom");
                var prefixTo = document.getElementById("txtprefixTo");
                var chkhqper = document.getElementById("chkprefix");
                if (chkhqper.checked == true) {
                    if (prefixFrom.value == "")
                    {
                        alert("Please enter Prefix from date.");
                        prefixFrom.focus();
                        return false;
                    }
                    if (prefixTo.value == "")
                    {
                        alert("Please enter Prefix To date.");
                        prefixTo.focus();
                        return false;
                    }
                }


                var ret1 = chkdate(prefixFrom, prefixTo, "Prefix From Date ", "Prefix To Date");
                if (ret1 == false) {
                    return false;
                }
                var ret3 = chkdate(prefixFrom, fromdate, "Prefix From Date ", "Leave Apply from date");
                if (ret3 == false) {
                    return false;
                }

                var sufixFrom = document.getElementById("txtsuffixFrom");
                var sufixTo = document.getElementById("txtsuffixTo");
                var chkhqper = document.getElementById("chksuffix");
                if (chkhqper.checked == true) {
                    if (sufixFrom.value == "")
                    {
                        alert("Please enter Suffix from date.");
                        sufixFrom.focus();
                        return false;
                    }
                    if (sufixTo.value == "")
                    {
                        alert("Please enter Suffix To date.");
                        sufixTo.focus();
                        return false;
                    }
                }
                var ret2 = chkdate(sufixFrom, sufixTo, "Sufix From Date ", "Sufix To Date");
                if (ret2 == false) {
                    return false;
                }

                var ret4 = chkdate(todate, sufixFrom, "Suffix From Date ", "Leave Apply To date");
                if (ret4 == false) {
                    return false;
                }

                for (i = 0; i < document.getElementsByName('hqperrad').length; i++) {
                    if (document.getElementsByName('hqperrad')[i].checked) {
                        if (document.getElementsByName('hqperrad')[i].value == "Y") {
                            var address = document.getElementById("txtconaddress");
                            var phoneNo = document.getElementById("txtphoneNo");
                            if (address.value == "")
                            {
                                alert("Please enter Contact Address");
                                address.focus();
                                return false;
                            }
                            if (phoneNo.value == "")
                            {
                                alert("Please enter Contact Phone No");
                                phoneNo.focus();
                                return false;
                            }
                            if (isNaN(phoneNo.value))
                            {
                                alert("Enter the valid Mobile Number");
                                return false;
                            }

                            if (phoneNo.value != '') {
                                if (phoneNo.value.length != 10)
                                {
                                    alert("Please enter valid Mobile number");
                                    return false;
                                }
                            }
                        }

                    }

                }


//                if (selectedLeave == "CL")
//                {
//                    var date1 = convertDate(fromdate.value);
//                    var date2 = convertDate(todate.value);
//                    var d1 = new Date(date1);
//                    var d2 = new Date(date2);
//                    var timeDiff = d2.getTime() - d1.getTime();
//                    var daysDiffCl = (timeDiff / (1000 * 3600 * 24)) + 1;
//
//
//                    if (daysDiffCl > 10) {
//                        alert("The Casual leave period can not be more than 10 days");
//                        return false;
//                    } else {
//                        return true;
//                    }
//
//                }
//                if (selectedLeave == "EL")
//                {
//                    var date1 = convertDate(fromdate.value);
//                    var date2 = convertDate(todate.value);
//                    var d1 = new Date(date1);
//                    var d2 = new Date(date2);
//                    var timeDiff = d2.getTime() - d1.getTime();
//                    var daysDiffEl = (timeDiff / (1000 * 3600 * 24)) + 1;
//                    if (daysDiffEl > 150) {
//                        alert("The Earned leave period can not be more than 150 days");
//                        return false;
//                    } else {
//                        return true;
//                    }
//
//                }




            }
            function convertDate(str) {
                var finaldate;
                var date = str.split("-");
                var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                for (var j = 0; j < months.length; j++) {
                    if (date[1] == months[j]) {
                        date[1] = months.indexOf(months[j]) + 1;
                        finaldate = date[1] + "/" + date[0] + "/" + date[2];
                    }
                }
                return finaldate;
            }
            function enablePrefixInputFields()
            {
                var chkhqper = document.getElementById("chkprefix");
                if (chkhqper.checked == true) {
                    document.getElementById('txtprefixFrom').disabled = false
                    document.getElementById('txtprefixTo').disabled = false
                } else {
                    document.getElementById('txtprefixFrom').disabled = true;
                    document.getElementById('txtprefixTo').disabled = true;
                }
            }
            function enableSuffixInputFields() {
                var chkhqper = document.getElementById("chksuffix");
                if (chkhqper.checked == true) {
                    document.getElementById("txtsuffixFrom").disabled = false;
                    document.getElementById("txtsuffixTo").disabled = false;
                } else {
                    document.getElementById('txtsuffixFrom').disabled = true;
                    document.getElementById('txtsuffixTo').disabled = true;
                }
            }


            function SelectSpn(empId, empName, desig, spc)
            {
                $.colorbox.close();
                $('#txtSancAuthority').textbox('setValue', empName + "," + desig);
                $('#hidAuthEmpId').val(empId);
                $('#hidSpcAuthCode').val(spc);
            }
            function SelectIssuingSpn(empId, empName, desig, spc)
            {
                $.colorbox.close();
                $('#txtIssuingAuthority').textbox('setValue', empName + "," + desig);
                $('#hidIssuingAuthEmpId').val(empId);
                $('#hidIssuingSpcAuthCode').val(spc);
            }
            function SelectEmp(empId, empName, desig, spc)
            {
                $.colorbox.close();
                $('#txtApplyFor').textbox('setValue', empName + "," + desig);
                $('#hidempId').val(empId);
                $('#hidSpcCode').val(spc);
            }
            function enableInputFields() {
                var chkhqper = document.getElementById("chkhqper");
                if (chkhqper.checked == true) {
                    document.getElementById("txtphoneNo").disabled = false;
                    document.getElementById("txtconaddress").disabled = false;
                } else {
                    document.getElementById("txtphoneNo").disabled = true;
                    document.getElementById("txtconaddress").disabled = true;
                }
            }
            function enableControlHPL() {
                var ifcommuted = document.getElementById("ifcommuted");
                var sltleaveType = document.getElementById("sltleaveType");
                if (sltleaveType.value == "HPL") {
                    ifcommuted.disabled = false;
                } else {
                    ifcommuted.checked = false;
                    ifcommuted.disabled = true;
                }
            }


            //            function setDivHeightLeaveAppE(divId) {
//                var containertbl = document.getElementById(divId);
//                if ((screen.width == 1024) && (screen.height == 768))
//                {
//
//                    containertbl.style.height = "500px";
//                }
//                else if ((screen.width == 800) && (screen.height == 600))
//                {
//
//                    containertbl.style.height = "500px";
//
//                }
//                else if ((screen.width == 1280) && (screen.height == 1024))
//                {
//
//                    containertbl.style.height = "500px";
//                } else {
//                    containertbl.style.height = "500px";
//                }
//
//            }
            function radioOption(me, clearoption) {
                radval = me.value;
                if (radval == "Y") {
                    document.getElementById("txtconaddress").disabled = false
                    document.getElementById("txtphoneNo").disabled = false
                } else {
                    document.getElementById("txtconaddress").disabled = true
                    document.getElementById("txtphoneNo").disabled = true
                }
            }
            function radioApplyOption(me, clearoption) {
                radval = me.value;
                if (radval == "Y") {
                    $('#applySearch').hide();
                    $('#txtApplyFor').textbox('setValue', '');
                } else {
                    $('#applySearch').show();
                    document.getElementById("applySearch").disabled = true;
                }
            }

        </script>
        <style type="text/html">
            .watermark {
                opacity: 0.5;
                color: BLACK;
                position: fixed;
                top: auto;
                left: 80%;
            }
        </style>
        <base href="${initParam['BaseURLPath']}" />  
    </head>

    <body >
        <form:form   action="leaveapplyedit.htm" method="POST" commandName="leaveForm">

            <div align="center">

                <div style="width:99%;">
                    <div  class="easyui-panel" title="Apply New Leave"  style="width:100%;overflow: auto;">
                        <input type="hidden" name="hidempId"  id="hidempId" value="${leaveForm.hidempId}"/>
                        <input type="hidden" name="applyFor"  id="applyFor" value="${leaveForm.applyFor}"/>
                        <input type="hidden" name="leaveId" id="leaveId" value="${leaveForm.leaveId}"/>
                        <input type="hidden" name="hidSpcCode" value="${leaveForm.hidSpcCode}" id="hidSpcCode"/>
                        <input type="hidden" name="hidAuthEmpId" id="hidAuthEmpId"/>
                        <input type="hidden" name="hidSpcAuthCode" id="hidSpcAuthCode"/>
                        <input type="hidden" name="hidIssuingAuthEmpId" id="hidIssuingAuthEmpId"/>
                        <input type="hidden" name="hidIssuingSpcAuthCode" id="hidIssuingSpcAuthCode"/>
                        <input type="hidden" name="offCode" id="offCode" value="${leaveForm.offCode}"/>

                        <table  cellpadding="5" style="font-size:12px; font-family:verdana;">
                            <c:if test="${not empty errors}">
                                <span style="color:red;"><b><c:out value="${errors}"/></b></span>
                                    </c:if>
                                    <c:if test="${not empty errors1}">
                                <span style="color:red;"><b><c:out value="${errors1}"/></b></span>
                                    </c:if>
                                    <c:if test="${not empty errors2}">
                                <span style="color:red;"><b><c:out value="${errors2}"/></b></span>
                                    </c:if>
                                    <c:if test = "${leaveForm.leaveId != null}">
                                <tr>
                                    <td align="center" width="10%"> <%=++i%>.</td>
                                    <td width="36%">Previous Leave Period</td>
                                    <td width="12%">a) From Date</td>
                                    <td id="innerdata" width="18%">  
                                        <c:out value="${leaveForm.txtperiodFrom}"/>
                                    </td>
                                    <td align="left"  width="10%">
                                        b) To Date
                                    </td>
                                    <td id="innerdata"  width="24%">   
                                        <c:out value="${leaveForm.txtperiodTo}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center"> <%=++i%>. </td>
                                    <td >Previous Leave Type:</td>
                                    <td colspan="4"><c:out value="${leaveForm.sltleaveType}"/></td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty hidempId}">
                                <tr style="height: 20px" >
                                    <td align="center" valign="top">.</td>
                                    <td  id="innerdata" align="left">                               
                                        Applying For: </td>
                                    <!--<td>
                                       
                                        <input type="radio" class="easyui-radio" checked="checked" name="radApply" id="selfApply"  value="Y"   onclick="radioApplyOption(this)"/>Self
                                    </td>
                                    <td align="left"><input type="radio" class="easyui-radio" name="radApply" id="othApply" value="N" onclick="radioApplyOption(this)"/>Other</td>-->
                                    <td align="left"  colspan="2"> <b><c:out  value="${leaveForm.applicantName}"/></b>&nbsp;</td>
                                </tr>
                            </c:if>
                            <!--<tr>
                              <td align="center"> </td>
                                <td >Applying For Other:</td>
                                <td colspan="4"><input class="easyui-textbox" id="txtApplyFor" type="text" name="txtApplyFor" style="width:50%;height:25px"    readonly="true"></input>
                                    <a href="javascript:void(applyFor())"  id="applySearch" class="easyui-linkbutton"> Search </a>

                                </td>
                            </tr>-->
                            <tr>
                                <td align="center"><c:out value="${empId}"/> <%=++i%>. </td>
                                <td >You are applying for your leave to :<span style="color: red">*</span></td>
                                <td colspan="4">
                                    <input class="easyui-textbox"  id="txtSancAuthority" type="text" name="txtSancAuthority" style="width:50%;height:25px"    readonly="true"></input>
                                    <a href="javascript:void(searchAuthority())" class="easyui-linkbutton"> Search </a>


                                </td>
                            </tr>
                            <tr>
                                <td align="center"><%=++i%>. </td>
                                <td >Issuing Authority/<br>Establishment Section Authority  :<span style="color: red">*</span></td>
                                <td colspan="4">
                                    <input class="easyui-textbox"  id="txtIssuingAuthority" type="text" name="txtIssuingAuthority" style="width:50%;height:25px"    readonly="true"></input>
                                    <c:if test = "${leaveForm.offCode != 'OLSGAD0010001'}">
                                        <a href="javascript:void(searchIssuingAuthority())" class="easyui-linkbutton"> Search </a>
                                    </c:if>

                                </td>
                            </tr>
                            <tr>
                                <td align="center"> <%=++i%>. </td>
                                <td >Type of Leave:<span style="color: red">*</span></td>
                                <td colspan="4"><input class="easyui-combobox"  id="sltleaveType" name="sltleaveType" data-options="valueField:'tolid',textField:'tol',url:'getLeaveType.htm',onSelect: function(rec){actionEvent(rec.tolid);}" style="width:300px;height:25px"></td>
                            </tr>
                            <tr>
                                <td align="center"> <%=++i%>. </td>
                                <td >If half day leave: (FORENOON/AFTERNOON)</td>
                                <td colspan="4">
                                    <select id="sltPartofday" class="easyui-combobox" name="sltPartofday" style="width:20%;">
                                        <option value=""></option>
                                        <option value="FN">FORENOON</option>
                                        <option value="AN">AFTERNOON</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="center"> <%=++i%>. </td>
                                <td >Surviving children If(Maternity/Paternity) Leave:</td>
                                <td colspan="4">
                                    <select id="sltChild" class="easyui-combobox" name="sltChild" label="No of Child" labelPosition="top" style="width:20%;">
                                        <option value=""></option>
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" width="10%"> <%=++i%>.</td>
                                <td width="36%">Duration of Leave you want to avail</td>
                                <td width="12%">a) From Date<span style="color: red">*</span></td>
                                <td id="innerdata" width="18%">  

                                    <input id="txtperiodFrom" name="txtperiodFrom"   readonly="true"  style="width:80px;" class="txtDate"  />
                                </td>
                                <td align="left"  width="10%">
                                    b) To Date<span style="color: red">*</span>
                                </td>
                                <td id="innerdata"  width="24%">   
                                    <input id="txtperiodTo" name="txtperiodTo"  readonly="true"  style="width:80px;" class="txtDate"  />
                                </td>
                            </tr>

                            <tr>
                                <td align="center"> <%=++i%>.</td>
                                <td>Public Holidays prior to leave period to be applied as Prefix</td>
                                <td align="left"><input type="checkbox" id="chkprefix"  name="chkprefix" class="easyui-checkbox" onclick="enablePrefixInputFields()"></td>
                                <td align="left">&nbsp;</td>
                                <td align="left"  >&nbsp;</td>
                                <td id="innerdata"  >&nbsp;</td>
                            </tr>
                            <tr style="height: 30px" >                               
                                <td align="center" >&nbsp;</td>                                
                                <td>&nbsp;</td>
                                <td>a) From Date</td>
                                <td id="innerdata" >   
                                    <input id="txtprefixFrom" name="txtprefixFrom"  readonly="true" name="txtprefixFrom"  style="width:80px;" class="txtDate"  />
                                </td>
                                <td> b) To Date </td>
                                <td id="innerdata" >  
                                    <input id="txtprefixTo" name="txtprefixTo"   readonly="true" name="txtprefixTo"  style="width:80px;" class="txtDate"  />
                                </td>
                            </tr>
                            <tr>
                                <td align="center"> <%=++i%>.</td>
                                <td >                                
                                    Public Holidays after leave period to be applied as Suffix</td>
                                <td align="left"  >
                                    <input type="checkbox" id="chksuffix"  name="chksuffix" class="easyui-checkbox" onclick="enableSuffixInputFields()">                                                               
                                </td>
                                <td align="left">&nbsp;</td>
                                <td align="left"  >&nbsp;</td>
                                <td id="innerdata" >&nbsp;</td>
                            </tr>
                            <tr style="height: 30px">
                                <td align="center" >&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>a) From Date</td>
                                <td id="innerdata">
                                    <input id="txtsuffixFrom"  readonly="true" name="txtsuffixFrom" styleId="txtsuffixFrom"  style="width:80px;" class="txtDate"  />
                                </td>
                                <td >b) To Date</td>
                                <td id="innerdata">
                                    <input id="txtsuffixTo" readonly="true"  name="txtsuffixTo" styleId="txtsuffixTo"  style="width:80px;" class="txtDate"  />
                                </td>
                            </tr>

                            <tr style="height: 20px" >
                                <td align="center"> <%=++i%>.</td>
                                <td >Do you Submit Medical Report along with this leave application?</td>
                                <td>
                                    <input type="radio" class="easyui-radio" name="ifmedical" value="Y"  />
                                    Yes 
                                </td>
                                <td align="left"  > <input type="radio" class="easyui-radio" name="ifmedical" value="N"  />No</td>
                                <td id="innerdata" colspan="2">&nbsp; </td>
                            </tr>


                            <tr style="height: 20px" >
                                <td align="center" valign="top"> <%=++i%>.</td>
                                <td  id="innerdata" align="left">                               
                                    Do you request for Head Quarter Leave Permission with this leave application? </td>
                                <td>
                                    <input type="radio" class="easyui-radio" name="hqperrad" id="hqperrad"  value="Y"   onclick="radioOption(this)"/>Yes
                                </td>
                                <td align="left"><input type="radio" class="easyui-radio" name="hqperrad" id="hqperrad" value="N" onclick="radioOption(this)"/>No</td>
                                <td align="left"  colspan="2">&nbsp;</td>
                            </tr>


                            <tr style="height: 20px" >
                                <td align="center" valign="top"> </td>
                                <td valign="top">Contact Address While On Leave </td>
                                <td colspan="5" valign="top">
                                    <input class="easyui-textarea"  name="txtconaddress" value="${leaveForm.txtconaddress}"  id="txtconaddress" style="width:91%;height:60px;border:1px solid #000000;" maxlength="500"  disabled="true"/> 
                                </td>
                            </tr>

                            <tr style="height: 30px" >
                                <td align="center" valign="top"> </td>
                                <td valign="top">Mobile No: </td>
                                <td colspan="5" valign="top">
                                    <input type="text" name="txtphoneNo" id="txtphoneNo" value="${leaveForm.txtphoneNo}"   style="width:91%;border:1px solid #000000;" maxlength="10" disabled="true" /> 
                                </td>  
                            </tr>

                            <tr style="height: 30px" >
                                <td align="center" valign="top"><%=++i%>.</td>
                                <td valign="top">Reason for Leave</td>
                                <td colspan="5" id="innerdataNote" >                                    
                                    <input class="easyui-textbox" type="textarea" name="txtnote" data-options="multiline:true"  styleId="txtnote" style="width:80%;height:60px;border:1px solid #000000;" labelPosition="top"  styleClass="textareacolor" maxlength="1000"/>                                                             
                                </td>
                            </tr>

                            <tr>
                                <td align="center" valign="top"><%=++i%>.</td>
                                <td valign="top">Attach Document</td>
                                <td colspan="5" valign="top">
                                    <c:forEach var="attachList" items="${leaveList}">
                                        <c:out value="${attachList.originalFileName}"/>
                                    </c:forEach>

                                    <a href="javascript:UploadFile(0)" class="atag">Attach File</a><br/>

                                    <div id="uploadfilelist"> </div>
                                </td>  
                            </tr>

                        </table>

                        <div style="text-align:center;padding:5px;margin:20px 0">
                            <input class="easyui-linkbutton c1" id="btn" type="submit" name="Save" style="width:120px;height:40px" value="Apply"  onclick="return applycheck()"/>
                            <input class="easyui-linkbutton c1" type="submit" name="Back1" style="width:120px;height:40px"  value="Back"/>

                        </div>
                    </div>
                </div>
            </div>
        </form:form>

    </body>
</html>