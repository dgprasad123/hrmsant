<%-- 
    Document   : InvestmentDeclaraionData
    Created on : 21 Dec, 2018, 11:55:35 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtWEFDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "A") {
                    deptcode = $('#hidAuthDeptCode').val();
                    $('#hidAuthOffCode').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                }
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                if (type == "A") {
                    $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "A") {
                            $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "A") {
                    offcode = $('#hidAuthOffCode').val();
                    $('#authSpc').empty();
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#postedspc').empty();
                }
                var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                if (type == "A") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#authSpc').append('<option value="">--Select Post--</option>');
                } else if (type == "P") {
                    $('#postedspc').append('<option value="">--Select Post--</option>');
                }
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "A") {
                            $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } else if (type == "P") {
                            $('#postedspc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        }
                    });
                });

                //Start Field Off Code
                if (type == "P") {
                    $('#sltPostedFieldOff').empty();
                    var url = 'transferGetFieldOffListJSON.htm?offcode=' + $('#hidPostedOffCode').val();
                    $('#sltPostedFieldOff').append('<option value="">--Select--</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#sltPostedFieldOff').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
                //End Field Off Code
            }

            function getPost(type) {
                if (type == "A") {
                    $('#authPostName').val($('#authSpc option:selected').text());
                } else if (type == "P") {
                    $('#postedspn').val($('#postedspc option:selected').text());
                }
            }

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

            function saveCheck() {
                if ($('#txtNotOrdNo').val() == "") {
                    alert("Please enter Order No");
                    $('#txtNotOrdNo').focus();
                    return false;
                }
                if ($('#txtNotOrdDt').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#postedspn').val() == "") {
                    alert("Please select Details of Posting");
                    return false;
                }
                if ($('#txtGP').val() == "") {
                    alert("Please enter Grade Pay");
                    $('#txtGP').focus();
                    return false;
                }
                if ($('#txtBasic').val() == "") {
                    alert("Please enter Pay");
                    $('#txtBasic').focus();
                    return false;
                }
                if ($('#txtWEFDt').val() == "") {
                    alert("Please enter Date of Effect of Pay");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form:form action="itdeclarationData.htm" commandName="command">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Investment Declaration
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empId" id="empId"/>


                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txtEmployeeName">Name of the Employee <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="txtEmployeeName" id="txtEmployeeName"/>
                            </div>
                            <div class="col-lg-3">
                                <label for="txtAddress"> Address of the Employee <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txtPan">Permanent Account Number</label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>                           
                            </div>
                            <div class="col-lg-3">
                                <label for="txtFy"> Financial Year <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="txtFy" id="txtFy"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label for="txtFy" class="text-center"> Details of claims and evidence thereof </label>
                            </div>
                        </div>

                        

                        

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label for="slno1">   House Rent Allowance:  </label>         
                            </div>
                            
                            
                        </div>       
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="slno">  Rent paid to the landlord </label>                   
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   
                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> Name of the landlord </label>                  
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   
                            </div>
                        </div>  
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="slno"> Address of the landlord </label>         
                            </div>
                            <div class="col-lg-3">
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                            <div class="col-lg-3">
                                Permanent Account Number of the landlord
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   
                            </div>
                        </div> 
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label for="note1"> Note: Permanent Account Number shall be furnished if the aggregate rent paid during the previous year exceeds one lakh rupees </label>                         
                            </div>
                        </div>    
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="slno"> Leave Travel Concessions or assistance </label>     
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>                          
                            </div>
                            <div class="col-lg-3">
                                Evidence/ Particulars
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>   
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label for="slno"> Deduction of interest on borrowing: </label>                         
                            </div>
                            
                        </div>       
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="slno"> Interest payable/paid to the lender </label>          
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>        
                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> Name of the lender </label>        
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                        </div>      
                         
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="slno"> Address of the lender </label>  
                            </div>
                            <div class="col-lg-3">
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>                  
                            </div>
                            <div class="col-lg-3">
                                
                            </div>
                            <div class="col-lg-3">   
                                
                            </div>
                        </div>     
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label for="slno"> Permanent Account Number of the lender </label>  
                            </div>
                            
                            
                        </div>    
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="slno"> (a) Financial Institutions(if available) </label>  
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>                        
                            </div>
                            <div class="col-lg-3">
                                
                            </div>
                            <div class="col-lg-3">   
                                
                            </div>
                        </div>     
                        <div class="row" style="margin-bottom: 7px;">
                            
                            <div class="col-lg-3">
                                <label for="slno"> (b) Employer(if available) </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                
                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>  
                        <div class="row" style="margin-bottom: 7px;">
                            
                            <div class="col-lg-3">
                                <label for="slno"> (c) Others </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                
                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label for="slno"> Deduction under Chapter VI-A</label> 
                            </div>
                            
                        </div> 

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label for="slno"> (A) Section 80C, 80CCC and 80CCD </label>        
                            </div>
                            
                        </div>  
                        <div class="row" style="margin-bottom: 7px;">
                            
                            <div class="col-lg-12">
                                <label for="slno"> (i) Section 80C </label>                         
                            </div>
                            
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (a) <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                               
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (b) <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                
                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (c) <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                
                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (d) <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                
                            </div>
                        </div>    
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (e) <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (f) <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (g) <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (ii) Section 80CCC </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (iii) Section 80CCD </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (B) Other sections (e.g. 80E, 80G, 80TTA, etc.) under Chapter VI-A. </label>                         
                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">   

                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (i) section <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                            <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (ii) section <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                            <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (iii) section <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                            <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (iv) section <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                            <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">
                                <label for="slno"> (v) section <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>   </label>                         
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPan" id="txtPan" readonly="true"/>       
                            </div>
                            <div class="col-lg-3">   
                                <form:textarea class="form-control" path="txtAddress" id="txtAddress" cols="80"/>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <button type="submit" name="submit" value="Save" class="btn btn-default" onclick="return saveCheck();">Save Posting</button>
                            <a href="itdeclarationList.htm"> <button type="button" name="submit" value="Return" class="btn btn-default">Return</button></a>
                        </div>
                    </div>
                </div>




            </form:form>
    </body>
</html>
