<%-- 
    Document   : SIParCadreEdit
    Created on : 24 Nov, 2023, 3:56:04 PM
    Author     : hp
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">    
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <link href="css/select2.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">

        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery2.0.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script> 
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.js"></script>
        <script src="js/select2.min.js"></script>

        <style>
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }
            .tblTrColor{
                background: rgb(174,238,209);
                background: radial-gradient(circle, rgba(174,238,209,0.9976191160057774) 0%, rgba(148,231,233,1) 100%);
                color: #000000;
                font-weight: bold;
            }
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .pageHeadingTxt{
                background-color: #DDEDE0;
                font-weight: bold;
                text-transform: uppercase;
                text-align: center;
                color:#000;
                font-size:18px;
                line-height: 20px;
                position: relative;
                letter-spacing: 0.2px;
                padding: 10px 0px;
            }
            .pageSubHeading{
                background-color: #DDEDE0;
                font-weight: bold;
                text-transform: uppercase;
                text-align: center;
                color:#0D8CE7;
                font-size:17px;
                line-height: 20px;
                position: relative;
                letter-spacing: 0.2px;
                padding: 10px 0px;
            }
            .myModalBody{}
            .new_sty{width:33%;}
            .new_sty li{width: 98%;}
            .new_sty li a{width: 100%;}
            .custom-select {
                width: 20%;
            }
            .custom-center {
                text-align: center;
            }
            .custom-dropdown {
                height: 40px;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                
                $(this).bind("contextmenu", function (e) {
                    e.preventDefault();
                });
                
            });
            
            function prepareAndUpdate() {
                if($("#curcadrecode").val() === "" )             
                {
                    alert("Please Select Current Cadre");
                    $("#curcadrecode").focus();
                    return false;
                }
                if($("#postGroupType").val() === "" )         
                {
                    alert("Please Select Post Group Type");
                    $("#postGroupType").focus();
                    return false;
                }
            }
        </script>

    </head>
    <body>
        <div id="wrapper"> 
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <form:form id="siParForm" action="updateSiParCadre.htm" method="POST" commandName="SIParCadreEdit"> 
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 style="text-align:center"> UPDATE CADRE DETAILS FOR SI & EQUIVALENT RANKS (GROUP - B) OFFICERS </h3>
                        </div>
                        
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-2"> <b style="font-size: 18px;">Hrms Id:</b></div>
                                <div class="col-lg-6" style="font-size: 18px;"> ${SIParCadreEdit.hrmsId} </div>
                                <div class="col-lg-2"> <form:hidden id="encHrmsId" path="encHrmsId" value="${SIParCadreEdit.encHrmsId}"/> </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-2"> <b style="font-size: 18px;">GPF / PRAN No:</b></div>
                                <div class="col-lg-6" style="font-size: 18px;"> ${SIParCadreEdit.gpfNumber} </div>
                                <div class="col-lg-2"> &nbsp; </div>
                            </div>    
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-2"> <b style="font-size: 18px;">Employee Name:</b></div>
                                <div class="col-lg-6" style="font-size: 18px;"> ${SIParCadreEdit.empName} &nbsp;<b style="color:#FF4500;">(${SIParCadreEdit.mobileNumber})</b> </div>
                                <div class="col-lg-2"> &nbsp; </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-2"> <b style="font-size: 18px;">D.O.B:</b></div>
                                <div class="col-lg-6" style="font-size: 18px;"> ${SIParCadreEdit.dob} </div>
                                <div class="col-lg-2"> &nbsp; </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-2"> <b style="font-size: 18px;">Current Designation:</b></div>
                                <div class="col-lg-6" style="font-size: 18px;"> ${SIParCadreEdit.spn} </div>
                                <div class="col-lg-2"> &nbsp; </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-2"> <b style="font-size: 18px;">Current Cadre:</b></div>
                                <div class="col-lg-6" style="font-size: 18px;color: #FF4500;"> ${SIParCadreEdit.cadreName} </div>
                                <div class="col-lg-2"> &nbsp; </div>
                            </div>    
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-2"> <b style="font-size: 18px;">Current Post Group</b></div>
                                <div class="col-lg-6" style="font-size: 18px;color: #FF4500;"> ${SIParCadreEdit.postGroupType} </div>
                                <div class="col-lg-2"> &nbsp; </div>
                            </div> 
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-2"> <b style="font-size: 18px;">Current Office:</b></div>
                                <div class="col-lg-6" style="font-size: 18px;"> ${SIParCadreEdit.officeName} </div>
                                <div class="col-lg-2"> &nbsp; </div>
                            </div>
                        </div>
                         
                        <hr class="thick-line">
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-2" align="right"> <b style="font-size: 17px;"> Current Cadre: </b> </div>            
                                <div class="col-lg-4">  
                                    <form:select path="curcadrecode" id="curcadrecode" class="form-control">
                                        <option value=""> -- Select -- </option>
                                        <option value="1484"> ODISHA POLICE-III </option>
                                    </form:select>
                                </div>
                                <div class="col-lg-2"> <b style="font-size: 17px;"> Post Group Type: </b> </div>
                                <div class="col-lg-4">  
                                    <form:select path="postGroupType" id="postGroupType" class="form-control">
                                        <option value=""> -- Select -- </option>
                                        <option value="B"> B </option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-8" align="center">  
                                    <c:if test="${ShowMsg eq 'Y'}">
                                        <b style="font-size: 20px;color: #007800">Data Updated Successfully</b>
                                    </c:if>
                                    <c:if test="${ShowMsg eq 'N'}">
                                        <b style="font-size: 20px;color: #FF4500">Data Updated Failed. Try Again</b>
                                    </c:if>    
                                </div>
                                <div class="col-lg-2"> &nbsp; </div>
                            </div>
                        </div>
                        
                    </div>
                            
                    <div class="panel-footer">
                        <div class="row" style="margin-bottom: 5px;">
                            <div class="btn-group col-lg-4"> &nbsp; </div>
                            <div class="col-lg-4" align="center">
                                <input type="submit" class="btn btn-success btn-lg" id="btn_Upd" name="Update" value="Update" onclick="return prepareAndUpdate();"/>
                            </div>
                            <div class="btn-group col-lg-4"> &nbsp; </div>
                        </div>
                    </div>        
                </div>
                            
            </form:form>
        </div>
    </body>
</html>
