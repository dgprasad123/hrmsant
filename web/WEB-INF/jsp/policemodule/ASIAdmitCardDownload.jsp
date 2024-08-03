<%-- 
    Document   : ASIAdmitCardDownload
    Created on : 25 Nov, 2020, 8:10:44 AM
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
        <link rel="stylesheet" href="/css/bootstrap.min.css">

        <title>admit Card</title>

        <style>
            .txt-center {
                text-align: center;
            }
            .border- {
                border: 1px solid #000 !important;
            }
            .padding {
                padding: 15px;
            }
            .mar-bot {
                margin-bottom: 15px;
            }
            .admit-card {
                border: 2px solid #000;
                padding: 10px;
                margin: 20px 0;
            }
            .BoxA h5, .BoxA p {
                margin: 0;
            }
            h5 {
                text-transform: uppercase;
            }

        </style>

    </head>
    <body>

        <section>
            <div class="container">
                <c:forEach items="${candidateList}" var="obj" varStatus="counter">
                    <div class="admit-card">
                        <div class="BoxA border- padding mar-bot txt-center">
                            <div class="row">
                                <div class="col-sm-12">

                                    <table width="100%">
                                        <tr>
                                            <td style="width:33%"> </td>
                                            <td style="width:33%"> <p>*** ADMIT CARD ***</p> </td>
                                            <td rowspan="3" style="font-size: 10px; text-align: center"><img src="images/OdishaPolice.png" width="100px;" /></td> 
                                        </tr>
                                        <tr>
                                            <td colspan="2" style="text-align: left; font-size: 10px;">WRITTEN EXAMINATION FOR PROMOTION TO THE RANK OF ASI OF POLICE FOR THE YEAR - 2022</td>
                                        </tr>
                                        <tr>
                                            <td style="text-align:left; font-size: 10px;">Hall Ticket No: <span style="font-weight: bold"> ${obj.admitCardRollNo} </span></td>
                                            <td style="text-align:left; font-size: 10px;">Exam Date & Time : <span style="font-weight: bold">  ${obj.examDate} </span></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="BoxA border- padding mar-bot" > 
                            <table width="100%" border="0" style="font-size:12px;">
                                <tr>
                                    <td> Name of the Exam Center </td>
                                    <td colspan="3"> : ${obj.sltCenter} </td>
                                    
                                </tr>
                                <tr>
                                    <td> </td>
                                    <td> </td>
                                    <td colspan="2" rowspan="4"> 
                                        <div class="col-sm-4 txt-center">

                                            <img src="displayNominatedEmployeeProfilePhoto.htm?nominationformId=${obj.nominationFormId}" id="candidatePhoto" width="100px;" height="100px;" border="1"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Name of the Candidate</td>
                                    <td> : ${obj.fullname}</td>

                                </tr>
                                <tr>
                                    <td> Name of the Father</td>
                                    <td> : ${obj.fathersname} </td>
                                </tr>
                                <tr>
                                    <td> Category </td>
                                    <td> : ${obj.category} </td>
                                </tr>
                                <tr>
                                    <td> Name of the District/Estt </td>
                                    <td colspan="3"> : ${obj.officeName} </td>
                                </tr>
                                <tr>
                                    <td style="width:35%"> &nbsp;  </td>
                                    <td style="width:35%"> &nbsp; </td>
                                    <td style="width:15%">  &nbsp; </td>
                                    <td style="width:15%"> &nbsp;  </td>
                                </tr>
                            </table>
                            <div class="row">


                                <table width="100%" border="0">
                                    
                                    <tr>
                                        <td style="width:33%"> Signature of the candidate </td>
                                        <td style="width:33%; text-align: center"> Signature of the Issuing Authority </td>
                                        <td style="width:34%; text-align: right"> Signature of the Invigilator </td>

                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="row">


                                <table width="100%" border="0">
                                    
                                    <tr>
                                        <td style="width:20%">  </td>
                                        <td style="width:60%; text-align: center;font-size: 11px;"> *** ODISHA POLICE *** </td>
                                        <td style="width:20%; text-align: right;font-size: 10px;"> Generated On: 22-NOV-2022 </td>

                                    </tr>
                                </table>
                            </div>        
                        


                    </div>
                    <c:if test="${(counter.index+1)%2==0}">
                        <p style="page-break-after: always;">&nbsp;</p>  
                    </c:if>
                </c:forEach>
            </div>

        </section>


        <!-- Optional JavaScript -->
        <!-- jQuery first, then Popper.js, then Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="js/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    </body>
</html>
