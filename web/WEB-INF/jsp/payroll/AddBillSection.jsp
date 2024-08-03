<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">

        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form class="form-inline" action="saveBillSection.htm" method="POST" commandName="command">
                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Manage Section Definition
                                    <input type="hidden" name="sectionId" value="${sectionId}" /></td>
                            </tr>
                            <tr>
                                <td align="right">Section Name:</td>
                                <td>                                       
                                    <input type="text" name="section" value="${section}" class="form-control" maxlength="49" style="width:100%" required />
                                </td>
                                <td align="right">No. of Posts:</td>
                                <td><input name="nofpost" type="number" value="${nofEmp}" class="form-control" required /></td>
                            </tr>
                            <tr>
                                <td align="right">Bill Type:</td>
                                <td colspan="3">${billType}<select name="billType" size="1" class="form-control" required>
                                        <c:if test="${isDHE eq 'N'}">
                                            <option value="">-Select-</option>
                                            <option value="REGULAR" <c:if test = "${not empty billType && billType=='REGULAR'}"> selected="selected"</c:if>>REGULAR</option>
                                            <option value="CONT6_REG" <c:if test = "${not empty billType && billType=='CONT6_REG'}"> selected="selected"</c:if>>CONTRACTUAL(SIX YR)</option>
                                            <option value="CONTRACTUAL" <c:if test = "${not empty billType && billType=='CONTRACTUAL'}"> selected="selected"</c:if>>CONTRACTUAL</option>
                                            <option value="XCADRE" <c:if test = "${not empty billType && billType=='XCADRE'}"> selected="selected"</c:if>>LEVEL-V(EX-CADRE)</option>
                                            <option value="SP_CATGORY" <c:if test = "${not empty billType && billType=='SP_CATGORY'}"> selected="selected"</c:if>>SPECIAL CATEGORY</option>
                                            <option value="DEPUTATION" <c:if test = "${not empty billType && billType=='DEPUTATION'}"> selected="selected"</c:if>>DEPUTATION</option>
                                        </c:if>
                                        <c:if test="${isDHE eq 'B'}">
                                            <option value="NONGOVTAID" <c:if test = "${not empty billType && billType=='NONGOVTAID'}"> selected="selected"</c:if>>NON GOVT. AIDED</option>
                                        </c:if>
                                    </select>
                                </td>
                            </tr>                        
                        </table>
                </div>
                <div class="panel-footer">                    
                    <input type="submit" value="Save Section Definition" class="btn btn-success" />
                </div>
                </form>
            </div>
        </div>
    </body>
</html>
