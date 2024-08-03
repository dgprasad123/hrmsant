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
                    document.getElementById('postId').value='${cc.postId}';
                });
            }
            function getDeptwiseCadreList() {
                $('#cadreId').empty();
                var url = 'getDeptewiseCadreList.htm?deptId=' + $('#departmentId').val();
                $('#cadreId').append('<option value="">Select Cadre</option>');
               // $('#cadreId').append('<option value="0">All</option>');
                
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#cadreId').append('<option value="' + obj.value + '">' + obj.label+ '</option>');
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
                                    <i class="fa fa-file"></i> <a href="commissionCourtCaseList.htm">Court Cases Details</a>
                                </li>                                    
                                    <li class="active">
                                        <i class="fa fa-file"></i> Update Court Case Details
                                    </li>
                                </ol>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align='center'><u>${officename} Court Cases</u></h3>
                                <form:form role="form" action="updateCommissionCourtCase.htm" commandName="saveCommissionCourtCase"  method="post">
                                    <input type='hidden' name="courtCaseId" value="${cc.courtCaseId}"/>
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Department Name</label>
                                            <Select name="departmentId" id="departmentId"  class="form-control" onchange="javascript: getDeptwiseCadreList();getCadrewisePostList()" required>
                                                <option value="">Select Department</option>
                                                <c:forEach items="${departmentList}" var="department">
                                                   <option value="${department.value}"  <c:if test = "${not empty cc.departmentId && cc.departmentId==department.value}"> <c:out value='selected="selected"'/></c:if>>${department.label}</option>
                                                </c:forEach>

                                            </select>  
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Group Name <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="groupName"  class="form-control">
                                                <option value="A"  <c:if test = "${not empty cc.groupName && cc.groupName=='A'}"> <c:out value='selected="selected"'/></c:if>>Group A </option>
                                                <option value="B"  <c:if test = "${not empty cc.groupName && cc.groupName=='B'}"> <c:out value='selected="selected"'/></c:if>>Group B</option>
                                                <option value="C" <c:if test = "${not empty cc.groupName && cc.groupName=='C'}"> <c:out value='selected="selected"'/></c:if>>Group C</option>
                                                <option value="D" <c:if test = "${not empty cc.groupName && cc.groupName=='D'}"> <c:out value='selected="selected"'/></c:if>>Group D</option>
                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Cadre <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="cadreId" id="cadreId"  class="form-control"  required>
                                                <option value="">Select Cadre</option>
                                                <option value="0">All</option>
                                                <c:forEach items="${cadreList}" var="cadre">
                                                    <option value="${cadre.value}"  <c:if test = "${not empty cc.cadreId && cc.cadreId==cadre.value}"> <c:out value='selected="selected"'/></c:if>>${cadre.label}</option>
                                                </c:forEach>

                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Post <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="postId" id="postId"  class="form-control" >
                                                 <option value="${cc.postId}">${cc.postName}</option>						
                                            </select>                 
                                        </div>
                                         <div class="form-group">
                                            <label for="exampleInputPassword1">Month<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="month"  class="form-control" required="1">
                                               <option value="January"  <c:if test = "${not empty cc.month && cc.month=='January'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                            <option value="February"  <c:if test = "${not empty cc.month && cc.month=='February'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                            <option value="March" <c:if test = "${not empty cc.month && cc.month=='March'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                            <option value="April" <c:if test = "${not empty cc.month && cc.month=='April'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                            <option value="May" <c:if test = "${not empty cc.month && cc.month=='May'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                            <option value="June" <c:if test = "${not empty cc.month && cc.month=='June'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                            <option value="July" <c:if test = "${not empty cc.month && cc.month=='July'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                            <option value="August"  <c:if test = "${not empty cc.month && cc.month=='August'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                            <option value="September" <c:if test = "${not empty cc.month && cc.month=='September'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                            <option value="October" <c:if test = "${not empty cc.month && cc.month=='October'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                            <option value="November" <c:if test = "${not empty cc.month && cc.month=='November'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                            <option value="December" <c:if test = "${not empty cc.month && cc.month=='December'}"> <c:out value='selected="selected"'/></c:if>>December</option>

                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Year <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="year"  class="form-control" required="1">
                                                <option value="">Select Year</option>
                                                <option value="2012" <c:if test = "${not empty cc.year && cc.year=='2012'}"> <c:out value='selected="selected"'/></c:if>>2012 </option>
                                                <option value="2013"  <c:if test = "${not empty cc.year && cc.year=='2013'}"> <c:out value='selected="selected"'/></c:if>>2013 </option>
                                                  <option value="2014" <c:if test = "${not empty cc.year && cc.year=='2014'}"> <c:out value='selected="selected"'/></c:if>>2014 </option>
                                                <option value="2015"  <c:if test = "${not empty cc.year && cc.year=='2015'}"> <c:out value='selected="selected"'/></c:if>>2015 </option>
                                                  <option value="2016"  <c:if test = "${not empty cc.year && cc.year=='2016'}"> <c:out value='selected="selected"'/></c:if>>2016 </option>
                                                <option value="2017" <c:if test = "${not empty cc.year && cc.year=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017 </option>
                                                <option value="2018"  <c:if test = "${not empty cc.year && cc.year=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                                <option value="2019"  <c:if test = "${not empty cc.year && cc.year=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                                <option value="2020"  <c:if test = "${not empty cc.year && cc.year=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                                  <option value="2021"  <c:if test = "${not empty cc.year && cc.year=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                                  <option value="2022"  <c:if test = "${not empty cc.year && cc.year=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                        </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="CourtCaseNo">Court Case No.<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="courtCaseNo" value="${cc.courtCaseNo}" type="text" class="form-control" id="CourtCaseNo"  required>
                                        </div>
                                        <div class="form-group">
                                            <label for="orderDate">Court Case Date</label>
                                            <input name="courtCaseDate" value="${cc.courtCaseDate}" type="date" class="form-control" id="CourtCaseDate"  >
                                        </div>
                                         <div class="form-group">
                                            <label for="VivavoceDate">Date of Submission Of PWC</label>
                                            <input name="pwcDate" value="${cc.pwcDate}" type="date" class="form-control" id="PWCDate"  >
                                        </div>
                                          <div class="form-group">
                                            <label for="VivavoceDate">Date of Filing Counter Affedevit</label>
                                            <input name="affedevitdate" value="${cc.affedevitdate}" type="date" class="form-control" id="Affedevitdate"  >
                                        </div>
                                         <div class="form-group">
                                            <label for="VivavoceDate">Date of Interim Order</label>
                                            <input name="interimorder" value="${cc.interimorder}" type="date" class="form-control" id="Interimorder"  >
                                        </div>
                                         <div class="form-group">
                                            <label for="VivavoceDate">Date of Final Judgement</label>
                                            <input name="judgementDate" value="${cc.judgementDate}" type="date" class="form-control" id="JudgementDate"  >
                                        </div>
                                         <div class="form-group">
                                            <label for="noOfSelectedCanddiates">If Any Stay Order Passed by Court(Yes/No)<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="orderPassed" value="${cc.orderPassed}" type="text" class="form-control" id="OrderPassed"  >
                                        </div>
                                         <div class="form-group">
                                            <label for="noOfSelectedCanddiates">Steps Taken for Vacation Of Stay <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="stepsStay" value="${cc.stepsStay}" type="text" class="form-control" id="StepsStay"  >
                                        </div>
                                       <div class="form-group">
                                            <label for="VivavoceDate">Date of Vacation Of Stay</label>
                                            <input name="stayDate" value="${cc.stayDate}" type="date" class="form-control" id="StayDate"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="FinalResultDate">Date Of Publication Of Final Result</label>
                                            <input name="finalResultDate" value="${cc.finalResultDate}" type="date" class="form-control" id="FinalResultDate"  >
                                        </div>
                                         <div class="form-group">
                                            <label for="SponsoringDate">Date Of Sponsoring of Candidates</label>
                                            <input name="sponsoringDate" value="${cc.sponsoringDate}" type="date" class="form-control" id="FinalResultDate"  >
                                        </div>
                                         <div class="form-group">
                                            <label for="remarks">Remarks</label>
                                            <input name="remarks" value="${cc.remarks}" type="text" class="form-control" id="remarks"  value="${Appoint.remarks}" >
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
     <script>
        getCadrewisePostList();
        
    </script> 
</html>
