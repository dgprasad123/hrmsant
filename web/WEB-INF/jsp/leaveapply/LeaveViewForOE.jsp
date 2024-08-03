<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<%
    int i = 1;
    String urlDownload = "";
    String fromDate = "";
    String toDate = "";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Leave Apply View</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/color.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <script language="javascript" src="js/servicehistory.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function () {
                $('#txtSancAuthority').val("");
//                $(".thickbox").colorbox({iframe: true, width: "50%", height: "50%",
//                });
            });
            $(document).ready(function () {
                $('#Search').linkbutton('disable');
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false,
                    scrollMonth: false,
                    scrollInput: false
                });
            });
            function actionEvent(rec) {
                if (rec == "2") {
                    $('#Search').linkbutton('enable');
                } else {
                    $('#Search').linkbutton('disable');


                }
            }

            function searchAuthority() {
                var url = 'leaveauthority.htm';
                $.colorbox({href: url, iframe: true, open: true, innerWidth: "700px", innerHeight: "400px", overlayClose: false});
            }
            function SelectSpn(empId, empName, desig, spc)
            {
                $.colorbox.close();
                $('#txtSancAuthority').textbox('setValue', empName + "," + desig);
                $('#hidAuthEmpId').val(empId);
                $('#hidSpcAuthCode').val(spc);
            }
            function savecheck() {
                var strUser = $('#sltActionType').combobox('getValue');
                if (strUser == '') {
                    alert("Select Take Action To Submit");
                    return false;
                }
                if (strUser == "2") {
                    if ($('#txtSancAuthority').val() == "") {
                        alert("Please select authority to forward the leave");
                        return false;
                    }

                }

                var approveFrom = document.getElementById("txtApproveFrom");
                var approveTo = document.getElementById("txtApproveTo");
                var ret = chkdate(approveFrom, approveTo, "Approve From date ", "Approve To date");
                if (ret == false) {
                    return false;
                }

            }
            function openNewWindow() {
                if ($('#txtOrdNo').val() == "") {
                    alert("Please Enter Order No.");
                    return false;
                }
                if ($('#txtOrdDate').val() == "") {
                    alert("Please Enter Order Date");
                    return false;
                }
            }
            function openSanctionOrder() {
                var officename = document.getElementById("offname").value;
                var myWindow = window.open("", "_self");
                myWindow.document.write("<b><center><p>GOVERNMENT OF ODISHA</p></center></b>");
                myWindow.document.write("<center><b> " + officename + "</b></center>");
                myWindow.document.write("<br>");
                myWindow.document.write("<b><center>* * * *</b></center>");
                myWindow.document.write("<br>");
                myWindow.document.write("<br>");

                myWindow.document.write("<b><center><u>OFFICE ORDER</u></b></center>");
                myWindow.document.write("<br>");
                myWindow.document.write("<br>");

                myWindow.document.write("<center>No.    ________________ /Gen.  ,</center><br> ");
                myWindow.document.write("<center>" + officename.toLowerCase() + " is granted earned leave for the period </center>");

            }




        </script>
    </head>
    <body>
        <form:form name="myForm"  action="leaveViewDataOE.htm" method="POST" commandName="leaveForm">
            <input type="hidden" name="statusId" id="statusId" value="${leaveForm.statusId}"/>
            <input type="hidden" name="offname" id="offname" value="${leaveForm.offname}"/>
            <input type="hidden" name="leaveId" id="leaveId" value="${leaveForm.leaveId}"/>
            <input type="hidden" name="hidempId" id="hidempId" value="${leaveForm.hidempId}"/>
            <input type="hidden" name="tollid" id="tollid" value="${leaveForm.tollid}"/>
            <input type="hidden" name="passString" id="passString"/>
            <input type="hidden" name="hidAuthEmpId" id="hidAuthEmpId" value="${leaveForm.hidAuthEmpId}"/>
            <input type="hidden" name="hidSpcAuthCode" id="hidSpcAuthCode" value="${leaveForm.hidSpcAuthCode}"/>
            <input type="hidden" name="hidTaskId" id="hidTaskId" value="${leaveForm.hidTaskId}"/>
            <div align="center">
                <div style="width:99%;">
                    <div id="tbl-container" class="easyui-panel" title="Leave Balance"  style="width:100%;overflow: auto;">
                        <table border="0" width="97%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
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

                    </div>
                </div>
            </div>
            <div align="center">
                <div style="width:99%;">

                    <div id="tbl-container" class="easyui-panel" title="Leave View"  style="width:100%;overflow: auto;">
                        <c:if test = "${leaveForm.statusId == '4'}"> 
                            <fieldset style="margin-bottom:5px;">
                                <legend class="PortletLegendText">Applicant Joining Report </legend> 
                                <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview" style="font-size:12px; font-family:verdana;">

                                    <tr style="height: 30px" >                               
                                        <td align="center"><%=i++%>.</td> 
                                        <td>Order No</td>
                                        <td id="innerdata">   
                                            <span><c:out value="${leaveForm.txtOrdNo}" /></span>
                                        </td>
                                        <td> Order Date </td>
                                        <td id="innerdata" >  
                                            <span><c:out value="${leaveForm.txtOrdDate}" /></span>
                                        </td>
                                    </tr>
                                    <tr style="height: 40px">                               
                                        <td align="center"> <%=i++%>. </td>
                                        <td>
                                            Joining Date :

                                        </td> 
                                        <td colspan="2">
                                            <span><c:out value="${leaveForm.joiningDate}" /></span>
                                        </td> 
                                    </tr>
                                    <tr style="height: 40px">                               
                                        <td align="center"> <%=i++%>. </td>
                                        <td>
                                            Joining Note :

                                        </td> 
                                        <td colspan="2">
                                            <span><c:out value="${leaveForm.joiningNote}" /></span>
                                        </td> 
                                    </tr>
                                    <tr style="height: 40px" >                               
                                        <td align="center"> <%=i++%>. </td>
                                        <td >Attached Document :</td> 
                                        <td  colspan="3">
                                            <c:forEach var="joinFileArrList" items="${leaveForm.joinFileArrList}">
                                                <a href='downloadLeaveFile.htm?attId=<c:out value="${joinFileArrList.attId}"/>' style="color:#0000FF;margin-left:10px;font-size:12px;" target="_blank"> <span><c:out value="${joinFileArrList.attFileName}"/><br></span></a>
                                                    </c:forEach>
                                    </tr>
                                </table> 
                            </fieldset>
                        </c:if>
                        <fieldset style="margin-bottom:5px;">
                            <legend class="PortletLegendText">Applicant Leave Report </legend> 
                            <table cellpadding="5" border="0" style="font-size:12px; font-family:verdana;">
                                <tr style="height: 40px" >                               
                                    <td align="center"> <%=i++%>. </td>
                                    <td>Applicant</td>
                                    <td colspan="3"><span><c:out  value="${leaveForm.applicantName}"/><br>(<c:out  value="${leaveForm.appOffname}"/>)</span></td> 
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center"> <%=i++%>. </td>
                                    <td>Submitted To</td>
                                    <td colspan="3"><span><c:out  value="${leaveForm.submittedTo}"/></span></td> 
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center"> <%=i++%>. </td>
                                    <td >Type of Leave :</td> 
                                    <td colspan="3"><span><c:out  value="${leaveForm.sltleaveType}"/></span></td> 
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="5%"> <%=i++%>. </td>
                                    <td width="20%">From Date :</td> 
                                    <td width="15%"><span><c:out  value="${leaveForm.txtperiodFrom}"/></span></td> 
                                    <td width="15%">To Date :</td> 
                                    <td width="45%"><span><c:out value="${leaveForm.txtperiodTo}"/></span></td> 
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" > <%=i++%>.</td>
                                    <td>Suffix From :</td> 
                                    <td><span><c:out value="${leaveForm.txtsuffixFrom}"/></span></td> 
                                    <td>Suffix To :</td> 
                                    <td ><span><c:out value="${leaveForm.txtsuffixTo}"/></span></td> 
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" > <%=i++%>. </td>
                                    <td>Prefix From :</td> 
                                    <td><span><c:out value="${leaveForm.txtprefixFrom}"/></span></td> 
                                    <td>Prefix To :</td> 
                                    <td width="30%"><span><c:out value="${leaveForm.txtprefixTo}"/></span></td> 
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center"> <%=i++%>. </td>
                                    <td >If Head Quarter Leave Permission  :</td> 
                                    <td  colspan="3"><c:out value="${leaveForm.hqperrad}"/></td> 
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center"> <%=i++%>. </td>
                                    <td >Contact Address While On Leave  :</td> 
                                    <td  colspan="3"><span><c:out value="${leaveForm.txtconaddress}"/></span></td> 
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center"> <%=i++%>. </td>
                                    <td >Phone No :</td> 
                                    <td  colspan="3"><span><c:out value="${leaveForm.txtphoneNo}"/></span></td> 
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center"> <%=i++%>. </td>
                                    <td >Reason For Leave :</td> 
                                    <td  colspan="3"><span><c:out value="${leaveForm.txtnote}"/></span></td> 
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center"> <%=i++%>. </td>
                                    <td >Attached Document :</td> 
                                    <td  colspan="3">
                                        <c:forEach var="fileArrList" items="${leaveForm.fileArrList}">
                                            <a href='downloadLeaveFile.htm?attId=<c:out value="${fileArrList.attId}"/>' style="color:#0000FF;margin-left:10px;font-size:12px;" target="_blank"> <span><c:out value="${fileArrList.attFileName}"/><br></span></a>
                                                </c:forEach>
                                </tr>
                            </table>
                        </fieldset>
                        <c:forEach var="LeaveFlowDtls" items="${work}">
                            <fieldset style="margin-bottom:5px;">
                                <legend class="PortletLegendText">Action taken Report </legend>    
                                <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview" style="font-size:12px; font-family:verdana;">
                                    <%i = 1;%>
                                    <tr style="height: 40px" >                               
                                        <td align="center" width="5%"><%=i++%>.</td>
                                        <td  width="20%">
                                            Action Taken By :
                                        </td>  
                                        <td  width="75%" colspan="2">
                                            <span><c:out value="${LeaveFlowDtls.actionTaken}" /></span>
                                        </td> 
                                    </tr>
                                    <tr style="height: 40px" >                               
                                        <td align="center" width="5%"><%=i++%>.</td>
                                        <td  width="20%">
                                            <c:out value="${LeaveFlowDtls.status}" /> :
                                        </td>  
                                        <td  width="75%" colspan="2">
                                            <span><c:out value="${LeaveFlowDtls.actionTakenBy}" /></span>
                                        </td> 
                                    </tr>
                                    <c:if test = "${(leaveForm.statusId == '1')||(leaveForm.statusId == '23')||(leaveForm.statusId == '5')}">   
                                        <tr style="height: 40px">                               
                                            <td align="center"><%=i++%>. </td>
                                            <td>
                                                Approve Date
                                            </td> 
                                            <td >
                                                From Date :<span><c:out value="${leaveForm.txtApproveFrom}" /></span>
                                            </td> 
                                            <td>
                                                To Date :<span><c:out value="${leaveForm.txtApproveTo}" /></span>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <tr style="height: 40px">                               
                                        <td align="center"> <%=i++%>. </td>
                                        <td>
                                            Action Taken Date :

                                        </td> 
                                        <td colspan="2">
                                            <span><c:out value="${LeaveFlowDtls.taskdate}" /></span>
                                        </td> 
                                    </tr>

                                    <tr style="height: 40px">                               
                                        <td align="center"> <%=i++%>. </td>
                                        <td>
                                            Note :
                                        </td> 
                                        <td colspan="2">
                                            <span><c:out value="${LeaveFlowDtls.note}" /></span>
                                        </td> 
                                    </tr>
                                </table>
                            </fieldset>
                        </c:forEach>
                        <c:if test="${leaveForm.passString == 'Task'}">
                            <c:if test = "${leaveForm.statusId == '41'}"> 
                                <fieldset style="margin-bottom:5px;">
                                    <legend class="PortletLegendText"></legend>
                                    <%i = 1;%>
                                    <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview" style="font-size:12px; font-family:verdana;">
                                        <input type="hidden" name="txtApproveFrom" id="txtApproveFrom" value="${leaveForm.txtApproveFrom}"/>
                                        <input type="hidden" name="joiningDate" id="joiningDate" value="${leaveForm.joiningDate}"/>

                                        <tr style="height: 30px" >
                                            <td align="center" width="5%"> <%=i++%>.</td>
                                            <td width="15%">Approve Date</td>
                                            <td width="15%">a) From Date</td>
                                            <td id="innerdata" width="15%">
                                                <c:out value="${leaveForm.txtApproveFrom}" />
                                                <input id="txtApproveFrom" name="txtApproveFrom"  readonly="true" value="${leaveForm.txtApproveFrom}" style="width:80px;" class="txtDate"  />
                                            </td>
                                            <td align="left"  width="15%">
                                                b) To Date
                                            </td>
                                            <td id="innerdata"  width="35%"   >  
                                                <input id="txtApproveTo" name="txtApproveTo"  readonly="true" value="${leaveForm.txtApproveTo}" style="width:80px;" class="txtDate"  />
                                            </td>
                                        </tr>
                                        <tr style="height: 40px">                               
                                            <td align="center"> <%=i++%>. </td>
                                            <td>
                                                Joining Date :

                                            </td> 
                                            <td colspan="2">
                                                <span><c:out value="${leaveForm.joiningDate}" /></span>
                                            </td> 
                                        </tr>
                                        <tr style="height: 40px">                               
                                            <td align="center"> <%=i++%>. </td>
                                            <td>
                                                Joining Note :

                                            </td> 
                                            <td colspan="2">
                                                <span><c:out value="${leaveForm.joiningNote}" /></span>
                                            </td> 
                                        </tr>
                                        <tr style="height: 40px" >                               
                                            <td align="center"> <%=i++%>. </td>
                                            <td >Attached Document :</td> 
                                            <td  colspan="3">
                                                <c:forEach var="joinFileArrList" items="${leaveForm.joinFileArrList}">
                                                    <a href='downloadLeaveFile.htm?attId=<c:out value="${joinFileArrList.attId}"/>' style="color:#0000FF;margin-left:10px;font-size:12px;" target="_blank"> <span><c:out value="${joinFileArrList.attFileName}"/><br></span></a>
                                                        </c:forEach>
                                        </tr>
                                        <tr style="height: 30px" >                               
                                            <td align="center"><%=i++%>.</td>                                

                                            <td>Order No <span style="color: red">*</span></td>
                                            <td id="innerdata">   
                                                <input class="easyui-textbox" type="text"  name="txtOrdNo" id="txtOrdNo"  style="width:100px;text-align: left;" />
                                            </td>
                                            <td> Order Date <span style="color: red">*</span></td>
                                            <td id="innerdata" >  
                                                <input id="txtOrdDate" name="txtOrdDate" readonly="true"  style="width:80px;" class="txtDate"  />
                                            </td>
                                        </tr>

                                    </table> 
                                </fieldset>
                            </c:if>
                        </c:if>
                       

                        <div style="text-align:center;padding:5px">
                            <c:if test="${leaveForm.passString == 'Task'}">
                                <c:if test = "${leaveForm.statusId == '41'}"> 
                                    <input class="easyui-linkbutton c1" style="width:120px;height:40px" type="submit" name="Submit" value="Submit"  onclick="return openNewWindow()" />
                                </c:if>
                            </c:if>



                            <c:if test = "${leaveForm.statusId == '4'}"> 
                                <input class="easyui-linkbutton c1" style="width:120px;height:40px" type="submit" name="Print" value="Print" />
                            </c:if>
                            <c:if test = "${leaveForm.statusId == '5'}"> 
                                <input class="easyui-linkbutton c1" style="width:120px;height:40px" type="submit" name="Print" value="Print" />
                            </c:if>
                            <c:if test = "${leaveForm.statusId == '41'}"> 
                                <input class="easyui-linkbutton c1" style="width:120px;height:40px" type="submit" name="Print" value="Print" />
                            </c:if>
                            <c:choose>
                                <c:when test="${leaveForm.passString == 'Own'}">
                                    <input class="easyui-linkbutton c1" style="width:120px;height:40px" type="submit" name="Back" value="Back"/>
                                    
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
