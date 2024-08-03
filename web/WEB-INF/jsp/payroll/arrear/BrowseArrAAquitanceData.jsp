<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(window).load(function () {
                // Fill modal with content from link href
                $("#viewModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });

            });

            function onlyIntegerRange(e) {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }
            function deleteArrData(aqslno, pMonth, pyear, billNo, calcuniqueno) {
                if (confirm('Are You sure to Delete ?')) {
                    $.ajax({
                        method: "GET",
                        url: "deleteArrAqDtls.htm",
                        data: {aqslno: aqslno, payMonth: pMonth, payYear: pyear, billNo: billNo, calcuniqueno: calcuniqueno},
                        success: function (data) {
                            if (data.deleted > 0) {
                                $("#" + aqslno + "_" + pMonth + "_" + pyear).remove();
                            }

                        },
                        error: function () {
                            alert('Error Occured');
                        }
                    });
                }
            }
            function editArrData(aqslno, pMonth, pyear, billNo, calcuniqueno) {
                $.ajax({
                    type: "GET",
                    url: "editArrData.htm",
                    data: {aqslno: aqslno, pmonth: pMonth, pyear: pyear, billNo: billNo, calcuniqueno: calcuniqueno, autoincrid: 0},
                    success: function (data) {

                        $("#hidAqSlno").val(data.aqslno);
                        $("#hidCalcuniqueno").val(data.calcuniqueno);
                        $("#payMonth").val(data.payMonth);
                        $("#payYear").val(data.payYear);

                        $("#txtDrawnBillNo").val(data.drawnBillNo);

                        $("#drawnPayAmt").val(data.drawnPayAmt);
                        $("#drawnGpAmt").val(data.drawnGpAmt);
                        $("#drawnDaAmt").val(data.drawnDaAmt);

                        $("#duePayAmt").val(data.duePayAmt);
                        $("#dueGpAmt").val(data.dueGpAmt);
                        $("#dueDaAmt").val(data.dueDaAmt);

                        $('#myModal').modal('show');
                        $('#btnUpdateArrDtls').show();
                    },
                    error: function () {
                        alert('Error Occured');
                    }
                });
            }

            function UpdateArrDtlsData() {

                var aqSlno = $("#hidAqSlno").val();
                var calcuniqueno = $("#hidCalcuniqueno").val();
                var pMonth = $("#payMonth").val();
                var pYear = $("#payYear").val();

                var drawnBill = $("#txtDrawnBillNo").val();

                var duePay = $("#duePayAmt").val();
                var dueGp = $("#dueGpAmt").val();
                var dueDa = $("#dueDaAmt").val();

                var drawnPay = $("#drawnPayAmt").val();
                var drawnGp = $("#drawnGpAmt").val();
                var drawnDa = $("#drawnDaAmt").val();

                if (drawnBill == '') {
                    alert("Please Enter Drawn Bill No ");
                    $("#txtDrawnBillNo").focus();
                    return false;

                } else if (duePay == '') {
                    alert("Please Enter Due Pay ");
                    $("#duePayAmt").focus();
                    return false;

                } else if (dueGp == '') {
                    alert("Please Enter Due GP ");
                    return false;

                } else if (dueDa == '') {
                    alert("Please Enter Due DA ");
                    return false;

                }
                if (drawnPay == '') {
                    alert("Please Enter Drawn Pay ");
                    $("#drawnPayAmt").focus();
                    return false;

                } else if (drawnGp == '') {
                    alert("Please Enter Drawn GP ");
                    return false;

                } else if (drawnDa == '') {
                    alert("Please Enter Drawn DA ");
                    return false;
                }
                else {
                    $('#btnUpdateArrDtls').hide();
                    $.ajax({
                        type: "GET",
                        dataType: "json",
                        url: "updateArrDtls.htm",
                        data: {aqslno: aqSlno, calcuniqueno: calcuniqueno, payMonth: pMonth, payYear: pYear, duePayAmt: duePay, drawnBillNo: drawnBill,
                            dueGpAmt: dueGp, dueDaAmt: dueDa, drawnPayAmt: drawnPay, drawnGpAmt: drawnGp, drawnDaAmt: drawnDa},
                        success: function (data) {
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(3).html(data.duePayAmt);
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(4).html(data.dueGpAmt);
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(5).html(data.dueDaAmt);
                            var totDueAmt = data.duePayAmt + data.dueGpAmt + data.dueDaAmt;
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(6).html(totDueAmt);

                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(7).html(data.drawnPayAmt);
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(8).html(data.drawnGpAmt);
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(9).html(data.drawnDaAmt);
                            var totDrawnAmt = data.drawnPayAmt + data.drawnGpAmt + data.drawnDaAmt;
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(10).html(totDrawnAmt);

                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(11).html(data.drawnBillNo);

                            var totArr100 = totDueAmt - totDrawnAmt;
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear + "_" + calcuniqueno).children().eq(12).html(totArr100);

                            $('#myModal').modal('hide');
                        },
                        error: function () {
                            alert('Error Occured');
                            $('#btnUpdateArrDtls').hide();
                        }
                    });
                }
            }

            function AddNewArrData(aqslno) {
                $('#AddModal').modal('show');
                $("#addAqslno").val(aqslno);
            }

            function SaveArrDtlsData() {

                var aqSlno = $("#aqslno").val();
                var payMonth = $("#addPayMonth").val();
                var payYear = $("#addPayYear").val();
                var drwnBillNo = $("#addDrawnBillNo").val();

                var duePayAmt = $("#addDuePayAmt").val();
                var dueGpAmt = $("#addDueGpAmt").val();
                var dueDaAmt = $("#addDueDaAmt").val();

                var drawnPayAmt = $("#addDrawnPayAmt").val();
                var drawnGpAmt = $("#addDrawnGpAmt").val();
                var drawnDaAmt = $("#addDrawnDaAmt").val();

                if (payMonth == '') {
                    alert("Please Enter Pay Month ");
                    $("#addPayMonth").focus();
                    //return false;

                } else if (payYear == '') {
                    alert("Please Enter Pay Year ");
                    $("#addPayYear").focus();
                    //return false;

                } else if (drwnBillNo == '') {
                    alert("Please Enter Drawn Vide Bill No ");
                    $("#addDrawnBillNo").focus();
                    //return false;

                } else if (duePayAmt == '') {
                    alert("Please Enter Due Pay ");
                    $("#addDuePayAmt").focus();
                    //return false;

                } else if (dueGpAmt == '') {
                    alert("Please Enter Due GP ");
                    $("#addDueGpAmt").focus();
                    //return false;

                } else if (dueDaAmt == '') {
                    alert("Please Enter Due DA ");
                    $("#addDueDaAmt").focus();
                    //return false;

                } else if (drawnPayAmt == '') {
                    alert("Please Enter Drawn Pay ");
                    $("#addDrawnPayAmt").focus();
                    return false;

                } else if (drawnGpAmt == '') {
                    alert("Please Enter Drawn GP ");
                    $("#addDrawnGpAmt").focus();
                    return false;

                } else if (drawnDaAmt == '') {
                    alert("Please Enter Drawn DA ");
                    $("#addDrawnDaAmt").focus();
                    return false;

                } else {
                    $('#btnSaveArrDtls').hide();
                    $.ajax({
                        type: "GET",
                        url: "saveArrData.htm",
                        data: {aqslno: aqSlno, payMonth: payMonth, payYear: payYear, duePayAmt: duePayAmt, drawnBillNo: drwnBillNo,
                            dueGpAmt: dueGpAmt, dueDaAmt: dueDaAmt, drawnPayAmt: drawnPayAmt, drawnGpAmt: drawnGpAmt, drawnDaAmt: drawnDaAmt},
                        success: function (data) {
                            location.reload();
                        },
                        error: function () {
                            alert('Error Occured');
                            $('#btnSaveArrDtls').show();
                        }
                    });
                }
            }

            function checkAll() {
                var allRows = document.getElementsByTagName('input');
                var selectChk = document.getElementById('chkArrearEmployeeAllData');
                if (selectChk.checked == true) {
                    for (var i = 0; i < allRows.length; i++) {
                        if (allRows[i].type == 'checkbox') {
                            allRows[i].checked = true;
                        }
                    }
                } else {
                    for (var i = 0; i < allRows.length; i++) {
                        if (allRows[i].type == 'checkbox') {
                            allRows[i].checked = false;
                        }
                    }
                }
            }

            function deleteSelectedRows(billNo) {
                //alert("billNo is: "+billNo);
                var checkBoxlength = $("input[name=chkArrearEmployeeData]:checked").length;
                if (checkBoxlength == 0) {
                    alert("Please select row(s) to Remove.");
                } else {
                    var checkedRows = "";
                    $("input[name=chkArrearEmployeeData]:checked").each(function () {
                        if (checkedRows == "") {
                            checkedRows = $(this).val();
                        } else {
                            checkedRows = checkedRows + "," + $(this).val();
                        }
                    });
                    //alert("Checked Rows are: "+checkedRows);
                    var ret;
                    ret = confirm("Are you sure to remove selected row(s)?");
                    if (ret == true) {
                        //return true;
                        window.location = "deleteMassArrAqDtls.htm?aqSlNoArr=" + checkedRows + "&billNo=" + billNo;
                    } else {
                        //return false;
                    }
                }
            }

        </script>
    </head>
    <body>
        <form:form action="backToBillListPage.htm" commandName="ArrAqDtlsModel" method="POST">
            <form:hidden path="aqslno"/>
        </form:form>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" align="center" style="font-weight: bold;">
                    <c:if test="${not empty HeaderDataList}">
                        <c:forEach var="hdrData" items="${HeaderDataList}">
                            <b> REVISED ARREAR PAY BILL  FROM <c:out value="${hdrData.fromMonth}"/> / <c:out value="${hdrData.fromYear}"/> TO 
                                <c:out value="${hdrData.toMonth}"/> / <c:out value="${hdrData.toYear}"/> OF MISCELLANEOUS, <br/>
                                <c:out value="${OffName}"/>, <c:out value="${DeptName}"/>. <br/>
                                Bill No:- <c:out value="${hdrData.billDesc}"/> </b>
                            </c:forEach>
                        </c:if>
                </div>

                <div class="panel-heading" align="center" style="font-weight: bold;">
                    <table width="100%" border="0">
                        <tr>
                            <th width="10%" style="text-align: center;">Name :</th>
                            <td width="20%" align="left"><c:out value="${arrAqMastBean.empName}"/> <b style="color: #0000FF;">(${arrAqMastBean.empCode})</b></td>
                            <th width="15%" style="text-align: center;">Designation :</th>
                            <td width="30%" align="left"><c:out value="${arrAqMastBean.curDesg}"/></td>
                            <td width="8%" align="center"> 
                                <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                    &nbsp;&nbsp;
                                </c:if>
                                <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                    <a href="javascript:AddNewArrData('${arrAqMastBean.aqSlNo}',${arrAqMastBean.payMonth},${arrAqMastBean.payYear})" class="btn btn-default">Add New</a>
                                </c:if>
                            </td>
                            <td width="8%" align="left">
                                <a href="javascript:deleteSelectedRows('${arrAqMastBean.billNo}');" class="btn btn-danger">Remove</a>
                            </td>
                            <td width="8%" align="center"> <!-- <input type="submit" value="Back"/> --> </td>
                        </tr>
                    </table>
                </div>

                <div class="panel-body">
                    <table class="table table-bordered table-hover">
                        <tr>
                            <th rowspan="2" width="3%">
                                <input type="checkbox" id="chkArrearEmployeeAllData" class="form-control" value="0" onclick="checkAll()"/>
                            </th>
                            <th rowspan="2"  width="3%">Sl No </th>
                            <th rowspan="2" width="9%">Month</th>
                            <th colspan="4" width="18%">Due</th>
                            <th colspan="4" width="18%">Drawn</th>
                            <th rowspan="2" width="11%">Drawn Vide <br/> Bill No.</th>
                            <th rowspan="2" width="8%">Arrear 100%</th>                            
                                <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                &nbsp;&nbsp;
                            </c:if>
                            <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                <th rowspan="2" width="5%" style="text-align: center;">Edit</th>
                                </c:if>

                            <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                &nbsp;&nbsp;
                            </c:if>
                            <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                <%--<th rowspan="2" width="5%" style="text-align: center;">Delete</th>--%>
                            </c:if>

                        </tr>
                        <tr>
                            <th>Pay</th>
                            <th>G.P</th>
                            <th>D.A</th>
                            <th>Total</th>
                            <th>Pay</th>
                            <th>G.P</th>
                            <th>D.A</th>
                            <th>Total</th>
                        </tr>
                        <c:if test="${not empty arrAqMastBean.arrDetails}">
                            <c:forEach var="arrAqDtls" items="${arrAqMastBean.arrDetails}" varStatus="cnt">
                                <tr style="height: 25px" id="${arrAqDtls.aqslno}_${arrAqDtls.payMonth}_${arrAqDtls.payYear}_${arrAqDtls.calcuniqueno}">
                                    <td>
                                        <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}">
                                            <input type="checkbox" class="form-control" name="chkArrearEmployeeData" id="chkArrearEmployeeData" value="${arrAqDtls.aqslno}@${arrAqDtls.payMonth}@${arrAqDtls.payYear}@${arrAqDtls.calcuniqueno}"/>
                                        </c:if>
                                    </td>
                                    <td>${cnt.index+1}</td>
                                    <td><c:out value="${arrAqDtls.payMonthName}"/> - <c:out value="${arrAqDtls.payYear}"/></td>
                                    <td><c:out value="${arrAqDtls.duePayAmt}"/></td> 
                                    <td><c:out value="${arrAqDtls.dueGpAmt}"/></td>
                                    <td><c:out value="${arrAqDtls.dueDaAmt}"/></td>
                                    <c:set var = "duetotal" value = "${arrAqDtls.dueTotalAmt}"/>
                                    <td style="font-weight: bold;">${arrAqDtls.dueTotalAmt}</td> 

                                    <td><c:out value="${arrAqDtls.drawnPayAmt}"/></td> 
                                    <td><c:out value="${arrAqDtls.drawnGpAmt}"/></td>
                                    <td><c:out value="${arrAqDtls.drawnDaAmt}"/></td>
                                    <c:set var = "drwantotal" value="${arrAqDtls.drawnTotalAmt}"/>
                                    <td style="font-weight: bold;">${arrAqDtls.drawnTotalAmt}</td>

                                    <td><c:out value="${arrAqDtls.drawnBillNo}"/></td>

                                    <td align="center" style="font-weight: bold;">${arrAqDtls.arrear100}</td>                                    

                                    <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                        &nbsp;&nbsp;
                                    </c:if>
                                    <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                        <td align="center">
                                            <a href="javascript:editArrData('${arrAqDtls.aqslno}',${arrAqDtls.payMonth},${arrAqDtls.payYear},${arrAqMastBean.billNo},${arrAqDtls.calcuniqueno})" class="btn btn-default"><img src="images/edit.png" alt="Edit"/></a>
                                        </td>
                                    </c:if>

                                    <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                        &nbsp;&nbsp;
                                    </c:if>
                                    <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                        <%--<td align="center">
                                            <a href="javascript:deleteArrData('${arrAqDtls.aqslno}',${arrAqDtls.payMonth},${arrAqDtls.payYear},${arrAqMastBean.billNo},${arrAqDtls.calcuniqueno})" class="btn btn-default"><img src="images/delete_icon.png" alt="Delete"/></a>
                                        </td>--%>
                                    </c:if>    
                                </tr> 
                            </c:forEach>
                        </c:if>  
                        <tr>
                            <th colspan="11" style="text-align: right;background-color: #F5F5F5;"> Grand Total &nbsp;&nbsp;&nbsp;&nbsp;</th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;"><c:out value="${arrAqMastBean.grandTotArr100}"/></th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;">&nbsp;</th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;">&nbsp;</th>
                        </tr>
                        <tr>
                            <th colspan="11" style="text-align: right;background-color: #F5F5F5;">40% &nbsp;&nbsp;&nbsp;&nbsp;</th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;"><c:out value="${arrAqMastBean.grandTotArr40}"/></th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;">&nbsp;</th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;">&nbsp;</th>
                        </tr>
                        <c:if test="${arrAqMastBean.percentageArraer eq '0'  && (arrAqMastBean.arrearpay ne arrAqMastBean.fullArrearpay)}">
                            <tr>
                                <th colspan="11" style="text-align: right;background-color: #F5F5F5;">60% &nbsp;&nbsp;&nbsp;&nbsp;</th>
                                <th width="8%" style="text-align: center;background-color: #F5F5F5;">
                                    <c:out value="${arrAqMastBean.grandTotArr60}"/>
                                </th>
                                <th width="8%" style="text-align: center;background-color: #F5F5F5;">&nbsp;</th>
                                <th width="8%" style="text-align: center;background-color: #F5F5F5;">&nbsp;</th>
                            </tr>
                        </c:if>
                        <tr>
                            <th colspan="11" style="text-align: right;background-color: #F5F5F5;"> Income Tax &nbsp;&nbsp;&nbsp;&nbsp;</th>
                            <th width="8%" style="text-align: center;">&nbsp;</th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;">${arrAqMastBean.incomeTaxAmt}</th> 
                            <th width="8%" style="text-align: center;">&nbsp;</th>
                        </tr>
                        <tr>
                            <th colspan="11" style="text-align: right;background-color: #F5F5F5;"> <c:out value="${acctType}"/> &nbsp;&nbsp;&nbsp;&nbsp;</th>
                            <th width="8%" style="text-align: center;">&nbsp;</th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;">${arrAqMastBean.cpfHead}</th> 
                            <th width="8%" style="text-align: center;">&nbsp;</th>
                        </tr>
                        <tr>
                            <th colspan="11" style="text-align: right;background-color: #F5F5F5;"> PT &nbsp;&nbsp;&nbsp;&nbsp;</th>
                            <th width="8%" style="text-align: center;">&nbsp;</th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;">${arrAqMastBean.pt}</th> 
                            <th width="8%" style="text-align: center;">&nbsp;</th>
                        </tr>
                        <tr>
                            <th colspan="11" style="text-align: right;background-color: #F5F5F5;"> Net Payable &nbsp;&nbsp;&nbsp;&nbsp;</th>
                            <th width="8%" style="text-align: right;background-color: #F5F5F5;">&nbsp;</th>
                            <th width="8%" style="text-align: center;background-color: #F5F5F5;">
                                <c:if test="${arrAqMastBean.percentageArraer eq '0'  && (arrAqMastBean.arrearpay eq arrAqMastBean.fullArrearpay)}">
                                    ${arrAqMastBean.grandTotArr100-(arrAqMastBean.incomeTaxAmt+arrAqMastBean.pt+arrAqMastBean.cpfHead)}
                                </c:if>
                                <c:if test="${arrAqMastBean.percentageArraer eq '0' && (arrAqMastBean.arrearpay ne arrAqMastBean.fullArrearpay)}">
                                    ${(arrAqMastBean.grandTotArr40) - (arrAqMastBean.incomeTaxAmt + arrAqMastBean.cpfHead + arrAqMastBean.pt)}
                                </c:if>
                                <c:if test="${arrAqMastBean.percentageArraer ne '0'}">
                                    ${(arrAqMastBean.grandTotArr40) - (arrAqMastBean.incomeTaxAmt + arrAqMastBean.cpfHead + arrAqMastBean.pt)}
                                </c:if>
                            </th>
                            <th width="8%" style="text-align: right;background-color: #F5F5F5;">&nbsp;</th>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer"> &nbsp; </div>
            </div>
        </div>

        <!-- Modal -->
        <div id="AddModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="border: 1px solid #0000FF;">
                <!-- Modal content-->
                <div class="modal-content" >
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><b style="color: #0000FF;">Add Arrear Data</b></h4>
                    </div>
                    <div class="modal-body">
                        <table align="center" cellpadding="2" cellspacing="2">
                            <tr style="height: 40px">
                                <th>Pay Month : </th>
                                <td><select name="addPayMonth" id="addPayMonth" class="form-control"  width="50%">
                                        <option value="0">JANUARY</option>
                                        <option value="1">FEBRUARY</option>
                                        <option value="2">MARCH</option>
                                        <option value="3">APRIL</option>
                                        <option value="4">MAY</option>
                                        <option value="5">JUNE</option>
                                        <option value="6">JULY</option>
                                        <option value="7">AUGUST</option>
                                        <option value="8">SEPTEMBER</option> 
                                        <option value="9">OCTOBER</option>
                                        <option value="10">NOVEMBER</option>
                                        <option value="11">DECEMBER</option>
                                    </select></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Pay Year : </th>
                                <td>
                                    <select name="addPayYear" id="addPayYear" class="form-control" width="50%">
                                        <option value="2016">2016</option>
                                        <option value="2017">2017</option>
                                        <option value="2018">2018</option>
                                        <option value="2019">2019</option>
                                        <option value="2020">2020</option>
                                        <option value="2021">2021</option>
                                        <option value="2022">2022</option>
                                        <option value="2023">2023</option>
                                        <option value="2024">2024</option>
                                    </select>
                                </td>
                            </tr>

                            <tr style="height: 40px">
                                <th>Drawn Vide Bill No : &nbsp;</th>
                                <td><input type="text" name="addDrawnBillNo" id="addDrawnBillNo" maxlength="39"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Due Pay : &nbsp;</th>
                                <td><input type="text" name="addDuePayAmt" id="addDuePayAmt" maxlength="6" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Due G.P : &nbsp;</th>
                                <td><input type="text" name="addDueGpAmt" id="addDueGpAmt"  maxlength="5" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Due D.A : &nbsp;</th>
                                <td><input type="text" name="addDueDaAmt" id="addDueDaAmt" maxlength="6" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 10px">
                                <td colspan="2" style="border-bottom: 3px solid #0000FE;"></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Drawn Pay : &nbsp;</th>
                                <td><input type="text" name="addDrawnPayAmt" id="addDrawnPayAmt" maxlength="6" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Drawn G.P : &nbsp;</th>
                                <td><input type="text" name="addDrawnGpAmt" id="addDrawnGpAmt" maxlength="5" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Drawn D.A : &nbsp;</th>
                                <td><input type="text" name="addDrawnDaAmt" id="addDrawnDaAmt" maxlength="6" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr> 
                            <tr style="height: 20px">     
                                <th>&nbsp;</th>
                                <td><input type="hidden" name="addAqslno" id="addAqslno"/></td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btnSaveArrDtls" onclick="SaveArrDtlsData()">Save</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="border: 1px solid #0000FF;">
                <!-- Modal content-->
                <div class="modal-content" >
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><b style="color: #0000FF;">Update Arrear Data</b></h4>
                    </div>
                    <div class="modal-body">
                        <table align="center" cellpadding="2" cellspacing="2">
                            <tr style="height: 40px">
                                <th>Drawn Vide Bill No : &nbsp;</th>
                                <td><input type="text" name="txtDrawnBillNo" id="txtDrawnBillNo" maxlength="39"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Due Pay : &nbsp;</th>
                                <td><input type="text" name="duePayAmt" id="duePayAmt" maxlength="6" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Due G.P : &nbsp;</th>
                                <td><input type="text" name="dueGpAmt" id="dueGpAmt"  maxlength="5" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Due D.A : &nbsp;</th>
                                <td><input type="text" name="dueDaAmt" id="dueDaAmt" maxlength="6" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 10px">
                                <td colspan="2" style="border-bottom: 3px solid #0000FE;"></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Drawn Pay : &nbsp;</th>
                                <td><input type="text" name="drawnPayAmt" id="drawnPayAmt" maxlength="6" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Drawn G.P : &nbsp;</th>
                                <td><input type="text" name="drawnGpAmt" id="drawnGpAmt" maxlength="5" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Drawn D.A : &nbsp;</th>
                                <td><input type="text" name="drawnDaAmt" id="drawnDaAmt" maxlength="6" onkeypress="return onlyIntegerRange(event)"/></td>
                            </tr>
                            <tr style="height: 20px">     
                                <th><input type="hidden" name="aqslno" id="hidAqSlno"/><input type="hidden" name="calcuniqueno" id="hidCalcuniqueno"/></th>
                                <td><input type="hidden" name="payMonth" id="payMonth"/> <input type="hidden" name="payYear" id="payYear"/> </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btnUpdateArrDtls" onclick="UpdateArrDtlsData()">Update</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
