<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $("#validateAadhar").on("show.bs.modal", function(e) {
                var link = $(e.relatedTarget);
                $(this).find(".modal-body").load(link.attr("href"));
            });
        </script>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="IDENTITYPAGESB" />
        </jsp:include>
        <div id="profile_container">
          
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <table class="table table-bordered">
                    <thead>
                        <tr class="bg-primary text-white">
                            <th>#</th>
                            <th>Identity Type</th>
                            <th>Identity No</th>
                            <th>Place of Issue</th>
                            <th>Date of Issue</th> 
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${identityInfoList}" var="identityList" varStatus="cnt">
                            <tr>
                                <td>${cnt.index+1}</td>
                                <td>${identityList.identityDocType}</td>
                                <td>
                                    ${identityList.identityNo}
                                    <c:if test="${identityList.identityDocType eq 'AADHAAR'}">
                                        <c:if test="${identityList.isVerified eq 'Y'}">
                                            <img src="images/verified.png" width="20" height="20"/>
                                           
                                        </c:if>

                                        <c:if test="${identityList.isVerified eq 'N' }">
                                            <img src="images/error.png" width="20" height="20"/>
                                            <a href="javascript:void(0);" data-remote="false" data-toggle="modal" title="Validate" data-target="#validateAadhar" class="btn btn-warning">Validate</a>
                                              <strong style="color:red">Please validate your AADHAAR</strong>
                                        </c:if>
                                        <c:if test="${empty identityList.isVerified}">
                                            <img src="images/error.png" width="20" height="20"/>
                                            <a href="javascript:void(0);" data-remote="false" data-toggle="modal" title="Validate" data-target="#validateAadhar" class="btn btn-warning">Validate</a>
                                             <strong style="color:red">Please validate your AADHAAR</strong>
                                        </c:if>

                                    </c:if>
                                    <c:if test="${identityList.identityDocType eq 'PAN'}">
                                        <c:if test="${fn:length(identityList.identityNo) ne 10}">
                                            <span style="color:red;">Invalid PAN</span>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td>${identityList.placeOfIssue}</td>
                                <td>${identityList.issueDate}</td> 
                                <c:if test="${identityList.isLocked eq 'N' && identityList.isVerified eq 'N' }">
                                    <td>
                                        <a href="employeeIdentityEdit.htm?identityDocType=${identityList.identityDocType}" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> Edit</a>
                                  
                                 
                                        </td>
                                </c:if>
                                <c:if test="${identityList.isLocked eq 'Y' ||  identityList.isVerified eq 'Y'}">
                                    <td><img src="images/Lock.png" width="20" height="20"/></td>
                                    </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="employeeIdentityNew.htm">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th>
                                 <input type="submit" name="action" value="Add New" class="btn btn-default"/>                                 
                                </th>
                            </tr>
                        </thead>
                    </table>
                </form:form>
            </div>
        </div>
        <div id="validateAadhar" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h2 class="modal-title">Validate Aadhar</h2>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="text-align:center;">
                            <img src="images/aadhar.png" width="400" />
                        </div>
                        <div id="aadhar_wrapper" style="display:block;">
                            <div id="generate_blk" style="border-top:3px solid #CCC;padding:10px;">
                                <div class="form-group">
                                    <label for="aadharNumber">Aadhar Number:</label>
                                    <input type="hidden" id="oldAadhar" value="${oAadharNo}" />
                                    <input type="text"  class="form-control input-lg" style="font-size:20pt;" value="${oAadharNo}" maxlength="12" placeholder="Enter Aadhaar Number" id="aadharNumber">
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
                    <div class="modal-footer">
                        <span id="msg"></span>                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        var counter = 0;
        $(document).ready(function() {
            setTimeout(function() {
                $("#notification_blk").slideUp();
            }, 5000);
            $('#btn_validate').prop("disabled", false);
            $('#validateAadhar').on('hidden.bs.modal', function() {
                window.location.reload(true);
            })
        });
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
                url: "GenerateAadharOTP.htm",
                data: {"oldAadhar": $('#oldAadhar').val(), "aadhar": $('#aadharNumber').val()},
                success: function(data) {
                    if(data.error == '-1')
                    {
                            $('#aadharerror_blk').html("Error: The Aadhar Number is already associated with an employee!");
                            $('#aadharerror_blk').css('display', 'block');   
                            $('#btn_validate').prop("disabled", false);
                            $('#loader_blk').css("display", "none");
                    }
                    else if(data.error == '-2')
                    {
                            $('#aadharerror_blk').html("Error in generating OTP. Please try after sometime!<br/>"+data.errorMsg);
                            $('#aadharerror_blk').css('display', 'block');     
                            $('#btn_validate').prop("disabled", false);
                            $('#loader_blk').css("display", "none");

                    }
                    else
                    {
                        //jsonObj = JSON.parse(data);
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
        //1000 will  run it every 1 second
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

                        }
                        else {
                            $('#btn_authenticate').prop("disabled", false);
                            $('#loader_blk1').css('display', 'none');
                            $('#mobile_blk').css('display', 'none');
                            $('#timer_blk').css('display', 'none');
                            $('#otperror_blk').html("Error: "+data.error+"!");
                            $('#otperror_blk').css('display', 'block');
                        }

                    });
        }
    </script>
</html>
