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
                    Employee Sanction of Leave
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Notification Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtNotOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Notification Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtNotOrdDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Sanctioning Authority
                        </div>
                        <div class="col-lg-8">
                            Department: 
                            <strong>
                                <c:out value="${sancdeptname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2"></div>
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
                                <c:out value="${lform.sanctioningPostName}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Type of Leave
                        </div>
                        <div class="col-lg-5">
                            <strong>
                                <c:out value="${leaveTypeName}"/>
                            </strong>
                        </div>
                        <div class="col-lg-5"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            <label>Period of Sanction</label>
                        </div>
                        <div class="col-lg-9"></div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(a) Date From
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtFrmDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(b) Date To
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtToDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(c) Prefix From
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtPrefixFrom}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(d) Prefix To
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtPrefixTo}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(e) Suffix From
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtSuffixFrom}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(f) Suffix To
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtSuffixTo}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(g) Join Time From
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtJoinTimeFrom}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(h) Join Time To
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${lform.txtJoinTimeTo}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            If Medical Certificate Submitted
                        </div>
                        <div class="col-lg-8">
                            <c:if test="${lform.chkMedicalCertificate eq 'Y'}">
                                <strong>
                                    Yes
                                </strong>
                            </c:if>
                            <c:if test="${lform.chkMedicalCertificate eq 'N'}">
                                <strong>
                                    No
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            If Commutted
                        </div>
                        <div class="col-lg-8">
                            <c:if test="${lform.chkCommuted eq 'Y'}">
                                <strong>
                                    Yes
                                </strong>
                            </c:if>
                            <c:if test="${lform.chkCommuted eq 'N'}">
                                <strong>
                                    No
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            Update Cadre Status(JPR)
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${lform.chkUpdateCadreStatus}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            If Long Term Basis
                        </div>
                        <div class="col-lg-8">
                            <c:if test="${lform.chkIfLongTermBasis eq 'Y'}">
                                <strong>
                                    Yes
                                </strong>
                            </c:if>
                            <c:if test="${lform.chkIfLongTermBasis eq 'N'}">
                                <strong>
                                    No
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Note(if any)
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${lform.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="LeaveSanctionList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>
