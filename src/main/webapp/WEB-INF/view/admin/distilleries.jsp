<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<h1>Управление винокурнями</h1>

<br>

<div class="table-responsive">
    <table class="table table-small-text" width="100%">
        <thead>
            <tr>
                <th>id</th>
                <th>название</th>
                <th>регион</th>
                <th>описание</th>
                <th width="150"></th>
            </tr>
        </thead>

        <c:forEach var="distillery" items="${distilleries}">
            <c:set var="shortDesc" value="${fn:substring(distillery.description, 0, 100)}" /> 
            <tr>
                <td>${distillery.id}</td>
                <td>${distillery.title}</td>
                <td>${distillery.region.name}</td>
                <td>
                    ${shortDesc}
                    <c:if test="${fn:length(distillery.description) >= 100}">...</c:if>
                </td>
                <td>
                    <s:url value="distilleries/{distilleryId}/edit" var="edit_distillery_url">
                        <s:param name="distilleryId" value="${distillery.id}" />
                    </s:url>
                    <s:url value="distilleries/{distilleryId}" var="delete_distillery_url">
                        <s:param name="distilleryId" value="${distillery.id}" />
                    </s:url>
                    <sf:form action="${delete_distillery_url}" method="delete">
                        <div class="btn-group btn-group-xs pull-right">
                            <a href="${edit_distillery_url}" class="btn btn-default">изменить</a>
                            <button type="submit" class="btn btn-default">удалить</button>
                        </div>
                    </sf:form>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="5">
                <a href="<s:url value="distilleries/new" />" class="btn btn-primary btn-sm">
                    добавить винокурню
                </a>
            </td>
        </tr>
    </table>
</div>


