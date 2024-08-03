<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                    Employee Joining
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            <label>Notification Order Details</label>
                        </div>
                        <div class="col-lg-6"></div>
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (a) Type
                        </div>
                        <div class="col-lg-6">
                            <strong>
                                <c:out value="${joinform.notType}"/>
                            </strong>
                        </div>
                        <div class="col-lg-4"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (b) Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.notOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (c) Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.notOrdDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (d) Department Name
                        </div>
                        <div class="col-lg-10">
                            <strong>
                                <c:out value="${joinform.notiDeptName}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (e) Office Name
                        </div>
                        <div class="col-lg-10">
                            <strong>
                                <c:out value="${joinform.notiOffName}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (f) Authority
                        </div>
                        <div class="col-lg-10">
                            <strong>
                                <c:out value="${joinform.notiSpn}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (g) Note
                        </div>
                        <div class="col-lg-10">
                            <strong>
                                <c:out value="${joinform.notNote}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-6">
                            <label>Relieve Order Details</label>
                        </div>
                        <div class="col-lg-6"></div>
                    </div>    

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (a) Relieve Report/Letter No.
                        </div>
                        <div class="col-lg-6">
                            <strong>
                                <c:out value="${joinform.rlvOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (b) Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.rlvOrdDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (c) Relieved On
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.rlvDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (d) Relieved Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.rlvTime}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (e) Due Date of Joining
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.joiningDueDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (f) Joining Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.joiningDueTime}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-6">
                            <label>Joining Order Details</label>
                        </div>
                        <div class="col-lg-6"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (a) Joining Report/Letter No.
                        </div>
                        <div class="col-lg-6">
                            <strong>
                                <c:out value="${joinform.joiningOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (b) Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.joiningOrdDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (c) Joined On
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.joiningDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (d) Joined Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.sltJoiningTime}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (e) Date of Effect of Pay
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.txtWEFDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (f) Unavailed joining Time Granted as EL
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.chkujt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-8"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (g) From Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.ujtFrmDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            (h) To Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${joinform.ujtToDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (i) Posting Details
                        </div>
                        <div class="col-lg-9">
                            Department:
                            <strong>
                                <c:out value="${posteddeptname}"/>
                            </strong>                    
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                    
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Office: 
                            <strong>
                                <c:out value="${postedoffice}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${joinform.spn}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                            
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            (j) Field Office
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${fieldoffice}"/>
                            </strong>                 
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            <label>Note(if any)</label>
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${joinform.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">                        
                    <a href="JoiningList.htm">
                        <button type="button" class="btn btn-default">Back</button>
                    </a>
                </div>
            </div>
        </div>
    </body>
</html>
