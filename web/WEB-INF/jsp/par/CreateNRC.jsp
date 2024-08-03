<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String pdflink = "";
    int parId = 0;
    String fiscalyear = "";
    String downloadlink = "";
%>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Performance Appraisal</title>

        <link rel="stylesheet" href="resources/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>
        <link rel="stylesheet" type="text/css" href="css/hrmis.css" />
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <link href="resources/css/colorbox.css" rel="stylesheet"/>
        <link rel="stylesheet" type="text/css" href="css/color.css"/>

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script language="javascript" src="js/servicehistory.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#showNRCMoreThan120Days').hide();
            <%--$('#showNRCLessThan120Days').hide(); --%>


                var fisclyear = $('#fiscalyear').val();
                var finyear = fisclyear.split("-");
                var min_year = finyear[0];
                var max_year = parseInt(finyear[0]) + 1;

                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    minDate: new Date(min_year, 3, 1),
                    maxDate: new Date(max_year, 2, 31),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });

                $('#nrcReason').combobox('setValue', $('#nrcreason').val());

                $('#empDepartment').combobox('clear');
                $('#empOffice').combobox('clear');
                $('#empPost').combobox('clear');

                $('#empDepartment').combobox({
                    url: 'getDeptListJSON.htm',
                    onSelect: function(record) {
                        $('#empOffice').combobox('clear');
                        $('#empPost').combobox('clear');
                        var url = 'getOfficeListJSON.htm?deptcode=' + record.deptCode;
                        $('#empOffice').combobox('reload', url);
                    }
                });
                $('#empOffice').combobox({
                    onSelect: function(record) {
                        $('#empPost').combobox('clear');
                        var url = 'getOfficeWithSPCList.htm?offcode=' + record.offCode;
                        $('#empPost').combobox('reload', url);
                    }
                });

                $('#cadreDepartment').combobox('clear');
                $('#cadreName').combobox('clear');

                $('#cadreDepartment').combobox({
                    url: 'getDeptListJSON.htm',
                    onSelect: function(record) {
                        $('#cadreName').combobox('clear');
                        var url = 'getCadreListJSON.htm?deptcode=' + record.deptCode;
                        $('#cadreName').combobox('reload', url);
                    }
                });


                $("#txtToDate").bind('change', function() {
                    var oneDay = 24 * 60 * 60 * 1000;
                    var ftemp = $("#txtFromDate").val().split("-");
                    var ttemp = $("#txtToDate").val().split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);

                    var diffdays = Math.round(Math.abs((fdt.getTime() - tdt.getTime()) / (oneDay)));
                    if (diffdays > 120) {
                        $("#showNRCLessThan120Days").hide();
                        $("#nrcReason1").combobox('disable');
                        $("#nrcReason2").attr("disabled", false);
                        $("#showNRCMoreThan120Days").show();
                    } else {
                        $("#showNRCLessThan120Days").show();
                        $("#showNRCMoreThan120Days").hide();
                        $("#nrcReason2").attr("disabled", true);
                        $("#nrcReason1").combobox('enable');
                    }
                });
            });

            function changepost() {
                $('#winsubstantivepost').window('open');
            }
            function changeGroup() {
                $('#winsubstantiveGroup').window('open');
            }

            function addCadre() {
                $('#wincadre').window('open');
            }

            function getPost() {
                $('#hidOffice').val($('#empOffice').combobox('getValue'));
                $('#office').text($('#empOffice').combobox('getText'));
                $('#hidspc').val($('#empPost').combobox('getValue'));
                $("#post").text($('#empPost').combobox('getText'));

                $('#empDepartment').combobox('clear');
                $('#empOffice').combobox('clear');
                $('#empPost').combobox('clear');

                $('#winsubstantivepost').window('close');
            }

            function getCadre() {
                $('#cadre').text($('#cadreName').combobox('getText'));
                $('#cadreCode').val($('#cadreName').combobox('getValue'));
                $('#wincadre').window('close');
            }
            function getGroup() {
                $('#postGrp').val($('#empPostGroup').combobox('getValue'));
                $("#group").text($('#empPostGroup').combobox('getText'));

                $('#winsubstantiveGroup').window('close');

            }

            function SelectSpn(offCode, spc, offName, authName)
            {
                $.colorbox.close();
                $('#hidspc').val(spc);
                $('#hidOffice').val(offCode);
                $("#office").html(offName);
                $("#post").text(authName);
            }

            function saveCheck() {
                var oneDay = 24 * 60 * 60 * 1000;
                if ($('#txtFromDate').val() == '') {
                    alert("Please Enter NRC From Date");
                    $('#txtFromDate').focus();
                    return false;
                }
                if ($('#txtToDate').val() == '') {
                    alert("Please Enter NRC To Date");
                    $('#txtToDate').focus();
                    return false;
                }
                if ($('#postGrp').val() == '') {
                    alert("Post Group Cannot be Blank");
                    $('#postGrp').focus();
                    return false;
                }

                if ($('#nrcattchfile').val() == '') {
                    alert("Please Upload Necessary Document");
                    $('#nrcattchfile').focus();
                    return false;
                }

                var ftemp = $("#txtFromDate").val().split("-");
                var ttemp = $("#txtToDate").val().split("-");
                var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                if (fdt > tdt) {
                    alert("From Date must be less than To Date");
                    return false;
                }
                if ($('#hidspc').val() == '') {
                    alert("Designation cannot be blank.");
                    return false;
                }
                if ($('#cadreCode').val() == '') {
                    alert("Service to which the officer belongs cannot be blank.");
                    return false;
                }
                var diffdays = Math.round(Math.abs((fdt.getTime() - tdt.getTime()) / (oneDay)));

                if (diffdays > 120) {
                    if ($('#nrcReason2').val() == '') {
                        alert("Please Select NRC Reason");
                        $('#nrcReason2').focus();
                        return false;
                    }
                } else {
                    if ($('#nrcReason1').val() == '') {
                        alert("Please Select NRC Reason");
                        $('#nrcReason1').focus();
                        return false;
                    }
                }

            <%--if ($('#fiscalyear').val() == '2023-24') {
                if (diffdays > 120) {
                    alert("NRC period must be at less than or equal to 120 days.");
                    return false;
                } 
            }--%>

            }

            var allowedExtensions = /(\.pdf)$/i;
            function uploadValidation(me) {
                if ($(me).val() != '') {
                    var fileId = $(me).attr("id");
                    var fi = document.getElementById(fileId);
                    var fsize = fi.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 4096) {
                        alert("File too Big, please select a file less than 4mb");
                        $("#" + fileId).val('');
                        return false;
                    } else {
                        if (!allowedExtensions.exec($("#" + fileId).val())) {
                            alert('Please upload file having extensions .pdf only.');
                            $("#" + fileId).val('');
                            return false;
                        } else {
                            $("#clearbtn").show();
                            return true;
                        }

                    }
                }
            }
        </script>
    </head>
    <body>
        <form action="par/addNRC.htm" method="POST" commandName="ParMastForm" enctype="multipart/form-data">
            <input type="hidden" name="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <input type="hidden" name="hidparid" value='${parMastForm.parid}'/>
            <input type="hidden" name="fiscalyear" id="fiscalyear" value='${parMastForm.fiscalyear}'/>
            <input type="hidden" id="nrcreason" value="${parMastForm.nrcReason}"/>
            <input type="hidden" name="parType" id="parType" value='${parMastForm.parType}'/>

            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center" class="easyui-panel" title="Financial Year">
                    <table border="0" width="100%" cellpadding="0" cellspacing="0" style="padding-left:20px;">
                        <tbody>
                            <tr style="height:40px">
                                <td align="left" width="15%">
                                    Financial Year : <b><c:out value="${parMastForm.fiscalyear}"/></b>
                                </td>
                                <td align="center" width="15%">For The Period From:</td>
                                <td align="center" width="15%">
                                    <fmt:formatDate var="frmDate" value="${parMastForm.periodfrom}" pattern="dd-MMM-yyyy"/>
                                    <input type="text" name="periodfrom" id="txtFromDate" readonly="true" class="txtDate" value='${frmDate}'/>
                                </td>
                                <td align="center" width="5%">To:</td>
                                <td align="left" width="50%">
                                    <fmt:formatDate var="toDate" value="${parMastForm.periodto}" pattern="dd-MMM-yyyy"/>
                                    <input type="text" name="periodto" id="txtToDate" readonly="true" class="txtDate" value='${toDate}'/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div align="center">
                <div align="center" class="easyui-panel" title="NRC Information">
                    <table border="0" width="95%" cellpadding="0" cellspacing="0" style="margin-left: 8px;font-family:Verdana;font-size:14px;">
                        <tr style="height:40px;">
                            <td width="25%">
                                <b>HRMS ID</b>
                            </td>
                            <td width="10%">&nbsp;</td>
                            <td width="50%">
                                <c:out value="${users.empId}"/>
                                <input type="hidden" name="empid" value='${users.empId}'/>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td>
                                <b>Full name of the officer</b>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <c:out value="${users.fullName}"/>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td>
                                <b>Date of birth</b>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <fmt:formatDate var="dob" value="${users.dob}" pattern="dd-MMM-yyyy"/>
                                <c:out value="${dob}"/>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td>
                                <b>Service to which the officer belongs</b>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <c:if test="${not empty parMastForm.cadreName}">
                                    <span id="cadre">
                                        <c:out value="${parMastForm.cadreName}"/>
                                    </span>
                                    <input type="hidden" id="cadreName" name="cadreName" value='${parMastForm.cadreName}'/>
                                </c:if>
                                <c:if test="${empty parMastForm.cadreName}">
                                    <span id="cadre">
                                        <c:out value="${users.cadrename}"/>
                                    </span>
                                    <input type="hidden" id="cadreCode" name="cadreCode" value='${users.cadrecode}'/>
                                </c:if>
                                <c:if test="${empty users.cadrecode}">
                                    &nbsp;&nbsp;
                                    <a href="javascript:void(0)" id="change" onclick="javascript:addCadre();">
                                        <button type="button">Add Cadre</button>
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td> <b>Group to which the officer belongs</b> </td>
                            <td>&nbsp;</td>
                            <td>
                                <%-- <c:if test="${parMastForm.fiscalyear eq '2023-24'}"> --%>
                                <c:if test="${not empty parMastForm.postGroupAppraise && not empty parMastForm.submittedOn}">
                                    <span id="group">
                                        <c:out value="${parMastForm.postGroupAppraise}"/>
                                    </span>
                                    <input type="hidden" name="postGrp" id="postGrp" value='${parMastForm.postGroupAppraise}'/>
                                </c:if>
                                <%--<c:if test="${parMastForm.fiscalyear ne '2023-24'}"> --%>
                                <c:if test="${empty parMastForm.postGroupAppraise && empty parMastForm.submittedOn}">
                                    <span id="group"> 
                                        <c:out value="${users.postgrp}"/>
                                    </span>
                                    <input type="hidden" name="postGrp" id="postGrp" value='${users.postgrp}'/>
                                </c:if>
                                &nbsp;&nbsp;

                                <a href="javascript:void(0)" id="change" onclick="javascript:changeGroup();">
                                    <button class=" btn-info" style="padding: 0px 12px !important;height: 33px;" type="button">Change</button>
                                </a>
                            </td>
                        </tr>

                        <tr style="height:40px;">
                            <td>
                                <b>Designation during the period of report</b>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <c:if test="${not empty parMastForm.spc}">
                                    <span id="post">
                                        <c:out value="${parMastForm.spn}"/>
                                    </span>
                                    <input type="hidden" name="spc" id="hidspc" value='${parMastForm.spc}'/>
                                </c:if>
                                <c:if test="${empty parMastForm.spc}">
                                    <span id="post">
                                        <c:out value="${users.spn}"/>
                                    </span>
                                    <input type="hidden" name="spc" id="hidspc" value='${users.curspc}'/>
                                    &nbsp;&nbsp;
                                    <a href="javascript:void(0)" id="change" onclick="javascript:changepost();">
                                        <button type="button">Change</button>
                                    </a>
                                </c:if>


                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td><b>Office to where posted</b></td>
                            <td>&nbsp;</td>
                            <td>
                                <c:if test="${empty parMastForm.offCode}">
                                    <span id="office">
                                        <c:out value="${users.offname}"/>
                                    </span>
                                    <input type="hidden" name="offCode" id="hidOffice" value='${users.offcode}'/>
                                </c:if>    
                                <c:if test="${not empty parMastForm.offCode}">
                                    <span id="office">
                                        <c:out value="${parMastForm.offname}"/>
                                    </span>
                                    <input type="hidden" name="offCode" id="hidOffice" value='${parMastForm.offCode}'/>
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td><b>Head Quarter(if any)</b></td>
                            <td>
                                &nbsp; 
                            </td>
                            <td>
                                <input type="text" name="headqtr" value='${parMastForm.headqtr}' size="40"/> 
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td><b>Reason for NRC</b></td>
                            <td>
                                &nbsp; 
                            </td>
                            <td>
                                <c:if test="${empty parMastForm.nrcReason}">
                                    <span id="showNRCLessThan120Days" style="width:120%">
                                        <input name="nrcReason" id="nrcReason1" class="easyui-combobox" style="width:160%" data-options="valueField:'reasonid',textField:'reason',url:'GetNRCReasonListJSON.htm',editable:false"/>
                                    </span>
                                    <span id="showNRCMoreThan120Days">
                                        <select name="nrcReason" id="nrcReason2" class="form-control">
                                            <option value=""> Select </option>
                                            <option value="07"> Office Of the Demmitted Authorities in the PAR Recording Change </option>
                                            <option value="08"> Study Leave </option>
                                            <option value="09"> Deputation </option>
                                            <option value="10"> Maternity Leave </option>
                                            <option value="11"> Suspension </option>
                                            <option value="12"> Long term training </option>
                                            <option value="13"> Long Leave </option>
                                        </select>
                                    </span>
                                </c:if>
                                <c:if test="${not empty parMastForm.nrcReason}">
                                    <c:out value="${parMastForm.nrcReasonDetail}"/>
                                </c:if>

                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td><b>Remarks</b></td>
                            <td>
                                &nbsp; 
                            </td>
                            <td>
                                <textarea name="remarks" id="remarks" style="width:700px;height:60px;border:1px solid #000000;"><c:out value="${parMastForm.remarks}"/></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td><b>Upload Necessary Documents</b></td>
                            <td>
                                &nbsp; 
                            </td>
                            <td>
                                <c:if test="${not empty parMastForm.fiscalyear}">
                                    <c:set var="fsyr" value="${parMastForm.fiscalyear}"/>
                                    <%
                                        fiscalyear = (String) pageContext.getAttribute("fsyr");
                                    %>
                                </c:if>
                                <c:if test="${not empty parMastForm.parid}">
                                    <c:set var="pid" value="${parMastForm.parid}"/>
                                    <%
                                        parId = Integer.parseInt(pageContext.getAttribute("pid") + "");
                                    %>
                                </c:if>
                                <%
                                    downloadlink = "DownloadNRCAttch.htm?parId=" + parId + "&fyear=" + fiscalyear;
                                %>
                                <c:if test="${nrcattch != null && nrcattch != ''}">
                                    <a href='<%=downloadlink%>' style="text-decoration:none;">
                                        <c:out value="${nrcattch}"/>
                                    </a>
                                </c:if>
                                <c:if test="${nrcattch == null || nrcattch == ''}">
                                    <input type="file" name="nrcattchfile" id="nrcattchfile" onchange="return uploadValidation(this)"/>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                    <br />
                </div>
            </div>
            <div align="right">
                <div style="margin-top:10px;margin-bottom:0px;" class="easyui-panel" align="right">
                    <table border="0" width="100%">
                        <tr>
                            <td width="20%" align="left" style="padding-left:20px;">
                                <input type="submit" name="newNRC" value="Back" class="easyui-linkbutton c7" style="width: 90px;height: 30px;"/>
                            </td>
                            <td width="50%">
                                <c:if test="${NRCError != null && NRCError != ''}">
                                    <span style="display:block;text-align:center;color:red;"><c:out value="${NRCError}"/></span>
                                </c:if>
                                <c:if test="${NRCPeriodError != null && NRCPeriodError != ''}">
                                    <span style="display:block;text-align:center;color:red;"><c:out value="${NRCPeriodError}"/></span>
                                </c:if>
                            </td>
                            <td width="30%" align="right" style="padding-right:20px;">
                                <c:if test="${not empty parMastForm.parid}">
                                    <c:set var="pid" value="${parMastForm.parid}"/>

                                    <%
                                        parId = Integer.parseInt(pageContext.getAttribute("pid") + "");
                                        pdflink = "ViewNRCPDF.htm?parid=" + parId;
                                    %>
                                </c:if>
                                <c:if test="${parMastForm.parid != null && parMastForm.parid != '' && parMastForm.parid > 0}">
                                    <c:if test="${isClosed == 'N'}">
                                        <input type="submit" name="newNRC" value="Delete" class="easyui-linkbutton c5" style="width: 90px;height: 30px;" onclick="return confirm('Are you sure to Delete!')"/>
                                    </c:if>
                                    <a href='<%=pdflink%>' class="easyui-linkbutton c6" style="width: 90px;height: 30px;" target="_blank">Download</a>

                                </c:if>
                                <c:if test="${isClosed != 'Y'}">
                                    <c:if test="${parMastForm.parid == null || parMastForm.parid == '' || parMastForm.parid == 0}">
                                        <input type="submit" name="newNRC" value="Submit for NRC" class="easyui-linkbutton c1" style="width: 150px;height: 30px;" id="nextbtn" onclick="return saveCheck();"/>
                                    </c:if>
                                </c:if>
                                <c:if test="${isClosed == 'Y'}">
                                    <span style="display:block;text-align:center;color:red;">
                                        NRC for Financial year <c:out value="${parMastForm.fiscalyear}"/> is closed.
                                    </span>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <%-- Start - Content for Child Window --%>
            <div id="winsubstantivepost" class="easyui-window" title="Search" style="width:700px;height:400px;top:50px;padding:10px 20px" closed="true" buttons="#searchdlg-buttons"
                 data-options="iconCls:'icon-search',modal:true">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr style="height:40px;">
                        <td width="20%">Department</td>
                        <td width="80%">
                            <input name="empDepartment" id="empDepartment" class="easyui-combobox" style="width:500px;" data-options="valueField:'deptCode',textField:'deptName',editable:false" />
                        </td>
                    </tr>
                    <tr style="height:40px;">
                        <td>Office Name</td>
                        <td>
                            <input name="empOffice" id="empOffice" class="easyui-combobox" style="width:500px;" data-options="valueField:'offCode',textField:'offName',editable:false"/>
                        </td>
                    </tr>
                    <tr style="height:40px;">
                        <td>Post</td>
                        <td>
                            <input name="empPost" id="empPost" class="easyui-combobox" style="width:400px;" data-options="valueField:'spc',textField:'spn',editable:false">
                        </td>
                    </tr>

                    <tr style="height:40px;">
                        <td>&nbsp;</td>
                        <td>
                            <button type="button" onclick="getPost()">Ok</button>
                        </td>
                    </tr>
                </table>
            </div>

            <div id="wincadre" class="easyui-window" title="Search" style="width:700px;height:400px;top:50px;padding:10px 20px" closed="true"
                 data-options="iconCls:'icon-search',modal:true">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr style="height:40px;">
                        <td width="20%">Department</td>
                        <td width="80%">
                            <input name="cadreDepartment" id="cadreDepartment" class="easyui-combobox" style="width:500px;" data-options="valueField:'deptCode',textField:'deptName',editable:false" />
                        </td>
                    </tr>
                    <tr style="height:40px;">
                        <td>Cadre</td>
                        <td>
                            <input name="cadreName" id="cadreName" class="easyui-combobox" style="width:500px;" data-options="valueField:'value',textField:'label',editable:false"/>
                        </td>
                    </tr>
                    <tr style="height:40px;">
                        <td>&nbsp;</td>
                        <td>
                            <button type="button" onclick="getCadre()">Ok</button>
                        </td>
                    </tr>
                </table>
            </div>
            <%-- End - Content for Child Window --%>
            <%-- start - Content for group change Window --%>

            <div id="winsubstantiveGroup" class="easyui-window" title="Search" style="width:700px;height:400px;top:50px;padding:10px 20px" closed="true" buttons="#searchdlg-buttons"
                 data-options="iconCls:'icon-search',modal:true">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr style="height:40px;">
                        <td width="20%">Post Group</td>
                        <td width="80%">
                            <select id="empPostGroup" class="easyui-combobox" name="empPostGroup" style="width:200px;">  
                                <option value="">Select</option>
                                <option value="A">A</option>
                                <option value="B">B</option>
                            </select>

                            <%--<input name="empGroup" id="empGroup" /> --%>
                        </td>
                    </tr>


                    <tr style="height:40px;">
                        <td>&nbsp;</td>
                        <td>
                            <button type="button" onclick="getGroup()">Ok</button>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>
