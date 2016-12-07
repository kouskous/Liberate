    /***** Definition du modele global ***************/
    var App = {};
    App.currentOnglet = "";
    App.currentVoletElement = "";
    App.onglets = [];
    App.tree = "";
    App.editeur = "";
    /********************************************/
//definition des evenements sur l'arbre:
    function defineArbreEvents(){
        //double click sur un fichier
        $(".isFile").on("dblclick",function(){
            id = $(this).attr("id");
            id = id.replace(/-/g,'/');
            id = id.replace('__','.'); 
            id2 = id.replace("Root",'');
            $.ajax({ 
                url      : "/Liber8/getFile",
                dataType : "json",
                type     : "POST",
                data     : "pathLogique="+id2,
                success  : function(data) {
                    
                    content=data["content"];
                    App.currentOnglet = id;
                    App.currentVoletElement = id;
                     $("#onglets li").removeClass("active");
                    
                    if(typeof App.onglets[id] === 'undefined'){
                        fileName = id.split('/');
                        fileName = fileName[fileName.length - 1];
                        $("#onglets").append('<li class="onglet active" data-id="'+ id +'" ><a href="#">'+fileName+' <i data-id="'+ id +'" class="icon-remove close-onglet"></i></a></li>');
                        App.onglets[id] = content;
                        App.editeur.setValue(App.onglets[id]);
                    
                    }
                   
                   
                }
            });
            
        });
        
        //selection d'un fichier
        $(".easytree-node").on("click", function(){
            id = $(this).attr("id");
            id = id.replace(/-/g,'/');
            App.currentVoletElement = id.replace('__','.');
        });
    } 
    
    function defineOngletsEvents(){
        var close = false;
        $("#onglets").on("click", ".close-onglet", function(){
            close = true;
            id = $(this).data("id");
            delete App.onglets[id];
            $(".onglet[data-id='"+ id +"']").remove();
            App.currentOnglet = "";
            for (var key in App.onglets) {
                App.currentOnglet = key;
            }
            
            if (App.currentOnglet !== ""){
                App.editeur.setValue(App.onglets[App.currentOnglet]);
            } else {
                App.editeur.setValue("");
            }
        });
        
        
        $("#onglets").on("click", ".onglet", function(){
            id = $(this).data("id");
            if (!close) {
                App.editeur.setValue(App.onglets[id]);
                App.currentOnglet = id;
                $("#onglets li").removeClass("active");
                $(this).addClass("active");
                
            }
            close = false;
        });
    }
    
    function refreshTree(){
        $("#arbre").html('<ul id ="Root"></ul>');
        $.ajax({ 
          url      : "/Liber8/getTree",
          dataType : "json",
          success  : function(data) {  
                        $.each(data, function(i, item) {
                            slug = data[i]["path"].split("/");
                            var parent_id='Root';
                            for(var j=2; j<slug.length;j++) {
                                var element = document.getElementById(parent_id);
                                parent_id=parent_id+"/"+slug[j];
                                var element1 =document.getElementById(parent_id);
                                type="isFolder";
                                if ((data[i]["type"] === "fichier") && (j === slug.length - 1)){
                                    type="isFile";
                                } 
                                verouillage = "";
                                if((data[i]["type"] === "fichier") && (data[i]["verrouillage"] === 1)) {
                                    verouillage = "verou-bloque";
                                } else if((data[i]["type"] === "fichier") && (data[i]["verrouillage"] === 2)) {
                                    verouillage = "verou-reserve";
                                }
                                if(element1===null) {
                                    id = parent_id.replace(/\//g,'-');
                                    id = id.replace('.','__');
                                    $(element).append('<li id="'+id+'" data-url="'+id+'" class="branche-arbre '+type+' '+verouillage+' noeud"><ul id='+parent_id+'><a>'+slug[j]+'</a></ul></li>');
                                }
                            }
                        });
                        App.tree = $('#arbre').easytree();
                        defineArbreEvents();
                    }       
        });
    }
    
$( document ).ready(function() {
    //inclusion de la coloration syntaxique
    App.editeur = ace.edit("editeur");
    App.editeur.setTheme("ace/theme/twilight");
    App.editeur.session.setMode("ace/mode/javascript"); 
    
    $("#editeur").on('click',function(){
        path = App.currentOnglet.replace(/\//g,'-');
        path = path.replace('.','__');
        element = $("#"+path);
        if ($(element).hasClass("verou-reserve")){
            App.editeur.setReadOnly(false);
        } else {
            App.editeur.setReadOnly(true);
        }
    });

    //volet gauche resizable
    $( "#sidebar-left" ).resizable();
    $( "#sidebar-left" ).resize(function(){
    left = $( "#sidebar-left" ).css("width");
    $("#content").css("margin-left", left);
    $("#content").css("width", (100 - left) + "%" );
    });
    
    
    defineOngletsEvents();
    //Recuperation de l'arboresance de l'utilisateur
    refreshTree();

    $(".user-action").click(function(){
        url = $(this).data("url");
        $.ajax({ 
        url      : "/Liber8/"+ url,
        dataType : "html",
        success  : function(data) {  
                        $("#modal_content").html(data);
                    }
        });
    });    
    
    /** Sauvegarder le fichier dont l'onglet est sélectionné **/
    $("#btn_sauvegarder").click(function(){
        path = App.currentOnglet.replace(/\//g,'-');
        path = path.replace('.','__');
        element = $("#"+path);
        if(element.hasClass("verou-reserve")) {
            content = App.editeur.getValue();
            path = (App.currentOnglet).slice(4);
            $.blockUI({ message: '<h2><img src="/Liber8/resources/blockUi/busy.gif" /> Enregistrement...</h2>' });
            $.ajax({ 
                url      : "/Liber8/saveFile",
                type     : 'POST',
                dataType : "json",
                data     :{
                              pathFichier: path,
                              contenuFichier: content
                          },
                success  : function(data) {  
                                  $.unblockUI();
                                  if(data.response){
                                      toastr.success("Enregistré");
                                  } else {
                                      toastr.warning(data.errors);
                                  }
                              }       
                  });
        }
        else {
            toastr.warning("Vous ne pouvez enregistrer que les fichiers vérouillés actuellement en cours d'édition");
        }
    });
    
    $("#editeur").keyup(function(){
       App.onglets[App.currentOnglet] = App.editeur.getValue(); 
    });
    
   
    $("#btn_compile").click(function(){
            apppa = App.currentVoletElement;
            appPath1 =apppa.slice(5);
            appPath=appPath1.split('/')[0];
        
             $.blockUI({ message: '<h2><img src="/Liber8/resources/blockUi/busy.gif" /> Compilation...</h2>' });

            $.ajax({ 
                url      : "/Liber8/compile",
                type     : 'POST',
                dataType : "json",
                data     :{
                              nomProjet: appPath
                          },
                success  : function(data) {  
                   
                    $.unblockUI();
                        if(data.response==="true"){
                            toastr.success("compilé");
                            
                
                            $(".console").css('display','block');
                            $(".body_console").html(data.content+"Vous pouvez telecharger l'executable <a href='/Liber8/downloadExec?nomExec="+data.nomExec+"' download='"+data.nomExec+"'>Cliquer ici </a> ");
                            
                        } else if((data.response==="false" && data.content!=="")|| (data.response==="true" && data.content!=="")) {
                            toastr.warning("erreur pendant la compilation");
                              $(".console").css('display','block');
                              console.log(data.content);
                              error_in_compile=data.content.split("^").join("<br />");
                              console.log(error_in_compile);
                              $(".body_console").html(error_in_compile);
                        }
                        else {
                              toastr.warning("un problème est survenu au niveau de l'application ! réessayer plus tard");
                        }
                        }       
            });
        
        
    });
    $('.closeconsole').click(function(){
        $(".console").hide();
    });
    
    $("#btn_verrouiller").click(function(){
        if(App.currentVoletElement !== ""){
            path = App.currentVoletElement.replace(/\//g,'-');
            path = path.replace('.','__');
            element = $("#"+path);
            if(element.hasClass("isFile")) {
                if(!(element.hasClass("verou-reserve") || element.hasClass("verou-bloque"))) {
                    appPath = App.currentVoletElement.slice(4);
                    $.ajax({ 
                        url      : "/Liber8/verrouillerFichier",
                        type     : 'POST',
                        dataType : "json",
                        data     :{
                                      pathLogique: appPath
                                  },
                        success  : function(data) {  
                                    if(data.response){
                                        toastr.success("Fichier vérouillé");
                                        path = App.currentVoletElement.replace(/\//g,'-');
                                        path = path.replace('.','__');
                                        element = $("#"+path);
                                        $(element).addClass("verou-reserve");
                                    } else {
                                        toastr.warning(data.errors);
                                    }
                                }       
                    });
                }
                else {
                    toastr.warning("Le fichier selectionné est déjà vérrouillé");
                }
            }
            else {
                toastr.warning("Veuillez sélectionner un fichier");
            }
        }
    });
    
    $("#btn_deverrouiller").click(function(){
        if(App.currentVoletElement !== ""){
            path = App.currentVoletElement.replace(/\//g,'-');
            path = path.replace('.','__');
            element = $("#"+path);
            if(element.hasClass("isFile")) {
                if(element.hasClass("verou-reserve")) {
                    appPath = App.currentVoletElement.slice(4);
                    $.ajax({ 
                        url      : "/Liber8/deverrouillerFichier",
                        type     : 'POST',
                        dataType : "json",
                        data     :{
                                      pathLogique: appPath
                                  },
                        success  : function(data) {  
                                    if(data.response){
                                        toastr.success("Fichier vérouillé");
                                        path = App.currentVoletElement.replace(/\//g,'-');
                                        path = path.replace('.','__');
                                        element = $("#"+path);
                                        currentVolet = App.currentVoletElement.slice(4);
                                        $(".close-onglet[data-id='"+ currentVolet +"']").trigger("click");
                                        $(element).removeClass("verou-reserve");
                                    } else {
                                        toastr.warning(data.errors);
                                    }
                                }       
                    });
                }
                else {
                    toastr.warning("Le fichier selectionné doit être un fichier que vous avez vérrouillé");
                }
            }
            else {
                toastr.warning("Veuillez sélectionner un fichier");
            }
        }
    });
    
    
    /** Supprimer le fichier sélectionné dans le volet **/
    $("#btn_supprimer").click(function(){
        path = App.currentVoletElement.replace(/\//g,'-');
        path = path.replace('.','__');
        element = $("#"+path);
        if(element.hasClass("verou-reserve")) {
            path = (App.currentVoletElement).slice(4);
            $.blockUI({ message: '<h2><img src="/Liber8/resources/blockUi/busy.gif" /> Suppression...</h2>' });
            $.ajax({ 
            url      : "/Liber8/removeFile",
            type     : 'POST',
            dataType : "json",
            data     :{
                          pathFichier: path
                      },
            success  : function(data) {  
                              $.unblockUI();
                              if(data.response==="true"){
                                    currentVolet = App.currentVoletElement.replace(/\//g,'-');
                                    currentVolet = currentVolet.replace('.','__');
                                    $(".close-onglet[data-id='"+App.currentVoletElement+"']").trigger("click");
                                    App.tree.removeNode(currentVolet);
                                    res = path.split("/");
                                    res.pop();
                                    finalPath = "Root";
                                    for(var i=0; i<res.length; i++) {
                                        finalPath = finalPath+res[i]+"/";
                                    }
                                    finalPath = finalPath.slice(0,-1);
                                    id = finalPath.replace(/\//g,'-');
                                    id = id.replace('.','__');
                                    App.tree.activateNode(id);
                                    App.currentVoletElement = finalPath;
                                    var nodes = App.tree.getAllNodes();
                                    App.tree.rebuildTree(nodes);
                                    defineArbreEvents();
                                    toastr.success("Suppression réussie");
                              } else {
                                    toastr.warning(data.errors);
                              }
                        }       
            });
        }
        else {
            toastr.warning("Vous ne pouvez supprimer que les fichiers que vous avez vérouillés");
        }   
    });
    
    $("#btn_push").click(function(){
            apppa = App.currentVoletElement;
            appPath =apppa.slice(5);
             $.blockUI({ message: '<h2><img src="/Liber8/resources/blockUi/busy.gif" /> push...</h2>' });

            $.ajax({ 
                url      : "/Liber8/pushProjet",
                type     : 'POST',
                dataType : "json",
                data     :{
                              projet: appPath
                          },
                success  : function(data) {
                            $.unblockUI();
                            if(data.response===true){
                                toastr.success("push réalisé avec succés");
                                refreshTree();
                            } else {
                                toastr.warning(data.errors);
                            }
                        }       
            });
    });   
});