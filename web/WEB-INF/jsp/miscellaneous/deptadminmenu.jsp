<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="index.html">Department Admin</a>
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
                <a href="index.html"><i class="fa fa-fw fa-dashboard"></i> Dashboard</a>
            </li>
            <li>
                <a href="getDeptvacantpostList.htm"><i class="fa fa-fw fa-edit"></i>Base Level Vacant Post</a>
            </li>
            
            <li>
                <a href="getRaschemeList.htm"><i class="fa fa-fw fa-edit"></i>Rehabilitation Scheme</a>
            </li> 
            
            <li>
                <a href="getScstRecruitmentList.htm"><i class="fa fa-fw fa-edit"></i>SC ST Recruitment</a>
                
            </li>
            <li>
                <a href="getRecruitmentDriveList.htm"><i class="fa fa-fw fa-edit"></i> Recruitment</a>
            </li>
            <li>
                <a href="getTrainingList.htm"><i class="fa fa-fw fa-edit"></i> Training</a>
            </li>
            <li>
                <a href="getBleoList.htm"><i class="fa fa-fw fa-edit"></i> Block Level Extention Officer</a>
            </li>            
        </ul>
    </div>
    <!-- /.navbar-collapse -->
</nav>
