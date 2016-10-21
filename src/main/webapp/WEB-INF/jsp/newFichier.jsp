<%-- 
    Document   : newFichier
    Created on : Oct 21, 2016, 10:00:04 AM
    Author     : Luc Di Sanza
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nouveau Fichier</title>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row-fluid">
                <h2> Nouveau Fichier </h2>
                <!--
                <form action = "newFichier" method="post">
                    <label>
                        Nom du Fichier
                    </label>
                    <input name="nomFichier" id="nomFichier" type="text" placeholder=" Nom du Fichier"/>             

                    <button type="submit">Créer le Fichier</button>
                </form>
                -->
                
                <h1>Faux formulaire à remplacer après par des opérations JS:</h1>
                
                <form action = "newFichier" method="post">     
                    <label>
                        pathFichier
                    </label>
                    <input name="pathFichier" id="pathFichier" type="text" placeholder=" path du projet"/> 
                    <button type="submit">Créer le Fichier</button>
                </form>
            </div>
        </div>
    </body>
</html>
