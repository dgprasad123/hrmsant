<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            var MONTH_NAMES = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            ];
            $(window).load(function () {
                // Fill modal with content from link href
                $("#viewModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });

                $("#editModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-content").load(link.attr("href"));
                });
            })

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

            function SaveIncomeTax(aqSlNo) {
                var taxAmt = $("#txtAmt" + aqSlNo).val();
                var taxBillNo = $("#billNo").val();
                if (taxAmt == '') {
                    alert("Please Enter Income Tax Amount");
                    return false;
                } else {
                    $.ajax({
                        type: "GET",
                        url: "saveItArrMast.htm",
                        data: {aqslno: aqSlNo, billNo: taxBillNo, taxAmt: taxAmt},
                        success: function (data) {
                            location.reload();
                        },
                        error: function () {
                            alert('Error Occured');
                        }
                    });
                }
            }

            function SaveCPF(aqSlNo) {
                var cpfVal = $("#txtCpf" + aqSlNo).val();
                var taxBillNo = $("#billNo").val();
                if (cpfVal == '') {
                    alert("Please Enter CPF Contribution ");
                    return false;
                } else {
                    $.ajax({
                        type: "GET",
                        url: "saveCpfArrMast.htm",
                        data: {aqslno: aqSlNo, billNo: taxBillNo, cpfAmt: cpfVal},
                        success: function (data) {
                            location.reload();
                        },
                        error: function () {
                            alert('Error Occured');
                        }
                    });
                }
            }

            function SavePT(aqSlNo) {
                var ptVal = $("#txtPt" + aqSlNo).val();
                var taxBillNo = $("#billNo").val();
                if (ptVal == '') {
                    alert("Please Enter PT Amount ");
                    return false;
                } else {
                    $.ajax({
                        type: "GET",
                        url: "savePtArrMast.htm",
                        data: {aqslno: aqSlNo, billNo: taxBillNo, ptAmt: ptVal},
                        success: function (data) {
                            location.reload();
                        },
                        error: function () {
                            alert('Error Occured');
                        }
                    });
                }
            }

            function DeleteArrMastData() {

                var aqSlno = $("#aqSlNo").val();
                var billNo = $("#billNo").val();
                if (confirm('Are You sure to Delete ?')) {
                    $.ajax({
                        type: "GET",
                        url: "deleteArrMast.htm",
                        data: {aqSlNo: aqSlno, billNo: billNo},
                        success: function (data) {
                            location.reload();
                        },
                        error: function () {
                            alert('Error Occured');
                        }
                    });
                } else {
                    return false;
                }

            }
            function formatDate(date) {
                var d = new Date(date),
                        month = '' + (d.getMonth() + 1),
                        day = '' + d.getDate(),
                        year = d.getFullYear();
                monthName = MONTH_NAMES[month];
                if (day.length < 2)
                    day = '0' + day;

                return [day, monthName, year].join('-');
            }

            function searchEmployee() {
                var searchemp = $("#searchemp").val();
                $.ajax({
                    type: "GET",
                    url: "searchEmpForArrMast.htm",
                    data: {searchemp: searchemp},
                    success: function (data) {
                        console.log(data);
                        if (data.msgcode == 1) {
                            $("#addempmsg").text(data.message);
                        } else {
                            $("#choiceDate").val(data.choiceDate);
                            $("#payrevisionbasic").val(data.payrevisionbasic);
                        }
                        //location.reload();
                    },
                    error: function () {
                        alert('Error Occured');
                    }
                });
            }
            function addEmployeeToBill() {
                billNo = $("#billNo").val();
                empCode = $("#searchemp").val();
                choiceDate = $("#choiceDate").val();
                payrevisionbasic = $("#payrevisionbasic").val();
                $.ajax({
                    type: "GET",
                    url: "addEmployeeToBill.htm",
                    data: {billNo: billNo, empCode: empCode, payrevisionbasic: payrevisionbasic, inputChoiceDate: choiceDate},
                    success: function (data) {
                        console.log(data);
                        if (data.msgcode == 1) {
                            $("#addempmsg").text(data.message);
                        } else {
                            var DateCreated = formatDate(data.choiceDate);
                            $("#choiceDate").val(DateCreated);
                            $("#payrevisionbasic").val(data.payrevisionbasic);
                            //location.reload();
                        }
                    },
                    error: function () {
                        alert('Error Occured');
                    }
                });
            }
            function reprocessArrAqMast(aqSlNo, billNo) {
                if (confirm('Are You sure to Reprocess ? ')) {
                    $.ajax({
                        type: "GET",
                        url: "reprocessArrAqMast.htm",
                        data: {aqslno: aqSlNo, billNo: billNo},
                        success: function (data) {
                            console.log(data);
                            if (data.processed == 1) {
                                alert("Reprocess Completed");
                            }
                        },
                        error: function () {
                            alert('Error Occured');
                        }
                    });
                }
            }
            function giveFullArrear(aqSlNo, billNo){
                if (confirm('Are You sure to Give 100% Arrear ? ')) {
                    $.ajax({
                        type: "GET",
                        url: "giveFullArrear.htm",
                        data: {aqslno: aqSlNo, billNo: billNo},
                        success: function (data) {
                            console.log(data);
                            if (data.processed == 1) {
                                alert("Completed. Refresh the Page");
                            }
                        },
                        error: function () {
                            alert('Error Occured');
                        }
                    });
                }
            }
            function ReCalculateArrMast(billNo) {

                $.ajax({
                    type: "GET",
                    url: "reCalArrMast.htm",
                    data: {billNo: billNo},
                    success: function (data) {
                        location.reload();
                    },
                    error: function () {
                        alert('Error Occured');
                    }
                });
            }
            
            function checkAll() {
                var allRows = document.getElementsByTagName('input');
                var selectChk = document.getElementById('chkArrearAllEmployee');
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

            function deleteSelectedEmployees(billNo) {
                var checkBoxlength = $("input[name=chkArrearEmployee]:checked").length;
                if (checkBoxlength == 0) {
                    alert("Please select employee(s) to Remove.");
                } else {
                    var checkedEmployees = "";
                    $("input[name=chkArrearEmployee]:checked").each(function(){
                       if(checkedEmployees == ""){
                           checkedEmployees = $(this).val();
                       }else{
                            checkedEmployees = checkedEmployees +","+ $(this).val();
                       }
                    });
                    //alert("Checked Employees are: "+checkedEmployees);
                    var ret;
                    ret = confirm("Are you sure to remove selected employee(s)?");
                    if (ret == true) {
                        //return true;
                        window.location = "deleteArrMast.htm?aqSlNoArr="+checkedEmployees+"&billNo="+billNo;
                    } else {
                        //return false;
                    }
                }
            }
            
        </script>
    </head>
    <body>
        <input type="hidden" name="billNo" id="billNo" value="${billNo}"/>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                        &nbsp;&nbsp;
                    </c:if>
                    <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                        <button type="button" class="btn btn-default" class="btn btn-info btn-lg" data-toggle="modal" data-target="#viewModalAddEmployee">Add Employee</button>
                        &nbsp;&nbsp;
                        <a href="javascript:ReCalculateArrMast('${billNo}');" class="btn btn-default">Re-calculate</a>
                        &nbsp;&nbsp;
                        <a href="javascript:deleteSelectedEmployees('${billNo}');" class="btn btn-danger">Remove</a>
                        <span style="font-weight: bold;color: red;" id="msgspan"></span>
                    </c:if>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" style="font-size: 11pt;">
                        <thead>
                            <tr>
                                <th width="5%">
                                    <input type="checkbox" id="chkArrearAllEmployee" value="0" onclick="checkAll()"/>
                                </th>
                                <th width="3%">Sl No</th>
                                <th width="6%">HRMS ID</th>
                                
                                <th width="16%">Employee Name</th>
                                <th width="16%">Designation</th>
                                <th width="7%">Arrear Pay(100%)</th>
                                <th width="7%">Arrear Pay(40%)</th>
                                <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                    &nbsp;
                                </c:if>
                                <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                    <th width="10%">Deduct Income Tax</th>
                                    <th width="10%">CPF Deduction</th>
                                    <th width="10%">PT Deduction</th>
                                </c:if>
                                <th width="5%">Net Pay</th>
                                <th width="5%">View</th>
                                <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                    &nbsp;&nbsp;
                                </c:if>

                                <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                    &nbsp;&nbsp;
                                </c:if>
                                <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                    <th width="5%">Reprocess</th>
                                    <th width="9%">Full Arrear</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${AqArrList}" var="arrAqMast" varStatus="cnt">    
                                <tr>
                                    <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}">
                                        <td>
                                            <%--<a href="deleteArrMast.htm?aqSlNo=${arrAqMast.aqSlNo}&billNo=${billNo}" class="btn btn-default"><img src="images/delete_icon.png" alt="Delete"/></a>--%>
                                            <input type="checkbox" name="chkArrearEmployee" id="chkArrearEmployee" value="${arrAqMast.aqSlNo}"/>
                                        </td>
                                    </c:if>
                                    <td>${cnt.index+1}</td>
                                    <td>${arrAqMast.empCode}</td>
                                    
                                    <td>${arrAqMast.empName}</td>
                                    <td>${arrAqMast.curDesg}</td>
                                    <td>${arrAqMast.arrearpay}</td>
                                    <td>
                                        ${arrAqMast.arrear40}
                                    </td>
                                    <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                        &nbsp;
                                    </c:if>
                                    <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                        <td><input name="incomeTaxAmt" id="txtAmt${arrAqMast.aqSlNo}" value="${arrAqMast.incomeTaxAmt}" maxlength="7" style="width:50%;" onkeypress="return onlyIntegerRange(event)" />
                                            <input type="button" value="Save" onclick="SaveIncomeTax('${arrAqMast.aqSlNo}')"/>
                                        </td>
                                        <td><input name="cpfHead" id="txtCpf${arrAqMast.aqSlNo}" value="${arrAqMast.cpfHead}" maxlength="6" style="width:50%;" onkeypress="return onlyIntegerRange(event)" />
                                            <input type="button" value="Save" onclick="SaveCPF('${arrAqMast.aqSlNo}')"/>
                                        </td>
                                        <td><input name="ptAmt" id="txtPt${arrAqMast.aqSlNo}" value="${arrAqMast.pt}" maxlength="5" style="width:50%;" onkeypress="return onlyIntegerRange(event)" />
                                            <input type="button" value="Save" onclick="SavePT('${arrAqMast.aqSlNo}')"/>
                                        </td>
                                    </c:if>

                                    <td>
                                        ${arrAqMast.arrear40 - (arrAqMast.incomeTaxAmt + arrAqMast.cpfHead + arrAqMast.pt)}
                                    </td>
                                    <td>
                                        <a href="browseArrAqData.htm?aqslno=${arrAqMast.aqSlNo}&billNo=${billNo}" class="btn btn-default"><img src="images/view_icon.png" alt="View Detail"/></a> <br/>
                                    </td>

                                    <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                        &nbsp;&nbsp;
                                    </c:if>
                                    <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                                        &nbsp;&nbsp;
                                    </c:if>
                                    <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                                        <td><a href="javascript:reprocessArrAqMast('${arrAqMast.aqSlNo}','${billNo}')" class="btn btn-default"><img src="images/process.png" height="20" alt="Reproess"/></a></td>
                                        <td><a href="javascript:giveFullArrear('${arrAqMast.aqSlNo}','${billNo}')" class="btn btn-default"><img src="images/action.png" height="20" alt="Full Arrear"/></a></td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="panel-footer">
                    <c:if test="${(BillSts == 5) or (BillSts == 7) or (BillSts == 3)}">
                        &nbsp;&nbsp;
                    </c:if>
                    <c:if test="${(BillSts < 2) or (BillSts == 4) or (BillSts == 8)}"> 
                        <button type="button" class="btn btn-default" class="btn btn-info btn-lg" data-toggle="modal" data-target="#viewModalAddEmployee">Add Employee</button>
                        &nbsp;&nbsp;
                        <a href="javascript:ReCalculateArrMast('${billNo}');" class="btn btn-default">Re-calculate</a>
                        <a href="javascript:deleteSelectedEmployees('${billNo}');" class="btn btn-danger">Remove</a>
                        <span style="font-weight: bold;color: red;" id="msgspan"></span>
                    </c:if>            
                </div>
            </div>
        </div>
        <!-- Add Employee -->
        <div id="viewModalAddEmployee" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Add Employee</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-2">HRMS ID:</label>
                                <div class="col-sm-4"><input type="text" id="searchemp" class="form-control" maxlength="8"/></div>
                                <div class="col-sm-2"><button type="button" class="btn btn-default" onclick="searchEmployee()">Search</button> </div>
                                <span id="addempmsg"></span>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Pay Revision Date (Choice Date):</label>
                                <div class="col-sm-4">
                                    <input type="text" id="choiceDate" class="txtDate" maxlength="10"/>(yyyy-mm-dd)
                                
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Pay Revision Fitted Amount:</label>
                                <div class="col-sm-4"><input type="text" id="payrevisionbasic" class="form-control" maxlength="8" onkeypress="return onlyIntegerRange(event)"/></div>
                            </div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="addEmployeeToBill()">Save</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>

        <!-- Print Bill Modal -->
        <div id="viewModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">View Aquitance Details</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">
                        <span id="msg"></span>                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>

        <!-- Print Bill Modal -->
        <div id="editModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Edit Aquitance Details</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">
                        <span id="msg"></span>                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>
    </body>
    <script type="text/javascript">
        $(function() {
            $('.txtDate').datetimepicker({
                format: 'YYYY-MM-DD',
                useCurrent: false,
                ignoreReadonly: true
            });
        });
    </script>
</html>
