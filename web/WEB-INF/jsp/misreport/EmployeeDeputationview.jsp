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
        <title>Employee Deputation</title>

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
            
            table tr td{padding:5px;}
        </style>

    </head>
    <body>        
        <form:form action="employeeProfile.htm" commandName="employeeProfile"  method="GET" target="_blank">
            <div align="center" style="overflow-x:hidden">
                <div  style="width: 100%">
                    <div>
                        <CENTER>
                            <table  border="0" cellpadding="2" cellspacing="0">
                                <tr>
                                    <td colspan="2" height="67" align="center" valign="bottom" style="font-family:Arial, Helvetica, sans-serif;font-size:40px;color:#000099">
                                        <strong>Employee Details</strong>    
                                    </td>
                                </tr>
                                <tr>
                                    <td  height="51" colspan="2" align="center" valign="top" style="font-family:Arial, Helvetica, sans-serif;font-size:40px;color:#000099"><strong>Government of Odisha</strong></td>
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
                            </table>
                            <%mypage = mypage + 1;%>
                            <table width="1040" height="100" border="1" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="printLabel" style="text-align:left;text-transform:uppercase;">NAME OF Officer: </td>
                                    <td class="printData" style="text-align:left;text-transform:uppercase;">&nbsp;<c:out value="${employeeProfile.empName}"/></td>
                                </tr>
                                <tr>
                                    <td width="250" class="printLabel" style="text-align:left;text-transform:uppercase;">
                                        <c:out value="${employeeProfile.accttype}"/> NO:
                                    </td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;">&nbsp;<c:out value="${employeeProfile.gpfno}"/></td>
                                    </tr>
                                   
                                   <tr>
                                    <td width="340" class="printLabel" style="text-align:left;text-transform:uppercase;">DATE OF BIRTH : </td>
                                    <td width="700" class="printData" style="text-align:left;text-transform:uppercase;">&nbsp;

                                        <c:if test="${not empty employeeProfile.dob}">
                                            <c:out value="${employeeProfile.dob}"/>
                                        </c:if>
                                        <c:if test="${not empty employeeProfile.dobText}">
                                            (<c:out value="${employeeProfile.dobText}"/>)
                                        </c:if>

                                    </td>
                                </tr>
                                    <tr height="50" >
                                    <td width="300" class="printLabel" style="text-align:left; ">DATE OF ENTRY IN <BR> GOVERMENT SERVICE :</td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;">&nbsp;

                                        ${employeeProfile.doeGov}
                                        <c:if test="${not empty employeeProfile.entryGovDateText}">
                                            (${employeeProfile.entryGovDateText})
                                        </c:if>
                                    </td>
                                </tr>
                                
                               <tr height="50" >
                                    <td width="300" class="printLabel" style="text-align:left; ">Current Designation :</td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;">&nbsp;
                                        ${employeeProfile.post}                       
                                    </td>
                                </tr>
                                  <tr height="50" >
                                    <td width="520" class="printLabel" style="text-align:left; ">HOME District:</td>
                                    <td width="520" class="printData" style="text-align:left;text-transform:uppercase;">&nbsp;
                                        ${employeeProfile.homeTown}
                                    </td>
                                </tr>
                                 <tr>
                                         <td width="250" class="printLabel" style="text-align:left; text-transform:uppercase;">HRMS ID:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;">&nbsp;<c:out value="${employeeProfile.empid}"/></td>
                               
                                    </tr>
                                    
                                    <tr>
                                         <td width="250" class="printLabel" style="text-align:left; text-transform:uppercase;">All Previous Place of Posting:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;">
                                             <table class="table" border="2px" cellspacing="0"  >                         
                                                <thead>
                                                    <tr border="2px">
                                                        <td style="width:2%">Sl No.</td>
                                                        <td style="width:10%">JOIN DATE</td>
                                                        <td style="width:10%">OFFICE NAME</td>
                                                        <td style="width:10%">POST</td>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${promotionList}" var="promotionList" varStatus="count">
                                                    <tr>
                                                        <td>${count.index + 1}</td>
                                                        <td style="width:10%">${promotionList.joindategoo}</td>
                                                        <td style="width:10%">${promotionList.office}</td>
                                                        <td style="width:10%">${promotionList.promotionalpost}</td>

                                                    </tr>
                                                </c:forEach>                            
                                                </tbody>
                                            </table>
                                    </td>
                               
                                    </tr>
                                        <tr>
                                         <td width="250" class="printLabel" style="text-align:left; text-transform:uppercase;">Current Place of Posting and date of joining:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;<c:out value="  ${employeecurrentProfile.currentJoinPlace} (${employeecurrentProfile.currentJoindate})"/></td>                               
                                    </tr>
                                      <c:if test="${employeeProfile.property_submitted_if_any == 1}">    

                                    <tr>
                                        <td class="printLabel" style="text-align:left;">Property Return</td>
                                        <td   class="printData">Submitted</td>
                                    </tr>

                                </c:if>
                                       <c:if test="${employeeProfile.property_submitted_if_any != 1}">    

                                    <tr>
                                        <td class="printLabel"  style="text-align:left;">Property Return</td>
                                        <td width="36%"  class="printData">Not Submitted</td>
                                    </tr>

                                </c:if>
                                    <tr>
                                        <td class="printLabel"  style="text-align:left;">CCR/PAR Status/Data</td>
                                        <td width="36%"  class="printData">
                                            
                                            <table width="700" height="50" border="1" cellpadding="0" cellspacing="0">
                                                <tr >
                                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Date</td>
                                                    <td width="350" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Status</td>
                                                
                                                    
                                                    
                                                    <c:forEach items="${ccrdetails}" var="ccrdetails">
                                                    <tr>
                                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp; ${ccrdetails.fiscalyear}</td>
                                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp; 
                                                           
                                                            Submitted
                                                             
                                                        
                                                        
                                                        </td>
                                                      </tr>
                                                </c:forEach>
                                            </table>
                                        </td>
                                    </tr>
                                    
                                     <tr>
                                        <td class="printLabel"  style="text-align:left;">Family Particular</td>
                                        <td width="36%"  class="printData">
                                            
                                <table width="700" height="50" border="1" cellpadding="0" cellspacing="0">
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
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <c:forEach items="${educations}" var="education">
                                <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                    <thead> </thead>
                                    <tr>
                                        <td class="printLabel" style="text-align:center;text-transform:uppercase;font-size:20px ">&nbsp;
                                            ${education.qualification}		
                                        </td>	
                                    </tr>
                                </table>


                                <table width="800" height="50" border="1" cellpadding="0" cellspacing="0">
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
                                            
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            
                                            <table width="1040" border="1" cellpadding="0" cellspacing="0" class="leaveaccttbl">
                                <thead>
                                    <tr>
                                        <td colspan="11" height="35">Leave Details </td>
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
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            Training Attended
                                        </td>
                                        <td>
                                            
                                             <table width="700" height="50" border="1" cellpadding="0" cellspacing="0">
                                <tr >
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Start Date</td>
                                    <td width="350" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Close Date</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Place </td>
       
                                </tr>
                                <c:forEach items="${trainingdetails}" var="trainingdetails">
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${trainingdetails.trainingSdate}</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${trainingdetails.trainingCdate} </td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${trainingdetails.trainingPlace}</td>
                                        </tr>
                                </c:forEach>
                            </table>
                                            
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            Disciplinary Proceeding Particulars
                                        </td>
                                        <td>
                                            
                                        <table width="700" height="50" border="1" cellpadding="0" cellspacing="0">
                                         <tr >
                                             <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Order Number</td>
                                             <td width="350" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Order Date</td>
                                             <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Description </td>

                                         </tr>
                                         <c:forEach items="${disclipnary}" var="disclipnary">
                                             <tr>
                                                 <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${disclipnary.memoDate}</td>
                                                 <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${disclipnary.memoNo} </td>
                                                 <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;${disclipnary.description}</td>
                                                 </tr>
                                         </c:forEach>
                                     </table>
                                            
                                        </td>
                                    </tr>
                            </table>
                        </center>
                    </div>                                      
                </div>
            </div>
  
        </form:form>
    </body>
</html>
