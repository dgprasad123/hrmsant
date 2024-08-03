<%-- 
    Document   : AdverseCommunication
    Created on : Oct 14, 2019, 10:56:40 AM
    Author     : manisha
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

    int slno = 0;

    String reportingAuth = "";
    String reviewingAuth = "";
    String acceptingAuth = "";
%>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />  
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Adverse Communication</title>

        <link href="css/hrmis.css" rel="stylesheet" type="text/css" />
        <link href="css/colorbox.css" rel="stylesheet" type="text/css" />
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />

        <script language="javascript" src="js/jquery.min-1.9.1.js" type="text/javascript"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>

        <script language="javascript" type="text/javascript">
            $(document).ready(function() {
                $('#txtOrdDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            });

            function submitAdverse() {
                if ($('#txtOrdNo').val() == '') {
                    alert("Please enter Order No.");
                    $('#txtOrdNo').focus();
                    return false;
                }
                if ($('#txtOrdDate').val() == '') {
                    alert("Please enter Order Date");
                    $('#txtOrdDate').focus();
                    return false;
                }
                if ($("#rdAuthority:checked").length == 0) {
                    alert("Please select at least one Authority.");
                    return false;
                }
                return true;
            }
        </script>

        <style type="text/css">
            body{
                font-family: Verdana,Arial,Helvetica,sans-serif;
            }
            table#reportgrid{
                font-family: Verdana,Arial,Helvetica,sans-serif;
                color:#000000;
                empty-cells:show;
                border-collapse: collapse;
            }
            .showinprinting
            {
                display:none;
            }
            table#reportgrid th{
                background-color: #E9E9EA;
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                color: #000000;                                
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 15px;

            }
            table#reportgrid td{                
                padding-left:10px;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 12px;
                height: 10px;
            }
            .alternateTD{               
                background-color:#E2F4FA;                
            }
            .alternateNormalTD{               
                background-color:#FFFFFF;                
            }
            .lastcolumn{                               
                border-right: 1px solid #091470;               
            }            
            .reportHeader{
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                border-right: 1px solid #091470;
            }
        </style>
    </head>
    <body>
        <form:form action="getviewPARAdmindetail.htm" commandName="parApplyForm" method="post">
           
            <form:hidden path="taskid"/>
            <form:hidden path="parid"/>
           
           <%--  <jsp:include page="../tab/ParMenu.jsp"/>   --%>
            <%--<html:form action="/JSP/ParApplyDispAction"> --%>
            <%-- <html:hidden property="applicantempid"/>
            <html:hidden property="apprisespc"/>
            <html:hidden property="parid"/>
            <html:hidden property="taskid"/> --%>
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div id="tbl-container" class="empInfoDiv" style="overflow: auto; scrollbar-base-color:#A6D3FF;">
                    <table border="0" cellpadding="0" id="reportgrid" cellspacing="0" width="100%">
                        <tbody id="emplist_list">
                            <tr style="height:40px;">                                        
                                <td width="10%">
                                    Order No:
                                </td>
                                <td width="40%">
                                      <form:input class="form-control" path="txtOrdNo"styleId="txtOrdNo"/>     
                      <%--  <html:text property="txtOrdNo" styleId="txtOrdNo"/> --%>
                        </td>
                        <td width="10%">
                            Order Date:
                        </td>
                        <td width="40%" align="center">
                            <form:input class="form-control" path="txtOrdDate"styleId="txtOrdDate"/>
                            <%-- <html:text property="txtOrdDate" styleId="txtOrdDate"/> --%>
                        </td>
                        </tr>
                        <tr style="height:40px;">
                            <td>
                                Appraise Name
                            </td>
                            <td colspan="3">
                                <span style="font-weight:bold;">${parApplyForm.empName}<%--<bean:write name="ParApplyForm" property="applicant"/>--%></span>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td colspan="4">
                                Adverse Remarks given by
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td>&nbsp;</td>
                            <td colspan="3"><strong>Reporting Authority</strong></td>
                        </tr>
                          <c:forEach items="${accepting}" var="acceptingbean">  
                                 ${acceptingbean.authorityname}
                        <%-- <c:if test="${acceptingbean.isacceptingcompleted eq 'Y'}"> 
                        <%--  <logic:notEmpty name="ParApplyForm" property="reportingauth">
                             <logic:iterate id="rptauth" name="ParApplyForm" property="reportingauth">
                                 <bean:define id="reportingid" name="rptauth" property="authorityempid"/>
                        <%--    <%
                                slno = slno + 1;
                                reportingAuth = (String)reportingid + "_REPORTING";
                            %> --%>
                        <%--  <tr>
                              <td>&nbsp;</td>
                              <td colspan="3">
                                  <html:radio property="rdAuthority" styleId="rdAuthority" value='<%=reportingAuth%>'/>&nbsp;<%=slno%>.&nbsp;<bean:write property="authorityname" name="rptauth"/> (<bean:write property="authorityspn" name="rptauth"/>)
                              </td>
                          </tr>
                      </logic:iterate>
                  </logic:notEmpty>  --%>
                          </c:forEach>
                      <%--  <logic:greaterThan value="6" name="ParApplyForm" property="parstatus"> --%>
                            <tr style="height:40px;">
                                <td>&nbsp;</td>
                                <td colspan="3"><strong>Reviewing Authority</strong></td>
                            </tr>
                          <%--  <logic:notEmpty name="ParApplyForm" property="reviewingauth">
                                <%slno = 0;%>
                                <logic:iterate id="rptauth" name="ParApplyForm" property="reviewingauth">
                                    <bean:define id="reviewingid" name="rptauth" property="authorityempid"/>
                        <%--   <%
                            slno = slno + 1;
                            reviewingAuth = (String)reviewingid + "_REVIEWING";
                        %> --%>
                        <%--  <tr>
                              <td>&nbsp;</td>
                              <td colspan="3">
                                  <html:radio property="rdAuthority" styleId="rdAuthority" value='<%=reviewingAuth%>'/>&nbsp;<%=slno%>.&nbsp;<bean:write property="authorityname" name="rptauth"/> (<bean:write property="authorityspn" name="rptauth"/>)
                              </td>
                          </tr>
                      </logic:iterate>
                  </logic:notEmpty>
              </logic:greaterThan> --%>
                        <%--  <logic:greaterThan value="7" name="ParApplyForm" property="parstatus"> --%>
                              <tr style="height:40px;">
                                  <td>&nbsp;</td>
                                  <td colspan="3"><strong>Accepting Authority</strong></td>
                              </tr>
                             <%-- <logic:notEmpty name="ParApplyForm" property="acceptingauth">
                                  <%slno = 0;%>
                                  <logic:iterate id="rptauth" name="ParApplyForm" property="acceptingauth">
                                      <bean:define id="acceptingid" name="rptauth" property="authorityempid"/>
                        <%--  <%
                              slno = slno + 1;
                              acceptingAuth = (String)acceptingid + "_ACCEPTING";
                          %> --%>
                        <%--   <tr>
                               <td>&nbsp;</td>
                               <td colspan="3">
                                   <html:radio property="rdAuthority" styleId="rdAuthority" value='<%=acceptingAuth%>'/>&nbsp;<%=slno%>.&nbsp;<bean:write property="authorityname" name="rptauth"/> (<bean:write property="authorityspn" name="rptauth"/>)
                               </td>
                           </tr>
                       </logic:iterate>
                   </logic:notEmpty>
               </logic:greaterThan> --%>
                        <tr style="height:40px;">
                            <td>
                                Note:
                            </td>
                            <td colspan="3">
                                 <form:textarea class="form-control" path="txtareaNote" rows="4" cols="50"/>
                                <%--  <html:textarea property="txtareaNote" rows="4" cols="50"/> --%>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td colspan="4">
                                <input type="button" value="Send" onclick="return submitAdverse()"/>
                                <input type="submit" class="btn btn-default" name="action" value="Back"/>
                            </td> 
                        </tr>
                        </tbody>                                
                    </table>
                </div>
            </div>
            <%--  </html:form> --%>
        </form:form>
    </body>
</html>