<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Change Mobile</title>
        
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script language="JavaScript" type="text/javascript">
            
            $(document).ready(function() {
                $("#newmobile").keypress(function(e) {
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
                var newmobile = $('#newmobile').val();
                if (newmobile == '') {
                    alert("Please enter Mobile Number");
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
        </script>
    </head>
    <body style="padding:0px;">
        <form action="ChangeMobile.htm">
            
            <div align="center" style="width:100%;height:80%;">
                <table id="dg" style="width:90%;height:100%;background:#E5F0C9;">
                    <tr>
                        <td align="center">Enter Mobile Number:</td>
                        <td>
                            <input type="text" name="newmobile" id="newmobile" maxlength="11" placeholder="Input Mobile" required/>
                        </td>
                    </tr>

                    <c:if test="${message == 'DUPLICATE'}">
                        <span style="color:red;font-size:12px;font-family:Verdana;">Entered Mobile Number is Duplicate</span>
                    </c:if>
                    <c:if test="${message == 'ERROR'}">
                        <span style="color:red;font-size:12px;font-family:Verdana;">Error Updating Mobile Number</span>
                    </c:if>
                    <c:if test="${message == 'CHANGED'}">
                        <span style="color:green;font-size:12px;font-family:Verdana;">Mobile Number Changed Successfully</span>
                    </c:if>
                    <tr>
                        <td>&nbsp;</td>
                        <td>
                            <button type="submit" value="Change">Submit</button>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>