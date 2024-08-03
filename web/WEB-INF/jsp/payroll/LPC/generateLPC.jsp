<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            function lpc_month_year() {
                var selmonths = $("#Selmonth option:selected").text();
                var selYears = $("#Selyear option:selected").text();
                if (selmonths != '' && selYears != '') {
                    var MonthYear = selmonths + "-" + selYears;
                    $("#SelectedMonthYear").val(MonthYear);
                } else {

                    $("#SelectedMonthYear").val("");
                }
            }
        </script>    
    </head>
    <body>

        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;layout: fixed;color:#000000;font-weight:bold;">
                                <thead> </thead>
                                <tr>
                                    <td width="20%" align="right">
                                        Employee Name:                    
                                    </td>
                                    <td width="38%" style="text-transform:uppercase;" align="left">
                                        <b> ${SelectedEmpObj.fullName} </b>
                                    </td>
                                    <td width="16%" align="right">
                                        HRMS ID:                    
                                    </td>
                                    <td width="26%">
                                        ${SelectedEmpObj.empId} 
                                    </td>
                                </tr>

                                <tr>
                                    <td align="right">Current Post: </td>
                                    <td >
                                        &nbsp; ${SelectedEmpObj.postname} 
                                    </td>
                                    <td align="right">GPF/ PPAN No:</td>
                                    <td><b style="text-transform:uppercase;"> ${SelectedEmpObj.gpfno}     &nbsp;</b></td>
                                </tr>
                                <tr>
                                    <td align="right">Current Cadre: </td>
                                    <td align="left"><b> ${SelectedEmpObj.cadrename}   &nbsp;</b></td>
                                    <td align="right">Current Status:</td>
                                    <td><b> ${SelectedEmpObj.depstatus}&nbsp;</b></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="panel panel-success">
                    <div class="panel-heading">Generate Last Pay Certificate</div>
                    <form:form role="form" action="downloadLPCReport.htm" commandName="payslipform"  method="post">
                        <input type="hidden" name='SelectedMonthYear' id='SelectedMonthYear' value=""/>
                        <input type="hidden" name='rlvId' id='rlvId' value="${rlvId}"/>
                       
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    Month:<span style="color: red">*</span>
                                </div>
                                <div class="col-lg-4">
                                    <Select name="month" id='Selmonth' class="form-control" required="1" onchange="lpc_month_year()">
                                        <option value="">Select Month</option>
                                        <option value="0" >January </option>
                                        <option value="1"  >February</option>
                                        <option value="2" >March</option>
                                        <option value="3" >April</option>
                                        <option value="4" >May</option>
                                        <option value="5" >June</option>
                                        <option value="6">July</option>
                                        <option value="7" >August</option>
                                        <option value="8">September</option>
                                        <option value="9">October</option>
                                        <option value="10">November</option>
                                        <option value="11">December</option>
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                    Year:<span style="color: red">*</span>
                                </div>
                                <div class="col-lg-4">
                                    <Select name="year" id='Selyear'  class="form-control" required="1" onchange="lpc_month_year()">
                                        <option value="">Select Year</option> 
                                         <option value="2019" >2019 </option>
                                        <option value="2020" >2020 </option>				
                                        <option value="2021">2021 </option>
                                        <option value="2022">2022 </option>
                                        <option value="2023">2023 </option>
                                        <option value="2024">2024 </option>
                                    </select>   
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;padding-top: 20px;">
                                <div class="col-lg-1">
                                    Relieved On<span style="color: red">*</span>
                                </div>
                                <div class="col-lg-4">
                                    <div class="input-group date" id="processDate">
                                        <input type="text" id="txtRlvDt" class=" form-control txtDate"  name='txtRlvDt' required="1"/>                                  
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                              
                                </div>
                                <div class="col-lg-1">
                                    Time<span style="color: red">*</span>
                                </div> 
                                <div class="col-lg-4">
                                    <Select name="sltRlvTime" id='sltRlvTime'  class="form-control" required="1" >
                                        <option value="">Select Time</option>                                                     
                                        <option value="FN" >Fore Noon </option>				
                                        <option value="AN">After Noon </option>                                    
                                    </select> 
                                </div>
                            </div>
                              <div class="row" style="margin-bottom: 7px;padding-top: 20px;">
                                <div class="col-lg-1">
                                    Comments(If Any)
                                </div>
                                <div class="col-lg-9">
                                    <textarea name="comments" id="comments" rows="4" class="form-control"></textarea>                           
                                </div>
                                
                            </div>


                            <div class="row" style="margin-bottom: 7px;padding-top: 20px;">
                                <div class="col-lg-5">
                                    &nbsp;
                                </div>
                                <div class="col-lg-6">
                                    <input type="submit" class="btn btn-primary" value="Generate PDF"  />&nbsp;
                                    <a href="RelieveList.htm" class="btn btn-primary" >Back</a>

                                </div>

                            </div>

                        </form:form>



                    </div>
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(function () {
                $('.txtDate').datetimepicker({
                    format: 'DD.MM.YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
        </script>
    </body>
</html>
