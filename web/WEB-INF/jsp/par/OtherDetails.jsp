<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Performance Appraisal</title> 

        <link href="resources/css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/hrmis.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>
        <link rel="stylesheet" type="text/css" href="css/jquery.classyedit.css"/>
        <link rel="stylesheet" type="text/css" href="css/color.css"/>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.classyedit.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('textarea').bind('cut copy paste', function(e) {
                    //e.preventDefault(); //disable cut,copy,paste
                });
                $("#selfappraisal").ClassyEdit();
                $("#specialcontribution").ClassyEdit();
                $("#hinderreason").ClassyEdit();
            });
            function replaceWordChars(text) {
                var s = text;

                s = s.replace(/[\u2018|\u2019|\u201A]/g, "\'");// smart single quotes and apostrophe

                s = s.replace(/[\u201C|\u201D|\u201E]/g, "\"");// smart double quotes

                s = s.replace(/\u2026/g, "...");// ellipsis

                s = s.replace(/[\u2013|\u2014]/g, "-");// dashes

                s = s.replace(/\u02C6/g, "^");// circumflex

                s = s.replace(/\u005C/g, "\\");// backward slash

                s = s.replace(/\u002F/g, "/");// forward slash
                //s = s.replace(/\u2039/g, "<");// open angle bracket

                //s = s.replace(/\u203A/g, ">");// close angle bracket

                s = s.replace(/[\u02DC|\u00A0]/g, "");// spaces

                s = s.replace(/\u00C2/g, "-");//multiple spaces
                //s = s.replace(/\s+/g, ' ');//multiple spaces
                //document.getElementById("your Textarea ID ").value = s;
                return s;
            }
            function validatePAR() {
                var selfappraisal = replaceWordChars($('#selfappraisal').val());
                $('#selfappraisal').val(selfappraisal);

                var specialcontribution = replaceWordChars($('#specialcontribution').val());
                $('#specialcontribution').val(specialcontribution);

                var hinderreason = replaceWordChars($('#hinderreason').val());
                $('#hinderreason').val(hinderreason);
            }
        </script>
    </head>
    <body>
        <table style="font-family:Verdana;" cellpadding="0" cellspacing="0">
            <tr>
                <td style="width:253px;"><div style="border-width:1px;width:251px;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;padding:10px 0px 10px 0px;text-align:center;">Personal Information</div></td>
                <td style="width:253px;"><div style="border-width:1px;width:251px;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;padding:10px 0px 10px 0px;text-align:center;">Absentee Statement</div></td>
                <td style="width:253px;"><div style="border-width:1px;width:251px;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;padding:10px 0px 10px 0px;text-align:center;">Achievements</div></td>
                <td style="width:253px;"><div style="border-width:1px;width:251px;font-weight:bold;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 0 1px;padding:10px 0px 10px 0px;text-align:center;">Other Details</div></td>
            </tr>
        </table>
        <form action="par/addPAR.htm" method="POST" commandName="parOtherDetails">
            <input type="hidden" name="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <input type="hidden" name="hidparid" value="${parMastForm.parid}"/>
            <input type="hidden" name="hidpaptid" value="${parOtherDetails.paptid}"/>
            <input type="hidden" name="empid" value="${users.empId}"/>
            <input type="hidden" name="spc" value="${users.substantivePost.spc}"/>
            <input type="hidden" name="fiscalyear" value="${parMastForm.fiscalyear}"/>
            <div align="center">
                <div class="easyui-panel" style="overflow: auto; scrollbar-base-color:#A6D3FF;">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style="padding-left:20px;margin-top:20px;">
                        <tr>
                            <td>
                                Brief description of duties/tasks entrusted.(in about 100 words)
                                (<span style="color:#FF0000">
                                    Do not copy from Microsoft Word. Use <b>Note Pad</b> Software to copy your details and paste here. 
                                </span>)
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <textarea name="selfappraisal" id="selfappraisal" style="width:900px;height:60px;border:1px solid #000000;"><c:out value="${parOtherDetails.selfappraisal}"/></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Significant work, if any, done
                                (<span style="color:#FF0000">
                                    Do not copy from Microsoft Word. Use <b>Note Pad</b> Software to copy your details and paste here. 
                                </span>)
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <textarea name="specialcontribution" id="specialcontribution" style="width:900px;height:60px;border:1px solid #000000;"><c:out value="${parOtherDetails.specialcontribution}"/></textarea>
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Work Done For Implementation of 5Ts(Transparency,Teamwork,Technology,Transformation and Time):
                            </td>
                        </tr>
                        <tr>
                            <td> 
                                <textarea name="fiveTComponentappraise" id="fiveTComponentappraise" style="width:900px;height:60px;border:1px solid #000000;"><c:out value="${parOtherDetails.fiveTComponentappraise}"/></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Mention the Factors, if any, that hindered your performance
                                (<span style="color:#FF0000">
                                    Do not copy from Microsoft Word. Use <b>Note Pad</b> Software to copy your details and paste here. 
                                </span>)
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <textarea name="hinderreason" id="hinderreason" style="width:900px;height:60px;border:1px solid #000000;"><c:out value="${parOtherDetails.hinderreason}"/></textarea>
                            </td>
                        </tr>
                    </table>

                    <table border="0" cellpadding="0" cellspacing="0" width="100%" class="easyui-panel">
                        <tr style="height: 60px">
                            <td align="center" width="10%">
                                <span style="font-size:16px;">Place</span>
                            </td>
                            <td align="left" width="70%">
                                <input type="text" name="place" id="place" class="easyui-textbox" value="${parOtherDetails.place}" size="40"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
                            
            <div align="center" style="margin-top: 10px;">
                <table width="100%" cellpadding="0" cellspacing="0">
                    <tr style="height: 40px">
                        <td width="100%" align="right">
                            <span style="padding-right:10px;">
                                <input type="hidden" name="pageno" value="4"/>
                                <table border="0" width="100%">
                                    <tr>
                                        <td width="50%" align="left" style="padding-left:20px;">
                                            <input type="submit" name="newPar" value="Previous" class="easyui-linkbutton c7" style="width:100px;height: 30px;"/>
                                        </td>
                                        <td width="50%" align="right" style="padding-right:20px;">
                                            <input type="submit" name="newPar" value="Save" class="easyui-linkbutton c1" style="width:100px;height: 30px;" onclick="validatePAR();"/>
                                        </td>
                                    </tr>
                                </table>
                            </span>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>
