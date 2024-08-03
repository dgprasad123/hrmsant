<%-- 
    Document   : CreateAERData
    Created on : 5 Jun, 2018, 11:14:19 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <style>
            body {padding-top:50px;}

            .box {
                border-radius: 3px;
                box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
                padding: 10px 25px;
                text-align: right;
                display: block;
                margin-top: 60px;
            }
            .box-icon {
                background-color: #57a544;
                border-radius: 50%;
                display: table;
                height: 100px;
                margin: 0 auto;
                width: 100px;
                margin-top: -61px;
            }
            .box-icon span {
                color: #fff;
                display: table-cell;
                text-align: center;
                vertical-align: middle;
            }
            .info h4 {
                font-size: 26px;
                letter-spacing: 2px;
                text-transform: uppercase;
            }
            .info > p {
                color: #717171;
                font-size: 16px;
                padding-top: 10px;
                text-align: justify;
            }
            .info > a {
                background-color: #03a9f4;
                border-radius: 2px;
                box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
                color: #fff;
                transition: all 0.5s ease 0s;
            }
            .info > a:hover {
                background-color: #0288d1;
                box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.16), 0 2px 5px 0 rgba(0, 0, 0, 0.12);
                color: #fff;
                transition: all 0.5s ease 0s;
            }
        </style>
    </head>
    <body>
        
        <form:form action="createAER.htm" commandName="command">


            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center"> ${OffName} </h1>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year <p class="text-primary">${command.financialYear}</p>
                                    <form:hidden path="financialYear"/>
                                    
                                    <p class="text-danger">
                                        <c:if test="${status eq 'true'}" > For this Financial Year AER is already Created. </c:if>
                                    </p>
                                </h3> 
                            </div>
                        </div>    

                    </div>
                    <div class="panel-body">
                        <div class="container">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
                                    <div class="box">
                                        <div class="box-icon">
                                            <span class="fa fa-3x fa-user"></span>
                                        </div>
                                        <div class="info">
                                            <h4 class="text-center">Single CO</h4>
                                            <p>The Office getting their allotment from single Budget Controlling Officer</p>
                                            <c:if test="${status eq 'false'}"> 
                                                <c:if test="${disableSingleCO eq 'false'}">  
                                                    <a href="createAERforSingleCO.htm?financialYear=${command.financialYear}&singleCO=Y" class="btn">Create</a> 
                                                </c:if>
                                                <c:if test="${disableSingleCO eq 'true'}"> 
                                                    <a href="createAERforSingleCO.htm?financialYear=${command.financialYear}&singleCO=Y" class="btn disabled">Create</a> 
                                                </c:if>
                                            </c:if>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
                                        <div class="box">
                                            <div class="box-icon">
                                                <span class="fa fa-3x fa-users"></span>
                                            </div>
                                            <div class="info">
                                                <h4 class="text-center">Multiple CO</h4>
                                                <p>The Office getting their allotment from multiple Budget Controlling Officer.</p>
                                                <c:if test="${status eq 'false'}"> <a href="createAERforMultipleCO.htm?financialYear=${command.financialYear}&singleCO=N" class="btn">Create</a> </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <button type="submit" name="btnAer" value="Back" class="btn btn-primary">Back to List</button>
                        </div>
                    </div>
                </div>



        </form:form>
    </body>
</html>
