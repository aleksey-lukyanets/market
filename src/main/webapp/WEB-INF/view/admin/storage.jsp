<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<h1>Наличие товаров на складе</h1>

<br>

<tiles:insertAttribute name="filter" />

<s:url value="/admin/storage" var="post_store_url" />
<sf:form method="post" action="${post_store_url}">
    <table class="table table-small-text" width="100%">
        <thead>
            <tr>
                <th>id</th>
                <th>название</th>
                <th>объём,&nbsp;мл</th>
                <th>алк.,&nbsp;%&nbsp;об.</th>

                <th>цена,&nbsp;руб.</th>
                <th>в&nbsp;наличии</th>
            </tr>
        </thead>

        <c:forEach var="storedUnit" items="${page.content}" varStatus="status">
            <tr>
                <td>${storedUnit.product.id}</td>
                <td>${storedUnit.product.distillery.title} ${storedUnit.product.name}</td>
                <td>${storedUnit.product.volume}</td>
                <td>${storedUnit.product.alcohol}</td>

                <td>${storedUnit.product.price}</td>
                <td>
                    <input type="hidden" id="productIds" name="productIds" value="${storedUnit.product.id}" />
                    <input type="checkbox"
                           name="actualIds"
                           <c:if test="${storedUnit.available}">checked</c:if>
                           value="${storedUnit.product.id}" />
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="6">
                <button type="submit" class="btn btn-primary pull-right">Сохранить</button>
            </td>
        </tr>
    </table>
</sf:form>

<tiles:insertAttribute name="pagination" />
