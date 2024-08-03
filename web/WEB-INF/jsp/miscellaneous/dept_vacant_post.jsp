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
                var url = 'getCadrewisePostList.htm?cadreId=' + $('#cadreId').val();
                $('#postId').append('<option value="">Select Post</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#postId').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                    });
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
              /*  var col_31 = $('#base_level_post1').val();
                var col_41 = $('#post_filed_up1').val();
                var col_51 = $('#post_requisition_sent1').val();
                if (col_31 != '' && col_41 != '' && col_51 != '') {

                    var result1 = parseInt(col_31) - (parseInt(col_41) + parseInt(col_51));
                     if (result1 < 0) {
                        alert("Negative Balance Post is not allowed ");
                        $('#balance_posts1').val("");
                        return false;
                    }
                    $('#balance_posts1').val(result1);


                }*/
                
                var id1 = $('#id1').val();
                var id2 = $('#id2').val();
                var id3 = $('#id3').val();
             
                if (id1 != '' && id2 != '' && id3 != '') {

                    var result = (parseInt(id1) + parseInt(id2)+ parseInt(id3));
                    $('#id4').val(result);

                }
                 var id5 = $('#id5').val();
                var id6 = $('#id6').val();
             
                if (id5 != '' && id6 != '' ) {

                    var result = (parseInt(id5) + parseInt(id6));
                    $('#id7').val(result);

                }
                
                var balance_posts=$('#balance_posts').val();
                 var post_dept_commissions=$('#post_dept_commissions').val();
                  var sentreq=$('#sentreq').val();
                  
                  if (balance_posts != '' && post_dept_commissions != '' && sentreq != '') {

                    var result = (parseInt(balance_posts) -( parseInt(post_dept_commissions)+ parseInt(sentreq)));
                    if(result >= 0) {
                           $('#departmentally_bal_post1').val(result);
                     } else {
                         $('#departmentally_bal_post1').val("0");
                         
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
                    }
                    $('#vacancy').val(result);
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
                    } else {
                     $('#balance_posts').val(result);
                   }


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
                               <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                                <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                                <jsp:useBean id="date" class="java.util.Date" />
                                <fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
                                <fmt:formatDate value="${date}" pattern="MMMM" var="currentMonth" />
                                <h2>New Base Level Vacant Detail</h2>
                                 <c:if test = "${not empty vacantPostList}">  
                                <c:set var="bpost" value="${vacantPostList[0].postFilledUp -vacantPostList[0].totalpostFilledUp}" />
                                <div class="alert alert-success">
                                    <strong>Balance Posts </strong> As on 31:05:2019  :: <strong>${bpost}</strong>
                                </div>
                            </c:if> 
                                <form:form role="form" action="saveDeptvacantpost.htm"  method="post" >
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Department Name</label>
                                            <input name="deptname" type="text" class="form-control" id="deptname" value="${LoginUserBean.loginname}" readonly >
                                        </div>
                                        <div class="form-group" style='display:none'>
                                            <label for="exampleInputPassword1">Office Name <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="offieId" id="offieId"  class="form-control" >
                                                <option value="">Select Office</option>
                                                <c:forEach items="${officeList}" var="office">
                                                    <option value="${office.value}">${office.label}</option>
                                                </c:forEach>

                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Month<span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="month"  class="form-control" required="1">
                                                <option value="${currentMonth}">${currentMonth}</option>
                                              
                                            </select>

                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Year <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="year"  class="form-control" required="1">
                                                <option value="${currentYear}">${currentYear}</option>
                                              
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Group Name <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="groupName"  class="form-control" required="1">
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
                                            <label for="exampleInputPassword1">Name of Base Level Post <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="postId" id="postId"  class="form-control" >
                                                <option value="">Select Post</option>
                                                <c:forEach items="${postList}" var="post">
                                                    <option value="${post.postcode}">${post.post}</option>
                                                </c:forEach>
                                                   <c:if test = "${not empty loginDeptId && (loginDeptId==24 || loginDeptId==22 || loginDeptId==16 || loginDeptId==18 )}"> <c:out value='selected="selected"'/>
                                                         <option value="NA">NA</option>
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
                                                        <input name="sancPost" type="text" class="form-control" id="sanc_post"  required onkeyup="validate_textbox(this.value, this.id)" onblur="vacancy_cal()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Men in Position <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="maninPost" type="text" class="form-control" id="man_pos"  required onkeyup="validate_textbox(this.value, this.id)" onblur="vacancy_cal()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Vacancy <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="vacancy" type="text" class="form-control" id="vacancy"  required onkeyup="validate_textbox(this.value, this.id)" readonly>
                                                    </div>

                                                   
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Posts cleared after 1.6.2019 <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="postCleared" type="text" class="form-control" id="post_cleared"  required onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of cleared Post Prior to 1.6.2019 <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="baseLevelPost" type="text" class="form-control" id="base_level_post"  required  onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup()">
                                                    </div>
                                                    
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Anticipated vacancy due to retirement, resignation, promotion in the Current Year <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="vacRetirment" type="text" class="form-control" id="vac_retirment"  required onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup()">
                                                    </div>
                                                   
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Total posts to be filled Up <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="postFilledUp" type="text" class="form-control" id="post_filed_up"  required  onkeyup="validate_textbox(this.value, this.id)" readonly>
                                                    </div>
                                                    
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No of Posts already filled up by the end of last month<span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="lastmonthfilled" type="text" class="form-control" id="lastmonthfilled"  required  onkeyup="validate_textbox(this.value, this.id)" onblur="total_filledup_month()">
                                                    </div>
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No of Posts already filled up during the month<span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="currentmonthfilled" type="text" class="form-control" id="currentmonthfilled"  required  onkeyup="validate_textbox(this.value, this.id)" onblur="total_filledup_month();">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Total posts filled Up <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="totalpostFilledUp" type="text" class="form-control" id="totalpostFilledUp"  required  onkeyup="validate_textbox(this.value, this.id)" readonly>
                                                    </div>
                                                   
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Balance Posts <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="postsBalance" type="text" class="form-control" id="balance_posts"  readonly="1" required>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No of balance posts for which requisition  sent  <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="sentreq" type="text" class="form-control" id="sentreq" maxlength="4"  onkeyup="validate_textbox(this.value, this.id)"  >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No of balance posts for which requisition is to be sent <span style='color:red; font-weight:bold;'>*</span> </label>
                                                        <input name="deptPostComm" type="text" class="form-control" id="post_dept_commissions"  onkeyup="validate_textbox(this.value, this.id)" >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Commission Name</label>
                                                        <Select name="commissionName"  class="form-control clear_data" id="deptPostBalance"  >
                                                            <option value="">Select</option>
                                                            <option value="OPSC">OPSC</option>	
                                                            <option value="OSSC">OSSC</option>
                                                            <option value="OSSSC">OSSSC</option>
                                                            <option value="Other">Other</option>
                                                        </select> 

                                                    </div> 
                                                    <div class="form-group" >
                                                        <label for="exampleInputPassword1">No of balance posts for which recruitment is to be made Departmentally <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="proDeptPostBalance" type="text" class="form-control" id="departmentally_bal_post1" maxlength="4" readonly required >
                                                    </div>

                                                </td>
                                                <td>&nbsp;</td>

                                                <td valign='top'>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Sanctioned Post <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="sancPostPro" type="text" class="form-control" id="sanc_post1"  required onkeyup="validate_textbox(this.value, this.id)" onblur="vacancy_cal1()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Men in Position <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="maninPostPro" type="text" class="form-control" id="man_pos1"  required onkeyup="validate_textbox(this.value, this.id)" onblur="vacancy_cal1()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Vacancy <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="vacancyPro" type="text" class="form-control" id="vacancy1"  required onkeyup="validate_textbox(this.value, this.id)" readonly>
                                                    </div>
                                                   

                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Posts cleared  after 1.6.2019 <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="proPostCleared" type="text" class="form-control" id="id1"  required onkeyup="validate_textbox(this.value, this.id)">
                                                    </div>
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of cleared  Post Prior to 1.6.2019 <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="probacklog" type="text" class="form-control" id="id2"  required  onkeyup="validate_textbox(this.value, this.id)"  >
                                                    </div>
                                                    
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of Post Vacant Due to  Retirement  <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="proRetirment" type="text" class="form-control" id="id3"  required onkeyup="validate_textbox(this.value, this.id)"  onblur="total_filledup1()">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Total posts to be filled Up <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="postFilledUppro" type="text" class="form-control" id="id4"  required  onkeyup="validate_textbox(this.value, this.id)" readonly >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No of Posts already filled up by the end of last month  <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="lastMonthPro" type="text" class="form-control" id="id5"  required  onkeyup="validate_textbox(this.value, this.id)">
                                                    </div>
                                                     <div class="form-group">
                                                        <label for="exampleInputPassword1">No of Posts already filled up during the month<span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="currentmonthfilledpro" type="text" class="form-control" id="id6"  required  onkeyup="validate_textbox(this.value, this.id)" >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Total posts filled Up <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="totalpostFilledUppro" type="text" class="form-control" id="id7"  required  onkeyup="validate_textbox(this.value, this.id)" >
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">No Of balance posts for which requisition sent <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="propostRequisitionSent" type="text" class="form-control" id="post_requisition_sent1"  required  onkeyup="validate_textbox(this.value, this.id)">
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputPassword1">Balance Posts <span style='color:red; font-weight:bold;'>*</span></label>
                                                        <input name="proPostsBalance" type="text" class="form-control" id="balance_posts1"  required >
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
    </body>
</html>
