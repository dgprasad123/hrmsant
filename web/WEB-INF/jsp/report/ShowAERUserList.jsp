<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="container" style="width:100%">
    <c:if test="${not empty userlist}">
        <c:forEach var="list" items="${userlist}">
            <div class="row">
                <div class="col-lg-5 col-xs-12 col-centered">
                    <c:out value="${list.label}"/>
                </div>
                <div class="col-lg-7 col-xs-12" style="border-left: 1px solid black;">
                    <c:out value="${list.value}"/>
                </div>
            </div>
            <hr />
        </c:forEach>
    </c:if>
</div>