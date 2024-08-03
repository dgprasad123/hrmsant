<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>        
        <title>:: HRMS, Government of Odisha ::</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/demo.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/debug.js"></script>
        <style type="text/css">
            .pagingServicList{
                background-color: #e5f0c9;			
                height:23px;
                background-repeat:repeat-x;
                text-align:center;
                padding-top:5px;
                padding-right: 5px;
                vertical-align:middle;
                color:#000000;
                font-family:Arial, Helvetica, sans-serif;
                font-weight:bold;
                font-size:0.9em;        
                text-transform:uppercase;
            }
            .modGrp{
                width: 30%;
                margin-bottom: 5px;
                margin-right: 5px;
                border:#5095ce solid 1px;
                float: left;
            }
            .modGrp span div{
                background-color: #e5f0c9;		
                height:28px;
                background-repeat:repeat-x;
                text-align:center;
                padding-top:5px;
                vertical-align:middle;
                color:#000000;
                font-family:Arial, Helvetica, sans-serif;
                font-weight:bold;
                font-size:0.9em;        
                text-transform:uppercase;       
            }
            .serviceList{
                list-style:none;
                display: block;
                padding:0.5em 0em 0.5em 1em;
                margin:0px;
                height:117px;
                min-height: 117px;
            }
            .serviceList li{
                margin-bottom:5px;
            }
            .serviceList li a{
                text-decoration:none;
                color:#000000;
            }
            .serviceList li a:hover{
                color:#FF6C00;
                font-weight:normal;
            }
        </style>
        <script type="text/javascript">
            var globalId = "";
            $(document).ready(function() {
                var divlen = $('.modGrp').length;
                /*Add Pagging in Module Group*/

                for (i = 1; i <= divlen; i++) {
                    $("#modgrouplistpaging").append('<li style="float:left;margin-left:2px;">' + i + '<\/li>');
                }
                /*Add Pagging in Module Group*/
                i = 0;
                $('.modGrp').each(function() {
                    i++;

                    insideUL = $(this).children('.serviceList').children();
                    var moduleListLen = $(insideUL).length;
                    var noofpage = moduleListLen / 6;
                    if (moduleListLen % 6 > 0 && noofpage > 1) {
                        noofpage++;
                    }
                    if (noofpage > 1) {
                        for (j = 1; j < noofpage; j++) {
                            $(this).children('.pagingServicList').children().append('<li style="float:left;margin-left:2px;" onclick="displayNextPage(this)"><a href="javascript:displayNextPage(this)">' + j + '<\/a><\/li>');
                        }
                    }
                    j = 0;
                    $(insideUL).each(function() {
                        j++;
                        if (j > 6) {
                            $(this).hide();
                        }
                    });

                });
            })
            function openWindow(linkurl, modname) {
                $("#win").window("open");
                $("#win").window("setTitle", modname);
                $("#winfram").attr("src", linkurl + globalId);
            }
            function displayNextPage(me) {

                var pageNo = parseInt($(me).text().trim());
                var start = (pageNo - 1) * 6;
                var end = pageNo * 6;
                var pagingServicList = $(me).parent().parent();
                var lilength = $(pagingServicList).siblings(".serviceList").children('li');
                var lilengthi = $(lilength).length;
                for (i = 0; i < lilengthi; i++) {
                    if (i <= start || i > end) {
                        $(lilength[i]).hide();
                    } else {
                        $(lilength[i]).show();
                    }
                }


                //$("li.start").siblings()

            }
        </script>
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
            <div style="height:640px;padding: 5px;overflow: auto">
                <c:if test="${isForeignbody eq 'Y'}">
                    <c:forEach var="grpListArr" items="${RollwiseGroupInfoBean.grpList}">
                        <c:if test="${grpListArr.modGrpId eq '029' || grpListArr.modGrpId eq '001'}">
                            <div class="modGrp"> 
                                <span><div align="center"> ${grpListArr.modGrpName} </div></span>
                                <ul class="serviceList">
                                    <c:forEach var="mplistarray" items="${grpListArr.moduleListArr}">                                         
                                        <li class="modclass">
                                            <c:if test="${mplistarray.newWindow eq 'Y'}">
                                                <a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                                </c:if>
                                                <c:if test="${mplistarray.newWindow eq 'N'}">
                                                <a href="javascript:openWindow('${mplistarray.linkurl}','${mplistarray.moduleName}')">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                                </c:if> 
                                        </li>
                                    </c:forEach>
                                </ul>
                                <div align="right" class="pagingServicList">
                                    <ul style="margin:0;padding:0;list-style:none;float:right;">                        										

                                    </ul>						
                                </div>
                            </div> 
                        </c:if>

                    </c:forEach>
                </c:if>
                <c:if test="${isForeignbody ne 'Y'}">
                    <c:forEach var="grpListArr" items="${RollwiseGroupInfoBean.grpList}">
                        <div class="modGrp"> 
                            <span><div align="center"> ${grpListArr.modGrpName} </div></span>
                            <ul class="serviceList">
                                <c:forEach var="mplistarray" items="${grpListArr.moduleListArr}">                                         
                                    <li class="modclass">
                                        <c:if test="${mplistarray.newWindow eq 'Y'}">
                                            <a href="${mplistarray.linkurl}" target="_blank">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                            </c:if>
                                            <c:if test="${mplistarray.newWindow eq 'N'}">
                                            <a href="javascript:openWindow('${mplistarray.linkurl}','${mplistarray.moduleName}')">${mplistarray.moduleName} <c:if test = "${mplistarray.isNew eq 'Y'}"><img src="images/new_icon.gif" height="25" style="vertical-align:top;margin-top:-5px;" /></c:if></a> 
                                            </c:if> 
                                    </li>
                                </c:forEach>
                            </ul>
                            <div align="right" class="pagingServicList">
                                <ul style="margin:0;padding:0;list-style:none;float:right;">                        										

                                </ul>						
                            </div>
                        </div> 
                    </c:forEach>
                </c:if>

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
        <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window',fit:true" style="width:1200px;height:500px;padding:5px;">
            <iframe id="winfram" frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%"></iframe>
        </div>
    </body>
</html>