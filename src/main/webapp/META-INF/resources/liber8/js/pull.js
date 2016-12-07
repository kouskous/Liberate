/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    $(".project-to-pull").click(function(){
        var appName = $(this).data("name");
        $.blockUI({ message: '<h2><img src="/Liber8/resources/blockUi/busy.gif" /> pull...</h2>' });
        $.ajax({ 
            url      : "/Liber8/pullProjet",
            type     : 'POST',
            dataType : "json",
            data     :{
                          projet: appName
                      },
            success  : function(data) {
                        $.unblockUI();
                        if(data.response===true){
                            toastr.success("pull réalisé avec succés");
                            refreshTree();
                            $("#close_modal_btn").trigger("click");
                            $(".onglet").trigger("click");
                            App.tree.activateNode("Root-"+appName);
                            App.currentVoletElement = "Root/"+appName;
                        } else {
                            toastr.warning(data.errors);
                        }
                    }       
        });
    });
