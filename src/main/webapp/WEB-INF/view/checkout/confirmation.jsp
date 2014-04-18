<%--
    Страница подтверждения приёма заказа (шаг 3 оформления заказа).
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<h1>Заказ подтверждён</h1>

<div class="clearfix">
    <div>
        <div class="col-md-7" style="padding-right: 20px;">
            <c:choose>
                <c:when test="${createdOrder.deliveryIncluded}">
                    <p><b>Ваш заказ будет скомплектован и доставлен в течение 24 часов.</b></p>
                </c:when>
                <c:otherwise>
                    <p><b>Ваш заказ будет скомплектован и доступен для получения в течение часа.</b></p>
                </c:otherwise>
            </c:choose>
            <p>Номер оплаченного счёта: <strong>${createdOrder.billNumber}</strong>.</p>
            <p>Благодарим вас за заказ в нашем магазине!</p>
            <br>
            <p>Просмотреть все сделанные заказы вы можете в разделе
                <a href="<c:url value="/customer/orders"/>">история заказов</a>.</p>
        </div>
        <div class="col-md-5">
            <div class="panel panel-default">
                <div class="panel-heading">Получатель</div>
                <div class="panel-body">
                    ${userAccount.name}
                    <br>
                    <c:if test="${createdOrder.deliveryIncluded}">
                        г. Санкт-Петербург<br>
                        ${userDetails.address}
                        <hr/>
                    </c:if>
                    ${userDetails.phone}
                    <br>
                    ${userAccount.email}
                </div>
            </div>
        </div>
    </div>
</div>

<br>

<center>
    <a href="<c:url value="/" />" class="btn btn-primary">
        вернуться в магазин
    </a>
</center>
        
<br>

<div id="detailsModal" class="modal fade bs-modal-sm" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog modal-sm" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Благодарность покупателю</h4>
            </div>
            <div class="modal-body">
                <p>Финальный этап оформления заказа.</p>
                <p>Вся информация о заказе была сохранена в базе данных, теперь она доступна покупателю в истории заказов и администрации магазина &ndash; в панели управления.</p>
            </div>
        </div>
    </div>
</div>