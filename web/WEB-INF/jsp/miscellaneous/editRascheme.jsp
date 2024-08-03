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
        <script src="js/basicjavascript.js" type="text/javascript"></script>
        <script  type="text/javascript">
            function validate_textbox(val, id) {
                if (isNaN(val)) {
                    $("#" + id).val('');
                    return false;

                }
                var col_1 = $('#id1').val();
                var col_2 = $('#id2').val();

                if (col_1 != '' && col_2 != '') {

                    var result = (parseInt(col_1) + parseInt(col_2));
                    $('#id3').val(result);

                }
                var col_4 = $('#id4').val();
                var col_5 = $('#id5').val();
                if (col_4 != '' && col_5 != '') {

                    var result = (parseInt(col_4) + parseInt(col_5));
                    $('#id6').val(result);

                }
                var col_10 = $('#id10').val();
                var col_11 = $('#id11').val();
                if (col_10 != '' && col_11 != '') {

                    var result = (parseInt(col_10) + parseInt(col_11));
                    $('#id7').val(result);

                }
                var col_7 = $('#id7').val();
                var col_6 = $('#id6').val();
                if (col_7 != '' && col_6 != '') {

                    var result = (parseInt(col_7) + parseInt(col_6));
                    $('#id8').val(result);

                }
                var col_3 = $('#id3').val();
                var col_8 = $('#id8').val();
                if (col_3 != '' && col_8 != '') {

                    var result = (parseInt(col_3) - parseInt(col_8));
                    $('#id9').val(result);

                }

            }
        </script>
    </head>
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
                                    <i class="fa fa-file"></i>  Recruitment through  RA Scheme
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>New Rehabilitation</h2>

                            <form:form role="form" action="updateRascheme.htm">
                                <input type='hidden' name="raSchemeId" value="${appointStatus.raSchemeId}"/>
                                <div class="box-body">
                                    <div class="form-group">
                                        <label for="exampleInputEmail1">Department Name</label>
                                        <input name="deptname" type="text" class="form-control" id="deptname" value="${LoginUserBean.loginname}" readonly >
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Month<span style='color:red; font-weight:bold;'>*</span></label>
                                        <Select name="month"  class="form-control" required="1">
                                            <option value="">Select Month</option>
                                            <option value="January"  <c:if test = "${not empty appointStatus.month && appointStatus.month=='January'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                            <option value="February"  <c:if test = "${not empty appointStatus.month && appointStatus.month=='February'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                            <option value="March" <c:if test = "${not empty appointStatus.month && appointStatus.month=='March'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                            <option value="April" <c:if test = "${not empty appointStatus.month && appointStatus.month=='April'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                            <option value="May" <c:if test = "${not empty appointStatus.month && appointStatus.month=='May'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                            <option value="June" <c:if test = "${not empty appointStatus.month && appointStatus.month=='June'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                            <option value="July" <c:if test = "${not empty appointStatus.month && appointStatus.month=='July'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                            <option value="August"  <c:if test = "${not empty appointStatus.month && appointStatus.month=='August'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                            <option value="September" <c:if test = "${not empty appointStatus.month && appointStatus.month=='September'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                            <option value="October" <c:if test = "${not empty appointStatus.month && appointStatus.month=='October'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                            <option value="November" <c:if test = "${not empty appointStatus.month && appointStatus.month=='November'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                            <option value="December" <c:if test = "${not empty appointStatus.month && appointStatus.month=='December'}"> <c:out value='selected="selected"'/></c:if>>December</option>

                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Year <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="year"  class="form-control" required="1">
                                                <option value="">Select Year</option>
                                                <option value="2017" <c:if test = "${not empty appointStatus.year && appointStatus.year=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017 </option>
                                              <option value="2018"  <c:if test = "${not empty appointStatus.year && appointStatus.year=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                              <option value="2019" <c:if test = "${not empty appointStatus.year && appointStatus.year=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                              <option value="2020"  <c:if test = "${not empty appointStatus.year && appointStatus.year=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                               <option value="2021"  <c:if test = "${not empty appointStatus.year && appointStatus.year=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                               <option value="2022"  <c:if test = "${not empty appointStatus.year && appointStatus.year=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                        </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">No of RA Cases till last month <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="lastmonthPending"  value="${appointStatus.lastmonthPending}" type="text" class="form-control" id="id1"  required onkeyup="validate_textbox(this.value, this.id)" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">No of cases instituted during the month <span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="institutedCase"  value="${appointStatus.institutedCase}" type="text" class="form-control" id="id2"  required onkeyup="validate_textbox(this.value, this.id)" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Total No.of cases under R.A Scheme <span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="totalCase"  value="${appointStatus.totalCase}" type="text" class="form-control" id="id3"  required onkeyup="validate_textbox(this.value, this.id)" required readonly="1">
                                    </div>

                                    <div class="form-group">
                                        <label for="exampleInputPassword1">No. of cases considered for appointment during the month<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="totalAppointment"  value="${appointStatus.totalAppointment}" type="text" class="form-control" id="id4" onkeyup="validate_textbox(this.value, this.id)" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">No. of cases considered and rejected during the month <span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="totalRejected"  value="${appointStatus.totalRejected}" type="text" class="form-control" id="id5" onkeyup="validate_textbox(this.value, this.id)" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Total no of cases disposed during the month<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="disposedcase"  value="${appointStatus.disposedcase}" type="text" class="form-control" id="id6" onkeyup="validate_textbox(this.value, this.id)" readonly="1">
                                    </div>
                                    
                                      <div class="form-group">
                                        <label for="exampleInputPassword1">No. of cases considered for appointment till last month<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="totalAppoLastmonth"  value="${appointStatus.totalAppoLastmonth}" type="text" class="form-control" id="id10" onkeyup="validate_textbox(this.value, this.id)" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">No. of cases considered and rejected till last month <span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="totalRejLastmonth"   value="${appointStatus.totalRejLastmonth}" type="text" class="form-control" id="id11" onkeyup="validate_textbox(this.value, this.id)" required>
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Total no of cases disposed till last month<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="disposedlastMonth"  value="${appointStatus.disposedlastMonth}" type="text" class="form-control" id="id7" onkeyup="validate_textbox(this.value, this.id)" readonly >
                                    </div>

                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Cumulative no of cases disposed<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="totalCleared"  value="${appointStatus.totalCleared}" type="text" class="form-control" id="id8" onkeyup="validate_textbox(this.value, this.id)" readonly="1">
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Balance no of cases pending<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="totalPending"  value="${appointStatus.totalPending}" type="text" class="form-control" id="id9" onkeyup="validate_textbox(this.value, this.id)" required  readonly="1">
                                    </div>

                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Remarks</label>
                                        <input name="remarks" type="text" class="form-control" id="remarks"  value="${appointStatus.remarks}" >
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
</body>
</html>


