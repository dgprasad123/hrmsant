<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }
        </style>
    </head>
    <body style="background-color:white">
        <div class="container">
            <div class="form-group">
                <c:if test="${not empty revertStatus}">
                    <c:if test="${revertStatus ne 1}">
                        <span style="display:block;text-align: center;color: red;font-weight: bold; font-size: 16px;">
                            Third Schedule Revert is not allowed.
                        </span>
                    </c:if>
                    <c:if test="${revertStatus eq 1}">
                        <span style="display:block;text-align: center;color: green;font-weight: bold; font-size: 16px;">
                            Third Schedule Reverted successfully.
                        </span>
                    </c:if>
                </c:if>
            </div> 
        </div>
    </body>
</html>
