<%-- 
    Document   : ViewEnrollment
    Created on : 7 jan, 2022, 1:38:37 PM
    Author     : devi
--%>

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
        <style>
            .row-margin{
                margin-bottom: 16px;
            }
        </style>
    </head>
    <body style="font-family: Helvetica;">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Equivalent Post
                </div>
                <div class="panel-body">
                   
                    <div class="row row-margin">
                        <div class="col-lg-2">
                            Notification Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${dptnform.txtNotOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Notification Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${dptnform.txtNotOrdDt}"/>
                            </strong>                               
                        </div>
                    </div>

                    <div class="row row-margin">
                        <div class="col-lg-2">
                            Details of Notifying Authority
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${dptnform.radnotifyingauthtype eq 'GOO'}">
                                <strong>
                                    Government of Orissa
                                </strong>
                            </c:if>
                            <c:if test="${dptnform.radnotifyingauthtype eq 'GOI'}">
                                <strong>
                                    Government of India
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-2"></div>
                    </div>           

                    <div class="row row-margin">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${dptnform.radnotifyingauthtype eq 'GOO'}">
                                Department: 
                                <strong>
                                    <c:out value="${notideptname}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row row-margin">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${dptnform.radnotifyingauthtype eq 'GOO'}">
                                Office: 
                                <strong>
                                    <c:out value="${notioffice}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row row-margin">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${dptnform.notifyingSpc}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                     <div class="row row-margin">
                        <div class="col-lg-2">
                            Details of Posting
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${dptnform.radpostingauthtype eq 'GOO'}">
                                <strong>
                                    Government of Orissa
                                </strong>
                            </c:if>
                            <c:if test="${dptnform.radpostingauthtype eq 'GOI'}">
                                <strong>
                                    Government of India
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-2"></div>
                    </div>    

                    <div class="row row-margin">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${dptnform.radpostingauthtype eq 'GOO'}">
                                Department: 
                                <strong>
                                    <c:out value="${posteddeptname}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row row-margin">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${dptnform.radpostingauthtype eq 'GOO'}">
                                Office: 
                                <strong>
                                    <c:out value="${postedoffice}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row row-margin">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${dptnform.postedSpc}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
       
                            
                     <div class="row row-margin">
                        <div class="col-lg-2">
                            Name of Current Cadre Controlling Department
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${cadredeptname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    
                       <div class="row row-margin">
                        <div class="col-lg-2">
                            Name of Current Cadre
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${curcadrename}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    
                       <div class="row row-margin">
                        <div class="col-lg-2">
                            Name of Current Grade
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${dptnform.sltGrade}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>     
                    <div class="row row-margin">
                        <div class="col-lg-2">
                           Name of Equivalent Department
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${eqvdeptname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div> 
                    <div class="row row-margin">
                        <div class="col-lg-2">
                           Name of Equivalent Post
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${eqvpostname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>       
                     <div class="row row-margin">
                        <div class="col-lg-2">
                            Name of Equivalent Department
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${eqvalcadredeptname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    
                       <div class="row row-margin">
                        <div class="col-lg-2">
                            Name of Equivalent Cadre
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${eqvcadrename}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    
                       <div class="row row-margin">
                        <div class="col-lg-2">
                            Name of Equivalent Grade
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${dptnform.equvalGrade}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>            
                    
                    <div class="row row-margin">
                        <div class="col-lg-2">
                            Note
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${dptnform.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="EquivalentPost.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>

