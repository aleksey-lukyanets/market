<%--
    Страница оплаты (шаг 2 оформления заказа).
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-validate.min.js" type="text/javascript"></script>

<h1>Проверка и оплата</h1>

<p>Пожалуйста, проверьте ещё раз позиции и способ получения вашего заказа.<br>
    Оплатить заказ вы можете банковской картой.</p>

<c:if test="${!empty orderFailureFlag}">
    <p class="error">Мы не смогли обработать ваш заказ. Пожалуйста, попробуйте оформить его ещё раз.</p>
</c:if>

<br>

<div class="clearfix">
    <div class="col-md-8" style="padding: 0;">
        <div class="panel panel-default" style="margin-right: 20px;">
            <div class="panel-heading">Позиции заказа</div>
            <table class="table table-small-text" width="100%">
                <thead>
                    <tr>
                        <th>наименование</th>
                        <th>цена за ед.</th>
                        <th>кол-во</th>
                        <th>цена</th>
                    </tr>
                </thead>
                <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="iter">
                    <c:set var="product" value="${cartItem.product}"/>
                    <tr>
                        <td>
                            ${product.distillery.title} ${product.name}
                        </td>
                        <td>
                            ${product.price} руб.
                        </td>
                        <td>
                            ${cartItem.quantity}
                        </td>
                        <td>
                            ${product.price * cartItem.quantity} руб.
                        </td>
                    </tr>
                </c:forEach>
                <c:choose>
                    <c:when test="${cart.deliveryIncluded}">
                        <tr>
                            <td colspan="3" align="right"><b>подитог:</b></td>
                            <td><b>${cart.productsCost} руб.</b></td>
                        </tr>
                        <tr style="background-color: white;">
                            <td colspan="3" align="right">доставка:</td>
                            <td>${deliveryCost} руб.</td>
                        </tr>
                        <tr>
                            <td colspan="3" align="right"><h5>сумма к оплате:</h5></td>
                            <td><h5>${cart.productsCost + deliveryCost}&nbsp;руб.</h5></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="3" align="right"><h5>сумма к оплате:</h5></td>
                            <td><h5>${cart.productsCost}&nbsp;руб.</h5></td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </div>
    </div>
    <div class="col-md-4" style="padding: 0;">
        <div class="panel panel-default">
            <div class="panel-heading">Способ получения</div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${cart.deliveryIncluded}">
                        <p>доставка курьером</p>
                        <p>${userAccount.name}<br>
                            ${contacts.phone}<br>
                            ${contacts.address}<br>
                            г. Санкт-Петербург</p>
                    </c:when>
                    <c:otherwise>
                        самовывоз со склада,<br>
                        ул. Пекарская, д. 221б
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<br>

<sf:form id="paymentForm" class="form-horizontal"
         modelAttribute="creditCard" commandName="creditCard" method="post">
    <div class="form-group">
        <label for="creditcard" class="col-sm-4 control-label">
            номер банковской карты
        </label>
        <div class="col-sm-5 has-feedback">
            <sf:input path="number" id="creditcard" name="creditCard"
                size="19" maxlength="19" class="form-control"
                data-validate="creditcard" data-description="creditcard" data-describedby="creditcard-desc"
                placeholder="0000 0000 0000 0000"/>
            <span id="creditcard" class="glyphicon form-control-feedback"></span>
            <div id="creditcard-desc"></div>
            <sf:errors path="number" cssClass="alert alert-danger" element="div"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-4">
            <a href="<c:url value="/cart" />" class="btn btn-default pull-right" style="margin-right: -25px;">
                вернуться к корзине
            </a>
        </div>
        <div class="col-sm-5">
            <button type="submit" class="btn btn-primary">
                оплатить
            </button>
        </div>
    </div>
</sf:form>

<script type="text/javascript">
    $('form').validate({
        onKeyup: true,
        onChange: true,
        submitHandler: function(form) {
            form.submit();
        },
        eachValidField: function() {
            var input = $(this).attr('id');
            $(this).closest('div.form-group').removeClass('has-error').addClass('has-success');
            $('span#' + input).removeClass('glyphicon-remove').addClass('glyphicon-ok');
        },
        eachInvalidField: function() {
            var input = $(this).attr('id');
            $(this).closest('div.form-group').removeClass('has-success').addClass('has-error');
            $('span#' + input).removeClass('glyphicon-ok').addClass('glyphicon-remove');
        },
        description: {
            creditcard: {
                required: '<div class="alert alert-danger">Обязательное поле.</div>',
                pattern: '<div class="alert alert-danger">Номер карты должен состоять из 13-16 цифр.</div>'
            }
        }
    });
    $.validateExtend({
        creditcard: {
            required: true,
            pattern: /\b(?:\d[ -]*?){13,16}\b/
        }
    });
</script>

<div id="detailsModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Подтверждение и оплата</h4>
            </div>
            <div class="modal-body">
                <p>Функции:</p>
                <ol type="1">
                    <li>предоставление покупателю информации о текущем заказе;</li>
                    <li>получение номера банковской карты.</li>
                </ol>
                <hr/>
                <p>Детали реализации</p>
                <ol type="1" class="discharged list-unstyled">
                    <li>Проверка данных формы выполняется дважды: на стороне пользователя и на стороне сервера. Подробности в разделе <a href="<c:url value="/inside#forms" />">проверка форм</a>.</li>
                </ol>
            </div>
        </div>
    </div>
</div>
