<%--
    Страница корзины.
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    .included-true {display: ${cart.deliveryIncluded ? 'anything' : 'none'};}
    .included-false {display: ${(! cart.deliveryIncluded) ? 'anything' : 'none'};}
</style>

<h1 class="post-title">Корзина</h1>

<c:choose>
    <c:when test="${cart.totalItems > 1}">
        <p>В вашей корзине находится ${cart.totalItems} товаров.</p>
    </c:when>
    <c:when test="${cart.totalItems == 1}">
        <p>В вашей корзине находится один товар.</p>
    </c:when>
    <c:otherwise>
        <p>Ваша корзина пуста.</p>
    </c:otherwise>
</c:choose>

<br>

<div >
    <div class="col-md-4" align="center">
        <c:if test="${!empty cart && cart.totalItems != 0}">
            <sf:form method="delete">
                <button type="submit" class="btn btn-default">
                    очистить корзину
                </button>
            </sf:form>
        </c:if>
    </div>
    <div class="col-md-4" align="center">
        <a href="<c:url value='/'/>" class="btn btn-primary">продолжить покупки</a>
    </div>
    <div class="col-md-4" align="center">
        <c:if test="${!empty cart && cart.totalItems != 0}">
            <security:authorize access="isAuthenticated()">
                <a id="next-step" href="<c:url value="${cart.deliveryIncluded ? '/checkout/details' : '/checkout/payment'}" />" class="btn btn-primary">
                    оформить заказ
                </a>
            </security:authorize>
            <security:authorize access="! isAuthenticated()">
                <a href="<c:url value="/login" />" class="btn btn-primary">
                    войти для оформления
                </a>
            </security:authorize>
        </c:if>
    </div>
</div>

<br>
<br>
<br>

<c:if test="${!empty cart && cart.totalItems != 0}">
    <div class="table">
        <table class="table" width="100%">
            <thead>
                <tr>
                    <th></th>
                    <th>товар</th>
                    <th>цена</th>
                    <th>количество</th>
                    <th align="center">общая цена</th>
                </tr>
            </thead>

            <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="iter">
                <c:set var="product" value="${cartItem.product}"/>
                <tr>
                    <td>
                        <img src="${pageContext.request.contextPath}${initParam.productImagePath}${product.distillery.title}/${product.name}.jpg"
                             alt="${product.distillery.title} ${product.name}"
                             width="100">
                    </td>
                    <td>
                        ${product.distillery.title} ${product.name}
                    </td>
                    <td>
                        ${product.price}&nbsp;руб.
                    </td>
                    <td width="125">
                        <sf:form method="put" modelAttribute="cartItem" commandName="cartItem">
                            <input type="hidden" name="productId" value="${product.id}">
                            <div class="input-group input-group-sm">
                                <input type="text" name="quantity" class="form-control"
                                       value="${cartItem.quantity}"
                                       maxlength="2" size="1">
                                <span class="input-group-btn">
                                    <button type="submit" class="btn btn-default">
                                        изменить
                                    </button>
                                </span>
                            </div>
                        </sf:form>
                    </td>
                    <td>
                        ${product.price * cartItem.quantity}&nbsp;руб.
                    </td>
                </tr>
            </c:forEach>
                
            <tr class="included-true">
                <td colspan="4" align="right">подитог:</td>
                <td>${cart.productsCost}&nbsp;руб.</td>
            </tr>
            <tr class="included-true">
                <td colspan="4" align="right">доставка по Петербургу:</td>
                <td>${deliveryCost}&nbsp;руб.</td>
            </tr>
            <tr class="included-true">
                <td colspan="4" align="right"><h4>итог:</h4></td>
                <td><h4>${cart.productsCost + deliveryCost}&nbsp;руб.</h4></td>
            </tr>
            <tr class="included-false">
                <td colspan="4" align="right"><h4>итог:</h4></td>
                <td><h4>${cart.productsCost}&nbsp;руб.</h4></td>
            </tr>
        </table>
    </div>

    <div>
        <div class="col-md-7" style="padding: 0;">
            <div class="panel panel-default">
                <div class="panel-body">
                    <form method="put" action="${pageContext.request.contextPath}/cart/delivery" class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                получение:
                            </label>
                            <div class="col-sm-8">
                                <div class="radio">
                                    <label for="deliveryIncludedTrue">
                                        <input type="radio" name="included" value="true"
                                            <c:if test="${cart.deliveryIncluded}">
                                                checked="checked"
                                            </c:if>>
                                        доставкой по Петербургу
                                    </label>
                                </div>
                                <div class="radio">
                                    <label for="deliveryIncludedFalse">
                                        <input type="radio" name="included" value="false"
                                            <c:if test="${! cart.deliveryIncluded}">
                                                checked="checked"
                                            </c:if>>
                                        самовывозом со склада,<br>ул. Пекарская, д. 221б
                                    </label>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-5" style="padding: 0 0 0 40px;">
            <div class="panel panel-primary included-true">
                <div class="panel-heading"><h3 class="panel-title">Гарантия доставки<br>на следующий день</h3></div>
                <div class="panel-body">Цена курьерской доставки в объёме
                    ${deliveryCost} руб. включена в заказ</div>
            </div>
        </div>
    </div>

</c:if>

<div id="detailsModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Корзина</h4>
            </div>
            <div class="modal-body">
                <p>Функции</p>
                <ol type="1">
                    <li>просмотр списка товаров;</li>
                    <li>изменение количества товаров;</li>
                    <li>очистка корзины;</li>
                    <li>выбор способа доставки.</li>
                </ol>
                <hr/>
                <p>Детали реализации</p>
                <ol type="1" class="discharged">
                    <li>Реализованы два способа хранения корзины: для гостей и для зарегистрированных пользователей. Оба способа используют экземпляры одного класса <code>market.domain.Cart</code>.
                        <ul>
                            <li>Корзина пользователя хранится в базе данных и, после авторизации, доступна ему в любое время с любого устройства.</li>
                            <li>Корзина гостя размещается в данных сессии и сохраняется в течение 30 минут с момента последнего посещения магазина. При регистрации нового пользователя содержимое его гостевой корзины автоматически переносится в корзину пользователя.</li>
                        </ul>
                    </li>
                    <li>Установка способа получения заказа реализована асинхронным запросом AJAX и выполняется без обновления страницы.</li>
                    <li>Следующий этап оформления заказа зависит от выбранного способа получения:
                        <ul>
                            <li>доставка курьером - будут уточнены контактные данные покупателя;</li>
                            <li>самовывоз - будет предложено проверить и оплатить заказ.</li>
                        </ul>
                        Процесс оформлени заказа наглядно изображён на диаграмме в разделе <a href="<c:url value="/inside#ckeckout" />">оформление заказа</a>.
                    </li>
                </ol>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/form2json.js" type="text/javascript"></script>
<script>
    $('body').delegate('input[name=included]:checked', 'change', function() {
        var $this = $(this);
        var form = $this.parents('form:first');
        var valueToSet = $(this).val();
        var jsonData = JSON.stringify(form.serializeObject());
        $.ajax({
            type: form.attr('method'),
            url: form.attr('action') + "/" + valueToSet,
            /*dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: jsonData,*/
            success: function(data) {
                var deliveryIncluded = data["deliveryIncluded"];
                if (deliveryIncluded) {
                    $('#next-step').attr('href', 'checkout/details');
                } else  {
                    $('#next-step').attr('href', 'checkout/payment');
                }
                $('.included-true').each(function() {
                    if (deliveryIncluded) {
                        $(this).show();
                    } else  {
                        $(this).hide();
                    }
                });
                $('.included-false').each(function() {
                    if (deliveryIncluded) {
                        $(this).hide();
                    } else  {
                        $(this).show();
                    }
                });
            },
            error: function() {
                alert("Что-то пошло не так.\nПопробуйте добавить товар ещё раз.");
            }
        });
    });
</script>
