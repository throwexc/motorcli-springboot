var mcApi = mcApi || {};

(function(w, $) {

    mcApi.swagger = mcApi.swagger || {};

    mcApi.swagger.addAuthToken = function () {
        var authToken = mcApi.auth.getToken();
        if (!authToken) {
            return false;
        }

        var cookieAuth = new SwaggerClient.ApiKeyAuthorization(mcApi.auth.tokenHeaderName, 'Bearer ' + authToken, 'header');
        swaggerUi.api.clientAuthorizations.add('bearerAuth', cookieAuth);
        return true;
    };


    function loginUserInternal(tenantId, callback) {
        var username = document.getElementById('userName').value;
        if (!username) {
            alert('Username is required, please try with a valid value !');
            return false;
        }

        var password = document.getElementById('password').value;
        if (!password) {
            alert('Password is required, please try with a valid value !');
            return false;
        }

        var params = {
            username : username,
            password : password
        };

        var url = mcApi.baseUrl + "/authentication/token";

        $.ajax({
            url: url,
            type: "POST",
            data: JSON.stringify(params),
            dataType:"json",
            contentType:"application/json; charset=UTF-8",
            error: function (request, message, ex) {
                alert('Login failed !');
            },
            success: function (data) {
                var expireDate = new Date( data.claims.exp * 1000);
                mcApi.auth.setToken(data.token, expireDate);
                callback();
            }
        });
    };

    mcApi.swagger.login = function (callback) {
        loginUserInternal(null, callback); // Login for host
    };

    mcApi.swagger.logout = function () {
        mcApi.auth.clearToken();
    };

    mcApi.swagger.closeAuthDialog = function () {
        if (document.getElementById('motorcli-api-auth-dialog')) {
            document.getElementsByClassName("swagger-ui")[1].removeChild(document.getElementById('motorcli-api-auth-dialog'));
        }
    };

    mcApi.swagger.openAuthDialog = function (loginCallback) {
        mcApi.swagger.closeAuthDialog();

        var abpAuthDialog = document.createElement('div');
        abpAuthDialog.className = 'dialog-ux';
        abpAuthDialog.id = 'motorcli-api-auth-dialog';

        document.getElementsByClassName("swagger-ui")[1].appendChild(abpAuthDialog);

        // -- backdrop-ux
        var backdropUx = document.createElement('div');
        backdropUx.className = 'backdrop-ux';
        abpAuthDialog.appendChild(backdropUx);

        // -- modal-ux
        var modalUx = document.createElement('div');
        modalUx.className = 'modal-ux';
        abpAuthDialog.appendChild(modalUx);

        // -- -- modal-dialog-ux
        var modalDialogUx = document.createElement('div');
        modalDialogUx.className = 'modal-dialog-ux';
        modalUx.appendChild(modalDialogUx);

        // -- -- -- modal-ux-inner
        var modalUxInner = document.createElement('div');
        modalUxInner.className = 'modal-ux-inner';
        modalDialogUx.appendChild(modalUxInner);

        // -- -- -- -- modal-ux-header
        var modalUxHeader = document.createElement('div');
        modalUxHeader.className = 'modal-ux-header';
        modalUxInner.appendChild(modalUxHeader);

        var modalHeader = document.createElement('h3');
        modalHeader.innerText = 'Authorize';
        modalUxHeader.appendChild(modalHeader);

        // -- -- -- -- modal-ux-content
        var modalUxContent = document.createElement('div');
        modalUxContent.className = 'modal-ux-content';
        modalUxInner.appendChild(modalUxContent);

        modalUxContent.onkeydown = function (e) {
            if (e.keyCode === 13) {
                //try to login when user presses enter on authorize modal
                mcApi.swagger.login(loginCallback);
            }
        };

        createInput(modalUxContent, 'userName', 'Username');
        createInput(modalUxContent, 'password', 'Password', 'password');

        //Buttons
        var authBtnWrapper = document.createElement('div');
        authBtnWrapper.className = 'auth-btn-wrapper';
        modalUxContent.appendChild(authBtnWrapper);

        //Close button
        var closeButton = document.createElement('button');
        closeButton.className = 'btn modal-btn auth btn-done button';
        closeButton.innerText = 'Close';
        closeButton.style.marginRight = '5px';
        closeButton.onclick = mcApi.swagger.closeAuthDialog;
        authBtnWrapper.appendChild(closeButton);

        //Authorize button
        var authorizeButton = document.createElement('button');
        authorizeButton.className = 'btn modal-btn auth authorize button';
        authorizeButton.innerText = 'Login';
        authorizeButton.onclick = function() {
            mcApi.swagger.login(loginCallback);
        };
        authBtnWrapper.appendChild(authorizeButton);
    };

    function createInput(container, id, title, type) {
        var wrapper = document.createElement('div');
        wrapper.className = 'wrapper';
        container.appendChild(wrapper);

        var label = document.createElement('label');
        label.innerText = title;
        wrapper.appendChild(label);

        var section = document.createElement('section');
        section.className = 'block-tablet col-10-tablet block-desktop col-10-desktop';
        wrapper.appendChild(section);

        var input = document.createElement('input');
        input.id = id;
        input.type = type ? type : 'text';
        input.style.width = '100%';

        section.appendChild(input);
    };

    // UI 替换部分
    mcApi.uiReady = function() {
        setTimeout(function() {
            $(".swagger-ui .topbar .wrapper .topbar-wrapper .link img").remove();
            $(".swagger-ui .topbar .wrapper .topbar-wrapper .link span")
                .html("Motor CLI API Docs");
            $(".swagger-ui .topbar .wrapper .topbar-wrapper .download-url-wrapper .select-label span")
                .html("选择文档");
        }, 500);
    };

    w.mcApi = mcApi;
})(window, jQuery);