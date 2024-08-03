<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Welcome to HRMS Odisha ::</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link rel="stylesheet" type="text/css" href="css/popupmain.css"/>  
        <script type="text/javascript">
            function ViewMessage() {
                $('#mask').hide();
                $('.window').hide();
                //   window.location="viewCommunicationDetails.htm";
            }
            function showInformationWindow()
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

                $(id).fadeIn(2000);
            }
            $(document).ready(function() {

                $('#mask').click(function() {
                    ViewMessage();
                });
            });
        </script>      
        <style type="text/css">
            #profile_container{width:95%;margin:0px auto;border-top:0px;}
            ol li{width:180px;float:left;text-align:left;}
        </style>  
        <script type="text/javascript">
            function callNoImage() {
                var userPhoto = document.getElementById('loginUserPhoto');
                userPhoto.src = "images/NoEmployee.png";

            }
        </script>
        <style type="text/css">
            .card {
                background-color: #ffffff;
                border-radius: 6px;
                box-shadow: 0 2px 2px rgba(204, 197, 185, 0.5);
                color: #252422;
                margin-bottom: 20px;
                position: relative;
                z-index: 1;
            }
            .card .image {
                width: 100%;
                overflow: hidden;
                height: 260px;
                border-radius: 6px 6px 0 0;
                position: relative;
                -webkit-transform-style: preserve-3d;
                -moz-transform-style: preserve-3d;
                transform-style: preserve-3d;
            }
            .card .image img {
                width: 100%;
            }
            .card .content {
                padding: 15px 15px 10px 15px;
            }
            .card .header {
                padding: 20px 20px 0;
            }
            .card .description {
                font-size: 16px;
                color: #66615b;
                text-transform:capitalize;
            }
            .card h6 {
                font-size: 12px;
                margin: 0;
            }
            .card .category,
            .card label {
                font-size: 14px;
                font-weight: 400;
                color: #9A9A9A;
                margin-bottom: 0px;
            }
            .card .category i,
            .card label i {
                font-size: 16px;
            }
            .card label {
                font-size: 15px;
                margin-bottom: 5px;
            }
            .card .title {
                margin: 0;
                color: #252422;
                font-weight: 300;
            }
            .card .avatar {
                width: 50px;
                height: 50px;
                overflow: hidden;
                border-radius: 50%;
                margin-right: 5px;
            }
            .card .footer {
                padding: 0;
                line-height: 30px;
            }
            .card .footer .legend {
                padding: 5px 0;
            }
            .card .footer hr {
                margin-top: 5px;
                margin-bottom: 5px;
            }
            .card .stats {
                color: #a9a9a9;
                font-weight: 300;
            }
            .card .stats i {
                margin-right: 2px;
                min-width: 15px;
                display: inline-block;
            }
            .card .footer div {
                display: inline-block;
            }
            .card .author {
                font-size: 12px;
                font-weight: 600;
                text-transform: uppercase;
            }
            .card .author i {
                font-size: 14px;
            }
            .card.card-separator:after {
                height: 100%;
                right: -15px;
                top: 0;
                width: 1px;
                background-color: #DDDDDD;
                content: "";
                position: absolute;
            }
            .card .ct-chart {
                margin: 30px 0 30px;
                height: 245px;
            }
            .card .table tbody td:first-child,
            .card .table thead th:first-child {
                padding-left: 15px;
            }
            .card .table tbody td:last-child,
            .card .table thead th:last-child {
                padding-right: 15px;
            }
            .card .alert {
                border-radius: 4px;
                position: relative;
            }
            .card .alert.alert-with-icon {
                padding-left: 65px;
            }
            .card .icon-big {
                font-size: 3em;
                min-height: 64px;
            }
            .card .numbers {
                font-size: 2em;
                text-align: right;
            }
            .card .numbers p {
                margin: 0;
            }
            .card ul.team-members li {
                padding: 10px 0px;
            }
            .card ul.team-members li:not(:last-child) {
                border-bottom: 1px solid #F1EAE0;
            }

            .card-user .image {
                border-radius: 8px 8px 0 0;
                height: 150px;
                position: relative;
                overflow: hidden;
            }
            .card-user .image img {
                width: 100%;
            }
            .card-user .image-plain {
                height: 0;
                margin-top: 110px;
            }
            .card-user .author {
                text-align: center;
                text-transform: none;
                margin-top: -65px;
            }
            .card-user .author .title {
                color: #403D39;
            }
            .card-user .author .title small {
                color: #ccc5b9;
            }
            .card-user .avatar {
                width: 140px;
                height: 140px;
                border-radius: 50%;
                position: relative;
                margin-bottom: 15px;
            }
            .card-user .avatar.border-white {
                border: 5px solid #FFFFFF;
            }
            .card-user .avatar.border-gray {
                border: 5px solid #ccc5b9;
            }
            .card-user .title {
                font-weight: 600;
                line-height: 24px;
            }
            .card-user .description {
                text-transform: fullsize-kana;
                margin-top: 10px;
            }
            .card-user .content {
                min-height: 200px;
            }
            .card-user.card-plain .avatar {
                height: 190px;
                width: 190px;
            }

            .card-map .map {
                height: 500px;
                padding-top: 20px;
            }
            .card-map .map > div {
                height: 100%;
            }

            .card-user .footer,
            .card-price .footer {
                padding: 5px 15px 10px;
            }
            .card-user hr,
            .card-price hr {
                margin: 5px 15px;
            }

            .card-plain {
                background-color: transparent;
                box-shadow: none;
                border-radius: 0;
            }
            .card-plain .image {
                border-radius: 4px;
            }
            .card span{
                color: #252422;
                font-weight: 500;
                line-height: 24px;
                font-size: 16px;
            }
            .lblval{
                text-transform: capitalize;
                color: #252422;
                font-weight: 500;
                line-height: 24px;
                font-size: 16px;
            }
        </style>
    </head>
    <body style="padding-top: 10px;">
      

        <div id="boxes">
            <div style=" left: 551.5px; display: none;" id="dialog" class="window"> 

                <table class="table-bordered" align='center'>
                    <tr>
                        <td align="center"><h2 class="alert alert-info" style="color:#FF0000;font-size:14pt;font-weight:bold;margin:0px;">
                                You have to submit the following details in order to complete your profile:</h2>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Personal Information:</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li>Employee Name</li>
                                <li>ACCT TYPE</li>
                                <li>GPF NO</li>
                                <li>FIRST NAME</li>
                                <li>LAST NAME</li>
                                <li>GENDER</li>
                                <li>MARITAL STATUS</li>
                                <li>CATEGORY</li>
                                <li>HEIGHT</li>
                                <li>DOB</li>
                                <li>JOIN DATE OF GOO</li>
                                <li>DATE OF ENTRY INTO GOVERNMENT</li>
                                <li>BLOOD GROUP</li>
                                <li>RELIGION</li>
                                <li>MOBILE</li>
                                <li>POST GROUP</li>
                                <li>HOME TOWN</li>
                                <li>DOMICILE</li>
                                <li>ID MARK</li>
                                <li>BANK NAME</li>
                                <li>BRANCH NAME</li>
                                <li>BANK ACCOUNT NUMBER</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Language</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">LANGUAGE AT LEAST ONE</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Identity</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li>PAN NUMBER</li>
                                <li>AADHAAR NUMBER</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Address</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">ADDRESS BOTH PERMANENT AND PRESENT</li>
                            </ol>


                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Family</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">FATHERS NAME/HUSBAND NAME</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Education</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">QUALIFICATION AT LEAST ONE</li>
                            </ol>
                        </td>
                    </tr>

                </table>            
                <div id="popupfoot" style="text-align:center;"> 
                    <a href="javascript:void(0)" onclick="javascript: ViewMessage();" class="close agree btn pri" style="background:#FF0000;color:#FFFFFF;">Close</a> 
                </div>            

            </div>  
            <div id="profile_container" style="background-color: #f4f3ef;">
                <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                    <div class="col-lg-4">
                        <div class="card card-user">
                            <div class="image" style="background-color: #337ab7;">
                                &nbsp;
                            </div>
                            <div class="content">
                                <div class="author">
                                    <img class="avatar border-white" src="profilephotoddoview.htm?empid=${employeeProfile.empid}" id="loginUserPhoto" onerror="callNoImage()" alt="..."/>
                                    <h4 class="title">${employeeProfile.empName}<br />
                                        <span><span>Employee Id : ${employeeProfile.empid}</span></span><br/>
                                        <span class="glyphicon glyphicon-phone-alt"> ${employeeProfile.mobile}</span>
                                    </h4>
                                </div>
                                <p class="description text-center">
                                    ${fn:toLowerCase(employeeProfile.spn)}
                                </p>
                                
                            </div>

                            <div class="text-center">
                                <div class="row">

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="card">                        
                            <div class="content">
                                <div class="row">
                                    <div class="col-md-12"> 
                                        <div class="form-group">
                                            <label>GPF No : </label>
                                            <span class="lblval">${employeeProfile.gpfno}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>GIS Type : </label>
                                            <span class="lblval">${employeeProfile.gisName}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>GIS No : </label>
                                            <span class="lblval">${employeeProfile.gisNo}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Date of Birth : </label>
                                            <span class="lblval">${employeeProfile.dob}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Date of Superannuation : </label>
                                            <span class="lblval">${employeeProfile.dor}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Date of Joining in GOO : </label>
                                            <span class="lblval">${employeeProfile.joindategoo}&nbsp;${employeeProfile.txtwefTime} </span>
                                        </div> 
                                        <div class="form-group">
                                            <label>Bank Name : </label>
                                            <span class="lblval">${employeeProfile.bankName}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Branch Name : </label>
                                            <span class="lblval">${employeeProfile.branchName}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>IFSC Code : </label>
                                            <span class="lblval">${employeeProfile.ifmsIFSCode}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Bank Account No : </label>
                                            <span class="lblval">${employeeProfile.bankaccno}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="card">                        
                            <div class="content">
                                <div class="row">
                                    <div class="col-md-12">                                    
                                        <div class="form-group">
                                            <label>Gender : </label>
                                            <span class="lblval">
                                                <c:if test="${employeeProfile.gender == 'M'}">
                                                    Male
                                                </c:if>
                                                <c:if test="${employeeProfile.gender == 'F'}">
                                                    Female
                                                </c:if>
                                                <c:if test="${employeeProfile.gender == 'T'}">
                                                    Transgender
                                                </c:if>     
                                            </span>
                                        </div>
                                        <div class="form-group">
                                            <label>Marital Status : </label>
                                            <span class="lblval" style="text-decoration: ">${employeeProfile.maritalStatus}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Category : </label>
                                            <span class="lblval">${employeeProfile.category}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Post Group : </label>
                                            <span class="lblval">${employeeProfile.postGrpType}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Height (in CM) : </label>
                                            <span class="lblval">${employeeProfile.height}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Blood Group : </label>
                                            <span class="lblval">${employeeProfile.bloodgrp}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Declaration of Home Town : </label>
                                            <span class="lblval">${employeeProfile.homeTown}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Domicile : </label>
                                            <span class="lblval">${employeeProfile.domicil}</span>
                                        </div>
                                        <div class="form-group">
                                            <label>Personal Identification Mark : </label>
                                            <span class="lblval">${fn:toLowerCase(employeeProfile.idmark)}</span>
                                        </div> 
                                        <div class="form-group">
                                            <label>Email : </label>
                                            <span class="lblval">${fn:toLowerCase(employeeProfile.email)}</span><br>
                                                                             
                                        </div> 
                                           
                                    </div>
                                </div>
                            </div>                                       
                        </div>
                    </div>
                </div>
                <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                    <c:if test="${employeeProfile.ifprofileVerified ne 'Y' }">
                        <a href="profile.htm">
                            <input type="submit" name="action" value="Edit" class="btn btn-default"/>
                             
                        </a>
                    </c:if>
                  <!--   <Strong style="display:block;text-align: center;font-weight: bold;font-size: 18px;color: red;text-align:center">Profile Updation is closed </strong>-->



                    <span style="height:30px;background:#F00;color:#FFFFFF;line-height:30px;text-align:center;font-weight:bold;" class="lblval">${dupMobileNo}</span>

                </div>
            </div>
            <script type='text/javascript'>
                function profile_unlock() {
                    var conf = confirm("Do you want to unlock this Profile data");
                    if (conf) {
                        window.location = "unlockProfile.htm";
                    }
                }
            </script>    
    </body>
</html>
