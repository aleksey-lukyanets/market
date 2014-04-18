<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-validate.min.js" type="text/javascript"></script>

<div class="form-group">
    <label for="name" class="col-sm-3 control-label">
        название
    </label>
    <div class="col-sm-6 has-feedback">
        <sf:input
            path="name" size="19" maxlength="20" class="form-control"
            name="name" id="name" value="${region.name}"
            data-validate="name" data-description="name" data-describedby="name-desc"/>
        <span id="name" class="glyphicon form-control-feedback"></span>
        <span class="help-block">обязательное поле</span>
        <div id="name-desc"></div>
        <sf:errors path="name" cssClass="alert alert-danger" element="div"/>
    </div>
</div>

<div class="form-group">
    <label for="subtitle" class="col-sm-3 control-label">
        подзаголовок
    </label>
    <div class="col-sm-6 has-feedback">
        <sf:input
            path="subtitle" size="19" maxlength="25" class="form-control"
            name="subtitle" id="subtitle" value="${region.subtitle}"
            data-validate="subtitle" data-description="subtitle" data-describedby="subtitle-desc"/>
        <span id="subtitle" class="glyphicon form-control-feedback"></span>
        <div id="subtitle-desc"></div>
        <sf:errors path="subtitle" cssClass="alert alert-danger" element="div"/>
    </div>
</div>

<div class="form-group">
    <label for="description" class="col-sm-3 control-label">
        описание
    </label>
    <div class="col-sm-8">
        <sf:textarea
            path="description" rows="10" maxlength="1000" class="form-control"
            name="description" id="description" value="${region.description}"/>
    </div>
</div>

<div class="form-group">
    <label for="color" class="col-sm-3 control-label">
        цветовая&nbsp;схема
    </label>
    <div class="col-sm-6 has-feedback">
        <sf:input
            path="color" size="19" maxlength="10" class="form-control"
            name="color" id="color" value="${region.color}"
            data-validate="color" data-description="color" data-describedby="color-desc"/>
        <span id="color" class="glyphicon form-control-feedback"></span>
        <div id="color-desc"></div>
        <sf:errors path="color" cssClass="alert alert-danger" element="div"/>
    </div>
</div>

<script type="text/javascript">
    //<![CDATA[
    $(document).ready(function() {
        $('#regionForm').validate({
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
                subtitle: {
                    pattern: '<div class="alert alert-danger">Специальные символы недопустимы.</div>'
                },
                color: {
                    pattern: '<div class="alert alert-danger">Допустимы только цифры, латинские буквы и дефис.</div>'
                }
            }
        });
        $.validateExtend({
            name: {
                required: true,
                pattern: /^[^#$%^&*()']*$/
            },
            subtitle: {
                pattern: /^[^#$%^*()']*$/
            },
            color: {
                pattern: /^(a-z|A-Z|0-9-)*[^#$%^&*()']*$/
            }
        });
    });
    //]]>
</script>
