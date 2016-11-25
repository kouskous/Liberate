/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Author : sofiaa faddi
 */

$( document ).ready(function() {
        console.log(App.currentVoletElement);
        $('#newfolder .alert-error').css('display','none');
        $("#btn_new_folder").click(function(){
        foldername = $("#input_name_folder").val();
        path = App.currentVoletElement.replace(/\//g,'-');
        path = path.replace('.','__');
        element = $("#"+path);
        if(foldername.length === 0){
            $('#newFolder .alert.alert-error').css('display','block');
            $('#newFolder .alert.alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom  du dossier est vide ! Veuillez le remplir </span>');
            
        } else {
            
            if(path==="") {
                 
                $('#newFolder .alert.alert-error').css('display','block');
                $('#newFolder .alert.alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Veuillez selectionner un projet </span>');
            
            }
            else if (element.hasClass("isFile")) {
                $('#newFolder .alert.alert-error').css('display','block');
                $('#newFolder .alert.alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Veuillez selectionner un dossier et non un fichier </span>');
            
            }
            else {
                if ((path !== "")&&(!$(element).hasClass("isFolder"))){
                    Logicpath = (App.currentVoletElement+"/"+foldername).slice(4);
                    $.ajax({ 
                    url      : "/Liber8/newDossier",
                    dataType : "json",
                    type     : "POST",
                    data     :{
                                pathDossier: Logicpath
                              },
                    success  : function(data) {  
                                    $('#projet').modal('hide') 
                                    var sourceNode = {};
                                    sourceNode.text = foldername;
                                    sourceNode.id = path+"-"+foldername.replace('.','__');
                                    sourceNode.isFolder = true;
                                    sourceNode.isExpanded = true;
                                    App.tree.addNode(sourceNode, path);
                                    var nodes = App.tree.getAllNodes();
                                    App.tree.rebuildTree(nodes);
                                    defineArbreEvents();
                                    $("#"+sourceNode.id).trigger("click");
                                    $("#"+path).next().css("display","block");
                                    App.tree.activateNode(sourceNode.id);
                                }       
                    });
                }
            }
        }
         
         
        
    });

});

