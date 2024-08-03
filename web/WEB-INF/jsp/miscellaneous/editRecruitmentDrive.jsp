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
                        $('#postCode').append('<option value="'+obj.postcode+'">'+obj.post+'</option>');
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
                                        <i class="fa fa-file"></i> Recruitment
                                    </li>
                                </ol>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>Recruitment</h2>

                                <form:form role="form" action="updateRecruitDriveData.htm">
                                       <input type='hidden' name="recruitDriveId" value="${RecruitDrive.recruitDriveId}"/>    
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Department Name</label>
                                            <input name="deptname" type="text" class="form-control" id="deptname" value="${LoginUserBean.loginname}" readonly >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Month of Requisition<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="month"  class="form-control"  required="1">
                                                <option value="">Select Month</option>
                                                  <option value="January"  <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='January'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                                <option value="February"  <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='February'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                                <option value="March" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='March'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                                <option value="April" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='April'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                                <option value="May" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='May'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                                <option value="June" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='June'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                                <option value="July" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='July'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                                <option value="August"  <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='August'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                                <option value="September" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='September'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                                <option value="October" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='October'}"> <c:out value='selected="selected"'/></c:if>>>October</option>
                                                <option value="November" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='November'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                                <option value="December" <c:if test = "${not empty RecruitDrive.month && RecruitDrive.month=='December'}"> <c:out value='selected="selected"'/></c:if>>December</option>

                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Year of Requisition<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="year"  class="form-control" required="1">
                                                <option value="">Select Year</option>
                                                <option value="2011" <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2011'}"> <c:out value='selected="selected"'/></c:if>>2011 </option>
                                                <option value="2012"  <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2012'}"> <c:out value='selected="selected"'/></c:if>>2012 </option>	
                                                <option value="2013" <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2013'}"> <c:out value='selected="selected"'/></c:if>>2013 </option>
                                                <option value="2014"  <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2014'}"> <c:out value='selected="selected"'/></c:if>>2014 </option>	
                                                <option value="2015" <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2015'}"> <c:out value='selected="selected"'/></c:if>>2015 </option>
                                                <option value="2016"  <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2016'}"> <c:out value='selected="selected"'/></c:if>>2016 </option>	
                                                <option value="2017" <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017 </option>
                                                <option value="2018"  <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                                <option value="2019"  <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                                <option value="2020"  <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                                 <option value="2021"  <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                                 <option value="2022"  <c:if test = "${not empty RecruitDrive.year && RecruitDrive.year=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                        </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Cadre <span style='color:red; font-weight:bold;'>*</span></label>
                                            <select name="cadreCode" id="cadreCode"  class="form-control" onchange="getCadrewisePostList()"  required="1">
                                                <option value="">Select Cadre</option>
                                                 <option value="0"  <c:if test = "${not empty RecruitDrive.cadreCode && RecruitDrive.cadreCode=='0'}"> <c:out value='selected="selected"'/></c:if>>All</option>
                                                <c:forEach items="${cadreList}" var="cadre">
                                                    <option value="${cadre.value}"  <c:if test = "${not empty RecruitDrive.cadreCode && RecruitDrive.cadreCode==cadre.value}"> <c:out value='selected="selected"'/></c:if>>${cadre.label}</option>
                                                </c:forEach>
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Post Name <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input type='hidden' id='hpostid' value='${RecruitDrive.postName}' />
                                            <Select name="postCode" id="postCode" class="form-control"  required="1">
                                                <option value="">Select Post</option>						
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Number of vacancy <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="totalVacanct" value="${RecruitDrive.totalVacanct}" type="text" class="form-control" id="totalVacanct"  required onkeyup="validate_textbox(this.value, this.id)">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Whether Requsition Sent to the recruiting Agency <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="reqSentAgentStatus" id="reqSentAgentStatus"  class="form-control" required="1">
                                                <option value="">Select </option>
                                                <option value="Yes" <c:if test = "${not empty RecruitDrive.reqSentAgentStatus && RecruitDrive.reqSentAgentStatus=='Yes'}"> <c:out value='selected="selected"'/></c:if>>Yes</option>	
                                                <option value="No" <c:if test = "${not empty RecruitDrive.reqSentAgentStatus && RecruitDrive.reqSentAgentStatus=='No'}"> <c:out value='selected="selected"'/></c:if>>NO</option>										
                                            </select>  
                                        </div>

                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Letter No <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="sentLetterNo" value="${RecruitDrive.sentLetterNo}" type="text" class="form-control" id="sentLetterNo"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Letter Date <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="sentDate" value="${RecruitDrive.sentDate}" type="text" class="form-control" id="sentDate"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Number of posts for which requsition sent <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="postReqSent" value="${RecruitDrive.postReqSent}" type="text" class="form-control" id="postReqSent" >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Name of the Recruiting Agency<span style='color:red; font-weight:bold;'>*</span></label>
                                            <input name="recruitAgency" value="${RecruitDrive.recruitAgency}" type="text" class="form-control" id="recruitAgency"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Advertisement Status(Advertisement no and date)</label>
                                            <input name="advDetails" value="${RecruitDrive.advDetails}" type="text" class="form-control" id="advDetails"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Examination Conducted (Examination date)</label>
                                            <input name="examDetails" value="${RecruitDrive.examDetails}" type="text" class="form-control" id="examDetails"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Result Published(Notification no and date)</label>
                                            <input name="resultPublished" value="${RecruitDrive.resultPublished}" type="text" class="form-control" id="resultPublished"  >
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Remarks</label>
                                            <input name="remarks" value="${RecruitDrive.remarks}" type="text" class="form-control" id="remarks"  >
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
                
                if($('#cadreId').val()!=''){
                    getCadrewisePostList();
                     
                   
                }
                
                
            </script>  
    </body>
</html>

