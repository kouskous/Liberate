<!DOCTYPE html>
<html lang="en">
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
        <link rel="stylesheet" href="/Liber8/resources/highlight/styles/darcula.css">
        <script src="/Liber8/resources/highlight/highlight.pack.js"></script>
        <script>hljs.initHighlightingOnLoad();</script>
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
			<a class="brand" href="#"><span># L!BER8</span></a>
								
				<!-- start: Header Menu -->
				<div class="nav-no-collapse header-nav">
                                    <ul class="nav pull-left menu-options">
                                        <li class="btn_action">
                                            <a class="user-action" href="#" data-toggle="modal" data-target="#projet" data-url=""><i class="halflings-icon white plus-sign"></i>Projet </a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a class="user-action" href="#" data-toggle="modal" data-target="#projet" data-url=""><i class="halflings-icon white plus-sign"></i>Dossier </a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a class="user-action" href="#" data-toggle="modal" data-target="#projet" data-url="newFile"><i class="halflings-icon white plus-sign"></i>Fichier </a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a href="#"><i class="icon-signin"></i>Importer</a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a href="#"><i class="icon-signout"></i>Exporter</a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a href="#"><i class="icon-upload-alt"></i>Push</a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a href="#"><i class="icon-save"></i>Enregistrer</a>
                                        </li>
                                        
                                        <li class="btn_action">
                                            <a href="#"><i class="icon-play"></i>Compiler</a>
                                        </li>

                                        <li class="dropdown hidden-phone">
                                                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                                                    <span><i class="halflings-icon cog white"></i>Options </span><span class="caret"></span>
                                                </a>
                                                <ul class="dropdown-menu notifications">	
                                                    <li>
                                                        <a href="#">
                                                            <span>
                                                                <a href="#">Gestion Utilisateurs</a>
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
                            <pre id="editeur"><code  contenteditable="">var x = 5;</code></pre>
                            <div id="lignes" displayLignes ></div>
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
                
                <script src="/Liber8/resources/liber8/js/principal.js"></script>
                 
                
	<!-- end: JavaScript-->
<div class="modal fade" id="projet" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div id="modal_content">
      </div>
    </div>
  </div>
</div>	
</body>
</html>
