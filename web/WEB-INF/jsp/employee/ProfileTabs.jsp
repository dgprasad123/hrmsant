<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
<link rel="stylesheet" type="text/css" href="css/popupmain.css"/>
<style type="text/css">
    #pmenu_wrap{width:95%;margin:0px auto;height:30px;font-family:Arial;font-size:10pt;font-weight:bold;}
    #pmenu_wrap ul{margin:0px;padding:0px;list-style-type:none;margin-left:-1px;}
    #pmenu_wrap ul li{float:left;}
    #pmenu_wrap ul li.sep{width:10px;height:30px;border-bottom:1px solid #CCCCCC;}
    #pmenu_wrap ul li a{float:left;border:1px solid #DDD;height:30px;line-height:30px;padding-left:15px;padding-right:15px;background:#F6F6F6;text-decoration:none;color:#333333;border-top-left-radius:5px;border-top-right-radius:5px;}
    #pmenu_wrap ul li a:hover{background:#1F79AF;color:#FFFFFF;border:1px solid #055590;}
    #pmenu_wrap ul li a.sel{background:#E8743B;color:#FFFFFF;border:1px solid #AF3E08;}
    .clr{clear:both;font-size:1px;}
    #profile_container{width:95%;margin:0px auto;border-top:0px;}
    ol li{width:180px;float:left;text-align:left;}
</style>  
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
<div id="pmenu_wrap">
    <ul>
        <li><a href="firstpagesb.htm" <c:if test = "${param.menuHighlight=='FIRSTPAGESB'}"> class="sel"</c:if>>First Page</a></li>
            <li class="sep"></li>
            <li><a href="employeeProfileView.htm" <c:if test = "${param.menuHighlight=='PROFILEPAGESB'}"> class="sel"</c:if>>Personal Information</a></li>
            <li class="sep"></li>
            <li><a href="employeeLanguageView.htm" <c:if test = "${param.menuHighlight=='LANGUAGEPAGESB'}"> class="sel"</c:if>>Language</a></li>
            <li class="sep"></li>
            <li><a href="employeeIdentityView.htm" <c:if test = "${param.menuHighlight=='IDENTITYPAGESB'}"> class="sel" </c:if>>Identity</a></li>    
            <li class="sep"></li>
            <li><a href="employeeAddressView.htm" <c:if test = "${param.menuHighlight=='ADDRESSPAGESB'}"> class="sel" </c:if>>Address</a></li>    
            <li class="sep"></li>
            <li><a href="employeeFamilyView.htm" <c:if test = "${param.menuHighlight=='FAMILYPAGESB'}"> class="sel" </c:if>>Family</a></li>        
            <li class="sep"></li>
            <li><a href="employeeEducationView.htm" <c:if test = "${param.menuHighlight=='EDUCATIONPAGESB'}"> class="sel" </c:if>>Education</a></li>
            <li class="sep"></li>        
            <li><a href="employeePostingView.htm" <c:if test = "${param.menuHighlight=='POSTINGPAGESB'}"> class="sel" </c:if>>Posting</a></li>
            <li class="sep"></li>
            <li><a href="EmployeeProfileAcknowledgement.htm" <c:if test = "${param.menuHighlight=='EMPPROFILEACKNOWLEDGE'}"> class="sel" </c:if>>Profile Acknowledgement</a></li> 
        
         <!--   <li class="sep"></li>
            <li><a href="EmployeePensionAcknowledgement.htm" <c:if test = "${param.menuHighlight=='EMPENSIONACKNOWLEDGE'}"> class="sel" </c:if>>Pension Acknowledgement</a></li> 
         -->
    </ul>
    <div class="clr"></div>
    <div id="boxes">
        <div style=" left: 551.5px; display: none;" id="dialog" class="window"> 

            <table class="table-bordered" align='center'>
                <tr>
                    <td align="center"><h2 class="alert alert-info" style="color:#FF0000;font-size:14pt;font-weight:bold;margin:0px;">
                            You have to submit the following details in order to complete your profile:</h2>
                        <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Personal Information Tab:</span>
                        <ol style="margin:0px;font-size:10pt;">
                            <li>Employee Name</li>
                            <li>ACCT TYPE</li>
                            <li>GPF NO</li>
                            <li>FIRST NAME</li>
                            <li>LAST NAME</li>
                            <li>GENDER</li>
                            <li>MARITAL STATUS</li>
                            <li>CATEGORY</li>
                            <li>POST GROUP</li>
                            <li>HEIGHT</li>
                            <li>DOB</li>
                            <li>DOS</li>
                            <li>JOIN DATE OF GOO</li>
                            <li>DATE OF ENTRY INTO GOVERNMENT</li>
                            <li>BLOOD GROUP</li>
                            <li>MOBILE</li>
                            <li>POST GROUP</li>
                            <li>HOME TOWN</li>
                            <li>DOMICILE</li>


                            <li>BANK ACCOUNT NUMBER</li>
                        </ol>
                        <div style="clear:both;"></div>
                        <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Language Tab</span>
                        <ol style="margin:0px;font-size:10pt;">
                            <li style="width:50%">LANGUAGE AT LEAST ONE</li>
                        </ol>
                        <div style="clear:both;"></div>
                        <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Identity Tab</span>
                        <ol style="margin:0px;font-size:10pt;">
                            <li>PAN NUMBER</li>
                            <li>AADHAAR NUMBER</li>
                        </ol>
                        <div style="clear:both;"></div>
                        <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Address Tab</span>
                        <ol style="margin:0px;font-size:10pt;">
                            <li style="width:50%">ADDRESS BOTH PERMANENT AND PRESENT </li>
                            <li style="width:50%">ADDRESS TYPE </li>
                            <li style="width:50%">ADDRESS</li>
                            <li style="width:50%">BLOCK</li>
                            <li style="width:50%">PIN CODE </li>
                        </ol>

                        <div style="clear:both;"></div>
                        <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Family Tab</span>
                        <ol style="margin:0px;font-size:10pt;">
                            <li style="width:50%">FATHERS NAME/HUSBAND NAME</li>
                            <li style="width:50%">NOMINEE AT LEAST ONE</li>
                            <li style="width:50%">IDENTITY NUMBER</li>
                            <li style="width:50%">IDENTITY TYPE</li>
                        </ol>
                        <div style="clear:both;"></div>
                        <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Education Tab</span>
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
        <div style="width: 1478px; font-size: 32pt; color:white; height: 602px; display: none; opacity: 0.8;" id="mask"></div>
    </div>
</div>
