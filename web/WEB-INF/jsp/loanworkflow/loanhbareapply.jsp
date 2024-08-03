<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String downloadlink = "";
    String deleteAttach = "";
    String loanid = "";
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css">

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
               var forwardto=$("#forwardto").val();
                 if ( forwardto == "") {
                    alert("Please select your loan authority ");
                    document.getElementById("forwardto").focus();
                    return false;
                }      
                var confirmloan = confirm("Do you want to apply HBA Long term advance ");
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
                    $('#PreAdvPur').textbox('clear');
                    $('#amounpretadv').textbox('clear');
                    $('#dateofdrawal').textbox('clear');
                    $('#13a').hide();
                    $('#13b').hide();
                    $('#13c').hide();
                } else {
                    $('#13a').show();
                    $('#13b').show();
                    $('#13c').show();


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
            $(document).ready(function () {
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
                 	

           // $( "#datecommleave" ).datepicker( "setDate", '${LoanForm.datecommleave}' );

            });
            
            function delAttach(obj,val_id) {
                if (confirm("Are you sure to Delete?")) {
                    var dataString = 'loanId=' + obj+'&vals=' +val_id;
                    //alert("dataString is: " + dataString);
                    $.ajax({
                        type: "POST",
                        url: 'deleteHBALoanAttachment.htm',
                        data: dataString,
                        dataType: "json"
                    }).done(function(serverResponse) {
                        $.messager.alert(serverResponse.msgType, serverResponse.msg);
                    });
                }
            }
        </script>    
    </head>

    <body> 
        <h4 class="bg-primary">Application form prescribed under the rules regulating the grant of advance to Government servants For building OF Houses </h4>
      
        <form class="form-horizontal"  action="saveHBAreapplyLoan.htm" method="POST" commandName="LoanHBAForm" onsubmit="return applyloan()" enctype="multipart/form-data">
             <input type="hidden" name="loanId" id="loanid" value="${LoanHBAForm.loanId}"/>
            <input type="hidden" name="taskid" id="taskid" value="${LoanHBAForm.taskid}"/>
             <input type="hidden" name="approvedBy" id="approvedBy" value="${LoanHBAForm.approvedBy}"/>
              <input type="hidden" name="approvedSpc" id="approvedSpc" value="${LoanHBAForm.approvedSpc}"/>
              <input type="hidden" name="hidOffCode" id="hidOffCode"/>
            <input type="hidden" name="hidOffName" id="hidOffName"/>
            <input type="hidden" name="hidSPC" id="hidSPC"/>
            <input type="hidden" name="forwardtoHrmsid" id="forwardtoHrmsid"/>
           <div id="tbl-container" class="easyui-panel" title="HBA ReApply"  style="width:100%;overflow: auto;">
                <div align="left" style="padding-left:10px">
                     <table style="width:100%">  
                        <tr>
                            <td style="width:10%">1(a).</td>
                            <td style="width:30%">Name:</td>
                            <td style="width:60%"> 
                               ${LoanHBAForm.empName}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">1(b).</td>
                            <td style="width:30%">Designation:</td>
                            <td style="width:60%"> 
                             ${LoanHBAForm.designation}
                            </td>
                        </tr>
                          <tr>
                            <td style="width:10%">1(c).</td>
                            <td style="width:30%">Scale of Pay:</td>
                            <td style="width:60%"> 
                             ${LoanHBAForm.cursalary}
                            </td>
                        </tr>
                         <tr>
                            <td style="width:10%">1(d).</td>
                            <td style="width:30%">Present pay:</td>
                            <td style="width:60%"> 
                             ${LoanHBAForm.curbasicsalary}
                            </td>
                        </tr>
                         <tr>
                            <td style="width:10%">1(e).</td>
                            <td style="width:30%">Net pay:</td>
                            <td style="width:60%"> 
                               ${LoanHBAForm.netPay}
                            </td>
                        </tr>
                          <tr>
                            <td style="width:10%">2(a).</td>
                            <td style="width:30%">Department or Office <br/>in which employed:</td>
                            <td style="width:60%"> 
                             ${LoanHBAForm.department}
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%">2(b).</td>
                            <td style="width:30%">Administrative Department:</td>
                            <td style="width:60%"> 
                             ${LoanHBAForm.department}
                            </td>
                        </tr>
                         <tr>
                            <td style="width:10%">2(c).</td>
                            <td style="width:30%">Station where posted</td>
                            <td style="width:60%"> 
                            ${LoanHBAForm.stationposted}
                            </td>
                        </tr>
                     </table> 
                   <a href="#part1" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">3.Please State Job Type Details:</a>
                    <table style="width:100%" id="part1" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">Job Type<br/>the length of service rendered<br/>under the Government:</td>
                            <td style="width:60%"> 
                                <textarea cols="50" rows="1" name="jobtype" id="jobtype" required>${LoanHBAForm.jobtype}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">2(a).</td>
                            <td style="width:30%">Permanent Post,<br/>if any, and the name of office<br/>and Department Concerned:</td>
                            <td style="width:60%"> 
                                <textarea cols="50" rows="1"  name="per_post" id="per_post">${LoanHBAForm.per_post}</textarea>
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">2(b).</td>
                            <td style="width:30%">Do you hold a permanent<br/> appointment under the Central Govt<br/>or any other state Govt<br/>if so, give particulars.:</td>
                            <td style="width:60%"> 
                                <textarea cols="50" rows="1"  name="per_appointment" id="per_appointment">${LoanHBAForm.per_appointment}</textarea>
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">3(a).</td>
                            <td style="width:30%">DOB:</td>
                            <td style="width:60%"> 
                               ${LoanHBAForm.dob}
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                         <tr>
                            <td style="width:10%" valign="top">3(b).</td>
                            <td style="width:30%">Next DOB:</td>
                            <td style="width:60%"> 
                                ${LoanHBAForm.ndob}
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                         <tr>
                            <td style="width:10%" valign="top">4.</td>
                            <td style="width:30%">Date on which you attend<br/>the age of 60 years:</td>
                            <td style="width:60%"> 
                               ${LoanHBAForm.superannuation}
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                         <tr>
                            <td style="width:10%" valign="top">5.</td>
                            <td style="width:30%">If your wife/husband a Govt servant<br/>If so, give her name, designation etc:</td>
                            <td style="width:60%"> 
                                <textarea cols="50" rows="1"  name="other_govt_ser" id="other_govt_ser">${LoanHBAForm.other_govt_ser}</textarea>
                            </td>
                        </tr>
                    </table>    
             
               <div style="height:20px"></div>
             <a href="#part2" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">4.Do you or does your wife/husband/minor child already own a house? :</a>
                    <table style="width:100%" id="part2" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">Station where it is situated<br/>with exact address:</td>
                            <td style="width:60%"> 
                                <textarea cols="50" rows="1" name="address" id="address" >${LoanHBAForm.address}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">2.</td>
                            <td style="width:30%">Floor area(in sq.ft.):</td>
                            <td style="width:60%"> 
                                 <input name="floor_area" class="form-control" id="floor_area" value="${LoanHBAForm.floor_area}">
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">3.</td>
                            <td style="width:30%">Its approx. valuation:</td>
                            <td style="width:60%"> 
                                 <input name="approx_valuation" class="form-control" id="approx_valuation" value="${LoanHBAForm.approx_valuation}">
                               
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">4.</td>
                            <td style="width:30%">Reasons for desiring to own<br/>another house or enlarging living <br/>accommodation in an existing house,<br/>as the case may be:</td>
                            <td style="width:60%"> 
                                 <textarea cols="50" rows="1"  name="reason" id="reason">${LoanHBAForm.reason}</textarea>
                            </td>
                        </tr>
                        
                    </table>  
             
                     <div style="height:20px"></div>
                     <a href="#part3" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">5.Do you require the advance for building in anew house? :</a>
                     <table style="width:100%" id="part3" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">Approx. floor plan<br/>of the house proposed to be constructed<br/>(in sq.ft.):</td>
                            <td style="width:60%"> 
                               <input name="constructed_area" class="form-control" id="constructed_area" value="${LoanHBAForm.constructed_area}">
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">2.</td>
                            <td style="width:30%">Cost Of land:</td>
                            <td style="width:60%"> 
                                 <input name="cost_land" class="form-control" id="cost_land"  value="${LoanHBAForm.cost_land}">
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">3.</td>
                            <td style="width:30%">Cost of building:</td>
                            <td style="width:60%"> 
                                 <input name="cost_building" class="form-control" id="cost_building"  value="${LoanHBAForm.cost_building}">
                               
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">4.</td>
                            <td style="width:30%">Total:</td>
                            <td style="width:60%"> 
                                <input name="total_amount" class="form-control" id="total_amount"  value="${LoanHBAForm.total_amount}">
                            </td>
                        </tr>
                          <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">5.</td>
                            <td style="width:30%">Amount of advance required</td>
                            <td style="width:60%"> 
                                <input name="amount_adv" class="form-control" id="amount_adv"  value="${LoanHBAForm.amount_adv}">
                            </td>
                        </tr>
                          <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">6.</td>
                            <td style="width:30%">No of years in which<br/>the advance with interest is <br/>proposed to be repaid</td>
                            <td style="width:60%"> 
                                <input name="noofYear" class="form-control" id="noofYear"  value="${LoanHBAForm.noofYear}">
                            </td>
                        </tr>
                        
                    </table> 
                     <div style="height:20px"></div>
                     <a href="#part4" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">5(b).Where you are already in possession of the land? :</a>
                     <table style="width:100%" id="part4" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">Name of the city<br/> or town where it is located:</td>
                            <td style="width:60%"> 
                               <input name="city_name" class="form-control" id="city_name" value="${LoanHBAForm.city_name}">
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">2.</td>
                            <td style="width:30%">Whether you wish<br/>to settle there<br/>after retirement:</td>
                            <td style="width:60%"> 
                                 <input name="settle_retir" class="form-control" id="settle_retir" value="${LoanHBAForm.settle_retir}">
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">3.</td>
                            <td style="width:30%">Area of plot<br/>(in sq. yds.):</td>
                            <td style="width:60%"> 
                                 <input name="area_plot" class="form-control" id="area_plot" value="${LoanHBAForm.area_plot}">
                               
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">4.</td>
                            <td style="width:30%">Name of Municipal or other local<br/>authority(if any)in whose<br/>jurisdiction it is located</td>
                            <td style="width:60%"> 
                                <input name="Localauthority" class="form-control" id="Localauthority" value="${LoanHBAForm.localauthority}">
                            </td>
                        </tr>
                          <tr>
                            <td style="width:10%" valign="top">5(c).</td>
                           
                            <td colspan="2"> 
                               If no plot of land is already in your Possession, how and when do you purpose to acquire one? State the approximate plot area(in sq. yds.) proposed to be required.
                            </td>
                        </tr>
                        <tr>                           
                            <td style="width:10%"></td>
                            <td  colspan="2">
                                 <textarea cols="80" rows="1"  name="propose_acquire" id="propose_acquire">${LoanHBAForm.propose_acquire}</textarea>
                                
                            </td>
                        </tr>
                          
                        
                    </table>  
                     
                     <div style="height:20px"></div>
                     <a href="#part5" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">6.Do you require the advance for enlarging living accommodation in an existing house? :</a>
                     <table style="width:100%" id="part5" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">Number of rooms in the house<br/>(excluding lavatory, bath room and kitchen):</td>
                            <td style="width:60%"> 
                               <input name="no_rooms" class="form-control" id="no_rooms" value="${LoanHBAForm.no_rooms}">
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">2.</td>
                            <td style="width:30%">Total floor area of the room<br/>(in sq ft):</td>
                            <td style="width:60%"> 
                                 <input name="total_floor" class="form-control" id="total_floor" value="${LoanHBAForm.total_floor}">
                            </td>
                        </tr>
                         <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">3.</td>
                            <td style="width:30%">If an additional storey is<br/>is proposed to be added,<br/>is the foundation strong enough:</td>
                            <td style="width:60%"> 
                                 <input name="additional_storey" class="form-control" id="additional_storey" value="${LoanHBAForm.additional_storey}">
                               
                            </td>
                        </tr>
                         <tr>
                             <th colspan="3"><h5 class="bg-primary"  style="height:20px">Particulars of addition desired</h5></th>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">4.</td>
                            <td style="width:30%">No of Rooms:</td>
                            <td style="width:60%"> 
                                <input name="addition_room" class="form-control" id="addition_room" value="${LoanHBAForm.addition_room}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">5.</td>
                            <td style="width:30%">Floor area in sq. ft:</td>
                            <td style="width:60%"> 
                                <input name="addition_farea" class="form-control" id="addition_farea" value="${LoanHBAForm.addition_farea}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">6.</td>
                            <td style="width:30%">Estimate Cost:</td>
                            <td style="width:60%"> 
                                <input name="est_cost" class="form-control" id="est_cost" value="${LoanHBAForm.est_cost}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">7.</td>
                            <td style="width:30%">Amount of advance desired:</td>
                            <td style="width:60%"> 
                                <input name="amount_desired" class="form-control" id="amount_desired" value="${LoanHBAForm.amount_desired}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">8.</td>
                            <td style="width:30%">Number of Year in which<br/>the advance with interest<br/>proposed to be repaid:</td>
                            <td style="width:60%"> 
                                <input name="year_repaid" class="form-control" id="year_repaid" value="${LoanHBAForm.year_repaid}">
                            </td>
                        </tr>
                          
                      </table>  
                      <div style="height:20px"></div>
                     <a href="#part6" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">7.Do you require the advance for repaying loan(s) taken earlirer for purchasing/constructing house?</a>
                     <table style="width:100%" id="part6" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">Exact Location of the house:</td>
                            <td style="width:60%"> 
                               <input name="exact_location" class="form-control" id="exact_location" value="${LoanHBAForm.exact_location}">
                            </td>
                        </tr>
                       
                        <tr>
                            <td style="width:10%" valign="top">2.</td>
                            <td style="width:30%">Floor area of the house:</td>
                            <td style="width:60%"> 
                                 <input name="exact_floor_area" class="form-control" id="exact_floor_area" value="${LoanHBAForm.exact_floor_area}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">3.</td>
                            <td style="width:30%">Plinth area of the house:</td>
                            <td style="width:60%"> 
                                 <input name="plinth_area" class="form-control" id="plinth_area" value="${LoanHBAForm.plinth_area}">
                               
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">4.</td>
                            <td style="width:30%">Total Cost of the house including land:</td>
                            <td style="width:60%"> 
                                <input name="total_land_cost" class="form-control" id="total_land_cost" value="${LoanHBAForm.total_land_cost}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">5.</td>
                            <td style="width:30%">Name and address of parties<br/>from whom loans were taken<br/>and the amount outstanding in their favour<br/>on the date of application:</td>
                            <td style="width:60%" valign="top"> 
                                   <textarea cols="50" rows="2"  name="parties_name_address" id="parties_name_address">${LoanHBAForm.parties_name_address}</textarea>
                              
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">6.</td>
                            <td style="width:30%">Amount of advance required:</td>
                            <td style="width:60%"> 
                                <input name="repay_adv_amount" class="form-control" id="repay_adv_amount" value="${LoanHBAForm.repay_adv_amount}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">7.</td>
                            <td style="width:30%">Number of years in which<br/>the advance with interest is proposed<br/> to be repaid:</td>
                            <td style="width:60%"> 
                                <input name="repay_year" class="form-control" id="amount_desired" value="${LoanHBAForm.amount_desired}">
                            </td>
                        </tr>
                        
                       
                          
                      </table>  
                     
                     
                     
                      <div style="height:20px"></div>
                     <a href="#part7" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">8(a).Do you require the advance for purchasing a ready made house or flat?</a>
                     <table style="width:100%" id="part7" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">Exact Location of the house:</td>
                            <td style="width:60%"> 
                               <input name="readymade_exact_loc" class="form-control" id="readymade_exact_loc" value="${LoanHBAForm.readymade_exact_loc}">
                            </td>
                        </tr>
                       
                        <tr>
                            <td style="width:10%" valign="top">2.</td>
                            <td style="width:30%">Floor area of the house:</td>
                            <td style="width:60%"> 
                                 <input name="readymade_floor_area" class="form-control" id="readymade_floor_area" value="${LoanHBAForm.readymade_floor_area}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">3.</td>
                            <td style="width:30%">Plinth area of the house:</td>
                            <td style="width:60%"> 
                                 <input name="readymade_plinth_area" class="form-control" id="readymade_plinth_area" value="${LoanHBAForm.readymade_plinth_area}">
                               
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">4.</td>
                            <td style="width:30%">Approximate age of the House</td>
                            <td style="width:60%"> 
                                <input name="house_age" class="form-control" id="house_age" value="${LoanHBAForm.house_age}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">5.</td>
                            <td style="width:30%">Municipal valuation of the house</td>
                            <td style="width:60%"> 
                                <input name="valuation_price" class="form-control" id="valuation_price" value="${LoanHBAForm.valuation_price}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">6.</td>
                            <td style="width:30%">Name and address of owner<br/>and the amount outstanding in their favour<br/>on the date of application:</td>
                            <td style="width:60%" valign="top"> 
                                   <textarea cols="50" rows="2"  name="owner_name" id="owner_name">${LoanHBAForm.owner_name}</textarea>
                              
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">7.</td>
                            <td style="width:30%">Approximate price expected to be paid:</td>
                            <td style="width:60%"> 
                                <input name="readymade_appro_amount" class="form-control" id="readymade_appro_amount"  value="${LoanHBAForm.readymade_appro_amount}">
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%" valign="top">8.</td>
                            <td style="width:30%">Amount of advance required:</td>
                            <td style="width:60%"> 
                                <input name="readymade_adv_amount" class="form-control" id="readymade_adv_amount"  value="${LoanHBAForm.readymade_adv_amount}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">9.</td>
                            <td style="width:30%">Number of years in which<br/>the advance with interest is proposed<br/> to be repaid:</td>
                            <td style="width:60%"> 
                                <input name="readymade_year" class="form-control" id="readymade_desired"  value="${LoanHBAForm.readymade_year}">
                            </td>
                        </tr>
                       </table>  
                        <div style="height:20px"></div>
                         <a href="#part8" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">8(b).If you dont already have a house in view, how, when and where do you propose to acquire one?</a>
                     <table style="width:100%" id="part8" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">The approx, amount up to which you will be prepared to buy a house:</td>
                            <td style="width:60%"> 
                               <input name="propose_amount" class="form-control" id="propose_amount"  value="${LoanHBAForm.propose_amount}">
                            </td>
                        </tr>
                       
                        <tr>
                            <td style="width:10%" valign="top">2.</td>
                            <td style="width:30%">The approx, amount of advance required:</td>
                            <td style="width:60%"> 
                                 <input name="propose_adv" class="form-control" id="propose_adv"  value="${LoanHBAForm.propose_adv}">
                            </td>
                        </tr>
                        
                        
                        
                        <tr>
                            <td style="width:10%" valign="top">3.</td>
                            <td style="width:30%">Number of Year in which<br/>the advance with interest<br/>proposed to be repaid:</td>
                            <td style="width:60%"> 
                                <input name="propose_repaid" class="form-control" id="propose_repaid"  value="${LoanHBAForm.propose_repaid}">
                            </td>
                        </tr>
                          
                      </table>
                            <div style="height:20px"></div>
                     <a href="#part9" class="btn btn-info" data-toggle="collapse" style="width:100%;text-align:left">9.Is the land on which the house stands or is proposed to be constructed, free holds or lease hold , state?</a>
                     <table style="width:100%" id="part9" class="collapse">  
                        <tr>
                            <td style="width:10%" valign="top">1.</td>
                            <td style="width:30%">The term of the lease:</td>
                            <td style="width:60%"> 
                               <input name="term_lease" class="form-control" id="term_lease"  value="${LoanHBAForm.term_lease}">
                            </td>
                        </tr>
                       
                        <tr>
                            <td style="width:10%" valign="top">2.</td>
                            <td style="width:30%">How much of the terms has already expired:</td>
                            <td style="width:60%"> 
                                 <input name="term_expired" class="form-control" id="term_expired"  value="${LoanHBAForm.term_expired}">
                            </td>
                        </tr>
                        
                        <tr>
                            <td style="width:10%" valign="top">3.</td>
                            <td style="width:30%">Whether conditions of the lease permit the land being mortgaged to Goverment:</td>
                            <td style="width:60%"> 
                                <input name="lease_condition" class="form-control" id="lease_condition"  value="${LoanHBAForm.lease_condition}">
                            </td>
                        </tr>
						 <tr>
                            <td style="width:10%" valign="top">4.</td>
                            <td style="width:30%">Premimum paid for the plot:</td>
                            <td style="width:60%"> 
                                <input name="premimum_paid" class="form-control" id="premimum_paid"  value="${LoanHBAForm.premimum_paid}">
                            </td>
                        </tr>
						 <tr>
                            <td style="width:10%" valign="top">5.</td>
                            <td style="width:30%">Annual rent of the plot:</td>
                            <td style="width:60%"> 
                                <input name="annual_rent" class="form-control" id="annual_rent"  value="${LoanHBAForm.annual_rent}">
                            </td>
                        </tr>
                          <tr>
                            <td>&nbsp;</td>
                        </tr> 
                     </table>
                     
                     <table style="width:100%">  
                          <tr>
                            <td>&nbsp;</td>
                        </tr>
                          <tr>
                            <td style="width:10%" valign="top">10(a).</td>
                            <td style="width:30%">Is your title to land/house undisputed and free from encumbrances?:</td>
                            <td style="width:60%" valign="top"> 
                               <input name="encumb_status" class="form-control" id="encumb_status"  value="${LoanHBAForm.encumb_status}">
                            </td>
                        </tr>
                         <tr>
                            <td style="width:10%" valign="top">10(b).</td>
                            <td style="width:30%">Can you produce, if required original documents(sale or lease-deed )in support of your title. If not state reasons thereof indicating what other documentary proof if any can you furnish in support of your claim?:</td>
                            <td style="width:60%" valign="top"> 
                               <input type='file' name="file_att"  id="file_att">
                               <c:if test="${ not empty LoanHBAForm.diskFileName}">
                                    <c:set var="lid" value="${LoanHBAForm.loanId}"/>
                                    <a href="DownloadhbaLoanAttch.htm?lid=${LoanHBAForm.loanId}&attchment=" target="_blank" >Download</a>
                                    <a href="javascript:void(0)" style="text-decoration:none;" onclick="delAttach(${LoanHBAForm.loanId},1)">
                                       <img src="images/Delete-icon.png"/>
                                    </a>
                               </c:if>
                                    
                            </td>
                        </tr>
                         <tr>
                            <td style="width:10%" valign="top">10(c).</td>
                            <td style="width:30%">Does the locality in which the plot of land/house is situated, process(essential services like roads, water-supply, drainage, sewerage, street lighting etc.(Please furnish a site plan with complete address):</td>
                            <td style="width:60%" valign="top"> 
                               <input type='file' name="file_att1"  id="file_att1">
                               <c:if test="${ not empty LoanHBAForm.diskFileName1}">
                                    <c:set var="lid" value="${LoanHBAForm.loanId}"/>
                                    <a href="DownloadhbaLoanAttch.htm?lid=${LoanHBAForm.loanId}&attchment=2" target="_blank" >Download</a>
                                    <a href="javascript:void(0)" style="text-decoration:none;" onclick="delAttach(${LoanHBAForm.loanId},2)">
                                     <img src="images/Delete-icon.png"/>
                                     </a>  
                               </c:if>
                                  
                            </td>
                        </tr>
                         <tr>
                            <td style="width:10%">11. </td>
                            <td style="width:30%">Comments</td>
                            <td valign="top">
                                  <textarea rows="4" cols="40" class="easyui-textarea" name="loancomments"  id="loancomments" styleId="txtcomments" style="width:40%;height:60px;;" styleClass="textareacolor" >${LoanHBAForm.loancomments}</textarea>
                            </td>   
                        </tr>
                        <tr>
                            <td style="width:10%">12. </td>
                            <td style="width:30%">Forward To</td>
                            <td valign="top">
                                <input id="forwardto" type="text" name="forwardto" style="width:300px;height:25px" readonly="true"   ></input>
                                <a href="javascript:void(0)" id="change" onclick="changepost()">
                                    <button type="button">Search</button>
                                </a>
                            </td>   
                        </tr>
                     </table>    
                       <div style="text-align:center;padding:5px">
                        <input class="btn-primary" type="submit" name="Save" value="Submit" style="width:100px;height:30px" />
                        <input class="btn-default" type="button" name="Back" value="Back" style="width:100px;height:30px" onclick="self.location = 'loanList.htm'"/>

                    </div>       
                     
                     
                     
                     
                
                
               </div>
               
           </div>
        </form>
`      
   </body>
</html>