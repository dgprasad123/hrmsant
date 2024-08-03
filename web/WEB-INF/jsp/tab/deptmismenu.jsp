<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    <jsp:useBean id="date" class="java.util.Date" />
    <fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
    <fmt:formatDate value="${date}" pattern="MMMM" var="currentMonth" />
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
    <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav side-nav">
            <li class="active">
                <a href="#"><i class="fa fa-fw fa-dashboard"></i> Dashboard</a>
            </li>
            <li>
                <a href="misDeptVacantPost.htm"><i class="fa fa-fw fa-edit"></i>Base Level Vacant Post</a>
            </li>
            <li>
                <a href="misRaschemeList.htm"><i class="fa fa-fw fa-edit"></i>Rehabilitation Scheme</a>
            </li>            
            <li>
                <a href="misScstRecruitmentList.htm"><i class="fa fa-fw fa-edit"></i>Special Recruitment</a>
                
            </li>
            <li>
                <a href="misRecruitmentDriveList.htm"><i class="fa fa-fw fa-edit"></i> Recruitment Drive</a>
            </li>
            <li>
                <a href="misTrainingList.htm"><i class="fa fa-fw fa-edit"></i> Training</a>
            </li>
            <li>
                <a href="misBleoList.htm"><i class="fa fa-fw fa-edit"></i> BLEO Recruitment</a>
            </li>
             <li>
                <a href="miscommissionPendingList.htm?year=${currentYear}&month=${currentMonth}"><i class="fa fa-fw fa-edit"></i> Monthly Activity Pending Requisition Report </a>
            </li>
            </li>
             <li>
                <a href="miscommissionCourtCaseList.htm?year=${currentYear}&month=${currentMonth}"><i class="fa fa-fw fa-edit"></i>Monthly Activity Court Case Report </a>
            </li>
            <li>
                <a href="changePasswordmis.htm"><i class="fa fa-fw fa-edit"></i> Change password</a>
            </li>
        </ul>
    </div>
    <!-- /.navbar-collapse -->
</nav>