<%--
    Опции фильтрации и сортировки заказов.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<div class="clearfix">
	<div class="pull-right">
		<div style="margin-bottom: 5px;">
			<div class="btn-group btn-xs">состояние заказа:</div>
			<div class="btn-group">
				<button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
					<c:out value="${executedOptions[currentExecuted]}"/>
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu-right">
					<c:forEach var="executed" items="${executedOptions}">
						<c:url var="paramed_url" value="">
							<c:forEach items="${param}" var="entry">
								<c:if test="${(entry.key != 'exec') && (entry.key != 'page')}">
									<c:param name="${entry.key}" value="${entry.value}"/>
								</c:if>
							</c:forEach>
							<c:param name="page" value="1"/>
							<c:param name="executed" value="${executed.key}"/>
						</c:url>
						<li><a href="<c:out value="${paramed_url}"/>"><c:out value="${executed.value}"/></a></li>
					</c:forEach>
				</ul>
			</div>
			&nbsp;
			<div class="btn-group btn-xs">создан:</div>
			<div class="btn-group">
				<button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
					<c:out value="${createdOptions[currentCreated]}"/>
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu-right">
					<c:forEach var="created" items="${createdOptions}">
						<c:url var="paramed_url" value="">
							<c:forEach items="${param}" var="entry">
								<c:if test="${(entry.key != 'created') && (entry.key != 'page')}">
									<c:param name="${entry.key}" value="${entry.value}"/>
								</c:if>
							</c:forEach>
							<c:param name="page" value="1"/>
							<c:param name="created" value="${created.key}"/>
						</c:url>
						<li><a href="<c:out value="${paramed_url}"/>"><c:out value="${created.value}"/></a></li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div style="margin-bottom: 20px;">
			<div class="btn-group btn-xs">упорядочить:</div>
			<div class="btn-group">
				<button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
					<c:out value="${sortOptions[currentSort]}"/>&nbsp;
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu-right">
					<c:forEach var="sort" items="${sortOptions}">
						<c:url var="paramed_url" value="">
							<c:forEach items="${param}" var="entry">
								<c:if test="${(entry.key != 'sort') && (entry.key != 'page')}">
									<c:param name="${entry.key}" value="${entry.value}"/>
								</c:if>
							</c:forEach>
							<c:param name="page" value="1"/>
							<c:param name="sort" value="${sort.key}"/>
						</c:url>
						<li><a href="<c:out value="${paramed_url}"/>"><c:out value="${sort.value}"/></a></li>
					</c:forEach>
				</ul>
			</div>
			<div class="btn-group">
				<button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
					<c:out value="${directOptions[currentDirection]}"/>&nbsp;
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu-right">
					<c:forEach var="direct" items="${directOptions}">
						<c:url var="paramed_url" value="">
							<c:forEach items="${param}" var="entry">
								<c:if test="${(entry.key != 'direct') && (entry.key != 'page')}">
									<c:param name="${entry.key}" value="${entry.value}"/>
								</c:if>
							</c:forEach>
							<c:param name="page" value="1"/>
							<c:param name="direct" value="${direct.key}"/>
						</c:url>
						<li><a href="<c:out value="${paramed_url}"/>"><c:out value="${direct.value}"/></a></li>
					</c:forEach>
				</ul>
			</div>
			&nbsp;
			<div class="btn-group btn-xs">на странице:</div>
			<div class="btn-group">
				<button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
					<c:out value="${pageSizeOptions[currentPageSize]}"/>&nbsp;
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu-right">
					<c:forEach var="size" items="${pageSizeOptions}">
						<c:url var="paramed_url" value="">
							<c:forEach items="${param}" var="entry">
								<c:if test="${(entry.key != 'size') && (entry.key != 'page')}">
									<c:param name="${entry.key}" value="${entry.value}"/>
								</c:if>
							</c:forEach>
							<c:param name="page" value="1"/>
							<c:param name="size" value="${size.key}"/>
						</c:url>
						<li style="width:auto;"><a href="<c:out value="${paramed_url}"/>"><c:out value="${size.value}"/></a></li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
</div>
