<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            
            function saveLanguage() {
                var language = $('#language').val();
                if (language == '') {
                    alert("Please select Language");
                    return false;
                }
                if (!$('#ifread').is(":checked") && !$('#ifwrite').is(":checked") && !$('#ifspeak').is(":checked")) {
                    alert("Please select at least one from Read/Write/Speak");
                    return false;
                }
               return true; 
            }
            
            function deleteLanguage(slno) {
                if (confirm("Are you sure to Delete?")) {
                    self.location = "deleteEmployeeLanguage.htm?slno=" + slno;
                } else {
                    return false;
                }
            }
            function editLanguage(slno){
                self.location = "editEmployeeSingleLanguage.htm?slno=" + slno;
            }
        </script>
    </head>
    <body>
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="LANGUAGEPAGESB" />
        </jsp:include>
        <form:form id="fm" action="saveEmployeeLanguage.htm" method="post" name="myForm" commandName="languagemodel">
            <form:hidden path="slno"/>
            <div style=" margin-bottom: 5px;" class="panel panel-info">
                <div class="panel-body">
                    <table border="0" cellpadding="0" cellspacing="0"  width="100%">
                        <tr>
                            <td width="5%">&nbsp;</td>
                            <td width="15%" >&nbsp;</td>
                            <td width="15%" align="center">&nbsp;</td>
                            <td width="15%" align="center">&nbsp;</td>
                            <td width="15%" align="center">&nbsp;</td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Language:</td>
                            <td>
                                <form:select path="language" id="language" style="width:50%;" class="form-control">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <form:options items="${languagelist}" itemLabel="langName" itemValue="langCode"/>
                                </form:select> 
                            </td>
                            <td>&nbsp; </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>If Read: <form:checkbox path="ifread" id="ifread" value="Y"/></td>
                            <td>If Write: <form:checkbox path="ifwrite" id="ifwrite" value="Y"/></td>
                            <td>If Speak: <form:checkbox path="ifspeak" id="ifspeak" value="Y"/></td>
                            <td>If Mother Tongue: <form:checkbox path="ifmlang" value="Y"/></td>
                        </tr>                        
                    </table>
                        
                    <div class="pull-left">
                        <input type="submit" name="save" value="Save" class="btn btn-primary" onclick="return saveLanguage();" />                        
                    </div>                    

                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th>#</th>
                                <th>Language</th>
                                <th>If read</th>
                                <th>If Write</th>
                                <th>If Speak</th>
                                <th>If Mother Tongue</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${emplanguageList}" var="emplanguage" varStatus="cnt">
                                <tr>
                                    <th scope="row">${cnt.index+1}</th>
                                    <td>${emplanguage.language}</td>
                                    <td>${emplanguage.ifread}</td>
                                    <td>${emplanguage.ifwrite}</td>
                                    <td>${emplanguage.ifspeak}</td>
                                    <td>${emplanguage.ifmlang}</td>
                                    <td>
                                        <c:if test="${emplanguage.isLocked eq 'N'}">
                                            <a href="javascript:editLanguage('${emplanguage.slno}');">Edit</a>&emsp;
                                            <a href="javascript:deleteLanguage('${emplanguage.slno}');">Delete</a>
                                        </c:if>
                                        <c:if test="${emplanguage.isLocked eq 'Y'}">
                                            <img src="images/Lock.png" width="20" height="20"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>

        </form:form>
    </body>
</html>
