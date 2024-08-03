<%-- 
    Document   : EmpQtrAllotment
    Created on : 16 Apr, 2018, 3:25:47 PM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee Quarter Allotment</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/moment.js"></script>
        <script type="text/javascript" src="js/jquery.min.js"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/bootstrap-confirmation.min.js"></script>
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}

            .apply-table td{padding:5px;}
            .tblres td{padding:5px;}
        </style>
        <script type="text/javascript">

            function getQtrList(rec) {
                if (rec) {
                    $('#qrtrtype').empty();
                    var url = 'getQuarterList.htm?qrtrunit=' + rec;
                    $('#qrtrtype').append('<option value="">-- Select --</option>');
                    $('#qrtrtype').append('<option value="-NA-">-NA-</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#qrtrtype').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }

            }

            function getBuildingList(rec) {
                if (rec) {
                    var qrtrunit = $('#qrtrunit').val();
                    var url = 'getBuildingNumList.htm?qrtrunit=' + qrtrunit + '&qrtrtype=' + rec;
                    $('#qtrbldgno').empty();
                    $('#qtrbldgno').append('<option value="">-- Select --</option>');
                     $('#qtrbldgno').append('<option value="-NA-">-NA-</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#qtrbldgno').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
            }

            function getalllist() {
                var unit = $('#qrtrunit').val();
                var type = $('#qrtrtype').val();
                var bldgno = $('#qtrbldgno').val();
                var result = unit.concat(" , ", type, " , ", bldgno);
               // document.getElementById("address").value = result;
            }

            function submitAllotment()
            {
                if (obj.orderNumber.value == '')
                {
                    alert("Please enter Order Number.");
                    obj.orderNumber.focus();
                    return false;
                }
                if (obj.orderDate.value == '')
                {
                    alert("Please select Order Date.");
                    obj.orderDate.focus();
                    return false;
                }
                if (obj.quarterNo.value == '')
                {
                    alert("Please enter Quarter Number.");
                    obj.quarterNo.focus();
                    return false;
                }
                if (obj.allotmentDate.value == '')
                {
                    alert("Please select Allotment Date.");
                    obj.allotmentDate.focus();
                    return false;
                }
                if (obj.possessionDate.value == '')
                {
                    alert("Please select Possession Date.");
                    obj.possessionDate.focus();
                    return false;
                }
                if (obj.qrtrunit.value == '')
                {
                    alert("Please enter Quarter Unit.");
                    obj.qrtrunit.focus();
                    return false;
                }
                if (obj.qrtrtype.value == '')
                {
                    alert("Please enter Quarter Type.");
                    obj.qrtrtype.focus();
                    return false;
                }
                if (obj.qtrbldgno.value == '')
                {
                    alert("Please enter Building Number.");
                    obj.qtrbldgno.focus();
                    return false;
                }

                if (obj.address.value == '')
                {
                    alert("Please enter Address.");
                    obj.address.focus();
                    return false;
                }
                if (obj.quarterRent.value == '')
                {
                    alert("Please enter Quarter Rent.");
                    obj.quarterRent.focus();
                    return false;
                }
                if (isNaN(obj.quarterRent.value))
                {
                    alert("Please enter a valid Integer Value.")
                    obj.quarterRent.focus();
                    obj.quarterRent.select();
                    return false;
                }
                if (obj.sewerageRent.value == '')
                {
                    alert("Please enter Sewerage Rent.");
                    obj.sewerageRent.focus();
                    return false;
                }
                if (isNaN(obj.sewerageRent.value))
                {
                    alert("Please enter a valid Integer Value.")
                    obj.sewerageRent.focus();
                    obj.sewerageRent.select();
                    return false;
                }
                if (obj.waterRent.value == '')
                {
                    alert("Please enter Water Rent.");
                    obj.waterRent.focus();
                    return false;
                }
                if (isNaN(obj.waterRent.value))
                {
                    alert("Please enter a valid Integer Value.")
                    obj.waterRent.focus();
                    obj.waterRent.select();
                    return false;
                }                
                if (obj.municipalityTax.value == '')
                {
                    alert("Please enter Municipality Tax.");
                    obj.municipalityTax.focus();
                    return false;
                }
                if (isNaN(obj.municipalityTax.value))
                {
                    alert("Please enter a valid Integer Value.")
                    obj.municipalityTax.focus();
                    return false;
                }
            }
            function showForm()
            {
                if ($('#isSurrendered').val() == 'true')
                {
                    alert("It seems like you have quarter which you did not surrender yet.\nPlease surrender the quarter from the list before adding a new.");
                    return;
                }
                else
                {
                    $('#btn_add_blk').fadeOut();
                    $('#formadd_blk').slideDown();
                }
            }
            function openNewEntry() {

                $('#btn_add_blk').fadeOut();
                $('#formadd_blk').slideDown();


            }
            function saveSurrenderData()
            {
                if ($('#order_number').val() == '')
                {
                    alert("Please enter Order Number.");
                    $('#order_number')[0].focus();
                    return false;
                }
                if ($('#order_date').val() == '')
                {
                    alert("Please select Order Date.");
                    $('#order_date')[0].focus();
                    return false;
                }
                if ($('#surrender_date').val() == '')
                {
                    alert("Please select Surrender Date.");
                    $('#surrender_date')[0].focus();
                    return false;
                }
                var notToPrintSBSurrender = 'N';
                if ($('#NotToPrintSB').is(":checked") == true) {
                    notToPrintSBSurrender = 'Y';
                }
                //alert($('#ifSurrendered').val());
                $.ajax({
                    url: 'surrenderQuarter.htm',
                    type: 'get',
                    data: 'qa_id=' + $('#bqaId').val() + '&emp_id=' + $('#empId').val() + '&order_number=' + $('#order_number').val() + '&order_date=' + $('#order_date').val() + '&surrender_date=' + $('#surrender_date').val() + '&NotToPrintSB=' + notToPrintSBSurrender,
                    success: function(retVal) {
                        self.location = 'addEmpQuarterAllotment.htm';
                    }
                });
            }

        </script>
    </head>
    <body>
        <div style="width:80%;margin:0px auto;">
            <h1>Manage Employee Quarter Allotment</h1>
            <div id="formadd_blk" style="display:none;">
                <form id="frmTraining" name="frmEmpQuarter" method="POST" commandName="empQuarterBean"  action="<c:if test="${opt == 'edit'}">update</c:if><c:if test="${opt == 'add'}">save</c:if>EmpQuarterAllotment.htm" onsubmit="return submitAllotment();">
                    <input type="hidden" name="qaId" id="qaId" value="${qaId}"/>
                    <input type="hidden" name="empId" id="empId" value="${empId}" />
                    <input type="hidden" name="bqaId" id="bqaId" value=""/>
                    <input type="hidden" name="isSurrendered" id="isSurrendered" value="${isSurrendered}" />
                    <input type="hidden" name="opt" id="opt" value="${opt}" />
                    <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;margin-top:10px;" align="center" class="apply-table">
                        <tr bgcolor="#007CC1" style="font-weight:bold;color:#FFFFFF;">
                            <td colspan="4">Please fill out all the fields below</td>
                        </tr>                        
                        <tr bgcolor="#FFFFFF">
                            <c:if test="${eqBean.ifSurrendered == 'Y'}">  
                                <td align="right" >
                                    Check Not to Print in Service Book <br/>(If Surrendered):</td>
                                <td ><input type="checkbox" name="chkSBPrintIfSurrender" value="Y" class="form-control" <c:if test="${eqBean.chkSBPrintIfSurrender eq 'Y'}">checked="checked"</c:if>/></td>
                                    <td></td>
                                    <td></td>
                            </c:if> 
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Check Not to Print in Service Book:</td>
                            <td><input type="checkbox" name="chkNotSBPrint" value="Y" class="form-control" <c:if test="${eqBean.chkNotSBPrint eq 'Y'}">checked="checked"</c:if>/></td>
                                <td></td>
                                <td></td>                    
                            </tr>
                            <tr bgcolor="#FFFFFF">
                                <td align="right">Order Number: <span style="color: red">*</span></td>
                                <td><input type="text" name="orderNumber" value="${eqBean.orderNumber}" class="form-control" /></td>
                            <td align="right">Order Date: <span style="color: red">*</span></td>
                            <td><div class='input-group date' id='orderDate1'><input type="text" value="${eqBean.orderDate}" id="orderDate" name="orderDate" path="orderDate1" class="form-control" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span></div></td>                    
                        </tr>                      
                        <tr bgcolor="#FFFFFF">
                            <td width="18%" align="right">Select Quarter: <span style="color: red">*</span></td>
                            <td colspan="3"><select name="qId" size="1" class="form-control">
                                    <c:forEach items="${quarterList}" var="quarter">
                                        <option value="${quarter.value}"<c:if test="${eqBean.qId == quarter.value}"> selected="selected"</c:if>>${quarter.label}</option>
                                    </c:forEach>
                                       
                                </select></td>
                        </tr>                   
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Quarter Number: <span style="color: red">*</span></td>
                            <td colspan="3"><input type="text" name="quarterNo" value="${eqBean.quarterNo}" class="form-control" /></td>
                        </tr>                      
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Allotment Date: <span style="color: red">*</span></td>
                            <td><div class='input-group date' id='allotmentDate1'><input type="text" value="${eqBean.allotmentDate}" id="allotmentDate" name="allotmentDate" path="allotmentDate1" class="form-control" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span></div></td>
                            <td align="right">Possession Date: <span style="color: red">*</span></td>
                            <td><div class='input-group date' id='possessionDate1'><input type="text" value="${eqBean.possessionDate}" id="possessionDate" name="possessionDate" path="possessionDate1" class="form-control" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span></div></td>                    
                        </tr>  
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Select Unit: <span style="color: red">*</span></td>
                            <td>                             
                                <select name="qrtrunit" id="qrtrunit" class="form-control" onclick="getQtrList(this.value)">
                                    <option value="">-- Select --</option>   
                                    <c:forEach items="${unitList}" var="unit">
                                        <option value="${unit.value}"<c:if test="${eqBean.qrtrunit eq unit.value}"> selected="selected"</c:if>>${unit.label}</option>                                     
                                    </c:forEach> 
                                         <option value="-NA-">-NA-</option> 
                                </select>                                      
                            </td>  
                            <td align="right">Select Type: <span style="color: red">*</span></td>
                            <td> 
                                <select name="qrtrtype" id="qrtrtype" class="form-control" onclick="getBuildingList(this.value)">
                                    <c:forEach items="${qtrtypeList}" var="type">
                                        <option value="${type.value}"<c:if test="${eqBean.qrtrtype eq type.value}"> selected="selected"</c:if>>${type.label}</option>                                     
                                    </c:forEach>       
                                </select>   
                            </td>  
                        <tr bgcolor="#FFFFFF"> 
                            <td align="right">Select Building Number: <span style="color: red">*</span></td>
                            <td> 
                                <select name="qtrbldgno" id="qtrbldgno" class="form-control" onchange="getalllist()">
                                    <c:forEach items="${buildingList}" var="bldg">
                                        <option value="${bldg.value}" <c:if test="${eqBean.qtrbldgno eq bldg.value}"> selected="selected"</c:if>>${bldg.label}</option>                                     
                                    </c:forEach>       
                                </select>   
                            </td>  

                            <td align="right">Address: <span style="color: red">*</span></td>
                            <td colspan="3"><input type="text" id="address" name="address" value="${eqBean.address}" class="form-control" /></td>
                        </tr>                         
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Quarter Rent: <span style="color: red">*</span></td>
                            <td><input type="text" name="quarterRent" value="${eqBean.quarterRent}" class="form-control" /></td>
                            <td align="right">Sewerage Rent: <span style="color: red">*</span></td>
                            <td><input type="text" name="sewerageRent" value="${eqBean.sewerageRent}" class="form-control" /></td>                    
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Water Rent: <span style="color: red">*</span></td>
                            <td><input type="text" name="waterRent" value="${eqBean.waterRent}" class="form-control" /></td>
                            <td align="right">Are you getting HRA?</td>
                            <td>
                                <select class="form-control" name="isGetHra">
                                    <option value="N"<c:if test="${eqBean.isGetHra == 'N'}"> selected="selected"</c:if>>No</option>
                                    <option value="Y"<c:if test="${eqBean.isGetHra == 'Y'}"> selected="selected"</c:if>>Yes</option>
                                </select>
                            </td>                    
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Municipality Tax: <span style="color: red">*</span></td>
                            <td><input type="text" name="municipalityTax" value="${eqBean.municipalityTax}" class="form-control" /></td>
                            <td align="right"></td>
                            <td></td>                    
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td colspan="4" align="center"><input type="submit" value="Save Details" class="btn btn-primary btn-success" />
                                <input type="button" value="Cancel" style="background:#900000" class="btn btn-primary btn-success" onclick="javascript: self.location = 'addEmpQuarterAllotment.htm'" />
                            </td>
                        </tr>
                    </table>
                </form>
            </div>

                <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;margin-bottom:10px;'>
                    <tr style="font-weight:bold;background:#0D508E;color:#FFFFFF;">
                        <td>Order Number</td>
                        <td>Order Date</td>
                        <td>Qtr No.</td>
                        <td>Allotment Date</td>
                        <td>Possession Date</td>
                        <td>Address</td>
                        <td>Quarter Rent</td>
                        <td>Water Rent</td>
                        <td>Sewerage Rent</td>
                        <td>Surrendered?</td>
                        <td>Getting HRA?</td>
                        <td align="center"></td>
                        <td align="center"></td>
                    </tr>
                <c:forEach items="${eqList}" var="eqallot">
                    <tr>
                        <td>${eqallot.orderNumber}</td>
                        <td>${eqallot.orderDate}</td>
                        <td>${eqallot.quarterNo}</td>
                        <td>${eqallot.allotmentDate}</td>
                        <td>${eqallot.possessionDate}</td>
                        <td>${eqallot.address}</td>
                        <td>${eqallot.quarterRent}</td>
                        <td>${eqallot.waterRent}</td>
                        <td>${eqallot.sewerageRent}</td>
                        <td>${eqallot.ifSurrendered}</td>
                        <td>${eqallot.isGetHra}</td>
                        <td align="center">
                            <a href="editEmpQuarterAllotment.htm?qaId=${eqallot.qaId}">Edit</a>
                            <!--<c:if test="${eqallot.isValidated eq 'Y'}">
                                <a href="viewEmpQuarterAllotment.htm?qaId=${eqallot.qaId}">View</a>
                            </c:if>-->
                        </td>
                        <td align="center"><c:choose><c:when test="${eqallot.ifSurrendered == 'Yes'}"><span style="color:#FF0000;font-weight:bold;">SURRENDERED</span></c:when>
                                <c:otherwise><a href="javascript:void(0)" class="btn btn-default" onclick="javascript: surrenderAllotment(${eqallot.qaId})">Surrender</a></c:otherwise></c:choose></td>
                            </tr>                            
                </c:forEach>
            </table>
            <c:if test="${opt == 'add'}">
                <p align="right" id="btn_add_blk">
                    <c:choose>
                        <c:when test="${isSurrendered == 'true'}">                                     
                            <button class="btn btn-large btn-primary btn-success" data-toggle="confirmation"
                                    data-btn-ok-label="Continue" data-btn-ok-class="btn-success"
                                    data-btn-ok-icon-class="material-icons" data-btn-ok-icon-content="check"
                                    data-btn-cancel-label="Cancel" data-btn-cancel-class="btn-danger"
                                    data-btn-cancel-icon-class="material-icons" data-btn-cancel-icon-content="close" data-placement="bottom" confirmationEvent="openNewEntry"
                                    data-title="Add New Quarter?" data-content="Are you sure want to add another quarter without surrender the previous quarter?">
                                Add New Allotment &raquo;
                            </button>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-large btn-primary" data-toggle="confirmation" data-title="Add New Quarter?"
                               href="javascript:openNewEntry()">Confirmation</a>
                        </c:otherwise>
                    </c:choose>                    
                </p>
            </c:if>
        </div>



        <div id="AddModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="border: 1px solid #0000FF;">
                <!-- Modal content-->
                <div class="modal-content" >
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title" style="color:#0067C4">Surrender Quarter</h4>
                    </div>
                    <div class="modal-body">                        
                        <table align="center" cellpadding="2" cellspacing="2">
                            <tr bgcolor="#FFFFFF" style="height: 60px">
                                <td align="right" style="height: 30px">Check Not to Print in Service Book:</td>
                                <td><input type="checkbox" name="NotToPrintSB" id="NotToPrintSB" value="Y" class="form-control" /></td>
                                <td></td>
                                <td></td>                    
                            </tr>
                            <tr style="height: 40px">
                                <th>Order Number : </th>
                                <td><input type="text" name="order_number" id="order_number" class="form-control" /></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Order Date: </th>
                                <td><div class='input-group date' id='order_date1'><input type="text" id="order_date" name="order_date" path="order_date1" class="form-control" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span></div></td>
                            </tr>
                            <tr style="height: 40px">
                                <th>Surrender Date: </th>
                                <td><div class='input-group date' id='surrender_date1'><input type="text" id="surrender_date" name="surrender_date" path="surrender_date1" class="form-control" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span></div></td>
                            </tr>                            
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="javascript: saveSurrenderData()">Save</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>


        <script type="text/javascript">
            var obj = document.frmEmpQuarter;
            $(function() {
                $('#allotmentDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#possessionDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#orderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                if ($('#opt').val() == 'edit')
                {
                    $('#formadd_blk').slideDown();
                }

                //alert($('#isSurrendered').val());

                $('#order_date').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#surrender_date').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function surrenderAllotment(bqaId) {
                $('#AddModal').modal('show');
                $("#bqaId").val(bqaId);
            }
            $('[data-toggle=confirmation]').confirmation({
                onConfirm: function(value) {
                    $('#btn_add_blk').fadeOut();
                    $('#formadd_blk').slideDown();
                },
                onCancel: function() {

                }
            });
        </script>


    </body>
</html>
