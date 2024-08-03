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
                <div style="margin-top: 10px;"><u>Statement of fixation of pay under Central Civil Service (Revised Pay) Rules,2016</u></div>
                <div style="margin-top: 10px;">[See Rule - 7]</div>
                <div style="margin-top: 20px;">
                    <p style="color:red;font-weight:16px;">
                        UPDATE THE FIELDS WHEREVER NECESSARY.
                    </p>
                    <form method="POST" id="thirdScheduleDataForm" commandName="thirdScheduleForm">
                        <input type="hidden" name="empid" value="${tform.empid}"/>
                        <input type="hidden" name="isIASCadre" value="Y"/>
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
                                <td>Designation of the post in which pay is to be fixed as on January, 2016</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <select name="deptCodeIAS" id="deptCodeIAS" onchange="getDeptWisePostListIAS();" style="width:450px;">
                                            <option value="">--Select--</option>
                                            <c:if test="${not empty deptlist}">
                                                <c:forEach var="list" items="${deptlist}">
                                                    <option value="${list.deptCode}">${list.deptName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <select name="hooSpc" id="hooSpc">
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
                                <td>Status(Substantive/Officiating) </td>
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
                                <td>Pre-revised Pay Band and Grade Pay or Scale</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
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
                                        <select name="revisionGP" id="revisionGP">
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
                                <td>5. </td>
                                <td>
                                    Existing emoluments
                                </td>
                                <td align="center">:</td>
                                <td>&nbsp;</td>   
                            </tr>
                            <tr style="height:60px;">
                                <td>&nbsp;</td>
                                <td>(a) Basic Pay (Pay in the applicable Pay Band plus applicable Grade Pay or basic pay in the applicable scale) in the pre-revised structure as on ${tform.dateOptionExercised} </td>
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
                                <td> (b) Dearness Allowance sanctioned w.e.f <c:out value="${tform.dateOptionExercised}"/></td>
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
                                <td> (c) Existing Emoluments (a + b)</td>
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

                            <tr style="height:60px;">
                                <td>6. </td>
                                <td>Basic Pay (Pay in the applicable Pay Band plus applicable Grade Pay or basic pay in the applicable scale) in the pre-revised structure as on <c:out value="${tform.dateOptionExercised}"/></td>
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
                                <td>7. </td>
                                <td>Applicable Level in the Pay Matrix corresponding to the Pay Band and Grade Pay or Scale shown at S.No. 4</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="curPostPaymatrixLevel" id="curPostPaymatrixLevel" size="5" maxlength="3" value="${tform.curPostPaymatrixLevel}"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.curPostPaymatrixLevel}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:130px;">
                                <td>8. </td>
                                <td>Amount arrived at by multiplying Sl.No.5 by 2.57</td>
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
                                <td>9. </td>
                                <td>Applicable Cell in the Level either equal to or just above the Amount at Sl.No.8</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        Cell Number <input type="text" name="payrevPaycell" id="payrevPaycell" size="5" maxlength="2" value="${tform.payrevPaycell}" onkeypress="return onlyIntegerRange(event)"/>
                                        <br />Appropriate Level <input type="text" name="payrevFittedLevel" id="payrevFittedLevel" size="2" maxlength="3" value="${tform.payrevFittedLevel}"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        Cell Number: <c:out value="${tform.payrevPaycell}"/>
                                        <br />Appropriate Level: <c:out value="${tform.payrevFittedLevel}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:80px;">
                                <td>10. </td>
                                <td>Revised Basic Pay (as to Sl.No.9)</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="payrevFittedAmount" id="payrevFittedAmount" size="6" maxlength="6" value="${tform.payrevFittedAmount}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.payrevFittedAmount}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:80px;">
                                <td>11. </td>
                                <td>Stepped up with reference to the revised Pay of Junior, if applicable [Rule 7(8) and 7(10) of CCS(RP) Rules,2016].Name and Pay of the Junior also to be indicated distinctly.</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="juniorName" id="juniorName" value="${tform.juniorName}"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.juniorName}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:80px;">
                                <td>12. </td>
                                <td>Revised Pay with reference to the Substantive Pay in cases where the Pay fixed in the Officiating Post is lower than the Pay fixed in the Substantive Post if applicable [Rule 7(11)]</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="paySubstantive" id="paySubstantive" size="6" maxlength="6" value="${tform.paySubstantive}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.paySubstantive}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:80px;">
                                <td>13. </td>
                                <td>Personal Pay, if any [Rule 7(7) and 7(8)]</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="payPersonal" id="payPersonal" size="6" maxlength="6" value="${tform.payPersonal}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.payPersonal}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:80px;">
                                <td>14. </td>
                                <td>Non-Practicing Allowance as admissible at present in the existing pre-revised structure (in terms of para 4 of this OM)</td>
                                <td align="center">:</td>
                                <td>
                                    <c:if test="${tform.isApproved != 'Y'}">
                                        <input type="text" name="npAllowance" id="npAllowance" size="6" maxlength="6" value="${tform.npAllowance}" onkeypress="return onlyIntegerRange(event)"/>
                                    </c:if>
                                    <c:if test="${tform.isApproved == 'Y'}">
                                        <c:out value="${tform.npAllowance}"/>
                                    </c:if>
                                </td>
                            </tr>

                            <tr style="height:60px;">
                                <td>15. </td>
                                <td>Date of next increment (Rule 10) and Pay after grant of increment</td>
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
                        </table>    
                        <div style="margin-top: 80px;">
                            <table width="80%" cellspacing="0" cellpadding="0" border="0" align="center" id="incrIASTable">
                                <thead>
                                    <tr>
                                        <td width="25%">Date of increment</td>
                                        <td width="35%">Pay after increment in applicable Level of Pay Matrix</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>Basic&emsp;&emsp;Cell&emsp;&emsp;Level</td>
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
                                                    <td>
                                                        <input type="text" name="revisedbasic" id="revisedbasic" size="7" maxlength="6" value="${incr.revisedbasic}" onkeypress="return onlyIntegerRange(event)"/>&nbsp;
                                                        <input type="text" name="incrCell" id="incrCell" maxlength="2" size="5" value="${incr.cell}" onkeypress="return onlyIntegerRange(event)"/>&nbsp;
                                                        <input type="text" name="incrLevel" id="incrLevel" maxlength="3" size="5" value="${incr.level}"/>
                                                        <button type="button" id="delete">Delete</button>
                                                    </td>
                                                </tr>
                                            </c:if>

                                            <c:if test="${tform.isApproved == 'Y'}">
                                                <tr>
                                                    <td>
                                                        <c:out value="${incr.incrDt}"/>
                                                    </td>
                                                    <td>
                                                        <c:out value="${incr.revisedbasic}"/>&emsp;
                                                        <c:out value="${incr.cell}"/>&emsp;
                                                        <c:out value="${incr.level}"/>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                            </table>
                            <c:if test="${tform.isApproved != 'Y'}">
                                <button type="button" onclick="addRowIAS();" class="easyui-linkbutton">Add Row</button>
                            </c:if><br />
                            <table width="100%" cellspacing="0" cellpadding="0" border="0">
                                <tr>
                                    <td width="3%">16. </td>
                                    <td width="30%">Any other relevant information </td>
                                    <td width="3%" align="center">:</td>
                                    <td width="60%">
                                        <c:if test="${tform.isApproved != 'Y'}">
                                            <textarea name="otherInfo" rows="5" cols="40">${tform.otherInfo}</textarea>
                                        </c:if>
                                        <c:if test="${tform.isApproved == 'Y'}">
                                            <c:out value="${tform.otherInfo}"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div style="margin-top: 30px;">
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
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
