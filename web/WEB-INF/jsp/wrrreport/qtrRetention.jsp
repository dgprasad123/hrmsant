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
            $(document).ready(function() {
                $('#orderDate').datetimepicker({
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

                $('#quarterAllowedFromDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });
                $('#quarterAllowedToDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom',
                    }
                });
                $('#vacateQuartersDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });
                $('#vacateQuartersdeath').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });

                $('#vacateQuartersRetirement').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });

                $('#transferOn').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });

                $('#reliefDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    widgetPositioning: {
                        horizontal: 'right',
                        vertical: 'bottom'
                    }
                });

            });

            function getDistWiseOfficeList(type) {
                var deptcode;
                var distcode;
                if (type == "TF") {
                    deptcode = $('#hidtfDeptCode').val();
                    distcode = $('#hidtfDistCode').val();
                    $('#hidtfOffCode').empty();
                    $('#hidtfOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "TT") {
                    deptcode = $('#hidTTDeptCode').val();
                    distcode = $('#hidTTDistCode').val();
                    $('#hidTTOffCode').empty();
                    $('#hidTTOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "TO") {
                    deptcode = $('#hidooDeptCode').val();
                    distcode = $('#hidooDistCode').val();
                    $('#hidooOffCode').empty();
                    $('#hidooOffCode').append('<option value="">--Select Office--</option>');
                }
                var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "TF") {
                            $('#hidtfOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "TT") {
                            $('#hidTTOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "TO") {
                            $('#hidooOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                });
            }

            function getOfficeWiseGenericPostList(type) {
                var offcode;
                var url;
                if (type == "TF") {
                    offcode = $('#hidtfOffCode').val();
                    $('#genericpostTF').empty();
                    $('#genericpostTF').append('<option value="">--Select Generic Post--</option>');
                    url = "getPostCodeListJSON.htm?offcode=" + offcode;
                } else if (type == "TT") {
                    offcode = $('#hidTTOffCode').val();
                    $('#genericpostTT').empty();
                    $('#genericpostTT').append('<option value="">--Select Generic Post--</option>');
                    url = "getPostCodeListJSON.htm?offcode=" + offcode;
                } else if (type == "TO") {
                    offcode = $('#hidooOffCode').val();
                    $('#genericpostoo').empty();
                    $('#genericpostoo').append('<option value="">--Select Generic Post--</option>');
                    url = "getPostCodeListJSON.htm?offcode=" + offcode;
                }
                //var url = 'getAuthorityPostListJSON.htm?offcode=' + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "TF") {
                            $('#genericpostTF').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        } else if (type == "TT") {
                            $('#genericpostTT').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        } else if (type == "TO") {
                            $('#genericpostoo').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        }
                    });
                });
            }

            function getPost(type) {
                if (type == "TF") {
                    $('#officeFromName').val($('#genericpostTF option:selected').text());
                } else if (type == "TT") {
                    $('#officeToName').val($('#genericpostTT option:selected').text());
                } else if (type == "TO") {
                    $('#deathOff').val($('#genericpostoo option:selected').text());
                }
            }


            function saveCheck() {
                var retentionType = $('#retentionType').val();
                var occupationTypes = $('#occupationTypes').val();
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
                if (occupationTypes == 3 || occupationTypes == 1 || occupationTypes == 2) {
                    var officeFromName = $('#officeFromName').val();
                    var officeToName = $('#officeToName').val();


                    if (officeFromName == "") {
                        alert('Please select Transfer from Office');
                        $('#officeFromName').focus();
                        return false;
                    }
                    if (officeToName == "") {
                        alert('Please select Transfer to Office');
                        $('#officeToName').focus();
                        return false;
                    }

                }
                if (occupationTypes == 4 || occupationTypes == 5 || occupationTypes == 6) {
                    var deathOff = $('#deathOff').val();


                    if (deathOff == "") {
                        alert('Please select office name');
                        $('#deathOff').focus();
                        return false;
                    }


                }


                if (occupationTypes == 3 || occupationTypes == 1 || occupationTypes == 4 || occupationTypes == 5 || occupationTypes == 6) {
                    var quarterAllowedFromDate = $('#quarterAllowedFromDate').val();
                    var quarterAllowedToDate = $('#quarterAllowedToDate').val();

                    if ($('#quarterAllowedFromDate').val() == '') {
                        alert('Retention Period From Date should not be blank.');
                        $('#quarterAllowedFromDate').focus();
                        return false;
                    }
                    if ($('#quarterAllowedToDate').val() == '') {
                        alert('Retention Period To Date should not be blank.');
                        $('#quarterAllowedToDate').focus();
                        return false;
                    }

                }

                if (occupationTypes == 1) {

                    var transferOn = $('#transferOn').val();
                    if ($('#transferOn').val() == '') {
                        alert('Transfer/Relieved Date  should not be blank.');
                        $('#transferOn').focus();
                        return false;
                    }
                }
                if (occupationTypes == 3) {
                    var reliefDate = $('#reliefDate').val();
                    var vacateQuartersDate = $('#vacateQuartersDate').val();
                    if ($('#reliefDate').val() == '') {
                        alert('Relief Date  should not be blank.');
                        $('#reliefDate').focus();
                        return false;
                    }

                    if ($('#vacateQuartersDate').val() == '') {
                        alert('Vacate Possession Quarter Date  should not be blank.');
                        $('#vacateQuartersDate').focus();
                        return false;
                    }
                }
                if (occupationTypes == 2) {

                    var vacateQuartersDate = $('#wefFromDate').val();
                    if ($('#wefFromDate').val() == '') {
                        alert('Flat Licence Fee W.e.f Date  should not be blank.');
                        $('#wefFromDate').focus();
                        return false;
                    }
                }
                if (occupationTypes == 4) {

                    var vacateQuartersdeath = $('#vacateQuartersRetirement').val();
                    if ($('#vacateQuartersRetirement').val() == '') {
                        alert('Vacate Possession Quarter Date  should not be blank.');
                        $('#vacateQuartersRetirement').focus();
                        return false;
                    }
                }

                if (occupationTypes == 5) {

                    var vacateQuartersdeath = $('#vacateQuartersdeath').val();
                    if ($('#vacateQuartersdeath').val() == '') {
                        alert('Vacate Possession Quarter Date  should not be blank.');
                        $('#vacateQuartersdeath').focus();
                        return false;
                    }
                }


                //return false;
            }
        </script>


    </head>
    <body>
        <jsp:include page="header_quarter.jsp">
            <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
        </jsp:include>    
        <div id="wrapper">

            <div id="page-wrapper">
                <div class="container-fluid" >
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">
                        <h4>
                            Goverment of Odisha<br/>
                            General Administration & Public Grievance(Rent) Department <br/>
                            Bhubaneswar
                            <hr style="height:2px;border-width:0;color:gray;background-color:gray"/>
                        </h4>
                        <h3 align='center' style='color:green'>
                            ${rtype}<br/>
                            ${rsubtype}
                        </h3>

                        <div class="panel panel-primary" style="z-index:10">
                            <div class="panel-heading">Retention Permission</div>
                            <div class="panel-body" style='font-size:15px;min-height:500px;margin-bottom:10px'>

                                <form:form autocomplete="off" role="form" action="SaveRetentionData.htm" commandName="EmpQuarterBean"  method="post"  class="form-horizontal">
                                    <input type="hidden" name="occupationTypes" id="occupationTypes" value="${occupationTypes}"/>
                                    <input type="hidden" name="retentionType" id="retentionType" value="${rtype}"/>
                                    <input type="hidden" name="consumerNo" id="consumerNo" value="${consumerNo}"/>
                                    <input type="hidden" name="empId" id="retentionSubType" value="${empId}"/>
                                    <input type="hidden" name="retentionSubType" id="retentionSubType" value="${rsubtype}"/>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="orderNumber">Rent Order No:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="orderNumber" id="orderNumber" readonly="true"/>
                                        </div>


                                        <label class="control-label col-sm-2" >Rent Order Date:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="orderDate" id="orderDate" readonly="true"/>                                            
                                        </div>
                                    </div>
                                    <c:if test = "${occupationTypes!='4' && occupationTypes!='5' && occupationTypes!='6' }">

                                        <div class="form-group">
                                            <label class="control-label col-sm-2" >Transfer From Office:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="officeFromName" id="officeFromName"/> 
                                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#transferFromModal">
                                                    <span class="glyphicon glyphicon-search"></span> Choose
                                                </button>
                                            </div>


                                            <label class="control-label col-sm-2">Transfer To Office:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="officeToName" id="officeToName"/>                                              
                                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#transferToModal">
                                                    <span class="glyphicon glyphicon-search"></span> Choose
                                                </button>
                                            </div>
                                        </div>


                                    </c:if>

                                    <c:if test = "${occupationTypes=='4' || occupationTypes=='5' || occupationTypes=='6'}">

                                        <div class="form-group">
                                            <label class="control-label col-sm-2" >Posted Office:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="deathOff" id="deathOff"/>
                                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#transferOffModal">
                                                    <span class="glyphicon glyphicon-search"></span> Choose
                                                </button>
                                            </div>

                                        </div>
                                    </c:if>
                                    <c:if test = "${occupationTypes!='2'}">
                                        <div class="form-group">
                                            <label class="control-label col-sm-2" >Retention Period From Date:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="quarterAllowedFromDate" id="quarterAllowedFromDate" readonly="true"/>                                               
                                            </div>


                                            <label class="control-label col-sm-2">Retention Period To Date:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="quarterAllowedToDate" id="quarterAllowedToDate" readonly="true"/>                                               
                                            </div>
                                        </div>

                                    </c:if>


                                    <!------------------------- Transfer To BBSR ---------------------> 
                                    <c:if test = "${not empty occupationTypes && occupationTypes=='1'}">
                                        <div class="form-group">
                                            <label class="control-label col-sm-2" >Tranfer/Relieved On:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="transferOn" id="transferOn" readonly="true"/>                                                  
                                            </div>

                                        </div>


                                    </c:if>     

                                    <!------------------------- Transfer To KBK ---------------------> 
                                    <c:if test = "${not empty occupationTypes && occupationTypes=='2'}">
                                        <div class="form-group">
                                            <label class="control-label col-sm-2" >Flat Licence Fee W.e.f Date:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="wefFromDate" id="wefFromDate"/>                                                  
                                            </div>

                                        </div>


                                    </c:if> 

                                    <!------------------------- Transfer To NON KBK ---------------------> 
                                    <c:if test = "${not empty occupationTypes && occupationTypes=='3'}">

                                        <div class="form-group">
                                            <label class="control-label col-sm-2">Relief Date:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="reliefDate" id="reliefDate" readonly="true"/>     

                                            </div>

                                            <label class="control-label col-sm-2">Vacate Possession Quarter Date:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="vacateQuartersDate" id="vacateQuartersDate" readonly="true"/>     

                                            </div>
                                        </div>
                                    </c:if> 

                                    <!------------------------- Retirement cases  ---------------------> 
                                    <c:if test = "${not empty occupationTypes && occupationTypes=='4'}">
                                        <div class="form-group">

                                            <label class="control-label col-sm-2">Due date of vacation:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="vacateQuartersRetirement" id="vacateQuartersRetirement" readonly="true"/>   
                                            </div>
                                        </div>

                                    </c:if> 

                                    <!------------------------- Retirement cases  ---------------------> 
                                    <c:if test = "${not empty occupationTypes && occupationTypes=='5'}">
                                        <div class="form-group">                                           
                                            <label class="control-label col-sm-2">Due date of vacation:</label>
                                            <div class="col-sm-4">
                                                <form:input class="form-control" path="vacateQuartersDeath" id="vacateQuartersdeath" readonly="true"/>                                                  
                                            </div>
                                        </div>

                                    </c:if> 



                                    <div align="center">
                                        <input type="button" class="btn btn-info" value="Back" onclick="back_page(${occupationTypes})" />   &nbsp;
                                        <input type="submit" class="btn btn-primary" value="Save" onclick="return saveCheck()" />  
                                    </div>


                                    <div id="transferFromModal" class="modal" role="dialog">
                                        <div class="modal-dialog">
                                            <!-- Modal content-->
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                    <h4 class="modal-title">Details of Transfer From</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="row" style="margin-bottom: 7px;">
                                                        <div class="col-lg-2">
                                                            <label for="hidtfDeptCode">Department</label>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <form:select path="hidtfDeptCode" id="hidtfDeptCode" class="form-control" onchange="removeDepedentDropdown('TF');">
                                                                <option value="">Select Department</option>
                                                                <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                                            </form:select>
                                                        </div>
                                                        <div class="col-lg-1">
                                                        </div>
                                                    </div>
                                                    <div class="row" style="margin-bottom: 7px;">
                                                        <div class="col-lg-2">
                                                            <label for="hidtfDistCode">District</label>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <form:select path="hidtfDistCode" id="hidtfDistCode" class="form-control" onchange="getDistWiseOfficeList('TF');">
                                                                <option value="">--Select District--</option>
                                                                <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                                            </form:select>
                                                        </div>
                                                        <div class="col-lg-1">
                                                        </div>
                                                    </div>
                                                    <div class="row" style="margin-bottom: 7px;">
                                                        <div class="col-lg-2">
                                                            <label for="hidtfOffCode">Office</label>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <form:select path="hidtfOffCode" id="hidtfOffCode" class="form-control" onchange="getOfficeWiseGenericPostList('TF');">
                                                                <option value="">--Select Office--</option>
                                                            </form:select>
                                                        </div>
                                                        <div class="col-lg-1">
                                                        </div>
                                                    </div>
                                                    <div class="row" style="margin-bottom: 7px;">
                                                        <div class="col-lg-2">
                                                            <label for="genericpostTF">Generic Post</label>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <form:select path="genericpostTF" id="genericpostTF" class="form-control" onchange="getPost('TF');">
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
                                    <div id="transferToModal" class="modal" role="dialog">
                                        <div class="modal-dialog">
                                            <!-- Modal content-->
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                    <h4 class="modal-title">Details of Transfer To</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="row" style="margin-bottom: 7px;">
                                                        <div class="col-lg-2">
                                                            <label for="hidTTDeptCode">Department</label>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <form:select path="hidTTDeptCode" id="hidTTDeptCode" class="form-control" onchange="removeDepedentDropdown('TT');">
                                                                <option value="">Select Department</option>
                                                                <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                                            </form:select>
                                                        </div>
                                                        <div class="col-lg-1">
                                                        </div>
                                                    </div>
                                                    <div class="row" style="margin-bottom: 7px;">
                                                        <div class="col-lg-2">
                                                            <label for="hidTTDistCode">District</label>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <form:select path="hidTTDistCode" id="hidTTDistCode" class="form-control" onchange="getDistWiseOfficeList('TT');">
                                                                <option value="">--Select District--</option>
                                                                <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                                            </form:select>
                                                        </div>
                                                        <div class="col-lg-1">
                                                        </div>
                                                    </div>
                                                    <div class="row" style="margin-bottom: 7px;">
                                                        <div class="col-lg-2">
                                                            <label for="hidTTOffCode">Office</label>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <form:select path="hidTTOffCode" id="hidTTOffCode" class="form-control" onchange="getOfficeWiseGenericPostList('TT');">
                                                                <option value="">--Select Office--</option>
                                                            </form:select>
                                                        </div>
                                                        <div class="col-lg-1">
                                                        </div>
                                                    </div>
                                                    <div class="row" style="margin-bottom: 7px;">
                                                        <div class="col-lg-2">
                                                            <label for="genericpostTT">Generic Post</label>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <form:select path="genericpostTT" id="genericpostTT" class="form-control" onchange="getPost('TT');">
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

                                </form:form>  



                            </div>

                        </div>


                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            function back_page(occupationTypes) {
                window.location = "searchtransferCategory.htm?occupationTypes=" + occupationTypes;
            }
        </script>   
    </body>
</html>
