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
                     $('#postCode')[0].value = $('#hpostid').val();
                });
            }
            function validate_textbox(val, id) {
                if (isNaN(val)) {
                    $("#" + id).val('');
                    return false;

                }
               
            }
            function display_req_details(vals){
                if(vals=="No"){
                    $(".hide_req").hide();
                    $(".clear_data").val('');
                } else {
                   $(".hide_req").show(); 
                }
                
            }
             function display_advr(vals){
                if(vals=="No"){
                    $(".adv_status_class").hide();
                    $(".advmentDetails").val('');
                } else {
                   $(".adv_status_class").show(); 
                }
                
            }
             function display_exam(vals){
                if(vals=="No"){
                    $(".exam_status_class").hide();
                    $(".examDetails").val('');
                } else {
                   $(".exam_status_class").show(); 
                }
                
            }
             function display_result(vals){
                if(vals=="No"){
                    $(".result_status_class").hide();
                    $(".resultDetails").val('');
                } else {
                   $(".result_status_class").show(); 
                }
                
            }
            function display_sponsor(vals){
                 if(vals=="No"){
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
                                <form:form role="form" action="updateScStRecruitment.htm">
                                 <input type='hidden' name="idRecruitment" value="${Appoint.idRecruitment}"/>
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Department Name</label>
                                            <input name="deptname" type="text" class="form-control" id="deptname" value="${LoginUserBean.loginname}" readonly >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Month of Requisition<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="month"  class="form-control" required="1">
                                                <option value="">Select Month</option>
                                                <option value="January"  <c:if test = "${not empty Appoint.month && Appoint.month=='January'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                                <option value="February"  <c:if test = "${not empty Appoint.month && Appoint.month=='February'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                                <option value="March" <c:if test = "${not empty Appoint.month && Appoint.month=='March'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                                <option value="April" <c:if test = "${not empty Appoint.month && Appoint.month=='April'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                                <option value="May" <c:if test = "${not empty Appoint.month && Appoint.month=='May'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                                <option value="June" <c:if test = "${not empty Appoint.month && Appoint.month=='June'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                                <option value="July" <c:if test = "${not empty Appoint.month && Appoint.month=='July'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                                <option value="August"  <c:if test = "${not empty Appoint.month && Appoint.month=='August'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                                <option value="September" <c:if test = "${not empty Appoint.month && Appoint.month=='September'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                                <option value="October" <c:if test = "${not empty Appoint.month && Appoint.month=='October'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                                <option value="November" <c:if test = "${not empty Appoint.month && Appoint.month=='November'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                                <option value="December" <c:if test = "${not empty Appoint.month && Appoint.month=='December'}"> <c:out value='selected="selected"'/></c:if>>December</option>

                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Year of Requisition <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="year"  class="form-control" required="1">
                                                <option value="">Select Year</option>
                                                <option value="2011" <c:if test = "${not empty Appoint.year && Appoint.year=='2011'}"> <c:out value='selected="selected"'/></c:if>>2011 </option>
                                                <option value="2012"  <c:if test = "${not empty Appoint.year && Appoint.year=='2012'}"> <c:out value='selected="selected"'/></c:if>>2012 </option>	
                                                <option value="2013" <c:if test = "${not empty Appoint.year && Appoint.year=='2013'}"> <c:out value='selected="selected"'/></c:if>>2013 </option>
                                                <option value="2014"  <c:if test = "${not empty Appoint.year && Appoint.year=='2014'}"> <c:out value='selected="selected"'/></c:if>>2014 </option>	
                                                <option value="2015" <c:if test = "${not empty Appoint.year && Appoint.year=='2015'}"> <c:out value='selected="selected"'/></c:if>>2015 </option>
                                                <option value="2016"  <c:if test = "${not empty Appoint.year && Appoint.year=='2016'}"> <c:out value='selected="selected"'/></c:if>>2016 </option>	
                                                <option value="2017" <c:if test = "${not empty Appoint.year && Appoint.year=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017 </option>
                                                <option value="2018"  <c:if test = "${not empty Appoint.year && Appoint.year=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                                  <option value="2019"  <c:if test = "${not empty Appoint.year && Appoint.year=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                                    <option value="2020"  <c:if test = "${not empty Appoint.year && Appoint.year=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                                     <option value="2021"  <c:if test = "${not empty Appoint.year && Appoint.year=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                                      <option value="2022"  <c:if test = "${not empty Appoint.year && Appoint.year=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Cadre <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="cadreCode"  class="form-control" id="cadreCode" onchange="getCadrewisePostList()" required="1">
                                                <option value="">Select Cadre</option>
                                                <option value="0"  <c:if test = "${not empty Appoint.cadreCode && Appoint.cadreCode=='0'}"> <c:out value='selected="selected"'/></c:if>>All</option>
                                                <c:forEach items="${cadreList}" var="cadre">
                                                    <option value="${cadre.value}" <c:if test = "${not empty Appoint.cadreCode && Appoint.cadreCode==cadre.value}"> <c:out value='selected="selected"'/></c:if>>${cadre.label}</option>
                                                </c:forEach>
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Post Name <span style='color:red; font-weight:bold;'>*</span></label>
                                             <input type='hidden' id='hpostid' value='${Appoint.postName}' />
                                            <Select name="postCode"  class="form-control" id="postCode" required="1" >
                                                <option value="">Select Post</option>						
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Backlog vacancy ST <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="stVacancy" type="text" class="form-control" id="stVacancy"  required onkeyup="validate_textbox(this.value, this.id)" value="${Appoint.stVacancy}">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Backlog vacancy SC <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="scVacancy" type="text" class="form-control" id="scVacancy"  required onkeyup="validate_textbox(this.value, this.id)" value="${Appoint.scVacancy}">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Requsition Sent to the recruiting Agency <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="reqSentStatus"  class="form-control" required="1" onchange="display_req_details(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes" <c:if test = "${not empty Appoint.reqSentStatus && Appoint.reqSentStatus=='Yes'}"> <c:out value='selected="selected"'/></c:if>>Yes</option>	
                                                <option value="No" <c:if test = "${not empty Appoint.reqSentStatus && Appoint.reqSentStatus=='No'}"> <c:out value='selected="selected"'/></c:if>>NO</option>						
                                            </select>  
                                        </div>
                                        <div class="form-group hide_req" >
                                            <label for="exampleInputPassword1">Total No of Requsition Sent<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="resent" type="text" class="form-control clear_data" id="rsent" value="${Appoint.resent}" >
                                        </div>
                                         <div class="form-group hide_req" >
                                            <label for="exampleInputPassword1">Total No of Sponsored<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="sponsored" type="text" class="form-control clear_data" id="sponsored"   value="${Appoint.sponsored}">
                                        </div>
                                         <div class="form-group hide_req" >
                                            <label for="exampleInputPassword1">Total No of Post Vacant<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="postVacant" type="text" class="form-control clear_data" id="post_vacant"   value="${Appoint.postVacant}">
                                        </div>
                                        <div class="form-group hide_req">
                                            <label for="exampleInputPassword1">Letter No <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="letterNo" type="text" class="form-control clear_data" id="letterNo"  value="${Appoint.letterNo}">
                                        </div>
                                        <div class="form-group hide_req" >
                                            <label for="exampleInputPassword1">Letter Date <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="letterDate" type="text" class="form-control clear_data" id="letterDate"  value="${Appoint.letterDate}">
                                        </div>
                                       
                                        <div class="form-group hide_req">
                                            <label for="exampleInputPassword1">Name of the Recruiting Agency<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="recruitAgency" type="text" class="form-control clear_data" id="recruitAgency"    value="${Appoint.recruitAgency}">
                                        </div>
                                         <hr style='border:2px solid green;display:' class='hide_req' /> 
                                         
                                          <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Advertisement Done<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="advStatus"  class="form-control" required="1" onchange="display_advr(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes" <c:if test = "${not empty Appoint.advStatus && Appoint.advStatus=='Yes'}"> <c:out value='selected="selected"'/></c:if>>Yes</option>	
                                                <option value="No" <c:if test = "${not empty Appoint.advStatus && Appoint.advStatus=='Yes'}"> <c:out value='selected="selected"'/></c:if>>NO</option>						
                                            </select>  
                                        </div>
                                         
                                        <div class="form-group adv_status_class" >
                                            <label for="exampleInputPassword1">Advertisement Status(Advertisement no and date)</label>
                                            <input name="advmentDetails" type="text" class="form-control" id="advmentDetails"   value="${Appoint.advmentDetails}">
                                        </div>
                                         <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Examination Conducted<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="examStatus"  class="form-control" required="1" onchange="display_exam(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes" <c:if test = "${not empty Appoint.examStatus && Appoint.examStatus=='Yes'}"> <c:out value='selected="selected"'/></c:if>>Yes</option>	
                                                <option value="No" <c:if test = "${not empty Appoint.examStatus && Appoint.examStatus=='No'}"> <c:out value='selected="selected"'/></c:if>>NO</option>						
                                            </select>  
                                        </div>
                                         
                                        <div class="form-group exam_status_class" >
                                            <label for="exampleInputPassword1">Examination Conducted (Examination date)</label>
                                            <input name="examDetails" type="text" class="form-control" id="examDetails"   value="${Appoint.examDetails}">
                                        </div>
                                         <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Result Published <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="resultStatus"  class="form-control" required="1" onchange="display_result(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes" <c:if test = "${not empty Appoint.resultStatus && Appoint.resultStatus=='Yes'}"> <c:out value='selected="selected"'/></c:if>>Yes</option>	
                                                <option value="No" <c:if test = "${not empty Appoint.resultStatus && Appoint.resultStatus=='No'}"> <c:out value='selected="selected"'/></c:if>>NO</option>						
                                            </select>  
                                        </div>
                                         
                                        <div class="form-group result_status_class" >
                                            <label for="exampleInputPassword1">Result Published(Notification no and date)</label>
                                            <input name="resultDetails" type="text" class="form-control" id="resultDetails"  value="${Appoint.resultDetails}" >
                                        </div>
                                         <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Sponsoring of candidate for Appointment <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="sponsoringStatus"  class="form-control" required="1" onchange="display_sponsor(this.value)">
                                                <option value="">Select</option>
                                                <option value="Yes" <c:if test = "${not empty Appoint.sponsoringStatus && Appoint.sponsoringStatus=='Yes'}"> <c:out value='selected="selected"'/></c:if>>Yes</option>	
                                                <option value="No" <c:if test = "${not empty Appoint.sponsoringStatus && Appoint.sponsoringStatus=='No'}"> <c:out value='selected="selected"'/></c:if>>NO</option>						
                                            </select>  
                                        </div>
                                        <div class="form-group sponsor_class" >
                                            <label for="exampleInputPassword1">Sponsoring of candidate for Appointment(Date & letter No)</label>
                                            <input name="appointmentDetails" type="text" class="form-control clear_class1" id="appointmentDetails"   value="${Appoint.appointmentDetails}" >
                                        </div>
                                        <div class="form-group sponsor_class"  >
                                            <label for="exampleInputPassword1">No Of  Candidates Sponsored For ST</label>
                                            <input name="stSponsored" type="text" class="form-control clear_class1" id="stSponsored" onkeyup="validate_textbox(this.value, this.id)"  value="${Appoint.stSponsored}">
                                        </div>
                                        <div class="form-group sponsor_class">
                                            <label for="exampleInputPassword1">No Of  Candidates Sponsored For SC</label>
                                            <input name="scSponsored" type="text" class="form-control clear_class1" id="scSponsored" onkeyup="validate_textbox(this.value, this.id)"  value="${Appoint.scSponsored}">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Remarks</label>
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
            <script type='text/javascript'>
                if($('#cadreCode').val()!=''){
                    getCadrewisePostList();
                    //alert($('#hpostid').val());
                   
                }
                
                
            </script>  
    </body>
</html>
