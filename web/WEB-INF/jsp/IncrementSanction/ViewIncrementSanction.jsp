<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Employee Increment
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Sanction Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtSanctionOrderNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Sanction Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtSanctionOrderDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Sanctioning Authority
                        </div>
                        <div class="col-lg-6">
                            Department: 
                            <strong>
                                <c:out value="${sancdeptname}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Office: 
                            <strong>
                                <c:out value="${sancoffice}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${incrementform.sancAuthPostName}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            With Effect From
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtWEFDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtWEFTime}"/>
                            </strong>                                
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Select Pay Commission
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.rdoPaycomm}"/>
                            </strong>
                        </div>
                        <div class="col-lg-8"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;" id="div6pay">
                        <div class="col-lg-2">
                            Scale of Pay/Pay Band
                        </div>
                        <div class="col-lg-5">
                            <strong>
                                <c:out value="${incrementform.sltPayScale}"/>
                            </strong>
                        </div>
                        <div class="col-lg-5"></div>
                    </div>
                    <div class="row" style="margin-bottom: 12px;" id="div6paygp">
                        <div class="col-lg-2">
                            Grade Pay
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtGradePay}"/>
                            </strong>
                        </div>
                        <div class="col-lg-8"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;" id="div7pay">
                        <div class="col-lg-2">
                            Pay Level
                        </div>
                        <div class="col-lg-2">   
                            <strong>
                                <c:out value="${incrementform.payLevel}"/>
                            </strong>   
                        </div>
                        <div class="col-lg-2">
                            Pay Cell
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.payCell}"/>
                            </strong>                               
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Increment Amount(in Rs.)
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtIncrAmt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Personal Pay(in Rs.)
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtP_pay}"/>
                            </strong>                             
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Other Pay(in Rs.)
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtOthPay}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Special Pay(in Rs.)
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtSPay}"/>
                            </strong>                          
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            New Basic(in Rs.)
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtNewBasic}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Desc. of Other Pay
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${incrementform.txtDescOth}"/>
                            </strong>                            
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Increment Level
                        </div>
                        <div class="col-lg-4">
                            <strong>
                                <c:out value="${incrementform.incrementLvl}"/>
                            </strong>
                        </div>
                        <div class="col-lg-4"></div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Increment Type
                        </div>
                        <div class="col-lg-4">
                            <strong>
                                <c:if test="${incrementform.incrementType eq 'A'}">
                                    Annual Increment
                                </c:if>
                                <c:if test="${incrementform.incrementType eq 'S'}">
                                    Stagnation Increment
                                </c:if>
                                <c:if test="${incrementform.incrementType eq 'D'}">
                                    Advance Increment
                                </c:if>
                                <c:if test="${incrementform.incrementType eq 'T'}">
                                    Antedated
                                </c:if>
                                <c:if test="${incrementform.incrementType eq 'P'}">
                                    Previous
                                </c:if>
                            </strong>
                        </div>
                        <div class="col-lg-4"></div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Note(if any)
                        </div>
                        <div class="col-lg-6">
                            <strong>
                                <c:out value="${incrementform.txtIncrNote}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="IncrementSanctionList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>
