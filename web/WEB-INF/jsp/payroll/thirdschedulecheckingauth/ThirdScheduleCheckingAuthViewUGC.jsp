<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
    </head>
    <body>
        <div align="center">
            <div style="width: 70%">
                <div style="margin-top: 20px;">THIRD SCHEDULE VIEW</div>
                <div style="margin-top: 10px;"><u>Form for Fixation of Pay under the Odisha Revised Scales of Pay(For</u></div>
                <div style="margin-top: 10px;">College Teachers) Rules 2019</div>
                <div style="margin-top: 10px;">[See Rule - 7]</div>
                <div style="margin-top: 20px;">
                    <p style="color:red;font-weight:16px;">
                        UPDATE THE FIELDS WHEREVER NECESSARY.
                    </p>
                    <form method="POST" id="thirdScheduleDataForm" commandName="thirdScheduleForm">
                        <input type="hidden" name="empid" value="${tform.empid}"/>
                        <table width="100%" cellspacing="0" cellpadding="0" border="0" >
                            <tr style="height:60px;">
                                <td width="3%">1. </td>
                                <td width="45%">Name of the Employee </td>
                                <td width="3%" align="center">:</td>
                                <td width="45%">
                                    <c:out value="${tform.empname}"/>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>2. </td>
                                <td>Name of the Head of Office <br/> (Designation only)</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <select name="hooSpc">
                                            <option value="">--Select--</option>
                                            <c:if test="${not empty PostList2nd}">
                                                <c:forEach var="postlist" items="${PostList2nd}">
                                                    <option value="${postlist.postcode}" <c:if test="${not empty tform.hooSpc && tform.hooSpc == postlist.postcode}"> <c:out value='selected="selected"'/></c:if>>${postlist.post}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.hooPostName}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>3. </td>
                                <td>Post held by the employee <br/> (Substantive/Officiating) </td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <select name="officiating">
                                            <option value="Substantive" <c:if test="${not empty tform.officiating && tform.officiating == 'Substantive'}"> <c:out value='selected="selected"'/></c:if>>Substantive</option>
                                            <option value="Officiating" <c:if test="${not empty tform.officiating && tform.officiating == 'Officiating'}"> <c:out value='selected="selected"'/></c:if>>Officiating</option>
                                        </select>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.officiating}"/><br />
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>4. </td>
                                <td>Existing Pay Band and Academic Grade Pay of the Post</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <!--<input type="text" name="revisionPayScale" size="20" value="${tform.revisionPayScale}"/>-->
                                        <select name="previousPayScale">
                                            <option value="">--Select--</option>
                                            <option value="4750-14680" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '4750-14680'}"> <c:out value='selected="selected"'/></c:if>>4750-14680</option>
                                            <option value="4930-14680" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '4930-14680'}"> <c:out value='selected="selected"'/></c:if>>4930-14680</option>
                                            <option value="5200-20200" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '5200-20200'}"> <c:out value='selected="selected"'/></c:if>>5200-20200</option>
                                            <option value="9300-34800" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '9300-34800'}"> <c:out value='selected="selected"'/></c:if>>9300-34800</option>
                                            <option value="15600-39100" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '15600-39100'}"> <c:out value='selected="selected"'/></c:if>>15600-39100</option>
                                            <option value="37400-67000" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '37400-67000'}"> <c:out value='selected="selected"'/></c:if>>37400-67000</option>
                                            <option value="67000-79000" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '67000-79000'}"> <c:out value='selected="selected"'/></c:if>>67000-79000</option>
                                            <option value="75500-80000" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '75500-80000'}"> <c:out value='selected="selected"'/></c:if>>75500-80000</option>
                                            <option value="80000" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '80000'}"> <c:out value='selected="selected"'/></c:if>>80000</option>
                                            <option value="90000" <c:if test="${not empty tform.previousPayScale && tform.previousPayScale == '90000'}"> <c:out value='selected="selected"'/></c:if>>90000</option>
                                        </select>
                                        <%--<input type="text" name="previousGp" size="5" maxlength="5" value="${tform.previousGp}"/>--%>
                                        <select name="previousGp" id="previousGp">
                                            <option value="">--Select--</option>
                                            <option value="0" <c:if test="${not empty tform.previousGp && tform.previousGp == '0'}"> <c:out value='selected="selected"'/></c:if>>0</option>
                                            <option value="1700" <c:if test="${not empty tform.previousGp && tform.previousGp == '1700'}"> <c:out value='selected="selected"'/></c:if>>1700</option>
                                            <option value="1775" <c:if test="${not empty tform.previousGp && tform.previousGp == '1775'}"> <c:out value='selected="selected"'/></c:if>>1775</option>
                                            <option value="1800" <c:if test="${not empty tform.previousGp && tform.previousGp == '1800'}"> <c:out value='selected="selected"'/></c:if>>1800</option>
                                            <option value="1900" <c:if test="${not empty tform.previousGp && tform.previousGp == '1900'}"> <c:out value='selected="selected"'/></c:if>>1900</option>
                                            <option value="2000" <c:if test="${not empty tform.previousGp && tform.previousGp == '2000'}"> <c:out value='selected="selected"'/></c:if>>2000</option>
                                            <option value="2200" <c:if test="${not empty tform.previousGp && tform.previousGp == '2200'}"> <c:out value='selected="selected"'/></c:if>>2200</option>
                                            <option value="2400" <c:if test="${not empty tform.previousGp && tform.previousGp == '2400'}"> <c:out value='selected="selected"'/></c:if>>2400</option>
                                            <option value="2800" <c:if test="${not empty tform.previousGp && tform.previousGp == '2800'}"> <c:out value='selected="selected"'/></c:if>>2800</option>
                                            <option value="4200" <c:if test="${not empty tform.previousGp && tform.previousGp == '4200'}"> <c:out value='selected="selected"'/></c:if>>4200</option>
                                            <option value="4600" <c:if test="${not empty tform.previousGp && tform.previousGp == '4600'}"> <c:out value='selected="selected"'/></c:if>>4600</option>
                                            <option value="4800" <c:if test="${not empty tform.previousGp && tform.previousGp == '4800'}"> <c:out value='selected="selected"'/></c:if>>4800</option>
                                            <option value="5400" <c:if test="${not empty tform.previousGp && tform.previousGp == '5400'}"> <c:out value='selected="selected"'/></c:if>>5400</option>
                                            <option value="6000" <c:if test="${not empty tform.previousGp && tform.previousGp == '6000'}"> <c:out value='selected="selected"'/></c:if>>6000</option>
                                            <option value="6600" <c:if test="${not empty tform.previousGp && tform.previousGp == '6600'}"> <c:out value='selected="selected"'/></c:if>>6600</option>
                                            <option value="7000" <c:if test="${not empty tform.previousGp && tform.previousGp == '7000'}"> <c:out value='selected="selected"'/></c:if>>7000</option>
                                            <option value="7600" <c:if test="${not empty tform.previousGp && tform.previousGp == '7600'}"> <c:out value='selected="selected"'/></c:if>>7600</option>
                                            <option value="8000" <c:if test="${not empty tform.previousGp && tform.previousGp == '8000'}"> <c:out value='selected="selected"'/></c:if>>8000</option>
                                            <option value="8700" <c:if test="${not empty tform.previousGp && tform.previousGp == '8700'}"> <c:out value='selected="selected"'/></c:if>>8700</option>
                                            <option value="8800" <c:if test="${not empty tform.previousGp && tform.previousGp == '8800'}"> <c:out value='selected="selected"'/></c:if>>8800</option>
                                            <option value="8900" <c:if test="${not empty tform.previousGp && tform.previousGp == '8900'}"> <c:out value='selected="selected"'/></c:if>>8900</option>
                                            <option value="9000" <c:if test="${not empty tform.previousGp && tform.previousGp == '9000'}"> <c:out value='selected="selected"'/></c:if>>9000</option>
                                            <option value="10000" <c:if test="${not empty tform.previousGp && tform.previousGp == '10000'}"> <c:out value='selected="selected"'/></c:if>>10000</option>
                                        </select>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.previousPayScale}"/><br />
                                        <c:out value="${tform.previousGp}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>5. </td>
                                <td>Corresponding Level in the Pay Matrix of the Pay Band and Academic Grade Pay of the present Post</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="curPostPaymatrixLevel" id="curPostPaymatrixLevel" size="5" maxlength="2" value="${tform.curPostPaymatrixLevel}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.curPostPaymatrixLevel}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:60px;">
                                <td>6. </td>
                                <td>Entry grade post and its corresponding Level in Pay Matrix</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <select name="entryDept" id="entryDept" onchange="getDeptWisePostList();" style="width:450px;">
                                            <option value="">--Select--</option>
                                            <c:if test="${not empty deptlist}">
                                                <c:forEach var="list" items="${deptlist}">
                                                    <option value="${list.deptCode}">${list.deptName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <select name="entryGpc" id="entryGpc">
                                            <option value="">--Select--</option>
                                            <c:if test="${not empty PostList6th}">
                                                <c:forEach var="postlist" items="${PostList6th}">
                                                    <option value="${postlist.postcode}" <c:if test="${not empty tform.entryGpc && tform.entryGpc == postlist.postcode}"> <c:out value='selected="selected"'/></c:if>>${postlist.post}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <input type="text" name="entryPaymatrixLevel" id="entryPaymatrixLevel" size="5" maxlength="2" value="${tform.entryPaymatrixLevel}" onkeypress="return onlyIntegerRange(event)"/><br />
					<span>
                                            N.B: Selected Post will be reflected in Print Copy.
                                        </span>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.entryPost}"/><br />
                                        <c:out value="${tform.entryPaymatrixLevel}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>7. </td>
                                <td>Existing Pay Band and Academic Grade Pay in which pay is drawn </td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <!--<input type="text" name="revisionPayScale" size="20" value="${tform.revisionPayScale}"/>-->
                                        <select name="revisionPayScale">
                                            <option value="">--Select--</option>
                                            <option value="4750-14680" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '4750-14680'}"> <c:out value='selected="selected"'/></c:if>>4750-14680</option>
                                            <option value="4930-14680" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '4930-14680'}"> <c:out value='selected="selected"'/></c:if>>4930-14680</option>
                                            <option value="5200-20200" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '5200-20200'}"> <c:out value='selected="selected"'/></c:if>>5200-20200</option>
                                            <option value="9300-34800" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '9300-34800'}"> <c:out value='selected="selected"'/></c:if>>9300-34800</option>
                                            <option value="15600-39100" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '15600-39100'}"> <c:out value='selected="selected"'/></c:if>>15600-39100</option>
                                            <option value="37400-67000" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '37400-67000'}"> <c:out value='selected="selected"'/></c:if>>37400-67000</option>
                                            <option value="67000-79000" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '67000-79000'}"> <c:out value='selected="selected"'/></c:if>>67000-79000</option>
                                            <option value="75500-80000" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '75500-80000'}"> <c:out value='selected="selected"'/></c:if>>75500-80000</option>
                                            <option value="80000" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '80000'}"> <c:out value='selected="selected"'/></c:if>>80000</option>
                                            <option value="90000" <c:if test="${not empty tform.revisionPayScale && tform.revisionPayScale == '90000'}"> <c:out value='selected="selected"'/></c:if>>90000</option>
                                        </select>
                                        <%--<input type="text" name="revisionGP" size="5" maxlength="5" value="${tform.revisionGP}"/>--%>
                                        <select name="revisionGP">
                                            <option value="">--Select--</option>
                                            <option value="0" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '0'}"> <c:out value='selected="selected"'/></c:if>>0</option>
                                            <option value="1700" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '1700'}"> <c:out value='selected="selected"'/></c:if>>1700</option>
                                            <option value="1775" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '1775'}"> <c:out value='selected="selected"'/></c:if>>1775</option>
                                            <option value="1800" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '1800'}"> <c:out value='selected="selected"'/></c:if>>1800</option>
                                            <option value="1900" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '1900'}"> <c:out value='selected="selected"'/></c:if>>1900</option>
                                            <option value="2000" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '2000'}"> <c:out value='selected="selected"'/></c:if>>2000</option>
                                            <option value="2200" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '2200'}"> <c:out value='selected="selected"'/></c:if>>2200</option>
                                            <option value="2400" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '2400'}"> <c:out value='selected="selected"'/></c:if>>2400</option>
                                            <option value="2800" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '2800'}"> <c:out value='selected="selected"'/></c:if>>2800</option>
                                            <option value="4200" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '4200'}"> <c:out value='selected="selected"'/></c:if>>4200</option>
                                            <option value="4600" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '4600'}"> <c:out value='selected="selected"'/></c:if>>4600</option>
                                            <option value="4800" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '4800'}"> <c:out value='selected="selected"'/></c:if>>4800</option>
                                            <option value="5400" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '5400'}"> <c:out value='selected="selected"'/></c:if>>5400</option>
                                            <option value="6000" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '6000'}"> <c:out value='selected="selected"'/></c:if>>6000</option>
                                            <option value="6600" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '6600'}"> <c:out value='selected="selected"'/></c:if>>6600</option>
                                            <option value="7000" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '7000'}"> <c:out value='selected="selected"'/></c:if>>7000</option>
                                            <option value="7600" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '7600'}"> <c:out value='selected="selected"'/></c:if>>7600</option>
                                            <option value="8000" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '8000'}"> <c:out value='selected="selected"'/></c:if>>8000</option>
                                            <option value="8700" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '8700'}"> <c:out value='selected="selected"'/></c:if>>8700</option>
                                            <option value="8800" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '8800'}"> <c:out value='selected="selected"'/></c:if>>8800</option>
                                            <option value="8900" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '8900'}"> <c:out value='selected="selected"'/></c:if>>8900</option>
                                            <option value="9000" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '9000'}"> <c:out value='selected="selected"'/></c:if>>9000</option>
                                            <option value="10000" <c:if test="${not empty tform.revisionGP && tform.revisionGP == '10000'}"> <c:out value='selected="selected"'/></c:if>>10000</option>
                                        </select>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.revisionPayScale}"/><br />
                                        <c:out value="${tform.revisionGP}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:60px;">
                                <td>8. </td>
                                <td>Existing Basic Pay (Pay + Academic Grade Pay)</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="revisionCurBasic" id="revisionCurBasic" size="10" maxlength="6" value="${tform.revisionCurBasic}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.revisionCurBasic}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>9. </td>
                                <td>Pay to be fixed in the Level of Pay Matrix </td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="basicpayFixPaymatrix" id="basicpayFixPaymatrix" size="5" maxlength="2" value="${tform.basicpayFixPaymatrix}" onkeypress="return onlyIntegerRange(event)"/>&ensp;
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.basicpayFixPaymatrix}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>10. </td>
                                <td>Date from which option exercised to come over to revised pay structure </td>
                                <td align="center">:</td>
                                <td>
                                    <c:out value="${tform.dateOptionExercised}"/>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>11. </td>
                                <td>
                                    Emoluments in the existing Pay Band and Academic Grade Pay on the date from which revised pay is opted. 
                                </td>
                                <td align="center">:</td>
                                <td>&nbsp;</td>   
                            </tr>
                            <tr style="height:60px;">
                                <td>&nbsp;</td>
                                <td>(a) Pay (including Personal Pay) </td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="basic" size="10" maxlength="6" id="basic" value="${tform.basic}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.basic}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:60px;">
                                <td>&nbsp;</td>
                                <td> (b) Academic Grade Pay</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <%--<input type="text" name="gp" size="10" id="gp" maxlength="5" value="${tform.gp}" onkeypress="return onlyIntegerRange(event)"/>--%>
                                        <select name="gp" id="gp">
                                            <option value="">--Select--</option>
                                            <option value="0" <c:if test="${not empty tform.gp && tform.gp == '0'}"> <c:out value='selected="selected"'/></c:if>>0</option>
                                            <option value="1700" <c:if test="${not empty tform.gp && tform.gp == '1700'}"> <c:out value='selected="selected"'/></c:if>>1700</option>
                                            <option value="1775" <c:if test="${not empty tform.gp && tform.gp == '1775'}"> <c:out value='selected="selected"'/></c:if>>1775</option>
                                            <option value="1800" <c:if test="${not empty tform.gp && tform.gp == '1800'}"> <c:out value='selected="selected"'/></c:if>>1800</option>
                                            <option value="1900" <c:if test="${not empty tform.gp && tform.gp == '1900'}"> <c:out value='selected="selected"'/></c:if>>1900</option>
                                            <option value="2000" <c:if test="${not empty tform.gp && tform.gp == '2000'}"> <c:out value='selected="selected"'/></c:if>>2000</option>
                                            <option value="2200" <c:if test="${not empty tform.gp && tform.gp == '2200'}"> <c:out value='selected="selected"'/></c:if>>2200</option>
                                            <option value="2400" <c:if test="${not empty tform.gp && tform.gp == '2400'}"> <c:out value='selected="selected"'/></c:if>>2400</option>
                                            <option value="2800" <c:if test="${not empty tform.gp && tform.gp == '2800'}"> <c:out value='selected="selected"'/></c:if>>2800</option>
                                            <option value="4200" <c:if test="${not empty tform.gp && tform.gp == '4200'}"> <c:out value='selected="selected"'/></c:if>>4200</option>
                                            <option value="4600" <c:if test="${not empty tform.gp && tform.gp == '4600'}"> <c:out value='selected="selected"'/></c:if>>4600</option>
                                            <option value="4800" <c:if test="${not empty tform.gp && tform.gp == '4800'}"> <c:out value='selected="selected"'/></c:if>>4800</option>
                                            <option value="5400" <c:if test="${not empty tform.gp && tform.gp == '5400'}"> <c:out value='selected="selected"'/></c:if>>5400</option>
                                            <option value="6000" <c:if test="${not empty tform.gp && tform.gp == '6000'}"> <c:out value='selected="selected"'/></c:if>>6000</option>
                                            <option value="6600" <c:if test="${not empty tform.gp && tform.gp == '6600'}"> <c:out value='selected="selected"'/></c:if>>6600</option>
                                            <option value="7000" <c:if test="${not empty tform.gp && tform.gp == '7000'}"> <c:out value='selected="selected"'/></c:if>>7000</option>
                                            <option value="7600" <c:if test="${not empty tform.gp && tform.gp == '7600'}"> <c:out value='selected="selected"'/></c:if>>7600</option>
                                            <option value="8000" <c:if test="${not empty tform.gp && tform.gp == '8000'}"> <c:out value='selected="selected"'/></c:if>>8000</option>
                                            <option value="8700" <c:if test="${not empty tform.gp && tform.gp == '8700'}"> <c:out value='selected="selected"'/></c:if>>8700</option>
                                            <option value="8800" <c:if test="${not empty tform.gp && tform.gp == '8800'}"> <c:out value='selected="selected"'/></c:if>>8800</option>
                                            <option value="8900" <c:if test="${not empty tform.gp && tform.gp == '8900'}"> <c:out value='selected="selected"'/></c:if>>8900</option>
                                            <option value="9000" <c:if test="${not empty tform.gp && tform.gp == '9000'}"> <c:out value='selected="selected"'/></c:if>>9000</option>
                                            <option value="10000" <c:if test="${not empty tform.gp && tform.gp == '10000'}"> <c:out value='selected="selected"'/></c:if>>10000</option>
                                        </select>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.gp}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:60px;">
                                <td>&nbsp;</td>
                                <td> (c) DA as on <c:out value="${tform.dateOptionExercised}"/></td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="da" size="10" id="da" maxlength="6" value="${tform.da}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.da}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:60px;">
                                <td>&nbsp;</td>
                                <td> (d) Total Emoluments (a to c)</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="totalpay" size="10" id="totalpay" maxlength="6" value="${tform.totalpay}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.totalpay}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:130px;">
                                <td>12. </td>
                                <td>Pay fixed in the revised pay structure by multiplying the existing basic pay (Sl.No.12) by a factor of 2.57 and rounded off to the nearest rupee. </td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="payrev257Basicpay" id="payrev257Basicpay" size="10" maxlength="6" value="${tform.payrev257Basicpay}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.payrev257Basicpay}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:80px;">
                                <td>13. </td>
                                <td>The Pay Cell in the appropriate Level in which the amount arrived at Sl no.12 is exactly fitted, if no such Cell 
                                    exact to the amount is available then the next above Cell in that Level
                                    <br/>
                                    OR
                                    <br/>
                                    If the amount so arrived is less than the first Cell in the Level then the pay is fitted at the first Cell of that Level. 
                                    (Cell No and the amount of pay be mentioned)
                                </td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        Cell Number <input type="text" name="payrevPaycell" id="payrevPaycell" size="5" maxlength="2" value="${tform.payrevPaycell}" onkeypress="return onlyIntegerRange(event)"/>
                                        <br>Appropriate Level <input type="text" name="payrevFittedLevel" id="payrevFittedLevel" size="3" maxlength="2" value="${tform.payrevFittedLevel}" onkeypress="return onlyIntegerRange(event)"/>
                                        <br>Fitted Amount <input type="text" name="payrevFittedAmount" id="payrevFittedAmount" size="6" maxlength="6" value="${tform.payrevFittedAmount}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        Cell Number: <c:out value="${tform.payrevPaycell}"/>
                                        <br>Appropriate Level: <c:out value="${tform.payrevFittedLevel}"/>
                                        <br>Fitted Amount: <c:out value="${tform.payrevFittedAmount}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>14. </td>
                                <td>Date of next increment </td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="doeIncr" class="txtDate" size="20" value="${tform.doeIncr}" readonly="true"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.doeIncr}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:60px;">
                                <td>15. </td>
                                <td>Any other relevant information </td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <textarea name="otherInfo" size="10">${tform.otherInfo}</textarea>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.otherInfo}"/>
                                    </c:if>
                                </td>
                            </tr>
                        </table>

                        <div style="margin-top: 80px;">
                            <table width="70%" cellspacing="0" cellpadding="0" border="0" align="center">
                                <tr> <td colspan="3" align="center"><b> Pay in the Cell in the Level after increment</b> </td></tr>
                            </table>
                            <table width="70%" cellspacing="0" cellpadding="0" border="0" align="center" id="incrTable">
                                <thead>
                                    <tr>
                                        <td width="25%">Date of increment </td>
                                        <td width="30%">Cell no and Pay and Pay </td>
                                        <td width="15%" align="center">Level</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty tform.incrementList}">
                                        <c:forEach var="incr" items="${tform.incrementList}">
                                            <c:if test="${tform.isApproved != 'Y'}">
                                                <tr>
                                                    <td>
                                                        <input type="text" name="incrDt" class="txtDate" size="20" value="${incr.incrDt}" readonly="true"/> 
                                                    </td>
                                                    <td align="center">
                                                        <input type="text" name="incrCell" id="incrCell" maxlength="2" size="5" value="${incr.cell}"/>
                                                        <input type="text" name="revisedbasic" id="revisedbasic" size="7" maxlength="6" value="${incr.revisedbasic}" onkeypress="return onlyIntegerRange(event)"/>
                                                        <input type="text" name="pp" id="pp" size="7" maxlength="6" value="${incr.pp}" onkeypress="return onlyIntegerRange(event)"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="incrLevel" id="incrLevel" maxlength="2" size="5" value="${incr.level}" onkeypress="return onlyIntegerRange(event)"/>
                                                        <button type="button" id="delete">Delete</button>
                                                    </td>
                                                </tr>
                                            </c:if>
                                            <c:if test="${tform.isApproved == 'Y'}">
                                                <tr>
                                                    <td>
                                                        <c:out value="${incr.incrDt}"/>
                                                    </td>
                                                    <td align="center">
                                                        <c:out value="${incr.cell}"/>&nbsp;
                                                        <c:out value="${incr.revisedbasic}"/>&nbsp;
                                                        <c:out value="${incr.pp}"/>
                                                    </td>
                                                    <td align="center">
                                                        <c:out value="${incr.level}"/>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                            </table>
                            <c:if test="${tform.isApproved != 'Y'}">
                                <button type="button" class="easyui-linkbutton" onclick="addRow();">Add Row</button>
                            </c:if>
                        </div>
                    </form>
                </div>

                <div style="margin-top: 30px;">
                    <table width="100%" cellspacing="0" cellpadding="0" border="0" style="border:0px;">
                        <tr>
                            <td style="border:0px;">Date :</td>  
                        </tr>
                        <tr>   
                            <td  style="border:0px;">Office:</td>
                        </tr>

                        <tr>   
                            <td  style="border:0px;text-align:right">Signature & Designation of Head of Office/ Competent Authority</td>
                        </tr>

                        <tr>   
                            <td  style="border:0px;text-align:right">&nbsp;</td>
                        </tr>
                        <tr>   
                            <td  style="border:0px;text-align:right">&nbsp;</td>
                        </tr>
                    </table>

                    <c:if test="${tform.isApproved != 'Y'}">
                        <div>
                            <button type="button" class="easyui-linkbutton" onclick="saveThirdSchedule('N');">Save as Draft</button>
                            <button type="button" class="easyui-linkbutton" onclick="saveThirdSchedule('Y');">Submit</button>
                        </div>
                        <div style="color:red;" style="padding-left: 50px;margin-top:20px;">
                            Save as Draft - Information will be saved but not Approved and can be edited.<br />
                            Submit - Information will be Submitted and cannot be edited.
                        </div>
                    </c:if>
                    <c:if test="${tform.isApproved == 'Y'}">
                        <div style="font-size:16px;color:red;">
                            APPROVED
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>
