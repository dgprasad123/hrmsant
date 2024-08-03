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
                        vertical: 'bottom'
                    }
                });
                $('#vacateQuartersDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });



                $('#reliefDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });



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
                 if ($('#oswasfileno').val() == '') {
                    alert('Oswas File no should not be blank.');
                    $('#oswasfileno').focus();
                    return false;
                }

                /* var quarterAllowedFromDate = $('#quarterAllowedFromDate').val();
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
                 }*/

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
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">
                        <h2>
                            Goverment of Odisha<br/>
                            General Administration & Public Grievance(Rent) Department <br/>
                            Bhubaneswar
                            <hr style="height:2px;border-width:0;color:gray;background-color:gray"/>
                        </h2>


                        <div class="panel panel-primary" >
                            <div class="panel-heading">Retention Permission</div>
                            <div class="panel-body" style='font-size:15px;min-height:400px'>

                                <form:form autocomplete="off" role="form" action="SaveExtensionRetentionData.htm" commandName="EmpQuarterBean"  method="post"  class="form-horizontal">

                                    <input type="hidden" name="consumerNo" id="consumerNo" value="${consumerNo}"/>
                                    <input type="hidden" name="empId" id="retentionSubType" value="${empId}"/>
                                    <input type="hidden" name="trackingId" id="trackingId" value="${trackingId}"/>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="orderNumber">Rent Order No:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="orderNumber" id="orderNumber" value="${orderNo}"/>
                                        </div>

                                        <label class="control-label col-sm-2" >Rent Order Date:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="orderDate" id="orderDate" readonly="true" value="${orderdate}"/>                                            
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" >Extension Retention Period From Date: </label>
                                        
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="quarterAllowedFromDate" id="quarterAllowedFromDate" readonly="true" value="${empDetails.quarterAllowedFromDate}"/>                  
                           
                                        </div>


                                        <label class="control-label col-sm-2">Extension Retention Period To Date:</label>
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="quarterAllowedToDate" id="quarterAllowedToDate" readonly="true" value="${empDetails.quarterAllowedToDate}"/>                                               
                                        </div>
                                    </div>  
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" >OSWAS File No: </label>
                                        
                                        <div class="col-sm-4">
                                            <form:input class="form-control" path="oswasFileNo" id="oswasFileNo" />                  
                           
                                        </div>


                                        <label class="control-label col-sm-2">Licence Fee:</label>
                                        <div class="col-sm-4">
                                           <input type="radio"  name="licenceFeeType" value="Flat" checked> <b >Flat</b>&nbsp;
                                        <input type="radio"  name="licenceFeeType" value="Standard" > <b >Standard</b>                                              
                                        </div>
                                    </div>    



                                    <div align="center">
                                        <input type="button" class="btn btn-info" value="Back" onclick="back_page(${occupationTypes})" />   &nbsp;
                                        <input type="submit" class="btn btn-primary" value="Save" onclick="return saveCheck()" />  
                                    </div>



                                </form:form>  



                            </div>

                        </div>


                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
