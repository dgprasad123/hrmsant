<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script  type="text/javascript">
            function getCadrewisePostList() {
                $('#postId').empty();
                var url = 'getDeptwisePostList.htm?deptId=' + $('#departmentId').val();
                $('#postId').append('<option value="">Select Post</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#postId').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                    });
                });
            }
            function getDeptwiseCadreList() {
                $('#cadreId').empty();
                var url = 'getDeptewiseCadreList.htm?deptId=' + $('#departmentId').val();
                $('#cadreId').append('<option value="">Select Cadre</option>');
                // $('#cadreId').append('<option value="0">All</option>');

                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#cadreId').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function validate_textbox(val, id) {
                if (isNaN(val)) {
                    $("#" + id).val('');
                    return false;

                }
                var col_3 = $('#base_level_post').val();
                var col_4 = $('#post_filed_up').val();
                var col_5 = $('#post_requisition_sent').val();
                if (col_3 != '' && col_4 != '' && col_5 != '') {

                    var result = parseInt(col_3) - (parseInt(col_4) + parseInt(col_5));
                    $('#balance_posts').val(result);


                }
                var col_31 = $('#base_level_post1').val();
                var col_41 = $('#post_filed_up1').val();
                var col_51 = $('#post_requisition_sent1').val();
                if (col_31 != '' && col_41 != '' && col_51 != '') {

                    var result1 = parseInt(col_31) - (parseInt(col_41) + parseInt(col_51));
                    $('#balance_posts1').val(result1);


                }


            }
            function vacancy_cal() {
                var sanc_post = $('#sanc_post').val();
                var man_pos = $('#man_pos').val();
                if (sanc_post != '' && man_pos != '') {
                    var result = parseInt(sanc_post) - (parseInt(man_pos));
                    $('#vacancy').val(result);

                }

            }

        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/commissionmenu.jsp"/>        
            <!-- Site wrapper -->
            <div class="wrapper">            
                <div id="page-wrapper">
                    <div class="container-fluid">
                        <!-- Page Heading -->
                        <div class="row">
                            <div class="col-lg-12">                            
                                <ol class="breadcrumb">
                                    <li>
                                        <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                    </li>
                                    <li class="active">
                                        <i class="fa fa-file"></i> <a href="commissionPendingList.htm">Monthly Pending List</a>
                                    </li>                                    
                                    <li class="active">
                                        <i class="fa fa-file"></i> Selected Candidate Category
                                    </li>
                                </ol>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align='center'><u>${officename} Pending Requisitions</u></h3>
                                        <form:form role="form" action="saveCommissionPending.htm" commandName="selectedCommissionPending"  method="post">
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Department Name</label>
                                            <Select name="departmentId" id="departmentId"  class="form-control" onchange="javascript: getDeptwiseCadreList();
                                                    getCadrewisePostList()" required>
                                                <option value="">Select Department</option>
                                                <c:forEach items="${departmentList}" var="department">
                                                    <option value="${department.value}">${department.label}</option>
                                                </c:forEach>

                                            </select>  
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Group Name <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="groupName"  class="form-control">
                                                <option value="">Select Group</option>
                                                <option value="A">Group A </option>
                                                <option value="B">Group B</option>
                                                <option value="C">Group C</option>
                                                <option value="D">Group D</option>
                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Cadre <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="cadreId" id="cadreId"  class="form-control"  required>
                                                <option value="">Select Cadre</option>

                                                <c:forEach items="${cadreList}" var="cadre">
                                                    <option value="${cadre.value}">${cadre.label}</option>
                                                </c:forEach>

                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Post <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="postId" id="postId"  class="form-control" >
                                                <option value="">Select Post</option>						
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Month<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="month"  class="form-control" required="1">
                                                <option value="">Select Month</option>
                                                <option value="January">January </option>
                                                <option value="February">February</option>
                                                <option value="March">March</option>
                                                <option value="April">April</option>
                                                <option value="May">May</option>
                                                <option value="June">June</option>
                                                <option value="July">July</option>
                                                <option value="August">August</option>
                                                <option value="September">September</option>
                                                <option value="October">October</option>
                                                <option value="November">November</option>
                                                <option value="December">December</option>

                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Year <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="year"  class="form-control" required="1">
                                                <option value="">Select Year</option>
                                                <option value="2012">2012 </option>
                                                <option value="2013">2013 </option>
                                                <option value="2014">2014 </option>
                                                <option value="2015">2015 </option>
                                                <option value="2016">2016 </option>
                                                <option value="2017">2017 </option>
                                                <option value="2018">2018 </option>
                                                <option value="2019">2019 </option>
                                                <option value="2020">2020 </option>
                                                <option value="2021">2021 </option>
                                                <option value="2022">2022 </option>
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="NoofVan">Number Of Vacancy<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="noofVan" type="text" class="form-control" id="NoofVan"  required>
                                        </div>
                                        <div class="form-group">
                                            <label for="ReqDate">Date Of Receipt of Requisition <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="reqDate" type="date" class="form-control" id="ReqDate"  required>
                                        </div>

                                        <div class="form-group">
                                            <label for="AdvNo">Advertisement Number</label>
                                            <input name="advNo" type="text" class="form-control" id="AdvNo"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="AdvDate">Advertisement Date</label>
                                            <input name="advDate" type="date" class="form-control" id="AdvDate"  >
                                        </div> 
                                        <div class="form-group">
                                            <label for="Examdate">Date of Preliminary Examination</label>
                                            <input name="examdate" type="date" class="form-control" id="Examdate"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="Writtendate">Date of Written Examination</label>
                                            <input name="writtendate" type="date" class="form-control" id="Writtendate"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="VivavoceDate">Date of Viva-Voce/Skill Test</label>
                                            <input name="vivavoceDate" type="date" class="form-control" id="VivavoceDate"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="FinalResultDate">Date Of Publication Of Final Result</label>
                                            <input name="finalResultDate" type="date" class="form-control" id="FinalResultDate"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="SponsoringDate">Date Of Sponsoring of Candidates</label>
                                            <input name="sponsoringDate" type="date" class="form-control" id="FinalResultDate"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="CoursecaseDetails">Court Cases If Any</label>
                                            <textarea name="coursecaseDetails"   class="form-control" ></textarea>
                                        </div>

                                        <div class="form-group">
                                            <label for="remarks">Remarks</label>
                                            <input name="remarks" type="text" class="form-control" id="remarks"  value="${Appoint.remarks}" >
                                        </div>

                                    </div>
                                    <!-- /.box-body -->

                                    <div class="box-footer">
                                        <input type="submit" class="btn btn-primary" value="Submit"  />
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
