<%-- 
    Document   : modalUnmappedSpc
    Created on : Feb 17, 2022, 12:56:10 PM
    Author     : Madhusmita
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <script type="text/javascript">
            <%--function Validation() {
            var getSelectedValue = document.querySelector('input[name="rdSpc"]:checked');
                    if (getSelectedValue != null){
            alert("SPC is not selected");
                    return false;
            }--%>
            $(function() {
                $("#submit").click(function() {
                    if ($('input[type=radio][name=rdSpc]:checked').length == 0)
                    {
                        alert('Please Select SPC');
                        return false;
                    }
                });

            });
        </script>
    </head>
    <body>
        <form:form action="mappedToBlankSpc.htm" commandName="substantivePost" method="post" >
            <form:hidden path="hidEmpCode" id="hidEmpCode"/>    
            <table class="table" border="0"  cellspacing="0"  style="font-size:12px; font-family:verdana;">
                <thead>
                    <tr>
                        <th>Sl No</th>
                        <th>Substantive Post Code</th>
                        <th>Substantive Post Name</th>
                    </tr>
                </thead>
                <tbody id="blankspclist">
                    <c:if test="${not empty blankSPClist}">
                        <c:forEach items="${blankSPClist}" var="blankSPClist">
                            <tr>
                                <td style="padding-right:5px">
                                    <input type="radio" name="rdSpc" id="rdSpc" value=${blankSPClist.spc}>
                                    <%--<form:radiobutton path="rdSpc"/>${blankSPClist.spc}--%>

                                </td>
                                <td>${blankSPClist.spc}</td>
                                <td>${blankSPClist.spn}</td>
                            </tr>
                        </c:forEach>
                    </c:if>        
                    <c:if test="${empty blankSPClist}">
                        <tr>
                            <td colspan="3" style="color:red;font-size: 20px;text-align: center;"><h3>DATA NOT AVAILABLE</h3></td>
                        </tr>
                    </c:if>            
                </tbody>
            </table>            
            <div class="row" align="center">
                <input type="submit" name="submit"  id="submit" style="width:100px" class="btn btn-primary" value="Assign" onclick="" />
            </div>

        </form:form>
    </body>
</html>