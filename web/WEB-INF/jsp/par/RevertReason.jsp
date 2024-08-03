<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
    </head>
    <body>
        <div style="padding:10px;width:80%;top:25%;left:10%;margin:0 auto;position:fixed;">
            <div style="width:80%;height:80%;background:#E5F0C9;margin:0 auto;position:relative;">
                <table style="width:100%;height:100%;">
                    <tr style="height:40px;">
                        <td align="center">
                            <span style="font-family:Verdana;font-size:16px;font-weight:bold;">Your PAR is reverted by Higher Authority</span>
                        </td>
                    </tr>
                </table>
                <p>
                <table style="width:100%;height:100%;font-family:Verdana;font-size:12px;">
                    <thead>
                        <tr>
                            <th width="40%">Reverted From</th>
                            <th width="60%">Reason for Revert</th>
                            <th width="60%">Reverted On</th>
                        </tr>
                    </thead>
                    <tr>
                        <td style="padding-left:10px;" align="center">
                            <c:out value="${authorityType}"/>
                            <br />
                            (<c:out value="${authorityName}"/>)
                        </td>
                        <td style="padding-left:10px;" align="center">
                            <span style="background:#E5F0C9;">
                                <c:out value="${revertRemaeks}"/>
                            </span>
                        </td>
                        <td style="padding-left:10px;" align="center">
                            <c:out value="${revertOnDate}"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
