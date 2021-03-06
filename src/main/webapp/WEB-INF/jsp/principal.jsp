<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="fr">
<head>
	
	<!-- start: Meta -->
	<meta charset="utf-8">
	<title># L!BER8</title>
	
	<!-- start: Mobile Specific -->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- end: Mobile Specific -->
	
	<!-- start: CSS -->
        <link id="bootstrap-style" href="/Liber8/resources/metro/css/bootstrap.min.css" rel="stylesheet">
	<link href="/Liber8/resources/metro/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link id="base-style" href="/Liber8/resources/metro/css/style.css" rel="stylesheet">
	<link id="base-style-responsive" href="/Liber8/resources/metro/css/style-responsive.css" rel="stylesheet">
        <link id="base-style-responsive" href="/Liber8/resources/liber8/css/principal.css" rel="stylesheet">
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&subset=latin,cyrillic-ext,latin-ext' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="/Liber8/resources/EasyTree/skin-win8/ui.easytree.css">
        <link id="base-style-responsive" href="/Liber8/resources/liber8/css/newProjet.css" rel="stylesheet">
        <link id="base-style-responsive" href="/Liber8/resources/liber8/css/pull.css" rel="stylesheet">
        <link rel="stylesheet" href="/Liber8/resources/Toastr/nuget/content/content/toastr.css"/>
        <!-- end: CSS -->
	

	<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
	  	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<link id="ie-style" href="resources/metro/css/ie.css" rel="stylesheet">
	<![endif]-->
	
	<!--[if IE 9]>
		<link id="ie9style" href="resources/metro/css/ie9.css" rel="stylesheet">
	<![endif]-->
		
	<!-- start: Favicon -->
	<link rel="shortcut icon" href="/Liber8/resources/metro/img/favicon.ico">
	<!-- end: Favicon -->
	
</head>

<body>
		<!-- start: Header -->
	<div class="navbar">
		<div class="navbar-inner">
			<a class="brand" href="#"><span># L!BER8 </span></a>
								
				<!-- start: Header Menu -->
				<div class="nav-no-collapse header-nav">
                                    <ul class="nav pull-left menu-options">
                                        <li class="btn_action">
                                            <a class="user-action" href="#" data-toggle="modal" data-target="#projet" data-url="newProjet"><i class="halflings-icon white plus-sign"></i>Projet </a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a class="user-action" href="#" data-toggle="modal" data-target="#projet" data-url="pull"><i class="icon-download-alt"></i>Pull</a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a id="btn_push" href="#"><i class="icon-upload-alt"></i>Push</a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a id="btn_compile" href="#"><i class="icon-play"></i>Compiler</a>
                                        </li>

                                        <li class="dropdown hidden-phone">
                                                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                                                    <span><i class="halflings-icon cog white"></i>Options </span><span class="caret"></span>
                                                </a>
                                                <ul class="dropdown-menu notifications">	
                                                    <li>
                                                        <a href="#">
                                                            <span>
                                                                <a  class="user-action" href="#" data-toggle="modal" data-target="#projet" data-url="gestionsUsers">Gestion Utilisateurs</a>
                                                            </span> 
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="#">
                                                            <span>
                                                                <a href="#">Gestion Demandes</a>
                                                            </span> 
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="#">
                                                            <span>
                                                                <a href="#">Wiki</a>
                                                            </span> 
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="#">
                                                            <span>
                                                                <a href="#">Javadoc</a>
                                                            </span> 
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="#">
                                                            <span>
                                                                <a href="#">TODO</a>
                                                            </span> 
                                                        </a>
                                                    </li>
                                                </ul>
                                        </li>

                                        <li class="dropdown">
                                                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                                                            <i class="halflings-icon white user"></i> ${sessionScope.user.pseudo}
                                                            <span class="caret"></span>
                                                    </a>
                                                    <ul class="dropdown-menu">
                                                            <li><a href="#"><i class="halflings-icon user"></i> Profile</a></li>
                                                            <li><a href="/Liber8/logout"><i class="halflings-icon off"></i> Déconnexion</a></li>
                                                    </ul>
                                        </li>
                                    </ul>
                                </div>
                    </div>
        </div>    
            
	<!-- start: Header -->
	
		<div class="container-fluid-full heightfull">
                    <div class="row-fluid">
				
			<!-- start: Main Menu -->
			<div id="sidebar-left" class="span2 ui-widget-content">
				<div class="nav-collapse sidebar-nav overscrollfolder">
                                    <div id="toolbar">
                                        
                                        <button id="btnNewFolder" class="user-action" data-toggle="modal" data-target="#projet" data-url="newDossier"><i class="icon-folder-open"></i></button>
                                        <button id="btnNewFile" class="user-action" data-toggle="modal" data-target="#projet" data-url="newFile"><i class="icon-file"></i></button>
                                        <button id="btn_rename" class="user-action" data-toggle="modal" data-target="#projet" data-url="renameFile"><i class="icon-edit"></i></button>
                                        <button id="btn_sauvegarder"><i class="icon-save"></i></button>
                                        <button id="btn_supprimer"><i class="icon-trash"></i></button>
                                        <button id="btn_verrouiller"><i class="icon-lock"></i></button>
                                        <button id="btn_deverrouiller"><i class="icon-unlock"></i></button>
                                    </div>    
                                    <div id="arbre">
                                        <ul id ="Root">

                                        </ul>
                                    </div>
				</div>

			</div>
			<!-- end: Main Menu -->
                        
                        <!-- start: Content -->	
                        <div id="content" >
                            <div id="menu">
                                <ul id="onglets">
                                </ul>
                            </div>

                            <div id="editeur"></div>
                        </div>
                        <div class="console">
                            <div class="title_console">
                                <span>Console</span>
                            </div>
                            <a  href='#' class='closeconsole'><i class='halflings-icon remove'></i></a>
                            <div class="body_console"></div>
                        </div>
                    </div>
		</div>
	
	<div class="clearfix"></div>
        
	
	<footer class="principal-footer">

		<p>
			<span style="text-align:left;float:left">&copy; 2016 Liberate technology</span>
			
		</p>

	</footer>
	
	<!-- start: JavaScript-->

                
		<script src="/Liber8/resources/metro/js/jquery-1.9.1.min.js"></script>
                
                <script src="/Liber8/resources/metro/js/jquery-migrate-1.0.0.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/modernizr.js"></script>
	
		<script src="/Liber8/resources/metro/js/bootstrap.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.cookie.js"></script>
	
		<script src='/Liber8/resources/metro/js/fullcalendar.min.js'></script>
	
		<script src='/Liber8/resources/metro/js/jquery.dataTables.min.js'></script>

		<script src="/Liber8/resources/metro/js/excanvas.js"></script>
                
                <script src="/Liber8/resources/metro/js/jquery.flot.js"></script>
                
                <script src="/Liber8/resources/metro/js/jquery.flot.pie.js"></script>
                
                <script src="/Liber8/resources/metro/js/jquery.flot.stack.js"></script>
                
                <script src="/Liber8/resources/metro/js/jquery.flot.resize.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.chosen.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.uniform.min.js"></script>
		
		<script src="/Liber8/resources/metro/js/jquery.cleditor.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.noty.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.elfinder.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.raty.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.iphone.toggle.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.uploadify-3.1.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.gritter.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.imagesloaded.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.masonry.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.knob.modified.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.sparkline.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/counter.js"></script>
	
		<script src="/Liber8/resources/metro/js/retina.js"></script>

		<script src="/Liber8/resources/metro/js/custom.js"></script>
                
                <script src="/Liber8/resources/metro/js/jquery-ui-1.10.0.custom.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.ui.touch-punch.js"></script>
                
                <script src="/Liber8/resources/EasyTree/jquery.easytree.js"></script>  
                
                <script src="/Liber8/resources/blockUi/jquery.blockUI.js"></script>
                
                <script src="/Liber8/resources/Toastr/toastr.js"></script>
                
                <script src="/Liber8/resources/liber8/js/principal.js"></script>

                <script src="/Liber8/resources/Ace/src/ace.js" type="text/javascript" charset="utf-8"></script>
                 
                
	<!-- end: JavaScript-->
<div class="modal fade" id="projet" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button id="close_modal_btn" type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div id="modal_content">
      </div>
    </div>
  </div>
</div>	
</body>
</html>
