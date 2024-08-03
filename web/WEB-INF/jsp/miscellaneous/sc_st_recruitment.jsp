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
                $('#postCode').empty();
                var url = 'getCadrewisePostList.htm?cadreId=' + $('#cadreCode').val();
                $('#postCode').append('<option value="">Select Post</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#postCode').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                    });
                });
            }
            function validate_textbox(val, id) {
                if (isNaN(val)) {
                    $("#" + id).val('');
                    return false;

                }

            }
            function display_req_details(vals) {
                if (vals == "No") {
                    $(".hide_req").hide();
                    $(".clear_data").val('');
                } else {
                    $(".hide_req").show();
                }

            }
            function display_advr(vals) {
                if (vals == "No") {
                    $(".adv_status_class").hide();
                    $(".advmentDetails").val('');
                } else {
                    $(".adv_status_class").show();
                }

            }
            function display_exam(vals) {
                if (vals == "No") {
                    $(".exam_status_class").hide();
                    $(".examDetails").val('');
                } else {
                    $(".exam_status_class").show();
                }

            }
            function display_result(vals) {
                if (vals == "No") {
                    $(".result_status_class").hide();
                    $(".resultDetails").val('');
                } else {
                    $(".result_status_class").show();
                }

            }
            function display_sponsor(vals) {
                if (vals == "No") {
                    $(".sponsor_class").hide();
                    $(".clear_class1").val('');
                } else {
                    $(".sponsor_class").show();
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptadminmenu.jsp"/>        
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
                                        <i class="fa fa-file"></i> SC/ST Recruitment
                                    </li>
                                </ol>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>SC/ST Recruitment</h2>
                                <form:form role="form" action="saveScStRecruitment.htm">

                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Department Name</label>
                                            <input name="deptname" type="text" class="form-control" id="deptname" value="${LoginUserBean.loginname}" readonly >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Month of Requisition<span style='color:red; font-weight:bold;'>*</span></label>
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
                                            <label for="exampleInputPassword1">Year of Requisition <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="year"  class="form-control" required="1">
                                                <option value="">Select Year</option>
                                                <option value="2011">2011 </option>
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
                                            <label for="exampleInputPassword1">Cadre <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="cadreCode"  class="form-control" id="cadreCode"  required="1">
                                                <option value="">Select Cadre</option>

                                                <c:forEach items="${cadreList}" var="cadre">
                                                    <option value="${cadre.value}">${cadre.label}</option>
                                                </c:forEach>
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Post Name <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="postCode"  class="form-control" id="postCode" required="1" >
                                                <option value="">Select Post</option>
                                                <c:forEach items="${postList}" var="post">
                                                    <option value="${post.postcode}">${post.post}</option>
                                                </c:forEach>
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Backlog vacancy ST <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="stVacancy" type="text" class="form-control" id="stVacancy"  required onkeyup="validate_textbox(this.value, this.id)">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Backlog vacancy SC <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="scVacancy" type="text" class="form-control" id="scVacancy"  required onkeyup="validate_textbox(this.value, this.id)">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Requsition Sent to the recruiting Agency <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="reqSentStatus"  class="form-control" required="1" onchange="display_req_details(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes">Yes</option>	
                                                <option value="No">NO</option>						
                                            </select>  
                                        </div>
                                        <div class="form-group hide_req" style='display:none'>
                                            <label for="exampleInputPassword1">Total No of Requsition Sent<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="resent" type="text" class="form-control clear_data" id="rsent"  >
                                        </div>
                                        <div class="form-group hide_req" style='display:none'>
                                            <label for="exampleInputPassword1">Total No of Sponsored<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="sponsored" type="text" class="form-control clear_data" id="sponsored"  >
                                        </div>
                                        <div class="form-group hide_req" style='display:none'>
                                            <label for="exampleInputPassword1">Total No of Post Vacant<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="postVacant" type="text" class="form-control clear_data" id="post_vacant"  >
                                        </div>
                                        <div class="form-group hide_req" style='display:none'>
                                            <label for="exampleInputPassword1">Letter No <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="letterNo" type="text" class="form-control clear_data" id="letterNo"  >
                                        </div>
                                        <div class="form-group hide_req" style='display:none'>
                                            <label for="exampleInputPassword1">Letter Date <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="letterDate" type="text" class="form-control clear_data" id="letterDate"  >
                                        </div>

                                        <div class="form-group hide_req" style='display:none'>
                                            <label for="exampleInputPassword1">Name of the Recruiting Agency<span style='color:red; font-weight:bold;'>*</span></label>

                                            <Select name="recruitAgency"  class="form-control clear_data" id="recruitAgency"  >
                                                <option value="">Select</option>
                                                <option value="OPSC">OPSC</option>	
                                                <option value="OSSC">OSSC</option>
                                                <option value="OSSSC">OSSSC</option>
                                            </select> 
                                        </div>
                                        <hr style='border:2px solid green;display:none' class='hide_req' /> 

                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Advertisement Done<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="advStatus"  class="form-control" required="1" onchange="display_advr(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes">Yes</option>	
                                                <option value="No">NO</option>						
                                            </select>  
                                        </div>

                                        <div class="form-group adv_status_class" style='display:none'>
                                            <label for="exampleInputPassword1">Advertisement Status(Advertisement no and date)</label>
                                            <input name="advmentDetails" type="text" class="form-control" id="advmentDetails"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Examination Conducted<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="examStatus"  class="form-control" required="1" onchange="display_exam(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes">Yes</option>	
                                                <option value="No">NO</option>						
                                            </select>  
                                        </div>

                                        <div class="form-group exam_status_class" style='display:none'>
                                            <label for="exampleInputPassword1">Examination Conducted (Examination date)</label>
                                            <input name="examDetails" type="text" class="form-control" id="examDetails"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Result Published <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="resultStatus"  class="form-control" required="1" onchange="display_result(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes">Yes</option>	
                                                <option value="No">NO</option>						
                                            </select>  
                                        </div>

                                        <div class="form-group result_status_class" style='display:none'>
                                            <label for="exampleInputPassword1">Result Published(Notification no and date)</label>
                                            <input name="resultDetails" type="text" class="form-control" id="resultDetails"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Sponsoring of candidate for Appointment <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="sponsoringStatus"  class="form-control" required="1" onchange="display_sponsor(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes">Yes</option>	
                                                <option value="No">NO</option>						
                                            </select>  
                                        </div>
                                        <div class="form-group sponsor_class" style='display:none'>
                                            <label for="exampleInputPassword1">Sponsoring of candidate for Appointment(Date & letter No)</label>
                                            <input name="appointmentDetails" type="text" class="form-control clear_class1" id="appointmentDetails"  >
                                        </div>
                                        <div class="form-group sponsor_class" style='display:none' >
                                            <label for="exampleInputPassword1">No Of  Candidates Sponsored For ST</label>
                                            <input name="stSponsored" type="text" class="form-control clear_class1" id="stSponsored" onkeyup="validate_textbox(this.value, this.id)" >
                                        </div>
                                        <div class="form-group sponsor_class" style='display:none'>
                                            <label for="exampleInputPassword1">No Of  Candidates Sponsored For SC</label>
                                            <input name="scSponsored" type="text" class="form-control clear_class1" id="scSponsored" onkeyup="validate_textbox(this.value, this.id)" >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Remarks</label>
                                            <input name="remarks" type="text" class="form-control" id="remarks"  >
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
