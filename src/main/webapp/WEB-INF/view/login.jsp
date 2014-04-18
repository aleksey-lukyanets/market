<%--
    Страница входа в магазин.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Вход в магазин</h1>

<br>

<c:if test="${'true' eq param.error}">
    <div class="bs-callout bs-callout-danger">
        <h4>Пользователя с таким сочетанием логина и пароля не существует.</h4>
    </div>
</c:if>
<c:if test="${'true' eq param.secured}">
    <div class="bs-callout bs-callout-danger">
        <h4>Для доступа к запрошенной странице у вас недостаточно прав.</h4>
    </div>
</c:if>

<div style="margin-top:70px; margin-bottom:100px;">
    <div>
        <form id="signupForm" class="form-horizontal"
              action="<c:url value='j_spring_security_check'/>" method="post">
            <div class="form-group">
                <label for="email" class="col-sm-4 control-label">
                    электронная почта
                </label>
                <div class="col-sm-5">
                    <input type="text"
                           size="31"
                           maxlength="45"
                           id="email"
                           name="email"
                           class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label for="password" class="col-sm-4 control-label">
                    пароль
                </label>
                <div class="col-sm-5">
                    <input type="password"
                           size="31"
                           maxlength="45"
                           id="password"
                           name="password"
                           class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-5">
                    <div class="checkbox">
                        <label for="_spring_security_remember_me">
                            <input type="checkbox"
                                   id="_spring_security_remember_me"
                                   name="_spring_security_remember_me"> запомнить меня
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-5">
                    <button type="submit" class="btn btn-success">Войти</button>
                    <a href="<c:url value="/customer/new" />" class="btn btn-default">Регистрация</a>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="bs-callout bs-callout-success">
    <h4>Готовая учётная запись</h4>
    <p>Для простоты вы можете воспользоваться подготовленной заранее учётной записью покупателя: логин <b>ivan.petrov@yandex.ru</b>, пароль <b>petrov</b>.</p>
    <p>Если вам интересен процесс регистрации, вы можете создать новую самостоятельно.</p>
</div>
<div class="bs-callout bs-callout-success">
    Учётная запись администратора: логин <b>admin</b>, пароль <b>password</b>.
</div>

<div id="detailsModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Авторизация</h4>
            </div>
            <div class="modal-body">
                <p>Детали реализации</p>
                <ol class="discharged">
                    <li>Авторизация и контроль доступа реализованы средствами Spring Security.</li>
                    <li>После успешной аутентификации пользователь будет перенаправлен на различные страницы, в зависимости от его уровня доступа:
                        <ul>
                            <li>на главную страницу магазина &ndash; для покупателя;</li>
                            <li>на страницу панели управления &ndash; для персонала магазина.</li>
                        </ul>
                    </li>
                </ol>
            </div>
        </div>
    </div>
</div>
