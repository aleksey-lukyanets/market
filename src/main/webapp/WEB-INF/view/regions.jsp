<%--
    Страница товаров региона.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<h1 class="post-title">Регион ${selectedRegion.name}</h1>

<div class="region-description">
    <div class="pull-right region-image">
        <img src="${pageContext.request.contextPath}${initParam.regionImagePath}${selectedRegion.name}.jpg"
             width="200" class="img-thumbnail" alt="${selectedRegion.name}"/>
    </div>
    ${selectedRegion.description}
</div>

<tiles:insertAttribute name="filter-panel" />

<c:forEach var="product" items="${page.content}" varStatus="iter">
    <div class="row">
        <div class="col-sm-3 col-xs-3 product-unit">
            <%--<object data="${pageContext.request.contextPath}${initParam.productImagePath}${product.distillery.title}/${product.name}.jpg"
                    type="image/jpg" class="img-responsive">
                <img src="${pageContext.request.contextPath}${initParam.productImagePath}default-whisky.jpg"
                     class="img-responsive" alt=""/>
                </object>--%>
            <img src="${pageContext.request.contextPath}${initParam.productImagePath}${product.distillery.title}/${product.name}.jpg"
                 class="img-responsive" width="90%" alt=""/>
        </div>
        <div class="col-sm-9 col-xs-9 product-unit">
            <div class="pull-right price-block">
                <div class="product-price product-label">${product.price} руб.</div>
                <form method="put" id="quantity-form" action="<c:url value="/cart"/>">
                    <input type="hidden" name="productId" value="${product.id}">
                    <input type="hidden" name="quantity" value="1">
                    <c:choose>
                        <c:when test="${product.storage.available}">
                            <c:set var="insideCart" value="false"/>
                            <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="iter">
                                <c:set var="cartProduct" value="${cartItem.product}"/>
                                <c:if test="${cartProduct.id == product.id}">
                                    <c:set var="insideCart" value="true"/>
                                </c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${insideCart == 'false'}">
                                    <button type="button" class="btn btn-primary quantity-button">в корзину</button>
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value="/cart"/>" class="btn btn-warning">в корзине</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-default" disabled="disabled">нет на складе</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
            <div class="product-item product-label">${product.distillery.title} ${product.name}</div>
            <i>${product.alcohol}% об. / ${product.volume} мл.</i>
            <div class="product-description">${product.description}</div>
        </div>
    </div>
</c:forEach>

<tiles:insertAttribute name="pagination" />

<div id="detailsModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Товары региона</h4>
            </div>
            <div class="modal-body">
                <p>Функции:</p>
                <ol>
                    <li>постраничное отображение товаров одного региона;</li>
                    <li>фильтрация и сортировка товаров;</li>
                    <li>добавление товара в корзину.</li>
                </ol>
                <hr/>
                <p>Детали реализации</p>
                <ol class="discharged">
                    <li>Постраничная выборка записей с учётом фильтрации и сортировки выполняется средствами Spring Data JPA.</li>
                    <li>Добавление товара в корзину реализовано асинхронным запросом AJAX и выполняется без обновления страницы.</li>
                    <li>Поскольку таблицы <code>product</code> и <code>region</code> связаны друг с другом через таблицу <code>distilleries</code>, для корректной сортировки полного списка продуктов отдельного региона используется JPA-запрос:
                        <pre>@Query(value = "SELECT p FROM Product p WHERE p.distillery IN "<br>        + "(SELECT d FROM Distillery d WHERE d.region = :region)")</pre>
                    </li>
                </ol>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/form2json.js" type="text/javascript"></script>
<script>
    $('button.quantity-button').click(function() {
        var $this = $(this);
        var form = $this.parents('form:first');
        var url = form.attr('action');
        var jsonData = JSON.stringify(form.serializeObject());
        $.ajax({
            type: "put",
            url: url,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: jsonData,
            success: function(data) {
                var totalItems = data["totalItems"];
                $this.after('<a href="'+url+'" class="btn btn-warning">в корзине</a>');
                $this.remove();
                $('#cart-total-items').empty().html('<span class="badge">'+totalItems+'</span>');
            },
            error: function() {
                alert("Что-то пошло не так.\nПопробуйте добавить товар ещё раз.");
            }
        });
    });
</script>
