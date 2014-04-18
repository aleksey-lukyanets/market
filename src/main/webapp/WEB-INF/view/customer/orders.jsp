<%--
    Страница истории заказов.
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<h1>История заказов</h1>

<div class="panel-group" id="accordion">
    <c:forEach var="order" items="${userOrders}" varStatus="iter">

        <div class="panel panel-default">
            <div class="panel-heading">
                <table width="100%">
                    <tr>
                        <td valign="bottom">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapse${iter.index}">
                                    Заказ от ${order.dateCreated}
                                </a>
                            </h4>
                        </td>
                        <td align="right" valign="bottom">
                            номер счёта: ${order.bill.number}
                        </td>
                    </tr>
                </table>
            </div>
            <div class="panel-collapse collapse" id="collapse${iter.index}">
                <table class="table">

                    <thead>
                        <tr>
                            <th>наименование</th>
                            <th>цена за ед.</th>
                            <th width="70">кол-во</th>
                            <th width="90">цена</th>
                        </tr>
                    </thead>

                    <c:forEach var="orderedProduct" items="${orderedProductsMap[order.id]}">
                        <tr>
                            <td>${orderedProduct.product.distillery.title} ${orderedProduct.product.name}</td>
                            <td>${orderedProduct.product.price} руб.</td>
                            <td>${orderedProduct.quantity}</td>
                            <td>${orderedProduct.quantity * orderedProduct.product.price} руб.</td>
                        </tr>
                    </c:forEach>

                    <c:if test="${order.deliveryIncluded}">
                        <tr>
                            <td colspan="3" align="right">подитог:</td>
                            <td>${order.productsCost} руб.</td>
                        </tr>
                        <tr>
                            <td colspan="3" align="right">доставка по Петербургу:</td>
                            <td>${order.deliveryСost} руб.</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td colspan="3" align="right">итог:</td>
                        <td>${order.productsCost + order.deliveryСost} руб.</td>
                    </tr>

                </table>
            </div>
        </div>
    </c:forEach>
</div>

<div id="detailsModal" class="modal fade bs-modal-sm" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog modal-sm" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">История заказов</h4>
            </div>
            <div class="modal-body">
                <p>Здесь перечислены все заказы, сделанные покупателем.</p>
            </div>
        </div>
    </div>
</div>
