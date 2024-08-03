


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="index.html">${LoginUserBean.loginname}</a>
    </div>
    <!-- Top Menu Items -->
    <ul class="nav navbar-right top-nav">                
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> ${LoginUserBean.loginname} <b class="caret"></b></a>
            <ul class="dropdown-menu">                
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
    <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->

    <!-- This Part of menu will visible for Services 1 -->
    <c:if test="${LoginUserBean.loginuserid eq 'services1'}">
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav side-nav">
                <li class="active">
                    <a href="servicesdashboard.htm"><i class="fa fa-fw fa-dashboard"></i> Dashboard</a>
                </li>
                <li>
                    <a href="postIncumbencyChart.htm"><i class="fa fa-fw fa-bar-chart-o"></i> Incumbency Report</a>
                </li>
                <li>
                    <a href="cadreEmployeeReportIAS.htm"><i class="fa fa-fw fa-table"></i> List of IAS</a>                
                </li>            
            </ul>
        </div>
    </c:if>
    <!-- This Part of menu will visible for Agodisha -->

  <c:if test="${LoginUserBean.loginuserid eq 'agodisha'}">
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav side-nav">
                <li class="active">
                    <a href="showXMLFileCreation.htm"><i class="fa fa-fw fa-dashboard"></i> LTA Thread </a>
                </li>
                <li>
                    <a href="ltaSchduleHTML.htm"><i class="fa fa-fw fa-dashboard"></i> LTA Schedule </a>
                </li>
            </ul>
        </div>
    </c:if>
    <!-- This Part of menu will visible for LIC of India  -->
    
    <c:if test="${LoginUserBean.loginuserid eq 'licindia'}">
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav side-nav">
                <li class="active">
                    <a href="LICReport.htm?offCode="><i class="fa fa-fw fa-dashboard"></i> LIC Report </a>
                </li>
                

            </ul>
        </div>
    </c:if>

    <!-- This Part of menu will visible for Controller of Accounts  -->

    <c:if test="${LoginUserBean.loginuserid eq 'caohrms'}">
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav side-nav">
                <li class="active">
                    <a href="TreasuryReportTPF.htm?offCode="><i class="fa fa-fw fa-dashboard"></i> Dashboard </a>
                </li>
                <li>
                    <a href="TreasuryReportTPF.htm?offCode="><i class="fa fa-fw fa-dashboard"></i> Treasury Wise Tpf List </a>
                </li>
            </ul>
        </div>
    </c:if>
    <!-- /.navbar-collapse -->
</nav>