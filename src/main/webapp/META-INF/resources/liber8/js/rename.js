$( document ).ready(function() {
    $('#renamefile .alert-error').css('display','none');

    /** Renommer le fichier sélectionné dans le volet **/
    $("#btn_renommer").click(function(){
        path = App.currentVoletElement.replace(/\//g,'-');
        path = path.replace('.','__');
        element = $("#"+path);
        if(element.hasClass("verou-reserve")) {
            path = (App.currentVoletElement).slice(4);
            name = $("#input_rename_file").val();
            if(name.length === 0){   
                $("#renamefile .alert-error").css("display","block");
                $("#renamefile .alert-error").html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom  du fichier est vide ! Veuillez le remplir </span>');
            }
            else { 
                $.blockUI({ message: '<h2><img src="/Liber8/resources/blockUi/busy.gif" /> Renommage...</h2>' });
                $.ajax({ 
                url      : "/Liber8/renameFile",
                type     : 'POST',
                dataType : "json",
                data     :{
                              pathFichier: path,
                              filename: name
                          },
                success  : function(data) {  
                                  $.unblockUI();
                                  if(data.response){
                                        currentVolet = App.currentVoletElement.slice(4);
                                        $(".close-onglet[data-id='"+ currentVolet +"']").trigger("click");
                                        refreshTree();
                                        $("#close_modal_btn").trigger("click");
                                        toastr.success("Renommage réussi");
                                  } else {
                                        $("#close_modal_btn").trigger("click");
                                        toastr.warning(data.errors);
                                  }
                            }       
                });
            }
        }
        else {
            $("#close_modal_btn").trigger("click");
            toastr.warning("Vous ne pouvez renommer que les fichiers que vous avez vérouillés");
        }
    });
});
