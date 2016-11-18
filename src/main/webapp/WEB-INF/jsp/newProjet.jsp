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
  
     <link id="base-style-responsive" href="/Liber8/resources/liber8/css/newProjet.css" rel="stylesheet">
	
</head>
    <body>    
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="box" span12>
                <div class="box-header" data-original-title>
						<h2><i class="icon-folder-close"></i></i><span class="break"></span>Nouveau projet</h2>
						
					</div>
               
                    <form action = "newProjet" method="POST" id="newprojet">
                        
                        <div class="box span6">
                            <div class="box-header" data-original-title="">
						<h2><i class="halflings-icon edit"></i><span class="break"></span>Général</h2>
					</div>
                           <div class="newproject">
                            <div class="projetinput">
                               
                               <label class="span4">Nom du Projet</label>
                                <input name="nomProjet" id="nomProjet" type="text" placeholder=" Nom du Projet"/>
                            </div>
                            <div class="control-group">
								<label class="control-label userline span4" for="selectErrortype">Langage</label>
								<div class="controls userline">
								  <select id="selectErrortype" data-rel="chosen" name="langageProjet">
									<option value="java">Java</option>
									<option value="c++">C++</option>
									<option value="c">C</option>
									<option value="javascript">Javascript</option>
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
								  <div  class="rad" style="clear:both"></div>
								  <label class="radio">
									<input type="radio" name="optionsRadios" id="optionsRadios2" value="existant">
									a partir d'un projet existant 
								  </label>
								</div>
							  </div>
                           </div></div>
                        <div class="box span6" id="adduser" class="coll">
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
                      <div class="alert alert-error role alert"><i class="icon-exclamation-sign" aria-hidden="true"></i><span></span></div>  
      
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
            <div class="alert alert-error role alert span12"><i class="icon-exclamation-sign" aria-hidden="true"></i><span></span></div>  
 
       <div class="ui-widget">
            <input id="usersname" type="text">
            
       </div>
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
		<script src="/Liber8/resources/metro/js/bootstrap.min.js"></script>

	
	
		
		
		
	
		

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
    
            var max_input     = 10; //le nombre maximun des input
            var adduser       = $("#adduser"); // div contenant tout les utilisateurs
            var add_button      = $("#adduserbouton"); //Ajouter bouton id
         
            var arrname = new Array();
            
            $('.addbouton').click(function(e) {
            $('#adduser .usernew .control-group label').each(function() { 
            arrname.push(this.innerHTML); });
               });
          
            numberinput=0;
            $(add_button).click(function(e){ //on add input button click
                 e.preventDefault();
                $('#listuser').css("display","block");
                if(numberinput < max_input){ //max input box allowed
                    numberinput++; //text box increment
                   
                   var getname=$('#usersname').val();
                     
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
					                   <div class="controls userline"><select id="selectError'+numberinput+'" data-rel="chosen" name="droit'+numberinput+'" class="droit"><option value="admin">admin</option>\n\
					                   <option value="reporteur">reporteur</option><option class="developpeur">developpeur</option></select><a class="remove_field"><input type="hidden" value="'+getname+'" class="utilisateur'+numberinput+'" name="utilisateur'+numberinput+'"><i class="icon-trash"></i></a></div></div>'); //add input box
                        		for(var i = availableTags.length - 1; i >= 0; i--) {
    if(availableTags[i] === getname) {
       availableTags.splice(i, 1);
    }
}

            
            }
            
                }
                                        
           
        });
    $(adduser).on("click",".remove_field", function(e){ //user click on remove text
        
          
        e.preventDefault(); 
        var valeursupprimer = $(this).parents('li').find('input[type=hidden]').val();
        $(this).parents('li').remove(); 
        availableTags.push(valeursupprimer );
        numberinput--;
    });
    $("#submitaddusrer").click(function(e){
  
        if(!$('#nomProjet').val()){
           
            $('#nomProjet').addClass("bordure");
             return false;
        }
        else {
           
            var nomprojet = $('#nomProjet').val();
            var lang = $('#selectErrortype').val();
            var utilisateur = {};
            var droit = {}
            var i =0;
             var j =0;
            $('input:hidden').each(function(){
            utilisateur[i++] = this.value;
            
});
            $('.droit').each(function(){
            droit[j++] = this.value;
});
             $.ajax({ 
                url      : "/Liber8/newProjet",
                dataType : "json",
                type     : "POST",
                data     : {
                       nomProjet : nomprojet,
                       langageProjet: lang,
                       utilisateur,
                       droit
                   },
                           
                       
                
                    
                success  : function(data) {
                    
                    if(data.errors!==""){
                    
                        $('#projet .modal-content .alert-error').css('display','block');
                            $('#listuser .modal-body .alert-error').css('display','none');
                         $('#projet .modal-content .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>'+data.errors+ '</span>');
                    
        }
                    
                    else{
                        $('#projet .modal-content .alert-error').css('display','none');
                         $('#projet').css("display","none");
                        
                    }
                }
            });
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
