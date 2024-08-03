<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link rel="stylesheet" type="text/css" href="css/popupmain.css"/>  
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

                $('#mask').click(function () {
                    ViewMessage();
                });
            });
        </script>      
        <style type="text/css">
            #profile_container{width:95%;margin:0px auto;border-top:0px;}
            ol li{width:180px;float:left;text-align:left;}
        </style> 
        <script type="text/javascript">
            $(function () {
                $('#issueDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#expiryDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });
            function setDocType() {
                var empId = $('#empId').val();
                var docType = $('#identityDocType').val();
                $('#hidIdentityId').val(empId + "," + docType);
                //alert($('#hidIdentityId').val($('#empId').val()+","+$('#identityDocType').val()));
            }
            function saveIdentity() {
                var idNo = $('#identityDocNo').val();
                var idDocType = $('#hidIdentityType').val();
                if (idNo == '') {
                    alert("Please enter Identity No");
                    return false;
                }
                if (idDocType == '') {
                    alert("Please enter Identity Type");
                    return false;
                } else if (idDocType == 'AADHAAR' && idNo.length < 12) {
                    alert("Please enter valid Aadhaar No");
                    return false;
                } else if (idDocType == 'PAN') {
                    if (idNo.length < 10 || idNo.length > 10) {
                        alert("Please enter valid PAN No");
                        $('#identityDocNo').focus();
                        return false;
                    } else if (idNo.length == 10) {
                        var alphanum = /^[0-9a-zA-Z]+$/;
                        if (!idNo.match(alphanum)) {
                            alert("Please enter valid PAN No");
                            $('#identityDocNo').focus();
                            return true;
                        }
                    }
                }

                return true;
            }
        </script>
    </head>
    <body>

        <div id="boxes">
            <div style=" left: 551.5px; display: none;" id="dialog" class="window"> 

                <table class="table-bordered" align='center'>
                    <tr>
                        <td align="center"><h2 class="alert alert-info" style="color:#FF0000;font-size:14pt;font-weight:bold;margin:0px;">
                                You have to submit the following details in order to complete your profile:</h2>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Personal Information</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li>Employee Name</li>
                                <li>ACCT TYPE</li>
                                <li>GPF NO</li>
                                <li>FIRST NAME</li>
                                <li>LAST NAME</li>
                                <li>GENDER</li>
                                <li>MARITAL STATUS</li>
                                <li>CATEGORY</li>
                                <li>HEIGHT</li>
                                <li>DOB</li>
                                <li>JOIN DATE OF GOO</li>
                                <li>DATE OF ENTRY INTO GOVERNMENT</li>
                                <li>BLOOD GROUP</li>
                                <li>RELIGION</li>
                                <li>MOBILE</li>
                                <li>POST GROUP</li>
                                <li>HOME TOWN</li>
                                <li>DOMICILE</li>
                                <li>ID MARK</li>
                                <li>BANK NAME</li>
                                <li>BRANCH NAME</li>
                                <li>BANK ACCOUNT NUMBER</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Language</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">LANGUAGE AT LEAST ONE</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Identity</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li>PAN NUMBER</li>
                                <li>AADHAAR NUMBER</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Address</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">ADDRESS BOTH PERMANENT AND PRESENT</li>
                            </ol>


                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Family</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">FATHERS NAME/HUSBAND NAME</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Education</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">QUALIFICATION AT LEAST ONE</li>
                            </ol>
                        </td>
                    </tr>

                </table>            
                <div id="popupfoot" style="text-align:center;"> 
                    <a href="javascript:void(0)" onclick="javascript: ViewMessage();" class="close agree btn pri" style="background:#FF0000;color:#FFFFFF;">Close</a> 
                </div>            

            </div>     
            <form:form id="fm" action="identity.htm" method="post" name="myForm" commandName="identity">
                <form:hidden id="empId" path="empId" value="${identity.empId}"/>
                <form:hidden id="hidIdentityId" path="hidIdentityId" value="${identity.hidIdentityId}"/>

                <div style=" margin-bottom: 5px;" class="panel panel-info">
                    <div class="panel-body">
                        <table border="0" cellpadding="0" cellspacing="0"  width="100%">
                            <tr>
                                <td width="5%">&nbsp;</td>
                                <td width="15%" >&nbsp;</td>
                                <td width="30%" align="center">&nbsp;</td>
                                <td width="40%" align="center">&nbsp;</td>
                                <td width="10%" align="center">&nbsp;</td>
                            </tr>
                            <tr height="40px">
                                <td align="center"><%=i++%>.</td>
                                <td>Identity Type:</td>
                                <td>
                                    <c:if test="${empty identity.identityDocType}">
                                        <form:select path="identityDocType" style="width:40%;" class="form-control" onchange="setDocType()">
                                            <form:option value="" label="Select" cssStyle="width:30%"/>
                                            <c:forEach items="${identityTypeList}" var="identityDocType">
                                                <form:option value="${identityDocType.identityTypeId}" label="${identityDocType.identityType}"/>
                                            </c:forEach>                                 
                                        </form:select>
                                    </c:if>
                                    <c:if test="${not empty identity.identityDocType}">
                                        <c:out value="${identity.identityDocType}"/>
                                        <input type="hidden" id="hidIdentityType" value="${identity.identityDocType}"/>
                                    </c:if>
                                </td>
                                <td>&nbsp; </td>
                                <td>&nbsp; </td>
                            </tr>
                            <tr height="40px">
                                <td align="center"><%=i++%>.</td>
                                <td>Identity No:</td>
                                <td>
                                    <form:input path="identityDocNo" class="form-control" style="width:50%;" id="identityDocNo" placeholder="Enter Identity No" maxlength="40"/>
                                    <c:if test="${identity.identityDocType eq 'PAN'}">
                                        <c:if test="${fn:length(identity.identityDocNo) ne 10}">
                                            <span style="color:red;">Invalid PAN</span>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td>&nbsp; </td>
                                <td>&nbsp; </td>
                            </tr>
                            <tr height="40px">
                                <td align="center"><%=i++%>.</td>
                                <td>Place of Issue:</td>
                                <td>
                                    <form:input path="placeOfIssue" class="form-control" style="width:50%;" id="placeOfIssue" placeholder="Enter Place of Issue" maxlength="30"/>
                                </td>
                                <td>&nbsp; </td>
                                <td>&nbsp; </td>
                            </tr>
                            <tr height="40px">
                                <td align="center"><%=i++%>.</td>
                                <td>Date of Issue:</td>
                                <td>
                                    <div class="input-group date" style="width:40%;" id="issueDate">
                                        <form:input class="form-control"  id="issueDate" path="issueDate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </td>
                                <td>&nbsp; </td>
                                <td>&nbsp; </td>
                            </tr>
                            <tr height="40px">
                                <td align="center"><%=i++%>.</td>
                                <td>Date of Expiry:</td>
                                <td>
                                    <div class='input-group date' style="width:40%;" id='expiryDate'>
                                        <form:input class="form-control"  id="expiryDate" path="expiryDate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </td>
                                <td>&nbsp; </td>
                                <td>&nbsp; </td>
                            </tr>
                        </table>

                        <div class="pull-left">
                            <c:if test="${empty identity.identityDocType}">
                              <input type="submit" name="btnAction" value="Save" class="btn btn-primary" onclick="return saveIdentity();"/>                              
                            </c:if>
                            <c:if test="${not empty identity.identityDocType}">
                                <input type="submit" name="btnAction" value="Update" class="btn btn-primary" onclick="return saveIdentity();"/>
                                <input type="submit" name="btnAction" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?');"/>
                                <input type="submit" name="btnAction" value="Back" class="btn btn-primary"/>
                            </c:if>
                            <span style=" color: red"> ${identity.printMsg}</span>
                        </div>

                        <table class="table table-bordered" style="margin-top:50px;">
                            <thead>
                                <tr class="bg-primary text-white">
                                    <th>#</th>
                                    <th>Identity Type</th>
                                    <th>Identity No</th>
                                    <th>Place of Issue</th>
                                    <th>Date of Issue</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${identityInfoList}" var="identityList" varStatus="cnt">
                                    <tr>
                                        <th scope="row">${cnt.index+1}<form:hidden id="identityDesc" path="identityDesc" value="${identityList.identityDesc}"/></th>
                                        <td>${identityList.identityDocType}</td>
                                        <td>
                                            ${identityList.identityNo}
                                            <c:if test="${identityList.identityDocType eq 'AADHAAR'}">
                                                <c:if test="${identityList.isVerified eq 'Y'}">
                                                    <img src="images/verified.png" width="20" height="20"/>
                                                </c:if>
                                                <c:if test="${identityList.isVerified eq 'N' }">
                                                    <img src="images/error.png" width="20" height="20"/>
                                                </c:if>
                                                <c:if test="${empty identityList.isVerified}">
                                                    <img src="images/error.png" width="20" height="20"/>
                                                </c:if>


                                            </c:if>
                                            <c:if test="${identityList.identityDocType eq 'PAN'}">
                                                <c:if test="${fn:length(identityList.identityNo) ne 10}">
                                                    <span style="color:red;">Invalid PAN</span>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>${identityList.placeOfIssue}</td>
                                        <td>${identityList.issueDate}</td>
                                        <td>
                                            <c:if test="${identityList.isLocked eq 'N'}">
                                                <a href="getIdentityData.htm?empId=${identity.empId}&idDesc=${identityList.identityDesc}">Edit</a>
                                            </c:if>
                                            <c:if test="${identityList.isLocked eq 'Y'}">
                                                <img src="images/Lock.png" width="20" height="20"/>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </div>

            </form:form>
    </body>
</html>
