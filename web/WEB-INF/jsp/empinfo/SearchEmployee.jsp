
<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            var rowSize = 20;
            var totalPages = 0;
            $(document).ready(function () {
                $("#deptName").change(function () {
                    $('#offcode').empty();
                    $("#agtreasurycode").val('');
                    
                    $('#offcode').append($('<option>', {
                                value: '',
                                text: '-- Select One --'
                            }));
                            
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                });
                $("#agtreasurycode").change(function () {
                    $('#offcode').empty();
                    
                    var url = 'getOfficeListTreasuryWiseJSON.htm?trCode=' + $("#agtreasurycode").val() + '&deptcode=' + $("#deptName").val();
                    
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                });
               
            });
            function searchEmployee() {
                var Criteria = $("#Criteria").val();
                var deptName = $("#deptName").val();
                var offcode = $("#offcode").val();
                if (deptName == "" && Criteria == "") {
                    alert("Select Office or Search Criteria");
                    return false;
                }
                var searchString = $("#searchString").val();
                

                var agtreasurycode = $("#agtreasurycode").val();
                var year = $("#year").val();
                var rowSize = 10;
                if ($("#Criteria").val() != "") {
                    if ($("#searchString").val() == "") {
                        alert("please Enter all the Search value");
                        $("#searchString").focus();
                        return false;
                    }
                }

                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                $("#employeedatagrid").empty();
                $.post("SearchEmployeeInfoJson.htm", {deptName: deptName, offcode: offcode, agtreasurycode: agtreasurycode, year: year, Criteria: Criteria, searchString: searchString, page: 1, rows: rowSize})
                        .done(function (data) {
                            var totalEmpFound = data.totalEmpFound;
                            $("#totalEmpFound").text(totalEmpFound);
                            totalPages = Math.round(totalEmpFound / rowSize);
                            var remainingData = totalEmpFound - (totalPages * rowSize);
                            if (remainingData > 0) {
                                totalPages++;
                            }
                            $("#pagingstatus").text("1 of " + totalPages);
                            $("#paging").val(1);
                            var employeelist = data.employeeList;
                            populateDataInGrid(employeelist);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
            }

            function gotoPage() {
                var pageNo = $("#paging").val();
                if (pageNo == "") {
                    alert("Insert Page No");
                    $("#paging").focus();
                    return false;
                }
                if (pageNo > totalPages) {
                    alert("Invallid Page no");
                    $("#paging").focus();
                    return false;
                }
                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                var Criteria = $("#Criteria").val();
                var searchString = $("#searchString").val();
                var deptName = $("#deptName").val();
                var offcode = $("#offcode").val();
                var agtreasurycode = $("#agtreasurycode").val();
                var year = $("#year").val();
                var rowSize = 10;
                $("#employeedatagrid").empty();
                $.post("SearchEmployeeInfoJson.htm", {deptName: deptName, offcode: offcode, agtreasurycode: agtreasurycode, year: year, Criteria: Criteria, searchString: searchString, page: pageNo, rows: rowSize})
                        .done(function (data) {
                            $("#pagingstatus").text(pageNo + " of " + totalPages);
                            var employeelist = data.employeeList;
                            populateDataInGrid(employeelist);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
            }

            function populateDataInGrid(employeelist) {                
                for (var i = 0; i < employeelist.length; i++) {
                    var row = '';
                    if (employeelist[i].hasPriv == 'Y') {
                        row = '<tr style="color:white; background:#83332b;">';
                        row = row + '<td>' + employeelist[i].empid + '<br>' + employeelist[i].gpfno + '</td>';
                        if (employeelist[i].ddohrmsid != '') {
                            row = row + '<td style="color: white ; background: darkcyan">' + employeelist[i].empName + '<br>' + handleUndefind(employeelist[i].post) + '</td>';
                        } else if (employeelist[i].ddohrmsid == '') {
                            row = row + '<td>' + employeelist[i].empName + '<br>' + handleUndefind(employeelist[i].post) + '</td>';
                        }

                        row = row + '<td><a href="javascript:void(0);" data-href="EmployeeBasicProfile.htm?empid=' + employeelist[i].empid + ' " class="openPopup">View</a></td>';
                        row = row + '<td><a href="newMessage.htm?empid=' + employeelist[i].empid + '">Message</a></td>';  
                        row = row + '</tr>';
                    } else if (employeelist[i].hasPriv == 'N') {
                       
                        row = '<tr>';
                        row = row + '<td>' + employeelist[i].empid + '<br>' + employeelist[i].gpfno + '</td>';
                        if (employeelist[i].ddohrmsid != '') {
                            row = row + '<td style="color: white ; background: darkcyan;">' + employeelist[i].empName + '<br>' + handleUndefind(employeelist[i].post) + '</td>';
                        } else if (employeelist[i].ddohrmsid == '') {
                            row = row + '<td>' + employeelist[i].empName + '<br>' + handleUndefind(employeelist[i].post) + '</td>';
                        }
                        row = row + '<td><a href="javascript:void(0);" data-href="EmployeeBasicProfile.htm?empid=' + employeelist[i].empid + ' " class="openPopup">View</a></td>';
                          row = row + '<td><a href="newMessage.htm?empid=' + employeelist[i].empid + '">Message</a></td>'; 
                        row = row + '</tr>';
                    }else{
                        row = '<tr>';
                        row = row + '<td>' + employeelist[i].empid + '<br>' + employeelist[i].gpfno + '</td>';
                        if (employeelist[i].ddohrmsid != '') {
                            row = row + '<td>' + employeelist[i].empName + '<br>' + handleUndefind(employeelist[i].post) + '</td>';
                        } else if (employeelist[i].ddohrmsid == '') {
                            row = row + '<td>' + employeelist[i].empName + '<br>' + handleUndefind(employeelist[i].post) + '</td>';
                        }
                        row = row + '<td><a href="javascript:void(0);" data-href="EmployeeBasicProfile.htm?empid=' + employeelist[i].empid + ' " class="openPopup">View</a></td>';
                          row = row + '<td><a href="newMessage.htm?empid=' + employeelist[i].empid + '">Message</a></td>'; 
                        row = row + '</tr>';
                    }


                    $("#employeedatagrid").append(row);
                }

                $('.openPopup').on('click', function () {
                    var dataURL = $(this).attr('data-href');
                    $('.modal-body').load(dataURL, function () {
                        $('#myModal').modal({show: true});
                    });
                });
            }
            function handleUndefind(datavar) {
                if (datavar) {
                    return datavar;
                } else {
                    return '';
                }
            }
        </script>
    </head>

    <c:choose>
        <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/AlfaMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                    </c:when>
                    <c:otherwise>                        
                        <div id="wrapper">
                            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
                            <div id="page-wrapper">
                            </c:otherwise>
                        </c:choose>
                        <form:form action="SearchEmployeeInfo.htm" commandName="searchEmployee" method="post">
                            <div class="container-fluid" style="margin-top: 10px;">
                                <div class="row">
                                    <label class="control-label col-sm-2 ">Department Name</label>
                                    <div class="col-sm-4">
                                        <form:select class="form-control" path="deptName" id="deptName">
                                            <form:option value="">-- Select One --</form:option>
                                            <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                        
                                        </form:select>
                                    </div>

                                    <label class="control-label col-sm-1">Treasury</label>
                                    <div class="col-sm-5">
                                        <form:select class="form-control" path="agtreasurycode" id="agtreasurycode">
                                            <form:option value="">-- Select One --</form:option>
                                            <form:options items="${treasuryList}" itemLabel="treasuryName" itemValue="agtreasuryCode"/>  
                                        </form:select>
                                    </div>
                                </div><br>
                                <div class="row">
                                    <label class="control-label col-sm-2">Office Name</label>
                                    <div class="col-sm-4">
                                        <form:select class="form-control" path="offcode">
                                            <form:option value="">-- Select One --</form:option>
                                            <form:options items="${officeList}" itemLabel="offName" itemValue="offCode"/>  
                                        </form:select>
                                    </div>
                                    <label class="control-label col-sm-1">Year</label>
                                    <div class="col-sm-5">
                                        <form:select class="form-control" path="year">
                                            <form:option value="0">Select</form:option>
                                            <form:option value="2012">2012</form:option>
                                            <form:option value="2013">2013</form:option>
                                            <form:option value="2014">2014</form:option>
                                            <form:option value="2015">2015</form:option>
                                            <form:option value="2016">2016</form:option>
                                            <form:option value="2017">2017</form:option>
                                            <form:option value="2018">2018</form:option>
                                            <form:option value="2019">2019</form:option>
                                            <form:option value="2020">2020</form:option>
                                            <form:option value="2020">2021</form:option>
                                            <form:option value="2020">2022</form:option>
                                        </form:select>
                                    </div>
                                </div><br>
                                <div class="row">
                                    <label class="control-label col-sm-2">Search criteria</label>
                                    <div class="col-sm-4">
                                        <form:select class="form-control" path="Criteria" id="Criteria">
                                            <form:option value="">select</form:option>
                                            <form:option value="GPFNO">GPF NO / PRAN</form:option>
                                            <form:option value="HRMSID">HRMS ID</form:option>
                                            <form:option value="FNAME">FIRST NAME</form:option>
                                        
                                        </form:select>
                                    </div>
                                    <label class="control-label col-sm-2" > Search String</label>
                                    <div class="col-sm-4" >
                                        <form:input path="searchString" name="searchString" id="searchString" class="form-control"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-top:10px;">
                                    <div class="col-sm-2">
                                        <input type="button" value="Search" id="searchbtn" onclick="return searchEmployee()"/>
                                    </div>
                                    <div class="control-label col-sm-4" style="font-weight: bolder;text-align: right;">Total Number of Record:</div>
                                    <div class="col-sm-1" id="totalEmpFound">  
                                    </div>
                                    <div class="control-label col-sm-1" style="font-weight: bolder;text-align: right;">Page:</div>
                                    <div class="col-lg-1"> <div class="loader"></div> </div>
                                    <div class="col-lg-1"><span id="pagingstatus"></span></div>
                                    <div class="col-sm-1" >
                                        <input type="text" name="paging" id="paging" class="form-control">  
                                    </div>
                                    <div class="col-sm-1" >
                                        <button type="button" class="form-control btn-primary" id="searchbtn" onclick="return gotoPage()">Go</button>
                                    </div>
                                </div>
                            </div> 

                            <div class="panel-footer">
                                <div class="table-responsive">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped">
                                            <thead>
                                                <tr>                                                    
                                                    <th>Hrms Id / Account No</th>
                                                    <th>Employee Name / Designation</th>
                                                    <th>Employee's Basic Profile</th> 
                                                    <th>Communication</th> 
                                                </tr>
                                            </thead>
                                            <tbody id="employeedatagrid">

                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                            </form:form>
                        </div>
                    </div>
                    <div class="modal fade" id="myModal" role="dialog" >
                        <div class="modal-dialog  modal-lg">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Employee Basic Profile</h4>
                                </div>
                                <div class="modal-body">

                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    </body> 
                    </html>


