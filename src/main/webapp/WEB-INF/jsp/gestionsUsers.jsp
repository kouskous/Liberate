<%-- 
    Document   : getUsers
    Created on : 19 nov. 2016, 17:59:47
    Author     : sofiaa faddi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head><link id="base-style-responsive" href="/Liber8/resources/liber8/css/newProjet.css" rel="stylesheet">
    </head>
       <body>    
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="box gestionsuser">
                    <div class="box-header" data-original-title>
						<h2><i class="icon-folder-close"></i></i><span class="break"></span>Gestion des utilisateurs</h2>
						
                    </div>
                    <h3> Utilisateurs assignés pour ce projet</h3>
                    
               
                 <p> Cliquer sur le bouton plus pour ajouter des utilisateurs à votre projet </p>
                            <ul class='usernew'>
                            </ul>
                            <a href="#"  data-toggle="modal" data-target="#gestionuser" class="addbouton"><i class="icon-plus"></i></a>
                      </div>      
                <div class="form-actions">
			<button type="submit" class="btn btn-primary" id="submitaddusrer">Appliquer</button>
		</div>
            </div>
        </div>
             <div class="modal fade" id="gestionuser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
      
               
    </body>
</html>
