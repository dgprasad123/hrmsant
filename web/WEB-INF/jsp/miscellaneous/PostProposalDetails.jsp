<%-- 
    Document   : PostProposalDetails
    Created on : Dec 29, 2018, 7:17:45 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" buffer="128kb"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/jquery.min.js" type="text/javascript"></script> 
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/basicjavascript.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            
            
            function validate(){
                
                if($("#postname").val()==''){
                    alert('Please enter Post Name.');
                    $("#postname").focus();
                    return false;
                }
                
                if($("#category").val()==''){
                    alert('Please Select Category.');
                    $("#category").focus();
                    return false;
                }
                
                if($("#goorderno").val()==''){
                    alert('Please Enter Government Order Number.');
                    $("#goorderno").focus();
                    return false;
                }
                
                if($("#godate").val()==''){
                    alert('Please Select Order Date.');
                    $("#godate").focus();
                    return false;
                }
                
            }
            
            function calculatePost(id1, id2, id3){
                var tot=0;
                tot=parseInt($("#"+id1).val())+parseInt($("#"+id2).val());
                $("#"+id3).val(tot);
            }
            
        </script>
    </head>
    <body>
        <form:form class="form-horizontal" commandName="postProposal" action="SavePostProposalDetail.htm">
            <form:hidden path="porposalId"/>
            <div class="container-fluid" style="margin-top: 10px;">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        THE PROPOSAL FOR DIRECT RECRUITMENT AGAINST VACANT POSTS
                    </div>
                    <div class="panel-body">

                        <div class="form-group">
                            <label class="control-label col-sm-2">Post Name:</label>
                            <div class="col-sm-4">
                                <form:input path="postname" cssClass="form-control"/>                                
                            </div>                        
                            <label class="control-label col-sm-1">Category:</label>
                            <div class="col-sm-2">
                                <form:select path="category" cssClass="form-control">
                                    <form:option value=""> -- Select One --</form:option>
                                    <form:option value="A"> A </form:option>
                                    <form:option value="B"> B </form:option>
                                    <form:option value="C"> C </form:option>
                                    <form:option value="D"> D </form:option>
                                </form:select>
                            </div>                            
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Pay Band & G.P:</label>
                            <div class="col-sm-4">
                                <form:input path="payband" cssClass="form-control"/>                                
                            </div>
                            <label class="control-label col-sm-1">Cadre:</label>
                            <div class="col-sm-2">
                                <form:input path="cadre" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Government Order No</label>
                            <div class="col-sm-2">
                                <form:input path="goorderno" cssClass="form-control"/>                                
                            </div>                            
                            <div class="col-sm-2">
                                <form:input path="godate" cssClass="form-control txtDate" readonly="true"/>
                            </div>
                        </div>
                        <hr style="border:1px solid #95B8E7;"/>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Sanctioned Strength of Regular Posts</label>
                            <label class="control-label col-sm-1">Plan</label>
                            <div class="col-sm-2">
                                <form:input path="sancstrengthplan" cssClass="form-control" onkeyup="calculatePost('sancstrengthplan','sancstrengthnonplan','sancstrengthtotal')"/>                                
                            </div>
                            <label class="control-label col-sm-1" for="pwd">Non-Plan</label>
                            <div class="col-sm-2">
                                <form:input path="sancstrengthnonplan" cssClass="form-control" onkeyup="calculatePost('sancstrengthplan','sancstrengthnonplan','sancstrengthtotal')"/>                                
                            </div>
                            <label class="control-label col-sm-1" for="pwd">Total</label>
                            <div class="col-sm-2">
                                <form:input path="sancstrengthtotal" cssClass="form-control" readonly="true"/>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Person in position against Sanctioned Posts</label>
                            <label class="control-label col-sm-1">Plan</label>
                            <div class="col-sm-2">
                                <form:input path="meninpositionplan" cssClass="form-control" onkeyup="calculatePost('meninpositionplan','meninpositionnonplan','meninpositiontotal')"/>
                            </div>
                            <label class="control-label col-sm-1">Non-Plan</label>
                            <div class="col-sm-2">
                                <form:input path="meninpositionnonplan" cssClass="form-control" onkeyup="calculatePost('meninpositionplan','meninpositionnonplan','meninpositiontotal')"/>
                            </div>
                            <label class="control-label col-sm-1">Total</label>
                            <div class="col-sm-2">
                                <form:input path="meninpositiontotal" cssClass="form-control" readonly="true"/>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Number of Contractual employee Regularised</label>
                            <label class="control-label col-sm-1">Plan</label>
                            <div class="col-sm-2">
                                <form:input path="noofcontractualplan" cssClass="form-control" onkeyup="calculatePost('noofcontractualplan','noofcontractualnonplan','noofcontractualtotal')"/>
                            </div>
                            <label class="control-label col-sm-1">Non-Plan</label>
                            <div class="col-sm-2">
                                <form:input path="noofcontractualnonplan" cssClass="form-control" onkeyup="calculatePost('noofcontractualplan','noofcontractualnonplan','noofcontractualtotal')"/>
                            </div>
                            <label class="control-label col-sm-1">Total</label>
                            <div class="col-sm-2">
                                <form:input path="noofcontractualtotal" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Number of employees conferred</label>
                            <label class="control-label col-sm-1">Plan</label>
                            <div class="col-sm-2">
                                <form:input path="noofconferredplan" cssClass="form-control" onkeyup="calculatePost('noofconferredplan','noofconferrednonplan','noofconferredtotal')"/>
                            </div>
                            <label class="control-label col-sm-1">Non-Plan</label>
                            <div class="col-sm-2">
                                <form:input path="noofconferrednonplan" cssClass="form-control" onkeyup="calculatePost('noofconferredplan','noofconferrednonplan','noofconferredtotal')"/>
                            </div>
                            <label class="control-label col-sm-1">Total</label>
                            <div class="col-sm-2">
                                <form:input path="noofconferredtotal" cssClass="form-control" readonly="true"/>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Vacancies against sanctioned post</label>
                            <label class="control-label col-sm-1">Plan</label>
                            <div class="col-sm-2">
                                <form:input path="noofvacancyplan" cssClass="form-control" onkeyup="calculatePost('noofvacancyplan','noofvacancynonplan','noofvacancytotal')"/>
                            </div>
                            <label class="control-label col-sm-1">Non-Plan</label>
                            <div class="col-sm-2">
                                <form:input path="noofvacancynonplan" cssClass="form-control" onkeyup="calculatePost('noofvacancyplan','noofvacancynonplan','noofvacancytotal')"/>
                            </div>
                            <label class="control-label col-sm-1">Total</label>
                            <div class="col-sm-2">
                                <form:input path="noofvacancytotal" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="pwd">Vacant posts (Group 'A','B','C' & 'D')</label>
                            <label class="control-label col-sm-1" for="pwd">Plan</label>
                            <div class="col-sm-2">
                                <form:input path="noofvacantplan" cssClass="form-control" onkeyup="calculatePost('noofvacantplan','noofvacantnonplan','noofvacanttotal')"/>
                            </div>
                            <label class="control-label col-sm-1" for="pwd">Non-Plan</label>
                            <div class="col-sm-2">
                                <form:input path="noofvacantnonplan" cssClass="form-control" onkeyup="calculatePost('noofvacantplan','noofvacantnonplan','noofvacanttotal')"/>
                            </div>
                            <label class="control-label col-sm-1" for="pwd">Total</label>
                            <div class="col-sm-2">
                                <form:input path="noofvacanttotal" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>
                        <hr style="border:1px solid #95B8E7;"/>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Justification:</label>
                            <div class="col-sm-4">
                                <form:textarea cssClass="form-control-plaintext" path="justification"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Remark:</label>
                            <div class="col-sm-4">
                                <form:textarea cssClass="form-control-plaintext" path="remark"/>
                            </div>
                        </div>

                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" class="btn btn-default" value="Save" onclick="return validate()"/>  
                        <a href="getProposalList.htm"><input type="button" name="action" class="btn btn-default" value="Back"/> </a>
                        
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
