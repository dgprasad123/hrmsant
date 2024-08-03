<%@ page contentType="text/html;charset=windows-1252"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>HRMS-Services</title>        



    </head>
    <body>
        <form action="getRollWiseLink.htm" commandName="RollwiseGroupInfoBean">

            <div align="center">


                <div style="width:100%;border:#5095ce solid 1px;background-color:#b1c242;">



                    <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;layout: fixed;color:#000000;font-weight:bold;">
                        <thead> </thead>
                        <tr>
                            <td width="17%" align="right">
                                Employee Name:                    
                            </td>
                            <td width="17%" style="text-transform:uppercase;" align="left">
                                <b> ${SelectedEmpObj.fullName} </b>
                            </td>
                            <td width="16%" align="right">
                                HRMS ID:                    
                            </td>
                            <td width="16%">
                                ${SelectedEmpObj.empId} 
                            </td>

                            <td width="17%" align="right">
                                Current Basic:                    
                            </td>
                            <td width="17%">
                                ${SelectedEmpObj.curBasic} 

                            </td>
                        </tr>

                        <tr>
                            <td align="right">Current Post: </td>
                            <td >
                                ${SelectedEmpObj.postname} &nbsp;
                            </td>
                            <td align="right">${SelectedEmpObj.acctType}  :</td>
                            <td><b style="text-transform:uppercase;"> ${SelectedEmpObj.gpfno} </b>
                                <c:if test="${SelectedEmpObj.ifgpfAssumed eq'Y'}">
                                    <b style="text-transform:uppercase; color:red;">(Assumed) </b> &nbsp;
                                </c:if>     
                            </td>

                            <td align="right">
                                PAY Fixed:                     
                            </td>
                            <td>
                                ${SelectedEmpObj.payCommission} 
                                <c:if test="${not empty SelectedEmpObj.payCommission}">
                                    th PAY
                                </c:if>
                            </td>


                        </tr>
                        <tr>
                            <td align="right">Current Cadre: </td>
                            <td align="left"><b> ${SelectedEmpObj.cadrename}   &nbsp;</b></td>
                            <td align="right">Current Status:</td>
                            <td><b> ${SelectedEmpObj.depstatus}&nbsp; </b> <b style="text-transform:uppercase; color:red;"> ${SelectedEmpObj.payheldupstatus} </b> </td>

                            <td align="right">
                                &nbsp;                   
                            </td>
                            <td>
                                &nbsp;
                            </td>


                        </tr>
                    </table>
                </div>
            </div>
            <div style="height:640px;padding: 5px;">

                <%-- <c:out value="${RollwiseGroupInfoBean.curOffice}"/>--%>



                <c:forEach var="grpListArr" items="${RollwiseGroupInfoBean.grpList}"> 
                    <%--<c:if test="${RollwiseGroupInfoBean.curOffice eq grpListArr.modOfc}">--%>


                    <div class="modGrp"> 
                        <span><div align="center"> ${grpListArr.modGrpName} </div></span>

                        <ul class="serviceList">

                            <c:forEach var="mplistarray" items="${grpListArr.moduleListArr}">

                                <c:if test="${RollwiseGroupInfoBean.curOffice eq mplistarray.modOfc}">
                                    <li class="modclass">
                                        <c:if test="${mplistarray.newWindow eq 'Y'}">
                                            <c:if test="${mplistarray.moduleId eq '74' || mplistarray.moduleId eq '322' }">
                                                <c:choose>

                                                    <c:when test="${isregular eq 'N' && ifreengaged eq 'Y' && ifretired eq 'Y'}">
                                                        <a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                                        </c:when>
                                                        <c:when test="${isregular eq 'N' && (ifreengaged ne 'Y' || ifreengaged eq null || ifreengaged eq '') && (ifretired ne 'Y' || ifreengaged eq null || ifreengaged eq '')}">

                                                    </c:when>
                                                    <c:when test="${isregular ne 'N'}">
                                                        <a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                                        </c:when>
                                                    </c:choose>                                                   
                                                </c:if>
                                                <c:if test="${mplistarray.moduleId ne '74' && mplistarray.moduleId ne '322' }">
                                                <a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${mplistarray.newWindow eq 'N'}">
                                            <a href="javascript:openWindow('${mplistarray.linkurl}','${mplistarray.moduleName}')">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                            </c:if>
                                    </li>
                                </c:if>
                                <c:if test="${RollwiseGroupInfoBean.curOffice ne mplistarray.modOfc}">

                                    <li class="modclass">
                                        <c:if test="${mplistarray.newWindow eq 'Y'}">
                                            <c:if test="${mplistarray.moduleId eq '74' || mplistarray.moduleId eq '322' }">
                                                <c:choose>

                                                    <c:when test="${isregular eq 'N' && ifreengaged eq 'Y' && ifretired eq 'Y'}">
                                                        <a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                                        </c:when>
                                                        <c:when test="${isregular eq 'N' && (ifreengaged ne 'Y' || ifreengaged eq null || ifreengaged eq '') && (ifretired ne 'Y' || ifreengaged eq null || ifreengaged eq '')}">

                                                    </c:when>
                                                    <c:when test="${isregular ne 'N'}">
                                                        <a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                                        </c:when>
                                                    </c:choose>                                                   
                                                </c:if>
                                                <c:if test="${mplistarray.moduleId ne '74' && mplistarray.moduleId ne '322' }">
                                                <a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a>
                                                </c:if>
                                                <%--<a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> --%>
                                            </c:if>
                                            <c:if test="${mplistarray.newWindow eq 'N'}">
                                            <a href="javascript:openWindow('${mplistarray.linkurl}','${mplistarray.moduleName}')">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                            </c:if>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                        <div align="right" class="pagingServicList">
                            <ul style="margin:0;padding:0;list-style:none;float:right;">                        										

                            </ul>						
                        </div>
                    </div> 


                </c:forEach>


            </div>
            <!--
                    <div align="center">
                        <div style="height:30px;width:100%; border: #5095ce solid 1px;background-color:#e5f0c9;font-weight:bold;color:#FFFFFF;vertical-align: middle;padding:10px 0px 0px 0px; ">
                            <ul id="modgrouplistpaging" style="margin:0;padding:0;list-style:none;float:right;font-size: 18px;margin-right: 30px;">                        										
                                
                            </ul>
                        </div>
                    </div>
            -->
        </form>

    </body>
</html>