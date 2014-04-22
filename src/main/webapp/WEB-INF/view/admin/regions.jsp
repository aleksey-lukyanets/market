<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<h1>Управление регионами</h1>

<br>

<div>
    <table class="table table-small-text" width="100%">
        <thead>
            <tr>
                <th>id</th>
                <th>название</th>
                <th>подзаголовок</th>
                <th>описание</th>
                <th>цвет</th>
                <th></th>
            </tr>
        </thead>

        <c:forEach var="region" items="${regions}">
            <c:set var="shortDesc" value="${fn:substring(region.description, 0, 120)}" /> 
            <tr>
                <td>${region.id}</td>
                <td>${region.name}</td>
                <td>${region.subtitle}</td>
                <td style="max-width:200px;">
                    ${shortDesc}<c:if test="${fn:length(region.description) >= 120}">...</c:if>
                </td>
                <td>${region.color}</td>
                <td width="150">
                    <s:url value="regions/{regionId}/edit" var="edit_region_url">
                        <s:param name="regionId" value="${region.id}" />
                    </s:url>
                    <s:url value="regions/{regionId}" var="delete_region_url">
                        <s:param name="regionId" value="${region.id}" />
                    </s:url>
                    <sf:form action="${delete_region_url}" method="delete">
                        <div class="btn-group btn-group-xs pull-right">
                            <a href="${edit_region_url}" class="btn btn-xs btn-default">изменить</a>
                            <button type="submit" class="btn btn-xs btn-default">удалить</button>
                        </div>
                    </sf:form>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="6">
                <a href="<s:url value="regions/new" />" class="btn btn-primary btn-sm">
                    добавить регион
                </a>
            </td>
        </tr>
    </table>
</div>


