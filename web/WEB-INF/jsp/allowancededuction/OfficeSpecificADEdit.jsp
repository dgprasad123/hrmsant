<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            function validateForm()
            {
                if ($('#adamttype').val() == '')
                {
                    alert("Please select Fixed or Formula.");
                    $('#adamttype')[0].focus();
                    return false;
                }
                if ($('#adamttype').val() == '1' && $('#advalue').val() == '')
                {
                    alert("Please enter a valid Fixed Value. Must be Integer.");
                    $('#advalue')[0].focus();
                    return false;
                }
                if ($('#adamttype').val() == '1' && isNaN($('#advalue').val()))
                {
                    alert("Please enter a valid Integer Value.");
                    $('#advalue')[0].focus();
                    $('#advalue')[0].select();
                    return false;
                }
            }
            function deleteAdInfo()
            {
                if(confirm("Are you sure you want to delete the Formula/Fixed Value?"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-horizontal" action="saveOfficeAllowanceAndDeduction.htm" method="POST" commandName="AllowanceDeductionbean">
                <div class="panel panel-default">
                    <div class="panel-heading">

                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="addesc">Description:</label>
                                    <div class="col-sm-9">
                                        <form:hidden path="adcode"/>
                                        <form:hidden path="adtype"/>
                                        <form:hidden path="whereupdated"/>
                                        <form:hidden path="updationRefCode"/>
                                        <form:hidden path="addesc"/>
                                        <label class="control-label" style="color:#008900;font-weight:bold;">${AllowanceDeductionbean.addesc}</label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="advalue">Fixed or Formula:</label>
                                    <div class="col-sm-9">
                                        <form:select path="adamttype" id="adamttype" class="form-control" onchange="javascript: checkType(this.value)">
                                            <option value="">--Select One--</option>
                                            <form:option value="1">FIXED</form:option>
                                            <form:option value="0">FORMULA</form:option>
                                        </form:select>                                        
                                    </div>
                                </div>                                    
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="advalue">Amount:</label>
                                    <div class="col-sm-9">                                        
                                        <form:input path="advalue" id="advalue" class="form-control"  />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="formula">Formula:</label>
                                    <div class="col-sm-9">                                        
                                        <form:select path="formula" id="formula" class="form-control">
                                            <option value="">--Select One--</option>
                                            <form:options items="${formulaList}" itemLabel="formulaName" itemValue="formulaName"/>
                                        </form:select>
                                    </div>
                                </div>  
                                    <div class="form-group">
                                    <label class="control-label col-sm-3" for="advalue">Object Head/BT ID:</label>
                                    <div class="col-sm-9">                                        
                                        <form:input path="head" id="head" class="form-control"  />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" class="btn btn-default" id="btn1" name="action" value="Save" onclick="javascript: return validateForm();" />
                        <input type="submit" class="btn btn-default" id="btn2" name="action" value="Cancel" />
                        <c:if test="${AllowanceDeductionbean.isupdated > 0}"><input type="submit" class="btn btn-default" style="background:#890000;color:#FFFFFF;" id="btn3" name="action" value="Delete" onclick="javascript: return deleteAdInfo()" /></c:if>
                        <input type="submit" class="btn btn-default" style="background:#00799B;color:#FFFFFF;" id="btn4" name="action" value="Advance" />
                    </div>
                </div>
            </form:form>
            <script type="text/javascript">
                $(document).ready(function() {
                    if ($('#adamttype').val() == '1')
                    {
                        $('#formula').attr("disabled", true);
                    }
                    if ($('#adamttype').val() == '0')
                    {
                        $('#advalue').attr("readonly", true);
                    }
                });
                function checkType(amtType)
                {
                    $('#formula').attr("disabled", false);
                    $('#advalue').attr("readonly", false);
                    if (amtType == 1)
                    {
                        $('#formula').attr("disabled", true);
                    }
                    if (amtType == 0)
                    {
                        $('#advalue').attr("readonly", true);
                    }
                }
            </script>
        </div>
    </body>
</html>
