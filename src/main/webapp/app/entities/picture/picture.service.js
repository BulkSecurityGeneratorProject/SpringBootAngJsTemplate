(function() {
    'use strict';
    angular
        .module('springBootAngJsTemplateApp')
        .factory('Picture', Picture)
        .factory('PictureRest', PictureRest);

    Picture.$inject = ['$resource'];
    PictureRest.$inject = ['$resource'];

    function Picture ($resource) {
        var resourceUrl =  'api/pictures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

    function PictureRest ($resource) {
        // var resourceUrl =  'api/pictures/:mode/:resId';
        var resourceUrl =  'api/getCollection/:json';

        return $resource(resourceUrl, {}, {
            'getList': {
                method: 'GET', isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
        });
    }
})();

