/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Author : Sofiaa Faddi
 */

$(document).ready(function() {

    $("#submitaddusrer").click(function(e){
        e.preventDefault(); 
        if(!$('#nomProjet').val()){
            $('#nomProjet').addClass("bordure");
            return false;
        } else {
            $('#nomProjet').removeClass("bordure");
            var nomprojet = $('#nomProjet').val();
            var lang = $('#selectErrortype').val();
            var utilisateur = {};
            var droit = {};
            var i =0;
            var j =0;
            $('input:hidden').each(function(){
                utilisateur[i++] = this.value;
            });
            $('.droit').each(function(){
                droit[j++] = this.value;
            });
            $.ajax({ 
                url      : "/Liber8/newProjet",
                dataType : "json",
                type     : "POST",
                data     : {
                       nomProjet : nomprojet,
                       langageProjet: lang,
                       utilisateurs: JSON.stringify(utilisateur),
                       droits: JSON.stringify(droit)
                   },
                           
                success  : function(data) {
                   if(data.errors!==""){
                         $("#close_modal_btn").trigger("click");
                         toastr.warning(data.errors);
                    }
                    else if(data.response==="true"){
                            $("#close_modal_btn").trigger("click");
                            var sourceNode = {};
                            sourceNode.text = nomprojet;
                            sourceNode.id = "Root-"+nomprojet;
                            sourceNode.isFolder = true;
                            sourceNode.isExpanded = true;
                            App.tree.addNode(sourceNode);
                            var nodes = App.tree.getAllNodes();
                            App.tree.rebuildTree(nodes);
                            App.tree.activateNode("Root-"+nomprojet);
                            defineArbreEvents();
                            App.currentVoletElement = "Root/"+nomprojet;
                    }
                }
            });    
        };
                
    });
    
    $('#listuser .closebtn').click(function(e){
         $('#listuser').modal('hide');
    });
    
    $.ajax({ 
        url      : "/Liber8/getUsers",
        dataType : "json",
        success  : function(data) { 
            var nameusers = data.response;
            availableTags = JSON.parse(nameusers);
            $( "#usersname" ).autocomplete({
                source: availableTags
            });

            var max_input     = 10; //le nombre maximun des input
            var adduser       = $("#adduser"); // div contenant tout les utilisateurs
            var add_button      = $("#adduserbouton"); //Ajouter bouton id

            var arrname = [];

            $('.addbouton').click(function(e) {
            $('#adduser .usernew .control-group label').each(function() { 
            arrname.push(this.innerHTML); });
               });

            numberinput=0;
            $(add_button).click(function(e){ //on add input button click
                e.preventDefault();
                $('#listuser').css("display","block");
                if(numberinput < max_input){ //max input box allowed
                    numberinput++; //text box increment
                    var getname=$('#usersname').val();
                    if (jQuery.inArray(getname,availableTags) === -1  || getname ==="") {
                              $('#listuser .modal-body .alert-error').css('display','block');
                                                  $('#listuser .modal-body .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom est incorrect ! Réessayer </span>');
                    } else {
                        $('#listuser .modal-body .alert-error').css('display','none');
                        $('#listuser').modal('hide');
                        $('#usersname').val("");
                        $(".usernew").append('<li><div class="control-group">\n\
                            <label class="control-label userline" for="selectError'+numberinput+'">'+getname+'</label>\n\
                            <div class="controls userline"><select id="selectError'+numberinput+'" data-rel="chosen" name="droit'+numberinput+'" class="droit"><option value="admin">admin</option>\n\
                            <option value="reporteur">reporteur</option><option class="developpeur">developpeur</option></select><a class="remove_field"><input type="hidden" value="'+getname+'" class="utilisateur'+numberinput+'" name="utilisateur'+numberinput+'"><i class="icon-trash"></i></a></div></div>'); //add input box
                        for(var i = availableTags.length - 1; i >= 0; i--) {
                            if(availableTags[i] === getname) {
                               availableTags.splice(i, 1);
                            }
                        }
                    }

                }


            });

            $(adduser).on("click",".remove_field", function(e){ //user click on remove text
                e.preventDefault(); 
                var valeursupprimer = $(this).parents('li').find('input[type=hidden]').val();
                $(this).parents('li').remove(); 
                availableTags.push(valeursupprimer );
                numberinput--;
            });
        }
    });
});
    
   
    