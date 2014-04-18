<%--
    Главное меню внешнего интерфейса.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="header-regions">
    <div class="region-item region-item-title">
        регион происхождения:
    </div>
</div>
<div class="header-regions">
    <c:forEach var="region" items="${regions}">
        <c:choose>
            <c:when test="${region.id == selectedRegion.id}">
                <div class="region-item region-item-active">
                    <span class="regionText">${region.name}</span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="region-item border-${region.color} scheme-grey">
                    <a class="regionText" href="<c:url value='/regions/${region.id}'/>">
                        ${region.name}
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>
