<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
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
        <script type="text/javascript" src="js/bootstrap.min.js"></script>        
    </head>
    <body style="padding-top: 10px;">
  
        <div id="boxes">
            <div style=" left: 551.5px; display: none;" id="dialog" class="window"> 

                <table class="table-bordered" align='center'>
                    <tr>
                        <td align="center"><h2 class="alert alert-info" style="color:#FF0000;font-size:14pt;font-weight:bold;margin:0px;">
                                You have to submit the following details in order to complete your profile:</h2>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Personal Information</span>
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
            <div id="profile_container">

                <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th>#</th>
                                <th>Name</th>
                                <th>Relation</th>
                                <th>Gender</th>
                                <th>Nominee</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${familyRelationList}" var="familyRelation" varStatus="cnt">
                                <tr>
                                    <th scope="row">${cnt.index+1}</th>
                                    <td>${familyRelation.initials} ${familyRelation.fname} ${familyRelation.mname} ${familyRelation.lname}</td>
                                    <td>${familyRelation.relation}</td>
                                    <td>${familyRelation.gender}</td>
                                    <td>
                                        <c:if test="${familyRelation.is_Nominee eq 'Y'}">
                                            <img src="images/verified.png" width="20" height="20"/>
                                        </c:if>
                                    </td>
                                    <td>                                    
                                        <a href="FamilyDetailDdo.htm?slno=${familyRelation.slno}" class="btn btn-default">
                                            <span class="glyphicon glyphicon-pencil"></span> Edit</a>

                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                    <form:form action="FamilyNewDdo.htm">
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
    </body>
    <script type="text/javascript">
        $(document).ready(function() {
            setTimeout(function() {
                $("#notification_blk").slideUp();
            }, 5000);
        });
    </script> 
</html>
