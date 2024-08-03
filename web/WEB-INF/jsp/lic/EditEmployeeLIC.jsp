<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        

        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>



    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form class="form-inline" action="updateEmployeeLicData.htm" method="POST" >

                        <input type='hidden' name='empid' value='${EmpId}'/>
                        <input type='hidden' name='elId' value='${licdata.elId}'/>

                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Manage LIC Details
                            </tr>
                            <tr>
                                <td align="right">Policy Number :</td>
                                <td> <input name="policyNo" id="policyNo" class="form-control" required="true" value='${licdata.policyNo}'  type='number'></td>
                                <td align="right">Insurance Type :</td>
                                <td>
                                    <select name="insuranceType" id="insuranceType"  size="1" class="form-control"  style="width:80%;"  required>
                                        <option value="LIC" <c:if test = "${not empty licdata.insuranceType && licdata.insuranceType=='LIC'}"> <c:out value='selected="selected"'/></c:if>>LIC</option>
                                        <option value="TLIC" <c:if test = "${not empty licdata.insuranceType && licdata.insuranceType=='TLIC'}"> <c:out value='selected="selected"'/></c:if>>TLIC</option>
                                        <option value="PLI" <c:if test = "${not empty licdata.insuranceType && licdata.insuranceType=='PLI'}"> <c:out value='selected="selected"'/></c:if>>PLI</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">Sub Amount :</td>
                                    <td> <input name="subAmount" id="subAmount" value='${licdata.subAmount}' class="form-control" required="true"  type='number' required></td>
                                <td align="right">WEF :</td>

                                <td> <input name="wef" readonly id="wef" class="form-control" type='text' required value='${licdata.wef}'></td>

                            </tr>
                            <tr>
                                <td align="right">Month :</td>
                                <td>
                                    <Select name="month"  class="form-control" required>

                                        <option value="0"  <c:if test = "${not empty licdata.month && licdata.month=='0'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                        <option value="1"  <c:if test = "${not empty licdata.month && licdata.month=='1'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                        <option value="2" <c:if test = "${not empty licdata.month && licdata.month=='2'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                        <option value="3" <c:if test = "${not empty licdata.month && licdata.month=='3'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                        <option value="4" <c:if test = "${not empty licdata.month && licdata.month=='4'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                        <option value="5" <c:if test = "${not empty licdata.month && licdata.month=='5'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                        <option value="6" <c:if test = "${not empty licdata.month && licdata.month=='6'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                        <option value="7"  <c:if test = "${not empty licdata.month && licdata.month=='7'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                        <option value="8" <c:if test = "${not empty licdata.month && licdata.month=='8'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                        <option value="9" <c:if test = "${not empty licdata.month && licdata.month=='9'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                        <option value="10" <c:if test = "${not empty licdata.month && licdata.month=='10'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                        <option value="11" <c:if test = "${not empty licdata.month && licdata.month=='11'}"> <c:out value='selected="selected"'/></c:if>>December</option>
                                        </select>

                                    </td>
                                    <td align="right">Year :</td>
                                    <td>
                                        <Select name="year"  class="form-control" required>
                                         <option value="2010" <c:if test = "${not empty licdata.year && licdata.year=='2010'}"> <c:out value='selected="selected"'/></c:if>>2010 </option>
                                        <option value="2011"  <c:if test = "${not empty licdata.year && licdata.year=='2011'}"> <c:out value='selected="selected"'/></c:if>>2011 </option>
                                        <option value="2012" <c:if test = "${not empty licdata.year && licdata.year=='2012'}"> <c:out value='selected="selected"'/></c:if>>2012 </option>
                                        <option value="2013"  <c:if test = "${not empty licdata.year && licdata.year=='2013'}"> <c:out value='selected="selected"'/></c:if>>2013</option>
                                        <option value="2014"  <c:if test = "${not empty licdata.year && licdata.year=='2014'}"> <c:out value='selected="selected"'/></c:if>>2014</option>
                                        <option value="2015"  <c:if test = "${not empty licdata.year && licdata.year=='2015'}"> <c:out value='selected="selected"'/></c:if>>2015</option>
                                        <option value="2016"  <c:if test = "${not empty licdata.year && licdata.year=='2016'}"> <c:out value='selected="selected"'/></c:if>>2016</option>
                                        <option value="2017"  <c:if test = "${not empty licdata.year && licdata.year=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017</option>
                                        <option value="2018" <c:if test = "${not empty licdata.year && licdata.year=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                        <option value="2019"  <c:if test = "${not empty licdata.year && licdata.year=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                        <option value="2020" <c:if test = "${not empty licdata.year && licdata.year=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                        <option value="2021"  <c:if test = "${not empty licdata.year && licdata.year=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021</option>
                                        <option value="2022"  <c:if test = "${not empty licdata.year && licdata.year=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022</option>                                        
                                        <option value="2023"  <c:if test = "${not empty licdata.year && licdata.year=='2023'}"> <c:out value='selected="selected"'/></c:if>>2023</option>
                                        <option value="2024"  <c:if test = "${not empty licdata.year && licdata.year=='2024'}"> <c:out value='selected="selected"'/></c:if>>2024</option>
                                        </select>  
                                    <td>

                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">Notes :</td>
                                    <td colspan=3>
                                        <input name="note" id="note" class="form-control"   type='text' style='width:300px' value='${licdata.note}' >
                                </td>

                            </tr>


                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" value="Save LIC Details" class="btn btn-success" />
                        </div>
                    </form>
                </div>

            </div>
        </div>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="/resources/demos/style.css">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script>
            $(function () {
                 $('#wef').datepicker({dateFormat: 'd-MM-yy'});
            });
        </script>
    </body>
</html>
