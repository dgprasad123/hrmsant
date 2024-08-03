<%-- 
    Document   : ApplyNDC
    Created on : 1 Sep, 2020, 11:04:55 AM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply Quarter NDC</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
            function getDeptWiseOfficeList() {
                var deptcode = $('#sltDept').val();

                $('#sltOffice').empty();
                $('#sltPost').empty();

                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                $('#sltOffice').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltOffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode = $('#sltOffice').val();
                $('#sltPost').empty();

                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#sltPost').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltPost').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost() {
                $('#pensionSanctioningAuthority').val($('#sltPost option:selected').text());
            }
            function checkTwinCity(isChecked)
            {
                $('#twin_city_blk').css('display', 'none');
                if(isChecked == 'Y')
                {
                    $('#twin_city_blk').css('display', 'block');
                }
            }
            function checkOccupy(isChecked)
            {
                $('#has_occupied_blk').css('display', 'none');
                if(isChecked == 'Y')
                {
                    $('#has_occupied_blk').css('display', 'block');
                }
            }
            function checkOutstanding(isChecked)
            {
                $('#has_cleared_outstanding_blk').css('display', 'none');
                $('#not_cleared_blk').css('display', 'none');
                if(isChecked == 'Y')
                {
                    $('#has_cleared_outstanding_blk').css('display', 'block');
                }
                if(isChecked == 'N')
                {
                    $('#not_cleared_blk').css('display', 'block');                   
                }
            } 
            function gratuityChecked(isChecked)
            {
                $('#rent_deposit').css('display', 'none');
                $('#btnSubmit').attr('disabled', false);
                if(isChecked == 'N')
                {
                    $('#rent_deposit').css('display', 'block');
                    $('#btnSubmit').attr('disabled', true);
                }                
            }
            function validateForm()
            {
                if($('#fullName').val() == '')
                {
                    alert("Please enter Full Name of the Employee.");
                    return false;
                }
                if($('#gpfNo').val() == '' && $('#hrmsId').val() == '')
                {
                    alert("Please enter either gpf No or HRMS ID.");
                    return false;
                }                

                //return false
            }
            
        </script>
    </head>
    <body>
        <form:form action="SavePSAApplication.htm" method="POST" commandName="QuarterBean" enctype="multipart/form-data" onsubmit="javascript: return validateForm()">
            <div class="container">
                <h2>Quarters NDC for Retiring/Retired Employees</h2>
                <!--<h3><input type="button" value="Apply for NDC" class="btn btn-primary" onclick="javascript: self.location='PSANDCApply.htm'" /></h3>-->
                <div class="panel-group">

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Employee Details:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr>
                                    <td align="right" width="25%">Full Name:</td>
                                    <td><input type="text" name="fullName" id="fullName" value="" class="form-control" /></td>
                                </tr>
                                <tr>
                                    <td align="right">GPF No:</td>
                                    <td><input type="text" name="gpfNo" id="gpfNo" class="form-control" /></td>
                                </tr>
                                <tr>
                                    <td align="right">HRMS ID:</td>
                                    <td><input type="text" name="hrmsId" id="hrmsId" class="form-control" /></td>
                                </tr>                                
                                <tr>
                                    <td align="right" width="30%">Mobile:</td>
                                    <td><input type="text" name="mobile" id="mobile" class="form-control" /></td>
                                </tr> 
                                <tr>
                                    <td colspan="2" style="font-weight:bold;" align="center"><input type="submit" value="Submit" id="btnSubmit" class="btn btn-success" />
                                        <input type="button" value="Cancel" class="btn btn-danger" /></td>
                                </tr>                                 
                            </table>      

                        </div>
                    </div>
 




                </div>
            </div>

            <div id="pensionAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Pension Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltDept" id="sltDept" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltOffice">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltOffice" id="sltOffice" class="form-control" onchange="getOfficeWisePostList();">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${offList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltPost">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltPost" id="sltPost" class="form-control" onchange="getPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${postList}" itemValue="spc" itemLabel="postname"/>
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


    </body>
</html>
