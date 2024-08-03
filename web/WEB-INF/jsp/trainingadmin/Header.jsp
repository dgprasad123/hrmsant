<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="left_container">
    <div style="border-bottom:1px solid #1A2734;margin-top:10px;padding:5px 0px;">Training Calendar<br />
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
            <li><a href="InstitutionDashboard.htm"<c:if  test = "${mnu=='DASHBOARD'}"> class="sel"</c:if>><img src="images/home.gif" style="vertical-align:middle;" /> &nbsp;Dashboard</a></li>
            <!--<li><a href="NISGParticipants.htm"<c:if  test = "${mnu=='NISG'}"> class="sel"</c:if>><img src="images/faculty.png" style="vertical-align:middle;" /> &nbsp;NISG Participants</a></li>-->
            <li><a href="TrainingCalendarList.htm"<c:if  test = "${mnu=='CALENDAR'}"> class="sel"</c:if>><img src="images/calendar.png" width="16" style="vertical-align:middle;" /> &nbsp;Training Calendar</a></li>
            <!--<li><a href="TrainingArchive.htm"<c:if  test = "${mnu=='ARCHIVE'}"> class="sel"</c:if>><img src="images/archive-icon.png" width="16" style="vertical-align:middle;" /> &nbsp;Training Archives</a></li>-->
            <li><a href="ManageFaculties.htm"<c:if  test = "${mnu=='FACULTIES'}"> class="sel"</c:if>><img src="images/faculty.png" width="16" style="vertical-align:middle;" /> &nbsp;Manage Faculties</a></li>
            <li><a href="ManageSponsors.htm"<c:if  test = "${mnu=='SPONSORS'}"> class="sel"</c:if>><img src="images/sponsor.png" width="16" style="vertical-align:middle;" /> &nbsp;Manage Sponsors</a></li>
            <li><a href="ManageVenues.htm"<c:if  test = "${mnu=='VENUES'}"> class="sel"</c:if>><img src="images/venue_icon.png" width="16" style="vertical-align:middle;" /> &nbsp;Manage Venues</a></li>
            <li><a href="InstituteProfile.htm"<c:if  test = "${mnu=='PROFILE'}"> class="sel"</c:if>><img src="images/profile.png" width="16" style="vertical-align:middle;" /> &nbsp;Profile</a></li>
            <li><a href="logout.htm"><img src="images/logout.png" width="16" style="vertical-align:middle;" /> &nbsp;Logout</a></li>
        </ul>
    </div>
</div>
<div style="float:left;width:82%">
    <div style="height:110px;padding-left:10px;padding-top:10px;background:#3483c5">
        <img src="images/training_logo.png" style="margin-left:10px;" />
    </div>
    <div style="min-height:570px;background:#FFFFFF;margin:10px;border:1px solid #DADADA;padding:10px;">