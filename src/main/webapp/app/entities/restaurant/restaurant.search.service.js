(function() {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .factory('RestaurantSearch', RestaurantSearch);

    RestaurantSearch.$inject = ['$resource'];

    function RestaurantSearch($resource) {
        var resourceUrl =  'api/_search/restaurants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
