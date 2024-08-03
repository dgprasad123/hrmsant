<%-- 
    Document   : EditAD
    Created on : Nov 15, 2016, 6:48:41 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table cellspacing="0" cellpadding="0" width="100%" border="0" class="tableview">
          <thead></thead>
              <tbody><tr>
                <td width="2%">&nbsp;</td>
                <td width="18%" align="left">Code</td>
                <td width="28%">
                 
                 
                  <input type="text" id="txtCode" class="textresizelevel" style="width:30%;text-align: left;" disabled="disabled" value="ADLHRA" maxlength="10" name="_piref34_1156784_34_1156783_1156783.txtCode">
                 
                    <input type="hidden" value="91" name="_piref34_1156784_34_1156783_1156783.slno">
                     <input type="hidden" value="ADLHRA" name="_piref34_1156784_34_1156783_1156783.txtCode">
                 </td>
                
              </tr>
          
                <tr>
                    <td>&nbsp;</td>
                    <td align="left">Head</td>
                    <td>
                     
                    
                    <input type="text" id="txtHead" class="textresizelevel" style="width: 60%;text-align: left;" disabled="disabled" value="ADDITIONAL HRA" maxlength="30" name="_piref34_1156784_34_1156783_1156783.txtHead">
                    <input type="hidden" value="ADDITIONAL HRA" name="_piref34_1156784_34_1156783_1156783.txtHead">
                    
                    </td>
                    
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td align="left"> Fixed or Formula 
                    </td>
                    <td align="left">
                       <select id="sltAdamttype" class="style12" style="width:60%" onchange="seventq()" name="_piref34_1156784_34_1156783_1156783.sltAdamttype"><option value="">--Select One--</option>
                                <option value="1">FIXED</option>
                                <option selected="selected" value="0">FORMULA</option></select>
                    </td>
                  </tr>          
                 
                  <tr>
                    <td>&nbsp;</td>
                    <td align="left"> Amount 
                    </td><td align="left" colspan="2">
                    
                        <input type="text" id="txtADamt" class="textresizelevel" style="width: 60%;text-align: left;" disabled="disabled" onblur="return isAmountAndMaxlengthCheck(this,6)" onkeypress="return numbersonly(this,event)" value="" maxlength="7" name="_piref34_1156784_34_1156783_1156783.txtADamt">
                    
                    
                    
                    </td>
                  </tr>     
                   <tr>
                    <td>&nbsp;</td>
                    <td align="left"> Formula 
                    </td>
                    <td align="left">
                     
                        
                        <select id="sltAdformula" class="style12" style="width:90%" name="_piref34_1156784_34_1156783_1156783.sltAdformula"><option value=""></option></select>
                        
                        
                    </td>
                  </tr>    
                  <tr>
                    <td>&nbsp;</td>
                    <td align="left">
                        
                        
                                Object Head
                        
                    </td>
                    <td align="left">
                        
                         
                              <input type="text" id="adheadCode" class="textresizelevel" style="width:30%;text-align: left;" disabled="disabled" value="" maxlength="5" name="_piref34_1156784_34_1156783_1156783.adheadCode">
                              <input type="button" id="btnChange" class="sessionstyle" onclick="changeBtnStatus();" value="Change" name="_piref34_1156784_34_1156783_1156783.btnChange">
                         
                    </td>
                  </tr>
                           
        </tbody></table>
    </body>
</html>
