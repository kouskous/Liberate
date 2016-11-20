/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template ks
 */

$( document ).ready(function() {
    $('#newfile .alert-error').css('display','none');
   $("#btn_new_file").click(function(){
      filename = $("#input_name_file").val();
        if(filename.length === 0){   
            $('#newfile .alert-error').css('display','block');
            $('#newfile .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom  du fichier est vide ! Veuillez le remplir </span>');
            
        } else {
            if(path==="") {
                 
                $('#newFolder .alert.alert-error').css('display','block');
                 $('#newFolder .alert.alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Veuillez selectionner un projet </span>');
            
             }
              
             
             else {
            
            if ((path != "")&&(!$(element).hasClass("isFile"))){
              Logicpath = (App.currentVoletElement+"/"+filename).slice(4);
              $.ajax({ 
                 url      : "/Liber8/newFile",
                 dataType : "json",
                 type     : "POST",
                 data     :{
                             pathFichier: Logicpath
                           },
                 success  : function(data) {  
                                  $(".close").trigger("click");
                                  var nodes = App.tree.getAllNodes();
                                  var sourceNode = {};
                                  sourceNode.text = filename;
                                  sourceNode.id = path+"-"+filename.replace('.','__');
                                  sourceNode.isFolder = false;
                                  App.tree.addNode(sourceNode, path);
                                  App.tree.rebuildTree();
                                  $("#"+sourceNode.id).addClass("isFile");
                                  $("#"+sourceNode.id).addClass("branche-arbre");
                                  defineArbreEvents();
                                  $("#"+sourceNode.id).trigger("dblclick");
                                  $("#"+path).next().css("display","block");
                                  App.tree.activateNode(sourceNode.id);
                             }       
                 });
            }
        
        }
    }
         
        
    });

   });
