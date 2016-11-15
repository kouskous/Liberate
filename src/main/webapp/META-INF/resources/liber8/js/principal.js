    /***** Definition du modele global ***************/
    var App = new Object();
    App.currentOnglet = "";
    App.currentVoletElement = "";
    App.onglets = new Array() ; 
    /********************************************/


$( document ).ready(function() {
    //inclusion de la coloration syntaxique
    $("#editeur code").keyup(function() {
         $('pre code').each(function(i, block) {
        hljs.highlightBlock(block);
      });
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
                                type="isFolder"
                                if ((data[i]["type"] == "fichier") && (j == slug.length - 1)){
                                    type="isFile";
                                } 
                                if(element1==null) {
                                    id = parent_id.replace(/\//g,'-');
                                    $(element).append('<li id="'+id+'" class="branche-arbre '+type+' noeud"><ul id='+parent_id+'><a>'+slug[j]+'</a></ul></li>');
                                }
                            }
                        });
                        $('#arbre').easytree();
                        defineArbreEvents();
                    }       
    });
    
    //definition des evenements sur l'arbre:
    function defineArbreEvents(){
        //double click sur un fichier
        $(".isFile").dblclick(function(){
            id = $(this).attr("id");
            id = id.replace(/-/g,'/');
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
            if(typeof App.onglets[id] === 'undefined'){
                fileName = id.split('/');
                fileName = fileName[fileName.length - 1];
                $("#onglets").append('<li class="onglet" data-id="'+ id +'" ><a href="#">'+fileName+' <i data-id="'+ id +'" class="icon-remove close-onglet"></i></a></li>');
                App.onglets[id] = content;
                $("#editeur code").html(App.onglets[id]);
            }
            }
            });
            
        });
        
        //selection d'un fichier
        $(".branche-arbre").bind("click");
        $(".branche-arbre").on("click", function(){
            id = $(this).attr("id");
            id = id.replace(/-/g,'/');
            App.currentVoletElement = id;
        });
        

        
    }
    
    function defineOngletsEvents(){
        $("#onglets").on("click", ".onglet", function(){
            id = $(this).data("id");
            $("#editeur code").html(App.onglets[id]);
            App.currentOnglet = id;
        });
        
        $("#onglets").on("click", ".close-onglet", function(){
            id = $(this).data("id");
            delete App.onglets[id];
            $(".onglet[data-id='"+ id +"']").remove();
            App.currentOnglet = "";
            for (var key in App.onglets) {
                App.currentOnglet = key;
                break;
            }
            if (App.currentOnglet !== ""){
                $("#editeur code").html(App.onglets[App.currentOnglet]);
            } else {
                $("#editeur code").html("");
            }
            
            
        });
    }
   
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
});


