<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
    StringBuffer sb = new StringBuffer();
    for (int i = 1; i <= 3; i++) {
        //sb.append((char) (int) (Math.random() * 79 + 23 + 7));
        int x = (int) (Math.random() * 20) + 5;
        //System.out.println("Random String is: "+x);
        //sb.append((int)(Math.random() * 20) + 5);
        sb.append(x);
    }
    String cap = new String(sb);
%>
<html>
    <head>
        <title>:: Welcome to HRMS Odisha ::</title>

        <!-- Bootstrap core CSS -->
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script type="text/javascript" src="js/aes.js"></script>
        <script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <style>
            body, html {
                height: 100%;
                background-repeat: no-repeat;
                /*background-image: linear-gradient(rgb(62,134,196), rgb(60,134,194));
                background: url(images/HRMS_PAYROLL_BANNER.PNG) no-repeat center center fixed; 
                background-color: #F7F7F7;*/
                -webkit-background-size: cover;
                -moz-background-size: cover;
                -o-background-size: cover;
                background-size: contain;
                background: url('images/bg.png');
            }

            .card-container.card {
                max-width: 350px;
                padding: 40px 40px;
            }

            .btn {
                font-weight: 700;
                height: 36px;
                -moz-user-select: none;
                -webkit-user-select: none;
                user-select: none;
                cursor: default;
            }

            /*
             * Card component
             */
            .card {
                background-color: #F7F7F7;
                /* just in case there no content*/
                padding: 20px 25px 30px;
                margin: 0 auto 25px;
                margin-top: 20px;
                /* shadows and rounded borders */
                -moz-border-radius: 2px;
                -webkit-border-radius: 2px;
                border-radius: 2px;
                -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
                -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
                box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
            }

            .profile-img-card {
                width: 96px;
                height: 96px;
                margin: 0 auto 10px;
                display: block;
                -moz-border-radius: 50%;
                -webkit-border-radius: 50%;
                border-radius: 50%;
            }

            /*
             * Form styles
             */
            .profile-name-card {
                font-size: 16px;
                font-weight: bold;
                text-align: center;
                margin: 10px 0 0;
                min-height: 1em;
            }

            .reauth-email {
                display: block;
                color: #404040;
                line-height: 2;
                margin-bottom: 10px;
                font-size: 14px;
                text-align: center;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                -moz-box-sizing: border-box;
                -webkit-box-sizing: border-box;
                box-sizing: border-box;
            }

            .form-signin #inputEmail,
            .form-signin #inputPassword {
                direction: ltr;
                height: 44px;
                font-size: 16px;
            }

            .form-signin input[type=email],
            .form-signin input[type=password],
            .form-signin input[type=text],
            .form-signin button {
                width: 100%;
                display: block;
                margin-bottom: 10px;
                z-index: 1;
                position: relative;
                -moz-box-sizing: border-box;
                -webkit-box-sizing: border-box;
                box-sizing: border-box;
            }

            .form-signin .form-control:focus {
                border-color: rgb(104, 145, 162);
                outline: 0;
                -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgb(104, 145, 162);
                box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgb(104, 145, 162);
            }

            .btn.btn-signin {
                /*background-color: #4d90fe; */
                background-color: #0F487E;
                /* background-color: linear-gradient(rgb(104, 145, 162), rgb(12, 97, 33));*/
                padding: 0px;
                font-weight: 700;
                font-size: 14px;
                height: 36px;
                -moz-border-radius: 3px;
                -webkit-border-radius: 3px;
                border-radius: 3px;
                border: none;
                -o-transition: all 0.218s;
                -moz-transition: all 0.218s;
                -webkit-transition: all 0.218s;
                transition: all 0.218s;
                cursor:pointer;
            }

            .btn.btn-signin:hover,
            .btn.btn-signin:active,
            .btn.btn-signin:focus {
                background-color: #5283D6;
            }

            .forgot-password {
                color: #498BF4;
            }

            .forgot-password:hover,
            .forgot-password:active,
            .forgot-password:focus{
                text-decoration:underline;
            }
            #hrms_footer{width:100%;height:30px;background:#EAEAEA;border-top:1px solid #DADADA;color:#555555;line-height:30px;font-size:9pt;text-align:center;}
            #error_msg{display: block;
                       color: #FF0000;font-weight:bold;
                       line-height: 2;
                       margin-bottom: 10px;
                       font-size: 14px;
                       text-align: center;
                       overflow: hidden;
                       text-overflow: ellipsis;
                       white-space: nowrap;
                       -moz-box-sizing: border-box;
                       -webkit-box-sizing: border-box;
                       box-sizing: border-box;}
            </style>

            <script type="text/javascript">


                if (top.frames.length != 0) {
                    if (window.location.href.replace)
                        top.location.replace(self.location.href);
                    else
                        top.location.href = self.document.href;
                }
                $(document).ready(function () {
                    $("#userName").attr('placeholder', 'User Id');
                    $("#password").attr('placeholder', 'password');
                    $("#cap1").attr('placeholder', 'Captcha');
                    document.getElementById('captcha_id').src = 'captcha.jpg?' + Math.random();

                    $('#dd').dialog({
                        title: 'Forgot Password',
                        width: 500,
                        height: 400,
                        closed: false,
                        cache: false,
                        modal: true,
                        onClose: function () {
                            $('#dd').dialog('refresh');
                            $('#mobile').val('');

                        }
                    });
                    $('#dd').dialog('close');

                    $('#fpMaintanance').dialog({
                        title: 'Maintanance',
                        width: 500,
                        height: 400,
                        closed: false,
                        cache: false,
                        modal: true,
                        onClose: function () {
                        }
                    });
                    $('#fpMaintanance').dialog('close');

                    $('input#cap1').bind('copy paste', function (e) {
                        e.preventDefault();
                    });

                    $('#otpdialog').dialog({
                        title: 'One Time Password',
                        width: 500,
                        height: 400,
                        closed: false,
                        cache: false,
                        modal: true,
                        onClose: function () {
                            $('#dd').dialog('refresh');
                            $('#otpdialog').dialog('refresh');
                        }
                    });
                    $('#otpdialog').dialog('close');

                    $('#resetPasswordDialog').dialog({
                        title: 'Reset Password',
                        width: 500,
                        height: 400,
                        closed: false,
                        cache: false,
                        modal: true,
                        onClose: function () {
                            $('#dd').dialog('refresh');
                            $('#otpdialog').dialog('refresh');
                            $('#resetPasswordDialog').dialog('refresh');
                        }
                    });
                    $('#resetPasswordDialog').dialog('close');

                    /*$('#loginotpdialog2').dialog({
                     title: 'Login OTP',
                     width: 500,
                     height: 400,
                     closed: false,
                     cache: false,
                     modal: true,
                     onClose: function() {
                     
                     }
                     });
                     $('#loginotpdialog2').dialog('close');*/
                    $('#loginotpdialog2').hide();

                    $("#usernamediv").show();
                    $("#otpdiv").hide();
                });

                function sendRequest() {
                    $("#mobile").val();

                    var mob = /^[1-9]{1}[0-9]{9}$/;
                    var dateExp = /^(([0-9])|([0-2][0-9])|([3][0-1]))\-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\-\d{4}$/;
                    var dateofbirth = $('#dateofbirth');

                    if ($("#mobile").val() == '') {
                        alert('Mobile Number cannot be blank');
                        $("#mobile").focus();
                        return false;
                    } else if (dateofbirth.val() == '') {
                        alert('Date of Birth cannot be blank');
                        return false;
                    } else if (mob.test($.trim($("#mobile").val())) == false) {
                        alert('Please enter a valid mobile number.');
                        return false;
                    } else if (dateExp.test($.trim(dateofbirth.val())) == false) {
                        alert('Please enter a valid Date.');
                        return false;
                    } else if ($('#cap1').val() == '') {
                        alert('Please enter the shown Characters in the Captcha field');
                        $('#cap1').focus();
                        return false;
                    } else {
                        $("#msgid").html('');
                        $("#msgid").css("text-align", "center");
                        $("#msgid").html('<span><img src="images/ajax-loader.gif" width="20" height="20"/>&nbsp;&nbsp;Please Wait...<\/span>');
                        $.ajax({
                            type: 'POST',
                            url: 'ForgotPassword.htm?mobile=' + $("#mobile").val() + '&dob=' + dateofbirth.val() + '&cap1=' + $('#cap1').val() + '&cap2=' + $('#cap2').val(),
                            success: function (msg) {
                                var str = '';
                                //alert("Msg is: "+msg);
                                setTimeout(function () {
                                    $("#msgid").html('');
                                    if (msg == 1) {
                                        //str = 'User Id and Password has been sent to your mobile.';
                                        //$("#msgid").text(str);
                                        $('#dd').dialog('close');
                                        $('#otpdialog').dialog('open');
                                    } else if (msg == "2") {
                                        str = '<h3>Your mobile number is not registered in HRMS.<br\/> ';
                                        str = str + 'Note: To register your mobile no, Please Contact<br\/>';
                                        str = str + 'your billing section or DDO.<br\/>';

                                        str = str + '<a href="https://drive.google.com/file/d/0B6DGe7Iwnj3vMGNPZExkTWNkY2c/view?usp=sharing">Click Here<\/a> how billing section or DDO can register<br\/>';
                                        str = str + 'the Mobile No<\/h3>';
                                        $("#msgid").append(str);
                                    } else if (msg == 3) {
                                        str = '<span style="color:red;"><br \/>Request limit is 3 times per day.</\span>';
                                        $("#msgid").append(str);
                                    } else if (msg == "4") {
                                        str = '<span style="color:red;"><br \/>Combination of Mobile and Date of Birth does not match!</\span>';
                                        $("#msgid").append(str);
                                    } else if (msg == "5") {
                                        str = '<span style="color:red;"><br \/>Invalid Captcha Code!</\span>';
                                        $("#msgid").append(str);
                                    } else if (msg == 6) {
                                        str = '<span style="color:red;"><br \/>Wait for 5 mins for Next Request!</\span>';
                                        $("#msgid").append(str);
                                    } else if (msg == 7) {
                                        str = '<span style="color:red;"><br \/>Mobile Number cannot be blank.</\span>';
                                        $("#msgid").append(str);
                                    }
                                }, 3000);
                            }
                        });


                    }

                }
                function ssoLogin() {
                    var myWindow = window.open("ssosamlrequestcreator", "ePraman", "width=500,height=500");
                }

                function validateOTP() {
                    $("#otpmsgid").css("text-align", "center");
                    $("#otpmsgid").html('<span><img src="images/ajax-loader.gif" width="20" height="20"/>&nbsp;&nbsp;Validating OTP...<\/span>');
                    $.ajax({
                        type: 'POST',
                        url: "ValidateForgotPasswordOTP.htm?enteredotp=" + $("#otp").val(),
                        success: function (data) {
                            var dataArr = data.split("@");
                            var status = parseInt(dataArr[0]);
                            var username = dataArr[1];
                            setTimeout(function () {
                                $("#otpmsgid").html('');
                                if (status == 0) {
                                    $("#resetusername").text("Username: " + username);
                                    $('#otpdialog').dialog('close');
                                    $('#resetPasswordDialog').dialog('open');
                                } else if (status == -1) {
                                    $("#otpmsgid").html('<span>Invalid OTP<\/span>');
                                }
                            }, 3000);
                        }
                    });
                }

                function resetPassword() {
                    $("#resetpasswordmsgid").css("text-align", "center");
                    $("#resetpasswordmsgid").html('<span><img src="images/ajax-loader.gif" width="20" height="20"/>&nbsp;&nbsp;Resetting Password...<\/span>');
                    $.ajax({
                        type: 'POST',
                        url: "ResetForgotPassword.htm?newpassword=" + encodeURIComponent($("#newpassword").val()) + "&confirmpassword=" + encodeURIComponent($("#confirmpassword").val()),
                        success: function (data) {
                            setTimeout(function () {
                                $("#resetpasswordmsgid").html('');
                                if (data == 1) {
                                    $("#resetpasswordbtn").hide();
                                    $("#resetpasswordmsgid").html('<span style="color:#008000;">Reset Password Successful<\/span>');
                                } else if (data == 2) {
                                    $("#resetpasswordmsgid").html('<span style="color:#FF0000;">Passwords do not match!<\/span>');
                                } else if (data == 3) {
                                    $("#resetpasswordmsgid").html('<span style="color:#FF0000;">Please enter both New Password and Confirm Password!<\/span>');
                                } else if (data == 4) {
                                    $("#resetpasswordmsgid").html('<span style="color:#FF0000;">Password does not meet the policy!<\/span>');
                                } else if (data == 5) {
                                    $("#resetpasswordmsgid").html('<span style="color:#FF0000;">Password must not be less than 8 characters!<\/span>');
                                } else if (data == -1) {
                                    $("#resetpasswordmsgid").html('<span style="color:#FF0000;">Session Timed Out!<\/span>');
                                }
                            }, 3000);
                        }
                    });
                }

                $.extend($.fn.validatebox.defaults.rules, {
                    confirmPass: {
                        validator: function (value, param) {
                            var pass = $(param[0]).passwordbox('getValue');
                            return value == pass;
                        },
                        message: 'New Password does not match with Confirm Password.'
                    },
                    minLength: {
                        validator: function (value, param) {
                            return value.length >= param[0];
                        },
                        message: 'Please enter at least {0} characters.'
                    }
                })

                function otpLogin() {
                    $("#usernamediv").hide();
                    $("#otpdiv").show();
                    $('#loginotpdialog2').hide();
                }
                function userNameLogin() {
                    $("#usernamediv").show();
                    $("#otpdiv").hide();
                    $("#loginotpdialog2").hide();
                }


                function sendLoginOTP() {
                    if (document.getElementById("userName1").value == "" && document.getElementById("mobile1").value == "") {
                        alert("Please enter username or mobile");
                        return false;
                    } else {
                        $("#loginotploadersendotp").css("display", "block");
                        $.ajax({
                            type: 'POST',
                            url: "SendLoginOTP.htm?userName=" + $("#userName1").val() + "&mobile=" + $("#mobile1").val(),
                            success: function (data) {
                                /*var dataArr = data.split("-");
                                 var mobile = parseInt(dataArr[0]);
                                 var otp = parseInt(dataArr[1]);*/
                                setTimeout(function () {
                                    $("#loginotpmsgid1").html('');
                                    if (data != '' && data != 'MI' && data != 'UI') {
                                        $("#otpdiv").hide();
                                        $('#loginotpdialog2').show();
                                        $("#mobilelabel").text(data);
                                    } else if (data != '' && data == 'MI') {
                                        $("#errormsg").html('<span style="color:red;">Invalid Mobile No<\/span>');
                                    } else if (data != '' && data == 'UI') {
                                        $("#errormsg").html('<span style="color:red;">Invalid Username<\/span>');
                                    }
                                }, 3000);
                            }
                        });
                    }
                }

                function validateLoginOTP() {
                    $("#loginotpmsgid").css("text-align", "center");
                    $("#loginotpmsgid").html('<span><img src="images/ajax-loader.gif" width="20" height="20"/>&nbsp;&nbsp;Validating OTP...<\/span>');
                    $("#loginotploadervalidateloginotp").css("display", "block");
                    $.ajax({
                        type: 'POST',
                        url: "ValidateLoginOTP.htm?enteredloginotp=" + $("#loginotp").val(),
                        success: function (data) {
                            setTimeout(function () {
                                $("#loginotpmsgid").html('');
                                if (data == 'Y') {
                                    //$('#loginotpdialog2').dialog('close');
                                    $("#userPassword").val('');
                                    $("#loginType").val("OTP");
                                    document.getElementById("loginform").submit();
                                } else if (data == 'N') {
                                    $("#loginotpmsgid2").html('<span>Invalid OTP</span>');
                                }
                            }, 3000);
                        }
                    });
                }

            </script>
        </head>

        <body>
            <div style="text-align:center;padding:5px;background:#FAFAFA;border-bottom:1px solid #DADADA;">
            <a href="https://hrmsodisha.gov.in"><img src="images/temp_logo.png" alt="Human Resource Management System" /></a>
        </div>
        <h1>

            <!-- <div align="center" style='color:red; font-size: 20px;'>
                 Balance slip for Long Term Advances (HBA,MCA,Computer) for 2018-19 uploaded by AG. View/download the same through HRMS login.
                 <a href="images/Agalert2.jpg" target="_blank">Click here to View Details </a>
             </div>-->

            <!--                 <div align="center" style='color:red; font-size: 20px;'>
                  <div align="center" style='color:red; font-size: 20px;'>
                          HRMS WEBSITE WILL NOT BE AVAILABLE FROM 02-MAR-2024 9 PM TO 03-MAR-2024 9 AM
                          DUE TO SERVER MAINTENANCE.<br />INCONVENIENCE IS HIGHLY REGRETTED.<BR />-->
            <!--<a href="images/SERVER_HRMS.pdf" target="_blank">Click to view Notification</a> 
          </div>-->

            <!--<div align="center" style='color:red; font-size: 20px;'>
                  If You have any HRMS Related Issues Please raise an Online Ticket in Your HRMS Login
            </div>-->

        </h1>
        <div class="container">
            <!--            <div style="width:95%;text-align:center;font-size:14pt;font-weight:bold;padding:10px;color:#FF0000;margin:0px auto;border:4px solid #FF0000;background:#EAEAEA">
                            Please update your HRMS Profile before 15.02.2024.<br />(Ref: GA & PG Deptt Letter No.1564/AR-12-01-2024). 
                            <a href="https://drive.google.com/file/d/1Wvo7gzotnonWkigZFvyTczqSMRk4k69-/view?usp=sharing" target="_blank">Click on the link for details</a>
                            HRMS website will be down from 1pm to 11pm today(26.01.2024) due to server maintenance.<br />Inconvenience is deeply Regretted.
                        </div>-->
             <div style="width:95%;text-align:center;font-size:14pt;font-weight:bold;padding:10px;color:#FF0000;margin:0px auto;border:4px solid #FF0000;background:#EAEAEA">
                The facility for submission of PAR for the years 2014-15 to 2022-23 is extended for a period of 1 month till 4th July 2024. Those who have not submitted their PAR for the above period should complete the task within due date
                <a href="https://drive.google.com/file/d/1U3Dh4xP7cq1KDkegYqmxALEKIraCLcen/view?usp=sharing" target="_blank">Click to view Notification</a> 
            </div>
            <div style="width:95%;text-align:center;font-size:14pt;font-weight:bold;padding:10px;color:#0000FF;margin:0px auto;border:4px solid #FF0000;background:#EAEAEA">
                Dateline for Submission of Self appraisal for the Year 2023-2024 is extended up to 4th July 2024.
               <!-- <a href="https://drive.google.com/file/d/19pX7ns5AWu6xUA2v3HY4oWXVHr09QVkO/view?usp=sharing" target="_blank">Click to view Notification</a>                 -->
            </div>
            <div class="">
                <!--   <div style="width:95%;text-align:center;font-size:16pt;font-weight:bold;padding:10px;color:#FF0000;margin:0px auto;border:4px solid #FF0000;background:#EAEAEA">
                       On submission of bill in HRMS, Token will be generated from IFMS Application of Finance Department.<br />
                       <span style="font-size:18pt;">Please Contact IFMS Helpline No: 1800 3456 739 (Toll Free) for generation of token.</span>
                   </div>-->
                <div class="card card-container" id="usernamediv">
                    <img id="profile-img" class="profile-img-card" src="images/logo3.png" />
                    <p id="profile-name" class="profile-name-card">Login to your Account</p>
                    <form class="form-signin" id="loginform" action="login.htm" commandName="loginForm" method="post" onsubmit="encrtyptPassword()" autocomplete="off">
                        <input type='hidden' name='randomText' id='randomTextValue' />
                        <input type='hidden' name='loginType' id='loginType' />                    
                        <span id="error_msg" class="error_msg"> ${errorMsg} </span>
                        <input type="text" name="userName" readonly id="userName" class="form-control" placeholder="User Id" onfocus="if (this.hasAttribute('readonly')) {
                                    this.removeAttribute('readonly');

                                    // fix for mobile safari to show virtual keyboard
                                    this.blur();
                                    this.focus();
                                }" required autofocus>
                        <input type="password" readonly name="userPassword" id="userPassword" class="form-control" placeholder="Password"  onfocus="if (this.hasAttribute('readonly')) {
                                    this.removeAttribute('readonly');
                                    // fix for mobile safari to show virtual keyboard
                                    this.blur();
                                    this.focus();
                                }" required>
                        <%--Captcha for production--%>
                        <div style="margin-bottom:10px;"><img id="captcha_id" name="imgCaptcha" src="captcha.jpg">
                            <span style="color:#888888;font-size:9pt;font-style:italic;">Can&rsquo;t read?</span> <a style="font-size:9pt;" href="javascript:void(0)" onclick="document.getElementById('captcha_id').src = 'captcha.jpg?' + Math.random();
                                    return false">Click here</a></div>
                        <input type="text" name="captcha" id="captcha" class="form-control" placeholder="Security Code" required>
                        <%--Captcha for local--%>
                        <%--  <div style="margin-bottom:10px;"><img id="captcha_id" name="imgCaptcha" src="captcha.jpg">
                             <span style="color:#888888;font-size:9pt;font-style:italic;">Can&rsquo;t read?</span> <a style="font-size:9pt;" href="javascript:void(0)" onclick="document.getElementById('captcha_id').src = 'captcha.jpg?' + Math.random();
                                     return false">Click here</a></div>
                         <input type="text" name="captcha" id="captcha" class="form-control" placeholder="Security Code" required>
                        
                        --%>

                        <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">Login</button>
                        <a href="javascript:ssoLogin()">Login using e-Pramaan</a><br />
                        <!--                        <a href="javascript:otpLogin();">Login using OTP</a>-->
                    </form>

                    <a class="forgot-password" href="javascript:void(0)" onclick="$('#dd').dialog('open')"> 
                        <%--  <a class="forgot-password" href="javascript:void(0)" onclick="$('#fpMaintanance').dialog('open')"> --%> 
                        Forgot your password?
                    </a>

                </div>

                <div class="card card-container" id="otpdiv">
                    <img id="profile-img" class="profile-img-card" src="images/logo3.png" />
                    <p id="profile-name" class="profile-name-card">Login Through OTP </p>
                    <input type='hidden' name='randomText' id='randomTextValue' />
                    <span id="error_msg" class="error_msg"> ${errorMsg} </span>
                    <input type="text" name="userName1" id="userName1" class="form-control" placeholder="User Name" required autofocus/>
                    <span style="display:block;text-align:center;color:#898989;font-weight:bold;">OR</span>
                    <input type="mobile" name="mobile" id="mobile1" class="form-control" placeholder="Mobile Number" required/> <br />                   
                    <span id="errormsg"></span>
                    <div id="loginotploadersendotp" style="display:none;width:100%;text-align:center;"> <img src="images/loading.gif"/></div>
                    <button class="btn btn-lg btn-primary btn-block btn-signin" type="button" onclick="sendLoginOTP();">Send OTP</button>
                    <a href="javascript:ssoLogin()">Login using e-Pramaan</a><br />
                    <a href="javascript:userNameLogin()">Login using UserName/Password</a>
                    <br>
                    <a class="forgot-password" href="javascript:void(0)" onclick="$('#dd').dialog('open')"> 
                        <%--  <a class="forgot-password" href="javascript:void(0)" onclick="$('#fpMaintanance').dialog('open')"> --%> 
                        Forgot your password?
                    </a>
                </div>
            </div>
            <div class="card card-container" id="loginotpdialog2" style="padding:20px;">
                <img id="profile-img" class="profile-img-card" src="images/logo3.png" />
                <p id="profile-name" class="profile-name-card">Please enter OTP received in your Mobile Number <span id="mobilelabel"></span></p>

                <input type="text" id="loginotp" class="form-control" placeholder="Enter One Time Password" required autofocus/><br />

                <div id="loginotploadervalidateloginotp" style="display:none;width:100%;text-align:center;"> <img src="images/loading.gif"/></div>

                <button class="btn btn-lg btn-primary btn-block btn-signin" type="button" onclick="validateLoginOTP();">Validate Login OTP</button>

                <div id="loginotpmsgid2" style="color:#008000;font-weigth:bold 14 px solid;"></div>
                <a href="javascript:ssoLogin()">Login using e-Pramaan</a><br />
                <a href="javascript:userNameLogin()">Login using UserName/Password</a>
                <br>
                <a class="forgot-password" href="javascript:void(0)" onclick="$('#dd').dialog('open')"> 
                    <%--  <a class="forgot-password" href="javascript:void(0)" onclick="$('#fpMaintanance').dialog('open')"> --%> 
                    Forgot your password?
                </a>
            </div> 
            <div id="hrms_footer">
                Copyright © CMGI - All Rights Reserved - General Administration & Public Grievance Department, Government of Odisha
            </div>
            <div id="fpMaintanance">
                <div style="padding:10px;margin-bottom:30px;color: red;">
                    <span style="font-weight:bold;font-size:14px;">This Service is temporarily unavailable.</span><br />
                    <span style="font-weight:bold;font-size:12px;">To get Username and Password please contact District Coordinator of your District.</span>
                </div>
            </div>

            <div id="dd" style="padding:20px;">
                Enter your 10 digit Registered (HRMS) Mobile Number
                <div style="padding:10px;">
                    <input id="mobile" class="easyui-validatebox" data-options="prompt:'Enter mobile number',required:true,validType:'mobile'" style="width:100%;height:32px">
                </div>
                Enter your Date of Birth (dd-MMM-yyyy)
                <div style="padding:10px;">
                    <%--<input type="text" name="dateofbirth" id="dateofbirth" style="width:200px;" class="easyui-validatebox" data-options="prompt:'Enter Date of Birth',required:true,validType:'dateofbirth'"> For Eg. 01-JAN-2001--%>
                    <input class="easyui-datebox" id="dateofbirth" name="dateofbirth" style="width:45%" data-options="required:true,formatter:myformatter,parser:myparser" editable="false">
                </div>
                Enter the below characters in text box.
                <div style="width:100%;padding:10px;">
                    <table border="0">  
                        <tbody>  
                            <tr>  
                                <td width="40%" align="middle">  
                                    <span style="background-color: aqua;font-size:16px;"><%=cap%></span>  
                                </td>

                                <td width="40%"><input type="text" name="cap1" value="" id="cap1"/></td>  
                                <td width="20%"><input type="hidden" name="cap2" value='<%=cap%>' id="cap2" readonly="readonly"/></td>  
                            </tr>  
                        </tbody>  
                    </table>
                </div>
                <div align="center" style="width:50%;padding:10px;">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" style="width:100%;height:32px;" onclick="sendRequest()">Send Request</a>
                </div>

                <div id="msgid" style="color:#008000;font-weigth:bold 14 px solid;">

                </div>
            </div>

            <div id="otpdialog" style="padding:20px;">
                Enter the OTP received in your Mobile Number registered in HRMS
                <div style="padding:10px;">
                    <input id="otp" class="easyui-validatebox" data-options="prompt:'Enter One Time Password',required:true,validType:'otp'" style="width:100%;height:32px;">
                </div>
                <div align="center" style="width:50%;padding:10px;">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" style="width:100%;height:32px;" onclick="validateOTP();">Validate OTP</a>
                </div>
                <div id="otpmsgid" style="color:#008000;font-weigth:bold 14 px solid;"></div>
            </div>

            <div id="resetPasswordDialog" style="padding:20px;">
                <div align="center" style="padding:10px;">
                    <span id="resetusername" style="display:block;color:#FF0000;text-align:center;font-weight: bold;"></span>
                </div>
                Enter New Password
                <div style="padding:10px;">
                    <input id="newpassword" class="easyui-passwordbox" data-options="prompt:'Enter New Password',required:true,validType:{minLength:[8]}" style="width:100%;height:32px">
                </div>
                Confirm New Password
                <div style="padding:10px;">
                    <input id="confirmpassword" class="easyui-passwordbox" data-options="prompt:'Confirm Password',required:true,validType:{minLength:[8],confirmPass:['#newpassword']}" style="width:100%;height:32px">
                </div>

                <div align="center" style="width:50%;padding:10px;" id="resetpasswordbtn">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" style="width:100%;height:32px;" onclick="resetPassword();">Reset Password</a>
                </div>

                <div id="resetpasswordmsgid" style="color:#008000;font-weigth:bold 14 px solid;"></div>

                <div id="message">
                    <h6>Password must contain the following:</h6>
                    <p>A <b>lowercase</b> letter</p>
                    <p>A <b>capital (uppercase)</b> letter</p>
                    <p>A <b>number</b></p>
                    <p>Minimum <b>8 characters</b></p>
                    <p>A <b>Special</b> character </p>
                </div>
            </div>
            <script>
                function encrtyptPassword() {
                    var val = document.getElementById('userPassword').value; //assign password to a variable
                    var rkEncryptionKey = CryptoJS.enc.Base64.parse('u/Gu5posvwDsXUnV5Zaq4g==');
                    var rkEncryptionIv = CryptoJS.enc.Base64.parse('5D9r9ZVzEYYgha93/aUK2w==');
                    var utf8Stringified = CryptoJS.enc.Utf8.parse(val);
                    var encrypted = CryptoJS.AES.encrypt(utf8Stringified.toString(), rkEncryptionKey,
                            {mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7, iv: rkEncryptionIv});
                    document.getElementById('userPassword').value = encrypted.ciphertext.toString(CryptoJS.enc.Base64);
                }
            </script>
    </body>
</html>
