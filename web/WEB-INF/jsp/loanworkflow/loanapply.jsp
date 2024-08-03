<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/color.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css">
        <link rel="stylesheet" type="text/css" href="css/loanhba.css">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <link  rel="stylesheet" type="text/css"  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function applyloan()
            {

                var radios = document.getElementsByName("loanapplyfor");
                var formValid = false;

                var i = 0;
                while (!formValid && i < radios.length) {
                    if (radios[i].checked)
                        formValid = true;
                    i++;
                }
                if (!formValid) {
                    alert("Please choose Loan Apply For");
                    return false;
                }


                var antprice = $("#txtantprice").val();
                if (isNaN(antprice) || antprice == "") {
                    alert("Invalid Anticipated Price");
                    // $("#txtantprice").textbox('focus');
                    return false;
                }
                var purtype = $('#purtype').val();
                if ($("#purtype:checked").length == 0) {
                    alert("Please select Purchase Type");
                    return false;
                }
                var amountadv = $("#amountadv").val();
                if (isNaN(amountadv) || amountadv == "") {
                    alert("Invalid Amount of advance");
                    document.getElementById("amountadv").focus();
                    return false;
                }
                if (parseInt(antprice) < parseInt(amountadv)) {
                    alert("Amount of advance should not  be more than Anticipated Price");
                    return false;

                }
                var instalments = $("#instalments").val();
                if (isNaN(instalments) || instalments == "") {
                    alert("Invalid No of instalments");
                    document.getElementById("instalments").focus();
                    return false;
                }

                if (instalments > 50 || instalments < 1) {
                    alert("No of instalments should not be more than 50 ");
                    document.getElementById("instalments").focus();
                    return false;
                }

                var radiosprevious = document.getElementsByName("previousAvail");
                var formValidprevious = false;

                var j = 0;
                while (!formValidprevious && j < radiosprevious.length) {
                    if (radiosprevious[j].checked)
                        formValidprevious = true;
                    j++;
                }
                if (!formValidprevious) {
                    alert("Please choose Whether advance for similar purpose was availed previously?");
                    return false;
                }

                var previousloan = $("input[name='previousAvail']:checked").val();
                if (previousloan == "Yes") {
                    var PreAdvPur = $("#PreAdvPur").val();
                    if (PreAdvPur == "") {
                        alert("Please enter Whether for Motor Car/Cycle/Moped/Personal Computer was availed previously?");
                        return false;
                    }
                    var amounpretadv = $("#amounpretadv").val();
                    if (amounpretadv == "" || isNaN(amounpretadv)) {
                        alert("Invalid Amount of advance was availed previously? ");
                        return false;
                    }
                    var dateofdrawal = $("#dateofdrawal").val();
                    if (dateofdrawal == "") {
                        alert("Please enter Date of drawal advance");
                        return false;
                    }
                }


                var forwardto = $("#forwardto").val();
                if (forwardto == "") {
                    alert("Please select your loan authority ");
                    document.getElementById("forwardto").focus();
                    return false;
                }
                var fup = document.getElementById('file_att');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();
                if (ext && ext != "pdf") {
                    alert("Upload pdf files only");
                    fup.focus();
                    return false;
                }
                var confirmloan = confirm("Are you sure to apply this loan? ");
                if (!confirmloan) {
                    return false;
                }
            }
            function changepost() {
                var url = 'ChangePostLoanController.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "50%"});
            }

            function SelectSpn(offCode, spc, offName, authName, spc_hrmsid)
            {
                $.colorbox.close();
                $('#hidSPC').val(spc);
                $('#hidOffCode').val(offCode);
                $("#hidOffName").val(offName);
                $("#forwardto").val(authName);
                $("#forwardtoHrmsid").val(spc_hrmsid);
            }
            function purpose_advance(vals) {

                if (vals == "No") {
                    $('#PreAdvPur').val('');
                    $('#amounpretadv').val('');
                    $('#dateofdrawal').val('');
                    $('#13a').hide();
                    $('#13b').hide();
                    $('#13c').hide();
                    document.getElementById('intpaidfull_1').checked = true;
                     $('#13e').hide();
                    $('#amountstanding').textbox('clear');
                } else {
                    $('#13a').show();
                    $('#13b').show();
                    $('#13c').show();
                    $('#13e').show();
                    document.getElementById('intpaidfull_1').checked = false;
                }
            }
            function interest_paid(vals) {
                if (vals == "No") {
                    $('#13e').show();

                } else {
                    $('#13e').hide();
                    $('#amountstanding').textbox('clear');
                }
            }
            $(document).ready(function() {
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });

            });

        </script>    
    </head>

    <body>       
        <form action="################saveLoan######.htm" method="POST" commandName="LoanForm" onsubmit="return applyloan()" enctype="multipart/form-data">
            <div class="form-group">
                <input type="hidden" name="empName" id="empName" value="${LoanForm.empName}"/>
                <input type="hidden" name="designation" id="designation" value="${LoanForm.designation}"/>
                <input type="hidden" name="basicsalary" id="basicsalary" value="${LoanForm.basicsalary}"/>
                <input type="hidden" name="netsalary" id="netsalary" value="${LoanForm.netsalary}"/>
                <input type="hidden" name="empSPC" id="empSPC" value="${LoanForm.empSPC}"/>
                <input type="hidden" name="hidOffCode" id="hidOffCode"/>
                <input type="hidden" name="hidOffName" id="hidOffName"/>
                <input type="hidden" name="hidSPC" id="hidSPC"/>
                <input type="hidden" name="forwardtoHrmsid" id="forwardtoHrmsid"/>

                <div id="tbl-container">
                    <h4 class="bg-primary loanheader">Application form for Advance for the purpose of Motor car/motor cycle/Moped/personal computer </h4>
                    <div class="container-fluid">
                        <table class="table table-striped table-top">
                            <tbody>
                                <tr>
                                    <td>1. Name</td>
                                    <td> 
                                        ${LoanForm.empName}
                                    </td>
                                </tr>
                                <tr>
                                    <td>2. Designation</td>
                                    <td> 
                                        ${LoanForm.designation}
                                    </td>
                                </tr>
                                <tr>
                                    <td>3. Office address</td>
                                    <td> 
                                        ${LoanForm.offaddress}
                                    </td>
                                </tr>
                                <tr>
                                    <td>4. Job Type</td>
                                    <td> 
                                        ${LoanForm.jobType}
                                    </td>
                                </tr>
                                <tr>
                                    <td>5. Basic  salary</td>
                                    <td> 
                                        ${LoanForm.basicsalary}
                                    </td>
                                </tr>
                                <tr>
                                    <td>6. Net  salary</td>
                                    <td> 
                                        ${LoanForm.netsalary}
                                    </td>
                                </tr>
                                <tr>
                                    <td>7.DOB</td>
                                    <td> 
                                        ${LoanForm.empdob}
                                    </td>
                                </tr>
                                <tr>
                                    <td>8.Date of Superannuation</td>
                                    <td> 
                                        ${LoanForm.superannuation}
                                    </td>
                                </tr>
                                <tr>
                                    <td valign='top'>9.Loan Apply For</td>
                                    <td> 
                                        <input type="radio" class="form-check-input" name="loanapplyfor"  value="MOTOR CAR" />&nbsp;MOTOR CAR<br/>
                                        <input type="radio" class="form-check-input" name="loanapplyfor"  value="MOTOR CYCLE" />&nbsp;MOTOR CYCLE<br/>
                                        <input type="radio" class="form-check-input" name="loanapplyfor"  value="MOPED"  />&nbsp;MOPED<br/>
                                        <input type="radio" class="form-check-input" name="loanapplyfor"  value="PERSONAL COMPUTER" />&nbsp;PERSONAL COMPUTER<br/>

                                    </td>
                                </tr>

                                <tr>
                                    <td>10.Anticipated Price</td>
                                    <td> 
                                        <input  class="form-control motorloan" id="txtantprice" type="text" name="antprice"></input>
                                    </td>
                                </tr>
                                <tr>
                                    <td>11.Purchase Type</td>
                                    <td> 
                                        <input type="radio" class="easyui-radio" name="purtype" id='purtype' value="New"  />&nbsp;New &nbsp;<input type="radio" class="easyui-radio" name="purtype" value="Second Hand" id='purtype' />Second Hand
                                    </td>
                                </tr>
                                <tr>
                                    <td>12.Amount of advance required</td>
                                    <td> 
                                        <input class="form-control motorloan" id="amountadv" type="text" name="amountadv"></input>
                                    </td>
                                </tr>
                                <tr>
                                    <td>13.No of installments</td>
                                    <td> 
                                        <input class="form-control motorloan" id="instalments" type="text" name="instalments" ></input>
                                    </td>
                                </tr>
                                <tr>
                                    <td>14.Whether advance for similar purpose was availed previously?</td>
                                    <td>
                                        <input type="radio" class="easyui-radio" name="previousAvail"  value="Yes"  onclick="purpose_advance(this.value)" />&nbsp;Yes &nbsp;<input type="radio" class="easyui-radio" name="previousAvail" value="No"  onclick="purpose_advance(this.value)" />No
                                    </td>   
                                </tr>
                                <tr id="13a" >
                                    <td>15.Whether for Motor Car/Cycle/Moped/Personal Computer</td>
                                    <td>
                                        <input class="form-control motorloan" id="PreAdvPur" type="text" name="PreAdvPur"></input>
                                    </td>   
                                </tr>
                                <tr  id="13b">
                                    <td>16.Amount of  advance</td>
                                    <td>
                                        <input class="form-control motorloan" id="amounpretadv" type="text" name="amounpretadv"></input>
                                    </td>   
                                </tr>
                                <tr id="13c">
                                    <td>17.Date of drawal advance</td>
                                    <td>

                                        <input class="txtDate form-control motorloan" id="dateofdrawal" type="text" name="dateofdrawal" readonly></input>

                                    </td>   
                                </tr>
                                <tr>
                                    <td>18.Principal along with Interest paid in Full? </td>
                                    <td>
                                        <input type="radio" class="easyui-radio" name="intpaidfull" id='intpaidfull_1' value="Yes"  onclick="interest_paid(this.value)" />Yes &nbsp;<input type="radio" class="easyui-radio" name="intpaidfull" value="No" id="intpaidfull_2" onclick="interest_paid(this.value)"/>No
                                    </td>   
                                </tr>
                                <tr id="13e" >
                                    <td>19.Amount of principal/interest standing</td>
                                    <td>
                                        <input class="form-control motorloan" id="amountstanding" type="text" name="amountstanding" ></input>
                                    </td>   
                                </tr>
                                <tr>
                                    <td>20.Whether the officer is on leave or is about to proceed?</td>
                                    <td>
                                        <input type="radio" class="easyui-radio" name="officerleave" value="Yes"  />Yes &nbsp;<input type="radio" class="easyui-radio" name="officerleave" value="No"  />No
                                    </td>   
                                </tr>
                                <tr>
                                    <td>21.Date of commencement leave</td>
                                    <td>                              
                                        <input class="txtDate form-control motorloan" id="datecommleave" type="text" name="datecommleave" readonly></input>
                                    </td>   
                                </tr>
                                <tr>
                                    <td>22.Date of expire leave</td>
                                    <td>

                                        <input class="txtDate form-control motorloan" id="dateexpireleave" type="text" name="dateexpireleave" readonly></input>
                                    </td>   
                                </tr>
                                <tr>
                                    <td>23.Attachment</td>
                                    <td>
                                        <input id="file_att" class='form-control motorloan' type="file" name="file_att"></input>
                                    </td>   
                                </tr>
                                <tr>
                                    <td>24.Forward to</td>
                                    <td>
                                        <div class="row">
                                            <div class="col-md-4">
                                                <input class="form-control" id="forwardto" type="text" name="forwardto" readonly="true" ></input>
                                            </div>
                                            <div class="col-md-8">
                                                <a href="javascript:void(0)" id="change" onclick="changepost()">
                                                    <button type="button" class="btn btn-info"><i class="fa fa-search" aria-hidden="true"></i>Search</button>
                                                </a>
                                            </div>
                                        </div>
                                    </td>   
                                </tr>


                            </tbody>
                        </table>
                    </div>      
                    <div class="text-center col-sm-12" style="margin-top: 20px;">
                        <input class="btn btn-success" type="submit" name="Save" value="Apply"/>
                        <input class="btn" type="button" name="Back" value="Back" onclick="self.location = 'loanList.htm'"/>

                    </div>      

                </div>
            </div>                    
        </form>
    </body>
</html>