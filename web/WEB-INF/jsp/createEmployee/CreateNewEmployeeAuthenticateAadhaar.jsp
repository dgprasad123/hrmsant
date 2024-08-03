<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Create Employee Authenticate Aadhaar::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function generateOTP()
            {
                var x = document.getElementById("aadharNumber").value;
                if (x == '')
                {
                    alert("Please enter your Aadhar Number.");
                    return false;
                }
                var regexp = /^[0-9]{4}[0-9]{4}[0-9]{4}$/;

                if (!regexp.test(x))
                {
                    alert("Please enter a valid Aadhar Number.");
                    return false;
                }
                $('#btn_validate').prop("disabled", true);
                $('#loader_blk').css("display", "block");
                $.ajax({
                    type: 'POST',
                    url: "GenerateAadharOTPCreateEmployee.htm",
                    data: {"aadhar": $('#aadharNumber').val()},
                    success: function(data) {
                        if (data.error == '-1')
                        {
                            $('#aadharerror_blk').html("Error: The Aadhar Number is already associated with an employee!");
                            $('#aadharerror_blk').css('display', 'block');
                            $('#btn_validate').prop("disabled", false);
                            $('#loader_blk').css("display", "none");
                        }
                        else if (data.error == '-2')
                        {
                            $('#aadharerror_blk').html("Error in generating OTP. Please try after sometime!");
                            $('#aadharerror_blk').css('display', 'block');
                            $('#btn_validate').prop("disabled", false);
                            $('#loader_blk').css("display", "none");
                        }
                        else
                        {
                            //jsonObj = JSON.parse(data);
                            $("#hidAadhaarNo").val($('#aadharNumber').val());
                            $('#aadharerror_blk').css('display', 'none');
                            $('#btn_validate').prop("disabled", false);
                            $('#loader_blk').css("display", "none");
                            $('#lbl_mobile').html(data.mobileNo);
                            $('#mobile_blk').css("display", "block");
                            $('#generate_blk').css('display', 'none');
                            $('#otp_blk').css('display', 'block');
                            //alert(data.transactionId);
                            $('#transactionId').val(data.transactionId);
                            //alert(data.error + "->" + data.mobileNo + "->" + data.txn);

                            counter = setInterval(timer, 1000);
                            return false;
                        }
                    }
                });
            }

            var count = 600;
            function timer()
            {

                count = count - 1;
                if (count <= 0)
                {
                    clearInterval(counter);
                    //counter ended, do something here
                    $('#otp_blk').css("display", "none");
                    $('#generate_blk').css('display', 'block');
                    $('#btn_validate').prop("disabled", false);
                    return;
                }
                timing = "";
                seconds = count % 60;
                minutes = Math.floor(count / 60);

                document.getElementById("clock").innerHTML = minutes + " minutes " + seconds + " seconds"; // watch for spelling
                //Do code for showing the number of seconds here
            }
            function validateAadhar()
            {
                var x = document.getElementById("otp").value;
                if (x == '')
                {
                    alert("Please enter your OTP.");
                    return false;
                }
                $('#btn_authenticate').prop("disabled", true);
                $('#loader_blk1').css('display', 'block');
                $.post("AuthenticateAadhar.htm", {otp: $('#otp').val(), transactionId: $('#transactionId').val(), aadhar: $('#aadharNumber').val()})
                        .done(function(data) {
                            if (data.error == "")
                            {
                                $('#verify_blk').css('display', 'block');
                                $('#aadhar_wrapper').css('display', 'none');
                                window.location = "createNewEmployee.htm?aadhaarno="+$("#hidAadhaarNo").val();
                            }
                            else {
                                $('#btn_authenticate').prop("disabled", false);
                                $('#loader_blk1').css('display', 'none');
                                $('#mobile_blk').css('display', 'none');
                                $('#timer_blk').css('display', 'none');
                                $('#otperror_blk').html("Error: " + data.error + "!");
                                $('#otperror_blk').css('display', 'block');
                            }
                        });
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-3"></div>
                        <div class="col-lg-7" style="color:red;font-size:20px;">
                            Please Authenticate Employee's Aadhaar before creating Employee in HRMS.
                        </div>
                        <div class="col-lg-2"></div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="row" style="text-align:center;">
                        <img src="images/aadhar.png" width="400" />
                    </div>
                    <div id="aadhar_wrapper" style="display:block;">
                        <div id="generate_blk" style="border-top:3px solid #CCC;padding:10px;">
                            <div class="form-group">
                                <label for="aadharNumber">Aadhar Number:</label>
                                <input type="hidden" id="hidAadhaarNo"/>
                                <input type="text" class="form-control input-lg" style="font-size:20pt;" maxlength="12" placeholder="Enter Aadhaar Number" id="aadharNumber"/>
                                <p style="color:#FF0000;font-style:italic;font-weight:bold;font-size:18pt;display:none;" id="aadharerror_blk"></p>
                            </div>
                            <button type="button" class="btn btn-primary" id="btn_validate" onclick="javascript: generateOTP()">Generate OTP</button>
                        </div>
                        <div id="otp_blk" style="display:none;" style="border-top:3px solid #CCC;padding:10px;">
                            <p style="color:#FF0000;font-style:italic;display:none;font-weight:bold;font-size:14pt;margin-top:7px;" id="mobile_blk">An OTP has been sent to your Mobile No. <span id="lbl_mobile"></span></p>

                            <div class="form-group">
                                <label for="otp">Enter OTP:</label>
                                <input type="text" name="otp"  class="form-control input-lg" style="font-size:20pt;width:300px;" maxlength="6" placeholder="OTP" id="otp">
                                <input type="hidden" name="transactionId" id="transactionId" value="" />
                                <p style="color:#008900;font-style:italic;font-weight:bold;font-size:14pt;" id="timer_blk">Your OTP is going to expire within <span id="clock"></span></p>
                                <p style="color:#FF0000;font-style:italic;font-weight:bold;font-size:18pt;display:none;" id="otperror_blk"></p>
                                <button type="button" class="btn btn-primary" id="btn_authenticate" onclick="javascript: validateAadhar()">Validate</button>
                            </div>            
                        </div>
                        <div id="loader_blk" style="display:none;text-align:center;">
                            <div style="width:100%;text-align:center;height:100px;overflow:hidden;"><img src="images/loader-new.gif" style="margin-top:-50px;" /></div>
                            <strong style="display:block;color:#008900;font-style:italic;font-size:16pt;">Please wait<br />Generating OTP...</strong>
                        </div>
                        <div id="loader_blk1" style="display:none;">
                            <div style="width:100%;text-align:center;height:70px;overflow:hidden;"><img src="images/loader-blue.gif" width="300" style="margin-top:-80px;" /><br />
                            </div>
                            <div style="width:100%;text-align:center;color:#0085FF;font-size:16pt;font-weight:bold;">Verifying your OTP...</div>
                        </div>
                    </div>
                    <div id="verify_blk" style="text-align:center;display:none;">
                        <span style="color:#008900;font-weight:bold;font-size:28pt;">Congratulations!</span><br />
                        <img src="images/verified-animated.gif" width="200" /><br />
                        <span style="color:#666666;font-weight:bold;font-size:20pt;">Your Aadhar Number has been verified successfully.</span><br />
                    </div>
                </div>
                <div class="panel-footer">
                </div>
            </div>
        </div>
    </body>
</html>
