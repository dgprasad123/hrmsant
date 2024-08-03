<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                getDeptWiseOfficeList();
            });
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidAuthDeptCode').val();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidAuthOffCode').empty();
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList() {
                var offcode = $('#hidAuthOffCode').val();

                var url = 'getPostListTrainingJSON.htm?deptcode=' + $('#hidAuthDeptCode').val() + '&offcode=' + offcode;
                $('#authSpc').empty();
                $('#authSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.value + '">' + obj.label + ' (' + obj.desc + ')</option>');
                    });
                });
            }
            function getPost(obj, idx) {
                $('#authName').val(obj[idx].text);
                $('#authVal').val(obj[idx].value);
            }
            function saveCheck()
            {
                if ($('#instituteCode').val() == '')
                {
                    alert("Please select Online Institute.");
                    $('#instituteCode')[0].focus();
                    return false;
                }

                if ($('#txtAmount').val() == '')
                {
                    alert("Please enter Course Fee.");
                    $('#txtAmount')[0].focus();
                    return false;
                }
                if (isNaN($('#txtAmount').val()))
                {
                    alert("Please enter a valid Integer Value.");
                    $('#txtAmount')[0].focus();
                    $('#txtAmount')[0].select();
                    return false;
                }
               /* if ($('#txtAmount').val() > 0)
                {
                    alert("You have already applied for the Online Training this Year.");
                    return false;
                }*/
                if($('#fromDate').val() == '')
                {
                    alert("Please enter From Date.");
                    return false;
                }
                if($('#toDate').val() == '')
                {
                    alert("Please enter To Date.");
                    return false;
                }                
                if ($('#authName').val() == '')
                {
                    alert("Please select the Authority.");
                    $('#authName')[0].focus();
                    return false;
                }

            }
            function getCourseDetail(id)
            {
                if (id == '')
                {
                    $('#courseDetailBlk').html("");
                    $('#courseDetailBlk').css('display', 'none');
                }
                $.ajax({
                    url: "GetCourseDetails.htm?courseId=" + id,
                    success: function(result) {
                        arrTemp = result.split('<==>');
                        result = '<strong>Institute Name: </strong>' + arrTemp[0] + '<br /><strong>Website: </strong>' + arrTemp[1] + '<br /><strong>Course Name: </strong>' + arrTemp[2]+ '<br /><strong>Course Type: </strong>' + arrTemp[3];
                       
                        if(arrTemp[3] == 'Completely Free')
                        {
                            $('#txtAmount').val(0);
                            $('#txtAmount').attr('readonly', true);
                        }
                        else
                        {
                            $('#txtAmount').val('');
                            $('#txtAmount').attr('readonly', false);                             
                        }
                        $('#courseDetailBlk').html(result);
                        $('#courseDetailBlk').css('display', 'block');
                    }
                });
            }
            $(document).ready(function() {


                $('#fromDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#toDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });                

            });
        </script>
    </head>
    <body>
        <form:form action="SaveOnlineTraining.htm" method="POST" commandName="OnlineTrainingBean" onsubmit="return saveCheck();">
            <input type="hidden" name="num_applied" id="num_applied" value="${numApplied}" />
            <input type="hidden" name="course_type" id="course_type" value="" />
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong style="color:#008900;">Apply Online</strong>
                    </div>
                    <div class="panel-body">

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="instituteCode">Institute Name</label>
                            </div>
                            <div class="col-lg-6">   
                                <form:select path="instituteCode" id="instituteCode" class="form-control" onchange="javascript: getCourseDetail(this.value)" style="width:80%;">
                                    <form:option value="">--Select Course--</form:option>
                                    <form:options items="${instituteList}" itemValue="instituteId" itemLabel="instituteDetail"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2" style="text-align:right;">

                            </div>
                            <div class="col-lg-6" id="courseDetailBlk" style="display:none;">

                            </div>                            
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtAmount">Course Fee<br /><span style="color:#FF0000;font-style:italic;">(Put 0 for Free Course)</span></label>
                            </div>
                            <div class="col-lg-2">
                                <input type="hidden" name="txtTrainingProgram" />
                                <form:input class="form-control" path="txtAmount" id="txtAmount" type="number" />
                            </div>                            
                        </div>                            

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtAmount">From Date:</label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date proDate' id='fromDateBlk'>
                                    <form:input class="form-control" path="fromDate" id="fromDate" readonly="readonly" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span></div>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtAmount">To Date:</label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date proDate' id='toDateBlk'>
                                    <form:input class="form-control" path="toDate" id="toDate" readonly="readonly" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span></div>
                            </div>                              
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="spc">Authority</label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="authName" id="authName" readonly="true"/>                           
                                <input type="hidden" name="authVal" id="authVal" />
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#authorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" name="submit" value="Save" class="btn btn-primary">Apply Online Training</button>
                    </div>
                </div>
            </div>

            <div id="authorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${sancOfflist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authSpc">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authSpc" id="authSpc" class="form-control" onchange="javascript: getPost(this, this.selectedIndex);">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${sancPostlist}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>                
        </form:form>

        <div id="online_list" style="width:95%;margin:0px auto;">
            <h1 style="color:#890000;font-size:15pt;margin-top:0px;">My Online Applications</h1>
            <table width="90%" class="table table-bordered">
                <tr style="font-weight:bold;background:#EAEAEA;">
                    <td>Sl#</td>
                    <td>Institute</td>
                    <td>Course Fee</td>
                    <td>From Date</td>
                    <td>To Date</td>
                    <td>Date Applied</td>
                    <td align="center">Upload Documents<br />(if Training Completed)</td>
                    <td align="center">Status</td>
                </tr>
                <c:forEach items="${trainingList}" var="list" varStatus="cnt">
                    <tr>
                        <td>${cnt.index+1}</td>
                        <td>${list.instituteName}</td>
                        <td>${list.fromDate}</td>
                        <td>${list.toDate}</td>
                        <td>${list.txtAmount}</td>
                        <td>${list.dateApplied}</td>
                        <td align="center">
                            <c:if test = "${empty list.certificatePath || empty list.receiptPath || empty list.intimationPath || empty list.bankPath}"><a href="UploadTrainingDocument.htm?id=${list.onlineTrainingId}">Upload Documents</a></c:if>
                            <c:if test = "${!empty list.intimationPath}"><a href="downloadOnlinePdf.htm?trainingId=${list.onlineTrainingId}&fileType=intimation" target="_blank" style="color:#008900;">Download Intimation Letter</a></c:if>
                            <c:if test = "${!empty list.receiptPath}"><br /><a href="downloadOnlinePdf.htm?trainingId=${list.onlineTrainingId}&fileType=receipt" target="_blank" style="color:#008900;">Download Money Receipt</a><br /></c:if>
                            <c:if test = "${!empty list.certificatePath}"><a target="_blank" href="downloadOnlinePdf.htm?trainingId=${list.onlineTrainingId}&fileType=certificate" style="color:#008900;">Download Certificate</a></c:if>
                            <c:if test = "${!empty list.bankPath}"><br /><a href="downloadOnlinePdf.htm?trainingId=${list.onlineTrainingId}&fileType=bank" target="_blank" style="color:#008900;">Download Bank Details</a></c:if>
                            </td>
                            <td align="center">${list.applicationStatus}</td>
                    </tr>                    
                </c:forEach>                
            </table>
        </div>
    </body>
</html>