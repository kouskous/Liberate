/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template ks
 */

$( document ).ready(function() {
        path = App.currentVoletElement.replace(/\//g,'-');
        path = path.replace('.','__');
        element = $("#"+path);
        $('#newfile .alert-error').css('display','none');
        $("#btn_new_file").click(function(){
        filename = $("#input_name_file").val();
        if(filename.length === 0){   
            $('#newfile .alert-error').css('display','block');
            $('#newfile .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom  du fichier est vide ! Veuillez le remplir </span>');
            
        } else {
            if(path==="") {
                 
                $('#newfile .alert.alert-error').css('display','block');
                 $('#newfile .alert.alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Veuillez selectionner un projet </span>');
            
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
                                if(data.response){
                                    $("#close_modal_btn").trigger("click");
                                    var sourceNode = {};
                                    sourceNode.text = filename;
                                    sourceNode.id = path+"-"+filename.replace('.','__');
                                    sourceNode.isFolder = false;
                                    sourceNode.liClass = "verou-reserve";
                                    App.tree.addNode(sourceNode, path);
                                    var nodes = App.tree.getAllNodes();
                                    App.tree.rebuildTree(nodes);
                                    $("#"+sourceNode.id).addClass("isFile");
                                    defineArbreEvents();
                                    $("#"+sourceNode.id).trigger("dblclick");
                                    $("#"+path).next().css("display","block");
                                    App.tree.activateNode(sourceNode.id);
                                } else {
                                    $("#close_modal_btn").trigger("click");
                                    toastr.warning(data.errors);
                                }
                                  
                            }       
                });
            }
        }
    }
         
        
    });

   });
