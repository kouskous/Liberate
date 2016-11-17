/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template 
 */

$( document ).ready(function() {
    $('#newfile .alert-error').css('display','none');
   $("#btn_new_file").click(function(){
      filename = $("#input_name_file").val();
        if(filename.length === 0){
            $('#newfile .alert-error').css('display','block');
            $('#newfile .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom  du fichier est vide ! Veuillez le remplir </span>');
            
       }    
        else {
            $('#newfile .alert-error').css('display','none'); 
            
      $.ajax({ 
        url      : "/Liber8/newFile",
        dataType : "json",
        type     : "POST",
        data     :{
                    pathFichier: (App.currentVoletElement+'/'+filename).slice(4)
                  },
        success  : function(data) {  
                        
                    }       
        });
       $('#projet').modal('hide');
              
        
          }
         
         
        
    });
   });
