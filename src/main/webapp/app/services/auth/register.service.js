(function () {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
