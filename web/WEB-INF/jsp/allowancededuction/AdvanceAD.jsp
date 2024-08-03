<%@page contentType="text/html" pageEncoding="UTF-8" buffer="512kb"%>
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
            $(document).ready(function(){
              // $('#fixed_blk').animate({top:"0px"}); 
            });
            function checkType(amtType, postCode)
            {
                $('#formula_' + postCode).attr("disabled", false);
                $('#advalue_' + postCode).attr("readonly", false);
                $('#formula_' + postCode).val('<=====>');
                $('#advalue_' + postCode).val("");
                if (amtType == '1')
                {
                    //$('#formula_' + postCode).attr("disabled", true);
                    $('#formula_' + postCode).val('<=====>');
                }
                else if (amtType == '0')
                {
                    $('#advalue_' + postCode).attr("readonly", true);
                    $('#advalue_' + postCode).val("");
                }
            }
            function checkAmt(amt, postCode)
            {
                if (amt != '')
                {
                    $('#adamttype_' + postCode).val("1");
                    //$('#formula_' + postCode).attr("disabled", true);
                    $('#formula_' + postCode).val('<=====>');
                }
                else
                {
                    $('#adamttype_' + postCode).val("");
                    $('#formula_' + postCode).attr("disabled", false);
                    
                }
            }
function checkFormula(formula, postCode)
            {
                if (formula != '<=====>')
                {
                    $('#adamttype_' + postCode).val("0");
                    $('#advalue_' + postCode).val("");
                    $('#advalue_' + postCode).attr("readonly", true);
                }
                else
                {
                    $('#adamttype_' + postCode).val("");
                    $('#advalue_' + postCode).val(""); 
                    $('#advalue_' + postCode).attr("readonly", false);
                }
            }            
            function validateForm()
            {
                for (var i = 0; i < $('#frmAD')[0].elements['uid[]'].length; i++)
                {
                    postId = $('#frmAD')[0].elements['uid[]'][i].value;
                    if ($('#adamttype_' + postId).val() == '1' && $('#advalue_' + postId).val() == '')
                    {
                        alert("Please enter a valid Fixed Value. Must be Integer.");
                        $('#advalue_' + postId)[0].focus();
                        return false;
                    }
                    if ($('#adamttype_' + postId).val() == '1' && isNaN($('#advalue_' + postId).val()))
                    {
                        alert("Please enter a valid Integer Value.");
                        $('#advalue_' + postId)[0].focus();
                        $('#advalue_' + postId)[0].select();
                        return false;
                    }
                    if ($('#adamttype_' + postId).val() == '0' && $('#formula_' + postId).val() == '')
                    {
                        alert("Please select a formula.");
                        $('#formula_' + postId)[0].focus();
                        return false;
                    }
                }
            }
        </script>
    </head>
    <body>

        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-body">
                    <form:form class="form-inline" id="frmAD" action="SaveAdvanceADAction.htm" method="POST" commandName="AllowanceDeductionbean" onsubmit="javascript: return validateForm()">
                        <input type="hidden" name="adcode" value="${adCode}" />
                        <input type="hidden" name="adtype" value="${adType1}" />
                        <input type="hidden" name="addesc" value="${adDesc}" />
                        <div id="fixed_blk" style="width:100%;padding:5px;background:#EAEAEA;height:45px;position:fixed;left:0px;top:0px;background:url('images/trans_bg.png')">
                            <div style="float:left;width:200px;"><input type="submit" name="action" value="Cancel" class="btn btn-success btn-md" style="font-weight:bold;background:#00629B" /></div>
                            <div style="float:right;"><input type="submit" name="action" value="Save Config" class="btn btn-success btn-md" style="font-weight:bold;" /></div>
                            <div style="clear:both;"></div>
                        </div>
                    </div>                    
                    <table class="table table-bordered">

                        <thead>
                            <tr>
                                <td colspan="5" align="center"><h2 style="color:#00799B;margin:0px;font-size:14pt;margin-bottom:8px;">Configuring <strong>${adDesc}</strong> <span style="color:#008900;font-weight:bold;">[${adType}]</span></h2></td>
                            </tr>                            
                            <tr>
                                <th width="5%">Sl No</th>
                                <th width="35%">POST</th>
                                <th width="15%">FORMULA/FIXED?</th>
                                <th width="15%">FIXED VALUE</th>
                                <th width="15%">FORMULA</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${adlist}" var="ol" varStatus="cnt">
                                <tr>
                                    <td>
                                        ${cnt.index+1}
                                        <input type="hidden" name="postCode" value="${ol.postCode}" />
                                        <input type="hidden" name="uid[]" value="${ol.postCode}" />
                                    </td>
                                    <td>${ol.postName}</td>
                                    <td><select class="form-control" name="adamttype" id="adamttype_${ol.postCode}" onchange="javascript: checkType(this.value, '${ol.postCode}')">
                                            <option value="">--Select One--</option>
                                            <option value="1"<c:if test="${ol.adamttype=='1'}"> selected="selected"</c:if>>FIXED</option>
                                            <option value="0"<c:if test="${ol.adamttype=='0'}"> selected="selected"</c:if>>FORMULA</option>
                                            </select>
                                        </td>
                                        <td><input type="text" id="advalue_${ol.postCode}" name="fixedValue" value="${ol.fixedValue}" class="form-control" onkeyup="javascript:checkAmt(this.value, '${ol.postCode}')" onblur="javascript:checkAmt(this.value, '${ol.postCode}')"  /></td>
                                    <td>
                                        <select class="form-control" name="formula" id="formula_${ol.postCode}" onchange="checkFormula(this.value, '${ol.postCode}')">
                                            <option value="<=====>">-Select-</option>
                                            <c:forEach items="${formulaList}" var="fl">
                                                <option value="${fl.formulaName}"<c:if test="${ol.formula==fl.formulaName}"> selected="selected"</c:if>>${fl.formulaName}</option> 
                                            </c:forEach>
                                        </select></td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                </form:form>
            </div>
        </div>
    </div>
</body>
</html>
