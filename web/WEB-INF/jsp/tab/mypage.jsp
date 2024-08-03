<%@page contentType="text/html" pageEncoding="UTF-8" import="hrms.common.CommonFunctions"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String completedtasklink = "";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.css">
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link rel="stylesheet" type="text/css" href="css/popupmain.css"/>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script language="javascript" src="js/servicehistory.js" type="text/javascript"></script>
        <style type="text/css">
            body{
                font-family:Verdana,sans-serif;
            }
            .alink{
                color:#fff !important;
                text-decoration: none;
                font-family: Verdana,sans-serif;
                font-size: 12px;                
                background-color: #286090;
                border-color: #204d74;
                white-space: nowrap;
                border: 1px solid transparent;
                border-radius: 4px;
                display: inline-block;
                padding: 6px 12px;

            }

            /* Style the container for inputs */
            .cp_container {
                background-color: #f1f1f1;
                padding: 10px;
            }
            /* Style all input fields */
            .ptbox {
                width: 100%;
                padding: 12px;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
                margin-top: 6px;
                margin-bottom: 16px;
            }

            /* Style the submit button */
            .btn_submit {
                background-color: #4CAF50;
                color: white;
                width:20%;
            }
            /* The message box is shown when the user clicks on the password field */
            #message {
                display:none;
                background: #f1f1f1;
                color: #000;
                position: relative;
                padding: 5px;
                margin-top: 10px;
            }

            #message p {
                padding: 5px 5px;
                font-size: 14px;
            }

            /* Add a green text color and a checkmark when the requirements are right */
            .valid {
                color: green;
            }

            .valid:before {
                position: relative;
                left: -35px;
            }

            /* Add a red text color and an "x" icon when the requirements are wrong */
            .invalid {
                color: red;
            }

            .invalid:before {
                position: relative;
                left: -35px;
            }


        </style>

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <script type="text/javascript" src="js/webcam.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <script language="JavaScript" type="text/javascript">
            function sendMessage() {
                $('#btn1').prop('disabled', true);
                $.ajax({
                    url: 'sendMessageToDc.htm',
                    type: 'post',
                    data: {
                        distCode: $('#distCode').val(),
                        offCode: $('#loginoffcode').val(),
                    },
                    success: function(retVal) {

                        if (retVal == 'Success')
                        {
                            alert("Message sent to Disttrict Co-Ordinator successfully");
                            $('#mask').hide();
                            $('.window').hide();
                        }
                    }
                });
            }
            function checkVisitDate() {
                $('#tab1 .trVisit').show();
                $('#tab1 .trButtons').hide();
            }
            function getGetBack() {
                $('#dialog').hide();
                $('#dialog1').show();
                showStateLevelDialog1();
            }
            function saveVisitDate() {
                if ($('#dateOfVisit').val() != '') {
                    $.ajax({
                        url: 'saveVisitedData.htm',
                        type: 'post',
                        data: {
                            visitedDate: $('#dateOfVisit').val(),
                        },
                        success: function(retVal) {
                            if (retVal == 'Success')
                            {
                                alert("Thank you for your Interest. Please Click ok to go to your HRMS Dashboard.");
                                $('#mask').hide();
                                $('.window').hide();
                            }
                            else
                            {
                                alert("There is some error while processing your request. Please try again later.");
                                $('#mask').hide();
                                $('.window').hide();
                            }
                        }
                    });
                } else {
                    alert("Please enter date of visit");
                    $('#dateOfVisit').focus();
                    return false;
                }

            }

            function getHolidays() {
                $.ajax({
                    url: 'getHolidayList.htm',
                    type: 'get',
                    success: function(retVal) {
                        strHolidays = retVal.toString();
                        arrHolidays = strHolidays.split(',');
                        $('#easyuical').calendar({
                            formatter: function(date) {
                                var d = date.getDate();
                                var opts = $(this).calendar('options');
                                var y = opts.year;
                                var m = opts.month;
                                fd = date.getDay();
                                nw = Math.ceil(d / 7);
                                d1 = d;
                                if (d1 < 10)
                                    d1 = '0' + d1;
                                if (m < 10)
                                    m = '0' + m;
                                for (var i = 0; i < arrHolidays.length; i++)
                                {
                                    arrTemp = arrHolidays[i].split('->');
                                    if ((d1 + '-' + m + '-' + y) == arrTemp[0])
                                    {
                                        if (arrTemp[2] == 'G')
                                            return '<span style="color:#FF0000;font-weight:bold;" title="' + arrTemp[1] + '">' + d + '</span>';
                                        else
                                            return '<span style="color:#008900;font-weight:bold;" title="' + arrTemp[1] + ' (Optional)">' + d + '</span>';
                                    }
                                }
                                if (fd == 6)
                                {
                                    if (nw == 2)
                                        return '<span style="color:#FF0000;font-weight:bold;" title="Second Saturday">' + d + '</span>';
                                    else
                                        return '<span style="color:#000000;">' + d + '</span>';
                                }
                                return d;
                            }
                        });
                    }
                });
            }
            function acceptingAuthsave() {
                var isConfirm = confirm("Are you sure to submit the Remarks?");
                if (isConfirm == true) {
                    $.ajax({
                        type: "POST",
                        url: "AcceptingAuthRemarksSave.htm?parid=" + $('#parid').val() + "&taskid=" + $('#taskid').val() + "&acceptingAuthRemarks=" + $('#sltAcceptingAuthorityRemarks').val(),
                        dataType: "json"
                    }).done(function(serverResponse) {
                        $.messager.alert(serverResponse.msgType, serverResponse.msg);
                        if (serverResponse.msg == "Accepted") {
                            $('#dg').datagrid('reload');
                        }
                    });
                } else {

                }
            }

            function detailsCombo() {
                openWindow('taskDetailView.htm?taskId=' + $('#taskid').val() + '&auth=&csrfPreventionSalt=' + $("#csrfPreventionSalt").val());
            }


            function openWindow(linkurl) {
                $("#win").window("open");
                $("#win").window("setTitle", "Task List");
                $("#winfram").attr("src", linkurl);

            }
            function closeWindow() {
                $("#win").window("close");
            }


            function callNoImage() {

                var userPhoto = document.getElementById('loginUserPhoto');
                userPhoto.src = "images/NoEmployee.png";

            }
            function UploadImage() {
                var url = 'fileUploadForm.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false, onClosed: refreshImage});
            }

            function refreshImage() {
                $("#loginUserPhoto").attr('src', 'profilephoto.htm?' + 'date=' + (new Date()).getTime());
            }

            function changepassword() {
                $('#changpwddlg').dialog('open');
            }
            function saveChangePassword() {
                encrtyptPassword();
                $.post('ChangePasswordAction.htm', $('#changpwdfm').serialize()).done(function(data) {
                    $("#userPassword").val('');
                    $("#newpassword").val('');
                    $("#confirmpassword").val('');
                    $('#msgspan').text(data.msg);
                    $('#changpwddlg').dialog({
                        closable: true
                    });
                });
            }
            function randomString(length) {
                var text = "";
                var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                for (var i = 0; i < length; i++) {
                    text += possible.charAt(Math.floor(Math.random() * possible.length));
                }
                return text;
            }
            var randomText = randomString(32);
            $("#randomTextValue").val(randomText);
            function encrtyptPassword() {
                var userPassword = $("#userPassword").val();
                var randomValue = randomString(32);
                var randmPassword = randomValue + userPassword;
                $("#userPassword").val(randmPassword);

                var newpassword = $("#newpassword").val();
                randomValue = randomString(32);
                randmPassword = randomValue + newpassword;
                $("#newpassword").val(randmPassword);

                var confirmpassword = $("#confirmpassword").val();
                randmPassword = randomValue + confirmpassword;
                $("#confirmpassword").val(randmPassword);
            }
            function changemobile() {

                var url = 'ChangeMobile.htm?newmobile=';

                $.colorbox({href: url, iframe: true, open: true, width: "50%", height: "30%"});
            }
            function changeAadhaar() {
                var url = 'ChangeAadhaar.htm?empId=' + $('#empid').val() + '&newAadhaar=';
                $.colorbox({href: url, iframe: true, open: true, width: "50%", height: "30%"});
            }
            function addTab(title, url) {
                if ($('#tt').tabs('exists', title)) {
                    $('#tt').tabs('select', title);
                } else {
                    var content = '<iframe scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:100%;"><\/iframe>';
                    $('#tt').tabs('add', {
                        title: title,
                        content: content,
                        closable: true
                    });
                }
            }
            function doSearch() {

                var parval = $('#parstatus').combobox('getValue');
                var parvalText = $('#parstatus').combobox('getText');
                var process = $('#sltProcess').combobox('getValue');
                var processText = $('#sltProcess').combobox('getText');
                //var empname = $('#txtEmpname').val();
                var empname = $("#txtEmpname").textbox('getValue');
                //var gpfno = $('#txtGPFNo').val();

                var gpfno = $("#txtGPFNo").textbox('getValue');

                if (parval == parvalText) {
                    alert("Invalid Status Selected");
                    return false;
                }
                if (process == processText) {
                    alert("Invalid Task Selected");
                    return false;
                }
                if (parval == "9") {
                    $('#viewcompleted').show();
                }

                if (empname != '') {
                    //process = "";
                    //parval = "";
                }
                if (gpfno != '') {
                    empname = "";
                    //process = "";
                    //parval = "";
                }

                $('#dg').datagrid('load', {
                    parstatus: parval,
                    processId: process,
                    empName: empname,
                    gpf: gpfno,
                    gpfno: gpfno

                });
            }
            //new code added
            $(document).ready(function() {



                //if close button is clicked
                $('.window .close').click(function(e) {
                    //Cancel the link behavior
                    e.preventDefault();

                    $('#mask').hide();
                    $('.window').hide();
                });

                //if mask is clicked
                $('#mask').click(function() {
                    $(this).hide();
                    $('.window').hide();

                });
                var noofmessage = $("#noofMessage").val();

                $.ajax({
                    url: 'checkLastNoOfDays.htm',
                    type: 'post',
                    data: {
                        loginempid: $('#loginempid').val()
                    },
                    success: function(retVal) {

                        if ((retVal == 'Success' && $('#loginAsDDO').val() == 'Y') && ($('#ifVisited').val() != 'Y' || $('#hasAERAuthorization').val() == 'Y' || $('#isAddlCharge').val() == 'Y'))
                        {

                            $('#dialog').show();
                            showStateLevelDialog();
                        } else if ((retVal == 'Success' && $('#isAddlCharge').val() == 'Y') && ($('#ifVisited').val() != 'Y' || $('#hasAERAuthorization').val() == 'Y' || $('#isAddlCharge').val() == 'Y')) {
                            $('#dialog').show();
                            showStateLevelDialog();
                        } else {
                            //  showStateLevelDialog();

                        }
                    }
                });
                $('#tab1 .trVisit').hide();
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false,
                    scrollMonth: false,
                    scrollInput: false
                });
                if ($("#forcereset").val() == "1") {
                    $('#changpwddlg').dialog({
                        closable: false
                    });
                    changepassword();
                }
                $("#capturesnap").hide();
                $('#viewcompleted').hide();
                getHolidays();
                $('#win').window({
                    onBeforeClose: function() {
                        $('#dg').datagrid('reload');

                    }
                })

                var parval = $('#parstatus').combobox('getValue');
                var process = $('#sltProcess').combobox('getValue');
                var empname = $('#txtEmpname').textbox('getValue');
                var gpfno = $('#txtGPFNo').textbox('getValue');

                $('#dg').datagrid({
                    url: "taskAction.htm?parstatus=" + parval + "&processId=" + process + "&empName=" + empname + "&gpf=" + gpfno
                            //url: "taskAction.htm"
                });

                $('#parstatus').combobox({
                    onChange: function(rec) {
                        if (rec.value != 9) {
                            $('#viewcompleted').hide();
                        }
                    }
                });
//                if ($('#loginAsDDO').val() == 'Y' || $('#ifVisited').val() != 'Y' || users.hasAERAuthorization == 'Y')
//                {
//                    $('#dialog').show();
//                    showStateLevelDialog();
//                }
//                else
//                {
//                    //  showStateLevelDialog();
//                }
                if (noofmessage > 0) {
                    showStateLevelDialog();
                }

            });
            function showStateLevelDialog1()
            {

                var id = '#dialog1';

                //Get the screen height and width
                var maskHeight = $(document).height();
                var maskWidth = $(window).width();

                //Set heigth and width to mask to fill up the whole screen
                $('#mask').css({'width': maskWidth, 'height': maskHeight});

                //transition effect		
                $('#mask').fadeIn(500);
                $('#mask').fadeTo("slow", 0.9);

                //Get the window height and width
                var winH = $(window).height() - 100;
                var winW = $(window).width();

                //Set the popup window to center
                $(id).css('top', winH / 2 - $(id).height() / 2);
                $(id).css('left', winW / 2 - $(id).width() / 2);

                if ($('#hasFilled').val() == "true")
                {
                    $('#mask').hide();
                    $('.window').hide();

                }
                else
                {
                    //transition effect
                    $(id).fadeIn(2000);
                }
            }
            function showStateLevelDialog()
            {

                var id = '#dialog';

                //Get the screen height and width
                var maskHeight = $(document).height();
                var maskWidth = $(window).width();

                //Set heigth and width to mask to fill up the whole screen
                $('#mask').css({'width': maskWidth, 'height': maskHeight});

                //transition effect		
                $('#mask').fadeIn(500);
                $('#mask').fadeTo("slow", 0.9);

                //Get the window height and width
                var winH = $(window).height() - 100;
                var winW = $(window).width();

                //Set the popup window to center
                $(id).css('top', winH / 2 - $(id).height() / 2);
                $(id).css('left', winW / 2 - $(id).width() / 2);

                if ($('#hasFilled').val() == "true")
                {
                    $('#mask').hide();
                    $('.window').hide();

                }
                else
                {
                    //transition effect
                    $(id).fadeIn(2000);
                }
            }
            function validateExpertise()
            {
                if (objE.areaOfExpertise.value == '')
                {
                    alert("Please enter Area of Expertise.");
                    objE.areaOfExpertise.focus();
                    return false;
                }
                if (objE.areaOfInterest.value == '')
                {
                    alert("Please enter Area of Interest.");
                    objE.areaOfInterest.focus();
                    return false;
                }
                if (objE.volWillingness.selectedIndex == 0)
                {
                    alert("Please select where you are willing to serve as State Level Observer in addition to my normal official duties? Yes/No");
                    objE.volWillingness.focus();
                    return false;
                }
                var postData = $('#frmExpertise').serialize();
                $.ajax({
                    url: 'saveExpertise.htm',
                    type: 'post',
                    data: postData,
                    success: function(retVal) {
                        if (retVal == 'Success')
                        {
                            alert("Thank you for your Interest. Please Click ok to go to your HRMS Dashboard.");
                            $('#mask').hide();
                            $('.window').hide();
                        }
                        else
                        {
                            alert("There is some error while processing your request. Please try again later.");
                            $('#mask').hide();
                            $('.window').hide();
                        }
                    }
                });
            }
            function ViewMessage() {
                $('#mask').hide();
                $('.window').hide();
                //   window.location="viewCommunicationDetails.htm";
            }
        </script>
        <style type="text/css">
            .icon-calendar {
                background: rgba(0, 0, 0, 0) url("images/icon_calendar.png") no-repeat scroll center center;
            }
            .icon-appl {
                background: rgba(0, 0, 0, 0) url("images/application.png") no-repeat scroll center center;
            }
        </style>
    </head>

    <body style="padding:0px;">
        <input type="hidden" id="forcereset" value="${forcereset}"/>
        <!-- Employees Expertise Popup-->  

        <div id="boxes">
            <div style=" left: 551.5px;  top: 30%;width:1200px; display: none;<c:if test="${forcereset eq '1'}">z-index:1</c:if>" id="dialog1" class="window"> 
                    <table id="tab1"  class="table-bordered" align='center'>
                        <tr>
                            <td align="center" colspan="3"><h2 class="alert alert-info" style="color:#FF0000;">CMGI will get back to you soon. </h2></td>
                        </tr>
                        <tr>
                            <td align="center" class="trButtons">
                                <input type="submit" id="btn1" value="Ok" name="btn" class="btn btn-danger" onclick="return sendMessage()"/>

                            </td>
                        </tr>
                    </table>
                </div>

                <div style=" left: 551.5px;  top: 30%;width:1200px; display: none;<c:if test="${forcereset eq '1'}">z-index:1</c:if>" id="dialog" class="window"> 
                <input type='hidden' name='noofmessage' id='noofMessage' value="${totalnotview}"/>
                <input type="hidden" id="ifProfileCompleted" value="${ifProfileCompleted}" />
                <input type="hidden" id="isAddlCharge" value="${isAddlCharge}" />
                <input type="hidden" id="loginAsDDO" value="${LoginUserBean.loginAsDDO}" />
                <input type="hidden" name="loginoffcode" id="loginoffcode" value="${LoginUserBean.loginoffcode}" />
                <input type="hidden" name="loginempid" id="loginempid" value="${LoginUserBean.loginempid}" />
                <input type="hidden" id="ifVisited" name="ifVisited" value="${LoginUserBean.ifVisited}" />
                <input type="hidden" id="distCode" name="distCode" value="${distCode}" />
                <input type="hidden" id="cadreCode" value="${trainingCadre}" />
                <input type="hidden" id="hasAERAuthorization" value="${users.hasAERAuthorization}" />

                <!-- <h1 style="font-size:18pt;color:#0A62AA;font-weight:bold;">Enrollment as Sate Level Observer</h1>
                 <form name="frmExpertise" id="frmExpertise" method="post" action="saveExtertise.htm">
                     <input type="hidden" name="hasFilled" id="hasFilled" value="${hasFilled}" />
 
                     <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="font-size:9pt;">
                         <tr bgcolor="#FFFFFF">
                             <td width="150">
                                 1.Name of the Officer:
                             </td>
                             <td>
                <c:out value="${userinfo.name}"/>
                <input type="hidden" name="name" value="${userinfo.name}" />
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                2.Designation:
            </td>
            <td>
                <c:out value="${userinfo.designation}"/>
                <input type="hidden" name="designation" value="${userinfo.designation}" />
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                3.Grade:
            </td>
            <td>
                <c:if test="${not empty userinfo.grade}"><c:out value="${userinfo.grade}"/>
                    <input type="hidden" name="grade" value="${userinfo.grade}" />
                </c:if>
                <c:if test="${empty userinfo.grade}"><input type="text" class="tb10" name="grade" />
                </c:if>
            </td> 
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                4.Parent Department:
            </td>
            <td>
                <c:out value="${userinfo.deptname}"/>
                <input type="hidden" name="deptname" value="${userinfo.deptname}" />
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                5.Present Place of Posting:
            </td>

            <td>
                <c:out value="${userinfo.postingPlace}"/>
                <input type="hidden" name="postingPlace" value="${userinfo.postingPlace}" />
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                6.Details of Office in which presently stationed:
            </td>
            <td>
                <c:out value="${userinfo.curofficename}"/>
                <input type="hidden" name="curofficename" value="${userinfo.curofficename}" />
            </td>              

        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                7.Area of Expertise:
            </td>
            <td>
                <textarea rows="3" cols="50" style="width:100%;" class="tb10" name="areaOfExpertise"></textarea>
            </td>              

        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                8.Area of Interest:
            </td>
            <td>
                <textarea rows="3" cols="50" style="width:100%;" name="areaOfInterest" class="tb10"></textarea>
            </td>              

        </tr> 
        <tr bgcolor="#FFFFFF">
            <td colspan="2">
                9.Voluntary willingness to serve as State Level Observer in addition to my normal official duties.
                <select size="1" class="tb10" style="width:100px;" name="volWillingness">
                    <option value="">-Select-</option>
                    <option value="Yes">Yes</option>
                    <option value="No">No</option>
                </select>
            </td>
        </tr>                     
        <tr bgcolor="#FFFFFF">
            <td>
                10.Mobile Number:
            </td>
            <td>


                <c:if test="${not empty userinfo.mobile}"><c:out value="${userinfo.mobile}"/>
                </c:if>
                <c:if test="${empty userinfo.mobile}"><input type="text" class="tb10" name="mobile" />
                </c:if>                             
            </td>              
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                11.Landline Number:
            </td>
            <td>
                <input type="text" name="landline" class="tb10" />
            </td>              
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                12.Office Phone Number:
            </td>
            <td>
                <input type="text" name="officePhone" class="tb10" />
            </td>              
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>
                13.Email Address:
            </td>
            <td>
                <c:if test="${not empty userinfo.emailid}"><c:out value="${userinfo.emailid}"/>
                    <input type="hidden" name="emailid" value="${userinfo.emailid}" />
                </c:if>
                <c:if test="${empty userinfo.emailid}"><input type="text" class="tb10" name="emailid" />
                </c:if>                            
            </td>              
        </tr>                    
    </table>
    
</form>-->


                <table id="tab1"  class="table-bordered" align='center'>

                    <c:if test="${LoginUserBean.loginAsDDO eq 'Y' || isAddlCharge eq 'Y' && LoginUserBean.ifVisited ne 'Y' }">
                        <tr>
                            <td align="center" colspan="3"><h2 class="alert alert-info" style="color:#FF0000;">Has the District Co-ordinator, CMGI of your district visited your office for certain awareness on HRMS etc. within last 60 days? </h2></td>
                        </tr>
                        <tr height="40px" class="trVisit">
                            <td align="right" style="font-size:20px;"  width="40%">
                                <b>Date of Visit&nbsp;&nbsp;&nbsp;&nbsp;</b><span style="color: red">*</span>&nbsp;&nbsp;
                            </td>
                            <td id="innerdata"  width="20%">  
                                <input id="dateOfVisit" name="dateOfVisit"  readonly="true"  style="width:200px;height:40px;" class="txtDate"  />
                            </td>
                            <td id="innerdata"  width="40%">  
                                <input type="submit" value="Submit" name="btn" class="btn btn-danger" onclick="return saveVisitDate()"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" class="trButtons" colspan="3">
                                <input type="submit" value="Yes" name="btn" class="btn btn-danger" onclick="return checkVisitDate()"/>
                                <input type="submit" value="No" name="btn" class="btn btn-danger" onclick="return getGetBack()"/>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${totalnotview > 0}">
                        <tr>
                            <td><h1 style="font-size:18pt;color:#0A62AA;font-weight:bold;">Total No of Communications</h1></td>
                        </tr>
                        <tr>
                            <td style='color:green'>
                                <h2 class="alert alert-info">Total No of Communications: ${totalCnt}</h2> 
                            </td>
                        </tr>
                        <tr>
                            <td style='color:blue'>
                                <h2 class="alert alert-success">Viewed Communications: ${totalview}</h2> 
                            </td>
                        </tr>
                        <tr>
                            <td style='color:blueviolet'>
                                <h2 class="alert alert-danger">Waiting For View Communications: ${totalnotview}</h2> 
                            </td>
                        </tr>
                    </c:if>

                </table>   

                <div id="popupfoot"> 
                    <c:if test="${totalnotview > 0}"><a href="javascript:ViewMessage();"   class="btn pri" data-options="plain:true,iconCls:'icon-leave'" onclick="addTab('My Communication', 'viewCommunicationDetails.htm')" >View message</a></c:if>


                    </div>            
                    <script type="text/javascript">
                        var objE = document.frmExpertise;
                    </script>
                </div>


                <div style="width: 1478px; font-size: 32pt; color:white; height: 602px; display: none; opacity: 0.8;" id="mask"></div>
            </div>
            <!-- Employees Expertise Popup Ends-->
            <form id= "myform" action="login.htm">
            <jsp:include page="topbanner.jsp"/>

            <input type="hidden" name="empId" value="${empId}" id="empid"/>
            <input type="hidden" name="curip" value="${curip}" id="curip" />

            <div style="padding:10px;">
                <div id="cc_layout" class="easyui-layout" style="width:100%;height:730px;">
                    <div data-options="region:'center',split:true" title="Dash Board">
                        <div id="tt" class="easyui-tabs" border="false" plain="true" fit="true" style="width:100%;height:680px;">
                            <div title="Home">
                                <div style="padding:5px;"></div>
                                <div id="tb" style="padding:3px;text-align:left;background-color: #f4f4f4">
                                    <label style="margin-left:10px;">Select Task </label>
                                    <input class="easyui-combobox form-control" id="sltProcess" name="sltProcess" style="width:15%" data-options="valueField:'value',textField:'label',url:'GetWorkflowProcessJSONData.htm'">
                                    <label style="margin-left:10px;"> Select Status </label>
                                    <input id="parstatus" name="parstatus" class="easyui-combobox form-control" style="width:15%" data-options="
                                           valueField: 'value',
                                           textField: 'label',
                                           data: [{
                                           label: 'All',
                                           value: ''
                                           },{
                                           label: 'PENDING AS REPORTING AUTHORITY',
                                           value: '6'
                                           },{
                                           label: 'PENDING AS REVIEWING AUTHORITY',
                                           value: '7'
                                           },{
                                           label: 'PENDING AS ACCEPTING AUTHORITY',
                                           value: '8'
                                           },{
                                           label: 'MY COMPLETED TASKS',
                                           value: '9'
                                           }]" />
                                    <label style="margin-left:10px;">Employee Name</label>
                                    <input type="text" id="txtEmpname" name="txtEmpname" class="easyui-textbox form-control" size="30" style="width:15%"/>
                                    <label style="margin-left:10px;">GPF No</label>
                                    <input type="text" id="txtGPFNo" name="txtGPFNo" class="easyui-textbox form-control" style="width:15%" />
                                    <a href="javascript:void(0)" class=" btn btn-success" onclick="doSearch()" plain="true" style="height:33px;width:40px;">Search</a>
                                    <%
                                        completedtasklink = "CompletedTask.htm";
                                    %>
                                    <div style="text-align:center;"><a href='<%=completedtasklink%>' id="viewcompleted" class="easyui-linkbutton  btn-info" target="_blank" style="background:rgba(16, 145, 127, 0.97);font-weight: bold;color:white">Print Report</a></div>
                                </div>
                                <div>
                                    <span style="font-size: 14px;font-weight: bold;color: #FF4500;">
                                        Total Pending PAR - ${totalPAR}, PENDING AS REPORTING - ${totalPAR1} , PENDING AS REVIEWING - ${totalPAR2}, PENDING AS ACCEPTING - ${totalPAR3} 
                                    </span>
                                </div>

                                <table id="dg" class="easyui-datagrid" style="width:100%;height:360px;" title="My Task"
                                       rownumbers="true" pagination="true" singleSelect="true"
                                       data-options="singleSelect:true,collapsible:true,fitColumns:true" toolbar="#tb">
                                    <thead>
                                        <tr>
                                            <th data-options="field:'taskId'">Task ID</th>
                                            <th data-options="field:'processname'">Task name</th>
                                            <th data-options="field:'applicant'">Initiated By</th>
                                            <th data-options="field:'dateOfInitiationAsString'">Initiated On</th>
                                            <th data-options="field:'status'">Status</th>
                                            <th data-options="field:'istaskcompleted',align:'center',formatter:quickView">Action</th>
                                            <th data-options="field:'auth',hidden:'true'">Authority Type</th>
                                            <th data-options="field:'statusId',hidden:'true'">Status Id</th>
                                        </tr> 
                                    </thead>
                                </table>
                                <script type="text/javascript">
                                    $(function() {
                                        var pager = $('#dg').datagrid('getPager'); // get the pager of datagrid
                                        pager.pagination({
                                            buttons: [{
                                                    iconCls: 'icon-edit',
                                                    handler: function() {
                                                        var row = $('#dg').datagrid('getSelected');

                                                        if (row == null) {
                                                            alert('Please Select row from Task List. ');
                                                            return false;
                                                        } else {
                                                            var auth1 = row.auth;
                                                            var statusid = row.statusId;
                                                            //openWindow('JSP/TaskAction.do?taskId='+row.taskId+'&submit=View&auth='+auth1);
                                                            if (statusid != '21') {
                                                                openWindow('taskDetailView.htm?processid=' + row.processId + '&taskId=' + row.taskId + '&auth=' + auth1 + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val());
                                                            } else if (statusid == '21') {
                                                                if ((auth1 != "REPORTING") && (auth1 != "REVIEWING") && (auth1 != "ACCEPTING")) {
                                                                    openWindow('JSP/ParApplyDispAction.do?submit=AdverseReport&taskid=' + row.taskId);
                                                                } else {
                                                                    openWindow('JSP/TaskAction.do?taskId=' + row.taskId + '&submit=View&auth=' + auth1);
                                                                }
                                                            }
                                                        }
                                                    }

                                                }]
                                        });
                                        $('#dg').datagrid({
                                            view: detailview,
                                            detailFormatter: function(index, row) {
                                                return '<div class="ddv" style="padding:5px 0;background:#E5F0C9;"><\/div>';
                                            },
                                            onExpandRow: function(index, row) {
                                                //alert("Status ID is:" +row.statusId);
                                                if (row.statusId == 9) {
                                                    alert("This facility is unavailable.");
                                                    return false;
                                                } else if (row.statusId != 8) {
                                                    alert("This facility is available for Accepting Authority only.");
                                                    return false;
                                                } else {
                                                    var ddv = $(this).datagrid('getRowDetail', index).find('div.ddv');
                                                    ddv.panel({
                                                        height: 60,
                                                        border: false,
                                                        cache: false,
                                                        href: 'AcceptingAuthRemarksPage.htm?taskid=' + row.taskId,
                                                        onLoad: function() {
                                                            $('#dg').datagrid('fixDetailRowHeight', index);
                                                        }
                                                    });
                                                    $('#dg').datagrid('fixDetailRowHeight', index);
                                                }
                                            }
                                        });
                                    })
                                    function quickView(val, row) {
                                        var auth1 = row.auth;
                                        var statusid = row.statusId;
                                        var url = "";
                                        if (statusid != '21') {
                                            url = 'taskDetailView.htm?taskId=' + row.taskId + '&auth=' + auth1 + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();
                                        } else if (statusid == '21') {
                                            if ((auth1 != "REPORTING") && (auth1 != "REVIEWING") && (auth1 != "ACCEPTING")) {
                                                url = 'JSP/ParApplyDispAction.do?submit=AdverseReport&taskid=' + row.taskId;
                                            } else {
                                                url = 'taskRedirectAction.htm?taskId=' + row.taskId + '&auth=' + auth1;
                                            }
                                        }
                                        //var url ='JSP/TaskAction.do?taskId='+row.taskId+'&submit=View&auth='+auth1;
                                        return "<a href='javascript:void(0)' onclick='openWindow(\"" + url + "\")'><img src='images/action.png' width='16' height='16'></a>"
                                    }
                                </script>

                                <div id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window'" closed="true" style="width:100%;height:100%;padding:5px;">
                                    <iframe id="winfram" frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%"></iframe>
                                </div>



                                <div class="easyui-layout" data-options="fit:true,split:true">
                                    <div data-options="region:'center',split:true,border:true">
                                        <div class="easyui-layout" data-options="fit:true,split:true">
                                            <!--
                                            <div title="Calendar" data-options="region:'west',split:true,border:true,collapsible:false" style="width:258">
                                                <div id="easyuical" class="easyui-calendar" style="width:250px;height:250px;"></div>
                                            </div>
                                            -->
                                            <div title="My Reports" data-options="region:'west',split:true,border:true" style="width:350">
                                                <c:if test="${users.usertype=='G'}">
                                                    <div style="margin-bottom:10px">
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('My Communication', 'viewCommunicationDetails.htm?')">My Communication (${totalnotview}/${totalCnt})</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('My Profile', 'firstpagesb.htm')">My Profile</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('PAYSLIP BROWSER', 'PaySlipList.htm')">My Pay Slip</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('View Loan Account', 'employeeloanaccount.htm')">My Loan Account</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Bank details change history', 'showbankaccountList2Employee.htm')">Bank details change history<img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></a>

                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Annual Loan Balance Slip', 'getEmployeeLTAdvanceList.htm')">Annual LTA Balance Slip</a>
                                                        <!--  <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                  addTab('Service Book', 'servicebook.htm')">My Service Book</a>-->
                                                        <c:if test="${LoginUserBean.loginEmployeeType eq 'Y' || LoginUserBean.loginEmployeeType eq 'C' || LoginUserBean.loginEmployeeType eq 'W' || LoginUserBean.loginEmployeeType eq 'G'}">
                                                            <a href="myserviceBookPrePage.htm" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" target="_blank">My Service Book</a>
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                    addTab('Service Book', 'MyServicebookPDF.htm')">My Service Book(PDF)</a>
                                                        </c:if>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Leave Account', 'myleaveAccountInputPage.htm')">My Leave Account</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('My Quarter Details', 'searchQuarterDetails.htm')">My Quarter Details</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('GIS Passbook', 'getGisPassBookList.htm?')">My GIS Passbook</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('My Annual IT Report', 'EmployeeITReport.htm?')">My Annual IT Report</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('My Calendar', 'myCalendar.htm?')">My Calendar</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('My Quarter Details', 'QuarterDetailsList.htm?')">My Quarter Details</a>
                                                        <a href='tabController.htm?rollId=<%=CommonFunctions.encodedTxt("11")%>' style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('', '')">Dashboard</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('My Drawal Particulars', 'salaryProjectionView.htm?')">My Drawal Particulars</a>



                                                    </div>
                                                </c:if>
                                            </div>
                                            <div title="User Privilege" data-options="region:'center',split:true,border:true" style="width:200">
                                                <input type="hidden" name="csrfPreventionSalt" id="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
                                                <c:if test="${users.usertype=='G'}">

                                                    <c:if test="${users.hasServiceBookValidateAuthorization=='Y'}">   
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Validate e-Service Book', 'ServiceBookValidatorPage.htm')">Validate e-Service Book</a>
                                                    </c:if>
                                                    <c:if test="${users.hasmyDistTab=='Y'}">   
                                                        <a href='tabController.htm?rollId=<%=CommonFunctions.encodedTxt("03")%>' class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-district'"> My District Interface </a>
                                                    </c:if>
                                                    <c:if test="${users.hasmyHodTab=='Y'}">   
                                                        <a href='tabController.htm?rollId=<%=CommonFunctions.encodedTxt("04")%>' class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-hod'"> My Hod Interface </a>
                                                    </c:if>

                                                    <c:if test="${users.hasCommandandAuthPriv=='Y'}">  
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cadre'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Grievance', 'adminGrievanceList.htm')">Grievance List</a>
                                                    </c:if>    

                                                    <c:if test="${users.hasPayRevisionAuth=='Y'}">   
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Third Schedule', 'ThirdScheduleEmpList.htm')">Third Schedule List</a>
                                                    </c:if>
                                                    <c:if test="${users.hascheckingAuth=='Y'}">   
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Third Schedule Checking Auth', 'ThirdScheduleCheckingAuthEmpList.htm')">Third Schedule Checking Auth List</a>

                                                    </c:if>
                                                    <c:if test="${users.hasverifyingAuth=='Y'}">   
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Third Schedule Verifying Auth', 'ThirdScheduleVerifyingAuthEmpList.htm')">Third Schedule Verifying Auth List</a>

                                                    </c:if>
                                                    <c:if test="${users.hasProfileAuthorization=='Y'}">   
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Verify Employee Data', 'showProfileReportController.htm')">Verify Employee Data</a>

                                                    </c:if>
                                                    <c:if test="${users.hasAERAuthorization=='Y'}">   
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Annual Establishment Review Report', 'displayAERlist.htm')">Annual Establishment Review</a>
                                                        <!-- <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                 addTab('Post Sanctioned Strength For Annual Establishment Review Report', 'GenericPostWiseStrength.htm')">Post Sanctioned Strength for Annual Establishment Review</a>-->

                                                    </c:if> 
                                                    <c:if test="${users.hasAerForReviewerAuthorization=='Y'}">   
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Annual Establishment Review Report ( AS CO )', 'displayAERlistForControllingAuthority.htm?btnAer=Search')">Annual Establishment Review ( AS CO )</a>
                                                        <!--  <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                  addTab('Post Sanctioned Strength For Annual Establishment Review Report ( AS CO )', 'GenericPostWiseStrengthForCO.htm')">Post Sanctioned Strength for Annual Establishment Review ( AS CO )</a>-->
                                                    </c:if>
                                                    <c:if test="${users.hasPostTerminationForVerifier=='Y'}">
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Post Termination Proposal(As CO)', 'PostTerminationCOScheduleII.htm?btnPTAer=GetList')">Post Termination Proposal(As CO)</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Post Termination List (By DDO)', 'COViewPostTerminationList.htm')">Post Termination List (By DDO)</a>
                                                    </c:if>
                                                    <c:if test="${users.hasAerForAcceptorAuthorization=='Y'}">   
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Annual Establishment Review Report ( AS AO )', 'displayAERlistForAdministrativeAuthority.htm?btnAer=Search')">Annual Establishment Review ( AS AO )</a>
                                                    </c:if>
                                                    <c:if test="${users.hasPostTerminationForAcceptor=='Y'}">
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Post Termination Proposal(As AO)', 'listingTerminationPostSchedule3.htm')">Post Termination Proposal(As AO)</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Post Termination List(By CO)', 'AOViewPostTerminationList.htm')">Post Termination List(By CO)</a>
                                                    </c:if>
                                                    <c:if test="${users.haspoliceDGTab=='Y'}">   
                                                        <a href='JSP/PoliceDG.do?submit=View' class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-office'" target="_blank"> Police DG Interface </a>
                                                    </c:if>
                                                    <c:if test="${users.hasparviewingAuthorization=='Y'}"> 
                                                        <a href='javascript:void(0)'  onclick="addTab('ParViewer', 'parViewer.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Nodal Officer PAR -Inspr. & above</a>
                                                    </c:if>
                                                    <c:if test="${users.isNodalOfficerSiPar=='Y'}"> 
                                                        <a href='javascript:void(0)'  onclick="addTab('SiParViewer', 'SiParViewer.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-office'" style="font-size: 14px;font-weight: bold;color: #FF4500;"> Nodal Officer for SI & Equivalent PAR</a>
                                                    </c:if>
                                                    <c:if test="${users.haspropertyadminAuthorization=='Y'}"> 
                                                        <a href='javascript:void(0)'  onclick="addTab('PropertyStatementAdmin', 'propertyStatementadmin.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Property Statement Admin </a>
                                                    </c:if>

                                                    <c:if test="${users.hasparcustodianAuthorization=='Y'}"> 
                                                        <a href='javascript:void(0)'  onclick="addTab('Par Custdian', 'parCustdian.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Par Custodian</a>
                                                        <a href='javascript:void(0)'  onclick="addTab('PAR Statement', 'dpcStatementDetailReport.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Prepare PAR Statement(DPC)</a>
                                                        <a href='javascript:void(0)'  onclick="addTab('Cadre NOC List', 'cadreNocList.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'">  NOC from Vigilance and Crime Branch List</a><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" />

                                                    </c:if>
                                                    <c:if test="${users.hasparreviewingAuthorization=='Y'}">  
                                                        <a href='javascript:void(0)'  onclick="addTab('Par Reviewing', 'reviewLogin.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Par Reviewing</a>
                                                    </c:if>

                                                    <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-dp'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('PROCEEDINGS', 'DiscProcedingList.htm')">Disciplinary Proceedings</a>

                                                    <c:if test="${users.hasmyOfficeTab=='Y'}"> 
                                                        <a href='tabController.htm?rollId=<%=CommonFunctions.encodedTxt("05")%>' class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-office'"> My Office Interface </a>
                                                        <!-- <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold; color:#008900"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-dp'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                 addTab('VIRTUAL TRAINING', 'DiscProcedingList.htm')" >REGISTER FOR VIRTUAL TRAINING</a>-->

                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Office Wise Second Schedule List', 'GetOfficeWiseSecondScheduleEmployeeList.htm')">Office Wise Second Schedule List</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Third Schedule List for Contractual 6Yrs To Regular Employee', 'ThirdScheduleContractual6YrsToRegular.htm')">Third Schedule List for Contractual 6 Years To Regular Employee</a>                                                        
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cadre'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Post Proposal', 'getProposalList.htm')">Post Proposal</a>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Leave Status', 'leavestatus.htm')">Leave Status</a><br/>

                                                    </c:if>
                                                    <c:if test="${users.hasmyCadreTab=='Y'}">
                                                        <a href='tabController.htm?rollId=<%=CommonFunctions.encodedTxt("01")%>' class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-office'"> My Cadre Interface </a>
                                                    </c:if>
                                                    <a href='http://orissalms.in/SingleSignOn.htm?hrmsId=${users.hrmsEncId}' class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-office'" target="_blank"> My Court Cases</a>
                                                    <br><!--<a href='javascript:void(0)'  onclick="addTab('My Training Applications', 'MyTrainingApplication.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> My Training Applications</a><br>-->
                                                    <a href='javascript:void(0)'  onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('Manage Training', 'ManageTraining.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Manage Training</a>
                                                    <a href='javascript:void(0)'  onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('Sanctioned Loan List', 'LoanAuthoritySanctionList.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'">Sanctioned Loan List</a>




                                                </c:if>
                                                <c:if test="${LoginUserBean.loginAsDDO=='Y' || displaySBTrack == true}">
                                                    <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('Track Completed Service Book.htm', 'TrackCompletedSB.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Track Completed Service Book</a><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /><br/>
                                                </c:if> 
                                                <c:if test="${LoginUserBean.loginAsDDO eq'Y' && LoginUserBean.loginOffLevel eq '01'}">    
                                                    <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('Nomination (By Authority)', 'viewRecommendationListByDepartment.htm')">Nomination List</a> <img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" />
                                                    <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('Premature Retirement...', 'nominationfinal.htm')">Premature Retirement, Out of turn promotion (within the batch and across the batch) <br/> Incentives in the HRMS Portal</a> <br/>
                                                    <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('DPC (By Authority)', 'DPCConductedList.htm')">DPCs</a>



                                                </c:if>     
                                                <c:if test="${LoginUserBean.loginAsDDO=='Y' || isAddlCharge=='Y'}">    
                                                    <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('Nomination (By Authority)', 'recommendationList.htm')">Out of turn Promotion/Incentive/Premature Retirement</a> <img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" />
                                                    <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('Property Statement Report', 'propertyStatementListForDDO.htm')">Property Statement Report</a> <img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" />

                                                </c:if>


                                                <c:if test="${LoginUserBean.loginAsDDO=='Y' || isAddlCharge=='Y'}">    
                                                    <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                            addTab('NOC From Vigilance & Crime branch', 'pensionerNocList.htm')">NOC From Vigilance & Crime branch</a><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /><br/>
                                                </c:if>

                                                <c:if test="${LoginUserBean.loginusertype ne 'M' && users.hasGroupCcustodianAuthorization=='Y'}">
                                                    <a href='javascript:void(0)'  onclick="addTab('Group C Custdian', 'groupCCustdianReport.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Group C Custodian</a>
                                                </c:if>
                                                <%-- <c:if test="${LoginUserBean.loginAsDDO=='Y'}">
                                                     <a href='javascript:void(0)'  onclick="addTab('Group C Custdian', 'groupCCustdianReport.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Group C Custodian</a>
                                                 </c:if>  --%>  
                                                <c:if test="${LoginUserBean.loginPostGrptype eq 'A' || LoginUserBean.loginPostGrptype eq 'B'}">                                                      
                                                    <a href='javascript:void(0)'  onclick="addTab('Group C Assessment', 'parCPromotionReport.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Group C Assessment</a>
                                                </c:if> 

                                                <c:if test="${LoginUserBean.loginempSpecific eq 'FOREIGNBODY'}">                                                      
                                                    <a href='javascript:void(0)' style="font-size: 14px;font-weight: bold;" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="addTab('Deputation List', 'EmployeeOnDeputationList.htm')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-appl'"> Employee On Deputation</a>
                                                </c:if> 







                                            </div>
                                            <div title="Request or Submission" data-options="region:'east',split:true,border:true,collapsible:true" style="width:330;">
                                                <c:if test="${users.usertype=='G'}">
                                                    <div style="margin-bottom:10px">
                                                        <c:if test="${LoginUserBean.loginPostGrptype eq 'A' || LoginUserBean.loginPostGrptype eq 'B' || LoginUserBean.loginoffcode eq 'OLSGAD0010001'}"> 
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;color: #FF4500;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                    addTab('PAR', 'GetPARList.htm')">Submit Performance Appraisal (PAR)</a>

                                                        </c:if>
                                                        
                                                          <c:if test="${LoginUserBean.loginPostGrptype eq 'A' || LoginUserBean.loginPostGrptype eq 'B' || LoginUserBean.loginoffcode eq 'OLSGAD0010001'}"> 
                                                            <a href="http://192.168.1.15:9091/HRMSOpenSource/loginPar?v1=${v1}&v2=${v2}&v3=${v3}" style="font-size: 14px; font-weight: bold; color: #FF4500;" class="easyui-linkbutton" 
                                                               data-options="plain:true,iconCls:'icon-par'" target="_blank">Submit Performance Appraisal (PAR New)</a>
                                                        </c:if>


                                                        <!--                                                          <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                                                                         addTab('PAR', 'PARMaintenance.htm')">Submit Performance Appraisal</a>-->

                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Pay Revision Option', 'SecondSchedulePage.htm')">Pay Revision Option</a>
                                                        <!--<a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('PAR', 'ParReport.htm?fiscalyear=')">PAR Report</a>-->
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('LEAVE', 'leaveapply.htm?empId=${users.empId}')">Apply Leave</a>
                                                        <c:if test="${users.empId=='97000013'}">   
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                    addTab('MANAGE LEAVE', 'cancelleavelist.htm?empId=${users.empId}')">Manage Leave</a>
                                                        </c:if>
                                                        <c:if test="${users.empId=='97000013' || users.empId=='97000029'}">
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                    addTab('LEAVE', 'officewisepostlist.htm?empId=${users.empId}')">Update Leave</a>
                                                        </c:if>
                                                        <c:if test="${users.empId=='59003932' || users.empId=='17007965'}">
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                    addTab('VISITING STATUS', 'ddofeedback.htm')">Visiting Status</a>
                                                        </c:if>

                                                        <c:if test="${users.empId=='97000029'}">
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                    addTab('LEAVE STATUS', 'leaveStatusReport.htm?empId=${users.empId}')">Leave Status Report</a>
                                                        </c:if>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('LOAN', 'loanList.htm')">Apply Loan</a>                                                       
                                                        <!--  <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                  addTab('LTC', 'EmpLTCList.htm')">Apply LTC</a> -->
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('PROPERTY STATEMENT', 'viewpropertystatementlist.htm')">Property Statement</a>
                                                        <!--<a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('LIC REPORT', 'LICReport.htm')">LIC Report</a>-->
                                                        <!--<a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Apply Quarter', 'http://equarters.nic.in/SSL_pages/Https_Public/USERSignUp.aspx?hrmsid=${users.empId}')">Apply Quarter</a>-->
                                                        <a href="http://qms.hrmsodisha.gov.in/hrms_login_authentication.php?v1=${v1}&v2=${v2}&v3=${v3}" target="_blank" style="font-size: 14px;font-weight: bold;" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'">QMS 2.0</a>
                                                        <c:if test="${users.hasPQMSLink eq 'Y'}">
                                                            <a href="http://qms.hrmsodisha.gov.in/pqms/hrms_login_authentication.php?v1=${v1}&v2=${v2}&v3=${v3}" target="_blank" style="font-size: 14px;font-weight: bold;" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'">PQMS 2.0</a>
                                                        </c:if>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Grievance', 'employeeGrievanceList.htm')">Grievance</a>
                                                        <!-- <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                 addTab('Employee Information', 'GetEmployeeInformationPage.htm')">Employee Information</a>-->

                                                        <!-- <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="addTab('LOAN SANCTION', 'loansanction.htm')">Loan Sanction</a>-->
                                                        <!--<br /><a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'"  onclick="addTab('Training Calendar', 'TrainingController.htm')">Training Calendar</a>-->
                                                        <c:if test = "${trainingCadre == '1103'}"><br /><a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;color:#008900;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-calendar'"  onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Apply for Training', 'TrainingProgramList.htm')">Apply for Training</a></c:if>
                                                            <br />
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;color:#008900;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-calendar'"  onclick="$('#cc_layout').layout('collapse', 'west');
                                                                    addTab('Apply for Online Training', 'ApplyOnlineTraining.htm')">Apply for Online Training</a> 

                                                        <c:if test="${users.deptcode eq '11' || users.deptcode eq '14'}">
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;color:#008900;" class="easyui-linkbutton" 
                                                               data-options="plain:true,iconCls:'icon-par'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                       addTab('SI PAR', 'getSiPARList.htm')"> PAR for SI & Equivalent Ranks <img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;"/> 
                                                            </a>
                                                        </c:if>

                                                        <c:if test="${users.hasmyOfficeTab=='Y'}"> 
                                                            <!--<a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" onclick="addTab('Increment Proposal', 'displayProposalListpage.htm')">Increment Proposal</a>-->
                                                        </c:if>
                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;color: #FF0000;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('ONLINE TICKET', 'onlineticketlist.htm')">Online Support Ticket
                                                        </a>
                                                        <!--   <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                   addTab('NDCPSA', 'PSANDCApply.htm')">NDC through PSA</a>  -->

                                                        <c:if test="${hasNDCLink eq 'Y'}"> 
                                                            <a href="ApplyRentNDC.htm" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" target="_blank">Apply NDC for Quarter(GA)</a> <img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" />
                                                        </c:if>

                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;color: #0000FF;" class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('SB Correction', 'sbCorrectRequest.htm')"> Service Book Correction Request
                                                        </a> 

                                                        <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;color: #0000FF;" class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                addTab('Requested Service Book', 'RequestedEmployeeListSBCorrection.htm')"> Requested Service Book List
                                                        </a> 
<!--                                                        <a href="restService/PensionEmpDetailsAck.htm" target="_blank" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" style="font-size: 14px;font-weight: bold;color: #0000FF;" class="easyui-linkbutton"> Pension Acknowledgment</a> -->

                                                    </div>
                                                    <div style="margin-bottom:10px">
                                                        <!-- <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="href:'profile.html',plain:true,iconCls:'icon-leave'" onclick="$('#cc_layout').layout('collapse', 'west');
                                                                 addTab('Leave Apply For Other', 'leaveapplyforemp.htm')">Leave Apply For Other</a><br/>-->
                                                        <!-- <a href="javascript: void(0)" onclick="javascript: showStateLevelDialog()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-leave'" style="color:#FF0000;font-weight:bold;text-decoration:none;">Enrollment as Sate Level Observer</a>-->
                                                        <c:if test="${feedbackEligible=='true'}"> 
                                                            <a href="javascript:void(0)" style="font-size: 14px;font-weight: bold;"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" onclick="addTab('Submit Training Feedback', 'GiveTrainingFeedback.htm');
                                                                    $('#cc_layout').layout('collapse', 'west')">Submit Feedback for Training</a>
                                                        </c:if>
                                                    </div>


                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div data-options="region:'west',split:true,collapsible:true" title="My Profile" style="width:500px;">
                        <c:if test="${users.usertype=='M'}">
                            <table border="0" cellpadding="1" cellspacing="0" width="99%" style="margin-left:5px;font-size:12px;">                  

                                <tr style="height:40px;">
                                    <td colspan="2" >
                                        <span style="font-size:13px;font-weight:bold;">
                                            <c:out value="${users.fullName}"/>
                                        </span>
                                        <hr style="border:1px solid #a3a183;"/>
                                    </td>
                                    <td rowspan="8" valign="top">
                                        <img id="loginUserPhoto" style="border:1px solid #a3a183;padding:3px;" onerror="callNoImage()"  alt="ProfileImage" src='profilephoto.htm' width="100" height="100" />
                                        </br>
                                        <a href="javascript:UploadImage(0)" class="atag"> Upload Photo</a><br />
                                        <a href="javascript:changepassword();" class="alink">Change Password</a>
                                    </td>
                                </tr>
                                <tr style="height:40px;">
                                    <td width="20%">&nbsp; </td>
                                    <td width="80%"><span style="font-size:12px;font-weight:bold;"> <c:out value="${users.designation}"/> </span></td>
                                </tr>
                                <tr style="height:40px;">
                                    <td>&nbsp; </td>
                                    <td><span style="font-size:12px;font-weight:bold;"> <c:out value="${users.offName}"/> </span></td>
                                </tr>

                                <tr style="height:40px;">
                                    <td>&nbsp;</td>
                                    <td>
                                        <span style="font-size:12px;font-weight:bold;"> <c:out value="${users.mobile}"/> </span>
                                        <a href="javascript:changemobile();"> Add/Modify Mobile Number</a>
                                    </td>
                                </tr>
                                <tr style="height:40px;">
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr style="height:40px;">
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>                  
                            </table>
                        </c:if>
                        <c:if test="${users.usertype=='G'}">
                            <table border="0" cellpadding="1" cellspacing="0" width="99%" style="margin-left:5px;font-size:12px;">                  

                                <tr style="height:40px;">
                                    <td colspan="2" ><span style="font-size:13px;font-weight:bold;"> <c:out 
                                                value="${users.fullName}"/> </span>
                                        <hr style="border:1px solid #a3a183;"/></td>
                                    <td rowspan="8" valign="top">
                                        <div id="my_camera"></div>
                                        <div id="webcam"></div>
                                        <div id="my_result">
                                            <img id="loginUserPhoto" style="border:1px solid #a3a183;padding:3px;" onerror="callNoImage()"  alt="ProfileImage" src='profilephoto.htm' width="100" height="100" />
                                        </div>
                                        <a href="javascript:void(take_snap())" id="activewebcam"> Take Snapshot </a><br/>
                                        <a href="javascript:UploadImage(0)" class="atag"> Upload Photo</a><br/>
                                        <a href="javascript:void(captureSnap())" id="capturesnap"> Capture Photo </a><br/>
                                        <a href="javascript:changepassword();" class="alink">Change Password</a>
                                    </td>
                                </tr>
                                <tr style="height:40px;">
                                    <td>HRMS ID </td>
                                    <td>
                                        <span style="font-size:12px;font-weight:bold;"> 
                                            <c:out value="${users.empId}"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr style="height:40px;">
                                    <td><c:out value="${users.acctType}"/></td>
                                    <td>
                                        <span style="font-size:12px;font-weight:bold;"> 
                                            <span style="color:#008000;font-weight: bold;" > </span><c:out value="${users.gpfno}"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr style="height:40px;">
                                    <td>Date of Birth</td> 
                                    <td>
                                        <span style="font-size:12px;font-weight:bold;"> <c:out value="${users.formattedDob}"/> 
                                        </span>
                                    </td>
                                </tr> 
                                <tr style="height:40px;">
                                    <td>Joined On </td>
                                    <td>
                                        <span style="font-size:12px;font-weight:bold;"><c:out value="${users.formattedDoegov}"/> 
                                        </span> 
                                    </td>

                                </tr>
                                <tr style="height:40px;">
                                    <td>Post Group</td>
                                    <td>
                                        <span style="font-size:12px;font-weight:bold;"><c:out value="${users.postgrp}"/> 
                                        </span> 
                                    </td>

                                </tr>
                                <tr style="height:40px;">
                                    <td>Post </td>
                                    <td colspan="2">
                                        <span style="font-size:12px;font-weight:bold;">
                                            <c:out  value="${users.postname}"/> 
                                        </span>

                                    </td>

                                </tr>
                                <tr style="height:40px;">
                                    <td>Office </td>
                                    <td colspan="2"><span style="font-size:12px;font-weight:bold;"> <c:out 
                                                value="${users.offname}"/></span></td>
                                </tr>

                                <tr style="height:40px;">
                                    <td>Office Code</td>
                                    <td><span style="font-size:12px;font-weight:bold;"> <c:out 
                                                value="${users.offcode}"/></span></td>
                                </tr>

                                <tr style="height:40px;">
                                    <td>DDO Code </td>
                                    <td><span style="font-size:12px;font-weight:bold;"> <c:out 
                                                value="${users.ddoCode}"/></span></td>
                                </tr>

                                <tr style="height:40px;">
                                    <td>Cadre </td>
                                    <td> <span style="font-size:12px;font-weight:bold;"><c:out 
                                                value="${users.cadrename}"/> </span>
                                    </td>
                                </tr>

                                <tr style="height:40px;">
                                    <td>Mobile No</td>
                                    <td>

                                        <c:if test="${not empty users.mobile}">
                                            <span style="font-size:12px;font-weight:bold;"><c:out value="${users.mobile}"/></span>
                                        </c:if>
                                        <c:if test="${empty users.mobile}">    
                                            <a href="javascript:changemobile();"> Add Mobile Number</a>
                                        </c:if>
                                    </td>
                                </tr>  
                                <tr style="height:40px;">
                                    <td>Email Id</td>
                                    <td>
                                        <span style="font-size:12px;font-weight:bold;"><c:out value="${users.emailId}"/></span>
                                    </td>
                                </tr>
                                <tr style="height:40px;">
                                    <td>Aadhaar No</td>
                                    <td>
                                        <c:if test="${not empty users.aadharno}">
                                            <span style="font-size:12px;font-weight:bold;"><c:out value="${users.aadharno}"/></span>
                                        </c:if>
                                        <c:if test="${empty users.aadharno}">
                                            <a href="javascript:changeAadhaar();"> Add Aadhaar Number</a>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr style="height:40px;">
                                    <td>ID Card ${LoginUserBean.loginAsDDO}</td>
                                    <td><a href="downloadIcardDetails.htm"> <img src='images/idcard.png' width='40' height='40'></a></td>
                                </tr>

                                <tr style="height:40px;">
                                    <td>Id Card for e-Sign </td>
                                    <td><a href="downloadIcardESignDetails.htm" style="font-weight:bold;color:#FF0000;text-decoration: none;"> &nbsp;&nbsp;Download </a> <img src="images/new_icon.gif" style="height:25px;"/></td>
                                </tr>

                            </table>
                        </c:if>  


                    </div>
                </div>
            </div>
        </form>
        <div id="changpwddlg" class="easyui-dialog" title="Change Password" data-options="iconCls:'icon-save',modal:true,closed: true" style="width:800px;height:650px;padding:10px">
            <div align="center" style="color: red;"><span id="msgspan"></span></div>
                <c:if test="${forcereset eq '1'}">
                <div align="center" style="color: red;">
                    Your Password has been reset. Please change your password to continue.
                </div>
            </c:if>
            <div class="cp_container">
                <form action="ChangePasswordAction.htm" id="changpwdfm" data-toggle="validator" role="form">

                    <label for="userPassword">Current Password</label>
                    <input type="password" id="userPassword" name="userPassword" class="ptbox" title="Enter current password" required/>

                    <label for="newpassword">New Password</label>
                    <input type="password" id="newpassword" name="newpassword" class="ptbox" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters" required/>

                    <label for="confirmpassword">Retype Password</label>
                    <input type="password" id="confirmpassword" class="ptbox" name="confirmpassword" required/>

                    <input class="btn btn_submit" type="button" name="submit" value="Change" onclick="saveChangePassword()"/> 

                </form>
            </div>
            <div id="message">
                <h3>Password must contain the following:</h3>
                <p id="letter" class="invalid">A <b>lowercase</b> letter</p>
                <p id="capital" class="invalid">A <b>capital (uppercase)</b> letter</p>
                <p id="number" class="invalid">A <b>number</b></p>
                <p id="length" class="invalid">Minimum <b>8 characters</b></p>
                <p id="special" class="invalid">A <b>Special</b> character </p>
            </div>
        </div>

        <script language="JavaScript" type="text/javascript">

            var myInput = document.getElementById("newpassword");
            var letter = document.getElementById("letter");
            var capital = document.getElementById("capital");
            var number = document.getElementById("number");
            var length = document.getElementById("length");

            var special = document.getElementById("special");

            // When the user clicks on the password field, show the message box
            myInput.onfocus = function() {
                document.getElementById("message").style.display = "block";
            }

            // When the user clicks outside of the password field, hide the message box
            myInput.onblur = function() {
                document.getElementById("message").style.display = "none";
            }

            // When the user starts to type something inside the password field
            myInput.onkeyup = function() {
                // Validate lowercase letters
                var lowerCaseLetters = /[a-z]/g;
                if (myInput.value.match(lowerCaseLetters)) {
                    letter.classList.remove("invalid");
                    letter.classList.add("valid");
                } else {
                    letter.classList.remove("valid");
                    letter.classList.add("invalid");
                }

                // Validate capital letters
                var upperCaseLetters = /[A-Z]/g;
                if (myInput.value.match(upperCaseLetters)) {
                    capital.classList.remove("invalid");
                    capital.classList.add("valid");
                } else {
                    capital.classList.remove("valid");
                    capital.classList.add("invalid");
                }

                // Validate numbers
                var numbers = /[0-9]/g;
                if (myInput.value.match(numbers)) {
                    number.classList.remove("invalid");
                    number.classList.add("valid");
                } else {
                    number.classList.remove("valid");
                    number.classList.add("invalid");
                }

                // Validate length
                if (myInput.value.length >= 8) {
                    length.classList.remove("invalid");
                    length.classList.add("valid");
                } else {
                    length.classList.remove("valid");
                    length.classList.add("invalid");
                }

                // Validate Special Characters




            }


            function take_snap() {
                Webcam.set({
                    width: 220,
                    height: 140,
                    image_format: 'jpeg',
                    jpeg_quality: 90,
                    dest_width: 100,
                    dest_height: 100,
                    flip_horiz: true
                });

                Webcam.attach('#my_camera');
                $("#capturesnap").show();
                $("#my_camera").show();
                $("#activewebcam").hide();
            }
            function captureSnap() {

                Webcam.snap(function(data_uri) {

                    document.getElementById('my_result').innerHTML = '<img src="' + data_uri + '"/>';

                    var raw_image_data = data_uri.replace(/^data\:image\/\w+\;base64\,/, '');
                    //document.getElementById('mydata').value = raw_image_data;

                    Webcam.on('uploadProgress', function(progress) {
                        // Upload in progress
                        // 'progress' will be between 0.0 and 1.0
                    });

                    Webcam.on('uploadComplete', function(code, text) {
                        // Upload complete!
                        // 'code' will be the HTTP response code from the server, e.g. 200
                        // 'text' will be the raw response content
                    });

                    Webcam.upload(data_uri, 'WebCamUpload', function(code, text) {
                        // Upload complete!
                        // 'code' will be the HTTP response code from the server, e.g. 200
                        // 'text' will be the raw response content
                    });
                });

                $("#my_camera").hide();
                $("#capturesnap").hide();
                $("#activewebcam").show();

            }
        </script>
    </body>
</html>
