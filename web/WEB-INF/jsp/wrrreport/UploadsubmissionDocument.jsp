<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script src="js/moment.js" type="text/javascript"></script>
<script src="js/jquery.min.js" type="text/javascript"></script>           
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="js/chosen.jquery.min.js"></script>
<script type="text/javascript">
    



    function saveNOC() {
        
        
        if ($("#uploadDocument").val() == "") {
            alert("Please upload the file");
            return false;
        }
        var fup = document.getElementById('uploadDocument');
        var fileName = fup.value;
        var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
        var ext = ext.toLowerCase();
        if ((ext != "pdf" && ext != "zip")) {
            alert("Upload pdf/zip files only");
            fup.focus();
            return false;
        }
        var fi = document.getElementById("uploadDocument");
        var fsize = fi.files.item(0).size;
        var file = Math.round((fsize / 1024));
        if (file >= 2048) {
            alert("File size too Big, please select a file less than 2mb");
            $("#uploadDocument").val('');
            return false;
        }
        var fd = new FormData();
        var files = $("#uploadDocument")[0].files;
        if (files.length > 0) {
            var filesnew = document.getElementById("uploadDocument").files[0];//$("#uploadDocumentvigilance")[0].files[0];
            fd.append("uploadDocument", filesnew);
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
                url: "savedocumentsubmission.htm",
                success: function (data) {
                    //alert(data);
                    $(currentRow).parent().parent().remove();
                    $("#id_save_noc_btm").hide();
                    $("#success_message").show();
                    $("#success_message").html("<h2>File Submitted Successfully!!!<h2>")
                    //$(".pensionModal").modal('hide');
                    //.ajax.reload();
                }
            });


        }





        return true;

    }


</script>        
<div class="container-fluid">
    <div align="center" style="margin-top:5px;margin-bottom:7px;">



        <div class="panel panel-primary" >
            <div class="panel-heading">Document Submission</div>
            <div class="panel-body" style='font-size:15px;min-height:100px'>

                <form:form autocomplete="off" role="form" action="#" commandName="EmpQuarterBean"  method="post"  class="form-horizontal" enctype="multipart/form-data" id="id_noc_upload_frm">

                    <input type="hidden" name="trackingId" value="${trackingId}"/>
                    <input type="hidden" name="consumerNo" value="${consumerNo}"/>
                    <input type="hidden" name="empId" value="${empId}"/>
                     <input type="hidden" name="occupationTypes" value="${occupationTypes}"/>
                     <input type="hidden" name="oppCaseId" value="0"/>
                    

                    <div class="form-group">
                        <label class="control-label col-sm-4">Attach  Document:<br/>
                        (PDF Format only)</label>
                        <div class="col-sm-4">
                            <input type="file" class="form-control-file" id="uploadDocument" name='uploadDocument'>                                
                        </div>
                    </div>

                    <!------------------------- Transfer To NON KBK ---------------------> 

                    <div align='center' class='text-success' id='success_message' style='display:none'></div>    

                    <div align="center">

                        <input type="button" class="btn btn-primary" value="Save" onclick="return saveNOC()" id='id_save_noc_btm' />  
                    </div>



                </form:form>  



            </div>

        </div>


    </div>
</div>