<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
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
            $(document).ready(function () {

                $('#mask').click(function () {
                    ViewMessage();
                });
            });
        </script>      
        <style type="text/css">
            #profile_container{width:95%;margin:0px auto;border-top:0px;}
            ol li{width:180px;float:left;text-align:left;}
        </style> 
        <script type="text/javascript">
            function saveLanguage() {
                var language = $('#language').val();
                if (language == '') {
                    alert("Please select Language");
                    return false;
                }
                if (!$('#ifread').is(":checked") && !$('#ifwrite').is(":checked") && !$('#ifspeak').is(":checked")) {
                    alert("Please select at least one from Read/Write/Speak");
                    return false;
                }
                return true;
            }
            function deleteLanguage(slno) {
                if (confirm("Are you sure to Delete?")) {
                    self.location = "deleteEmployeeLanguageDDO.htm?slno=" + slno;
                }
            }
            function editLanguage(slno) {
                self.location = "editEmployeeSingleLanguageDDO.htm?slno=" + slno;
            }
        </script>
    </head>
    <body>
        <div style="width:150px;margin:0px auto;background:#008900;height:30px;text-align:center;margin-bottom:10px;"><a href="javascript: void(0)" onclick="javascript: showInformationWindow();" style="color:#FFFFFF;font-weight:bold;line-height:30px;">Completing Profile</a></div>
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
            <form:form id="fm" action="savelanguage.htm" method="post" name="myForm" commandName="languagemodel">
                <form:hidden path="slno"/>
                <div style=" margin-bottom: 5px;" class="panel panel-info">
                    <div class="panel-body">
                        <table border="0" cellpadding="0" cellspacing="0"  width="100%">
                            <tr>
                                <td width="5%">&nbsp;</td>
                                <td width="15%" >&nbsp;</td>
                                <td width="15%" align="center">&nbsp;</td>
                                <td width="15%" align="center">&nbsp;</td>
                                <td width="15%" align="center">&nbsp;</td>
                            </tr>
                            <tr height="40px">
                                <td align="center"><%=i++%>.</td>
                                <td>Language:</td>
                                <td>
                                    <form:select path="language" style="width:50%;" class="form-control">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <form:options items="${languagelist}" itemLabel="langName" itemValue="langCode"/>
                                    </form:select> 
                                </td>
                                <td>&nbsp; </td>
                                <td>&nbsp; </td>
                            </tr>
                            <tr height="40px">
                                <td align="center"><%=i++%>.</td>
                                <td>If Read: <form:checkbox path="ifread" id="ifread" value="Y"/></td>
                                <td>If Write: <form:checkbox path="ifwrite" id="ifwrite" value="Y"/></td>
                                <td>If Speak: <form:checkbox path="ifspeak" id="ifspeak" value="Y"/></td>
                                <td>If Mother Tongue: <form:checkbox path="ifmlang" value="Y"/></td>
                            </tr>                        
                        </table>

                        <div class="pull-left">
                            <input type="submit" name="save" value="Save" class="btn btn-primary" onclick="return saveLanguage();" />                        
                        </div>                    
                        <br />
                        <table class="table table-bordered">
                            <thead>
                                <tr class="bg-primary text-white">
                                    <th>#</th>
                                    <th>Language</th>
                                    <th>If read</th>
                                    <th>If Write</th>
                                    <th>If Speak</th>
                                    <th>If Mother Tongue</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${emplanguageList}" var="emplanguage" varStatus="cnt">
                                    <tr>
                                        <th scope="row">${cnt.index+1}</th>
                                        <td>${emplanguage.language}</td>
                                        <td>${emplanguage.ifread}</td>
                                        <td>${emplanguage.ifwrite}</td>
                                        <td>${emplanguage.ifspeak}</td>
                                        <td>${emplanguage.ifmlang}</td>
                                        <td>
                                            <c:if test="${emplanguage.isLocked eq 'N'}">
                                                <a href="javascript:editLanguage('${emplanguage.slno}');">Edit</a>&emsp;
                                                <a href="javascript:deleteLanguage('${emplanguage.slno}');">Delete</a>
                                            </c:if>
                                            <c:if test="${emplanguage.isLocked eq 'Y'}">
                                                <img src="images/Lock.png" width="20" height="20"/>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </div>

            </form:form>
    </body>
</html>
