(function() {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .controller('RestaurantDetailController', RestaurantDetailController);

    RestaurantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'pics', 'Restaurant', 'Customer', 'Picture'];

    function RestaurantDetailController($scope, $rootScope, $stateParams, previousState, entity, pics, Restaurant, Customer, Picture) {
        var vm = this;

        vm.restaurant = entity;
        vm.previousState = previousState.name;
        vm.pictures = pics;

        var unsubscribe = $rootScope.$on('springBootAngJsTemplateApp:restaurantUpdate', function(event, result) {
            vm.restaurant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
