<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<h1>Управление товарами</h1>

<br>

<tiles:insertAttribute name="filter" />

<div class="table-responsive">
    <table class="table table-small-text" width="100%">
        <thead>
            <tr>
                <th>id</th>
                <th>винокурня</th>
                <th width="110">наименование<br>товара</th>
                <th>возр.</th>
                <th>регион</th>

                <th>объём,<br>мл.</th>
                <th>алк.<br>%&nbsp;об.</th>
                <th>цена,<br>руб.</th>
                <th width="160">описание</th>
                <th></th>
            </tr>
        </thead>

        <c:forEach var="product" items="${page.content}">
            <c:set var="shortDesc" value="${fn:substring(product.description, 0, 60)}" /> 
            <tr>
                <td>${product.id}</td>
                <td>${product.distillery.title}</td>
                <td>${product.name}</td>
                <td>${product.age}</td>
                <td>${product.distillery.region.name}</td>

                <td>${product.volume}</td>
                <td>${product.alcohol}</td>
                <td>${product.price}</td>
                <td>
                    ${shortDesc}<c:if test="${fn:length(product.description) >= 60}">...</c:if>
                </td>
                <td>
                    <s:url value="products/{productId}/edit" var="edit_product_url">
                        <s:param name="productId" value="${product.id}" />
                    </s:url>
                    <s:url value="products/{productId}" var="delete_product_url">
                        <s:param name="productId" value="${product.id}" />
                    </s:url>
                    <sf:form action="${delete_product_url}" method="delete">
                        <div class="pull-right">
                            <a href="${edit_product_url}" class="btn btn-xs btn-default">изменить</a>
                            <br>
                            <button type="submit" class="btn btn-xs btn-default">удалить</button>
                        </div>
                    </sf:form>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="10">
                <a href="<s:url value="/admin/products/new" />" class="btn btn-primary btn-sm">
                    добавить товар
                </a>
            </td>
        </tr>
    </table>
</div>

<tiles:insertAttribute name="pagination" />
