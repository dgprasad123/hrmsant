<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Performance Appraisal</title> 

        <link href="resources/css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/hrmis.css" />
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="css/color.css"/>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" src="js/servicehistory.js" type="text/javascript"></script>
        <script language="javascript" type="text/javascript">

            $(document).ready(function() {
                $("#AbsenceOthTr").hide();
                var value = "";
                var text = "";
                
                $('#absencecause').combobox({
                    onSelect: function(record) {
                        if (record.parleaveid == "L") {
                            text = "tol";
                            value = "tolid";
                            $("#AbsenceOthTr").hide();
                            $("#absenceCauseOth").val("");
                            $('#absenceCauseOth').textbox('clear');
                            
                        } else if (record.parleaveid == "T") {
                            text = "trainingtype";
                            value = "trainingtypeid";
                            $("#AbsenceOthTr").hide();
                            $('#absenceCauseOth').textbox('clear');
                            
                        }else if (record.parleaveid == "O") {
                            $("#AbsenceOthTr").show();
                            
                        }else{
                           $("#AbsenceOthTr").hide();
                           $("#absenceCauseOth").val(""); 
                           $('#absenceCauseOth').textbox('clear');
                        }
                        
                        $('#leavetype').combobox('clear');
                        var url = 'getLeaveorTrainingListJSON.htm?leavecause=' + record.parleaveid;
                        
                        $('#leavetype').combobox({
                            url: url,
                            valueField: value,
                            textField: text,
                            onLoadSuccess: onLoadSuccess
                        });
                    }
                });

                $('#absencecause').combobox('setValue', $('#parleavecause').val());
                function onLoadSuccess() {
                    $('#leavetype').combobox('setValue', $('#leaveortraining').val());
                }

                var fdate = $('#hidparfrmdt').val().split("-");
                var tdate = $('#hidpartodt').val().split("-");

                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    minDate: new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0]),
                    maxDate: new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            });
            
            
    
            function saveCheck() {
                if ($('#fromdate').val() == '') {
                    alert("Please enter From Date");
                    return false;
                }
                if ($('#todate').val() == '') {
                    alert("Please enter To Date");
                    return false;
                }

                if (($('#fromdate').val() != '') && ($('#todate').val() != '')) {
                    var ftemp = $("#fromdate").val().split("-");
                    var ttemp = $("#todate").val().split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form action="par/addPAR.htm" method="POST" commandName="parAbsentee">
            <input type="hidden" name="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <input type="hidden" name="hidparid" value="${parAbsentee.hidparid}"/>
            <input type="hidden" name="hidpabid" value="${parAbsentee.hidpabid}"/>
            <input type="hidden" id="parleavecause" value="${parAbsentee.leavecause}"/>
            <input type="hidden" id="leaveortraining" value="${parAbsentee.leaveortrainingtype}"/>
            <input type="hidden" name="hidparfrmdt" id="hidparfrmdt" value='${parfrmdt}'/>
            <input type="hidden" name="hidpartodt" id="hidpartodt" value='${partodt}'/>
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div class="easyui-panel" align="center">
                    <table border="0" width="90%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                        <tbody>
                            <tr style="height:40px">
                                <td width="90%" align="left">
                                    Period(s) of absence (on leave, training etc., if 30 days or more). Please  mention date(s)
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>	                                 
            </div>
            <div align="center">
                <div id="tbl-container" class="easyui-panel">
                    <table style="width:100%;height:50px;" border="0" cellpadding="0" cellspacing="0">    
                        <tr style="height:40px;">
                            <td width="5%" align="center">1.</td>
                            <td width="15%" align="left">From date</td>
                            <td width="80%" align="left">
                                <%--<fmt:formatDate var="frmDt" value="${parAbsentee.fromdate}" pattern="dd-MMM-yyyy"/>--%>
                                <input type="text" name="fromdate" id="fromdate" class="txtDate" readonly="true" style="width:20%" value='${parAbsentee.fromdate}' class="easyui-textbox"/>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td width="5%" align="center">2.</td>
                            <td align="left">To date</td>
                            <td align="left">
                                <%--<fmt:formatDate var="toDt" value="${parAbsentee.todate}" pattern="dd-MMM-yyyy"/>--%>
                                <input type="text" name="todate" id="todate" class="txtDate" readonly="true" style="width:20%" value='${parAbsentee.todate}' class="easyui-textbox"/>
                            </td>  
                        </tr>
                        <tr style="height:40px;">
                            <td width="5%" align="center">3.</td>
                            <td align="left">Cause of Absence</td>
                            <td align="left">
                                <input name="leavecause" id="absencecause" class="easyui-combobox" style="width:30%" data-options="valueField:'parleaveid',textField:'leavecause',url:'GetAbsenceCauseListJSON.htm',editable:false" />
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td width="5%" align="center">4.</td>   
                            <td align="left">Leave Type</td>
                            <td align="left">
                                <input name="leaveortrainingtype" id="leavetype" class="easyui-combobox" style="width:50%" data-options="editable:false"/>
                            </td>  
                        </tr>
                        <tr style="height:40px;" id="AbsenceOthTr">
                            <td width="5%" align="center">4.</td>
                            <td align="left">If Others</td>
                            <td align="left">
                                <input name="absenceCauseOth" id="absenceCauseOth" class="easyui-textbox" style="width:50%" maxlength="60"/>
                            </td>  
                        </tr>
                    </table>
                </div>
            </div>
            <div align="center">
                <div class="controlpanelDiv">	
                    <table width="100%" cellpadding="0" cellspacing="0" >
                        <tr style="height: 40px">
                            <td width="100%" align="left">
                                <span style="padding-left:10px;">
                                    <input type="hidden" name="mode" value="absentee"/>
                                    <input type="submit" name="newPar" value="Cancel" class="easyui-linkbutton c6" style="width: 90px;height: 30px;"/>
                                    <input type="submit" name="newPar" value="Save" class="easyui-linkbutton c1" onclick="return saveCheck();" style="width: 90px;height: 30px;"/>  
                                </span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </form>
    </body>
</html>
