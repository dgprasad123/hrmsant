<%-- 
    Document   : LtaScheduleInterface
    Created on : 20 Apr, 2017, 11:21:22 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">



        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#sltMonth').combobox({
                    onChange: function (record) {
                        var month = $('#sltMonth').combobox('getValue');
                        var sltYear = $('#sltYear').combobox('getValue');
                        var sltTrcode = $('#sltTrcode').combobox('getValue');
                        if (sltYear != '') {
                            $('#sltmajorHEad').combobox('clear');
                            var url = 'getMajorHeadListTreasuryWise.htm?aqyear=' + sltYear + '&aqmonth=' + month + '&trcode=' + sltTrcode;
                            $('#sltmajorHEad').combobox('reload', url);
                        } else {
                            if (sltYear != '') {
                                alert('Please select Year');
                            }
                        }
                    }
                });

                $('#sltYear').combobox({
                    onSelect: function (param) {
                        $('#sltMonth').combobox('clear');

                    }
                });

                $('#sltTrcode').combobox({
                    onSelect: function (param) {
                        $('#sltMonth').combobox('clear');

                    }
                });

            });



            function getvoucherlist() {
                var month = $('#sltMonth').combobox('getValue');
                var sltYear = $('#sltYear').combobox('getValue');
                var sltTrcode = $('#sltTrcode').combobox('getValue');
                var majorhead = $('#sltmajorHEad').combobox('getValue');
                if (majorhead != '') {
                    $('#dgVch').datagrid({
                        url: "getVoucherListMajorHeadWise.htm?majorhead=" + majorhead + "&aqyear=" + sltYear + "&aqmonth=" + month + "&trcode=" + sltTrcode
                    });
                } else {
                    alert('Please select major head.');
                }
            }






            function viewHBASchedulePDF(billid) {
                var url = "LoanAdvSchedulePDF.htm?schedule=HBA&billNo=" + billid;
                window.open(url, "_blank");
            }

            function viewCOMPSchedulePDF(billid) {
                var url = "LoanAdvSchedulePDF.htm?schedule=CMPA&billNo=" + billid;
                window.open(url, "_blank");
            }

            function viewMCASchedulePDF(billid) {
                var url = "LoanAdvSchedulePDF.htm?schedule=MCA&billNo=" + billid;
                window.open(url, "_blank");
            }

            function viewMOPASchedulePDF(billid) {
                var url = "LoanAdvSchedulePDF.htm?schedule=MOPA&billNo=" + billid;
                window.open(url, "_blank");
            }

            function viewVehicleSchedulePDF(billid) {
                var url = "LoanAdvSchedulePDF.htm?schedule=VE&billNo=" + billid;
                window.open(url, "_blank");
            }

            function viewGPFSchedulePDF(billid) {
                //var url = "GPFScheduleHTML.htm?billNo=" + billid;
                var url = "RedirectGPFScheduleReport.htm?billNo=" + billid;
                window.open(url, "_blank");
            }
            function viewAcquitancePDF(billid) {
                var url = "aqbillreport.htm?format=f1&billNo=" + billid;
                window.open(url, "_blank");
            }

            function downloadAquitanceSchedulePDF(billid,vchNo) {
                
                var month = $('#sltMonth').combobox('getValue');
                var sltYear = $('#sltYear').combobox('getValue');
                var treasury = $('#sltTrcode').combobox('getValue');
                var majorhead = $('#sltmajorHEad').combobox('getValue');
                
                //var url = "DownloadAGAquitancePDF.htm?billNo=" + billid + "&month=" + month + "&year=" + sltYear;
                var url = "DownloadAGAquitancePDF.htm?billNo=" + billid + "&month=" + month + "&year=" + sltYear + "&treasury=" + treasury + "&majorhead=" + majorhead + "&vchno=" + vchNo;
                window.open(url, "_blank");
            }


        </script>
    </head>
    <body>
        <div id="wrapper">
            <%--<jsp:include page="../tab/ServiceConditionAdminMenu.jsp"/>--%>
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>       
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                LTA Schedule
                                <small> </small>
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>   
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> 
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <div class="row">
                        <form>
                            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                                <thead></thead>
                                <tr style="height:30">
                                    <td align="center" width="16%"><b>Select Treasury:</b>&nbsp;<span style="color: red">*</span></td>
                                    <td width="16%">
                                        <input class="easyui-combobox" id="sltTrcode" name="sltTrcode" style="width:50%;height:26px;" data-options="valueField:'agtreasuryCode',textField:'treasuryName',url:'getAGTreasuryListJSON.htm'">                                
                                    </td>
                                    <td align="center" width="17%"><b>Year:</b>&nbsp;<span style="color: red">*</span></td>
                                    <td width="16%">
                                        <input class="easyui-combobox" id="sltYear" name="sltYear" style="width:50%;height:26px;" data-options="valueField:'value',textField:'label',url:'getYearList.htm'">                                
                                    </td>
                                    <td width="17%" align="center"><b>Month:</b>&nbsp;<span style="color: red">*</span></td>
                                    <td width="17%">
                                        <select name="sltMonth" id="sltMonth" class="easyui-combobox" style="width:60%;">
                                            <option value="0">  </option>
                                            <option value="1"> JANUARY </option>
                                            <option value="2"> FEBRUARY </option>
                                            <option value="3"> MARCH </option>
                                            <option value="4"> APRIL </option>
                                            <option value="5"> MAY </option>
                                            <option value="6"> JUNE </option>
                                            <option value="7"> JULY </option>
                                            <option value="8"> AUGUST </option>
                                            <option value="9"> SEPTEMBER </option>
                                            <option value="10"> OCTOBER </option>
                                            <option value="11"> NOVEMBER </option>
                                            <option value="12"> DECEMBER </option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>


                                    <td align="center" ><b>Select Major Head:</b>&nbsp;<span style="color: red">*</span></td>
                                    <td>
                                        <input class="easyui-combobox" id="sltmajorHEad" name="sltmajorHEad" style="width:50%;height:26px;" data-options="valueField:'value',textField:'label'">                                
                                    </td>
                                    <td colspan="3" align="center">                                
                                        <input type="button" value="Get Voucher List" class="sessionstyle" onclick="return getvoucherlist()"/>                        
                                    </td>
                                </tr>
                            </table>
                            <div style="margin:20px 0;"></div>

                            <table id="dgVch" class="easyui-datagrid" title="Basic DataGrid" style="width:100%;height:350px"
                                   data-options="singleSelect:true,fitColumns:true,method:'get'">
                                <thead>
                                    <tr>
                                        <th data-options="field:'vchno',align:'center'">Voucher No</th>
                                        <th data-options="field:'hbabillId',align:'center',formatter:viewHBASchedule">HBA Schedule</th>
                                        <th data-options="field:'mcaBillId',align:'center',formatter:viewMCASchedule">MCA Schedule</th>
                                        <th data-options="field:'billid',align:'center',formatter:viewMOPASchedule">MOPA Schedule</th>
                                        <th data-options="field:'veBillId',align:'center',formatter:viewVESchedule">Vehicle Schedule</th>
                                        <th data-options="field:'compaBillId',align:'center',formatter:viewCOMPASchedule">Computer Adv Schedule</th>
                                        <th data-options="field:'gpfBillId',align:'center',formatter:viewGPFSchedule">GPF Schedule</th>
                                        <th data-options="field:'rrrr',align:'center',formatter:viewAquitancechedule"> View Acquitance</th>
                                        <th data-options="field:'temp',align:'center',formatter:downloadAquitanceSchedule"> Download Digitally Signed Aquitance</th>

                                    </tr>
                                </thead>
                            </table>
                            <script type="text/javascript">

                                function viewAquitancechedule(val, row) {
                                    var data = row.billid;
                                    return "<a href='javascript:void(0)' onclick='viewAcquitancePDF(\"" + data + "\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";

                                }

                                function downloadAquitanceSchedule(val, row) {
                                    var data = row.billid;
                                    var vchno = row.vchno;
                                    //return "<a href='javascript:void(0)' onclick='downloadAquitanceSchedulePDF(\"" + data + "\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";
                                     return "<a href='javascript:void(0)' onclick='downloadAquitanceSchedulePDF(\"" + data + "\",\""+vchno+"\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";

                                }

                                function viewGPFSchedule(val, row) {
                                    var data = row.billid;
                                    return "<a href='javascript:void(0)' onclick='viewGPFSchedulePDF(\"" + data + "\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";

                                }

                                function viewMOPASchedule(val, row) {
                                    var data = row.billid;
                                    return "<a href='javascript:void(0)' onclick='viewMOPASchedulePDF(\"" + data + "\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";

                                }

                                function viewMCASchedule(val, row) {
                                    var data = row.mcaBillId;
                                    return "<a href='javascript:void(0)' onclick='viewMCASchedulePDF(\"" + data + "\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";

                                }

                                function viewVESchedule(val, row) {
                                    var data = row.mcaBillId;
                                    return "<a href='javascript:void(0)' onclick='viewVehicleSchedulePDF(\"" + data + "\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";

                                }

                                function viewHBASchedule(val, row) {
                                    var data = row.hbabillId;
                                    return "<a href='javascript:void(0)' onclick='viewHBASchedulePDF(\"" + data + "\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";

                                }



                                function viewCOMPASchedule(val, row) {
                                    var data = row.compaBillId;
                                    return "<a href='javascript:void(0)' onclick='viewCOMPSchedulePDF(\"" + data + "\")'><img src='images/pdf_icon.png' width='15' height='15'></a>";

                                }

                                $('#dgVch').datagrid({
                                    rowStyler: function (index, row) {
                                        if (index % 2 == 0) {
                                            return 'background-color:#EBEBED;color:#969696;font-weight:bold;';
                                        } else {
                                            return 'background-color:#FFFFFF;color:#969696;font-weight:bold;';
                                        }
                                    }
                                });
                            </script>

                        </form>

                    </div>

                </div>
            </div>
        </div>
    </body>
</html>
