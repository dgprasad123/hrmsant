<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form:form class="form-inline" action="browserAqData.htm" method="POST" commandName="command">
            <form:hidden path="aqslNo" value="${command.aqslNo}"/>
            <form:hidden path="nowDedn" value="${command.nowDedn}"/>
            <form:hidden path="adCode" value="${command.adCode}"/>
            <form:hidden path="adType" value="${command.adType}"/>
             <form:hidden path="dedType" value="${command.dedType}"/>
              <form:hidden path="billNo" value="${command.billNo}"/>
              <form:hidden path="adRefId" value="${command.adRefId}"/>
            <c:if test="${command.adType=='A'}">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Allowance Details</h4>
                </div>
                <div class="form-group">
                    <label >Allowance Type :</label><br>
                    ${command.adCode}
                </div><br>
                <div class="form-group">
                    <label >Enter Amount :</label><br>
                    <form:input path="dedAmt" class="form-control" id="dedAmt" placeholder="Enter Amount"/>
                </div><br>
            </c:if>
            <c:if test="${command.adType!='A'}">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Deduction Details</h4>
                </div>
                <div class="form-group">
                    <label >Deduction Type :</label><br>
                    ${command.adCode}
                </div><br>
                <div class="form-group">
                    <label >Enter Amount :</label><br>
                    <form:input path="dedAmt" class="form-control" id="dedAmt" placeholder="Enter Amount"/>
                </div><br>
                 <div class="form-group">
                    <label >Enter Instalment No :</label><br>
                    <form:input path="dedInstalNo" class="form-control" id="dedInstalNo" placeholder="Enter Instalment No"/>
                </div><br>
                <div class="form-group">
                    <label >Loan Description :</label><br>
                    <form:input path="refDesc" class="form-control" id="refDesc" placeholder="Enter Instalment Detail"/>
                </div><br>
                 <div class="form-group">
                    <label >Total Recovery Amount :</label><br>
                    <form:input path="totRecAmt" class="form-control" id="totRecAmt" placeholder="Enter Amount"/>
                </div><br>
                <div class="form-group">
                    <label >Policy No :</label><br>
                    <form:input path="policyNo" class="form-control" id="policyNo" placeholder="Enter Policy No"/>
                </div><br>
                <div class="form-group">
                    <label >Enter Instalment Count :</label><br>
                    <form:input path="dedInstalCount" class="form-control" id="dedInstalCount" placeholder="Enter Instalment Count"/>
                </div><br>
                
            </c:if>
            <input type="submit" value="Update" name="Update" class="btn btn-default"/>
        </form:form>
    </body>
</html>
