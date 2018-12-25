var mcApi = mcApi || {};

(function(w, $) {

    mcApi.baseUrl = "";

    /* ------------------------- AUTHORIZATION ------------------------- */
    mcApi.auth = mcApi.auth || {};

    mcApi.auth.tokenCookieName = 'MotorCLIAPI.AuthToken';
    mcApi.auth.tokenHeaderName = 'Authorization';

    mcApi.auth.setToken = function (authToken, expireDate) {
        mcApi.utils.setCookieValue(mcApi.auth.tokenCookieName, authToken, expireDate);
    };

    mcApi.auth.getToken = function () {
        return mcApi.utils.getCookieValue(mcApi.auth.tokenCookieName);
    };

    mcApi.auth.clearToken = function () {
        mcApi.auth.setToken();
    };

    /* ------------------------- UTILS ------------------------- */

    mcApi.utils = mcApi.utils || {};

    /**
     * Sets a cookie value for given key.
     * This is a simple implementation created to be used by ABP.
     * Please use a complete cookie library if you need.
     * @param {string} key
     * @param {string} value
     * @param {Date} expireDate (optional). If not specified the cookie will expire at the end of session.
     */
    mcApi.utils.setCookieValue = function (key, value, expireDate) {
        var cookieValue = encodeURIComponent(key) + '=';

        if (value) {
            cookieValue = cookieValue + encodeURIComponent(value);
        }

        if (expireDate) {
            cookieValue = cookieValue + "; expires=" + expireDate.toUTCString();
        }

        document.cookie = cookieValue;
    };

    /**
     * Gets a cookie with given key.
     * This is a simple implementation created to be used by ABP.
     * Please use a complete cookie library if you need.
     * @param {string} key
     * @returns {string} Cookie value or null
     */
    mcApi.utils.getCookieValue = function (key) {
        var equalities = document.cookie.split('; ');
        for (var i = 0; i < equalities.length; i++) {
            if (!equalities[i]) {
                continue;
            }

            var splitted = equalities[i].split('=');
            if (splitted.length != 2) {
                continue;
            }

            if (decodeURIComponent(splitted[0]) === key) {
                return decodeURIComponent(splitted[1] || '');
            }
        }

        return null;
    };

    /**
     * Deletes cookie for given key.
     * This is a simple implementation created to be used by ABP.
     * Please use a complete cookie library if you need.
     * @param {string} key
     * @param {string} path (optional)
     */
    mcApi.utils.deleteCookie = function (key, path) {
        var cookieValue = encodeURIComponent(key) + '=';

        cookieValue = cookieValue + "; expires=" + (new Date(new Date().getTime() - 86400000)).toUTCString();

        if (path) {
            cookieValue = cookieValue + "; path=" + path;
        }

        document.cookie = cookieValue;
    }

    mcApi.ready = function(cb) {
        if(mcApi.readyTimeOut) {
            clearTimeout(mcApi.readyTimeOut);
            mcApi.readyTimeOut = null;
        }
        mcApi.readyTimeOut = setTimeout(function() {
            mcApi.readyTimeOut = null;
            cb.call(mcApi);
        }, 100);
    };

    mcApi.setBaseUrl = function(url) {
      mcApi.baseUrl = url;
    };

    mcApi.resetAuthStatus = function() {
        if (mcApi.auth.getToken()) {

            $("button.unlocked").removeClass("unlocked").addClass("locked").html("<svg width=\"20\" height=\"20\"><use href=\"#locked\" xlink:href=\"#locked\"><svg viewBox=\"0 0 20 20\" id=\"locked\" width=\"100%\" height=\"100%\">\n" +
                "      <path d=\"M15.8 8H14V5.6C14 2.703 12.665 1 10 1 7.334 1 6 2.703 6 5.6V8H4c-.553 0-1 .646-1 1.199V17c0 .549.428 1.139.951 1.307l1.197.387C5.672 18.861 6.55 19 7.1 19h5.8c.549 0 1.428-.139 1.951-.307l1.196-.387c.524-.167.953-.757.953-1.306V9.199C17 8.646 16.352 8 15.8 8zM12 8H8V5.199C8 3.754 8.797 3 10 3c1.203 0 2 .754 2 2.199V8z\"></path>\n" +
                "    </svg></use></svg>");

        } else {
            $("button.locked").removeClass("locked").addClass("unlocked").html("<svg width=\"20\" height=\"20\"><use href=\"#unlocked\" xlink:href=\"#unlocked\"><svg viewBox=\"0 0 20 20\" id=\"unlocked\" width=\"100%\" height=\"100%\">\n" +
                "      <path d=\"M15.8 8H14V5.6C14 2.703 12.665 1 10 1 7.334 1 6 2.703 6 5.6V6h2v-.801C8 3.754 8.797 3 10 3c1.203 0 2 .754 2 2.199V8H4c-.553 0-1 .646-1 1.199V17c0 .549.428 1.139.951 1.307l1.197.387C5.672 18.861 6.55 19 7.1 19h5.8c.549 0 1.428-.139 1.951-.307l1.196-.387c.524-.167.953-.757.953-1.306V9.199C17 8.646 16.352 8 15.8 8z\"></path>\n" +
                "    </svg></use></svg>");
        }
    };

    mcApi.onComplete = function() {

        var $btn = $("#authorize");

        if (mcApi.auth.getToken()) {
            $btn.html("Logout<i class=\"fa fa-lock\" style=\"margin-left: 20px;\"></i>");
            $btn.addClass("lock");
        } else {
            $btn.html("Authorize<i class=\"fa fa-unlock-alt\" style=\"margin-left: 20px;\"></i>");
            $btn.removeClass("lock");
        }

        setTimeout(function() {
            $(".swagger-ui .wrapper .opblock-tag-section").click(function() {
                setTimeout(function() {
                    mcApi.resetAuthStatus();
                }, 500);
            });
        }, 1000);

        mcApi.resetAuthStatus();
    };

    mcApi.onApiChange = function() {
        // mcApi.ready(function() {
        //
        // });
    };

    mcApi.authorizeBtnClick = function() {
        var $btn = $("#authorize");

        if (mcApi.auth.getToken()) {
            mcApi.swagger.logout();
            $btn.html("Authorize<i class=\"fa fa-unlock-alt\" style=\"margin-left: 20px;\"></i>");
            $btn.removeClass("lock");
            mcApi.resetAuthStatus();
        } else {
            mcApi.swagger.openAuthDialog(function() {
                $btn.html("Logout<i class=\"fa fa-lock\" style=\"margin-left: 20px;\"></i>");
                $btn.addClass("lock");
                mcApi.swagger.closeAuthDialog();
                mcApi.resetAuthStatus();
            });
        }
    };

    w.mcApi = mcApi;
})(window, jQuery);