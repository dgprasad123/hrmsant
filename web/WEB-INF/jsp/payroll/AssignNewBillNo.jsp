<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function getBillDetail()
            {
                if($('#billNo').val() == '')
                {
                    alert("Please enter a valid Bill Number.");
                    $('#billNo')[0].focus();
                    return false;
                }
                if(isNaN($('#billNo').val()))
                {
                    alert("Please enter a valid Bill Number.");
                                        $('#billNo')[0].focus();
                                        $('#billNo')[0].select();
                    return false;
                }
                $('#loader').css('display', 'block');
                $.ajax({
                    url: 'GetPreviousBillDetail.htm',
                    type: 'get',
                    data: 'billNo='+$('#billNo').val(),
                    success: function (retVal) {
                        $('#loader').css('display', 'none');
                        $('#resultDetails').html(retVal);
                    }
                });
            }
            function regenerateBillNo(billNo)
            {
                if(confirm("Are you sure you want to reassign a Bill number to the existing?"))
                {
                    $('#loader1').css('display', 'block');
                    $('#btn_generate')[0].disabled = true;
                    $.ajax({
                                        url: 'SaveNewBillNo.htm',
                                        type: 'get',
                                        data: 'billNo='+$('#billNo').val(),
                                        success: function (retVal) {
                                            $('#loader1').css('display', 'none');
                                            $('#btn_generate')[0].disabled = false;
                                           $('#resultDetails').html(retVal);
                                        }
                                    });                    
                }
            }
                    </script>
    </head>
    </head>
    <body>
            <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid" style="min-height:500px;">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Assign New Bill Number
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2 style="margin-top:0px;">Assign New Bill Number</h2>
                        <div class="panel-body">
                            <form name="frmBill" method="post">
                        <div class="row">
                            <div class="col-lg-12">

                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="amonth" style="text-align:right;">Bill Number:</label>
                                    <div class="col-sm-2">
                                        <input type="text" name="billNo" id="billNo" value="" />
                                    </div>
                                    <div class="col-sm-2"><input type="button" class="btn btn-sm btn-success " value="Search" onclick="javascript:getBillDetail()" />
                                    <input type="button" class="btn btn-sm btn-success " value="Reset" onclick="javascript: self.location='AssignNewBillNo.htm'" /></div>
                                    <div class="col-sm-2"><span id="loader" style="display:none;color:#999999;font-size:8pt;font-style:italic;"><img src="images/ajax-loader.gif" /> Please wait...</span></div>
                                </div>                                    
                                
                            </div>
                            </form>
                        </div>
                                <div class="row" id="resultDetails" style="margin-top:10px;">
                                    
                                </div>
                    </div>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
    </body>
</html>
