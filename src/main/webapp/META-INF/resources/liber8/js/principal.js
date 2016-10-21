$( document ).ready(function() {
    $("#editeur code").keyup(function() {
         $('pre code').each(function(i, block) {
        hljs.highlightBlock(block);
      });
    });
});


