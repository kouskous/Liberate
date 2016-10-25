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
            App.currentOnglet = id;
            App.currentVoletElement = id;
        });
        
        $(".branche-arbre").click(function(){
            id = $(this).attr("id");
            id = id.replace(/-/g,'/');
            App.currentVoletElement = id;
        });
    }






});


