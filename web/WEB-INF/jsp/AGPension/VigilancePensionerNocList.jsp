<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/sb-admin.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script language="javascript" type="text/javascript" >
            var currentRow;
            $(document).ready(function() {
                $('.date').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#id_tbl').DataTable({
                    "pageLength": 100,
                    "lengthMenu": [[100, 200, 300], [100, 200, 300, "All"]],
                    "dom": '<"pull-right" f><t>'
                });
                $('.dataTables_filter input[type="search"]').css(
                        {'width': '250px', 'display': 'inline-block'}
                );


            });

            function popupNOc(me) {
                currentRow = me;
                var dataURL = $(me).attr('data-href');
                $("#pensionModal").modal('show');
                $('.modal-body').load(dataURL, function() {
                    $('#pensionModal').modal({show: true});
                });
            }
            function showHideBlocks(val)
            {
                //alert(val);
                //for block 1
                if (val == 'hrmsid' || val == 'name')
                {
                    $("#blk1").show();
                }
                else
                {
                    $("#blk1").hide();
                }
                //for block 2
                if (val == 'post')
                {
                    $("#blk2").show();
                    $("#blk1").hide();
                }
                else
                {
                    $("#blk2").hide();

                }
                //for block 3 and block 4
                if (val == 'dtrange')
                {
                    $("#blk3").show();
                    $("#blk4").show();

                    $("#blk1").hide();
                    $("#blk2").hide();
                }
                else
                {
                    $("#blk3").hide();
                    $("#blk4").hide();
                }
                //for block 5
                if (val == 'dob')
                {
                    $("#blk5").show();
                    $("#blk1").hide();
                    $("#blk2").hide();
                    $("#blk3").hide();
                    $("#blk4").hide();
                }
                else
                {
                    $("#blk5").hide();
                }

                //for block 6
                if (val == 'dept')
                {
                    $("#blk6").show();
                    $("#blk1").hide();
                    $("#blk2").hide();
                    $("#blk3").hide();
                    $("#blk4").hide();
                    $("#blk5").hide();
                }
                else
                {
                    $("#blk6").hide();
                }
            }

        </script>

    </head>

    <c:choose>
        <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/AlfaMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper">
                    </c:when>
                    <c:when test = "${fn:contains(LoginUserBean.loginusername, 'paradmin')}">
                        <body style="margin-top:0px;background:#188B7A;">
                            <jsp:include page="../tab/ParMenu.jsp"/>  
                            <div id="wrapper"> 
                                <div id="page-wrapper" >
                                </c:when>
                                <c:otherwise>
                                    <body>
                                        <div id="wrapper">
                                            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
                                            <div id="page-wrapper">
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="container-fluid" style="padding-bottom: 125px;">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            <h2 style="color:  #0071c5;" align="center">${loginName} NOC ${nocStatusType} List</h2>
                                                        </div>
                                                    </div>

                                                </div>
                                                <form:form action="findApplication.htm" method="post" commandName="PensionNOCBean">
                                                    <div class="row" style="margin-top:20px" >
                                                        <div class="col-lg-1"  style="left:50px; font-weight: bold">From Date:</div>

                                                        <div class="col-lg-2" >

                                                            <div style="left:50px;" class='input-group date' id='processDate'>

                                                                <form:input class="date form-control" path="dateFrom" id="date_from" readonly="true"/>
                                                                <span class="input-group-addon"
                                                                      <span class="glyphicon glyphicon-time"></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                        <div class="col-lg-1" style="left:50px; font-weight: bold" >To Date:</div>
                                                        <div class="col-lg-2" >
                                                            <div class='input-group date' id='processDate'>
                                                                <form:input class="date form-control" path="dateTo" id="date_to" readonly="true"/>
                                                                <span class="input-group-addon">
                                                                    <span class="glyphicon glyphicon-time"></span>
                                                                </span>
                                                            </div>
                                                        </div>
                                                        <div class="col-lg-2" >
                                                            <input type="submit" name="action" value="Find" class="btn btn-success"/>
                                                        </div>     
                                                    </div>

                                                </form:form>

                                                <div class="panel-body">
                                                    <div class="table-responsive">
                                                        <!-- <form:form action="searchApplication.htm" method="post" commandName="SearchApplBean">
                                                         <div class="row">
                                                             <div class="col-lg-3">
                                                                 <div class="form-group">
                                                                     <label for="email">Search By:</label>
                                                                     <select class="form-control" onchange="showHideBlocks(this.value)" name="searchBy" id="searchBy">
                                                                         <option value="">--Select--</option>
                                                                         <option value="dept" ${SearchApplBean.searchBy == "dept" ? "selected='selected'": ''}>Department</option>
                                                                         <option value="post" ${SearchApplBean.searchBy == "post" ? "selected='selected'": ''}>Post</option>
                                                                         <option value="hrmsid" ${SearchApplBean.searchBy == "hrmsid" ? "selected='selected'": ''}>HRMS Id</option>
                                                                         <option value="name" ${SearchApplBean.searchBy == "name" ? "selected='selected'": ''}>Name</option>
                                                                         <option value="dob" ${SearchApplBean.searchBy == "dob" ? "selected='selected'": ''}>Date of Birth</option>
                                                                         <option value="dtrange" ${SearchApplBean.searchBy == "dtrange" ? "selected='selected'": ''}>Date Range(Date of Application)</option>
                                                                     </select>
                                                                 </div>
                                                             </div>
                                                             <div class="col-lg-3" id="blk1" style="${(SearchApplBean.searchBy == "hrmsid" || SearchApplBean.searchBy == "name") ? 'display:block': 'display:none'}">
                                                                 <div class="form-group">
                                                                     <label for="keyword">Keyword</label>
                                                                     <input type="text" name="keyword" id="keyword" class="form-control" value="${SearchApplBean.keyword}"/>
                                                                 </div>
                                                             </div>
                                                             <div class="col-lg-3" id="blk2" style="${(SearchApplBean.searchBy == "post") ? 'display:block': 'display:none'}">
                                                                 <div class="form-group">
                                                                     <label for="keyword">Choose a Post:</label>
                                                                     <select class="form-control" name="postcode" id="postcode">
                                                                         <option value="">--Select--</option>
                                                            <c:forEach var="eachPost" items="${postList}" varStatus="slnoCnt">
                                                                <option value="${eachPost.post}" ${SearchApplBean.postcode == eachPost.post ? "selected='selected'": ''}>${eachPost.post}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-lg-2" id="blk3" style="${(SearchApplBean.searchBy == "dtrange") ? 'display:block': 'display:none'}">
                                                    <div class="form-group">
                                                        <label for="keyword">Date From:</label>
                                                        <input type="date" name="date_from" id="date_from" class="form-control" value="${SearchApplBean.date_from}"/>
                                                        
                                                    </div>
                                                </div>
                                                <div class="col-lg-2" id="blk4" style="${(SearchApplBean.searchBy == "dtrange") ? 'display:block': 'display:none'}">
                                                    <div class="form-group">
                                                        <label for="keyword">Date To:</label>
                                                        <input type="date" name="date_to" id="date_to" class="form-control" value="${SearchApplBean.date_to}"/>
                                                    </div>
                                                </div>
                                                <div class="col-lg-2" id="blk5" style="${(SearchApplBean.searchBy == "dob") ? 'display:block': 'display:none'}">
                                                    <div class="form-group">
                                                        <label for="keyword">Date:</label>
                                                        <input type="date" name="dob" id="dob" class="form-control" value="${SearchApplBean.dob}"/>
                                                    </div>
                                                </div>
                                                <div class="col-lg-2" id="blk6" style="display:none">
                                                    <div class="form-group">
                                                        <label for="keyword">Department Name:</label>
                                                        <select class="form-control" name="dept_name" id="dept_name">
                                                            <option value="">--Select--</option>
                                                            <c:forEach var="eachDept" items="${departmentList}" varStatus="slnoCnt">
                                                                <option value="${eachDept.deptCode}" ${deptCode == eachDept.deptCode ? "selected='selected'": ''}>${eachDept.deptName}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-lg-3">
                                                    <div class="form-group">
                                                        <button type="submit" class="btn btn-primary" style="margin-top:25px;" onclick="searchAppl()">Submit</button>
                                                    </div>
                                                </div>
                                            </div>
                                                        </form:form>-->
                                                        <div class="table-responsive">
                                                            <table class="table table-bordered table-hover table-striped" id="id_tbl">
                                                                <thead>
                                                                    <tr style="background-color: #0071c5;color: #ffffff;">
                                                                        <th>Slno</th>
                                                                        <th>HRMS ID</th>
                                                                        <th>Name</th> 
                                                                        <th>Father's/Husband's Name</th>
                                                                        <th>DOB</th>
                                                                        <th>DOE</th>
                                                                        <th>POST</th>
                                                                        <th>Department</th>
                                                                        <th>NOC For</th>
                                                                        <th>Profile</th>
                                                                        <th>NOC Detail</th>  
                                                                        <th>Status / Action </th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach items="${pensionList}" var="pnoc" varStatus="count">
                                                                        <tr>                                    
                                                                            <td>${count.index + 1}</td>
                                                                            <td>${pnoc.hrmsid}</td>
                                                                            <td>${pnoc.name}</td>
                                                                            <td>${pnoc.fatherName}(${pnoc.relation})</td>
                                                                            <td>${pnoc.dob}</td>
                                                                            <td>${pnoc.doe}</td>
                                                                            <td>${pnoc.post}</td>
                                                                            <td>${pnoc.departmentName}</td>
                                                                            <td style='color:green;'>${pnoc.nocfor}</td>
                                                                            <td><a target="_blank" href="viewProfileVerificationControllerDC.htm?empId=${pnoc.hrmsid}">View Profile</a></td>
                                                                            <td>                                                                           
                                                                                <c:if test="${not empty pnoc.vigilanceNocReason}">
                                                                                    <a href="downloadAttachmentOfNOC.htm?nocId=${pnoc.nocId}" class="btn btn-default" >
                                                                                        <span class="glyphicon glyphicon-paperclip"></span>  Download document
                                                                                    </a> 
                                                                                </c:if>
                                                                                <c:if  test="${not empty pnoc.cbNocReason}">
                                                                                    <a href="downloadAttachmentOfNOCForCB.htm?nocId=${pnoc.nocId}" class="btn btn-default" >
                                                                                        <span class="glyphicon glyphicon-paperclip"></span>  Download document
                                                                                    </a> 
                                                                                </c:if>

                                                                                <c:if test="${ empty pnoc.vigilanceNocReason &&  pnoc.vNocStatus eq 'Y'}">
                                                                                    <a href="GeneratevNoc.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" class="btn btn-default" >
                                                                                        <span class="glyphicon glyphicon-paperclip"></span>  Download document
                                                                                    </a> 
                                                                                </c:if>
                                                                                <c:if  test="${ empty pnoc.cbNocReason &&  pnoc.cNocStatus eq 'Y' }">
                                                                                    <a href="GenerateNoc.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" class="btn btn-default" >
                                                                                        <span class="glyphicon glyphicon-paperclip"></span>  Download document
                                                                                    </a> 
                                                                                </c:if>

                                                                            </td>                                                                            
                                                                            <td>
                                                                                <c:if test="${empty pnoc.cbNocReason &&  pnoc.cNocStatus eq 'Y'  }">
                                                                                    <strong class=" text-success">NOC is Submited</strong>
                                                                                </c:if>
                                                                                <c:if test="${not empty pnoc.cbNocReason}">
                                                                                    <strong style='color:red'>NOC Request is Declined</strong>                                                                                
                                                                                </c:if>

                                                                                <c:if test="${empty pnoc.vigilanceNocReason &&  pnoc.vNocStatus eq 'Y'  }">
                                                                                    <strong class=" text-success">NOC is Submited</strong>
                                                                                </c:if>
                                                                                <c:if test="${not empty pnoc.vigilanceNocReason}">
                                                                                    <strong style='color:red'>NOC Request is Declined</strong>                                                                                
                                                                                </c:if>

                                                                                <c:if test="${not empty adminStatus &&  adminStatus eq 'N'}">
                                                                                    <c:if test="${(pnoc.vNocStatus eq 'N' && loginName eq 'Vigilance')  }">
                                                                                        <a href="javascript:void(0);" data-href="VigilanceNocUpload.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" onclick="popupNOc(this)" id='id_view' data-target="#pensionModal" class='text-danger'>Upload NOC</a>
                                                                                        <!--<a href="VigilanceNocUpload.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}"/><input type="button" name='button' value="Upload NOC" class="btn-danger" onclick="popupNOc()"/></a><!-- <!---->
                                                                                          <!--<a href="VigilanceNocUpload.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" data-remote="false" data-toggle="modal" title="Lock Bill" data-target="#pensionModal"  class='text-danger'><strong class=" text-danger">Upload NOC</strong></a>
                                                                                        -->
                                                                                    </c:if>


                                                                                    <c:if test="${pnoc.cNocStatus eq 'N' && loginName eq 'Crime Branch'}">
                                                                                        <a href="javascript:void(0);" data-href="VigilanceNocUpload.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" onclick="popupNOc(this)" id='id_view' data-target="#pensionModal" class='text-danger'>Upload NOC</a>
                                                                                       <!--<a href="VigilanceNocUpload.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" class='text-danger'><strong class=" text-danger">Upload NOC</strong></a>-->
                                                                                    </c:if>

                                                                                </c:if>&nbsp;






                                                                            </td>
                                                                        </tr>

                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </div>                
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="pensionModal" class="modal fade" role="dialog">
                                        <div class="modal-dialog" style="width:1000px;">
                                            <!-- Modal content-->
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                    <h4 class="modal-title">&nbsp;</h4>
                                                </div>
                                                <div class="modal-body">

                                                </div>
                                                <div class="modal-footer">                       
                                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                        </div>
                    </div>
            </div>
        </div>
    </body>
</html>
