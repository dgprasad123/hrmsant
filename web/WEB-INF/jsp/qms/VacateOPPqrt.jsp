<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">

            $(document).ready(function () {
                $(".btn-info").show();
                $(".btn-danger").show();
                $("#qrtFee").val("");
                $('#orderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });
                $('#oPPorderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });

                $('#vacationDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });
                $('#wefFromDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });



            });
            function radioClicked() {
                var radioValue = $("input[name='vacateStatus']:checked").val();


                if (radioValue == "Yes") {
                    $("#id_intimation").show();
                    $("#id_opp").hide();
                    $(".btn-info").show();
                    $(".btn-danger").show();
                } else if (radioValue == "No") {
                    $("#id_intimation").hide();
                    $("#id_opp").show();
                    $(".btn-info").show();
                    $(".btn-danger").show();
                    $("#qrtFee").val("0");
                } else {
                    //$("#qrtFee").val("0");
                    $("#id_intimation").hide();
                    $("#id_opp").hide();
                    $(".btn-info").hide();
                    $(".btn-danger").hide();
                }

            }

            function getDistWiseOfficeList(type) {
                var deptcode;
                var distcode;
                deptcode = $('#hidooDeptCode').val();
                distcode = $('#hidooDistCode').val();
                $('#hidooOffCode').empty();
                $('#hidooOffCode').append('<option value="">--Select Office--</option>');
                var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#hidooOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWiseGenericPostList(type) {
                var offcode;
                var url;
                offcode = $('#hidooOffCode').val();
                $('#genericpostoo').empty();
                $('#genericpostoo').append('<option value="">--Select Generic Post--</option>');
                url = "getPostCodeListJSON.htm?offcode=" + offcode;
                //var url = 'getAuthorityPostListJSON.htm?offcode=' + offcode;

                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#genericpostoo').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getPost(type) {
                $('#deathOff').val($('#genericpostoo option:selected').text());
            }

            function saveVacationStatus() {
                var radioValue = "Yes";
                if (radioValue == "Yes") {
                    if ($('#orderNumber').val() == '') {
                        alert('Order No should not be blank.');
                        $('#orderNumber').focus();
                        return false;
                    }

                    if ($('#orderDate').val() == '') {
                        alert('Order Date should not be blank.');
                        $('#orderDate').focus();
                        return false;
                    }
                    var deathOff = $('#deathOff').val();

                    if (deathOff == "") {
                        alert('Please select DDO/Head of Office');
                        $('#deathOff').focus();
                        return false;
                    }
                    if ($('#vacationDate').val() == '') {
                        alert('Quarter Vacation Date should not be blank.');
                        $('#vacationDate').focus();
                        return false;
                    }
                    if ($('#qrtFee').val() == '') {
                        alert('Quarter outstanding dues should not be blank.');
                        $('#qrtFee').focus();
                        return false;
                    }
                    if (isNaN($('#qrtFee').val())) {
                        alert('Invalid Quarter outstanding dues.');
                        $('#qrtFee').focus();
                        return false;
                    }
                    if (!/^[0-9]+$/.test($('#qrtFee').val())) {
                        alert('Invalid Quarter outstanding dues. Only number is allowed');
                        return false;
                    }
                    return true;
                }
                if (radioValue == "No") {
                    if ($('#oPPorderNumber').val() == '') {
                        alert('Order No should not be blank.');
                        $('#oPPorderNumber').focus();
                        return false;
                    }

                    if ($('#oPPorderDate').val() == '') {
                        alert('Order Date should not be blank.');
                        $('#oPPorderDate').focus();
                        return false;
                    }
                    if ($('#wefFromDate').val() == '') {
                        alert('Quarter Cancelled Date should not be blank.');
                        $('#wefFromDate').focus();
                        return false;
                    }
                    return true;
                }

                return true;
            }

        </script>    
    </head>
    <body>
         <jsp:include page="header_quarter.jsp">
                    <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
                </jsp:include>    
        <div id="wrapper">                  
            <div id="page-wrapper">
                <div class="container-fluid">
                    <form:form role="form" action="SaveOPPVacationStatus.htm" commandName="EmpQuarterBean"  method="post" class="form-horizontal">
                        <div align="center" style="margin-top:5px;margin-bottom:7px;">
                            <h2>Status of Allotted Government Quarters</h2>
                            <div class="panel panel-primary">
                                <div class="panel-heading"><strong>Vacation Status</strong></div>
                                <div class="panel-body" style='font-size:15px;'>

                                    <input type="hidden"  name='consumerNo' id='consumerNo' value="${consumerNo}"  />
                                    <input type="hidden"  name='occupationTypes' id='consumerNo' value="${occupationTypes}"  />
                                    <input type="hidden"  name='empId' id='consumerNo' value="${empId}"  />                                    
                                     <input type="hidden" id="NOCRejected" name="vacateStatus" value="Yes" >                                    
                                </div>
                            </div>

                            <div class="panel panel-success" id="id_intimation" >
                                <div class="panel-heading"><strong>Intimation regarding vacation/outstanding dues to DDO</strong></div>
                                <div class="panel-body" style='font-size:15px;min-height:400px'>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="orderNumber">Rent Order No:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="orderNumber" id="orderNumber" value="${orderNo}" readonly="true"/>
                                        </div>


                                        <label class="control-label col-sm-2" >Rent Order Date:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="orderDate" id="orderDate"  value="${orderdate}" readonly="true"/>                                            
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" >DDO/Head of Office :</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="deathOff" id="deathOff"/>

                                        </div>
                                        <div class="col-sm-2"><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#transferOffModal">
                                                <span class="glyphicon glyphicon-search"></span> Search
                                            </button></div>

                                    </div> 
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="orderNumber">Vacation Quarter Date:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="vacationDate" id="vacationDate" readonly="true"  />
                                        </div>


                                        <label class="control-label col-sm-2" >Quarter outstanding dues:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="qrtFee" id="qrtFee" maxlength="6" value="1110.00" />                                            
                                        </div>
                                    </div>     
                                </div>
                            </div>

                           
                        </div>

                        <div id="transferOffModal" class="modal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Details of Office Off</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="hidooDeptCode">Department</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="hidooDeptCode" id="hidooDeptCode" class="form-control" onchange="removeDepedentDropdown('TO');">
                                                    <option value="">Select Department</option>
                                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="hidooDistCode">District</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="hidooDistCode" id="hidooDistCode" class="form-control" onchange="getDistWiseOfficeList('TO');">
                                                    <option value="">--Select District--</option>
                                                    <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="hidooOffCode">Office</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="hidooOffCode" id="hidooOffCode" class="form-control" onchange="getOfficeWiseGenericPostList('TO');">
                                                    <option value="">--Select Office--</option>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="genericpostoo">Generic Post</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="genericpostoo" id="genericpostoo" class="form-control" onchange="getPost('TO');">
                                                    <option value="">--Select Generic Post--</option>
                                                    <form:options items="${gpcpostedList}" itemValue="value" itemLabel="label"/>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div> 
                        <input type="submit" name="action" value="Save" class="btn  btn-info"  onclick="return saveVacationStatus()"/>
                        <input type="button" name="action" value="Back" class="btn btn-danger" onclick="backPage()" />                        
                    </form:form> 
                </div>

            </div>
        </div>
    </div>
    <script type="text/Javascript">
        function backPage(){
         window.location="notVacatedQrtList.htm" ;
        }
    </script>    
</body>
</html>
