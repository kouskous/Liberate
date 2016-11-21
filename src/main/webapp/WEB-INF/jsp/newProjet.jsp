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
									à partir d'un projet existant 
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
								<button type="button" class="btn" data-dismiss="modal">Annuler</button>
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
                <script src="/Liber8/resources/liber8/js/newProjet.js"></script>       
