<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="container-fluid">
    <div id="working_projects_list" class="box black span4" ontablet="span6" ondesktop="span4">
        <div class="box-header">
                <h2><i class="halflings-icon white list"></i><span class="break"></span>Selectionnez un projet dont vous êtes collaborateur...</h2>
        </div>
        <div class="box-content">
                <ul class="dashboard-list metro">
                    <c:choose>
                        <c:when test="${projets.size() > 0}">
                            <c:forEach items="${projets}" var="projet">
                                <li class="project-to-pull" data-id="${projet.idProjet}" data-name="${projet.nom}">
                                        <a >
                                                <i class="icon-folder-open green"></i>                               
                                                <strong> ${projet.nom} </strong>  
                                        </a>
                                </li>
                            </c:forEach>
                        </c:when>    
                        <c:otherwise>
                            <p> Il n'existe aucun projet dont vous êtes collaborateur</p>
                        </c:otherwise>
                    </c:choose> 


                </ul>
        </div>
    </div>
</div>
<script src="/Liber8/resources/liber8/js/pull.js"></script>       

