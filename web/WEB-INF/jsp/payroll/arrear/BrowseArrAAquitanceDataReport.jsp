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
            $(window).load(function() {
                // Fill modal with content from link href
                $("#viewModal").on("show.bs.modal", function(e) {
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

            function editArrData(aqslno, pMonth, pyear) {
                $.ajax({
                    type: "GET",
                    url: "editArrData.htm",
                    data: {aqslno: aqslno, pmonth: pMonth, pyear: pyear},
                    success: function(data) {

                        $("#hidAqSlno").val(data.aqslno);
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
                    },
                    error: function() {
                        alert('Error Occured');
                    }
                });
            }

            function UpdateArrDtlsData() {

                var aqSlno = $("#hidAqSlno").val();
                var pMonth = $("#payMonth").val();
                var pYear = $("#payYear").val();

                var duePay = $("#duePayAmt").val();
                var dueGp = $("#dueGpAmt").val();
                var dueDa = $("#dueDaAmt").val();

                var drawnPay = $("#drawnPayAmt").val();
                var drawnGp = $("#drawnGpAmt").val();
                var drawnDa = $("#drawnDaAmt").val();

                if (duePay == '') {
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
                    $.ajax({
                        type: "GET",
                        dataType: "json",
                        url: "updateArrDtls.htm",
                        data: {aqslno: aqSlno, payMonth: pMonth, payYear: pYear, duePayAmt: duePay,
                            dueGpAmt: dueGp, dueDaAmt: dueDa, drawnPayAmt: drawnPay, drawnGpAmt: drawnGp, drawnDaAmt: drawnDa},
                        success: function(data) {
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(2).html(data.duePayAmt);
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(3).html(data.dueGpAmt);
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(4).html(data.dueDaAmt);
                            var totDueAmt = data.duePayAmt + data.dueGpAmt + data.dueDaAmt;
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(5).html(totDueAmt);

                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(6).html(data.drawnPayAmt);
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(7).html(data.drawnGpAmt);
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(8).html(data.drawnDaAmt);
                            var totDrawnAmt = data.drawnPayAmt + data.drawnGpAmt + data.drawnDaAmt;
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(9).html(totDrawnAmt);

                            var totArr100 = totDueAmt - totDrawnAmt;
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(11).html(totArr100);

                            var totArr40 = totArr100 * 0.4;
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(12).html(totArr40);

                            var totArr60 = totArr100 * 0.6;
                            $("#" + aqSlno + "_" + pMonth + "_" + pYear).children().eq(13).html(totArr60);

                            $('#myModal').modal('hide');
                        },
                        error: function() {
                            alert('Error Occured');
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
                    $.ajax({
                        type: "GET",
                        url: "saveArrData.htm",
                        data: {aqslno: aqSlno, payMonth: payMonth, payYear: payYear, duePayAmt: duePayAmt, drawnBillNo: drwnBillNo,
                            dueGpAmt: dueGpAmt, dueDaAmt: dueDaAmt, drawnPayAmt: drawnPayAmt, drawnGpAmt: drawnGpAmt, drawnDaAmt: drawnDaAmt},
                        success: function(data) {
                            location.reload();
                        },
                        error: function() {
                            alert('Error Occured');
                        }
                    });
                }
            }

        </script>
        <style type="text/css">
            .table > tbody > tr > td, .table > tbody > tr > th{padding:0px 8px 0px 8px;}

        </style>
    </head>
    <body style="margin:10px;">
        <form:form action="backToBillListPage.htm" commandName="ArrAqDtlsModel" method="POST">
            <form:hidden path="aqslno"/>
        </form:form>

        <table style="font-size:10pt;" width="100%" class="table table-bordered">
            <tr>
                <td colspan="14" style="text-align:center;background:#EAEAEA;" bgcolor="#EAEAEA">
                    <c:if test="${not empty HeaderDataList}">
                        <c:forEach var="hdrData" items="${HeaderDataList}">
                            <b> REVISED ARREAR PAY BILL  FROM <c:out value="${hdrData.fromMonth}"/> / <c:out value="${hdrData.fromYear}"/> TO 
                                <c:out value="${hdrData.toMonth}"/> / <c:out value="${hdrData.toYear}"/> OF MISCELLANEOUS, 
                                <c:out value="${OffName}"/>, <c:out value="${DeptName}"/>
                                Bill No:- <c:out value="${hdrData.billDesc}"/> </b>
                            </c:forEach>
                        </c:if>                                
                </td>
            </tr>
            <tr>
                <td colspan="8" style="font-size:11pt;"><strong>Name : </strong>${arrAqMastBean.empName} <b style="color: #0000FF;">(${arrAqMastBean.empCode})</b></td>
                <td colspan="6" style="font-size:11pt;"><strong>Designation : </strong>${arrAqMastBean.curDesg}</td>
            </tr>
            <tr>
                <th rowspan="2"  width="3%">Sl No </th>
                <th rowspan="2" width="9%" style="text-align:center;">Month</th>
                <th colspan="4" width="18%" style="text-align:center;">Due</th>
                <th colspan="4" width="18%" style="text-align:center;">Drawn</th>
                <th rowspan="2" width="11%">Drawn Vide <br/> Bill No.</th>
                <th rowspan="2" width="8%" style="text-align:right;">Arrear 100%</th>                
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
                    <tr style="height: 25px" id="${arrAqDtls.aqslno}_${arrAqDtls.payMonth}_${arrAqDtls.payYear}">
                        <td>${cnt.index+1}</td>
                        <td nowrap style="text-align:center;"><c:out value="${arrAqDtls.payMonthName}"/> - <c:out value="${arrAqDtls.payYear}"/></td>
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

                        <td align="center" style="font-weight: bold;text-align:right;">${arrAqDtls.arrear100}</td>
                        
                    </tr> 
                </c:forEach>
            </c:if>  
            <tr>
                <th colspan="11" style="text-align: right;"> Grand Total &nbsp;&nbsp;&nbsp;&nbsp;</th>
                <th width="8%" style="text-align: right;"><c:out value="${arrAqMastBean.grandTotArr100}"/></th>                
            </tr>
            <tr>
                <th colspan="11" style="text-align: right;"> Arrear 40% &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;"><c:out value="${arrAqMastBean.grandTotArr40}"/></th>                
            </tr>
            <tr>
                <th colspan="11" style="text-align: right;"> Income Tax &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;">${arrAqMastBean.incomeTaxAmt}</th>                 
            </tr>
            <tr>
                <th colspan="11" style="text-align: right;"> CPF &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;">${arrAqMastBean.cpfHead}</th>                 
            </tr>                          
            <tr>
                <th colspan="11" style="text-align: right;"> PT &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;">${arrAqMastBean.pt}</th>                 
            </tr>                        
            <tr>
                <th colspan="11" style="text-align: right;background-color: #F5F5F5;"> Net Payable &nbsp;&nbsp;&nbsp;&nbsp;</th>                
                <th width="8%" style="text-align: right;background-color: #F5F5F5;">${(arrAqMastBean.grandTotArr40) - (arrAqMastBean.incomeTaxAmt) - (arrAqMastBean.pt)-(arrAqMastBean.cpfHead)}</th>                
            </tr>
        </tbody>
    </table>




</body>
</html>