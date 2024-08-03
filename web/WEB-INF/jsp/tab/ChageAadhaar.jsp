<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Change Aadhaar</title>

        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script language="JavaScript" type="text/javascript">

            $(document).ready(function() {
                $("#newAadhaar").keypress(function(e) {
                    //if the letter is not digit then display error and don't type anything
                    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                        //display error message
                        $("#errmsg").html("Digits Only").show().fadeOut("slow");
                        return false;
                    }
                });
            });

            function noCTRL(e)
            {
                var code = (document.all) ? event.keyCode : e.which;

                var msg = "Sorry, this functionality is disabled.";
                if (parseInt(code) == 17) //CTRL
                {
                    alert(msg);
                    window.event.returnValue = false;
                }
            }

            function savecheck() {
                var numregex = /^[0-9]+$/;
                var newaadhaar = $('#newAadhaar').val();
                if (newaadhaar == '') {
                    alert("Please enter Aadhaar Number");
                    return false;
                }
            }

            var message = "Right Click Not Allowed.";

            ///////////////////////////////////
            function clickIE4() {
                if (event.button == 2) {
                    alert(message);
                    return false;
                }
            }

            function clickNS4(e) {
                if (document.layers || document.getElementById && !document.all) {
                    if (e.which == 2 || e.which == 3) {
                        alert(message);
                        return false;
                    }
                }
            }

            if (document.layers) {
                document.captureEvents(Event.MOUSEDOWN);
                document.onmousedown = clickNS4;
            } else if (document.all && !document.getElementById) {
                document.onmousedown = clickIE4;
            }

            document.oncontextmenu = new Function("alert(message);return false");
            function encrtyptAadhaar() {
                var val = document.getElementById('newAadhaar').value; //assign password to a variable
                if (val)
                {
                    var rkEncryptionKey = CryptoJS.enc.Base64.parse('u/Gu5posvwDsXUnV5Zaq4g==');
                    var rkEncryptionIv = CryptoJS.enc.Base64.parse('5D9r9ZVzEYYgha93/aUK2w==');
                    var utf8Stringified = CryptoJS.enc.Utf8.parse(val);
                    var encrypted = CryptoJS.AES.encrypt(utf8Stringified.toString(), rkEncryptionKey,
                            {mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7, iv: rkEncryptionIv});
                    document.getElementById('newAadhaar').value = encrypted.ciphertext.toString(CryptoJS.enc.Base64);
                }
            }
            function validateAadhaar()
            {
                var aadhar = document.getElementById("newAadhaar").value;
                var adharcardTwelveDigit = /^\d{12}$/;
                var adharSixteenDigit = /^\d{16}$/;
                if (aadhar != '') {
                    if (aadhar.match(adharcardTwelveDigit)) {
                        encrtyptAadhaar();
                        return true;
                    }
                    else if (aadhar.match(adharSixteenDigit)) {
                        encrtyptAadhaar();
                        return true;
                    }
                    else {
                        alert("Enter valid Aadhar Number");
                        document.getElementById('newAadhaar').select();
                        document.getElementById('newAadhaar').focus();
                        return false;
                    }
                }
                
            }
        </script>
    </head>
    <body style="padding:0px;">
        <form action="ChangeAadhaar.htm" onsubmit="return validateAadhaar()">
            <input type="hidden" name="empId" value="${empId}"/>
            <div align="center" style="width:100%;height:80%;">
                <table id="dg" style="width:90%;height:100%;background:#E5F0C9;">
                    <tr>
                        <td align="center">Enter Aadhaar Number:</td>
                        <td>
                            <input type="text" name="newAadhaar" id="newAadhaar" maxlength="12" placeholder="Input Aadhaar Number" required/>
                        </td>
                    </tr>

                    <c:if test="${message == 'DUPLICATE'}">
                        <span style="color:red;font-size:12px;font-family:Verdana;">Entered Aadhaar Number is Duplicate</span>
                    </c:if>
                    <c:if test="${message == 'ERROR'}">
                        <span style="color:red;font-size:12px;font-family:Verdana;">Error Updating Aadhaar Number</span>
                    </c:if>
                    <c:if test="${message == 'CHANGED'}">
                        <span style="color:green;font-size:12px;font-family:Verdana;">Aadhaar Number Updated Successfully</span>
                    </c:if>
                    <tr>
                        <td>&nbsp;</td>
                        <td>
                            <button type="submit">Submit</button>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>