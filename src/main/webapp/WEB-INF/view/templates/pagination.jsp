<%--
    Навигация по страницам для многостраничных списков.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<c:url var="prevUrl" value="">
    <c:forEach items="${param}" var="entry">
        <c:if test="${entry.key != 'page'}">
            <c:param name="${entry.key}" value="${entry.value}" />
        </c:if>
    </c:forEach>
    <c:param name="page" value="${currentIndex - 1}" />
</c:url>

<c:url var="nextUrl" value="">
    <c:forEach items="${param}" var="entry">
        <c:if test="${entry.key != 'page'}">
            <c:param name="${entry.key}" value="${entry.value}" />
        </c:if>
    </c:forEach>
    <c:param name="page" value="${currentIndex + 1}" />
</c:url>

<c:if test="${page.totalPages > 1}">
    <center>
        <div class="btn-toolbar" role="toolbar">
            <ul class="pagination custom-pagination pagination-sm" style="margin-top:0;margin-bottom:0;">
                <li class=""><span>страницы:</span></li>
            <%--    <c:choose>
                    <c:when test="${currentIndex == 1}">
                    <li class="disabled"><a href="#">предыдущая</a></li>
                    </c:when>
                    <c:otherwise>
                    <li><a href="${prevUrl}">предыдущая</a></li>
                    </c:otherwise>
                </c:choose>--%>
                <c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
                    <c:url var="pageUrl" value="">
                        <c:forEach items="${param}" var="entry">
                            <c:if test="${entry.key != 'page'}">
                                <c:param name="${entry.key}" value="${entry.value}" />
                            </c:if>
                        </c:forEach>
                        <c:param name="page" value="${i}" />
                    </c:url>
                    <c:choose>
                        <c:when test="${i == currentIndex}">
                        <li class="active"><span><c:out value="${i}" /></span></li>
                        </c:when>
                        <c:otherwise>
                        <li><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            <%--    <c:choose>
                    <c:when test="${currentIndex == page.totalPages}">
                    <li class="disabled"><a href="#">следующая</a></li>
                    </c:when>
                    <c:otherwise>
                    <li><a href="${nextUrl}">следующая</a></li>
                    </c:otherwise>
                </c:choose>--%>
            </ul>
        </div>
    </center>
</c:if>
