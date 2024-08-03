<%-- 
    Document   : ParMenu
    Created on : Jun 24, 2019, 3:17:59 PM
    Author     : manisha
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </button>

</div>        
<div style="width:100%;height:100px;background:#FFFFFF;text-align:center;position:fixed;z-index:11">
 <img src="images/HRMSbanner.jpg" alt=""  style="width:1800px;height:70px;" />
    
</div>
<div>&nbsp;</div>
<div style="width:100%;height:30px;background:#EAEAEA;border-bottom:3px solid #CCCCCC;position:fixed;top:70px;z-index:11">
    <div style="float:left;width:30%;">
        <span id="date_time" style="line-height:30px;margin-left:10px;color:#900000;font-style:italic;font-weight:bold;"></span>
    </div>
    <div style="float:right;width:60%;text-align:right;line-height:30px;padding-right:20px;">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> Welcome ${LoginUserBean.loginname}</a>
        <a href="logout.htm"><i class="fa fa-fw fa-power-off"></i> Log Out</a>

    </div>
    <div style="clear:both;"></div>
</div>           
<div class="collapse navbar-collapse navbar-ex1-collapse" >
    <ul class="nav navbar-nav side-nav"  style="top:90px;background-color:#188B7A;">
        <li class="active"  style="text-align:center;">
            <a href="#" style="background:#000;width:150px;color:#FFFFFF;margin-top:30px;margin-left:35px;border:5px solid #E1D7D5;padding-top:10px;padding-bottom:10px;">Dashboard</a>
        </li>            

        <c:forEach items="${Privileges}" var="Privilege">
            <li style="margin-top:25px;">
                <a href="${Privilege.modurl}" style="background:#5926B1;color:#FFFFFF;border-radius:20px;margin-left:25px;width:150px;text-align:center;height:80px;padding-top:10px;padding-bottom:10px;">${Privilege.modname}</a>
            </li>
        </c:forEach>
        <li style="margin-top:25px;">
            <a href="showpasswordchange.htm" style="background:#5926B1;color:#FFFFFF;border-radius:20px;margin-left:25px;width:150px;text-align:center;height:80px;padding-top:10px;padding-bottom:10px;">Change Password<br />&nbsp;</a>
        </li>
       
    </ul>
</div>
<script type="text/javascript" src="js/date_time.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $('body').css('background-color', '#188B7A');
        $('body').css('margin-top', '0px');
        date_time('date_time');
        $(window).resize(function() {
            if ($(window).width() > 768) {
                $('#date_time').css('display', 'block');
            }
            else
            {
                $('#date_time').css('display', 'none');
            }
        });
    });
</script> 