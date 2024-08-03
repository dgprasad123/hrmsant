<!DOCTYPE html>
<html lang="en">
<head>
  <title>View Transfer Details</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <style>
	table th{
		font-size: 14px;
		color: #fff;
		background: #3D649E;
		padding: 0px 10px;
		border: 1px solid #e6e7e2;
		text-align: left;
		font-weight: normal;
	}
  </style>
  <script type="text/javascript">
	$("#id_save").hide();
	$("#id_more").hide();
	
	function show_table(){
		var gpf=$("#gpf").val();
		if(gpf==""){
			alert("Enter GPF/HRMS ID");
			return false;
		}
		$("#id_current_posting").show();
	}
	function hide_table(){
		$("#gpf").val("");
		$("#id_more").hide();
		$("#id_current_posting").hide();
		$("#id_transfer_details_page").hide();
		$("#office").val("");
		$("#posting").val("");
	}
	function show_transfer_page(){
		$("#id_save").show();
		$("#id_transfer_details_page").show();
		$("#office").val("");
		$("#posting").val("");
	}
	function save_data(){
		$("#id_save").hide();
		$("#id_more").show();
		$("#id_transfer_list").show();
		
		$("#id_transfer_list").show();
	}
  </script>
</head>
<body>
 
<div class="container">  
  <div class="panel-group">
   

    <div class="panel panel-primary">
      <div class="panel-heading">&nbsp;</div>
      <div class="panel-body">
		<h4 align="center">
			GOVERNMENT OF ODISHA<br/>
			GENERAL ADMINISTRATION & PUBLIC GRIEVANCE DEPARTMENT
		</h4>
		<h4 align="center">
			NOTIFICATION
		</h4>
		<div align="center">
			Bhubaneswar, dated the 26th April, 2021
		</div>
		
		<form class="form-horizontal" action="#" style="margin-top:10px">
			<div class="form-group">
			  <label class="control-label col-sm-2" for="email">Enter GPF/HRMS ID:</label>
			  <div class="col-sm-5">
					<input type="text" class="form-control" id="gpf"  name="gpf">
			  </div>
			   <div class="col-sm-3">
					<button type="button" class="btn btn-warning" onclick="show_table();">Search</button>
					&nbsp;
					<button type="button" class="btn btn-info" onclick="hide_table();">Clear Search</button>
			  </div>
			</div>
		</form>
		<table class="table table-bordered" id="id_current_posting" style="display:none">
		  <thead>
			<tr>
			  <th scope="col">#</th>
			   <th scope="col">Name</th>
			   <th scope="col">Current Cadre</th>
			  <th scope="col">Current Posting</th>
			  <th scope="col">&nbsp;</th>			
			</tr>
		  </thead>
		  <tbody>
			<tr>
			  <td scope="row">1</td>
			  <td scope="row">Shri Madhusudan Das</td>			  
			  <td>OAS (S) </td>
			  <td>OAS (S), PD,DRDA, Bhadrak</td>
			  <td><button class="btn btn-danger" onclick="show_transfer_page()">Transfer To</button></td>
			</tr>
			</tbody>
	    </table>
		<div id="id_transfer_details_page" style="display:none">
		<form class="form-horizontal" action="#" style="margin-top:10px"  >
			<div class="form-group">
			  <label class="control-label col-sm-2" for="email">Select Department:</label>
			  <div class="col-sm-5">
						<select name="department" id="department" class="form-control">
						 <option value="">Select</option>
							<option value="01">AGRICULTURE AND FARMERS EMPOWERMENT</option><option value="02">COMMERCE AND TRANSPORT (COMMERCE)</option><option value="03">COMMERCE AND TRANSPORT (TRANSPORT)</option><option value="04">COOPERATION</option><option value="18">ELECTRONICS &amp; INFORMATION TECHNOLOGY</option><option value="05">ENERGY</option><option value="06">EXCISE</option><option value="07">FINANCE</option><option value="08">FISHERIES AND ANIMAL RESOURCES DEVELOPMENT</option><option value="09">FOOD SUPPLIES AND CONSUMER WELFARE</option><option value="10">FOREST AND ENVIRONMENT</option><option value="11">GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE</option><option value="33">HANDLOOMS, TEXTILES AND HANDICRAFTS</option><option value="12">HEALTH AND FAMILY WELFARE</option><option value="13">HIGHER EDUCATION</option><option value="14">HOME</option><option value="15">HOUSING AND URBAN DEVELOPMENT</option><option value="16">INDUSTRIES</option><option value="17">INFORMATION AND PUBLIC RELATIONS</option><option value="19">LABOUR AND ESI</option><option value="20">LAW</option><option value="73">MICRO  SMALL AND MEDIUM ENTERPRISES</option><option value="34">ODIA LANGUAGE LITERATURE &amp; CULTURE</option><option value="50">ODISHA LEGISLATIVE ASSEMBLY</option><option value="21">PANCHAYATI RAJ  AND DRINKING WATER</option><option value="22">PARLIAMENTARY AFFAIRS</option><option value="23">PLANNING AND CONVERGENCE</option><option value="24">PUBLIC ENTERPRISE</option><option value="26">REVENUE AND DISASTER MANAGEMENT</option><option value="27">RURAL DEVELOPMENT</option><option value="31">SCHEDULED TRIBES AND SCHEDULED CASTES DEVELOPMENT, MINORITIES AND BACKWARD CLASSES WELFARE</option><option value="28">SCHOOL AND MASS EDUCATION</option><option value="29">SCIENCE AND TECHNOLOGY</option><option value="72">SKILL DEVELOPMENT AND TECHNICAL EDUCATION</option><option value="74">SOCIAL SECURITY AND EMPOWERMENT OF PERSON WITH DISABILITIES </option><option value="30">SPORTS AND YOUTH SERVICES</option><option value="32">STEEL AND MINES</option><option value="35">TOURISM</option><option value="36">WATER RESOURCES</option><option value="37">WOMEN AND CHILD DEVELOPMENT AND MISSION SHAKTI</option><option value="38">WORKS</option>
						</select>
			  </div>
			  
			</div>
			<div class="form-group">
			  <label class="control-label col-sm-2" for="email">Select Office:</label>
			  <div class="col-sm-2">
						<select name="office" id="office" class="form-control">
							<option value="">Select</option><option value="OLSGAD0010001">CENTER FOR MODERNIZING GOVERNMENT INITIATIVE(CMGGAD001)</option><option value="BBSGAD0020000">CHAIRMAN , ODISHA STAFF SELECTION COMMISSION, ODISHA(BBSGAD002)</option><option value="BBSGAD0040000">CHAIRMAN, ODISHA SUBORDINATE STAFF SELECTION COMMISSION, BHUBANESWAR(BBSGAD004)</option><option value="OLSGAD0010003">DEPARTMENT OF ADMINISTRATIVE REFORMS AND PUBLIC GRIEVANCES, GOVERNMENT OF INDIA, NEW DELHI(OLSGAD001)</option><option value="CTSGAD0010000">DIRECTOR INSPECTOR GENERAL OF POLICE, VIGILANCE, ODISHA(CTSGAD001)</option><option value="OLSGAD0010000">GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE DEPARTMENT,GOVERNMENT OF ODISHA(OLSGAD001)</option><option value="KRDGAD0010000">GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE (RENT) DEPARTMENT, GOVERNMENT OF ODISHA, BHUBANESWAR(KRDGAD001)</option><option value="BBSGAD0030000">GOPABANDHU ACADEMY OF ADMINISTRATION, BHUBANESWAR(BBSGAD003)</option><option value="CTCGAD0010000">ODISHA PUBLIC SERVICE COMMISSION, ODISHA(CTCGAD001)</option><option value="BBSGAD0050000">OFFICE OF LOKAYUKTA,ODISHA,BHUBANESWAR(BBSGAD005)</option><option value="KRDGAD0020000">REGISTRAR, ODISHA ADMINISTRATIVE TRIBUNAL, ODISHA(KRDGAD002)</option><option value="BLSGAD0010000">SUPERINTENDENT OF POLICE, VIGILANCE BALASORE DIVISION, BALASORE(BLSGAD001)</option><option value="BAMGAD0010000">SUPERINTENDENT OF POLICE, VIGILANCE BERHAMPUR DIVISION, BERHAMPUR(BAMGAD001)</option><option value="KRDGAD0030000">SUPERINTENDENT OF POLICE, VIGILANCE,  BHUBANESWAR(KRDGAD003)</option><option value="CTSGAD0020000">SUPERINTENDENT OF POLICE, VIGILANCE CUTTACK DIVISION, CUTTACK(CTSGAD002)</option><option value="SBPGAD0010000">SUPERINTENDENT OF POLICE, VIGILANCE DIVISION, SAMBALPUR(SBPGAD001)</option><option value="JYRGAD0010000">SUPERINTENDENT OF POLICE, VIGILANCE, KORAPUT DIVISION, JEYPORE(JYRGAD001)</option><option value="PNPGAD0010000">SUPERINTENDENT OF POLICE, VIGILANCE, ROURKELA(PNPGAD001)</option>
						</select>
			  </div>
				<label class="control-label col-sm-2" for="email">Select Post:</label>
					<div class="col-sm-2">
						<select name="posting" id="posting" class="form-control">
							<option value="">Select</option>
							<option value="OLSGAD0010001">ED CMGI</option>
							<option value="BBSGAD0020000">Deputy Secretary</option>
							<option value="BBSGAD0040000">Joint Secretary</option>
							<option value="BBSGAD0040000">Additional Secretary</option>
							<option value="BBSGAD0040000">Special Secretary</option>
						</select>
			  </div>
			<div class="col-sm-4">	
				<input type="radio" name="charge" checked/>Main Charge &nbsp;
				<input type="radio" name="charge" />Additional Charge
			</div>	
			  
			  
			</div>
		</form>	
		</div>		
		<div align="center">
			<button type="button" class="btn btn-success" onclick="save_data()" id="id_save"  style="display:none">Save</button>
		</div>
		<div align="center">
			<button type="button" class="btn btn-warning" onclick="hide_table()" id="id_more"  style="display:none">Add More</button>
		</div>			
		<table class="table table-bordered" id="id_transfer_list" style="display:none">
			 <tbody>
			<tr>
			  <td scope="row">1</td>
			  <td scope="row">Shri Madhusudan Das, OAS (S), PD,
				DRDA, Bhadrak is transferred and posted as ED, CMGI GA & PG Department.</td>
			</tr>
			
		  </tbody>
		</table>	
	  </div>
    </div>

   
  </div>
</div>

</body>
</html>