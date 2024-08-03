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
                    Employee Promotion
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            Notification Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${promotionForm.txtNotOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Notification Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${promotionForm.txtNotOrdDt}"/>
                            </strong>                                
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
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

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Office: 
                            <strong>
                                <c:out value="${notioffice}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${promotionForm.notifyingPostName}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            Allotment Description
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${promotionForm.sltAllotmentDesc}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2"></div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            If Retrospective Promotion
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${promotionForm.chkRetroPromotion eq 'Y'}">
                                <strong>
                                    Yes
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.chkRetroPromotion eq 'N'}">
                                <strong>
                                    No
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-2"></div>
                        <div class="col-lg-2"></div>
                    </div>    

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            <label>Details of Cadre, Grade and Post</label>
                        </div>
                        <div class="col-lg-9"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
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

                    <div class="row" style="margin-bottom: 10px;">
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

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(c) Name of the Grade
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${promotionForm.sltGrade}"/>
                            </strong>                           
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(d) Cadre Level
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${promotionForm.sltCadreLevel}"/>
                            </strong>                           
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(e) Description
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${promotionForm.sltDescription}"/>
                            </strong>                           
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(f) Allotment Year
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${promotionForm.txtAllotmentYear}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            &nbsp;&nbsp;(g) Cadre Id
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${promotionForm.txtCadreId}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>
                    <br />
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-6">
                            <label>If Post Details is Available</label>
                        </div>
                        <div class="col-lg-6"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
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

                    <div class="row" style="margin-bottom: 10px;">
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
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            <label>Post Classification</label>
                        </div>
                        <div class="col-lg-8">
                            <c:if test="${promotionForm.rdPostClassification eq 'A'}">
                                <strong>
                                    Adhoc
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.rdPostClassification eq 'T'}">
                                <strong>
                                    Temporary
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.rdPostClassification eq 'O'}">
                                <strong>
                                    On Probation
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.rdPostClassification eq 'P'}">
                                <strong>
                                    Permanent
                                </strong>
                            </c:if>
                            <c:if test="${empty promotionForm.rdPostClassification}">
                                <strong>
                                    None
                                </strong>
                            </c:if>
                            <br />
                            <c:if test="${promotionForm.rdPostStatus eq 'O'}">
                                <strong>
                                    Officiating
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.rdPostStatus eq 'S'}">
                                <strong>
                                    Substantive
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.rdPostStatus eq 'A'}">
                                <strong>
                                    None
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                    <br />
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            Date of Effect of Joining in Cadre / Post
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${promotionForm.txtCadreJoiningWEFDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Time
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${promotionForm.sltCadreJoiningWEFTime eq 'FN'}">
                                <strong>
                                    Fore Noon
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.sltCadreJoiningWEFTime eq 'AN'}">
                                <strong>
                                    After Noon
                                </strong>
                            </c:if>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            Update Cadre Status(JPR)
                        </div>
                        <div class="col-lg-8">
                            <strong>
                                <c:out value="${promotionForm.chkUpdateCadreStatus}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            Joined As Such
                        </div>
                        <div class="col-lg-8">
                            <c:if test="${promotionForm.chkJoinedAsSuch eq 'Y'}">
                                <strong>
                                    Yes
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.chkJoinedAsSuch ne 'Y'}">
                                <strong>
                                    No
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-3">
                            Proforma Promotion
                        </div>
                        <div class="col-lg-8">
                            <c:if test="${promotionForm.chkProformaPromotion eq 'Y'}">
                                <strong>
                                    Yes
                                </strong>
                            </c:if>
                            <c:if test="${promotionForm.chkProformaPromotion ne 'Y'}">
                                <strong>
                                    No
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                    <br />

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            <label>Details of Posting</label>
                        </div>
                        <div class="col-lg-9">
                            Department: 
                            <strong>
                                <c:out value="${posteddeptname}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Office: 
                            <strong>
                                <c:out value="${postedoffice}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${promotionForm.postedPostName}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>        

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            (a) Field Office:
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${fieldoffice}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            <label>Details of Pay</label>
                        </div>
                        <div class="col-lg-10"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            (a) Scale of Pay/Pay Band
                        </div>
                        <div class="col-lg-5">
                            <strong>
                                <c:out value="${promotionForm.sltPayScale}"/>
                            </strong>
                        </div>
                        <div class="col-lg-5"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            (b) Grade Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${promotionForm.txtGP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            (c) Pay in Substantive Post
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${promotionForm.txtBasic}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            (d) Special Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${promotionForm.txtSP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            (e) Personal Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${promotionForm.txtPP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            (f) Other Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${promotionForm.txtOP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            (g) Description of Other Pay
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${promotionForm.txtDescOP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            Post Group
                        </div>
                        <div class="col-lg-4">
                            <strong>
                                <c:out value="${promotionForm.sltPostGroup}"/>
                            </strong>
                        </div>
                        <div class="col-lg-6"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            Date of Effect of Pay
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${promotionForm.txtWEFDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${promotionForm.sltWEFTime}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2">
                            Note(if any)
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${promotionForm.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="PromotionList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>
