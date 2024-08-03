<%-- 
    Document   : Main24Q
    Created on : 5 Nov, 2019, 5:20:38 PM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>26Q Form:: Challan</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('.txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function addrow() {
                trid1 = $('#tab1 tr.dataTR').length;
                if ($('#pan' + trid1).val() == '')
                {
                    alert("Please enter PAN.");
                    $('#pan' + trid1)[0].focus();
                    return false;
                }
                if ($('#deducteeName' + trid1).val() == '')
                {
                    alert("Please enter Deductee Name.");
                    $('#deducteeName' + trid1)[0].focus();
                    return false;
                }
                if ($('#paymentDate' + trid1).val() == '')
                {
                    alert("Please enter Payment Date.");
                    $('#paymentDate' + trid1)[0].focus();
                    return false;
                }
                if ($('#amount' + trid1).val() == '')
                {
                    alert("Please enter Amount.");
                    $('#amount' + trid1)[0].focus();
                    return false;
                }
                if (isNaN($('#amount' + trid1).val()))
                {
                    alert("Please enter Valid Integer Value.");
                    $('#amount' + trid1)[0].focus();
                    $('#amount' + trid1)[0].select();
                    return false;
                }
                if ($('#deducteeCode' + trid1).val() == '')
                {
                    alert("Please select Deductee Code.");
                    $('#deducteeCode' + trid1)[0].focus();
                    return false;
                }
                var totalTDS = 0;
                for (i = 1; i <= trid1; i++)
                {
                    totalTDS += parseInt($('#amount' + i).val());
                }
                //alert(parseInt($('#totalTDSAmount').val()) + "->" + parseInt(totalTDS));
                if (parseInt($('#totalTDSAmount').val()) < parseInt(totalTDS))
                {
                    alert("Sum of all the Deductee Amount should be less than equal to total TDS Amount. (" + $('#totalTDSAmount').val() + ")");
                    return false;
                }
                if (confirm("Are you sure you want to add new row?\nOnce added a new row, you have to put all the values for it, and its cannot be deleted anymore."))
                {
                    trid1 = trid1 + 2;
                    content = '<tr class="dataTR" id="drow_' + (trid1 - 1) + '">';
                    content += '                                    <td>' + (trid1 - 1) + '</td>';
                    content += '                                    <td>';
                    content += '                                        <input type="text" class="form-control" name="refNumber" id="refNumber' + (trid1 - 1) + '" />';
                    content += '                                    </td>';
                    content += '                                    <td><input type="text" class="form-control" name="pan" id="pan' + (trid1 - 1) + '" /></td>';
                    content += '                                    <td><input type="text" class="form-control" name="deducteeName" id="deducteeName' + (trid1 - 1) + '" /></td>';
                    content += '                                    <td><div class="input-group date" id="processDate">';
                    content += '                                            <input type="text" class="form-control txtNotOrdDt" id="paymentDate' + (trid1 - 1) + '" name="paymentDate" readonly="true"/>';
                    content += '                                            <span class="input-group-addon">';
                    content += '                                                <span class="glyphicon glyphicon-time"></span>';
                    content += '                                            </span>';
                    content += '                                        </div></td>';
                    content += '                                    <td><input type="text" class="form-control" name="amount" id="amount' + (trid1 - 1) + '" onblur="javascript: calculateTotal(this.value)" /></td>';
                    content += '                                    <td><select class="form-control" name="deducteeCode" id="deducteeCode' + (trid1 - 1) + '">';
                    content += '                                            <option value="">-Select-</option>';
                    content += '                                            <option value="01">01</option>';
                    content += '                                            <option value="02">02</option>';
                    content += '                                        </select></td>';
                    content += '                                </tr>';
                    $('#tab1 > tbody:last').append(content);
                    $('.txtNotOrdDt').datetimepicker({
                        format: 'D-MMM-YYYY',
                        useCurrent: false,
                        ignoreReadonly: true
                    });
                }

            }
            function validateForm()
            {
                trid1 = $('#tab1 tr.dataTR').length;
                totAmount = parseInt($('#totalAmount').val());
                for (i = ${startIdx}; i <= trid1; i++)
                {
                    if ($('#pan' + i).val() == '')
                    {
                        alert("Please enter PAN.");
                        $('#pan' + i)[0].focus();
                        return false;
                    }
                    if ($('#deducteeName' + i).val() == '')
                    {
                        alert("Please enter Deductee Name.");
                        $('#deducteeName' + i)[0].focus();
                        return false;
                    }
                    if ($('#paymentDate' + i).val() == '')
                    {
                        alert("Please enter Payment Date.");
                        $('#paymentDate' + i)[0].focus();
                        return false;
                    }
                    if ($('#amount' + i).val() == '')
                    {
                        alert("Please enter Amount.");
                        $('#amount' + i)[0].focus();
                        return false;
                    }
                    if (isNaN($('#amount' + i).val()))
                    {
                        alert("Please enter Valid Integer Value.");
                        $('#amount' + i)[0].focus();
                        $('#amount' + i)[0].select();
                        return false;
                    }
                    if ($('#deducteeCode' + i).val() == '')
                    {
                        alert("Please select Deductee Code.");
                        $('#deducteeCode' + i)[0].focus();
                        return false;
                    }
                    var totalTDS = 0;
                    for (j = 1; j <= trid1; j++)
                    {
                        totalTDS += parseInt($('#amount' + j).val());
                    }
                    //alert((parseInt(totalTDS)+totAmount));
                    if (parseInt($('#totalTDSAmount').val()) < (parseInt(totalTDS)))
                    {
                        alert("Sum of all the Deductee Amount should be less than equal to total TDS Amount. (" + $('#totalTDSAmount').val() + ")");
                        return false;
                    }
                    if ($('#refNumber' + i).val() == '')
                    {
                        $('#refNumber' + i).val('N/A');
                    }
                }
                $('#quarter')[0].disabled = false;
                $('#financialYear')[0].disabled = false;
            }
            function confirmDelete(deducteeId)
            {
                if (confirm("Are you sure you want to delete?"))
                {
                    $.ajax({
                        dataType: 'text',
                        type: "POST",
                        url: "DeleteDeductee.htm?deducteeId=" + deducteeId,
                        success: function(result) {
                            self.location = 'Annexure-I-26Q.htm?month=' + $('#month').val();
                        }
                    });

                }
            }
            function calculateTotal(amount)
            {
                trid1 = $('#tab1 tr.dataTR').length;
                var totalTDS = 0;
                for (j = 1; j <= trid1; j++)
                {
                    totalTDS += parseInt($('#amount' + j).val());
                }  
                $('#total_blk').html('&#8377;'+totalTDS);
                if (parseInt($('#totalTDSAmount').val()) < (parseInt(totalTDS)))
                {
                    $('#tr_total').css('background', '#FF0000');
                }
                else
                {
                    $('#tr_total').css('background', '#E5FAC9');
                }
            }
        </script>

        <style type="text/css">
            .frmTable td{padding:8px;}
        </style>
    </head>
    <body style="margin-top:10px;">

        <jsp:include page="ITTab.jsp">
            <jsp:param name="menuHighlight" value="26Q" />
        </jsp:include>
        <div id="page-wrapper">
            <div style="width:98%;margin:0px auto;">
                <p style="margin:0px;"><a href="Challan26Q.htm" class="btn btn-primary btn-lg" style="background:#890000;">Challan Form</a>
                    <a href="Annexure-I-26Q.htm" class="btn btn-primary btn-lg" style="background:#890000;">Annexure-I</a></p>  
            </div>
            <form:form class="form-inline" action="SaveDeducteeDetail.htm" style="margin:0px;" commandName="DeducteeBean" onsubmit="javascript: return validateForm()">
                <div class="container-fluid" style="padding-top: 5px;padding-bottom: 5px;">

                    <div class="panel-heading" style="background:#EAEAEA;border-bottom:2px solid #DADADA;">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="form-group" style="width:20%;">
                                    <label for="financialYear">&nbsp;Financial Year:</label>
                                    <form:select path="financialYear" id="financialYear" class="form-control" onchange="showMonth()" disabled="true">
                                        <option value="2019-20" selected="selected">2019-20</option>
                                    </form:select>
                                </div>
                                <div class="form-group" style="width:20%;">
                                    <label for="quarter">&nbsp;Quarter:</label>
                                    <form:select path="quarter" id="quarter" class="form-control" disabled="true">
                                        <option value="">Select</option>
                                        <option value="1" selected>Q1</option>
                                        <option value="2">Q2</option>
                                        <option value="3">Q3</option>
                                        <option value="4">Q4</option>
                                    </form:select>
                                </div>
                                <div class="form-group" style="width:20%;">
                                    <label for="sltMonth">&nbsp;Month:</label>
                                    <form:select path="month" id="month" class="form-control" onchange="self.location = 'Annexure-I-26Q.htm?month=' + this.value">
                                        <option value="">Select</option>
                                        <option value="April"<c:if test="${month eq 'April'}"> selected="selected"</c:if>>April</option>
                                        <option value="May"<c:if test="${month eq 'May'}"> selected="selected"</c:if>>May</option>
                                        <option value="June"<c:if test="${month eq 'June'}"> selected="selected"</c:if>>June</option>
                                    </form:select>
                                </div>      
                                <div class="form-group" style="width:30%;">
                                    <label for="sltMonth">&nbsp;Total TDS to be allocated as per 24G:</label>
                                    <span style="color:#890000;font-size:14pt;">&#8377;${totalTDSAmount}</span>
                                    <input type="hidden" id="totalTDSAmount" name="totalTDSAmount" value="${totalTDSAmount}" />
                                    <input type="hidden" id="totalAmount" value="${totalAmount}" />
                                </div>                                          
                            </div>
                        </div>
                    </div>                        
                    <div class="row" style="margin:10px 0px;">
                        <div class="col-lg-12" align="center"> 
                            <b style="color:#0D8BE6;font-size:20px;">ANNEXURE - I <span style="color:#890000;">(26Q)</span>: DEDUCTEE WISE BREAK UP OF TDS</b> 
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="row" style="min-height:400px;width:100%;margin:0px auto;">
                                <table class="table table-bordered" style="font-size:10pt;margin-bottom:0px;" id="tab1">
                                    <tr bgcolor="#EAEAEA" style="font-weight:bold;">
                                        <td width="4%">Sl#</td>
                                        <td width="15%">Reference Number<br />(if any)</td>
                                        <td width="15%">PAN</td>
                                        <td width="15%">Name</td>
                                        <td width="17%">Date of Payment</td>
                                        <td width="15%">Amount</td>
                                        <td>Deductee Code<br />(01-Company<br />02-Other than Company)</td>
                                        <td></td>
                                    </tr>
                                    <tbody>
                                        <c:forEach var="list" items="${li}" varStatus="cnt">
                                            <tr class="dataTR" id="drow_1">
                                                <td>${cnt.index+1}</td>
                                                <td>
                                                    ${list.refNumber}
                                                </td>
                                                <td>${list.pan}</td>
                                                <td>${list.deducteeName}</td>
                                                <td>${list.paymentDate}</td>
                                                <td>${list.amount}<input type="hidden" id="amount${cnt.index+1}" value="${list.amount}" /></td>
                                                <td>${list.deducteeCode}</td>
                                                <td><a href="javascript: void(0)" onclick="javascript: confirmDelete(${list.deducteeId})">Delete</a></td>
                                            </tr>                                         
                                        </c:forEach>
                                        <tr class="dataTR" id="drow_${startIdx}">
                                            <td>${startIdx}</td>
                                            <td>
                                                <form:input type="text" class="form-control" path="refNumber" id="refNumber${startIdx}" />
                                            </td>
                                            <td><form:input type="text" class="form-control" path="pan" id="pan${startIdx}" /></td>
                                            <td><form:input type="text" class="form-control" path="deducteeName" id="deducteeName${startIdx}" /></td>
                                            <td><div class="input-group date" id="processDate">
                                                    <form:input type="text" class="form-control txtNotOrdDt" id="paymentDate${startIdx}" path="paymentDate" readonly="true"/>
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span>
                                                </div></td>
                                            <td><input type="text" class="form-control" name="amount" id="amount${startIdx}" onblur="javascript: calculateTotal(this.value)" /></td>
                                            <td><form:select class="form-control" path="deducteeCode" id="deducteeCode${startIdx}">
                                        <option value="">-Select-</option>
                                        <option value="01">01</option>
                                        <option value="02">02</option>
                                    </form:select></td>
                                    </tr>
                                    </tbody>
                                </table>
                                            <table class="table table-bordered">
                                                <tr style="font-size:13pt;font-weight:bold;background:#E5FAC9;" id="tr_total">
                                                    <td width="34%" align="right">Total TDS Amount as per 24G:</td>
                                                    <td width="15%">&#8377;${totalTDSAmount}</td>
                                                    <td width="17%" align="right">Total</td>
                                                    <td><span id="total_blk">&#8377;${totalAmount}</span></td>
                                                </tr>
                                            </table>
                                <div class="row">
                                    <div class="col-lg-4"></div>
                                    <div class="col-lg-4" style="text-align:center;"><input type="submit" value="Save Deductee Detail" class="btn btn-primary" /></div>
                                    <div class="col-lg-4" style="text-align:right;">
                                        <p align="right" style="margin-top:0px;"><!--<input type="button" value="Remove" class="btn btn-primary" onclick="javascript: addrow()" style="background:#890000;" />-->
                                            <input type="button" value="Add More" class="btn btn-primary" onclick="javascript: addrow()" style="background:#008900;" /></p>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                </div>
            </form:form> 
        </div>
    </body>
</html>
