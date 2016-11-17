/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$( document ).ready(function() {
   $("#btn_new_file").click(function(){
      filename = $("#input_name_file").val();
      path = App.currentVoletElement.replace(/\//g,'-');
      path = path.replace('.','__');
      element = $("#"+path);
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
                            sourceNode.id = path+"-"+filename;
                            sourceNode.isFolder = false;
                            App.tree.addNode(sourceNode, path);
                            App.tree.rebuildTree();
                            $("#"+sourceNode.id).addClass("isFile");
                            $("#"+sourceNode.id).addClass("branche-arbre");
                            defineArbreEvents();
                       }       
           });
      }

   });
});
