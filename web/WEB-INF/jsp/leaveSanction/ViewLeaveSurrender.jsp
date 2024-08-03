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
                    Employee Leave Surrender
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${leaveSurrenderForm.txtNotOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${leaveSurrenderForm.txtNotOrdDt}"/>
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
                                <c:out value="${leaveSurrenderForm.sanctioningPostName}"/>
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
                            <label>Period of Surrender</label>
                        </div>
                        <div class="col-lg-9"></div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(a) From Year
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${leaveSurrenderForm.sltFromYear}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(b) To Year
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${leaveSurrenderForm.sltToYear}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(c) Date From
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${leaveSurrenderForm.txtFrmDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(d) Date To
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${leaveSurrenderForm.txtToDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            &nbsp;&nbsp;(e) No. of Days<span style="color: red">*</span>
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${leaveSurrenderForm.txtDays}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2"></div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Note(if any)
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${leaveSurrenderForm.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="LeaveSurrenderList.htm"><button type="button" class="btn btn-default">Back</button></a> 
                </div>
            </div>
        </div>
    </body>
</html>
