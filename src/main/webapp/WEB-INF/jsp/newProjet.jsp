<%-- 
    Document   : newProjet
    Created on : Oct 20, 2016, 3:20:10 PM
    Author     : Faddi sofiaa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
        <head>
	
	<!-- start: Meta -->
	<meta charset="utf-8">
	<title># L!BER8</title>

	
	<!-- start: Mobile Specific -->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- end: Mobile Specific -->
	
	<!-- start: CSS -->
    <link id="bootstrap-style" href="/Liber8/resources/metro/css/bootstrap.min.css" rel="stylesheet">
	<link href="/Liber8/resources/metro/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link id="base-style" href="/Liber8/resources/metro/css/style.css" rel="stylesheet">
     <link href="/Liber8/resources/metro/css/jquery-ui-1.8.21.custom.css" rel="stylesheet">
	<link id="base-style-responsive" href="/Liber8/resources/metro/css/style-responsive.css" rel="stylesheet">
     <link id="base-style-responsive" href="/Liber8/resources/liber8/css/newProjet.css" rel="stylesheet">
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&subset=latin,cyrillic-ext,latin-ext' rel='stylesheet' type='text/css'>
	<!-- end: CSS -->

	<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
	  	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<link id="ie-style" href="resources/metro/css/ie.css" rel="stylesheet">
	<![endif]-->
	
	<!--[if IE 9]>
		<link id="ie9style" href="resources/metro/css/ie9.css" rel="stylesheet">
	<![endif]-->
		
	<!-- start: Favicon -->
	<link rel="shortcut icon" href="/Liber8/resources/metro/img/favicon.ico">
	<!-- end: Favicon -->
	
</head>
    <body>    
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="box" span12>
                <div class="box-header" data-original-title>
						<h2><i class="icon-folder-close"></i></i><span class="break"></span>Nouveau projet</h2>
						
					</div>
                <h2>${Erreur}</h2>
                    <form action = "getUsers" method="post" id="newprojet">
                        <div class="box span6">
                            <div class="box-header" data-original-title="">
						<h2><i class="halflings-icon edit"></i><span class="break"></span>Général</h2>
					</div>
                           <div class="newproject">
                            <div class="projetinput">
                               
                               <label class="span4">Nom du Projet</label>
                                <input name="name" id="nomProjet" type="text" placeholder=" Nom du Projet"/>
                            </div>
                            <div class="control-group">
								<label class="control-label userline span4" for="selectErrortype">Langage</label>
								<div class="controls userline">
								  <select id="selectErrortype" data-rel="chosen" name="langage">
									<option>Java</option>
									<option>C++</option>
									<option>C</option>
									<option>Javascript</option>
								  </select>
								</div>
							  </div>
                            <div class="control-group">
								<h4 class="control-label">Source</h4>
								<div class="controls">
								  <label class="radio">
									<input type="radio" name="optionsRadios" id="optionsRadios1" value="vide" checked="">
									vide
								  </label>
								  <div style="clear:both"></div>
								  <label class="radio">
									<input type="radio" name="optionsRadios" id="optionsRadios2" value="existant">
									a partir d'un projet existant 
								  </label>
								</div>
							  </div>
                           </div></div>
                        <div class="box span6" id="adduser">
                            <div class="box-header" data-original-title="">
						<h2><i class="icon-user"></i><span class="break"></span>Collaboration et partage</h2>
					</div>
                            <p> Cliquer sur le bouton plus pour ajouter des utilisateurs à votre projets </p>
                            <ul class='usernew'>
                            </ul>
                            <a href="#"  data-toggle="modal" data-target="#listuser" class="addbouton"><i class="icon-plus"></i></a>
                           
                        </div>
                         <div class="form-actions">
								<button type="submit" class="btn btn-primary" id="submitaddusrer">Terminer</button>
								<button class="btn">Annuler</button>
							  </div>
                       
                    </form>
                </div>
            </div>
        </div>
        <div class="modal fade" id="listuser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Choisissez un utilisateur</h4>
      </div>
      <div class="modal-body">
       <div class="ui-widget">
            <input id="usersname" type="text">
       </div>
        <div class="alert alert-error role alert"><i class="icon-exclamation-sign" aria-hidden="true"></i><span></span></div>  
      </div>
      <div class="modal-footer">
          
        <button type="button" class="btn btn-primary" id="adduserbouton">Enregistrer</button>
        <button type="button" class="btn closebtn">Fermer</button>
      </div>
    </div>
  </div>
</div>    
                   <script src="/Liber8/resources/metro/js/jquery-1.9.1.min.js"></script>
                    <script src="/Liber8/resources/metro/js/jquery-migrate-1.0.0.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery-ui-1.10.0.custom.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.ui.touch-punch.js"></script>
	
		<script src="/Liber8/resources/metro/js/modernizr.js"></script>
	
		<script src="/Liber8/resources/metro/js/bootstrap.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.cookie.js"></script>
	
		<script src='/Liber8/resources/metro/js/fullcalendar.min.js'></script>
	
		<script src='/Liber8/resources/metro/js/jquery.dataTables.min.js'></script>

		<script src="/Liber8/resources/metro/js/excanvas.js"></script>
	<script src="/Liber8/resources/metro/js/jquery.flot.js"></script>
	<script src="/Liber8/resources/metro/js/jquery.flot.pie.js"></script>
	<script src="/Liber8/resources/metro/js/jquery.flot.stack.js"></script>
	<script src="/Liber8/resources/metro/js/jquery.flot.resize.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.chosen.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.uniform.min.js"></script>
		
		<script src="/Liber8/resources/metro/js/jquery.cleditor.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.noty.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.elfinder.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.raty.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.iphone.toggle.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.uploadify-3.1.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.gritter.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.imagesloaded.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.masonry.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.knob.modified.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.sparkline.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/counter.js"></script>

		<script src="/Liber8/resources/metro/js/retina.js"></script>

		<script>
$( function() {
      $.ajax({ 
      url      : "/Liber8/getUsers",
          dataType : "json",
          success  : function(data) { 
          var nameusers = data.response;
          availableTags = JSON.parse(nameusers);
            $( "#usersname" ).autocomplete({
               source: availableTags
             });
    
            var max_input     = 20; //le nombre maximun des input
            var adduser       = $("#adduser"); // div contenant tout les utilisateurs
            var add_button      = $("#adduserbouton"); //Ajouter bouton id
            $('#listuser .modal-body .alert-error').css('display','none');
            var arrname = new Array();
            
            $('.addbouton').click(function(e) {
            $('#adduser .usernew .control-group label').each(function() { 
            arrname.push(this.innerHTML); });
               });
          
            numberinput=1;
            $(add_button).click(function(e){ //on add input button click
                 e.preventDefault();
                $('#listuser').css("display","block");
                if(numberinput < max_input){ //max input box allowed
                    numberinput++; //text box increment
                   var getname=$('#usersname').val();
                   if (jQuery.inArray(getname,arrname) === -1) {
                       console.log(arrname);
                   }
                   
                     
           	        	if (jQuery.inArray(getname,availableTags) === -1  || getname ==="") {
                            $('#listuser .modal-body .alert-error').css('display','block');
    				                $('#listuser .modal-body .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom est incorrect ! Réessayer </span>');
  			             	}
                                        
           	        	else {
                            $('#listuser .modal-body .alert-error').css('display','none');
                            $('#listuser').modal('hide');
                            $('#usersname').val("");
           			             $(".usernew").append('<li><div class="control-group">\n\
				                  	<label class="control-label userline" for="selectError'+numberinput+'">'+getname+'</label>\n\
					                   <div class="controls userline"><select id="selectError'+numberinput+'" data-rel="chosen" name="userprojet[]"><option>admin</option>\n\
					                   <option>reporteur</option><option>developpeur</option></select><a class="remove_field"><i class="icon-trash"></i></a></div></div>'); //add input box
                        		}
            
                }
                                        
           
        });
    $(adduser).on("click",".remove_field", function(e){ //user click on remove text
        e.preventDefault(); $(this).parents('li').remove(); numberinput--;
    });
    $("#submitaddusrer").click(function(e){
        if(!$('#nomProjet').val()){
           
            $('#nomProjet').addClass("bordure");
             return false;
        }
        else {
             $('#nomProjet').removeClass("bordure");
        }
        
    });
    $('#listuser .closebtn').click(function(e){
         $('#listuser').modal('hide');
    });
    }
        });
      });
    
  </script>
	<!-- end: JavaScript-->
	
    </body>
</html>
