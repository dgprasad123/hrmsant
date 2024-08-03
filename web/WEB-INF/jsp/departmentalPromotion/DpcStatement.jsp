<%-- 
    Document   : DpcStatement
    Created on : Dec 27, 2020, 9:16:45 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
      function isNumberKey(evt)
      {
         var charCode = (evt.which) ? evt.which : event.keyCode
         if (charCode > 31 && (charCode < 48 || charCode > 57))
            return false;

         return true;
      }            

            </script>
    </head>
    <body>
        <c:if test="${isSubmitted == 'N'}">
        <form id="frmTraining" name="frmDpc" method="POST" commandName="dpcform"  action="saveDPCConductedData.htm" onsubmit="return confirm('Are you sure you want to submit?')">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" style="font-size:15pt;font-weight:bold;">
                    DPCs Conducted to fill up vacancies of 2021
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-2">
                            Name of the Department/HOD/District:
                        </div>
                        <div class="col-lg-9">
                            <strong><c:out value="${officename}"/></strong>
                        </div>
                    </div>
                    <div class="row">
                        <table  class="table table-bordered">
                            <tr>
                                <td rowspan="2">Sl No </td>
                                <td colspan="2" rowspan="2">No. of DPCs due to be conducted to fill up vacancies of 2021 </td>
                                <td rowspan="2">No. of DPCs conducted to fill up vacancies of 2021 </td>
                                <td colspan="5">&nbsp;</td>
                                <td rowspan="2">No. of DPCs not conducted </td>
                                <td rowspan="2">Reasons thereof </td>
                            </tr>
                            <tr>
                                <td>Group-A</td>
                                <td>Group-B</td>
                                <td>Group-C</td>
                                <td>Group-D</td>
                                <td>Total</td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td>4</td>
                                <td>5</td>
                                <td>6</td>
                                <td>7</td>
                                <td>8</td>
                                <td>9</td>
                                <td>10</td>
                                <td>11</td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>Department Level </td>
                                <td><input type="text" name="dlDpcsDue" class="form-control" onkeypress="return isNumberKey(event)" required="required" /></td>
                                <td><input type="text" name="dlDpcs" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dlGroupA" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dlGroupB" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dlGroupC" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dlGroupD" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td>&nbsp;</td>
                                <td><input type="text" name="dlDpcsNotConducted" class="form-control"/></td>
                                <td><input type="text" name="dlReasonsThereof" class="form-control"/></td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>Heads of Department Level </td>
                                <td><input type="text" name="hdDpcsDue" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="hdDpcs" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="hdGroupA" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="hdGroupB" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="hdGroupC" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="hdGroupD" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td>&nbsp;</td>
                                <td><input type="text" name="hdDpcsNotConducted" class="form-control"/></td>
                                <td><input type="text" name="hdReasonsThereof" class="form-control"/></td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td>District Level </td>
                                <td><input type="text" name="dsDpcsDue" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dsDpcs" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dsGroupA" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dsGroupB" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dsGroupC" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td><input type="text" name="dsGroupD" class="form-control" onkeypress="return isNumberKey(event)" required="required"/></td>
                                <td>&nbsp;</td>
                                <td><input type="text" name="dsDpcsNotConducted" class="form-control" required="required"/></td>
                                <td><input type="text" name="dsReasonsThereof" class="form-control" required="required"/></td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="panel-footer">
                    <input type="submit" class="btn btn-primary" name="action" value="Submit"/>                      
                </div>
            </div>
        </div>
    </form>
        </c:if>
        <c:if test="${isSubmitted == 'Y'}">
            <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" style="font-size:15pt;font-weight:bold;">
                    DPCs Conducted to fill up vacancies of 2021
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-2">
                            Name of the Department/HOD/District:
                        </div>
                        <div class="col-lg-9">
                            <strong><c:out value="${officename}"/></strong>
                        </div>
                    </div>
                    <div class="row">
                        <table  class="table table-bordered">
                            <tr style="background:#C6E5FF;font-weight:bold;">
                                <td rowspan="2">Sl No </td>
                                <td colspan="2" rowspan="2">No. of DPCs due to be conducted to fill up vacancies of 2021 </td>
                                <td rowspan="2">No. of DPCs conducted to fill up vacancies of 2021 </td>
                                <td colspan="5">&nbsp;</td>
                                <td rowspan="2">No. of DPCs not conducted </td>
                                <td rowspan="2">Reasons thereof </td>
                            </tr>
                            <tr>
                                <td>Group-A</td>
                                <td>Group-B</td>
                                <td>Group-C</td>
                                <td>Group-D</td>
                                <td>Total</td>
                            </tr>
                            <tr style="font-weight:bold;background:#EAEAEA;">
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td>4</td>
                                <td>5</td>
                                <td>6</td>
                                <td>7</td>
                                <td>8</td>
                                <td>9</td>
                                <td>10</td>
                                <td>11</td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>Department Level </td>
                                <td>${dpcDetail.dlDpcsDue}</td>
                                <td>${dpcDetail.dlDpcs}</td>
                                <td>${dpcDetail.dlGroupA}</td>
                                <td>${dpcDetail.dlGroupB}</td>
                                <td>${dpcDetail.dlGroupC}</td>
                                <td>${dpcDetail.dlGroupD}</td>
                                <td>${dpcDetail.dlTotal}</td>
                                <td>${dpcDetail.dlDpcsNotConducted}</td>
                                <td>${dpcDetail.dlReasonsThereof}</td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>Heads of Department Level</td>
                                <td>${dpcDetail.hdDpcsDue}</td>
                                <td>${dpcDetail.hdDpcs}</td>
                                <td>${dpcDetail.hdGroupA}</td>
                                <td>${dpcDetail.hdGroupB}</td>
                                <td>${dpcDetail.hdGroupC}</td>
                                <td>${dpcDetail.hdGroupD}</td>
                                <td>${dpcDetail.hdTotal}</td>
                                <td>${dpcDetail.hdDpcsNotConducted}</td>
                                <td>${dpcDetail.hdReasonsThereof}</td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td>District Level</td>
                                <td>${dpcDetail.dsDpcsDue}</td>
                                <td>${dpcDetail.dsDpcs}</td>
                                <td>${dpcDetail.dsGroupA}</td>
                                <td>${dpcDetail.dsGroupB}</td>
                                <td>${dpcDetail.dsGroupC}</td>
                                <td>${dpcDetail.dsGroupD}</td>
                                <td>${dpcDetail.dsTotal}</td>
                                <td>${dpcDetail.dsDpcsNotConducted}</td>
                                <td>${dpcDetail.dsReasonsThereof}</td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="panel-footer">
                    <input type="button" class="btn btn-primary" name="action" value="Print PDF" onclick="javascript: window.open('GenerateDPCpdf.htm', '', 'width=600,height=600')"/>                      
                </div>
            </div>
        </div>
        </c:if>                        
    </body>

</html>
