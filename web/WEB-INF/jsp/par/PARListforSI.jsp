<%-- 
    Document   : PARListforSI
    Created on : 15 Apr, 2023, 1:36:11 PM
    Author     : Manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <base href="${initParam['BaseURLPath']}" />                    

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Performance Appraisal</title> 
        <link href="resources/css/bootstrap.css" rel="stylesheet">
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="css/color.css"/>

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript">

            $(document).ready(function() {
                $('#fiscalyear').combobox('setValue', $('#defaultfiscalyear').val());
                getPARFiscalYearStatus($('#fiscalyear').combobox('getValue'));
            });

            function doSearch() {
                var fiscalyear = $('#fiscalyear').combobox('getValue');
                $('#dg').datagrid('load', {
                    fiscalyear: fiscalyear
                });
                getPARFiscalYearStatus(fiscalyear);
            }

            function getPARFiscalYearStatus(fy) {
                $.ajax({
                    type: "POST",
                    url: "par/getSiPARFiscalYearOpenStatus.htm?fiscalyear=" + fy + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val(),
                    dataType: "json"
                }).done(function(serverResponse) {
                    //alert("Status is: "+serverResponse.isClosed);
                    if (serverResponse.isClosed == 'Y') {
                        $('#parbtn').hide();
                        $('#parfiscalyearstatus').show();
                    } else {
                        $('#parbtn').show();
                        $('#parfiscalyearstatus').hide();
                    }
                });
            }

            function editPAR(val, row) {
                var parid = row.parid;
                var encparid = row.encryptedParid;
                var parstatus = row.parstatus;
                var iseditable = row.iseditable;

                //alert(iseditable);
                if (parstatus < 6 || parstatus == 16 || parstatus == 18 || parstatus == 19) {
                    var editdata = 'par/editPAR.htm?parid=' + parid + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();
                    var viewdata = 'par/PARDetailView1.htm?parid=' + encparid + '&taskid=0&auth=' + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();
                    var deletedata = 'par/deletePAR.htm?parid=' + parid + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();

                    //return "<a href='"+editdata+"'>Edit</a>/<a href='"+viewdata+"' target='_blank'>View</a>";
                    if (parstatus != 16 && parstatus != 18 && parstatus != 19) {
                        return "<a href='" + editdata + "'>Edit</a>/<a href='" + viewdata + "' target='_blank'>View</a>";
                    } else if (parstatus == 16 || parstatus == 18 || parstatus == 19) {
                        if (iseditable == 'Y' && parstatus == 16) {
                            //return "<a href='" + editdata + "'>Edit</a>/<a href='" + viewdata + "' target='_blank'>View</a>/<a href='javascript:delEtePAR(" + parid + ")'>Delete</a>&nbsp;&nbsp;&nbsp&nbsp;<a href='javascript:viewRevertReason(" + parid + ")'><img src='./images/revert.png' width='25' height='25' alt='Reverted' title='Reverted'/></a>";
                            return "<a href='" + editdata + "'>Edit</a>/<a href='" + viewdata + "' target='_blank'>View</a>&nbsp;&nbsp;&nbsp&nbsp;<a href='javascript:viewRevertReason(" + parid + ")'><img src='./images/revert.png' width='25' height='25' alt='Reverted' title='Reverted'/></a>";
                        } else {
                            return "<a href='" + editdata + "'>Edit</a>/<a href='" + viewdata + "' target='_blank'>View</a>&nbsp;&nbsp;&nbsp&nbsp;<a href='javascript:viewRevertReason(" + parid + ")'><img src='./images/revert.png' width='25' height='25' alt='Reverted' title='Reverted'/></a>";
                        }
                    }
                } else if (parstatus >= 6 && (parstatus != 17 && parstatus != 16 && parstatus != 18 && parstatus != 19)) {
                    //var data = 'viewPAR.htm?parid='+parid;
                    var viewdata = 'par/PARDetailView1.htm?parid=' + encparid + '&taskid=0&auth=' + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();
                    return "<a href='" + viewdata + "' target='_blank'>View</a>";
                } else if (parstatus == 17) {
                    var editnrc = 'par/NRCDetailView.htm?parid=' + parid + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();
                    return "<a href='" + editnrc + "'>View</a>";
                }
            }
            /*function delEtePAR(PARID){
             if(window.confirm("Are You Sure You want to delete?")){
             alert(PARID);
             }
             
             }*/

            function submitPAR(val, row) {
                var parid = row.parid;
                var parstatus = row.parstatus;
                var isclosed = row.isClosed;
                var authRemarksClosed = row.authRemarksClosed;
                //alert("PAR Status is: "+ parstatus);
                if (isclosed == "N" || authRemarksClosed == "N") {
                    if (parstatus < 6 || parstatus == 16 || parstatus == 18 || parstatus == 19) {
                        var submitdata = 'par/submitPAR.htm?parid=' + parid + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();
                        return "<a href='" + submitdata + "'>Submit</a>";
                    } else if (parstatus >= 6) {
                        if (parstatus == 6) {
                            return "<strong style='color:green'>Submitted to Reporting Authority</strong>";
                        } else if (parstatus == 7) {
                            return "<strong style='color:#FF1493'>Submitted to Reviewing Authority</strong>";
                        } else if (parstatus == 8) {
                            return "<strong style='color:#4B0082'>Submitted to Accepting Authority</strong>";
                        } else if (parstatus == 9) {
                            return "Completed";
                        } else if (parstatus == 17) {
                            return "<strong style='color:#4B0082'>Requested for NRC</strong>";
                        } else if (parstatus == 116) {
                            return "<strong style='color:#4B0082'>Previous Year PAR is Uploaded</strong>";
                        }
                    }
                } else if (isclosed == "Y" && authRemarksClosed == "Y") {
                    if (parstatus != 9) {
                        if (parstatus == 17) {
                            return "<span style='color:red'>NRC is Completed</span>";
                        } else {
                            return "<span style='color:red'>PAR is Closed</span>";
                        }
                    } else {
                        return "Completed";
                    }
                }
            }

            function viewRevertReason(parid) {
                var url = 'par/RevertReason.htm?parid=' + parid + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();
                $.colorbox({href: url, iframe: true, open: true, width: "60%", height: "50%"});
            }

            function createCheck() {
                var fiscalyear = $('#fiscalyear').combobox('getValue');
                if (fiscalyear == '') {
                    alert("Please select Financial Year");
                    return false;
                }
            }
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <form action="par/addPAR.htm" method="POST" commandName="parMastForm">
            <input type="hidden" name="csrfPreventionSalt" id="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <input type="hidden" id="defaultfiscalyear" value="${defaultfiscalyear}"/>
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div class="empInfoDiv" align="center">
                    <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                        <tr>
                            <td align="right" width='60%'>
                                <input name="fiscalyear" id="fiscalyear" class="easyui-combobox" style="width:20%" data-options="valueField:'fy',textField:'fy',url:'GetFiscalYearListJSON.htm'" />
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <button class="btn btn-md btn-warning" type="button" onclick="doSearch();">Ok</button>
                            </td>
                            <td align="left" width='40%'>
                                &nbsp;&nbsp;&nbsp;&nbsp; <b style="color:#FF4500;font-size: 17px;"> This PAR is only for Sub-Inspector & Equivalent Ranks. </b>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <table id="dg" class="easyui-datagrid" style="width:100%;height:400px;" title="My PAR" rownumbers="true" singleSelect="true" 
                   url="GetPARListForSIJSON.htm" singleSelect="true" pagination="true" collapsible="false" data-options="nowrap:false">
                <thead>
                    <tr>
                        <th data-options="field:'taskid',width:10,hidden:true"></th>
                        <th data-options="field:'parstatus',width:10,hidden:true"></th>
                        <th data-options="field:'isClosed',width:10,hidden:true"></th>
                        <th data-options="field:'authRemarksClosed',width:10,hidden:true"></th>
                        <th data-options="field:'parid',width:80">PAR No</th>
                        <th data-options="field:'periodfrom',width:150">Period From</th>
                        <th data-options="field:'periodto',width:150">Period To</th>
                        <th data-options="field:'designation',width:500">Designation during the Period of Report</th>
                        <th data-options="field:'isauthSet',width:100,formatter:editPAR">Edit</th>
                        <th data-options="field:'refid',width:300,formatter:submitPAR">Submit</th>
                    </tr> 
                </thead>
            </table>

            <table style="width:100%;height: 90px;">
                <tr>
                    <td width="30%" align="left" style="padding-left:10px;">
                        <input type="hidden" name="pageno" value="0"/>
                        <%--<button type="submit">Create PAR</button>--%>
                        <span id="parfiscalyearstatus" style="font-weight:bold; color: #FF0000;">
                            PAR for selected Financial Year is closed.
                            <%--  PAR is Under Maintenance.Please try after 4 PM today. --%>
                        </span>
                        <span id="parbtn">
                            <input type="submit" name="newPar" value="Create PAR" class="btn btn-info"  onclick="return createCheck();"/>
                            &nbsp;&nbsp;&nbsp;&nbsp; 
                            <input type="submit" name="newPar" value="Request For NRC" class="btn btn-primary"  onclick="return createCheck();"/>
                        </span>
                        &nbsp;&nbsp;
                        <%--<c:if test="${HideBtn ne 'Yes' && parMastForm.parType ne 'SiPar'}">
                            <span id="initiateparbtn">
                                <input type="submit" name="newPar" value="Initiate PAR for Others" class="easyui-linkbutton c5" style="width:150px" onclick="return createCheck();"/>
                            </span>
                        </c:if> --%>
                    </td>
                    <td width="70%" align="left">&nbsp;</td>
                </tr>
            </table>
        </form>
    </body>
</html>

