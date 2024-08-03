<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script src="js/bootstrap.min.js"></script>

        <script language="javascript" type="text/javascript">
            function saveCheck() {

                var year = $('#sltYear').val();
                var month = $('#sltMonth').val();
                if (year == "") {
                    alert("Please select Year");
                    return false;
                }
                if (month == "") {
                    alert("Please select Month");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <form:form action="CreateBulkLICDivisionWiseExcel.htm" commandName="command">
                    <div class="container-fluid">
                        <table class="table" border="0" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
                            <tr style="height:40px;">
                                <td width="10%" align="center">
                                    Select Division
                                </td>
                                <td width="25%">
                                    <form:select path="sltDivision" id="sltDivision" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${divisionlist}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </td>
                            </tr>
                            <tr style="height:40px;">
                                <td>
                                    Select Year
                                </td>
                                <td>
                                    <form:select path="sltYear" id="sltYear" class="form-control" style="width:100px;">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="2010">2010</form:option>
                                        <form:option value="2011">2011</form:option>
                                        <form:option value="2012">2012</form:option>
                                        <form:option value="2013">2013</form:option>
                                        <form:option value="2014">2014</form:option>
                                        <form:option value="2015">2015</form:option>
                                        <form:option value="2016">2016</form:option>
                                        <form:option value="2017">2017</form:option>
                                        <form:option value="2018">2018</form:option>
                                        <form:option value="2019">2019</form:option>
                                        <form:option value="2020">2020</form:option>
                                        <form:option value="2021">2021</form:option>
                                        <form:option value="2022">2022</form:option>
                                        <form:option value="2023">2023</form:option>
                                        <form:option value="2024">2024</form:option>
                                    </form:select>
                                </td>
                                <td>
                                    Select Month
                                </td>
                                <td>
                                    <form:select path="sltMonth" id="sltMonth" class="form-control" style="width:100px;">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="0">JAN</form:option>
                                        <form:option value="1">FEB</form:option>
                                        <form:option value="2">MAR</form:option>
                                        <form:option value="3">APR</form:option>
                                        <form:option value="4">MAY</form:option>
                                        <form:option value="5">JUN</form:option>
                                        <form:option value="6">JUL</form:option>
                                        <form:option value="7">AUG</form:option>
                                        <form:option value="8">SEP</form:option>
                                        <form:option value="9">OCT</form:option>
                                        <form:option value="10">NOV</form:option>
                                        <form:option value="11">DEC</form:option>
                                    </form:select>
                                </td>
                                <td>
                                    <input type="submit" name="submit" value="Download Excel" class="btn btn-success" class="form-control"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>