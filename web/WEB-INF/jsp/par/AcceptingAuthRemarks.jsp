<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
    </head>
    <body>
            <input type="hidden" name="parid" id="parid" value="${parid}"/>
            <input type="hidden" name="taskid" id="taskid" value="${taskid}"/>
            <div style="width:60%;height:55px;">
                <div style="float:left;margin-left:20px;">
                    <select name="sltAcceptingAuthorityRemarks" id="sltAcceptingAuthorityRemarks" class="easyui-combobox" style="width:220px;">
                        <option value="Accepted">I Accept</option>
                    </select>
                    <a href="javascript:acceptingAuthsave();" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">Save</a>
                    <a href="javascript:detailsCombo();" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" id="detailsCombo">Details</a>
                </div>
                 <%--<div style="float:left;margin-left:20px;">
                    <logic:notEqual value="0" name="ParApplyForm" property="grading">
                        <logic:equal value="1" name="ParApplyForm" property="grading">
                            <span style="font-weight:bold;font-size:30px;color:#FFA500;">*</span>
                        </logic:equal>
                        <logic:equal value="2" name="ParApplyForm" property="grading">
                            <span style="font-weight:bold;font-size:30px;color:#FFA500;">**</span>
                        </logic:equal>
                        <logic:equal value="3" name="ParApplyForm" property="grading">
                            <span style="font-weight:bold;font-size:30px;color:#FFA500;">***</span>
                        </logic:equal>
                        <logic:equal value="4" name="ParApplyForm" property="grading">
                            <span style="font-weight:bold;font-size:30px;color:#FFA500;">****</span>
                        </logic:equal>
                        <logic:equal value="5" name="ParApplyForm" property="grading">
                            <span style="font-weight:bold;font-size:30px;color:#FFA500;">*****</span>
                        </logic:equal>
                    </logic:notEqual>
                </div>--%>
            </div>
    </body>
</html>
