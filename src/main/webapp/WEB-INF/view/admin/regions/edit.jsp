<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<h1>Редактирование региона</h1>

<br>

<s:url value="/admin/regions/{regionId}" var="edit_action">
    <s:param name="regionId" value="${region.id}" />
</s:url>
<sf:form id="regionForm" class="form-horizontal"
         modelAttribute="region" commandName="region"
         action="${edit_action}" method="put">
    
    <sf:hidden path="id" name="id" value="${region.id}"/>
    
    <tiles:insertAttribute name="form-fields" />
    
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-5">
            <a href="<c:url value="/admin/regions" />" class="btn btn-default">
                Вернуться
            </a>
            <button type="submit" class="btn btn-primary">
                Изменить
            </button>
        </div>
    </div>
            
</sf:form>

