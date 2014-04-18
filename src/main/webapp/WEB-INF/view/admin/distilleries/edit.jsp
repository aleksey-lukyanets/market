<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<h1>Редактирование винокурни</h1>

<br>

<s:url value="/admin/distilleries/{distilleryId}" var="edit_action">
    <s:param name="distilleryId" value="${distillery.id}" />
</s:url>
<sf:form id="distilleryForm" class="form-horizontal"
         modelAttribute="distillery" commandName="distillery"
         action="${edit_action}" method="put">
    
    <sf:hidden path="id" name="id" value="${distillery.id}"/>
    
    <tiles:insertAttribute name="form-fields" />
    
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-8">
            <a href="<c:url value="/admin/distilleries" />" class="btn btn-default">
                Вернуться
            </a>
            <button type="submit" class="btn btn-primary">
                Изменить
            </button>
        </div>
    </div>
            
</sf:form>

