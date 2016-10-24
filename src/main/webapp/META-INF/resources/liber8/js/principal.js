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

});


