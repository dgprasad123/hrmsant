<%-- 
    Document   : ASICenterwiseCandidateList
    Created on : 30 Nov, 2020, 12:25:51 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<!doctype html>
<html lang="en">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!-- Bootstrap CSS -->


        <title>Attendance List</title>

        <style type="text/css">

            td{
                border: 1px solid #000000;
            }
            .borderstyle{
                border: 1px solid #000000;
            }
        </style>
        <script type="text/javascript">
            function callNoImage() {
                var userPhoto = document.getElementById('userPhoto');
                userPhoto.src = "images/NoEmployee.png";
            }
        </script>

    </head>
    <body>

        <section>
            <div>
                <table width="100%"  border="0" cellspacing="0" cellpadding="0" style="border: none;">
                    <thead>

                        <tr> 
                            <th colspan="10">

                    <div style="text-align: center; font-size:18px;">WRITTEN EXAMINATION FOR ASI OF POLICE HELD ON 27-11-2022</div>
                    <div style="text-align: center; font-size:22px;">ATTENDANCE SHEET</div>
                    <div style="text-align: left; font-size:18px;">Name of  the Exam center : <span style="font-weight: normal;">${centerName}</span> </div>
                    <div style="text-align: left; font-size:18px;">Room No:</div>
                    <div style="text-align: left; font-size:18px;">Name of the Invigilator:</div>

                    </th>

                    </tr>

                    <tr style="height:35px;">
                        <th style="width:5%" class="borderstyle"> Sl No</th>
                        <th style="width:7%" class="borderstyle"> Hall Ticket No</th>
                        <th style="width:10%" class="borderstyle"> Name of the Candidate </th>
                        <th style="width:10%" class="borderstyle"> Dist/Estt. </th>
                        <th style="width:5%" class="borderstyle"> Paper-1 Series</th>
                        <th style="width:15%" class="borderstyle"> Signature of the Candidate</th>
                        <th style="width:5%" class="borderstyle"> Paper-2 Series</th>
                        <th style="width:15%" class="borderstyle"> Signature of the Candidate</th>
                        <th style="width:10%" class="borderstyle"> Initial of the Invigilator </th>
                        <th style="width:10%" class="borderstyle"> Remarks </th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${candidateList}" var="obj" varStatus="counter">
                            <tr style="height:33px;">
                                <td style="width:5%"> &nbsp;${counter.index+1} </td>
                                <td style="width:7%; text-align: left"> ${obj.admitCardRollNo} </td>
                                <td style="width:10%"> ${obj.fullname} </td>
                                <td style="width:10%"> ${obj.officeName} </td>
                                <td style="width:5%"> &nbsp; </td>
                                <td style="width:15%"> &nbsp; </td>
                                <td style="width:5%"> &nbsp; </td>
                                <td style="width:15%"> &nbsp; </td>
                                <td style="width:10%"> &nbsp; </td>
                                <td style="width:10%"> &nbsp; </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="5" style="width:50%;text-align: left; font-size:20px;border: none;">Signature of the Supervisory Officer</td>
                            <td colspan="5" style="width:50%;text-align: left; font-size:20px;border: none;">Signature of the Invigilator</td>
                        </tr>
                    </tfoot>
                </table>                
            </div>

        </section>    

    </body>
</html>
