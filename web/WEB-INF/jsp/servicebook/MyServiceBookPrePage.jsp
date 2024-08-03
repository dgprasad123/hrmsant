<%-- 
    Document   : ServiceBookPrePage
    Created on : Aug 21, 2020, 7:30:59 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <link rel="stylesheet" type="text/css" href="css/PrintDetail.css">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="font-awesome/css/font-awesome.min.css">
        <script src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="css/colorbox.css" rel="stylesheet" type="text/css"> 
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script src="js/jquery.colorbox-min.js"></script>
        <style type="text/css">
            .loader {
                border: 16px solid yellow; /* Light grey */
                border-top: 16px solid blue;
                border-right: 16px solid green;
                border-bottom: 16px solid red;
                border-radius: 50%;
                width: 120px;
                height: 120px;
                animation: spin 2s linear infinite; 
                margin-top: 400px;
                left: 50%;                
            }
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }            
        </style>
        <script type="text/javascript">
            function callNoImage() {
                var userPhoto = document.getElementById('sbUserPhoto');
                userPhoto.src = "images/NoEmployee.png";
            }
            function generateSBLanguage(moduleName, notId, sblangid) {

            }
            function callNoSBPage() {
                var userPhoto = document.getElementById('imgid');
                userPhoto.src = "images/SB1stPage.png";
            }
            function closeFeedBackForm() {
                $("#floatingform").hide();
            }
        </script>
    </head>
    <body id="result">
        <div align="center">
            <div class="loader" align="center"></div>
        </div>        
    </body>
    <script type="text/javascript">
        $("#result").load("servicebook.htm", function () {
            $("#floatingform").hide();
            $("#flox-chat-img").click(function () {
                $("#floatingform").show();
            });
            $('#servicePeriodFrom').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
            $('#servicePeriodTo').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
            $(".iframe").colorbox({iframe: true, width: "80%", height: "80%"});
        });
    </script>
</html>
