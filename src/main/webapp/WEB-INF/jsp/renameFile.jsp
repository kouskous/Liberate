<div class="constainer">
<div  id="renamefile">
        <div class="box">
            <div class="box-header" data-original-title="">
		<h2><i class="halflings-icon edit"></i><span class="break"></span>Renommer le fichier</h2>
            </div>
            <div class="contain_modal">
                <label>
                    Nom du fichier:
                </label>
                <input name="input_rename_file" id="input_rename_file" type="text" placeholder="Ex: index.html"/> 
                 <div class="form-actions1">
                      <button id="btn_renommer"  class="btn btn-primary" > Renommer </button>
                 </div>
                <div class="alert alert-error role alert"><i class="icon-exclamation-sign" aria-hidden="true"></i><span></span></div>  
               </div>
        </div>
</div>
</div>

<script src="/Liber8/resources/liber8/js/rename.js"></script>

<style>
    #renamefile {
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
   #renamefile label {
        padding-bottom: 10px;
    }