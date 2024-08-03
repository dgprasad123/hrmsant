<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <script type="text/javascript" src="js/date_time.js"></script>
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>

    </div>
    <c:if test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">    
        <div style="width:100%;height:119px;background:#FFFFFF;text-align:center;">
            <img src="images/banner3.png" alt="" />
        </div>
        <!-- Top Menu Items -->
        <div style="width:100%;height:30px;background:#EAEAEA;">
            <div style="float:left;width:30%;">
                <span id="date_time" style="line-height:30px;margin-left:10px;color:#900000;font-style:italic;font-weight:bold;"></span>
            </div>
            <div style="float:right;width:60%;text-align:right;line-height:30px;padding-right:20px;">
                <a href="#" class="dropdown-toggle " data-toggle="dropdown"><i class="fa fa-user"></i> Welcome ${LoginUserBean.loginname}</a>
                <a href="logout.htm"><i class="fa fa-fw fa-power-off"></i> Log Out</a>

            </div>
            <div style="clear:both;"></div>
        </div>   
    </c:if>
    <c:choose>
        <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}"></c:when>
        <c:otherwise>
            <!-- Top Menu Items -->
            <ul class="nav navbar-right top-nav new_sty">

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle btn-hover color-9" data-toggle="dropdown"><i class="fa fa-user"></i> ${LoginUserBean.loginname} <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="#"><i class="fa fa-fw fa-user"></i> Profile</a>
                        </li>                
                        <li>
                            <a href="#"><i class="fa fa-fw fa-gear"></i> Settings</a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="logout.htm"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </c:otherwise>
    </c:choose>

    <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">

        <c:if test="${LoginUserBean.loginusertype ne 'F'}">
            <ul class="nav navbar-nav side-nav"<c:if test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">  style="top:150px;background-color:#FFFFFF;"</c:if>>
                <li class="active"<c:if test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">  style="background:#008900;color:#FFFFFF;"</c:if>>
                        <a href="#"><i class="fa fa-fw fa-dashboard"></i> Dashboard</a>
                    </li>            

                <c:forEach items="${Privileges}" var="Privilege">
                    <li  style="border-bottom: 1px solid #016e6e;border-top: 1px solid #008989;">
                        <a href="${Privilege.modurl}"><i class="fa fa-fw fa-table"></i> ${Privilege.modname}</a>
                    </li>
                </c:forEach>
                
                <li>
                    <a href="empCadreMIS.htm"><i class="fa fa-fw fa-dashboard"></i> Employee Cadre Report</a>
                </li> 
                <li>
                    <a href="showpasswordchange.htm"><i class="fa fa-fw fa-table"></i> Change Password</a>
                </li>
            </ul>
        </c:if>
        
        <c:if test="${LoginUserBean.loginempid eq '97000074'}">
            <ul class="nav navbar-nav side-nav">
                <li class="active">
                    <a href="#"><i class="fa fa-fw fa-dashboard"></i> Dashboard</a>
                </li>            

                <c:forEach items="${Privileges}" var="Privilege">
                    <li  style="border-bottom: 1px solid #016e6e;border-top: 1px solid #008989;">
                        <a href="${Privilege.modurl}"><i class="fa fa-fw fa-table"></i> ${Privilege.modname}</a>
                    </li>
                </c:forEach>

                <li>
                    <a href="showpasswordchange.htm"><i class="fa fa-fw fa-table"></i> Change Password</a>
                </li>
            </ul>
        </c:if>
        
        <c:if test="${LoginUserBean.loginusertype eq 'F'}">
            <ul class="nav navbar-nav side-nav">
                <li class="active">
                    <a href="#"><i class="fa fa-fw fa-dashboard"></i> Dashboard</a>
                </li>            
                <li>
                    <a href="showpasswordchange.htm"><i class="fa fa-fw fa-table"></i> Change Password</a>
                </li>
                <li>
                    <a href="showDdoDetails.htm"><i class="fa fa-fw fa-table"></i> Single Bill Verification </a>
                </li>
                <c:if test="${LoginUserBean.loginuserid eq 'tr.dtinon'}">
                    <li>
                        <a href="EmployeeNPSDataReportPage.htm"><i class="fa fa-fw fa-table"></i> Employee NPS Data </a>
                    </li>
                    <li>
                        <a href="npsDataExcelPage.htm"><i class="fa fa-fw fa-table"></i> ALL HRMS NPS DATA EXCEL </a>
                    </li>                    
                    <li>
                        <a href="nonnpsExcelPage.htm"><i class="fa fa-fw fa-table"></i> HRMS NON NPS DATA EXCEL(Jul 2022 onwards) </a>
                    </li>
                </c:if>
            </ul>
        </c:if>
    </div>
    <!-- /.navbar-collapse -->
</nav>
<c:if test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}"> 
    <script type="text/javascript">
        $(document).ready(function () {
            $('#page-wrapper').css('margin-top', '149px');
            $('body').css('background-color', '#FFFFFF');
            date_time('date_time');
            $(window).resize(function () {
                if ($(window).width() > 768) {
                    $('#date_time').css('display', 'block');
                }
                else
                {
                    $('#date_time').css('display', 'none');
                }
            });
        });
    </script>
</c:if>