<%-- 
    Document   : saveFichier
    Created on : Oct 21, 2016, 5:26:21 PM
    Author     : Luc Di Sanza
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row-fluid">
                <h2> Sauvegarder fichier </h2>
                
                <h1>Faux formulaire à remplacer après par des opérations JS:</h1>
                
                <form action = "saveFichier" method="post">     
                    <label>
                        pathFichier
                    </label>
                    <input name="pathFichier" id="pathFichier" type="text" placeholder=" path du fichier"/> 
                    <label>
                        contenu fichier
                    </label>
                    <input name="contenuFichier" id="contenuFichier" type="text" placeholder=" contenu"/> 
                    <button type="submit">Sauvegarder le fichier</button>
                </form>
            </div>
        </div>
    </body>
</html>
