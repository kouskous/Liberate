<div class="constainer">
<form action = "newProjet" method="GET" id="newfile">
        <div class="box">
            <div class="box-header" data-original-title="">
		<h2><i class="halflings-icon edit"></i><span class="break nouveau"></span>Nouveau fichier</h2>
            </div>
            <div class="contain_modal">
                <label>
                    Nom du fichier:
                </label>
                <input name="input_name_file" id="input_name_file" type="text" placeholder="Ex: index.html"/> 
                 <div class="form-actions1">
                      <button id="btn_new_file"  class="btn btn-primary" > Créer </button>
                 </div>
                <div class="alert alert-error role alert"><i class="icon-exclamation-sign" aria-hidden="true"></i><span></span></div>  
               </div>
        </div>
</form>
</div>

<script src="/Liber8/resources/liber8/js/newFile.js"></script>
<style>
    #newfile {
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
   #newfile label {
        padding-bottom: 10px;
    }