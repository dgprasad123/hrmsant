

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script src="js/jquery.freezeheader.js"></script>
        <script type="text/javascript">

            /*  $(window).load(function() {
             $("#viewDistinctPostModal").on("show.bs.modal", function(e) {
             var link = $(e.relatedTarget);
             
             $(this).find(".modal-body").load(link.attr("href"));
             
             });
             });*/
            $(document).ready(function() {
                $("#dataTab").freezeHeader();
            });

            function getOfficeList() {
                var url = 'getOfficeListJSON.htm?deptcode=' + $("#deptCode").val();
                var valText = $("#deptCode option:selected").html();
                $("#hiddenDeptName").val(valText);

                $.getJSON(url, function(data) {
                    $('#offCode').empty();
                    $('#postcode').empty();
                    $('#offCode').append($('<option>').text('Select Post').attr('value', ''));
                    $.each(data, function(i, obj) {
                        $('#offCode').append($('<option>').text(obj.offName).attr('value', obj.offCode));
                    });
                });
            }
            function getPostList(deptCode) {

                self.location = 'DuplicatePostCorrection.htm?deptCode=' + deptCode
            }
            function auto_select_result(vals) {
                vals = vals.toUpperCase();
                document.getElementById('id_search').value = vals;
                var Ocode = document.getElementById('offCode').value;
                vals = vals + "0000";
                // $("#hiddenOfficeName").val(vals);

                $('#offCode').val(vals);
                getPostList();

            }
            function markRow(postCode)
            {
                if ($('#post_code_' + postCode)[0].checked)
                {
                    $('#post_code_' + postCode)[0].checked = false;
                    $('#tr_' + postCode).css('background', '#FFFFFF');
                    $('#is_correct_' + postCode)[0].checked = false;
                }
                else
                {
                    $('#post_code_' + postCode)[0].checked = true;
                    $('#trVerified_' + postCode).css('background', '#789ac7');
                    $('#tr_' + postCode).css('background', '#FFE5D1');
                }
            }

            function checkDuplicate(postCode)
            {
                if ($('#post_code_' + postCode)[0].checked == false)
                {
                    $('#post_code_' + postCode)[0].checked = true;
                    $('#tr_' + postCode).css('background', '#FFE5D1');
                }
            }
            function validateForm() {
                isChecked = false;
                isCorrect = false;
                var dupPostCode = "";
                var finPostCode = "";
                cnt = 0;
                var checkBoxArray = "";
                $("input[name='postCodes']:checked").each(function() {
                    if (checkBoxArray == "") {
                        checkBoxArray = $(this).val();
                    } else {
                        checkBoxArray = checkBoxArray + "," + $(this).val();
                        cnt++;
                    }
                });
                if (checkBoxArray == "") {
                    alert("Please Select Posts");
                    return false;
                } else {
                    if (cnt == 1) {
                        isChecked = true;
                    } else {
                        alert("Please mark Two No.of Posts as duplicate before merging.");
                        return false;
                    }
                    for (var i = 0; i < document.frmPost.elements['isCorrect'].length; i++)
                    {
                        if (document.frmPost.elements['isCorrect'][i].checked)
                        {
                            isCorrect = true;
                            break;
                        }
                    }
                    if (!isCorrect)
                    {
                        alert("Please mark one post from the duplicates as Correct.");
                        return false;
                    }
                    if (confirm("Are you sure you want to mark these posts as Duplicate?"))
                    {
                        $("#showConfirmModal").modal('show');
                    }
                    else
                    {
                        return false;
                    }

                }
                getSelectedPostValues();
            }

            function getSelectedPostValues() {
                var chkpostcodevalue = "";
                var finalPostCode = "";
                var dupPostCode = "";
                var finalPostName = "";
                var dupPostName = "";
                $("input[type='checkbox'][name='postCodes']:checked").each(function() {
                    var postvalue = this.value;
                    var postname = $("#postname_" + postvalue).text();
                    if (chkpostcodevalue != "") {
                        chkpostcodevalue = chkpostcodevalue + "@" + postvalue + "_" + postname;
                    } else {
                        chkpostcodevalue = postvalue + "_" + postname;
                    }

                });
                var postcode = chkpostcodevalue.split("@");
                var postcode1 = postcode[0].split("_");
                var postcode2 = postcode[1].split("_");
                var pstCode1 = postcode1[0].split(",");
                var pName1 = postcode1[1].split(",");
                var pstCode2 = postcode2[0].split(",");
                var pName2 = postcode2[1].split(",");
                //alert(postcode1);
                var rdpostcodevalue = $("input[type='radio'][name='isCorrect']:checked").val();
                //alert("PostCode:::" + rdpostcodevalue + ";;" + pstCode1 + ";;" + pName1 + "--->" + pstCode2 + "===" + pName2);

                if (rdpostcodevalue == pstCode1) {
                    finalPostCode = rdpostcodevalue;
                    finalPostName = pName1;
                } else {
                    dupPostCode = pstCode1;
                    dupPostName = pName1;
                }
                if (rdpostcodevalue == pstCode2) {
                    finalPostCode = rdpostcodevalue;
                    finalPostName = pName2;
                } else {
                    dupPostCode = pstCode2;
                    dupPostName = pName2;
                }

                $("#dupPostname").text(dupPostName);
                $("#finPostname").text(finalPostName);
                $("#dupPostCode").val(dupPostCode);
                $("#finPostCode").val(finalPostCode);
                //alert($('#dupPostCode').val());
                //alert($('#finPostCode').val());



            }

            function confirmValidateToUpdate() {
                if (confirm("Are you Sure to Merge these Duplicate Post with Final Post ?")) {
                    var url = "getMergedPost.htm?dupPost=" + $("#dupPostCode").val() + "&finPost=" + $("#finPostCode").val();
                    $('#btnUpdate').hide();
                    //alert(url);
                    self.location = url;
                }
                return false;
            }

            function viewDuplicates() {
                isChecked = false;
                isCorrect = false;
                var dupPostCode = "";
                var finPostCode = "";
                cnt = 0;
                var checkBoxArray = "";
                $("input[name='postCodes']:checked").each(function() {
                    if (checkBoxArray == "") {
                        checkBoxArray = $(this).val();
                    } else {
                        checkBoxArray = checkBoxArray + "," + $(this).val();
                        cnt++;
                    }
                });
                if (checkBoxArray == "") {
                    alert("Please Select Posts");
                    return false;
                } else {
                    if (cnt == 1) {
                        isChecked = true;
                    } else {
                        alert("Please mark Two No.of Posts as duplicate before merging.");
                        return false;
                    }
                    for (var i = 0; i < document.frmPost.elements['isCorrect'].length; i++)
                    {
                        if (document.frmPost.elements['isCorrect'][i].checked)
                        {
                            isCorrect = true;
                            break;
                        }
                    }
                    if (!isCorrect)
                    {
                        alert("Please mark one post from the duplicates as Correct.");
                        return false;
                    }

                }
                if (isCorrect) {
                    getSelectedPostValues();
                    $("#viewDistinctPostModal").find(".modal-body").load("getDistinctPostCount.htm?dupPost=" + $("#dupPostCode").val() + "&finPost=" + $("#finPostCode").val());
                    $("#viewDistinctPostModal").modal("toggle");

                }


            }
            function viewPreviousMergedPost(finalPostCode) {
                var postlist = "";
                $('#mergedPostList').empty();
                var url = 'viewPreviousMergedPostJSON.htm?finalPostCode=' + finalPostCode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        postlist = postlist + "<tr>" +
                                "<td>" + (i + 1) + "</td>" +
                                "<td>" + obj.postCodes + "</td>" +
                                "<td>" + obj.post + "</td>" +
                                "</tr>";
                    });
                    $('#mergedPostList').append(postlist);

                });

            }



        </script>        
    </head>
    <body>

        <input type="hidden" id="dupPostCode"/>
        <input type="hidden" id="finPostCode"/>
        <input type="hidden" id="hiddeptCode"/>        
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
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
                                    <i class="fa fa-file"></i> Post Cleansing
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <form role="form" class="form-horizontal" name="frmPost" id="frmPost"  commandName="DuplicatePost" action="MergeDuplicatePost.htm"  method="post">
                        <div class="row">
                            <div class="col-lg-12">

                                <div class="col-lg-12" style="text-align:center;font-weight: bold;">
                                    <h1><u>Duplicate Post Correction</u></h1>
                                </div>
                                <c:if test="${not empty mergedMsg}">
                                    <div class="col-lg-12" style="text-align:center;font-weight: bold;" id="mergedMsgShown">
                                        <h3><u>${mergedMsg}</u></h3>
                                    </div>
                                </c:if>


                                <div class="form-group">
                                    <label class="control-label col-sm-1">Department</label>
                                    <div class="col-sm-8">
                                        <select class="form-control"  name="deptcode" id="deptCode" onchange="getPostList(this.value)" required="1">
                                            <option value=''>Select Department</option>
                                            <c:forEach items="${deptList}" var="dept" >
                                                <option value="${dept.deptCode}"<c:if test="${dept.deptCode eq deptCode}"> selected="selected"</c:if>>${dept.deptName}</option>
                                            </c:forEach>                                        
                                        </select>   
                                    </div>
                                </div>
                                <div style="position:fixed;top:170px;right:20px;text-align:right;">

                                    <button type="button" id="btnViewDuplicate" class="btn btn-danger" onclick="javascript:viewDuplicates();" style="font-weight: bold;">Update And Merge Duplicates</button>
                                    <%--<input type="button" value="Check Duplicates" 
                                           class="btn btn-md btn-success" onclick="return validateForm();"  />--%>
                                </div>
                                <div class="panel-footer">
                                    <div class="table-responsive">
                                        <div class="myTable headerDiv">
                                            <table class="table table-bordered table-hover" id="dataTab" style="background: lightgoldenrodyellow">
                                                <thead>
                                                    <tr>
                                                        <th width="4%"></th>
                                                        <th width="4%">Sl#</th>
                                                        <th>Post Code</th>
                                                        <th>Post Name</th>
                                                        <th>Vacation</th>
                                                        <th>Post Group</th>
                                                        <th>Is Authority</th>
                                                        <th>No. of Posts Available</th>
                                                        <th width="15%">No. of Employees Mapped<br/>On respective Post Code</th>
                                                        <th width="10%">Total No. of Employees <br/> On Generic Post Code</th>
                                                        <th width="10%">Mark as Correct?</th>
                                                        <th width="5%">View Merged Post</th>
                                                    </tr>
                                                </thead>
                                                <tbody style="overflow-y: auto">
                                                    <c:forEach items="${postList}" var="postList" varStatus="count">
                                                        <c:if test="${postList.verifiedMergedPost=='V'}">
                                                            <tr id="trVerified_${postList.postcode}" style="background:lightsteelblue;">
                                                                <td align="center" onclick="javascript:markRow('${postList.postcode}')"><input type="checkbox" name="postCodes" id="post_code_${postList.postcode}" value="${postList.postcode}" onclick="javascript:markRow('${postList.postcode}')" /></td>
                                                                <td onclick="javascript:markRow('${postList.postcode}');">${count.index+1}</td>
                                                                <td align="center">${postList.postcode}</td>
                                                                <td onclick="javascript:markRow('${postList.postcode}')"  id="postname_${postList.postcode}" >${postList.post}</td>
                                                                <td align="center">${postList.vacation}</td>
                                                                <td align="center">${postList.postgrpType}</td>
                                                                <td align="center">${postList.isauth}</td>
                                                                <td align="center" onclick="javascript:markRow('${postList.postcode}')">${postList.totNumPost}</td>
                                                                <td align="center" onclick="javascript:markRow('${postList.postcode}')">${postList.numEmployees}</td>
                                                                <td align="center" onclick="javascript:markRow('${postList.postcode}')" >${postList.availEmpOnGenPostName}</td>
                                                                <td align="center"><input type="radio" value="${postList.postcode}" name="isCorrect" id="is_correct_${postList.postcode}" 
                                                                                          onclick="javascript:checkDuplicate('${postList.postcode}')"/></td>
                                                                <td align="center" >
                                                                    <a href="javascript:viewPreviousMergedPost('<c:out value="${postList.postcode}"/>')" />                                                                    
                                                                    <button type="button" style="width:100%;font-weight:bold" class="btn btn-primary" data-remote="false" data-toggle="modal" data-target="#viewPreviousMergedPost">View</button>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                        <c:if test="${postList.verifiedMergedPost=='U'}">
                                                            <tr id="tr_${postList.postcode}" style="background:#FFFFFF">
                                                                <td align="center" onclick="javascript:markRow('${postList.postcode}')" ><input type="checkbox" name="postCodes" id="post_code_${postList.postcode}" value="${postList.postcode}" onclick="javascript:markRow('${postList.postcode}')" /></td>
                                                                <td onclick="javascript:markRow('${postList.postcode}');" >${count.index+1}</td>
                                                                <td align="center">${postList.postcode}</td>
                                                                <td onclick="javascript:markRow('${postList.postcode}')"  id="postname_${postList.postcode}" >${postList.post}</td>
                                                                <td align="center">${postList.vacation}</td>
                                                                <td align="center">${postList.postgrpType}</td>
                                                                <td align="center">${postList.isauth}</td>
                                                                <td align="center" onclick="javascript:markRow('${postList.postcode}')" >${postList.totNumPost}</td>
                                                                <td align="center" onclick="javascript:markRow('${postList.postcode}')" >${postList.numEmployees}</td>
                                                                <td align="center" onclick="javascript:markRow('${postList.postcode}')" >${postList.availEmpOnGenPostName}</td>
                                                                <td align="center" width="5%"><input type="radio" value="${postList.postcode}" name="isCorrect" id="is_correct_${postList.postcode}" 
                                                                                          onclick="javascript:checkDuplicate('${postList.postcode}')" /></td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>


                                </div>
                            </div>

                        </div>
                    </form>
                </div>
            </div>

            <!-- Modal -->
            <div id="showConfirmModal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Show Duplicates</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <table class="table" border="2"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1"> 
                                    <%--<tr><td><b>Duplicate Post:</b></td><td>dupPostName</td></tr>
                                    <br/>
                                    <tr><td><b>Final Post:</b></td><td>finalPostName</td></tr>--%>

                                    <tr><td style="text-align:left; font-size: 12px;padding-left:20px" >
                                            <label style="font-size: large">Duplicate Post: </label>
                                        </td>
                                        <td style="padding-right:80px">
                                            <span id="dupPostname"  style="color: red;font-size: large;"></span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="text-align:left; font-size: 12px;padding-left:20px" >
                                            <label style="font-size: large">Final Post: </label>
                                        </td>
                                        <td style="padding-right:80px">
                                            <span id="finPostname"  style="color: green;font-size: large;"></span>
                                        </td>
                                    </tr>
                                </table>                 
                            </div>
                        </div>
                        <div class="modal-footer">
                            <span id="msg"></span>
                            <%--<button type="button" class="btn btn-success" onclick="getMergedPost('${dupPostCode}','${finPostCode}')">Merge Duplicates</button>--%>
                            <%--<input type="submit" Value="Merge Duplicates" name="MergeDuplicates" class="btn btn-success" onclick="return confirmValidate();"/>--%>
                            <button type="button" id="btnUpdate" class="btn btn-success" onclick="javascript:confirmValidateToUpdate();" >Merge Duplicates</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal" >Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="viewDistinctPostModal" class="modal fade" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content modal-lg">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h3 class="table-active"><u>Duplicate Post Details</u></h3>
                        </div>
                        <div class="modal-body"></div>
                        <div class="modal-footer">                       
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="viewPreviousMergedPost" class="modal fade" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content modal-lg">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h3 class="table-active"><u>Merged Post List</u></h3>
                        </div>
                        <div class="modal-body">
                            <table class="table" border="1"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1"> 
                                <thead>
                                    <tr >
                                        <th style="text-align:center;">Sl No</th>
                                        <th style="text-align:center;">Post Code</th>
                                        <th style="text-align:center;">Post Name</th>
                                    </tr>
                                </thead>
                                <tbody id="mergedPostList" style="text-align:center;">                                                    

                                </tbody>
                            </table>  
                        </div>
                        <div class="modal-footer">                       
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
    </body>
</html>
