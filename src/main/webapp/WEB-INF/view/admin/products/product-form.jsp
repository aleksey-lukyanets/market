<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-validate.min.js" type="text/javascript"></script>

<div class="form-group">
    <label for="distillery" class="col-sm-3 control-label">
        винокурня
    </label>
    <div class="col-sm-5">
        <div class="btn-group">
            <sf:select multiple="single"
                path="distillery.id" class="form-control"
                name="distillery" id="distillery">
                <sf:options items="${distilleries}" itemValue="id" itemLabel="title" />
            </sf:select>
        </div>
    </div>
</div>

<div class="form-group">
    <label for="name" class="col-sm-3 control-label">
        название товара
    </label>
    <div class="col-sm-3 has-feedback">
        <sf:input
            path="name" size="19" maxlength="20" class="form-control"
            name="name" id="name" value="${product.name}"
            data-validate="name" data-description="name" data-describedby="name-desc"/>
        <span id="name" class="glyphicon form-control-feedback"></span>
        <span class="help-block">обязательное поле</span>
        <div id="name-desc"></div>
        <sf:errors path="name" cssClass="alert alert-danger" element="div"/>
    </div>
    
    <label for="age" class="col-sm-2 control-label">
        возраст
    </label>
    <div class="col-sm-3 has-feedback">
        <sf:input
            path="age" size="19" maxlength="10" class="form-control"
            name="age" id="age" value="${product.age}"
            data-validate="age" data-description="age" data-describedby="age-desc"/>
        <span id="age" class="glyphicon form-control-feedback"></span>
        <span class="help-block">обязательное поле<br>ноль &mdash; возраст не заявлен</span>
        <div id="age-desc"></div>
        <sf:errors path="age" cssClass="alert alert-danger" element="div"/>
    </div>
</div>

<div class="form-group">
    <label for="alcohol" class="col-sm-3 control-label">
        сод. алкоголя, % об.
    </label>
    <div class="col-sm-3 has-feedback">
        <sf:input
            path="alcohol" size="19" maxlength="10" class="form-control"
            name="alcohol" id="alcohol" value="${product.alcohol}" placeholder="40,0"
            data-validate="alcohol" data-description="alcohol" data-describedby="alcohol-desc"/>
        <span id="alcohol" class="glyphicon form-control-feedback"></span>
        <span class="help-block">обязательное поле</span>
        <div id="alcohol-desc"></div>
        <sf:errors path="alcohol" cssClass="alert alert-danger" element="div"/>
    </div>
    
    <label for="volume" class="col-sm-2 control-label">
        объём, мл.
    </label>
    <div class="col-sm-3 has-feedback">
        <sf:input
            path="volume" size="19" maxlength="10" class="form-control"
            name="volume" id="volume" value="${product.volume}"
            data-validate="volume" data-description="volume" data-describedby="volume-desc"/>
        <span id="volume" class="glyphicon form-control-feedback"></span>
        <span class="help-block">обязательное поле</span>
        <div id="volume-desc"></div>
        <sf:errors path="volume" cssClass="alert alert-danger" element="div"/>
    </div>
</div>

<div class="form-group">
    <label for="description" class="col-sm-3 control-label">
        описание
    </label>
    <div class="col-sm-8">
        <sf:textarea
            path="description" rows="4" maxlength="500" class="form-control"
            name="description" id="description" value="${product.description}"/>
    </div>
</div>

<div class="form-group">
    <label for="price" class="col-sm-3 control-label">
        цена, руб.
    </label>
    <div class="col-sm-3 has-feedback">
        <sf:input
            path="price" size="19" maxlength="10" class="form-control"
            name="price" id="price" value="${product.price}"
            data-validate="price" data-description="price" data-describedby="price-desc"/>
        <span id="price" class="glyphicon form-control-feedback"></span>
        <span class="help-block">обязательное поле</span>
        <div id="price-desc"></div>
        <sf:errors path="price" cssClass="alert alert-danger" element="div"/>
    </div>
</div>

<script type="text/javascript">
    //<![CDATA[
    $(document).ready(function() {
        $('#productForm').validate({
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
                name: {
                    pattern: '<div class="alert alert-danger">Специальные символы недопустимы.</div>'
                },
                age: {
                    pattern: '<div class="alert alert-danger">Допустимы только цифры и тире.</div>'
                },
                volume: {
                    pattern: '<div class="alert alert-danger">Допустимы только цифры.</div>'
                },
                alcohol: {
                    pattern: '<div class="alert alert-danger">Допустимы только цифры, точка и запятая.</div>'
                },
                price: {
                    pattern: '<div class="alert alert-danger">Допустимы только цифры.</div>'
                }
            }
        });
        $.validateExtend({
            name: {
                required: true,
                pattern: /^[^#$%^&*()']*$/
            },
            age: {
                required: true,
                pattern: /^[0-9-]*$/
            },
            volume: {
                required: true,
                pattern: /^[0-9]*$/
            },
            alcohol: {
                required: true,
                pattern: /^(\d*[.,])?\d+$/
            },
            price: {
                required: true,
                pattern: /^[0-9]*$/
            }
        });
    });
    //]]>
</script>
