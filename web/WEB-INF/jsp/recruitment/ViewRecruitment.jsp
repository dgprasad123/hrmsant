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
                    Employee Recruitment
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Notification Type
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${recruitment.sltNotificationType}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Notification Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${recruitment.txtNotOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Notification Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${recruitment.txtNotOrdDt}"/>
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
                                <c:out value="${recruitment.notifyingPostName}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            <label>Details of Cadre, Grade and Post</label>
                        </div>
                        <div class="col-lg-9"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(a) Cadre Controlling Department
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${cadredeptname}"/>
                            </strong>                           
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(b) Name of the Cadre
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${cadrename}"/>
                            </strong>                           
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(c) Name of the Grade
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${recruitment.sltGrade}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(d) Cadre Level
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${recruitment.sltCadreLevel}"/>
                            </strong>                           
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(e) Description
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${recruitment.sltDescription}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(f) Allotment Year
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${recruitment.txtAllotmentYear}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(g) Cadre Id
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${recruitment.txtCadreId}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>
                    <br />
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-6">
                            <label>Please Fill up Column below if Post Details is Available</label>
                        </div>
                        <div class="col-lg-6"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(i) Posting Department
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${postingdeptname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(ii) Name of the Generic Post
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${genericpost}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                    <br />
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            <label>Post Classification</label>
                        </div>
                        <div class="col-lg-8">
                            <c:if test="${recruitment.rdPostClassification eq 'A'}">
                                <strong>
                                    Adhoc
                                </strong>
                            </c:if>
                            <c:if test="${recruitment.rdPostClassification eq 'T'}">
                                <strong>
                                    Temporary
                                </strong>
                            </c:if>
                            <c:if test="${recruitment.rdPostClassification eq 'O'}">
                                <strong>
                                    On Probation
                                </strong>
                            </c:if>
                            <c:if test="${recruitment.rdPostClassification eq 'P'}">
                                <strong>
                                    Permanent
                                </strong>
                            </c:if>
                            <c:if test="${empty recruitment.rdPostClassification}">
                                <strong>
                                    None
                                </strong>
                            </c:if>
                            <br />
                            <c:if test="${recruitment.rdPostStatus eq 'O'}">
                                <strong>
                                    Officiating
                                </strong>
                            </c:if>
                            <c:if test="${recruitment.rdPostStatus eq 'S'}">
                                <strong>
                                    Substantive
                                </strong>
                            </c:if>
                            <c:if test="${recruitment.rdPostStatus eq 'A'}">
                                <strong>
                                    None
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                    <br />
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-3">
                            Date of Effect of Joining in Cadre / Post
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${recruitment.txtCadreJoiningWEFDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${recruitment.sltCadreJoiningWEFTime}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            <label>Details of Pay</label>
                        </div>
                        <div class="col-lg-10"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (a) Scale of Pay/Pay Band
                        </div>
                        <div class="col-lg-5">
                            <strong>
                                <c:out value="${recruitment.sltPayScale}"/>
                            </strong>
                        </div>
                        <div class="col-lg-5"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (b) Grade Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${recruitment.txtGP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (c) Pay in Substantive Post
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${recruitment.txtBasic}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (d) Special Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${recruitment.txtSP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (e) Personal Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${recruitment.txtPP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (f) Other Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${recruitment.txtOP}"/>
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
                                <c:out value="${recruitment.txtDescOP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Date of Effect of Pay
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${recruitment.txtWEFDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${recruitment.sltWEFTime}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Note(if any)
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${recruitment.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="RecruitmentList.htm"><input type="button" name="btnRecruitment" value="Back" class="btn btn-default"/></a>
                </div>
            </div>
        </div>
    </body>
</html>
