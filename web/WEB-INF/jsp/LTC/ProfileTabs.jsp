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
    $(document).ready(function(){
   
                $('#mask').click(function () {
ViewMessage();
                });
    });
    function validateURL(pageName, ltcID)
    {
        if(ltcID == 0)
        {
            alert("Please complete the Basic Information.")
            self.location = 'BasicInfo.htm';
        }
        else
        {
            self.location = pageName+'.htm?id='+ltcID;
        }
    }
</script>
<div id="pmenu_wrap">
    <ul>
        <li><a href="BasicInfo.htm?id=${param.ltcID}" <c:if  test = "${param.menuHighlight=='BASICINFO'}"> class="sel"</c:if>>Basic Information</a></li>
            <li class="sep"></li>
            <li><a href="javascript:void(0)" onclick="javascript: validateURL('FamilyMembers', ${param.ltcID})" <c:if  test = "${param.menuHighlight=='FAMILYMEMBERS'}"> class="sel"</c:if>>Family Members</a></li>
            <li class="sep"></li>
            <li><a href="javascript:void(0)" onclick="javascript: validateURL('ReimbursementDetails', ${param.ltcID})" <c:if  test = "${param.menuHighlight=='REIMBURSEMENTDETAILS'}"> class="sel"</c:if>>Reimbursement Details</a></li>
            <li class="sep"></li>
            <li><a href="javascript:void(0)" onclick="javascript: validateURL('LTCReportingAuthority', ${param.ltcID})" <c:if  test = "${param.menuHighlight=='LTCRA'}"> class="sel"</c:if>>Reporting Authority</a></li>
            <li class="sep"></li>
            <li><a href="javascript:void(0)" onclick="javascript: validateURL('LTCIssuingAuthority', ${param.ltcID})" <c:if  test = "${param.menuHighlight=='LTCIA'}"> class="sel"</c:if>>Issuing Authority</a></li>
            <li class="sep"></li>
            <li><a href="javascript:void(0)" onclick="javascript: validateURL('ValidateLTC', ${param.ltcID})" <c:if  test = "${param.menuHighlight=='VALIDATE'}"> class="sel"</c:if>>Validate & Submit</a></li>
        
    </ul>
    <div class="clr"></div>

</div>
