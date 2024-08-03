<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/jquery.datetimepicker.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script  type="text/javascript">
            $(document).ready(function () {
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            });
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
            function check_training_status(vals) {
                if (vals == "Induction") {
                    $(".ind_class").show();
                    $(".job_class").hide();
                } else {
                    $(".ind_class").hide();
                    $(".job_class").show();
                }

            }
            function job_training(vals) {
                if (vals == 1) {
                    $("#ind_emp_list_details").show();
                    $("#job_emp_list_details").hide();
                } else {
                    $("#ind_emp_list_details").hide();
                    $("#job_emp_list_details").show();
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
                                        <i class="fa fa-file"></i> Training
                                    </li>
                                </ol>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>Training</h2>
                                <form:form role="form" action="updateTraining.htm">
                                    <input type='hidden' name="trainingId" value="${trainingForm.trainingId}"/>
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Department Name</label>
                                            <input name="deptname" type="text" class="form-control" id="deptname" value="${LoginUserBean.loginname}" readonly >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Month<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="month"  class="form-control" required="1">
                                                <option value="">Select Month</option>
                                                <option value="January"  <c:if test = "${not empty trainingForm.month && trainingForm.month=='January'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                                <option value="February"  <c:if test = "${not empty trainingForm.month && trainingForm.month=='February'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                                <option value="March" <c:if test = "${not empty trainingForm.month && trainingForm.month=='March'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                                <option value="April" <c:if test = "${not empty trainingForm.month && trainingForm.month=='April'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                                <option value="May" <c:if test = "${not empty trainingForm.month && trainingForm.month=='May'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                                <option value="June" <c:if test = "${not empty trainingForm.month && trainingForm.month=='June'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                                <option value="July" <c:if test = "${not empty trainingForm.month && trainingForm.month=='July'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                                <option value="August"  <c:if test = "${not empty trainingForm.month && trainingForm.month=='August'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                                <option value="September" <c:if test = "${not empty trainingForm.month && trainingForm.month=='September'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                                <option value="October" <c:if test = "${not empty trainingForm.month && trainingForm.month=='October'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                                <option value="November" <c:if test = "${not empty trainingForm.month && trainingForm.month=='November'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                                <option value="December" <c:if test = "${not empty trainingForm.month && trainingForm.month=='December'}"> <c:out value='selected="selected"'/></c:if>>December</option>

                                                </select>

                                            </div>
                                            <div class="form-group">
                                                    <label for="exampleInputPassword1">Year <span style='color:red; font-weight:bold;'>*</span></label>
                                                    <Select name="year"  class="form-control" required="1">
                                                            <option value="">Select Year</option>
                                                            <option value="2017" <c:if test = "${not empty trainingForm.year && trainingForm.year=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017 </option>
                                                     <option value="2018" <c:if test = "${not empty trainingForm.year && trainingForm.year=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                                    <option value="2019" <c:if test = "${not empty trainingForm.year && trainingForm.year=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                                    <option value="2020" <c:if test = "${not empty trainingForm.year && trainingForm.year=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                                     <option value="2021" <c:if test = "${not empty trainingForm.year && trainingForm.year=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                                     <option value="2022" <c:if test = "${not empty trainingForm.year && trainingForm.year=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                                     
                                                    </select>                 
                                            </div>    

                                            <div class="form-group">
                                                <label for="exampleInputPassword1">Cadre <span style='color:red; font-weight:bold;'>*</span></label>
                                                <select name="cadreCode" id="cadreCode"  class="form-control" onchange="getCadrewisePostList()" required>
                                                    <option value="">Select Cadre</option>

                                                    <option value="0"  <c:if test = "${not empty trainingForm.cadreCode && trainingForm.cadreCode=='0'}"> <c:out value='selected="selected"'/></c:if>>All</option>
                                                <c:forEach items="${cadreList}" var="cadre">
                                                    <option value="${cadre.value}"  <c:if test = "${not empty trainingForm.cadreCode && trainingForm.cadreCode==cadre.value}"> <c:out value='selected="selected"'/></c:if>>${cadre.label}</option>
                                                </c:forEach>
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Post Name <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input type='hidden' id='hpostid' value='${trainingForm.postName}' />
                                            <Select name="postCode" id="postCode"  class="form-control" required="1">

                                                <option value="">Select Post</option>						
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Total Number of Candidates appointed <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="totalAppointment" value="${trainingForm.totalAppointment}" type="text" class="form-control" id="totalAppointment"  required onkeyup="validate_textbox(this.value, this.id)" >
                                        </div>

                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Date of Appointment <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="appointmentDate" value="${trainingForm.appointmentDate}" type="text" class="form-control txtDate" id="appointmentDate"   >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Training Type <span style='color:red; font-weight:bold;'>*</span></label>
                                            <c:if test = "${not empty trainingForm.trainingType && trainingForm.trainingType=='Induction'}"> 
                                                <input type="radio" name="trainingType" checked id="training_type" value="Induction" onclick='check_training_status(this.value)'/>Induction&nbsp;
                                            </c:if>
                                            <c:if test = "${not empty trainingForm.trainingType && trainingForm.trainingType=='Job Training'}"> 
                                                <input type="radio" name="trainingType" checked id="training_type" value="Job Training" onclick='check_training_status(this.value)'/>On the Job Training
                                            </c:if>
                                        </div>



                                        <div class="form-group ind_class" >
                                            <label for="exampleInputPassword1" style='color:blue'>Induction  Training</label>

                                        </div>
                                       
                                            <div class="form-group ind_class" >
                                                <label for="exampleInputPassword1">Number of Employee Trained <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="empIndvTrained" value="${trainingForm.empIndvTrained}" type="text" class="form-control" id="empIndvTrained"   onkeyup="validate_textbox(this.value, this.id)" >
                                            </div>

                                            <div class="form-group ind_class" >
                                                <label for="exampleInputPassword1">Name of Institute <span style='color:red; font-weight:bold;'>*</span></label>
                                                <Select name="pinstitute" id="pinstitute"  class="form-control">
                                                    <option value="">Select Institute</option>
                                                    <c:forEach items="${DeInstList}" var="deinst">
                                                        <option value="${deinst.value}"  <c:if test = "${not empty trainingForm.pinstitute && trainingForm.pinstitute==deinst.value}"> <c:out value='selected="selected"'/></c:if>>${deinst.label}</option>
                                                    </c:forEach>

                                                </select>                 
                                            </div>
                                            <div class="form-group ind_class" >
                                                <label for="exampleInputPassword1">To Be Trained <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input type='button' name='ind_list' value='Choose Employee List For Induction Training' onclick="job_training('1')">
                                                <br/>
                                                <table id='ind_emp_list_details'  style='display:none'>
                                                    <tr>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Pratap Ku Dash&nbsp;<br/>

                                                        </td>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Debasis sahoo&nbsp;<br/>
                                                        </td>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Gagan Kumar Nanda&nbsp;<br/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Bikas Ku Dash&nbsp;<br/>

                                                        </td>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Manoj Ku sahoo&nbsp;<br/>
                                                        </td>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Supriya Kumar Nanda&nbsp;<br/>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="form-group ind_class" >
                                                <label for="exampleInputPassword1">Place of Training <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="trainingPlace" value="${trainingForm.trainingPlace}" type="text" class="form-control" id="trainingPlace" >
                                            </div>
                                            <div class="form-group ind_class" >
                                                <label for="exampleInputPassword1">From Date <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="pfdate" value="${trainingForm.pfdate}" type="text" class="form-control txtDate" id="pfdate" >
                                            </div>
                                            <div class="form-group ind_class">
                                                <label for="exampleInputPassword1">To Date <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="ptdate" value="${trainingForm.ptdate}"  type="text" class="form-control txtDate" id="ptdate" >
                                            </div>
                                            <div class="form-group ind_class" >
                                                <label for="exampleInputPassword1">Duration<span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="trainingDuration" value="${trainingForm.trainingDuration}" type="text" class="form-control" id="trainingDuration"  >
                                            </div>
                                            <div class="form-group indb_class" style="display:none">
                                                <label for="exampleInputPassword1">Total Number of Employee Training out of Candidates Appointed<span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="indtotalEmpTrained" value="${trainingForm.indtotalEmpTrained}" type="text" class="form-control" id="indtotalEmpTrained"  onkeyup="validate_textbox(this.value, this.id)" >
                                            </div>

                                      
                                      
                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1" style='color:blue'>On the Job Training</label>

                                            </div>
                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1">Number of Employee Trained <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="empJobTrained" value="${trainingForm.empJobTrained}" type="text" class="form-control" id="empJobTrained"   onkeyup="validate_textbox(this.value, this.id)" >
                                            </div>
                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1">To Be Trained <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input type='button' name='job_list' value='Choose Employee List For On Job Training'  onclick="job_training('2')" >
                                                <br/>
                                                <table id='job_emp_list_details' >
                                                    <tr>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Pratap Ku Dash&nbsp;<br/>

                                                        </td>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Debasis sahoo&nbsp;<br/>
                                                        </td>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Gagan Kumar Nanda&nbsp;<br/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Bikas Ku Dash&nbsp;<br/>

                                                        </td>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Manoj Ku sahoo&nsbp;<br/>
                                                        </td>
                                                        <td>
                                                            <input type='checkbox' name='ind_emp_list[]' value='1'/>Supriya Kumar Nanda&nsbp;<br/>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1">Name of Institute <span style='color:red; font-weight:bold;'>*</span></label>
                                                <Select name="jinstitute" id="jinstitute"  class="form-control" >
                                                    <option value="">Select Institute</option>
                                                    <c:forEach items="${DeInstList}" var="deinst">
                                                        <option value="${deinst.value}"  <c:if test = "${not empty trainingForm.jinstitute && trainingForm.jinstitute==deinst.value}"> <c:out value='selected="selected"'/></c:if>>${deinst.label}</option>
                                                    </c:forEach>

                                                </select>                 
                                            </div>
                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1">Place of Training <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="jobPlaceTraining" value="${trainingForm.jobPlaceTraining}" type="text" class="form-control" id="jobPlaceTraining">
                                            </div>
                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1">From Date <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="jfdate"  value="${trainingForm.jfdate}" type="text" class="form-control txtDate" id="jfdate" >
                                            </div>
                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1">To Date <span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="jtdate" value="${trainingForm.jtdate}" type="text" class="form-control txtDate" id="jtdate" >
                                            </div>
                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1">Duration<span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="jobDuration" value="${trainingForm.jobDuration}" type="text" class="form-control" id="jobDuration"  >
                                            </div>

                                            <div class="form-group job_class" >
                                                <label for="exampleInputPassword1">Total Number of Employee Training out of Candidates Appointed<span style='color:red; font-weight:bold;'>*</span></label>
                                                <input name="totalEmpTrained"   value="${trainingForm.totalEmpTrained}" type="text" class="form-control" id="totalEmpTrained"  onkeyup="validate_textbox(this.value, this.id)" >
                                            </div>
                                       
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Remarks</label>
                                            <input name="remarks"  value="${trainingForm.remarks}" type="text" class="form-control" id="remarks"   >
                                        </div>
                                    </div>                                    

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
            if ($('#cadreCode').val() != '') {
                getCadrewisePostList();
                //alert($('#hpostid').val());

            }


        </script> 
    </body>
</html>

