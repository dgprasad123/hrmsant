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
                    <c:if test="${payFixform.notType == 'PAYFIXATION'}">
                        Employee Pay Fixation
                    </c:if>
                    <c:if test="${payFixform.notType == 'PAYREVISION'}">
                        Employee Pay Revision
                    </c:if>
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Notification Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.txtNotOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Notification Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.txtNotOrdDt}"/>
                            </strong>                               
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Notifying Authority
                        </div>
                        <div class="col-lg-9">
                            Department: 
                            <strong>
                                <c:out value="${notideptname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Office: 
                            <strong>
                                <c:out value="${notioffice}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${payFixform.notifyingPostName}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            <label>Details of Pay</label>
                        </div>
                        <div class="col-lg-9">

                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Pay Commission
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.rdoPaycomm}"/>
                            </strong>
                        </div>                            
                        <div class="col-lg-2"></div>
                        <div class="col-lg-6"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;" id="div6pay">
                        <div class="col-lg-2">
                            (a) Revised Scale of Pay/Pay Band
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.sltPayScale}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (b) Grade Pay
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.txtGP}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;"  id="div7pay">
                        <div class="col-lg-2">
                            Pay Level
                        </div>
                        <div class="col-lg-2">   
                            <strong>
                                <c:out value="${payFixform.payLevel}"/>
                            </strong>  
                        </div>
                        <div class="col-lg-2">
                            Pay Cell
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.payCell}"/>
                            </strong>                               
                        </div>
                    </div>    

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (c) Revised Basic
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${payFixform.txtBasic}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (d) Personal Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${payFixform.txtPP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (e) Special Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${payFixform.txtSP}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (f) Other Emoluments Falling Under Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${payFixform.txtOP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (g) Description of Other Pay
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${payFixform.txtDescOP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (h) With Effect From Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.txtWEFDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            With Effect From Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.sltWEFTime}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (i) Date of Next Increment
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${payFixform.txtNextIncrementDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-8"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Note(if any)
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${payFixform.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                    <c:if test="${payFixform.notType eq 'PAYFIXATION'}">
                        <div class="row" style="margin-bottom: 12px;">
                            <div class="col-lg-2">
                                Reason of Pay Fixation
                            </div>
                            <div class="col-lg-3">
                                <strong>
                                    <c:out value="${payFixform.sltPayFixationReason}"/>
                                </strong>
                            </div>
                            <div class="col-lg-7"></div>
                        </div>
                    </c:if>
                    <div class="row" style="margin-bottom: 12px;"></div>
                    <div class="row">
                        <div class="col-lg-12">
                            <section class="panel" style="padding-bottom: 5px;">
                                <div class="panel-body" style="background-color: #F0BCB4;">
                                    <div class="col-md-1 form-group" align="center"> <b> Sl No </b> </div>
                                    <div class="col-md-2 form-group" align="center"> <b> With Effect</br> From Date </b> </div>
                                    <div class="col-md-1 form-group" align="center"> <b> Increment </br> Amount </b> </div>
                                    <div class="col-md-1 form-group" align="center"> <b> Personal Pay </b> </div>
                                    <div class="col-md-1 form-group" align="center"> <b> Other Pay </b> </div>
                                    <div class="col-md-1 form-group" align="center"> <b> Special Pay </b> </div>
                                    <div class="col-md-1 form-group" align="center"> <b> New Basic </b> </div>
                                    <div class="col-md-3 form-group" align="center"> 
                                        <b> Description of<br> Other Pay </b>
                                    </div>
                                </div>
                            </section>
                        </div>
                    </div>

                    <c:if test="${payFixform.retroIncrement.size() gt 0}">
                        <c:forEach items="${payFixform.retroIncrement}" varStatus="slnoCnt1">
                            <div id="addRow${slnoCnt1.index+1}" class="panel-body">
                                <div class="col-md-1 form-group"></div>
                                <div class="col-md-2 form-group "> 
                                    <div class="input-group date" id="processDate">
                                        <form:input path="retroIncrement[${slnoCnt1.index}].wefDate" id="wefDate${slnoCnt1.index+1}" class="form-control datepickertxt" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-md-1 form-group"><c:out value="retroIncrement[${slnoCnt1.index}].incrAmount"/></div>
                                <div class="col-md-1 form-group"><c:out value="retroIncrement[${slnoCnt1.index}].personalPay"/></div>
                                <div class="col-md-1 form-group"><c:out value="retroIncrement[${slnoCnt1.index}].otherPay"/></div>
                                <div class="col-md-1 form-group"><c:out value="retroIncrement[${slnoCnt1.index}].specialPay"/></div>
                                <div class="col-md-1 form-group"><c:out value="retroIncrement[${slnoCnt1.index}].newBasic"/></div>
                                <div class="col-md-3 form-group">
                                    <c:out value="retroIncrement[${slnoCnt1.index}].descOtherPay"/>
                                </div>
                                <div class="col-md-1 form-group"></div>
                            </div>
                        </c:forEach>
                    </c:if>
                </div>
                <div class="panel-footer">
                    <a href="PayFixationList.htm?notType=${payFixform.notType}" ><input type="button" name="btnPayFixation" value="Back" class="btn btn-primary"/> </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
