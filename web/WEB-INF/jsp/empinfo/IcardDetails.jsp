<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        

        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script>
            function callNoImage() {

                var userPhoto = document.getElementById('loginUserPhoto');
                userPhoto.src = "images/NoEmployee.png";

            }
        </script>    

    </head>
    <body>
        <div class="container-fluid">
            <div >
                <div >

                    <table  align='center'  width='50%'  >
                        <tr>

                            <td  style='width:90%'>
                                <strong >Identity Card</strong>     <br/>
                                Id No: ${users.empId}
                            </td>
                            <td  ><img src="images/odgovt.gif" height="80" width="80"/></td>
                        </tr>

                        <tr  bgcolor="#888">
                            <th colspan='2' >
                        <div  style='color:white;paddding:5px;font-weight:bold;font-size:20px;text-align: center'>
                            Government Of Odisha<br/>
                            Home Department 
                        </div>        
                        </th>
                        </tr>
                        <tr>
                            <td align='center' width='100'  colspan='2' style='padding-top:2px'>
                                <img id="loginUserPhoto" style="border:1px solid #a3a183;padding:3px;" onerror="callNoImage()"  alt="ProfileImage" src='displayprofilephoto.htm?empid=${users.empId}' width="100" height="100" />

                            </td>
                        </tr>
                        <tr>
                            <td align='center'   colspan='2' style='padding-top:2px'>
                                <div style='color:#00ADBC;font-weight:bold'>${users.fullName}</div>
                            </td>
                        </tr>
                        <tr>
                            <td align='center' width='100'  colspan='2' >${users.postname}</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td valign="top">Department:</td>
                            <td>${users.offname}</td>
                        </tr> 
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <td valign="top">Date of Issue:</td>
                            <td>${cdate}</td>
                        </tr> 
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <td valign="top">Valid Upto:</td>
                            <td>${yearAdd}</td>
                        </tr> 
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <td valign="top">Emergency Contact:</td>
                            <td>${users.mobile}</td>
                        </tr>

                    </table>
                    <table  align='center'  width='50%'>                       


                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th style='width:50%'>Signature of <br/>Issuing Authority</th>
                            <th>Signature of <br/>Card Holder</th>
                        </tr> 
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                        </tr>
                    </table>    


                </div>

                <div style='clear:both;margin:40px'>&nbsp;</div>
                <div style="height:200px"></div>
                <table  align='center'  width='40%'>
                    <tr>
                        <td>Date of Birth:</td>
                        <td>${users.formattedDob}</td>
                    </tr> 
                    <tr>
                        <td>GPF / PRAN :</td>
                        <td><span style="color:#008000;font-weight: bold;" >(<c:out value="${users.acctType}"/>) </span><c:out value="${users.gpfno}"/></td>
                    </tr> 
                    <tr>
                        <td>Tel No.(0):</td>
                        <td></td>
                    </tr> 
                    <tr>
                        <td>Tel No.(R):</td>
                        <td></td>
                    </tr> 
                    <tr>
                        <td>Mobile:</td>
                        <td>${users.mobile}</td>
                    </tr>
                    <tr>
                        <td>Email:</td>
                        <td></td>
                    </tr>
                    <tr>
                        <th colspan='2'><hr style='border-width: 4px; border-style: inset;'/></th>
                    </tr>
                </table>
                <table align='center'  width='40%'>
                    <tr>
                        <th>&nbsp;</th>
                        <th  colspan='1' align='center'  style='color:#00ADBC;font-weight:bold'>INSTRUCTIONS</th>
                    </tr>

                    <tr>
                        <td align='right' valign="top">1&nbsp;</td>
                        <td>
                            Please surrender this card on expire/ retirement/ resignation/ transfer/ suspension/ dismissal/ removal from service. its loss should be reported immediately to the police and Director Secretariat Security 
                        </td>
                    </tr>
                    <tr>
                        <td align='right' valign="top">2&nbsp;</td>
                        <td>
                            Loss/mutilation/late renewal/ unauthorised retention etc.will entail penal consequences as per rules.
                        </td>
                    </tr>
                    <tr>
                        <td align='right' valign="top">3&nbsp;</td>
                        <td>
                            Please display this card while entering the Secretariat premises.
                        </td>
                    </tr>
                    <tr>
                        <td align='right' valign="top">4&nbsp;</td>
                        <td>
                            This card is not transferable
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>   
                </table>


            </div>
        </div>

    </body>
</html>
