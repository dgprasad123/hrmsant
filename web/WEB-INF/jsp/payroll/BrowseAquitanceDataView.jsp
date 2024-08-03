<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<% int j = 0;%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(window).load(function () {
                // Fill modal with content from link href
                $("#myModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find("#modalbody").load(link.attr("href"));
                });
            });

            function viewLess() {
                $('#allowanceTbl tr').each(function () {
                    var adAmt = $(this).find("td").eq(3).html();
                    if (adAmt == 0) {
                        $(this).hide();
                    }
                });
                $('#deductionTbl tr').each(function () {
                    var adAmt = $(this).find("td").eq(6).html();
                    if (parseInt(adAmt) === 0) {
                        $(this).hide();
                    }
                });

            }
        </script>

    </head>
    <body>
        <form:form class="form-inline" action="browserAqData.htm" method="POST" commandName="command">

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-2"><b>HRMS ID:</b></div>
                            <div class="col-lg-4">${aqReportBean.empcode}</div>
                            <div class="col-lg-2"><b>SCALE:</b></div>
                            <div class="col-lg-4">${aqReportBean.payscale}</div>
                        </div>
                        <div class="row">
                            <div class="col-lg-2"><b>EMPLOYEE NAME:</b></div>
                            <div class="col-lg-4">${aqReportBean.empname}</div>
                            <div class="col-lg-2"><b>DDO:</b></div>
                            <div class="col-lg-4">&nbsp;</div>
                        </div>
                        <div class="row">
                            <div class="col-lg-2"><b>OFFICE NAME:</b></div>
                            <div class="col-lg-4">&nbsp;</div>
                            <div class="col-lg-2"><b>PLAN SECTOR:</b></div>
                            <div class="col-lg-4">&nbsp;</div>
                        </div>
                        <div class="row">
                            <div class="col-lg-2"><b>DESIGNATION:</b></div>
                            <div class="col-lg-4">${aqReportBean.curdesg}</div>
                            <div class="col-lg-2"><b>GPF No:</b></div>
                            <div class="col-lg-4">${aqReportBean.gpfaccno}</div>
                        </div>
                        <div class="row">
                            <div class="col-lg-2"><b>MONTHLY BASIC:</b>${aqReportBean.monbasic}</div>
                            <div class="col-lg-2"><b>CURRENT BASIC:</b></div>
                            <div class="col-lg-2">
                                <span id="curbasic">${aqReportBean.curbasic}</span>
                            </div>
                            <div class="col-lg-2"><b>Days In Month: </b>${aqReportBean.aqday}</div>
                            <div class="col-lg-2"><b>Pay Days: </b>${aqReportBean.payday}</div>
                        </div>
                        <div >
                            <input type="hidden" name="aqslno" id="aqslno" value="${aqSlNo}"/>
                            <div >
                                <a href="backToBrowserAquitance.htm?aqslno=${aqSlNo}&billNo=${billNo}" class="btn btn-default">Back</a>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-6">
                                    <table class="table table-bordered">
                                        <thead>                   	
                                            <tr height="30px">
                                                <th width="5%" align="center" >SL.</th>
                                                <th width="30%" align="center">ALLOWANCES</th>
                                                <th width="45%" align="center">ALLOWANCES NAME</th>
                                                <th width="20%" align="center">AMOUNT</th>
                                            </tr>
                                        </thead>
                                        <tbody id="allowanceTbl">
                                            <c:forEach items="${allowanceObjList}" var="allowanceObj" varStatus="cnt">
                                                <c:if test="${allowanceObj.amount ne '0.0' || allowanceObj.amount ne '0'}">
                                                    <tr>
                                                        <td>${cnt.index+1}</td>
                                                        <td>${allowanceObj.allowance}</td>
                                                        <td>${allowanceObj.allowanceName}</td>
                                                        <td  align="right">${allowanceObj.amount}</td>

                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                            <tr>
                                                <td  align="right" style="font-size:12px;font-family:verdana;border-top:1px solid black;border-bottom:1px solid black;">&nbsp;</td>
                                                <td  align="right" style="font-size:12px;font-family:verdana;border-top:1px solid black;border-bottom:1px solid black;">&nbsp;</td>
                                                <td  align="right" style="font-size:12px;font-family:verdana;border-top:1px solid black;border-bottom:1px solid black;">Total Allowances:</td>
                                                <td  align="right" style="font-size:12px;font-family:verdana;border-top:1px solid black;border-bottom:1px solid black;"><span id="totalAllDisplay">${totAll}</span></td>

                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="col-lg-6">
                                    <span style="color: red;">${updateMessage}</a></span>
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th width="5%" align="center" >SL.</th>
                                                <th width="15%" align="center" >DEDUCTIONS</th>
                                                <th width="30%" align="center" >DEDUCTIONS NAME</th>
                                                <th width="10%" align="center">ACCOUNT NO.</th>
                                                <th width="20%" align="center" >DEDUCTION FOR</th>
                                                <th width="10%" align="center" >INSTALMENT COUNT.</th>
                                                <th width="10%" align="center">AMOUNT</th>
                                            </tr>
                                        </thead>
                                        <tbody id="deductionTbl">
                                            <c:forEach items="${deductionobjList}" var="deductionobj" varStatus="cnt">
                                                <c:if test="${deductionobj.amount ne '0.0'  || allowanceObj.amount ne '0'}">
                                                    <tr>
                                                        <td>${cnt.index+1}</td>
                                                        <td>${deductionobj.deduction} <c:if test="${not empty deductionobj.nowdedn}">(${deductionobj.nowdedn})</c:if></td>
                                                        <td>${deductionobj.deductionName}</td>
                                                        <td>${deductionobj.accNo}</td>
                                                        <td>${deductionobj.deductionFor}</td>
                                                        <td>${deductionobj.noofInstal}</td>
                                                        <td  align="right">${deductionobj.amount}</td>

                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                            <tr>
                                                <td  align="right" style="font-size:12px;font-family:verdana;border-top:1px solid black;border-bottom:1px solid black;">&nbsp;</td>
                                                <td  align="right" style="font-size:12px;font-family:verdana;border-top:1px solid black;border-bottom:1px solid black;">&nbsp;</td>
                                                <td  colspan="4" align="right" style="font-size:12px;font-family:verdana;border-top:1px solid black;border-bottom:1px solid black;">Total Deduction:</td>
                                                <td  align="right" style="font-size:12px;font-family:verdana;border-top:1px solid black;border-bottom:1px solid black;"><span id="totalAllDisplay">${totDed}</span></td>
                                            </tr>
                                        </tbody>
                                    </table>


                                </div>
                            </div>  
                        </div>
                        <table class="table table-bordered">     
                            <tr>
                                <td align="center">Gross Rs.${gross}</td>
                                <td  align="center">Net Rs.${net}</td>

                            </tr>
                        </table>

                    </div>

                </div>


                <div id="myModal" class="modal fade" role="dialog">
                    <div class="modal-dialog" style="width:1000px;">
                        <!-- Modal content-->
                        <div class="modal-content">                        
                            <div class="modal-body" id="modalbody">

                            </div>
                            <div class="modal-footer">
                                <span id="msg"></span>                        

                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>

                    </div>
                </div>

                <div id="basicModal" class="modal fade" role="dialog">
                    <div class="modal-dialog" style="width:1000px;">
                        <!-- Modal content-->
                        <div class="modal-content">

                            <div class="modal-body">
                                <p>Basic : <input type="text" name="aqbasic" id="aqbasic" class="form-control"/></p>
                            </div>
                            <div class="modal-footer">
                                <span id="msg"></span>                        
                                <button type="button" class="btn btn-default" onclick="changeAqBasic()" data-dismiss="modal">Save</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>

                    </div>
                </div>


            </form:form>
    </body>
</html>
