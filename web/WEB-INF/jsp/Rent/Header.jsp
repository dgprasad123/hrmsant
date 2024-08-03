<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="left_container">
    <div style="border-bottom:1px solid #1A2734;margin-top:10px;padding:5px 0px;">Manage NDC<br />
        <table width="100%" style="border:0px;">
            <tr>
                <td width="40"><img src="images/photo_icon.png" style="width:32px;" /></td>
                <td style="line-height:15px;"><span style="font-size:9pt;font-style:italic;color:#999999;font-weight:normal;">Welcome Administrator</span><br />
            <span style="font-size:10pt;font-weight:bold;text-transform:uppercase;color:#FFFFFF;">${fullName}</span></td>
            </tr>
        </table>
        </div>
    <div style="border-top:1px solid #3B5A7C;font-size:10pt;padding-top:5px;">
        <ul style="">
            <c:set var="mnu" scope="session" value='<%=request.getParameter("menuHighlight")%>'/>
            <li><a href="NDCDashboard.htm"<c:if  test = "${mnu=='DASHBOARD'}"> class="sel"</c:if>><img src="images/home.gif" style="vertical-align:middle;" /> &nbsp;Dashboard</a></li>
            <li><a href="NDCApplicationList.htm"<c:if  test = "${mnu=='NDCAPPLICATIONS'}"> class="sel"</c:if>><img src="images/calendar.png" width="16" style="vertical-align:middle;" /> &nbsp;Application List</a></li>
            <li><a href="DisposedApplicationList.htm"<c:if  test = "${mnu=='DISPOSEDNDCAPPLICATIONS'}"> class="sel"</c:if>><img src="images/calendar.png" width="16" style="vertical-align:middle;" /> &nbsp;Disposed Application List</a></li>
            <li><a href="NDCPasswordChange.htm"<c:if  test = "${mnu=='CHANGEPASSWORD'}"> class="sel"</c:if>><img src="images/calendar.png" width="16" style="vertical-align:middle;" /> &nbsp;Change Password</a></li>
            <li><a href="logout.htm"><img src="images/logout.png" width="16" style="vertical-align:middle;" /> &nbsp;Logout</a></li>
        </ul>
    </div>
</div>
<div style="float:left;width:82%">
    <div style="height:110px;padding-left:10px;padding-top:10px;background:#3483c5">
        <img src="images/ndc_logo.png" style="margin-left:10px;" />
    </div>
    <div style="min-height:570px;background:#FFFFFF;margin:10px;border:1px solid #DADADA;padding:10px;">