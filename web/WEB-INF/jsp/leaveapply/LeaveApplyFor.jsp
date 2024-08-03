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
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
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
                $.colorbox({href: url, iframe: true, open: true, width: "50%", height: "50%", overlayClose: false});
            }
            
             function applyFor() {
                var url = 'leaveapplyfor.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "50%", height: "50%", overlayClose: false});
            }

            function UploadFile() {
                var url = 'UploadDocumentAction.htm?KeepThis=true&TB_iframe=true&height=375&width=452&scrollwin=true';
                $.colorbox({href: url, iframe: true, open: true, width: "50%", height: "50%", overlayClose: false});
            }
            $(document).ready(function() {
                $(".thickbox").colorbox({iframe: true, width: "50%", height: "50%",
                });
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
                var currDate = $('#curdate').val();
                var currentDate = currDate.split("-");
                var min_date = currentDate[0];
                var min_mon = currentDate[1];
                var min_year = currentDate[2];
                var cur_mon;
                if (min_mon == "Jan") {
                    cur_mon = 0;
                } else if (min_mon == "Feb") {
                    cur_mon = 1;
                } else if (min_mon == "Mar") {
                    cur_mon = 2;
                } else if (min_mon == "Apr") {
                    cur_mon = 3;
                } else if (min_mon == "May") {
                    cur_mon = 4;
                } else if (min_mon == "Jun") {
                    cur_mon = 5;
                } else if (min_mon == "Jul") {
                    cur_mon = 6;
                } else if (min_mon == "Aug") {
                    cur_mon = 7;
                } else if (min_mon == "Sep") {
                    cur_mon = 8;
                } else if (min_mon == "Oct") {
                    cur_mon = 9;
                } else if (min_mon == "Nov") {
                    cur_mon = 10;
                } else if (min_mon == "Dec") {
                    cur_mon = 11;
                }

                $('#txtperiodFrom').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    minDate: new Date(min_year, cur_mon, min_date),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });

            });
            var req = null;
            var READY_STATE_UNINITIALIZED = 0;
            var READY_STATE_LOADING = 1;
            var READY_STATE_LOADED = 2;
            var READY_STATE_INTERACTIVE = 3;
            var READY_STATE_COMPLETE = 4;
            function initXMLHTTPRequest() {
                var xRequest = null;
                if (window.XMLHttpRequest) {
                    xRequest = new XMLHttpRequest();
                } else if (window.ActiveXObject) {
                    xRequest = new ActiveXObject("Microsoft.XMLHTTP");
                }
                return xRequest;
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
                var leavetype = document.getElementById("sltleaveType");
                if (leavetype.value == "")
                {
                    alert("Please Select Type of Leave");
                    leavetype.focus();
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

                var ret = chkdate(fromdate, todate, "From date ", "To date");
                if (ret == false) {
                    return false;
                }
                var prefixFrom = document.getElementById("txtprefixFrom");
                var prefixTo = document.getElementById("txtprefixTo");
                var ret1 = chkdate(prefixFrom, prefixTo, "Prefix From Date ", "Prefix To Date");
                if (ret1 == false) {
                    return false;
                }
                var sufixFrom = document.getElementById("txtsuffixFrom");
                var sufixTo = document.getElementById("txtsuffixTo");
                var ret2 = chkdate(sufixFrom, sufixTo, "Sufix From Date ", "Sufix To Date");
                if (ret2 == false) {
                    return false;
                }
                var phNo = document.getElementById("txtphoneNo").value;
                if (isNaN(phNo))
                {
                    alert("Enter the valid Mobile Number");
                    return false;
                }

                if (phNo != '') {
                    if (phNo.length != 10)
                    {
                        alert("Please enter valid Mobile number");
                        return false;
                    }
                }
            }

            function enablePrefixInputFields()
            {
                var chkhqper = document.getElementById("chkprefix");
                if (chkhqper.checked == true) {
                    document.getElementById("txtprefixFrom").disabled = false;
                    document.getElementById("txtprefixTo").disabled = false;
                    document.getElementById("linkprefixFrom").style.display = "inline";
                    document.getElementById("linkprefixTo").style.display = "inline";
                } else {
                    document.getElementById("txtprefixFrom").disabled = true;
                    document.getElementById("txtprefixTo").disabled = true;
                    document.getElementById("linkprefixFrom").style.display = "none";
                    document.getElementById("linkprefixTo").style.display = "none";
                }
            }
            function enableSuffixInputFields() {
                var chkhqper = document.getElementById("chksuffix");
                if (chkhqper.checked == true) {
                    document.getElementById("txtsuffixFrom").disabled = false;
                    document.getElementById("txtsuffixTo").disabled = false;
                    document.getElementById("linksuffixFrom").style.display = "inline";
                    document.getElementById("linksuffixTo").style.display = "inline";
                } else {
                    document.getElementById("txtsuffixFrom").disabled = true;
                    document.getElementById("txtsuffixTo").disabled = true;
                    document.getElementById("linksuffixFrom").style.display = "none";
                    document.getElementById("linksuffixTo").style.display = "none";

                }
            }


            function SelectSpn(empId, empName, desig, spc)
            {
                $.colorbox.close();
                $('#txtSancAuthority').textbox('setValue', empName + "," + desig);
                alert(empId);
                $('#hidAuthEmpId').val(empId);
                $('#hidSpcAuthCode').val(spc);
            }
            function enableInputFields() {
                var chkhqper = document.getElementById("chkhqper");
                if (chkhqper.checked == true) {
                    document.getElementById("txtphoneNo").disabled = false
                    document.getElementById("txtconaddress").disabled = false
                } else {
                    document.getElementById("txtphoneNo").disabled = true
                    document.getElementById("txtconaddress").disabled = true
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
                    document.getElementById("txtphoneNo").disabled = false
                    document.getElementById("txtconaddress").disabled = false
                } else {
                    document.getElementById("txtphoneNo").disabled = true
                    document.getElementById("txtconaddress").disabled = true
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
        <form:form  action="leaveapplyedit.htm" method="POST" commandName="leaveForm">
            <div align="center">
                <div style="width:99%;">
                    <div id="tbl-container" class="easyui-panel" title="Apply New Leave"  style="width:100%;overflow: auto;">
                        <form:hidden path="hidempId" id="hidempId"/>
                        <form:hidden path="hidSpcCode" id="hidSpcCode"/>
                        <input type="hidden" name="hidAuthEmpId" id="hidAuthEmpId"/>
                        <input type="hidden" name="hidSpcAuthCode" id="hidSpcAuthCode"/>
                        <table cellpadding="5">
                            <tr>
                                <td align="center"> <%=++i%>. </td>
                                <td >Applying For Other:</td>
                                <td colspan="4"><input class="easyui-textbox" id="txtApplyFor" type="text" name="txtApplyFor" style="width:300px;height:25px"    readonly="true"></input>
                                    <a href="javascript:void(applyFor())" class="easyui-linkbutton"> Search </a>
                                </td>
                            </tr>

                            <tr>
                                <td align="center"><c:out value="${empId}"/> <%=++i%>. </td>
                                <td >Applying For Leave To:<span style="color: red">*</span></td>
                                <td colspan="4"><input class="easyui-textbox" id="txtSancAuthority" type="text" name="txtSancAuthority" style="width:300px;height:25px"    readonly="true"></input>
                                    <a href="javascript:void(searchAuthority())" class="easyui-linkbutton"> Search </a>
                                </td>
                            </tr>

                            <tr>
                                <td align="center"> <%=++i%>. </td>
                                <td >Type of Leave:</td>
                                <td colspan="4"><input class="easyui-combobox"  id="tolId" name="sltleaveType" data-options="valueField:'value',textField:'label',url:'getLeaveType.htm'" style="width:300px;height:25px"></td>
                            </tr>
                            <tr>
                                <td align="center" width="10%"> <%=++i%>.</td>
                                <td width="36%">Duration of Leave you want to avail</td>
                                <td width="12%">a) From Date<span style="color: red">*</span></td>
                                <td id="innerdata" width="18%">    
                                    <input name="txtperiodFrom" readonly="true"  style="width:80px;" class="txtDate"  />


                                </td>
                                <td align="left"  width="10%">
                                    b) To Date<span style="color: red">*</span>
                                </td>
                                <td id="innerdata"  width="24%"   >                                    

                                    <input name="txtperiodTo"  readonly="true"  style="width:80px;" class="txtDate"  />
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
                                    <input class="easyui-datetimebox" type="text" disabled="true" name="txtprefixFrom" styleId="txtprefixFrom"  onblur="dateValidation(this)" styleClass="txtDate" style="width:100px;text-align: left;" />
                                </td>
                                <td> b) To Date </td>
                                <td id="innerdata" >  
                                    <input class="easyui-datetimebox" type="text" disabled="true" name="txtprefixTo" styleId="txtprefixTo"  onblur="dateValidation(this)" styleClass="txtDate" style="width:100px;text-align: left;" />
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
                            <tr style="height: 30px" >
                                <td align="center" >&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>a) From Date</td>
                                <td id="innerdata" >
                                    <input class="easyui-datetimebox" type="text" disabled="true" name="txtsuffixFrom" styleId="txtsuffixFrom"  onblur="dateValidation(this)" styleClass="txtDate" style="width:100px;text-align: left;" />
                                </td>
                                <td >b) To Date</td>
                                <td id="innerdata"  >
                                    <input class="easyui-datetimebox" type="text" disabled="true"  name="txtsuffixTo" styleId="txtsuffixTo"  onblur="dateValidation(this)" styleClass="txtDate" style="width:100px;text-align: left;" />
                                </td>
                            </tr>

                            <tr style="height: 20px" >
                                <td align="center"> <%=++i%>.</td>
                                <td >Do you Submit Medical Certificate along with this leave application?</td>
                                <td>
                                    <input type="radio" class="easyui-radio" name="ifmedical" value="Y"  />
                                    Yes 
                                    <input type="radio" class="easyui-radio" name="ifmedical" value="N"  />No
                                </td>
                                <td align="left"  >&nbsp;</td>
                                <td id="innerdata" colspan="2">&nbsp; </td>
                            </tr>


                            <tr style="height: 20px" >
                                <td align="center" valign="top"> <%=++i%>.</td>
                                <td  id="innerdata" align="left">                               
                                    Do you request for Head Quarter Leave Permission with this leave application? </td>
                                <td>
                                    <input type="radio" class="easyui-radio" name="chkhqper" styleId="chkhqper"  value="Y"   onclick="radioOption(this)"/>
                                    Yes<input type="radio" class="easyui-radio" name="chkhqper" value="N" onclick="radioOption(this)"/>No
                                </td>
                                <td align="left">&nbsp;</td>
                                <td align="left"  colspan="2">&nbsp;</td>
                            </tr>


                            <tr style="height: 20px" >
                                <td align="center" valign="top"> </td>
                                <td valign="top">Contact Address While On Leave </td>
                                <td colspan="5" valign="top">

                                    <input class="easyui-textarea" type="textarea" name="txtconaddress"  styleId="txtconaddress" style="width:91%;height:60px;border:1px solid #000000;" styleClass="textareacolor" disabled="true"/> 

                                </td>
                            </tr>

                            <tr style="height: 30px" >
                                <td align="center" valign="top"> </td>
                                <td valign="top">Mobile No: </td>
                                <td colspan="5" valign="top">

                                    <input class="easyui-textbox" type="text" name="txtphoneNo" styleId="txtphoneNo" style="width:91%;border:1px solid #000000;" disabled="true"/> 

                                </td>  

                            </tr>

                            <tr style="height: 20px" >
                                <td align="center" valign="top"><%=++i%>.</td>
                                <td valign="top">Reason for Leave</td>
                                <td colspan="5" id="innerdataNote" >                                    
                                    <input class="easyui-textarea" type="textarea" name="txtnote"  styleId="txtnote" style="width:91%;height:60px;border:1px solid #000000;" styleClass="textareacolor" onkeyup="maxlengthcheck('txtnote', 1000)"/>                                                             
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

                        <div style="text-align:center;padding:5px">
                            <input class="easyui-linkbutton" type="submit" name="Save" value="Apply"/>
                             <input class="easyui-linkbutton" type="submit" name="Back1" value="Back"/>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript" language="javascript">
            enablePrefixInputFields();
            enableSuffixInputFields();
            setDivHeightLeaveAppE('tbl-container');
        </script>
    </body>
</html>