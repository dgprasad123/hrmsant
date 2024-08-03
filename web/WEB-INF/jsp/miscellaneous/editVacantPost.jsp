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
                vacancy_cal();
                total_filledup();
                total_filledup_month();
                var col_3 = $('#post_filed_up').val();
                var col_4 = $('#totalpostFilledUp').val();
               
                if (col_3 != '' && col_4 != '') {

                    var result = parseInt(col_3) - parseInt(col_4);
                    if (result < 0) {
                        alert("Negative Balance Post is not allowed ");
                        $('#balance_posts').val("");
                        return false;
                    } else {
                    $('#balance_posts').val(result);
                }


                }
                 var balance_posts=$('#balance_posts').val();
                 var post_dept_commissions=$('#post_dept_commissions').val();
                  var sentreq=$('#sentreq').val();
                  
                  if (balance_posts != '' && post_dept_commissions != '' && sentreq != '') {

                    var result = (parseInt(balance_posts) -( parseInt(post_dept_commissions)+ parseInt(sentreq)));
                    if(result >= 0) {
                           $('#departmentally_bal_post1').val(result);
                     }

                }


            }
             function vacancy_cal() {
                var sanc_post = $('#sanc_post').val();
                var man_pos = $('#man_pos').val();
                if (sanc_post != '' && man_pos != '') {
                    var result = parseInt(sanc_post) - (parseInt(man_pos));
                    if (result < 0) {
                        alert("Negative Vacancy Post is not allowed ");
                        $('#vacancy').val("");
                        return false;
                    } else {
                      $('#vacancy').val(result);
                   }
                }

            }function vacancy_cal1() {
                var sanc_post = $('#sanc_post1').val();
                var man_pos = $('#man_pos1').val();
                if (sanc_post != '' && man_pos != '') {
                    var result = parseInt(sanc_post) - (parseInt(man_pos));
                    if (result < 0) {
                        alert("Negative Vacancy Post is not allowed ");
                        $('#vacancy1').val("");
                        return false;
                    }
                    $('#vacancy1').val(result);
                }

            }
             function total_filledup() {
                var post_cleared = $('#post_cleared').val();
                var base_level_post = $('#base_level_post').val();
                var vac_retirment = $('#vac_retirment').val();
                if (post_cleared != '' && base_level_post != '' && vac_retirment != '') {
                    var result = parseInt(post_cleared) + (parseInt(base_level_post))+ (parseInt(vac_retirment));
                    
                    $('#post_filed_up').val(result);
                }

            }
            function total_filledup1() {
                var post_cleared1 = $('#post_cleared1').val();
                var vac_retirment = $('#vac_retirment').val();
                var vac_retirment1 = $('#vac_retirment1').val();
                if (post_cleared1 != '' && vac_retirment != '' && vac_retirment1 != '') {
                    var result = parseInt(post_cleared1) + (parseInt(vac_retirment))+ (parseInt(vac_retirment1));
                    
                    $('#post_filed_up1').val(result);
                }

            }
            function total_filledup_month() {
                var lastmonthfilled = $('#lastmonthfilled').val();
                var currentmonthfilled = $('#currentmonthfilled').val();
              
                if (lastmonthfilled != '' && currentmonthfilled != '') {
                    var result = parseInt(lastmonthfilled) + (parseInt(currentmonthfilled));
                   // var result=parseInt($('#post_filed_up').val())-(parseInt(result))
                    $('#totalpostFilledUp').val(result);
                }
                var col_3 = $('#post_filed_up').val();
                var col_4 = $('#totalpostFilledUp').val();
               
                if (col_3 != '' && col_4 != '') {

                    var result = parseInt(col_3) - parseInt(col_4);
                    if (result < 0) {
                        alert("Negative Balance Post is not allowed ");
                        $('#balance_posts').val("");
                        return false;
                    }
                    $('#balance_posts').val(result);


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
                                        <i class="fa fa-file"></i> New Base Level Vacant
                                    </li>
                                </ol>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>New Base Level Vacant Detail</h2>
                                <c:if test = "${not empty vacantPostList}">  
                                <c:set var="bpost" value="${vacantPostList[0].postFilledUp -vacantPostList[0].totalpostFilledUp}" />
                                <div class="alert alert-success">
                                    <strong>Balance Posts </strong> As on 31:05:2019  :: <strong>${bpost}</strong>
                                </div>
                            </c:if> 
                                <form:form role="form" action="updateDeptvacantpost.htm"  method="post">
                                    <input type='hidden' name="idVacantPost" value="${VacantPost.idVacantPost}"/>
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Department Name</label>
                                            <input name="deptname" type="text" class="form-control" id="deptname" value="${LoginUserBean.loginname}" readonly >
                                        </div>
                                         
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Month<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="month"  class="form-control" required="1">
                                                <option value="${VacantPost.month}">${VacantPost.month}</option>
                                               

                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Year <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="year"  class="form-control" required="1">
                                                <option value="${VacantPost.year}">${VacantPost.year}</option>
                                                
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Group Name <span style='color:red; font-weight:bold;'>*</span></label>
                                           <Select name="groupName"  class="form-control">
                                                <option value="">Select Group</option>
                                                <option value="A"  <c:if test = "${not empty VacantPost.groupName && VacantPost.groupName=='A'}"> <c:out value='selected="selected"'/></c:if>>Group A </option>
                                                <option value="B"  <c:if test = "${not empty VacantPost.groupName && VacantPost.groupName=='B'}"> <c:out value='selected="selected"'/></c:if>>Group B</option>
                                                <option value="C" <c:if test = "${not empty VacantPost.groupName && VacantPost.groupName=='C'}"> <c:out value='selected="selected"'/></c:if>>Group C</option>
                                                <option value="D" <c:if test = "${not empty VacantPost.groupName && VacantPost.groupName=='D'}"> <c:out value='selected="selected"'/></c:if>>Group D</option>
                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Cadre <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="cadreId" id="cadreId"  class="form-control" onchange="getCadrewisePostList()" required>
                                                <option value="">Select Cadre</option>
                                                <option value="0"  <c:if test = "${not empty VacantPost.cadreId && VacantPost.cadreId=='0'}"> <c:out value='selected="selected"'/></c:if>>All</option>
                                                <c:forEach items="${cadreList}" var="cadre">
                                                    <option value="${cadre.value}"  <c:if test = "${not empty VacantPost.cadreId && VacantPost.cadreId==cadre.value}"> <c:out value='selected="selected"'/></c:if>>${cadre.label}</option>
                                                </c:forEach>

                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Post <span style='color:red; font-weight:bold;'>*</span></label>
                                            <input type='hidden' id='hpostid' value='${VacantPost.postId}' />
                                            <Select name="postId" id="postId"  class="form-control" >
                                                <option value="">Select Post</option>
                                                 <c:forEach items="${postList}" var="post">
                                                    <option value="${post.postcode}"  <c:if test = "${not empty VacantPost.postId && VacantPost.postId==post.postcode}"> <c:out value='selected="selected"'/></c:if>>${post.post}</option>
                                                </c:forEach>
                                                     <c:if test = "${not empty loginDeptId && (loginDeptId==24 || loginDeptId==22 || loginDeptId==16 || loginDeptId==18 )}"> <c:out value='selected="selected"'/>
                                                         <option value="NA"  <c:if test = "${not empty VacantPost.postId && VacantPost.postId=='NA'}"> <c:out value='selected="selected"'/></c:if>>NA</option>
                                                   </c:if>
                                            </select>                 
                                        </div>
										
                                       <hr style='border:2px solid green'/>
                                        <table>
                                            <tr>
                                                <th>Base Level Posts(DIRECT RECRUITMENT)</th>
                                                <th>&nbsp;</th>
                                                <th>Promotional Posts</th>
                                            </tr>
                                            <tr>
                                                <td colspan='3'>
                                                    <hr style='border:2px solid green'/>   
                                                </td>
                                            </tr>

                                            <tr>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Sanctioned Post <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="sancPost" type="text" class="form-control" value="${VacantPost.sancPost}" id="sanc_post"  required onkeyup="validate_textbox(this.value, this.id)" onblur="vacancy_cal()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Men in Position <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="maninPost" type="text" class="form-control"  value="${VacantPost.maninPost}"  id="man_pos"  required onkeyup="validate_textbox(this.value, this.id)" onblur="vacancy_cal()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Vacancy <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="vacancy" type="text" class="form-control"  value="${VacantPost.vacancy}" id="vacancy"  required onkeyup="validate_textbox(this.value, this.id)" readonly>
                                                    </div>

                                                   
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Posts cleared by F.D/E.C/Govt. after 1.6.2019 <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="postCleared" type="text" class="form-control"  value="${VacantPost.postCleared}" id="post_cleared"  required onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Vacant Post Prior to  1.6.2019<span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="baseLevelPost" type="text" class="form-control"  value="${VacantPost.baseLevelPost}" id="base_level_post"  required  onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup()">
                                                    </div>
                                                    
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Anticipated vacancy due to retirement, resignation, promotion in the Current Year<span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="vacRetirment" type="text" class="form-control"  value="${VacantPost.vacRetirment}" id="vac_retirment"  required onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup()">
                                                    </div>
                                                   
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Total posts to be filled Up <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="postFilledUp" type="text"  value="${VacantPost.postFilledUp}" class="form-control" id="post_filed_up"  required  onkeyup="validate_textbox(this.value, this.id)" readonly>
                                                    </div>
                                                    
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No of Posts already filled up by the end of last month<span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="lastmonthfilled" type="text"  value="${VacantPost.lastmonthfilled}" class="form-control" id="lastmonthfilled"  required  onkeyup="validate_textbox(this.value, this.id)" onblur="total_filledup_month()">
                                                    </div>
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No of Posts already filled up during the month<span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="currentmonthfilled" type="text"  value="${VacantPost.currentmonthfilled}" class="form-control" id="currentmonthfilled"  required  onkeyup="validate_textbox(this.value, this.id)" onblur="total_filledup_month();">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Total posts filled Up <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="totalpostFilledUp" type="text"  value="${VacantPost.totalpostFilledUp}" class="form-control" id="totalpostFilledUp"  required  onkeyup="validate_textbox(this.value, this.id)" readonly>
                                                    </div>
                                                   
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Balance Posts <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="postsBalance" type="text"  value="${VacantPost.postsBalance}" class="form-control" id="balance_posts"  readonly="1">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No of balance posts for which requisition  sent <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="sentreq" type="text"  value="${VacantPost.sentreq}" class="form-control" id="sentreq"  maxlength="4"  onkeyup="validate_textbox(this.value, this.id)" >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No of balance posts for which requisition is to be sent<span style='color:red; font-weight:bold;'>*</span> </label>
                                                        <input name="deptPostComm"  value="${VacantPost.deptPostComm}" type="text" class="form-control" id="post_dept_commissions"   onkeyup="validate_textbox(this.value, this.id)">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Commission Name</label>
                                                        <Select name="commissionName"  class="form-control clear_data" id="deptPostBalance"  >
                                                            <option value="">Select</option>
                                                            <option value="OPSC" <c:if test = "${not empty VacantPost.commissionName && VacantPost.commissionName=='OPSC'}"> <c:out value='selected="selected"'/></c:if>>OPSC</option>	
                                                            <option value="OSSC" <c:if test = "${not empty VacantPost.commissionName && VacantPost.commissionName=='OSSC'}"> <c:out value='selected="selected"'/></c:if>>OSSC</option>
                                                            <option value="OSSSC" <c:if test = "${not empty VacantPost.commissionName && VacantPost.commissionName=='OSSSC'}"> <c:out value='selected="selected"'/></c:if>>OSSSC</option>
                                                              <option value="Other" <c:if test = "${not empty VacantPost.commissionName && VacantPost.commissionName=='Other'}"> <c:out value='selected="selected"'/></c:if>>Other</option>
                                                        </select> 

                                                    </div> 
                                                    <div class="form-group" >
                                                        <label for="exampleInputPassword1">No of balance posts for which recruitment is to be made Departmentally</label>
                                                        <input name="proDeptPostBalance" value="${VacantPost.proDeptPostBalance}" type="text" class="form-control" id="departmentally_bal_post1" maxlength="4"  readonly="1" required >
                                                    </div>

                                                </td>
                                                <td>&nbsp;</td>

                                                <td valign='top'>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Sanctioned Post <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="sancPostPro" value="${VacantPost.sancPostPro}"  type="text" class="form-control" id="sanc_post1"  required onkeyup="validate_textbox(this.value, this.id)" onblur="vacancy_cal1()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Men in Position <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="maninPostPro" value="${VacantPost.maninPostPro}"  type="text" class="form-control" id="man_pos1"  required onkeyup="validate_textbox(this.value, this.id)" onblur="vacancy_cal1()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Vacancy <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="vacancyPro"  value="${VacantPost.vacancyPro}" type="text" class="form-control" id="vacancy1"  required onkeyup="validate_textbox(this.value, this.id)" readonly>
                                                    </div>
                                                   

                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Posts cleared by F.D/E.C after  1.6.2019 <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="proPostCleared" value="${VacantPost.proPostCleared}" type="text" class="form-control" id="post_cleared1"  required onkeyup="validate_textbox(this.value, this.id)" onblur="total_filledup1()">
                                                    </div>
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Backlog Vacant Post Prior to  1.6.2019 <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="probacklog" value="${VacantPost.probacklog}"  type="text" class="form-control" id="vac_retirment"  required  onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup1()">
                                                    </div>
                                                    
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Post Vacant Due to  Retirement  <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="proRetirment" value="${VacantPost.proRetirment}" type="text" class="form-control" id="vac_retirment1"  required onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup1()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Total posts to be filled Up <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="postFilledUppro" value="${VacantPost.postFilledUppro}" type="text" class="form-control" id="post_filed_up1"  required  onkeyup="validate_textbox(this.value, this.id)" >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No of Posts already filled up by the end of last month  <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="lastMonthPro" value="${VacantPost.lastMonthPro}"  type="text" class="form-control" id="post_filed_up1"  required  onkeyup="validate_textbox(this.value, this.id)">
                                                    </div>
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No of Posts already filled up during the month<span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="currentmonthfilledpro" value="${VacantPost.currentmonthfilledpro}"  type="text" class="form-control" id="currentmonthfilled1"  required  onkeyup="validate_textbox(this.value, this.id)" >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Total posts filled Up <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="totalpostFilledUppro" value="${VacantPost.totalpostFilledUppro}" type="text" class="form-control" id="totalpostFilledUp1"  required  onkeyup="validate_textbox(this.value, this.id)" >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of balance posts for which requisition sent <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="propostRequisitionSent" value="${VacantPost.propostRequisitionSent}" type="text" class="form-control" id="post_requisition_sent1"  required  onkeyup="validate_textbox(this.value, this.id)">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Balance Posts <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="proPostsBalance" value="${VacantPost.proPostsBalance}"  type="text" class="form-control" id="balance_posts1"  >
                                                    </div>
                                                   

                                                </td>
                                            </tr>   

                                        </table>


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
                    //alert($('#hpostid').val());
                   
                }
                
                
            </script>    
    </body>
</html>
