<%-- 
    Document   : JoiningToFieldOffice
    Created on : Feb 13, 2023, 12:03:58 PM
    Author     : Madhusmita
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">

            $(document).ready(function() {
                getFieldofficeData();

            });
            function getFieldofficeData() {
                var empId =${loginempid};
                var url = 'getFieldOfficeDetailsJSON.htm?empid=' + empId;
                $.getJSON(url, function(data) {
                    //alert(url);
                    if (data.hidTempFieldOffCode == "") {
                        alert("Field data is not available");
                    } else {
                        //alert(data.hidTempFieldOffCode);
                        $("#distname").val(data.hidDistname);
                        $("#fieldOfficeName").val(data.sltFieldOffice);

                    }
                });


            }

            function getPoliceStations() {

                var distcode = $('#hidDistCode').val();
                //alert(distcode);
                $('#sltFieldOffice').empty();
                var url = 'getPoliceStationListJSON.htm?distcode=' + distcode;
                $.getJSON(url, function(data) {
                    $('#sltFieldOffice').append('<option value="">--Select Office--</option>');
                    $.each(data, function(i, obj) {
                        $('#sltFieldOffice').append('<option value="' + obj.offCode + '">' + obj.offEn + '</option>');
                    });
                });
            }

            function saveCheck() {
                if ($('#hidDistCode').val() == "") {
                    alert("Enter District Name");
                    return false;
                }
                if ($('#sltFieldOffice').val() == "") {
                    alert("Enter Field Office Name");
                    return false;
                }

                if (confirm("Do you want to save field details?")) {
                    return true;
                }
                return false;
            }
        </script>
    </head>
    <body>
        <div style="width:80%;margin:0px auto;">
            <div id="page-wrapper">
                <div class="container-fluid">

                    <div class="panel panel-default">
                        <div class="panel panel-header">

                            <h3 style="text-align:center;"><u>Join In Field Office</u></h3>
                            <h4 style="color:#016e6e;text-align:center;"><c:out value="${msg}"/></h4>
                        </div>
                        <div class="panel-body">
                            <form:form class="form-horizontal" action="joinfieldOffice.htm" commandName="joiningForm"  method="post">
                                <form:hidden path="hidempcode" id="hidempCode"/>
                                <table class="table-bordered">
                                    <div id="showfieldData">
                                        <div class="panel panel-default">
                                            <div class="panel-heading" style="font-weight:bold; background-color: #d6e9c6">EMPLOYEE FIELD OFFICE DETAILS</div>
                                            <div class="panel-body">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4" style="padding-top: 0px;">District Name:</label>
                                                    <div class="col-sm-6" style="margin-top:0px;">
                                                        <form:input class="form-control" path="distname" id="distname" disabled="true"/>                                                     

                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4" style="padding-top: 0px;">Field Office Name:</label>
                                                    <div class="col-sm-6" style="margin-top:0px;">
                                                        <form:input class="form-control" path="fieldOfficeName" id="fieldOfficeName" disabled="true"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-footer">                                        
                                        <a href="#" data-remote="false" data-toggle="modal" data-target="#savefieldDatamodal" style="">
                                            <button type="button" style="width:10%" class="btn btn-primary" >Add Field Office</button></a>                                       

                                    </div>
                                </table>


                                <div id="savefieldDatamodal" class="modal" role="dialog">
                                    <div class="modal-dialog">
                                        <!-- Modal content-->
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                <h4 class="modal-title">ADD FIELD OFFICE DETAILS</h4>
                                            </div>
                                            <div class="modal-body">
                                                <div class="row" style="margin-bottom: 7px;">

                                                    <div class="form-group">
                                                        <label class="control-label col-sm-3" style="padding-top: 0px;">District:</label>
                                                        <span class="col-sm-7">
                                                            <form:select path="hidDistCode" class="form-control" onchange="getPoliceStations();">
                                                                <form:option value="">--Select District--</form:option>
                                                                <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>
                                                            </form:select> 
                                                        </span>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-3" style="padding-top: 0px;">Field Office:</label>
                                                        <span class="col-sm-7">
                                                            <form:select path="sltFieldOffice" class="form-control">
                                                                <form:option value="">--Select Field Office--</form:option>
                                                                <%--<form:options items="${officeList}" itemValue="offCode" itemLabel="offEn"/>--%>
                                                            </form:select> 
                                                        </span>
                                                    </div>
                                                    <div class="col-lg-1">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <input type="submit" name="action" value="Save" style="width:80px" class="btn btn-primary" onclick="return saveCheck();"/>
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
    </body>
</html>
