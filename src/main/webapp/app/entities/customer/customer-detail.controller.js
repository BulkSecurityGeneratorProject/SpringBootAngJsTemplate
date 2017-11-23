(function() {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .controller('CustomerDetailController', CustomerDetailController);

    CustomerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Customer', 'Restaurant'];

    function CustomerDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Customer, Restaurant) {
        var vm = this;

        vm.customer = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('springBootAngJsTemplateApp:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
