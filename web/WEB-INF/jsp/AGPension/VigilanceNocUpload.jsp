<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript">
    $(document).ready(function () {


    })
    function radioClicked() {
        $("#uploadNocDocumentDiv").hide();
        // $("#vigilanceNocReasonDiv").show();
        var radioValue = $("input[name='NOC']:checked").val();
        /* if (radioValue == "NOCApproved") {
         $("#uploadNocDocumentDiv").show();
         } else if (radioValue == "NOCRejected") {
         $("#vigilanceNocReasonDiv").show();
         }*/

        if (radioValue == "NOCRejected") {
            $("#uploadNocDocumentDiv").show();
            //$("#uploadDocumentvigilance").show();
            // $("input[name='vigilanceNocReason']").val("Y");
            $("#id_text_value").val("Declined");

        } else {
            $("#uploadNocDocumentDiv").hide();
            $("#id_text_value").val("");
            $("#uploadDocumentvigilance").val("");
        }
        //  $("#uploadNocDocumentDiv").show();

    }


    function saveNOC() {
        var radioValue = $("input[name='NOC']:checked").val();
        var radioTYpe = "";
        if (radioValue == "NOCApproved" || radioValue == "NOCRejected") {
            if (radioValue == "NOCRejected") {
                radioTYpe = "NOC Rejected";
            }
            if (radioValue == "NOCApproved") {
                radioTYpe = "NOC Approved";
            }

            if (radioValue == "NOCRejected") {
                var fup = document.getElementById('uploadDocumentvigilance');
                if ($("#uploadDocumentvigilance").val() == "") {
                    alert("Please upload the " + radioTYpe + " Document.");
                    return false;
                }
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();
                if ((ext != "pdf" && ext != "zip")) {
                    alert("Upload pdf/zip files only");
                    fup.focus();
                    return false;
                }
                var fi = document.getElementById("uploadDocumentvigilance");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 2048) {
                    alert("File too Big, please select a file less than 2mb");
                    $("#uploadDocumentvigilance").val('');
                    return false;
                }
            }
            //  postData=$("#id_noc_upload_frm").serialize();
            var fd = new FormData();
            var files = $("#uploadDocumentvigilance")[0].files;
            if (files.length > 0) {
                var filesnew = document.getElementById("uploadDocumentvigilance").files[0];//$("#uploadDocumentvigilance")[0].files[0];
                fd.append("uploadDocumentvigilance", filesnew);               
                var other_data = $("#id_noc_upload_frm").serializeArray();
                $.each(other_data, function (key, input) {
                    fd.append(input.name, input.value);

                });
                $.ajax({
                    type: "POST",
                    data: fd,
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false,
                    url: "saveVigilanceNOC.htm",
                    success: function (data) {
                        $(currentRow).parent().parent().remove();
                        $("#id_save_noc_btm").hide();
                        $("#success_message").show();
                        $("#success_message").html("<h2>NOC Submitted Successfully!!!<h2>")
                        //$(".pensionModal").modal('hide');
                        //.ajax.reload();
                    }
                });


            } else {
                postData = $("#id_noc_upload_frm").serialize();
                $.ajax({
                    type: "POST",
                    data: postData,
                    //contentType:false,
                    // processData:false,
                    url: "saveVigilanceNOC.htm",
                    success: function (data) {
                        $(currentRow).parent().parent().remove();
                        $("#id_save_noc_btm").hide();
                        $("#success_message").show();
                        $("#success_message").html("<h2>NOC Submitted Successfully!!!<h2>")
                        //$(".pensionModal").modal('hide');
                        //.ajax.reload();
                    }
                });
            }





        } else {
            alert("Please Choose NOC Status Radio Button");
            return false;
        }



        return true;

    }


</script>        

<div class="container-fluid" style="padding-bottom: 125px;">
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="row">
                <div class="col-lg-12">
                    <h2 style="color:  #0071c5;" align="center">${loginName} NOC Upload Panel</h2>
                </div>
            </div>

        </div>
        <div class="panel panel-primary">
            <div class="panel-heading">Employee Details</div>
            <div class="panel-body">
                <form:form action="saveVigilanceNOC.htm"  method="POST" commandName="PensionNOCBean" enctype="multipart/form-data" id="id_noc_upload_frm">
                    <input type="hidden"  name='vigilanceNocReason' id='id_text_value'  />
                    <c:forEach items="${pensionList}" var="pnoc" varStatus="count">

                        <form:hidden path="nocId" />

                        <input type="hidden" name="hrmsid" value=" ${pnoc.hrmsid}"/>
                        <div class="form-group row">
                            <label for="Name:" class="col-sm-2 col-form-label">Name:</label>
                            <div class="col-sm-10">
                                ${pnoc.name}
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">Father's Name:</label>
                            <div class="col-sm-10">
                                ${pnoc.fatherName}
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">Department Name:</label>
                            <div class="col-sm-10">
                                ${pnoc.departmentName}
                            </div>
                        </div> 
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">Office Name:</label>
                            <div class="col-sm-10">
                                ${pnoc.offname}
                            </div>
                        </div>    
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">DDO Code:</label>
                            <div class="col-sm-10">
                                ${pnoc.ddoCode}
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">HRMS ID:</label>
                            <div class="col-sm-10">
                                ${pnoc.hrmsid}
                            </div>
                        </div>    
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">Post:</label>
                            <div class="col-sm-10">
                                ${pnoc.post}
                            </div>
                        </div>     

                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">Present Address:</label>
                            <div class="col-sm-10">
                                ${pnoc.address}
                            </div>
                        </div> 
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">DOB:</label>
                            <div class="col-sm-10">
                                ${pnoc.dob}
                            </div>
                        </div> 
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">DOS:</label>
                            <div class="col-sm-10">
                                ${pnoc.dos}
                            </div>
                        </div>   
                        <div class="form-group row">
                            <label for="fa" class="col-sm-2 col-form-label">Mobile:</label>
                            <div class="col-sm-10">
                                ${pnoc.mobile}
                            </div>
                        </div>


                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="fa">&nbsp;</label>
                            </div>
                            <div class="col-lg-2"> 
                                <input type="radio" id="NOCApproved" name="NOC" value="NOCApproved" onclick="radioClicked()"> <b>NOC Request Approved</b>
                            </div>
                            <div class="col-lg-2">
                                <input type="radio" id="NOCRejected" name="NOC" value="NOCRejected" onclick="radioClicked()">  <b>NOC Request Declined</b>
                            </div>                                                                    
                        </div>

                        <div class="form-group row" id="uploadNocDocumentDiv" style="display:none">
                            <label for="fa" class="col-sm-2 col-form-label">NOC Upload:</label>
                            <div class="col-sm-10">
                                <input type="file" class="form-control-file" id="uploadDocumentvigilance" name='uploadDocumentvigilance'>                                                                            
                            </div>
                        </div> 
                        <div align='center' class='text-success' id='success_message' style='display:none'></div>    



                        <input type="button" name="action" value="Save" class="btn btn-default"  onclick="return saveNOC()" id='id_save_noc_btm'/>

                    </c:forEach>     
                </form:form>

            </div>
        </div>
    </div>
</div>