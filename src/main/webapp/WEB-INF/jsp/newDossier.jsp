<%-- 
    Document   : inscription
    Created on : 19 oct. 2016, 22:06:40
    Author     : sofiafaddi
--%>

<div class="constainer">
<div  id="newFolder">
        <div class="box">
            <div class="box-header" data-original-title="">
		<h2><i class="halflings-icon edit"></i><span class="break"></span>Nouveau Dossier</h2>
            </div>
            <div class="contain_modal">
                <label>
                    Nom du dossier:
                </label>
                <input name="input_name_file" id="input_name_folder" type="text" placeholder="Ex: controlleur"/> 
                 <div class="form-actions1">
                      <button id="btn_new_folder"  class="btn btn-primary" > Créer </button>
                 </div>
                <div class="alert alert-error role alert"><i class="icon-exclamation-sign" aria-hidden="true"></i><span></span></div>  
               </div>
        </div>
</div>
</div>

<script src="/Liber8/resources/liber8/js/newFolder.js"></script>
<style>
    #newFolder {
        width: 70%;
        margin: 0 auto;
    }
    .form-actions1 {
        margin: 20px auto;
        text-align: center;
    }
    .contain_modal {
        padding:10px;
    }
   #newFolder label {
        padding-bottom: 10px;
    }
    #newFolder .alert.alert-error {
        display: none;
    }