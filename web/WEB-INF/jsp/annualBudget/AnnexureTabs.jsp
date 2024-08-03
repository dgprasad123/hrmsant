<%-- 
    Document   : AnnexureTabs
    Created on : 10 Nov, 2020, 9:41:49 AM
    Author     : Surendra
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>

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
    $(document).ready(function () {

        
    });
</script>
<div id="pmenu_wrap">
    <ul>
        <li><a href="DDOAnnexureIIAController.htm?budgetid=${budgetid}" <c:if  test = "${param.menuHighlight=='ANNEXIIA'}"> class="sel"</c:if>>Annexure-IIA</a></li>
        <li class="sep"></li>
        <li><a href="DDOAnnexureIIBController.htm?budgetid=${budgetid}" <c:if  test = "${param.menuHighlight=='ANNEXIIB'}"> class="sel"</c:if>>Annexure-IIB</a></li>
        <li class="sep"></li>
        <%--<li><a href="DDOAnnexureIIIAController.htm" <c:if  test = "${param.menuHighlight=='ANNEXIIIA'}"> class="sel"</c:if>>Annexure-IIIA</a></li>
        <li class="sep"></li>
        <li><a href="DDOAnnexureIIIBController.htm" <c:if  test = "${param.menuHighlight=='ANNEXIIIB'}"> class="sel"</c:if>>Annexure-IIIB</a></li>
        <li class="sep"></li>
        <li><a href="DDOAnnexureIIICController.htm" <c:if  test = "${param.menuHighlight=='ANNEXIIIC'}"> class="sel"</c:if>>Annexure-IIIC</a></li>--%>
    </ul>
    <div class="clr"></div>
    
</div>
