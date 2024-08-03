<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% String url = "";
    String myempId = "";
    String attachId = "";
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
    int i = 0;
%>
<%!int mypage = 0;
    boolean payrecordprinted = false;
    int numofrowsleft = 0;
%>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Service Book</title>

        <style>
            .sr-record{
                text-align:center;
                font-size: 14;
                border: 2px solid black;
            }
            .sr-record-data{
                text-align:center;
                font-size: 12px !important;          
                border: 2px solid black;
            }
            .sr-record1{
                text-align:center;
                font-size: 12;
                border: 1px solid black;
            }
        </style>

    </head>
    <body>        
        <form:form action="employeeProfile.htm" commandName="employeeProfile"  method="GET" target="_blank">

            <div align="center" style="overflow-x:hidden">

                <div  style="width: 100%">

                    <div>
                        <CENTER>
                            <table width="1040" height="50" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="940" >&nbsp;</td>
                                </tr>
                            </table>
                            <table width="1040" border="1" cellpadding="0" cellspacing="0" bordercolor="#000099">

                                <tr>
                                    <td width="1040" height="1600">
                                        <table width="1040" height="1013" border="0" cellpadding="0" cellspacing="0">

                                            <tr>
                                                <td colspan="2" height="70" align="center" valign="middle">&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="154" align="center" valign="middle">&nbsp;<img src='<%=basePath%>/images/odgovt.gif' height="144" width="165"/></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="67" align="center" valign="bottom" style="font-family:Arial, Helvetica, sans-serif;font-size:40px;color:#000099">
                                                    <strong>HUMAN RESOURCES MANAGEMENT SYSTEM</strong>    	</td>
                                            </tr>
                                            <tr>
                                                <td  height="51" colspan="2" align="center" valign="top" style="font-family:Arial, Helvetica, sans-serif;font-size:40px;color:#000099"><strong>Government of Odisha</strong></td>
                                            </tr>

                                            <tr> 
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>

                                            <tr>
                                                <td height="180" colspan="2" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:55px;float:right;font-style:normal;color:#000099">&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td height="40" colspan="2" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:55px;font-style:normal;color:#000099"><strong>SERVICE BOOK </strong></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="40" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:55px;color:#000099"><strong>OF</strong></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="150" align="center">
                                                    <img src="displayemployeeprofilephoto.htm" id="sbUserPhoto" onerror="callNoImage()" height="150px" width="140px" border="2"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="40" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:45px;font-style:normal;color:#000099">
                                                    <strong><c:out value="${employeeProfile.empName}"/></strong>
                                                </td>	
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="30" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                    <strong>
                                                        <c:out value="${employeeProfile.empCadre}"/>&nbsp;
                                                        <c:out value="${employeeProfile.empAllotmentYear}"/>&nbsp;
                                                    </strong>

                                                </td>
                                            </tr>
                                            <c:if test="${not empty cadreId}">
                                                <tr>
                                                    <td width="515" height="30" align="right" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                        <strong>
                                                            CADRE ID :
                                                        </strong>&nbsp;
                                                    </td>
                                                    <td width="515" height="30" align="left" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                        &nbsp;<c:out value="${employeeProfile.cadreId}"/>
                                                    </td>
                                                </tr>
                                            </c:if>
                                            <tr height="35px">
                                                <td width="515" height="31" align="right" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">

                                                    <strong>
                                                        HRMS ID :
                                                    </strong>

                                                </td>
                                                <td width="515" height="31" align="left" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">

                                                    &nbsp;<c:out value="${employeeProfile.empid}"/>
                                                    &nbsp;
                                                </td>
                                            </tr>

                                            <tr height="35px">
                                                <td width="515" height="31" align="right" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">

                                                    <strong>
                                                        <c:out value="${employeeProfile.accttype}"/> NO :
                                                    </strong>

                                                </td>
                                                <td width="515" height="31" align="left" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">

                                                    &nbsp;<c:out value="${employeeProfile.gpfno}"/>
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr height="35px">
                                                <td width="515" height="31" align="right" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                    <c:if test="${not empty gistype}">
                                                        <strong>
                                                            <c:out value="${employeeProfile.gistype}"/> NO. :
                                                        </strong>
                                                    </c:if>&nbsp;	
                                                </td>
                                                <td width="515" height="31" align="left" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                    <c:if test="${not empty gis}">   
                                                        &nbsp; <c:out value="${employeeProfile.gis}"/>
                                                    </c:if>&nbsp;
                                                </td>
                                            </tr>
                                            <c:if test="${empty empPhotoPath}">
                                                <tr>
                                                    <td colspan="2" height="100" align="center">
                                                        &nbsp;
                                                    </td>
                                                </tr>
                                            </c:if>
                                            <c:if test="${empty empCadre}">

                                                <tr>
                                                    <td colspan="2" height="50" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                        &nbsp;
                                                    </td>
                                                </tr>
                                            </c:if>
                                            <c:if test="${empty cadreId}">

                                                <tr>
                                                    <td colspan="2" height="30" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                        &nbsp;
                                                    </td>
                                                </tr>
                                            </c:if>

                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="25" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="90" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:30px;font-style:italic;color:#000099">
                                                    <img src='<%=basePath%>/images/file.JPG' height="80" width="90"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="25" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:15px;color:#000099">Developed By</td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="25"  align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:15px;color:#000099"> Centre for Modernizing Government Initiative(CMGI)</td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="25" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:8px;color:#000099;text-transform: uppercase;">
                                                    DATE OF PRINTING: <c:out value="${employeeProfile.serverDate}"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                            <table width="1040"  style="left: 18px;">
                                <tr>
                                    <td class="printData" style="text-align:left">
                                        GOVERNMENT OF ODISHA
                                    </td>
                                    <td class="printFooter" style="text-align:center;text-transform: uppercase">&nbsp;

                                    </td>
                                    <td colspan="2" class="printData" style="text-align:right;">	
                                        SERVICE BOOK OF <c:out value="${employeeProfile.empName}"/>
                                        <c:if test="${not empty gpfno}">
                                            <c:if test="${not empty empCadre}">
                                                (<c:out value="${employeeProfile.gpfno}"/>),
                                            </c:if>
                                            <c:if test="${not empty empCadre}">
                                                (<c:out value="${employeeProfile.gpfno}"/>)
                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <hr/>
                                    </td>
                                </tr>
                            </table>
                            <table border="1" cellpadding="0" cellspacing="0" width="1040" height="1600" style="left: 18px;">
                                <tr>
                                    <td colspan="3" valign="middle" align="center">                                        
                                        <img src='displayemployeefistpagesb.htm' border="2" id="imgid" onerror="callNoSBPage()" width="750px" height="1200px"/>
                                    </td>
                                </tr>        
                            </table>
                            <%mypage = mypage + 1;%>
                            <table width="1040" style="left: 18px;">
                                <tr>
                                    <td colspan="4" height="5px">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="printData" >&nbsp;

                                    </td>
                                    <td class="printFooter" style="text-align:center">&nbsp;</td>
                                    <td class="printData" style="text-align:right; text-transform: uppercase;">Page:<%=mypage%></td>
                                </tr>
                            </table>   
                            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                            <table width="1040"  style="left: 18px;">
                                <tr>
                                    <td class="printData" style="text-align:left">
                                        GOVERNMENT OF ODISHA
                                    </td>
                                    <td class="printFooter" style="text-align:center;text-transform: uppercase">&nbsp;

                                    </td>
                                    <td colspan="2" class="printData" style="text-align:right;">	
                                        SERVICE BOOK OF <c:out value="${employeeProfile.empName}"/>
                                        <c:if test="${not empty gpfno}">
                                            <c:if test="${not empty empCadre}">
                                                (<c:out value="${employeeProfile.gpfno}"/>),
                                            </c:if>
                                            <c:if test="${not empty empCadre}">
                                                (<c:out value="${employeeProfile.gpfno}"/>)
                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <hr/>
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="100" border="1" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">NAME: </td>
                                    <td colspan="3" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;<c:out value="${employeeProfile.empName}"/></td>
                                </tr>
                                <tr>
                                    <td width="250" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">
                                        <c:out value="${employeeProfile.accttype}"/> NO:
                                    </td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;<c:out value="${employeeProfile.gpfno}"/></td>
                                    <td width="250" class="printLabel" style="text-align:left; text-transform:uppercase;font-size:18px">HRMS ID:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;<c:out value="${employeeProfile.empid}"/></td>
                                </tr>
                            </table>
                            <table width="1040" height="150" border="1" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="250" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Present Address:</br>(Residence)</td>
                                    <td width="750" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;

                                        <%--<c:if test="${not empty employeeProfile.residenceAdd}">
                                            <c:out value="${employeeProfile.residenceAdd}"/>
                                        </c:if>--%>
                                        <c:if test="${not empty presentaddress}">
                                            <c:out value="${presentaddress}"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="100" border="1" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="340" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">DATE OF BIRTH <br>BY CHRISTIAN ERA AS <br>NEARLY AS CAN <br>BE ASCERTAINED : </td>
                                    <td width="700" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;

                                        <c:if test="${not empty employeeProfile.dob}">
                                            <c:out value="${employeeProfile.dob}"/>
                                        </c:if>
                                        <c:if test="${not empty employeeProfile.dobText}">
                                            (<c:out value="${employeeProfile.dobText}"/>)
                                        </c:if>

                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Employee Educational Details: </td>
                                </tr>
                            </table>
                            <c:forEach items="${educations}" var="education">
                                <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                    <thead> </thead>
                                    <tr>
                                        <td class="printLabel" style="text-align:center;text-transform:uppercase;font-size:20px ">&nbsp;
                                            ${education.qualification}		
                                        </td>	
                                    </tr>
                                </table>


                                <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                    <thead> </thead>
                                    <tr>
                                        <td width="300" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px ">Year of Passing</td>
                                        <td width="100" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">
                                            &nbsp;
                                            <c:if test="${not empty education.yearofpass}">
                                                ${education.yearofpass}
                                            </c:if>	
                                        </td>
                                        <td width="160" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Faculty</td>
                                        <td width="170" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                            ${education.faculty}

                                        </td>
                                        <td width="200" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Degree/<br>Certificate</td>
                                        <td width="100" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                            ${education.degree}
                                        </td>
                                    </tr>
                                </table>

                                <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">	
                                    <thead> </thead>
                                    <tr>
                                        <td width="300" class="printLabel"  style="text-align:left;text-transform:uppercase;font-size:18px">Subject</td>
                                        <td width="740" class="printData"  style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                            <c:if test="${not empty education.subject}">
                                                ${education.subject}
                                            </c:if>	
                                        </td>
                                    </tr>	 
                                </table>

                                <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                    <thead> </thead>
                                    <tr>
                                        <td width="300" class="printLabel"  style="text-align:left;text-transform:uppercase;font-size:18px">Board/University</td>
                                        <td width="740" class="printData"  style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                            ${education.board}
                                        </td>
                                    </tr>	
                                </table>
                            </c:forEach>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="300" class="printLabel" style="text-align:left;font-size:18px">HEIGHT(in cm):</td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        <c:if test="${employeeProfile.height gt 0}">
                                            ${employeeProfile.height}
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="100" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="300" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Personal <br>Identification Mark : </td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        ${employeeProfile.idmark}
                                    </td>
                                </tr>
                            </table> 

                            <table width="1040" height="130" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="300" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Father's name <br>and residence : </td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">
                                        <c:forEach items="${familyRel}" var="familyRelation">
                                            <c:if test="${familyRelation.relation eq 'FATHER'}">
                                                <b>${familyRelation.fatherName}</b><br>
                                            </c:if>
                                        </c:forEach>
                                        <%--<c:forEach items="${address}" var="premanentAddr">
                                            ${premanentAddr.address}
                                        </c:forEach>&nbsp;--%>
                                        <c:if test="${not empty permanentaddress}">
                                            <c:out value="${permanentaddress}"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>

                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">		
                                <thead> </thead>
                                <tr height="50" >
                                    <td width="300" class="printLabel" style="text-align:left; font-size:18px">DATE OF ENTRY IN <BR> GOVERMENT SERVICE :</td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;

                                        ${employeeProfile.doeGov}
                                        <c:if test="${not empty employeeProfile.entryGovDateText}">
                                            (${employeeProfile.entryGovDateText})
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="80" border="1" cellpadding="0" cellspacing="0">		
                                <thead> </thead>
                                <tr height="80" >
                                    <td width="300" class="printLabel" style="text-align:left; font-size:18px">DATE OF ENTRY IN THE<BR>SERVICE FOR WHICH<BR>SERVICE BOOK CREATED:</td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        ${employeeProfile.joindategoo}
                                        (${employeeProfile.joinDateText})
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">		
                                <thead> </thead>
                                <tr height="50" >
                                    <td width="520" class="printLabel" style="text-align:left; font-size:18px">DECLARATION OF HOME TOWN:</td>
                                    <td width="520" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        ${employeeProfile.homeTown}
                                    </td>
                                </tr>
                            </table>
                            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                            <table width="1040"  style="left: 18px;">
                                <tr>
                                    <td class="printData" style="text-align:left">
                                        GOVERNMENT OF ODISHA
                                    </td>
                                    <td class="printFooter" style="text-align:center;text-transform: uppercase">&nbsp;

                                    </td>
                                    <td colspan="2" class="printData" style="text-align:right;">	
                                        SERVICE BOOK OF <c:out value="${employeeProfile.empName}"/>
                                        <c:if test="${not empty gpfno}">
                                            <c:if test="${not empty empCadre}">
                                                (<c:out value="${employeeProfile.gpfno}"/>),
                                            </c:if>
                                            <c:if test="${not empty empCadre}">
                                                (<c:out value="${employeeProfile.gpfno}"/>)
                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <hr/>
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="500" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">DATE OF SUPERANNUATION: </td>
                                    <td width="500" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;${employeeProfile.dor}</td>
                                </tr>
                            </table>
                            <table width="1040" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr height="50" >
                                    <td width="250" class="printLabel" style="text-align:left; text-transform:uppercase;font-size:18px">Category:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        <c:if test="${not empty employeeProfile.category}">
                                            ${employeeProfile.category}
                                        </c:if>
                                    </td>
                                    <td width="250" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Marital Status:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        ${employeeProfile.maritalStatus}
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="280" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Cell Phone:</td>
                                    <td width="760" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp; ${employeeProfile.mobile}</td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Identity of the Employee:</td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="220" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Type of Identification</td>
                                    <td width="240" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">No.</td>
                                    <td width="280" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Place of Issue</td>
                                    <td width="150" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Date of Issue</td>
                                    <td width="150" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Date of Expiry</td>
                                </tr>
                                <c:forEach items="${identity}" var="identity">
                                    <tr>
                                        <td width="220" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${identity.identityDesc}</td>
                                        <td width="240" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${identity.identityNo}</td>
                                        <td width="280" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${identity.placeOfIssue}</td>
                                        <td width="150" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${identity.issueDate}</td>
                                        <td width="150" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${identity.expiryDate}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Family of the Employee:</td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr >
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Relation Type</td>
                                    <td width="350" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Name</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Is Alive?</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">DOB</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Marital Status</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Age</td>
                                </tr>
                                <c:forEach items="${familyRel}" var="family">
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${family.relation}</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${family.initials} ${family.fname} ${family.mname} ${family.lname}</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${family.ifalive}</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${family.dob}</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${family.marital}</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${family.age}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Nominee Detail:</td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">                                
                                <tr>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Relation Type</td>
                                    <td width="350" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Name</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Is Alive?</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">DOB</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Marital Status</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Age</td>
                                </tr>
                                <c:forEach items="${nomineedetail}" var="ndetail">
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${ndetail.relation}</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${ndetail.initials} ${ndetail.fname} ${ndetail.mname} ${ndetail.lname}</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${ndetail.ifalive}</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${ndetail.dob}</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${ndetail.marital}</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${ndetail.age}</td>
                                    </tr>
                                </c:forEach>                                
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">                                
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Employee Reservation Category:</td>
                                </tr>
                            </table>   
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">                                
                                <tr>
                                    <td width="740" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Is Employee under any Reservation Category?</td>                                                                                                                                            
                                    <td width="300" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp; <c:out default="" escapeXml="true" value="${not empty employeeProfile.ifReservation && employeeProfile.ifReservation eq 'Y'? 'Yes' : 'No'}" /></td>
                                </tr>
                            </table> 
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">                                
                                <tr>
                                    <td width="740" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Reservation Category Under Which Employed </td>
                                    <td width="300" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;
                                        <c:if test="${not empty employeeProfile.category}">
                                            ${employeeProfile.category}
                                        </c:if>	
                                    </td>	
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">                                
                                <tr>
                                    <td width="740" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Is Employee under Rehabilitation Assistance Scheme?</td>
                                    <td width="300" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp; <c:out default="" escapeXml="true" value="${not empty employeeProfile.ifRehabiltation && employeeProfile.ifRehabiltation eq 'Y'? 'Yes' : 'No'}" /></td>
                                </tr>
                            </table>
                            <%
                                mypage++;
                            %>
                            <table width="1040"   style="left: 18px;">
                                <thead> </thead>
                                <tr>
                                    <td colspan="4" height="5px">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="printData" >
                                        &nbsp;
                                    </td>
                                    <td class="printFooter" style="text-align:center">&nbsp;</td>
                                    <td class="printData" style="text-align:right; text-transform: uppercase;">Page:<%=mypage%></td>
                                </tr>
                            </table>
                        </center>
                    </div>                   
                    <div style="page-break-before: always;">
                        <table width="1040" cellspacing="0" cellpadding="0" border="1">
                            <thead>
                                <tr class="alternateTD">
                                    <th width="300" style="text-align:center;font-size:16px;" class="printLabelHeader">Post/Cadre/Scale of Pay</th>
                                    <th width="120" style="text-align:center;font-size:16px;" class="printLabelHeader">Pay</th>
                                    <th width="90" style="text-align:center;font-size:16px;" class="printLabelHeader">WEF</th>
                                    <th width="450" style="text-align:center;font-size:16px;border-right:1px solid #666666;" colspan="2" class="printLabelHeader">Entry in the Service Book </th>
                                </tr>
                            </thead>
                            <c:forEach items="${esb.empsbrecord}" var="servicehistory" varStatus="cnt">

                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">${cnt.index + 1}</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                <c:if test="${not empty servicehistory.spn}"> ${servicehistory.spn} </br> </c:if>  
                                                <c:if test="${not empty servicehistory.cadre}"> ${servicehistory.cadre} </br>  </c:if>
                                                <c:if test="${not empty servicehistory.payscale}">
                                                    ${servicehistory.payscale} 
                                                </c:if>
                                            </span>


                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        <c:if test="${not empty servicehistory.pay}">
                                            PAY: Rs.${servicehistory.pay}/- 
                                        </c:if>

                                    </td>
                                    <td width="130" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        ${servicehistory.wefChange}
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> ${servicehistory.category}</u></b>  <br/>
                                            <span id="sblangid${cnt.index + 1}">${servicehistory.sbdescription}</span>
                                            &nbsp;	

                                            <c:if test="${not empty servicehistory.moduleNote}">
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> ${servicehistory.moduleNote}
                                                </p>

                                            </c:if>
                                        </div>
                                    </td>
                                </tr>

                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ${servicehistory.entauth} ON ${servicehistory.doe}
                                    </td>
                                </tr>

                            </c:forEach>
                        </table>

                    </div>
                </div>
            </div>
            <div style="page-break-before: always;">
                <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                <div align="center" style="overflow-x:hidden">
                    <div  style="width: 100%">
                        <div>
                            <CENTER>
                                <div class="container-fluid">

                                    <table table width="1040" border="0" cellpadding="0" cellspacing="0" class="leaveaccttbl">
                                        <thead style="border: 2px solid black;">
                                            <tr style="height:45px">
                                                <th colspan="12"  style="text-align:center;font-size: 20;">
                                        <h4>PAY IN SERVICE RECORD</h4>&nbsp;&nbsp;(STATEMENT OF EMOLUMENTS)
                                        </th>
                                        </tr>  
                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class=" sr-record" width="8%"><b>Particulars</b></th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <th class=" sr-record" width="8%"><b>From<br/>${daList.wefdate}</b></th>
                                                    </c:forEach>
                                        </tr>
                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class=" sr-record" width="8%">Pay</th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <td class="sr-record1" width="8%">${daList.pay}</td>
                                            </c:forEach>
                                        </tr>
                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class=" sr-record" width="10%">DA</th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <td class="sr-record1" width="10%">${daList.da}</td>
                                            </c:forEach>

                                        </tr>
                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class="sr-record" width="10%">Additional DA</th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <td class="sr-record1" width="10%">${daList.ada}</td>
                                            </c:forEach>
                                        </tr>

                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class=" sr-record" width="10%">Special Pay</th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <td class="sr-record1" width="10%">${daList.specialPay}</td>
                                            </c:forEach>
                                        </tr>
                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class=" sr-record" width="10%">Personal Pay</th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <td class="sr-record1" width="10%">${daList.personalPay}</td>
                                            </c:forEach>
                                        </tr>
                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class=" sr-record" width="10%">Other pay</th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <td class="sr-record1" width="10%">${daList.othEmoulment}</td>
                                            </c:forEach>
                                        </tr>
                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class=" sr-record" width="10%">Leave Pay</th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <td class="sr-record1" width="10%">${daList.leaveSal}</td>
                                            </c:forEach>
                                        </tr>
                                        <tr style="height:45px;border: 2px solid black;">
                                            <th class=" sr-record" width="10%">Total Pay</th>
                                                <c:forEach items="${payInServicelist}" var="daList" >
                                                <td class="sr-record1" width="10%">${daList.totPay}</td>
                                            </c:forEach>
                                        </tr>


                                        </tr>
                                        </thead>
                                        <tbody style="border: 2px solid black;">
                                            <c:forEach items="${servicerecordList}" var="srlist" varStatus="cnt">
                                                <tr style="height:90px;border: 2px black;">  
                                                    <td class="printData sr-record-data"  width="10%">${cnt.index+1}</td>
                                                    <td class="printData sr-record-data"  width="10%">${srlist.station}</td>

                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </CENTER>

                        </div>
                    </div>
                </div>
            </div>

            <div style="page-break-before: always;">
                <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                <div align="center" style="overflow-x:hidden">
                    <div  style="width: 100%">
                        <div>
                            <CENTER>
                                <div class="container-fluid">

                                    <table table width="1040" border="1" cellpadding="0" cellspacing="0" class="leaveaccttbl" bordercolor="#000099">
                                        <thead style="border: 2px solid black;">
                                            <tr style="height:45px">
                                                <th colspan="6" class="" style="text-align:center;font-size: 16;">
                                                    Service Record
                                                </th>
                                            </tr>  
                                            <tr style="height:45px;border: 2px solid black;">
                                                <th class=" sr-record" width="10%">SL NO</th>
                                                <th class=" sr-record"  width="10%">Station</th>
                                                <th class=" sr-record"  width="30%">SUBSTANTIVE APPOINTMENT/SERVICE DETAILS </th>
                                                <th class=" sr-record"  width="10%">From Date</th>
                                                <th class=" sr-record" width="10%">To date</th>                                              
                                            </tr>
                                        </thead>
                                        <tbody style="border: 2px solid black;">
                                            <c:forEach items="${servicerecordList}" var="srlist" varStatus="cnt">
                                                <tr style="height:90px;border: 2px black;">  
                                                    <td class="printData sr-record-data"  width="10%">${cnt.index+1}</td>
                                                    <td class="printData sr-record-data"  width="10%">${srlist.station}</td>
                                                    <td class="printData sr-record-data"  width="10%">${srlist.details}</td>
                                                    <td class="printData sr-record-data"  width="10%">${srlist.fromDate}</td>
                                                    <td class="printData sr-record-data" width="10%">${srlist.toDate}</td>                                                                             
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </CENTER>

                        </div>
                    </div>
                </div>
            </div>                   



            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
            <div align="center" style="overflow-x:hidden">

                <div  style="width: 100%">

                    <div>
                        <CENTER>
                            <table width="1040" border="1" cellpadding="0" cellspacing="0" class="leaveaccttbl">
                                <thead>
                                    <tr>
                                        <td colspan="11" height="35">${eap.leaveType} ACCOUNT </td>
                                    </tr>
                                    <tr>
                                        <td height="100" colspan="2">Period of Account </td>
                                        <td width="8%" rowspan="2"><p align="center">No of </br>Complete</br> Months </p></td>
                                        <td width="8%" rowspan="2">Leave Credit in Days </td>
                                        <td width="8%" rowspan="2">Total</br> No.of </br>Leave</br>(EOL)</br> Availed </td>
                                        <td width="8%" rowspan="2">EL to be Deducted </td>
                                        <td width="8%" rowspan="2">Balance</br> on </br>return</br> from </br>Leave </td>
                                        <td width="30%" colspan="3">Leave Availed </td>
                                        <td width="6%" rowspan="2">Balance Leave </td>
                                    </tr>
                                    <tr>
                                        <td width="12%" height="23" >From</td>
                                        <td width="12%">To</td>
                                        <td width="12%">From</td>
                                        <td width="12%">To</td>
                                        <td width="6%">Total</td>
                                    </tr>
                                </thead>

                                <c:if test="${eap.leaveOBalDate != ''}">    

                                    <tr>
                                        <td class="printLabel" colspan="6" style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OPENING BALANCE ON DATE &nbsp;&nbsp;${eap.leaveOBalDate}&nbsp;${eap.fnan}</td>
                                        <td width="8%" class="printLabel">&nbsp;${eap.leaveOBal}</td>
                                        <td width="36%" colspan="4" class="printData">&nbsp;</td>
                                    </tr>

                                </c:if>

                                <c:forEach items="${eap.creditLvList}" var="creditDetails">                
                                    <c:if test="${creditDetails.creditType eq 'U'}">            
                                        <tr>
                                            <td colspan="2" class="printData" style="text-align:center;font-size: 14;">
                                                UNAVAILED JOINING TIME
                                            </td>
                                            <td width="8%" rowspan="2" class="printData">&nbsp;${creditDetails.compMonths}</td>
                                            <td width="8%" rowspan="2" class="printData">&nbsp;${creditDetails.leaveCredited}</td>
                                            <td width="8%" rowspan="2" class="printData">&nbsp; ${creditDetails.totEOLNumber}</td>
                                            <td width="8%" rowspan="2" class="printData">&nbsp;${creditDetails.leaveDeduct}</td>
                                            <td width="8%" rowspan="2" class="printData">&nbsp;${creditDetails.creditBalShow}</td>
                                            <td width="36%" rowspan="2" class="printData">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td  width="12%" class="printData">&nbsp;${creditDetails.fromDate}</td>
                                            <td  width="12%" class="printData">&nbsp;${creditDetails.toDate}</td>
                                        </tr>
                                    </c:if>                    
                                    <c:if test="${creditDetails.creditType eq 'G'}">                    
                                        <tr>
                                            <td width="11%" class="printData">&nbsp;${creditDetails.fromDate}</td>
                                            <td width="12%" class="printData">&nbsp;${creditDetails.toDate}</td>
                                            <td width="8%" class="printData">&nbsp;${creditDetails.compMonths}</td>
                                            <td width="8%" class="printData">&nbsp;${creditDetails.leaveCredited}</td>
                                            <td width="8%" class="printData">&nbsp;${creditDetails.totEOLNumber}</td>
                                            <td width="8%" class="printData">&nbsp;${creditDetails.leaveDeduct}</td>
                                            <td width="8%" class="printData">&nbsp;${creditDetails.creditBalShow}</td>

                                            <td class="printData" colspan="4">&nbsp;</td>
                                        </tr>
                                    </c:if>
                                    <c:forEach items="${creditDetails.availedLeave}" var="aLeave">
                                        <tr>
                                            <td colspan="7" width="62%">&nbsp;</td>
                                            <td width="14%" class="printData">${aLeave.fromdate}</td>
                                            <td width="14%" class="printData">${aLeave.todate}</td>
                                            <td width="5%" class="printData">${aLeave.totalNoofdays}</td>
                                            <td width="5%" class="printData">${aLeave.balanceLeave}</td>
                                        </tr>
                                    </c:forEach>
                                    <c:forEach items="${creditDetails.surrenderedLeave}" var="surrenderedLeave">
                                        <tr>
                                            <td colspan="7" width="62%">&nbsp;</td>
                                            <td width="14%" class="printData">SURRENDERED</td>
                                            <td width="14%" class="printData">LEAVE</td>
                                            <td width="5%" class="printData">${surrenderedLeave.surrenderDays}</td>
                                            <td width="5%" class="printData">${surrenderedLeave.balanceLeave}</td>
                                        </tr>
                                    </c:forEach>    
                                </c:forEach>
                            </table>
                        </CENTER>
                    </div>
                </div>
            </div>                                  

            <div style="page-break-before: always;">
                <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                <div align="center" style="overflow-x:hidden">
                    <div  style="width: 100%">
                        <div>
                            <CENTER>
                                <div class="container-fluid">

                                    <table table width="1040" border="1" cellpadding="0" cellspacing="0" class="leaveaccttbl" bordercolor="#000099">
                                        <thead style="border: 3px solid black;">
                                            <tr style="height:45px">
                                                <th colspan="6" class="printLabel" style="text-align:center;font-size: 16;">
                                                    INSURANCE PASS BOOK
                                                </th>
                                            </tr>  
                                            <tr style="height:45px;border: 3px solid black;">
                                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="10%">SL NO</th>
                                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="10%">Instrument<br/> No.</th>
                                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="10%">Treasury <br/>Voucher No.</th>
                                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="10%">Amount<br/>Deposited</th>
                                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="30%">Name of the Treasury & Date of Deposit</th>
                                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="30%">Deposited By</th>
                                            </tr>
                                        </thead>
                                        <tbody style="border: 3px solid black;">
                                            <c:forEach items="${gisList}" var="gis" varStatus="cnt">
                                                <tr style="height:90px;border: 3px black;">    
                                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;" width="10%">${cnt.index+1}</td>
                                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;" width="10%">${gis.instru}</td>
                                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;" width="10%">${gis.vchno}</td>
                                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;" width="10%">${gis.amoutDeposit}</td>
                                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;" width="30%">
                                                        <c:if test = "${not empty gis.trName}">
                                                            ${gis.trName}<br/>
                                                        </c:if>    
                                                        <strong>Date of Deposit: ${gis.dod}</strong>
                                                    </td>
                                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;"width="30%">${gis.depositBy}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </CENTER>

                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
