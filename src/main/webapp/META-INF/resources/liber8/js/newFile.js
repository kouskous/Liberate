/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$( document ).ready(function() {
   $("#btn_new_file").click(function(){
      filename = $("#input_name_file").val();
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
   });
});
