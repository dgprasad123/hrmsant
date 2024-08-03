<%-- 
    Document   : agOdishaMenu
    Created on : 10 Oct, 2022, 12:01:19 PM
    Author     : Devikrushna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<style type="text/css">
   
    
   
    
    
</style>

<div id="left_container">
        <div style="margin: 15px;;padding:5px 0px;padding-bottom:17px;font-size: 15pt;">MANAGE ACCOUNTS GENERAL, ODISHA  <br />
        <table width="100%" style="border:0px; margin-top: 20px;">
            <tr>
                <td width="40"><img src="images/photo_icon.png" style="width:30px;" /></td>
                <td style="line-height:15px;"><span style="font-size:14pt;font-style:italic;color:#5e4e4e;font-weight:normal;">Welcome To ${LoginUserBean.loginname} </span><br />

            </tr>
        </table>
    </div>
   </div>             
<div>&nbsp;</div>
          
        <div class="collapse navbar-collapse navbar-ex1-collapse" >
             <ul class="nav navbar-nav side-nav"  style="top:150px;background-color:rgb(202, 221, 219);">
                 <%--  <li class="active"  style="text-align:center;">
                    <a href="#" style="background:#000;width:150px;color:#FFFFFF;margin-top:30px;margin-left:35px;border:5px solid #E1D7D5;padding-top:10px;padding-bottom:10px;">Dashboard</a>
                </li>    --%>        

                <c:forEach items="${Privileges}" var="Privilege">
                    <li style="margin-top:25px;">
                        <a href="${Privilege.modurl}" style="background:#9C9D76;color:#FFFFFF;border-radius:20px;margin-left:25px;width:130px;text-align:center;height:70px;padding-top:10px;padding-bottom:10px;">${Privilege.modname}</a>
                    </li>
                </c:forEach>
                <li style="margin-top:25px;">
                    <a href="showpasswordchange.htm" style="background:#9C9D76;color:#FFFFFF;border-radius:20px;margin-left:25px;width:130px;text-align:center;height:70px;padding-top:10px;padding-bottom:10px;">Change Password<br />&nbsp;</a>
                </li>
                <li style="margin-top:25px;">
                 <a href="logout.htm" style="background:#9C9D76;color:#FFFFFF;border-radius:20px;margin-left:25px;width:130px;text-align:center;height:70px;padding-top:10px;padding-bottom:10px;"> Log Out</a>
                 </li>
            </ul>
        </div>
                    <script type="text/javascript" src="js/date_time.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        $('body').css('background-color', 'rgb(220, 234, 232)');
        $('body').css('margin-top', '0px');
       date_time('date_time');
    $( window ).resize(function() {   
if($(window).width() > 768) {
     $('#date_time').css('display', 'block');
}
else
{
    $('#date_time').css('display', 'none');
}
    });
    });
</script> 