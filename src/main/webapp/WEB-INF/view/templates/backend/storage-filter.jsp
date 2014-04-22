<%--
    Опции фильтрации и сортировки единиц хранения.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="clearfix" style="margin-bottom: 20px;">
    <div class="pull-right">
        <div class="btn-group btn-xs">наличие:</div>
        <div class="btn-group">
            <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
                ${availableOptions[currentAvailable]}
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu dropdown-menu-right">
                <c:forEach var="available" items="${availableOptions}">
                    <c:url var="paramed_url" value="">
                        <c:forEach items="${param}" var="entry">
                            <c:if test="${(entry.key != 'available') && (entry.key != 'page')}">
                                <c:param name="${entry.key}" value="${entry.value}" />
                            </c:if>
                        </c:forEach>
                        <c:param name="page" value="1" />
                        <c:param name="available" value="${available.key}" />
                    </c:url>
                    <li><a href="${paramed_url}">${available.value}</a></li>
                </c:forEach>
            </ul>
        </div>
        &nbsp;
        <div class="btn-group btn-xs">упорядочить:</div>
        <div class="btn-group">
            <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
                ${sortOptions[currentSort]}&nbsp;
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu dropdown-menu-right">
                <c:forEach var="sort" items="${sortOptions}">
                    <c:url var="paramed_url" value="">
                        <c:forEach items="${param}" var="entry">
                            <c:if test="${(entry.key != 'sort') && (entry.key != 'page')}">
                                <c:param name="${entry.key}" value="${entry.value}" />
                            </c:if>
                        </c:forEach>
                        <c:param name="page" value="1" />
                        <c:param name="sort" value="${sort.key}" />
                    </c:url>
                    <li><a href="${paramed_url}">${sort.value}</a></li>
                </c:forEach>
            </ul>
        </div>
        <div class="btn-group">
            <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
                ${directOptions[currentDirection]}&nbsp;
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu dropdown-menu-right">
                <c:forEach var="direct" items="${directOptions}">
                    <c:url var="paramed_url" value="">
                        <c:forEach items="${param}" var="entry">
                            <c:if test="${(entry.key != 'direct') && (entry.key != 'page')}">
                                <c:param name="${entry.key}" value="${entry.value}" />
                            </c:if>
                        </c:forEach>
                        <c:param name="page" value="1" />
                        <c:param name="direct" value="${direct.key}" />
                    </c:url>
                    <li><a href="${paramed_url}">${direct.value}</a></li>
                </c:forEach>
            </ul>
        </div>
        &nbsp;
        <div class="btn-group btn-xs">на странице:</div>
        <div class="btn-group">
            <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
                ${pageSizeOptions[currentPageSize]}&nbsp;
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu dropdown-menu-right">
                <c:forEach var="size" items="${pageSizeOptions}">
                    <c:url var="paramed_url" value="">
                        <c:forEach items="${param}" var="entry">
                            <c:if test="${(entry.key != 'size') && (entry.key != 'page')}">
                                <c:param name="${entry.key}" value="${entry.value}" />
                            </c:if>
                        </c:forEach>
                        <c:param name="page" value="1" />
                        <c:param name="size" value="${size.key}" />
                    </c:url>
                    <li style="width:auto;"><a href="${paramed_url}">${size.value}</a></li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
